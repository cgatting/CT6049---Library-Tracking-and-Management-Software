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
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.diagnostics.SecuredLogger;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Loggable;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleResultSet;
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
abstract class GeneratedResultSet
implements OracleResultSet,
Loggable {
    protected PhysicalConnection connection;
    protected Object acProxy;

    @Override
    public abstract boolean absolute(int var1) throws SQLException;

    @Override
    public abstract void afterLast() throws SQLException;

    @Override
    public abstract void beforeFirst() throws SQLException;

    @Override
    public abstract void cancelRowUpdates() throws SQLException;

    @Override
    public abstract void clearWarnings() throws SQLException;

    @Override
    public abstract void deleteRow() throws SQLException;

    @Override
    public abstract boolean first() throws SQLException;

    @Override
    public abstract int getConcurrency() throws SQLException;

    @Override
    public abstract String getCursorName() throws SQLException;

    @Override
    public abstract int getFetchDirection() throws SQLException;

    @Override
    public abstract int getFetchSize() throws SQLException;

    @Override
    public abstract int getHoldability() throws SQLException;

    @Override
    public abstract ResultSetMetaData getMetaData() throws SQLException;

    @Override
    public abstract int getRow() throws SQLException;

    @Override
    public abstract Statement getStatement() throws SQLException;

    @Override
    public abstract int getType() throws SQLException;

    @Override
    public abstract SQLWarning getWarnings() throws SQLException;

    @Override
    public abstract void insertRow() throws SQLException;

    @Override
    public abstract boolean isAfterLast() throws SQLException;

    @Override
    public abstract boolean isBeforeFirst() throws SQLException;

    @Override
    public abstract boolean isClosed() throws SQLException;

    @Override
    public abstract boolean isFirst() throws SQLException;

    @Override
    public abstract boolean isLast() throws SQLException;

    @Override
    public abstract boolean last() throws SQLException;

    @Override
    public abstract void moveToCurrentRow() throws SQLException;

    @Override
    public abstract void moveToInsertRow() throws SQLException;

    @Override
    public abstract boolean next() throws SQLException;

    @Override
    public abstract boolean previous() throws SQLException;

    @Override
    public abstract void refreshRow() throws SQLException;

    @Override
    public abstract boolean relative(int var1) throws SQLException;

    @Override
    public abstract boolean rowDeleted() throws SQLException;

    @Override
    public abstract boolean rowInserted() throws SQLException;

    @Override
    public abstract boolean rowUpdated() throws SQLException;

    @Override
    public abstract void setFetchDirection(int var1) throws SQLException;

    @Override
    public abstract void setFetchSize(int var1) throws SQLException;

    @Override
    public abstract void updateRow() throws SQLException;

    @Override
    public abstract boolean wasNull() throws SQLException;

    protected GeneratedResultSet(PhysicalConnection physicalConnection) {
        this.connection = physicalConnection;
    }

    @Override
    public SecuredLogger getLogger() {
        if (this.connection != null) {
            return this.connection.securedLogger;
        }
        return null;
    }

    @Override
    public void close() throws SQLException {
        this.connection = null;
    }

    @Override
    public abstract int findColumn(String var1) throws SQLException;

    @Override
    public abstract Array getArray(int var1) throws SQLException;

    @Override
    public abstract BigDecimal getBigDecimal(int var1) throws SQLException;

    @Override
    public abstract BigDecimal getBigDecimal(int var1, int var2) throws SQLException;

    @Override
    public abstract Blob getBlob(int var1) throws SQLException;

    @Override
    public abstract boolean getBoolean(int var1) throws SQLException;

    @Override
    public abstract byte getByte(int var1) throws SQLException;

    @Override
    public abstract byte[] getBytes(int var1) throws SQLException;

    @Override
    public abstract Clob getClob(int var1) throws SQLException;

    @Override
    public abstract Date getDate(int var1) throws SQLException;

    @Override
    public abstract Date getDate(int var1, Calendar var2) throws SQLException;

    @Override
    public abstract double getDouble(int var1) throws SQLException;

    @Override
    public abstract float getFloat(int var1) throws SQLException;

    @Override
    public abstract int getInt(int var1) throws SQLException;

    @Override
    public abstract long getLong(int var1) throws SQLException;

    @Override
    public abstract NClob getNClob(int var1) throws SQLException;

    @Override
    public abstract String getNString(int var1) throws SQLException;

    @Override
    public abstract Object getObject(int var1) throws SQLException;

    @Override
    public abstract Object getObject(int var1, Map<String, Class<?>> var2) throws SQLException;

    @Override
    public abstract Ref getRef(int var1) throws SQLException;

    @Override
    public abstract RowId getRowId(int var1) throws SQLException;

    @Override
    public abstract short getShort(int var1) throws SQLException;

    @Override
    public abstract SQLXML getSQLXML(int var1) throws SQLException;

    @Override
    public abstract String getString(int var1) throws SQLException;

    @Override
    public abstract Time getTime(int var1) throws SQLException;

    @Override
    public abstract Time getTime(int var1, Calendar var2) throws SQLException;

    @Override
    public abstract Timestamp getTimestamp(int var1) throws SQLException;

    @Override
    public abstract Timestamp getTimestamp(int var1, Calendar var2) throws SQLException;

    @Override
    public abstract URL getURL(int var1) throws SQLException;

    @Override
    public abstract ARRAY getARRAY(int var1) throws SQLException;

    @Override
    public abstract BFILE getBFILE(int var1) throws SQLException;

    @Override
    public abstract BFILE getBfile(int var1) throws SQLException;

    @Override
    public abstract BLOB getBLOB(int var1) throws SQLException;

    @Override
    public abstract CHAR getCHAR(int var1) throws SQLException;

    @Override
    public abstract CLOB getCLOB(int var1) throws SQLException;

    @Override
    public abstract ResultSet getCursor(int var1) throws SQLException;

    @Override
    public abstract DATE getDATE(int var1) throws SQLException;

    @Override
    public abstract INTERVALDS getINTERVALDS(int var1) throws SQLException;

    @Override
    public abstract INTERVALYM getINTERVALYM(int var1) throws SQLException;

    @Override
    public abstract NUMBER getNUMBER(int var1) throws SQLException;

    @Override
    public abstract OPAQUE getOPAQUE(int var1) throws SQLException;

    @Override
    public abstract Datum getOracleObject(int var1) throws SQLException;

    @Override
    public abstract ORAData getORAData(int var1, ORADataFactory var2) throws SQLException;

    @Override
    public abstract Object getObject(int var1, OracleDataFactory var2) throws SQLException;

    @Override
    public abstract RAW getRAW(int var1) throws SQLException;

    @Override
    public abstract REF getREF(int var1) throws SQLException;

    @Override
    public abstract ROWID getROWID(int var1) throws SQLException;

    @Override
    public abstract STRUCT getSTRUCT(int var1) throws SQLException;

    @Override
    public abstract TIMESTAMPLTZ getTIMESTAMPLTZ(int var1) throws SQLException;

    @Override
    public abstract TIMESTAMPTZ getTIMESTAMPTZ(int var1) throws SQLException;

    @Override
    public abstract TIMESTAMP getTIMESTAMP(int var1) throws SQLException;

    @Override
    public abstract CustomDatum getCustomDatum(int var1, CustomDatumFactory var2) throws SQLException;

    @Override
    public abstract InputStream getAsciiStream(int var1) throws SQLException;

    @Override
    public abstract InputStream getBinaryStream(int var1) throws SQLException;

    @Override
    public abstract Reader getCharacterStream(int var1) throws SQLException;

    @Override
    public abstract Reader getNCharacterStream(int var1) throws SQLException;

    @Override
    public abstract InputStream getUnicodeStream(int var1) throws SQLException;

    @Override
    public void updateArray(int n2, Array array) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateArray").fillInStackTrace();
    }

    @Override
    public void updateBigDecimal(int n2, BigDecimal bigDecimal) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBigDecimal").fillInStackTrace();
    }

    @Override
    public void updateBlob(int n2, Blob blob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBlob").fillInStackTrace();
    }

    @Override
    public void updateBoolean(int n2, boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBoolean").fillInStackTrace();
    }

    @Override
    public void updateByte(int n2, byte by) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateByte").fillInStackTrace();
    }

    @Override
    public void updateBytes(int n2, byte[] byArray) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBytes").fillInStackTrace();
    }

    @Override
    public void updateClob(int n2, Clob clob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateClob").fillInStackTrace();
    }

    @Override
    public void updateDate(int n2, Date date) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateDate").fillInStackTrace();
    }

    public void updateDate(int n2, Date date, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateDate").fillInStackTrace();
    }

    @Override
    public void updateDouble(int n2, double d2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateDouble").fillInStackTrace();
    }

    @Override
    public void updateFloat(int n2, float f2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateFloat").fillInStackTrace();
    }

    @Override
    public void updateInt(int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateInt").fillInStackTrace();
    }

    @Override
    public void updateLong(int n2, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateLong").fillInStackTrace();
    }

    @Override
    public void updateNClob(int n2, NClob nClob) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateNClob").fillInStackTrace();
    }

    @Override
    public void updateNString(int n2, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateNString").fillInStackTrace();
    }

    @Override
    public void updateObject(int n2, Object object) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateObject").fillInStackTrace();
    }

    @Override
    public void updateObject(int n2, Object object, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateObject").fillInStackTrace();
    }

    @Override
    public void updateRef(int n2, Ref ref) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateRef").fillInStackTrace();
    }

    @Override
    public void updateRowId(int n2, RowId rowId) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateRowId").fillInStackTrace();
    }

    @Override
    public void updateShort(int n2, short s2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateShort").fillInStackTrace();
    }

    @Override
    public void updateSQLXML(int n2, SQLXML sQLXML) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateSQLXML").fillInStackTrace();
    }

    @Override
    public void updateString(int n2, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateString").fillInStackTrace();
    }

    @Override
    public void updateTime(int n2, Time time) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateTime").fillInStackTrace();
    }

    public void updateTime(int n2, Time time, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateTime").fillInStackTrace();
    }

    @Override
    public void updateTimestamp(int n2, Timestamp timestamp) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateTimestamp").fillInStackTrace();
    }

    public void updateTimestamp(int n2, Timestamp timestamp, Calendar calendar) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateTimestamp").fillInStackTrace();
    }

    public void updateURL(int n2, URL uRL) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateURL").fillInStackTrace();
    }

    @Override
    public void updateARRAY(int n2, ARRAY aRRAY) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateARRAY").fillInStackTrace();
    }

    @Override
    public void updateBFILE(int n2, BFILE bFILE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBFILE").fillInStackTrace();
    }

    @Override
    public void updateBfile(int n2, BFILE bFILE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBfile").fillInStackTrace();
    }

    @Override
    public void updateBLOB(int n2, BLOB bLOB) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBLOB").fillInStackTrace();
    }

    @Override
    public void updateCHAR(int n2, CHAR cHAR) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateCHAR").fillInStackTrace();
    }

    @Override
    public void updateCLOB(int n2, CLOB cLOB) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateCLOB").fillInStackTrace();
    }

    @Override
    public void updateDATE(int n2, DATE dATE) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateDATE").fillInStackTrace();
    }

    @Override
    public void updateINTERVALDS(int n2, INTERVALDS iNTERVALDS) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateINTERVALDS").fillInStackTrace();
    }

    @Override
    public void updateINTERVALYM(int n2, INTERVALYM iNTERVALYM) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateINTERVALYM").fillInStackTrace();
    }

    @Override
    public void updateNUMBER(int n2, NUMBER nUMBER) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateNUMBER").fillInStackTrace();
    }

    @Override
    public void updateOracleObject(int n2, Datum datum) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateOracleObject").fillInStackTrace();
    }

    @Override
    public void updateORAData(int n2, ORAData oRAData) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateORAData").fillInStackTrace();
    }

    @Override
    public void updateRAW(int n2, RAW rAW) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateRAW").fillInStackTrace();
    }

    @Override
    public void updateREF(int n2, REF rEF) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateREF").fillInStackTrace();
    }

    @Override
    public void updateROWID(int n2, ROWID rOWID) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateROWID").fillInStackTrace();
    }

    @Override
    public void updateSTRUCT(int n2, STRUCT sTRUCT) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateSTRUCT").fillInStackTrace();
    }

    @Override
    public void updateTIMESTAMPLTZ(int n2, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateTIMESTAMPLTZ").fillInStackTrace();
    }

    @Override
    public void updateTIMESTAMPTZ(int n2, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateTIMESTAMPTZ").fillInStackTrace();
    }

    @Override
    public void updateTIMESTAMP(int n2, TIMESTAMP tIMESTAMP) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateTIMESTAMP").fillInStackTrace();
    }

    @Override
    public void updateCustomDatum(int n2, CustomDatum customDatum) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateCustomDatum").fillInStackTrace();
    }

    @Override
    public void updateBlob(int n2, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBlob").fillInStackTrace();
    }

    @Override
    public void updateBlob(int n2, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBlob").fillInStackTrace();
    }

    @Override
    public void updateClob(int n2, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateClob").fillInStackTrace();
    }

    @Override
    public void updateClob(int n2, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateClob").fillInStackTrace();
    }

    @Override
    public void updateNClob(int n2, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateNClob").fillInStackTrace();
    }

    @Override
    public void updateNClob(int n2, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateNClob").fillInStackTrace();
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateAsciiStream").fillInStackTrace();
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateAsciiStream").fillInStackTrace();
    }

    @Override
    public void updateAsciiStream(int n2, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateAsciiStream").fillInStackTrace();
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBinaryStream").fillInStackTrace();
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBinaryStream").fillInStackTrace();
    }

    @Override
    public void updateBinaryStream(int n2, InputStream inputStream, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateBinaryStream").fillInStackTrace();
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateCharacterStream").fillInStackTrace();
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateCharacterStream").fillInStackTrace();
    }

    @Override
    public void updateCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateCharacterStream").fillInStackTrace();
    }

    @Override
    public void updateNCharacterStream(int n2, Reader reader) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateNCharacterStream").fillInStackTrace();
    }

    @Override
    public void updateNCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateNCharacterStream").fillInStackTrace();
    }

    public void updateUnicodeStream(int n2, InputStream inputStream, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "updateUnicodeStream").fillInStackTrace();
    }

    @Override
    public Array getArray(String string) throws SQLException {
        return this.getArray(this.findColumn(string));
    }

    @Override
    public BigDecimal getBigDecimal(String string) throws SQLException {
        return this.getBigDecimal(this.findColumn(string));
    }

    @Override
    public BigDecimal getBigDecimal(String string, int n2) throws SQLException {
        return this.getBigDecimal(this.findColumn(string), n2);
    }

    @Override
    public Blob getBlob(String string) throws SQLException {
        return this.getBlob(this.findColumn(string));
    }

    @Override
    public boolean getBoolean(String string) throws SQLException {
        return this.getBoolean(this.findColumn(string));
    }

    @Override
    public byte getByte(String string) throws SQLException {
        return this.getByte(this.findColumn(string));
    }

    @Override
    public byte[] getBytes(String string) throws SQLException {
        return this.getBytes(this.findColumn(string));
    }

    @Override
    public Clob getClob(String string) throws SQLException {
        return this.getClob(this.findColumn(string));
    }

    @Override
    public Date getDate(String string) throws SQLException {
        return this.getDate(this.findColumn(string));
    }

    @Override
    public Date getDate(String string, Calendar calendar) throws SQLException {
        return this.getDate(this.findColumn(string), calendar);
    }

    @Override
    public double getDouble(String string) throws SQLException {
        return this.getDouble(this.findColumn(string));
    }

    @Override
    public float getFloat(String string) throws SQLException {
        return this.getFloat(this.findColumn(string));
    }

    @Override
    public int getInt(String string) throws SQLException {
        return this.getInt(this.findColumn(string));
    }

    @Override
    public long getLong(String string) throws SQLException {
        return this.getLong(this.findColumn(string));
    }

    @Override
    public NClob getNClob(String string) throws SQLException {
        return this.getNClob(this.findColumn(string));
    }

    @Override
    public String getNString(String string) throws SQLException {
        return this.getNString(this.findColumn(string));
    }

    @Override
    public Object getObject(String string) throws SQLException {
        return this.getObject(this.findColumn(string));
    }

    @Override
    public Object getObject(String string, Map<String, Class<?>> map) throws SQLException {
        return this.getObject(this.findColumn(string), map);
    }

    @Override
    public Ref getRef(String string) throws SQLException {
        return this.getRef(this.findColumn(string));
    }

    @Override
    public RowId getRowId(String string) throws SQLException {
        return this.getRowId(this.findColumn(string));
    }

    @Override
    public short getShort(String string) throws SQLException {
        return this.getShort(this.findColumn(string));
    }

    @Override
    public SQLXML getSQLXML(String string) throws SQLException {
        return this.getSQLXML(this.findColumn(string));
    }

    @Override
    public String getString(String string) throws SQLException {
        return this.getString(this.findColumn(string));
    }

    @Override
    public Time getTime(String string) throws SQLException {
        return this.getTime(this.findColumn(string));
    }

    @Override
    public Time getTime(String string, Calendar calendar) throws SQLException {
        return this.getTime(this.findColumn(string), calendar);
    }

    @Override
    public Timestamp getTimestamp(String string) throws SQLException {
        return this.getTimestamp(this.findColumn(string));
    }

    @Override
    public Timestamp getTimestamp(String string, Calendar calendar) throws SQLException {
        return this.getTimestamp(this.findColumn(string), calendar);
    }

    @Override
    public URL getURL(String string) throws SQLException {
        return this.getURL(this.findColumn(string));
    }

    @Override
    public ARRAY getARRAY(String string) throws SQLException {
        return this.getARRAY(this.findColumn(string));
    }

    @Override
    public BFILE getBFILE(String string) throws SQLException {
        return this.getBFILE(this.findColumn(string));
    }

    @Override
    public BFILE getBfile(String string) throws SQLException {
        return this.getBfile(this.findColumn(string));
    }

    @Override
    public BLOB getBLOB(String string) throws SQLException {
        return this.getBLOB(this.findColumn(string));
    }

    @Override
    public CHAR getCHAR(String string) throws SQLException {
        return this.getCHAR(this.findColumn(string));
    }

    @Override
    public CLOB getCLOB(String string) throws SQLException {
        return this.getCLOB(this.findColumn(string));
    }

    @Override
    public ResultSet getCursor(String string) throws SQLException {
        return this.getCursor(this.findColumn(string));
    }

    @Override
    public DATE getDATE(String string) throws SQLException {
        return this.getDATE(this.findColumn(string));
    }

    @Override
    public INTERVALDS getINTERVALDS(String string) throws SQLException {
        return this.getINTERVALDS(this.findColumn(string));
    }

    @Override
    public INTERVALYM getINTERVALYM(String string) throws SQLException {
        return this.getINTERVALYM(this.findColumn(string));
    }

    @Override
    public NUMBER getNUMBER(String string) throws SQLException {
        return this.getNUMBER(this.findColumn(string));
    }

    @Override
    public OPAQUE getOPAQUE(String string) throws SQLException {
        return this.getOPAQUE(this.findColumn(string));
    }

    @Override
    public Datum getOracleObject(String string) throws SQLException {
        return this.getOracleObject(this.findColumn(string));
    }

    @Override
    public ORAData getORAData(String string, ORADataFactory oRADataFactory) throws SQLException {
        return this.getORAData(this.findColumn(string), oRADataFactory);
    }

    @Override
    public Object getObject(String string, OracleDataFactory oracleDataFactory) throws SQLException {
        return this.getObject(this.findColumn(string), oracleDataFactory);
    }

    @Override
    public RAW getRAW(String string) throws SQLException {
        return this.getRAW(this.findColumn(string));
    }

    @Override
    public REF getREF(String string) throws SQLException {
        return this.getREF(this.findColumn(string));
    }

    @Override
    public ROWID getROWID(String string) throws SQLException {
        return this.getROWID(this.findColumn(string));
    }

    @Override
    public STRUCT getSTRUCT(String string) throws SQLException {
        return this.getSTRUCT(this.findColumn(string));
    }

    @Override
    public TIMESTAMPLTZ getTIMESTAMPLTZ(String string) throws SQLException {
        return this.getTIMESTAMPLTZ(this.findColumn(string));
    }

    @Override
    public TIMESTAMPTZ getTIMESTAMPTZ(String string) throws SQLException {
        return this.getTIMESTAMPTZ(this.findColumn(string));
    }

    @Override
    public TIMESTAMP getTIMESTAMP(String string) throws SQLException {
        return this.getTIMESTAMP(this.findColumn(string));
    }

    @Override
    public CustomDatum getCustomDatum(String string, CustomDatumFactory customDatumFactory) throws SQLException {
        return this.getCustomDatum(this.findColumn(string), customDatumFactory);
    }

    @Override
    public InputStream getAsciiStream(String string) throws SQLException {
        return this.getAsciiStream(this.findColumn(string));
    }

    @Override
    public InputStream getBinaryStream(String string) throws SQLException {
        return this.getBinaryStream(this.findColumn(string));
    }

    @Override
    public Reader getCharacterStream(String string) throws SQLException {
        return this.getCharacterStream(this.findColumn(string));
    }

    @Override
    public Reader getNCharacterStream(String string) throws SQLException {
        return this.getNCharacterStream(this.findColumn(string));
    }

    @Override
    public InputStream getUnicodeStream(String string) throws SQLException {
        return this.getUnicodeStream(this.findColumn(string));
    }

    @Override
    public void updateArray(String string, Array array) throws SQLException {
        this.updateArray(this.findColumn(string), array);
    }

    @Override
    public void updateBigDecimal(String string, BigDecimal bigDecimal) throws SQLException {
        this.updateBigDecimal(this.findColumn(string), bigDecimal);
    }

    @Override
    public void updateBlob(String string, Blob blob) throws SQLException {
        this.updateBlob(this.findColumn(string), blob);
    }

    @Override
    public void updateBoolean(String string, boolean bl) throws SQLException {
        this.updateBoolean(this.findColumn(string), bl);
    }

    @Override
    public void updateByte(String string, byte by) throws SQLException {
        this.updateByte(this.findColumn(string), by);
    }

    @Override
    public void updateBytes(String string, byte[] byArray) throws SQLException {
        this.updateBytes(this.findColumn(string), byArray);
    }

    @Override
    public void updateClob(String string, Clob clob) throws SQLException {
        this.updateClob(this.findColumn(string), clob);
    }

    @Override
    public void updateDate(String string, Date date) throws SQLException {
        this.updateDate(this.findColumn(string), date);
    }

    public void updateDate(String string, Date date, Calendar calendar) throws SQLException {
        this.updateDate(this.findColumn(string), date, calendar);
    }

    @Override
    public void updateDouble(String string, double d2) throws SQLException {
        this.updateDouble(this.findColumn(string), d2);
    }

    @Override
    public void updateFloat(String string, float f2) throws SQLException {
        this.updateFloat(this.findColumn(string), f2);
    }

    @Override
    public void updateInt(String string, int n2) throws SQLException {
        this.updateInt(this.findColumn(string), n2);
    }

    @Override
    public void updateLong(String string, long l2) throws SQLException {
        this.updateLong(this.findColumn(string), l2);
    }

    @Override
    public void updateNClob(String string, NClob nClob) throws SQLException {
        this.updateNClob(this.findColumn(string), nClob);
    }

    @Override
    public void updateNString(String string, String string2) throws SQLException {
        this.updateNString(this.findColumn(string), string2);
    }

    @Override
    public void updateObject(String string, Object object) throws SQLException {
        this.updateObject(this.findColumn(string), object);
    }

    @Override
    public void updateObject(String string, Object object, int n2) throws SQLException {
        this.updateObject(this.findColumn(string), object, n2);
    }

    @Override
    public void updateRef(String string, Ref ref) throws SQLException {
        this.updateRef(this.findColumn(string), ref);
    }

    @Override
    public void updateRowId(String string, RowId rowId) throws SQLException {
        this.updateRowId(this.findColumn(string), rowId);
    }

    @Override
    public void updateShort(String string, short s2) throws SQLException {
        this.updateShort(this.findColumn(string), s2);
    }

    @Override
    public void updateSQLXML(String string, SQLXML sQLXML) throws SQLException {
        this.updateSQLXML(this.findColumn(string), sQLXML);
    }

    @Override
    public void updateString(String string, String string2) throws SQLException {
        this.updateString(this.findColumn(string), string2);
    }

    @Override
    public void updateTime(String string, Time time) throws SQLException {
        this.updateTime(this.findColumn(string), time);
    }

    public void updateTime(String string, Time time, Calendar calendar) throws SQLException {
        this.updateTime(this.findColumn(string), time, calendar);
    }

    @Override
    public void updateTimestamp(String string, Timestamp timestamp) throws SQLException {
        this.updateTimestamp(this.findColumn(string), timestamp);
    }

    public void updateTimestamp(String string, Timestamp timestamp, Calendar calendar) throws SQLException {
        this.updateTimestamp(this.findColumn(string), timestamp, calendar);
    }

    public void updateURL(String string, URL uRL) throws SQLException {
        this.updateURL(this.findColumn(string), uRL);
    }

    @Override
    public void updateARRAY(String string, ARRAY aRRAY) throws SQLException {
        this.updateARRAY(this.findColumn(string), aRRAY);
    }

    @Override
    public void updateBFILE(String string, BFILE bFILE) throws SQLException {
        this.updateBFILE(this.findColumn(string), bFILE);
    }

    @Override
    public void updateBfile(String string, BFILE bFILE) throws SQLException {
        this.updateBfile(this.findColumn(string), bFILE);
    }

    @Override
    public void updateBLOB(String string, BLOB bLOB) throws SQLException {
        this.updateBLOB(this.findColumn(string), bLOB);
    }

    @Override
    public void updateCHAR(String string, CHAR cHAR) throws SQLException {
        this.updateCHAR(this.findColumn(string), cHAR);
    }

    @Override
    public void updateCLOB(String string, CLOB cLOB) throws SQLException {
        this.updateCLOB(this.findColumn(string), cLOB);
    }

    @Override
    public void updateDATE(String string, DATE dATE) throws SQLException {
        this.updateDATE(this.findColumn(string), dATE);
    }

    @Override
    public void updateINTERVALDS(String string, INTERVALDS iNTERVALDS) throws SQLException {
        this.updateINTERVALDS(this.findColumn(string), iNTERVALDS);
    }

    @Override
    public void updateINTERVALYM(String string, INTERVALYM iNTERVALYM) throws SQLException {
        this.updateINTERVALYM(this.findColumn(string), iNTERVALYM);
    }

    @Override
    public void updateNUMBER(String string, NUMBER nUMBER) throws SQLException {
        this.updateNUMBER(this.findColumn(string), nUMBER);
    }

    @Override
    public void updateOracleObject(String string, Datum datum) throws SQLException {
        this.updateOracleObject(this.findColumn(string), datum);
    }

    @Override
    public void updateORAData(String string, ORAData oRAData) throws SQLException {
        this.updateORAData(this.findColumn(string), oRAData);
    }

    @Override
    public void updateRAW(String string, RAW rAW) throws SQLException {
        this.updateRAW(this.findColumn(string), rAW);
    }

    @Override
    public void updateREF(String string, REF rEF) throws SQLException {
        this.updateREF(this.findColumn(string), rEF);
    }

    @Override
    public void updateROWID(String string, ROWID rOWID) throws SQLException {
        this.updateROWID(this.findColumn(string), rOWID);
    }

    @Override
    public void updateSTRUCT(String string, STRUCT sTRUCT) throws SQLException {
        this.updateSTRUCT(this.findColumn(string), sTRUCT);
    }

    @Override
    public void updateTIMESTAMPLTZ(String string, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        this.updateTIMESTAMPLTZ(this.findColumn(string), tIMESTAMPLTZ);
    }

    @Override
    public void updateTIMESTAMPTZ(String string, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        this.updateTIMESTAMPTZ(this.findColumn(string), tIMESTAMPTZ);
    }

    @Override
    public void updateTIMESTAMP(String string, TIMESTAMP tIMESTAMP) throws SQLException {
        this.updateTIMESTAMP(this.findColumn(string), tIMESTAMP);
    }

    @Override
    public void updateCustomDatum(String string, CustomDatum customDatum) throws SQLException {
        this.updateCustomDatum(this.findColumn(string), customDatum);
    }

    @Override
    public void updateBlob(String string, InputStream inputStream) throws SQLException {
        this.updateBlob(this.findColumn(string), inputStream);
    }

    @Override
    public void updateBlob(String string, InputStream inputStream, long l2) throws SQLException {
        this.updateBlob(this.findColumn(string), inputStream, l2);
    }

    @Override
    public void updateClob(String string, Reader reader) throws SQLException {
        this.updateClob(this.findColumn(string), reader);
    }

    @Override
    public void updateClob(String string, Reader reader, long l2) throws SQLException {
        this.updateClob(this.findColumn(string), reader, l2);
    }

    @Override
    public void updateNClob(String string, Reader reader) throws SQLException {
        this.updateNClob(this.findColumn(string), reader);
    }

    @Override
    public void updateNClob(String string, Reader reader, long l2) throws SQLException {
        this.updateNClob(this.findColumn(string), reader, l2);
    }

    @Override
    public void updateAsciiStream(String string, InputStream inputStream) throws SQLException {
        this.updateAsciiStream(this.findColumn(string), inputStream);
    }

    @Override
    public void updateAsciiStream(String string, InputStream inputStream, int n2) throws SQLException {
        this.updateAsciiStream(this.findColumn(string), inputStream, n2);
    }

    @Override
    public void updateAsciiStream(String string, InputStream inputStream, long l2) throws SQLException {
        this.updateAsciiStream(this.findColumn(string), inputStream, l2);
    }

    @Override
    public void updateBinaryStream(String string, InputStream inputStream) throws SQLException {
        this.updateBinaryStream(this.findColumn(string), inputStream);
    }

    @Override
    public void updateBinaryStream(String string, InputStream inputStream, int n2) throws SQLException {
        this.updateBinaryStream(this.findColumn(string), inputStream, n2);
    }

    @Override
    public void updateBinaryStream(String string, InputStream inputStream, long l2) throws SQLException {
        this.updateBinaryStream(this.findColumn(string), inputStream, l2);
    }

    @Override
    public void updateCharacterStream(String string, Reader reader) throws SQLException {
        this.updateCharacterStream(this.findColumn(string), reader);
    }

    @Override
    public void updateCharacterStream(String string, Reader reader, int n2) throws SQLException {
        this.updateCharacterStream(this.findColumn(string), reader, n2);
    }

    @Override
    public void updateCharacterStream(String string, Reader reader, long l2) throws SQLException {
        this.updateCharacterStream(this.findColumn(string), reader, l2);
    }

    @Override
    public void updateNCharacterStream(String string, Reader reader) throws SQLException {
        this.updateNCharacterStream(this.findColumn(string), reader);
    }

    @Override
    public void updateNCharacterStream(String string, Reader reader, long l2) throws SQLException {
        this.updateNCharacterStream(this.findColumn(string), reader, l2);
    }

    public void updateUnicodeStream(String string, InputStream inputStream, int n2) throws SQLException {
        this.updateUnicodeStream(this.findColumn(string), inputStream, n2);
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return this.connection;
    }

    @Override
    public void setACProxy(Object object) {
        this.acProxy = object;
    }

    @Override
    public Object getACProxy() {
        return this.acProxy;
    }
}

