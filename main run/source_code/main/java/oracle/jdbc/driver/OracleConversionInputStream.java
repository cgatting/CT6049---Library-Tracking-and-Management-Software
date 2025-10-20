/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleBufferedStream;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CharacterSet;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleConversionInputStream
extends OracleBufferedStream {
    static final int CHUNK_SIZE = 32768;
    DBConversion converter;
    int conversion;
    InputStream istream;
    Reader reader;
    byte[] convbuf;
    int convbufSize;
    char[] javaChars;
    int javaCharsSize;
    int maxSize;
    int totalSize;
    int numUnconvertedBytes;
    boolean endOfStream;
    private short csform;
    int[] nbytes;

    public OracleConversionInputStream(DBConversion dBConversion, InputStream inputStream, int n2) {
        this(dBConversion, inputStream, n2, 1);
    }

    public OracleConversionInputStream(DBConversion dBConversion, InputStream inputStream, int n2, short s2) {
        super(32768);
        this.istream = inputStream;
        this.conversion = n2;
        this.converter = dBConversion;
        this.maxSize = 0;
        this.totalSize = 0;
        this.numUnconvertedBytes = 0;
        this.endOfStream = false;
        this.nbytes = new int[1];
        this.csform = s2;
        this.currentBufferSize = this.initialBufferSize;
        switch (n2) {
            case 0: {
                this.javaCharsSize = 32768;
                this.convbufSize = 32768;
                break;
            }
            case 1: {
                this.convbufSize = 16384;
                this.javaCharsSize = 16384;
                break;
            }
            case 2: {
                this.convbufSize = 16384;
                this.javaCharsSize = 32768;
                break;
            }
            case 3: {
                this.convbufSize = 8192;
                this.javaCharsSize = 16384;
                break;
            }
            case 4: {
                int n3 = 32768 / this.converter.getMaxCharbyteSize();
                this.convbufSize = n3 * 2;
                this.javaCharsSize = n3;
                break;
            }
            case 5: {
                if (this.converter.isUcs2CharSet()) {
                    this.convbufSize = 16384;
                    this.javaCharsSize = 16384;
                    break;
                }
                this.convbufSize = 32768;
                this.javaCharsSize = 32768;
                break;
            }
            case 7: {
                int n4;
                this.javaCharsSize = n4 = 32768 / (this.csform == 2 ? this.converter.getMaxNCharbyteSize() : this.converter.getMaxCharbyteSize());
                this.convbufSize = 0;
                break;
            }
            case 13: {
                this.convbufSize = 16384;
                this.javaCharsSize = 0;
                break;
            }
            case 14: {
                this.javaCharsSize = 16384;
                this.convbufSize = 0;
                break;
            }
            case 15: {
                this.convbufSize = this.javaCharsSize = 32768 / (this.csform == 2 ? this.converter.getMaxNCharbyteSize() : this.converter.sMaxCharSize);
                break;
            }
            case 16: {
                this.javaCharsSize = 32768 / (this.csform == 2 ? this.converter.getMaxNCharbyteSize() : this.converter.sMaxCharSize);
                this.convbufSize = 0;
                break;
            }
            default: {
                this.convbufSize = 32768;
                this.javaCharsSize = 32768;
            }
        }
    }

    public OracleConversionInputStream(DBConversion dBConversion, InputStream inputStream, int n2, int n3) {
        this(dBConversion, inputStream, n2, 1);
        this.maxSize = n3;
        this.totalSize = 0;
    }

    public OracleConversionInputStream(DBConversion dBConversion, Reader reader, int n2, int n3, short s2) {
        this(dBConversion, (InputStream)null, n2, s2);
        this.reader = reader;
        this.maxSize = n3;
        this.totalSize = 0;
    }

    public void allocateBuffers() {
        if (this.resizableBuffer == null) {
            this.resizableBuffer = new byte[this.currentBufferSize];
            this.javaChars = new char[this.javaCharsSize];
            if (this.convbufSize > 0) {
                this.convbuf = new byte[this.convbufSize];
            }
        }
    }

    void deallocateBuffers() {
        this.convbuf = null;
        this.javaChars = null;
        this.resizableBuffer = null;
    }

    public void setFormOfUse(short s2) {
        this.csform = s2;
    }

    @Override
    public boolean needBytes(int n2) throws IOException {
        return this.needBytes();
    }

    @Override
    public boolean needBytes() throws IOException {
        if (this.closed) {
            return false;
        }
        if (this.pos < this.count) {
            return true;
        }
        if (this.istream != null) {
            return this.needBytesFromStream();
        }
        if (this.reader != null) {
            return this.needBytesFromReader();
        }
        return false;
    }

    public boolean needBytesFromReader() throws IOException {
        try {
            int n2 = 0;
            n2 = this.maxSize == 0 ? this.javaCharsSize : Math.min(this.maxSize - this.totalSize, this.javaCharsSize);
            if (n2 <= 0) {
                this.deallocateBuffers();
                return false;
            }
            this.allocateBuffers();
            int n3 = this.reader.read(this.javaChars, 0, n2);
            if (n3 == -1) {
                this.deallocateBuffers();
                return false;
            }
            this.totalSize += n3;
            switch (this.conversion) {
                case 7: {
                    if (this.csform == 2) {
                        this.count = this.converter.javaCharsToNCHARBytes(this.javaChars, n3, this.resizableBuffer);
                        break;
                    }
                    this.count = this.converter.javaCharsToCHARBytes(this.javaChars, n3, this.resizableBuffer);
                    break;
                }
                case 14: {
                    this.count = CharacterSet.convertJavaCharsToAL16UTF16Bytes(this.javaChars, 0, this.resizableBuffer, 0, n3);
                    break;
                }
                case 16: {
                    if (this.csform == 2) {
                        this.count = this.converter.javaCharsToNCHARBytes(this.javaChars, n3, this.resizableBuffer);
                        break;
                    }
                    this.count = this.converter.javaCharsToDbCsBytes(this.javaChars, n3, this.resizableBuffer);
                    break;
                }
                default: {
                    System.arraycopy(this.convbuf, 0, this.resizableBuffer, 0, n3);
                    this.count = n3;
                    break;
                }
            }
        }
        catch (SQLException sQLException) {
            throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
        }
        this.pos = 0;
        return true;
    }

    public boolean needBytesFromStream() throws IOException {
        if (!this.endOfStream) {
            try {
                int n2 = 0;
                n2 = this.maxSize == 0 ? this.convbufSize : Math.min(this.maxSize - this.totalSize, this.convbufSize);
                int n3 = 0;
                if (n2 <= 0) {
                    this.endOfStream = true;
                    this.istream.close();
                    if (this.numUnconvertedBytes != 0) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 55).fillInStackTrace();
                    }
                } else {
                    this.allocateBuffers();
                    n3 = this.istream.read(this.convbuf, this.numUnconvertedBytes, n2 - this.numUnconvertedBytes);
                }
                if (n3 == -1) {
                    this.endOfStream = true;
                    this.istream.close();
                    if (this.numUnconvertedBytes != 0) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 55).fillInStackTrace();
                    }
                } else {
                    this.totalSize += (n3 += this.numUnconvertedBytes);
                }
                if (n3 <= 0) {
                    this.deallocateBuffers();
                    return false;
                }
                switch (this.conversion) {
                    case 0: {
                        this.nbytes[0] = n3;
                        int n4 = this.converter.CHARBytesToJavaChars(this.convbuf, 0, this.javaChars, 0, this.nbytes, this.javaCharsSize);
                        this.numUnconvertedBytes = this.nbytes[0];
                        for (int i2 = 0; i2 < this.numUnconvertedBytes; ++i2) {
                            this.convbuf[i2] = this.convbuf[n3 - this.numUnconvertedBytes];
                        }
                        this.count = DBConversion.javaCharsToAsciiBytes(this.javaChars, n4, this.resizableBuffer);
                        break;
                    }
                    case 1: {
                        this.nbytes[0] = n3;
                        int n5 = this.converter.CHARBytesToJavaChars(this.convbuf, 0, this.javaChars, 0, this.nbytes, this.javaCharsSize);
                        this.numUnconvertedBytes = this.nbytes[0];
                        for (int i3 = 0; i3 < this.numUnconvertedBytes; ++i3) {
                            this.convbuf[i3] = this.convbuf[n3 - this.numUnconvertedBytes];
                        }
                        this.count = DBConversion.javaCharsToUcs2Bytes(this.javaChars, n5, this.resizableBuffer);
                        break;
                    }
                    case 2: {
                        int n6 = DBConversion.RAWBytesToHexChars(this.convbuf, n3, this.javaChars);
                        this.count = DBConversion.javaCharsToAsciiBytes(this.javaChars, n6, this.resizableBuffer);
                        break;
                    }
                    case 3: {
                        int n7 = DBConversion.RAWBytesToHexChars(this.convbuf, n3, this.javaChars);
                        this.count = DBConversion.javaCharsToUcs2Bytes(this.javaChars, n7, this.resizableBuffer);
                        break;
                    }
                    case 4: {
                        int n8 = DBConversion.ucs2BytesToJavaChars(this.convbuf, n3, this.javaChars);
                        this.count = this.converter.javaCharsToCHARBytes(this.javaChars, n8, this.resizableBuffer);
                        break;
                    }
                    case 12: {
                        int n9 = DBConversion.ucs2BytesToJavaChars(this.convbuf, n3, this.javaChars);
                        this.count = DBConversion.javaCharsToAsciiBytes(this.javaChars, n9, this.resizableBuffer);
                        break;
                    }
                    case 5: {
                        DBConversion.asciiBytesToJavaChars(this.convbuf, n3, this.javaChars);
                        this.count = this.converter.javaCharsToCHARBytes(this.javaChars, n3, this.resizableBuffer);
                        break;
                    }
                    case 13: {
                        this.count = DBConversion.asciiBytesToUTF16Bytes(this.convbuf, n3, this.resizableBuffer);
                        break;
                    }
                    case 15: {
                        DBConversion.asciiBytesToJavaChars(this.convbuf, n3, this.javaChars);
                        this.count = this.converter.javaCharsToDbCsBytes(this.javaChars, n3, this.resizableBuffer);
                        break;
                    }
                    default: {
                        System.arraycopy(this.convbuf, 0, this.resizableBuffer, 0, n3);
                        this.count = n3;
                        break;
                    }
                }
            }
            catch (SQLException sQLException) {
                throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
            }
            this.pos = 0;
            return true;
        }
        this.deallocateBuffers();
        return false;
    }

    @Override
    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }
}

