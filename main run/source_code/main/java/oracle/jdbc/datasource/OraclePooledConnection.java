/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.transaction.xa.XAResource;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.driver.OracleCloseCallback;

public interface OraclePooledConnection
extends PooledConnection {
    public Connection getLogicalHandle() throws SQLException;

    @Deprecated
    public void setLastAccessedTime(long var1) throws SQLException;

    @Deprecated
    public long getLastAccessedTime() throws SQLException;

    public void registerCloseCallback(OracleCloseCallback var1, Object var2);

    @Deprecated
    public void registerImplicitCacheConnectionEventListener(ConnectionEventListener var1);

    public void setStatementCacheSize(int var1) throws SQLException;

    public int getStatementCacheSize() throws SQLException;

    public void setImplicitCachingEnabled(boolean var1) throws SQLException;

    public boolean getImplicitCachingEnabled() throws SQLException;

    public void setExplicitCachingEnabled(boolean var1) throws SQLException;

    public boolean getExplicitCachingEnabled() throws SQLException;

    public void purgeImplicitCache() throws SQLException;

    public void purgeExplicitCache() throws SQLException;

    public PreparedStatement getStatementWithKey(String var1) throws SQLException;

    public CallableStatement getCallWithKey(String var1) throws SQLException;

    public XAResource getXAResource() throws SQLException;

    public boolean setShardingKeyIfValid(OracleShardingKey var1, OracleShardingKey var2, int var3) throws SQLException;

    public void setShardingKey(OracleShardingKey var1, OracleShardingKey var2) throws SQLException;
}

