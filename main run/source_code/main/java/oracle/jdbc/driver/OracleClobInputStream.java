/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.sql.SQLException;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleBufferedStream;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleClob;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CharacterSet;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleClobInputStream
extends OracleBufferedStream {
    protected long lobOffset;
    protected OracleClob clob;
    protected long markedByte;
    protected boolean endOfStream;
    protected char[] charBuf;
    protected boolean asciiStrictConversion;
    boolean internalClob = false;

    public OracleClobInputStream(OracleClob oracleClob, int n2) throws SQLException {
        this(oracleClob, n2, 1L, false);
    }

    public OracleClobInputStream(OracleClob oracleClob, int n2, boolean bl) throws SQLException {
        this(oracleClob, n2, 1L, bl);
    }

    public OracleClobInputStream(OracleClob oracleClob, int n2, long l2) throws SQLException {
        this(oracleClob, n2, l2, false);
    }

    public OracleClobInputStream(OracleClob oracleClob, int n2, long l2, boolean bl) throws SQLException {
        super(n2);
        if (oracleClob == null || n2 <= 0 || l2 < 1L) {
            throw new IllegalArgumentException();
        }
        this.lobOffset = l2;
        this.clob = oracleClob;
        this.markedByte = -1L;
        this.endOfStream = false;
        this.internalClob = bl;
        this.asciiStrictConversion = ((PhysicalConnection)oracleClob.getInternalConnection()).isStrictAsciiConversion;
    }

    @Override
    public boolean needBytes(int n2) throws IOException {
        this.ensureOpen();
        if (this.pos >= this.count) {
            if (!this.endOfStream) {
                try {
                    if (n2 > this.currentBufferSize || n2 == 0) {
                        this.currentBufferSize = Math.max(n2, this.initialBufferSize);
                        PhysicalConnection physicalConnection = (PhysicalConnection)this.clob.getInternalConnection();
                        try (Monitor.CloseableLock closeableLock = physicalConnection.acquireCloseableLock();){
                            this.resizableBuffer = physicalConnection.getByteBuffer(this.currentBufferSize);
                            this.charBuf = physicalConnection.getCharBuffer(this.currentBufferSize);
                        }
                    }
                    this.count = this.clob.getChars(this.lobOffset, this.currentBufferSize, this.charBuf);
                    CharacterSet.convertJavaCharsToASCIIBytes(this.charBuf, 0, this.resizableBuffer, 0, this.count, this.asciiStrictConversion);
                    if (this.count < this.currentBufferSize) {
                        this.endOfStream = true;
                    }
                    if (this.count > 0) {
                        this.pos = 0;
                        this.lobOffset += (long)this.count;
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

    protected void ensureOpen() throws IOException {
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
            throw new IllegalArgumentException(DatabaseError.findMessage(196, null));
        }
        this.markedByte = this.lobOffset - (long)this.count + (long)this.pos;
    }

    public void markInternal(int n2) {
    }

    @Override
    public void reset() throws IOException {
        this.ensureOpen();
        if (this.markedByte < 0L) {
            throw new IOException(DatabaseError.findMessage(195, null));
        }
        this.lobOffset = this.markedByte;
        this.pos = this.count;
        this.endOfStream = false;
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
                l4 = this.clob.length() - this.lobOffset + 1L;
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
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        try {
            PhysicalConnection physicalConnection = (PhysicalConnection)this.clob.getInternalConnection();
            try (Monitor.CloseableLock closeableLock = physicalConnection.acquireCloseableLock();){
                if (this.charBuf != null) {
                    physicalConnection.cacheBuffer(this.charBuf);
                    this.charBuf = null;
                }
                if (this.resizableBuffer != null) {
                    physicalConnection.cacheBuffer(this.resizableBuffer);
                    this.resizableBuffer = null;
                }
                this.currentBufferSize = 0;
            }
            if (this.clob != null && this.internalClob) {
                this.clob.free();
                this.internalClob = false;
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
    public int available() throws IOException {
        int n2;
        this.ensureOpen();
        if (this.clob.isActivePrefetch() && (n2 = this.clob.getPrefetchedDataSize()) > 0) {
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
        try {
            return this.clob.getInternalConnection();
        }
        catch (Exception exception) {
            return null;
        }
    }
}

