/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.diagnostics.SecuredLogger;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleClosedStatement;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Wrappable;
import oracle.jdbc.internal.Loggable;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleStatementWrapper
implements oracle.jdbc.internal.OracleStatement,
Loggable {
    private Object forEquals;
    protected oracle.jdbc.internal.OracleStatement statement;
    static final OracleCallableStatement closedStatement = new OracleClosedStatement(9);
    static final OracleCallableStatement closedStatementAC = new OracleClosedStatement(8);
    private SecuredLogger securedLogger = null;
    OracleStatement.SqlKind sqlKind = OracleStatement.SqlKind.UNINITIALIZED;
    long checkSum = 0L;
    boolean checkSumComputationFailure = false;
    Object acProxy;

    OracleStatementWrapper(oracle.jdbc.OracleStatement oracleStatement) throws SQLException {
        this.forEquals = oracleStatement;
        this.statement = (oracle.jdbc.internal.OracleStatement)oracleStatement;
        ((Wrappable)((Object)oracleStatement)).setWrapper(this);
        this.initializeLogger();
    }

    private void initializeLogger() {
    }

    @Override
    public void close() throws SQLException {
        if (this.statement == closedStatement || this.statement == closedStatementAC) {
            return;
        }
        this.checkSum = ((OracleStatement)this.statement).checkSum = this.checkSum;
        this.checkSumComputationFailure = ((OracleStatement)this.statement).checkSumComputationFailure;
        this.sqlKind = ((OracleStatement)this.statement).sqlKind;
        this.statement.close();
        ((Wrappable)((Object)this.statement)).setWrapper(null);
        this.statement = closedStatement;
    }

    void beClosed(boolean bl) throws SQLException {
        this.close();
        this.statement = bl ? closedStatementAC : closedStatement;
    }

    @Override
    public void closeWithKey(String string) throws SQLException {
        this.statement.closeWithKey(string);
        this.statement = closedStatement;
    }

    @DisableTrace
    public boolean equals(Object object) {
        return object != null && this.getClass() == object.getClass() && this.forEquals == ((OracleStatementWrapper)object).forEquals;
    }

    @DisableTrace
    public int hashCode() {
        return this.forEquals.hashCode();
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return this.statement.getFetchDirection();
    }

    @Override
    public int getFetchSize() throws SQLException {
        return this.statement.getFetchSize();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return this.statement.getMaxFieldSize();
    }

    @Override
    public int getMaxRows() throws SQLException {
        return this.statement.getMaxRows();
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return this.statement.getQueryTimeout();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return this.statement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return this.statement.getResultSetHoldability();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return this.statement.getResultSetType();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return this.statement.getUpdateCount();
    }

    @Override
    public void cancel() throws SQLException {
        this.statement.cancel();
    }

    @Override
    public void clearBatch() throws SQLException {
        this.statement.clearBatch();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.statement.clearWarnings();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return this.statement.getMoreResults();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return this.statement.executeBatch();
    }

    @Override
    public void setFetchDirection(int n2) throws SQLException {
        this.statement.setFetchDirection(n2);
    }

    @Override
    public void setFetchSize(int n2) throws SQLException {
        this.statement.setFetchSize(n2);
    }

    @Override
    public void setMaxFieldSize(int n2) throws SQLException {
        this.statement.setMaxFieldSize(n2);
    }

    @Override
    public void setMaxRows(int n2) throws SQLException {
        this.statement.setMaxRows(n2);
    }

    @Override
    public void setQueryTimeout(int n2) throws SQLException {
        this.statement.setQueryTimeout(n2);
    }

    @Override
    public boolean getMoreResults(int n2) throws SQLException {
        return this.statement.getMoreResults(n2);
    }

    @Override
    public void setEscapeProcessing(boolean bl) throws SQLException {
        this.statement.setEscapeProcessing(bl);
    }

    @Override
    public int executeUpdate(String string) throws SQLException {
        return this.statement.executeUpdate(string);
    }

    @Override
    public void addBatch(String string) throws SQLException {
        this.statement.addBatch(string);
    }

    @Override
    public void setCursorName(String string) throws SQLException {
        this.statement.setCursorName(string);
    }

    @Override
    public boolean execute(String string) throws SQLException {
        return this.statement.execute(string);
    }

    @Override
    public int executeUpdate(String string, int n2) throws SQLException {
        return this.statement.executeUpdate(string, n2);
    }

    @Override
    public boolean execute(String string, int n2) throws SQLException {
        return this.statement.execute(string, n2);
    }

    @Override
    public int executeUpdate(String string, int[] nArray) throws SQLException {
        return this.statement.executeUpdate(string, nArray);
    }

    @Override
    public boolean execute(String string, int[] nArray) throws SQLException {
        return this.statement.execute(string, nArray);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.statement.getConnection();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return this.statement.getGeneratedKeys();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return this.statement.getResultSet();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.statement.getWarnings();
    }

    @Override
    public int executeUpdate(String string, String[] stringArray) throws SQLException {
        return this.statement.executeUpdate(string, stringArray);
    }

    @Override
    public boolean execute(String string, String[] stringArray) throws SQLException {
        return this.statement.execute(string, stringArray);
    }

    @Override
    public ResultSet executeQuery(String string) throws SQLException {
        return this.statement.executeQuery(string);
    }

    @Override
    public void clearDefines() throws SQLException {
        this.statement.clearDefines();
    }

    @Override
    public void defineColumnType(int n2, int n3) throws SQLException {
        this.statement.defineColumnType(n2, n3);
    }

    @Override
    public void defineColumnType(int n2, int n3, int n4) throws SQLException {
        this.statement.defineColumnType(n2, n3, n4);
    }

    @Override
    public void defineColumnType(int n2, int n3, int n4, short s2) throws SQLException {
        this.statement.defineColumnType(n2, n3, n4, s2);
    }

    @Override
    public void defineColumnTypeBytes(int n2, int n3, int n4) throws SQLException {
        this.statement.defineColumnTypeBytes(n2, n3, n4);
    }

    @Override
    public void defineColumnTypeChars(int n2, int n3, int n4) throws SQLException {
        this.statement.defineColumnTypeChars(n2, n3, n4);
    }

    @Override
    public void defineColumnType(int n2, int n3, String string) throws SQLException {
        this.statement.defineColumnType(n2, n3, string);
    }

    @Override
    public int getRowPrefetch() {
        return this.statement.getRowPrefetch();
    }

    @Override
    public void setRowPrefetch(int n2) throws SQLException {
        this.statement.setRowPrefetch(n2);
    }

    @Override
    public int getLobPrefetchSize() throws SQLException {
        return this.statement.getLobPrefetchSize();
    }

    @Override
    public void setLobPrefetchSize(int n2) throws SQLException {
        this.statement.setLobPrefetchSize(n2);
    }

    @Override
    public int creationState() {
        return this.statement.creationState();
    }

    @Override
    public boolean isNCHAR(int n2) throws SQLException {
        return this.statement.isNCHAR(n2);
    }

    @Override
    public void setDatabaseChangeRegistration(DatabaseChangeRegistration databaseChangeRegistration) throws SQLException {
        this.statement.setDatabaseChangeRegistration(databaseChangeRegistration);
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.statement.isClosed();
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return this.statement.isPoolable();
    }

    @Override
    public void setPoolable(boolean bl) throws SQLException {
        this.statement.setPoolable(bl);
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        if (clazz.isInterface()) {
            return clazz.isInstance(this);
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface() && clazz.isInstance(this)) {
            return (T)this;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return this.statement.isCloseOnCompletion();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        this.statement.closeOnCompletion();
    }

    @Override
    public long[] executeLargeBatch() throws SQLException {
        return this.statement.executeLargeBatch();
    }

    @Override
    public long executeLargeUpdate(String string) throws SQLException {
        return this.statement.executeLargeUpdate(string);
    }

    @Override
    public long executeLargeUpdate(String string, int n2) throws SQLException {
        return this.statement.executeLargeUpdate(string, n2);
    }

    @Override
    public long executeLargeUpdate(String string, int[] nArray) throws SQLException {
        return this.statement.executeLargeUpdate(string, nArray);
    }

    @Override
    public long executeLargeUpdate(String string, String[] stringArray) throws SQLException {
        return this.statement.executeLargeUpdate(string, stringArray);
    }

    @Override
    public long getLargeMaxRows() throws SQLException {
        return this.statement.getLargeMaxRows();
    }

    @Override
    public long getLargeUpdateCount() throws SQLException {
        return this.statement.getLargeUpdateCount();
    }

    @Override
    public void setLargeMaxRows(long l2) throws SQLException {
        this.statement.setLargeMaxRows(l2);
    }

    @Override
    public void setFixedString(boolean bl) {
        this.statement.setFixedString(bl);
    }

    @Override
    public boolean getFixedString() {
        return this.statement.getFixedString();
    }

    @Override
    public int sendBatch() throws SQLException {
        return this.statement.sendBatch();
    }

    @Override
    public boolean getserverCursor() {
        return this.statement.getserverCursor();
    }

    @Override
    public int getcacheState() {
        return this.statement.getcacheState();
    }

    @Override
    public int getstatementType() {
        return this.statement.getstatementType();
    }

    @Override
    public String[] getRegisteredTableNames() throws SQLException {
        return this.statement.getRegisteredTableNames();
    }

    @Override
    public long getRegisteredQueryId() throws SQLException {
        return this.statement.getRegisteredQueryId();
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    @Override
    public void setSnapshotSCN(long l2) throws SQLException {
        this.statement.setSnapshotSCN(l2);
    }

    @Override
    public OracleStatement.SqlKind getSqlKind() throws SQLException {
        return this.statement == closedStatement ? this.sqlKind : this.statement.getSqlKind();
    }

    @Override
    public long getChecksum() throws SQLException {
        if (this.statement != closedStatement) {
            return this.statement.getChecksum();
        }
        if (this.checkSumComputationFailure) {
            throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getChecksum").fillInStackTrace();
        }
        return this.checkSum;
    }

    @Override
    public void registerBindChecksumListener(OracleStatement.BindChecksumListener bindChecksumListener) throws SQLException {
        this.statement.registerBindChecksumListener(bindChecksumListener);
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
    public long getQueryId() throws SQLException {
        return this.statement.getQueryId();
    }

    @Override
    public byte[] getCompileKey() throws SQLException {
        return this.statement.getCompileKey();
    }

    @Override
    public boolean isSimpleIdentifier(String string) throws SQLException {
        return this.statement.isSimpleIdentifier(string);
    }

    @Override
    public String enquoteIdentifier(String string, boolean bl) throws SQLException {
        return this.statement.enquoteIdentifier(string, bl);
    }

    @Override
    public String enquoteLiteral(String string) throws SQLException {
        return this.statement.enquoteLiteral(string);
    }

    @Override
    public String enquoteNCharLiteral(String string) throws SQLException {
        return this.statement.enquoteNCharLiteral(string);
    }

    @Override
    public void setShardingKeyRpnTokens(byte[] byArray) throws SQLException {
        this.statement.setShardingKeyRpnTokens(byArray);
    }

    @Override
    public byte[] getShardingKeyRpnTokens() throws SQLException {
        return this.statement.getShardingKeyRpnTokens();
    }

    @Override
    @DisableTrace
    public SecuredLogger getLogger() {
        return this.securedLogger;
    }
}

