/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Arrays;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleBufferedStream;
import oracle.jdbc.driver.OracleConversionInputStream;
import oracle.jdbc.driver.OracleConversionInputStreamInternal;
import oracle.jdbc.driver.OracleConversionReader;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.util.RepConversion;
import oracle.sql.CharacterSet;
import oracle.sql.converter.CharacterSetMetaData;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CHARACTER_SET_CONVERSION})
public class DBConversion {
    public static final boolean DO_CONVERSION_WITH_REPLACEMENT = true;
    public static final short ORACLE8_PROD_VERSION = 8030;
    protected short serverNCharSetId;
    protected short serverCharSetId;
    protected short clientCharSetId;
    protected CharacterSet serverCharSet;
    protected CharacterSet serverNCharSet;
    protected CharacterSet clientCharSet;
    protected CharacterSet asciiCharSet;
    protected boolean isServerCharSetFixedWidth;
    protected boolean isServerNCharSetFixedWidth;
    protected int c2sNlsRatio;
    protected int s2cNlsRatio;
    protected int sMaxCharSize;
    protected int cMaxCharSize;
    protected int maxNCharSize;
    protected boolean isServerCSMultiByte;
    private boolean isStrictASCIIConversion = false;
    private boolean isQuickASCIIConversion = false;
    public static final short DBCS_CHARSET = -1;
    public static final short UCS2_CHARSET = -5;
    public static final short ASCII_CHARSET = 1;
    public static final short ISO_LATIN_1_CHARSET = 31;
    public static final short WE8ISO8859P15_CHARSET = 46;
    public static final short AL24UTFFSS_CHARSET = 870;
    public static final short UTF8_CHARSET = 871;
    public static final short AL32UTF8_CHARSET = 873;
    public static final short AL16UTF16_CHARSET = 2000;

    public DBConversion(short s2, short s3, short s4, boolean bl, boolean bl2) throws SQLException {
        this.isStrictASCIIConversion = bl;
        this.isQuickASCIIConversion = bl2;
        if (s3 != -1) {
            this.init(s2, s3, s4);
        }
    }

    public DBConversion(short s2, short s3, short s4) throws SQLException {
        this(s2, s3, s4, false, false);
    }

    void init(short s2, short s3, short s4) throws SQLException {
        switch (s3) {
            case -5: 
            case 1: 
            case 2: 
            case 31: 
            case 46: 
            case 178: 
            case 870: 
            case 871: 
            case 873: {
                break;
            }
            default: {
                DBConversion.unexpectedCharset(s3);
            }
        }
        this.serverCharSetId = s2;
        this.clientCharSetId = s3;
        this.serverCharSet = CharacterSet.make(this.serverCharSetId);
        this.serverNCharSetId = s4;
        this.serverNCharSet = CharacterSet.make(this.serverNCharSetId);
        this.clientCharSet = CharacterSet.make(this.clientCharSetId);
        this.c2sNlsRatio = CharacterSetMetaData.getRatio(s2, s3);
        this.s2cNlsRatio = CharacterSetMetaData.getRatio(s3, s2);
        this.sMaxCharSize = CharacterSetMetaData.getRatio(s2, 1);
        this.cMaxCharSize = CharacterSetMetaData.getRatio(s3, 1);
        this.maxNCharSize = CharacterSetMetaData.getRatio(s4, 1);
        this.findFixedWidthInfo();
    }

    void findFixedWidthInfo() throws SQLException {
        this.isServerCharSetFixedWidth = CharacterSetMetaData.isFixedWidth(this.serverCharSetId);
        this.isServerNCharSetFixedWidth = CharacterSetMetaData.isFixedWidth(this.serverNCharSetId);
        this.isServerCSMultiByte = this.sMaxCharSize > 1;
    }

    public short getServerCharSetId() {
        return this.serverCharSetId;
    }

    public short getNCharSetId() {
        return this.serverNCharSetId;
    }

    public boolean IsNCharFixedWith() {
        return this.serverNCharSetId == 2000;
    }

