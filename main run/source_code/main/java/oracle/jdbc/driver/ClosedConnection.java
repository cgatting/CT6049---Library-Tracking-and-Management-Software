/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletionStage;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.AbstractConnectionBuilder;
import oracle.jdbc.internal.CompletionStageUtil;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.pool.OraclePooledConnection;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class ClosedConnection
extends PhysicalConnection {
    ClosedConnection() {
        this.lifecycle = 4;
    }

    @Override
    void initializePassword(OpaqueString opaqueString) throws SQLException {
    }

    @Override
    OracleStatement RefCursorBytesToStatement(byte[] byArray, OracleStatement oracleStatement) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    OracleStatement createImplicitResultSetStatement(OracleStatement oracleStatement) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    int getDefaultStreamChunkSize() {
        return -1;
    }

    @Override
    short doGetVersionNumber() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    String doGetDatabaseProductVersion() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    void doRollback() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    void doCommit(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    void doSetAutoCommit(boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    void cancelOperationOnServer(boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    void doAbort() throws SQLException {
    }

    @Override
    void open(OracleStatement oracleStatement) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    void logon(AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    final CompletionStage<Void> logonAsync(AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) {
        return CompletionStageUtil.failedStage(DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace());
    }

    @Override
    public void getPropertyForPooledConnection(OraclePooledConnection oraclePooledConnection) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public BLOB createTemporaryBlob(Connection connection, boolean bl, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public CLOB createTemporaryClob(Connection connection, boolean bl, int n2, short s2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public boolean attachServerConnection() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public void detachServerConnection(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public boolean needToPurgeStatementCache() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public boolean isDRCPEnabled() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public int getVarTypeMaxLenCompat() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public void closeLogicalConnection() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public void cleanupAndClose() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public boolean isLifecycleOpen() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public void clearDrcpTagName() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public void setClientIdentifier(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public void clearClientIdentifier(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public void beginRequest() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public void endRequest() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }

    @Override
    public int freeTemporaryBlobsAndClobs() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
    }
}

