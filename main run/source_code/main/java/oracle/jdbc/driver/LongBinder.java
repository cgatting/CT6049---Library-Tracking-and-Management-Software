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

class LongBinder
extends VarnumBinder {
    long paramVal;

    LongBinder(long l2) {
        this.paramVal = l2;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        byte[] byArray2 = null;
        int n13 = 0;
        long l3 = this.paramVal;
        long l4 = 0L;
        if (bl2) {
            lArray[n11] = l4 = byteArray.getPosition();
            oraclePreparedStatement.lastBoundDataOffsets[n2] = l4;
            byArray2 = oraclePreparedStatement.connection.methodTempLittleByteBuffer;
            n13 = 0;
        } else {
            byArray2 = byArray;
            n13 = n7 + 1;
        }
        int n14 = LongBinder.setLongInternal(byArray2, n13, l3);
        if (bl2) {
            byteArray.put(byArray2, 0, n14);
            sArray[n10] = 0;
            nArray[n11] = n14;
            oraclePreparedStatement.lastBoundDataLengths[n2] = n14;
        } else {
            byArray2[n7] = (byte)n14;
            sArray[n10] = 0;
        }
        sArray[n9] = (short)(n14 + 1);
        return l2;
    }

    @Override
    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, NUMBER.toBytes(this.paramVal), n4, null, 0);
    }
}

