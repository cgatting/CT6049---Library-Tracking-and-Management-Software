/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Arrays;
import oracle.jdbc.driver.BinaryDoubleCopyingBinder;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.internal.OracleConnection;
import oracle.sql.Datum;

class BinaryDoubleBinder
extends Binder {
    double paramVal;
    Binder theBinaryDoubleCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)101;
        binder.bytelen = 8;
    }

    BinaryDoubleBinder(double d2) {
        BinaryDoubleBinder.init(this);
        this.paramVal = d2;
    }

    @Override
    Binder copyingBinder() {
        if (this.theBinaryDoubleCopyingBinder == null) {
            this.theBinaryDoubleCopyingBinder = new BinaryDoubleCopyingBinder(this.paramVal);
        }
        return this.theBinaryDoubleCopyingBinder;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        int n13;
        byte[] byArray2 = null;
        int n14 = 8;
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
        double d2 = this.paramVal;
        n14 = this.getDatumBytes(oraclePreparedStatement, d2, byArray2, n13);
        if (bl2) {
            byteArray.put(byArray2, 0, n14);
            nArray[n11] = n14;
            oraclePreparedStatement.lastBoundDataLengths[n2] = n14;
        }
        sArray[n10] = 0;
        sArray[n9] = (short)n14;
        return l2;
    }

    private int getDatumBytes(OraclePreparedStatement oraclePreparedStatement, double d2, byte[] byArray, int n2) throws SQLException {
        int n3 = 8;
        if (d2 == 0.0) {
            d2 = 0.0;
        } else if (d2 != d2) {
            d2 = Double.NaN;
        }
        long l2 = Double.doubleToLongBits(d2);
        int n4 = (int)l2;
        int n5 = (int)(l2 >> 32);
        int n6 = n4;
        int n7 = n4 >>= 8;
        int n8 = n4 >>= 8;
        int n9 = n4 >>= 8;
        int n10 = n5;
        int n11 = n5 >>= 8;
        int n12 = n5 >>= 8;
        int n13 = n5 >>= 8;
        if ((n13 & 0x80) == 0) {
            n13 |= 0x80;
        } else {
            n13 ^= 0xFFFFFFFF;
            n12 ^= 0xFFFFFFFF;
            n11 ^= 0xFFFFFFFF;
            n10 ^= 0xFFFFFFFF;
            n9 ^= 0xFFFFFFFF;
            n8 ^= 0xFFFFFFFF;
            n7 ^= 0xFFFFFFFF;
            n6 ^= 0xFFFFFFFF;
        }
        byArray[n2 + 7] = (byte)n6;
        byArray[n2 + 6] = (byte)n7;
        byArray[n2 + 5] = (byte)n8;
        byArray[n2 + 4] = (byte)n9;
        byArray[n2 + 3] = (byte)n10;
        byArray[n2 + 2] = (byte)n11;
        byArray[n2 + 1] = (byte)n12;
        byArray[n2] = (byte)n13;
        return n3;
    }

    @Override
    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        byte[] byArray = oraclePreparedStatement.connection.methodTempLittleByteBuffer;
        int n5 = this.getDatumBytes(oraclePreparedStatement, this.paramVal, byArray, 0);
        return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, Arrays.copyOf(byArray, n5), n4, null, 0);
    }
}

