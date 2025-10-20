/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
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
abstract class GeneratedScrollableResultSet
extends OracleResultSet {
    protected OracleStatement statement;
    protected long currentRow;
    protected long fetchedRowCount;

    GeneratedScrollableResultSet(PhysicalConnection physicalConnection, OracleStatement oracleStatement) throws SQLException {
        super(physicalConnection);
        this.statement = oracleStatement;
        this.currentRow = -1L;
        this.fetchedRowCount = 0L;
        if (oracleStatement.sqlKind == OracleStatement.SqlKind.SELECT_FOR_UPDATE && physicalConnection.autocommit && physicalConnection.protocolId == 0) {
            this.needCommitAtClose = true;
        }
    }

    @Override
    public Array getArray(int n2) throws SQLException {
        return this.getARRAY(n2);
    }

    @Override
    public BigDecimal getBigDecimal(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getBigDecimal").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getBigDecimal").fillInStackTrace();
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
            BigDecimal bigDecimal = this.statement.getBigDecimal(this.currentRow, n2);
            return bigDecimal;
        }
    }

    @Override
    public BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getBigDecimal").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getBigDecimal").fillInStackTrace();
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
            BigDecimal bigDecimal = this.statement.getBigDecimal(this.currentRow, n2, n3);
            return bigDecimal;
        }
    }

    @Override
    public Blob getBlob(int n2) throws SQLException {
        return this.getBLOB(n2);
    }

    @Override
    public boolean getBoolean(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getBoolean").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getBoolean").fillInStackTrace();
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
            boolean bl = this.statement.getBoolean(this.currentRow, n2);
            return bl;
        }
    }

    @Override
    public byte getByte(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getByte").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getByte").fillInStackTrace();
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
            byte by = this.statement.getByte(this.currentRow, n2);
            return by;
        }
    }

    @Override
    public byte[] getBytes(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getBytes").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
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
            byte[] byArray = this.statement.getBytes(this.currentRow, n2);
            return byArray;
        }
    }

    @Override
    public Clob getClob(int n2) throws SQLException {
        return this.getCLOB(n2);
    }

    @Override
    public Date getDate(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getDate").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getDate").fillInStackTrace();
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
            Date date = this.statement.getDate(this.currentRow, n2);
            return date;
        }
    }

    @Override
    public Date getDate(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getDate").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getDate").fillInStackTrace();
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
            Date date = this.statement.getDate(this.currentRow, n2, calendar);
            return date;
        }
    }

    @Override
    public double getDouble(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getDouble").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getDouble").fillInStackTrace();
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
            double d2 = this.statement.getDouble(this.currentRow, n2);
            return d2;
        }
    }

    @Override
    public float getFloat(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getFloat").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getFloat").fillInStackTrace();
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
            float f2 = this.statement.getFloat(this.currentRow, n2);
            return f2;
        }
    }

    @Override
    public int getInt(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getInt").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getInt").fillInStackTrace();
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
            int n3 = this.statement.getInt(this.currentRow, n2);
            return n3;
        }
    }

    @Override
    public long getLong(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getLong").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getLong").fillInStackTrace();
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
            long l2 = this.statement.getLong(this.currentRow, n2);
            return l2;
        }
    }

    @Override
    public NClob getNClob(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getNClob").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getNClob").fillInStackTrace();
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
            NClob nClob = this.statement.getNClob(this.currentRow, n2);
            return nClob;
        }
    }

    @Override
    public String getNString(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getNString").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getNString").fillInStackTrace();
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
            String string = this.statement.getNString(this.currentRow, n2);
            return string;
        }
    }

    @Override
    public Object getObject(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getObject").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getObject").fillInStackTrace();
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
            Object object = this.statement.getObject(this.currentRow, n2);
            return object;
        }
    }

    @Override
    public Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getObject").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getObject").fillInStackTrace();
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
            Object object = this.statement.getObject(this.currentRow, n2, map);
            return object;
        }
    }

    @Override
    public Ref getRef(int n2) throws SQLException {
        return this.getREF(n2);
    }

    @Override
    public RowId getRowId(int n2) throws SQLException {
        return this.getROWID(n2);
    }

    @Override
    public short getShort(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getShort").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getShort").fillInStackTrace();
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
            short s2 = this.statement.getShort(this.currentRow, n2);
            return s2;
        }
    }

    @Override
    public SQLXML getSQLXML(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getSQLXML").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getSQLXML").fillInStackTrace();
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
            SQLXML sQLXML = this.statement.getSQLXML(this.currentRow, n2);
            return sQLXML;
        }
    }

    @Override
    public String getString(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getString").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getString").fillInStackTrace();
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
            String string = this.statement.getString(this.currentRow, n2);
            return string;
        }
    }

    @Override
    public Time getTime(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getTime").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getTime").fillInStackTrace();
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
            Time time = this.statement.getTime(this.currentRow, n2);
            return time;
        }
    }

    @Override
    public Time getTime(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getTime").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getTime").fillInStackTrace();
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
            Time time = this.statement.getTime(this.currentRow, n2, calendar);
            return time;
        }
    }

    @Override
    public Timestamp getTimestamp(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getTimestamp").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getTimestamp").fillInStackTrace();
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
            Timestamp timestamp = this.statement.getTimestamp(this.currentRow, n2);
            return timestamp;
        }
    }

    @Override
    public Timestamp getTimestamp(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getTimestamp").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getTimestamp").fillInStackTrace();
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
            Timestamp timestamp = this.statement.getTimestamp(this.currentRow, n2, calendar);
            return timestamp;
        }
    }

    @Override
    public URL getURL(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getURL").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getURL").fillInStackTrace();
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
            URL uRL = this.statement.getURL(this.currentRow, n2);
            return uRL;
        }
    }

    @Override
    public ARRAY getARRAY(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getARRAY").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getARRAY").fillInStackTrace();
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
            ARRAY aRRAY = this.statement.getARRAY(this.currentRow, n2);
            return aRRAY;
        }
    }

    @Override
    public BFILE getBFILE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getBFILE").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getBFILE").fillInStackTrace();
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
            BFILE bFILE = this.statement.getBFILE(this.currentRow, n2);
            return bFILE;
        }
    }

    @Override
    public BFILE getBfile(int n2) throws SQLException {
        return this.getBFILE(n2);
    }

    @Override
    public BLOB getBLOB(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getBLOB").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getBLOB").fillInStackTrace();
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
            BLOB bLOB = this.statement.getBLOB(this.currentRow, n2);
            return bLOB;
        }
    }

    @Override
    public CHAR getCHAR(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getCHAR").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getCHAR").fillInStackTrace();
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
            CHAR cHAR = this.statement.getCHAR(this.currentRow, n2);
            return cHAR;
        }
    }

    @Override
    public CLOB getCLOB(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getCLOB").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getCLOB").fillInStackTrace();
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
            CLOB cLOB = this.statement.getCLOB(this.currentRow, n2);
            return cLOB;
        }
    }

    @Override
    public ResultSet getCursor(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getCursor").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getCursor").fillInStackTrace();
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
            ResultSet resultSet = this.statement.getCursor(this.currentRow, n2);
            return resultSet;
        }
    }

    @Override
    public DATE getDATE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getDATE").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getDATE").fillInStackTrace();
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
            DATE dATE = this.statement.getDATE(this.currentRow, n2);
            return dATE;
        }
    }

    @Override
    public INTERVALDS getINTERVALDS(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getINTERVALDS").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getINTERVALDS").fillInStackTrace();
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
            INTERVALDS iNTERVALDS = this.statement.getINTERVALDS(this.currentRow, n2);
            return iNTERVALDS;
        }
    }

    @Override
    public INTERVALYM getINTERVALYM(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getINTERVALYM").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getINTERVALYM").fillInStackTrace();
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
            INTERVALYM iNTERVALYM = this.statement.getINTERVALYM(this.currentRow, n2);
            return iNTERVALYM;
        }
    }

    @Override
    public NUMBER getNUMBER(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getNUMBER").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getNUMBER").fillInStackTrace();
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
            NUMBER nUMBER = this.statement.getNUMBER(this.currentRow, n2);
            return nUMBER;
        }
    }

    @Override
    public OPAQUE getOPAQUE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getOPAQUE").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getOPAQUE").fillInStackTrace();
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
            OPAQUE oPAQUE = this.statement.getOPAQUE(this.currentRow, n2);
            return oPAQUE;
        }
    }

    @Override
    public Datum getOracleObject(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getOracleObject").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getOracleObject").fillInStackTrace();
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
            Datum datum = this.statement.getOracleObject(this.currentRow, n2);
            return datum;
        }
    }

    @Override
    public ORAData getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getORAData").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getORAData").fillInStackTrace();
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
            ORAData oRAData = this.statement.getORAData(this.currentRow, n2, oRADataFactory);
            return oRAData;
        }
    }

    @Override
    public Object getObject(int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getObject").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getObject").fillInStackTrace();
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
            Object object = this.statement.getObject(this.currentRow, n2, oracleDataFactory);
            return object;
        }
    }

    @Override
    public RAW getRAW(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getRAW").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getRAW").fillInStackTrace();
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
            RAW rAW = this.statement.getRAW(this.currentRow, n2);
            return rAW;
        }
    }

    @Override
    public REF getREF(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getREF").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getREF").fillInStackTrace();
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
            REF rEF = this.statement.getREF(this.currentRow, n2);
            return rEF;
        }
    }

    @Override
    public ROWID getROWID(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getROWID").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getROWID").fillInStackTrace();
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
            ROWID rOWID = this.statement.getROWID(this.currentRow, n2);
            return rOWID;
        }
    }

    @Override
    public STRUCT getSTRUCT(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getSTRUCT").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getSTRUCT").fillInStackTrace();
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
            STRUCT sTRUCT = this.statement.getSTRUCT(this.currentRow, n2);
            return sTRUCT;
        }
    }

    @Override
    public TIMESTAMPLTZ getTIMESTAMPLTZ(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getTIMESTAMPLTZ").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getTIMESTAMPLTZ").fillInStackTrace();
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
            TIMESTAMPLTZ tIMESTAMPLTZ = this.statement.getTIMESTAMPLTZ(this.currentRow, n2);
            return tIMESTAMPLTZ;
        }
    }

    @Override
    public TIMESTAMPTZ getTIMESTAMPTZ(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getTIMESTAMPTZ").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getTIMESTAMPTZ").fillInStackTrace();
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
            TIMESTAMPTZ tIMESTAMPTZ = this.statement.getTIMESTAMPTZ(this.currentRow, n2);
            return tIMESTAMPTZ;
        }
    }

    @Override
    public TIMESTAMP getTIMESTAMP(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getTIMESTAMP").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getTIMESTAMP").fillInStackTrace();
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
            TIMESTAMP tIMESTAMP = this.statement.getTIMESTAMP(this.currentRow, n2);
            return tIMESTAMP;
        }
    }

    @Override
    public CustomDatum getCustomDatum(int n2, CustomDatumFactory customDatumFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getCustomDatum").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getCustomDatum").fillInStackTrace();
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
            CustomDatum customDatum = this.statement.getCustomDatum(this.currentRow, n2, customDatumFactory);
            return customDatum;
        }
    }

    @Override
    public InputStream getAsciiStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getAsciiStream").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getAsciiStream").fillInStackTrace();
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
            InputStream inputStream = this.statement.getAsciiStream(this.currentRow, n2);
            return inputStream;
        }
    }

    @Override
    public InputStream getBinaryStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getBinaryStream").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getBinaryStream").fillInStackTrace();
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
            InputStream inputStream = this.statement.getBinaryStream(this.currentRow, n2);
            return inputStream;
        }
    }

    @Override
    public Reader getCharacterStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getCharacterStream").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getCharacterStream").fillInStackTrace();
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
            Reader reader = this.statement.getCharacterStream(this.currentRow, n2);
            return reader;
        }
    }

    @Override
    public Reader getNCharacterStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getNCharacterStream").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getNCharacterStream").fillInStackTrace();
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
            Reader reader = this.statement.getNCharacterStream(this.currentRow, n2);
            return reader;
        }
    }

    @Override
    public InputStream getUnicodeStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 10, "getUnicodeStream").fillInStackTrace();
            }
            if (this.connection.isClosedInternal()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, "getUnicodeStream").fillInStackTrace();
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
            InputStream inputStream = this.statement.getUnicodeStream(this.currentRow, n2);
            return inputStream;
        }
    }
}

