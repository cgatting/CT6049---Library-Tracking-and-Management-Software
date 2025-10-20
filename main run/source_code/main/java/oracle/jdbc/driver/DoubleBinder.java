/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Arrays;
import oracle.core.lmx.CoreException;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.driver.VarnumBinder;
import oracle.jdbc.internal.OracleConnection;
import oracle.sql.Datum;

class DoubleBinder
extends VarnumBinder {
    double paramVal;
    char[] digits = new char[20];

    DoubleBinder(double d2) {
        this.paramVal = d2;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        byte[] byArray2 = null;
        int n13 = 0;
        double d2 = this.paramVal;
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
        int n14 = this.getDatumBytes(oraclePreparedStatement, d2, byArray2, n13);
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
        return l2;
    }

    private int getDatumBytes(OraclePreparedStatement oraclePreparedStatement, double d2, byte[] byArray, int n2) throws SQLException {
        int n3 = 0;
        if (d2 == 0.0) {
            byArray[n2] = -128;
            n3 = 1;
        } else if (d2 == Double.POSITIVE_INFINITY) {
            byArray[n2] = -1;
            byArray[n2 + 1] = 101;
            n3 = 2;
        } else if (d2 == Double.NEGATIVE_INFINITY) {
            byArray[n2] = 0;
            n3 = 1;
        } else {
            long l2;
            int n4;
            int n5;
            boolean bl;
            boolean bl2 = bl = d2 < 0.0;
            if (bl) {
                d2 = -d2;
            }
            if ((n5 = ((n4 = (int)((l2 = Double.doubleToLongBits(d2)) >> 52 & 0x7FFL)) > 1023 ? 126 : 127) - (int)((double)(n4 - 1023) / 6.643856189774725)) < 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, CoreException.getMessage((byte)3) + " trying to bind " + d2).fillInStackTrace();
            }
            if (n5 > 192) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, CoreException.getMessage((byte)2) + " trying to bind " + d2).fillInStackTrace();
            }
            if (d2 > factorTable[n5]) {
                while (n5 > 0 && d2 > factorTable[--n5]) {
                }
            } else {
                while (n5 < 193 && d2 <= factorTable[n5 + 1]) {
                    ++n5;
                }
            }
            if (d2 == factorTable[n5]) {
                if (n5 < 65) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, CoreException.getMessage((byte)3) + " trying to bind " + d2).fillInStackTrace();
                }
                if (n5 > 192) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, CoreException.getMessage((byte)2) + " trying to bind " + d2).fillInStackTrace();
                }
                if (bl) {
                    byArray[n2] = (byte)(62 - (127 - n5));
                    byArray[n2 + 1] = 100;
                    byArray[n2 + 2] = 102;
                    n3 = 3;
                } else {
                    byArray[n2] = (byte)(192 + (128 - n5));
                    byArray[n2 + 1] = 2;
                    n3 = 2;
                }
            } else {
                int n6;
                if (n5 < 64) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, CoreException.getMessage((byte)3) + " trying to bind " + d2).fillInStackTrace();
                }
                if (n5 > 191) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, CoreException.getMessage((byte)2) + " trying to bind " + d2).fillInStackTrace();
                }
                long l3 = bl ? l2 & Long.MAX_VALUE : l2;
                long l4 = l3 & 0xFFFFFFFFFFFFFL;
                int n7 = n4;
                char[] cArray = oraclePreparedStatement.digits;
                if (n7 == 0) {
                    while ((l4 & 0x10000000000000L) == 0L) {
                        l4 <<= 1;
                        --n7;
                    }
                    n6 = 53 + n7;
                    ++n7;
                } else {
                    l4 |= 0x10000000000000L;
                    n6 = 53;
                }
                n3 = this.dtoa(byArray, n2, d2, bl, false, cArray, n7 -= 1023, l4, n6);
            }
        }
        return n3;
    }

    @Override
    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        byte[] byArray = oraclePreparedStatement.connection.methodTempLittleByteBuffer;
        int n5 = this.getDatumBytes(oraclePreparedStatement, this.paramVal, byArray, 0);
        return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, Arrays.copyOf(byArray, n5), n4, null, 0);
    }
}

