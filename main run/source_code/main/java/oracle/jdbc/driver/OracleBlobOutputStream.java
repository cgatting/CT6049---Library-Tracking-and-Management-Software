/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleBlob;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.BLOB;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleBlobOutputStream
extends OutputStream {
    long lobOffset;
    OracleBlob blob;
    byte[] buf;
    int count;
    int bufSize;
    boolean isClosed;

    public OracleBlobOutputStream(BLOB bLOB, int n2) throws SQLException {
        this((OracleBlob)bLOB, n2);
    }

    public OracleBlobOutputStream(OracleBlob oracleBlob, int n2) throws SQLException {
        this(oracleBlob, n2, 1L);
    }

    public OracleBlobOutputStream(BLOB bLOB, int n2, long l2) throws SQLException {
        this((OracleBlob)bLOB, n2, l2);
    }

    public OracleBlobOutputStream(OracleBlob oracleBlob, int n2, long l2) throws SQLException {
        if (oracleBlob == null || n2 <= 0 || l2 < 1L) {
            throw new IllegalArgumentException("Illegal Arguments");
        }
        this.blob = oracleBlob;
        this.lobOffset = l2;
        PhysicalConnection physicalConnection = (PhysicalConnection)oracleBlob.getInternalConnection();
        try (Monitor.CloseableLock closeableLock = physicalConnection.acquireCloseableLock();){
            this.buf = physicalConnection.getByteBuffer(n2);
        }
        this.count = 0;
        this.bufSize = n2;
        this.isClosed = false;
    }

    @Override
    public void write(int n2) throws IOException {
        this.ensureOpen();
        if (this.count >= this.bufSize) {
            this.flushBuffer();
        }
        this.buf[this.count++] = (byte)n2;
    }

    @Override
    public void write(byte[] byArray, int n2, int n3) throws IOException {
        this.ensureOpen();
        int n4 = n2;
        int n5 = Math.min(n3, byArray.length - n2);
        if (n5 >= 2 * this.bufSize) {
            if (this.count > 0) {
                this.flushBuffer();
            }
            try {
                this.lobOffset += (long)this.blob.setBytes(this.lobOffset, byArray, n2, n5);
            }
            catch (SQLException sQLException) {
                throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
            }
        } else {
            int n6 = n4 + n5;
            while (n4 < n6) {
                int n7 = Math.min(this.bufSize - this.count, n6 - n4);
                System.arraycopy(byArray, n4, this.buf, this.count, n7);
                n4 += n7;
                this.count += n7;
                if (this.count < this.bufSize) continue;
                this.flushBuffer();
            }
        }
    }

    @Override
    public void flush() throws IOException {
        this.ensureOpen();
        this.flushBuffer();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void close() throws IOException {
        if (this.isClosed) {
            return;
        }
        try {
            this.isClosed = true;
            this.flushBuffer();
            return;
        }
        finally {
            try {
                PhysicalConnection physicalConnection = (PhysicalConnection)this.blob.getInternalConnection();
                try (Monitor.CloseableLock closeableLock = physicalConnection.acquireCloseableLock();){
                    if (this.buf != null) {
                        physicalConnection.cacheBuffer(this.buf);
                        this.buf = null;
                    }
                }
            }
            catch (SQLException sQLException) {
                throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
            }
        }
    }

    private void flushBuffer() throws IOException {
        try {
            if (this.count > 0) {
                this.lobOffset += (long)this.blob.setBytes(this.lobOffset, this.buf, 0, this.count);
                this.count = 0;
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

    protected OracleConnection getConnectionDuringExceptionHandling() {
        try {
            return this.blob.getInternalConnection();
        }
        catch (Exception exception) {
            return null;
        }
    }
}

