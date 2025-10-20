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

abstract class CharCopyingBinder
extends Binder {
    CharCopyingBinder() {
    }

    @Override
    Binder copyingBinder() {
        return this;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        int n13;
        int n14;
        char[] cArray2;
        if (bl2) {
            return OraclePreparedStatement.theStaticDBACopyingBinder.bind(oraclePreparedStatement, n2, n3, n4, byArray, cArray, sArray, n5, n6, n7, n8, n9, n10, bl, l2, byteArray, lArray, nArray, n11, bl2, n12);
        }
        if (n3 == 0) {
            cArray2 = oraclePreparedStatement.lastBoundChars;
            n14 = oraclePreparedStatement.lastBoundCharOffsets[n2];
            sArray[n10] = oraclePreparedStatement.lastBoundInds[n2];
            sArray[n9] = oraclePreparedStatement.lastBoundLens[n2];
            if (cArray2 == cArray && n14 == n8) {
                if (oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
                    if (sArray[n10] == -1) {
                        l2 = CRC64.updateChecksum(l2, Accessor.NULL_DATA_BYTES, 0, Accessor.NULL_DATA_BYTES.length);
                    } else {
                        int n15 = oraclePreparedStatement.lastBoundCharLens[n2];
                        l2 = CRC64.updateChecksum(l2, cArray2, n14, n15);
                    }
                }
                return l2;
            }
            n13 = oraclePreparedStatement.lastBoundCharLens[n2];
            if (n13 > n6) {
                n13 = n6;
            }
        } else {
            cArray2 = cArray;
            n14 = n8 - n6;
            sArray[n10] = sArray[n10 - 1];
            sArray[n9] = sArray[n9 - 1];
            n13 = n6;
        }
        System.arraycopy(cArray2, n14, cArray, n8, n13);
        if (oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
            l2 = sArray[n10] == -1 ? CRC64.updateChecksum(l2, Accessor.NULL_DATA_BYTES, 0, Accessor.NULL_DATA_BYTES.length) : CRC64.updateChecksum(l2, cArray2, n14, n13);
        }
        return l2;
    }
}

