/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.DMSFactory;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.LRUCache;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.STATEMENT_CACHE})
final class LRUStatementCache {
    private int cacheSize;
    private final LRUCache<OraclePreparedStatement> implicitCache = new LRUCache(true);
    private final LRUCache<OraclePreparedStatement> explicitCache = new LRUCache(false);
    private boolean implicitCacheEnabled = false;
    private boolean explicitCacheEnabled = false;
    static final String DMS_HIT_COUNT_NAME = "StatementCacheHit";
    static final String DMS_HIT_COUNT_DESCRIPTION = "Statement found in cache";
    static final String DMS_MISS_COUNT_NAME = "StatementCacheMiss";
    static final String DMS_MISS_COUNT_DESCRIPTION = "Statement not found in cache";
    DMSFactory.DMSEvent dmsStatementCacheHitCount = null;
    DMSFactory.DMSEvent dmsStatementCacheMissCount = null;

    LRUStatementCache(int n2) throws SQLException {
        if (n2 < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 123).fillInStackTrace();
        }
        this.cacheSize = n2;
        this.implicitCache.vacancy(n2);
        this.explicitCache.vacancy(n2);
    }

    protected void createDMSSensors(DMSFactory.DMSNoun dMSNoun) {
        this.dmsStatementCacheHitCount = DMSFactory.getInstance().createEvent(dMSNoun, DMS_HIT_COUNT_NAME, DMS_HIT_COUNT_DESCRIPTION);
        this.dmsStatementCacheMissCount = DMSFactory.getInstance().createEvent(dMSNoun, DMS_MISS_COUNT_NAME, DMS_MISS_COUNT_DESCRIPTION);
    }

    public void resize(int n2) throws SQLException {
        if (n2 < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 123).fillInStackTrace();
        }
        while (this.implicitCache.size() > n2) {
            this.implicitCache.removeLeastRecent().exitImplicitCacheToClose();
        }
        while (this.explicitCache.size() > n2) {
            this.explicitCache.removeLeastRecent().exitExplicitCacheToClose();
        }
        this.cacheSize = n2;
        this.implicitCache.vacancy(n2);
        this.explicitCache.vacancy(n2);
    }

    public void setImplicitCachingEnabled(boolean bl) throws SQLException {
        if (!bl) {
            this.purgeImplicitCache();
        }
        this.implicitCacheEnabled = bl;
    }

    public boolean getImplicitCachingEnabled() throws SQLException {
        boolean bl = this.cacheSize == 0 ? false : this.implicitCacheEnabled;
        return bl;
    }

    public void setExplicitCachingEnabled(boolean bl) throws SQLException {
        if (!bl) {
            this.purgeExplicitCache();
        }
        this.explicitCacheEnabled = bl;
    }

    public boolean getExplicitCachingEnabled() throws SQLException {
        boolean bl = this.cacheSize == 0 ? false : this.explicitCacheEnabled;
        return bl;
    }

    void addToImplicitCache(OraclePreparedStatement oraclePreparedStatement, String string, int n2, int n3) throws SQLException {
        if (!this.implicitCacheEnabled || this.cacheSize == 0 || oraclePreparedStatement.cacheState == 2) {
            return;
        }
        oraclePreparedStatement.enterImplicitCache();
        this.implicitCache.add(oraclePreparedStatement, n2, n3, string);
        while (this.implicitCache.size() > this.cacheSize) {
            this.implicitCache.removeLeastRecent().exitImplicitCacheToClose();
        }
    }

    void addToExplicitCache(OraclePreparedStatement oraclePreparedStatement, String string) throws SQLException {
        if (!this.explicitCacheEnabled || this.cacheSize == 0 || oraclePreparedStatement.cacheState == 2) {
            return;
        }
        oraclePreparedStatement.enterExplicitCache();
        this.explicitCache.add(oraclePreparedStatement, 0, 0, string);
        while (this.explicitCache.size() > this.cacheSize) {
            this.explicitCache.removeLeastRecent().exitExplicitCacheToClose();
        }
    }

    OracleStatement searchImplicitCache(String string, int n2, int n3, PhysicalConnection physicalConnection) throws SQLException {
        if (!this.implicitCacheEnabled || this.cacheSize == 0) {
            return null;
        }
        OraclePreparedStatement oraclePreparedStatement = this.implicitCache.removeMostRecent(n2, n3, string);
        if (oraclePreparedStatement != null) {
            oraclePreparedStatement.exitImplicitCacheToActive();
            this.dmsStatementCacheHitCount.occurred();
        } else {
            this.dmsStatementCacheMissCount.occurred();
        }
        return oraclePreparedStatement;
    }

    OracleStatement searchExplicitCache(String string) throws SQLException {
        if (!this.explicitCacheEnabled || 0 == this.cacheSize) {
            return null;
        }
        OraclePreparedStatement oraclePreparedStatement = this.explicitCache.removeMostRecent(0, 0, string);
        if (null != oraclePreparedStatement) {
            oraclePreparedStatement.exitExplicitCacheToActive();
            this.dmsStatementCacheHitCount.occurred();
        } else {
            this.dmsStatementCacheMissCount.occurred();
        }
        return oraclePreparedStatement;
    }

    void purgeImplicitCache() throws SQLException {
        OraclePreparedStatement oraclePreparedStatement = this.implicitCache.removeLeastRecent();
        while (null != oraclePreparedStatement) {
            oraclePreparedStatement.exitImplicitCacheToClose();
            oraclePreparedStatement = this.implicitCache.removeLeastRecent();
        }
    }

    void purgeExplicitCache() throws SQLException {
        OraclePreparedStatement oraclePreparedStatement = this.explicitCache.removeLeastRecent();
        while (null != oraclePreparedStatement) {
            oraclePreparedStatement.exitExplicitCacheToClose();
            oraclePreparedStatement = this.explicitCache.removeLeastRecent();
        }
    }

    int getCacheSize() {
        return this.cacheSize;
    }

    void close() throws SQLException {
        this.purgeImplicitCache();
        this.implicitCache.close();
        this.purgeExplicitCache();
        this.explicitCache.close();
    }

    private OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    protected void clearCursorIds() {
        this.implicitCache.forEach(oraclePreparedStatement -> oraclePreparedStatement.clearCursorId());
        this.explicitCache.forEach(oraclePreparedStatement -> oraclePreparedStatement.clearCursorId());
    }

    public String toString() {
        return "implicitCache=" + this.implicitCache.toString() + ", explicitCache=" + this.explicitCache.toString();
    }
}