    public short getClientCharSet() {
        if (this.clientCharSetId == -1) {
            return this.serverCharSetId;
        }
        return this.clientCharSetId;
    }

    public CharacterSet getDbCharSetObj() {
        return this.serverCharSet;
    }

    public CharacterSet getDriverCharSetObj() {
        return this.clientCharSet;
    }

    public CharacterSet getDriverNCharSetObj() {
        return this.serverNCharSet;
    }

    CharacterSet getCharacterSet(short s2) {
        if (s2 == 2) {
            return this.getDriverNCharSetObj();
        }
        return this.getDriverCharSetObj();
    }

    public static final short findDriverCharSet(short n2, short s2) {
        int n3 = 0;
        switch (n2) {
            case 1: 
            case 2: 
            case 31: 
            case 46: 
            case 178: 
            case 873: {
                n3 = n2;
                break;
            }
            default: {
                n3 = s2 >= 8030 ? 871 : 870;
            }
        }
        return (short)n3;
    }

    public static final byte[] stringToDriverCharBytes(String string, short s2) throws SQLException {
        if (string == null) {
            return null;
        }
        byte[] byArray = null;
        switch (s2) {
            case -5: 
            case 2000: {
                byArray = CharacterSet.stringToAL16UTF16Bytes(string);
                break;
            }
            case 1: 
            case 2: {
                byArray = CharacterSet.stringToASCII(string);
                break;
            }
            case 870: 
            case 871: {
                byArray = CharacterSet.stringToUTF(string);
                break;
            }
            case 873: {
                byArray = CharacterSet.stringToAL32UTF8(string);
                break;
            }
            default: {
                DBConversion.unexpectedCharset(s2);
            }
        }
        return byArray;
    }

    public byte[] StringToCharBytes(String string) throws SQLException {
        if (string.length() == 0) {
            return null;
        }
        switch (this.clientCharSetId) {
            case -1: {
                return this.serverCharSet.convertWithReplacement(string);
            }
            case 2: 
            case 31: 
            case 46: 
            case 178: {
                return this.clientCharSet.convertWithReplacement(string);
            }
            case 1: {
                if (!this.isQuickASCIIConversion) break;
                byte[] byArray = new byte[string.length()];
                CharacterSet.convertJavaCharsToASCIIBytes(string.toCharArray(), 0, byArray, 0, string.length(), false);
                return byArray;
            }
        }
        return DBConversion.stringToDriverCharBytes(string, this.clientCharSetId);
    }

    public String CharBytesToString(byte[] byArray, int n2) throws SQLException {
        return this.CharBytesToString(byArray, n2, true);
    }

    public String CharBytesToString(byte[] byArray, int n2, boolean bl) throws SQLException {
        String string = null;
        if (byArray.length == 0) {
            return string;
        }
        switch (this.clientCharSetId) {
            case -5: {
                string = CharacterSet.AL16UTF16BytesToString(byArray, n2);
                break;
            }
            case 1: {
                string = new String(byArray, 0, 0, n2);
                break;
            }
            case 2: 
            case 31: 
            case 46: 
            case 178: {
                if (bl) {
                    string = this.clientCharSet.toStringWithReplacement(byArray, 0, n2);
                    break;
                }
                string = this.clientCharSet.toString(byArray, 0, n2);
                break;
            }
            case 870: 
            case 871: {
                string = CharacterSet.UTFToString(byArray, 0, n2, bl);
                break;
            }
            case 873: {
                string = CharacterSet.AL32UTF8ToString(byArray, 0, n2, bl);
                break;
            }
            case -1: {
                string = this.serverCharSet.toStringWithReplacement(byArray, 0, n2);
                break;
            }
            default: {
                DBConversion.unexpectedCharset(this.clientCharSetId);
            }
        }
        return string;
    }

