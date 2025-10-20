/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.driver.VarnumBinder;
import oracle.jdbc.internal.OracleConnection;
import oracle.sql.Datum;
import oracle.sql.NUMBER;

class IntBinder
extends VarnumBinder {
    int paramVal;

    IntBinder(int n2) {
        this.paramVal = n2;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        byte[] byArray2 = null;
        int n13 = 0;
        int n14 = this.paramVal;
        int n15 = 0;
        long l3 = 0L;
        if (bl2) {
            lArray[n11] = l3 = byteArray.getPosition();
            oraclePreparedStatement.lastBoundDataOffsets[n2] = l3;
            byArray2 = oraclePreparedStatement.connection.methodTempLittleByteBuffer;
            n13 = 0;
        } else {
            byArray2 = byArray;
            n13 = n7 + 1;
        }
        n15 = NUMBER.toBytes(n14, byArray2, n13);
        sArray[n10] = 0;
        sArray[n9] = (short)(n15 + 1);
        if (bl2) {
            byteArray.put(byArray2, 0, n15);
            nArray[n11] = n15;
            oraclePreparedStatement.lastBoundDataLengths[n2] = n15;
        } else {
            byArray2[n7] = (byte)n15;
        }
        return l2;
    }

    @Override
    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, NUMBER.toBytes(this.paramVal), n4, null, 0);
    }
}

