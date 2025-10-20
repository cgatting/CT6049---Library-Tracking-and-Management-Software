/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import oracle.core.lmx.CoreException;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.driver.VarnumBinder;
import oracle.jdbc.internal.OracleConnection;
import oracle.sql.Datum;

class BigDecimalBinder
extends VarnumBinder {
    BigDecimal paramVal;

    BigDecimalBinder(BigDecimal bigDecimal) {
        this.paramVal = bigDecimal;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        byte[] byArray2 = null;
        int n13 = 0;
        BigDecimal bigDecimal = this.paramVal;
        long l3 = 0L;
        if (bl2) {
            lArray[n11] = l3 = byteArray.getPosition();
            oraclePreparedStatement.lastBoundDataOffsets[n2] = l3;
            byArray2 = oraclePreparedStatement.connection.methodTempLittleByteBuffer;
            n13 = 0;
        } else {
            byArray2 = byArray;
            n13 = n7 + 1;
        }
        int n14 = this.getDatumBytes(oraclePreparedStatement, bigDecimal, byArray2, n13);
        if (bl2) {
            byteArray.put(byArray2, 0, n14);
            sArray[n10] = 0;
            nArray[n11] = n14;
            oraclePreparedStatement.lastBoundDataLengths[n2] = n14;
        } else {
            byArray2[n7] = (byte)n14;
            sArray[n10] = 0;
        }
        sArray[n9] = (short)(n14 + 1);
        if (oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
            l2 = CRC64.updateChecksum(l2, bigDecimal.toString());
        }
        return l2;
    }

    private int getDatumBytes(OraclePreparedStatement oraclePreparedStatement, BigDecimal bigDecimal, byte[] byArray, int n2) throws SQLException {
        char c2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12 = 0;
        String string = bigDecimal.toString();
        int n13 = string.indexOf("E");
        if (n13 != -1) {
            StringBuffer stringBuffer = new StringBuffer(string.length() + 5);
            n11 = 0;
            BigDecimal bigDecimal2 = null;
            n10 = string.charAt(0) == '-' ? 1 : 0;
            String string2 = string.substring(n13 + 1);
            String string3 = string.substring(n10 != 0 ? 1 : 0, n13);
            bigDecimal2 = new BigDecimal(string3);
            n9 = string2.charAt(0) == '-' ? 1 : 0;
            string2 = string2.substring(1);
            n11 = Integer.parseInt(string2);
            String string4 = bigDecimal2.toString();
            n8 = string4.indexOf(".");
            n7 = string4.length();
            n6 = n7--;
            if (n8 != -1) {
                string4 = string4.substring(0, n8) + string4.substring(n8 + 1);
                if (n9 != 0) {
                    n11 -= n8;
                } else {
                    n6 = ++n11;
                }
            } else if (n9 != 0) {
                n11 -= n7;
            } else {
                n6 = ++n11;
            }
            if (n10 != 0) {
                stringBuffer.append("-");
            }
            if (n9 != 0) {
                stringBuffer.append("0.");
                for (n5 = 0; n5 < n11; ++n5) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(string4);
            } else {
                n5 = n11 > n7 ? n11 : n7;
                for (n4 = 0; n4 < n5; ++n4) {
                    if (n6 == n4) {
                        stringBuffer.append(".");
                    }
                    stringBuffer.append(n7 > n4 ? string4.charAt(n4) : (char)'0');
                }
            }
            string = stringBuffer.toString();
        }
        int n14 = string.length();
        n11 = string.indexOf(46);
        n10 = n3 = string.charAt(0) == '-' ? 1 : 0;
        int n15 = 2;
        n8 = n14;
        if (n11 == -1) {
            n11 = n14;
        } else if ((n14 - n11 & 1) != 0) {
            n8 = n14 + 1;
        }
        while (n10 < n14 && ((c2 = string.charAt(n10)) < '1' || c2 > '9')) {
            ++n10;
        }
        if (n10 >= n14) {
            byArray[n2] = -128;
            n12 = 1;
        } else {
            int n16 = n10 < n11 ? 2 - (n11 - n10 & 1) : 1 + (n10 - n11 & 1);
            n9 = (n11 - n10 - 1) / 2;
            if (n9 > 62) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, CoreException.getMessage((byte)3) + " trying to bind " + bigDecimal).fillInStackTrace();
            }
            if (n9 < -65) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, CoreException.getMessage((byte)2) + " trying to bind " + bigDecimal).fillInStackTrace();
            }
            n7 = n10 + n16 + 38;
            if (n7 > n14) {
                n7 = n14;
            }
            for (n6 = n10 + n16; n6 < n7; n6 += 2) {
                if (n6 == n11) {
                    --n6;
                    if (n7 >= n14) continue;
                    ++n7;
                    continue;
                }
                if (string.charAt(n6) == '0' && (n6 + 1 >= n14 || string.charAt(n6 + 1) == '0')) continue;
                n15 = (n6 - n10 - n16) / 2 + 3;
            }
            n5 = n2 + 2;
            n6 = n10 + n16;
            if (n3 == 0) {
                byArray[n2] = (byte)(192 + n9 + 1);
                n4 = string.charAt(n10) - 48;
                if (n16 == 2) {
                    n4 = n4 * 10 + (n10 + 1 < n14 ? string.charAt(n10 + 1) - 48 : 0);
                }
                byArray[n2 + 1] = (byte)(n4 + 1);
                while (n5 < n2 + n15) {
                    if (n6 == n11) {
                        ++n6;
                    }
                    n4 = (string.charAt(n6) - 48) * 10;
                    if (n6 + 1 < n14) {
                        n4 += string.charAt(n6 + 1) - 48;
                    }
                    byArray[n5++] = (byte)(n4 + 1);
                    n6 += 2;
                }
            } else {
                byArray[n2] = (byte)(62 - n9);
                n4 = string.charAt(n10) - 48;
                if (n16 == 2) {
                    n4 = n4 * 10 + (n10 + 1 < n14 ? string.charAt(n10 + 1) - 48 : 0);
                }
                byArray[n2 + 1] = (byte)(101 - n4);
                while (n5 < n2 + n15) {
                    if (n6 == n11) {
                        ++n6;
                    }
                    n4 = (string.charAt(n6) - 48) * 10;
                    if (n6 + 1 < n14) {
                        n4 += string.charAt(n6 + 1) - 48;
                    }
                    byArray[n5++] = (byte)(101 - n4);
                    n6 += 2;
                }
                if (n15 < 21) {
                    byArray[n2 + n15++] = 102;
                }
            }
            n12 = n15;
        }
        return n12;
    }

    @Override
    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        byte[] byArray = oraclePreparedStatement.connection.methodTempLittleByteBuffer;
        int n5 = this.getDatumBytes(oraclePreparedStatement, this.paramVal, byArray, 0);
        return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, Arrays.copyOf(byArray, n5), n4, null, 0);
    }
}

