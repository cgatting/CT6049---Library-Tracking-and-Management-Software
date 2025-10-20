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
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.driver.OraclePreparedStatementWrapper;
import oracle.jdbc.driver.OracleStatementWrapper;
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
class OracleCallableStatementWrapper
extends OraclePreparedStatementWrapper
implements oracle.jdbc.internal.OracleCallableStatement {
    protected oracle.jdbc.internal.OracleCallableStatement callableStatement = null;
    Object acProxy;

    OracleCallableStatementWrapper(OracleCallableStatement oracleCallableStatement) throws SQLException {
        super(oracleCallableStatement);
        this.callableStatement = (oracle.jdbc.internal.OracleCallableStatement)oracleCallableStatement;
    }

    @Override
    public void setArray(String string, Array array) throws SQLException {
        this.callableStatement.setArray(string, array);
    }

    @Override
    public void setBigDecimal(String string, BigDecimal bigDecimal) throws SQLException {
        this.callableStatement.setBigDecimal(string, bigDecimal);
    }

    @Override
    public void setBlob(String string, Blob blob) throws SQLException {
        this.callableStatement.setBlob(string, blob);
    }

    @Override
    public void setBoolean(String string, boolean bl) throws SQLException {
        this.callableStatement.setBoolean(string, bl);
    }

    @Override
    public void setByte(String string, byte by) throws SQLException {
        this.callableStatement.setByte(string, by);
    }

    @Override
    public void setBytes(String string, byte[] byArray) throws SQLException {
        this.callableStatement.setBytes(string, byArray);
    }

    @Override
    public void setClob(String string, Clob clob) throws SQLException {
        this.callableStatement.setClob(string, clob);
    }

    @Override
    public void setDate(String string, Date date) throws SQLException {
        this.callableStatement.setDate(string, date);
    }

    @Override
    public void setDate(String string, Date date, Calendar calendar) throws SQLException {
        this.callableStatement.setDate(string, date, calendar);
    }

    @Override
    public void setDouble(String string, double d2) throws SQLException {
        this.callableStatement.setDouble(string, d2);
    }

    @Override
    public void setFloat(String string, float f2) throws SQLException {
        this.callableStatement.setFloat(string, f2);
    }

    @Override
    public void setInt(String string, int n2) throws SQLException {
        this.callableStatement.setInt(string, n2);
    }

    @Override
    public void setLong(String string, long l2) throws SQLException {
        this.callableStatement.setLong(string, l2);
    }

    @Override
    public void setNClob(String string, NClob nClob) throws SQLException {
        this.callableStatement.setNClob(string, nClob);
    }

    @Override
    public void setNString(String string, String string2) throws SQLException {
        this.callableStatement.setNString(string, string2);
    }

    @Override
    public void setObject(String string, Object object) throws SQLException {
        this.callableStatement.setObject(string, object);
    }

    @Override
    public void setObject(String string, Object object, int n2) throws SQLException {
        this.callableStatement.setObject(string, object, n2);
    }

    @Override
    public void setRef(String string, Ref ref) throws SQLException {
        this.callableStatement.setRef(string, ref);
    }

    @Override
    public void setRowId(String string, RowId rowId) throws SQLException {
        this.callableStatement.setRowId(string, rowId);
    }

    @Override
    public void setShort(String string, short s2) throws SQLException {
        this.callableStatement.setShort(string, s2);
    }

    @Override
    public void setSQLXML(String string, SQLXML sQLXML) throws SQLException {
        this.callableStatement.setSQLXML(string, sQLXML);
    }

    @Override
    public void setString(String string, String string2) throws SQLException {
        this.callableStatement.setString(string, string2);
    }

    @Override
    public void setTime(String string, Time time) throws SQLException {
        this.callableStatement.setTime(string, time);
    }

    @Override
    public void setTime(String string, Time time, Calendar calendar) throws SQLException {
        this.callableStatement.setTime(string, time, calendar);
    }

    @Override
    public void setTimestamp(String string, Timestamp timestamp) throws SQLException {
        this.callableStatement.setTimestamp(string, timestamp);
    }

    @Override
    public void setTimestamp(String string, Timestamp timestamp, Calendar calendar) throws SQLException {
        this.callableStatement.setTimestamp(string, timestamp, calendar);
    }

    @Override
    public void setURL(String string, URL uRL) throws SQLException {
        this.callableStatement.setURL(string, uRL);
    }

    @Override
    public void setARRAY(String string, ARRAY aRRAY) throws SQLException {
        this.callableStatement.setARRAY(string, aRRAY);
    }

    @Override
    public void setBFILE(String string, BFILE bFILE) throws SQLException {
        this.callableStatement.setBFILE(string, bFILE);
    }

    @Override
    public void setBfile(String string, BFILE bFILE) throws SQLException {
        this.callableStatement.setBfile(string, bFILE);
    }

    @Override
    public void setBinaryFloat(String string, float f2) throws SQLException {
        this.callableStatement.setBinaryFloat(string, f2);
    }

    @Override
    public void setBinaryFloat(String string, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        this.callableStatement.setBinaryFloat(string, bINARY_FLOAT);
    }

    @Override
    public void setBinaryDouble(String string, double d2) throws SQLException {
        this.callableStatement.setBinaryDouble(string, d2);
    }

    @Override
    public void setBinaryDouble(String string, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        this.callableStatement.setBinaryDouble(string, bINARY_DOUBLE);
    }

    @Override
    public void setBLOB(String string, BLOB bLOB) throws SQLException {
        this.callableStatement.setBLOB(string, bLOB);
    }

    @Override
    public void setCHAR(String string, CHAR cHAR) throws SQLException {
        this.callableStatement.setCHAR(string, cHAR);
    }

    @Override
    public void setCLOB(String string, CLOB cLOB) throws SQLException {
        this.callableStatement.setCLOB(string, cLOB);
    }

    @Override
    public void setCursor(String string, ResultSet resultSet) throws SQLException {
        this.callableStatement.setCursor(string, resultSet);
    }

    @Override
    public void setDATE(String string, DATE dATE) throws SQLException {
        this.callableStatement.setDATE(string, dATE);
    }

    @Override
    public void setFixedCHAR(String string, String string2) throws SQLException {
        this.callableStatement.setFixedCHAR(string, string2);
    }

    @Override
    public void setINTERVALDS(String string, INTERVALDS iNTERVALDS) throws SQLException {
        this.callableStatement.setINTERVALDS(string, iNTERVALDS);
    }

    @Override
    public void setINTERVALYM(String string, INTERVALYM iNTERVALYM) throws SQLException {
        this.callableStatement.setINTERVALYM(string, iNTERVALYM);
    }

    @Override
    public void setNUMBER(String string, NUMBER nUMBER) throws SQLException {
        this.callableStatement.setNUMBER(string, nUMBER);
    }

    @Override
    public void setOPAQUE(String string, OPAQUE oPAQUE) throws SQLException {
        this.callableStatement.setOPAQUE(string, oPAQUE);
    }

    @Override
    public void setOracleObject(String string, Datum datum) throws SQLException {
        this.callableStatement.setOracleObject(string, datum);
    }

    @Override
    public void setORAData(String string, ORAData oRAData) throws SQLException {
        this.callableStatement.setORAData(string, oRAData);
    }

    @Override
    public void setRAW(String string, RAW rAW) throws SQLException {
        this.callableStatement.setRAW(string, rAW);
    }

    @Override
    public void setREF(String string, REF rEF) throws SQLException {
        this.callableStatement.setREF(string, rEF);
    }

    @Override
    public void setRefType(String string, REF rEF) throws SQLException {
        this.callableStatement.setRefType(string, rEF);
    }

    @Override
    public void setROWID(String string, ROWID rOWID) throws SQLException {
        this.callableStatement.setROWID(string, rOWID);
    }

    @Override
    public void setSTRUCT(String string, STRUCT sTRUCT) throws SQLException {
        this.callableStatement.setSTRUCT(string, sTRUCT);
    }

    @Override
    public void setTIMESTAMPLTZ(String string, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        this.callableStatement.setTIMESTAMPLTZ(string, tIMESTAMPLTZ);
    }

    @Override
    public void setTIMESTAMPTZ(String string, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        this.callableStatement.setTIMESTAMPTZ(string, tIMESTAMPTZ);
    }

    @Override
    public void setTIMESTAMP(String string, TIMESTAMP tIMESTAMP) throws SQLException {
        this.callableStatement.setTIMESTAMP(string, tIMESTAMP);
    }

    @Override
    public void setCustomDatum(String string, CustomDatum customDatum) throws SQLException {
        this.callableStatement.setCustomDatum(string, customDatum);
    }

    @Override
    public void setBlob(String string, InputStream inputStream) throws SQLException {
        this.callableStatement.setBlob(string, inputStream);
    }

    @Override
    public void setBlob(String string, InputStream inputStream, long l2) throws SQLException {
        this.callableStatement.setBlob(string, inputStream, l2);
    }

    @Override
    public void setClob(String string, Reader reader) throws SQLException {
        this.callableStatement.setClob(string, reader);
    }

    @Override
    public void setClob(String string, Reader reader, long l2) throws SQLException {
        this.callableStatement.setClob(string, reader, l2);
    }

    @Override
    public void setNClob(String string, Reader reader) throws SQLException {
        this.callableStatement.setNClob(string, reader);
    }

    @Override
    public void setNClob(String string, Reader reader, long l2) throws SQLException {
        this.callableStatement.setNClob(string, reader, l2);
    }

    @Override
    public void setAsciiStream(String string, InputStream inputStream) throws SQLException {
        this.callableStatement.setAsciiStream(string, inputStream);
    }

    @Override
    public void setAsciiStream(String string, InputStream inputStream, int n2) throws SQLException {
        this.callableStatement.setAsciiStream(string, inputStream, n2);
    }

    @Override
    public void setAsciiStream(String string, InputStream inputStream, long l2) throws SQLException {
        this.callableStatement.setAsciiStream(string, inputStream, l2);
    }

    @Override
    public void setBinaryStream(String string, InputStream inputStream) throws SQLException {
        this.callableStatement.setBinaryStream(string, inputStream);
    }

    @Override
    public void setBinaryStream(String string, InputStream inputStream, int n2) throws SQLException {
        this.callableStatement.setBinaryStream(string, inputStream, n2);
    }

    @Override
    public void setBinaryStream(String string, InputStream inputStream, long l2) throws SQLException {
        this.callableStatement.setBinaryStream(string, inputStream, l2);
    }

    @Override
    public void setCharacterStream(String string, Reader reader) throws SQLException {
        this.callableStatement.setCharacterStream(string, reader);
    }

    @Override
    public void setCharacterStream(String string, Reader reader, int n2) throws SQLException {
        this.callableStatement.setCharacterStream(string, reader, n2);
    }

    @Override
    public void setCharacterStream(String string, Reader reader, long l2) throws SQLException {
        this.callableStatement.setCharacterStream(string, reader, l2);
    }

    @Override
    public void setNCharacterStream(String string, Reader reader) throws SQLException {
        this.callableStatement.setNCharacterStream(string, reader);
    }

    @Override
    public void setNCharacterStream(String string, Reader reader, long l2) throws SQLException {
        this.callableStatement.setNCharacterStream(string, reader, l2);
    }

    @Override
    public void setUnicodeStream(String string, InputStream inputStream, int n2) throws SQLException {
        this.callableStatement.setUnicodeStream(string, inputStream, n2);
    }

    @Override
    public void setNull(String string, int n2, String string2) throws SQLException {
        this.callableStatement.setNull(string, n2, string2);
    }

    @Override
    public void setNull(String string, int n2) throws SQLException {
        this.callableStatement.setNull(string, n2);
    }

    @Override
    public Array getArray(int n2) throws SQLException {
        return this.callableStatement.getArray(n2);
    }

    @Override
    public BigDecimal getBigDecimal(int n2) throws SQLException {
        return this.callableStatement.getBigDecimal(n2);
    }

    @Override
    public BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        return this.callableStatement.getBigDecimal(n2, n3);
    }

    @Override
    public Blob getBlob(int n2) throws SQLException {
        return this.callableStatement.getBlob(n2);
    }

    @Override
    public boolean getBoolean(int n2) throws SQLException {
        return this.callableStatement.getBoolean(n2);
    }

    @Override
    public byte getByte(int n2) throws SQLException {
        return this.callableStatement.getByte(n2);
    }

    @Override
    public byte[] getBytes(int n2) throws SQLException {
        return this.callableStatement.getBytes(n2);
    }

    @Override
    public Clob getClob(int n2) throws SQLException {
        return this.callableStatement.getClob(n2);
    }

    @Override
    public Date getDate(int n2) throws SQLException {
        return this.callableStatement.getDate(n2);
    }

    @Override
    public Date getDate(int n2, Calendar calendar) throws SQLException {
        return this.callableStatement.getDate(n2, calendar);
    }

    @Override
    public double getDouble(int n2) throws SQLException {
        return this.callableStatement.getDouble(n2);
    }

    @Override
    public float getFloat(int n2) throws SQLException {
        return this.callableStatement.getFloat(n2);
    }

    @Override
    public int getInt(int n2) throws SQLException {
        return this.callableStatement.getInt(n2);
    }

    @Override
    public long getLong(int n2) throws SQLException {
        return this.callableStatement.getLong(n2);
    }

    @Override
    public NClob getNClob(int n2) throws SQLException {
        return this.callableStatement.getNClob(n2);
    }

    @Override
    public String getNString(int n2) throws SQLException {
        return this.callableStatement.getNString(n2);
    }

    @Override
    public Object getObject(int n2) throws SQLException {
        return this.callableStatement.getObject(n2);
    }

    @Override
    public Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.callableStatement.getObject(n2, map);
    }

    @Override
    public Ref getRef(int n2) throws SQLException {
        return this.callableStatement.getRef(n2);
    }

    @Override
    public RowId getRowId(int n2) throws SQLException {
        return this.callableStatement.getRowId(n2);
    }

    @Override
    public short getShort(int n2) throws SQLException {
        return this.callableStatement.getShort(n2);
    }

    @Override
    public SQLXML getSQLXML(int n2) throws SQLException {
        return this.callableStatement.getSQLXML(n2);
    }

    @Override
    public String getString(int n2) throws SQLException {
        return this.callableStatement.getString(n2);
    }

    @Override
    public Time getTime(int n2) throws SQLException {
        return this.callableStatement.getTime(n2);
    }

    @Override
    public Time getTime(int n2, Calendar calendar) throws SQLException {
        return this.callableStatement.getTime(n2, calendar);
    }

    @Override
    public Timestamp getTimestamp(int n2) throws SQLException {
        return this.callableStatement.getTimestamp(n2);
    }

    @Override
    public Timestamp getTimestamp(int n2, Calendar calendar) throws SQLException {
        return this.callableStatement.getTimestamp(n2, calendar);
    }

    @Override
    public URL getURL(int n2) throws SQLException {
        return this.callableStatement.getURL(n2);
    }

    @Override
    public ARRAY getARRAY(int n2) throws SQLException {
        return this.callableStatement.getARRAY(n2);
    }

    @Override
    public BFILE getBFILE(int n2) throws SQLException {
        return this.callableStatement.getBFILE(n2);
    }

    @Override
    public BFILE getBfile(int n2) throws SQLException {
        return this.callableStatement.getBfile(n2);
    }

    @Override
    public BLOB getBLOB(int n2) throws SQLException {
        return this.callableStatement.getBLOB(n2);
    }

    @Override
    public CHAR getCHAR(int n2) throws SQLException {
        return this.callableStatement.getCHAR(n2);
    }

    @Override
    public CLOB getCLOB(int n2) throws SQLException {
        return this.callableStatement.getCLOB(n2);
    }

    @Override
    public ResultSet getCursor(int n2) throws SQLException {
        return this.callableStatement.getCursor(n2);
    }

    @Override
    public DATE getDATE(int n2) throws SQLException {
        return this.callableStatement.getDATE(n2);
    }

    @Override
    public INTERVALDS getINTERVALDS(int n2) throws SQLException {
        return this.callableStatement.getINTERVALDS(n2);
    }

    @Override
    public INTERVALYM getINTERVALYM(int n2) throws SQLException {
        return this.callableStatement.getINTERVALYM(n2);
    }

    @Override
    public NUMBER getNUMBER(int n2) throws SQLException {
        return this.callableStatement.getNUMBER(n2);
    }

    @Override
    public OPAQUE getOPAQUE(int n2) throws SQLException {
        return this.callableStatement.getOPAQUE(n2);
    }

    @Override
    public Datum getOracleObject(int n2) throws SQLException {
        return this.callableStatement.getOracleObject(n2);
    }

    @Override
    public Object getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        return this.callableStatement.getORAData(n2, oRADataFactory);
    }

    @Override
    public Object getObject(int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        return this.callableStatement.getObject(n2, oracleDataFactory);
    }

    @Override
    public RAW getRAW(int n2) throws SQLException {
        return this.callableStatement.getRAW(n2);
    }

    @Override
    public REF getREF(int n2) throws SQLException {
        return this.callableStatement.getREF(n2);
    }

    @Override
    public ROWID getROWID(int n2) throws SQLException {
        return this.callableStatement.getROWID(n2);
    }

    @Override
    public STRUCT getSTRUCT(int n2) throws SQLException {
        return this.callableStatement.getSTRUCT(n2);
    }

    @Override
    public TIMESTAMPLTZ getTIMESTAMPLTZ(int n2) throws SQLException {
        return this.callableStatement.getTIMESTAMPLTZ(n2);
    }

    @Override
    public TIMESTAMPTZ getTIMESTAMPTZ(int n2) throws SQLException {
        return this.callableStatement.getTIMESTAMPTZ(n2);
    }

    @Override
    public TIMESTAMP getTIMESTAMP(int n2) throws SQLException {
        return this.callableStatement.getTIMESTAMP(n2);
    }

    @Override
    public Object getCustomDatum(int n2, CustomDatumFactory customDatumFactory) throws SQLException {
        return this.callableStatement.getCustomDatum(n2, customDatumFactory);
    }

    @Override
    public InputStream getAsciiStream(int n2) throws SQLException {
        return this.callableStatement.getAsciiStream(n2);
    }

    @Override
    public InputStream getBinaryStream(int n2) throws SQLException {
        return this.callableStatement.getBinaryStream(n2);
    }

    @Override
    public Reader getCharacterStream(int n2) throws SQLException {
        return this.callableStatement.getCharacterStream(n2);
    }

    @Override
    public Reader getNCharacterStream(int n2) throws SQLException {
        return this.callableStatement.getNCharacterStream(n2);
    }

    @Override
    public InputStream getUnicodeStream(int n2) throws SQLException {
        return this.callableStatement.getUnicodeStream(n2);
    }

    @Override
    public Array getArray(String string) throws SQLException {
        return this.callableStatement.getArray(string);
    }

    @Override
    public BigDecimal getBigDecimal(String string) throws SQLException {
        return this.callableStatement.getBigDecimal(string);
    }

    @Override
    public BigDecimal getBigDecimal(String string, int n2) throws SQLException {
        return this.callableStatement.getBigDecimal(string, n2);
    }

    @Override
    public Blob getBlob(String string) throws SQLException {
        return this.callableStatement.getBlob(string);
    }

    @Override
    public boolean getBoolean(String string) throws SQLException {
        return this.callableStatement.getBoolean(string);
    }

    @Override
    public byte getByte(String string) throws SQLException {
        return this.callableStatement.getByte(string);
    }

    @Override
    public byte[] getBytes(String string) throws SQLException {
        return this.callableStatement.getBytes(string);
    }

    @Override
    public Clob getClob(String string) throws SQLException {
        return this.callableStatement.getClob(string);
    }

    @Override
    public Date getDate(String string) throws SQLException {
        return this.callableStatement.getDate(string);
    }

    @Override
    public Date getDate(String string, Calendar calendar) throws SQLException {
        return this.callableStatement.getDate(string, calendar);
    }

    @Override
    public double getDouble(String string) throws SQLException {
        return this.callableStatement.getDouble(string);
    }

    @Override
    public float getFloat(String string) throws SQLException {
        return this.callableStatement.getFloat(string);
    }

    @Override
    public int getInt(String string) throws SQLException {
        return this.callableStatement.getInt(string);
    }

    @Override
    public long getLong(String string) throws SQLException {
        return this.callableStatement.getLong(string);
    }

    @Override
    public NClob getNClob(String string) throws SQLException {
        return this.callableStatement.getNClob(string);
    }

    @Override
    public String getNString(String string) throws SQLException {
        return this.callableStatement.getNString(string);
    }

    @Override
    public Object getObject(String string) throws SQLException {
        return this.callableStatement.getObject(string);
    }

    @Override
    public Object getObject(String string, Map<String, Class<?>> map) throws SQLException {
        return this.callableStatement.getObject(string, map);
    }

    @Override
    public Ref getRef(String string) throws SQLException {
        return this.callableStatement.getRef(string);
    }

    @Override
    public RowId getRowId(String string) throws SQLException {
        return this.callableStatement.getRowId(string);
    }

    @Override
    public short getShort(String string) throws SQLException {
        return this.callableStatement.getShort(string);
    }

    @Override
    public SQLXML getSQLXML(String string) throws SQLException {
        return this.callableStatement.getSQLXML(string);
    }

    @Override
    public String getString(String string) throws SQLException {
        return this.callableStatement.getString(string);
    }

    @Override
    public Time getTime(String string) throws SQLException {
        return this.callableStatement.getTime(string);
    }

    @Override
    public Time getTime(String string, Calendar calendar) throws SQLException {
        return this.callableStatement.getTime(string, calendar);
    }

    @Override
    public Timestamp getTimestamp(String string) throws SQLException {
        return this.callableStatement.getTimestamp(string);
    }

    @Override
    public Timestamp getTimestamp(String string, Calendar calendar) throws SQLException {
        return this.callableStatement.getTimestamp(string, calendar);
    }

    @Override
    public URL getURL(String string) throws SQLException {
        return this.callableStatement.getURL(string);
    }

    @Override
    public InputStream getAsciiStream(String string) throws SQLException {
        return this.callableStatement.getAsciiStream(string);
    }

    @Override
    public InputStream getBinaryStream(String string) throws SQLException {
        return this.callableStatement.getBinaryStream(string);
    }

    @Override
    public Reader getCharacterStream(String string) throws SQLException {
        return this.callableStatement.getCharacterStream(string);
    }

    @Override
    public Reader getNCharacterStream(String string) throws SQLException {
        return this.callableStatement.getNCharacterStream(string);
    }

    @Override
    public InputStream getUnicodeStream(String string) throws SQLException {
        return this.callableStatement.getUnicodeStream(string);
    }

    @Override
    public void setObject(String string, Object object, int n2, int n3) throws SQLException {
        this.callableStatement.setObject(string, object, n2, n3);
    }

    @Override
    public <T> T getObject(String string, Class<T> clazz) throws SQLException {
        return this.callableStatement.getObject(string, clazz);
    }

    @Override
    public <T> T getObject(int n2, Class<T> clazz) throws SQLException {
        return this.callableStatement.getObject(n2, clazz);
    }

    @Override
    public Object getAnyDataEmbeddedObject(int n2) throws SQLException {
        return this.callableStatement.getAnyDataEmbeddedObject(n2);
    }

    @Override
    public void setStructDescriptor(int n2, StructDescriptor structDescriptor) throws SQLException {
        this.callableStatement.setStructDescriptor(n2, structDescriptor);
    }

    @Override
    public void setStructDescriptor(String string, StructDescriptor structDescriptor) throws SQLException {
        this.callableStatement.setStructDescriptor(string, structDescriptor);
    }

    @Override
    public void close() throws SQLException {
        super.close();
        this.callableStatement = OracleStatementWrapper.closedStatement;
    }

    @Override
    void beClosed(boolean bl) throws SQLException {
        super.beClosed(bl);
        this.callableStatement = bl ? closedStatementAC : closedStatement;
    }

    @Override
    public void closeWithKey(String string) throws SQLException {
        this.callableStatement.closeWithKey(string);
        this.callableStatement = closedStatement;
        this.preparedStatement = this.callableStatement;
        this.statement = this.callableStatement;
    }

    @Override
    public void registerOutParameter(int n2, int n3, int n4, int n5) throws SQLException {
        this.callableStatement.registerOutParameter(n2, n3, n4, n5);
    }

    @Override
    public void registerOutParameterBytes(int n2, int n3, int n4, int n5) throws SQLException {
        this.callableStatement.registerOutParameterBytes(n2, n3, n4, n5);
    }

    @Override
    public void registerOutParameterChars(int n2, int n3, int n4, int n5) throws SQLException {
        this.callableStatement.registerOutParameterChars(n2, n3, n4, n5);
    }

    @Override
    public void registerIndexTableOutParameter(int n2, int n3, int n4, int n5) throws SQLException {
        this.callableStatement.registerIndexTableOutParameter(n2, n3, n4, n5);
    }

    @Override
    public void registerOutParameter(String string, int n2, int n3, int n4) throws SQLException {
        this.callableStatement.registerOutParameter(string, n2, n3, n4);
    }

    @Override
    public void registerOutParameter(String string, int n2, String string2) throws SQLException {
        this.callableStatement.registerOutParameter(string, n2, string2);
    }

    @Override
    public int sendBatch() throws SQLException {
        return this.callableStatement.sendBatch();
    }

    @Override
    public void setExecuteBatch(int n2) throws SQLException {
        this.callableStatement.setExecuteBatch(n2);
    }

    @Override
    public Object getPlsqlIndexTable(int n2) throws SQLException {
        return this.callableStatement.getPlsqlIndexTable(n2);
    }

    @Override
    public Object getPlsqlIndexTable(int n2, Class<?> clazz) throws SQLException {
        return this.callableStatement.getPlsqlIndexTable(n2, clazz);
    }

    @Override
    public Datum[] getOraclePlsqlIndexTable(int n2) throws SQLException {
        return this.callableStatement.getOraclePlsqlIndexTable(n2);
    }

    @Override
    public void setStringForClob(String string, String string2) throws SQLException {
        this.callableStatement.setStringForClob(string, string2);
    }

    @Override
    public void setBytesForBlob(String string, byte[] byArray) throws SQLException {
        this.callableStatement.setBytesForBlob(string, byArray);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return this.callableStatement.wasNull();
    }

    @Override
    public void registerOutParameter(int n2, int n3) throws SQLException {
        this.callableStatement.registerOutParameter(n2, n3);
    }

    @Override
    public void registerOutParameter(int n2, int n3, int n4) throws SQLException {
        this.callableStatement.registerOutParameter(n2, n3, n4);
    }

    @Override
    public void registerOutParameter(int n2, int n3, String string) throws SQLException {
        this.callableStatement.registerOutParameter(n2, n3, string);
    }

    @Override
    public void registerOutParameter(String string, int n2) throws SQLException {
        this.callableStatement.registerOutParameter(string, n2);
    }

    @Override
    public void registerOutParameter(String string, int n2, int n3) throws SQLException {
        this.callableStatement.registerOutParameter(string, n2, n3);
    }

    @Override
    public void registerOutParameterAtName(String string, int n2) throws SQLException {
        this.callableStatement.registerOutParameterAtName(string, n2);
    }

    @Override
    public void registerOutParameterAtName(String string, int n2, int n3) throws SQLException {
        this.callableStatement.registerOutParameterAtName(string, n2, n3);
    }

    @Override
    public void registerOutParameterAtName(String string, int n2, String string2) throws SQLException {
        this.callableStatement.registerOutParameterAtName(string, n2, string2);
    }

    @Override
    public byte[] privateGetBytes(int n2) throws SQLException {
        return ((oracle.jdbc.driver.OracleCallableStatement)this.callableStatement).privateGetBytes(n2);
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
    public void registerOutParameter(int n2, SQLType sQLType) throws SQLException {
        this.callableStatement.registerOutParameter(n2, sQLType);
    }

    @Override
    public void registerOutParameter(int n2, SQLType sQLType, int n3) throws SQLException {
        this.callableStatement.registerOutParameter(n2, sQLType, n3);
    }

    @Override
    public void registerOutParameter(int n2, SQLType sQLType, String string) throws SQLException {
        this.callableStatement.registerOutParameter(n2, sQLType, string);
    }

    @Override
    public void registerOutParameter(String string, SQLType sQLType) throws SQLException {
        this.callableStatement.registerOutParameter(string, sQLType);
    }

    @Override
    public void registerOutParameter(String string, SQLType sQLType, int n2) throws SQLException {
        this.callableStatement.registerOutParameter(string, sQLType, n2);
    }

    @Override
    public void registerOutParameter(String string, SQLType sQLType, String string2) throws SQLException {
        this.callableStatement.registerOutParameter(string, sQLType, string2);
    }

    @Override
    public void setObject(String string, Object object, SQLType sQLType) throws SQLException {
        this.callableStatement.setObject(string, object, sQLType);
    }

    @Override
    public void setObject(String string, Object object, SQLType sQLType, int n2) throws SQLException {
        this.callableStatement.setObject(string, object, sQLType, n2);
    }
}