    public String NCharBytesToString(byte[] byArray, int n2) throws SQLException {
        String string = null;
        if (this.clientCharSetId == -1) {
            string = this.serverNCharSet.toStringWithReplacement(byArray, 0, n2);
        } else {
            switch (this.serverNCharSetId) {
                case -5: 
                case 2000: {
                    string = CharacterSet.AL16UTF16BytesToString(byArray, n2);
                    break;
                }
                case 1: 
                case 2: {
                    string = new String(byArray, 0, 0, n2);
                    break;
                }
                case 31: 
                case 46: 
                case 178: {
                    string = this.serverNCharSet.toStringWithReplacement(byArray, 0, n2);
                    break;
                }
                case 870: 
                case 871: {
                    string = CharacterSet.UTFToString(byArray, 0, n2);
                    break;
                }
                case 873: {
                    string = CharacterSet.AL32UTF8ToString(byArray, 0, n2);
                    break;
                }
                case -1: {
                    string = this.serverCharSet.toStringWithReplacement(byArray, 0, n2);
                    break;
                }
                default: {
                    DBConversion.unexpectedCharset(this.clientCharSetId);
                }
            }
        }
        return string;
    }

    public int javaCharsToCHARBytes(char[] cArray, int n2, byte[] byArray) throws SQLException {
        return this.javaCharsToCHARBytes(cArray, n2, byArray, this.clientCharSetId);
    }

    public int javaCharsToCHARBytes(char[] cArray, int n2, byte[] byArray, int n3, int n4) throws SQLException {
        return this.javaCharsToCHARBytes(cArray, n2, byArray, n3, this.clientCharSetId, n4);
    }

    public byte[] javaCharsToCHARBytes(char[] cArray) throws SQLException {
        byte[] byArray = new byte[cArray.length * this.getMaxCharbyteSize()];
        return Arrays.copyOf(byArray, this.javaCharsToCHARBytes(cArray, cArray.length, byArray));
    }

    public int javaCharsToNCHARBytes(char[] cArray, int n2, byte[] byArray) throws SQLException {
        return this.javaCharsToCHARBytes(cArray, n2, byArray, this.serverNCharSetId);
    }

    public int javaCharsToNCHARBytes(char[] cArray, int n2, byte[] byArray, int n3, int n4) throws SQLException {
        return this.javaCharsToCHARBytes(cArray, n2, byArray, n3, this.serverNCharSetId, n4);
    }

    protected int javaCharsToCHARBytes(char[] cArray, int n2, byte[] byArray, short s2) throws SQLException {
        return this.javaCharsToCHARBytes(cArray, 0, byArray, 0, s2, n2);
    }

    protected int javaCharsToCHARBytes(char[] cArray, int n2, byte[] byArray, int n3, short s2, int n4) throws SQLException {
        int n5 = 0;
        switch (s2) {
            case -5: 
            case 2000: {
                n5 = CharacterSet.convertJavaCharsToAL16UTF16Bytes(cArray, n2, byArray, n3, n4);
                break;
            }
            case 2: 
            case 46: 
            case 178: {
                int[] nArray = new int[]{n4};
                this.clientCharSet.convertWithReplacement(cArray, n2, byArray, n3, nArray);
                n5 = nArray[0];
                break;
            }
            case 1: {
                n5 = CharacterSet.convertJavaCharsToASCIIBytes(cArray, n2, byArray, n3, n4, this.isStrictASCIIConversion);
                break;
            }
            case 31: {
                n5 = CharacterSet.convertJavaCharsToISOLATIN1Bytes(cArray, n2, byArray, n3, n4);
                break;
            }
            case 870: 
            case 871: {
                n5 = CharacterSet.convertJavaCharsToUTFBytes(cArray, n2, byArray, n3, n4);
                break;
            }
            case 873: {
                n5 = CharacterSet.convertJavaCharsToAL32UTF8Bytes(cArray, n2, byArray, n3, n4);
                break;
            }
            case -1: {
                n5 = this.javaCharsToDbCsBytes(cArray, n2, byArray, n3, n4);
                break;
            }
            default: {
                DBConversion.unexpectedCharset(this.clientCharSetId);
            }
        }
        return n5;
    }

