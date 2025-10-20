/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.regex.Pattern;
import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.RESULT_FETCH, Feature.PARAMETER_SET})
abstract class BlockSource {
    private static final int BLOCK_SIZE = 32768;

    BlockSource() {
    }

    static final BlockSource createBlockSource(boolean bl, Implementation implementation) {
        if (bl) {
            return ThreadLocalBlockSource.createBlockSource(implementation);
        }
        return BlockSource.createBlockSource(implementation);
    }

    static BlockSource createBlockSource(Implementation implementation) {
        switch (implementation) {
            case DUMB: {
                return DumbBlockSource.createBlockSource();
            }
            case SIMPLE: {
                return SimpleCachingBlockSource.createBlockSource();
            }
            case SOFT: {
                return SoftCachingBlockSource.createBlockSource();
            }
            case THREADED: {
                return ThreadedCachingBlockSource.createBlockSource();
            }
        }
        return null;
    }

    abstract int getBlockSize();

    abstract byte[] get();

    abstract void put(byte[] var1);

    @DefaultLogger(value="oracle.jdbc")
    @Supports(value={Feature.RESULT_FETCH, Feature.PARAMETER_SET})
    private static final class ThreadLocalBlockSource
    extends BlockSource {
        private static Implementation IMPL = null;
        private static final ThreadLocal<BlockSource> REF = new ThreadLocal<BlockSource>(){

            @Override
            protected BlockSource initialValue() {
                return BlockSource.createBlockSource(IMPL);
            }
        };

        static BlockSource createBlockSource(Implementation implementation) {
            assert (IMPL == null || IMPL == implementation) : "IMPL: " + (Object)((Object)IMPL) + " impl: " + (Object)((Object)implementation);
            IMPL = implementation;
            return new ThreadLocalBlockSource();
        }

        private ThreadLocalBlockSource() {
        }

        @Override
        int getBlockSize() {
            return REF.get().getBlockSize();
        }

        @Override
        byte[] get() {
            BlockSource blockSource = REF.get();
            return blockSource.get();
        }

        @Override
        void put(byte[] byArray) {
            BlockSource blockSource = REF.get();
            blockSource.put(byArray);
        }
    }

    @DefaultLogger(value="oracle.jdbc")
    @Supports(value={Feature.RESULT_FETCH, Feature.PARAMETER_SET})
    private static final class SoftCachingBlockSource
    extends BlockSource {
        private static final int CACHE_SIZE = 1024;
        private final SoftReference<byte[]>[] blocks = new SoftReference[1024];
        private int top = 0;

        private static BlockSource createBlockSource() {
            return new SoftCachingBlockSource();
        }

        private SoftCachingBlockSource() {
        }

        @Override
        int getBlockSize() {
            return 32768;
        }

        @Override
        byte[] get() {
            while (this.top > 0) {
                SoftReference<byte[]> softReference = this.blocks[--this.top];
                this.blocks[this.top] = null;
                byte[] byArray = softReference.get();
                if (byArray == null) continue;
                return byArray;
            }
            return new byte[32768];
        }

        @Override
        void put(byte[] byArray) {
            assert (byArray != null) : "block is null";
            assert (byArray.length == 32768) : "block.length: " + byArray.length;
            if (this.top < this.blocks.length) {
                this.blocks[this.top++] = new SoftReference<byte[]>(byArray);
            } else {
                int n2 = this.top;
                while (n2 > 0) {
                    if (this.blocks[--n2].get() != null) continue;
                    this.blocks[n2] = new SoftReference<byte[]>(byArray);
                    return;
                }
            }
        }
    }

