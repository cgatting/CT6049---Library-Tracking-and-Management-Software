/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.Hashtable;
import oracle.jdbc.driver.ResultSetCache;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
final class ResultSetCacheManager {
    private static final Hashtable<String, ResultSetCache> cacheTable = new Hashtable(10);
    private static final Monitor CACHE_TABLE_MONITOR = Monitor.newInstance();

    ResultSetCacheManager() {
    }

    static ResultSetCache getResultSetCache(String string, long l2, int n2) {
        try (Monitor.CloseableLock closeableLock = CACHE_TABLE_MONITOR.acquireCloseableLock();){
            String string2 = string;
            ResultSetCache resultSetCache = cacheTable.get(string2);
            if (resultSetCache == null) {
                resultSetCache = new ResultSetCache(l2, n2);
                cacheTable.put(string2, resultSetCache);
            }
            ResultSetCache resultSetCache2 = resultSetCache;
            return resultSetCache2;
        }
    }
}

