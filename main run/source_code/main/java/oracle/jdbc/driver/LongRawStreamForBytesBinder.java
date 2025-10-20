/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OraclePreparedStatementReadOnly;

class LongRawStreamForBytesBinder
extends Binder {
    LongRawStreamForBytesBinder() {
        this.type = (short)24;
        this.bytelen = 0;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        sArray[n10] = 0;
        return l2;
    }

    @Override
    Binder copyingBinder() {
        return OraclePreparedStatementReadOnly.theStaticLongRawStreamForBytesCopyingBinder;
    }
}

