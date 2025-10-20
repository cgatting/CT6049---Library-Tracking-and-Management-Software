/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleDriverExtension;
import oracle.jdbc.driver.T2CConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.pool.OracleOCIConnectionPool;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
public abstract class OracleOCIConnection
extends T2CConnection {
    OracleOCIConnectionPool ociConnectionPool = null;
    boolean isPool = false;
    boolean aliasing = false;

    public OracleOCIConnection(String string, @Blind(value=PropertiesBlinder.class) Properties properties, Object object) throws SQLException {
        this(string, properties, (OracleDriverExtension)object);
    }

    OracleOCIConnection(String string, @Blind(value=PropertiesBlinder.class) Properties properties, OracleDriverExtension oracleDriverExtension) throws SQLException {
        super(string, properties, oracleDriverExtension);
    }

    public byte[] getConnectionId() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = this.t2cGetConnectionId(this.m_nativeState);
            if (byArray == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 254, "Cannot create a ByteArray for the connectionId").fillInStackTrace();
            }
            this.aliasing = true;
            byte[] byArray2 = byArray;
            return byArray2;
        }
    }

    public void passwordChange(String string, @Blind String string2, @Blind String string3) throws SQLException, IOException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.ociPasswordChange(string, string2, string3);
        }
    }

    @Override
    public void close() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.lifecycle == 2 || this.lifecycle == 4 || this.aliasing) {
                return;
            }
            super.close();
            this.ociConnectionPool.connectionClosed((oracle.jdbc.oci.OracleOCIConnection)this);
        }
    }

    public void setConnectionPool(OracleOCIConnectionPool oracleOCIConnectionPool) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.ociConnectionPool = oracleOCIConnectionPool;
        }
    }

    @Override
    public void setStmtCacheSize(int n2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            super.setStmtCacheSize(n2, bl);
        }
    }
}

