/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleClob;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CLOB;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleClobReader
extends Reader {
    OracleClob clob;
    DBConversion dbConversion;
    long lobOffset;
    long markedChar;
    char[] resizableBuffer;
    int initialBufferSize;
    int currentBufferSize;
    int pos;
    int count;
    long maxPosition = Long.MAX_VALUE;
    boolean isClosed;
    boolean endOfStream;

    public OracleClobReader(CLOB cLOB) throws SQLException {
        this((OracleClob)cLOB);
    }

    public OracleClobReader(OracleClob oracleClob) throws SQLException {
        this(oracleClob, ((PhysicalConnection)oracleClob.getInternalConnection()).getDefaultStreamChunkSize() / 3);
    }

    public OracleClobReader(CLOB cLOB, int n2) throws SQLException {
        this((OracleClob)cLOB, n2);
    }

    public OracleClobReader(OracleClob oracleClob, int n2) throws SQLException {
        this(oracleClob, n2, 1L);
    }

    public OracleClobReader(CLOB cLOB, int n2, long l2) throws SQLException {
        this((OracleClob)cLOB, n2, l2);
    }

    public OracleClobReader(OracleClob oracleClob, int n2, long l2) throws SQLException {
        if (oracleClob == null || n2 <= 0 || oracleClob.getInternalConnection() == null || l2 < 1L) {
            throw new IllegalArgumentException();
        }
        this.dbConversion = ((PhysicalConnection)oracleClob.getInternalConnection()).conversion;
        this.clob = oracleClob;
        this.lobOffset = l2;
        this.markedChar = -1L;
        this.resizableBuffer = null;
        this.initialBufferSize = n2;
        this.currentBufferSize = 0;
        this.count = 0;
        this.pos = 0;
        this.isClosed = false;
    }

    public OracleClobReader(CLOB cLOB, int n2, long l2, long l3) throws SQLException {
        this((OracleClob)cLOB, n2, l2, l3);
    }

    public OracleClobReader(OracleClob oracleClob, int n2, long l2, long l3) throws SQLException {
        this(oracleClob, n2, l2);
        this.maxPosition = l2 + l3;
    }

    @Override
    public int read(char[] cArray, int n2, int n3) throws IOException {
        this.ensureOpen();
        int n4 = n2;
        int n5 = n4 + Math.min(n3, cArray.length - n2);
        if (!this.needChars(n5 - n4)) {
            return -1;
        }
        n4 += this.writeChars(cArray, n4, n5 - n4);
        while (n4 < n5 && this.needChars(n5 - n4)) {
            n4 += this.writeChars(cArray, n4, n5 - n4);
        }
        return n4 - n2;
    }

    protected boolean needChars(int n2) throws IOException {
        this.ensureOpen();
        if (this.pos >= this.count) {
            if (!this.endOfStream) {
                try {
                    if (n2 > this.currentBufferSize) {
                        this.currentBufferSize = Math.max(n2, this.initialBufferSize);
                        PhysicalConnection physicalConnection = (PhysicalConnection)this.clob.getInternalConnection();
                        try (Monitor.CloseableLock closeableLock = physicalConnection.acquireCloseableLock();){
                            this.resizableBuffer = physicalConnection.getCharBuffer(this.currentBufferSize);
                        }
                    }
                    int n3 = this.currentBufferSize;
                    if (this.maxPosition - this.lobOffset < (long)this.currentBufferSize) {
                        n3 = (int)(this.maxPosition - this.lobOffset);
                    }
                    this.count = this.clob.getChars(this.lobOffset, n3, this.resizableBuffer);
                    if (this.count < this.currentBufferSize) {
                        this.endOfStream = true;
                    }
                    if (this.count > 0) {
                        this.pos = 0;
                        this.lobOffset += (long)this.count;
                        if (this.lobOffset >= this.maxPosition) {
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

    protected int writeChars(char[] cArray, int n2, int n3) {
        int n4 = Math.min(n3, this.count - this.pos);
        System.arraycopy(this.resizableBuffer, this.pos, cArray, n2, n4);
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
        if (this.isClosed) {
            return;
        }
        try {
            this.isClosed = true;
            PhysicalConnection physicalConnection = (PhysicalConnection)this.clob.getInternalConnection();
            try (Monitor.CloseableLock closeableLock = physicalConnection.acquireCloseableLock();){
                if (this.resizableBuffer != null) {
                    physicalConnection.cacheBuffer(this.resizableBuffer);
                    this.resizableBuffer = null;
                }
            }
        }
        catch (SQLException sQLException) {
            throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
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

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int n2) throws IOException {
        if (n2 < 0) {
            throw new IllegalArgumentException(DatabaseError.findMessage(195, null));
        }
        this.markedChar = this.lobOffset - (long)this.count + (long)this.pos;
    }

    @Override
    public void reset() throws IOException {
        this.ensureOpen();
        if (this.markedChar < 0L) {
            throw new IOException(DatabaseError.findMessage(195, null));
        }
        this.lobOffset = this.markedChar;
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
                long l4 = this.clob.length() - this.lobOffset + 1L;
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

    protected OracleConnection getConnectionDuringExceptionHandling() {
        try {
            return this.clob.getInternalConnection();
        }
        catch (Exception exception) {
            return null;
        }
    }
}

