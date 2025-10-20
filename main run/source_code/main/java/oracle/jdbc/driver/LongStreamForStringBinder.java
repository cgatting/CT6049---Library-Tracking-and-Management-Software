/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OraclePreparedStatementReadOnly;

class LongStreamForStringBinder
extends Binder {
    LongStreamForStringBinder() {
        this.type = (short)8;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        sArray[n10] = 0;
        oraclePreparedStatement.lastBoundDataOffsets[n2] = -1L;
        oraclePreparedStatement.lastBoundDataLengths[n2] = 0;
        return l2;
    }

    @Override
    Binder copyingBinder() {
        return OraclePreparedStatementReadOnly.theStaticLongStreamForStringCopyingBinder;
    }
}

