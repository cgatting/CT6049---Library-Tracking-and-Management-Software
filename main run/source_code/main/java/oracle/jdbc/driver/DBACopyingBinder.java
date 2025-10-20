/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.PhysicalConnection;

class DBACopyingBinder
extends Binder {
    DBACopyingBinder() {
    }

    @Override
    Binder copyingBinder() {
        return this;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        if (n3 == 0) {
            sArray[n10] = oraclePreparedStatement.lastBoundInds[n2];
            sArray[n9] = oraclePreparedStatement.lastBoundLens[n2];
            nArray[n11] = oraclePreparedStatement.lastBoundDataLengths[n2];
            lArray[n11] = oraclePreparedStatement.lastBoundDataOffsets[n2];
        } else {
            sArray[n10] = sArray[n10 - 1];
            sArray[n9] = sArray[n9 - 1];
            nArray[n11] = nArray[n11 - oraclePreparedStatement.numberOfBindPositions];
            lArray[n11] = lArray[n11 - oraclePreparedStatement.numberOfBindPositions];
        }
        if (oraclePreparedStatement.connection.checksumMode.needToCalculateBindChecksum()) {
            l2 = sArray[n10] == -1 ? CRC64.updateChecksum(l2, Accessor.NULL_DATA_BYTES, 0, Accessor.NULL_DATA_BYTES.length) : byteArray.updateChecksum(lArray[n11], nArray[n11], PhysicalConnection.CHECKSUM, l2);
        }
        return l2;
    }

    @Override
    void lastBoundValueCleanup(OraclePreparedStatement oraclePreparedStatement, int n2) {
        if (oraclePreparedStatement.lastBoundBlobs != null) {
            oraclePreparedStatement.moveTempLobsToFree(oraclePreparedStatement.lastBoundBlobs[n2]);
        }
        if (oraclePreparedStatement.lastBoundClobs != null) {
            oraclePreparedStatement.moveTempLobsToFree(oraclePreparedStatement.lastBoundClobs[n2]);
        }
    }
}

