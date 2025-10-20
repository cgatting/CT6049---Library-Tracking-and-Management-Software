/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.OracleClob;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CLOB;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OracleClobWriter
extends Writer {
    private final OracleClobWriterLock lock;
    DBConversion dbConversion;
    OracleClob clob;
    long lobOffset;
    char[] charBuf;
    byte[] nativeBuf;
    int pos;
    int count;
    int chunkSize;
    boolean isClosed;

    public OracleClobWriter(CLOB cLOB, int n2) throws SQLException {
        this((OracleClob)cLOB, n2);
    }

    public OracleClobWriter(OracleClob oracleClob, int n2) throws SQLException {
        this(oracleClob, n2, 1L);
    }

    public OracleClobWriter(CLOB cLOB, int n2, long l2) throws SQLException {
        this((OracleClob)cLOB, n2, l2);
    }

    public OracleClobWriter(OracleClob oracleClob, int n2, long l2) throws SQLException {
        super(new OracleClobWriterLock());
        this.lock = (OracleClobWriterLock)((Writer)this).lock;
        if (oracleClob == null || n2 <= 0 || oracleClob.getJavaSqlConnection() == null || l2 < 1L) {
            throw new IllegalArgumentException();
        }
        this.dbConversion = ((PhysicalConnection)oracleClob.getInternalConnection()).conversion;
        this.clob = oracleClob;
        this.lobOffset = l2;
        this.charBuf = new char[n2];
        this.nativeBuf = new byte[n2 * 3];
        this.count = 0;
        this.pos = 0;
        this.chunkSize = n2;
        this.isClosed = false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void write(char[] cArray, int n2, int n3) throws IOException {
        OracleClobWriterLock oracleClobWriterLock = this.lock;
        synchronized (oracleClobWriterLock) {
            this.ensureOpen();
            int n4 = n2;
            int n5 = Math.min(n3, cArray.length - n2);
            if (n5 >= 2 * this.chunkSize) {
                if (this.count > 0) {
                    this.flushBuffer();
                }
                try {
                    this.lobOffset += (long)this.clob.putChars(this.lobOffset, cArray, n2, n5);
                }
                catch (SQLException sQLException) {
                    throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
                }
                return;
            }
            int n6 = n4 + n5;
            while (n4 < n6) {
                int n7 = Math.min(this.chunkSize - this.count, n6 - n4);
                System.arraycopy(cArray, n4, this.charBuf, this.count, n7);
                n4 += n7;
                this.count += n7;
                if (this.count < this.chunkSize) continue;
                this.flushBuffer();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void flush() throws IOException {
        OracleClobWriterLock oracleClobWriterLock = this.lock;
        synchronized (oracleClobWriterLock) {
            this.ensureOpen();
            this.flushBuffer();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() throws IOException {
        OracleClobWriterLock oracleClobWriterLock = this.lock;
        synchronized (oracleClobWriterLock) {
            this.flushBuffer();
            this.isClosed = true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void flushBuffer() throws IOException {
        OracleClobWriterLock oracleClobWriterLock = this.lock;
        synchronized (oracleClobWriterLock) {
            try {
                if (this.count > 0) {
                    this.lobOffset += (long)this.clob.putChars(this.lobOffset, this.charBuf, 0, this.count);
                    this.count = 0;
                }
            }
            catch (SQLException sQLException) {
                throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
            }
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

    private static final class OracleClobWriterLock {
        private OracleClobWriterLock() {
        }
    }
}

