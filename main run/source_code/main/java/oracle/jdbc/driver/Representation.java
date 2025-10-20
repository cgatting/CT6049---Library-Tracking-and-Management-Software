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
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLData;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
import oracle.jdbc.OracleNClob;
import oracle.jdbc.OracleOpaque;
import oracle.jdbc.OracleRef;
import oracle.jdbc.OracleStruct;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.Redirector;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NCLOB;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
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
public class Representation {
    public static final Representation VARCHAR;
    public static final Representation FIXED_CHAR;
    public static final Representation CHAR;
    public static final Representation VCS;
    public static final Representation LONG;
    public static final Representation NUMBER;
    public static final Representation VARNUM;
    public static final Representation BINARY_FLOAT;
    public static final Representation BINARY_DOUBLE;
    public static final Representation RAW;
    public static final Representation VBI;
    public static final Representation LONG_RAW;
    public static final Representation ROWID;
    public static final Representation RESULT_SET;
    public static final Representation RSET;
    public static final Representation DATE;
    public static final Representation BLOB;
    public static Representation JSON;
    public static final Representation CLOB;
    public static final Representation BFILE;
    public static final Representation NAMED_TYPE;
    public static final Representation REF_TYPE;
    public static final Representation TIMESTAMP;
    public static final Representation TIMESTAMPTZ;
    public static final Representation OLD_TIMESTAMPTZ;
    public static final Representation TIMESTAMPLTZ;
    public static final Representation INTERVALYM;
    public static final Representation INTERVALDS;
    public static final Representation UROWID;
    public static final Representation PLSQL_INDEX_TABLE;
    public static final Representation T2S_OVERLONG_RAW;
    public static final Representation SET_CHAR_BYTES;
    public static final Representation NULL_TYPE;
    public static final Representation DML_RETURN_PARAM;
    public static final Representation NVARCHAR;
    public static final Representation FIXED_NCHAR;
    public static final Representation NCHAR;
    public static final Representation NVCS;
    public static final Representation NCLOB;
    protected final List<Class<?>> tableB3Classes;
    protected final Map<Class<?>, Redirector<?>> redirectorCache = new ConcurrentHashMap(1024);
    protected final String identifier;

    protected Representation(String string, Class<?> ... classArray) {
        this.identifier = string;
        assert (classArray.length > 0) : classArray.length;
        this.tableB3Classes = Collections.unmodifiableList(Arrays.asList(classArray));
        assert (this.tableB3Classes.size() > 0) : this.tableB3Classes.size();
        this.redirectorCache.putAll(Redirector.createRedirectorMap(this.tableB3Classes));
    }

    @DisableTrace
    public String toString() {
        return "oracle.jdbc.driver.Representation[" + this.identifier + "]";
    }

