/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Arrays;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.internal.OracleConnection;
import oracle.sql.Datum;

class PlsqlBooleanBinder
extends Binder {
    int paramVal;
    Binder thePlsqlBooleanCopyingBinder = null;

    PlsqlBooleanBinder(int n2) {
        PlsqlBooleanBinder.init(this);
        this.paramVal = n2;
    }

    static void init(Binder binder) {
        binder.type = (short)252;
        binder.bytelen = 4;
    }

    @Override
    Binder copyingBinder() {
        if (this.thePlsqlBooleanCopyingBinder == null) {
            this.thePlsqlBooleanCopyingBinder = new PlsqlBooleanBinder(this.paramVal);
        }
        return this.thePlsqlBooleanCopyingBinder;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        byte[] byArray2 = byArray;
        int n13 = n7 + 1;
        int n14 = this.paramVal;
        int n15 = 0;
        long l3 = 0L;
        if (bl2) {
            lArray[n11] = l3 = byteArray.getPosition();
            oraclePreparedStatement.lastBoundDataOffsets[n2] = l3;
            lArray[n11] = byteArray.getPosition();
            byArray2 = oraclePreparedStatement.connection.methodTempLittleByteBuffer;
            n13 = 0;
        } else {
            byArray2 = byArray;
            n13 = n7 + 1;
        }
        n15 = this.getDatumBytes(oraclePreparedStatement, n14, byArray2, n13);
        sArray[n10] = 0;
        sArray[n9] = (short)(n15 + 1);
        byteArray.put(byArray2, 0, n15);
        nArray[n11] = n15;
        return l2;
    }

    private int getDatumBytes(OraclePreparedStatement oraclePreparedStatement, int n2, byte[] byArray, int n3) throws SQLException {
        int n4;
        if (n2 != 0) {
            byArray[n3] = 1;
            byArray[n3 + 1] = 0;
            byArray[n3 + 2] = 0;
            byArray[n3 + 3] = 0;
            n4 = 4;
        } else {
            byArray[n3] = 0;
            byArray[n3 + 1] = 0;
            byArray[n3 + 2] = 0;
            byArray[n3 + 3] = 0;
            n4 = 4;
        }
        return n4;
    }

    @Override
    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        byte[] byArray = oraclePreparedStatement.connection.methodTempLittleByteBuffer;
        int n5 = this.getDatumBytes(oraclePreparedStatement, this.paramVal, byArray, 0);
        return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, Arrays.copyOf(byArray, n5), n4, null, 0);
    }
}

