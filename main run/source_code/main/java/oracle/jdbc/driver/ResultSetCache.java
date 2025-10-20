/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.ResultSetCacheEntry;
import oracle.jdbc.driver.ResultSetCacheEntryKey;
import oracle.jdbc.driver.T4CTTIOqcsta;
import oracle.jdbc.driver.T4CTTIkscn;
import oracle.jdbc.driver.T4CTTIqcinv;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.util.RepConversion;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
final class ResultSetCache
implements oracle.jdbc.internal.ResultSetCache,
Monitor.WaitableMonitor {
    static final int COMPILE_KEY_SIZE = 16;
    static final int RUNTIME_KEY_SIZE = 16;
    private static final int CACHE_ID_SIZE = 16;
    private static final long STAT_SEND_INTERVAL = 30000L;
    private final byte[] cacheId = new byte[16];
    private final byte[] cacheIdAsNibbles = new byte[32];
    private boolean isCacheIdAsNibblesReady = false;
    private final long cacheLagInMillis;
    private long nextPingTime = 0L;
    private AtomicLong invalidationCount = new AtomicLong(0L);
    private AtomicLong invalidatedQueryCount = new AtomicLong(0L);
    private AtomicLong validQueriesPurged = new AtomicLong(0L);
    private AtomicLong invalidatedBeforeCompletion = new AtomicLong(0L);
    private AtomicInteger cacheHits = new AtomicInteger(0);
    private long lastStatSentAt;
    private T4CTTIOqcsta oqcsta;
    private AtomicBoolean needToSendStats = new AtomicBoolean(false);
    private long registrationId = -1L;
    private ResultSetCacheState state = ResultSetCacheState.INIT;
    private long visibleSCN;
    private final CacheStorage cacheStorage;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();
    private final Condition monitorCondition = this.newMonitorCondition();

    ResultSetCache(long l2, int n2) {
        assert (l2 > 0L);
        assert (n2 >= 0);
        new Random().nextBytes(this.cacheId);
        this.cacheStorage = new CacheStorage(l2);
        this.cacheLagInMillis = n2;
    }

    void setState(ResultSetCacheState resultSetCacheState) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (resultSetCacheState == this.state) {
                return;
            }
            this.state = resultSetCacheState;
            if (resultSetCacheState == ResultSetCacheState.STARTED) {
                this.monitorNotifyAll();
            }
        }
    }

    ResultSetCacheState getState() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            ResultSetCacheState resultSetCacheState = this.state;
            return resultSetCacheState;
        }
    }

    byte[] getCacheId() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = this.cacheId;
            return byArray;
        }
    }

    byte[] getCacheIdAsNibbles() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (!this.isCacheIdAsNibblesReady) {
                RepConversion.bArray2Nibbles(this.cacheId, this.cacheIdAsNibbles);
                this.isCacheIdAsNibblesReady = true;
            }
            byte[] byArray = this.cacheIdAsNibbles;
            return byArray;
        }
    }

    long getRegistrationId() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            long l2 = this.registrationId;
            return l2;
        }
    }

    void setRegistrationId(long l2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.registrationId = l2;
        }
    }

    void setOQCSTA(T4CTTIOqcsta t4CTTIOqcsta) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.oqcsta = t4CTTIOqcsta;
        }
    }

    T4CTTIOqcsta getOQCSTA() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            T4CTTIOqcsta t4CTTIOqcsta = this.oqcsta;
            return t4CTTIOqcsta;
        }
    }

    void setVisibleSCN(long l2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.visibleSCN = l2;
            this.nextPingTime = System.currentTimeMillis() + this.cacheLagInMillis;
        }
    }

    long getVisibleSCN() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            long l2 = this.visibleSCN;
            return l2;
        }
    }

    void processCommittedInvalidation(T4CTTIqcinv t4CTTIqcinv) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            long l2 = t4CTTIqcinv.kpdqcqid;
            if (l2 == 0L) {
                return;
            }
            this.invalidationCount.incrementAndGet();
            long l3 = t4CTTIqcinv.kpdqcscn.getSCN();
            if (!T4CTTIkscn.isLessThanUnsigned(l3, this.visibleSCN)) {
                int n2 = this.cacheStorage.removeResultsetCacheEntries(l2);
                this.invalidatedQueryCount.addAndGet(n2);
            }
            this.needToSendStats.set(true);
        }
    }

    ResultSetCacheEntry getResultSetCacheEntry(OracleStatement oracleStatement) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            ResultSetCacheEntry resultSetCacheEntry;
            byte[] byArray = oracleStatement.getCompileKey();
            if (byArray == null || byArray.length == 0) {
                ResultSetCacheEntry resultSetCacheEntry2 = null;
                return resultSetCacheEntry2;
            }
            byte[] byArray2 = oracleStatement.getRuntimeKey();
            if (byArray2 == null) {
                ResultSetCacheEntry resultSetCacheEntry3 = null;
                return resultSetCacheEntry3;
            }
            ResultSetCacheEntryKey resultSetCacheEntryKey = new ResultSetCacheEntryKey(byArray, byArray2);
            long l2 = oracleStatement.getQueryId();
            ArrayList<Long> arrayList = oracleStatement.connection.getResultSetCacheLocalInvalidations();
            if (l2 == 0L || arrayList.contains(l2)) {
                ResultSetCacheEntry resultSetCacheEntry4 = null;
                return resultSetCacheEntry4;
            }
            if (System.currentTimeMillis() > this.nextPingTime) {
                oracleStatement.connection.pingDatabase();
            }
            if ((resultSetCacheEntry = this.cacheStorage.getResultsetCacheEntry(resultSetCacheEntryKey, l2, oracleStatement.connection.userName)) != null && resultSetCacheEntry.userName.equals(oracleStatement.connection.userName)) {
                if (resultSetCacheEntry.isValid()) {
                    this.cacheHits.incrementAndGet();
                    this.needToSendStats.set(true);
                    if (this.lastStatSentAt == 0L) {
                        this.lastStatSentAt = System.currentTimeMillis();
                    }
                }
                ResultSetCacheEntry resultSetCacheEntry5 = resultSetCacheEntry;
                return resultSetCacheEntry5;
            }
            ResultSetCacheEntry resultSetCacheEntry6 = null;
            return resultSetCacheEntry6;
        }
    }

    void registerConnection(OracleConnection oracleConnection) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.state == ResultSetCacheState.INIT) {
                this.setState(ResultSetCacheState.STARTING);
            } else if (this.state == ResultSetCacheState.STARTING) {
                try {
                    this.monitorWait();
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    boolean needToSendStatsResetIfTrue() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (System.currentTimeMillis() - this.lastStatSentAt > 30000L && this.needToSendStats.weakCompareAndSet(true, false)) {
                this.lastStatSentAt = System.currentTimeMillis();
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    @Override
    public int getCacheLag() {
        return (int)this.cacheLagInMillis;
    }

    @Override
    public long getInvalidationCount() {
        return this.invalidationCount.get();
    }

    @Override
    public long getInvalidatedQueryCount() {
        return this.invalidatedQueryCount.get();
    }

    @Override
    public long getInvalidatedBeforeCompletion() {
        return this.invalidatedBeforeCompletion.get();
    }

    @Override
    public long getValidQueriesPurged() {
        return this.validQueriesPurged.get();
    }

    @Override
    public int getCacheHits() {
        return this.cacheHits.get();
    }

    void updateCurrentCacheSize(long l2) {
        this.cacheStorage.incrementCacheSize(l2);
    }

    @Override
    public long getCurrentCacheSize() {
        return this.cacheStorage.getCacheSize();
    }

    @Override
    public long getMaxCacheSize() {
        return this.cacheStorage.maxSize();
    }

    @Override
    public long getNumberOfCacheEntries() {
        return this.cacheStorage.size();
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }

    @Override
    public final Condition getMonitorCondition() {
        return this.monitorCondition;
    }

    private static class CacheStorage
    extends LinkedHashMap<ResultSetCacheEntryKey, ResultSetCacheEntry>
    implements Monitor {
        private static final int INITIAL_SIZE = 10;
        private static final long serialVersionUID = 1L;
        private long currentCacheSize;
        private final long maxCacheSize;
        private final Map<Long, LinkedList<ResultSetCacheEntry>> queryIdIndex;
        private final Monitor.CloseableLock monitorLock = this.newDefaultLock();

        CacheStorage(long l2) {
            super(10);
            this.queryIdIndex = new HashMap<Long, LinkedList<ResultSetCacheEntry>>(10);
            this.maxCacheSize = l2;
        }

        ResultSetCacheEntry getResultsetCacheEntry(ResultSetCacheEntryKey resultSetCacheEntryKey, long l3, String string) {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                ResultSetCacheEntry resultSetCacheEntry = (ResultSetCacheEntry)super.get(resultSetCacheEntryKey);
                if (resultSetCacheEntry == null) {
                    resultSetCacheEntry = new ResultSetCacheEntry(resultSetCacheEntryKey, l3);
                    resultSetCacheEntry.userName = string;
                    super.put(resultSetCacheEntryKey, resultSetCacheEntry);
                    this.currentCacheSize += resultSetCacheEntry.getSizeInMemory();
                    this.queryIdIndex.computeIfAbsent(l3, l2 -> new LinkedList()).add(resultSetCacheEntry);
                }
                ResultSetCacheEntry resultSetCacheEntry2 = resultSetCacheEntry;
                return resultSetCacheEntry2;
            }
        }

        int removeResultsetCacheEntries(Long l2) {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                int n2 = 0;
                LinkedList<ResultSetCacheEntry> linkedList = this.queryIdIndex.remove(l2);
                if (linkedList != null) {
                    for (ResultSetCacheEntry resultSetCacheEntry : linkedList) {
                        if (this.remove(resultSetCacheEntry.getResultSetCacheEntryKey()) != null) {
                            this.currentCacheSize -= resultSetCacheEntry.getSizeInMemory();
                        }
                        resultSetCacheEntry.invalidate();
                        ++n2;
                    }
                }
                int n3 = n2;
                return n3;
            }
        }

        void incrementCacheSize(long l2) {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                this.currentCacheSize += l2;
            }
        }

        long getCacheSize() {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                long l2 = this.currentCacheSize;
                return l2;
            }
        }

        long maxSize() {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                long l2 = this.maxCacheSize;
                return l2;
            }
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<ResultSetCacheEntryKey, ResultSetCacheEntry> entry) {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                boolean bl;
                boolean bl2 = bl = this.currentCacheSize >= this.maxCacheSize;
                if (bl) {
                    ResultSetCacheEntry resultSetCacheEntry = entry.getValue();
                    this.currentCacheSize = -1L * resultSetCacheEntry.getSizeInMemory();
                    LinkedList<ResultSetCacheEntry> linkedList = this.queryIdIndex.get(resultSetCacheEntry.getQueryId());
                    linkedList.remove(resultSetCacheEntry);
                }
                boolean bl3 = bl;
                return bl3;
            }
        }

        @Override
        public final Monitor.CloseableLock getMonitorLock() {
            return this.monitorLock;
        }
    }

    static enum ResultSetCacheState {
        INIT,
        STARTING,
        STARTED,
        STARTUP_FAILED;

    }
}

