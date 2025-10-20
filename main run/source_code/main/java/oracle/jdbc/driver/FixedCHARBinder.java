/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.FixedCHARCopyingBinder;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.internal.OracleConnection;
import oracle.sql.CharacterSet;
import oracle.sql.Datum;

class FixedCHARBinder
extends Binder {
    String paramVal;
    Binder theFixedCHARCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)96;
        binder.bytelen = 0;
    }

    FixedCHARBinder(String string) {
        FixedCHARBinder.init(this);
        this.paramVal = string;
    }

    @Override
    long bind(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4, byte[] byArray, char[] cArray, short[] sArray, int n5, int n6, int n7, int n8, int n9, int n10, boolean bl, long l2, ByteArray byteArray, long[] lArray, int[] nArray, int n11, boolean bl2, int n12) throws SQLException {
        String string = this.paramVal;
        if (bl) {
            this.paramVal = null;
        }
        if (string == null) {
            sArray[n10] = -1;
            if (bl2) {
                lArray[n11] = -1L;
                nArray[n11] = 0;
            }
        } else {
            sArray[n10] = 0;
            int n13 = string.length();
            if (bl2) {
                long l3;
                lArray[n11] = l3 = byteArray.getPosition();
                oraclePreparedStatement.lastBoundDataOffsets[n2] = l3;
                CharacterSet characterSet = oraclePreparedStatement.getCharacterSetForBind(n2, (short)n12);
                int n14 = characterSet.getOracleId() == 1 && !oraclePreparedStatement.connection.isStrictAsciiConversion ? byteArray.putAsciiString(string) : byteArray.putStringWithReplacement(string, characterSet);
                nArray[n11] = n14;
                oraclePreparedStatement.lastBoundDataLengths[n2] = n14;
            } else {
                string.getChars(0, n13, cArray, n8);
            }
            if ((n13 <<= 1) > 65534) {
                n13 = 65534;
            }
            sArray[n9] = (short)n13;
        }
        return l2;
    }

    @Override
    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        String string = this.paramVal;
        CharacterSet characterSet = oraclePreparedStatement.getCharacterSetForBind(n2, (short)n3);
        if (characterSet.getOracleId() == 1 && !oraclePreparedStatement.connection.isStrictAsciiConversion) {
            String string2 = string;
            if (string2 == null || string2.length() == 0) {
                return null;
            }
            int n5 = string2.length();
            byte[] byArray = new byte[n5];
            for (int i2 = 0; i2 < n5; ++i2) {
                byArray[i2] = (byte)string2.charAt(i2);
            }
            return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, byArray, n4, null, 0);
        }
        return SQLUtil.makeDatum((OracleConnection)oraclePreparedStatement.connection, characterSet.convertWithReplacement(string), n4, null, 0);
    }

    @Override
    Binder copyingBinder() {
        if (this.theFixedCHARCopyingBinder == null) {
            this.theFixedCHARCopyingBinder = new FixedCHARCopyingBinder(this.paramVal);
        }
        return this.theFixedCHARCopyingBinder;
    }
}

