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
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OracleParameterMetaData;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatementWrapper;
import oracle.jdbc.driver.Wrappable;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
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
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleClosedStatement
implements OracleCallableStatement,
Wrappable<OracleStatementWrapper> {
    private final int errorCode;
    Object acProxy;

    OracleClosedStatement(int n2) {
        this.errorCode = n2;
    }

    @Override
    public void setWrapper(OracleStatementWrapper oracleStatementWrapper) {
    }

    @Override
    public void setArray(int n2, Array array) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setArrayAtName(String string, Array array) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setArray(String string, Array array) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBigDecimal(int n2, BigDecimal bigDecimal) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBigDecimalAtName(String string, BigDecimal bigDecimal) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBigDecimal(String string, BigDecimal bigDecimal) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBlob(int n2, Blob blob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBlobAtName(String string, Blob blob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBlob(String string, Blob blob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBoolean(int n2, boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBooleanAtName(String string, boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBoolean(String string, boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setByte(int n2, byte by) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setByteAtName(String string, byte by) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setByte(String string, byte by) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBytes(int n2, byte[] byArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBytesAtName(String string, byte[] byArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBytes(String string, byte[] byArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setClob(int n2, Clob clob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setClobAtName(String string, Clob clob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setClob(String string, Clob clob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDate(int n2, Date date) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDateAtName(String string, Date date) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDate(String string, Date date) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDate(int n2, Date date, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDateAtName(String string, Date date, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDate(String string, Date date, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDouble(int n2, double d2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDoubleAtName(String string, double d2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDouble(String string, double d2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setFloat(int n2, float f2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setFloatAtName(String string, float f2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setFloat(String string, float f2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setInt(int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setIntAtName(String string, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setInt(String string, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setLong(int n2, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setLongAtName(String string, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setLong(String string, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNClob(int n2, NClob nClob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNClobAtName(String string, NClob nClob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNClob(String string, NClob nClob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNString(int n2, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNStringAtName(String string, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNString(String string, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObject(int n2, Object object) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObjectAtName(String string, Object object) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObject(String string, Object object) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObject(int n2, Object object, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObjectAtName(String string, Object object, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObject(String string, Object object, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRef(int n2, Ref ref) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRefAtName(String string, Ref ref) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRef(String string, Ref ref) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRowId(int n2, RowId rowId) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRowIdAtName(String string, RowId rowId) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRowId(String string, RowId rowId) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setShort(int n2, short s2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setShortAtName(String string, short s2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setShort(String string, short s2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setSQLXML(int n2, SQLXML sQLXML) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setSQLXMLAtName(String string, SQLXML sQLXML) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setSQLXML(String string, SQLXML sQLXML) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setString(int n2, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setStringAtName(String string, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setString(String string, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTime(int n2, Time time) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTimeAtName(String string, Time time) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTime(String string, Time time) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTime(int n2, Time time, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTimeAtName(String string, Time time, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTime(String string, Time time, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTimestamp(int n2, Timestamp timestamp) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTimestampAtName(String string, Timestamp timestamp) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTimestamp(String string, Timestamp timestamp) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTimestamp(int n2, Timestamp timestamp, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTimestampAtName(String string, Timestamp timestamp, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTimestamp(String string, Timestamp timestamp, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setURL(int n2, URL uRL) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setURLAtName(String string, URL uRL) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setURL(String string, URL uRL) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setARRAY(int n2, ARRAY aRRAY) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setARRAYAtName(String string, ARRAY aRRAY) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setARRAY(String string, ARRAY aRRAY) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBFILE(int n2, BFILE bFILE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBFILEAtName(String string, BFILE bFILE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBFILE(String string, BFILE bFILE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBfile(int n2, BFILE bFILE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBfileAtName(String string, BFILE bFILE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBfile(String string, BFILE bFILE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryFloat(int n2, float f2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryFloatAtName(String string, float f2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryFloat(String string, float f2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryFloat(int n2, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryFloatAtName(String string, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryFloat(String string, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryDouble(int n2, double d2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryDoubleAtName(String string, double d2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryDouble(String string, double d2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryDouble(int n2, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryDoubleAtName(String string, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryDouble(String string, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBLOB(int n2, BLOB bLOB) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBLOBAtName(String string, BLOB bLOB) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBLOB(String string, BLOB bLOB) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCHAR(int n2, CHAR cHAR) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCHARAtName(String string, CHAR cHAR) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCHAR(String string, CHAR cHAR) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCLOB(int n2, CLOB cLOB) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCLOBAtName(String string, CLOB cLOB) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCLOB(String string, CLOB cLOB) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCursor(int n2, ResultSet resultSet) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCursorAtName(String string, ResultSet resultSet) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCursor(String string, ResultSet resultSet) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDATE(int n2, DATE dATE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDATEAtName(String string, DATE dATE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDATE(String string, DATE dATE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setFixedCHAR(int n2, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setFixedCHARAtName(String string, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setFixedCHAR(String string, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setINTERVALDS(int n2, INTERVALDS iNTERVALDS) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setINTERVALDSAtName(String string, INTERVALDS iNTERVALDS) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setINTERVALDS(String string, INTERVALDS iNTERVALDS) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setINTERVALYM(int n2, INTERVALYM iNTERVALYM) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setINTERVALYMAtName(String string, INTERVALYM iNTERVALYM) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setINTERVALYM(String string, INTERVALYM iNTERVALYM) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNUMBER(int n2, NUMBER nUMBER) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNUMBERAtName(String string, NUMBER nUMBER) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNUMBER(String string, NUMBER nUMBER) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setOPAQUE(int n2, OPAQUE oPAQUE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setOPAQUEAtName(String string, OPAQUE oPAQUE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setOPAQUE(String string, OPAQUE oPAQUE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setOracleObject(int n2, Datum datum) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setOracleObjectAtName(String string, Datum datum) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setOracleObject(String string, Datum datum) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setORAData(int n2, ORAData oRAData) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setORADataAtName(String string, ORAData oRAData) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setORAData(String string, ORAData oRAData) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRAW(int n2, RAW rAW) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRAWAtName(String string, RAW rAW) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRAW(String string, RAW rAW) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setREF(int n2, REF rEF) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setREFAtName(String string, REF rEF) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setREF(String string, REF rEF) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRefType(int n2, REF rEF) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRefTypeAtName(String string, REF rEF) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setRefType(String string, REF rEF) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setROWID(int n2, ROWID rOWID) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setROWIDAtName(String string, ROWID rOWID) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setROWID(String string, ROWID rOWID) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setSTRUCT(int n2, STRUCT sTRUCT) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setSTRUCTAtName(String string, STRUCT sTRUCT) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setSTRUCT(String string, STRUCT sTRUCT) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTIMESTAMPLTZ(int n2, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTIMESTAMPLTZAtName(String string, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTIMESTAMPLTZ(String string, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTIMESTAMPTZ(int n2, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTIMESTAMPTZAtName(String string, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTIMESTAMPTZ(String string, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTIMESTAMP(int n2, TIMESTAMP tIMESTAMP) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTIMESTAMPAtName(String string, TIMESTAMP tIMESTAMP) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setTIMESTAMP(String string, TIMESTAMP tIMESTAMP) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCustomDatum(int n2, CustomDatum customDatum) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCustomDatumAtName(String string, CustomDatum customDatum) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCustomDatum(String string, CustomDatum customDatum) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBlob(int n2, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBlobAtName(String string, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBlob(String string, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBlob(int n2, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBlobAtName(String string, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBlob(String string, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setClob(int n2, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setClobAtName(String string, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setClob(String string, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setClob(int n2, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setClobAtName(String string, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setClob(String string, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNClob(int n2, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNClobAtName(String string, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNClob(String string, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNClob(int n2, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNClobAtName(String string, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNClob(String string, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setAsciiStream(String string, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream inputStream, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setAsciiStream(String string, InputStream inputStream, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setAsciiStream(String string, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryStream(String string, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream inputStream, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryStream(String string, InputStream inputStream, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBinaryStream(String string, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCharacterStream(int n2, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCharacterStream(String string, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCharacterStream(int n2, Reader reader, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCharacterStream(String string, Reader reader, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCharacterStream(String string, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNCharacterStream(int n2, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNCharacterStreamAtName(String string, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNCharacterStream(String string, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNCharacterStreamAtName(String string, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNCharacterStream(String string, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setUnicodeStream(int n2, InputStream inputStream, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setUnicodeStreamAtName(String string, InputStream inputStream, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setUnicodeStream(String string, InputStream inputStream, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Array getArray(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Array getArray(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public BigDecimal getBigDecimal(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public BigDecimal getBigDecimal(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public BigDecimal getBigDecimal(String string, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Blob getBlob(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Blob getBlob(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean getBoolean(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean getBoolean(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public byte getByte(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public byte getByte(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public byte[] getBytes(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public byte[] getBytes(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Clob getClob(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Clob getClob(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Date getDate(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Date getDate(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Date getDate(int n2, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Date getDate(String string, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public double getDouble(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public double getDouble(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public float getFloat(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public float getFloat(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getInt(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getInt(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public long getLong(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public long getLong(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public NClob getNClob(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public NClob getNClob(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public String getNString(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public String getNString(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Object getObject(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Object getObject(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Object getObject(String string, Map<String, Class<?>> map) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Ref getRef(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Ref getRef(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public RowId getRowId(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public RowId getRowId(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public short getShort(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public short getShort(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public SQLXML getSQLXML(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public SQLXML getSQLXML(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public String getString(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public String getString(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Time getTime(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Time getTime(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Time getTime(int n2, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Time getTime(String string, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Timestamp getTimestamp(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Timestamp getTimestamp(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Timestamp getTimestamp(int n2, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Timestamp getTimestamp(String string, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public URL getURL(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public URL getURL(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ARRAY getARRAY(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public ARRAY getARRAY(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public BFILE getBFILE(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public BFILE getBFILE(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public BFILE getBfile(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public BFILE getBfile(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public BLOB getBLOB(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public BLOB getBLOB(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public CHAR getCHAR(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public CHAR getCHAR(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public CLOB getCLOB(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public CLOB getCLOB(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ResultSet getCursor(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public ResultSet getCursor(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public DATE getDATE(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public DATE getDATE(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public INTERVALDS getINTERVALDS(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public INTERVALDS getINTERVALDS(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public INTERVALYM getINTERVALYM(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public INTERVALYM getINTERVALYM(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public NUMBER getNUMBER(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public NUMBER getNUMBER(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public OPAQUE getOPAQUE(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public OPAQUE getOPAQUE(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Datum getOracleObject(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public Datum getOracleObject(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ORAData getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public ORAData getORAData(String string, ORADataFactory oRADataFactory) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Object getObject(int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public Object getObject(String string, OracleDataFactory oracleDataFactory) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public RAW getRAW(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public RAW getRAW(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public REF getREF(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public REF getREF(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ROWID getROWID(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public ROWID getROWID(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public STRUCT getSTRUCT(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public STRUCT getSTRUCT(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public TIMESTAMPLTZ getTIMESTAMPLTZ(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public TIMESTAMPLTZ getTIMESTAMPLTZ(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public TIMESTAMPTZ getTIMESTAMPTZ(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public TIMESTAMPTZ getTIMESTAMPTZ(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public TIMESTAMP getTIMESTAMP(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public TIMESTAMP getTIMESTAMP(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public CustomDatum getCustomDatum(int n2, CustomDatumFactory customDatumFactory) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    public CustomDatum getCustomDatum(String string, CustomDatumFactory customDatumFactory) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public InputStream getAsciiStream(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public InputStream getAsciiStream(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public InputStream getBinaryStream(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public InputStream getBinaryStream(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Reader getCharacterStream(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Reader getCharacterStream(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Reader getNCharacterStream(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Reader getNCharacterStream(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public InputStream getUnicodeStream(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public InputStream getUnicodeStream(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNull(int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNull(String string, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNull(int n2, int n3, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNull(String string, int n2, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNullAtName(String string, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setNullAtName(String string, int n2, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObject(int n2, Object object, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObject(String string, Object object, int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObjectAtName(String string, Object object, int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getFetchDirection() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getFetchSize() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getMaxRows() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getResultSetType() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void cancel() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void clearBatch() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void clearWarnings() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void close() throws SQLException {
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setFetchDirection(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setFetchSize(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setMaxFieldSize(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setMaxRows(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setQueryTimeout(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean getMoreResults(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setEscapeProcessing(boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int executeUpdate(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void addBatch(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setCursorName(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean execute(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int executeUpdate(String string, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean execute(String string, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int executeUpdate(String string, int[] nArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean execute(String string, int[] nArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Connection getConnection() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int executeUpdate(String string, String[] stringArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean execute(String string, String[] stringArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ResultSet executeQuery(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void clearDefines() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void defineColumnType(int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void defineColumnType(int n2, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void defineColumnType(int n2, int n3, int n4, short s2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void defineColumnTypeBytes(int n2, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void defineColumnTypeChars(int n2, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void defineColumnType(int n2, int n3, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getRowPrefetch() {
        return -1;
    }

    @Override
    public void setRowPrefetch(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getLobPrefetchSize() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setLobPrefetchSize(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void closeWithKey(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int creationState() {
        return -1;
    }

    @Override
    public boolean isNCHAR(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int executeUpdate() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void addBatch() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void clearParameters() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean execute() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public ResultSet getReturnResultSet() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void defineParameterTypeBytes(int n2, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void defineParameterTypeChars(int n2, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void defineParameterType(int n2, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public int getExecuteBatch() {
        return -1;
    }

    @Override
    public int sendBatch() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setExecuteBatch(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setPlsqlIndexTable(int n2, Object object, int n3, int n4, int n5, int n6) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setFormOfUse(int n2, short s2) {
    }

    @Override
    public void setDisableStmtCaching(boolean bl) {
    }

    @Override
    public OracleParameterMetaData OracleGetParameterMetaData() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerReturnParameter(int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerReturnParameter(int n2, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerReturnParameter(int n2, int n3, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBytesForBlob(int n2, byte[] byArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBytesForBlobAtName(String string, byte[] byArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setStringForClob(int n2, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setStringForClobAtName(String string, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setStructDescriptor(int n2, StructDescriptor structDescriptor) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setStructDescriptor(String string, StructDescriptor structDescriptor) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setStructDescriptorAtName(String string, StructDescriptor structDescriptor) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Object getAnyDataEmbeddedObject(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(int n2, int n3, int n4, int n5) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(int n2, int n3, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(int n2, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameterBytes(int n2, int n3, int n4, int n5) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameterChars(int n2, int n3, int n4, int n5) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Object getPlsqlIndexTable(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Object getPlsqlIndexTable(int n2, Class<?> clazz) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public Datum[] getOraclePlsqlIndexTable(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerIndexTableOutParameter(int n2, int n3, int n4, int n5) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setStringForClob(String string, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setBytesForBlob(String string, byte[] byArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(String string, int n2, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean wasNull() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(String string, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(String string, int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(String string, int n2, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameterAtName(String string, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameterAtName(String string, int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameterAtName(String string, int n2, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public byte[] privateGetBytes(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setDatabaseChangeRegistration(DatabaseChangeRegistration databaseChangeRegistration) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return true;
    }

    @Override
    public boolean isPoolable() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setPoolable(boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
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
    public <T> T getObject(int n2, Class<T> clazz) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public <T> T getObject(String string, Class<T> clazz) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setFixedString(boolean bl) {
    }

    @Override
    public boolean getFixedString() {
        return false;
    }

    @Override
    public boolean getserverCursor() {
        return false;
    }

    @Override
    public int getcacheState() {
        return 0;
    }

    @Override
    public int getstatementType() {
        return 3;
    }

    @Override
    public void setCheckBindTypes(boolean bl) {
    }

    @Override
    public void setInternalBytes(int n2, byte[] byArray, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void enterImplicitCache() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void enterExplicitCache() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void exitImplicitCacheToActive() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void exitExplicitCacheToActive() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void exitImplicitCacheToClose() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void exitExplicitCacheToClose() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public String[] getRegisteredTableNames() throws SQLException {
        return null;
    }

    @Override
    public long getRegisteredQueryId() throws SQLException {
        return -1L;
    }

    @Override
    public String getOriginalSql() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setSnapshotSCN(long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    @Override
    public OracleStatement.SqlKind getSqlKind() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public long getChecksum() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerBindChecksumListener(OracleStatement.BindChecksumListener bindChecksumListener) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
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
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public byte[] getCompileKey() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObject(int n2, Object object, SQLType sQLType) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObject(int n2, Object object, SQLType sQLType, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(int n2, SQLType sQLType) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(int n2, SQLType sQLType, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(int n2, SQLType sQLType, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(String string, SQLType sQLType) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(String string, SQLType sQLType, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void registerOutParameter(String string, SQLType sQLType, String string2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObject(String string, Object object, SQLType sQLType) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setObject(String string, Object object, SQLType sQLType, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public void setShardingKeyRpnTokens(byte[] byArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }

    @Override
    public byte[] getShardingKeyRpnTokens() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), this.errorCode).fillInStackTrace();
    }
}