    @DefaultLogger(value="oracle.jdbc")
    @Supports(value={Feature.RESULT_FETCH, Feature.PARAMETER_SET})
    static final class ThreadedCachingBlockSource
    extends BlockSource
    implements Monitor {
        private static final BlockReleaser RELEASER = BlockReleaser.access$100();
        private static final BlockReleaserListener LISTENER = BlockReleaserListener.access$300();
        private static final Collection<WeakReference<ThreadedCachingBlockSource>> ALL_INSTANCES = new LinkedList<WeakReference<ThreadedCachingBlockSource>>();
        private static final Monitor ALL_INSTANCES_MONITOR = Monitor.newInstance();
        private static volatile long LAST_MEMORY_EVENT_MILLIS = 0L;
        private static final int INITIAL_CACHE_SIZE = 32;
        private int top = 0;
        private byte[][] stack = new byte[32][];
        private int lowWaterMark = 0;
        private int recentLowWaterMark = 0;
        private final Monitor.CloseableLock monitorLock = this.newDefaultLock();

        static void stopBlockReleaserThread() {
            BlockReleaser.SOLE_INSTANCE.interrupt();
        }

        private static void releaseFromAllSources() {
            try (Monitor.CloseableLock closeableLock = ALL_INSTANCES_MONITOR.acquireCloseableLock();){
                Iterator<WeakReference<ThreadedCachingBlockSource>> iterator = ALL_INSTANCES.iterator();
                while (iterator.hasNext()) {
                    ThreadedCachingBlockSource threadedCachingBlockSource = (ThreadedCachingBlockSource)iterator.next().get();
                    if (threadedCachingBlockSource == null) {
                        iterator.remove();
                        continue;
                    }
                    threadedCachingBlockSource.releaseUnusedBlocks();
                }
            }
        }

        static BlockSource createBlockSource() {
            try {
                ThreadedCachingBlockSource threadedCachingBlockSource = new ThreadedCachingBlockSource();
                WeakReference<ThreadedCachingBlockSource> weakReference = new WeakReference<ThreadedCachingBlockSource>(threadedCachingBlockSource);
                try (Monitor.CloseableLock closeableLock = ALL_INSTANCES_MONITOR.acquireCloseableLock();){
                    ALL_INSTANCES.add(weakReference);
                }
                return threadedCachingBlockSource;
            }
            catch (OutOfMemoryError outOfMemoryError) {
                LAST_MEMORY_EVENT_MILLIS = System.currentTimeMillis();
                throw outOfMemoryError;
            }
        }

        private ThreadedCachingBlockSource() {
        }

        final void releaseUnusedBlocks() {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                assert (this.top >= this.recentLowWaterMark);
                this.lowWaterMark = System.currentTimeMillis() - LAST_MEMORY_EVENT_MILLIS < 300000L ? this.recentLowWaterMark : Math.min((this.lowWaterMark + this.recentLowWaterMark) / 2, this.recentLowWaterMark);
                int n2 = this.top - this.lowWaterMark;
                while (this.top > n2) {
                    this.stack[--this.top] = null;
                }
                this.recentLowWaterMark = this.top;
            }
        }

        private final void checkLowWater() {
            this.recentLowWaterMark = Math.min(this.recentLowWaterMark, this.top);
        }

