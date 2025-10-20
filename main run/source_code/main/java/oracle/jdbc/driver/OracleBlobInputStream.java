/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.sql.SQLException;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleBufferedStream;
import oracle.jdbc.internal.OracleBfile;
import oracle.jdbc.internal.OracleBlob;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleBlobInputStream
extends OracleBufferedStream {
    long lobOffset;
    OracleBlob blob;
    OracleBfile bfile;
    boolean isStreamForBlob;
    long markedByte;
    boolean endOfStream = false;
    long maxPosition = Long.MAX_VALUE;
    boolean internalBlob = false;

    public OracleBlobInputStream(OracleBlob oracleBlob, int n2) throws SQLException {
        this(oracleBlob, n2, 1L, false);
    }

    public OracleBlobInputStream(OracleBlob oracleBlob, int n2, long l2) throws SQLException {
        this(oracleBlob, n2, l2, false);
    }

    public OracleBlobInputStream(OracleBlob oracleBlob, int n2, boolean bl) throws SQLException {
        this(oracleBlob, n2, 1L, bl);
    }

    public OracleBlobInputStream(OracleBlob oracleBlob, int n2, long l2, boolean bl) throws SQLException {
        super(n2);
        if (oracleBlob == null || n2 <= 0 || l2 < 1L) {
            throw new IllegalArgumentException("Illegal Arguments");
        }
        this.isStreamForBlob = true;
        this.blob = oracleBlob;
        this.bfile = null;
        this.markedByte = -1L;
        this.lobOffset = l2;
        this.internalBlob = bl;
    }

    public OracleBlobInputStream(OracleBlob oracleBlob, int n2, long l2, long l3, boolean bl) throws SQLException {
        this(oracleBlob, n2, l2, bl);
        this.maxPosition = l2 + l3;
    }

    public OracleBlobInputStream(OracleBlob oracleBlob, int n2, long l2, long l3) throws SQLException {
        this(oracleBlob, n2, l2, false);
        this.maxPosition = l2 + l3;
    }

    public OracleBlobInputStream(OracleBfile oracleBfile, int n2) throws SQLException {
        this(oracleBfile, n2, 1L);
    }

    public OracleBlobInputStream(OracleBfile oracleBfile, int n2, long l2) throws SQLException {
        super(n2);
        if (oracleBfile == null || n2 <= 0 || l2 < 1L) {
            throw new IllegalArgumentException("Illegal Arguments");
        }
        this.isStreamForBlob = false;
        this.blob = null;
        this.bfile = oracleBfile;
        this.markedByte = -1L;
        this.lobOffset = l2;
    }

    @Override
    public boolean needBytes(int n2) throws IOException {
        this.ensureOpen();
        if (this.pos >= this.count) {
            if (!this.endOfStream) {
                if (n2 > this.currentBufferSize || n2 == 0) {
                    this.currentBufferSize = Math.max(n2, this.initialBufferSize);
                    this.resizableBuffer = new byte[this.currentBufferSize];
                }
                try {
                    int n3 = (long)this.currentBufferSize < this.maxPosition - this.lobOffset ? this.currentBufferSize : (int)(this.maxPosition - this.lobOffset);
                    this.count = this.isStreamForBlob ? this.blob.getBytes(this.lobOffset, n3, this.resizableBuffer) : this.bfile.getBytes(this.lobOffset, n3, this.resizableBuffer);
                    if (this.count < this.currentBufferSize) {
                        this.endOfStream = true;
                    }
                    if (this.count > 0) {
                        this.pos = 0;
                        this.lobOffset += (long)this.count;
                        if (this.lobOffset > this.maxPosition) {
                            this.endOfStream = true;
                        }
                        return true;
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

    void ensureOpen() throws IOException {
        try {
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 57, null).fillInStackTrace();
            }
        }
        catch (SQLException sQLException) {
            throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
        }
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        this.markedByte = this.lobOffset - (long)this.count + (long)this.pos;
    }

    public void markInternal(int n2) {
    }

    @Override
    public void reset() throws IOException {
        this.ensureOpen();
        if (this.markedByte < 0L) {
            throw new IOException("Mark invalid or stream not marked.");
        }
        this.lobOffset = this.markedByte;
        this.pos = this.count;
        this.endOfStream = false;
    }

    @Override
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        try {
            if (this.blob != null && this.internalBlob) {
                this.blob.free();
                this.internalBlob = false;
            }
        }
        catch (SQLException sQLException) {
            throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
        }
        finally {
            super.close();
        }
    }

    @Override
    public long skip(long l2) throws IOException {
        this.ensureOpen();
        long l3 = 0L;
        if ((long)(this.count - this.pos) >= l2) {
            this.pos = (int)((long)this.pos + l2);
            l3 += l2;
        } else {
            l3 += (long)(this.count - this.pos);
            this.pos = this.count;
            try {
                long l4 = 0L;
                l4 = this.isStreamForBlob ? this.blob.length() - this.lobOffset + 1L : this.bfile.length() - this.lobOffset + 1L;
                if (l4 >= l2 - l3) {
                    this.lobOffset += l2 - l3;
                    l3 += l2 - l3;
                } else {
                    this.lobOffset += l4;
                    l3 += l4;
                }
            }
            catch (SQLException sQLException) {
                throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
            }
        }
        return l3;
    }

    @Override
    public int available() throws IOException {
        int n2;
        this.ensureOpen();
        if (this.isStreamForBlob && this.blob.isActivePrefetch() && (n2 = this.blob.getPrefetchedDataSize()) > 0) {
            if (this.lobOffset == 1L) {
                return n2;
            }
            if (this.lobOffset - 1L < (long)n2) {
                return n2 - this.pos;
            }
        }
        return super.available();
    }

    @Override
    protected OracleConnection getConnectionDuringExceptionHandling() {
        OracleConnection oracleConnection = null;
        try {
            if (this.isStreamForBlob && this.blob != null) {
                oracleConnection = this.blob.getInternalConnection();
            } else if (!this.isStreamForBlob && this.bfile != null) {
                oracleConnection = this.bfile.getInternalConnection();
            }
        }
        catch (Exception exception) {
            oracleConnection = null;
        }
        return oracleConnection;
    }
}

