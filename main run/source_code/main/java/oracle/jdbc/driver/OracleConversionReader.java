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
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleConversionReader
extends Reader {
    static final int CHUNK_SIZE = 32768;
    DBConversion dbConversion;
    int conversion;
    InputStream istream;
    char[] buf;
    byte[] byteBuf;
    int pos;
    int count;
    int numUnconvertedBytes;
    boolean isClosed;
    boolean endOfStream;
    private short csform;
    int[] nbytes;

    public OracleConversionReader(DBConversion dBConversion, InputStream inputStream, int n2) throws SQLException {
        if (dBConversion == null || inputStream == null || n2 != 8 && n2 != 9) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        this.dbConversion = dBConversion;
        this.conversion = n2;
        this.istream = inputStream;
        this.count = 0;
        this.pos = 0;
        this.numUnconvertedBytes = 0;
        this.isClosed = false;
        this.nbytes = new int[1];
        if (n2 == 8) {
            this.byteBuf = new byte[16384];
            this.buf = new char[32768];
        } else if (n2 == 9) {
            this.byteBuf = new byte[32768];
            this.buf = new char[32768];
        }
    }

    public void setFormOfUse(short s2) {
        this.csform = s2;
    }

    @Override
    public int read(char[] cArray, int n2, int n3) throws IOException {
        this.ensureOpen();
        if (!this.needChars()) {
            return -1;
        }
        int n4 = n2;
        int n5 = n4 + Math.min(n3, cArray.length - n2);
        n4 += this.writeChars(cArray, n4, n5 - n4);
        while (n4 < n5 && this.needChars()) {
            n4 += this.writeChars(cArray, n4, n5 - n4);
        }
        return n4 - n2;
    }

    protected boolean needChars() throws IOException {
        this.ensureOpen();
        if (this.pos >= this.count) {
            if (!this.endOfStream) {
                try {
                    int n2 = this.istream.read(this.byteBuf, this.numUnconvertedBytes, this.byteBuf.length - this.numUnconvertedBytes);
                    if (n2 == -1) {
                        this.endOfStream = true;
                        if (this.numUnconvertedBytes != 0) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 55).fillInStackTrace();
                        }
                    }
                    if ((n2 += this.numUnconvertedBytes) > 0) {
                        switch (this.conversion) {
                            case 8: {
                                this.count = DBConversion.RAWBytesToHexChars(this.byteBuf, n2, this.buf);
                                break;
                            }
                            case 9: {
                                this.nbytes[0] = n2;
                                this.count = this.csform == 2 ? this.dbConversion.NCHARBytesToJavaChars(this.byteBuf, 0, this.buf, 0, this.nbytes, this.buf.length) : this.dbConversion.CHARBytesToJavaChars(this.byteBuf, 0, this.buf, 0, this.nbytes, this.buf.length);
                                this.numUnconvertedBytes = this.nbytes[0];
                                for (int i2 = 0; i2 < this.numUnconvertedBytes; ++i2) {
                                    this.byteBuf[i2] = this.byteBuf[n2 - this.numUnconvertedBytes + i2];
                                }
                                break;
                            }
                            default: {
                                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23).fillInStackTrace();
                            }
                        }
                        if (this.count > 0) {
                            this.pos = 0;
                            return true;
                        }
                    }
                }
                catch (SQLException sQLException) {
                    throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
                }
            }
            return false;
        }
        return true;
    }

    protected int writeChars(char[] cArray, int n2, int n3) {
        int n4 = Math.min(n3, this.count - this.pos);
        System.arraycopy(this.buf, this.pos, cArray, n2, n4);
        this.pos += n4;
        return n4;
    }

    @Override
    public boolean ready() throws IOException {
        this.ensureOpen();
        return this.pos < this.count;
    }

    @Override
    public void close() throws IOException {
        if (!this.isClosed) {
            this.isClosed = true;
            this.istream.close();
        }
    }

    void ensureOpen() throws IOException {
        try {
            if (this.isClosed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 57, null).fillInStackTrace();
            }
        }
        catch (SQLException sQLException) {
            throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
        }
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }
}

