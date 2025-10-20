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
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import oracle.jdbc.OracleParameterMetaData;
import oracle.jdbc.OracleStatement;
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

public interface OraclePreparedStatement
extends PreparedStatement,
OracleStatement {
    public static final short FORM_NCHAR = 2;
    public static final short FORM_CHAR = 1;

    public void defineParameterTypeBytes(int var1, int var2, int var3) throws SQLException;

    public void defineParameterTypeChars(int var1, int var2, int var3) throws SQLException;

    public void defineParameterType(int var1, int var2, int var3) throws SQLException;

    public int getExecuteBatch();

    public int sendBatch() throws SQLException;

    public void setARRAY(int var1, ARRAY var2) throws SQLException;

    public void setBfile(int var1, BFILE var2) throws SQLException;

    public void setBFILE(int var1, BFILE var2) throws SQLException;

    public void setBLOB(int var1, BLOB var2) throws SQLException;

    public void setCHAR(int var1, CHAR var2) throws SQLException;

    public void setCLOB(int var1, CLOB var2) throws SQLException;

    public void setCursor(int var1, ResultSet var2) throws SQLException;

    public void setCustomDatum(int var1, CustomDatum var2) throws SQLException;

    public void setORAData(int var1, ORAData var2) throws SQLException;

    public void setDATE(int var1, DATE var2) throws SQLException;

    public void setExecuteBatch(int var1) throws SQLException;

    public void setFixedCHAR(int var1, String var2) throws SQLException;

    public void setNUMBER(int var1, NUMBER var2) throws SQLException;

    public void setBinaryFloat(int var1, float var2) throws SQLException;

    public void setBinaryFloat(int var1, BINARY_FLOAT var2) throws SQLException;

    public void setBinaryDouble(int var1, double var2) throws SQLException;

    public void setBinaryDouble(int var1, BINARY_DOUBLE var2) throws SQLException;

    public void setOPAQUE(int var1, OPAQUE var2) throws SQLException;

    public void setOracleObject(int var1, Datum var2) throws SQLException;

    public void setStructDescriptor(int var1, StructDescriptor var2) throws SQLException;

    public void setRAW(int var1, RAW var2) throws SQLException;

    public void setREF(int var1, REF var2) throws SQLException;

    public void setRefType(int var1, REF var2) throws SQLException;

    public void setROWID(int var1, ROWID var2) throws SQLException;

    public void setSTRUCT(int var1, STRUCT var2) throws SQLException;

    public void setTIMESTAMP(int var1, TIMESTAMP var2) throws SQLException;

    public void setTIMESTAMPTZ(int var1, TIMESTAMPTZ var2) throws SQLException;

    public void setTIMESTAMPLTZ(int var1, TIMESTAMPLTZ var2) throws SQLException;

    public void setINTERVALYM(int var1, INTERVALYM var2) throws SQLException;

    public void setINTERVALDS(int var1, INTERVALDS var2) throws SQLException;

    public void setNullAtName(String var1, int var2, String var3) throws SQLException;

    public void setNullAtName(String var1, int var2) throws SQLException;

    public void setBooleanAtName(String var1, boolean var2) throws SQLException;

    public void setByteAtName(String var1, byte var2) throws SQLException;

    public void setShortAtName(String var1, short var2) throws SQLException;

    public void setIntAtName(String var1, int var2) throws SQLException;

    public void setLongAtName(String var1, long var2) throws SQLException;

    public void setFloatAtName(String var1, float var2) throws SQLException;

    public void setDoubleAtName(String var1, double var2) throws SQLException;

    public void setBinaryFloatAtName(String var1, float var2) throws SQLException;

    public void setBinaryFloatAtName(String var1, BINARY_FLOAT var2) throws SQLException;

    public void setBinaryDoubleAtName(String var1, double var2) throws SQLException;

    public void setBinaryDoubleAtName(String var1, BINARY_DOUBLE var2) throws SQLException;

    public void setBigDecimalAtName(String var1, BigDecimal var2) throws SQLException;

    public void setStringAtName(String var1, String var2) throws SQLException;

    public void setStringForClob(int var1, String var2) throws SQLException;

    public void setStringForClobAtName(String var1, String var2) throws SQLException;

    public void setFixedCHARAtName(String var1, String var2) throws SQLException;

    public void setCursorAtName(String var1, ResultSet var2) throws SQLException;

    public void setROWIDAtName(String var1, ROWID var2) throws SQLException;

    public void setArrayAtName(String var1, Array var2) throws SQLException;

    public void setARRAYAtName(String var1, ARRAY var2) throws SQLException;

    public void setOPAQUEAtName(String var1, OPAQUE var2) throws SQLException;

    public void setStructDescriptorAtName(String var1, StructDescriptor var2) throws SQLException;

    public void setSTRUCTAtName(String var1, STRUCT var2) throws SQLException;

    public void setRAWAtName(String var1, RAW var2) throws SQLException;

    public void setCHARAtName(String var1, CHAR var2) throws SQLException;

    public void setDATEAtName(String var1, DATE var2) throws SQLException;

    public void setNUMBERAtName(String var1, NUMBER var2) throws SQLException;

    public void setBLOBAtName(String var1, BLOB var2) throws SQLException;

    public void setBlobAtName(String var1, Blob var2) throws SQLException;

    public void setBlobAtName(String var1, InputStream var2, long var3) throws SQLException;

    public void setBlobAtName(String var1, InputStream var2) throws SQLException;

    public void setCLOBAtName(String var1, CLOB var2) throws SQLException;

    public void setClobAtName(String var1, Clob var2) throws SQLException;

    public void setClobAtName(String var1, Reader var2, long var3) throws SQLException;

    public void setClobAtName(String var1, Reader var2) throws SQLException;

    public void setBFILEAtName(String var1, BFILE var2) throws SQLException;

    public void setBfileAtName(String var1, BFILE var2) throws SQLException;

    public void setBytesAtName(String var1, byte[] var2) throws SQLException;

    public void setBytesForBlob(int var1, byte[] var2) throws SQLException;

    public void setBytesForBlobAtName(String var1, byte[] var2) throws SQLException;

    public void setDateAtName(String var1, Date var2) throws SQLException;

    public void setDateAtName(String var1, Date var2, Calendar var3) throws SQLException;

    public void setTimeAtName(String var1, Time var2) throws SQLException;

    public void setTimeAtName(String var1, Time var2, Calendar var3) throws SQLException;

    public void setTimestampAtName(String var1, Timestamp var2) throws SQLException;

    public void setTimestampAtName(String var1, Timestamp var2, Calendar var3) throws SQLException;

    public void setINTERVALYMAtName(String var1, INTERVALYM var2) throws SQLException;

    public void setINTERVALDSAtName(String var1, INTERVALDS var2) throws SQLException;

    public void setTIMESTAMPAtName(String var1, TIMESTAMP var2) throws SQLException;

    public void setTIMESTAMPTZAtName(String var1, TIMESTAMPTZ var2) throws SQLException;

    public void setTIMESTAMPLTZAtName(String var1, TIMESTAMPLTZ var2) throws SQLException;

    public void setAsciiStreamAtName(String var1, InputStream var2, int var3) throws SQLException;

    public void setAsciiStreamAtName(String var1, InputStream var2, long var3) throws SQLException;

    public void setAsciiStreamAtName(String var1, InputStream var2) throws SQLException;

    public void setBinaryStreamAtName(String var1, InputStream var2, int var3) throws SQLException;

    public void setBinaryStreamAtName(String var1, InputStream var2, long var3) throws SQLException;

    public void setBinaryStreamAtName(String var1, InputStream var2) throws SQLException;

    public void setCharacterStreamAtName(String var1, Reader var2, long var3) throws SQLException;

    public void setCharacterStreamAtName(String var1, Reader var2) throws SQLException;

    public void setUnicodeStreamAtName(String var1, InputStream var2, int var3) throws SQLException;

    public void setCustomDatumAtName(String var1, CustomDatum var2) throws SQLException;

    public void setORADataAtName(String var1, ORAData var2) throws SQLException;

    public void setObjectAtName(String var1, Object var2, int var3, int var4) throws SQLException;

    public void setObjectAtName(String var1, Object var2, int var3) throws SQLException;

    public void setRefTypeAtName(String var1, REF var2) throws SQLException;

    public void setRefAtName(String var1, Ref var2) throws SQLException;

    public void setREFAtName(String var1, REF var2) throws SQLException;

    public void setObjectAtName(String var1, Object var2) throws SQLException;

    public void setOracleObjectAtName(String var1, Datum var2) throws SQLException;

    public void setURLAtName(String var1, URL var2) throws SQLException;

    public void setCheckBindTypes(boolean var1);

    @Deprecated
    public void setPlsqlIndexTable(int var1, Object var2, int var3, int var4, int var5, int var6) throws SQLException;

    public void setFormOfUse(int var1, short var2);

    public void setDisableStmtCaching(boolean var1);

    public OracleParameterMetaData OracleGetParameterMetaData() throws SQLException;

    public void registerReturnParameter(int var1, int var2) throws SQLException;

    public void registerReturnParameter(int var1, int var2, int var3) throws SQLException;

    public void registerReturnParameter(int var1, int var2, String var3) throws SQLException;

    public ResultSet getReturnResultSet() throws SQLException;

    public void setNCharacterStreamAtName(String var1, Reader var2, long var3) throws SQLException;

    public void setNCharacterStreamAtName(String var1, Reader var2) throws SQLException;

    public void setNClobAtName(String var1, NClob var2) throws SQLException;

    public void setNClobAtName(String var1, Reader var2, long var3) throws SQLException;

    public void setNClobAtName(String var1, Reader var2) throws SQLException;

    public void setNStringAtName(String var1, String var2) throws SQLException;

    public void setRowIdAtName(String var1, RowId var2) throws SQLException;

    public void setSQLXMLAtName(String var1, SQLXML var2) throws SQLException;
}

