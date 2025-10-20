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
import oracle.jdbc.driver.JavaToJavaConverter;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OracleStatement;
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
import oracle.sql.CharacterSet;
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
abstract class GeneratedUpdatableResultSet
extends OracleResultSet {
    protected static final int MAX_CHA_BUFFER_SIZE = 1024;
    protected static final int MAX_BYTE_BUFFER_SIZE = 1024;
    OracleResultSet resultSet;
    boolean isRowDeleted = false;

    GeneratedUpdatableResultSet(OracleStatement oracleStatement, OracleResultSet oracleResultSet) throws SQLException {
        super(oracleStatement.connection);
        this.resultSet = oracleResultSet;
        if (oracleStatement.sqlKind == OracleStatement.SqlKind.SELECT_FOR_UPDATE && oracleStatement.connection.autocommit && oracleStatement.connection.protocolId == 0) {
            this.needCommitAtClose = true;
        }
    }

    abstract void ensureOpen() throws SQLException;

    abstract void setIsNull(NullStatus var1);

    abstract void setIsNull(boolean var1);

    abstract boolean isOnInsertRow();

    abstract boolean isUpdatingRow() throws SQLException;

    abstract boolean isRowBufferUpdatedAt(int var1) throws SQLException;

    abstract Updater<?> getUpdater(int var1) throws SQLException;

    abstract void setUpdater(int var1, Updater<?> var2) throws SQLException;

    @Override
    public Array getArray(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Array array;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getArray").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getArray").fillInStackTrace();
                }
                array = JavaToJavaConverter.convert(updater.getValue(), Array.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                array = this.resultSet.getArray(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = array;
            return updater;
        }
    }

    @Override
    public BigDecimal getBigDecimal(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            BigDecimal bigDecimal;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getBigDecimal").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getBigDecimal").fillInStackTrace();
                }
                bigDecimal = JavaToJavaConverter.convert(updater.getValue(), BigDecimal.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                bigDecimal = this.resultSet.getBigDecimal(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = bigDecimal;
            return updater;
        }
    }

    @Override
    public BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            BigDecimal bigDecimal;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getBigDecimal").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getBigDecimal").fillInStackTrace();
                }
                bigDecimal = JavaToJavaConverter.convert(updater.getValue(), BigDecimal.class, this.connection, updater.getExtra(), n3);
                this.setIsNull(updater.getValue() == null);
            } else {
                bigDecimal = this.resultSet.getBigDecimal(n2, n3);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = bigDecimal;
            return updater;
        }
    }

    @Override
    public Blob getBlob(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Blob blob;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getBlob").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getBlob").fillInStackTrace();
                }
                blob = JavaToJavaConverter.convert(updater.getValue(), Blob.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                blob = this.resultSet.getBlob(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = blob;
            return updater;
        }
    }

    @Override
    public boolean getBoolean(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Boolean bl;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getBoolean").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Updater<?> updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getBoolean").fillInStackTrace();
                }
                bl = JavaToJavaConverter.convert(updater.getValue(), Boolean.class, this.connection, updater.getExtra(), null);
                if (bl == null) {
                    bl = false;
                }
                this.setIsNull(updater.getValue() == null);
            } else {
                bl = this.resultSet.getBoolean(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            boolean bl2 = bl;
            return bl2;
        }
    }

    @Override
    public byte getByte(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Byte by;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getByte").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Updater<?> updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getByte").fillInStackTrace();
                }
                by = JavaToJavaConverter.convert(updater.getValue(), Byte.class, this.connection, updater.getExtra(), null);
                if (by == null) {
                    by = 0;
                }
                this.setIsNull(updater.getValue() == null);
            } else {
                by = this.resultSet.getByte(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            byte by2 = by;
            return by2;
        }
    }

    @Override
    public byte[] getBytes(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            byte[] byArray;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getBytes").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getBytes").fillInStackTrace();
                }
                byArray = JavaToJavaConverter.convert(updater.getValue(), byte[].class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                byArray = this.resultSet.getBytes(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = (Updater<?>)byArray;
            return updater;
        }
    }

    @Override
    public Clob getClob(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Clob clob;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getClob").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getClob").fillInStackTrace();
                }
                clob = JavaToJavaConverter.convert(updater.getValue(), Clob.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                clob = this.resultSet.getClob(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = clob;
            return updater;
        }
    }

    @Override
    public Date getDate(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Date date;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getDate").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getDate").fillInStackTrace();
                }
                date = JavaToJavaConverter.convert(updater.getValue(), Date.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                date = this.resultSet.getDate(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = date;
            return updater;
        }
    }

    @Override
    public Date getDate(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Date date;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getDate").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getDate").fillInStackTrace();
                }
                date = JavaToJavaConverter.convert(updater.getValue(), Date.class, this.connection, updater.getExtra(), calendar);
                this.setIsNull(updater.getValue() == null);
            } else {
                date = this.resultSet.getDate(n2, calendar);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = date;
            return updater;
        }
    }

    @Override
    public double getDouble(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Double d2;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getDouble").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Updater<?> updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getDouble").fillInStackTrace();
                }
                d2 = JavaToJavaConverter.convert(updater.getValue(), Double.class, this.connection, updater.getExtra(), null);
                if (d2 == null) {
                    d2 = 0.0;
                }
                this.setIsNull(updater.getValue() == null);
            } else {
                d2 = this.resultSet.getDouble(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            double d3 = d2;
            return d3;
        }
    }

    @Override
    public float getFloat(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Float f2;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getFloat").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Updater<?> updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getFloat").fillInStackTrace();
                }
                f2 = JavaToJavaConverter.convert(updater.getValue(), Float.class, this.connection, updater.getExtra(), null);
                if (f2 == null) {
                    f2 = Float.valueOf(0.0f);
                }
                this.setIsNull(updater.getValue() == null);
            } else {
                f2 = Float.valueOf(this.resultSet.getFloat(n2));
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            float f3 = f2.floatValue();
            return f3;
        }
    }

    @Override
    public int getInt(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Integer n3;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getInt").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Updater<?> updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getInt").fillInStackTrace();
                }
                n3 = JavaToJavaConverter.convert(updater.getValue(), Integer.class, this.connection, updater.getExtra(), null);
                if (n3 == null) {
                    n3 = 0;
                }
                this.setIsNull(updater.getValue() == null);
            } else {
                n3 = this.resultSet.getInt(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            int n4 = n3;
            return n4;
        }
    }

    @Override
    public long getLong(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Long l2;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getLong").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Updater<?> updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getLong").fillInStackTrace();
                }
                l2 = JavaToJavaConverter.convert(updater.getValue(), Long.class, this.connection, updater.getExtra(), null);
                if (l2 == null) {
                    l2 = 0L;
                }
                this.setIsNull(updater.getValue() == null);
            } else {
                l2 = this.resultSet.getLong(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            long l3 = l2;
            return l3;
        }
    }

    @Override
    public NClob getNClob(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            NClob nClob;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getNClob").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getNClob").fillInStackTrace();
                }
                nClob = JavaToJavaConverter.convert(updater.getValue(), NClob.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                nClob = this.resultSet.getNClob(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = nClob;
            return updater;
        }
    }

    @Override
    public String getNString(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            String string;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getNString").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getNString").fillInStackTrace();
                }
                string = JavaToJavaConverter.convert(updater.getValue(), String.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                string = this.resultSet.getNString(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = string;
            return updater;
        }
    }

    @Override
    public Object getObject(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getObject").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getObject").fillInStackTrace();
                }
                object = JavaToJavaConverter.convert(updater.getValue(), Object.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                object = this.resultSet.getObject(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = object;
            return updater;
        }
    }

    @Override
    public Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getObject").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getObject").fillInStackTrace();
                }
                object = JavaToJavaConverter.convert(updater.getValue(), Object.class, this.connection, updater.getExtra(), map);
                this.setIsNull(updater.getValue() == null);
            } else {
                object = this.resultSet.getObject(n2, map);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = object;
            return updater;
        }
    }

    @Override
    public Ref getRef(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Ref ref;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getRef").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getRef").fillInStackTrace();
                }
                ref = JavaToJavaConverter.convert(updater.getValue(), Ref.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                ref = this.resultSet.getRef(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = ref;
            return updater;
        }
    }

    @Override
    public RowId getRowId(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            RowId rowId;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getRowId").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getRowId").fillInStackTrace();
                }
                rowId = JavaToJavaConverter.convert(updater.getValue(), RowId.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                rowId = this.resultSet.getRowId(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = rowId;
            return updater;
        }
    }

    @Override
    public short getShort(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Short s2;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getShort").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                Updater<?> updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getShort").fillInStackTrace();
                }
                s2 = JavaToJavaConverter.convert(updater.getValue(), Short.class, this.connection, updater.getExtra(), null);
                if (s2 == null) {
                    s2 = 0;
                }
                this.setIsNull(updater.getValue() == null);
            } else {
                s2 = this.resultSet.getShort(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            short s3 = s2;
            return s3;
        }
    }

    @Override
    public SQLXML getSQLXML(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            SQLXML sQLXML;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getSQLXML").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getSQLXML").fillInStackTrace();
                }
                sQLXML = JavaToJavaConverter.convert(updater.getValue(), SQLXML.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                sQLXML = this.resultSet.getSQLXML(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = sQLXML;
            return updater;
        }
    }

    @Override
    public String getString(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            String string;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getString").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getString").fillInStackTrace();
                }
                string = JavaToJavaConverter.convert(updater.getValue(), String.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                string = this.resultSet.getString(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = string;
            return updater;
        }
    }

    @Override
    public Time getTime(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Time time;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getTime").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getTime").fillInStackTrace();
                }
                time = JavaToJavaConverter.convert(updater.getValue(), Time.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                time = this.resultSet.getTime(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = time;
            return updater;
        }
    }

    @Override
    public Time getTime(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Time time;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getTime").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getTime").fillInStackTrace();
                }
                time = JavaToJavaConverter.convert(updater.getValue(), Time.class, this.connection, updater.getExtra(), calendar);
                this.setIsNull(updater.getValue() == null);
            } else {
                time = this.resultSet.getTime(n2, calendar);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = time;
            return updater;
        }
    }

    @Override
    public Timestamp getTimestamp(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Timestamp timestamp;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getTimestamp").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getTimestamp").fillInStackTrace();
                }
                timestamp = JavaToJavaConverter.convert(updater.getValue(), Timestamp.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                timestamp = this.resultSet.getTimestamp(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = timestamp;
            return updater;
        }
    }

    @Override
    public Timestamp getTimestamp(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Timestamp timestamp;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getTimestamp").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getTimestamp").fillInStackTrace();
                }
                timestamp = JavaToJavaConverter.convert(updater.getValue(), Timestamp.class, this.connection, updater.getExtra(), calendar);
                this.setIsNull(updater.getValue() == null);
            } else {
                timestamp = this.resultSet.getTimestamp(n2, calendar);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = timestamp;
            return updater;
        }
    }

    @Override
    public URL getURL(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            URL uRL;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getURL").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getURL").fillInStackTrace();
                }
                uRL = JavaToJavaConverter.convert(updater.getValue(), URL.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                uRL = this.resultSet.getURL(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = uRL;
            return updater;
        }
    }

    @Override
    public ARRAY getARRAY(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            ARRAY aRRAY;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getARRAY").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getARRAY").fillInStackTrace();
                }
                aRRAY = JavaToJavaConverter.convert(updater.getValue(), ARRAY.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                aRRAY = this.resultSet.getARRAY(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = aRRAY;
            return updater;
        }
    }

    @Override
    public BFILE getBFILE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            BFILE bFILE;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getBFILE").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getBFILE").fillInStackTrace();
                }
                bFILE = JavaToJavaConverter.convert(updater.getValue(), BFILE.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                bFILE = this.resultSet.getBFILE(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = bFILE;
            return updater;
        }
    }

    @Override
    public BFILE getBfile(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            BFILE bFILE;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getBfile").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getBfile").fillInStackTrace();
                }
                bFILE = JavaToJavaConverter.convert(updater.getValue(), BFILE.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                bFILE = this.resultSet.getBfile(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = bFILE;
            return updater;
        }
    }

    @Override
    public BLOB getBLOB(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            BLOB bLOB;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getBLOB").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getBLOB").fillInStackTrace();
                }
                bLOB = JavaToJavaConverter.convert(updater.getValue(), BLOB.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                bLOB = this.resultSet.getBLOB(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = bLOB;
            return updater;
        }
    }

    @Override
    public CHAR getCHAR(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            CHAR cHAR;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getCHAR").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getCHAR").fillInStackTrace();
                }
                cHAR = JavaToJavaConverter.convert(updater.getValue(), CHAR.class, this.connection, updater.getExtra(), CharacterSet.make(this.connection.getJdbcCsId()));
                this.setIsNull(updater.getValue() == null);
            } else {
                cHAR = this.resultSet.getCHAR(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = cHAR;
            return updater;
        }
    }

    @Override
    public CLOB getCLOB(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            CLOB cLOB;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getCLOB").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getCLOB").fillInStackTrace();
                }
                cLOB = JavaToJavaConverter.convert(updater.getValue(), CLOB.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                cLOB = this.resultSet.getCLOB(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = cLOB;
            return updater;
        }
    }

    @Override
    public ResultSet getCursor(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            ResultSet resultSet;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getCursor").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getCursor").fillInStackTrace();
                }
                resultSet = JavaToJavaConverter.convert(updater.getValue(), ResultSet.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                resultSet = this.resultSet.getCursor(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = resultSet;
            return updater;
        }
    }

    @Override
    public DATE getDATE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            DATE dATE;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getDATE").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getDATE").fillInStackTrace();
                }
                dATE = JavaToJavaConverter.convert(updater.getValue(), DATE.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                dATE = this.resultSet.getDATE(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = dATE;
            return updater;
        }
    }

    @Override
    public INTERVALDS getINTERVALDS(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            INTERVALDS iNTERVALDS;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getINTERVALDS").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getINTERVALDS").fillInStackTrace();
                }
                iNTERVALDS = JavaToJavaConverter.convert(updater.getValue(), INTERVALDS.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                iNTERVALDS = this.resultSet.getINTERVALDS(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = iNTERVALDS;
            return updater;
        }
    }

    @Override
    public INTERVALYM getINTERVALYM(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            INTERVALYM iNTERVALYM;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getINTERVALYM").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getINTERVALYM").fillInStackTrace();
                }
                iNTERVALYM = JavaToJavaConverter.convert(updater.getValue(), INTERVALYM.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                iNTERVALYM = this.resultSet.getINTERVALYM(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = iNTERVALYM;
            return updater;
        }
    }

    @Override
    public NUMBER getNUMBER(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            NUMBER nUMBER;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getNUMBER").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getNUMBER").fillInStackTrace();
                }
                nUMBER = JavaToJavaConverter.convert(updater.getValue(), NUMBER.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                nUMBER = this.resultSet.getNUMBER(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = nUMBER;
            return updater;
        }
    }

    @Override
    public OPAQUE getOPAQUE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            OPAQUE oPAQUE;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getOPAQUE").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getOPAQUE").fillInStackTrace();
                }
                oPAQUE = JavaToJavaConverter.convert(updater.getValue(), OPAQUE.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                oPAQUE = this.resultSet.getOPAQUE(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = oPAQUE;
            return updater;
        }
    }

    @Override
    public Datum getOracleObject(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum datum;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getOracleObject").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getOracleObject").fillInStackTrace();
                }
                datum = JavaToJavaConverter.convert(updater.getValue(), Datum.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                datum = this.resultSet.getOracleObject(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = datum;
            return updater;
        }
    }

    @Override
    public ORAData getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            ORAData oRAData;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getORAData").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getORAData").fillInStackTrace();
                }
                oRAData = JavaToJavaConverter.convert(updater.getValue(), ORAData.class, this.connection, updater.getExtra(), oRADataFactory);
                this.setIsNull(updater.getValue() == null);
            } else {
                oRAData = this.resultSet.getORAData(n2, oRADataFactory);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = oRAData;
            return updater;
        }
    }

    @Override
    public Object getObject(int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getObject").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getObject").fillInStackTrace();
                }
                object = JavaToJavaConverter.convert(updater.getValue(), Object.class, this.connection, updater.getExtra(), oracleDataFactory);
                this.setIsNull(updater.getValue() == null);
            } else {
                object = this.resultSet.getObject(n2, oracleDataFactory);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = object;
            return updater;
        }
    }

    @Override
    public RAW getRAW(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            RAW rAW;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getRAW").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getRAW").fillInStackTrace();
                }
                rAW = JavaToJavaConverter.convert(updater.getValue(), RAW.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                rAW = this.resultSet.getRAW(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = rAW;
            return updater;
        }
    }

    @Override
    public REF getREF(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            REF rEF;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getREF").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getREF").fillInStackTrace();
                }
                rEF = JavaToJavaConverter.convert(updater.getValue(), REF.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                rEF = this.resultSet.getREF(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = rEF;
            return updater;
        }
    }

    @Override
    public ROWID getROWID(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            ROWID rOWID;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getROWID").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getROWID").fillInStackTrace();
                }
                rOWID = JavaToJavaConverter.convert(updater.getValue(), ROWID.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                rOWID = this.resultSet.getROWID(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = rOWID;
            return updater;
        }
    }

    @Override
    public STRUCT getSTRUCT(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            STRUCT sTRUCT;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getSTRUCT").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getSTRUCT").fillInStackTrace();
                }
                sTRUCT = JavaToJavaConverter.convert(updater.getValue(), STRUCT.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                sTRUCT = this.resultSet.getSTRUCT(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = sTRUCT;
            return updater;
        }
    }

    @Override
    public TIMESTAMPLTZ getTIMESTAMPLTZ(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            TIMESTAMPLTZ tIMESTAMPLTZ;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getTIMESTAMPLTZ").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getTIMESTAMPLTZ").fillInStackTrace();
                }
                tIMESTAMPLTZ = JavaToJavaConverter.convert(updater.getValue(), TIMESTAMPLTZ.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                tIMESTAMPLTZ = this.resultSet.getTIMESTAMPLTZ(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = tIMESTAMPLTZ;
            return updater;
        }
    }

    @Override
    public TIMESTAMPTZ getTIMESTAMPTZ(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            TIMESTAMPTZ tIMESTAMPTZ;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getTIMESTAMPTZ").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getTIMESTAMPTZ").fillInStackTrace();
                }
                tIMESTAMPTZ = JavaToJavaConverter.convert(updater.getValue(), TIMESTAMPTZ.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                tIMESTAMPTZ = this.resultSet.getTIMESTAMPTZ(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = tIMESTAMPTZ;
            return updater;
        }
    }

    @Override
    public TIMESTAMP getTIMESTAMP(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            TIMESTAMP tIMESTAMP;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getTIMESTAMP").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getTIMESTAMP").fillInStackTrace();
                }
                tIMESTAMP = JavaToJavaConverter.convert(updater.getValue(), TIMESTAMP.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                tIMESTAMP = this.resultSet.getTIMESTAMP(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = tIMESTAMP;
            return updater;
        }
    }

    @Override
    public CustomDatum getCustomDatum(int n2, CustomDatumFactory customDatumFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            CustomDatum customDatum;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getCustomDatum").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getCustomDatum").fillInStackTrace();
                }
                customDatum = JavaToJavaConverter.convert(updater.getValue(), CustomDatum.class, this.connection, updater.getExtra(), customDatumFactory);
                if (customDatum == null) {
                    customDatum = null;
                }
                this.setIsNull(updater.getValue() == null);
            } else {
                customDatum = this.resultSet.getCustomDatum(n2, customDatumFactory);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = customDatum;
            return updater;
        }
    }

    @Override
    public InputStream getAsciiStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            InputStream inputStream;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getAsciiStream").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getAsciiStream").fillInStackTrace();
                }
                inputStream = JavaToJavaConverter.convert(updater.getValue(), InputStream.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                inputStream = this.resultSet.getAsciiStream(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = inputStream;
            return updater;
        }
    }

    @Override
    public InputStream getBinaryStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            InputStream inputStream;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getBinaryStream").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getBinaryStream").fillInStackTrace();
                }
                inputStream = JavaToJavaConverter.convert(updater.getValue(), InputStream.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                inputStream = this.resultSet.getBinaryStream(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = inputStream;
            return updater;
        }
    }

    @Override
    public Reader getCharacterStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Reader reader;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getCharacterStream").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getCharacterStream").fillInStackTrace();
                }
                reader = JavaToJavaConverter.convert(updater.getValue(), Reader.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                reader = this.resultSet.getCharacterStream(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = reader;
            return updater;
        }
    }

    @Override
    public Reader getNCharacterStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Reader reader;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getNCharacterStream").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getNCharacterStream").fillInStackTrace();
                }
                reader = JavaToJavaConverter.convert(updater.getValue(), Reader.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                reader = this.resultSet.getNCharacterStream(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = reader;
            return updater;
        }
    }

    @Override
    public InputStream getUnicodeStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            InputStream inputStream;
            Updater<?> updater;
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "getUnicodeStream").fillInStackTrace();
            }
            if (this.isOnInsertRow() || this.isUpdatingRow() && this.isRowBufferUpdatedAt(n2)) {
                updater = this.getUpdater(n2);
                if (updater == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "getUnicodeStream").fillInStackTrace();
                }
                inputStream = JavaToJavaConverter.convert(updater.getValue(), InputStream.class, this.connection, updater.getExtra(), null);
                this.setIsNull(updater.getValue() == null);
            } else {
                inputStream = this.resultSet.getUnicodeStream(n2);
                this.setIsNull(NullStatus.VALUE_IN_RSET);
            }
            updater = inputStream;
            return updater;
        }
    }

    @Override
    public void updateArray(int n2, Array array) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateArray").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateArray").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Array>(array){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setArray(n2, (Array)this.value);
                }
            });
        }
    }

    @Override
    public void updateBigDecimal(int n2, BigDecimal bigDecimal) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBigDecimal").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBigDecimal").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<BigDecimal>(bigDecimal){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBigDecimal(n2, (BigDecimal)this.value);
                }
            });
        }
    }

    @Override
    public void updateBlob(int n2, Blob blob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBlob").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBlob").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Blob>(blob){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBlob(n2, (Blob)this.value);
                }
            });
        }
    }

    @Override
    public void updateBoolean(int n2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBoolean").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBoolean").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Boolean>(Boolean.valueOf(bl)){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBoolean(n2, (Boolean)this.value);
                }
            });
        }
    }

    @Override
    public void updateByte(int n2, byte by) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateByte").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateByte").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Byte>(Byte.valueOf(by)){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setByte(n2, (Byte)this.value);
                }
            });
        }
    }

    @Override
    public void updateBytes(int n2, byte[] byArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBytes").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBytes").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<byte[]>(byArray){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBytes(n2, (byte[])this.value);
                }
            });
        }
    }

    @Override
    public void updateClob(int n2, Clob clob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateClob").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateClob").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Clob>(clob){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setClob(n2, (Clob)this.value);
                }
            });
        }
    }

    @Override
    public void updateDate(int n2, Date date) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateDate").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateDate").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Date>(date){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setDate(n2, (Date)this.value);
                }
            });
        }
    }

    @Override
    public void updateDate(int n2, Date date, final Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateDate").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateDate").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Date>(date){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setDate(n2, (Date)this.value, calendar);
                }

                @Override
                public Object getExtra() {
                    return calendar;
                }
            });
        }
    }

    @Override
    public void updateDouble(int n2, double d2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateDouble").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateDouble").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Double>(Double.valueOf(d2)){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setDouble(n2, (Double)this.value);
                }
            });
        }
    }

    @Override
    public void updateFloat(int n2, float f2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateFloat").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateFloat").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Float>(Float.valueOf(f2)){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setFloat(n2, ((Float)this.value).floatValue());
                }
            });
        }
    }

    @Override
    public void updateInt(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateInt").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateInt").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Integer>(Integer.valueOf(n3)){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setInt(n2, (Integer)this.value);
                }
            });
        }
    }

    @Override
    public void updateLong(int n2, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateLong").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateLong").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Long>(Long.valueOf(l2)){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setLong(n2, (Long)this.value);
                }
            });
        }
    }

    @Override
    public void updateNClob(int n2, NClob nClob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateNClob").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateNClob").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<NClob>(nClob){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setNClob(n2, (NClob)this.value);
                }
            });
        }
    }

    @Override
    public void updateNString(int n2, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateNString").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateNString").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<String>(string){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setNString(n2, (String)this.value);
                }
            });
        }
    }

    @Override
    public void updateObject(int n2, Object object) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateObject").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateObject").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Object>(object){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setObject(n2, this.value);
                }
            });
        }
    }

    @Override
    public void updateObject(int n2, Object object, final int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateObject").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateObject").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Object>(object){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setObject(n2, this.value, n3);
                }

                @Override
                public Object getExtra() {
                    return n3;
                }
            });
        }
    }

    @Override
    public void updateRef(int n2, Ref ref) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateRef").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateRef").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Ref>(ref){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setRef(n2, (Ref)this.value);
                }
            });
        }
    }

    @Override
    public void updateRowId(int n2, RowId rowId) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateRowId").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateRowId").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<RowId>(rowId){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setRowId(n2, (RowId)this.value);
                }
            });
        }
    }

    @Override
    public void updateShort(int n2, short s2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateShort").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateShort").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Short>(Short.valueOf(s2)){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setShort(n2, (Short)this.value);
                }
            });
        }
    }

    @Override
    public void updateSQLXML(int n2, SQLXML sQLXML) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateSQLXML").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateSQLXML").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<SQLXML>(sQLXML){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setSQLXML(n2, (SQLXML)this.value);
                }
            });
        }
    }

    @Override
    public void updateString(int n2, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateString").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateString").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<String>(string){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setString(n2, (String)this.value);
                }
            });
        }
    }

    @Override
    public void updateTime(int n2, Time time) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateTime").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateTime").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Time>(time){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setTime(n2, (Time)this.value);
                }
            });
        }
    }

    @Override
    public void updateTime(int n2, Time time, final Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateTime").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateTime").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Time>(time){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setTime(n2, (Time)this.value, calendar);
                }

                @Override
                public Object getExtra() {
                    return calendar;
                }
            });
        }
    }

    @Override
    public void updateTimestamp(int n2, Timestamp timestamp) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateTimestamp").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateTimestamp").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Timestamp>(timestamp){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setTimestamp(n2, (Timestamp)this.value);
                }
            });
        }
    }

    @Override
    public void updateTimestamp(int n2, Timestamp timestamp, final Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateTimestamp").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateTimestamp").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Timestamp>(timestamp){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setTimestamp(n2, (Timestamp)this.value, calendar);
                }

                @Override
                public Object getExtra() {
                    return calendar;
                }
            });
        }
    }

    @Override
    public void updateURL(int n2, URL uRL) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateURL").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateURL").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<URL>(uRL){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setURL(n2, (URL)this.value);
                }
            });
        }
    }

    @Override
    public void updateARRAY(int n2, ARRAY aRRAY) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateARRAY").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateARRAY").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<ARRAY>(aRRAY){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setARRAY(n2, (ARRAY)this.value);
                }
            });
        }
    }

    @Override
    public void updateBFILE(int n2, BFILE bFILE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBFILE").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBFILE").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<BFILE>(bFILE){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBFILE(n2, (BFILE)this.value);
                }
            });
        }
    }

    @Override
    public void updateBfile(int n2, BFILE bFILE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBfile").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBfile").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<BFILE>(bFILE){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBfile(n2, (BFILE)this.value);
                }
            });
        }
    }

    @Override
    public void updateBLOB(int n2, BLOB bLOB) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBLOB").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBLOB").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<BLOB>(bLOB){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBLOB(n2, (BLOB)this.value);
                }
            });
        }
    }

    @Override
    public void updateCHAR(int n2, CHAR cHAR) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateCHAR").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateCHAR").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<CHAR>(cHAR){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setCHAR(n2, (CHAR)this.value);
                }
            });
        }
    }

    @Override
    public void updateCLOB(int n2, CLOB cLOB) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateCLOB").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateCLOB").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<CLOB>(cLOB){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setCLOB(n2, (CLOB)this.value);
                }
            });
        }
    }

    @Override
    public void updateDATE(int n2, DATE dATE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateDATE").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateDATE").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<DATE>(dATE){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setDATE(n2, (DATE)this.value);
                }
            });
        }
    }

    @Override
    public void updateINTERVALDS(int n2, INTERVALDS iNTERVALDS) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateINTERVALDS").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateINTERVALDS").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<INTERVALDS>(iNTERVALDS){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setINTERVALDS(n2, (INTERVALDS)this.value);
                }
            });
        }
    }

    @Override
    public void updateINTERVALYM(int n2, INTERVALYM iNTERVALYM) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateINTERVALYM").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateINTERVALYM").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<INTERVALYM>(iNTERVALYM){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setINTERVALYM(n2, (INTERVALYM)this.value);
                }
            });
        }
    }

    @Override
    public void updateNUMBER(int n2, NUMBER nUMBER) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateNUMBER").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateNUMBER").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<NUMBER>(nUMBER){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setNUMBER(n2, (NUMBER)this.value);
                }
            });
        }
    }

    @Override
    public void updateOracleObject(int n2, Datum datum) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateOracleObject").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateOracleObject").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Datum>(datum){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setOracleObject(n2, (Datum)this.value);
                }
            });
        }
    }

    @Override
    public void updateORAData(int n2, ORAData oRAData) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateORAData").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateORAData").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<ORAData>(oRAData){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setORAData(n2, (ORAData)this.value);
                }
            });
        }
    }

    @Override
    public void updateRAW(int n2, RAW rAW) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateRAW").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateRAW").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<RAW>(rAW){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setRAW(n2, (RAW)this.value);
                }
            });
        }
    }

    @Override
    public void updateREF(int n2, REF rEF) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateREF").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateREF").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<REF>(rEF){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setREF(n2, (REF)this.value);
                }
            });
        }
    }

    @Override
    public void updateROWID(int n2, ROWID rOWID) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateROWID").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateROWID").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<ROWID>(rOWID){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setROWID(n2, (ROWID)this.value);
                }
            });
        }
    }

    @Override
    public void updateSTRUCT(int n2, STRUCT sTRUCT) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateSTRUCT").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateSTRUCT").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<STRUCT>(sTRUCT){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setSTRUCT(n2, (STRUCT)this.value);
                }
            });
        }
    }

    @Override
    public void updateTIMESTAMPLTZ(int n2, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateTIMESTAMPLTZ").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateTIMESTAMPLTZ").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<TIMESTAMPLTZ>(tIMESTAMPLTZ){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setTIMESTAMPLTZ(n2, (TIMESTAMPLTZ)this.value);
                }
            });
        }
    }

    @Override
    public void updateTIMESTAMPTZ(int n2, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateTIMESTAMPTZ").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateTIMESTAMPTZ").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<TIMESTAMPTZ>(tIMESTAMPTZ){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setTIMESTAMPTZ(n2, (TIMESTAMPTZ)this.value);
                }
            });
        }
    }

    @Override
    public void updateTIMESTAMP(int n2, TIMESTAMP tIMESTAMP) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateTIMESTAMP").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateTIMESTAMP").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<TIMESTAMP>(tIMESTAMP){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setTIMESTAMP(n2, (TIMESTAMP)this.value);
                }
            });
        }
    }

    @Override
    public void updateCustomDatum(int n2, CustomDatum customDatum) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateCustomDatum").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateCustomDatum").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<CustomDatum>(customDatum){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setCustomDatum(n2, (CustomDatum)this.value);
                }
            });
        }
    }

    @Override
    public void updateBlob(int n2, InputStream inputStream) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBlob").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBlob").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<InputStream>(inputStream){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBlob(n2, (InputStream)this.value);
                }
            });
        }
    }

    @Override
    public void updateBlob(int n2, InputStream inputStream, final long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBlob").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBlob").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<InputStream>(inputStream){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBlob(n2, (InputStream)this.value, l2);
                }

                @Override
                public Object getExtra() {
                    return l2;
                }
            });
        }
    }

    @Override
    public void updateClob(int n2, Reader reader) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateClob").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateClob").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Reader>(reader){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setClob(n2, (Reader)this.value);
                }
            });
        }
    }

    @Override
    public void updateClob(int n2, Reader reader, final long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateClob").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateClob").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Reader>(reader){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setClob(n2, (Reader)this.value, l2);
                }

                @Override
                public Object getExtra() {
                    return l2;
                }
            });
        }
    }

    @Override
    public void updateNClob(int n2, Reader reader) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateNClob").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateNClob").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Reader>(reader){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setNClob(n2, (Reader)this.value);
                }
            });
        }
    }

    @Override
    public void updateNClob(int n2, Reader reader, final long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateNClob").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateNClob").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Reader>(reader){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setNClob(n2, (Reader)this.value, l2);
                }

                @Override
                public Object getExtra() {
                    return l2;
                }
            });
        }
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateAsciiStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateAsciiStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<InputStream>(inputStream){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setAsciiStream(n2, (InputStream)this.value);
                }
            });
        }
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream, final int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateAsciiStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateAsciiStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<InputStream>(inputStream){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setAsciiStream(n2, (InputStream)this.value, n3);
                }

                @Override
                public Object getExtra() {
                    return n3;
                }
            });
        }
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream, final long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateAsciiStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateAsciiStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<InputStream>(inputStream){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setAsciiStream(n2, (InputStream)this.value, l2);
                }

                @Override
                public Object getExtra() {
                    return l2;
                }
            });
        }
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBinaryStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBinaryStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<InputStream>(inputStream){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBinaryStream(n2, (InputStream)this.value);
                }
            });
        }
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream, final int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBinaryStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBinaryStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<InputStream>(inputStream){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBinaryStream(n2, (InputStream)this.value, n3);
                }

                @Override
                public Object getExtra() {
                    return n3;
                }
            });
        }
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream, final long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateBinaryStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateBinaryStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<InputStream>(inputStream){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setBinaryStream(n2, (InputStream)this.value, l2);
                }

                @Override
                public Object getExtra() {
                    return l2;
                }
            });
        }
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateCharacterStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateCharacterStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Reader>(reader){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setCharacterStream(n2, (Reader)this.value);
                }
            });
        }
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader, final int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateCharacterStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateCharacterStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Reader>(reader){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setCharacterStream(n2, (Reader)this.value, n3);
                }

                @Override
                public Object getExtra() {
                    return n3;
                }
            });
        }
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader, final long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateCharacterStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateCharacterStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Reader>(reader){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setCharacterStream(n2, (Reader)this.value, l2);
                }

                @Override
                public Object getExtra() {
                    return l2;
                }
            });
        }
    }

    @Override
    public void updateNCharacterStream(int n2, Reader reader) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateNCharacterStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateNCharacterStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Reader>(reader){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setNCharacterStream(n2, (Reader)this.value);
                }
            });
        }
    }

    @Override
    public void updateNCharacterStream(int n2, Reader reader, final long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateNCharacterStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateNCharacterStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<Reader>(reader){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setNCharacterStream(n2, (Reader)this.value, l2);
                }

                @Override
                public Object getExtra() {
                    return l2;
                }
            });
        }
    }

    @Override
    public void updateUnicodeStream(int n2, InputStream inputStream, final int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.isRowDeleted) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 82, "updateUnicodeStream").fillInStackTrace();
            }
            if (n2 < 1 || n2 > this.getColumnCount()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, "updateUnicodeStream").fillInStackTrace();
            }
            this.setUpdater(n2, new Updater<InputStream>(inputStream){

                @Override
                public void set(OraclePreparedStatement oraclePreparedStatement, int n2) throws SQLException {
                    oraclePreparedStatement.setUnicodeStream(n2, (InputStream)this.value, n3);
                }

                @Override
                public Object getExtra() {
                    return n3;
                }
            });
        }
    }

    protected static abstract class Updater<T> {
        protected T value;

        Updater(T t2) {
            this.value = t2;
        }

        abstract void set(OraclePreparedStatement var1, int var2) throws SQLException;

        Object getExtra() {
            return null;
        }

        final T getValue() throws SQLException {
            return this.value;
        }

        final boolean isNull() {
            return this.value == null;
        }
    }

    static enum NullStatus {
        VALUE_UNKNOWN,
        VALUE_IN_RSET,
        VALUE_NULL,
        VALUE_NOT_NULL;

    }
}

