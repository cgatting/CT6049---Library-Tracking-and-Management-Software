/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.Statement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.ForwardOnlyResultSet;
import oracle.jdbc.driver.GeneratedResultSet;
import oracle.jdbc.driver.InsensitiveScrollableResultSet;
import oracle.jdbc.driver.OldUpdatableResultSet;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.SensitiveScrollableResultSet;
import oracle.jdbc.driver.UpdatableResultSet;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET})
abstract class OracleResultSet
extends GeneratedResultSet {
    static final boolean DEBUG = false;
    boolean closed = false;
    SQLWarning sqlWarning = null;
    protected boolean needCommitAtClose = false;

    static OracleResultSet createResultSet(OracleStatement oracleStatement) throws SQLException {
        if (!oracleStatement.sqlKind.isSELECT()) {
            oracleStatement.realRsetType = ResultSetType.FORWARD_READ_ONLY;
            oracleStatement.described = true;
        }
        if (oracleStatement.realRsetType == ResultSetType.UNKNOWN) {
            return oracleStatement.userRsetType.createResultSet(oracleStatement);
        }
        return oracleStatement.realRsetType.createResultSet(oracleStatement);
    }

    protected OracleResultSet(PhysicalConnection physicalConnection) {
        super(physicalConnection);
    }

    protected abstract void doneFetchingRows(boolean var1) throws SQLException;

    @Override
    public abstract OracleResultSet.AuthorizationIndicator getAuthorizationIndicator(int var1) throws SQLException;

    @Override
    public OracleResultSet.AuthorizationIndicator getAuthorizationIndicator(String string) throws SQLException {
        return this.getAuthorizationIndicator(this.findColumn(string));
    }

    boolean isValidRow() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23, "isValidRow").fillInStackTrace();
    }

    @Override
    public void close() throws SQLException {
        OracleStatement oracleStatement;
        if (this.needCommitAtClose && (oracleStatement = this.getOracleStatement()) != null && oracleStatement.connection != null) {
            try (Monitor.CloseableLock closeableLock = oracleStatement.connection.acquireCloseableLock();){
                boolean bl = oracleStatement.connection.autoCommitSpecCompliant;
                oracleStatement.connection.autoCommitSpecCompliant = false;
                oracleStatement.connection.commit();
                oracleStatement.connection.autoCommitSpecCompliant = bl;
            }
        }
        this.closed = true;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10).fillInStackTrace();
        }
        return this.sqlWarning;
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.sqlWarning = null;
    }

    @Override
    public abstract String getCursorName() throws SQLException;

    @Override
    public abstract ResultSetMetaData getMetaData() throws SQLException;

    @Override
    public abstract boolean next() throws SQLException;

    @Override
    public abstract boolean wasNull() throws SQLException;

    @Override
    public abstract boolean isBeforeFirst() throws SQLException;

    @Override
    public abstract boolean isAfterLast() throws SQLException;

    @Override
    public abstract boolean isFirst() throws SQLException;

    @Override
    public abstract boolean isLast() throws SQLException;

    @Override
    public abstract void beforeFirst() throws SQLException;

    @Override
    public abstract void afterLast() throws SQLException;

    @Override
    public abstract boolean first() throws SQLException;

    @Override
    public abstract boolean last() throws SQLException;

    @Override
    public abstract int getRow() throws SQLException;

    @Override
    public abstract boolean absolute(int var1) throws SQLException;

    @Override
    public abstract boolean relative(int var1) throws SQLException;

    @Override
    public abstract boolean previous() throws SQLException;

    @Override
    public void setFetchDirection(int n2) throws SQLException {
    }

    @Override
    public int getFetchDirection() throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10).fillInStackTrace();
        }
        return 1000;
    }

    @Override
    public abstract void setFetchSize(int var1) throws SQLException;

    @Override
    public abstract int getFetchSize() throws SQLException;

    @Override
    public abstract int getType() throws SQLException;

    @Override
    public abstract int getConcurrency() throws SQLException;

    @Override
    public int getHoldability() throws SQLException {
        if (this.isClosed()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, null).fillInStackTrace();
        }
        return 1;
    }

    @Override
    public void insertRow() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "insertRow").fillInStackTrace();
    }

    @Override
    public void updateRow() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateRow").fillInStackTrace();
    }

    @Override
    public void deleteRow() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "deleteRow").fillInStackTrace();
    }

    @Override
    public void refreshRow() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23, null).fillInStackTrace();
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "moveToInsertRow").fillInStackTrace();
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "cancelRowUpdates").fillInStackTrace();
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "moveToCurrentRow").fillInStackTrace();
    }

    @Override
    public abstract Statement getStatement() throws SQLException;

    @Override
    public void updateNull(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateNull").fillInStackTrace();
    }

    @Override
    public void updateNull(String string) throws SQLException {
        this.updateNull(this.findColumn(string));
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return false;
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
    public abstract <T> T getObject(int var1, Class<T> var2) throws SQLException;

    @Override
    public <T> T getObject(String string, Class<T> clazz) throws SQLException {
        return this.getObject(this.findColumn(string), clazz);
    }

    @Override
    public void updateObject(int n2, Object object, SQLType sQLType) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateObject").fillInStackTrace();
    }

    @Override
    public void updateObject(int n2, Object object, SQLType sQLType, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateObject").fillInStackTrace();
    }

    @Override
    public void updateObject(String string, Object object, SQLType sQLType) throws SQLException {
        this.updateObject(this.findColumn(string), object, sQLType);
    }

    @Override
    public void updateObject(String string, Object object, SQLType sQLType, int n2) throws SQLException {
        this.updateObject(this.findColumn(string), object, sQLType, n2);
    }

    @Override
    protected OracleConnection getConnectionDuringExceptionHandling() {
        return this.connection;
    }

    abstract OracleStatement getOracleStatement() throws SQLException;

    abstract int refreshRows(long var1, int var3) throws SQLException;

    void insertRow(RowId rowId) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89, "call appendRow").fillInStackTrace();
    }

    abstract void removeCurrentRowFromCache() throws SQLException;

    abstract int getColumnCount() throws SQLException;

    boolean isComplete() throws SQLException {
        return this.closed;
    }

    @Override
    public int getBytes(int n2, byte[] byArray, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    protected void awaitPublishing() throws SQLException {
    }

    static enum ResultSetType {
        UNKNOWN(-1, -1, false){

            @Override
            OracleResultSet createResultSet(OracleStatement oracleStatement) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException(23, null).fillInStackTrace();
            }

            @Override
            ResultSetType downgrade() {
                return UNKNOWN;
            }
        }
        ,
        FORWARD_READ_ONLY(1003, 1007, false){

            @Override
            OracleResultSet createResultSet(OracleStatement oracleStatement) throws SQLException {
                return new ForwardOnlyResultSet(oracleStatement.connection, oracleStatement);
            }

            @Override
            ResultSetType downgrade() {
                return FORWARD_READ_ONLY;
            }
        }
        ,
        FORWARD_UPDATABLE(1003, 1008, true){

            @Override
            OracleResultSet createResultSet(OracleStatement oracleStatement) throws SQLException {
                ForwardOnlyResultSet forwardOnlyResultSet = new ForwardOnlyResultSet(oracleStatement.connection, oracleStatement);
                if (oracleStatement.connection.isOldUpdateableResultSet) {
                    return new OldUpdatableResultSet(oracleStatement, forwardOnlyResultSet);
                }
                return new UpdatableResultSet(oracleStatement, forwardOnlyResultSet);
            }

            @Override
            ResultSetType downgrade() {
                return FORWARD_READ_ONLY;
            }
        }
        ,
        INSENSITIVE_READ_ONLY(1004, 1007, false){

            @Override
            OracleResultSet createResultSet(OracleStatement oracleStatement) throws SQLException {
                return new InsensitiveScrollableResultSet(oracleStatement.connection, oracleStatement);
            }

            @Override
            ResultSetType downgrade() {
                return FORWARD_READ_ONLY;
            }
        }
        ,
        INSENSITIVE_UPDATABLE(1004, 1008, true){

            @Override
            OracleResultSet createResultSet(OracleStatement oracleStatement) throws SQLException {
                InsensitiveScrollableResultSet insensitiveScrollableResultSet = new InsensitiveScrollableResultSet(oracleStatement.connection, oracleStatement);
                if (oracleStatement.connection.isOldUpdateableResultSet) {
                    return new OldUpdatableResultSet(oracleStatement, insensitiveScrollableResultSet);
                }
                return new UpdatableResultSet(oracleStatement, insensitiveScrollableResultSet);
            }

            @Override
            ResultSetType downgrade() {
                return INSENSITIVE_READ_ONLY;
            }
        }
        ,
        SENSITIVE_READ_ONLY(1005, 1007, true){

            @Override
            OracleResultSet createResultSet(OracleStatement oracleStatement) throws SQLException {
                return new SensitiveScrollableResultSet(oracleStatement.connection, oracleStatement);
            }

            @Override
            ResultSetType downgrade() {
                return INSENSITIVE_READ_ONLY;
            }
        }
        ,
        SENSITIVE_UPDATABLE(1005, 1008, true){

            @Override
            OracleResultSet createResultSet(OracleStatement oracleStatement) throws SQLException {
                SensitiveScrollableResultSet sensitiveScrollableResultSet = new SensitiveScrollableResultSet(oracleStatement.connection, oracleStatement);
                if (oracleStatement.connection.isOldUpdateableResultSet) {
                    return new OldUpdatableResultSet(oracleStatement, sensitiveScrollableResultSet);
                }
                return new UpdatableResultSet(oracleStatement, sensitiveScrollableResultSet);
            }

            @Override
            ResultSetType downgrade() {
                return INSENSITIVE_READ_ONLY;
            }
        };

        private final int type;
        private final int concur;
        private final boolean isIdentifierRequired;

        static ResultSetType typeFor(int n2, int n3) throws SQLException {
            for (ResultSetType resultSetType : (ResultSetType[])ResultSetType.class.getEnumConstants()) {
                if (resultSetType.getType() != n2 || resultSetType.getConcur() != n3) continue;
                return resultSetType;
            }
            throw (SQLException)DatabaseError.createSqlException(68, "type: " + n2 + " concurency: " + n3).fillInStackTrace();
        }

        private ResultSetType(int n3, int n4, boolean bl) {
            this.type = n3;
            this.concur = n4;
            this.isIdentifierRequired = bl;
        }

        int getType() {
            return this.type;
        }

        int getConcur() {
            return this.concur;
        }

        boolean isIdentifierRequired() {
            return this.isIdentifierRequired;
        }

        boolean isForwardOnly() {
            return this.type == 1003;
        }

        boolean isScrollable() {
            return this.type != 1003;
        }

        boolean isUpdatable() {
            return this.concur == 1008;
        }

        abstract OracleResultSet createResultSet(OracleStatement var1) throws SQLException;

        abstract ResultSetType downgrade();
    }
}

