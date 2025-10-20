/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.InputStream;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class OracleBufferedStream
extends InputStream
implements Monitor {
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();
    byte[] resizableBuffer;
    int initialBufferSize;
    int currentBufferSize;
    int pos = 0;
    int count = 0;
    long maxPosition = Integer.MAX_VALUE;
    boolean closed = false;
    OracleStatement statement;

    public OracleBufferedStream(int n2) {
        this.initialBufferSize = n2;
        this.currentBufferSize = 0;
        this.resizableBuffer = null;
    }

    public OracleBufferedStream(OracleStatement oracleStatement, int n2) {
        this(n2);
        this.statement = oracleStatement;
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
        this.resizableBuffer = null;
    }

    public boolean needBytes() throws IOException {
        return this.needBytes(Math.max(this.initialBufferSize, this.currentBufferSize));
    }

    public abstract boolean needBytes(int var1) throws IOException;

    public int flushBytes(int n2) {
        int n3 = n2 > this.count - this.pos ? this.count - this.pos : n2;
        this.pos += n3;
        return n3;
    }

    public int writeBytes(byte[] byArray, int n2, int n3) {
        int n4 = n3 > this.count - this.pos ? this.count - this.pos : n3;
        System.arraycopy(this.resizableBuffer, this.pos, byArray, n2, n4);
        this.pos += n4;
        return n4;
    }

    @Override
    public int read() throws IOException {
        OracleBufferedStream oracleBufferedStream = this.statement == null ? this : this.statement.connection;
        try (Monitor.CloseableLock closeableLock = oracleBufferedStream.acquireCloseableLock();){
            int n2 = this.readInternal();
            return n2;
        }
    }

    private final int readInternal() throws IOException {
        if (this.closed || this.isNull()) {
            return -1;
        }
        if (this.needBytes()) {
            return this.resizableBuffer[this.pos++] & 0xFF;
        }
        return -1;
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        return this.read(byArray, 0, byArray.length);
    }

    @Override
    public int read(byte[] byArray, int n2, int n3) throws IOException {
        if (n3 == 0) {
            return 0;
        }
        OracleBufferedStream oracleBufferedStream = this.statement == null ? this : this.statement.connection;
        try (Monitor.CloseableLock closeableLock = oracleBufferedStream.acquireCloseableLock();){
            int n4 = this.readInternal(byArray, n2, n3);
            return n4;
        }
    }

    private final int readInternal(byte[] byArray, int n2, int n3) throws IOException {
        int n4 = n2;
        if (this.closed || this.isNull()) {
            return -1;
        }
        int n5 = n3 > byArray.length ? n4 + byArray.length : n4 + n3;
        if (!this.needBytes(n3)) {
            return -1;
        }
        n4 += this.writeBytes(byArray, n4, n5 - n4);
        while (n4 < n5 && this.needBytes(n5 - n4)) {
            n4 += this.writeBytes(byArray, n4, n5 - n4);
        }
        return n4 - n2;
    }

    @Override
    public int available() throws IOException {
        if (this.closed || this.isNull()) {
            return 0;
        }
        return this.count - this.pos;
    }

    public boolean isNull() throws IOException {
        return false;
    }

    @Override
    public void mark(int n2) {
    }

    @Override
    public void reset() throws IOException {
        Monitor.CloseableLock closeableLock = this.statement.connection.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw new IOException(DatabaseError.findMessage(194, null));
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
        catch (Throwable throwable3) {
            if (closeableLock != null) {
                if (throwable != null) {
                    try {
                        closeableLock.close();
                    }
                    catch (Throwable throwable4) {
                        throwable.addSuppressed(throwable4);
                    }
                } else {
                    closeableLock.close();
                }
            }
            throw throwable3;
        }
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    public long skip(int n2) throws IOException {
        OracleBufferedStream oracleBufferedStream = this.statement == null ? this : this.statement.connection;
        try (Monitor.CloseableLock closeableLock = oracleBufferedStream.acquireCloseableLock();){
            long l2 = this.skipInternal(n2);
            return l2;
        }
    }

    private final int skipInternal(int n2) throws IOException {
        int n3;
        int n4 = n2;
        if (this.closed || this.isNull()) {
            return -1;
        }
        if (!this.needBytes()) {
            return -1;
        }
        for (n3 = 0; n3 < n4 && this.needBytes(); n3 += this.flushBytes(n4 - n3)) {
        }
        return n3;
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return this.statement.getConnectionDuringExceptionHandling();
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }
}

