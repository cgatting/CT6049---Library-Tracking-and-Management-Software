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

abstract class ByteCopyingBinder
extends Binder {
    ByteCopyingBinder() {
    }

    @Override
    Binder copyingBinder() {
        return this;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        if (bl2) {
            return OraclePreparedStatement.theStaticDBACopyingBinder.bind(oraclePreparedStatement, n2, n3, n4, byArray, cArray, sArray, n5, n6, n7, n8, n9, n10, bl, l2, byteArray, lArray, nArray, n11, bl2, n12);
        }
        byte[] byArray2 = null;
        int n13 = 0;
        int n14 = oraclePreparedStatement.lastBoundByteLens[n2];
        if (n3 == 0) {
            byArray2 = oraclePreparedStatement.lastBoundBytes;
            n13 = oraclePreparedStatement.lastBoundByteOffsets[n2];
            sArray[n10] = oraclePreparedStatement.lastBoundInds[n2];
            sArray[n9] = oraclePreparedStatement.lastBoundLens[n2];
            if (byArray2 == byArray && n13 == n7) {
                if (oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
                    if (sArray[n10] == -1) {
                        l2 = CRC64.updateChecksum(l2, Accessor.NULL_DATA_BYTES, 0, Accessor.NULL_DATA_BYTES.length);
                    } else {
                        n14 = oraclePreparedStatement.lastBoundByteLens[n2];
                        l2 = CRC64.updateChecksum(l2, byArray2, n13, n14);
                    }
                }
                return l2;
            }
            n14 = oraclePreparedStatement.lastBoundByteLens[n2];
            if (n14 > n5) {
                n14 = n5;
            }
        } else {
            byArray2 = byArray;
            n13 = n7 - n5;
            sArray[n10] = sArray[n10 - 1];
            sArray[n9] = sArray[n9 - 1];
            n14 = n5;
        }
        System.arraycopy(byArray2, n13, byArray, n7, n14);
        if (oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
            l2 = sArray[n10] == -1 ? CRC64.updateChecksum(l2, Accessor.NULL_DATA_BYTES, 0, Accessor.NULL_DATA_BYTES.length) : CRC64.updateChecksum(l2, byArray2, n13, n14);
        }
        return l2;
    }
}

