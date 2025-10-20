/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.internal.OracleConnection;
import oracle.sql.Datum;

abstract class DatumBinder
extends Binder {
    byte[] paramVal;
    boolean skipBindChecksumForLobs = false;
    ByteBuffer lobData = null;

    DatumBinder(byte[] byArray) {
        this.paramVal = byArray;
    }

    DatumBinder(byte[] byArray, ByteBuffer byteBuffer) {
        this.paramVal = byArray;
        this.lobData = byteBuffer;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        byte[] byArray2 = this.paramVal;
        ByteBuffer byteBuffer = this.lobData;
        if (bl) {
            this.paramVal = null;
            this.lobData = null;
        }
        if (byArray2 == null) {
            sArray[n10] = -1;
            if (bl2) {
                lArray[n11] = -1L;
                nArray[n11] = 0;
            }
        } else {
            sArray[n10] = 0;
            int n13 = byArray2.length;
            if (bl2) {
                long l3;
                lArray[n11] = l3 = byteArray.getPosition();
                oraclePreparedStatement.lastBoundDataOffsets[n2] = l3;
                byteArray.put(byArray2, 0, n13);
                if (byteBuffer != null) {
                    int n14 = byteBuffer.remaining();
                    byteArray.put(byteBuffer.array(), 0, n14);
                    nArray[n11] = n13 + n14;
                    oraclePreparedStatement.lastBoundDataLengths[n2] = n13 + n14;
                } else {
                    nArray[n11] = n13;
                    oraclePreparedStatement.lastBoundDataLengths[n2] = n13;
                }
            } else {
                System.arraycopy(byArray2, 0, byArray, n7, byArray2.length);
            }
            sArray[n9] = (short)n13;
        }
        if (!this.skipBindChecksumForLobs && oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
            l2 = sArray[n10] == -1 ? CRC64.updateChecksum(l2, Accessor.NULL_DATA_BYTES, 0, Accessor.NULL_DATA_BYTES.length) : CRC64.updateChecksum(l2, byArray2, 0, byArray2.length);
        }
        return l2;
    }

    @Override
    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, this.paramVal, n4, null, 0);
    }
}

