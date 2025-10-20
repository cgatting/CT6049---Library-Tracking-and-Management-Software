/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.LongStreamForStringBinder;
import oracle.jdbc.driver.OraclePreparedStatement;

class LongStreamForStringCopyingBinder
extends LongStreamForStringBinder {
    LongStreamForStringCopyingBinder() {
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        l2 = super.bind(oraclePreparedStatement, n2, n3, n4, byArray, cArray, sArray, n5, n6, n7, n8, n9, n10, bl, l2, byteArray, lArray, nArray, n11, bl2, n12);
        oraclePreparedStatement.parameterStream[n4][n2] = n3 == 0 ? oraclePreparedStatement.lastBoundStream[n2] : oraclePreparedStatement.parameterStream[n4 - 1][n2];
        return l2;
    }
}

