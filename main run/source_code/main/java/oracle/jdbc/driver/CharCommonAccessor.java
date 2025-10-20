/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CHAR;
import oracle.sql.CharacterSet;
import oracle.sql.Datum;
import oracle.sql.ROWID;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class CharCommonAccessor
extends Accessor {
    int max_len = 0;
    protected static final String[] DATE_FORMATS = new String[]{"yyyy-MM-dd HH:mm:ss z", "EEE MMM dd HH:mm:ss z yyyy", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss z", "HH:mm:ss"};
    private static final DateTimeFormatter ORACLE_DATE_TIME = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 1, 9, SignStyle.NORMAL).appendLiteral('-').appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NEVER).appendLiteral('-').appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NEVER).optionalStart().appendLiteral(' ').appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NEVER).appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 1, 2, SignStyle.NEVER).appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 1, 2, SignStyle.NEVER).optionalStart().appendLiteral('.').appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, false).optionalEnd().optionalEnd().optionalStart().appendLiteral(' ').appendZoneId().optionalEnd().toFormatter();
    private static final DateTimeFormatter ORACLE_TIME = new DateTimeFormatterBuilder().appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NEVER).appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 1, 2, SignStyle.NEVER).appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 1, 2, SignStyle.NEVER).optionalStart().appendLiteral('.').appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, false).optionalEnd().optionalStart().appendLiteral(' ').appendZoneId().optionalEnd().toFormatter();
    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = new DateTimeFormatter[]{ORACLE_DATE_TIME, DateTimeFormatter.ISO_DATE_TIME, DateTimeFormatter.RFC_1123_DATE_TIME, DateTimeFormatter.ISO_DATE, ORACLE_TIME, DateTimeFormatter.ISO_TIME};

    CharCommonAccessor(OracleStatement oracleStatement, int n2, short s2, boolean bl) {
        super(s2 == 2 ? Representation.NVARCHAR : Representation.VARCHAR, oracleStatement, n2, bl);
    }

    void init(OracleStatement oracleStatement, int n2, int n3, int n4, short s2, int n5, boolean bl, int n6) throws SQLException {
        if (bl) {
            if (n2 != 23) {
                n2 = 1;
            }
            if (oracleStatement.maxFieldSize > 0 && (n4 == -1 || n4 < oracleStatement.maxFieldSize)) {
                n4 = oracleStatement.maxFieldSize;
            }
        }
        this.init(oracleStatement, n2, n3, s2, bl);
        if (bl && oracleStatement.connection.defaultnchar) {
            this.formOfUse = (short)2;
        }
        this.initForDataAccess(n5, n4, null);
    }

    void init(OracleStatement oracleStatement, int n2, int n3, int n4, boolean bl, int n5, int n6, int n7, long l2, int n8, short s2, int n9) throws SQLException {
        this.init(oracleStatement, n2, n3, s2, false);
        this.initForDescribe(n2, n4, bl, n5, n6, n7, l2, n8, s2, null, n9);
        int n10 = oracleStatement.maxFieldSize;
        if (n10 != 0 && n10 <= n4) {
            n4 = n10;
        }
        this.initForDataAccess(0, n4, null);
    }

    @Override
    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        this.max_len = n3;
        if (n2 != 0) {
            this.externalType = n2;
        }
        this.charLength = this.isNullByDescribe ? 0 : (n3 >= 0 && (n3 < this.representationMaxLength || this.statement.isFetchStreams) ? n3 + 1 : this.representationMaxLength + 1);
    }

    @Override
    int getInt(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0;
        }
        String string = this.getString(n2);
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException numberFormatException) {
            try {
                return Double.valueOf(string).intValue();
            }
            catch (NumberFormatException numberFormatException2) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 59).fillInStackTrace();
            }
        }
    }

    @Override
    boolean getBoolean(int n2) throws SQLException {
        String string = this.getString(n2);
        if (string == null || string.trim().equals("0") || string.trim().compareToIgnoreCase("f") == 0 || string.trim().compareToIgnoreCase("false") == 0 || string.trim().compareToIgnoreCase("n") == 0 || string.trim().compareToIgnoreCase("no") == 0) {
            return false;
        }
        if (string.trim().equals("1") || string.trim().compareToIgnoreCase("t") == 0 || string.trim().compareToIgnoreCase("true") == 0 || string.trim().compareToIgnoreCase("y") == 0 || string.trim().compareToIgnoreCase("yes") == 0) {
            return true;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 59).fillInStackTrace();
    }

    @Override
    short getShort(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0;
        }
        String string = this.getString(n2);
        try {
            return Short.valueOf(string);
        }
        catch (NumberFormatException numberFormatException) {
            try {
                return Double.valueOf(string).shortValue();
            }
            catch (NumberFormatException numberFormatException2) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 59).fillInStackTrace();
            }
        }
    }

    @Override
    byte getByte(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0;
        }
        String string = this.getString(n2);
        try {
            return Byte.valueOf(string);
        }
        catch (NumberFormatException numberFormatException) {
            try {
                return Double.valueOf(string).byteValue();
            }
            catch (NumberFormatException numberFormatException2) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 59).fillInStackTrace();
            }
        }
    }

    @Override
    long getLong(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0L;
        }
        String string = this.getString(n2);
        try {
            return Long.valueOf(string);
        }
        catch (NumberFormatException numberFormatException) {
            try {
                return Double.valueOf(string).longValue();
            }
            catch (NumberFormatException numberFormatException2) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 59).fillInStackTrace();
            }
        }
    }

    @Override
    float getFloat(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0.0f;
        }
        String string = this.getString(n2);
        try {
            return Float.valueOf(string).floatValue();
        }
        catch (NumberFormatException numberFormatException) {
            try {
                return Double.valueOf(string).floatValue();
            }
            catch (NumberFormatException numberFormatException2) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 59).fillInStackTrace();
            }
        }
    }

    @Override
    double getDouble(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0.0;
        }
        try {
            return Double.valueOf(this.getString(n2));
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 59).fillInStackTrace();
        }
    }

    @Override
    BigDecimal getBigDecimal(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        try {
            return new BigDecimal(this.getString(n2).trim());
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 59).fillInStackTrace();
        }
    }

    @Override
    BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        BigDecimal bigDecimal = this.getBigDecimal(n2);
        if (bigDecimal != null) {
            bigDecimal.setScale(n3, 6);
        }
        return bigDecimal;
    }

    @Override
    BigInteger getBigInteger(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        return this.getBigDecimal(n2).toBigInteger();
    }

    @Override
    String getString(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        String string = this.rowData.getString(this.getOffset(n2), this.getLength(n2), this.statement.connection.conversion.getCharacterSet(this.formOfUse));
        if (string != null && this.max_len > 0 && this.max_len < string.length()) {
            return string.substring(0, this.max_len);
        }
        return string;
    }

    @Override
    Date getDate(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        Date date = null;
        try {
            date = this.getJavaSqlDate(this.getString(n2).trim());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132, null, (Throwable)illegalArgumentException).fillInStackTrace();
        }
        return date;
    }

    Date getJavaSqlDate(String string) throws SQLException {
        if (null == string) {
            throw new IllegalArgumentException();
        }
        int n2 = string.indexOf(45);
        if (0 == n2) {
            String string2 = string.substring(1);
            Date date = Date.valueOf(string2);
            Calendar calendar = this.statement.getDefaultCalendar();
            calendar.setTime(date);
            calendar.set(1, (calendar.get(1) - 1) * -1);
            return new Date(calendar.getTimeInMillis());
        }
        return Date.valueOf(string);
    }

    @Override
    Time getTime(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        Time time = null;
        try {
            time = Time.valueOf(this.getString(n2).trim());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132, null, (Throwable)illegalArgumentException).fillInStackTrace();
        }
        return time;
    }

    @Override
    Timestamp getTimestamp(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        Timestamp timestamp = null;
        try {
            timestamp = this.getJavaSqlTimestamp(this.getString(n2).trim());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132, null, (Throwable)illegalArgumentException).fillInStackTrace();
        }
        return timestamp;
    }

    Timestamp getJavaSqlTimestamp(String string) throws SQLException {
        if (null == string) {
            throw new IllegalArgumentException();
        }
        int n2 = string.indexOf(45);
        if (0 == n2) {
            String string2 = string.substring(1);
            Timestamp timestamp = Timestamp.valueOf(string2);
            Calendar calendar = this.statement.getDefaultCalendar();
            calendar.setTime(timestamp);
            calendar.set(1, (calendar.get(1) - 1) * -1);
            return new Timestamp(calendar.getTimeInMillis());
        }
        return Timestamp.valueOf(string);
    }

    @Override
    java.util.Date getJavaUtilDate(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        java.util.Date date = null;
        for (String string : DATE_FORMATS) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(string);
            try {
                date = simpleDateFormat.parse(this.getString(n2).trim());
            }
            catch (ParseException parseException) {
                continue;
            }
            if (date != null) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date);
                if (gregorianCalendar.isSet(0) && gregorianCalendar.get(0) == 0 && gregorianCalendar.get(1) != 1) {
                    gregorianCalendar.set(1, gregorianCalendar.get(1) - 1);
                    date = gregorianCalendar.getTime();
                }
            }
            return date;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
    }

    @Override
    Calendar getCalendar(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        Calendar calendar = (Calendar)this.statement.getDefaultCalendar().clone();
        calendar.setTime(this.getJavaUtilDate(n2));
        return calendar;
    }

    @Override
    InputStream getAsciiStream(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        DBConversion dBConversion = this.statement.connection.conversion;
        int[] nArray = new int[1];
        char[] cArray = this.rowData.getChars(this.getOffset(n2), this.getLength(n2), dBConversion.getCharacterSet(this.formOfUse), nArray);
        return dBConversion.CharsToStream(cArray, 0, nArray[0], 10);
    }

    @Override
    InputStream getUnicodeStream(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        DBConversion dBConversion = this.statement.connection.conversion;
        int[] nArray = new int[1];
        char[] cArray = this.rowData.getChars(this.getOffset(n2), this.getLength(n2), dBConversion.getCharacterSet(this.formOfUse), nArray);
        return dBConversion.CharsToStream(cArray, 0, nArray[0] << 1, 11);
    }

    @Override
    Reader getCharacterStream(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        return new StringReader(this.getString(n2));
    }

    @Override
    InputStream getBinaryStream(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        return new ByteArrayInputStream(this.getBytes(n2));
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getString(n2);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.getString(n2);
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getCHAR(n2);
    }

    @Override
    CHAR getCHAR(int n2) throws SQLException {
        byte[] byArray = this.getBytes(n2);
        if (byArray == null || byArray.length == 0) {
            return null;
        }
        CharacterSet characterSet = this.statement.connection.conversion.getCharacterSet(this.formOfUse);
        return new CHAR(byArray, characterSet);
    }

    @Override
    URL getURL(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        try {
            return new URL(this.getString(n2));
        }
        catch (MalformedURLException malformedURLException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 136).fillInStackTrace();
        }
    }

    @Override
    ROWID getROWID(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        byte[] byArray = this.getBytesInternal(n2);
        ROWID rOWID = null;
        if (byArray != null) {
            rOWID = new ROWID(byArray);
        }
        return rOWID;
    }

    byte[] getBytesFromHexChars(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        byte[] byArray = this.rowData.getBytesFromHex(this.getOffset(n2), this.getLength(n2), this.statement.connection.conversion.getCharacterSet(this.formOfUse));
        if (byArray.length > this.charLength - 1) {
            byte[] byArray2 = new byte[this.charLength - 1];
            System.arraycopy(byArray, 0, byArray2, 0, byArray2.length);
            byArray = byArray2;
        }
        return byArray;
    }

    @Override
    LocalDate getLocalDate(int n2) throws SQLException {
        LocalDate localDate = null;
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        String string = this.getString(n2);
        for (DateTimeFormatter dateTimeFormatter : DATE_TIME_FORMATTERS) {
            try {
                localDate = LocalDate.parse(string, dateTimeFormatter);
            }
            catch (DateTimeException dateTimeException) {
                // empty catch block
            }
        }
        if (localDate != null) {
            if (localDate.getYear() < 0) {
                localDate = localDate.minusYears(-1L);
            }
            return localDate;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
    }

    @Override
    LocalDateTime getLocalDateTime(int n2) throws SQLException {
        LocalDateTime localDateTime = null;
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        String string = this.getString(n2);
        for (DateTimeFormatter dateTimeFormatter : DATE_TIME_FORMATTERS) {
            try {
                localDateTime = LocalDateTime.parse(string, dateTimeFormatter);
            }
            catch (DateTimeException dateTimeException) {
                // empty catch block
            }
        }
        if (localDateTime != null) {
            if (localDateTime.getYear() < 0) {
                localDateTime = localDateTime.minusYears(-1L);
            }
            return localDateTime;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
    }

    @Override
    LocalTime getLocalTime(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        String string = this.getString(n2);
        for (DateTimeFormatter dateTimeFormatter : DATE_TIME_FORMATTERS) {
            try {
                return LocalTime.parse(string, dateTimeFormatter);
            }
            catch (DateTimeException dateTimeException) {
            }
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
    }

    @Override
    OffsetDateTime getOffsetDateTime(int n2) throws SQLException {
        OffsetDateTime offsetDateTime = null;
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        String string = this.getString(n2);
        for (DateTimeFormatter dateTimeFormatter : DATE_TIME_FORMATTERS) {
            try {
                offsetDateTime = OffsetDateTime.parse(string, dateTimeFormatter);
            }
            catch (DateTimeException dateTimeException) {
                // empty catch block
            }
        }
        if (offsetDateTime != null) {
            if (offsetDateTime.getYear() < 0) {
                offsetDateTime = offsetDateTime.minusYears(-1L);
            }
            return offsetDateTime;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
    }

    @Override
    OffsetTime getOffsetTime(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        String string = this.getString(n2);
        for (DateTimeFormatter dateTimeFormatter : DATE_TIME_FORMATTERS) {
            try {
                return OffsetTime.parse(string, dateTimeFormatter);
            }
            catch (DateTimeException dateTimeException) {
            }
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
    }

    @Override
    ZonedDateTime getZonedDateTime(int n2) throws SQLException {
        ZonedDateTime zonedDateTime = null;
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        String string = this.getString(n2);
        for (DateTimeFormatter dateTimeFormatter : DATE_TIME_FORMATTERS) {
            try {
                zonedDateTime = ZonedDateTime.parse(string, dateTimeFormatter);
            }
            catch (DateTimeException dateTimeException) {
                // empty catch block
            }
        }
        if (zonedDateTime != null) {
            if (zonedDateTime.getYear() < 0) {
                zonedDateTime = zonedDateTime.minusYears(-1L);
            }
            return zonedDateTime;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
    }
}

