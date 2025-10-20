/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OraclePreparedStatement;

abstract class DateCommonBinder
extends Binder {
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
    static final int[] MONTH_LENGTH = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    static final int[] LEAP_MONTH_LENGTH = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    static final int ORACLE_DATE_CENTURY = 0;
    static final int ORACLE_DATE_YEAR = 1;
    static final int ORACLE_DATE_MONTH = 2;
    static final int ORACLE_DATE_DAY = 3;
    static final int ORACLE_DATE_HOUR = 4;
    static final int ORACLE_DATE_MIN = 5;
    static final int ORACLE_DATE_SEC = 6;
    static final int ORACLE_DATE_NANO1 = 7;
    static final int ORACLE_DATE_NANO2 = 8;
    static final int ORACLE_DATE_NANO3 = 9;
    static final int ORACLE_DATE_NANO4 = 10;
    private static int HOUR_MILLISECOND = 3600000;
    private static int MINUTE_MILLISECOND = 60000;
    private static int SECOND_MILLISECOND = 1000;

    DateCommonBinder() {
    }

    static final long floorDivide(long l2, long l3) {
        return l2 >= 0L ? l2 / l3 : (l2 + 1L) / l3 - 1L;
    }

    static final int floorDivide(int n2, int n3) {
        return n2 >= 0 ? n2 / n3 : (n2 + 1) / n3 - 1;
    }

    static final int floorDivide(int n2, int n3, int[] nArray) {
        if (n2 >= 0) {
            nArray[0] = n2 % n3;
            return n2 / n3;
        }
        int n4 = (n2 + 1) / n3 - 1;
        nArray[0] = n2 - n4 * n3;
        return n4;
    }

    static final int floorDivide(long l2, int n2, int[] nArray) {
        if (l2 >= 0L) {
            nArray[0] = (int)(l2 % (long)n2);
            return (int)(l2 / (long)n2);
        }
        int n3 = (int)((l2 + 1L) / (long)n2 - 1L);
        nArray[0] = (int)(l2 - (long)(n3 * n2));
        return n3;
    }

    static void setOracleNanos(long l2, byte[] byArray, int n2) {
        byArray[10 + n2] = (byte)(l2 & 0xFFL);
        byArray[9 + n2] = (byte)(l2 >> 8 & 0xFFL);
        byArray[8 + n2] = (byte)(l2 >> 16 & 0xFFL);
        byArray[7 + n2] = (byte)(l2 >> 24 & 0xFFL);
    }

    static void setOracleHMS(int n2, byte[] byArray, int n3) {
        if (n2 < 0) {
            throw new RuntimeException("Assertion botch: negative time");
        }
        byArray[6 + n3] = (byte)((n2 /= 1000) % 60 + 1);
        byArray[5 + n3] = (byte)((n2 /= 60) % 60 + 1);
        byArray[4 + n3] = (byte)((n2 /= 60) + 1);
    }

    static final int setOracleCYMD(long l2, byte[] byArray, int n2, OraclePreparedStatement oraclePreparedStatement) throws SQLException {
        int n3;
        int n4;
        boolean bl;
        int n5;
        int n6;
        int n7;
        long l3;
        TimeZone timeZone = oraclePreparedStatement.getDefaultTimeZone(true);
        Calendar calendar = oraclePreparedStatement.getDefaultCalendar();
        calendar.setTimeInMillis(l2);
        int n8 = calendar.get(15);
        int n9 = calendar.get(16);
        long l4 = l2 + (long)n8;
        if (l4 >= -12219292800000L) {
            int n10;
            int n11;
            int n12;
            l3 = 2440588L + DateCommonBinder.floorDivide(l4, 86400000L) - 1721426L;
            if (l3 > 0L) {
                n12 = (int)(l3 / 146097L);
                n7 = (int)(l3 % 146097L);
                n11 = n7 / 36524;
                n6 = (n7 %= 36524) / 1461;
                n10 = (n7 %= 1461) / 365;
                n7 %= 365;
            } else {
                int[] nArray = new int[1];
                n12 = DateCommonBinder.floorDivide(l3, 146097, nArray);
                n11 = DateCommonBinder.floorDivide(nArray[0], 36524, nArray);
                n6 = DateCommonBinder.floorDivide(nArray[0], 1461, nArray);
                n10 = DateCommonBinder.floorDivide(nArray[0], 365, nArray);
                n7 = nArray[0];
            }
            n5 = 400 * n12 + 100 * n11 + 4 * n6 + n10;
            if (n11 == 4 || n10 == 4) {
                n7 = 365;
            } else {
                ++n5;
            }
            bl = (n5 & 3) == 0 && (n5 % 100 != 0 || n5 % 400 == 0);
            n4 = (int)((l3 + 1L) % 7L);
        } else {
            l3 = 2440588L + DateCommonBinder.floorDivide(l4, 86400000L) - 1721424L;
            n5 = (int)DateCommonBinder.floorDivide(4L * l3 + 1464L, 1461L);
            long l5 = 365 * (n5 - 1) + DateCommonBinder.floorDivide(n5 - 1, 4);
            n7 = (int)(l3 - l5);
            bl = (n5 & 3) == 0;
            n4 = (int)((l3 - 1L) % 7L);
        }
        int n13 = 0;
        int n14 = n3 = bl ? 60 : 59;
        if (n7 >= n3) {
            n13 = bl ? 1 : 2;
        }
        int n15 = (12 * (n7 + n13) + 6) / 367;
        int n16 = n7 - (bl ? LEAP_NUM_DAYS[n15] : NUM_DAYS[n15]) + 1;
        n4 += n4 < 0 ? 8 : 1;
        long l6 = l4 / 86400000L;
        n6 = (int)(l4 - l6 * 86400000L);
        if (n6 < 0) {
            n6 = (int)((long)n6 + 86400000L);
        }
        if ((long)(n6 += n9) >= 86400000L) {
            n6 = (int)((long)n6 - 86400000L);
            if (++n16 > (bl ? LEAP_MONTH_LENGTH[n15] : MONTH_LENGTH[n15])) {
                n16 = 1;
                if (++n15 == 12) {
                    n15 = 0;
                    ++n5;
                }
            }
        }
        if (n5 <= 0) {
            --n5;
        }
        if (n5 > 9999 || n5 < -4712) {
            throw (SQLException)DatabaseError.createSqlException(268).fillInStackTrace();
        }
        byArray[0 + n2] = (byte)(n5 / 100 + 100);
        byArray[1 + n2] = (byte)(n5 % 100 + 100);
        byArray[2 + n2] = (byte)(n15 + 1);
        byArray[3 + n2] = (byte)n16;
        return n6;
    }
}

