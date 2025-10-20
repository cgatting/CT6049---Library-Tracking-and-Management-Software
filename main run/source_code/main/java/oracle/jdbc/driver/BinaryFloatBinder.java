/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Arrays;
import oracle.jdbc.driver.BinaryFloatCopyingBinder;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.internal.OracleConnection;
import oracle.sql.Datum;

class BinaryFloatBinder
extends Binder {
    float paramVal;
    Binder theBinaryFloatCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)100;
        binder.bytelen = 4;
    }

    BinaryFloatBinder(float f2) {
        BinaryFloatBinder.init(this);
        this.paramVal = f2;
    }

    @Override
    Binder copyingBinder() {
        if (this.theBinaryFloatCopyingBinder == null) {
            this.theBinaryFloatCopyingBinder = new BinaryFloatCopyingBinder(this.paramVal);
        }
        return this.theBinaryFloatCopyingBinder;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        byte[] byArray2 = null;
        int n13 = 0;
        float f2 = this.paramVal;
        int n14 = 4;
        long l3 = 0L;
        if (bl2) {
            lArray[n11] = l3 = byteArray.getPosition();
            oraclePreparedStatement.lastBoundDataOffsets[n2] = l3;
            byArray2 = oraclePreparedStatement.connection.methodTempLittleByteBuffer;
            n13 = 0;
        } else {
            byArray2 = byArray;
            n13 = n7;
        }
        n14 = this.getDatumBytes(oraclePreparedStatement, f2, byArray2, n13);
        if (bl2) {
            byteArray.put(byArray2, 0, n14);
            oraclePreparedStatement.lastBoundDataLengths[n2] = n14;
            nArray[n11] = n14;
        }
        sArray[n10] = 0;
        sArray[n9] = (short)n14;
        return l2;
    }

    private int getDatumBytes(OraclePreparedStatement oraclePreparedStatement, float f2, byte[] byArray, int n2) throws SQLException {
        int n3;
        int n4 = 4;
        if ((double)f2 == 0.0) {
            f2 = 0.0f;
        } else if (f2 != f2) {
            f2 = Float.NaN;
        }
        int n5 = n3 = Float.floatToIntBits(f2);
        int n6 = n3 >>= 8;
        int n7 = n3 >>= 8;
        int n8 = n3 >>= 8;
        if ((n8 & 0x80) == 0) {
            n8 |= 0x80;
        } else {
            n8 ^= 0xFFFFFFFF;
            n7 ^= 0xFFFFFFFF;
            n6 ^= 0xFFFFFFFF;
            n5 ^= 0xFFFFFFFF;
        }
        byArray[n2 + 3] = (byte)n5;
        byArray[n2 + 2] = (byte)n6;
        byArray[n2 + 1] = (byte)n7;
        byArray[n2] = (byte)n8;
        return n4;
    }

    @Override
    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        byte[] byArray = oraclePreparedStatement.connection.methodTempLittleByteBuffer;
        int n5 = this.getDatumBytes(oraclePreparedStatement, this.paramVal, byArray, 0);
        return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, Arrays.copyOf(byArray, n5), n4, null, 0);
    }
}