    public int CHARBytesToJavaChars(byte[] byArray, int n2, char[] cArray, int n3, int[] nArray, int n4, boolean bl) throws SQLException {
        if (bl) {
            return this.NCHARBytesToJavaChars(byArray, n2, cArray, n3, nArray, n4);
        }
        return this.CHARBytesToJavaChars(byArray, n2, cArray, n3, nArray, n4);
    }

    public int CHARBytesToJavaChars(byte[] byArray, int n2, char[] cArray, int n3, int[] nArray, int n4) throws SQLException {
        return DBConversion._CHARBytesToJavaChars(byArray, n2, cArray, n3, this.clientCharSetId, nArray, n4, this.serverCharSet, this.serverNCharSet, this.clientCharSet, false);
    }

    public int NCHARBytesToJavaChars(byte[] byArray, int n2, char[] cArray, int n3, int[] nArray, int n4) throws SQLException {
        return DBConversion._CHARBytesToJavaChars(byArray, n2, cArray, n3, this.serverNCharSetId, nArray, n4, this.serverCharSet, this.serverNCharSet, this.clientCharSet, true);
    }

    static final int _CHARBytesToJavaChars(byte[] byArray, int n2, char[] cArray, int n3, short s2, int[] nArray, int n4, CharacterSet characterSet, CharacterSet characterSet2, CharacterSet characterSet3, boolean bl) throws SQLException {
        int n5 = 0;
        int n6 = 0;
        switch (s2) {
            case -5: 
            case 2000: {
                n6 = nArray[0] - nArray[0] % 2;
                if (n4 > cArray.length - n3) {
                    n4 = cArray.length - n3;
                }
                if (n4 * 2 < n6) {
                    n6 = n4 * 2;
                }
                n5 = CharacterSet.convertAL16UTF16BytesToJavaChars(byArray, n2, cArray, n3, n6, true);
                nArray[0] = nArray[0] - n6;
                break;
            }
            case 1: {
                n6 = nArray[0];
                if (n4 > cArray.length - n3) {
                    n4 = cArray.length - n3;
                }
                if (n4 < n6) {
                    n6 = n4;
                }
                n5 = CharacterSet.convertASCIIBytesToJavaChars(byArray, n2, cArray, n3, n6);
                nArray[0] = nArray[0] - n6;
                break;
            }
            case 31: 
            case 46: 
            case 178: {
                n6 = nArray[0];
                n5 = characterSet.toCharWithReplacement(byArray, n2, cArray, n3, n6);
                nArray[0] = nArray[0] - n5;
                break;
            }
            case 870: 
            case 871: {
                if (n4 > cArray.length - n3) {
                    n4 = cArray.length - n3;
                }
                n5 = CharacterSet.convertUTFBytesToJavaChars(byArray, n2, cArray, n3, nArray, true, n4);
                break;
            }
            case 873: {
                if (n4 > cArray.length - n3) {
                    n4 = cArray.length - n3;
                }
                n5 = CharacterSet.convertAL32UTF8BytesToJavaChars(byArray, n2, cArray, n3, nArray, true, n4);
                break;
            }
            case -1: {
                DBConversion.unexpectedCharset((short)-1);
                break;
            }
            default: {
                String string;
                char[] cArray2;
                int n7;
                CharacterSet characterSet4 = characterSet3;
                if (bl) {
                    characterSet4 = characterSet2;
                }
                if ((n7 = (cArray2 = (string = characterSet4.toStringWithReplacement(byArray, n2, nArray[0])).toCharArray()).length) > n4) {
                    n7 = n4;
                }
                n5 = n7;
                nArray[0] = nArray[0] - n7;
                System.arraycopy(cArray2, 0, cArray, n3, n7);
            }
        }
        return n5;
    }

