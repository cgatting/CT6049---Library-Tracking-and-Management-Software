/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import oracle.jdbc.OracleData;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.InsensitiveScrollableResultSet;
import oracle.jdbc.driver.JavaToJavaConverter;
import oracle.jdbc.driver.LogicalConnection;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OraclePreparedStatementWrapper;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OracleResultSetMetaData;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.proxy.ProxyFactory;
import oracle.jdbc.proxy._Proxy_;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.CustomDatum;
import oracle.sql.CustomDatumFactory;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NCLOB;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.RAW;
import oracle.sql.REF;
import oracle.sql.ROWID;
import oracle.sql.STRUCT;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OldUpdatableResultSet
extends OracleResultSet {
    protected static final int MAX_CHAR_BUFFER_SIZE = 1024;
    protected static final int MAX_BYTE_BUFFER_SIZE = 1024;
    protected static final int ASCII_STREAM = 1;
    protected static final int BINARY_STREAM = 2;
    protected static final int UNICODE_STREAM = 3;
    protected static final int VALUE_NULL = 1;
    protected static final int VALUE_NOT_NULL = 2;
    protected static final int VALUE_UNKNOWN = 3;
    protected static final int VALUE_IN_RSET = 4;
    static final int concurrencyType = 1008;
    static final int BEGIN_COLUMN_INDEX = 0;
    private int wasNull;
    private static int _MIN_STREAM_SIZE = 4000;
    OracleResultSet resultSet;
    boolean isRowDeleted = false;
    boolean isCachedRset;
    OracleStatement scrollStmt;
    ResultSetMetaData rsetMetaData;
    private int columnCount;
    private OraclePreparedStatement deleteStmt;
    private OraclePreparedStatement insertStmt;
    private OraclePreparedStatement updateStmt;
    private int[] indexColsChanged;
    private Object[] rowBuffer;
    private boolean[] m_nullIndicator;
    private int[][] typeInfo;
    private boolean isInserting;
    private boolean isUpdating;
    ArrayList<Clob> tempClobsToFree = null;
    ArrayList<Blob> tempBlobsToFree = null;

    OldUpdatableResultSet(OracleStatement oracleStatement, OracleResultSet oracleResultSet) throws SQLException {
        super(oracleStatement.connection);
        this.resultSet = oracleResultSet;
        this.scrollStmt = oracleStatement;
        this.deleteStmt = null;
        this.insertStmt = null;
        this.updateStmt = null;
        this.indexColsChanged = null;
        this.rowBuffer = null;
        this.m_nullIndicator = null;
        this.typeInfo = null;
        this.isInserting = false;
        this.isUpdating = false;
        this.wasNull = -1;
        this.rsetMetaData = null;
        this.columnCount = 0;
        this.getInternalMetadata();
        this.isCachedRset = true;
    }

    void ensureOpen() throws SQLException {
        if (this.closed) {
            if (this.connection.isClosed()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10).fillInStackTrace();
        }
        if (this.resultSet == null || this.scrollStmt == null || this.scrollStmt.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
    }

    @Override
    public void close() throws SQLException {
        if (this.closed) {
            return;
        }
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            super.close();
            if (this.resultSet != null) {
                this.resultSet.close();
            }
            if (this.insertStmt != null) {
                this.insertStmt.close();
            }
            if (this.updateStmt != null) {
                this.updateStmt.close();
            }
            if (this.deleteStmt != null) {
                this.deleteStmt.close();
            }
            if (this.scrollStmt != null) {
                this.scrollStmt.notifyCloseRset();
            }
            this.cancelRowInserts();
            this.connection = LogicalConnection.closedConnection;
            this.resultSet = null;
            this.scrollStmt = null;
            this.rsetMetaData = null;
            this.scrollStmt = null;
            this.deleteStmt = null;
            this.insertStmt = null;
            this.updateStmt = null;
            this.indexColsChanged = null;
            this.rowBuffer = null;
            this.m_nullIndicator = null;
            this.typeInfo = null;
        }
    }

    @Override
    public int getCursorId() throws SQLException {
        return this.scrollStmt.cursorId;
    }

    @Override
    public boolean wasNull() throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            switch (this.wasNull) {
                case 1: {
                    boolean bl = true;
                    return bl;
                }
                case 2: {
                    boolean bl = false;
                    return bl;
                }
                case 4: {
                    boolean bl = this.resultSet.wasNull();
                    return bl;
                }
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 24).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    @Override
    public Statement getStatement() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            OracleStatement oracleStatement = this.scrollStmt;
            return oracleStatement;
        }
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            SQLWarning sQLWarning = this.resultSet.getWarnings();
            if (this.sqlWarning == null) {
                SQLWarning sQLWarning2 = sQLWarning;
                return sQLWarning2;
            }
            SQLWarning sQLWarning3 = this.sqlWarning;
            while (sQLWarning3.getNextWarning() != null) {
                sQLWarning3 = sQLWarning3.getNextWarning();
            }
            sQLWarning3.setNextWarning(sQLWarning);
            sQLWarning3 = this.sqlWarning;
            return sQLWarning3;
        }
    }

    @Override
    public void clearWarnings() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.sqlWarning = null;
            this.resultSet.clearWarnings();
        }
    }

    @Override
    public OracleResultSet.AuthorizationIndicator getAuthorizationIndicator(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            OracleResultSet.AuthorizationIndicator authorizationIndicator = this.resultSet.getAuthorizationIndicator(n2);
            return authorizationIndicator;
        }
    }

    @Override
    public boolean next() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.cancelRowChanges();
            if (this.isRowDeleted) {
                this.isRowDeleted = false;
                boolean bl = this.resultSet.isValidRow();
                return bl;
            }
            boolean bl = this.resultSet.next();
            return bl;
        }
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                boolean bl = this.resultSet.isFirst();
                return bl;
            }
            boolean bl = this.resultSet.isBeforeFirst();
            return bl;
        }
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            boolean bl = this.resultSet.isAfterLast();
            return bl;
        }
    }

    @Override
    public boolean isFirst() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            boolean bl = this.resultSet.isFirst();
            return bl;
        }
    }

    @Override
    public boolean isLast() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            boolean bl = this.resultSet.isLast();
            return bl;
        }
    }

    @Override
    public void beforeFirst() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.cancelRowChanges();
            this.isRowDeleted = false;
            this.resultSet.beforeFirst();
        }
    }

    @Override
    public void afterLast() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.cancelRowChanges();
            this.isRowDeleted = false;
            this.resultSet.afterLast();
        }
    }

    @Override
    public boolean first() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.cancelRowChanges();
            this.isRowDeleted = false;
            boolean bl = this.resultSet.first();
            return bl;
        }
    }

    @Override
    public boolean last() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.cancelRowChanges();
            this.isRowDeleted = false;
            boolean bl = this.resultSet.last();
            return bl;
        }
    }

    @Override
    public int getRow() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            int n2 = this.resultSet.getRow();
            return n2;
        }
    }

    @Override
    public boolean absolute(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.cancelRowChanges();
            this.isRowDeleted = false;
            boolean bl = this.resultSet.absolute(n2);
            return bl;
        }
    }

    @Override
    public boolean relative(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.cancelRowChanges();
            if (this.isRowDeleted) {
                --n2;
                this.isRowDeleted = false;
            }
            boolean bl = this.resultSet.relative(n2);
            return bl;
        }
    }

    @Override
    public boolean previous() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.cancelRowChanges();
            this.isRowDeleted = false;
            boolean bl = this.resultSet.previous();
            return bl;
        }
    }

    void addToTempLobsToFree(Clob clob) {
        if (this.tempClobsToFree == null) {
            this.tempClobsToFree = new ArrayList();
        }
        this.tempClobsToFree.add(clob);
    }

    void addToTempLobsToFree(Blob blob) {
        if (this.tempBlobsToFree == null) {
            this.tempBlobsToFree = new ArrayList();
        }
        this.tempBlobsToFree.add(blob);
    }

    void cleanTempLobs() {
        this.cleanTempClobs(this.tempClobsToFree);
        this.cleanTempBlobs(this.tempBlobsToFree);
        this.tempClobsToFree = null;
        this.tempBlobsToFree = null;
    }

    void cleanTempBlobs(ArrayList<Blob> arrayList) {
        if (arrayList != null) {
            Iterator<Blob> iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                try {
                    ((BLOB)iterator.next()).freeTemporary();
                }
                catch (SQLException sQLException) {
                }
            }
        }
    }

    void cleanTempClobs(ArrayList<Clob> arrayList) {
        if (arrayList != null) {
            Iterator<Clob> iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                try {
                    ((CLOB)iterator.next()).freeTemporary();
                }
                catch (SQLException sQLException) {
                }
            }
        }
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.resultSet.getMetaData();
    }

    @Override
    public int findColumn(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            int n2 = this.resultSet.findColumn(string);
            return n2;
        }
    }

    @Override
    public void setFetchDirection(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.resultSet.setFetchDirection(n2);
        }
    }

    @Override
    public int getFetchDirection() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            int n2 = this.resultSet.getFetchDirection();
            return n2;
        }
    }

    @Override
    public void setFetchSize(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.resultSet.setFetchSize(n2);
        }
    }

    @Override
    public int getFetchSize() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            int n2 = this.resultSet.getFetchSize();
            return n2;
        }
    }

    @Override
    public int getType() throws SQLException {
        this.ensureOpen();
        return this.scrollStmt.realRsetType.getType();
    }

    @Override
    public int getConcurrency() throws SQLException {
        this.ensureOpen();
        return 1008;
    }

    @Override
    public String getCursorName() throws SQLException {
        Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
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
    public boolean rowUpdated() throws SQLException {
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return this.isRowDeleted;
    }

    @Override
    public void insertRow() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            if (!this.isOnInsertRow()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 83).fillInStackTrace();
            }
            this.prepareInsertRowStatement();
            this.prepareInsertRowBinds();
            this.executeInsertRow();
        }
    }

    @Override
    public void updateRow() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            if (this.isOnInsertRow()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 84).fillInStackTrace();
            }
            int n2 = this.getNumColumnsChanged();
            if (n2 > 0) {
                this.prepareUpdateRowStatement(n2);
                this.prepareUpdateRowBinds(n2);
                this.executeUpdateRow();
            }
        }
    }

    @Override
    public void deleteRow() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            if (this.isOnInsertRow()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 84).fillInStackTrace();
            }
            this.prepareDeleteRowStatement();
            this.prepareDeleteRowBinds();
            this.executeDeleteRow();
            this.isRowDeleted = true;
        }
    }

    @Override
    public void refreshRow() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            if (this.isOnInsertRow()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 84).fillInStackTrace();
            }
            this.resultSet.refreshRow();
        }
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isUpdating) {
                this.isUpdating = false;
                this.clearRowBuffer();
            }
        }
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isOnInsertRow()) {
                return;
            }
            this.isRowDeleted = false;
            this.isInserting = true;
            if (this.rowBuffer == null) {
                this.rowBuffer = new Object[this.getColumnCount()];
            }
            if (this.m_nullIndicator == null) {
                this.m_nullIndicator = new boolean[this.getColumnCount()];
            }
            this.clearRowBuffer();
        }
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            this.cancelRowInserts();
        }
    }

    @Override
    public <T> T getObject(int n2, Class<T> clazz) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            assert (clazz != null) : "type: null";
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Object var5_5 = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null) {
                    var5_5 = datum.toClass(clazz);
                }
            } else {
                this.setIsNull(4);
                var5_5 = this.resultSet.getObject(n2, clazz);
            }
            datum = var5_5;
            return (T)datum;
        }
    }

    @Override
    public void updateNull(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            this.setRowBufferAt(n2, null);
        }
    }

    @Override
    int getColumnCount() throws SQLException {
        if (this.columnCount == 0) {
            this.columnCount = this.resultSet.getColumnCount();
        }
        return this.columnCount;
    }

    ResultSetMetaData getInternalMetadata() throws SQLException {
        if (this.rsetMetaData == null) {
            this.rsetMetaData = this.resultSet.getMetaData();
        }
        return this.rsetMetaData;
    }

    private void cancelRowChanges() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.isInserting) {
                this.cancelRowInserts();
            }
            if (this.isUpdating) {
                this.cancelRowUpdates();
            }
        }
    }

    boolean isOnInsertRow() {
        return this.isInserting;
    }

    private void cancelRowInserts() {
        if (this.isInserting) {
            this.isInserting = false;
            this.clearRowBuffer();
        }
    }

    boolean isUpdatingRow() {
        return this.isUpdating;
    }

    private void clearRowBuffer() {
        int n2;
        if (this.rowBuffer != null) {
            for (n2 = 0; n2 < this.rowBuffer.length; ++n2) {
                this.rowBuffer[n2] = null;
            }
        }
        if (this.m_nullIndicator != null) {
            for (n2 = 0; n2 < this.m_nullIndicator.length; ++n2) {
                this.m_nullIndicator[n2] = false;
            }
        }
        if (this.typeInfo != null) {
            for (n2 = 0; n2 < this.typeInfo.length; ++n2) {
                if (this.typeInfo[n2] == null) continue;
                for (int i2 = 0; i2 < this.typeInfo[n2].length; ++i2) {
                    this.typeInfo[n2][i2] = 0;
                }
            }
        }
        this.cleanTempLobs();
    }

    protected void setRowBufferAt(int n2, Datum datum) throws SQLException {
        this.setRowBufferAt(n2, datum, null);
    }

    protected void setRowBufferAt(int n2, Object object, int[] nArray) throws SQLException {
        if (!this.isInserting) {
            if (this.isBeforeFirst() || this.isAfterLast() || this.getRow() == 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            this.isUpdating = true;
        }
        if (n2 < 1 || n2 > this.getColumnCount()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "setRowBufferAt").fillInStackTrace();
        }
        if (this.rowBuffer == null) {
            this.rowBuffer = new Object[this.getColumnCount()];
        }
        if (this.m_nullIndicator == null) {
            this.m_nullIndicator = new boolean[this.getColumnCount()];
            for (int i2 = 0; i2 < this.getColumnCount(); ++i2) {
                this.m_nullIndicator[i2] = false;
            }
        }
        if (nArray != null) {
            if (this.typeInfo == null) {
                this.typeInfo = new int[this.getColumnCount()][];
            }
            this.typeInfo[n2 - 1] = nArray;
        }
        this.rowBuffer[n2 - 1] = object;
        this.m_nullIndicator[n2 - 1] = object == null;
    }

    protected Datum getRowBufferDatumAt(int n2) throws SQLException {
        Object object;
        if (n2 < 1 || n2 > this.getColumnCount()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "getRowBufferDatumAt").fillInStackTrace();
        }
        Datum datum = null;
        if (this.rowBuffer != null && (object = this.rowBuffer[n2 - 1]) != null) {
            if (object instanceof Datum) {
                datum = (Datum)object;
            } else {
                OracleResultSetMetaData oracleResultSetMetaData = (OracleResultSetMetaData)this.getInternalMetadata();
                datum = SQLUtil.makeOracleDatum(this.connection, object, oracleResultSetMetaData.getColumnType(n2), null, oracleResultSetMetaData.isNCHAR(n2));
                this.rowBuffer[n2 - 1] = datum;
            }
        }
        return datum;
    }

    protected Object getRowBufferAt(int n2) throws SQLException {
        if (n2 < 1 || n2 > this.getColumnCount()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "getRowBufferDatumAt").fillInStackTrace();
        }
        if (this.rowBuffer != null) {
            return this.rowBuffer[n2 - 1];
        }
        return null;
    }

    protected boolean isRowBufferUpdatedAt(int n2) throws SQLException {
        if (n2 < 1 || n2 > this.getColumnCount()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "getRowBufferDatumAt").fillInStackTrace();
        }
        if (this.rowBuffer == null) {
            return false;
        }
        return this.rowBuffer[n2 - 1] != null || this.m_nullIndicator[n2 - 1];
    }

    private void prepareInsertRowStatement() throws SQLException {
        if (this.insertStmt == null) {
            String string = this.scrollStmt.sqlObject.getInsertSqlForUpdatableResultSet(this);
            PreparedStatement preparedStatement = this.connection.prepareStatement(string);
            this.insertStmt = (OraclePreparedStatement)((OraclePreparedStatementWrapper)preparedStatement).preparedStatement;
            this.insertStmt.setQueryTimeout(this.scrollStmt.getQueryTimeout());
            if (this.scrollStmt.sqlObject.generatedSqlNeedEscapeProcessing()) {
                this.insertStmt.setEscapeProcessing(true);
            }
        }
    }

    private void prepareInsertRowBinds() throws SQLException {
        int n2 = 1;
        n2 = this.prepareSubqueryBinds(this.insertStmt, n2);
        OracleResultSetMetaData oracleResultSetMetaData = (OracleResultSetMetaData)this.getInternalMetadata();
        for (int i2 = 1; i2 <= this.getColumnCount(); ++i2) {
            Object object = this.getRowBufferAt(i2);
            if (object != null) {
                if (object instanceof Reader) {
                    if (oracleResultSetMetaData.isNCHAR(i2)) {
                        this.insertStmt.setFormOfUse(n2 + i2 - 1, (short)2);
                    }
                    this.insertStmt.setCharacterStream(n2 + i2 - 1, (Reader)object, this.typeInfo[i2 - 1][0]);
                    continue;
                }
                if (object instanceof InputStream) {
                    if (this.typeInfo[i2 - 1][1] == 2) {
                        this.insertStmt.setBinaryStream(n2 + i2 - 1, (InputStream)object, this.typeInfo[i2 - 1][0]);
                        continue;
                    }
                    if (this.typeInfo[i2 - 1][1] != 1) continue;
                    this.insertStmt.setAsciiStream(n2 + i2 - 1, (InputStream)object, this.typeInfo[i2 - 1][0]);
                    continue;
                }
                Datum datum = this.getRowBufferDatumAt(i2);
                if (oracleResultSetMetaData.isNCHAR(i2)) {
                    this.insertStmt.setFormOfUse(n2 + i2 - 1, (short)2);
                }
                this.insertStmt.setOracleObject(n2 + i2 - 1, datum);
                continue;
            }
            int n3 = this.getInternalMetadata().getColumnType(i2);
            if (n3 == 2006 || n3 == 2002 || n3 == 2008 || n3 == 2007 || n3 == 2003 || n3 == 2009) {
                this.insertStmt.setNull(n2 + i2 - 1, n3, this.getInternalMetadata().getColumnTypeName(i2));
                continue;
            }
            this.insertStmt.setNull(n2 + i2 - 1, n3);
        }
    }

    private void executeInsertRow() throws SQLException {
        if (this.insertStmt.executeUpdate() != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 85).fillInStackTrace();
        }
    }

    private int getNumColumnsChanged() throws SQLException {
        int n2 = 0;
        if (this.indexColsChanged == null) {
            this.indexColsChanged = new int[this.getColumnCount()];
        }
        if (this.rowBuffer != null) {
            for (int i2 = 0; i2 < this.getColumnCount(); ++i2) {
                if (this.rowBuffer[i2] == null && (this.rowBuffer[i2] != null || !this.m_nullIndicator[i2])) continue;
                this.indexColsChanged[n2++] = i2;
            }
        }
        return n2;
    }

    private void prepareUpdateRowStatement(int n2) throws SQLException {
        if (this.updateStmt != null) {
            this.updateStmt.close();
        }
        String string = this.scrollStmt.sqlObject.getUpdateSqlForUpdatableResultSet(this, n2, this.rowBuffer, this.indexColsChanged);
        PreparedStatement preparedStatement = this.connection.prepareStatement(string);
        this.updateStmt = (OraclePreparedStatement)((OraclePreparedStatementWrapper)preparedStatement).preparedStatement;
        this.updateStmt.setQueryTimeout(this.scrollStmt.getQueryTimeout());
        if (this.scrollStmt.sqlObject.generatedSqlNeedEscapeProcessing()) {
            this.updateStmt.setEscapeProcessing(true);
        }
    }

    private void prepareUpdateRowBinds(int n2) throws SQLException {
        int n3 = 1;
        n3 = this.prepareSubqueryBinds(this.updateStmt, n3);
        OracleResultSetMetaData oracleResultSetMetaData = (OracleResultSetMetaData)this.getInternalMetadata();
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = this.indexColsChanged[i2];
            Object object = this.getRowBufferAt(n4 + 1);
            if (object != null) {
                if (object instanceof Reader) {
                    if (oracleResultSetMetaData.isNCHAR(n4 + 1)) {
                        this.updateStmt.setFormOfUse(n3, (short)2);
                    }
                    this.updateStmt.setCharacterStream(n3++, (Reader)object, this.typeInfo[n4][0]);
                    continue;
                }
                if (object instanceof InputStream) {
                    if (this.typeInfo[n4][1] == 2) {
                        this.updateStmt.setBinaryStream(n3++, (InputStream)object, this.typeInfo[n4][0]);
                        continue;
                    }
                    if (this.typeInfo[n4][1] != 1) continue;
                    this.updateStmt.setAsciiStream(n3++, (InputStream)object, this.typeInfo[n4][0]);
                    continue;
                }
                Datum datum = this.getRowBufferDatumAt(n4 + 1);
                if (oracleResultSetMetaData.isNCHAR(n4 + 1)) {
                    this.updateStmt.setFormOfUse(n3, (short)2);
                }
                this.updateStmt.setOracleObject(n3++, datum);
                continue;
            }
            int n5 = this.getInternalMetadata().getColumnType(n4 + 1);
            if (n5 == 2006 || n5 == 2002 || n5 == 2008 || n5 == 2007 || n5 == 2003 || n5 == 2009) {
                this.updateStmt.setNull(n3++, n5, this.getInternalMetadata().getColumnTypeName(n4 + 1));
                continue;
            }
            if (oracleResultSetMetaData.isNCHAR(n4 + 1)) {
                this.updateStmt.setFormOfUse(n3, (short)2);
            }
            this.updateStmt.setNull(n3++, n5);
        }
        this.prepareCompareSelfBinds(this.updateStmt, n3);
    }

    private void executeUpdateRow() throws SQLException {
        try {
            if (this.updateStmt.executeUpdate() == 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 85).fillInStackTrace();
            }
            if (this.isCachedRset) {
                this.refreshRows(this.getRow(), 1);
                this.cancelRowUpdates();
            }
        }
        finally {
            if (this.updateStmt != null) {
                this.updateStmt.close();
                this.updateStmt = null;
            }
        }
    }

    @Override
    int refreshRows(long l2, int n2) throws SQLException {
        return this.resultSet.refreshRows(l2 - 1L, n2);
    }

    private void prepareDeleteRowStatement() throws SQLException {
        if (this.deleteStmt == null) {
            String string = this.scrollStmt.sqlObject.getDeleteSqlForUpdatableResultSet(this);
            PreparedStatement preparedStatement = this.connection.prepareStatement(string);
            this.deleteStmt = (OraclePreparedStatement)((OraclePreparedStatementWrapper)preparedStatement).preparedStatement;
            this.deleteStmt.setQueryTimeout(this.scrollStmt.getQueryTimeout());
            if (this.scrollStmt.sqlObject.generatedSqlNeedEscapeProcessing()) {
                this.deleteStmt.setEscapeProcessing(true);
            }
        }
    }

    private void prepareDeleteRowBinds() throws SQLException {
        int n2 = 1;
        n2 = this.prepareSubqueryBinds(this.deleteStmt, n2);
        this.prepareCompareSelfBinds(this.deleteStmt, n2);
    }

    private void executeDeleteRow() throws SQLException {
        if (this.deleteStmt.executeUpdate() == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 85).fillInStackTrace();
        }
        if (this.isCachedRset) {
            this.removeCurrentRowFromCache();
        }
    }

    @Override
    void removeCurrentRowFromCache() throws SQLException {
        this.resultSet.removeCurrentRowFromCache();
    }

    @Override
    public boolean isFromResultSetCache() throws SQLException {
        return this.resultSet instanceof InsensitiveScrollableResultSet ? ((InsensitiveScrollableResultSet)this.resultSet).isFromResultSetCache() : false;
    }

    @Override
    public byte[] getCompileKey() throws SQLException {
        return this.scrollStmt.getCompileKey();
    }

    @Override
    public byte[] getRuntimeKey() throws SQLException {
        return this.scrollStmt.getRuntimeKey();
    }

    private int prepareCompareSelfBinds(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
        oraclePreparedStatement.setRowId(n2, ((InsensitiveScrollableResultSet)this.resultSet).getPrependedRowId());
        return n2 + 1;
    }

    private int prepareSubqueryBinds(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
        return n2 + this.scrollStmt.copyBinds(oraclePreparedStatement, n2 - 1);
    }

    protected void setIsNull(int n2) {
        this.wasNull = n2;
    }

    protected void setIsNull(boolean bl) {
        this.setIsNull(bl ? 1 : 2);
    }

    @Override
    protected void doneFetchingRows(boolean bl) throws SQLException {
        this.resultSet.doneFetchingRows(bl);
    }

    @Override
    OracleStatement getOracleStatement() throws SQLException {
        return this.resultSet == null ? null : this.resultSet.getOracleStatement();
    }

    @Override
    public Datum getOracleObject(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Datum datum = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
            } else {
                datum = this.resultSet.getOracleObject(n2);
                this.setIsNull(4);
            }
            Datum datum2 = datum;
            return datum2;
        }
    }

    @Override
    public String getString(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            String string = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getRowBufferDatumAt(n2);
                this.setIsNull(object == null);
                if (object != null) {
                    string = ((Datum)object).stringValue(this.connection);
                }
            } else {
                this.setIsNull(4);
                string = this.resultSet.getString(n2);
            }
            object = string;
            return object;
        }
    }

    @Override
    public boolean getBoolean(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            boolean bl = false;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Datum datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null) {
                    bl = datum.booleanValue();
                }
            } else {
                this.setIsNull(4);
                bl = this.resultSet.getBoolean(n2);
            }
            boolean bl2 = bl;
            return bl2;
        }
    }

    @Override
    public byte getByte(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            byte by = 0;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Datum datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null) {
                    by = datum.byteValue();
                }
            } else {
                this.setIsNull(4);
                by = this.resultSet.getByte(n2);
            }
            byte by2 = by;
            return by2;
        }
    }

    @Override
    public short getShort(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            short s2 = 0;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                long l2 = this.getLong(n2);
                if (l2 > 65537L || l2 < -65538L) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 26, "getShort").fillInStackTrace();
                }
                s2 = (short)l2;
            } else {
                this.setIsNull(4);
                s2 = this.resultSet.getShort(n2);
            }
            short s3 = s2;
            return s3;
        }
    }

    @Override
    public int getInt(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            int n3 = 0;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Datum datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null) {
                    n3 = datum.intValue();
                }
            } else {
                this.setIsNull(4);
                n3 = this.resultSet.getInt(n2);
            }
            int n4 = n3;
            return n4;
        }
    }

    @Override
    public long getLong(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            long l2 = 0L;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Datum datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null) {
                    l2 = datum.longValue();
                }
            } else {
                this.setIsNull(4);
                l2 = this.resultSet.getLong(n2);
            }
            long l3 = l2;
            return l3;
        }
    }

    @Override
    public float getFloat(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            float f2 = 0.0f;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Datum datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null) {
                    f2 = datum.floatValue();
                }
            } else {
                this.setIsNull(4);
                f2 = this.resultSet.getFloat(n2);
            }
            float f3 = f2;
            return f3;
        }
    }

    @Override
    public double getDouble(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            double d2 = 0.0;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Datum datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null) {
                    d2 = datum.doubleValue();
                }
            } else {
                this.setIsNull(4);
                d2 = this.resultSet.getDouble(n2);
            }
            double d3 = d2;
            return d3;
        }
    }

    @Override
    public BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Serializable serializable;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            BigDecimal bigDecimal = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                serializable = this.getRowBufferDatumAt(n2);
                this.setIsNull(serializable == null);
                if (serializable != null) {
                    bigDecimal = serializable.bigDecimalValue();
                }
            } else {
                this.setIsNull(4);
                bigDecimal = this.resultSet.getBigDecimal(n2);
            }
            serializable = bigDecimal;
            return serializable;
        }
    }

    @Override
    public byte[] getBytes(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            byte[] byArray = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getRowBufferDatumAt(n2);
                this.setIsNull(object == null);
                if (object != null) {
                    byArray = ((Datum)object).getBytes();
                }
            } else {
                this.setIsNull(4);
                byArray = this.resultSet.getBytes(n2);
            }
            object = byArray;
            return object;
        }
    }

    @Override
    public Date getDate(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Serializable serializable;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Date date = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                serializable = this.getRowBufferDatumAt(n2);
                this.setIsNull(serializable == null);
                if (serializable != null) {
                    date = serializable.dateValue();
                }
            } else {
                this.setIsNull(4);
                date = this.resultSet.getDate(n2);
            }
            serializable = date;
            return serializable;
        }
    }

    @Override
    public Time getTime(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Serializable serializable;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Time time = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                serializable = this.getRowBufferDatumAt(n2);
                this.setIsNull(serializable == null);
                if (serializable != null) {
                    time = serializable.timeValue();
                }
            } else {
                this.setIsNull(4);
                time = this.resultSet.getTime(n2);
            }
            serializable = time;
            return serializable;
        }
    }

    @Override
    public Timestamp getTimestamp(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Serializable serializable;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Timestamp timestamp = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                serializable = this.getRowBufferDatumAt(n2);
                this.setIsNull(serializable == null);
                if (serializable != null) {
                    timestamp = serializable.timestampValue();
                }
            } else {
                this.setIsNull(4);
                timestamp = this.resultSet.getTimestamp(n2);
            }
            serializable = timestamp;
            return serializable;
        }
    }

    @Override
    public InputStream getAsciiStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            InputStream inputStream = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getRowBufferAt(n2);
                this.setIsNull(object == null);
                if (object != null) {
                    if (object instanceof InputStream) {
                        inputStream = (InputStream)object;
                    } else {
                        Datum datum = this.getRowBufferDatumAt(n2);
                        inputStream = datum.asciiStreamValue();
                    }
                }
            } else {
                this.setIsNull(4);
                inputStream = this.resultSet.getAsciiStream(n2);
            }
            object = inputStream;
            return object;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public InputStream getUnicodeStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            InputStream inputStream = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getRowBufferAt(n2);
                this.setIsNull(object == null);
                if (object != null) {
                    if (object instanceof InputStream) {
                        inputStream = (InputStream)object;
                    } else {
                        Datum datum = this.getRowBufferDatumAt(n2);
                        DBConversion dBConversion = this.connection.conversion;
                        byte[] byArray = datum.shareBytes();
                        if (datum instanceof RAW) {
                            inputStream = dBConversion.ConvertStream(new ByteArrayInputStream(byArray), 3);
                        } else {
                            if (!(datum instanceof CHAR)) throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getUnicodeStream").fillInStackTrace();
                            inputStream = dBConversion.ConvertStream(new ByteArrayInputStream(byArray), 1);
                        }
                    }
                }
            } else {
                this.setIsNull(4);
                inputStream = this.resultSet.getUnicodeStream(n2);
            }
            object = inputStream;
            return object;
        }
    }

    @Override
    public InputStream getBinaryStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            InputStream inputStream = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getRowBufferAt(n2);
                this.setIsNull(object == null);
                if (object != null) {
                    if (object instanceof InputStream) {
                        inputStream = (InputStream)object;
                    } else {
                        Datum datum = this.getRowBufferDatumAt(n2);
                        inputStream = datum.binaryStreamValue();
                    }
                }
            } else {
                this.setIsNull(4);
                inputStream = this.resultSet.getBinaryStream(n2);
            }
            object = inputStream;
            return object;
        }
    }

    @Override
    public Object getObject(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Object object2 = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getOracleObject(n2);
                this.setIsNull(object == null);
                if (object != null) {
                    object2 = ((Datum)object).toJdbc();
                }
            } else {
                this.setIsNull(4);
                object2 = this.resultSet.getObject(n2);
            }
            object = object2;
            return object;
        }
    }

    @Override
    public Object getObject(int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            if (oracleDataFactory == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            Object object2 = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getObject(n2);
                this.setIsNull(object == null);
                object2 = oracleDataFactory.create(object, 0);
            } else {
                this.setIsNull(4);
                object2 = this.resultSet.getObject(n2, oracleDataFactory);
            }
            object = object2;
            return object;
        }
    }

    @Override
    public Reader getCharacterStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Reader reader = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getRowBufferAt(n2);
                this.setIsNull(object == null);
                if (object != null) {
                    if (object instanceof Reader) {
                        reader = (Reader)object;
                    } else {
                        Datum datum = this.getRowBufferDatumAt(n2);
                        reader = datum.characterStreamValue();
                    }
                }
            } else {
                this.setIsNull(4);
                reader = this.resultSet.getCharacterStream(n2);
            }
            object = reader;
            return object;
        }
    }

    @Override
    public BigDecimal getBigDecimal(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Serializable serializable;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            BigDecimal bigDecimal = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                serializable = this.getRowBufferDatumAt(n2);
                this.setIsNull(serializable == null);
                if (serializable != null) {
                    bigDecimal = serializable.bigDecimalValue();
                }
            } else {
                this.setIsNull(4);
                bigDecimal = this.resultSet.getBigDecimal(n2);
            }
            serializable = bigDecimal;
            return serializable;
        }
    }

    @Override
    public Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Object object2 = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getOracleObject(n2);
                this.setIsNull(object == null);
                if (object != null) {
                    object2 = object instanceof STRUCT ? ((STRUCT)object).toJdbc((Map)map) : ((Datum)object).toJdbc();
                }
            } else {
                this.setIsNull(4);
                object2 = this.resultSet.getObject(n2, map);
            }
            object = object2;
            return object;
        }
    }

    @Override
    public Ref getRef(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            REF rEF = this.getREF(n2);
            return rEF;
        }
    }

    @Override
    public Blob getBlob(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            BLOB bLOB = this.getBLOB(n2);
            return bLOB;
        }
    }

    @Override
    public Clob getClob(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            CLOB cLOB = this.getCLOB(n2);
            return cLOB;
        }
    }

    @Override
    public Array getArray(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            ARRAY aRRAY = this.getARRAY(n2);
            return aRRAY;
        }
    }

    @Override
    public Date getDate(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Serializable serializable;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Date date = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                serializable = this.getOracleObject(n2);
                this.setIsNull(serializable == null);
                if (serializable != null) {
                    Serializable serializable2;
                    if (serializable instanceof DATE) {
                        date = ((DATE)serializable).dateValue(calendar);
                    } else if (serializable instanceof TIMESTAMP) {
                        serializable2 = ((TIMESTAMP)serializable).timestampValue(calendar);
                        long l2 = ((Timestamp)serializable2).getTime();
                        date = new Date(l2);
                    } else {
                        serializable2 = new DATE(((Datum)serializable).stringValue(this.connection));
                        if (serializable2 != null) {
                            date = ((DATE)serializable2).dateValue(calendar);
                        }
                    }
                }
            } else {
                this.setIsNull(4);
                date = this.resultSet.getDate(n2, calendar);
            }
            serializable = date;
            return serializable;
        }
    }

    @Override
    public Time getTime(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Serializable serializable;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Time time = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                serializable = this.getOracleObject(n2);
                this.setIsNull(serializable == null);
                if (serializable != null) {
                    Serializable serializable2;
                    if (serializable instanceof DATE) {
                        time = ((DATE)serializable).timeValue(calendar);
                    } else if (serializable instanceof TIMESTAMP) {
                        serializable2 = ((TIMESTAMP)serializable).timestampValue(calendar);
                        long l2 = ((Timestamp)serializable2).getTime();
                        time = new Time(l2);
                    } else {
                        serializable2 = new DATE(((Datum)serializable).stringValue(this.connection));
                        if (serializable2 != null) {
                            time = ((DATE)serializable2).timeValue(calendar);
                        }
                    }
                }
            } else {
                this.setIsNull(4);
                time = this.resultSet.getTime(n2, calendar);
            }
            serializable = time;
            return serializable;
        }
    }

    @Override
    public Timestamp getTimestamp(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Serializable serializable;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            Timestamp timestamp = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                serializable = this.getOracleObject(n2);
                this.setIsNull(serializable == null);
                if (serializable != null) {
                    if (serializable instanceof DATE) {
                        timestamp = ((DATE)serializable).timestampValue(calendar);
                    } else if (serializable instanceof TIMESTAMP) {
                        timestamp = ((TIMESTAMP)serializable).timestampValue(calendar);
                    } else {
                        DATE dATE = new DATE(((Datum)serializable).stringValue(this.connection));
                        if (dATE != null) {
                            timestamp = dATE.timestampValue(calendar);
                        }
                    }
                }
            } else {
                this.setIsNull(4);
                timestamp = this.resultSet.getTimestamp(n2, calendar);
            }
            serializable = timestamp;
            return serializable;
        }
    }

    @Override
    public URL getURL(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            URL uRL = null;
            int n3 = this.getInternalMetadata().getColumnType(n2);
            int n4 = SQLUtil.getInternalType(n3);
            if (n4 == 96 || n4 == 1 || n4 == 8) {
                try {
                    object = this.getString(n2);
                    if (object == null) {
                        uRL = null;
                    }
                    uRL = new URL((String)object);
                }
                catch (MalformedURLException malformedURLException) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 136).fillInStackTrace();
                }
            } else {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getURL").fillInStackTrace();
            }
            object = uRL;
            return object;
        }
    }

    @Override
    public ResultSet getCursor(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            ResultSet resultSet = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Datum datum = this.getOracleObject(n2);
                this.setIsNull(datum == null);
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCursor").fillInStackTrace();
            }
            this.setIsNull(4);
            ResultSet resultSet2 = resultSet = this.resultSet.getCursor(n2);
            return resultSet2;
        }
    }

    @Override
    public ROWID getROWID(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            ROWID rOWID = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof ROWID)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getROWID").fillInStackTrace();
                }
                rOWID = (ROWID)datum;
            } else {
                this.setIsNull(4);
                rOWID = this.resultSet.getROWID(n2);
            }
            datum = rOWID;
            return datum;
        }
    }

    @Override
    public NUMBER getNUMBER(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            NUMBER nUMBER = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof NUMBER)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getNUMBER").fillInStackTrace();
                }
                nUMBER = (NUMBER)datum;
            } else {
                this.setIsNull(4);
                nUMBER = this.resultSet.getNUMBER(n2);
            }
            datum = nUMBER;
            return datum;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public DATE getDATE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            DATE dATE = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                if (datum != null) {
                    if (datum instanceof DATE) {
                        dATE = (DATE)datum;
                    } else {
                        if (!(datum instanceof TIMESTAMP)) throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getDATE").fillInStackTrace();
                        Timestamp timestamp = ((TIMESTAMP)datum).timestampValue();
                        dATE = new DATE(timestamp);
                    }
                } else {
                    this.setIsNull(datum == null);
                }
            } else {
                this.setIsNull(4);
                dATE = this.resultSet.getDATE(n2);
            }
            datum = dATE;
            return datum;
        }
    }

    @Override
    public TIMESTAMP getTIMESTAMP(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            TIMESTAMP tIMESTAMP = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof TIMESTAMP)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTIMESTAMP").fillInStackTrace();
                }
                tIMESTAMP = (TIMESTAMP)datum;
            } else {
                this.setIsNull(4);
                tIMESTAMP = this.resultSet.getTIMESTAMP(n2);
            }
            datum = tIMESTAMP;
            return datum;
        }
    }

    @Override
    public TIMESTAMPTZ getTIMESTAMPTZ(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            TIMESTAMPTZ tIMESTAMPTZ = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof TIMESTAMPTZ)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTIMESTAMPTZ").fillInStackTrace();
                }
                tIMESTAMPTZ = (TIMESTAMPTZ)datum;
            } else {
                this.setIsNull(4);
                tIMESTAMPTZ = this.resultSet.getTIMESTAMPTZ(n2);
            }
            datum = tIMESTAMPTZ;
            return datum;
        }
    }

    @Override
    public TIMESTAMPLTZ getTIMESTAMPLTZ(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            TIMESTAMPLTZ tIMESTAMPLTZ = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof TIMESTAMPLTZ)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTIMESTAMPLTZ").fillInStackTrace();
                }
                tIMESTAMPLTZ = (TIMESTAMPLTZ)datum;
            } else {
                this.setIsNull(4);
                tIMESTAMPLTZ = this.resultSet.getTIMESTAMPLTZ(n2);
            }
            datum = tIMESTAMPLTZ;
            return datum;
        }
    }

    @Override
    public INTERVALDS getINTERVALDS(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            INTERVALDS iNTERVALDS = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof INTERVALDS)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getINTERVALDS").fillInStackTrace();
                }
                iNTERVALDS = (INTERVALDS)datum;
            } else {
                this.setIsNull(4);
                iNTERVALDS = this.resultSet.getINTERVALDS(n2);
            }
            datum = iNTERVALDS;
            return datum;
        }
    }

    @Override
    public INTERVALYM getINTERVALYM(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            INTERVALYM iNTERVALYM = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof INTERVALYM)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getINTERVALYM").fillInStackTrace();
                }
                iNTERVALYM = (INTERVALYM)datum;
            } else {
                this.setIsNull(4);
                iNTERVALYM = this.resultSet.getINTERVALYM(n2);
            }
            datum = iNTERVALYM;
            return datum;
        }
    }

    @Override
    public ARRAY getARRAY(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            ARRAY aRRAY = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof ARRAY)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getARRAY").fillInStackTrace();
                }
                aRRAY = (ARRAY)datum;
            } else {
                this.setIsNull(4);
                aRRAY = this.resultSet.getARRAY(n2);
            }
            datum = aRRAY;
            return datum;
        }
    }

    @Override
    public STRUCT getSTRUCT(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            STRUCT sTRUCT = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof STRUCT)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getSTRUCT").fillInStackTrace();
                }
                sTRUCT = (STRUCT)datum;
            } else {
                this.setIsNull(4);
                sTRUCT = this.resultSet.getSTRUCT(n2);
            }
            datum = sTRUCT;
            return datum;
        }
    }

    @Override
    public OPAQUE getOPAQUE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            OPAQUE oPAQUE = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof OPAQUE)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOPAQUE").fillInStackTrace();
                }
                oPAQUE = (OPAQUE)datum;
            } else {
                this.setIsNull(4);
                oPAQUE = this.resultSet.getOPAQUE(n2);
            }
            datum = oPAQUE;
            return datum;
        }
    }

    @Override
    public REF getREF(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            REF rEF = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof REF)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getREF").fillInStackTrace();
                }
                rEF = (REF)datum;
            } else {
                this.setIsNull(4);
                rEF = this.resultSet.getREF(n2);
            }
            datum = rEF;
            return datum;
        }
    }

    @Override
    public CHAR getCHAR(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            CHAR cHAR = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof CHAR)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCHAR").fillInStackTrace();
                }
                cHAR = (CHAR)datum;
            } else {
                this.setIsNull(4);
                cHAR = this.resultSet.getCHAR(n2);
            }
            datum = cHAR;
            return datum;
        }
    }

    @Override
    public RAW getRAW(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            RAW rAW = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof RAW)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getRAW").fillInStackTrace();
                }
                rAW = (RAW)datum;
            } else {
                this.setIsNull(4);
                rAW = this.resultSet.getRAW(n2);
            }
            datum = rAW;
            return datum;
        }
    }

    @Override
    public BLOB getBLOB(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            BLOB bLOB = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof BLOB)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBLOB").fillInStackTrace();
                }
                bLOB = (BLOB)datum;
            } else {
                this.setIsNull(4);
                bLOB = this.resultSet.getBLOB(n2);
            }
            datum = bLOB;
            return datum;
        }
    }

    public NCLOB getNCLOB(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            NCLOB nCLOB = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof NCLOB)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCLOB").fillInStackTrace();
                }
                nCLOB = (NCLOB)datum;
            } else {
                this.setIsNull(4);
                nCLOB = (NCLOB)this.resultSet.getNClob(n2);
            }
            datum = nCLOB;
            return datum;
        }
    }

    @Override
    public CLOB getCLOB(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            CLOB cLOB = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof CLOB)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCLOB").fillInStackTrace();
                }
                cLOB = (CLOB)datum;
            } else {
                this.setIsNull(4);
                cLOB = this.resultSet.getCLOB(n2);
            }
            datum = cLOB;
            return datum;
        }
    }

    @Override
    public BFILE getBFILE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            BFILE bFILE = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                datum = this.getRowBufferDatumAt(n2);
                this.setIsNull(datum == null);
                if (datum != null && !(datum instanceof BFILE)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBFILE").fillInStackTrace();
                }
                bFILE = (BFILE)datum;
            } else {
                this.setIsNull(4);
                bFILE = this.resultSet.getBFILE(n2);
            }
            datum = bFILE;
            return datum;
        }
    }

    @Override
    public BFILE getBfile(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            BFILE bFILE = this.getBFILE(n2);
            return bFILE;
        }
    }

    @Override
    public CustomDatum getCustomDatum(int n2, CustomDatumFactory customDatumFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            if (customDatumFactory == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            CustomDatum customDatum = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getRowBufferDatumAt(n2);
                this.setIsNull(object == null);
                customDatum = customDatumFactory.create((Datum)object, 0);
            } else {
                this.setIsNull(4);
                customDatum = this.resultSet.getCustomDatum(n2, customDatumFactory);
            }
            object = customDatum;
            return object;
        }
    }

    @Override
    public ORAData getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            if (oRADataFactory == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            ORAData oRAData = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getRowBufferDatumAt(n2);
                this.setIsNull(object == null);
                oRAData = oRADataFactory.create((Datum)object, 0);
            } else {
                this.setIsNull(4);
                oRAData = this.resultSet.getORAData(n2, oRADataFactory);
            }
            object = oRAData;
            return object;
        }
    }

    @Override
    public NClob getNClob(int n2) throws SQLException {
        this.ensureOpen();
        if (this.isRowDeleted) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
        }
        NCLOB nCLOB = this.getNCLOB(n2);
        if (nCLOB == null) {
            return null;
        }
        if (!(nCLOB instanceof NClob)) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 184).fillInStackTrace();
        }
        return nCLOB;
    }

    @Override
    public String getNString(int n2) throws SQLException {
        this.ensureOpen();
        if (this.isRowDeleted) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
        }
        return this.getString(n2);
    }

    @Override
    public Reader getNCharacterStream(int n2) throws SQLException {
        this.ensureOpen();
        if (this.isRowDeleted) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
        }
        return this.getCharacterStream(n2);
    }

    @Override
    public RowId getRowId(int n2) throws SQLException {
        this.ensureOpen();
        if (this.isRowDeleted) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
        }
        return this.getROWID(n2);
    }

    @Override
    public SQLXML getSQLXML(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            SQLXML sQLXML = null;
            this.setIsNull(3);
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                object = this.getRowBufferDatumAt(n2);
                this.setIsNull(object == null);
                if (object != null && !(object instanceof SQLXML)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getSQLXML").fillInStackTrace();
                }
                sQLXML = (SQLXML)object;
            } else {
                this.setIsNull(4);
                sQLXML = this.resultSet.getSQLXML(n2);
            }
            object = sQLXML;
            return object;
        }
    }

    @Override
    public void updateRowId(int n2, RowId rowId) throws SQLException {
        this.updateROWID(n2, (ROWID)rowId);
    }

    @Override
    public void updateNCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        this.updateCharacterStream(n2, reader, l2);
    }

    @Override
    public void updateNCharacterStream(int n2, Reader reader) throws SQLException {
        this.updateCharacterStream(n2, reader);
    }

    @Override
    public void updateSQLXML(int n2, SQLXML sQLXML) throws SQLException {
        this.updateOracleObject(n2, (Datum)((Object)sQLXML));
    }

    @Override
    public void updateNString(int n2, String string) throws SQLException {
        this.updateString(n2, string);
    }

    @Override
    public void updateNClob(int n2, NClob nClob) throws SQLException {
        this.updateClob(n2, (Clob)nClob);
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream, long l2) throws SQLException {
        this.updateAsciiStream(n2, inputStream, (int)l2);
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream) throws SQLException {
        this.updateAsciiStream(n2, inputStream, Integer.MAX_VALUE);
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream, long l2) throws SQLException {
        this.updateBinaryStream(n2, inputStream, (int)l2);
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream) throws SQLException {
        this.updateBinaryStream(n2, inputStream, Integer.MAX_VALUE);
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        this.updateCharacterStream(n2, reader, (int)l2);
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader) throws SQLException {
        this.updateCharacterStream(n2, reader, Integer.MAX_VALUE);
    }

    @Override
    public void updateBlob(int n2, InputStream inputStream) throws SQLException {
        if (inputStream == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "updateBlob").fillInStackTrace();
        }
        Blob blob = this.connection.createBlob();
        this.addToTempLobsToFree(blob);
        int n3 = ((BLOB)blob).getBufferSize();
        OutputStream outputStream = blob.setBinaryStream(1L);
        byte[] byArray = new byte[n3];
        try {
            int n4;
            while ((n4 = inputStream.read(byArray)) != -1) {
                outputStream.write(byArray, 0, n4);
            }
            outputStream.close();
            this.updateBlob(n2, blob);
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
    }

    @Override
    public void updateBlob(int n2, InputStream inputStream, long l2) throws SQLException {
        if (inputStream == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "updateBlob").fillInStackTrace();
        }
        Blob blob = this.connection.createBlob();
        this.addToTempLobsToFree(blob);
        int n3 = ((BLOB)blob).getBufferSize();
        OutputStream outputStream = blob.setBinaryStream(1L);
        byte[] byArray = new byte[n3];
        try {
            int n4;
            for (long i2 = l2; i2 > 0L && (n4 = inputStream.read(byArray, 0, Math.min(n3, (int)i2))) != -1; i2 -= (long)n4) {
                outputStream.write(byArray, 0, n4);
            }
            outputStream.close();
            this.updateBlob(n2, blob);
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
    }

    @Override
    public void updateClob(int n2, Reader reader, long l2) throws SQLException {
        this.updateClob(n2, reader, l2, (short)1);
    }

    void updateClob(int n2, Reader reader, long l2, short s2) throws SQLException {
        if (reader == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "updateClob").fillInStackTrace();
        }
        Clob clob = s2 == 1 ? this.connection.createClob() : this.connection.createNClob();
        this.addToTempLobsToFree(clob);
        int n3 = ((CLOB)clob).getBufferSize();
        Writer writer = clob.setCharacterStream(1L);
        char[] cArray = new char[n3];
        try {
            int n4;
            for (long i2 = l2; i2 > 0L && (n4 = reader.read(cArray, 0, Math.min(n3, (int)i2))) != -1; i2 -= (long)n4) {
                writer.write(cArray, 0, n4);
            }
            writer.close();
            this.updateClob(n2, clob);
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
    }

    @Override
    public void updateClob(int n2, Reader reader) throws SQLException {
        if (reader == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "updateClob").fillInStackTrace();
        }
        Clob clob = this.connection.createClob();
        this.addToTempLobsToFree(clob);
        int n3 = ((CLOB)clob).getBufferSize();
        Writer writer = clob.setCharacterStream(1L);
        char[] cArray = new char[n3];
        try {
            int n4;
            while ((n4 = reader.read(cArray)) != -1) {
                writer.write(cArray, 0, n4);
            }
            writer.close();
            this.updateClob(n2, clob);
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
    }

    void updateClob(int n2, InputStream inputStream, int n3) throws SQLException {
        this.updateClob(n2, inputStream, n3, (short)1);
    }

    void updateClob(int n2, InputStream inputStream, int n3, short s2) throws SQLException {
        if (inputStream == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "updateClob").fillInStackTrace();
        }
        Clob clob = s2 == 1 ? this.connection.createClob() : this.connection.createNClob();
        this.addToTempLobsToFree(clob);
        int n4 = ((CLOB)clob).getBufferSize();
        OutputStream outputStream = clob.setAsciiStream(1L);
        byte[] byArray = new byte[n4];
        try {
            int n5;
            for (long i2 = (long)n3; i2 > 0L && (n5 = inputStream.read(byArray, 0, Math.min(n4, (int)i2))) != -1; i2 -= (long)n5) {
                outputStream.write(byArray, 0, n5);
            }
            outputStream.close();
            this.updateClob(n2, clob);
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
    }

    void updateNClob(int n2, InputStream inputStream, int n3) throws SQLException {
        if (inputStream == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "updateNClob").fillInStackTrace();
        }
        NClob nClob = this.connection.createNClob();
        this.addToTempLobsToFree(nClob);
        int n4 = ((NCLOB)nClob).getBufferSize();
        OutputStream outputStream = nClob.setAsciiStream(1L);
        byte[] byArray = new byte[n4];
        try {
            int n5;
            for (long i2 = (long)n3; i2 > 0L && (n5 = inputStream.read(byArray, 0, Math.min(n4, (int)i2))) != -1; i2 -= (long)n5) {
                outputStream.write(byArray, 0, n5);
            }
            outputStream.close();
            this.updateNClob(n2, nClob);
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
    }

    @Override
    public void updateNClob(int n2, Reader reader, long l2) throws SQLException {
        if (reader == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "updateNClob").fillInStackTrace();
        }
        NClob nClob = this.connection.createNClob();
        this.addToTempLobsToFree(nClob);
        int n3 = ((CLOB)((Object)nClob)).getBufferSize();
        Writer writer = nClob.setCharacterStream(1L);
        char[] cArray = new char[n3];
        try {
            int n4;
            for (long i2 = l2; i2 > 0L && (n4 = reader.read(cArray, 0, Math.min(n3, (int)i2))) != -1; i2 -= (long)n4) {
                writer.write(cArray, 0, n4);
            }
            writer.close();
            this.updateNClob(n2, nClob);
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
    }

    @Override
    public void updateNClob(int n2, Reader reader) throws SQLException {
        if (reader == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "updateNClob").fillInStackTrace();
        }
        NClob nClob = this.connection.createNClob();
        this.addToTempLobsToFree(nClob);
        int n3 = ((CLOB)((Object)nClob)).getBufferSize();
        Writer writer = nClob.setCharacterStream(1L);
        char[] cArray = new char[n3];
        try {
            int n4;
            while ((n4 = reader.read(cArray)) != -1) {
                writer.write(cArray, 0, n4);
            }
            writer.close();
            this.updateNClob(n2, nClob);
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
    }

    @Override
    public void updateString(int n2, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (string == null || string.length() == 0) {
                this.updateNull(n2);
            } else {
                this.updateObject(n2, (Object)string);
            }
        }
    }

    @Override
    public void updateBoolean(int n2, boolean bl) throws SQLException {
        this.updateObject(n2, (Object)bl);
    }

    @Override
    public void updateByte(int n2, byte by) throws SQLException {
        this.updateObject(n2, (Object)by);
    }

    @Override
    public void updateShort(int n2, short s2) throws SQLException {
        this.updateObject(n2, (Object)s2);
    }

    @Override
    public void updateInt(int n2, int n3) throws SQLException {
        this.updateObject(n2, (Object)n3);
    }

    @Override
    public void updateLong(int n2, long l2) throws SQLException {
        this.updateObject(n2, (Object)l2);
    }

    @Override
    public void updateFloat(int n2, float f2) throws SQLException {
        this.updateObject(n2, (Object)Float.valueOf(f2));
    }

    @Override
    public void updateDouble(int n2, double d2) throws SQLException {
        this.updateObject(n2, (Object)d2);
    }

    @Override
    public void updateBigDecimal(int n2, BigDecimal bigDecimal) throws SQLException {
        this.updateObject(n2, (Object)bigDecimal);
    }

    @Override
    public void updateBytes(int n2, byte[] byArray) throws SQLException {
        this.updateObject(n2, (Object)byArray);
    }

    @Override
    public void updateDate(int n2, Date date) throws SQLException {
        this.updateObject(n2, (Object)date);
    }

    @Override
    public void updateTime(int n2, Time time) throws SQLException {
        this.updateObject(n2, (Object)time);
    }

    @Override
    public void updateTimestamp(int n2, Timestamp timestamp) throws SQLException {
        this.updateObject(n2, (Object)timestamp);
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream, int n3) throws SQLException {
        block11: {
            block12: {
                this.ensureOpen();
                if (this.isRowDeleted) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
                }
                OracleResultSetMetaData oracleResultSetMetaData = (OracleResultSetMetaData)this.getInternalMetadata();
                int n4 = oracleResultSetMetaData.getColumnType(n2);
                if (inputStream == null || n3 <= 0) break block12;
                switch (n4) {
                    case 2005: {
                        this.updateClob(n2, inputStream, n3);
                        break block11;
                    }
                    case 2011: {
                        this.updateNClob(n2, inputStream, n3);
                        break block11;
                    }
                    case 2004: {
                        this.updateBlob(n2, inputStream, (long)n3);
                        break block11;
                    }
                    case -1: {
                        int[] nArray = new int[]{n3, 1};
                        this.setRowBufferAt(n2, inputStream, nArray);
                        break block11;
                    }
                    default: {
                        try {
                            int n5;
                            int n6 = 0;
                            byte[] byArray = new byte[1024];
                            char[] cArray = new char[1024];
                            StringBuilder stringBuilder = new StringBuilder(1024);
                            for (n5 = n3; n5 > 0 && (n6 = n5 >= 1024 ? inputStream.read(byArray) : inputStream.read(byArray, 0, n5)) != -1; n5 -= n6) {
                                DBConversion.asciiBytesToJavaChars(byArray, n6, cArray);
                                stringBuilder.append(cArray, 0, n6);
                            }
                            inputStream.close();
                            if (n5 == n3) {
                                this.updateNull(n2);
                                return;
                            }
                            this.updateString(n2, stringBuilder.toString());
                            break block11;
                        }
                        catch (IOException iOException) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
                        }
                    }
                }
            }
            this.setRowBufferAt(n2, null);
        }
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream, int n3) throws SQLException {
        block9: {
            block10: {
                this.ensureOpen();
                if (this.isRowDeleted) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
                }
                int n4 = this.getInternalMetadata().getColumnType(n2);
                if (inputStream == null || n3 <= 0) break block10;
                switch (n4) {
                    case 2004: {
                        this.updateBlob(n2, inputStream, (long)n3);
                        break block9;
                    }
                    case -4: {
                        int[] nArray = new int[]{n3, 2};
                        this.setRowBufferAt(n2, inputStream, nArray);
                        break block9;
                    }
                    default: {
                        try {
                            int n5;
                            int n6 = 0;
                            byte[] byArray = new byte[1024];
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                            for (n5 = n3; n5 > 0 && (n6 = n5 >= 1024 ? inputStream.read(byArray) : inputStream.read(byArray, 0, n5)) != -1; n5 -= n6) {
                                byteArrayOutputStream.write(byArray, 0, n6);
                            }
                            inputStream.close();
                            if (n5 == n3) {
                                this.updateNull(n2);
                                return;
                            }
                            this.updateBytes(n2, byteArrayOutputStream.toByteArray());
                            break block9;
                        }
                        catch (IOException iOException) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
                        }
                    }
                }
            }
            this.setRowBufferAt(n2, null);
        }
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader, int n3) throws SQLException {
        block10: {
            block11: {
                int n4 = 0;
                this.ensureOpen();
                if (this.isRowDeleted) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
                }
                OracleResultSetMetaData oracleResultSetMetaData = (OracleResultSetMetaData)this.getInternalMetadata();
                int n5 = oracleResultSetMetaData.getColumnType(n2);
                if (reader == null || n3 <= 0) break block11;
                switch (n5) {
                    case 2005: {
                        this.updateClob(n2, reader, (long)n3);
                        break block10;
                    }
                    case 2011: {
                        this.updateNClob(n2, reader, (long)n3);
                        break block10;
                    }
                    case -1: {
                        int[] nArray = new int[]{n3};
                        this.setRowBufferAt(n2, reader, nArray);
                        break block10;
                    }
                    default: {
                        try {
                            int n6;
                            char[] cArray = new char[1024];
                            StringBuilder stringBuilder = new StringBuilder(1024);
                            for (n6 = n3; n6 > 0 && (n4 = n6 >= 1024 ? reader.read(cArray) : reader.read(cArray, 0, n6)) != -1; n6 -= n4) {
                                stringBuilder.append(cArray, 0, n4);
                            }
                            reader.close();
                            if (n6 == n3) {
                                this.updateNull(n2);
                                return;
                            }
                            this.updateString(n2, stringBuilder.toString());
                            break block10;
                        }
                        catch (IOException iOException) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
                        }
                    }
                }
            }
            this.setRowBufferAt(n2, null);
        }
    }

    @Override
    public void updateObject(int n2, Object object, int n3) throws SQLException {
        this.updateObject(n2, object);
    }

    @Override
    public void updateObject(int n2, Object object, SQLType sQLType) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        int n3 = sQLType.getVendorTypeNumber();
        this.updateObject(n2, object, n3, false);
    }

    @Override
    public void updateObject(int n2, Object object, SQLType sQLType, int n3) throws SQLException {
        this.updateObject(n2, object, sQLType);
    }

    @Override
    public void updateObject(int n2, Object object) throws SQLException {
        this.updateObject(n2, object, 0, true);
    }

    private void updateObject(int n2, Object object, int n3, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82).fillInStackTrace();
            }
            if (object != null && object instanceof Reader) {
                this.updateCharacterStream(n2, (Reader)object);
            } else {
                Datum datum = null;
                if (object != null) {
                    Object object2;
                    if (object instanceof OracleData) {
                        object2 = ((OracleData)object).toJDBCObject(this.connection);
                        if (object2 instanceof _Proxy_) {
                            final _Proxy_ _Proxy_2 = (_Proxy_)object2;
                            object2 = AccessController.doPrivileged(new PrivilegedAction<Object>(){

                                @Override
                                public Object run() {
                                    return ProxyFactory.extractDelegate(_Proxy_2);
                                }
                            });
                        }
                        object = object2;
                    }
                    if (object instanceof Datum) {
                        datum = (Datum)object;
                    } else {
                        object2 = (OracleResultSetMetaData)this.getInternalMetadata();
                        int n4 = n2;
                        int n5 = bl ? ((OracleResultSetMetaData)object2).getColumnType(n4) : n3;
                        switch (n5) {
                            case -15: 
                            case -9: 
                            case -1: 
                            case 1: 
                            case 12: {
                                if (object instanceof byte[]) {
                                    byte[] byArray = (byte[])object;
                                    char[] cArray = new char[byArray.length * 3];
                                    int n6 = DBConversion.RAWBytesToHexChars(byArray, byArray.length, cArray);
                                    object = new String(cArray, 0, n6);
                                    break;
                                }
                                if (!(object instanceof Boolean)) break;
                                object = "" + ((Boolean)object != false ? 1 : 0);
                                break;
                            }
                            case 93: {
                                if (!(object instanceof Calendar)) break;
                                object = JavaToJavaConverter.convert(object, TIMESTAMP.class, this.connection, 0, null);
                                break;
                            }
                            case 91: {
                                if (!(object instanceof String)) break;
                                object = Date.valueOf((String)object);
                            }
                        }
                        datum = SQLUtil.makeOracleDatum(this.connection, object, n5, null, ((OracleResultSetMetaData)object2).isNCHAR(n4));
                    }
                }
                this.setRowBufferAt(n2, datum);
            }
        }
    }

    @Override
    public void updateOracleObject(int n2, Datum datum) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setRowBufferAt(n2, datum);
        }
    }

    @Override
    public void updateROWID(int n2, ROWID rOWID) throws SQLException {
        this.updateOracleObject(n2, (Datum)rOWID);
    }

    @Override
    public void updateNUMBER(int n2, NUMBER nUMBER) throws SQLException {
        this.updateOracleObject(n2, (Datum)nUMBER);
    }

    @Override
    public void updateDATE(int n2, DATE dATE) throws SQLException {
        this.updateOracleObject(n2, (Datum)dATE);
    }

    @Override
    public void updateINTERVALYM(int n2, INTERVALYM iNTERVALYM) throws SQLException {
        this.updateOracleObject(n2, (Datum)iNTERVALYM);
    }

    @Override
    public void updateINTERVALDS(int n2, INTERVALDS iNTERVALDS) throws SQLException {
        this.updateOracleObject(n2, (Datum)iNTERVALDS);
    }

    @Override
    public void updateTIMESTAMP(int n2, TIMESTAMP tIMESTAMP) throws SQLException {
        this.updateOracleObject(n2, (Datum)tIMESTAMP);
    }

    @Override
    public void updateTIMESTAMPTZ(int n2, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        this.updateOracleObject(n2, (Datum)tIMESTAMPTZ);
    }

    @Override
    public void updateTIMESTAMPLTZ(int n2, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        this.updateOracleObject(n2, (Datum)tIMESTAMPLTZ);
    }

    @Override
    public void updateARRAY(int n2, ARRAY aRRAY) throws SQLException {
        this.updateOracleObject(n2, (Datum)aRRAY);
    }

    @Override
    public void updateSTRUCT(int n2, STRUCT sTRUCT) throws SQLException {
        this.updateOracleObject(n2, (Datum)sTRUCT);
    }

    public void updateOPAQUE(int n2, OPAQUE oPAQUE) throws SQLException {
        this.updateOracleObject(n2, (Datum)oPAQUE);
    }

    @Override
    public void updateREF(int n2, REF rEF) throws SQLException {
        this.updateOracleObject(n2, (Datum)rEF);
    }

    @Override
    public void updateCHAR(int n2, CHAR cHAR) throws SQLException {
        this.updateOracleObject(n2, (Datum)cHAR);
    }

    @Override
    public void updateRAW(int n2, RAW rAW) throws SQLException {
        this.updateOracleObject(n2, (Datum)rAW);
    }

    @Override
    public void updateBLOB(int n2, BLOB bLOB) throws SQLException {
        this.updateOracleObject(n2, (Datum)bLOB);
    }

    @Override
    public void updateCLOB(int n2, CLOB cLOB) throws SQLException {
        this.updateOracleObject(n2, (Datum)cLOB);
    }

    @Override
    public void updateBFILE(int n2, BFILE bFILE) throws SQLException {
        this.updateOracleObject(n2, (Datum)bFILE);
    }

    @Override
    public void updateBfile(int n2, BFILE bFILE) throws SQLException {
        this.updateOracleObject(n2, (Datum)bFILE);
    }

    @Override
    public void updateCustomDatum(int n2, CustomDatum customDatum) throws SQLException {
        throw new Error("wanna do datum = ((CustomDatum) x).toDatum(m_comm)");
    }

    @Override
    public void updateORAData(int n2, ORAData oRAData) throws SQLException {
        Datum datum = oRAData.toDatum(this.connection);
        this.updateOracleObject(n2, datum);
    }

    @Override
    public void updateRef(int n2, Ref ref) throws SQLException {
        this.updateREF(n2, (REF)ref);
    }

    @Override
    public void updateBlob(int n2, Blob blob) throws SQLException {
        this.updateBLOB(n2, (BLOB)blob);
    }

    @Override
    public void updateClob(int n2, Clob clob) throws SQLException {
        this.updateCLOB(n2, (CLOB)clob);
    }

    @Override
    public void updateArray(int n2, Array array) throws SQLException {
        this.updateARRAY(n2, (ARRAY)array);
    }
}

