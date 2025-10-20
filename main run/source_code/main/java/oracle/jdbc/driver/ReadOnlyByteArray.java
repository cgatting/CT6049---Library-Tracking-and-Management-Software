/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.security.MessageDigest;
import java.sql.SQLException;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CharacterSet;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class ReadOnlyByteArray
extends ByteArray {
    private ByteArray array;

    static ByteArray newReadOnlyByteArray(ByteArray byteArray) {
        if (byteArray instanceof ReadOnlyByteArray) {
            return byteArray;
        }
        return new ReadOnlyByteArray(byteArray);
    }

    private ReadOnlyByteArray() {
    }

    private ReadOnlyByteArray(ByteArray byteArray) {
        this.array = byteArray;
    }

    @Override
    long getCapacity() {
        return this.array.getCapacity();
    }

    @Override
    long length() {
        return this.array.length();
    }

    @Override
    void put(long l2, byte by) {
        assert (false) : "attempt to modify a read-only byte array";
    }

    @Override
    byte get(long l2) {
        return this.array.get(l2);
    }

    @Override
    void put(long l2, byte[] byArray, int n2, int n3) {
        assert (false) : "attempt to modify a read-only byte array";
    }

    @Override
    void get(long l2, byte[] byArray, int n2, int n3) {
        this.array.get(l2, byArray, n2, n3);
    }

    @Override
    char[] getChars(long l2, int n2, CharacterSet characterSet, int[] nArray) throws SQLException {
        return this.array.getChars(l2, n2, characterSet, nArray);
    }

    @Override
    long updateChecksum(long l2, int n2, CRC64 cRC64, long l3) {
        return this.array.updateChecksum(l2, n2, cRC64, l3);
    }

    @Override
    void updateDigest(MessageDigest messageDigest, long l2, int n2) {
        this.array.updateDigest(messageDigest, l2, n2);
    }

    @Override
    void free() {
    }

    void deepFree() {
        this.array.free();
        this.array = null;
    }
}

