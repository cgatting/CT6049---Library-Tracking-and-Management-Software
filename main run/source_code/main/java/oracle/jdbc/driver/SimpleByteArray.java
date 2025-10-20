/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.security.MessageDigest;
import java.sql.SQLException;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CharacterSet;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class SimpleByteArray
extends ByteArray {
    protected byte[] bytes;

    protected SimpleByteArray(byte[] byArray) {
        this.setBytes(byArray);
    }

    void setBytes(byte[] byArray) {
        this.bytes = byArray;
    }

    @Override
    long length() {
        return this.bytes.length;
    }

    @Override
    void put(long l2, byte by) {
        assert (l2 >= 0L && l2 < (long)this.bytes.length) : "index = " + l2 + " length = " + this.bytes.length;
        this.bytes[(int)l2] = by;
    }

    @Override
    byte get(long l2) {
        assert (l2 >= 0L && l2 < (long)this.bytes.length) : "index = " + l2 + " length = " + this.bytes.length;
        return this.bytes[(int)l2];
    }

    @Override
    void put(long l2, byte[] byArray, int n2, int n3) {
        assert (l2 >= 0L && l2 + (long)n3 < (long)this.bytes.length) : "offset = " + l2 + " length = " + n3 + " bytes.length = " + this.bytes.length;
        assert (n2 >= 0 && n2 + n3 < byArray.length) : "srcOffset = " + n2 + " length = " + n3 + " src.length = " + byArray.length;
        System.arraycopy(byArray, n2, this.bytes, (int)l2, n3);
    }

    @Override
    void get(long l2, byte[] byArray, int n2, int n3) {
        assert (l2 >= 0L && l2 + (long)n3 <= (long)this.bytes.length && n2 >= 0 && n2 + n3 <= byArray.length) : " offset: " + l2 + " bytes.length: " + this.bytes.length + " destOffset: " + n2 + " length: " + n3;
        System.arraycopy(this.bytes, (int)l2, byArray, n2, n3);
    }

    char[] getChars(long l2, int n2, DBConversion dBConversion, int n3, int[] nArray) throws SQLException {
        int n4;
        assert (l2 >= 0L && n2 >= 0 && (long)this.bytes.length >= l2 + (long)n2) : "bytes.length: " + this.bytes.length + " offset: " + l2 + " lengthInBytes: " + n2;
        assert (dBConversion != null) : "conversion is null";
        assert (nArray != null && nArray.length >= 1) : "out_lengthInChars: " + nArray;
        boolean bl = n3 == 2;
        char[] cArray = new char[n2 * dBConversion.cMaxCharSize];
        int[] nArray2 = new int[]{n2};
        nArray[0] = n4 = dBConversion.CHARBytesToJavaChars(this.bytes, (int)l2, cArray, 0, nArray2, cArray.length, bl);
        return cArray;
    }

    @Override
    char[] getChars(long l2, int n2, CharacterSet characterSet, int[] nArray) throws SQLException {
        assert (l2 >= 0L && n2 >= 0) : "offset: " + l2 + " lengthInBytes: " + n2;
        assert ((long)this.bytes.length >= l2 + (long)n2) : "bytes.length: " + this.bytes.length + " offset: " + l2 + " lengthInBytes: " + n2;
        assert (nArray != null && nArray.length > 0) : "out_lengthInChars: " + nArray;
        String string = characterSet.toString(this.bytes, (int)l2, n2);
        char[] cArray = string.toCharArray();
        nArray[0] = cArray.length;
        return cArray;
    }

    @Override
    long updateChecksum(long l2, int n2, CRC64 cRC64, long l3) {
        return CRC64.updateChecksum(l3, this.bytes, (int)l2, n2);
    }

    @Override
    void updateDigest(MessageDigest messageDigest, long l2, int n2) {
        assert (l2 >= 0L && l2 + (long)n2 <= (long)this.bytes.length) : " valOffset: " + l2 + " bytes.length: " + this.bytes.length + " valLen: " + n2;
        messageDigest.update(this.bytes, (int)l2, n2);
    }

    @Override
    byte[] getBlockBasic(long l2, int[] nArray) {
        nArray[0] = (int)l2;
        return l2 < (long)this.bytes.length ? this.bytes : null;
    }

    @Override
    void free() {
    }

    @Override
    long getCapacity() {
        return this.bytes.length;
    }
}

