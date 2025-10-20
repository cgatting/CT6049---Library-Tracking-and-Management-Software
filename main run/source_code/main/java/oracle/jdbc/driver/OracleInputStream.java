/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.sql.SQLException;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleBufferedStream;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class OracleInputStream
extends OracleBufferedStream {
    int columnIndex;
    Accessor accessor;
    OracleInputStream nextStream;
    boolean hasBeenOpen = false;

    protected OracleInputStream(OracleStatement oracleStatement, int n2, Accessor accessor) {
        super(oracleStatement, oracleStatement.connection.getDefaultStreamChunkSize());
        this.closed = true;
        this.statement = oracleStatement;
        this.columnIndex = n2;
        this.accessor = accessor;
        this.nextStream = null;
        OracleInputStream oracleInputStream = this.statement.streamList;
        if (oracleInputStream == null || this.columnIndex < oracleInputStream.columnIndex) {
            this.nextStream = this.statement.streamList;
            this.statement.streamList = this;
        } else if (this.columnIndex == oracleInputStream.columnIndex) {
            this.nextStream = oracleInputStream.nextStream;
            oracleInputStream.nextStream = null;
            this.statement.streamList = this;
        } else {
            while (oracleInputStream.nextStream != null && this.columnIndex > oracleInputStream.nextStream.columnIndex) {
                oracleInputStream = oracleInputStream.nextStream;
            }
            if (oracleInputStream.nextStream != null && this.columnIndex == oracleInputStream.nextStream.columnIndex) {
                this.nextStream = oracleInputStream.nextStream.nextStream;
                oracleInputStream.nextStream.nextStream = null;
                oracleInputStream.nextStream = this;
            } else {
                this.nextStream = oracleInputStream.nextStream;
                oracleInputStream.nextStream = this;
            }
        }
    }

    @DisableTrace
    public String toString() {
        return "OIS@" + Integer.toHexString(this.hashCode()) + "{statement = " + this.statement + ", accessor = " + this.accessor + ", nextStream = " + this.nextStream + ", columnIndex = " + this.columnIndex + ", hasBeenOpen = " + this.hasBeenOpen + "}";
    }

    @Override
    public boolean needBytes(int n2) throws IOException {
        if (this.closed) {
            return false;
        }
        if (this.pos >= this.count) {
            if (n2 > this.currentBufferSize) {
                this.currentBufferSize = Math.max(n2, this.initialBufferSize);
                this.resizableBuffer = new byte[this.currentBufferSize];
            }
            try {
                int n3 = this.getBytes(this.currentBufferSize);
                this.pos = 0;
                this.count = n3;
                if (this.count == -1) {
                    if (this.nextStream == null) {
                        this.statement.connection.releaseLine();
                    }
                    this.closed = true;
                    this.accessor.fetchNextColumns();
                    return false;
                }
            }
            catch (SQLException sQLException) {
                throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean isNull() throws IOException {
        boolean bl = false;
        try {
            bl = this.accessor.isNull(0);
        }
        catch (SQLException sQLException) {
            throw (IOException)DatabaseError.createIOException(sQLException).fillInStackTrace();
        }
        return bl;
    }

    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public void close() throws IOException {
        try (Monitor.CloseableLock closeableLock = this.statement.connection.acquireCloseableLock();){
            if (!this.closed && this.hasBeenOpen) {
                while (this.statement.nextStream != this) {
                    this.statement.nextStream.close();
                    this.statement.nextStream = this.statement.nextStream.nextStream;
                }
                if (!this.isNull()) {
                    while (this.needBytes(Math.max(this.initialBufferSize, this.currentBufferSize))) {
                        this.pos = this.count;
                    }
                }
                this.closed = true;
                this.resizableBuffer = null;
                this.currentBufferSize = 0;
            }
        }
    }

    public abstract int getBytes(int var1) throws IOException;
}

