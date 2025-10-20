/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.LongSummaryStatistics;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.driver.OpaqueAccessToken;
import oracle.jdbc.logging.annotations.Blind;
import oracle.net.nt.Clock;
import oracle.net.nt.TimeoutInterruptHandler;

final class AccessTokenCache<T extends OpaqueAccessToken>
implements Supplier<T> {
    private static final long EXPIRATION_THRESHOLD = 30000L;
    private static final long UPDATE_THRESHOLD = 60000L;
    private final Supplier<T> tokenSupplier;
    private final Executor executor;
    private final LongSummaryStatistics latency = new LongSummaryStatistics();
    private final AtomicInteger getCount = new AtomicInteger(0);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition updateCondition = this.lock.newCondition();
    private boolean isUpdating = false;
    private T token = null;
    private RuntimeException failure;

    public static <T extends OpaqueAccessToken> AccessTokenCache<T> create(Supplier<T> supplier) {
        try {
            return new AccessTokenCache<T>(OracleDriver.getExecutorService(), supplier);
        }
        catch (SQLException sQLException) {
            throw new IllegalStateException(sQLException);
        }
    }

    private AccessTokenCache(Executor executor, @Blind Supplier<T> supplier) {
        this.executor = executor;
        this.tokenSupplier = supplier;
    }

    @Override
    @Blind
    public T get() {
        this.getCount.incrementAndGet();
        T t2 = this.token;
        if (t2 != null && !AccessTokenCache.isExpiring(t2)) {
            return t2;
        }
        this.lock.lock();
        try {
            while (t2 == this.token && this.failure == null) {
                if (!this.isUpdating) {
                    this.isUpdating = true;
                    this.requestUpdate();
                }
                this.updateCondition.await();
            }
            if (this.failure != null) {
                RuntimeException runtimeException = this.failure;
                this.failure = null;
                throw runtimeException;
            }
            T t3 = this.token;
            return t3;
        }
        catch (InterruptedException interruptedException) {
            throw new RuntimeException(interruptedException);
        }
        finally {
            this.lock.unlock();
        }
    }

    private void requestUpdate() {
        this.executor.execute(() -> {
            try {
                long l2 = System.nanoTime();
                OpaqueAccessToken opaqueAccessToken = (OpaqueAccessToken)this.tokenSupplier.get();
                this.latency.accept(System.nanoTime() - l2);
                this.update(Objects.requireNonNull(opaqueAccessToken, "token supplier has output a null value"), null);
            }
            catch (RuntimeException runtimeException) {
                this.update(null, runtimeException);
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void update(@Blind T t2, RuntimeException runtimeException) {
        boolean bl = this.getCount.getAndSet(0) != 0 && runtimeException == null;
        this.lock.lock();
        try {
            this.token = t2;
            this.failure = runtimeException;
            this.isUpdating = bl;
            this.updateCondition.signalAll();
        }
        finally {
            this.lock.unlock();
        }
        if (bl) {
            this.scheduleUpdate(TimeUnit.SECONDS.toMillis(((OpaqueAccessToken)t2).expiration().toEpochSecond()));
        }
    }

    private void scheduleUpdate(long l2) {
        long l3 = TimeUnit.NANOSECONDS.toMillis(Math.round(this.latency.getAverage() * 1.2));
        long l4 = l2 - 60000L - System.currentTimeMillis();
        TimeoutInterruptHandler.scheduleTask(this::requestUpdate, Math.max(0L, l4 - l3));
    }

    private static boolean isExpiring(@Blind OpaqueAccessToken opaqueAccessToken) {
        return Clock.currentTimeMillis() + 30000L >= TimeUnit.SECONDS.toMillis(opaqueAccessToken.expiration().toEpochSecond());
    }
}

