/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.SetCHARBinder;

class LittleEndianSetCHARBinder
extends SetCHARBinder {
    LittleEndianSetCHARBinder(byte[] byArray) {
        super(byArray);
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
            cArray[n8] = (char)n13;
            sArray[n9] = n13 > 65532 ? -2 : (short)(n13 + 2);
            int n14 = n8 + (n13 >> 1);
            if (n13 % 2 == 1) {
                cArray[n14 + 1] = (char)(byArray2[--n13] & 0xFF);
            }
            while (n13 > 0) {
                cArray[n14--] = (char)(byArray2[n13 -= 2] & 0xFF | byArray2[n13 + 1] << 8);
            }
        }
        if (oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
            l2 = sArray[n10] == -1 ? CRC64.updateChecksum(l2, Accessor.NULL_DATA_BYTES, 0, Accessor.NULL_DATA_BYTES.length) : CRC64.updateChecksum(l2, byArray2, 0, byArray2.length);
        }
        return l2;
    }
}

