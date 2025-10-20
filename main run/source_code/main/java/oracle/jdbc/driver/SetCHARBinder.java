/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.driver.SetCHARCopyingBinder;
import oracle.jdbc.internal.OracleConnection;
import oracle.sql.Datum;

class SetCHARBinder
extends Binder {
    byte[] paramVal;
    Binder theSetCHARCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)996;
        binder.bytelen = 0;
    }

    SetCHARBinder(byte[] byArray) {
        SetCHARBinder.init(this);
        this.paramVal = byArray;
    }

    @Override
    Binder copyingBinder() {
        if (this.theSetCHARCopyingBinder == null) {
            this.theSetCHARCopyingBinder = new SetCHARCopyingBinder();
        }
        return this.theSetCHARCopyingBinder;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        byte[] byArray2 = this.paramVal;
        if (bl) {
            this.paramVal = null;
        }
        if (byArray2 == null) {
            sArray[n10] = -1;
            if (bl2) {
                lArray[n11] = -1L;
                nArray[n11] = 0;
            }
        } else {
            sArray[n10] = 0;
            int n13 = byArray2.length;
            sArray[n9] = n13 > 65532 ? -2 : (short)(n13 + 2);
            if (bl2) {
                long l3;
                lArray[n11] = l3 = byteArray.getPosition();
                oraclePreparedStatement.lastBoundDataOffsets[n2] = l3;
                nArray[n11] = n13;
                oraclePreparedStatement.lastBoundDataLengths[n2] = n13;
                byteArray.put(byArray2, 0, n13);
            } else {
                cArray[n8] = (char)n13;
                int n14 = n8 + (n13 >> 1);
                if (n13 % 2 == 1) {
                    cArray[n14 + 1] = (char)(byArray2[--n13] << 8);
                }
                while (n13 > 0) {
                    cArray[n14--] = (char)(byArray2[n13 -= 2] << 8 | byArray2[n13 + 1] & 0xFF);
                }
            }
        }
        if (oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
            l2 = sArray[n10] == -1 ? CRC64.updateChecksum(l2, Accessor.NULL_DATA_BYTES, 0, Accessor.NULL_DATA_BYTES.length) : CRC64.updateChecksum(l2, byArray2, 0, byArray2.length);
        }
        return l2;
    }

    @Override
    short updateInoutIndicatorValue(short s2) {
        return (short)(s2 | 4);
    }

    @Override
    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, this.paramVal, n4, null, 0);
    }
}

