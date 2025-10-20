/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.lang.reflect.Executable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.driver.AggregateByteArray;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.ClioSupport;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Log;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CharacterSet;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.OCI_INTERNAL})
class T2CCharByteArray
extends AggregateByteArray {
    char[] charArray;
    DBConversion conversion;

    T2CCharByteArray(char[] cArray, ByteArray byteArray) {
        super(PhysicalConnection.EMPTY_BYTE_ARRAY, byteArray);
        this.charArray = cArray;
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    long length() {
        return (long)this.charArray.length + this.extension.length();
    }

    void setChars(char[] cArray) {
        this.charArray = cArray;
    }

    void setDBConversion(DBConversion dBConversion) {
        this.conversion = dBConversion;
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    char[] getChars(long l2, int n2, CharacterSet characterSet, int[] nArray) throws SQLException {
        if (l2 < (long)this.charArray.length) {
            char[] cArray = new char[n2];
            System.arraycopy(this.charArray, (int)l2, cArray, 0, n2);
            nArray[0] = n2;
            return cArray;
        }
        return this.extension.getChars(l2 - (long)this.charArray.length, n2, characterSet, nArray);
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    void get(long l2, byte[] byArray, int n2, int n3) {
        if (l2 < (long)this.charArray.length) {
            try {
                int n4 = this.conversion.javaCharsToCHARBytes(this.charArray, (int)l2, byArray, n2, n3);
            }
            catch (SQLException sQLException) {
            }
        } else {
            this.extension.get(l2 - (long)this.charArray.length, byArray, n2, n3);
        }
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    byte get(long l2) {
        if (l2 < (long)this.charArray.length) {
            return (byte)(this.charArray[(int)l2] & 0xFF);
        }
        return this.extension.get(l2 - (long)this.charArray.length);
    }

    @Log
    protected void debug(Logger logger, Level level, Executable executable, String string) {
        ClioSupport.log(logger, level, this.getClass(), executable, string);
    }
}