    public byte[] asciiBytesToCHARBytes(byte[] byArray) {
        byte[] byArray2 = null;
        switch (this.clientCharSetId) {
            case -5: {
                byArray2 = new byte[byArray.length * 2];
                int n2 = 0;
                for (int i2 = 0; i2 < byArray.length; ++i2) {
                    byArray2[n2++] = 0;
                    byArray2[n2++] = byArray[i2];
                }
                break;
            }
            case -1: {
                if (this.asciiCharSet == null) {
                    this.asciiCharSet = CharacterSet.make(1);
                }
                try {
                    byArray2 = this.serverCharSet.convert(this.asciiCharSet, byArray, 0, byArray.length);
                }
                catch (SQLException sQLException) {}
                break;
            }
            default: {
                byArray2 = byArray;
            }
        }
        return byArray2;
    }

    public int javaCharsToDbCsBytes(char[] cArray, int n2, byte[] byArray) throws SQLException {
        int n3 = this.javaCharsToDbCsBytes(cArray, 0, byArray, 0, n2);
        return n3;
    }

    public int javaCharsToDbCsBytes(char[] cArray, int n2, byte[] byArray, int n3, int n4) throws SQLException {
        int n5 = 0;
        DBConversion.catchCharsLen(cArray, n2, n4);
        String string = new String(cArray, n2, n4);
        byte[] byArray2 = this.serverCharSet.convertWithReplacement(string);
        string = null;
        if (byArray2 != null) {
            n5 = byArray2.length;
            DBConversion.catchBytesLen(byArray, n3, n5);
            System.arraycopy(byArray2, 0, byArray, n3, n5);
            byArray2 = null;
        }
        return n5;
    }

    public static final int javaCharsToUcs2Bytes(char[] cArray, int n2, byte[] byArray) throws SQLException {
        int n3 = DBConversion.javaCharsToUcs2Bytes(cArray, 0, byArray, 0, n2);
        return n3;
    }

    public static final int javaCharsToUcs2Bytes(char[] cArray, int n2, byte[] byArray, int n3, int n4) throws SQLException {
        DBConversion.catchCharsLen(cArray, n2, n4);
        DBConversion.catchBytesLen(byArray, n3, n4 * 2);
        int n5 = n4 + n2;
        int n6 = n3;
        for (int i2 = n2; i2 < n5; ++i2) {
            byArray[n6++] = (byte)(cArray[i2] >> 8 & 0xFF);
            byArray[n6++] = (byte)(cArray[i2] & 0xFF);
        }
        return n6 - n3;
    }

    public static final int ucs2BytesToJavaChars(byte[] byArray, int n2, char[] cArray) throws SQLException {
        return CharacterSet.AL16UTF16BytesToJavaChars(byArray, n2, cArray);
    }

    public static final byte[] stringToAsciiBytes(String string) {
        return CharacterSet.stringToASCII(string);
    }

    public static final int asciiBytesToJavaChars(byte[] byArray, int n2, char[] cArray) throws SQLException {
        return CharacterSet.convertASCIIBytesToJavaChars(byArray, 0, cArray, 0, n2);
    }

    public static final int javaCharsToAsciiBytes(char[] cArray, int n2, byte[] byArray) throws SQLException {
        return CharacterSet.convertJavaCharsToASCIIBytes(cArray, 0, byArray, 0, n2);
    }

