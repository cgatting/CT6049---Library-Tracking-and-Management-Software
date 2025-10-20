/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.internal.OracleBfile;
import oracle.jdbc.internal.OracleBlob;
import oracle.jdbc.internal.OracleClob;
import oracle.jdbc.internal.OracleNClob;
import oracle.jdbc.internal.OracleOpaque;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.CharacterSet;
import oracle.sql.DATE;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NCLOB;
import oracle.sql.NUMBER;
import oracle.sql.RAW;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.UPDATEABLE_RESULT_SET})
public abstract class JavaToJavaConverter<S, T> {
    protected static final long MILLIS_IN_SECOND = 1000L;
    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    private static final Calendar UTC_US_CALENDAR = Calendar.getInstance(UTC_TIME_ZONE, Locale.US);
    protected Calendar cachedUTCUSCalendar = (Calendar)UTC_US_CALENDAR.clone();
    protected static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    protected static final DateTimeFormatter OFFSET_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ");
    private static int INTYMYEAROFFSET = Integer.MIN_VALUE;
    private static int INTYMMONTHOFFSET = 60;
    private static int INTERVALDSOFFSET = 60;
    private static int INTERVALDAYOFFSET = Integer.MIN_VALUE;
    private static final Map<Key, JavaToJavaConverter<?, ?>> CONVERTERS = new HashMap();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static <S, T> T convert(S s2, Class<T> clazz, OracleConnection oracleConnection, Object object, Object object2) throws SQLException {
        if (s2 == null) {
            return clazz.cast(null);
        }
        if (clazz.isInstance(s2)) {
            return clazz.cast(s2);
        }
        Key key = new Key(s2.getClass(), clazz);
        JavaToJavaConverter<?, ?> javaToJavaConverter = CONVERTERS.get(key);
        if (javaToJavaConverter != null) {
            try {
                return (T)javaToJavaConverter.convert(s2, oracleConnection, object, object2);
            }
            catch (SQLException sQLException) {
                throw sQLException;
            }
            catch (Throwable throwable) {
                throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)oracleConnection, 132, s2.getClass().getName() + " to " + clazz.getName(), throwable).fillInStackTrace();
            }
        }
        Throwable throwable = null;
        for (Map.Entry<Key, JavaToJavaConverter<?, ?>> entry : CONVERTERS.entrySet()) {
            if (!entry.getKey().satisfies(key)) continue;
            try {
                javaToJavaConverter = entry.getValue();
                return (T)javaToJavaConverter.convert(s2, oracleConnection, object, object2);
            }
            catch (Throwable throwable2) {
                try {
                    if (throwable == null) continue;
                    throwable2.initCause(throwable);
                }
                catch (IllegalStateException illegalStateException) {}
                continue;
                finally {
                    throwable = throwable2;
                }
            }
        }
        throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)oracleConnection, 132, s2.getClass().getName() + " to " + clazz.getName(), throwable).fillInStackTrace();
    }

    private JavaToJavaConverter() {
    }

    protected abstract T convert(S var1, OracleConnection var2, Object var3, Object var4) throws Exception;

    Calendar getSessionCalendar(OracleConnection oracleConnection) {
        Calendar calendar;
        String string = oracleConnection.getSessionTimeZone();
        if (string == null) {
            calendar = Calendar.getInstance();
        } else {
            TimeZone timeZone = TimeZone.getTimeZone(string);
            calendar = Calendar.getInstance(timeZone);
        }
        return calendar;
    }

    public static void main(String[] stringArray) throws Exception {
        int n2 = 0;
        String string = stringArray[n2++];
        System.out.println("\toriginal:\t" + string);
        while (n2 < stringArray.length) {
            Class<?> clazz = Class.forName(stringArray[n2++]);
            System.out.println("\t" + string.getClass().getName() + "\tto:\t" + clazz.getName());
            string = JavaToJavaConverter.convert(string, clazz, null, null, null);
            System.out.println("\tresult:\t" + string);
        }
    }

    static {
        CONVERTERS.put(new Key(BigDecimal.class, NUMBER.class), new JavaToJavaConverter<BigDecimal, NUMBER>(){

            @Override
            protected NUMBER convert(BigDecimal bigDecimal, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(bigDecimal);
            }
        });
        CONVERTERS.put(new Key(BigInteger.class, NUMBER.class), new JavaToJavaConverter<BigInteger, NUMBER>(){

            @Override
            protected NUMBER convert(BigInteger bigInteger, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(bigInteger);
            }
        });
        CONVERTERS.put(new Key(BINARY_DOUBLE.class, Double.class), new JavaToJavaConverter<BINARY_DOUBLE, Double>(){

            @Override
            protected Double convert(BINARY_DOUBLE bINARY_DOUBLE, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bINARY_DOUBLE.doubleValue();
            }
        });
        CONVERTERS.put(new Key(BINARY_DOUBLE.class, String.class), new JavaToJavaConverter<BINARY_DOUBLE, String>(){

            @Override
            protected String convert(BINARY_DOUBLE bINARY_DOUBLE, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bINARY_DOUBLE.stringValue();
            }
        });
        CONVERTERS.put(new Key(BINARY_FLOAT.class, Float.class), new JavaToJavaConverter<BINARY_FLOAT, Float>(){

            @Override
            protected Float convert(BINARY_FLOAT bINARY_FLOAT, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Float.valueOf(bINARY_FLOAT.floatValue());
            }
        });
        CONVERTERS.put(new Key(BINARY_FLOAT.class, String.class), new JavaToJavaConverter<BINARY_FLOAT, String>(){

            @Override
            protected String convert(BINARY_FLOAT bINARY_FLOAT, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bINARY_FLOAT.stringValue();
            }
        });
        CONVERTERS.put(new Key(Boolean.class, BigDecimal.class), new JavaToJavaConverter<Boolean, BigDecimal>(){

            @Override
            protected BigDecimal convert(Boolean bl, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bl != false ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        CONVERTERS.put(new Key(Boolean.class, Byte.class), new JavaToJavaConverter<Boolean, Byte>(){

            @Override
            protected Byte convert(Boolean bl, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bl != false ? (byte)1 : 0;
            }
        });
        CONVERTERS.put(new Key(Boolean.class, Double.class), new JavaToJavaConverter<Boolean, Double>(){

            @Override
            protected Double convert(Boolean bl, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bl != false ? 1.0 : 0.0;
            }
        });
        CONVERTERS.put(new Key(Boolean.class, Float.class), new JavaToJavaConverter<Boolean, Float>(){

            @Override
            protected Float convert(Boolean bl, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Float.valueOf(bl != false ? 1.0f : 0.0f);
            }
        });
        CONVERTERS.put(new Key(Boolean.class, Integer.class), new JavaToJavaConverter<Boolean, Integer>(){

            @Override
            protected Integer convert(Boolean bl, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bl != false ? 1 : 0;
            }
        });
        CONVERTERS.put(new Key(Boolean.class, Long.class), new JavaToJavaConverter<Boolean, Long>(){

            @Override
            protected Long convert(Boolean bl, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bl != false ? 1L : 0L;
            }
        });
        CONVERTERS.put(new Key(Boolean.class, NUMBER.class), new JavaToJavaConverter<Boolean, NUMBER>(){

            @Override
            protected NUMBER convert(Boolean bl, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bl != false ? new NUMBER(1) : new NUMBER(0);
            }
        });
        CONVERTERS.put(new Key(Boolean.class, Short.class), new JavaToJavaConverter<Boolean, Short>(){

            @Override
            protected Short convert(Boolean bl, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bl != false ? (short)1 : 0;
            }
        });
        CONVERTERS.put(new Key(Boolean.class, String.class), new JavaToJavaConverter<Boolean, String>(){

            @Override
            protected String convert(Boolean bl, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return bl != false ? "1" : "0";
            }
        });
        CONVERTERS.put(new Key(Byte.class, NUMBER.class), new JavaToJavaConverter<Byte, NUMBER>(){

            @Override
            protected NUMBER convert(Byte by, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(by);
            }
        });
        CONVERTERS.put(new Key(byte[].class, CHAR.class), new JavaToJavaConverter<byte[], CHAR>(){

            @Override
            protected CHAR convert(byte[] byArray, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                CharacterSet characterSet = null;
                if (object2 != null && object2 instanceof CharacterSet) {
                    characterSet = (CharacterSet)object2;
                } else if (oracleConnection != null) {
                    characterSet = CharacterSet.make(oracleConnection.getJdbcCsId());
                }
                if (characterSet == null) {
                    throw new IllegalArgumentException("Charset needs to be defined while making CHAR type");
                }
                return new CHAR(byArray, characterSet);
            }
        });
        CONVERTERS.put(new Key(byte[].class, DATE.class), new JavaToJavaConverter<byte[], DATE>(){

            @Override
            protected DATE convert(byte[] byArray, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new DATE(byArray);
            }
        });
        CONVERTERS.put(new Key(byte[].class, InputStream.class), new JavaToJavaConverter<byte[], InputStream>(){

            @Override
            protected InputStream convert(byte[] byArray, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new ByteArrayInputStream(byArray);
            }
        });
        CONVERTERS.put(new Key(byte[].class, NUMBER.class), new JavaToJavaConverter<byte[], NUMBER>(){

            @Override
            protected NUMBER convert(byte[] byArray, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(byArray);
            }
        });
        CONVERTERS.put(new Key(byte[].class, RAW.class), new JavaToJavaConverter<byte[], RAW>(){

            @Override
            protected RAW convert(byte[] byArray, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return RAW.newRAW(byArray);
            }
        });
        CONVERTERS.put(new Key(byte[].class, String.class), new JavaToJavaConverter<byte[], String>(){

            @Override
            protected String convert(byte[] byArray, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new String(byArray);
            }
        });
        CONVERTERS.put(new Key(byte[].class, TIMESTAMP.class), new JavaToJavaConverter<byte[], TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(byte[] byArray, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMP(byArray);
            }
        });
        CONVERTERS.put(new Key(byte[].class, TIMESTAMPLTZ.class), new JavaToJavaConverter<byte[], TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(byte[] byArray, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPLTZ(byArray);
            }
        });
        CONVERTERS.put(new Key(Calendar.class, Date.class), new JavaToJavaConverter<Calendar, Date>(){

            @Override
            protected Date convert(Calendar calendar, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Date(calendar.getTimeInMillis());
            }
        });
        CONVERTERS.put(new Key(Calendar.class, java.util.Date.class), new JavaToJavaConverter<Calendar, java.util.Date>(){

            @Override
            protected java.util.Date convert(Calendar calendar, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return calendar.getTime();
            }
        });
        CONVERTERS.put(new Key(Calendar.class, DATE.class), new JavaToJavaConverter<Calendar, DATE>(){

            @Override
            protected DATE convert(Calendar calendar, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new DATE(calendar);
            }
        });
        CONVERTERS.put(new Key(Calendar.class, String.class), new JavaToJavaConverter<Calendar, String>(){

            @Override
            protected String convert(Calendar calendar, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return calendar.toString();
            }
        });
        CONVERTERS.put(new Key(Calendar.class, Time.class), new JavaToJavaConverter<Calendar, Time>(){

            @Override
            protected Time convert(Calendar calendar, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Time(calendar.getTimeInMillis());
            }
        });
        CONVERTERS.put(new Key(Calendar.class, Timestamp.class), new JavaToJavaConverter<Calendar, Timestamp>(){

            @Override
            protected Timestamp convert(Calendar calendar, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Timestamp(calendar.getTimeInMillis());
            }
        });
        CONVERTERS.put(new Key(Calendar.class, TIMESTAMP.class), new JavaToJavaConverter<Calendar, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(Calendar calendar, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMP(new Timestamp(calendar.getTimeInMillis()));
            }
        });
        CONVERTERS.put(new Key(Calendar.class, TIMESTAMPLTZ.class), new JavaToJavaConverter<Calendar, TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(Calendar calendar, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPLTZ((Connection)oracleConnection, this.getSessionCalendar(oracleConnection), new Date(calendar.getTimeInMillis()));
            }
        });
        CONVERTERS.put(new Key(Calendar.class, TIMESTAMPTZ.class), new JavaToJavaConverter<Calendar, TIMESTAMPTZ>(){

            @Override
            protected TIMESTAMPTZ convert(Calendar calendar, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPTZ((Connection)oracleConnection, new Date(calendar.getTimeInMillis()), calendar);
            }
        });
        CONVERTERS.put(new Key(CHAR.class, BigDecimal.class), new JavaToJavaConverter<CHAR, BigDecimal>(){

            @Override
            protected BigDecimal convert(CHAR cHAR, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new BigDecimal(cHAR.getString());
            }
        });
        CONVERTERS.put(new Key(CHAR.class, Boolean.class), new JavaToJavaConverter<CHAR, Boolean>(){

            @Override
            protected Boolean convert(CHAR cHAR, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return cHAR.booleanValue();
            }
        });
        CONVERTERS.put(new Key(CHAR.class, Byte.class), new JavaToJavaConverter<CHAR, Byte>(){

            @Override
            protected Byte convert(CHAR cHAR, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return cHAR.byteValue();
            }
        });
        CONVERTERS.put(new Key(CHAR.class, Date.class), new JavaToJavaConverter<CHAR, Date>(){

            @Override
            protected Date convert(CHAR cHAR, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Date(java.util.Date.parse(cHAR.getString()));
            }
        });
        CONVERTERS.put(new Key(CHAR.class, InputStream.class), new JavaToJavaConverter<CHAR, InputStream>(){

            @Override
            protected InputStream convert(CHAR cHAR, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new ByteArrayInputStream(cHAR.shareBytes());
            }
        });
        CONVERTERS.put(new Key(CHAR.class, Long.class), new JavaToJavaConverter<CHAR, Long>(){

            @Override
            protected Long convert(CHAR cHAR, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Long(cHAR.getString());
            }
        });
        CONVERTERS.put(new Key(CHAR.class, Reader.class), new JavaToJavaConverter<CHAR, Reader>(){

            @Override
            protected Reader convert(CHAR cHAR, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new StringReader(cHAR.getString());
            }
        });
        CONVERTERS.put(new Key(CHAR.class, String.class), new JavaToJavaConverter<CHAR, String>(){

            @Override
            protected String convert(CHAR cHAR, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return cHAR.getString();
            }
        });
        CONVERTERS.put(new Key(CHAR.class, Time.class), new JavaToJavaConverter<CHAR, Time>(){

            @Override
            protected Time convert(CHAR cHAR, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Time(java.util.Date.parse(cHAR.getString()));
            }
        });
        CONVERTERS.put(new Key(CHAR.class, Timestamp.class), new JavaToJavaConverter<CHAR, Timestamp>(){

            @Override
            protected Timestamp convert(CHAR cHAR, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Timestamp(java.util.Date.parse(cHAR.getString()));
            }
        });
        CONVERTERS.put(new Key(Character.class, String.class), new JavaToJavaConverter<Character, String>(){

            @Override
            protected String convert(Character c2, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return c2.toString();
            }
        });
        CONVERTERS.put(new Key(Date.class, Calendar.class), new JavaToJavaConverter<Date, Calendar>(){

            @Override
            protected Calendar convert(Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            }
        });
        CONVERTERS.put(new Key(Date.class, java.util.Date.class), new JavaToJavaConverter<Date, java.util.Date>(){

            @Override
            protected java.util.Date convert(Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new java.util.Date(date.getTime());
            }
        });
        CONVERTERS.put(new Key(Date.class, DATE.class), new JavaToJavaConverter<Date, DATE>(){

            @Override
            protected DATE convert(Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new DATE(date);
            }
        });
        CONVERTERS.put(new Key(Date.class, String.class), new JavaToJavaConverter<Date, String>(){

            @Override
            protected String convert(Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return date.toString();
            }
        });
        CONVERTERS.put(new Key(Date.class, Timestamp.class), new JavaToJavaConverter<Date, Timestamp>(){

            @Override
            protected Timestamp convert(Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Timestamp(date.getTime());
            }
        });
        CONVERTERS.put(new Key(Date.class, TIMESTAMP.class), new JavaToJavaConverter<Date, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMP(date);
            }
        });
        CONVERTERS.put(new Key(java.util.Date.class, Calendar.class), new JavaToJavaConverter<java.util.Date, Calendar>(){

            @Override
            protected Calendar convert(java.util.Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            }
        });
        CONVERTERS.put(new Key(java.util.Date.class, java.util.Date.class), new JavaToJavaConverter<java.util.Date, java.util.Date>(){

            @Override
            protected java.util.Date convert(java.util.Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Date(date.getTime());
            }
        });
        CONVERTERS.put(new Key(java.util.Date.class, DATE.class), new JavaToJavaConverter<java.util.Date, DATE>(){

            @Override
            protected DATE convert(java.util.Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new DATE(new Date(date.getTime()));
            }
        });
        CONVERTERS.put(new Key(java.util.Date.class, String.class), new JavaToJavaConverter<java.util.Date, String>(){

            @Override
            protected String convert(java.util.Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return date.toString();
            }
        });
        CONVERTERS.put(new Key(java.util.Date.class, Timestamp.class), new JavaToJavaConverter<java.util.Date, Timestamp>(){

            @Override
            protected Timestamp convert(java.util.Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Timestamp(date.getTime());
            }
        });
        CONVERTERS.put(new Key(java.util.Date.class, TIMESTAMP.class), new JavaToJavaConverter<java.util.Date, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(java.util.Date date, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMP(new Timestamp(date.getTime()));
            }
        });
        CONVERTERS.put(new Key(DATE.class, Calendar.class), new JavaToJavaConverter<DATE, Calendar>(){

            @Override
            protected Calendar convert(DATE dATE, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dATE.dateValue());
                return calendar;
            }
        });
        CONVERTERS.put(new Key(DATE.class, java.util.Date.class), new JavaToJavaConverter<DATE, java.util.Date>(){

            @Override
            protected java.util.Date convert(DATE dATE, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new java.util.Date(dATE.dateValue().getTime());
            }
        });
        CONVERTERS.put(new Key(DATE.class, Date.class), new JavaToJavaConverter<DATE, Date>(){

            @Override
            protected Date convert(DATE dATE, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Date(dATE.dateValue().getTime());
            }
        });
        CONVERTERS.put(new Key(DATE.class, String.class), new JavaToJavaConverter<DATE, String>(){

            @Override
            protected String convert(DATE dATE, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return dATE.stringValue();
            }
        });
        CONVERTERS.put(new Key(DATE.class, Time.class), new JavaToJavaConverter<DATE, Time>(){

            @Override
            protected Time convert(DATE dATE, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return dATE.timeValue();
            }
        });
        CONVERTERS.put(new Key(DATE.class, Timestamp.class), new JavaToJavaConverter<DATE, Timestamp>(){

            @Override
            protected Timestamp convert(DATE dATE, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return dATE.timestampValue();
            }
        });
        CONVERTERS.put(new Key(DATE.class, TIMESTAMP.class), new JavaToJavaConverter<DATE, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(DATE dATE, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMP(dATE);
            }
        });
        CONVERTERS.put(new Key(Double.class, NUMBER.class), new JavaToJavaConverter<Double, NUMBER>(){

            @Override
            protected NUMBER convert(Double d2, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(BigDecimal.valueOf(d2));
            }
        });
        CONVERTERS.put(new Key(Duration.class, INTERVALDS.class), new JavaToJavaConverter<Duration, INTERVALDS>(){

            @Override
            protected INTERVALDS convert(Duration duration, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                long l2 = duration.getSeconds();
                int n2 = (int)Math.signum(l2);
                l2 = Math.abs(l2);
                int n3 = Math.abs(duration.getNano());
                int n4 = n2 * (int)(l2 / 86400L);
                int n5 = n2 * (int)((l2 %= 86400L) / 3600L);
                int n6 = n2 * (int)((l2 %= 3600L) / 60L);
                l2 %= 60L;
                byte[] byArray = new byte[11];
                byArray[0] = (byte)((n4 += INTERVALDAYOFFSET) >> 24 & 0xFF);
                byArray[1] = (byte)(n4 >> 16 & 0xFF);
                byArray[2] = (byte)(n4 >> 8 & 0xFF);
                byArray[3] = (byte)(n4 & 0xFF);
                byArray[4] = (byte)(n5 + INTERVALDSOFFSET);
                byArray[5] = (byte)(n6 + INTERVALDSOFFSET);
                byArray[6] = (byte)(l2 + (long)INTERVALDSOFFSET);
                n3 = n2 * n3 + INTERVALDAYOFFSET;
                byArray[7] = (byte)(n3 >> 24 & 0xFF);
                byArray[8] = (byte)(n3 >> 16 & 0xFF);
                byArray[9] = (byte)(n3 >> 8 & 0xFF);
                byArray[10] = (byte)(n3 & 0xFF);
                return new INTERVALDS(byArray);
            }
        });
        CONVERTERS.put(new Key(Duration.class, String.class), new JavaToJavaConverter<Duration, String>(){

            @Override
            protected String convert(Duration duration, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return JavaToJavaConverter.convert(duration, INTERVALDS.class, oracleConnection, object, object2).toString();
            }
        });
        CONVERTERS.put(new Key(Float.class, NUMBER.class), new JavaToJavaConverter<Float, NUMBER>(){

            @Override
            protected NUMBER convert(Float f2, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(f2.floatValue());
            }
        });
        CONVERTERS.put(new Key(InputStream.class, CLOB.class), new JavaToJavaConverter<InputStream, CLOB>(){

            @Override
            protected CLOB convert(InputStream inputStream, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                int n2;
                CLOB cLOB = (CLOB)oracleConnection.createClob();
                OutputStream outputStream = cLOB.setAsciiStream(1L);
                long l2 = Long.MAX_VALUE;
                if (object != null && object instanceof Number) {
                    l2 = ((Number)object).longValue();
                }
                for (long i2 = 0L; i2 < l2 && (n2 = inputStream.read()) != -1; ++i2) {
                    outputStream.write(n2);
                }
                outputStream.close();
                return cLOB;
            }
        });
        CONVERTERS.put(new Key(InputStream.class, NCLOB.class), new JavaToJavaConverter<InputStream, CLOB>(){

            @Override
            protected NCLOB convert(InputStream inputStream, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                int n2;
                NCLOB nCLOB = (NCLOB)oracleConnection.createNClob();
                OutputStream outputStream = nCLOB.setAsciiStream(1L);
                long l2 = Long.MAX_VALUE;
                if (object != null && object instanceof Number) {
                    l2 = ((Number)object).longValue();
                }
                for (long i2 = 0L; i2 < l2 && (n2 = inputStream.read()) != -1; ++i2) {
                    outputStream.write(n2);
                }
                outputStream.close();
                return nCLOB;
            }
        });
        CONVERTERS.put(new Key(InputStream.class, String.class), new JavaToJavaConverter<InputStream, String>(){

            @Override
            protected String convert(InputStream inputStream, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                String string;
                block4: {
                    int n2;
                    long l2 = Long.MAX_VALUE;
                    if (object != null && object instanceof Number) {
                        l2 = ((Number)object).longValue();
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    for (long i2 = 0L; i2 < l2 && (n2 = inputStream.read()) != -1; ++i2) {
                        byteArrayOutputStream.write(n2);
                    }
                    string = null;
                    try {
                        string = byteArrayOutputStream.toString("US-ASCII");
                    }
                    catch (UnsupportedEncodingException unsupportedEncodingException) {
                        if ($assertionsDisabled) break block4;
                        throw new AssertionError((Object)"can't happen");
                    }
                }
                return string;
            }
        });
        CONVERTERS.put(new Key(Integer.class, NUMBER.class), new JavaToJavaConverter<Integer, NUMBER>(){

            @Override
            protected NUMBER convert(Integer n2, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(n2);
            }
        });
        CONVERTERS.put(new Key(INTERVALDS.class, String.class), new JavaToJavaConverter<INTERVALDS, String>(){

            @Override
            protected String convert(INTERVALDS iNTERVALDS, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return iNTERVALDS.stringValue();
            }
        });
        CONVERTERS.put(new Key(INTERVALYM.class, String.class), new JavaToJavaConverter<INTERVALYM, String>(){

            @Override
            protected String convert(INTERVALYM iNTERVALYM, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return iNTERVALYM.stringValue();
            }
        });
        CONVERTERS.put(new Key(LocalDate.class, Date.class), new JavaToJavaConverter<LocalDate, Date>(){

            @Override
            protected Date convert(LocalDate localDate, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Date.valueOf(localDate);
            }
        });
        CONVERTERS.put(new Key(LocalDate.class, DATE.class), new JavaToJavaConverter<LocalDate, DATE>(){

            @Override
            protected DATE convert(LocalDate localDate, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createDATE(Date.valueOf(localDate));
            }
        });
        CONVERTERS.put(new Key(LocalDate.class, java.util.Date.class), new JavaToJavaConverter<LocalDate, java.util.Date>(){

            @Override
            protected java.util.Date convert(LocalDate localDate, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return java.util.Date.from(LocalDateTime.of(localDate, LocalTime.MIDNIGHT).toInstant(ZoneOffset.UTC));
            }
        });
        CONVERTERS.put(new Key(LocalDate.class, String.class), new JavaToJavaConverter<LocalDate, String>(){

            @Override
            protected String convert(LocalDate localDate, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
        });
        CONVERTERS.put(new Key(LocalDate.class, Timestamp.class), new JavaToJavaConverter<LocalDate, Timestamp>(){

            @Override
            protected Timestamp convert(LocalDate localDate, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Timestamp.valueOf(LocalDateTime.of(localDate, LocalTime.MIDNIGHT));
            }
        });
        CONVERTERS.put(new Key(LocalDate.class, TIMESTAMP.class), new JavaToJavaConverter<LocalDate, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(LocalDate localDate, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createTIMESTAMP(Timestamp.valueOf(LocalDateTime.of(localDate, LocalTime.MIDNIGHT)));
            }
        });
        CONVERTERS.put(new Key(LocalDateTime.class, Date.class), new JavaToJavaConverter<LocalDateTime, Date>(){

            @Override
            protected Date convert(LocalDateTime localDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Date.valueOf(localDateTime.toLocalDate());
            }
        });
        CONVERTERS.put(new Key(LocalDateTime.class, DATE.class), new JavaToJavaConverter<LocalDateTime, DATE>(){

            @Override
            protected DATE convert(LocalDateTime localDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createDATE(Timestamp.valueOf(localDateTime));
            }
        });
        CONVERTERS.put(new Key(LocalDateTime.class, java.util.Date.class), new JavaToJavaConverter<LocalDateTime, java.util.Date>(){

            @Override
            protected java.util.Date convert(LocalDateTime localDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return java.util.Date.from(localDateTime.toInstant(ZoneOffset.UTC));
            }
        });
        CONVERTERS.put(new Key(LocalDateTime.class, String.class), new JavaToJavaConverter<LocalDateTime, String>(){

            @Override
            protected String convert(LocalDateTime localDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return localDateTime.format(DATE_TIME_FORMAT);
            }
        });
        CONVERTERS.put(new Key(LocalDateTime.class, Time.class), new JavaToJavaConverter<LocalDateTime, Time>(){

            @Override
            protected Time convert(LocalDateTime localDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Time.valueOf(localDateTime.toLocalTime());
            }
        });
        CONVERTERS.put(new Key(LocalDateTime.class, Timestamp.class), new JavaToJavaConverter<LocalDateTime, Timestamp>(){

            @Override
            protected Timestamp convert(LocalDateTime localDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Timestamp.valueOf(localDateTime);
            }
        });
        CONVERTERS.put(new Key(LocalDateTime.class, TIMESTAMP.class), new JavaToJavaConverter<LocalDateTime, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(LocalDateTime localDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createTIMESTAMP(Timestamp.valueOf(localDateTime));
            }
        });
        CONVERTERS.put(new Key(LocalDateTime.class, TIMESTAMPLTZ.class), new JavaToJavaConverter<LocalDateTime, TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(LocalDateTime localDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPLTZ((Connection)oracleConnection, localDateTime);
            }
        });
        CONVERTERS.put(new Key(LocalDateTime.class, TIMESTAMPTZ.class), new JavaToJavaConverter<LocalDateTime, TIMESTAMPTZ>(){

            @Override
            protected TIMESTAMPTZ convert(LocalDateTime localDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPTZ((Connection)oracleConnection, localDateTime);
            }
        });
        CONVERTERS.put(new Key(LocalTime.class, DATE.class), new JavaToJavaConverter<LocalTime, DATE>(){

            @Override
            protected DATE convert(LocalTime localTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createDATE(new Time(localTime.getLong(ChronoField.MILLI_OF_DAY)), this.cachedUTCUSCalendar);
            }
        });
        CONVERTERS.put(new Key(LocalTime.class, java.util.Date.class), new JavaToJavaConverter<LocalTime, java.util.Date>(){

            @Override
            protected java.util.Date convert(LocalTime localTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new java.util.Date(localTime.getLong(ChronoField.MILLI_OF_DAY));
            }
        });
        CONVERTERS.put(new Key(LocalTime.class, String.class), new JavaToJavaConverter<LocalTime, String>(){

            @Override
            protected String convert(LocalTime localTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return localTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
            }
        });
        CONVERTERS.put(new Key(LocalTime.class, Time.class), new JavaToJavaConverter<LocalTime, Time>(){

            @Override
            protected Time convert(LocalTime localTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Time.valueOf(localTime);
            }
        });
        CONVERTERS.put(new Key(LocalTime.class, Timestamp.class), new JavaToJavaConverter<LocalTime, Timestamp>(){

            @Override
            protected Timestamp convert(LocalTime localTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Timestamp timestamp = new Timestamp(localTime.getLong(ChronoField.MILLI_OF_DAY));
                timestamp.setNanos(localTime.get(ChronoField.NANO_OF_SECOND));
                return timestamp;
            }
        });
        CONVERTERS.put(new Key(LocalTime.class, TIMESTAMP.class), new JavaToJavaConverter<LocalTime, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(LocalTime localTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Timestamp timestamp = new Timestamp(localTime.getLong(ChronoField.MILLI_OF_DAY));
                timestamp.setNanos(localTime.get(ChronoField.NANO_OF_SECOND));
                return oracleConnection.createTIMESTAMP(timestamp, this.cachedUTCUSCalendar);
            }
        });
        CONVERTERS.put(new Key(Long.class, NUMBER.class), new JavaToJavaConverter<Long, NUMBER>(){

            @Override
            protected NUMBER convert(Long l2, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(l2);
            }
        });
        CONVERTERS.put(new Key(Number.class, BigDecimal.class), new JavaToJavaConverter<Number, BigDecimal>(){

            @Override
            protected BigDecimal convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new BigDecimal(number.toString());
            }
        });
        CONVERTERS.put(new Key(Number.class, BINARY_DOUBLE.class), new JavaToJavaConverter<Number, BINARY_DOUBLE>(){

            @Override
            protected BINARY_DOUBLE convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new BINARY_DOUBLE(number.doubleValue());
            }
        });
        CONVERTERS.put(new Key(Number.class, BINARY_FLOAT.class), new JavaToJavaConverter<Number, BINARY_FLOAT>(){

            @Override
            protected BINARY_FLOAT convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new BINARY_FLOAT(number.floatValue());
            }
        });
        CONVERTERS.put(new Key(Number.class, Boolean.class), new JavaToJavaConverter<Number, Boolean>(){

            @Override
            protected Boolean convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return !number.equals(0.0);
            }
        });
        CONVERTERS.put(new Key(Number.class, Byte.class), new JavaToJavaConverter<Number, Byte>(){

            @Override
            protected Byte convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return number.byteValue();
            }
        });
        CONVERTERS.put(new Key(Number.class, Double.class), new JavaToJavaConverter<Number, Double>(){

            @Override
            protected Double convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return number.doubleValue();
            }
        });
        CONVERTERS.put(new Key(Number.class, Float.class), new JavaToJavaConverter<Number, Float>(){

            @Override
            protected Float convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Float.valueOf(number.floatValue());
            }
        });
        CONVERTERS.put(new Key(Number.class, Integer.class), new JavaToJavaConverter<Number, Integer>(){

            @Override
            protected Integer convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return number.intValue();
            }
        });
        CONVERTERS.put(new Key(Number.class, Long.class), new JavaToJavaConverter<Number, Long>(){

            @Override
            protected Long convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return number.longValue();
            }
        });
        CONVERTERS.put(new Key(Number.class, NUMBER.class), new JavaToJavaConverter<Number, NUMBER>(){

            @Override
            protected NUMBER convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(new BigDecimal(number.toString()));
            }
        });
        CONVERTERS.put(new Key(Number.class, Short.class), new JavaToJavaConverter<Number, Short>(){

            @Override
            protected Short convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return number.shortValue();
            }
        });
        CONVERTERS.put(new Key(Number.class, String.class), new JavaToJavaConverter<Number, String>(){

            @Override
            protected String convert(Number number, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return number.toString();
            }
        });
        CONVERTERS.put(new Key(NUMBER.class, BigDecimal.class), new JavaToJavaConverter<NUMBER, BigDecimal>(){

            @Override
            protected BigDecimal convert(NUMBER nUMBER, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return nUMBER.bigDecimalValue();
            }
        });
        CONVERTERS.put(new Key(NUMBER.class, BigInteger.class), new JavaToJavaConverter<NUMBER, BigInteger>(){

            @Override
            protected BigInteger convert(NUMBER nUMBER, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return nUMBER.bigIntegerValue();
            }
        });
        CONVERTERS.put(new Key(NUMBER.class, Boolean.class), new JavaToJavaConverter<NUMBER, Boolean>(){

            @Override
            protected Boolean convert(NUMBER nUMBER, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return nUMBER.booleanValue();
            }
        });
        CONVERTERS.put(new Key(NUMBER.class, Byte.class), new JavaToJavaConverter<NUMBER, Byte>(){

            @Override
            protected Byte convert(NUMBER nUMBER, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return nUMBER.byteValue();
            }
        });
        CONVERTERS.put(new Key(NUMBER.class, Double.class), new JavaToJavaConverter<NUMBER, Double>(){

            @Override
            protected Double convert(NUMBER nUMBER, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return nUMBER.doubleValue();
            }
        });
        CONVERTERS.put(new Key(NUMBER.class, Float.class), new JavaToJavaConverter<NUMBER, Float>(){

            @Override
            protected Float convert(NUMBER nUMBER, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Float.valueOf(nUMBER.floatValue());
            }
        });
        CONVERTERS.put(new Key(NUMBER.class, Integer.class), new JavaToJavaConverter<NUMBER, Integer>(){

            @Override
            protected Integer convert(NUMBER nUMBER, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return nUMBER.intValue();
            }
        });
        CONVERTERS.put(new Key(NUMBER.class, Long.class), new JavaToJavaConverter<NUMBER, Long>(){

            @Override
            protected Long convert(NUMBER nUMBER, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return nUMBER.longValue();
            }
        });
        CONVERTERS.put(new Key(NUMBER.class, Short.class), new JavaToJavaConverter<NUMBER, Short>(){

            @Override
            protected Short convert(NUMBER nUMBER, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return nUMBER.shortValue();
            }
        });
        CONVERTERS.put(new Key(NUMBER.class, String.class), new JavaToJavaConverter<NUMBER, String>(){

            @Override
            protected String convert(NUMBER nUMBER, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return nUMBER.stringValue();
            }
        });
        CONVERTERS.put(new Key(Object.class, CHAR.class), new JavaToJavaConverter<Object, CHAR>(){

            @Override
            protected CHAR convert(Object object, OracleConnection oracleConnection, Object object2, Object object3) throws Exception {
                CharacterSet characterSet = null;
                if (object3 != null && object3 instanceof CharacterSet) {
                    characterSet = (CharacterSet)object3;
                } else if (oracleConnection != null) {
                    characterSet = CharacterSet.make(oracleConnection.getJdbcCsId());
                }
                if (characterSet == null) {
                    throw new IllegalArgumentException("Charset needs to be defined while coverting to CHAR type");
                }
                return new CHAR(object, characterSet);
            }
        });
        CONVERTERS.put(new Key(OffsetDateTime.class, Date.class), new JavaToJavaConverter<OffsetDateTime, Date>(){

            @Override
            protected Date convert(OffsetDateTime offsetDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Date.valueOf(offsetDateTime.toLocalDate());
            }
        });
        CONVERTERS.put(new Key(OffsetDateTime.class, DATE.class), new JavaToJavaConverter<OffsetDateTime, DATE>(){

            @Override
            protected DATE convert(OffsetDateTime offsetDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createDATE(Date.valueOf(offsetDateTime.toLocalDate()));
            }
        });
        CONVERTERS.put(new Key(OffsetDateTime.class, java.util.Date.class), new JavaToJavaConverter<OffsetDateTime, java.util.Date>(){

            @Override
            protected java.util.Date convert(OffsetDateTime offsetDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return java.util.Date.from(offsetDateTime.toInstant());
            }
        });
        CONVERTERS.put(new Key(OffsetDateTime.class, String.class), new JavaToJavaConverter<OffsetDateTime, String>(){

            @Override
            protected String convert(OffsetDateTime offsetDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return offsetDateTime.format(OFFSET_DATE_TIME_FORMAT);
            }
        });
        CONVERTERS.put(new Key(OffsetDateTime.class, Time.class), new JavaToJavaConverter<OffsetDateTime, Time>(){

            @Override
            protected Time convert(OffsetDateTime offsetDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Time.valueOf(offsetDateTime.toLocalTime());
            }
        });
        CONVERTERS.put(new Key(OffsetDateTime.class, Timestamp.class), new JavaToJavaConverter<OffsetDateTime, Timestamp>(){

            @Override
            protected Timestamp convert(OffsetDateTime offsetDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Timestamp.valueOf(offsetDateTime.toLocalDateTime());
            }
        });
        CONVERTERS.put(new Key(OffsetDateTime.class, TIMESTAMP.class), new JavaToJavaConverter<OffsetDateTime, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(OffsetDateTime offsetDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createTIMESTAMP(Timestamp.valueOf(offsetDateTime.toLocalDateTime()));
            }
        });
        CONVERTERS.put(new Key(OffsetDateTime.class, TIMESTAMPLTZ.class), new JavaToJavaConverter<OffsetDateTime, TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(OffsetDateTime offsetDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPLTZ((Connection)oracleConnection, offsetDateTime);
            }
        });
        CONVERTERS.put(new Key(OffsetDateTime.class, TIMESTAMPTZ.class), new JavaToJavaConverter<OffsetDateTime, TIMESTAMPTZ>(){

            @Override
            protected TIMESTAMPTZ convert(OffsetDateTime offsetDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPTZ(offsetDateTime);
            }
        });
        CONVERTERS.put(new Key(OffsetTime.class, DATE.class), new JavaToJavaConverter<OffsetTime, DATE>(){

            @Override
            protected DATE convert(OffsetTime offsetTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createDATE(new Time(offsetTime.getLong(ChronoField.MILLI_OF_DAY)));
            }
        });
        CONVERTERS.put(new Key(OffsetTime.class, java.util.Date.class), new JavaToJavaConverter<OffsetTime, java.util.Date>(){

            @Override
            protected java.util.Date convert(OffsetTime offsetTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new java.util.Date(offsetTime.getLong(ChronoField.MILLI_OF_DAY));
            }
        });
        CONVERTERS.put(new Key(OffsetTime.class, String.class), new JavaToJavaConverter<OffsetTime, String>(){

            @Override
            protected String convert(OffsetTime offsetTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return offsetTime.format(DateTimeFormatter.ISO_OFFSET_TIME);
            }
        });
        CONVERTERS.put(new Key(OffsetTime.class, Time.class), new JavaToJavaConverter<OffsetTime, Time>(){

            @Override
            protected Time convert(OffsetTime offsetTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Time.valueOf(offsetTime.toLocalTime());
            }
        });
        CONVERTERS.put(new Key(OffsetTime.class, Timestamp.class), new JavaToJavaConverter<OffsetTime, Timestamp>(){

            @Override
            protected Timestamp convert(OffsetTime offsetTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Timestamp timestamp = new Timestamp(offsetTime.getLong(ChronoField.MILLI_OF_DAY));
                timestamp.setNanos(offsetTime.get(ChronoField.NANO_OF_SECOND));
                return timestamp;
            }
        });
        CONVERTERS.put(new Key(OffsetTime.class, TIMESTAMP.class), new JavaToJavaConverter<OffsetTime, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(OffsetTime offsetTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Timestamp timestamp = new Timestamp(offsetTime.getLong(ChronoField.MILLI_OF_DAY));
                timestamp.setNanos(offsetTime.get(ChronoField.NANO_OF_SECOND));
                return oracleConnection.createTIMESTAMP(timestamp);
            }
        });
        CONVERTERS.put(new Key(OffsetTime.class, TIMESTAMPLTZ.class), new JavaToJavaConverter<OffsetTime, TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(OffsetTime offsetTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPLTZ((Connection)oracleConnection, offsetTime);
            }
        });
        CONVERTERS.put(new Key(OffsetTime.class, TIMESTAMPTZ.class), new JavaToJavaConverter<OffsetTime, TIMESTAMPTZ>(){

            @Override
            protected TIMESTAMPTZ convert(OffsetTime offsetTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPTZ(offsetTime);
            }
        });
        CONVERTERS.put(new Key(OracleBfile.class, InputStream.class), new JavaToJavaConverter<OracleBfile, InputStream>(){

            @Override
            protected InputStream convert(OracleBfile oracleBfile, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleBfile.binaryStreamValue();
            }
        });
        CONVERTERS.put(new Key(OracleBfile.class, Reader.class), new JavaToJavaConverter<OracleBfile, Reader>(){

            @Override
            protected Reader convert(OracleBfile oracleBfile, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleBfile.characterStreamValue();
            }
        });
        CONVERTERS.put(new Key(OracleBlob.class, InputStream.class), new JavaToJavaConverter<OracleBlob, InputStream>(){

            @Override
            protected InputStream convert(OracleBlob oracleBlob, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleBlob.getBinaryStream();
            }
        });
        CONVERTERS.put(new Key(OracleBlob.class, Reader.class), new JavaToJavaConverter<OracleBlob, Reader>(){

            @Override
            protected Reader convert(OracleBlob oracleBlob, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return ((BLOB)oracleBlob).characterStreamValue();
            }
        });
        CONVERTERS.put(new Key(OracleClob.class, InputStream.class), new JavaToJavaConverter<OracleClob, InputStream>(){

            @Override
            protected InputStream convert(OracleClob oracleClob, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleClob.binaryStreamValue();
            }
        });
        CONVERTERS.put(new Key(OracleClob.class, Reader.class), new JavaToJavaConverter<OracleClob, Reader>(){

            @Override
            protected Reader convert(OracleClob oracleClob, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleClob.getCharacterStream();
            }
        });
        CONVERTERS.put(new Key(OracleClob.class, String.class), new JavaToJavaConverter<OracleClob, String>(){

            @Override
            protected String convert(OracleClob oracleClob, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleClob.stringValue();
            }
        });
        CONVERTERS.put(new Key(OracleNClob.class, InputStream.class), new JavaToJavaConverter<OracleNClob, InputStream>(){

            @Override
            protected InputStream convert(OracleNClob oracleNClob, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleNClob.binaryStreamValue();
            }
        });
        CONVERTERS.put(new Key(OracleNClob.class, Reader.class), new JavaToJavaConverter<OracleNClob, Reader>(){

            @Override
            protected Reader convert(OracleNClob oracleNClob, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleNClob.getCharacterStream();
            }
        });
        CONVERTERS.put(new Key(OracleNClob.class, String.class), new JavaToJavaConverter<OracleNClob, String>(){

            @Override
            protected String convert(OracleNClob oracleNClob, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleNClob.stringValue();
            }
        });
        CONVERTERS.put(new Key(OracleOpaque.class, byte[].class), new JavaToJavaConverter<OracleOpaque, byte[]>(){

            @Override
            protected byte[] convert(OracleOpaque oracleOpaque, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleOpaque.getBytes();
            }
        });
        CONVERTERS.put(new Key(Period.class, INTERVALYM.class), new JavaToJavaConverter<Period, INTERVALYM>(){

            @Override
            protected INTERVALYM convert(Period period, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                if (period.getDays() > 0) {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)oracleConnection, 132, period.getClass().getName() + " to " + Period.class.getName()).fillInStackTrace();
                }
                int n2 = period.getYears();
                int n3 = period.getMonths();
                byte[] byArray = new byte[]{(byte)((n2 += INTYMYEAROFFSET) >> 24 & 0xFF), (byte)(n2 >> 16 & 0xFF), (byte)(n2 >> 8 & 0xFF), (byte)(n2 & 0xFF), (byte)(n3 + INTYMMONTHOFFSET)};
                return new INTERVALYM(byArray);
            }
        });
        CONVERTERS.put(new Key(Period.class, String.class), new JavaToJavaConverter<Period, String>(){

            @Override
            protected String convert(Period period, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                if (period.getDays() > 0) {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)oracleConnection, 132, period.getClass().getName() + " to " + String.class.getName()).fillInStackTrace();
                }
                int n2 = period.getYears();
                int n3 = period.getMonths();
                if (Math.signum(n2) != Math.signum(n3)) {
                    throw (SQLException)DatabaseError.createSqlException((oracle.jdbc.internal.OracleConnection)oracleConnection, 132, period.getClass().getName() + " to " + String.class.getName()).fillInStackTrace();
                }
                String string = "";
                if (n2 < 0) {
                    n2 = Math.abs(n2);
                    n3 = Math.abs(n3);
                    string = "-";
                }
                return string + n2 + "-" + n3;
            }
        });
        CONVERTERS.put(new Key(RAW.class, byte[].class), new JavaToJavaConverter<RAW, byte[]>(){

            @Override
            protected byte[] convert(RAW rAW, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return rAW.getBytes();
            }
        });
        CONVERTERS.put(new Key(RAW.class, InputStream.class), new JavaToJavaConverter<RAW, InputStream>(){

            @Override
            protected InputStream convert(RAW rAW, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return rAW.binaryStreamValue();
            }
        });
        CONVERTERS.put(new Key(RAW.class, Reader.class), new JavaToJavaConverter<RAW, Reader>(){

            @Override
            protected Reader convert(RAW rAW, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return rAW.characterStreamValue();
            }
        });
        CONVERTERS.put(new Key(RAW.class, String.class), new JavaToJavaConverter<RAW, String>(){

            @Override
            protected String convert(RAW rAW, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return rAW.stringValue();
            }
        });
        CONVERTERS.put(new Key(Reader.class, CLOB.class), new JavaToJavaConverter<Reader, CLOB>(){

            @Override
            protected CLOB convert(Reader reader, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                int n2;
                CLOB cLOB = (CLOB)((Object)oracleConnection.createNClob());
                Writer writer = cLOB.setCharacterStream(1L);
                long l2 = Long.MAX_VALUE;
                if (object != null && object instanceof Number) {
                    l2 = ((Number)object).longValue();
                }
                for (long i2 = 0L; i2 < l2 && (n2 = reader.read()) != -1; ++i2) {
                    writer.write(n2);
                }
                writer.close();
                return cLOB;
            }
        });
        CONVERTERS.put(new Key(Reader.class, NCLOB.class), new JavaToJavaConverter<Reader, NCLOB>(){

            @Override
            protected NCLOB convert(Reader reader, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                int n2;
                NCLOB nCLOB = (NCLOB)oracleConnection.createNClob();
                Writer writer = nCLOB.setCharacterStream(1L);
                long l2 = Long.MAX_VALUE;
                if (object != null && object instanceof Number) {
                    l2 = ((Number)object).longValue();
                }
                for (long i2 = 0L; i2 < l2 && (n2 = reader.read()) != -1; ++i2) {
                    writer.write(n2);
                }
                writer.close();
                return nCLOB;
            }
        });
        CONVERTERS.put(new Key(Reader.class, String.class), new JavaToJavaConverter<Reader, String>(){

            @Override
            protected String convert(Reader reader, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                int n2;
                StringBuilder stringBuilder = new StringBuilder();
                long l2 = Long.MAX_VALUE;
                if (object != null && object instanceof Number) {
                    l2 = ((Number)object).longValue();
                }
                int n3 = 0;
                while ((long)n3 < l2 && (n2 = reader.read()) != -1) {
                    stringBuilder.appendCodePoint(n2);
                    ++n3;
                }
                return stringBuilder.toString();
            }
        });
        CONVERTERS.put(new Key(RowId.class, String.class), new JavaToJavaConverter<RowId, String>(){

            @Override
            protected String convert(RowId rowId, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return rowId.toString();
            }
        });
        CONVERTERS.put(new Key(Short.class, NUMBER.class), new JavaToJavaConverter<Short, NUMBER>(){

            @Override
            protected NUMBER convert(Short s2, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(s2);
            }
        });
        CONVERTERS.put(new Key(SQLXML.class, String.class), new JavaToJavaConverter<SQLXML, String>(){

            @Override
            protected String convert(SQLXML sQLXML, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return sQLXML.toString();
            }
        });
        CONVERTERS.put(new Key(String.class, BigDecimal.class), new JavaToJavaConverter<String, BigDecimal>(){

            @Override
            protected BigDecimal convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new BigDecimal(string);
            }
        });
        CONVERTERS.put(new Key(String.class, BigInteger.class), new JavaToJavaConverter<String, BigInteger>(){

            @Override
            protected BigInteger convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new BigInteger(string);
            }
        });
        CONVERTERS.put(new Key(String.class, Byte.class), new JavaToJavaConverter<String, Byte>(){

            @Override
            protected Byte convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Byte(string);
            }
        });
        CONVERTERS.put(new Key(String.class, Calendar.class), new JavaToJavaConverter<String, Calendar>(){

            @Override
            protected Calendar convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateFormat.getInstance().parse(string));
                return calendar;
            }
        });
        CONVERTERS.put(new Key(String.class, Date.class), new JavaToJavaConverter<String, Date>(){

            @Override
            protected Date convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Date(DateFormat.getInstance().parse(string).getTime());
            }
        });
        CONVERTERS.put(new Key(String.class, java.util.Date.class), new JavaToJavaConverter<String, java.util.Date>(){

            @Override
            protected java.util.Date convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return DateFormat.getInstance().parse(string);
            }
        });
        CONVERTERS.put(new Key(String.class, DATE.class), new JavaToJavaConverter<String, DATE>(){

            @Override
            protected DATE convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new DATE(string);
            }
        });
        CONVERTERS.put(new Key(String.class, Double.class), new JavaToJavaConverter<String, Double>(){

            @Override
            protected Double convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Double(string);
            }
        });
        CONVERTERS.put(new Key(String.class, Float.class), new JavaToJavaConverter<String, Float>(){

            @Override
            protected Float convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Float(string);
            }
        });
        CONVERTERS.put(new Key(String.class, InputStream.class), new JavaToJavaConverter<String, InputStream>(){

            @Override
            protected InputStream convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new ByteArrayInputStream(string.getBytes());
            }
        });
        CONVERTERS.put(new Key(String.class, Integer.class), new JavaToJavaConverter<String, Integer>(){

            @Override
            protected Integer convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Integer(string);
            }
        });
        CONVERTERS.put(new Key(String.class, Long.class), new JavaToJavaConverter<String, Long>(){

            @Override
            protected Long convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Long(string);
            }
        });
        CONVERTERS.put(new Key(String.class, NUMBER.class), new JavaToJavaConverter<String, NUMBER>(){

            @Override
            protected NUMBER convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new NUMBER(new BigDecimal(string));
            }
        });
        CONVERTERS.put(new Key(String.class, Reader.class), new JavaToJavaConverter<String, Reader>(){

            @Override
            protected Reader convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new StringReader(string);
            }
        });
        CONVERTERS.put(new Key(String.class, Short.class), new JavaToJavaConverter<String, Short>(){

            @Override
            protected Short convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Short(string);
            }
        });
        CONVERTERS.put(new Key(String.class, Time.class), new JavaToJavaConverter<String, Time>(){

            @Override
            protected Time convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Time(DateFormat.getInstance().parse(string).getTime());
            }
        });
        CONVERTERS.put(new Key(String.class, Timestamp.class), new JavaToJavaConverter<String, Timestamp>(){

            @Override
            protected Timestamp convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Timestamp(DateFormat.getInstance().parse(string).getTime());
            }
        });
        CONVERTERS.put(new Key(String.class, TIMESTAMP.class), new JavaToJavaConverter<String, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMP(string);
            }
        });
        CONVERTERS.put(new Key(String.class, TIMESTAMPLTZ.class), new JavaToJavaConverter<String, TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPLTZ((Connection)oracleConnection, string);
            }
        });
        CONVERTERS.put(new Key(String.class, TIMESTAMPTZ.class), new JavaToJavaConverter<String, TIMESTAMPTZ>(){

            @Override
            protected TIMESTAMPTZ convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPTZ((Connection)oracleConnection, string);
            }
        });
        CONVERTERS.put(new Key(String.class, URL.class), new JavaToJavaConverter<String, URL>(){

            @Override
            protected URL convert(String string, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new URL(string);
            }
        });
        CONVERTERS.put(new Key(Time.class, Calendar.class), new JavaToJavaConverter<Time, Calendar>(){

            @Override
            protected Calendar convert(Time time, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(time.getTime()));
                return calendar;
            }
        });
        CONVERTERS.put(new Key(Time.class, Date.class), new JavaToJavaConverter<Time, Date>(){

            @Override
            protected Date convert(Time time, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Date(time.getTime());
            }
        });
        CONVERTERS.put(new Key(Time.class, java.util.Date.class), new JavaToJavaConverter<Time, java.util.Date>(){

            @Override
            protected java.util.Date convert(Time time, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new java.util.Date(time.getTime());
            }
        });
        CONVERTERS.put(new Key(Time.class, DATE.class), new JavaToJavaConverter<Time, DATE>(){

            @Override
            protected DATE convert(Time time, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new DATE(new Date(time.getTime()));
            }
        });
        CONVERTERS.put(new Key(Time.class, String.class), new JavaToJavaConverter<Time, String>(){

            @Override
            protected String convert(Time time, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return time.toString();
            }
        });
        CONVERTERS.put(new Key(Time.class, Timestamp.class), new JavaToJavaConverter<Time, Timestamp>(){

            @Override
            protected Timestamp convert(Time time, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Timestamp(time.getTime());
            }
        });
        CONVERTERS.put(new Key(Time.class, TIMESTAMP.class), new JavaToJavaConverter<Time, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(Time time, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMP(time);
            }
        });
        CONVERTERS.put(new Key(Time.class, TIMESTAMPLTZ.class), new JavaToJavaConverter<Time, TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(Time time, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPLTZ((Connection)oracleConnection, time);
            }
        });
        CONVERTERS.put(new Key(Time.class, TIMESTAMPTZ.class), new JavaToJavaConverter<Time, TIMESTAMPTZ>(){

            @Override
            protected TIMESTAMPTZ convert(Time time, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPTZ((Connection)oracleConnection, time);
            }
        });
        CONVERTERS.put(new Key(Timestamp.class, Calendar.class), new JavaToJavaConverter<Timestamp, Calendar>(){

            @Override
            protected Calendar convert(Timestamp timestamp, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Timestamp(timestamp.getTime()));
                return calendar;
            }
        });
        CONVERTERS.put(new Key(Timestamp.class, Date.class), new JavaToJavaConverter<Timestamp, Date>(){

            @Override
            protected Date convert(Timestamp timestamp, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Date(timestamp.getTime());
            }
        });
        CONVERTERS.put(new Key(Timestamp.class, java.util.Date.class), new JavaToJavaConverter<Timestamp, java.util.Date>(){

            @Override
            protected java.util.Date convert(Timestamp timestamp, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new java.util.Date(timestamp.getTime());
            }
        });
        CONVERTERS.put(new Key(Timestamp.class, DATE.class), new JavaToJavaConverter<Timestamp, DATE>(){

            @Override
            protected DATE convert(Timestamp timestamp, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new DATE(timestamp);
            }
        });
        CONVERTERS.put(new Key(Timestamp.class, String.class), new JavaToJavaConverter<Timestamp, String>(){

            @Override
            protected String convert(Timestamp timestamp, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return timestamp.toString();
            }
        });
        CONVERTERS.put(new Key(Timestamp.class, Time.class), new JavaToJavaConverter<Timestamp, Time>(){

            @Override
            protected Time convert(Timestamp timestamp, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new Time(timestamp.getTime());
            }
        });
        CONVERTERS.put(new Key(Timestamp.class, TIMESTAMP.class), new JavaToJavaConverter<Timestamp, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(Timestamp timestamp, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMP(timestamp);
            }
        });
        CONVERTERS.put(new Key(Timestamp.class, TIMESTAMPLTZ.class), new JavaToJavaConverter<Timestamp, TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(Timestamp timestamp, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPLTZ((Connection)oracleConnection, timestamp);
            }
        });
        CONVERTERS.put(new Key(Timestamp.class, TIMESTAMPTZ.class), new JavaToJavaConverter<Timestamp, TIMESTAMPTZ>(){

            @Override
            protected TIMESTAMPTZ convert(Timestamp timestamp, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPTZ((Connection)oracleConnection, timestamp);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMP.class, Calendar.class), new JavaToJavaConverter<TIMESTAMP, Calendar>(){

            @Override
            protected Calendar convert(TIMESTAMP tIMESTAMP, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tIMESTAMP.timestampValue());
                return calendar;
            }
        });
        CONVERTERS.put(new Key(TIMESTAMP.class, Date.class), new JavaToJavaConverter<TIMESTAMP, Date>(){

            @Override
            protected Date convert(TIMESTAMP tIMESTAMP, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMP.dateValue();
            }
        });
        CONVERTERS.put(new Key(TIMESTAMP.class, java.util.Date.class), new JavaToJavaConverter<TIMESTAMP, java.util.Date>(){

            @Override
            protected java.util.Date convert(TIMESTAMP tIMESTAMP, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new java.util.Date(tIMESTAMP.timestampValue().getTime());
            }
        });
        CONVERTERS.put(new Key(TIMESTAMP.class, DATE.class), new JavaToJavaConverter<TIMESTAMP, DATE>(){

            @Override
            protected DATE convert(TIMESTAMP tIMESTAMP, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new DATE(tIMESTAMP.dateValue());
            }
        });
        CONVERTERS.put(new Key(TIMESTAMP.class, String.class), new JavaToJavaConverter<TIMESTAMP, String>(){

            @Override
            protected String convert(TIMESTAMP tIMESTAMP, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMP.stringValue();
            }
        });
        CONVERTERS.put(new Key(TIMESTAMP.class, Time.class), new JavaToJavaConverter<TIMESTAMP, Time>(){

            @Override
            protected Time convert(TIMESTAMP tIMESTAMP, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMP.timeValue();
            }
        });
        CONVERTERS.put(new Key(TIMESTAMP.class, Timestamp.class), new JavaToJavaConverter<TIMESTAMP, Timestamp>(){

            @Override
            protected Timestamp convert(TIMESTAMP tIMESTAMP, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMP.timestampValue();
            }
        });
        CONVERTERS.put(new Key(TIMESTAMP.class, TIMESTAMPLTZ.class), new JavaToJavaConverter<TIMESTAMP, TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(TIMESTAMP tIMESTAMP, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPLTZ((Connection)oracleConnection, tIMESTAMP.timestampValue());
            }
        });
        CONVERTERS.put(new Key(TIMESTAMP.class, TIMESTAMPTZ.class), new JavaToJavaConverter<TIMESTAMP, TIMESTAMPTZ>(){

            @Override
            protected TIMESTAMPTZ convert(TIMESTAMP tIMESTAMP, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPTZ((Connection)oracleConnection, tIMESTAMP.timestampValue());
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, Calendar.class), new JavaToJavaConverter<TIMESTAMPLTZ, Calendar>(){

            @Override
            protected Calendar convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = this.getSessionCalendar(oracleConnection);
                calendar.setTime(tIMESTAMPLTZ.timestampValue(oracleConnection));
                return calendar;
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, Date.class), new JavaToJavaConverter<TIMESTAMPLTZ, Date>(){

            @Override
            protected Date convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPLTZ.dateValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, java.util.Date.class), new JavaToJavaConverter<TIMESTAMPLTZ, java.util.Date>(){

            @Override
            protected java.util.Date convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new java.util.Date(tIMESTAMPLTZ.timestampValue(oracleConnection).getTime());
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, DATE.class), new JavaToJavaConverter<TIMESTAMPLTZ, DATE>(){

            @Override
            protected DATE convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new DATE(tIMESTAMPLTZ.timestampValue(oracleConnection));
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, LocalDateTime.class), new JavaToJavaConverter<TIMESTAMPLTZ, LocalDateTime>(){

            @Override
            protected LocalDateTime convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPLTZ.localDateTimeValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, OffsetDateTime.class), new JavaToJavaConverter<TIMESTAMPLTZ, OffsetDateTime>(){

            @Override
            protected OffsetDateTime convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPLTZ.offsetDateTimeValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, ZonedDateTime.class), new JavaToJavaConverter<TIMESTAMPLTZ, ZonedDateTime>(){

            @Override
            protected ZonedDateTime convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPLTZ.zonedDateTimeValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, String.class), new JavaToJavaConverter<TIMESTAMPLTZ, String>(){

            @Override
            protected String convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPLTZ.stringValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, Time.class), new JavaToJavaConverter<TIMESTAMPLTZ, Time>(){

            @Override
            protected Time convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPLTZ.timeValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, Timestamp.class), new JavaToJavaConverter<TIMESTAMPLTZ, Timestamp>(){

            @Override
            protected Timestamp convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPLTZ.timestampValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, TIMESTAMP.class), new JavaToJavaConverter<TIMESTAMPLTZ, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMP(tIMESTAMPLTZ.timestampValue(oracleConnection));
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPLTZ.class, TIMESTAMPTZ.class), new JavaToJavaConverter<TIMESTAMPLTZ, TIMESTAMPTZ>(){

            @Override
            protected TIMESTAMPTZ convert(TIMESTAMPLTZ tIMESTAMPLTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = this.getSessionCalendar(oracleConnection);
                return new TIMESTAMPTZ((Connection)oracleConnection, tIMESTAMPLTZ.timestampValue(oracleConnection), calendar);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, Calendar.class), new JavaToJavaConverter<TIMESTAMPTZ, Calendar>(){

            @Override
            protected Calendar convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tIMESTAMPTZ.timestampValue(oracleConnection));
                calendar.setTimeZone(tIMESTAMPTZ.getTimeZone());
                return calendar;
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, Date.class), new JavaToJavaConverter<TIMESTAMPTZ, Date>(){

            @Override
            protected Date convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPTZ.dateValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, java.util.Date.class), new JavaToJavaConverter<TIMESTAMPTZ, java.util.Date>(){

            @Override
            protected java.util.Date convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new java.util.Date(tIMESTAMPTZ.timestampValue(oracleConnection).getTime());
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, DATE.class), new JavaToJavaConverter<TIMESTAMPTZ, DATE>(){

            @Override
            protected DATE convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new DATE(tIMESTAMPTZ.timestampValue(oracleConnection));
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, OffsetDateTime.class), new JavaToJavaConverter<TIMESTAMPTZ, OffsetDateTime>(){

            @Override
            protected OffsetDateTime convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPTZ.offsetDateTimeValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, ZonedDateTime.class), new JavaToJavaConverter<TIMESTAMPTZ, ZonedDateTime>(){

            @Override
            protected ZonedDateTime convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPTZ.toZonedDateTime();
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, OffsetTime.class), new JavaToJavaConverter<TIMESTAMPTZ, OffsetTime>(){

            @Override
            protected OffsetTime convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPTZ.toOffsetTime();
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, String.class), new JavaToJavaConverter<TIMESTAMPTZ, String>(){

            @Override
            protected String convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPTZ.stringValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, Time.class), new JavaToJavaConverter<TIMESTAMPTZ, Time>(){

            @Override
            protected Time convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPTZ.timeValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, Timestamp.class), new JavaToJavaConverter<TIMESTAMPTZ, Timestamp>(){

            @Override
            protected Timestamp convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return tIMESTAMPTZ.timestampValue(oracleConnection);
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, TIMESTAMP.class), new JavaToJavaConverter<TIMESTAMPTZ, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMP(tIMESTAMPTZ.timestampValue(oracleConnection));
            }
        });
        CONVERTERS.put(new Key(TIMESTAMPTZ.class, TIMESTAMPLTZ.class), new JavaToJavaConverter<TIMESTAMPTZ, TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(TIMESTAMPTZ tIMESTAMPTZ, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                Calendar calendar = this.getSessionCalendar(oracleConnection);
                return new TIMESTAMPLTZ((Connection)oracleConnection, tIMESTAMPTZ.timestampValue(oracleConnection), calendar);
            }
        });
        CONVERTERS.put(new Key(URL.class, String.class), new JavaToJavaConverter<URL, String>(){

            @Override
            protected String convert(URL uRL, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return uRL.toString();
            }
        });
        CONVERTERS.put(new Key(ZonedDateTime.class, Date.class), new JavaToJavaConverter<ZonedDateTime, Date>(){

            @Override
            protected Date convert(ZonedDateTime zonedDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Date.valueOf(zonedDateTime.toLocalDate());
            }
        });
        CONVERTERS.put(new Key(ZonedDateTime.class, DATE.class), new JavaToJavaConverter<ZonedDateTime, DATE>(){

            @Override
            protected DATE convert(ZonedDateTime zonedDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createDATE(Timestamp.valueOf(zonedDateTime.toLocalDateTime()));
            }
        });
        CONVERTERS.put(new Key(ZonedDateTime.class, java.util.Date.class), new JavaToJavaConverter<ZonedDateTime, java.util.Date>(){

            @Override
            protected java.util.Date convert(ZonedDateTime zonedDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return java.util.Date.from(zonedDateTime.toInstant());
            }
        });
        CONVERTERS.put(new Key(ZonedDateTime.class, String.class), new JavaToJavaConverter<ZonedDateTime, String>(){

            @Override
            protected String convert(ZonedDateTime zonedDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return zonedDateTime.format(OFFSET_DATE_TIME_FORMAT);
            }
        });
        CONVERTERS.put(new Key(ZonedDateTime.class, Timestamp.class), new JavaToJavaConverter<ZonedDateTime, Timestamp>(){

            @Override
            protected Timestamp convert(ZonedDateTime zonedDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return Timestamp.valueOf(zonedDateTime.toLocalDateTime());
            }
        });
        CONVERTERS.put(new Key(ZonedDateTime.class, TIMESTAMP.class), new JavaToJavaConverter<ZonedDateTime, TIMESTAMP>(){

            @Override
            protected TIMESTAMP convert(ZonedDateTime zonedDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createTIMESTAMP(Timestamp.valueOf(zonedDateTime.toLocalDateTime()));
            }
        });
        CONVERTERS.put(new Key(ZonedDateTime.class, TIMESTAMPLTZ.class), new JavaToJavaConverter<ZonedDateTime, TIMESTAMPLTZ>(){

            @Override
            protected TIMESTAMPLTZ convert(ZonedDateTime zonedDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return oracleConnection.createTIMESTAMPLTZ(Timestamp.valueOf(zonedDateTime.toLocalDateTime()), Calendar.getInstance(TimeZone.getTimeZone(zonedDateTime.getOffset())));
            }
        });
        CONVERTERS.put(new Key(ZonedDateTime.class, TIMESTAMPTZ.class), new JavaToJavaConverter<ZonedDateTime, TIMESTAMPTZ>(){

            @Override
            protected TIMESTAMPTZ convert(ZonedDateTime zonedDateTime, OracleConnection oracleConnection, Object object, Object object2) throws Exception {
                return new TIMESTAMPTZ(zonedDateTime);
            }
        });
    }

    private static final class Key {
        private final Class<?> source;
        private final Class<?> target;
        private final int hash;

        Key(Class<?> clazz, Class<?> clazz2) {
            this.source = clazz;
            this.target = clazz2;
            this.hash = clazz.hashCode() ^ Integer.rotateRight(clazz2.hashCode(), 1);
        }

        @DisableTrace
        public int hashCode() {
            return this.hash;
        }

        @DisableTrace
        public boolean equals(Object object) {
            Key key = (Key)object;
            return this.hash == key.hash && this.source == key.source && this.target == key.target;
        }

        public boolean satisfies(Key key) {
            return this.source.isAssignableFrom(key.source) && key.target.isAssignableFrom(this.target);
        }

        @DisableTrace
        public String toString() {
            return "[" + this.source.getName() + " to " + this.target.getName() + "]";
        }
    }
}

