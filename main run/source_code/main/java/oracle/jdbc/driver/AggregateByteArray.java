/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.security.MessageDigest;
import java.sql.SQLException;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.SimpleByteArray;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CharacterSet;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class AggregateByteArray
extends SimpleByteArray {
    protected ByteArray extension;

    AggregateByteArray(byte[] byArray, ByteArray byteArray) {
        super(byArray);
        this.extension = byteArray;
    }

    @Override
    long length() {
        return (long)this.bytes.length + this.extension.length();
    }

    @Override
    void put(long l2, byte by) {
        if (l2 < (long)this.bytes.length) {
            super.put(l2, by);
        } else {
            this.extension.put(l2 - (long)this.bytes.length, by);
        }
    }

    @Override
    byte get(long l2) {
        if (l2 < (long)this.bytes.length) {
            return super.get(l2);
        }
        return this.extension.get(l2 - (long)this.bytes.length);
    }

    @Override
    void put(long l2, byte[] byArray, int n2, int n3) {
        assert (l2 < (long)this.bytes.length == l2 + (long)n3 <= (long)this.bytes.length) : "offset:" + l2 + " length:" + n3 + " bytes.length:" + this.bytes.length;
        if (l2 < (long)this.bytes.length) {
            super.put(l2, byArray, n2, n3);
        } else {
            this.extension.put(l2 - (long)this.bytes.length, byArray, n2, n3);
        }
    }

    @Override
    void get(long l2, byte[] byArray, int n2, int n3) {
        assert (l2 < (long)this.bytes.length == l2 + (long)n3 <= (long)this.bytes.length) : "offset:" + l2 + " length:" + n3 + " bytes.length:" + this.bytes.length;
        if (l2 < (long)this.bytes.length) {
            super.get(l2, byArray, n2, n3);
        } else {
            this.extension.get(l2 - (long)this.bytes.length, byArray, n2, n3);
        }
    }

    @Override
    char[] getChars(long l2, int n2, CharacterSet characterSet, int[] nArray) throws SQLException {
        assert (l2 < (long)this.bytes.length == l2 + (long)n2 <= (long)this.bytes.length) : "offset:" + l2 + " lengthInBytes:" + n2 + " bytes.length:" + this.bytes.length;
        if (l2 < (long)this.bytes.length) {
            return super.getChars(l2, n2, characterSet, nArray);
        }
        return this.extension.getChars(l2 - (long)this.bytes.length, n2, characterSet, nArray);
    }

    @Override
    long updateChecksum(long l2, int n2, CRC64 cRC64, long l3) {
        assert (l2 < (long)this.bytes.length == l2 + (long)n2 <= (long)this.bytes.length) : "offset:" + l2 + " length:" + n2 + " bytes.length:" + this.bytes.length;
        if (l2 < (long)this.bytes.length) {
            return super.updateChecksum(l2, n2, cRC64, l3);
        }
        return this.extension.updateChecksum(l2 - (long)this.bytes.length, n2, cRC64, l3);
    }

    @Override
    void updateDigest(MessageDigest messageDigest, long l2, int n2) {
        super.updateDigest(messageDigest, l2, n2);
    }

    @Override
    byte[] getBlockBasic(long l2, int[] nArray) {
        if (l2 < (long)this.bytes.length) {
            return super.getBlockBasic(l2, nArray);
        }
        return this.extension.getBlockBasic(l2, nArray);
    }

    @Override
    void free() {
        super.free();
        this.extension.free();
    }
}

