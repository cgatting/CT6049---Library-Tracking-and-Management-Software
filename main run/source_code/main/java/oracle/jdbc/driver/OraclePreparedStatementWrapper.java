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
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import oracle.jdbc.OracleParameterMetaData;
import oracle.jdbc.OraclePreparedStatement;
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
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
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
class OraclePreparedStatementWrapper
extends OracleStatementWrapper
implements oracle.jdbc.internal.OraclePreparedStatement {
    protected oracle.jdbc.internal.OraclePreparedStatement preparedStatement = null;
    Object acProxy;

    OraclePreparedStatementWrapper(OraclePreparedStatement oraclePreparedStatement) throws SQLException {
        super(oraclePreparedStatement);
        this.preparedStatement = (oracle.jdbc.internal.OraclePreparedStatement)oraclePreparedStatement;
    }

    @Override
    public void close() throws SQLException {
        super.close();
        this.preparedStatement = OracleStatementWrapper.closedStatement;
    }

    @Override
    void beClosed(boolean bl) throws SQLException {
        super.beClosed(bl);
        this.preparedStatement = bl ? closedStatementAC : closedStatement;
    }

    @Override
    public void closeWithKey(String string) throws SQLException {
        this.preparedStatement.closeWithKey(string);
        this.preparedStatement = closedStatement;
        this.statement = this.preparedStatement;
    }

    @Override
    public void setArray(int n2, Array array) throws SQLException {
        this.preparedStatement.setArray(n2, array);
    }

    @Override
    public void setBigDecimal(int n2, BigDecimal bigDecimal) throws SQLException {
        this.preparedStatement.setBigDecimal(n2, bigDecimal);
    }

    @Override
    public void setBlob(int n2, Blob blob) throws SQLException {
        this.preparedStatement.setBlob(n2, blob);
    }

    @Override
    public void setBoolean(int n2, boolean bl) throws SQLException {
        this.preparedStatement.setBoolean(n2, bl);
    }

    @Override
    public void setByte(int n2, byte by) throws SQLException {
        this.preparedStatement.setByte(n2, by);
    }

    @Override
    public void setBytes(int n2, byte[] byArray) throws SQLException {
        this.preparedStatement.setBytes(n2, byArray);
    }

    @Override
    public void setClob(int n2, Clob clob) throws SQLException {
        this.preparedStatement.setClob(n2, clob);
    }

    @Override
    public void setDate(int n2, Date date) throws SQLException {
        this.preparedStatement.setDate(n2, date);
    }

    @Override
    public void setDate(int n2, Date date, Calendar calendar) throws SQLException {
        this.preparedStatement.setDate(n2, date, calendar);
    }

    @Override
    public void setDouble(int n2, double d2) throws SQLException {
        this.preparedStatement.setDouble(n2, d2);
    }

    @Override
    public void setFloat(int n2, float f2) throws SQLException {
        this.preparedStatement.setFloat(n2, f2);
    }

    @Override
    public void setInt(int n2, int n3) throws SQLException {
        this.preparedStatement.setInt(n2, n3);
    }

    @Override
    public void setLong(int n2, long l2) throws SQLException {
        this.preparedStatement.setLong(n2, l2);
    }

    @Override
    public void setNClob(int n2, NClob nClob) throws SQLException {
        this.preparedStatement.setNClob(n2, nClob);
    }

    @Override
    public void setNString(int n2, String string) throws SQLException {
        this.preparedStatement.setNString(n2, string);
    }

    @Override
    public void setObject(int n2, Object object) throws SQLException {
        this.preparedStatement.setObject(n2, object);
    }

    @Override
    public void setObject(int n2, Object object, int n3) throws SQLException {
        this.preparedStatement.setObject(n2, object, n3);
    }

    @Override
    public void setRef(int n2, Ref ref) throws SQLException {
        this.preparedStatement.setRef(n2, ref);
    }

    @Override
    public void setRowId(int n2, RowId rowId) throws SQLException {
        this.preparedStatement.setRowId(n2, rowId);
    }

    @Override
    public void setShort(int n2, short s2) throws SQLException {
        this.preparedStatement.setShort(n2, s2);
    }

    @Override
    public void setSQLXML(int n2, SQLXML sQLXML) throws SQLException {
        this.preparedStatement.setSQLXML(n2, sQLXML);
    }

    @Override
    public void setString(int n2, String string) throws SQLException {
        this.preparedStatement.setString(n2, string);
    }

    @Override
    public void setTime(int n2, Time time) throws SQLException {
        this.preparedStatement.setTime(n2, time);
    }

    @Override
    public void setTime(int n2, Time time, Calendar calendar) throws SQLException {
        this.preparedStatement.setTime(n2, time, calendar);
    }

    @Override
    public void setTimestamp(int n2, Timestamp timestamp) throws SQLException {
        this.preparedStatement.setTimestamp(n2, timestamp);
    }

    @Override
    public void setTimestamp(int n2, Timestamp timestamp, Calendar calendar) throws SQLException {
        this.preparedStatement.setTimestamp(n2, timestamp, calendar);
    }

    @Override
    public void setURL(int n2, URL uRL) throws SQLException {
        this.preparedStatement.setURL(n2, uRL);
    }

    @Override
    public void setARRAY(int n2, ARRAY aRRAY) throws SQLException {
        this.preparedStatement.setARRAY(n2, aRRAY);
    }

    @Override
    public void setBFILE(int n2, BFILE bFILE) throws SQLException {
        this.preparedStatement.setBFILE(n2, bFILE);
    }

    @Override
    public void setBfile(int n2, BFILE bFILE) throws SQLException {
        this.preparedStatement.setBfile(n2, bFILE);
    }

    @Override
    public void setBinaryFloat(int n2, float f2) throws SQLException {
        this.preparedStatement.setBinaryFloat(n2, f2);
    }

    @Override
    public void setBinaryFloat(int n2, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        this.preparedStatement.setBinaryFloat(n2, bINARY_FLOAT);
    }

    @Override
    public void setBinaryDouble(int n2, double d2) throws SQLException {
        this.preparedStatement.setBinaryDouble(n2, d2);
    }

    @Override
    public void setBinaryDouble(int n2, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        this.preparedStatement.setBinaryDouble(n2, bINARY_DOUBLE);
    }

    @Override
    public void setBLOB(int n2, BLOB bLOB) throws SQLException {
        this.preparedStatement.setBLOB(n2, bLOB);
    }

    @Override
    public void setCHAR(int n2, CHAR cHAR) throws SQLException {
        this.preparedStatement.setCHAR(n2, cHAR);
    }

    @Override
    public void setCLOB(int n2, CLOB cLOB) throws SQLException {
        this.preparedStatement.setCLOB(n2, cLOB);
    }

    @Override
    public void setCursor(int n2, ResultSet resultSet) throws SQLException {
        this.preparedStatement.setCursor(n2, resultSet);
    }

    @Override
    public void setDATE(int n2, DATE dATE) throws SQLException {
        this.preparedStatement.setDATE(n2, dATE);
    }

    @Override
    public void setFixedCHAR(int n2, String string) throws SQLException {
        this.preparedStatement.setFixedCHAR(n2, string);
    }

    @Override
    public void setINTERVALDS(int n2, INTERVALDS iNTERVALDS) throws SQLException {
        this.preparedStatement.setINTERVALDS(n2, iNTERVALDS);
    }

    @Override
    public void setINTERVALYM(int n2, INTERVALYM iNTERVALYM) throws SQLException {
        this.preparedStatement.setINTERVALYM(n2, iNTERVALYM);
    }

    @Override
    public void setNUMBER(int n2, NUMBER nUMBER) throws SQLException {
        this.preparedStatement.setNUMBER(n2, nUMBER);
    }

    @Override
    public void setOPAQUE(int n2, OPAQUE oPAQUE) throws SQLException {
        this.preparedStatement.setOPAQUE(n2, oPAQUE);
    }

    @Override
    public void setOracleObject(int n2, Datum datum) throws SQLException {
        this.preparedStatement.setOracleObject(n2, datum);
    }

    @Override
    public void setORAData(int n2, ORAData oRAData) throws SQLException {
        this.preparedStatement.setORAData(n2, oRAData);
    }

    @Override
    public void setRAW(int n2, RAW rAW) throws SQLException {
        this.preparedStatement.setRAW(n2, rAW);
    }

    @Override
    public void setREF(int n2, REF rEF) throws SQLException {
        this.preparedStatement.setREF(n2, rEF);
    }

    @Override
    public void setRefType(int n2, REF rEF) throws SQLException {
        this.preparedStatement.setRefType(n2, rEF);
    }

    @Override
    public void setROWID(int n2, ROWID rOWID) throws SQLException {
        this.preparedStatement.setROWID(n2, rOWID);
    }

    @Override
    public void setSTRUCT(int n2, STRUCT sTRUCT) throws SQLException {
        this.preparedStatement.setSTRUCT(n2, sTRUCT);
    }

    @Override
    public void setTIMESTAMPLTZ(int n2, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        this.preparedStatement.setTIMESTAMPLTZ(n2, tIMESTAMPLTZ);
    }

    @Override
    public void setTIMESTAMPTZ(int n2, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        this.preparedStatement.setTIMESTAMPTZ(n2, tIMESTAMPTZ);
    }

    @Override
    public void setTIMESTAMP(int n2, TIMESTAMP tIMESTAMP) throws SQLException {
        this.preparedStatement.setTIMESTAMP(n2, tIMESTAMP);
    }

    @Override
    public void setCustomDatum(int n2, CustomDatum customDatum) throws SQLException {
        this.preparedStatement.setCustomDatum(n2, customDatum);
    }

    @Override
    public void setBlob(int n2, InputStream inputStream) throws SQLException {
        this.preparedStatement.setBlob(n2, inputStream);
    }

    @Override
    public void setBlob(int n2, InputStream inputStream, long l2) throws SQLException {
        this.preparedStatement.setBlob(n2, inputStream, l2);
    }

    @Override
    public void setClob(int n2, Reader reader) throws SQLException {
        this.preparedStatement.setClob(n2, reader);
    }

    @Override
    public void setClob(int n2, Reader reader, long l2) throws SQLException {
        this.preparedStatement.setClob(n2, reader, l2);
    }

    @Override
    public void setNClob(int n2, Reader reader) throws SQLException {
        this.preparedStatement.setNClob(n2, reader);
    }

    @Override
    public void setNClob(int n2, Reader reader, long l2) throws SQLException {
        this.preparedStatement.setNClob(n2, reader, l2);
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream) throws SQLException {
        this.preparedStatement.setAsciiStream(n2, inputStream);
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream, int n3) throws SQLException {
        this.preparedStatement.setAsciiStream(n2, inputStream, n3);
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream, long l2) throws SQLException {
        this.preparedStatement.setAsciiStream(n2, inputStream, l2);
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream) throws SQLException {
        this.preparedStatement.setBinaryStream(n2, inputStream);
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream, int n3) throws SQLException {
        this.preparedStatement.setBinaryStream(n2, inputStream, n3);
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream, long l2) throws SQLException {
        this.preparedStatement.setBinaryStream(n2, inputStream, l2);
    }

    @Override
    public void setCharacterStream(int n2, Reader reader) throws SQLException {
        this.preparedStatement.setCharacterStream(n2, reader);
    }

    @Override
    public void setCharacterStream(int n2, Reader reader, int n3) throws SQLException {
        this.preparedStatement.setCharacterStream(n2, reader, n3);
    }

    @Override
    public void setCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        this.preparedStatement.setCharacterStream(n2, reader, l2);
    }

    @Override
    public void setNCharacterStream(int n2, Reader reader) throws SQLException {
        this.preparedStatement.setNCharacterStream(n2, reader);
    }

    @Override
    public void setNCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        this.preparedStatement.setNCharacterStream(n2, reader, l2);
    }

    @Override
    public void setUnicodeStream(int n2, InputStream inputStream, int n3) throws SQLException {
        this.preparedStatement.setUnicodeStream(n2, inputStream, n3);
    }

    @Override
    public void setArrayAtName(String string, Array array) throws SQLException {
        this.preparedStatement.setArrayAtName(string, array);
    }

    @Override
    public void setBigDecimalAtName(String string, BigDecimal bigDecimal) throws SQLException {
        this.preparedStatement.setBigDecimalAtName(string, bigDecimal);
    }

    @Override
    public void setBlobAtName(String string, Blob blob) throws SQLException {
        this.preparedStatement.setBlobAtName(string, blob);
    }

    @Override
    public void setBooleanAtName(String string, boolean bl) throws SQLException {
        this.preparedStatement.setBooleanAtName(string, bl);
    }

    @Override
    public void setByteAtName(String string, byte by) throws SQLException {
        this.preparedStatement.setByteAtName(string, by);
    }

    @Override
    public void setBytesAtName(String string, byte[] byArray) throws SQLException {
        this.preparedStatement.setBytesAtName(string, byArray);
    }

    @Override
    public void setClobAtName(String string, Clob clob) throws SQLException {
        this.preparedStatement.setClobAtName(string, clob);
    }

    @Override
    public void setDateAtName(String string, Date date) throws SQLException {
        this.preparedStatement.setDateAtName(string, date);
    }

    @Override
    public void setDateAtName(String string, Date date, Calendar calendar) throws SQLException {
        this.preparedStatement.setDateAtName(string, date, calendar);
    }

    @Override
    public void setDoubleAtName(String string, double d2) throws SQLException {
        this.preparedStatement.setDoubleAtName(string, d2);
    }

    @Override
    public void setFloatAtName(String string, float f2) throws SQLException {
        this.preparedStatement.setFloatAtName(string, f2);
    }

    @Override
    public void setIntAtName(String string, int n2) throws SQLException {
        this.preparedStatement.setIntAtName(string, n2);
    }

    @Override
    public void setLongAtName(String string, long l2) throws SQLException {
        this.preparedStatement.setLongAtName(string, l2);
    }

    @Override
    public void setNClobAtName(String string, NClob nClob) throws SQLException {
        this.preparedStatement.setNClobAtName(string, nClob);
    }

    @Override
    public void setNStringAtName(String string, String string2) throws SQLException {
        this.preparedStatement.setNStringAtName(string, string2);
    }

    @Override
    public void setObjectAtName(String string, Object object) throws SQLException {
        this.preparedStatement.setObjectAtName(string, object);
    }

    @Override
    public void setObjectAtName(String string, Object object, int n2) throws SQLException {
        this.preparedStatement.setObjectAtName(string, object, n2);
    }

    @Override
    public void setRefAtName(String string, Ref ref) throws SQLException {
        this.preparedStatement.setRefAtName(string, ref);
    }

    @Override
    public void setRowIdAtName(String string, RowId rowId) throws SQLException {
        this.preparedStatement.setRowIdAtName(string, rowId);
    }

    @Override
    public void setShortAtName(String string, short s2) throws SQLException {
        this.preparedStatement.setShortAtName(string, s2);
    }

    @Override
    public void setSQLXMLAtName(String string, SQLXML sQLXML) throws SQLException {
        this.preparedStatement.setSQLXMLAtName(string, sQLXML);
    }

    @Override
    public void setStringAtName(String string, String string2) throws SQLException {
        this.preparedStatement.setStringAtName(string, string2);
    }

    @Override
    public void setTimeAtName(String string, Time time) throws SQLException {
        this.preparedStatement.setTimeAtName(string, time);
    }

    @Override
    public void setTimeAtName(String string, Time time, Calendar calendar) throws SQLException {
        this.preparedStatement.setTimeAtName(string, time, calendar);
    }

    @Override
    public void setTimestampAtName(String string, Timestamp timestamp) throws SQLException {
        this.preparedStatement.setTimestampAtName(string, timestamp);
    }

    @Override
    public void setTimestampAtName(String string, Timestamp timestamp, Calendar calendar) throws SQLException {
        this.preparedStatement.setTimestampAtName(string, timestamp, calendar);
    }

    @Override
    public void setURLAtName(String string, URL uRL) throws SQLException {
        this.preparedStatement.setURLAtName(string, uRL);
    }

    @Override
    public void setARRAYAtName(String string, ARRAY aRRAY) throws SQLException {
        this.preparedStatement.setARRAYAtName(string, aRRAY);
    }

    @Override
    public void setBFILEAtName(String string, BFILE bFILE) throws SQLException {
        this.preparedStatement.setBFILEAtName(string, bFILE);
    }

    @Override
    public void setBfileAtName(String string, BFILE bFILE) throws SQLException {
        this.preparedStatement.setBfileAtName(string, bFILE);
    }

    @Override
    public void setBinaryFloatAtName(String string, float f2) throws SQLException {
        this.preparedStatement.setBinaryFloatAtName(string, f2);
    }

    @Override
    public void setBinaryFloatAtName(String string, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        this.preparedStatement.setBinaryFloatAtName(string, bINARY_FLOAT);
    }

    @Override
    public void setBinaryDoubleAtName(String string, double d2) throws SQLException {
        this.preparedStatement.setBinaryDoubleAtName(string, d2);
    }

    @Override
    public void setBinaryDoubleAtName(String string, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        this.preparedStatement.setBinaryDoubleAtName(string, bINARY_DOUBLE);
    }

    @Override
    public void setBLOBAtName(String string, BLOB bLOB) throws SQLException {
        this.preparedStatement.setBLOBAtName(string, bLOB);
    }

    @Override
    public void setCHARAtName(String string, CHAR cHAR) throws SQLException {
        this.preparedStatement.setCHARAtName(string, cHAR);
    }

    @Override
    public void setCLOBAtName(String string, CLOB cLOB) throws SQLException {
        this.preparedStatement.setCLOBAtName(string, cLOB);
    }

    @Override
    public void setCursorAtName(String string, ResultSet resultSet) throws SQLException {
        this.preparedStatement.setCursorAtName(string, resultSet);
    }

    @Override
    public void setDATEAtName(String string, DATE dATE) throws SQLException {
        this.preparedStatement.setDATEAtName(string, dATE);
    }

    @Override
    public void setFixedCHARAtName(String string, String string2) throws SQLException {
        this.preparedStatement.setFixedCHARAtName(string, string2);
    }

    @Override
    public void setINTERVALDSAtName(String string, INTERVALDS iNTERVALDS) throws SQLException {
        this.preparedStatement.setINTERVALDSAtName(string, iNTERVALDS);
    }

    @Override
    public void setINTERVALYMAtName(String string, INTERVALYM iNTERVALYM) throws SQLException {
        this.preparedStatement.setINTERVALYMAtName(string, iNTERVALYM);
    }

    @Override
    public void setNUMBERAtName(String string, NUMBER nUMBER) throws SQLException {
        this.preparedStatement.setNUMBERAtName(string, nUMBER);
    }

    @Override
    public void setOPAQUEAtName(String string, OPAQUE oPAQUE) throws SQLException {
        this.preparedStatement.setOPAQUEAtName(string, oPAQUE);
    }

    @Override
    public void setOracleObjectAtName(String string, Datum datum) throws SQLException {
        this.preparedStatement.setOracleObjectAtName(string, datum);
    }

    @Override
    public void setORADataAtName(String string, ORAData oRAData) throws SQLException {
        this.preparedStatement.setORADataAtName(string, oRAData);
    }

    @Override
    public void setRAWAtName(String string, RAW rAW) throws SQLException {
        this.preparedStatement.setRAWAtName(string, rAW);
    }

    @Override
    public void setREFAtName(String string, REF rEF) throws SQLException {
        this.preparedStatement.setREFAtName(string, rEF);
    }

    @Override
    public void setRefTypeAtName(String string, REF rEF) throws SQLException {
        this.preparedStatement.setRefTypeAtName(string, rEF);
    }

    @Override
    public void setROWIDAtName(String string, ROWID rOWID) throws SQLException {
        this.preparedStatement.setROWIDAtName(string, rOWID);
    }

    @Override
    public void setSTRUCTAtName(String string, STRUCT sTRUCT) throws SQLException {
        this.preparedStatement.setSTRUCTAtName(string, sTRUCT);
    }

    @Override
    public void setTIMESTAMPLTZAtName(String string, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        this.preparedStatement.setTIMESTAMPLTZAtName(string, tIMESTAMPLTZ);
    }

    @Override
    public void setTIMESTAMPTZAtName(String string, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        this.preparedStatement.setTIMESTAMPTZAtName(string, tIMESTAMPTZ);
    }

    @Override
    public void setTIMESTAMPAtName(String string, TIMESTAMP tIMESTAMP) throws SQLException {
        this.preparedStatement.setTIMESTAMPAtName(string, tIMESTAMP);
    }

    @Override
    public void setCustomDatumAtName(String string, CustomDatum customDatum) throws SQLException {
        this.preparedStatement.setCustomDatumAtName(string, customDatum);
    }

    @Override
    public void setBlobAtName(String string, InputStream inputStream) throws SQLException {
        this.preparedStatement.setBlobAtName(string, inputStream);
    }

    @Override
    public void setBlobAtName(String string, InputStream inputStream, long l2) throws SQLException {
        this.preparedStatement.setBlobAtName(string, inputStream, l2);
    }

    @Override
    public void setClobAtName(String string, Reader reader) throws SQLException {
        this.preparedStatement.setClobAtName(string, reader);
    }

    @Override
    public void setClobAtName(String string, Reader reader, long l2) throws SQLException {
        this.preparedStatement.setClobAtName(string, reader, l2);
    }

    @Override
    public void setNClobAtName(String string, Reader reader) throws SQLException {
        this.preparedStatement.setNClobAtName(string, reader);
    }

    @Override
    public void setNClobAtName(String string, Reader reader, long l2) throws SQLException {
        this.preparedStatement.setNClobAtName(string, reader, l2);
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream inputStream) throws SQLException {
        this.preparedStatement.setAsciiStreamAtName(string, inputStream);
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream inputStream, int n2) throws SQLException {
        this.preparedStatement.setAsciiStreamAtName(string, inputStream, n2);
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream inputStream, long l2) throws SQLException {
        this.preparedStatement.setAsciiStreamAtName(string, inputStream, l2);
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream inputStream) throws SQLException {
        this.preparedStatement.setBinaryStreamAtName(string, inputStream);
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream inputStream, int n2) throws SQLException {
        this.preparedStatement.setBinaryStreamAtName(string, inputStream, n2);
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream inputStream, long l2) throws SQLException {
        this.preparedStatement.setBinaryStreamAtName(string, inputStream, l2);
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader) throws SQLException {
        this.preparedStatement.setCharacterStreamAtName(string, reader);
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader, int n2) throws SQLException {
        this.preparedStatement.setCharacterStreamAtName(string, reader, n2);
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader, long l2) throws SQLException {
        this.preparedStatement.setCharacterStreamAtName(string, reader, l2);
    }

    @Override
    public void setNCharacterStreamAtName(String string, Reader reader) throws SQLException {
        this.preparedStatement.setNCharacterStreamAtName(string, reader);
    }

    @Override
    public void setNCharacterStreamAtName(String string, Reader reader, long l2) throws SQLException {
        this.preparedStatement.setNCharacterStreamAtName(string, reader, l2);
    }

    @Override
    public void setUnicodeStreamAtName(String string, InputStream inputStream, int n2) throws SQLException {
        this.preparedStatement.setUnicodeStreamAtName(string, inputStream, n2);
    }

    @Override
    public void setNull(int n2, int n3) throws SQLException {
        this.preparedStatement.setNull(n2, n3);
    }

    @Override
    public void setNull(int n2, int n3, String string) throws SQLException {
        this.preparedStatement.setNull(n2, n3, string);
    }

    @Override
    public void setNullAtName(String string, int n2) throws SQLException {
        this.preparedStatement.setNullAtName(string, n2);
    }

    @Override
    public void setNullAtName(String string, int n2, String string2) throws SQLException {
        this.preparedStatement.setNullAtName(string, n2, string2);
    }

    @Override
    public void setObject(int n2, Object object, int n3, int n4) throws SQLException {
        this.preparedStatement.setObject(n2, object, n3, n4);
    }

    @Override
    public void setObjectAtName(String string, Object object, int n2, int n3) throws SQLException {
        this.preparedStatement.setObjectAtName(string, object, n2, n3);
    }

    @Override
    public void setStructDescriptor(int n2, StructDescriptor structDescriptor) throws SQLException {
        this.preparedStatement.setStructDescriptor(n2, structDescriptor);
    }

    @Override
    public void setStructDescriptorAtName(String string, StructDescriptor structDescriptor) throws SQLException {
        this.preparedStatement.setStructDescriptorAtName(string, structDescriptor);
    }

    @Override
    public int executeUpdate() throws SQLException {
        return this.preparedStatement.executeUpdate();
    }

    @Override
    public void addBatch() throws SQLException {
        this.preparedStatement.addBatch();
    }

    @Override
    public void clearParameters() throws SQLException {
        this.preparedStatement.clearParameters();
    }

    @Override
    public boolean execute() throws SQLException {
        return this.preparedStatement.execute();
    }

    @Override
    public void setCheckBindTypes(boolean bl) {
        this.preparedStatement.setCheckBindTypes(bl);
    }

    @Override
    public ResultSet getReturnResultSet() throws SQLException {
        return this.preparedStatement.getReturnResultSet();
    }

    @Override
    public void defineParameterTypeBytes(int n2, int n3, int n4) throws SQLException {
        this.preparedStatement.defineParameterTypeBytes(n2, n3, n4);
    }

    @Override
    public void defineParameterTypeChars(int n2, int n3, int n4) throws SQLException {
        this.preparedStatement.defineParameterTypeChars(n2, n3, n4);
    }

    @Override
    public void defineParameterType(int n2, int n3, int n4) throws SQLException {
        this.preparedStatement.defineParameterType(n2, n3, n4);
    }

    @Override
    public int getExecuteBatch() {
        return this.preparedStatement.getExecuteBatch();
    }

    @Override
    public int sendBatch() throws SQLException {
        return this.preparedStatement.sendBatch();
    }

    @Override
    public void setPlsqlIndexTable(int n2, Object object, int n3, int n4, int n5, int n6) throws SQLException {
        this.preparedStatement.setPlsqlIndexTable(n2, object, n3, n4, n5, n6);
    }

    @Override
    public void setFormOfUse(int n2, short s2) {
        this.preparedStatement.setFormOfUse(n2, s2);
    }

    @Override
    public void setDisableStmtCaching(boolean bl) {
        this.preparedStatement.setDisableStmtCaching(bl);
    }

    @Override
    public OracleParameterMetaData OracleGetParameterMetaData() throws SQLException {
        return this.preparedStatement.OracleGetParameterMetaData();
    }

    @Override
    public void registerReturnParameter(int n2, int n3) throws SQLException {
        this.preparedStatement.registerReturnParameter(n2, n3);
    }

    @Override
    public void registerReturnParameter(int n2, int n3, int n4) throws SQLException {
        this.preparedStatement.registerReturnParameter(n2, n3, n4);
    }

    @Override
    public void registerReturnParameter(int n2, int n3, String string) throws SQLException {
        this.preparedStatement.registerReturnParameter(n2, n3, string);
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return this.preparedStatement.executeQuery();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.preparedStatement.getMetaData();
    }

    @Override
    public void setBytesForBlob(int n2, byte[] byArray) throws SQLException {
        this.preparedStatement.setBytesForBlob(n2, byArray);
    }

    @Override
    public void setBytesForBlobAtName(String string, byte[] byArray) throws SQLException {
        this.preparedStatement.setBytesForBlobAtName(string, byArray);
    }

    @Override
    public void setStringForClob(int n2, String string) throws SQLException {
        this.preparedStatement.setStringForClob(n2, string);
    }

    @Override
    public void setStringForClobAtName(String string, String string2) throws SQLException {
        this.preparedStatement.setStringForClobAtName(string, string2);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return this.preparedStatement.getParameterMetaData();
    }

    @Override
    public void setExecuteBatch(int n2) throws SQLException {
        this.preparedStatement.setExecuteBatch(n2);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return this.preparedStatement.isPoolable();
    }

    @Override
    public void setPoolable(boolean bl) throws SQLException {
        this.preparedStatement.setPoolable(bl);
    }

    @Override
    public void setInternalBytes(int n2, byte[] byArray, int n3) throws SQLException {
        this.preparedStatement.setInternalBytes(n2, byArray, n3);
    }

    @Override
    public void enterImplicitCache() throws SQLException {
        this.preparedStatement.enterImplicitCache();
    }

    @Override
    public void enterExplicitCache() throws SQLException {
        this.preparedStatement.enterExplicitCache();
    }

    @Override
    public void exitImplicitCacheToActive() throws SQLException {
        this.preparedStatement.exitImplicitCacheToActive();
    }

    @Override
    public void exitExplicitCacheToActive() throws SQLException {
        this.preparedStatement.exitExplicitCacheToActive();
    }

    @Override
    public void exitImplicitCacheToClose() throws SQLException {
        this.preparedStatement.exitImplicitCacheToClose();
    }

    @Override
    public void exitExplicitCacheToClose() throws SQLException {
        this.preparedStatement.exitExplicitCacheToClose();
    }

    @Override
    public String getOriginalSql() throws SQLException {
        return this.preparedStatement.getOriginalSql();
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
    public void setObject(int n2, Object object, SQLType sQLType) throws SQLException {
        this.preparedStatement.setObject(n2, object, sQLType);
    }

    @Override
    public void setObject(int n2, Object object, SQLType sQLType, int n3) throws SQLException {
        this.preparedStatement.setObject(n2, object, sQLType, n3);
    }

    @Override
    public long executeLargeUpdate() throws SQLException {
        return this.preparedStatement.executeLargeUpdate();
    }
}

