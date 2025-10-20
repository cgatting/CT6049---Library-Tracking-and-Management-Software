/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.OracleInputStream;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.T2CCallableStatement;
import oracle.jdbc.driver.T2CConnection;
import oracle.jdbc.driver.T2CPreparedStatement;
import oracle.jdbc.driver.T2CStatement;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.OCI_INTERNAL})
class T2CInputStream
extends OracleInputStream {
    native int t2cGetBytes(long var1, int var3, byte[] var4, int var5, Accessor[] var6, Object[] var7, Object[] var8, long var9);

    T2CInputStream(OracleStatement oracleStatement, int n2, Accessor accessor) {
        super(oracleStatement, n2, accessor);
    }

    private int getRowNumber() {
        int n2 = 0;
        if (this.statement.isFetchStreams) {
            if (this.statement instanceof T2CStatement) {
                if (((T2CStatement)this.statement).needToRetainRows) {
                    n2 = this.statement.storedRowCount;
                }
            } else if (this.statement instanceof T2CPreparedStatement) {
                if (((T2CPreparedStatement)this.statement).needToRetainRows) {
                    n2 = this.statement.storedRowCount;
                }
            } else if (((T2CCallableStatement)this.statement).needToRetainRows) {
                n2 = this.statement.storedRowCount;
            }
        }
        return n2;
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    public int getBytes(int n2) throws IOException {
        try (Monitor.CloseableLock closeableLock = this.statement.connection.acquireCloseableLock();){
            int n3;
            if (n2 > this.currentBufferSize) {
                this.currentBufferSize = Math.max(n2, this.initialBufferSize);
                this.resizableBuffer = new byte[this.currentBufferSize];
            }
            long l2 = this.statement.connection.useNio ? 1 : 0;
            if (this.statement.connection.useNio) {
                if (this.statement.nioBuffers[3] == null || this.statement.nioBuffers[3].capacity() < this.resizableBuffer.length) {
                    this.statement.nioBuffers[3] = ByteBuffer.allocateDirect(this.resizableBuffer.length);
                } else {
                    this.statement.nioBuffers[3].rewind();
                }
            }
            int n4 = 0;
            n4 = this.t2cGetBytes(this.statement.c_state, this.columnIndex, this.resizableBuffer, this.currentBufferSize, this.statement.accessors, this.statement.nioBuffers, this.statement.lobPrefetchMetaData, l2);
            boolean bl = false;
            try {
                n3 = this.getRowNumber();
                if (n4 == -1) {
                    ((T2CConnection)this.statement.connection).checkError(n4, this.statement.sqlWarning);
                } else if (n4 == -2) {
                    bl = true;
                    this.accessor.setNull(n3, true);
                    n4 = 0;
                } else if (n4 >= 0) {
                    this.accessor.setNull(n3, false);
                }
            }
            catch (SQLException sQLException) {
                throw new IOException(sQLException.getMessage());
            }
            if (n4 <= 0) {
                n4 = -1;
                bl = true;
            }
            if (this.statement.connection.useNio) {
                ByteBuffer byteBuffer = this.statement.nioBuffers[3];
                if (byteBuffer != null && n4 > 0) {
                    byteBuffer.get(this.resizableBuffer, 0, n4);
                }
                if (bl) {
                    try {
                        this.statement.extractNioDefineBuffers(this.columnIndex);
                    }
                    catch (SQLException sQLException) {
                        throw new IOException(sQLException.getMessage());
                    }
                }
            }
            if (bl && this.statement.lobPrefetchMetaData != null) {
                this.statement.processLobPrefetchMetaData(this.statement.lobPrefetchMetaData);
            }
            n3 = n4;
            return n3;
        }
    }

    @Override
    public boolean isNull() throws IOException {
        if (!this.statement.isFetchStreams) {
            this.needBytes();
            return super.isNull();
        }
        return false;
    }
}

