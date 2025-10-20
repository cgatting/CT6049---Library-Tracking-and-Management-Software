/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OraclePreparedStatement;
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

public interface OracleCallableStatement
extends CallableStatement,
OraclePreparedStatement {
    public ARRAY getARRAY(int var1) throws SQLException;

    public InputStream getAsciiStream(int var1) throws SQLException;

    public BFILE getBFILE(int var1) throws SQLException;

    public BFILE getBfile(int var1) throws SQLException;

    public InputStream getBinaryStream(int var1) throws SQLException;

    public InputStream getBinaryStream(String var1) throws SQLException;

    public BLOB getBLOB(int var1) throws SQLException;

    public CHAR getCHAR(int var1) throws SQLException;

    @Override
    public Reader getCharacterStream(int var1) throws SQLException;

    public CLOB getCLOB(int var1) throws SQLException;

    public ResultSet getCursor(int var1) throws SQLException;

    public Object getCustomDatum(int var1, CustomDatumFactory var2) throws SQLException;

    public Object getORAData(int var1, ORADataFactory var2) throws SQLException;

    public Object getObject(int var1, OracleDataFactory var2) throws SQLException;

    public Object getAnyDataEmbeddedObject(int var1) throws SQLException;

    public DATE getDATE(int var1) throws SQLException;

    public NUMBER getNUMBER(int var1) throws SQLException;

    public OPAQUE getOPAQUE(int var1) throws SQLException;

    public Datum getOracleObject(int var1) throws SQLException;

    public RAW getRAW(int var1) throws SQLException;

    public REF getREF(int var1) throws SQLException;

    public ROWID getROWID(int var1) throws SQLException;

    public STRUCT getSTRUCT(int var1) throws SQLException;

    public INTERVALYM getINTERVALYM(int var1) throws SQLException;

    public INTERVALDS getINTERVALDS(int var1) throws SQLException;

    public TIMESTAMP getTIMESTAMP(int var1) throws SQLException;

    public TIMESTAMPTZ getTIMESTAMPTZ(int var1) throws SQLException;

    public TIMESTAMPLTZ getTIMESTAMPLTZ(int var1) throws SQLException;

    public InputStream getUnicodeStream(int var1) throws SQLException;

    public InputStream getUnicodeStream(String var1) throws SQLException;

    public void registerOutParameter(int var1, int var2, int var3, int var4) throws SQLException;

    public void registerOutParameterBytes(int var1, int var2, int var3, int var4) throws SQLException;

    public void registerOutParameterChars(int var1, int var2, int var3, int var4) throws SQLException;

    @Override
    public int sendBatch() throws SQLException;

    @Override
    public void setExecuteBatch(int var1) throws SQLException;

    @Deprecated
    public Object getPlsqlIndexTable(int var1) throws SQLException;

    @Deprecated
    public Object getPlsqlIndexTable(int var1, Class<?> var2) throws SQLException;

    @Deprecated
    public Datum[] getOraclePlsqlIndexTable(int var1) throws SQLException;

    @Deprecated
    public void registerIndexTableOutParameter(int var1, int var2, int var3, int var4) throws SQLException;

    public void setBinaryFloat(String var1, BINARY_FLOAT var2) throws SQLException;

    public void setBinaryDouble(String var1, BINARY_DOUBLE var2) throws SQLException;

    public void setStringForClob(String var1, String var2) throws SQLException;

    public void setBytesForBlob(String var1, byte[] var2) throws SQLException;

    public void registerOutParameter(String var1, int var2, int var3, int var4) throws SQLException;

    @Override
    public void setNull(String var1, int var2, String var3) throws SQLException;

    @Override
    public void setNull(String var1, int var2) throws SQLException;

    @Override
    public void setBoolean(String var1, boolean var2) throws SQLException;

    @Override
    public void setByte(String var1, byte var2) throws SQLException;

    @Override
    public void setShort(String var1, short var2) throws SQLException;

    @Override
    public void setInt(String var1, int var2) throws SQLException;

    @Override
    public void setLong(String var1, long var2) throws SQLException;

    @Override
    public void setFloat(String var1, float var2) throws SQLException;

    public void setBinaryFloat(String var1, float var2) throws SQLException;

    public void setBinaryDouble(String var1, double var2) throws SQLException;

    @Override
    public void setDouble(String var1, double var2) throws SQLException;

    @Override
    public void setBigDecimal(String var1, BigDecimal var2) throws SQLException;

    @Override
    public void setString(String var1, String var2) throws SQLException;

    public void setFixedCHAR(String var1, String var2) throws SQLException;

    public void setCursor(String var1, ResultSet var2) throws SQLException;

    public void setROWID(String var1, ROWID var2) throws SQLException;

    public void setRAW(String var1, RAW var2) throws SQLException;

    public void setCHAR(String var1, CHAR var2) throws SQLException;

    public void setDATE(String var1, DATE var2) throws SQLException;

    public void setNUMBER(String var1, NUMBER var2) throws SQLException;

    public void setBLOB(String var1, BLOB var2) throws SQLException;

    @Override
    public void setBlob(String var1, Blob var2) throws SQLException;

    public void setCLOB(String var1, CLOB var2) throws SQLException;

    @Override
    public void setClob(String var1, Clob var2) throws SQLException;

    public void setBFILE(String var1, BFILE var2) throws SQLException;

    public void setBfile(String var1, BFILE var2) throws SQLException;

    @Override
    public void setBytes(String var1, byte[] var2) throws SQLException;

    @Override
    public void setDate(String var1, Date var2) throws SQLException;

    @Override
    public void setTime(String var1, Time var2) throws SQLException;

    @Override
    public void setTimestamp(String var1, Timestamp var2) throws SQLException;

    public void setINTERVALYM(String var1, INTERVALYM var2) throws SQLException;

    public void setINTERVALDS(String var1, INTERVALDS var2) throws SQLException;

    public void setTIMESTAMP(String var1, TIMESTAMP var2) throws SQLException;

    public void setTIMESTAMPTZ(String var1, TIMESTAMPTZ var2) throws SQLException;

    public void setTIMESTAMPLTZ(String var1, TIMESTAMPLTZ var2) throws SQLException;

    @Override
    public void setAsciiStream(String var1, InputStream var2, int var3) throws SQLException;

    @Override
    public void setBinaryStream(String var1, InputStream var2, int var3) throws SQLException;

    public void setUnicodeStream(String var1, InputStream var2, int var3) throws SQLException;

    @Override
    public void setCharacterStream(String var1, Reader var2, int var3) throws SQLException;

    @Override
    public void setDate(String var1, Date var2, Calendar var3) throws SQLException;

    @Override
    public void setTime(String var1, Time var2, Calendar var3) throws SQLException;

    @Override
    public void setTimestamp(String var1, Timestamp var2, Calendar var3) throws SQLException;

    @Override
    public void setURL(String var1, URL var2) throws SQLException;

    public void setArray(String var1, Array var2) throws SQLException;

    public void setARRAY(String var1, ARRAY var2) throws SQLException;

    public void setOPAQUE(String var1, OPAQUE var2) throws SQLException;

    public void setStructDescriptor(String var1, StructDescriptor var2) throws SQLException;

    public void setSTRUCT(String var1, STRUCT var2) throws SQLException;

    public void setCustomDatum(String var1, CustomDatum var2) throws SQLException;

    public void setORAData(String var1, ORAData var2) throws SQLException;

    @Override
    public void setObject(String var1, Object var2, int var3, int var4) throws SQLException;

    @Override
    public void setObject(String var1, Object var2, int var3) throws SQLException;

    public void setRefType(String var1, REF var2) throws SQLException;

    public void setRef(String var1, Ref var2) throws SQLException;

    public void setREF(String var1, REF var2) throws SQLException;

    @Override
    public void setObject(String var1, Object var2) throws SQLException;

    public void setOracleObject(String var1, Datum var2) throws SQLException;

    public void registerOutParameterAtName(String var1, int var2) throws SQLException;

    public void registerOutParameterAtName(String var1, int var2, int var3) throws SQLException;

    public void registerOutParameterAtName(String var1, int var2, String var3) throws SQLException;
}

