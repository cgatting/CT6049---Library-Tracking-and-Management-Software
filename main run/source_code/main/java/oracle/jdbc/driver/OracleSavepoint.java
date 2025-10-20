/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleSql;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleSavepoint
implements oracle.jdbc.internal.OracleSavepoint,
Monitor {
    private static final int MAX_ID_VALUE = 0x7FFFFFFE;
    private static final int INVALID_ID_VALUE = -1;
    static final int NAMED_SAVEPOINT_TYPE = 2;
    static final int UNNAMED_SAVEPOINT_TYPE = 1;
    static final int UNKNOWN_SAVEPOINT_TYPE = 0;
    private static int s_seedId = 0;
    private int m_id = -1;
    private String m_name = null;
    private int m_type = 0;
    Object acProxy;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();

    OracleSavepoint() {
        this.m_type = 1;
        this.m_id = this.getNextId();
        this.m_name = null;
    }

    OracleSavepoint(String string) throws SQLException {
        if (string != null && string.length() != 0 && !OracleSql.isValidObjectName(string)) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        if (string == null || string.trim().compareTo("") == 0) {
            this.m_type = 1;
            this.m_id = this.getNextId();
            this.m_name = null;
        } else {
            this.m_type = 2;
            this.m_name = string;
            this.m_id = -1;
        }
    }

    @Override
    public int getSavepointId() throws SQLException {
        if (this.m_type == 2) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 118).fillInStackTrace();
        }
        return this.m_id;
    }

    @Override
    public String getSavepointName() throws SQLException {
        if (this.m_type == 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 119).fillInStackTrace();
        }
        return this.m_name;
    }

    int getType() {
        return this.m_type;
    }

    private int getNextId() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n2 = s_seedId = (s_seedId + 1) % 0x7FFFFFFE;
            return n2;
        }
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    @Override
    public void setACProxy(Object object) {
        this.acProxy = object;
    }

    @Override
    public Object getACProxy() {
        return this.acProxy;
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }
}

