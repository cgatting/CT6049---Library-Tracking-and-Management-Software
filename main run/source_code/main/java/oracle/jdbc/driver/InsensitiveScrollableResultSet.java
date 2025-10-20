/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.sql.Statement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.GeneratedScrollableResultSet;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class InsensitiveScrollableResultSet
extends GeneratedScrollableResultSet {
    protected boolean isAllFetched;
    protected boolean isDoneFetchingRows = false;
    protected final long maxRows;
    final boolean resultFromCache;

    InsensitiveScrollableResultSet(PhysicalConnection physicalConnection, OracleStatement oracleStatement) throws SQLException {
        super(physicalConnection, oracleStatement);
        this.fetchedRowCount = oracleStatement.validRows;
        this.resultFromCache = oracleStatement.resultFromCache;
        oracleStatement.resultFromCache = false;
        this.isAllFetched = oracleStatement.isAllFetched;
        this.maxRows = oracleStatement.getMaxRows();
        if (this.maxRows > 0L && this.maxRows <= this.fetchedRowCount) {
            this.fetchedRowCount = this.maxRows;
            this.doneFetchingRows(false);
        }
    }

    void ensureOpen() throws SQLException {
        this.ensureOpen(null);
    }

    void ensureOpen(String string) throws SQLException {
        if (this.closed) {
            if (this.connection.isClosed()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, string).fillInStackTrace();
            }
            throw (SQLException)DatabaseError.createSqlException(10, string).fillInStackTrace();
        }
        this.ensureOpenStatement(string);
    }

    void ensureOpenPlus(String string) throws SQLException {
        this.ensureOpen(string);
        if (this.isForwardOnly()) {
            throw (SQLException)DatabaseError.createSqlException(75, string).fillInStackTrace();
        }
    }

    private final void ensureOpenStatement(String string) throws SQLException {
        if (this.statement.closed) {
            throw (SQLException)DatabaseError.createSqlException(9, string).fillInStackTrace();
        }
    }

    protected boolean isForwardOnly() {
        return false;
    }

    @Override
    public int getType() throws SQLException {
        this.ensureOpen("getType");
        return 1004;
    }

    @Override
    public int getConcurrency() throws SQLException {
        this.ensureOpen("getConcurrency");
        return 1007;
    }

    @Override
    public String getCursorName() throws SQLException {
        Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                this.ensureOpen("getCursorName");
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23, "getCursorName").fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
        catch (Throwable throwable3) {
            if (closeableLock != null) {
                if (throwable != null) {
                    try {
                        closeableLock.close();
                    }
                    catch (Throwable throwable4) {
                        throwable.addSuppressed(throwable4);
                    }
                } else {
                    closeableLock.close();
                }
            }
            throw throwable3;
        }
    }

    @Override
    public void close() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                return;
            }
            super.close();
            if (this.statement.numReturnParams <= 0) {
                this.doneFetchingRows(false);
                this.statement.endOfResultSet(false);
                this.statement.closeCursorOnPlainStatement();
            }
            this.statement.closeByDependent();
            if (this.statement.isClosed() && this.statement.wrapper != null) {
                this.statement.wrapper.beClosed(this.connection.isClosed());
            }
        }
    }

    @Override
    public boolean wasNull() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("wasNull");
            boolean bl = this.statement.wasNullValue(this.currentRow);
            return bl;
        }
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        this.ensureOpen("rowDeleted");
        return false;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getMetaData");
            if (!this.statement.isOpen) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 144, "getMetaData").fillInStackTrace();
            }
            ResultSetMetaData resultSetMetaData = this.statement.getResultSetMetaData();
            return resultSetMetaData;
        }
    }

    @Override
    public Statement getStatement() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getStatement");
            oracle.jdbc.internal.OracleStatement oracleStatement = this.statement.wrapper == null ? this.statement : this.statement.wrapper;
            return oracleStatement;
        }
    }

    @Override
    public int findColumn(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("findColumn");
            int n2 = this.statement.getColumnIndex(string);
            return n2;
        }
    }

    @Override
    public void setFetchSize(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("setFetchSize");
            this.statement.setPrefetchInternal(n2, false, false);
        }
    }

    @Override
    public int getFetchSize() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getFetchSize");
            int n2 = this.statement.getPrefetchInternal(false);
            return n2;
        }
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("isBeforeFirst");
            boolean bl = !this.isEmptyResultSet() && this.currentRow == -1L;
            return bl;
        }
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("isAfterLast");
            boolean bl = this.currentRow == this.fetchedRowCount;
            return bl;
        }
    }

    @Override
    public boolean isFirst() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("isFirst");
            boolean bl = !this.isEmptyResultSet() && this.currentRow == 0L;
            return bl;
        }
    }

    @Override
    public boolean isLast() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("isLast");
            if (this.isForwardOnly()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "isLast").fillInStackTrace();
            }
            if (!this.isAllFetched && this.currentRow + 1L == this.fetchedRowCount) {
                this.fetchMoreRows();
            }
            assert (this.isAllFetched || this.fetchedRowCount > this.currentRow + 1L) : "isAllFetched: " + this.isAllFetched + ", fetchedRowCount: " + this.fetchedRowCount + ", currentRow: " + this.currentRow;
            if (this.fetchedRowCount == 0L) {
                boolean bl = false;
                return bl;
            }
            boolean bl = this.isAllFetched && this.currentRow + 1L == this.fetchedRowCount;
            return bl;
        }
    }

    @Override
    public int getRow() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getRow");
            if (this.isEmptyResultSet()) {
                int n2 = 0;
                return n2;
            }
            if (this.currentRow == this.fetchedRowCount) {
                int n3 = 0;
                return n3;
            }
            int n4 = (int)this.currentRow + 1;
            return n4;
        }
    }

    @Override
    public boolean absolute(int n2) throws SQLException {
        this.ensureOpen("absolute");
        if (this.connection.isClosed()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "absolute").fillInStackTrace();
        }
        if (this.isForwardOnly()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "absolute").fillInStackTrace();
        }
        return this.absoluteInternal(n2);
    }

    @Override
    public boolean first() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("first");
            if (this.connection.isClosed()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "first").fillInStackTrace();
            }
            if (this.isForwardOnly()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "first").fillInStackTrace();
            }
            boolean bl = this.absoluteInternal(1L);
            return bl;
        }
    }

    @Override
    public boolean next() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("next");
            if (this.statement.sqlKind.isPlsqlOrCall()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 166, "next").fillInStackTrace();
            }
            boolean bl = this.absoluteInternal(this.currentRow + 2L);
            return bl;
        }
    }

    @Override
    public boolean previous() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpenPlus("previous");
            if (this.currentRow > -1L) {
                boolean bl = this.absoluteInternal(this.currentRow);
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    @Override
    public boolean last() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpenPlus("last");
            if (this.isEmptyResultSet()) {
                boolean bl = false;
                return bl;
            }
            while (!this.isAllFetched) {
                this.fetchMoreRows();
            }
            this.currentRow = this.fetchedRowCount - 1L;
            boolean bl = true;
            return bl;
        }
    }

    @Override
    public void beforeFirst() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpenPlus("beforeFirst");
            if (this.isForwardOnly()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "beforeFirst").fillInStackTrace();
            }
            this.absolute(0);
        }
    }

    @Override
    public void afterLast() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpenPlus("afterLast");
            if (!this.isEmptyResultSet()) {
                while (!this.isAllFetched) {
                    this.fetchMoreRows();
                }
                this.currentRow = this.fetchedRowCount;
            }
        }
    }

    @Override
    public boolean relative(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpenPlus("relative");
            if (n2 == 0) {
                boolean bl = this.isValidRow();
                return bl;
            }
            if (n2 == 1) {
                boolean bl = this.next();
                return bl;
            }
            if (n2 == -1) {
                boolean bl = this.previous();
                return bl;
            }
            if (this.currentRow + (long)n2 < 0L) {
                boolean bl = this.absoluteInternal(0L);
                return bl;
            }
            boolean bl = this.absoluteInternal(this.currentRow + (long)n2 + 1L);
            return bl;
        }
    }

    @Override
    public void refreshRow() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("refreshRow");
            if (!this.statement.isRowidPrepended) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23, "refreshRow").fillInStackTrace();
            }
            if (this.currentRow < 0L || this.currentRow >= this.fetchedRowCount) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 11, "refreshRow").fillInStackTrace();
            }
            try {
                long l2 = this.currentRow;
                if (this.getFetchDirection() == 1001) {
                    l2 = Math.max(0L, this.currentRow - (long)this.getFetchSize());
                }
                this.refreshRows(l2, this.getFetchSize());
            }
            catch (SQLRecoverableException sQLRecoverableException) {
                throw sQLRecoverableException;
            }
            catch (SQLException sQLException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Unsupported syntax for refreshRow()", (Throwable)sQLException).fillInStackTrace();
            }
        }
    }

    @Override
    public <T> T getObject(int n2, Class<T> clazz) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            assert (clazz != null) : "type: null";
            this.ensureOpen("getObject");
            this.ensureValidColumnIndex(n2);
            if (this.currentRow < 0L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 14, "getObject").fillInStackTrace();
            }
            if (this.currentRow == this.fetchedRowCount) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 289, "getObject").fillInStackTrace();
            }
            T t2 = this.statement.getObject(this.currentRow, n2, clazz);
            return t2;
        }
    }

    private final void ensureValidColumnIndex(int n2) throws SQLException {
        if (n2 < 1 || n2 > this.statement.getNumberOfUserColumns()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getObject").fillInStackTrace();
        }
    }

    @Override
    public int getBytes(int n2, byte[] byArray, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getBytes").fillInStackTrace();
            }
            if (this.connection.isClosed()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getBytes").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.statement.getNumberOfUserColumns()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
            }
            if (this.currentRow < 0L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 14).fillInStackTrace();
            }
            if (this.currentRow == this.fetchedRowCount) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 289).fillInStackTrace();
            }
            int n4 = this.statement.getBytes(this.currentRow, n2, byArray, n3);
            return n4;
        }
    }

    @Override
    public OracleResultSet.AuthorizationIndicator getAuthorizationIndicator(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getAuthorizationIndicator");
            if (this.currentRow < 0L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 14, "getAuthorizationIndicator").fillInStackTrace();
            }
            if (this.currentRow == this.fetchedRowCount) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 289, "getAuthorizationIndicator").fillInStackTrace();
            }
            OracleResultSet.AuthorizationIndicator authorizationIndicator = this.statement.getAuthorizationIndicator(this.currentRow, n2);
            return authorizationIndicator;
        }
    }

    void hackLast() throws SQLException {
        assert (this.isAfterLast()) : "not after last";
        this.currentRow = this.fetchedRowCount - 1L;
    }

    protected boolean absoluteInternal(long l2) throws SQLException {
        long l3 = l2 - 1L;
        if (l2 == 0L) {
            this.currentRow = l3;
        } else if (l3 >= 0L && l3 < this.fetchedRowCount) {
            this.currentRow = l3;
        } else if (l3 >= 0L) {
            this.fetchNextRows(l3);
        } else {
            this.fetchLastRows(-l2);
        }
        assert (this.currentRow < this.fetchedRowCount || this.isAllFetched) : "currentRow: " + this.currentRow + ", fetchedRowCount: " + this.fetchedRowCount + ", isAllFetched: " + this.isAllFetched;
        assert (-1L <= this.currentRow && this.currentRow <= this.fetchedRowCount) : "currentRow: " + this.currentRow + ", fetchedRowCount: " + this.fetchedRowCount;
        return this.isCurrentRowValid();
    }

    private final void fetchNextRows(long l2) throws SQLException {
        while (!this.isAllFetched && this.fetchedRowCount <= l2) {
            this.fetchMoreRows();
        }
        this.handleFetchNextRowsCompletion(l2);
    }

    private final void handleFetchNextRowsCompletion(long l2) throws SQLException {
        if (l2 < this.fetchedRowCount) {
            this.currentRow = l2;
        } else {
            assert (this.isAllFetched) : "isAllFetched: " + this.isAllFetched;
            this.currentRow = this.fetchedRowCount;
            if (this.isForwardOnly()) {
                this.doneFetchingRows(false);
            }
        }
    }

    private final void fetchLastRows(long l2) throws SQLException {
        while (!this.isAllFetched) {
            this.fetchMoreRows();
        }
        long l3 = this.fetchedRowCount - l2;
        this.currentRow = l3 >= 0L && l3 < this.fetchedRowCount ? l3 : -1L;
    }

    protected void fetchMoreRows() throws SQLException {
        assert (!this.isAllFetched) : "isAllFetched: " + this.isAllFetched;
        this.clearWarnings();
        this.fetchedRowCount += this.statement.fetchMoreRows(this.fetchedRowCount);
        this.handleFetchMoreRowsCompletion();
    }

    private final void handleFetchMoreRowsCompletion() throws SQLException {
        this.isAllFetched = this.statement.isAllFetched;
        if (this.currentRow == this.fetchedRowCount && this.isForwardOnly()) {
            this.doneFetchingRows(false);
        }
        if (this.maxRows > 0L && this.fetchedRowCount > this.maxRows) {
            this.fetchedRowCount = this.maxRows;
            this.doneFetchingRows(false);
            this.sqlWarning = DatabaseError.addSqlWarning(this.sqlWarning, 275);
        }
    }

    @Override
    protected void doneFetchingRows(boolean bl) throws SQLException {
        if (this.isDoneFetchingRows) {
            return;
        }
        this.isDoneFetchingRows = true;
        this.isAllFetched = true;
        try {
            this.statement.closeQuery();
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
    }

    protected boolean isEmptyResultSet() throws SQLException {
        if (this.fetchedRowCount > 0L) {
            return false;
        }
        if (this.isAllFetched) {
            return true;
        }
        this.fetchMoreRows();
        assert (this.fetchedRowCount >= 0L) : "fetchedRowCount: " + this.fetchedRowCount;
        return this.fetchedRowCount == 0L;
    }

    @Override
    boolean isValidRow() throws SQLException {
        return this.isCurrentRowValid();
    }

    private final boolean isCurrentRowValid() {
        return this.currentRow > -1L && this.currentRow < this.fetchedRowCount;
    }

    protected long getValidRows() {
        return this.fetchedRowCount;
    }

    @Override
    OracleStatement getOracleStatement() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            OracleStatement oracleStatement = this.statement;
            return oracleStatement;
        }
    }

    @Override
    void removeCurrentRowFromCache() throws SQLException {
        assert (this.currentRow < this.fetchedRowCount) : "currentRow:" + this.currentRow + " fetchedRowCount:" + this.fetchedRowCount;
        if (!this.isAllFetched && this.currentRow + 1L == this.fetchedRowCount) {
            this.fetchMoreRows();
        }
        this.statement.removeRowFromCache(this.currentRow);
        --this.fetchedRowCount;
    }

    @Override
    public boolean isFromResultSetCache() throws SQLException {
        return this.resultFromCache;
    }

    @Override
    public byte[] getCompileKey() throws SQLException {
        return this.statement.getCompileKey();
    }

    @Override
    public byte[] getRuntimeKey() throws SQLException {
        return this.statement.getRuntimeKey();
    }

    @Override
    int refreshRows(long l2, int n2) throws SQLException {
        return this.statement.refreshRows(l2, n2);
    }

    @Override
    void insertRow(RowId rowId) throws SQLException {
        if (this.currentRow < this.fetchedRowCount) {
            this.statement.insertRow(this.currentRow + 1L, rowId);
            ++this.currentRow;
        } else {
            this.statement.insertRow(this.currentRow, rowId);
        }
        ++this.fetchedRowCount;
    }

    @Override
    int getColumnCount() throws SQLException {
        if (this.statement.accessors != null) {
            return this.statement.numberOfDefinePositions - (1 + this.statement.offsetOfFirstUserColumn);
        }
        return this.getMetaData().getColumnCount();
    }

    RowId getPrependedRowId() throws SQLException {
        return this.statement.getPrependedRowId(this.currentRow);
    }

    @Override
    public int getCursorId() throws SQLException {
        return this.statement.cursorId;
    }
}

