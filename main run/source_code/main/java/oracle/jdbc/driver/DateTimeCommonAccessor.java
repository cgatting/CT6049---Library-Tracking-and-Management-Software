/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.OracleDriver;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.DATE;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TIMEZONETAB;
import oracle.sql.ZONEIDMAP;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class DateTimeCommonAccessor
extends Accessor {
    static final int GREGORIAN_CUTOVER_YEAR = 1582;
    static final long GREGORIAN_CUTOVER = -12219292800000L;
    static final int JAN_1_1_JULIAN_DAY = 1721426;
    static final int EPOCH_JULIAN_DAY = 2440588;
    static final int ONE_SECOND = 1000;
    static final int ONE_MINUTE = 60000;
    static final int ONE_HOUR = 3600000;
    static final long ONE_DAY = 86400000L;
    static final int[] NUM_DAYS = new int[]{0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
    static final int[] LEAP_NUM_DAYS = new int[]{0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
    static final int ORACLE_CENTURY = 0;
    static final int ORACLE_YEAR = 1;
    static final int ORACLE_MONTH = 2;
    static final int ORACLE_DAY = 3;
    static final int ORACLE_HOUR = 4;
    static final int ORACLE_MIN = 5;
    static final int ORACLE_SEC = 6;
    static final int ORACLE_NANO1 = 7;
    static final int ORACLE_NANO2 = 8;
    static final int ORACLE_NANO3 = 9;
    static final int ORACLE_NANO4 = 10;
    static final int ORACLE_TZ1 = 11;
    static final int ORACLE_TZ2 = 12;
    static final int SIZE_DATE = 7;
    static int OFFSET_HOUR = 20;
    static int OFFSET_MINUTE = 60;
    static byte REGIONIDBIT = (byte)-128;
    static final int MAX_TIMESTAMP_LENGTH = 11;
    protected final byte[] tmpBytes;
    static TimeZone epochTimeZone;
    static long epochTimeZoneOffset;

    static int setHighOrderbits(int n2) {
        return (n2 & 0x1FC0) >> 6;
    }

    static int setLowOrderbits(int n2) {
        return (n2 & 0x3F) << 2;
    }

    static int getHighOrderbits(int n2) {
        return (n2 & 0x7F) << 6;
    }

    static int getLowOrderbits(int n2) {
        return (n2 & 0xFC) >> 2;
    }

    DateTimeCommonAccessor(Representation representation, OracleStatement oracleStatement, int n2, boolean bl) {
        super(representation, oracleStatement, n2, bl);
        this.tmpBytes = new byte[this.representationMaxLength];
    }

    @Override
    Date getDate(int n2) throws SQLException {
        return this.getDate(n2, null);
    }

    @Override
    Date getDate(int n2, Calendar calendar) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        Calendar calendar2 = calendar == null ? this.statement.getDefaultCalendar() : (Calendar)calendar.clone();
        this.getBytesInternal(n2, this.tmpBytes);
        int n3 = this.oracleYear(this.tmpBytes);
        calendar2.clear();
        calendar2.set(1, n3);
        calendar2.set(2, this.oracleMonth(this.tmpBytes));
        calendar2.set(5, this.oracleDay(this.tmpBytes));
        if (OracleDriver.getSystemPropertyDateZeroTime()) {
            calendar2.set(11, 0);
            calendar2.set(12, 0);
            calendar2.set(13, 0);
        } else {
            calendar2.set(11, this.oracleHour(this.tmpBytes));
            calendar2.set(12, this.oracleMin(this.tmpBytes));
            calendar2.set(13, this.oracleSec(this.tmpBytes));
        }
        calendar2.set(14, 0);
        if (n3 > 0 && calendar2.isSet(0)) {
            calendar2.set(0, 1);
        }
        Date date = new Date(calendar2.getTimeInMillis());
        return date;
    }

    @Override
    LocalDate getLocalDate(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        for (int i2 = 0; i2 < this.tmpBytes.length; ++i2) {
            this.tmpBytes[i2] = 0;
        }
        this.getBytesInternal(n2, this.tmpBytes);
        return LocalDate.of(this.oracleYear(this.tmpBytes), this.oracleMonth(this.tmpBytes) + 1, this.oracleDay(this.tmpBytes));
    }

    @Override
    LocalDateTime getLocalDateTime(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        for (int i2 = 0; i2 < this.tmpBytes.length; ++i2) {
            this.tmpBytes[i2] = 0;
        }
        this.getBytesInternal(n2, this.tmpBytes);
        return LocalDateTime.of(this.oracleYear(this.tmpBytes), this.oracleMonth(this.tmpBytes) + 1, this.oracleDay(this.tmpBytes), this.oracleHour(this.tmpBytes), this.oracleMin(this.tmpBytes), this.oracleSec(this.tmpBytes), this.tmpBytes.length > 7 ? this.oracleNanos(this.tmpBytes) : 0);
    }

    @Override
    LocalTime getLocalTime(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        for (int i2 = 0; i2 < this.tmpBytes.length; ++i2) {
            this.tmpBytes[i2] = 0;
        }
        this.getBytesInternal(n2, this.tmpBytes);
        return LocalTime.of(this.oracleHour(this.tmpBytes), this.oracleMin(this.tmpBytes), this.oracleSec(this.tmpBytes), this.tmpBytes.length > 7 ? this.oracleNanos(this.tmpBytes) : 0);
    }

    @Override
    OffsetDateTime getOffsetDateTime(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        for (int i2 = 0; i2 < this.tmpBytes.length; ++i2) {
            this.tmpBytes[i2] = 0;
        }
        this.getBytesInternal(n2, this.tmpBytes);
        LocalDateTime localDateTime = LocalDateTime.of(this.oracleYear(this.tmpBytes), this.oracleMonth(this.tmpBytes) + 1, this.oracleDay(this.tmpBytes), this.oracleHour(this.tmpBytes), this.oracleMin(this.tmpBytes), this.oracleSec(this.tmpBytes), this.tmpBytes.length > 7 ? this.oracleNanos(this.tmpBytes) : 0);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
        if (this.tmpBytes.length > 11) {
            ZoneOffset zoneOffset;
            if ((this.oracleTZ1(this.tmpBytes) & REGIONIDBIT) != 0) {
                int n3 = 0;
                n3 = DateTimeCommonAccessor.getHighOrderbits(this.oracleTZ1(this.tmpBytes));
                long l2 = offsetDateTime.toInstant().toEpochMilli();
                TIMEZONETAB tIMEZONETAB = this.statement.connection.getTIMEZONETAB();
                if (tIMEZONETAB.checkID(n3 += DateTimeCommonAccessor.getLowOrderbits(this.oracleTZ2(this.tmpBytes)))) {
                    tIMEZONETAB.updateTable(this.statement.connection, n3);
                }
                int n4 = tIMEZONETAB.getOffset(l2, n3);
                zoneOffset = ZoneOffset.ofTotalSeconds(n4 / 1000);
            } else {
                int n5 = this.oracleTZ1(this.tmpBytes) - OFFSET_HOUR;
                int n6 = Math.abs(this.oracleTZ2(this.tmpBytes) - OFFSET_MINUTE);
                zoneOffset = ZoneOffset.ofHoursMinutes(n5, (int)Math.signum(n5) * n6);
            }
            return offsetDateTime.withOffsetSameInstant(zoneOffset);
        }
        return offsetDateTime;
    }

    @Override
    OffsetTime getOffsetTime(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        for (int i2 = 0; i2 < this.tmpBytes.length; ++i2) {
            this.tmpBytes[i2] = 0;
        }
        this.getBytesInternal(n2, this.tmpBytes);
        LocalTime localTime = LocalTime.of(this.oracleHour(this.tmpBytes), this.oracleMin(this.tmpBytes), this.oracleSec(this.tmpBytes), this.tmpBytes.length > 7 ? this.oracleNanos(this.tmpBytes) : 0);
        OffsetTime offsetTime = OffsetTime.of(localTime, ZoneOffset.UTC);
        if (this.tmpBytes.length > 11) {
            ZoneOffset zoneOffset;
            if ((this.oracleTZ1(this.tmpBytes) & REGIONIDBIT) != 0) {
                int n3 = 0;
                n3 = DateTimeCommonAccessor.getHighOrderbits(this.oracleTZ1(this.tmpBytes));
                long l2 = offsetTime.atDate(LocalDate.now()).toInstant().toEpochMilli();
                TIMEZONETAB tIMEZONETAB = this.statement.connection.getTIMEZONETAB();
                if (tIMEZONETAB.checkID(n3 += DateTimeCommonAccessor.getLowOrderbits(this.oracleTZ2(this.tmpBytes)))) {
                    tIMEZONETAB.updateTable(this.statement.connection, n3);
                }
                int n4 = tIMEZONETAB.getOffset(l2, n3);
                zoneOffset = ZoneOffset.ofTotalSeconds(n4 / 1000);
            } else {
                int n5 = this.oracleTZ1(this.tmpBytes) - OFFSET_HOUR;
                int n6 = Math.abs(this.oracleTZ2(this.tmpBytes) - OFFSET_MINUTE);
                zoneOffset = ZoneOffset.ofHoursMinutes(n5, (int)Math.signum(n5) * n6);
            }
            return offsetTime.withOffsetSameInstant(zoneOffset);
        }
        return offsetTime;
    }

    @Override
    ZonedDateTime getZonedDateTime(int n2) throws SQLException {
        ZoneId zoneId;
        if (this.isNull(n2)) {
            return null;
        }
        for (int i2 = 0; i2 < this.tmpBytes.length; ++i2) {
            this.tmpBytes[i2] = 0;
        }
        this.getBytesInternal(n2, this.tmpBytes);
        LocalDateTime localDateTime = LocalDateTime.of(this.oracleYear(this.tmpBytes), this.oracleMonth(this.tmpBytes) + 1, this.oracleDay(this.tmpBytes), this.oracleHour(this.tmpBytes), this.oracleMin(this.tmpBytes), this.oracleSec(this.tmpBytes), this.tmpBytes.length > 7 ? this.oracleNanos(this.tmpBytes) : 0);
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
        if (this.tmpBytes.length <= 11) {
            zoneId = offsetDateTime.getOffset();
        } else if ((this.oracleTZ1(this.tmpBytes) & REGIONIDBIT) != 0) {
            int n3 = DateTimeCommonAccessor.getHighOrderbits(this.oracleTZ1(this.tmpBytes)) + DateTimeCommonAccessor.getLowOrderbits(this.oracleTZ2(this.tmpBytes));
            try {
                zoneId = ZoneId.of(ZONEIDMAP.getRegion(n3));
            }
            catch (DateTimeException dateTimeException) {
                long l2 = offsetDateTime.toInstant().toEpochMilli();
                TIMEZONETAB tIMEZONETAB = this.statement.connection.getTIMEZONETAB();
                if (tIMEZONETAB.checkID(n3)) {
                    tIMEZONETAB.updateTable(this.statement.connection, n3);
                }
                int n4 = tIMEZONETAB.getOffset(l2, n3);
                zoneId = ZoneOffset.ofTotalSeconds(n4 / 1000);
            }
        } else {
            int n5 = this.oracleTZ1(this.tmpBytes) - OFFSET_HOUR;
            int n6 = Math.abs(this.oracleTZ2(this.tmpBytes) - OFFSET_MINUTE);
            zoneId = ZoneOffset.ofHoursMinutes(n5, (int)Math.signum(n5) * n6);
        }
        return offsetDateTime.atZoneSameInstant(zoneId);
    }

    @Override
    Time getTime(int n2) throws SQLException {
        Time time = null;
        if (this.isNull(n2)) {
            return null;
        }
        TimeZone timeZone = this.statement.getDefaultTimeZone();
        if (timeZone != epochTimeZone) {
            epochTimeZoneOffset = DateTimeCommonAccessor.calculateEpochOffset(timeZone);
            epochTimeZone = timeZone;
        }
        this.getBytesInternal(n2, this.tmpBytes);
        time = new Time((long)this.oracleTime(this.tmpBytes) - epochTimeZoneOffset);
        return time;
    }

    @Override
    Time getTime(int n2, Calendar calendar) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        if (calendar == null) {
            return this.getTime(n2);
        }
        this.getBytesInternal(n2, this.tmpBytes);
        int n3 = this.oracleYear(this.tmpBytes);
        Calendar calendar2 = (Calendar)calendar.clone();
        calendar2.clear();
        calendar2.set(1, 1970);
        calendar2.set(2, 0);
        calendar2.set(5, 1);
        calendar2.set(11, this.oracleHour(this.tmpBytes));
        calendar2.set(12, this.oracleMin(this.tmpBytes));
        calendar2.set(13, this.oracleSec(this.tmpBytes));
        calendar2.set(14, 0);
        if (n3 > 0 && calendar2.isSet(0)) {
            calendar2.set(0, 1);
        }
        return new Time(calendar2.getTimeInMillis());
    }

    @Override
    Timestamp getTimestamp(int n2) throws SQLException {
        return this.getTimestamp(n2, null);
    }

    @Override
    Timestamp getTimestamp(int n2, Calendar calendar) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        Calendar calendar2 = calendar == null ? this.statement.getDefaultCalendar() : (Calendar)calendar.clone();
        this.getBytesInternal(n2, this.tmpBytes);
        int n3 = this.oracleYear(this.tmpBytes);
        calendar2.clear();
        calendar2.set(1, n3);
        calendar2.set(2, this.oracleMonth(this.tmpBytes));
        calendar2.set(5, this.oracleDay(this.tmpBytes));
        calendar2.set(11, this.oracleHour(this.tmpBytes));
        calendar2.set(12, this.oracleMin(this.tmpBytes));
        calendar2.set(13, this.oracleSec(this.tmpBytes));
        calendar2.set(14, 0);
        if (n3 > 0 && calendar2.isSet(0)) {
            calendar2.set(0, 1);
        }
        Timestamp timestamp = new Timestamp(calendar2.getTimeInMillis());
        if (this.getLength(n2) >= 11) {
            timestamp.setNanos(this.oracleNanos(this.tmpBytes));
        }
        return timestamp;
    }

    @Override
    DATE getDATE(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        byte[] byArray = new byte[7];
        this.rowData.get(this.getOffset(n2), byArray, 0, 7);
        return new DATE(byArray);
    }

    @Override
    TIMESTAMP getTIMESTAMP(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return new TIMESTAMP(this.getBytesInternal(n2));
    }

    @Override
    java.util.Date getJavaUtilDate(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        this.getBytesInternal(n2, this.tmpBytes);
        int n3 = this.oracleYear(this.tmpBytes);
        Calendar calendar = this.statement.getDefaultCalendar();
        calendar.set(1, n3);
        calendar.set(2, this.oracleMonth(this.tmpBytes));
        calendar.set(5, this.oracleDay(this.tmpBytes));
        calendar.set(11, this.oracleHour(this.tmpBytes));
        calendar.set(12, this.oracleMin(this.tmpBytes));
        calendar.set(13, this.oracleSec(this.tmpBytes));
        calendar.set(14, 0);
        if (n3 > 0 && calendar.isSet(0)) {
            calendar.set(0, 1);
        }
        java.util.Date date = calendar.getTime();
        return date;
    }

    @Override
    Calendar getCalendar(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        this.getBytesInternal(n2, this.tmpBytes);
        int n3 = this.oracleYear(this.tmpBytes);
        Calendar calendar = (Calendar)this.statement.getDefaultCalendar().clone();
        calendar.clear();
        calendar.set(1, n3);
        calendar.set(2, this.oracleMonth(this.tmpBytes));
        calendar.set(5, this.oracleDay(this.tmpBytes));
        calendar.set(11, this.oracleHour(this.tmpBytes));
        calendar.set(12, this.oracleMin(this.tmpBytes));
        calendar.set(13, this.oracleSec(this.tmpBytes));
        if (this.getLength(n2) >= 11) {
            calendar.set(14, this.oracleNanos(this.tmpBytes) / 1000000);
        } else {
            calendar.set(14, 0);
        }
        if (n3 > 0 && calendar.isSet(0)) {
            calendar.set(0, 1);
        }
        return calendar;
    }

    final int oracleYear(byte[] byArray) {
        int n2 = ((byArray[0] & 0xFF) - 100) * 100 + (byArray[1] & 0xFF) - 100;
        return n2 < 0 ? n2 + 1 : n2;
    }

    final int oracleMonth(byte[] byArray) {
        return byArray[2] - 1;
    }

    final int oracleDay(byte[] byArray) {
        return byArray[3];
    }

    final int oracleHour(byte[] byArray) {
        return byArray[4] - 1;
    }

    final int oracleMin(byte[] byArray) {
        return byArray[5] - 1;
    }

    final int oracleSec(byte[] byArray) {
        return byArray[6] - 1;
    }

    final int oracleTZ1(byte[] byArray) {
        return byArray[11];
    }

    final int oracleTZ2(byte[] byArray) {
        return byArray[12];
    }

    final int oracleTime(byte[] byArray) {
        int n2 = this.oracleHour(byArray);
        n2 *= 60;
        n2 += this.oracleMin(byArray);
        n2 *= 60;
        n2 += this.oracleSec(byArray);
        return n2 *= 1000;
    }

    final int oracleNanos(byte[] byArray) {
        int n2 = (byArray[7] & 0xFF) << 24;
        n2 |= (byArray[8] & 0xFF) << 16;
        n2 |= (byArray[9] & 0xFF) << 8;
        return n2 |= byArray[10] & 0xFF & 0xFF;
    }

    static final long computeJulianDay(boolean bl, int n2, int n3, int n4) {
        boolean bl2 = n2 % 4 == 0;
        int n5 = n2 - 1;
        long l2 = 365L * (long)n5 + DateTimeCommonAccessor.floorDivide(n5, 4L) + 1721423L;
        if (bl) {
            bl2 = bl2 && (n2 % 100 != 0 || n2 % 400 == 0);
            l2 += DateTimeCommonAccessor.floorDivide(n5, 400L) - DateTimeCommonAccessor.floorDivide(n5, 100L) + 2L;
        }
        return l2 + (long)n4 + (long)(bl2 ? LEAP_NUM_DAYS[n3] : NUM_DAYS[n3]);
    }

    static final long floorDivide(long l2, long l3) {
        return l2 >= 0L ? l2 / l3 : (l2 + 1L) / l3 - 1L;
    }

    static final long julianDayToMillis(long l2) {
        return (l2 - 2440588L) * 86400000L;
    }

    static final long zoneOffset(TimeZone timeZone, int n2, int n3, int n4, int n5, int n6) {
        return timeZone.getOffset(n2 < 0 ? 0 : 1, n2, n3, n4, n5, n6);
    }

    static long getMillis(int n2, int n3, int n4, int n5, TimeZone timeZone) {
        long l2;
        long l3;
        boolean bl = n2 >= 1582;
        if (bl != (l3 = ((l2 = DateTimeCommonAccessor.computeJulianDay(bl, n2, n3, n4)) - 2440588L) * 86400000L) >= -12219292800000L) {
            l2 = DateTimeCommonAccessor.computeJulianDay(!bl, n2, n3, n4);
            l3 = (l2 - 2440588L) * 86400000L;
        }
        return (l3 += (long)n5) - DateTimeCommonAccessor.zoneOffset(timeZone, n2, n3, n4, DateTimeCommonAccessor.julianDayToDayOfWeek(l2), n5);
    }

    static final int julianDayToDayOfWeek(long l2) {
        int n2;
        return n2 + ((n2 = (int)((l2 + 1L) % 7L)) < 0 ? 8 : 1);
    }

    static long calculateEpochOffset(TimeZone timeZone) {
        return DateTimeCommonAccessor.zoneOffset(timeZone, 1970, 0, 1, 5, 0);
    }

    String toText(int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl, String string) throws SQLException {
        return oracle.sql.TIMESTAMPTZ.toString(n2, n3, n4, n5, n6, n7, n8, string);
    }
}