    <T> T getObject(Accessor accessor, int n2, Class<T> clazz) throws SQLException {
        if (clazz == null) {
            throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)Representation.getConnectionDuringExceptionHandling(), 282).fillInStackTrace();
        }
        Redirector<T> redirector = this.getRedirector(clazz);
        if (redirector == null) {
            throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)Representation.getConnectionDuringExceptionHandling(), 4, clazz).fillInStackTrace();
        }
        return redirector.redirect(accessor, n2);
    }

    final <T> Redirector<T> getRedirector(Class<T> clazz) {
        Redirector<Object> redirector = this.redirectorCache.get(clazz);
        if (redirector == null) {
            redirector = this.createRedirector(clazz);
            this.redirectorCache.put(clazz, redirector);
        }
        return redirector;
    }

    private final <T> Redirector<T> createRedirector(Class<T> clazz) {
        if (SQLData.class.isAssignableFrom(clazz) || OracleData.class.isAssignableFrom(clazz) || ORAData.class.isAssignableFrom(clazz)) {
            return Redirector.createObjectRedirector(clazz);
        }
        return Redirector.createValueOfRedirector(clazz, this.tableB3Classes);
    }

    static OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    static {
        FIXED_CHAR = VARCHAR = new Representation("VARCHAR", String.class, Reader.class, CHAR.class, InputStream.class, RowId.class, ROWID.class, BigDecimal.class, Double.class, Float.class, BigInteger.class, Long.class, Integer.class, Short.class, Byte.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Timestamp.class, Date.class, Calendar.class, java.sql.Date.class, Time.class, Boolean.class);
        CHAR = VARCHAR;
        VCS = VARCHAR;
        LONG = new Representation("LONG", String.class, Reader.class, InputStream.class);
        VARNUM = NUMBER = new Representation("NUMBER", BigDecimal.class, NUMBER.class, String.class, Double.class, Float.class, BigInteger.class, Long.class, Integer.class, Short.class, Byte.class, Boolean.class);
        BINARY_FLOAT = new Representation("BINARY_FLOAT", Float.class, Double.class, BigDecimal.class, BINARY_FLOAT.class, NUMBER.class, String.class, BigInteger.class, Long.class, Integer.class, Short.class, Byte.class);
        BINARY_DOUBLE = new Representation("BINARY_DOUBLE", Double.class, BigDecimal.class, BINARY_DOUBLE.class, NUMBER.class, String.class, Float.class, BigInteger.class, Long.class, Integer.class, Short.class, Byte.class);
        RAW = new Representation("RAW", byte[].class, RAW.class, String.class, InputStream.class, Reader.class);
        VBI = null;
        LONG_RAW = RAW;
        ROWID = new Representation("ROWID", RowId.class, ROWID.class, String.class);
        RSET = RESULT_SET = new Representation("RESULT_SET", ResultSet.class);
        DATE = new Representation("DATE", Timestamp.class, Date.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Calendar.class, DATE.class, TIMESTAMP.class, String.class, java.sql.Date.class, Time.class);
        BLOB = new Representation("BLOB", Blob.class, BLOB.class, OracleBlob.class, InputStream.class, byte[].class);
        ArrayList<Class> arrayList = new ArrayList<Class>();
        arrayList.add(byte[].class);
        arrayList.add(String.class);
        arrayList.add(Reader.class);
        arrayList.add(InputStream.class);
        arrayList.add(OracleJsonValue.class);
        arrayList.add(OracleJsonStructure.class);
        arrayList.add(OracleJsonObject.class);
        arrayList.add(OracleJsonArray.class);
        arrayList.add(OracleJsonString.class);
        arrayList.add(OracleJsonNumber.class);
        arrayList.add(OracleJsonDecimal.class);
        arrayList.add(OracleJsonFloat.class);
        arrayList.add(OracleJsonDouble.class);
        arrayList.add(OracleJsonBinary.class);
        arrayList.add(OracleJsonTimestamp.class);
        arrayList.add(OracleJsonDate.class);
        arrayList.add(OracleJsonIntervalDS.class);
        arrayList.add(OracleJsonIntervalYM.class);
        arrayList.add(OracleJsonParser.class);
        arrayList.add(OracleJsonDatum.class);
        arrayList.add(Datum.class);
        if (PhysicalConnection.isJsonJarPresent()) {
            arrayList.add(JsonValue.class);
            arrayList.add(JsonParser.class);
            arrayList.add(JsonStructure.class);
            arrayList.add(JsonObject.class);
            arrayList.add(JsonArray.class);
            arrayList.add(JsonString.class);
            arrayList.add(JsonNumber.class);
        }
        JSON = new Representation("JSON", arrayList.toArray(new Class[arrayList.size()]));
        CLOB = new Representation("CLOB", Clob.class, CLOB.class, OracleClob.class, Reader.class, String.class, InputStream.class);
        BFILE = new Representation("BFILE", BFILE.class, OracleBfile.class, InputStream.class, byte[].class);
        NAMED_TYPE = new Representation("NAMED_TYPE", SQLXML.class, OracleData.class, ORAData.class, OPAQUE.class, OracleOpaque.class, Struct.class, STRUCT.class, OracleStruct.class, Array.class, ARRAY.class, OracleArray.class);
        REF_TYPE = new Representation("REF_TYPE", Ref.class, REF.class, OracleRef.class);
        TIMESTAMP = new Representation("TIMESTAMP", Timestamp.class, TIMESTAMP.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Calendar.class, Date.class, DATE.class, String.class, java.sql.Date.class, Time.class, byte[].class);
        TIMESTAMPTZ = new Representation("TIMESTAMPTZ", TIMESTAMPTZ.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Timestamp.class, TIMESTAMP.class, Calendar.class, Date.class, DATE.class, String.class, java.sql.Date.class, Time.class, byte[].class);
        OLD_TIMESTAMPTZ = new Representation("OLD_TIMESTAMPTZ", TIMESTAMPTZ.class, Timestamp.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, TIMESTAMP.class, Calendar.class, Date.class, String.class, java.sql.Date.class, Time.class);
        TIMESTAMPLTZ = new Representation("TIMESTAMPLTZ", TIMESTAMPLTZ.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Timestamp.class, TIMESTAMP.class, Calendar.class, Date.class, DATE.class, String.class, java.sql.Date.class, Time.class, byte[].class);
        INTERVALYM = new Representation("INTERVALYM", INTERVALYM.class, Period.class, String.class);
        INTERVALDS = new Representation("INTERVALDS", INTERVALDS.class, Duration.class, String.class);
        UROWID = null;
        PLSQL_INDEX_TABLE = null;
        T2S_OVERLONG_RAW = null;
        SET_CHAR_BYTES = null;
        NULL_TYPE = null;
        DML_RETURN_PARAM = null;
        FIXED_NCHAR = NVARCHAR = new Representation("NVARCHAR", String.class, Reader.class, CHAR.class, InputStream.class, BigDecimal.class, Double.class, Float.class, BigInteger.class, Long.class, Integer.class, Short.class, Byte.class, Timestamp.class, Date.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Calendar.class, java.sql.Date.class, Time.class, Boolean.class);
        NCHAR = NVARCHAR;
        NVCS = NVARCHAR;
        NCLOB = new Representation("NCLOB", NClob.class, NCLOB.class, OracleNClob.class, Reader.class, String.class, InputStream.class);
    }
}

