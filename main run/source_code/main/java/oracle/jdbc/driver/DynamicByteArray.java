/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedAction;
import java.sql.SQLException;
import oracle.jdbc.driver.BlockSource;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.SimpleByteArray;
import oracle.jdbc.driver.T4CMAREngine;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.net.ns.NetException;
import oracle.sql.CharacterSet;
import oracle.sql.converter.CharacterConverter1Byte;
import oracle.sql.converter.CharacterConverterJDBC;
import oracle.sql.converter.JdbcCharacterConverters;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH, Feature.PARAMETER_SET, Feature.CHARACTER_SET_CONVERSION})
final class DynamicByteArray
extends ByteArray {
    static final int INITIAL_BLOCKS = 16;
    static final int GROWTH_FACTOR = 8;
    private static final int CACHED_CHAR_ARRAY_SIZE = 256;
    private static final int COMPACT_THRESHOLD = 131072;
    private final BlockSource source;
    private final long blockSize;
    private byte[][] blocks = new byte[16][];
    protected long length = 0L;
    private char[] charsForNewString = null;
    private static final char[] ISO_LATIN_1_TO_JAVA;
    private static final char[] WE8DEC_TO_JAVA;
    private static final char[] WE8MSWIN1252_TO_JAVA;
    private static final char[] WE8ISO8859P15_TO_JAVA;
    private static final char[] JAVA_TO_ISO_LATIN_1_LEVEL1;
    private static final char[] JAVA_TO_ISO_LATIN_1_LEVEL2;
    private static final char[] JAVA_TO_WE8DEC_LEVEL1;
    private static final char[] JAVA_TO_WE8DEC_LEVEL2;
    private static final char[] JAVA_TO_WE8MSWIN1252_LEVEL1;
    private static final char[] JAVA_TO_WE8MSWIN1252_LEVEL2;
    private static final char[] JAVA_TO_US7ASCII_LEVEL1;
    private static final char[] JAVA_TO_US7ASCII_LEVEL2;
    private static final char[] JAVA_TO_WE8ISO8859P15_LEVEL1;
    private static final char[] JAVA_TO_WE8ISO8859P15_LEVEL2;
    private static final char INVALID_ORA_CHAR = '\uffff';
    private static final char JAVA_TO_ISO_LATIN_1_REPLACEMENT;
    private static final char JAVA_TO_WE8DEC_REPLACEMENT;
    private static final char JAVA_TO_WE8MSWIN1252_REPLACEMENT;
    private static final char JAVA_TO_US7ASCII_REPLACEMENT;
    private static final char JAVA_TO_WE8ISO8859P15_REPLACEMENT;
    private int globalBlockIndex = -1;
    private int globalByteIndex = -1;
    private int globalRemaining = -1;
    private int globalBlockSize = -1;
    private byte[] globalBytes = null;
    private static final char UTF16_REPLACEMENT_CHAR = '\ufffd';

    static DynamicByteArray createDynamicByteArray(BlockSource blockSource) {
        return new DynamicByteArray(blockSource);
    }

    private DynamicByteArray(BlockSource blockSource) {
        assert (blockSource != null) : "source is null";
        this.source = blockSource;
        this.blockSize = blockSource.getBlockSize();
    }

    @Override
    long length() {
        return this.length;
    }

    @Override
    long getCapacity() {
        long l2 = 0L;
        for (byte[] byArray : this.blocks) {
            if (byArray == null) continue;
            l2 += (long)byArray.length;
        }
        return l2;
    }

    private void grow() {
        byte[][] byArrayArray = new byte[this.blocks.length * 8][];
        System.arraycopy(this.blocks, 0, byArrayArray, 0, this.blocks.length);
        this.blocks = byArrayArray;
    }

    private final char[] getCharsForNewString(int n2) {
        if (this.charsForNewString == null || n2 > this.charsForNewString.length) {
            this.charsForNewString = new char[Math.max(256, n2)];
        }
        return this.charsForNewString;
    }

    final int unmarshalCLR(T4CMAREngine t4CMAREngine) throws SQLException, IOException {
        int n2 = 0;
        int n3 = 0;
        n2 = t4CMAREngine.unmarshalUB1();
        if (n2 == 0) {
            return 0;
        }
        if (t4CMAREngine.escapeSequenceNull(n2)) {
            return 0;
        }
        this.globalBlockIndex = (int)(this.position / this.blockSize);
        this.globalByteIndex = (int)(this.position % this.blockSize);
        if (n2 == 254) {
            while (true) {
                int n4 = n2 = t4CMAREngine.useCLRBigChunks ? t4CMAREngine.unmarshalSB4() : (int)t4CMAREngine.unmarshalUB1();
                if (n2 > 0) {
                    this.unmarshalBuffer(t4CMAREngine, n2);
                    n3 += n2;
                    continue;
                }
                break;
            }
        } else {
            this.unmarshalBuffer(t4CMAREngine, n2);
            n3 += n2;
        }
        this.position += (long)n3;
        this.length = Math.max(this.length, this.position + 1L);
        return n3;
    }

    final int unmarshalCLR(T4CMAREngine t4CMAREngine, int n2) throws SQLException, IOException {
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        n3 = t4CMAREngine.unmarshalUB1();
        if (n3 == 0) {
            return 0;
        }
        if (t4CMAREngine.escapeSequenceNull(n3)) {
            return 0;
        }
        this.globalBlockIndex = (int)(this.position / this.blockSize);
        this.globalByteIndex = (int)(this.position % this.blockSize);
        if (n3 == 254) {
            while (true) {
                int n6 = n3 = t4CMAREngine.useCLRBigChunks ? t4CMAREngine.unmarshalSB4() : (int)t4CMAREngine.unmarshalUB1();
                if (n3 > 0) {
                    n5 = Math.min(n2 - n4, n3);
                    this.unmarshalBuffer(t4CMAREngine, n5);
                    n4 += n5;
                    int n7 = n3 - n5;
                    if (n7 <= 0) continue;
                    t4CMAREngine.unmarshalBuffer(T4CMAREngine.IGNORED, 0, n7);
                    continue;
                }
                break;
            }
        } else {
            n5 = Math.min(n2 - n4, n3);
            this.unmarshalBuffer(t4CMAREngine, n5);
            n4 += n5;
            int n8 = n3 - n5;
            if (n8 > 0) {
                t4CMAREngine.unmarshalBuffer(T4CMAREngine.IGNORED, 0, n8);
            }
        }
        this.position += (long)n4;
        this.length = Math.max(this.length, this.position + 1L);
        return n4;
    }

    private final void unmarshalBuffer(T4CMAREngine t4CMAREngine, int n2) throws SQLException, IOException {
        while (n2 > 0) {
            while (this.globalBlockIndex >= this.blocks.length) {
                this.grow();
            }
            if (this.blocks[this.globalBlockIndex] == null) {
                this.blocks[this.globalBlockIndex] = this.source.get();
            }
            int n3 = Math.min(n2, (int)this.blockSize - this.globalByteIndex);
            t4CMAREngine.unmarshalNBytes(this.blocks[this.globalBlockIndex], this.globalByteIndex, n3);
            this.globalByteIndex += n3;
            if (this.globalByteIndex >= (int)this.blockSize) {
                this.globalByteIndex = 0;
                ++this.globalBlockIndex;
            }
            n2 -= n3;
        }
    }

    final int unmarshalCLRforREFS(T4CMAREngine t4CMAREngine) throws SQLException, IOException {
        byte[] byArray = t4CMAREngine.unmarshalCLRforREFS();
        if (byArray == null) {
            return 0;
        }
        this.put(byArray);
        return byArray.length;
    }

    final void marshalB1Array(T4CMAREngine t4CMAREngine, long l2, int n2) throws IOException {
        assert (l2 >= 0L && l2 + (long)n2 <= this.length) : " offset: " + l2 + " this.length: " + this.length + " length: " + n2;
        int n3 = (int)(l2 / this.blockSize);
        int n4 = (int)(l2 % this.blockSize);
        int n5 = (int)this.blockSize;
        int n6 = n2;
        do {
            t4CMAREngine.marshalB1Array(this.blocks[n3], n4, Math.min(n5 - n4, n6));
            ++n3;
        } while ((n6 -= n5 - (n4 = 0)) > 0);
    }

    void writeZeroCopyIO(T4CMAREngine t4CMAREngine, long l2, int n2) throws IOException, NetException {
        int n3;
        int n4;
        assert (l2 >= 0L && l2 + (long)n2 <= this.length) : " offset: " + l2 + " this.length: " + this.length + " length: " + n2;
        int n5 = (int)(l2 / this.blockSize);
        int n6 = (int)(l2 % this.blockSize);
        int n7 = (int)this.blockSize;
        boolean bl = true;
        boolean bl2 = false;
        if (n2 % 1703910 == 0) {
            n4 = n2 / 1703910;
            n3 = 1703910;
        } else {
            n4 = n2 / 1703910 + 1;
            n3 = n2 % 1703910;
        }
        for (int i2 = 0; i2 < n4; ++i2) {
            int n8;
            if (i2 == n4 - 1) {
                n8 = n3;
                bl2 = true;
            } else {
                n8 = 1703910;
            }
            t4CMAREngine.writeZeroCopyIOHeader(bl, n8, bl2);
            bl = false;
            do {
                int n9 = n7 - n6;
                int n10 = Math.min(n9, n8);
                t4CMAREngine.writeZeroCopyIOData(this.blocks[n5], n6, n10);
                n8 -= n10;
                if (n6 + n10 == n7) {
                    ++n5;
                    n6 = 0;
                    continue;
                }
                n6 += n10;
            } while (n8 > 0);
        }
    }

    void copyLeft(long l2, int n2) {
        int n3;
        assert (l2 >= this.position && n2 >= 0 && this.length >= this.position + (long)n2) : "this.position: " + this.position + " this.length: " + this.length + " srcOffset: " + l2 + " length: " + n2;
        if (l2 == this.position || n2 == 0) {
            this.position += (long)n2;
            return;
        }
        assert (l2 > this.position && n2 > 0);
        int n4 = (int)(this.position / this.blockSize);
        int n5 = (int)(this.position % this.blockSize);
        int n6 = (int)(l2 / this.blockSize);
        int n7 = (int)(l2 % this.blockSize);
        int n8 = (int)this.blockSize;
        for (int i2 = n2; i2 > 0; i2 -= n3) {
            n3 = Math.min(i2, Math.min(n8 - n7, n8 - n5));
            System.arraycopy(this.blocks[n6], n7, this.blocks[n4], n5, n3);
            if ((n7 += n3) == n8) {
                ++n6;
                n7 = 0;
            }
            if ((n5 += n3) != n8) continue;
            ++n4;
            n5 = 0;
        }
        this.position += (long)n2;
    }

    @Override
    void put(long l2, byte by) {
        assert (l2 >= 0L) : "index: " + l2;
        int n2 = (int)(l2 / this.blockSize);
        int n3 = (int)(l2 % this.blockSize);
        while (n2 >= this.blocks.length) {
            this.grow();
        }
        if (this.blocks[n2] == null) {
            this.blocks[n2] = this.source.get();
        }
        this.blocks[n2][n3] = by;
        this.length = Math.max(this.length, l2 + 1L);
    }

    @Override
    byte get(long l2) {
        assert (l2 >= 0L && l2 < this.length);
        int n2 = (int)(l2 / this.blockSize);
        int n3 = (int)(l2 % this.blockSize);
        assert (this.blocks.length >= n2 && this.blocks[n2] != null) : "invalid read--blocks.length: " + this.blocks.length + " blockIndex: " + n2 + (n2 < this.blocks.length ? " blocks[" + n2 + "]: " + this.blocks[n2] : "");
        return this.blocks[n2][n3];
    }

    @Override
    void put(long l2, byte[] byArray, int n2, int n3) {
        assert (byArray != null) : "src is null";
        assert (l2 >= 0L && n2 + n3 <= byArray.length) : "offset: " + l2 + " src.length: " + byArray.length + " srcOffset: " + n2 + " length: " + n3;
        int n4 = (int)(l2 / this.blockSize);
        int n5 = (int)(l2 % this.blockSize);
        int n6 = (int)this.blockSize;
        int n7 = n3;
        while (true) {
            if (n4 >= this.blocks.length) {
                this.grow();
                continue;
            }
            if (this.blocks[n4] == null) {
                this.blocks[n4] = this.source.get();
            }
            System.arraycopy(byArray, n2 + n3 - n7, this.blocks[n4], n5, Math.min(n6 - n5, n7));
            ++n4;
            n5 = 0;
            if ((n7 -= n6 - n5) <= 0) break;
        }
        this.length = Math.max(this.length, l2 + (long)n3 + 1L);
    }

    @Override
    void get(long l2, byte[] byArray, int n2, int n3) {
        assert (l2 >= 0L && l2 + (long)n3 <= this.length && n2 >= 0 && n2 + n3 <= byArray.length) : " offset: " + l2 + " this.length: " + this.length + " destOffset: " + n2 + " length: " + n3;
        int n4 = (int)(l2 / this.blockSize);
        int n5 = (int)(l2 % this.blockSize);
        int n6 = (int)this.blockSize;
        int n7 = n3;
        do {
            System.arraycopy(this.blocks[n4], n5, byArray, n2 + n3 - n7, Math.min(n6 - n5, n7));
            ++n4;
        } while ((n7 -= n6 - (n5 = 0)) > 0);
    }

    @Override
    void free() {
        for (int i2 = 0; i2 < this.blocks.length; ++i2) {
            if (this.blocks[i2] == null) continue;
            this.source.put(this.blocks[i2]);
            this.blocks[i2] = null;
        }
        this.position = 0L;
        this.length = 0L;
        this.charsForNewString = null;
    }

    @Override
    long updateChecksum(long l2, int n2, CRC64 cRC64, long l3) {
        int n3 = (int)(l2 / this.blockSize);
        int n4 = (int)(l2 % this.blockSize);
        int n5 = n2;
        long l4 = l3;
        while (n5 > 0) {
            int n6 = Math.min(n5, (int)this.blockSize - n4);
            l4 = CRC64.updateChecksum(l4, this.blocks[n3], n4, n6);
            n5 -= n6;
            ++n3;
            n4 = 0;
        }
        return l4;
    }

    @Override
    void updateDigest(MessageDigest messageDigest, long l2, int n2) {
        assert (l2 >= 0L && l2 + (long)n2 <= this.length) : " valOffset: " + l2 + " this.length: " + this.length + " valLen: " + n2;
        int n3 = (int)(l2 / this.blockSize);
        int n4 = (int)(l2 % this.blockSize);
        int n5 = n2;
        while (n5 > 0) {
            int n6 = Math.min(n5, (int)this.blockSize - n4);
            messageDigest.update(this.blocks[n3], n4, n6);
            n5 -= n6;
            ++n3;
            n4 = 0;
        }
    }

    @Override
    final char[] getChars(long l2, int n2, CharacterSet characterSet, int[] nArray) throws SQLException {
        String string = this.getString(l2, n2, characterSet);
        char[] cArray = string.toCharArray();
        nArray[0] = cArray.length;
        return cArray;
    }

    @Override
    final String getString(long l2, int n2, CharacterSet characterSet) throws SQLException {
        switch (characterSet.getOracleId()) {
            case 1: {
                return this.getStringFromUS7ASCII(l2, n2);
            }
            case 2: {
                return this.getStringFrom1Byte(l2, n2, WE8DEC_TO_JAVA);
            }
            case 31: {
                return this.getStringFrom1Byte(l2, n2, ISO_LATIN_1_TO_JAVA);
            }
            case 46: {
                return this.getStringFrom1Byte(l2, n2, WE8ISO8859P15_TO_JAVA);
            }
            case 178: {
                return this.getStringFrom1Byte(l2, n2, WE8MSWIN1252_TO_JAVA);
            }
            case 2000: {
                return this.getStringFromAL16UTF16(l2, n2);
            }
            case 2002: {
                return this.getStringFromAL16UTF16LE(l2, n2);
            }
            case 871: {
                return this.getStringFromUTF8(l2, n2);
            }
            case 873: {
                return this.getStringFromAL32UTF8(l2, n2);
            }
        }
        assert (false) : "charSet: " + characterSet.toString();
        int n3 = (int)(l2 / this.blockSize);
        int n4 = (int)(l2 % this.blockSize);
        if (n2 <= (int)this.blockSize - n4) {
            return characterSet.toString(this.blocks[n3], n4, n2);
        }
        byte[] byArray = this.get(l2, n2);
        return characterSet.toString(byArray, 0, byArray.length);
    }

    @Override
    final int putString(long l2, String string, CharacterSet characterSet) throws SQLException {
        if (string == null || string.length() == 0) {
            return 0;
        }
        switch (characterSet.getOracleId()) {
            case 1: {
                return this.putStringFrom1Byte(l2, string, JAVA_TO_US7ASCII_LEVEL1, JAVA_TO_US7ASCII_LEVEL2);
            }
            case 2: {
                return this.putStringFrom1Byte(l2, string, JAVA_TO_WE8DEC_LEVEL1, JAVA_TO_WE8DEC_LEVEL2);
            }
            case 31: {
                return this.putStringFrom1Byte(l2, string, JAVA_TO_ISO_LATIN_1_LEVEL1, JAVA_TO_ISO_LATIN_1_LEVEL2);
            }
            case 46: {
                return this.putStringFrom1Byte(l2, string, JAVA_TO_WE8ISO8859P15_LEVEL1, JAVA_TO_WE8ISO8859P15_LEVEL2);
            }
            case 178: {
                return this.putStringFrom1Byte(l2, string, JAVA_TO_WE8MSWIN1252_LEVEL1, JAVA_TO_WE8MSWIN1252_LEVEL2);
            }
            case 2000: {
                return this.putStringFromAL16UTF16(l2, string);
            }
            case 2002: {
                return this.putStringFromAL16UTF16LE(l2, string);
            }
            case 871: {
                return this.putStringFromUTF8(l2, string);
            }
            case 873: {
                return this.putStringFromAL32UTF8(l2, string);
            }
        }
        return this.putStringFromCharset(l2, string, characterSet);
    }

    @Override
    final int putStringWithReplacement(long l2, String string, CharacterSet characterSet) throws SQLException {
        if (string == null || string.length() == 0) {
            return 0;
        }
        switch (characterSet.getOracleId()) {
            case 871: 
            case 873: 
            case 2000: 
            case 2002: {
                return this.putString(l2, string, characterSet);
            }
            case 31: {
                return this.putStringFromISOLatin1WithReplacement(l2, string);
            }
            case 46: {
                return this.putStringFrom1ByteWithReplacement(l2, string, JAVA_TO_WE8ISO8859P15_LEVEL1, JAVA_TO_WE8ISO8859P15_LEVEL2, JAVA_TO_WE8ISO8859P15_REPLACEMENT);
            }
            case 1: {
                return this.putStringFrom1ByteWithReplacement(l2, string, JAVA_TO_US7ASCII_LEVEL1, JAVA_TO_US7ASCII_LEVEL2, JAVA_TO_US7ASCII_REPLACEMENT);
            }
            case 2: {
                return this.putStringFrom1ByteWithReplacement(l2, string, JAVA_TO_WE8DEC_LEVEL1, JAVA_TO_WE8DEC_LEVEL2, JAVA_TO_WE8DEC_REPLACEMENT);
            }
            case 178: {
                return this.putStringFrom1ByteWithReplacement(l2, string, JAVA_TO_WE8MSWIN1252_LEVEL1, JAVA_TO_WE8MSWIN1252_LEVEL2, JAVA_TO_WE8MSWIN1252_REPLACEMENT);
            }
        }
        return this.putStringFromCharsetWithReplacement(l2, string, characterSet);
    }

    private final String getStringFromUS7ASCII(long l2, int n2) throws SQLException {
        assert (l2 >= 0L && n2 >= 0 && this.length >= l2 + (long)n2) : "this.length: " + this.length + " offset: " + l2 + " lengthInBytes: " + n2;
        int n3 = (int)(l2 / this.blockSize);
        int n4 = (int)(l2 % this.blockSize);
        byte[] byArray = null;
        int n5 = n2;
        char[] cArray = this.getCharsForNewString(n2);
        int n6 = 0;
        while (n5 > 0) {
            byArray = this.blocks[n3++];
            int n7 = Math.min(n4 + n5, byArray.length);
            n5 = n5 - n7 + n4;
            while (n4 < n7) {
                cArray[n6++] = (char)byArray[n4++];
            }
            n4 = 0;
        }
        assert (n6 == n2) : "charIndex: " + n6 + "\tlengthInBytes: " + n2;
        return new String(cArray, 0, n6);
    }

    @Override
    final int putAsciiString(long l2, String string) {
        assert (l2 >= 0L && string != null && string.length() >= 0) : "this.length: " + this.length + " offset: " + l2 + " val.length(): " + string.length();
        int n2 = string.length();
        int n3 = 0;
        int n4 = (int)(l2 / this.blockSize);
        int n5 = (int)(l2 % this.blockSize);
        byte[] byArray = null;
        while (true) {
            if (n4 >= this.blocks.length) {
                this.grow();
                continue;
            }
            if (this.blocks[n4] == null) {
                this.blocks[n4] = this.source.get();
            }
            byArray = this.blocks[n4++];
            int n6 = Math.min(n5 + (n2 - n3), byArray.length);
            while (n5 < n6) {
                byArray[n5++] = (byte)string.charAt(n3++);
            }
            n5 = 0;
            if (n3 >= n2) break;
        }
        this.length = Math.max(this.length, l2 + (long)n2 + 1L);
        assert (n3 == n2) : "charIndex: " + n3 + "\tstrLen: " + n2;
        return n2;
    }

    private static final char[] intToChar(int[] nArray) {
        char[] cArray = new char[nArray.length];
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            cArray[i2] = (char)nArray[i2];
        }
        return cArray;
    }

    private final int getCharsFrom1Byte(char[] cArray, long l2, int n2, char[] cArray2) throws SQLException {
        assert (l2 >= 0L && n2 >= 0 && this.length >= l2 + (long)n2) : "this.length: " + this.length + " offset: " + l2 + " lengthInBytes: " + n2;
        int n3 = (int)(l2 / this.blockSize);
        int n4 = (int)(l2 % this.blockSize);
        byte[] byArray = null;
        int n5 = n2;
        int n6 = 0;
        while (n5 > 0) {
            byArray = this.blocks[n3++];
            int n7 = Math.min(n4 + n5, byArray.length);
            n5 = n5 - n7 + n4;
            while (n4 < n7) {
                cArray[n6++] = cArray2[byArray[n4++] & 0xFF];
            }
            n4 = 0;
        }
        assert (n6 == n2) : "charIndex: " + n6 + "\tlengthInBytes: " + n2;
        return n6;
    }

    private final String getStringFrom1Byte(long l2, int n2, char[] cArray) throws SQLException {
        char[] cArray2 = this.getCharsForNewString(n2);
        int n3 = this.getCharsFrom1Byte(cArray2, l2, n2, cArray);
        return new String(cArray2, 0, n3);
    }

    private final int putStringFrom1Byte(long l2, String string, char[] cArray, char[] cArray2) throws SQLException {
        assert (l2 >= 0L && string != null && string.length() >= 0) : "this.length: " + this.length + " offset: " + l2 + " val.length(): " + string.length();
        int n2 = string.length();
        int n3 = 0;
        int n4 = (int)(l2 / this.blockSize);
        int n5 = (int)(l2 % this.blockSize);
        byte[] byArray = null;
        while (true) {
            if (n4 >= this.blocks.length) {
                this.grow();
                continue;
            }
            if (this.blocks[n4] == null) {
                this.blocks[n4] = this.source.get();
            }
            byArray = this.blocks[n4++];
            int n6 = Math.min(n5 + (n2 - n3), byArray.length);
            while (n5 < n6) {
                int n7;
                char c2;
                int n8;
                char c3;
                if ((c3 = cArray2[cArray[n8 = (c2 = string.charAt(n3++)) >>> 8] + (n7 = c2 & 0xFF)]) != '\uffff') {
                    byArray[n5++] = (byte)c3;
                    continue;
                }
                throw (SQLException)DatabaseError.createSqlException(155).fillInStackTrace();
            }
            n5 = 0;
            if (n3 >= n2) break;
        }
        this.length = Math.max(this.length, l2 + (long)n2 + 1L);
        assert (n3 == n2) : "charIndex: " + n3 + "\tstrLen: " + n2;
        return n2;
    }

    private int putStringFrom1ByteWithReplacement(long l2, String string, char[] cArray, char[] cArray2, char c2) {
        assert (l2 >= 0L && string != null && string.length() >= 0) : "this.length: " + this.length + " offset: " + l2 + " val.length(): " + string.length();
        int n2 = string.length();
        int n3 = 0;
        int n4 = 0;
        int n5 = (int)(l2 / this.blockSize);
        int n6 = (int)(l2 % this.blockSize);
        byte[] byArray = null;
        while (true) {
            if (n5 >= this.blocks.length) {
                this.grow();
                continue;
            }
            if (this.blocks[n5] == null) {
                this.blocks[n5] = this.source.get();
            }
            byArray = this.blocks[n5++];
            while (n6 < byArray.length && n3 < n2) {
                byte by;
                char c3;
                if ((c3 = string.charAt(n3++)) < '\u0080') {
                    by = (byte)c3;
                } else if (Character.isHighSurrogate(c3)) {
                    by = (byte)c2;
                    if (n3 < n2 && Character.isLowSurrogate(string.charAt(n3))) {
                        ++n3;
                    }
                } else {
                    int n7 = c3 >>> 8;
                    int n8 = c3 & 0xFF;
                    char c4 = cArray2[cArray[n7] + n8];
                    by = c4 == '\uffff' ? (byte)c2 : (byte)c4;
                }
                byArray[n6++] = by;
                ++n4;
            }
            n6 = 0;
            if (n3 >= n2) break;
        }
        this.length = Math.max(this.length, l2 + (long)n4);
        assert (n3 == n2) : "charIndex: " + n3 + "\tstrLen: " + n2;
        return n4;
    }

    private int putStringFromISOLatin1WithReplacement(long l2, String string) {
        assert (l2 >= 0L && string != null && string.length() >= 0) : "this.length: " + this.length + " offset: " + l2 + " val.length(): " + string.length();
        int n2 = string.length();
        int n3 = 0;
        int n4 = (int)(l2 / this.blockSize);
        int n5 = (int)(l2 % this.blockSize);
        byte[] byArray = null;
        while (true) {
            if (n4 >= this.blocks.length) {
                this.grow();
                continue;
            }
            if (this.blocks[n4] == null) {
                this.blocks[n4] = this.source.get();
            }
            byArray = this.blocks[n4++];
            int n6 = Math.min(n5 + (n2 - n3), byArray.length);
            while (n5 < n6) {
                char c2 = string.charAt(n3++);
                byArray[n5++] = (byte)(c2 > '\u00ff' ? 191 : (int)c2);
            }
            n5 = 0;
            if (n3 >= n2) break;
        }
        this.length = Math.max(this.length, l2 + (long)n2);
        assert (n3 == n2) : "charIndex: " + n3 + "\tstrLen: " + n2;
        return n2;
    }

    private final void setGlobals(long l2, int n2) {
        this.globalBlockIndex = (int)(l2 / this.blockSize);
        this.globalByteIndex = (int)(l2 % this.blockSize);
        this.globalRemaining = n2;
        this.globalBlockSize = (int)this.blockSize;
        this.globalBytes = this.globalBlockIndex < this.blocks.length ? this.blocks[this.globalBlockIndex++] : null;
    }

    private final byte next() {
        assert (this.globalRemaining > 0) : "next overrun in DBA";
        byte by = this.globalBytes[this.globalByteIndex++];
        --this.globalRemaining;
        if (this.globalByteIndex >= this.globalBlockSize) {
            this.globalBytes = this.globalBlockIndex < this.blocks.length ? this.blocks[this.globalBlockIndex++] : null;
            this.globalByteIndex = 0;
        }
        return by;
    }

    private final byte peek() {
        assert (this.globalRemaining > 0) : "peek overrun in DBA";
        return this.globalBytes[this.globalByteIndex];
    }

    private final void back() {
        if (this.globalByteIndex == 0) {
            this.globalByteIndex = this.globalBlockSize;
            --this.globalBlockIndex;
            this.globalBytes = this.blocks[this.globalBlockIndex];
        }
        --this.globalByteIndex;
        ++this.globalRemaining;
    }

    private final int getCharsFromAL16UTF16(char[] cArray, long l2, int n2) throws SQLException {
        assert (l2 >= 0L && n2 >= 0 && this.length >= l2 + (long)n2) : "this.length: " + this.length + " offset: " + l2 + " lengthInBytes: " + n2;
        assert (n2 % 2 == 0) : "lengthInBytes: " + n2;
        this.setGlobals(l2, n2);
        int n3 = 0;
        while (this.globalRemaining > 0) {
            int n4 = this.next() << 8;
            cArray[n3++] = (char)(n4 | this.next() & 0xFF);
        }
        assert (n3 == n2 / 2) : "charIndex: " + n3 + "\tlengthInBytes/2: " + n2 / 2;
        return n3;
    }

    private final String getStringFromAL16UTF16(long l2, int n2) throws SQLException {
        char[] cArray = this.getCharsForNewString(n2 / 2);
        int n3 = this.getCharsFromAL16UTF16(cArray, l2, n2);
        return new String(cArray, 0, n3);
    }

    private final int putStringFromAL16UTF16(long l2, String string) throws SQLException {
        int n2;
        assert (l2 >= 0L && string != null && string.length() >= 0) : "this.length: " + this.length + " offset: " + l2 + " val.length(): " + string.length();
        int n3 = string.length();
        int n4 = 0;
        int n5 = (int)(l2 / this.blockSize);
        int n6 = (int)(l2 % this.blockSize);
        byte[] byArray = null;
        int n7 = n2 = n3 * 2;
        boolean bl = false;
        while (true) {
            char c2;
            if (n5 >= this.blocks.length) {
                this.grow();
                continue;
            }
            if (this.blocks[n5] == null) {
                this.blocks[n5] = this.source.get();
            }
            byArray = this.blocks[n5++];
            int n8 = Math.min(n6 + n7, byArray.length);
            n7 = n7 - n8 + n6;
            if (bl) {
                bl = false;
                byArray[n6++] = (byte)(string.charAt(n4 - 1) & 0xFF);
            }
            while (n6 < n8 - 1) {
                c2 = string.charAt(n4++);
                byArray[n6++] = (byte)(c2 >>> 8 & 0xFF);
                byArray[n6++] = (byte)(c2 & 0xFF);
            }
            if (n6 < n8) {
                c2 = string.charAt(n4++);
                byArray[n6++] = (byte)(c2 >>> 8 & 0xFF);
                bl = true;
            }
            n6 = 0;
            if (n7 <= 0) break;
        }
        this.length = Math.max(this.length, l2 + (long)n2 + 1L);
        assert (n4 == n3) : "charIndex: " + n4 + "\ttotalSrcBytes: " + n2;
        return n2;
    }

    private final int getCharsFromAL16UTF16LE(char[] cArray, long l2, int n2) throws SQLException {
        assert (l2 >= 0L && n2 >= 0 && this.length >= l2 + (long)n2) : "this.length: " + this.length + " offset: " + l2 + " lengthInBytes: " + n2;
        assert (n2 % 2 == 0) : "lengthInBytes: " + n2;
        this.setGlobals(l2, n2);
        int n3 = 0;
        while (this.globalRemaining > 0) {
            byte by = this.next();
            int n4 = this.next() << 8 | by & 0xFF;
            if (!this.isHiSurrogate((char)n4)) {
                cArray[n3++] = n4;
                continue;
            }
            if (this.globalRemaining == 0) {
                cArray[n3++] = 65533;
                break;
            }
            by = this.next();
            char c2 = (char)(this.next() << 8 | by & 0xFF);
            cArray[n3++] = this.isLoSurrogate(c2) ? n4 : 65533;
            cArray[n3++] = c2;
        }
        assert (n3 == n2 / 2) : "charIndex: " + n3 + "\tlengthInBytes/2: " + n2 / 2;
        return n3;
    }

    private final String getStringFromAL16UTF16LE(long l2, int n2) throws SQLException {
        char[] cArray = this.getCharsForNewString(n2 / 2);
        int n3 = this.getCharsFromAL16UTF16LE(cArray, l2, n2);
        return new String(cArray, 0, n3);
    }

    private final int putStringFromAL16UTF16LE(long l2, String string) throws SQLException {
        int n2;
        assert (l2 >= 0L && string != null && string.length() >= 0) : "this.length: " + this.length + " offset: " + l2 + " val.length(): " + string.length();
        int n3 = string.length();
        int n4 = 0;
        int n5 = (int)(l2 / this.blockSize);
        int n6 = (int)(l2 % this.blockSize);
        byte[] byArray = null;
        int n7 = n2 = n3 * 2;
        boolean bl = false;
        while (true) {
            char c2;
            if (n5 >= this.blocks.length) {
                this.grow();
                continue;
            }
            if (this.blocks[n5] == null) {
                this.blocks[n5] = this.source.get();
            }
            byArray = this.blocks[n5++];
            int n8 = Math.min(n6 + n7, byArray.length);
            n7 = n7 - n8 + n6;
            if (bl) {
                bl = false;
                byArray[n6++] = (byte)(string.charAt(n4 - 1) >>> 8);
            }
            while (n6 < n8 - 1) {
                c2 = string.charAt(n4++);
                byArray[n6++] = (byte)(c2 & 0xFF);
                byArray[n6++] = (byte)(c2 >>> 8);
            }
            if (n6 < n8) {
                c2 = string.charAt(n4++);
                byArray[n6++] = (byte)(c2 & 0xFF);
                bl = true;
            }
            n6 = 0;
            if (n7 <= 0) break;
        }
        this.length = Math.max(this.length, l2 + (long)n2 + 1L);
        assert (n4 == n3) : "charIndex: " + n4 + "\ttotalSrcBytes: " + n2;
        return n2;
    }

    private final String getStringFromUTF8(long l2, int n2) throws SQLException {
        assert (l2 >= 0L && n2 >= 0 && this.length >= l2 + (long)n2) : "this.length: " + this.length + " offset: " + l2 + " lengthInBytes: " + n2;
        this.setGlobals(l2, n2);
        int n3 = (int)((l2 + (long)n2 - 1L) / this.blockSize) + 1;
        int n4 = (int)((l2 + (long)n2 - 1L) % this.blockSize) + 1;
        char[] cArray = this.getCharsForNewString(n2);
        int n5 = 0;
        while (this.globalRemaining > 0) {
            char c2;
            byte by;
            int n6;
            byte by2 = this.next();
            if (by2 >= 0) {
                cArray[n5++] = (char)by2;
                n6 = this.globalBlockIndex == n3 ? n4 : (this.globalBlockIndex < n3 ? (int)this.blockSize : 0);
                for (by = this.globalByteIndex; by < n6 && this.globalBytes != null && (by2 = this.globalBytes[by]) >= 0; ++by) {
                    cArray[n5++] = (char)by2;
                }
                this.globalRemaining -= by - this.globalByteIndex;
                this.globalByteIndex = by;
                if (this.globalByteIndex < this.globalBlockSize) continue;
                this.globalByteIndex = 0;
                this.globalBytes = this.globalBlockIndex < this.blocks.length ? this.blocks[this.globalBlockIndex++] : null;
                continue;
            }
            int n7 = by2 & 0xF0;
            byte by3 = by2;
            if ((by3 = (byte)(by3 << 2)) >= 0) {
                if (this.globalRemaining < 1) {
                    cArray[n5++] = 65533;
                    continue;
                }
                c2 = this.conv2ByteUTFtoUTF16(by2, this.next());
                cArray[n5++] = c2;
                continue;
            }
            if ((by3 = (byte)(by3 << 1)) >= 0) {
                if (this.globalRemaining < 2) {
                    cArray[n5++] = 65533;
                    continue;
                }
                n6 = this.next();
                int n8 = this.conv3ByteUTFtoUTF16(by2, (byte)n6, by = (byte)this.next());
                if (this.isHiSurrogate((char)n8) && this.globalRemaining > 0) {
                    by2 = this.peek();
                    if ((byte)(by2 & 0xF0) != -32) {
                        cArray[n5++] = 65533;
                        continue;
                    }
                    this.next();
                    if (this.globalRemaining < 2) {
                        cArray[n5++] = 65533;
                        continue;
                    }
                    n6 = this.next();
                    c2 = this.conv3ByteUTFtoUTF16(by2, (byte)n6, by = (byte)this.next());
                    cArray[n5++] = this.isLoSurrogate(c2) ? n8 : 65533;
                    cArray[n5++] = c2;
                    continue;
                }
                cArray[n5++] = n8;
                continue;
            }
            cArray[n5++] = 65533;
        }
        return new String(cArray, 0, n5);
    }

    private final int putStringFromUTF8(long l2, String string) throws SQLException {
        assert (l2 >= 0L && string != null && string.length() >= 0) : "this.length: " + this.length + " offset: " + l2 + " val.length(): " + string.length();
        int n2 = string.length();
        int n3 = 0;
        int n4 = (int)(l2 / this.blockSize);
        int n5 = (int)(l2 % this.blockSize);
        byte[] byArray = null;
        int n6 = 0;
        int n7 = 0;
        while (true) {
            char c2;
            if (n4 >= this.blocks.length) {
                this.grow();
                continue;
            }
            if (this.blocks[n4] == null) {
                this.blocks[n4] = this.source.get();
            }
            byArray = this.blocks[n4++];
            int n8 = byArray.length;
            int n9 = n5;
            if (n7 > 0) {
                c2 = string.charAt(n3++);
                if (n7 == 2) {
                    byArray[n5++] = (byte)(0x80 | c2 >>> 6 & 0x3F);
                }
                byArray[n5++] = (byte)(0x80 | c2 >>> 0 & 0x3F);
                n7 = 0;
            }
            while (n5 < n8 - 3 && n3 < n2) {
                if ((c2 = string.charAt(n3++)) >= '\u0000' && c2 <= '\u007f') {
                    byArray[n5++] = (byte)c2;
                    continue;
                }
                if (c2 > '\u07ff') {
                    byArray[n5++] = (byte)(0xE0 | c2 >>> 12 & 0xF);
                    byArray[n5++] = (byte)(0x80 | c2 >>> 6 & 0x3F);
                    byArray[n5++] = (byte)(0x80 | c2 >>> 0 & 0x3F);
                    continue;
                }
                byArray[n5++] = (byte)(0xC0 | c2 >>> 6 & 0x1F);
                byArray[n5++] = (byte)(0x80 | c2 >>> 0 & 0x3F);
            }
            while (n5 < n8 && n3 < n2) {
                c2 = string.charAt(n3);
                if (c2 >= '\u0000' && c2 <= '\u007f') {
                    byArray[n5++] = (byte)c2;
                } else if (c2 > '\u07ff') {
                    byArray[n5++] = (byte)(0xE0 | c2 >>> 12 & 0xF);
                    if (n5 < n8) {
                        byArray[n5++] = (byte)(0x80 | c2 >>> 6 & 0x3F);
                        if (n5 < n8) {
                            byArray[n5++] = (byte)(0x80 | c2 >>> 0 & 0x3F);
                        } else {
                            n7 = 1;
                        }
                    } else {
                        n7 = 2;
                    }
                } else {
                    byArray[n5++] = (byte)(0xC0 | c2 >>> 6 & 0x1F);
                    if (n5 < n8) {
                        byArray[n5++] = (byte)(0x80 | c2 >>> 0 & 0x3F);
                    } else {
                        n7 = 1;
                    }
                }
                if (n7 != 0) continue;
                ++n3;
            }
            n6 += n5 - n9;
            n5 = 0;
            if (n3 >= n2) break;
        }
        this.length = Math.max(this.length, l2 + (long)n6 + 1L);
        assert (n3 == n2) : "charIndex: " + n3 + "\ttotalBytesWritten: " + n6;
        return n6;
    }

    private final String getStringFromAL32UTF8(long l2, int n2) throws SQLException {
        assert (l2 >= 0L && n2 >= 0 && this.length >= l2 + (long)n2) : "this.length: " + this.length + " offset: " + l2 + " lengthInBytes: " + n2;
        this.setGlobals(l2, n2);
        int n3 = (int)((l2 + (long)n2 - 1L) / this.blockSize) + 1;
        int n4 = (int)((l2 + (long)n2 - 1L) % this.blockSize) + 1;
        char[] cArray = this.getCharsForNewString(n2);
        int n5 = 0;
        while (this.globalRemaining > 0) {
            byte by = this.next();
            if (by >= 0) {
                int n6;
                cArray[n5++] = (char)by;
                int n7 = this.globalBlockIndex == n3 ? n4 : (this.globalBlockIndex < n3 ? (int)this.blockSize : 0);
                for (n6 = this.globalByteIndex; n6 < n7 && this.globalBytes != null && (by = this.globalBytes[n6]) >= 0; ++n6) {
                    cArray[n5++] = (char)by;
                }
                this.globalRemaining -= n6 - this.globalByteIndex;
                this.globalByteIndex = n6;
                if (this.globalByteIndex < this.globalBlockSize) continue;
                this.globalByteIndex = 0;
                this.globalBytes = this.globalBlockIndex < this.blocks.length ? this.blocks[this.globalBlockIndex++] : null;
                continue;
            }
            byte by2 = by;
            if ((by2 = (byte)(by2 << 2)) >= 0) {
                if (this.globalRemaining < 1) {
                    cArray[n5++] = 65533;
                    continue;
                }
                cArray[n5++] = this.conv2ByteUTFtoUTF16(by, this.next());
                continue;
            }
            if ((by2 = (byte)(by2 << 1)) >= 0) {
                if (this.globalRemaining < 2) {
                    cArray[n5++] = 65533;
                    continue;
                }
                cArray[n5++] = this.conv3ByteAL32UTF8toUTF16(by, this.next(), this.next());
                continue;
            }
            if ((by2 = (byte)(by2 << 1)) >= 0) {
                if (this.globalRemaining < 3) {
                    cArray[n5++] = 65533;
                    continue;
                }
                n5 = this.conv4ByteAL32UTF8toUTF16(by, this.next(), this.next(), this.next(), cArray, n5);
                continue;
            }
            cArray[n5++] = 65533;
        }
        return new String(cArray, 0, n5);
    }

    private final int putStringFromAL32UTF8(long l2, String string) throws SQLException {
        assert (l2 >= 0L && string != null && string.length() >= 0) : "this.length: " + this.length + " offset: " + l2 + " val.length(): " + string.length();
        int n2 = string.length();
        int n3 = 0;
        int n4 = (int)(l2 / this.blockSize);
        int n5 = (int)(l2 % this.blockSize);
        byte[] byArray = null;
        int n6 = 0;
        int n7 = 0;
        byte[] byArray2 = new byte[3];
        while (true) {
            int n8;
            char c2;
            char c3;
            int n9;
            if (n4 >= this.blocks.length) {
                this.grow();
                continue;
            }
            if (this.blocks[n4] == null) {
                this.blocks[n4] = this.source.get();
            }
            byArray = this.blocks[n4++];
            int n10 = byArray.length;
            int n11 = n5;
            if (n7 > 0) {
                for (n9 = 0; n9 < n7; ++n9) {
                    byArray[n5++] = byArray2[n9];
                }
                n7 = 0;
            }
            while (n5 < n10 - 4 && n3 < n2) {
                c3 = string.charAt(n3++);
                c2 = '\u0000';
                if (c3 >= '\u0000' && c3 <= '\u007f') {
                    byArray[n5++] = (byte)c3;
                    continue;
                }
                if (this.isHiSurrogate(c3)) {
                    if (n3 < n2 && this.isLoSurrogate(c2 = string.charAt(n3))) {
                        n8 = (c3 >>> 6 & 0xF) + 1;
                        byArray[n5++] = (byte)(n8 >>> 2 | 0xF0);
                        byArray[n5++] = (byte)((n8 & 3) << 4 | c3 >>> 2 & 0xF | 0x80);
                        byArray[n5++] = (byte)((c3 & 3) << 4 | c2 >>> 6 & 0xF | 0x80);
                        byArray[n5++] = (byte)(c2 & 0x3F | 0x80);
                        ++n3;
                        continue;
                    }
                    byArray[n5++] = -17;
                    byArray[n5++] = -65;
                    byArray[n5++] = -67;
                    continue;
                }
                if (c3 > '\u07ff') {
                    byArray[n5++] = (byte)(0xE0 | c3 >>> 12 & 0xF);
                    byArray[n5++] = (byte)(0x80 | c3 >>> 6 & 0x3F);
                    byArray[n5++] = (byte)(0x80 | c3 >>> 0 & 0x3F);
                    continue;
                }
                byArray[n5++] = (byte)(0xC0 | c3 >>> 6 & 0x1F);
                byArray[n5++] = (byte)(0x80 | c3 >>> 0 & 0x3F);
            }
            while (n5 < n10 && n3 < n2) {
                byte by;
                c3 = string.charAt(n3++);
                c2 = '\u0000';
                if (c3 >= '\u0000' && c3 <= '\u007f') {
                    byArray[n5++] = (byte)c3;
                    continue;
                }
                if (this.isHiSurrogate(c3)) {
                    if (n3 < n2 && this.isLoSurrogate(c2 = string.charAt(n3))) {
                        n8 = (c3 >>> 6 & 0xF) + 1;
                        byArray[n5++] = (byte)(n8 >>> 2 | 0xF0);
                        n9 = (byte)((n8 & 3) << 4 | c3 >>> 2 & 0xF | 0x80);
                        by = (byte)((c3 & 3) << 4 | c2 >>> 6 & 0xF | 0x80);
                        byte by2 = (byte)(c2 & 0x3F | 0x80);
                        if (n5 < n10) {
                            byArray[n5++] = n9;
                        } else {
                            byArray2[n7++] = n9;
                        }
                        if (n5 < n10) {
                            byArray[n5++] = by;
                        } else {
                            byArray2[n7++] = by;
                        }
                        if (n5 < n10) {
                            byArray[n5++] = by2;
                        } else {
                            byArray2[n7++] = by2;
                        }
                        ++n3;
                        continue;
                    }
                    byArray[n5++] = -17;
                    if (n5 < n10) {
                        byArray[n5++] = -65;
                    } else {
                        byArray2[n7++] = -65;
                    }
                    if (n5 < n10) {
                        byArray[n5++] = -67;
                        continue;
                    }
                    byArray2[n7++] = -67;
                    continue;
                }
                if (c3 > '\u07ff') {
                    byArray[n5++] = (byte)(0xE0 | c3 >>> 12 & 0xF);
                    n9 = (byte)(0x80 | c3 >>> 6 & 0x3F);
                    by = (byte)(0x80 | c3 >>> 0 & 0x3F);
                    if (n5 < n10) {
                        byArray[n5++] = n9;
                    } else {
                        byArray2[n7++] = n9;
                    }
                    if (n5 < n10) {
                        byArray[n5++] = by;
                        continue;
                    }
                    byArray2[n7++] = by;
                    continue;
                }
                byArray[n5++] = (byte)(0xC0 | c3 >>> 6 & 0x1F);
                if (n5 < n10) {
                    byArray[n5++] = (byte)(0x80 | c3 >>> 0 & 0x3F);
                    continue;
                }
                byArray2[n7++] = (byte)(0x80 | c3 >>> 0 & 0x3F);
            }
            n6 += n5 - n11;
            n5 = 0;
            if (n3 >= n2 && n7 <= 0) break;
        }
        this.length = Math.max(this.length, l2 + (long)n6 + 1L);
        assert (n3 == n2) : "charIndex: " + n3 + "\ttotalBytesWritten: " + n6;
        return n6;
    }

    private final int putStringFromCharset(long l2, String string, CharacterSet characterSet) throws SQLException {
        byte[] byArray = characterSet.convert(string);
        this.put(l2, byArray, 0, byArray.length);
        return byArray.length;
    }

    private final int putStringFromCharsetWithReplacement(long l2, String string, CharacterSet characterSet) throws SQLException {
        byte[] byArray = characterSet.convertWithReplacement(string);
        this.put(l2, byArray, 0, byArray.length);
        return byArray.length;
    }

    private final boolean isHiSurrogate(char c2) {
        return (char)(c2 & 0xFC00) == '\ud800';
    }

    private final boolean isLoSurrogate(char c2) {
        return (char)(c2 & 0xFC00) == '\udc00';
    }

    private final boolean check80toBF(byte by) {
        return (by & 0xFFFFFFC0) == -128;
    }

    private final boolean check80to8F(byte by) {
        return (by & 0xFFFFFFF0) == -128;
    }

    private final boolean check80to9F(byte by) {
        return (by & 0xFFFFFFE0) == -128;
    }

    private final boolean checkA0toBF(byte by) {
        return (by & 0xFFFFFFE0) == -96;
    }

    private final boolean check90toBF(byte by) {
        return (by & 0xFFFFFFC0) == -128 && (by & 0x30) != 0;
    }

    private final char conv2ByteUTFtoUTF16(byte by, byte by2) {
        if (by < -62 || by > -33 || !this.check80toBF(by2)) {
            this.back();
            return '\ufffd';
        }
        return (char)((by & 0x1F) << 6 | by2 & 0x3F);
    }

    private final char conv3ByteUTFtoUTF16(byte by, byte by2, byte by3) {
        if (!(by == -32 && this.checkA0toBF(by2) && this.check80toBF(by3) || by >= -31 && by <= -17 && this.check80toBF(by2) && this.check80toBF(by3))) {
            this.back();
            this.back();
            return '\ufffd';
        }
        return (char)((by & 0xF) << 12 | (by2 & 0x3F) << 6 | by3 & 0x3F);
    }

    private final char conv3ByteAL32UTF8toUTF16(byte by, byte by2, byte by3) {
        if (!(by == -32 && this.checkA0toBF(by2) && this.check80toBF(by3) || by >= -31 && by <= -20 && this.check80toBF(by2) && this.check80toBF(by3) || by == -19 && this.check80to9F(by2) && this.check80toBF(by3) || by >= -18 && by <= -17 && this.check80toBF(by2) && this.check80toBF(by3))) {
            this.back();
            this.back();
            return '\ufffd';
        }
        return (char)((by & 0xF) << 12 | (by2 & 0x3F) << 6 | by3 & 0x3F);
    }

    private final int conv4ByteAL32UTF8toUTF16(byte by, byte by2, byte by3, byte by4, char[] cArray, int n2) {
        boolean bl = false;
        if (!(by == -16 && this.check90toBF(by2) && this.check80toBF(by3) && this.check80toBF(by4) || by >= -15 && by <= -13 && this.check80toBF(by2) && this.check80toBF(by3) && this.check80toBF(by4) || by == -12 && this.check80to8F(by2) && this.check80toBF(by3) && this.check80toBF(by4))) {
            this.back();
            this.back();
            this.back();
            cArray[n2++] = 65533;
        } else {
            cArray[n2++] = (char)((((by & 7) << 2 | by2 >>> 4 & 3) - 1 & 0xF) << 6 | (by2 & 0xF) << 2 | by3 >>> 4 & 3 | 0xD800);
            cArray[n2++] = (char)((by3 & 0xF) << 6 | by4 & 0x3F | 0xDC00);
        }
        return n2;
    }

    @Override
    byte[] getBlockBasic(long l2, int[] nArray) {
        int n2 = (int)(l2 / this.blockSize);
        nArray[0] = (int)(l2 % this.blockSize);
        if (n2 >= this.blocks.length) {
            return null;
        }
        return this.blocks[n2];
    }

    private static String escape(String string) {
        StringBuilder stringBuilder = new StringBuilder(string.length() * 6);
        for (char c2 : string.toCharArray()) {
            stringBuilder.append("\\u");
            byte by = (byte)(c2 >> 8);
            String string2 = "00" + Integer.toHexString(by & 0xFF);
            stringBuilder.append(string2, string2.length() - 2, string2.length());
            string2 = "00" + Integer.toHexString((byte)c2 & 0xFF);
            stringBuilder.append(string2, string2.length() - 2, string2.length());
        }
        return stringBuilder.toString();
    }

    @Override
    ByteArray compact() {
        ByteArray byteArray = this;
        assert (this.length > 0L) : "DynamicByteArray length cannot be less than 0 length = " + this.length;
        if (this.length == 0L) {
            byteArray = new SimpleByteArray(PhysicalConnection.EMPTY_BYTE_ARRAY);
        } else if (this.length < 131072L) {
            byteArray = new SimpleByteArray(this.get(0L, (int)this.length()));
        }
        return byteArray;
    }

    @Override
    int getUtf8Bytes(long l2, int n2, byte[] byArray, int n3, CharacterSet characterSet) throws SQLException {
        Object object;
        String string;
        int n4 = n2;
        switch (characterSet.getOracleId()) {
            case 1: {
                return this.getBytes(l2, n2, byArray, n3);
            }
            case 2: {
                return this.getBytesFrom1Byte(l2, n2, WE8DEC_TO_JAVA, byArray, n3);
            }
            case 31: {
                return this.getBytesFrom1Byte(l2, n2, ISO_LATIN_1_TO_JAVA, byArray, n3);
            }
            case 46: {
                return this.getBytesFrom1Byte(l2, n2, WE8ISO8859P15_TO_JAVA, byArray, n3);
            }
            case 178: {
                return this.getBytesFrom1Byte(l2, n2, WE8MSWIN1252_TO_JAVA, byArray, n3);
            }
            case 2000: {
                return this.getBytesFromAL16UTF16(l2, n2, byArray, n3);
            }
            case 2002: {
                return this.getBytesFromAL16UTF16LE(l2, n2, byArray, n3);
            }
            case 871: {
                return this.getBytes(l2, n2, byArray, n3);
            }
            case 873: {
                return this.getBytes(l2, n2, byArray, n3);
            }
        }
        assert (false) : "charSet: " + characterSet.toString();
        int n5 = (int)(l2 / this.blockSize);
        int n6 = (int)(l2 % this.blockSize);
        if (n2 <= (int)this.blockSize - n6) {
            string = characterSet.toString(this.blocks[n5], n6, n2);
        } else {
            object = this.get(l2, n2);
            string = characterSet.toString((byte[])object, 0, ((byte[])object).length);
        }
        object = CharacterSet.make(871);
        byte[] byArray2 = ((CharacterSet)object).convert(string);
        System.arraycopy(byArray2, 0, byArray, n3, byArray2.length);
        n4 = byArray2.length;
        return n4;
    }

    private int getBytesFrom1Byte(long l2, int n2, char[] cArray, byte[] byArray, int n3) throws SQLException {
        char[] cArray2 = this.getCharsForNewString(n2);
        int n4 = this.getCharsFrom1Byte(cArray2, l2, n2, cArray);
        return CharacterSet.convertJavaCharsToAL32UTF8Bytes(cArray2, 0, byArray, n3, n4);
    }

    private final int getBytesFromAL16UTF16(long l2, int n2, byte[] byArray, int n3) throws SQLException {
        char[] cArray = this.getCharsForNewString(n2 / 2);
        int n4 = this.getCharsFromAL16UTF16(cArray, l2, n2);
        return CharacterSet.convertJavaCharsToAL32UTF8Bytes(cArray, 0, byArray, n3, n4);
    }

    private final int getBytesFromAL16UTF16LE(long l2, int n2, byte[] byArray, int n3) throws SQLException {
        char[] cArray = this.getCharsForNewString(n2 / 2);
        int n4 = this.getCharsFromAL16UTF16LE(cArray, l2, n2);
        return CharacterSet.convertJavaCharsToAL32UTF8Bytes(cArray, 0, byArray, n3, n4);
    }

    private final int getBytes(long l2, int n2, byte[] byArray, int n3) throws SQLException {
        assert (l2 >= 0L && n2 >= 0 && this.length >= l2 + (long)n2) : "this.length: " + this.length + " offset: " + l2 + " lengthInBytes: " + n2;
        int n4 = (int)(l2 / this.blockSize);
        int n5 = (int)(l2 % this.blockSize);
        byte[] byArray2 = null;
        int n6 = n2;
        int n7 = 0;
        while (n6 > 0) {
            byArray2 = this.blocks[n4++];
            int n8 = Math.min(n5 + n6, byArray2.length);
            int n9 = n8 - n5;
            System.arraycopy(byArray2, n5, byArray, n3, n9);
            n6 -= n9;
            n3 += n9;
            n7 += n9;
            n5 = 0;
        }
        return n7;
    }

    static {
        final char[][] cArrayArray = new char[19][];
        AccessController.doPrivileged(new PrivilegedAction<Object>(){

            @Override
            public Object run() {
                JdbcCharacterConverters jdbcCharacterConverters = CharacterConverterJDBC.getInstance(31);
                cArrayArray[0] = DynamicByteArray.intToChar(((CharacterConverter1Byte)jdbcCharacterConverters).m_ucsChar);
                cArrayArray[1] = ((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharLevel1;
                cArrayArray[2] = ((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharLevel2;
                cArrayArray[3] = new char[]{(char)((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharReplacement};
                jdbcCharacterConverters = CharacterConverterJDBC.getInstance(2);
                cArrayArray[4] = DynamicByteArray.intToChar(((CharacterConverter1Byte)jdbcCharacterConverters).m_ucsChar);
                cArrayArray[5] = ((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharLevel1;
                cArrayArray[6] = ((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharLevel2;
                cArrayArray[7] = new char[]{(char)((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharReplacement};
                jdbcCharacterConverters = CharacterConverterJDBC.getInstance(178);
                cArrayArray[8] = DynamicByteArray.intToChar(((CharacterConverter1Byte)jdbcCharacterConverters).m_ucsChar);
                cArrayArray[9] = ((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharLevel1;
                cArrayArray[10] = ((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharLevel2;
                cArrayArray[11] = new char[]{(char)((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharReplacement};
                jdbcCharacterConverters = CharacterConverterJDBC.getInstance(1);
                cArrayArray[12] = ((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharLevel1;
                cArrayArray[13] = ((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharLevel2;
                cArrayArray[14] = new char[]{(char)((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharReplacement};
                jdbcCharacterConverters = CharacterConverterJDBC.getInstance(46);
                cArrayArray[15] = DynamicByteArray.intToChar(((CharacterConverter1Byte)jdbcCharacterConverters).m_ucsChar);
                cArrayArray[16] = ((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharLevel1;
                cArrayArray[17] = ((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharLevel2;
                cArrayArray[18] = new char[]{(char)((CharacterConverter1Byte)jdbcCharacterConverters).m_oraCharReplacement};
                return null;
            }
        });
        ISO_LATIN_1_TO_JAVA = cArrayArray[0];
        JAVA_TO_ISO_LATIN_1_LEVEL1 = cArrayArray[1];
        JAVA_TO_ISO_LATIN_1_LEVEL2 = cArrayArray[2];
        JAVA_TO_ISO_LATIN_1_REPLACEMENT = cArrayArray[3][0];
        WE8DEC_TO_JAVA = cArrayArray[4];
        JAVA_TO_WE8DEC_LEVEL1 = cArrayArray[5];
        JAVA_TO_WE8DEC_LEVEL2 = cArrayArray[6];
        JAVA_TO_WE8DEC_REPLACEMENT = cArrayArray[7][0];
        WE8MSWIN1252_TO_JAVA = cArrayArray[8];
        JAVA_TO_WE8MSWIN1252_LEVEL1 = cArrayArray[9];
        JAVA_TO_WE8MSWIN1252_LEVEL2 = cArrayArray[10];
        JAVA_TO_WE8MSWIN1252_REPLACEMENT = cArrayArray[11][0];
        JAVA_TO_US7ASCII_LEVEL1 = cArrayArray[12];
        JAVA_TO_US7ASCII_LEVEL2 = cArrayArray[13];
        JAVA_TO_US7ASCII_REPLACEMENT = cArrayArray[14][0];
        WE8ISO8859P15_TO_JAVA = cArrayArray[15];
        JAVA_TO_WE8ISO8859P15_LEVEL1 = cArrayArray[16];
        JAVA_TO_WE8ISO8859P15_LEVEL2 = cArrayArray[17];
        JAVA_TO_WE8ISO8859P15_REPLACEMENT = cArrayArray[18][0];
    }
}