    static final int asciiBytesToUTF16Bytes(byte[] byArray, int n2, byte[] byArray2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("Number of bytes to convert is negative:" + n2);
        }
        int n3 = Math.min(n2 * 2, byArray2.length & 0xFFFFFFFE);
        for (int i2 = 0; i2 < n3; i2 += 2) {
            byArray2[i2] = 0;
            byArray2[i2 + 1] = byArray[i2 >>> 1];
        }
        return n3;
    }

    public static final boolean isCharSetMultibyte(short s2) {
        switch (s2) {
            case 1: 
            case 31: 
            case 46: {
                return false;
            }
            case -5: 
            case -1: 
            case 870: 
            case 871: 
            case 873: {
                return true;
            }
        }
        return false;
    }

    public int getMaxCharbyteSize() {
        return this._getMaxCharbyteSize(this.clientCharSetId);
    }

    public int getMaxNCharbyteSize() {
        return this._getMaxCharbyteSize(this.serverNCharSetId);
    }

    public int _getMaxCharbyteSize(short s2) {
        switch (s2) {
            case 1: {
                return 1;
            }
            case 31: 
            case 46: {
                return 1;
            }
            case 870: 
            case 871: {
                return 3;
            }
            case -5: 
            case 2000: {
                return 2;
            }
            case -1: {
                return 4;
            }
            case 873: {
                return 4;
            }
        }
        return 1;
    }

    public boolean isUcs2CharSet() {
        return this.clientCharSetId == -5;
    }

    public static final int RAWBytesToHexChars(byte[] byArray, int n2, char[] cArray) {
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            cArray[n3++] = (char)RepConversion.nibbleToHex((byte)(byArray[i2] >> 4 & 0xF));
            cArray[n3++] = (char)RepConversion.nibbleToHex((byte)(byArray[i2] & 0xF));
        }
        return n3;
    }

    public final int hexDigit2Nibble(char c2) throws SQLException {
        int n2 = Character.digit(c2, 16);
        if (n2 == -1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 59, "Invalid hex digit: " + c2).fillInStackTrace();
        }
        return n2;
    }

    public final byte[] hexString2Bytes(String string) throws SQLException {
        int n2 = string.length();
        char[] cArray = new char[n2];
        string.getChars(0, n2, cArray, 0);
        return this.hexChars2Bytes(cArray, 0, n2);
    }

    public final byte[] hexChars2Bytes(char[] cArray, int n2, int n3) throws SQLException {
        byte[] byArray;
        int n4 = 0;
        int n5 = n2;
        if (n3 == 0) {
            return new byte[0];
        }
        if (n3 % 2 > 0) {
            byArray = new byte[(n3 + 1) / 2];
            byArray[n4++] = (byte)this.hexDigit2Nibble(cArray[n5++]);
        } else {
            byArray = new byte[n3 / 2];
        }
        while (n4 < byArray.length) {
            byArray[n4] = (byte)(this.hexDigit2Nibble(cArray[n5++]) << 4 | this.hexDigit2Nibble(cArray[n5++]));
            ++n4;
        }
        return byArray;
    }

    public InputStream ConvertStream(InputStream inputStream, int n2) {
        return new OracleConversionInputStream(this, inputStream, n2);
    }

    public InputStream ConvertStream(InputStream inputStream, int n2, int n3) {
        return new OracleConversionInputStream(this, inputStream, n2, n3);
    }

    public InputStream ConvertStreamInternal(InputStream inputStream, int n2, int n3) {
        return new OracleConversionInputStreamInternal(this, inputStream, n2, n3);
    }

    public InputStream ConvertStream(Reader reader, int n2, int n3, short s2) {
        OracleConversionInputStream oracleConversionInputStream = new OracleConversionInputStream(this, reader, n2, n3, s2);
        return oracleConversionInputStream;
    }

    public InputStream ConvertStreamInternal(Reader reader, int n2, int n3, short s2) {
        OracleConversionInputStreamInternal oracleConversionInputStreamInternal = new OracleConversionInputStreamInternal(this, reader, n2, n3, s2);
        return oracleConversionInputStreamInternal;
    }

    public Reader ConvertCharacterStream(InputStream inputStream, int n2) throws SQLException {
        return new OracleConversionReader(this, inputStream, n2);
    }

    public Reader ConvertCharacterStream(InputStream inputStream, int n2, short s2) throws SQLException {
        OracleConversionReader oracleConversionReader = new OracleConversionReader(this, inputStream, n2);
        oracleConversionReader.setFormOfUse(s2);
        return oracleConversionReader;
    }

    public InputStream CharsToStream(char[] cArray, int n2, int n3, int n4) throws SQLException {
        if (n4 == 10) {
            return new AsciiStream(cArray, n2, n3);
        }
        if (n4 == 11) {
            return new UnicodeStream(cArray, n2, n3);
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 39, "unknownConversion").fillInStackTrace();
    }

    static final void unexpectedCharset(short s2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(35, "DBConversion").fillInStackTrace();
    }

    protected static final void catchBytesLen(byte[] byArray, int n2, int n3) throws SQLException {
        if (n2 + n3 > byArray.length) {
            throw (SQLException)DatabaseError.createSqlException(39, "catchBytesLen").fillInStackTrace();
        }
    }

    protected static final void catchCharsLen(char[] cArray, int n2, int n3) throws SQLException {
        if (n2 + n3 > cArray.length) {
            throw (SQLException)DatabaseError.createSqlException(39, "catchCharsLen").fillInStackTrace();
        }
    }

    public static final int getUtfLen(char c2) {
        int n2 = 0;
        n2 = (c2 & 0xFF80) == 0 ? 1 : ((c2 & 0xF800) == 0 ? 2 : 3);
        return n2;
    }

    int encodedByteLength(String string, boolean bl) throws SQLException {
        int n2 = 0;
        if (string != null && (n2 = string.length()) != 0) {
            n2 = bl ? (this.isServerNCharSetFixedWidth ? n2 * this.maxNCharSize : this.serverNCharSet.encodedByteLength(string)) : (this.isServerCharSetFixedWidth ? n2 * this.sMaxCharSize : this.serverCharSet.encodedByteLength(string));
        }
        return n2;
    }

    int encodedByteLength(char[] cArray, boolean bl) throws SQLException {
        int n2 = 0;
        if (cArray != null && (n2 = cArray.length) != 0) {
            n2 = bl ? (this.isServerNCharSetFixedWidth ? n2 * this.maxNCharSize : this.serverNCharSet.encodedByteLength(cArray)) : (this.isServerCharSetFixedWidth ? n2 * this.sMaxCharSize : this.serverCharSet.encodedByteLength(cArray));
        }
        return n2;
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    class UnicodeStream
    extends OracleBufferedStream {
        UnicodeStream(char[] cArray, int n2, int n3) {
            super(n3);
            this.currentBufferSize = this.initialBufferSize;
            this.resizableBuffer = new byte[this.currentBufferSize];
            int n4 = n2;
            int n5 = 0;
            while (n5 < n3) {
                char c2 = cArray[n4++];
                this.resizableBuffer[n5++] = (byte)(c2 >> 8 & 0xFF);
                this.resizableBuffer[n5++] = (byte)(c2 & 0xFF);
            }
            this.count = n3;
        }

        @Override
        public boolean needBytes() {
            return !this.closed && this.pos < this.count;
        }

        @Override
        public boolean needBytes(int n2) {
            return !this.closed && this.pos < this.count;
        }
    }

    class AsciiStream
    extends OracleBufferedStream {
        AsciiStream(char[] cArray, int n2, int n3) {
            super(n3);
            this.currentBufferSize = this.initialBufferSize;
            this.resizableBuffer = new byte[this.currentBufferSize];
            if (DBConversion.this.serverCharSetId == 1 || !DBConversion.this.isStrictASCIIConversion) {
                int n4 = n2;
                for (int i2 = 0; i2 < n3; ++i2) {
                    this.resizableBuffer[i2] = (byte)cArray[n4++];
                }
            } else {
                if (DBConversion.this.asciiCharSet == null) {
                    DBConversion.this.asciiCharSet = CharacterSet.make(1);
                }
                this.resizableBuffer = DBConversion.this.asciiCharSet.convertWithReplacement(new String(cArray, n2, n3));
            }
            this.count = n3;
        }

        @Override
        public boolean needBytes() {
            return !this.closed && this.pos < this.count;
        }

        @Override
        public boolean needBytes(int n2) {
            return !this.closed && this.pos < this.count;
        }
    }
}

