/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.BinaryFloatBinder;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.OraclePreparedStatement;

class BinaryFloatNullBinder
extends BinaryFloatBinder {
    BinaryFloatNullBinder() {
        super(0.0f);
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        sArray[n10] = -1;
        if (bl2) {
            lArray[n11] = -1L;
            nArray[n11] = 0;
        }
        return l2;
    }

    @Override
    Binder copyingBinder() {
        return this;
    }
}

