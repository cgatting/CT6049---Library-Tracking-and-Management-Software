/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.OracleTimeout;
import oracle.jdbc.driver.OracleTimeoutPollingThread;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleTimeoutThreadPerVM
extends OracleTimeout
implements Monitor {
    private static final OracleTimeoutPollingThread watchdog = new OracleTimeoutPollingThread();
    private OracleStatement statement;
    private long interruptAfter;
    private String name;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();

    OracleTimeoutThreadPerVM(String string) {
        this.name = string;
        this.interruptAfter = Long.MAX_VALUE;
        watchdog.addTimeout(this);
    }

    static void stopWatchdog() {
        try {
            watchdog.interrupt();
        }
        catch (SecurityException securityException) {
        }
    }

    @Override
    void close() {
        watchdog.removeTimeout(this);
    }

    @Override
    void setTimeout(long l2, OracleStatement oracleStatement) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.interruptAfter != Long.MAX_VALUE) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 131).fillInStackTrace();
            }
            this.statement = oracleStatement;
            this.interruptAfter = System.currentTimeMillis() + l2;
        }
    }

    @Override
    void cancelTimeout() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.statement = null;
            this.interruptAfter = Long.MAX_VALUE;
        }
    }

    void interruptIfAppropriate(long l2) {
        OracleStatement oracleStatement;
        block18: {
            if (l2 < this.interruptAfter) {
                return;
            }
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                oracleStatement = this.statement;
                if (l2 >= this.interruptAfter) {
                    this.statement = null;
                    this.interruptAfter = Long.MAX_VALUE;
                    break block18;
                }
                return;
            }
        }
        try {
            oracleStatement.cancel();
        }
        catch (Throwable throwable) {
        }
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }
}

