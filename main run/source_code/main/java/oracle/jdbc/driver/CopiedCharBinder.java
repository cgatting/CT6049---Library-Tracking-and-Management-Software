/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.OraclePreparedStatement;

class CopiedCharBinder
extends Binder {
    char[] value;
    short len;
    short inoutIndicatorValue;

    CopiedCharBinder(short s2, char[] cArray, short s3, short s4) {
        this.type = s2;
        this.bytelen = 0;
        this.value = cArray;
        this.len = s3;
        this.inoutIndicatorValue = s4;
    }

    @Override
    Binder copyingBinder() {
        return this;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        sArray[n10] = 0;
        sArray[n9] = this.len;
        System.arraycopy(this.value, 0, cArray, n8, this.value.length);
        if (oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
            l2 = CRC64.updateChecksum(l2, this.value, 0, this.value.length);
        }
        return l2;
    }

    @Override
    short updateInoutIndicatorValue(short s2) {
        return this.inoutIndicatorValue;
    }
}

