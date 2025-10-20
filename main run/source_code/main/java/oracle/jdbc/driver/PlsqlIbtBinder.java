/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Arrays;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OraclePreparedStatementReadOnly;
import oracle.jdbc.driver.PlsqlIbtBindInfo;
import oracle.sql.Datum;

class PlsqlIbtBinder
extends Binder {
    Binder thePlsqlIbtCopyingBinder = OraclePreparedStatementReadOnly.theStaticPlsqlIbtCopyingBinder;

    PlsqlIbtBinder() {
        PlsqlIbtBinder.init(this);
    }

    static void init(Binder binder) {
        binder.type = (short)998;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        PlsqlIbtBindInfo plsqlIbtBindInfo = oraclePreparedStatement.parameterPlsqlIbt[n4][n2];
        if (bl) {
            oraclePreparedStatement.parameterPlsqlIbt[n4][n2] = null;
        }
        int n13 = plsqlIbtBindInfo.ibtValueIndex;
        switch (plsqlIbtBindInfo.element_internal_type) {
            case 9: {
                for (int i2 = 0; i2 < plsqlIbtBindInfo.curLen; ++i2) {
                    int n14 = 0;
                    String string = (String)plsqlIbtBindInfo.arrayData[i2];
                    if (string != null) {
                        n14 = string.length();
                        if (n14 > plsqlIbtBindInfo.elemMaxLen - 1) {
                            n14 = plsqlIbtBindInfo.elemMaxLen - 1;
                        }
                        string.getChars(0, n14, oraclePreparedStatement.ibtBindChars, n13 + 1);
                        oraclePreparedStatement.ibtBindIndicators[plsqlIbtBindInfo.ibtIndicatorIndex + i2] = 0;
                        oraclePreparedStatement.ibtBindChars[n13] = (char)(n14 <<= 1);
                        oraclePreparedStatement.ibtBindIndicators[plsqlIbtBindInfo.ibtLengthIndex + i2] = (short)(n14 == 0 ? 3 : (short)(n14 + 2));
                    } else {
                        oraclePreparedStatement.ibtBindIndicators[plsqlIbtBindInfo.ibtIndicatorIndex + i2] = -1;
                        Arrays.fill(oraclePreparedStatement.ibtBindChars, n13, n13 + plsqlIbtBindInfo.elemMaxLen, '\u0000');
                    }
                    n13 += plsqlIbtBindInfo.elemMaxLen;
                }
                break;
            }
            case 6: 
            case 12: 
            case 180: {
                for (int i3 = 0; i3 < plsqlIbtBindInfo.curLen; ++i3) {
                    byte[] byArray2 = null;
                    if (plsqlIbtBindInfo.arrayData[i3] != null) {
                        byArray2 = ((Datum)plsqlIbtBindInfo.arrayData[i3]).getBytes();
                    }
                    if (byArray2 == null) {
                        oraclePreparedStatement.ibtBindIndicators[plsqlIbtBindInfo.ibtIndicatorIndex + i3] = -1;
                        oraclePreparedStatement.ibtBindBytes[n13] = -1;
                    } else {
                        oraclePreparedStatement.ibtBindIndicators[plsqlIbtBindInfo.ibtIndicatorIndex + i3] = 0;
                        oraclePreparedStatement.ibtBindIndicators[plsqlIbtBindInfo.ibtLengthIndex + i3] = (short)(byArray2.length + 1);
                        oraclePreparedStatement.ibtBindBytes[n13] = (byte)byArray2.length;
                        System.arraycopy(byArray2, 0, oraclePreparedStatement.ibtBindBytes, n13 + 1, byArray2.length);
                    }
                    n13 += plsqlIbtBindInfo.elemMaxLen;
                    if (!oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) continue;
                    l2 = byArray2 == null ? CRC64.updateChecksum(l2, Accessor.NULL_DATA_BYTES, 0, Accessor.NULL_DATA_BYTES.length) : CRC64.updateChecksum(l2, byArray2, 0, byArray2.length);
                }
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 97).fillInStackTrace();
            }
        }
        return l2;
    }

    @Override
    Binder copyingBinder() {
        return this.thePlsqlIbtCopyingBinder;
    }
}

