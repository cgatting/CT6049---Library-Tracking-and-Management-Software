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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import oracle.jdbc.OracleNClob;
import oracle.jdbc.OracleOpaque;
import oracle.jdbc.OracleRef;
import oracle.jdbc.OracleStruct;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.PhysicalConnection;
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
abstract class Redirector<T> {
    private final Class<T> type;
    private static final HashMap<Class<?>, Redirector<?>> CLASS_TO_REDIRECTOR = new HashMap();
    private static final HashMap<Class<?>, Redirector<?>> CLASS_TO_ERROR;

    private Redirector(Class<T> clazz) {
        this.type = clazz;
    }

    abstract T redirect(Accessor var1, int var2) throws SQLException;

    final Class<T> getTarget() {
        return this.type;
    }

    @DisableTrace
    public String toString() {
        return super.toString() + "[" + this.type.getName() + "]";
    }

    static <V> Redirector<V> createObjectRedirector(Class<V> clazz) {
        return new Redirector<V>((Class)clazz){

            @Override
            final V redirect(Accessor accessor, int n2) throws SQLException {
                try {
                    Object object = accessor.getObject(n2);
                    Class clazz = this.getTarget();
                    if (object != null && !clazz.isInstance(object)) {
                        throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)1.getConnectionDuringExceptionHandling(), 49, clazz.getName()).fillInStackTrace();
                    }
                    return object;
                }
                catch (ClassCastException classCastException) {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)1.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
                }
            }
        };
    }

    static <V> Redirector<V> createValueOfRedirector(Class<V> clazz, List<Class<?>> list) {
        if (Modifier.isPublic(clazz.getModifiers())) {
            Method[] methodArray = clazz.getDeclaredMethods();
            int n2 = Integer.MAX_VALUE;
            Method method = null;
            Class<?> clazz2 = null;
            for (Method method2 : methodArray) {
                if (Modifier.isStatic(method2.getModifiers()) && Modifier.isPublic(method2.getModifiers()) && method2.getName().equals("valueOf") && method2.getParameterTypes().length == 1 && clazz.isAssignableFrom(method2.getReturnType())) {
                    int n3 = 0;
                    for (Class<?> clazz3 : list) {
                        if (method2.getParameterTypes()[0].isAssignableFrom(clazz3)) {
                            if (n3 >= n2) break;
                            n2 = n3;
                            method = method2;
                            clazz2 = clazz3;
                            break;
                        }
                        ++n3;
                    }
                }
                if (n2 == 0) break;
            }
            if (method != null) {
                return Redirector.createValueOfRedirector(clazz, method, clazz2);
            }
        }
        return new Redirector<V>((Class)clazz){

            @Override
            final V redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)2.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        };
    }

    private static <V, W> Redirector<V> createValueOfRedirector(Class<V> clazz, final Method method, Class<W> clazz2) {
        final Redirector<?> redirector = CLASS_TO_REDIRECTOR.get(clazz2);
        return new Redirector<V>(clazz){

            @Override
            final V redirect(Accessor accessor, int n2) throws SQLException {
                try {
                    if (accessor.isNull(n2)) {
                        return null;
                    }
                    return method.invoke(null, redirector.redirect(accessor, n2));
                }
                catch (IllegalAccessException illegalAccessException) {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)3.getConnectionDuringExceptionHandling(), 1, illegalAccessException.getMessage()).fillInStackTrace();
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)3.getConnectionDuringExceptionHandling(), 1, illegalArgumentException.getMessage()).fillInStackTrace();
                }
                catch (InvocationTargetException invocationTargetException) {
                    if (invocationTargetException.getTargetException() instanceof SQLException) {
                        throw (SQLException)invocationTargetException.getTargetException();
                    }
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)3.getConnectionDuringExceptionHandling(), 1, "Got something other than a SQLException: " + invocationTargetException.getTargetException()).fillInStackTrace();
                }
            }
        };
    }

    static Map<Class<?>, Redirector<?>> createRedirectorMap(Collection<Class<?>> collection) {
        Map map = (Map)CLASS_TO_ERROR.clone();
        for (Class<?> clazz : collection) {
            assert (CLASS_TO_REDIRECTOR.get(clazz) != null) : clazz;
            map.put(clazz, CLASS_TO_REDIRECTOR.get(clazz));
        }
        return map;
    }

    static OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    static {
        CLASS_TO_REDIRECTOR.put(Array.class, new Redirector<Array>(Array.class){

            @Override
            final Array redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getARRAY(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(BigDecimal.class, new Redirector<BigDecimal>(BigDecimal.class){

            @Override
            final BigDecimal redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBigDecimal(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Blob.class, new Redirector<Blob>(Blob.class){

            @Override
            final Blob redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBLOB(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Boolean.class, new Redirector<Boolean>(Boolean.class){

            @Override
            final Boolean redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBoolean(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Byte.class, new Redirector<Byte>(Byte.class){

            @Override
            final Byte redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getByte(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(byte[].class, new Redirector<byte[]>(byte[].class){

            @Override
            final byte[] redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBytes(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Clob.class, new Redirector<Clob>(Clob.class){

            @Override
            final Clob redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getCLOB(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(java.sql.Date.class, new Redirector<java.sql.Date>(java.sql.Date.class){

            @Override
            final java.sql.Date redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getDate(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Double.class, new Redirector<Double>(Double.class){

            @Override
            final Double redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getDouble(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Float.class, new Redirector<Float>(Float.class){

            @Override
            final Float redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return Float.valueOf(accessor.getFloat(n2));
            }
        });
        CLASS_TO_REDIRECTOR.put(Integer.class, new Redirector<Integer>(Integer.class){

            @Override
            final Integer redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getInt(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Long.class, new Redirector<Long>(Long.class){

            @Override
            final Long redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getLong(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(NClob.class, new Redirector<NClob>(NClob.class){

            @Override
            final NClob redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getNClob(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Ref.class, new Redirector<Ref>(Ref.class){

            @Override
            final Ref redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getREF(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(RowId.class, new Redirector<RowId>(RowId.class){

            @Override
            final RowId redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getROWID(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Short.class, new Redirector<Short>(Short.class){

            @Override
            final Short redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getShort(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(SQLXML.class, new Redirector<SQLXML>(SQLXML.class){

            @Override
            final SQLXML redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getSQLXML(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(String.class, new Redirector<String>(String.class){

            @Override
            final String redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getString(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Struct.class, new Redirector<Struct>(Struct.class){

            @Override
            final Struct redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getStruct(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Time.class, new Redirector<Time>(Time.class){

            @Override
            final Time redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getTime(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Timestamp.class, new Redirector<Timestamp>(Timestamp.class){

            @Override
            final Timestamp redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getTimestamp(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(URL.class, new Redirector<URL>(URL.class){

            @Override
            final URL redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getURL(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(BigInteger.class, new Redirector<BigInteger>(BigInteger.class){

            @Override
            final BigInteger redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBigInteger(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Date.class, new Redirector<Date>(Date.class){

            @Override
            final Date redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getJavaUtilDate(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Calendar.class, new Redirector<Calendar>(Calendar.class){

            @Override
            final Calendar redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getCalendar(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Duration.class, new Redirector<Duration>(Duration.class){

            @Override
            final Duration redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getDuration(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(LocalDate.class, new Redirector<LocalDate>(LocalDate.class){

            @Override
            final LocalDate redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getLocalDate(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(LocalDateTime.class, new Redirector<LocalDateTime>(LocalDateTime.class){

            @Override
            final LocalDateTime redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getLocalDateTime(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(LocalTime.class, new Redirector<LocalTime>(LocalTime.class){

            @Override
            final LocalTime redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getLocalTime(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OffsetDateTime.class, new Redirector<OffsetDateTime>(OffsetDateTime.class){

            @Override
            final OffsetDateTime redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOffsetDateTime(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OffsetTime.class, new Redirector<OffsetTime>(OffsetTime.class){

            @Override
            final OffsetTime redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOffsetTime(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Period.class, new Redirector<Period>(Period.class){

            @Override
            final Period redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getPeriod(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(ZonedDateTime.class, new Redirector<ZonedDateTime>(ZonedDateTime.class){

            @Override
            final ZonedDateTime redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getZonedDateTime(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(ARRAY.class, new Redirector<ARRAY>(ARRAY.class){

            @Override
            final ARRAY redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getARRAY(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(BFILE.class, new Redirector<BFILE>(BFILE.class){

            @Override
            final BFILE redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBFILE(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(BINARY_FLOAT.class, new Redirector<BINARY_FLOAT>(BINARY_FLOAT.class){

            @Override
            final BINARY_FLOAT redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBINARY_FLOAT(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(BINARY_DOUBLE.class, new Redirector<BINARY_DOUBLE>(BINARY_DOUBLE.class){

            @Override
            final BINARY_DOUBLE redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBINARY_DOUBLE(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(BLOB.class, new Redirector<BLOB>(BLOB.class){

            @Override
            final BLOB redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBLOB(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(CHAR.class, new Redirector<CHAR>(CHAR.class){

            @Override
            final CHAR redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getCHAR(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(CLOB.class, new Redirector<CLOB>(CLOB.class){

            @Override
            final CLOB redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getCLOB(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(ResultSet.class, new Redirector<ResultSet>(ResultSet.class){

            @Override
            final ResultSet redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getCursor(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(DATE.class, new Redirector<DATE>(DATE.class){

            @Override
            final DATE redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getDATE(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(INTERVALDS.class, new Redirector<INTERVALDS>(INTERVALDS.class){

            @Override
            final INTERVALDS redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getINTERVALDS(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(INTERVALYM.class, new Redirector<INTERVALYM>(INTERVALYM.class){

            @Override
            final INTERVALYM redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getINTERVALYM(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(NCLOB.class, new Redirector<NCLOB>(NCLOB.class){

            @Override
            final NCLOB redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getNCLOB(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(NUMBER.class, new Redirector<NUMBER>(NUMBER.class){

            @Override
            final NUMBER redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getNUMBER(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OPAQUE.class, new Redirector<OPAQUE>(OPAQUE.class){

            @Override
            final OPAQUE redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOPAQUE(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(ORAData.class, new Redirector<ORAData>(ORAData.class){

            @Override
            final ORAData redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getORAData(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleData.class, new Redirector<OracleData>(OracleData.class){

            @Override
            final OracleData redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleData(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(RAW.class, new Redirector<RAW>(RAW.class){

            @Override
            final RAW redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getRAW(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(REF.class, new Redirector<REF>(REF.class){

            @Override
            final REF redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getREF(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(ROWID.class, new Redirector<ROWID>(ROWID.class){

            @Override
            final ROWID redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getROWID(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(STRUCT.class, new Redirector<STRUCT>(STRUCT.class){

            @Override
            final STRUCT redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getSTRUCT(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(TIMESTAMPLTZ.class, new Redirector<TIMESTAMPLTZ>(TIMESTAMPLTZ.class){

            @Override
            final TIMESTAMPLTZ redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getTIMESTAMPLTZ(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(TIMESTAMPTZ.class, new Redirector<TIMESTAMPTZ>(TIMESTAMPTZ.class){

            @Override
            final TIMESTAMPTZ redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getTIMESTAMPTZ(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(TIMESTAMP.class, new Redirector<TIMESTAMP>(TIMESTAMP.class){

            @Override
            final TIMESTAMP redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getTIMESTAMP(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleArray.class, new Redirector<OracleArray>(OracleArray.class){

            @Override
            final OracleArray redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getARRAY(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleBfile.class, new Redirector<OracleBfile>(OracleBfile.class){

            @Override
            final OracleBfile redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBFILE(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleBlob.class, new Redirector<OracleBlob>(OracleBlob.class){

            @Override
            final OracleBlob redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBLOB(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleClob.class, new Redirector<OracleClob>(OracleClob.class){

            @Override
            final OracleClob redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getCLOB(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleNClob.class, new Redirector<OracleNClob>(OracleNClob.class){

            @Override
            final OracleNClob redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getNCLOB(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleOpaque.class, new Redirector<OracleOpaque>(OracleOpaque.class){

            @Override
            final OracleOpaque redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOPAQUE(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleRef.class, new Redirector<OracleRef>(OracleRef.class){

            @Override
            final OracleRef redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getREF(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleStruct.class, new Redirector<OracleStruct>(OracleStruct.class){

            @Override
            final OracleStruct redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getSTRUCT(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(InputStream.class, new Redirector<InputStream>(InputStream.class){

            @Override
            final InputStream redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getBinaryStream(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Reader.class, new Redirector<Reader>(Reader.class){

            @Override
            final Reader redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getCharacterStream(n2);
            }
        });
        if (PhysicalConnection.isJsonJarPresent()) {
            CLASS_TO_REDIRECTOR.put(JsonValue.class, new Redirector<JsonValue>(JsonValue.class){

                @Override
                final JsonValue redirect(Accessor accessor, int n2) throws SQLException {
                    if (accessor.isNull(n2)) {
                        return null;
                    }
                    return accessor.getJsonValue(n2);
                }
            });
            CLASS_TO_REDIRECTOR.put(JsonParser.class, new Redirector<JsonParser>(JsonParser.class){

                @Override
                final JsonParser redirect(Accessor accessor, int n2) throws SQLException {
                    if (accessor.isNull(n2)) {
                        return null;
                    }
                    return accessor.getJsonParser(n2);
                }
            });
            CLASS_TO_REDIRECTOR.put(JsonStructure.class, new Redirector<JsonStructure>(JsonStructure.class){

                @Override
                final JsonStructure redirect(Accessor accessor, int n2) throws SQLException {
                    if (accessor.isNull(n2)) {
                        return null;
                    }
                    return accessor.getJsonStructure(n2);
                }
            });
            CLASS_TO_REDIRECTOR.put(JsonObject.class, new Redirector<JsonObject>(JsonObject.class){

                @Override
                final JsonObject redirect(Accessor accessor, int n2) throws SQLException {
                    if (accessor.isNull(n2)) {
                        return null;
                    }
                    return accessor.getJsonObject(n2);
                }
            });
            CLASS_TO_REDIRECTOR.put(JsonArray.class, new Redirector<JsonArray>(JsonArray.class){

                @Override
                final JsonArray redirect(Accessor accessor, int n2) throws SQLException {
                    if (accessor.isNull(n2)) {
                        return null;
                    }
                    return accessor.getJsonArray(n2);
                }
            });
            CLASS_TO_REDIRECTOR.put(JsonString.class, new Redirector<JsonString>(JsonString.class){

                @Override
                final JsonString redirect(Accessor accessor, int n2) throws SQLException {
                    if (accessor.isNull(n2)) {
                        return null;
                    }
                    return accessor.getJsonString(n2);
                }
            });
            CLASS_TO_REDIRECTOR.put(JsonNumber.class, new Redirector<JsonNumber>(JsonNumber.class){

                @Override
                final JsonNumber redirect(Accessor accessor, int n2) throws SQLException {
                    if (accessor.isNull(n2)) {
                        return null;
                    }
                    return accessor.getJsonNumber(n2);
                }
            });
        }
        CLASS_TO_REDIRECTOR.put(OracleJsonValue.class, new Redirector<OracleJsonValue>(OracleJsonValue.class){

            @Override
            final OracleJsonValue redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonValue(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonStructure.class, new Redirector<OracleJsonStructure>(OracleJsonStructure.class){

            @Override
            final OracleJsonStructure redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonStructure(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonObject.class, new Redirector<OracleJsonObject>(OracleJsonObject.class){

            @Override
            final OracleJsonObject redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonObject(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonArray.class, new Redirector<OracleJsonArray>(OracleJsonArray.class){

            @Override
            final OracleJsonArray redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonArray(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonString.class, new Redirector<OracleJsonString>(OracleJsonString.class){

            @Override
            final OracleJsonString redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonString(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonNumber.class, new Redirector<OracleJsonNumber>(OracleJsonNumber.class){

            @Override
            final OracleJsonNumber redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonNumber(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonDecimal.class, new Redirector<OracleJsonDecimal>(OracleJsonDecimal.class){

            @Override
            final OracleJsonDecimal redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonDecimal(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonFloat.class, new Redirector<OracleJsonFloat>(OracleJsonFloat.class){

            @Override
            final OracleJsonFloat redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonFloat(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonDouble.class, new Redirector<OracleJsonDouble>(OracleJsonDouble.class){

            @Override
            final OracleJsonDouble redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonDouble(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonTimestamp.class, new Redirector<OracleJsonTimestamp>(OracleJsonTimestamp.class){

            @Override
            final OracleJsonTimestamp redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonTimestamp(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonDate.class, new Redirector<OracleJsonDate>(OracleJsonDate.class){

            @Override
            final OracleJsonDate redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonDate(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonBinary.class, new Redirector<OracleJsonBinary>(OracleJsonBinary.class){

            @Override
            final OracleJsonBinary redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonBinary(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonIntervalDS.class, new Redirector<OracleJsonIntervalDS>(OracleJsonIntervalDS.class){

            @Override
            final OracleJsonIntervalDS redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonIntervalDS(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonIntervalYM.class, new Redirector<OracleJsonIntervalYM>(OracleJsonIntervalYM.class){

            @Override
            final OracleJsonIntervalYM redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonIntervalYM(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonDatum.class, new Redirector<OracleJsonDatum>(OracleJsonDatum.class){

            @Override
            final OracleJsonDatum redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonDatum(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(Datum.class, new Redirector<Datum>(Datum.class){

            @Override
            final Datum redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getDatum(n2);
            }
        });
        CLASS_TO_REDIRECTOR.put(OracleJsonParser.class, new Redirector<OracleJsonParser>(OracleJsonParser.class){

            @Override
            final OracleJsonParser redirect(Accessor accessor, int n2) throws SQLException {
                if (accessor.isNull(n2)) {
                    return null;
                }
                return accessor.getOracleJsonParser(n2);
            }
        });
        CLASS_TO_ERROR = new HashMap();
        CLASS_TO_ERROR.put(Array.class, new Redirector<Array>(Array.class){

            @Override
            final Array redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)94.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(BigDecimal.class, new Redirector<BigDecimal>(BigDecimal.class){

            @Override
            final BigDecimal redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)95.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Blob.class, new Redirector<Blob>(Blob.class){

            @Override
            final Blob redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)96.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Boolean.class, new Redirector<Boolean>(Boolean.class){

            @Override
            final Boolean redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)97.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Byte.class, new Redirector<Byte>(Byte.class){

            @Override
            final Byte redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)98.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(byte[].class, new Redirector<byte[]>(byte[].class){

            @Override
            final byte[] redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)99.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Clob.class, new Redirector<Clob>(Clob.class){

            @Override
            final Clob redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)100.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(java.sql.Date.class, new Redirector<java.sql.Date>(java.sql.Date.class){

            @Override
            final java.sql.Date redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)101.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Double.class, new Redirector<Double>(Double.class){

            @Override
            final Double redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)102.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Float.class, new Redirector<Float>(Float.class){

            @Override
            final Float redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)103.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Integer.class, new Redirector<Integer>(Integer.class){

            @Override
            final Integer redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)104.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Long.class, new Redirector<Long>(Long.class){

            @Override
            final Long redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)105.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(NClob.class, new Redirector<NClob>(NClob.class){

            @Override
            final NClob redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)106.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Ref.class, new Redirector<Ref>(Ref.class){

            @Override
            final Ref redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)107.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(RowId.class, new Redirector<RowId>(RowId.class){

            @Override
            final RowId redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)108.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Short.class, new Redirector<Short>(Short.class){

            @Override
            final Short redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)109.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(SQLXML.class, new Redirector<SQLXML>(SQLXML.class){

            @Override
            final SQLXML redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)110.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(String.class, new Redirector<String>(String.class){

            @Override
            final String redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)111.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Struct.class, new Redirector<Struct>(Struct.class){

            @Override
            final Struct redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)112.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Time.class, new Redirector<Time>(Time.class){

            @Override
            final Time redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)113.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Timestamp.class, new Redirector<Timestamp>(Timestamp.class){

            @Override
            final Timestamp redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)114.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(URL.class, new Redirector<URL>(URL.class){

            @Override
            final URL redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)115.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(BigInteger.class, new Redirector<BigInteger>(BigInteger.class){

            @Override
            final BigInteger redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)116.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Date.class, new Redirector<Date>(Date.class){

            @Override
            final Date redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)117.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Calendar.class, new Redirector<Calendar>(Calendar.class){

            @Override
            final Calendar redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)118.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Duration.class, new Redirector<Duration>(Duration.class){

            @Override
            final Duration redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)119.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(LocalDate.class, new Redirector<LocalDate>(LocalDate.class){

            @Override
            final LocalDate redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)120.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(LocalDateTime.class, new Redirector<LocalDateTime>(LocalDateTime.class){

            @Override
            final LocalDateTime redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)121.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(LocalTime.class, new Redirector<LocalTime>(LocalTime.class){

            @Override
            final LocalTime redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)122.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OffsetDateTime.class, new Redirector<OffsetDateTime>(OffsetDateTime.class){

            @Override
            final OffsetDateTime redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)123.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OffsetTime.class, new Redirector<OffsetTime>(OffsetTime.class){

            @Override
            final OffsetTime redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)124.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Period.class, new Redirector<Period>(Period.class){

            @Override
            final Period redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)125.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(ZonedDateTime.class, new Redirector<ZonedDateTime>(ZonedDateTime.class){

            @Override
            final ZonedDateTime redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)126.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(ARRAY.class, new Redirector<ARRAY>(ARRAY.class){

            @Override
            final ARRAY redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)127.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(BFILE.class, new Redirector<BFILE>(BFILE.class){

            @Override
            final BFILE redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)128.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(BINARY_FLOAT.class, new Redirector<BINARY_FLOAT>(BINARY_FLOAT.class){

            @Override
            final BINARY_FLOAT redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)129.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(BINARY_DOUBLE.class, new Redirector<BINARY_DOUBLE>(BINARY_DOUBLE.class){

            @Override
            final BINARY_DOUBLE redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)130.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(BLOB.class, new Redirector<BLOB>(BLOB.class){

            @Override
            final BLOB redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)131.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(CHAR.class, new Redirector<CHAR>(CHAR.class){

            @Override
            final CHAR redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)132.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(CLOB.class, new Redirector<CLOB>(CLOB.class){

            @Override
            final CLOB redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)133.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(ResultSet.class, new Redirector<ResultSet>(ResultSet.class){

            @Override
            final ResultSet redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)134.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(DATE.class, new Redirector<DATE>(DATE.class){

            @Override
            final DATE redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)135.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(INTERVALDS.class, new Redirector<INTERVALDS>(INTERVALDS.class){

            @Override
            final INTERVALDS redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)136.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(INTERVALYM.class, new Redirector<INTERVALYM>(INTERVALYM.class){

            @Override
            final INTERVALYM redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)137.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(NCLOB.class, new Redirector<NCLOB>(NCLOB.class){

            @Override
            final NCLOB redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)138.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(NUMBER.class, new Redirector<NUMBER>(NUMBER.class){

            @Override
            final NUMBER redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)139.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OPAQUE.class, new Redirector<OPAQUE>(OPAQUE.class){

            @Override
            final OPAQUE redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)140.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(ORAData.class, new Redirector<ORAData>(ORAData.class){

            @Override
            final ORAData redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)141.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OracleData.class, new Redirector<OracleData>(OracleData.class){

            @Override
            final OracleData redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)142.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(RAW.class, new Redirector<RAW>(RAW.class){

            @Override
            final RAW redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)143.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(REF.class, new Redirector<REF>(REF.class){

            @Override
            final REF redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)144.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(ROWID.class, new Redirector<ROWID>(ROWID.class){

            @Override
            final ROWID redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)145.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(STRUCT.class, new Redirector<STRUCT>(STRUCT.class){

            @Override
            final STRUCT redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)146.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(TIMESTAMPLTZ.class, new Redirector<TIMESTAMPLTZ>(TIMESTAMPLTZ.class){

            @Override
            final TIMESTAMPLTZ redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)147.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(TIMESTAMPTZ.class, new Redirector<TIMESTAMPTZ>(TIMESTAMPTZ.class){

            @Override
            final TIMESTAMPTZ redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)148.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(TIMESTAMP.class, new Redirector<TIMESTAMP>(TIMESTAMP.class){

            @Override
            final TIMESTAMP redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)149.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OracleArray.class, new Redirector<OracleArray>(OracleArray.class){

            @Override
            final OracleArray redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)150.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OracleBfile.class, new Redirector<OracleBfile>(OracleBfile.class){

            @Override
            final OracleBfile redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)151.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OracleBlob.class, new Redirector<OracleBlob>(OracleBlob.class){

            @Override
            final OracleBlob redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)152.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OracleClob.class, new Redirector<OracleClob>(OracleClob.class){

            @Override
            final OracleClob redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)153.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OracleNClob.class, new Redirector<OracleNClob>(OracleNClob.class){

            @Override
            final OracleNClob redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)154.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OracleOpaque.class, new Redirector<OracleOpaque>(OracleOpaque.class){

            @Override
            final OracleOpaque redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)155.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OracleRef.class, new Redirector<OracleRef>(OracleRef.class){

            @Override
            final OracleRef redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)156.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(OracleStruct.class, new Redirector<OracleStruct>(OracleStruct.class){

            @Override
            final OracleStruct redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)157.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(InputStream.class, new Redirector<InputStream>(InputStream.class){

            @Override
            final InputStream redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)158.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        CLASS_TO_ERROR.put(Reader.class, new Redirector<Reader>(Reader.class){

            @Override
            final Reader redirect(Accessor accessor, int n2) throws SQLException {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)159.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        });
        if (PhysicalConnection.isJsonJarPresent()) {
            CLASS_TO_ERROR.put(JsonValue.class, new Redirector<JsonValue>(JsonValue.class){

                @Override
                final JsonValue redirect(Accessor accessor, int n2) throws SQLException {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)160.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
                }
            });
            CLASS_TO_ERROR.put(JsonParser.class, new Redirector<JsonParser>(JsonParser.class){

                @Override
                final JsonParser redirect(Accessor accessor, int n2) throws SQLException {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)161.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
                }
            });
            CLASS_TO_ERROR.put(JsonStructure.class, new Redirector<JsonStructure>(JsonStructure.class){

                @Override
                final JsonStructure redirect(Accessor accessor, int n2) throws SQLException {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)162.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
                }
            });
            CLASS_TO_ERROR.put(JsonObject.class, new Redirector<JsonObject>(JsonObject.class){

                @Override
                final JsonObject redirect(Accessor accessor, int n2) throws SQLException {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)163.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
                }
            });
            CLASS_TO_ERROR.put(JsonArray.class, new Redirector<JsonArray>(JsonArray.class){

                @Override
                final JsonArray redirect(Accessor accessor, int n2) throws SQLException {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)164.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
                }
            });
            CLASS_TO_ERROR.put(JsonString.class, new Redirector<JsonString>(JsonString.class){

                @Override
                final JsonString redirect(Accessor accessor, int n2) throws SQLException {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)165.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
                }
            });
            CLASS_TO_ERROR.put(JsonNumber.class, new Redirector<JsonNumber>(JsonNumber.class){

                @Override
                final JsonNumber redirect(Accessor accessor, int n2) throws SQLException {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)166.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
                }
            });
        }
    }
}

