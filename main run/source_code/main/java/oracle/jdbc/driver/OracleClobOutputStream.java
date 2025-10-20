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
import oracle.jdbc.internal.OracleClob;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CLOB;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleClobOutputStream
extends OutputStream {
    long lobOffset;
    OracleClob clob;
    byte[] buf;
    int count;
    int bufSize;
    boolean isClosed;

    public OracleClobOutputStream(CLOB cLOB, int n2) throws SQLException {
        this((OracleClob)cLOB, n2);
    }

    public OracleClobOutputStream(OracleClob oracleClob, int n2) throws SQLException {
        this(oracleClob, n2, 1L);
    }

    public OracleClobOutputStream(CLOB cLOB, int n2, long l2) throws SQLException {
        this((OracleClob)cLOB, n2, l2);
    }

    public OracleClobOutputStream(OracleClob oracleClob, int n2, long l2) throws SQLException {
        if (oracleClob == null || n2 <= 0 || l2 < 1L) {
            throw new IllegalArgumentException();
        }
        this.clob = oracleClob;
        this.lobOffset = l2;
        PhysicalConnection physicalConnection = (PhysicalConnection)oracleClob.getInternalConnection();
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
                char[] cArray = new char[n5];
                for (int i2 = 0; i2 < n5; ++i2) {
                    cArray[i2] = (char)byArray[i2 + n2];
                }
                this.lobOffset += (long)this.clob.putChars(this.lobOffset, cArray);
            }
            catch (SQLException sQLException) {
                throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
            }
            return;
        }
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
                PhysicalConnection physicalConnection = (PhysicalConnection)this.clob.getInternalConnection();
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
                char[] cArray = new char[this.count];
                for (int i2 = 0; i2 < this.count; ++i2) {
                    cArray[i2] = (char)this.buf[i2];
                }
                this.lobOffset += (long)this.clob.putChars(this.lobOffset, cArray);
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
            return this.clob.getInternalConnection();
        }
        catch (Exception exception) {
            return null;
        }
    }
}

