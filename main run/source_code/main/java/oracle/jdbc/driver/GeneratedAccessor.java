/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.json.JsonArray
 *  javax.json.JsonNumber
 *  javax.json.JsonObject
 *  javax.json.JsonString
 *  javax.json.JsonStructure
 *  javax.json.JsonValue
 *  javax.json.stream.JsonParser
 */
package oracle.jdbc.driver;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import oracle.jdbc.OracleArray;
import oracle.jdbc.OracleBfile;
import oracle.jdbc.OracleBlob;
import oracle.jdbc.OracleClob;
import oracle.jdbc.OracleData;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OracleNClob;
import oracle.jdbc.OracleOpaque;
import oracle.jdbc.OracleRef;
import oracle.jdbc.OracleStruct;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.internal.OracleConnection;
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
import oracle.sql.json.OracleJsonArray;
import oracle.sql.json.OracleJsonBinary;
import oracle.sql.json.OracleJsonDate;
import oracle.sql.json.OracleJsonDatum;
import oracle.sql.json.OracleJsonDecimal;
import oracle.sql.json.OracleJsonDouble;
import oracle.sql.json.OracleJsonFloat;
import oracle.sql.json.OracleJsonIntervalDS;
import oracle.sql.json.OracleJsonIntervalYM;
import oracle.sql.json.OracleJsonNumber;
import oracle.sql.json.OracleJsonObject;
import oracle.sql.json.OracleJsonParser;
import oracle.sql.json.OracleJsonString;
import oracle.sql.json.OracleJsonStructure;
import oracle.sql.json.OracleJsonTimestamp;
import oracle.sql.json.OracleJsonValue;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class GeneratedAccessor {
    OracleStatement statement;

    GeneratedAccessor() {
    }

    abstract boolean isNull(int var1) throws SQLException;

    Array getArray(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getArray not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    BigDecimal getBigDecimal(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBigDecimal not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBigDecimal not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Blob getBlob(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBlob not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    boolean getBoolean(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return false;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBoolean not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    byte getByte(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return 0;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getByte not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    byte[] getBytes(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBytes not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Clob getClob(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getClob not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    java.sql.Date getDate(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getDate not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    java.sql.Date getDate(int n2, Calendar calendar) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getDate not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    double getDouble(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return 0.0;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getDouble not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    float getFloat(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return 0.0f;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getFloat not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    int getInt(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return 0;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getInt not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    long getLong(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return 0L;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getLong not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    NClob getNClob(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getNClob not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    String getNString(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getNString not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Object getObject(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getObject not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getObject not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Ref getRef(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getRef not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    RowId getRowId(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getRowId not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    short getShort(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return 0;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getShort not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    SQLXML getSQLXML(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getSQLXML not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    String getString(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getString not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Struct getStruct(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getStruct not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Time getTime(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTime not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Time getTime(int n2, Calendar calendar) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTime not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Timestamp getTimestamp(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTimestamp not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Timestamp getTimestamp(int n2, Calendar calendar) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTimestamp not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    URL getURL(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getURL not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    BigInteger getBigInteger(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBigInteger not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Date getJavaUtilDate(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getJavaUtilDate not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Calendar getCalendar(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCalendar not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Duration getDuration(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getDuration not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    LocalDate getLocalDate(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getLocalDate not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    LocalDateTime getLocalDateTime(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getLocalDateTime not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    LocalTime getLocalTime(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getLocalTime not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OffsetDateTime getOffsetDateTime(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOffsetDateTime not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OffsetTime getOffsetTime(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOffsetTime not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Period getPeriod(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getPeriod not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    ZonedDateTime getZonedDateTime(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getZonedDateTime not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    ARRAY getARRAY(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getARRAY not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    BFILE getBFILE(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBFILE not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    BFILE getBfile(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBfile not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    BINARY_FLOAT getBINARY_FLOAT(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBINARY_FLOAT not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    BINARY_DOUBLE getBINARY_DOUBLE(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBINARY_DOUBLE not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    BLOB getBLOB(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBLOB not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    CHAR getCHAR(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCHAR not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    CLOB getCLOB(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCLOB not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    ResultSet getCursor(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCursor not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    DATE getDATE(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getDATE not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    INTERVALDS getINTERVALDS(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getINTERVALDS not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    INTERVALYM getINTERVALYM(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getINTERVALYM not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    NCLOB getNCLOB(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getNCLOB not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    NUMBER getNUMBER(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getNUMBER not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OPAQUE getOPAQUE(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOPAQUE not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Datum getOracleObject(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleObject not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    ORAData getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getORAData not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    ORAData getORAData(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getORAData not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleData getOracleData(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleData not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Object getObject(int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getObject not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    RAW getRAW(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getRAW not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    REF getREF(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getREF not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    ROWID getROWID(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getROWID not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    STRUCT getSTRUCT(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getSTRUCT not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    TIMESTAMPLTZ getTIMESTAMPLTZ(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTIMESTAMPLTZ not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    TIMESTAMPTZ getTIMESTAMPTZ(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTIMESTAMPTZ not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    TIMESTAMP getTIMESTAMP(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTIMESTAMP not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    CustomDatum getCustomDatum(int n2, CustomDatumFactory customDatumFactory) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCustomDatum not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleArray getOracleArray(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleArray not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleBfile getOracleBfile(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleBfile not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleBlob getOracleBlob(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleBlob not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleClob getOracleClob(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleClob not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleNClob getOracleNClob(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleNClob not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleOpaque getOracleOpaque(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleOpaque not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleRef getOracleRef(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleRef not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleStruct getOracleStruct(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleStruct not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    InputStream getAsciiStream(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getAsciiStream not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    InputStream getBinaryStream(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBinaryStream not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Reader getCharacterStream(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCharacterStream not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Reader getNCharacterStream(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getNCharacterStream not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    InputStream getUnicodeStream(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getUnicodeStream not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    JsonValue getJsonValue(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getJsonValue not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    JsonParser getJsonParser(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getJsonParser not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    JsonStructure getJsonStructure(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getJsonStructure not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    JsonObject getJsonObject(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getJsonObject not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    JsonArray getJsonArray(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getJsonArray not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    JsonString getJsonString(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getJsonString not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    JsonNumber getJsonNumber(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getJsonNumber not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonValue getOracleJsonValue(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonValue not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonStructure getOracleJsonStructure(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonStructure not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonObject getOracleJsonObject(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonObject not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonArray getOracleJsonArray(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonArray not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonString getOracleJsonString(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonString not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonNumber getOracleJsonNumber(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonNumber not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonDecimal getOracleJsonDecimal(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonDecimal not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonFloat getOracleJsonFloat(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonFloat not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonDouble getOracleJsonDouble(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonDouble not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonTimestamp getOracleJsonTimestamp(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonTimestamp not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonDate getOracleJsonDate(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonDate not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonBinary getOracleJsonBinary(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonBinary not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonIntervalDS getOracleJsonIntervalDS(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonIntervalDS not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonIntervalYM getOracleJsonIntervalYM(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonIntervalYM not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonDatum getOracleJsonDatum(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonDatum not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    Datum getDatum(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getDatum not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    OracleJsonParser getOracleJsonParser(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOracleJsonParser not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return this.statement.getConnectionDuringExceptionHandling();
    }
}

