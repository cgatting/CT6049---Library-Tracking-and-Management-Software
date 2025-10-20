/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Stack;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CharacterSet;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class ByteArray {
    protected long position = 0L;
    private Stack<Long> positions = new Stack();

    ByteArray() {
    }

    abstract long length();

    abstract long getCapacity();

    final long getPosition() {
        return this.position;
    }

    final void setPosition(long l2) {
        assert (l2 >= 0L) : "index: " + l2;
        assert (this.positions.isEmpty()) : "positions: " + this.positions.size();
        this.position = l2;
    }

    final void pushPosition(long l2) {
        assert (l2 >= 0L) : "newPosition: " + l2;
        this.positions.push(this.position);
        this.position = l2;
    }

    final long popPosition() {
        assert (!this.positions.isEmpty()) : "positions: " + this.positions.size();
        this.position = this.positions.pop();
        return this.position;
    }

    final void reset() {
        this.setPosition(0L);
    }

    final byte get() {
        assert (this.position < this.length()) : "position: " + this.position + " length: " + this.length();
        return this.get(this.position++);
    }

    final byte[] getBytes(int n2) {
        assert (n2 >= 0 && this.length() >= this.getPosition() + (long)n2) : "this.length: " + this.length() + " position: " + this.position + " length: " + n2;
        byte[] byArray = this.get(this.position, n2);
        this.position += (long)n2;
        return byArray;
    }

    final void getBytes(byte[] byArray, int n2, int n3) {
        assert (byArray != null) : "dest is null";
        assert (n2 >= 0 && n3 >= 0 && byArray.length >= n2 + n3) : "destOffset: " + n2 + " length: " + n3 + " dest.length: " + byArray.length;
        assert (this.length() >= this.position + (long)n3) : " this.length: " + this.length() + " this.position: " + this.position + " length: " + n3;
        this.get(this.position, byArray, n2, n3);
        this.position += (long)n3;
    }

    final int getShort() {
        assert (this.position + 1L < this.length()) : "position: " + this.position + " length: " + this.length();
        return (this.get() & 0xFF) << 8 | this.get() & 0xFF;
    }

    final int getInt() {
        assert (this.position + 3L < this.length()) : "position: " + this.position + " length: " + this.length();
        return (this.get() & 0xFF) << 24 | (this.get() & 0xFF) << 16 | (this.get() & 0xFF) << 8 | this.get() & 0xFF;
    }

    final String getString(int n2, CharacterSet characterSet) throws SQLException {
        String string = this.getString(this.position, n2, characterSet);
        this.position += (long)n2;
        return string;
    }

    final int putString(String string, CharacterSet characterSet) throws SQLException {
        int n2 = this.putString(this.position, string, characterSet);
        this.position += (long)n2;
        return n2;
    }

    final int putStringWithReplacement(String string, CharacterSet characterSet) throws SQLException {
        int n2 = this.putStringWithReplacement(this.position, string, characterSet);
        this.position += (long)n2;
        return n2;
    }

    final int putAsciiString(String string) {
        int n2 = this.putAsciiString(this.position, string);
        this.position += (long)n2;
        return n2;
    }

    final void put(byte by) {
        this.put(this.position++, by);
    }

    final void putShort(short s2) {
        this.putShort(this.position, s2);
        this.position += 2L;
    }

    final void putInt(int n2) {
        this.putInt(this.position, n2);
        this.position += 4L;
    }

    final void put(byte[] byArray) {
        this.put(this.position, byArray);
        this.position += (long)byArray.length;
    }

    final void put(byte[] byArray, int n2, int n3) {
        assert (byArray != null) : "src is null";
        assert (n2 >= 0 && n3 >= 0) : "srcOffset: " + n2 + " length: " + n3;
        assert (byArray.length >= n2 + n3) : "src.length: " + byArray.length + " srcOffset: " + n2 + " length: " + n3;
        this.put(this.position, byArray, n2, n3);
        this.position += (long)n3;
    }

    abstract void put(long var1, byte var3);

    abstract byte get(long var1);

    final void putShort(long l2, short s2) {
        this.put(l2, (byte)(s2 >> 8 & 0xFF));
        this.put(l2 + 1L, (byte)(s2 & 0xFF));
    }

    final void putInt(long l2, int n2) {
        for (int i2 = 3; i2 >= 0; --i2) {
            this.put(l2 + (long)i2, (byte)(n2 & 0xFF));
            n2 >>= 8;
        }
    }

    final void put(long l2, byte[] byArray) {
        assert (l2 >= 0L && byArray != null) : "offset: " + l2 + " src: " + byArray;
        this.put(l2, byArray, 0, byArray.length);
    }

    abstract void put(long var1, byte[] var3, int var4, int var5);

    void put(long l2, ByteArray byteArray, long l3, int n2) {
        assert (l2 >= 0L && byteArray != null && l3 >= 0L) : "offset: " + l2 + " src: " + byteArray + " srcOffset: " + l3;
        byte[] byArray = byteArray.get(l3, n2);
        this.put(l2, byArray, 0, n2);
    }

    final byte[] get(long l2, int n2) {
        assert (l2 >= 0L && n2 >= 0 && this.length() >= l2 + (long)n2) : "this.length: " + this.length() + " offset: " + l2 + " length: " + n2;
        byte[] byArray = new byte[n2];
        this.get(l2, byArray, 0, n2);
        return byArray;
    }

    abstract void get(long var1, byte[] var3, int var4, int var5);

    String getString(long l2, int n2, CharacterSet characterSet) throws SQLException {
        int[] nArray = new int[1];
        char[] cArray = this.getChars(l2, n2, characterSet, nArray);
        if (nArray[0] == cArray.length) {
            return new String(cArray);
        }
        return String.valueOf(cArray, 0, nArray[0]);
    }

    int putString(long l2, String string, CharacterSet characterSet) throws SQLException {
        if (string == null || string.length() == 0) {
            return 0;
        }
        byte[] byArray = characterSet.convert(string);
        this.put(l2, byArray);
        return byArray.length;
    }

    int putStringWithReplacement(long l2, String string, CharacterSet characterSet) throws SQLException {
        if (string == null || string.length() == 0) {
            return 0;
        }
        byte[] byArray = characterSet.convertWithReplacement(string);
        this.put(l2, byArray);
        return byArray.length;
    }

    int putAsciiString(long l2, String string) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        int n2 = string.length();
        byte[] byArray = new byte[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            byArray[i2] = (byte)string.charAt(i2);
        }
        this.put(l2, byArray);
        return n2;
    }

    char[] getChars(long l2, int n2, CharacterSet characterSet) throws SQLException {
        int[] nArray = new int[1];
        char[] cArray = this.getChars(l2, n2, characterSet, nArray);
        if (nArray[0] == cArray.length) {
            return cArray;
        }
        char[] cArray2 = new char[nArray[0]];
        System.arraycopy(cArray, 0, cArray2, 0, cArray2.length);
        return cArray2;
    }

    abstract char[] getChars(long var1, int var3, CharacterSet var4, int[] var5) throws SQLException;

    boolean equalBytes(long l2, int n2, ByteArray byteArray, long l3) throws SQLException {
        if (l2 + (long)n2 >= this.length() || l3 + (long)n2 >= byteArray.length()) {
            return false;
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            if (this.get(l2 + (long)i2) == byteArray.get(l3 + (long)i2)) continue;
            return false;
        }
        return true;
    }

    abstract long updateChecksum(long var1, int var3, CRC64 var4, long var5);

    abstract void updateDigest(MessageDigest var1, long var2, int var4);

    byte[] getBytesFromHex(long l2, int n2, CharacterSet characterSet) throws SQLException {
        int[] nArray = new int[1];
        char[] cArray = this.getChars(l2, n2, characterSet, nArray);
        int n3 = nArray[0];
        byte[] byArray = new byte[(n3 + 1) / 2];
        boolean bl = true;
        int n4 = 0;
        for (char c2 : cArray) {
            if (bl) {
                byArray[n4] = (byte)(this.hexDigit2Nibble(c2) << 4);
            } else {
                int n5 = n4++;
                byArray[n5] = (byte)(byArray[n5] + (this.hexDigit2Nibble(c2) & 0xF));
            }
            bl = !bl;
        }
        return byArray;
    }

    final int hexDigit2Nibble(char c2) throws SQLException {
        int n2 = Character.digit(c2, 16);
        if (n2 == -1) {
            throw (SQLException)DatabaseError.createSqlException(59, "Invalid hex digit: " + c2).fillInStackTrace();
        }
        return n2;
    }

    final void freeSpace(long l2, int n2) {
    }

    byte[] getBlockBasic(long l2, int[] nArray) {
        throw new Error("not implemented");
    }

    abstract void free();

    ByteArray compact() {
        return this;
    }

    int getUtf8Bytes(long l2, int n2, byte[] byArray, int n3, CharacterSet characterSet) throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }
}