        @Override
        final int getBlockSize() {
            return 32768;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        final byte[] get() {
            Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
            Throwable throwable = null;
            try {
                if (this.top == 0) {
                    try {
                        byte[] byArray = new byte[32768];
                        return byArray;
                    }
                    catch (OutOfMemoryError outOfMemoryError) {
                        LAST_MEMORY_EVENT_MILLIS = System.currentTimeMillis();
                        throw outOfMemoryError;
                    }
                }
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
            byte[] byArray = this.stack[--this.top];
            this.checkLowWater();
            return byArray;
            finally {
                if (closeableLock != null) {
                    if (throwable != null) {
                        try {
                            closeableLock.close();
                        }
                        catch (Throwable throwable3) {
                            throwable.addSuppressed(throwable3);
                        }
                    } else {
                        closeableLock.close();
                    }
                }
            }
        }

        /*
         * Unable to fully structure code
         */
        @Override
        final void put(byte[] var1_1) {
            block18: {
                block19: {
                    block20: {
                        var2_2 = this.acquireCloseableLock();
                        var3_3 = null;
                        if (!ThreadedCachingBlockSource.$assertionsDisabled && var1_1.length != 32768) {
                            throw new AssertionError((Object)("block.length: " + var1_1.length));
                        }
                        if (this.top != this.stack.length) ** GOTO lbl28
                        if (System.currentTimeMillis() - ThreadedCachingBlockSource.LAST_MEMORY_EVENT_MILLIS >= 300000L) break block18;
                        if (var2_2 == null) break block19;
                        if (var3_3 == null) break block20;
                        try {
                            var2_2.close();
                        }
                        catch (Throwable var4_4) {
                            var3_3.addSuppressed(var4_4);
                        }
                        break block19;
                    }
                    var2_2.close();
                }
                return;
            }
            try {
                try {
                    var4_5 = new byte[this.stack.length * 4][];
                    System.arraycopy(this.stack, 0, var4_5, 0, this.stack.length);
                    this.stack = var4_5;
lbl28:
                    // 2 sources

                    this.stack[this.top++] = var1_1;
                }
                catch (OutOfMemoryError var4_6) {
                    ThreadedCachingBlockSource.LAST_MEMORY_EVENT_MILLIS = System.currentTimeMillis();
                }
            }
            catch (Throwable var4_8) {
                var3_3 = var4_8;
                throw var4_8;
            }
            catch (Throwable var5_9) {
                throw var5_9;
            }
            finally {
                if (var2_2 != null) {
                    if (var3_3 != null) {
                        try {
                            var2_2.close();
                        }
                        catch (Throwable var4_7) {
                            var3_3.addSuppressed(var4_7);
                        }
                    } else {
                        var2_2.close();
                    }
                }
            }
        }

        @Override
        public final Monitor.CloseableLock getMonitorLock() {
            return this.monitorLock;
        }

        @DefaultLogger(value="oracle.jdbc")
        @Supports(value={Feature.RESULT_FETCH, Feature.PARAMETER_SET})
        private static final class BlockReleaserListener
        implements NotificationListener {
            private static final BlockReleaserListener SOLE_INSTANCE = new BlockReleaserListener();

            private BlockReleaserListener() {
                MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
                NotificationEmitter notificationEmitter = (NotificationEmitter)((Object)memoryMXBean);
                notificationEmitter.addNotificationListener(this, null, null);
                Pattern pattern = Pattern.compile(".*Old.*");
                for (MemoryPoolMXBean memoryPoolMXBean : ManagementFactory.getMemoryPoolMXBeans()) {
                    if (memoryPoolMXBean.getType() != MemoryType.HEAP || !memoryPoolMXBean.isCollectionUsageThresholdSupported() || !pattern.matcher(memoryPoolMXBean.getName()).matches() || memoryPoolMXBean.getCollectionUsageThreshold() != 0L) continue;
                    MemoryUsage memoryUsage = memoryPoolMXBean.getUsage();
                    final long l2 = (long)(memoryUsage.getMax() == -1L ? (double)Runtime.getRuntime().maxMemory() * 0.9 : (double)memoryUsage.getMax() * 0.9);
                    final MemoryPoolMXBean memoryPoolMXBean2 = memoryPoolMXBean;
                    AccessController.doPrivileged(new PrivilegedAction<Object>(){

                        @Override
                        public Object run() {
                            memoryPoolMXBean2.setCollectionUsageThreshold(l2);
                            return null;
                        }
                    });
                }
            }

            @Override
            public void handleNotification(Notification notification, Object object) {
                String string = notification.getType();
                if (string.equals("java.management.memory.collection.threshold.exceeded")) {
                    LAST_MEMORY_EVENT_MILLIS = System.currentTimeMillis();
                    BlockReleaser.releaseAllUnusedBlocks();
                }
            }

            public void unregister() {
                MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
                NotificationEmitter notificationEmitter = (NotificationEmitter)((Object)memoryMXBean);
                try {
                    notificationEmitter.removeNotificationListener(this, null, null);
                }
                catch (ListenerNotFoundException listenerNotFoundException) {
                }
            }
        }

        @DefaultLogger(value="oracle.jdbc")
        @Supports(value={Feature.RESULT_FETCH, Feature.PARAMETER_SET})
        private static final class BlockReleaser
        extends Thread
        implements Monitor.WaitableMonitor {
            private static final String BLOCK_RELEASER_THREAD_NAME = "oracle.jdbc.driver.BlockSource.ThreadedCachingBlockSource.BlockReleaser";
            private static final int DELAY_MILLIS = 300000;
            private static final BlockReleaser SOLE_INSTANCE = new BlockReleaser();
            private final Monitor.CloseableLock monitorLock = this.newDefaultLock();
            private final Condition monitorCondition = this.newMonitorCondition();

            static void releaseAllUnusedBlocks() {
                try (Monitor.CloseableLock closeableLock = SOLE_INSTANCE.acquireCloseableLock();){
                    SOLE_INSTANCE.monitorNotifyAll();
                }
            }

            private BlockReleaser() {
                super(BLOCK_RELEASER_THREAD_NAME);
                this.setDaemon(true);
                this.setPriority(4);
                this.start();
            }

            @Override
            public void run() {
                while (true) {
                    try {
                        while (true) {
                            try (Monitor.CloseableLock closeableLock = SOLE_INSTANCE.acquireCloseableLock();){
                                SOLE_INSTANCE.monitorWait(300000L);
                            }
                            ThreadedCachingBlockSource.releaseFromAllSources();
                        }
                    }
                    catch (InterruptedException interruptedException) {
                        BlockReleaserListener.SOLE_INSTANCE.unregister();
                        return;
                    }
                    catch (ThreadDeath threadDeath) {
                        BlockReleaserListener.SOLE_INSTANCE.unregister();
                        throw threadDeath;
                    }
                    catch (Throwable throwable) {
                        continue;
                    }
                    break;
                }
            }

            @Override
            public final Monitor.CloseableLock getMonitorLock() {
                return this.monitorLock;
            }

            @Override
            public Condition getMonitorCondition() {
                return this.monitorCondition;
            }
        }
    }

    @DefaultLogger(value="oracle.jdbc")
    @Supports(value={Feature.RESULT_FETCH, Feature.PARAMETER_SET})
    static final class SimpleCachingBlockSource
    extends BlockSource {
        private static final int INITIAL_CACHE_SIZE = 32;
        private static final long RELEASE_NANOS = 300000000L;
        private int top = 0;
        private byte[][] stack = new byte[32][];
        private int lowWaterMark = 0;
        private int recentLowWaterMark = 0;
        private long nextReleaseNanos = System.nanoTime() + 300000000L;

        static BlockSource createBlockSource() {
            return new SimpleCachingBlockSource();
        }

        private SimpleCachingBlockSource() {
        }

        final void releaseUnusedBlocks() {
            long l2 = System.nanoTime();
            if (l2 < this.nextReleaseNanos) {
                return;
            }
            this.nextReleaseNanos = l2 + 300000000L;
            assert (this.top >= this.recentLowWaterMark);
            this.lowWaterMark = Math.min((this.lowWaterMark + this.recentLowWaterMark) / 2, this.recentLowWaterMark);
            int n2 = this.top - this.lowWaterMark;
            while (this.top > n2) {
                this.stack[--this.top] = null;
            }
            this.recentLowWaterMark = this.top;
        }

        private final void checkLowWater() {
            this.recentLowWaterMark = Math.min(this.recentLowWaterMark, this.top);
        }

        @Override
        final int getBlockSize() {
            return 32768;
        }

        @Override
        final byte[] get() {
            if (this.top == 0) {
                return new byte[32768];
            }
            byte[] byArray = this.stack[--this.top];
            this.checkLowWater();
            return byArray;
        }

        @Override
        final void put(byte[] byArray) {
            assert (byArray.length == 32768) : "block.length: " + byArray.length;
            if (this.top == this.stack.length) {
                byte[][] byArrayArray = new byte[this.stack.length * 4][];
                System.arraycopy(this.stack, 0, byArrayArray, 0, this.stack.length);
                this.stack = byArrayArray;
            }
            this.stack[this.top++] = byArray;
            this.releaseUnusedBlocks();
        }
    }

    @DefaultLogger(value="oracle.jdbc")
    @Supports(value={Feature.RESULT_FETCH, Feature.PARAMETER_SET})
    static class DumbBlockSource
    extends BlockSource {
        static final BlockSource createBlockSource() {
            return new DumbBlockSource();
        }

        DumbBlockSource() {
        }

        @Override
        final int getBlockSize() {
            return 32768;
        }

        @Override
        final byte[] get() {
            return new byte[32768];
        }

        @Override
        final void put(byte[] byArray) {
        }
    }

    static enum Implementation {
        DUMB,
        SIMPLE,
        SOFT,
        THREADED;

    }
}

