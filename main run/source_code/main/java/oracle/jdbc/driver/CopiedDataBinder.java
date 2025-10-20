/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.OraclePreparedStatement;

class CopiedDataBinder
extends Binder {
    byte[] value;

    CopiedDataBinder(short s2, byte[] byArray, int n2) {
        this.type = s2;
        this.bytelen = (short)n2;
        this.value = byArray;
        this.bytelen = n2;
    }

    @Override
    Binder copyingBinder() {
        return this;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        long l3;
        sArray[n10] = 0;
        sArray[n9] = (short)this.bytelen;
        lArray[n11] = l3 = byteArray.getPosition();
        oraclePreparedStatement.lastBoundDataOffsets[n2] = l3;
        byteArray.put(this.value, 0, this.bytelen);
        nArray[n11] = this.bytelen;
        oraclePreparedStatement.lastBoundDataLengths[n2] = this.bytelen;
        if (oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
            l2 = CRC64.updateChecksum(l2, this.value, 0, this.value.length);
        }
        return l2;
    }
}

