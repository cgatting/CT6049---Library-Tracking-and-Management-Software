/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.datasource;

import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Supplier;
import javax.net.ssl.SSLContext;
import javax.sql.CommonDataSource;
import oracle.jdbc.AccessToken;
import oracle.jdbc.OracleHostnameResolver;
import oracle.jdbc.OracleShardingKeyBuilder;
import oracle.jdbc.pool.OracleShardingKeyBuilderImpl;

public interface OracleCommonDataSource
extends CommonDataSource {
    default public OracleShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
        return new OracleShardingKeyBuilderImpl();
    }

    public void setDataSourceName(String var1) throws SQLException;

    public String getDataSourceName();

    public String getDatabaseName();

    public void setDatabaseName(String var1) throws SQLException;

    public void setServerName(String var1) throws SQLException;

    public String getServerName();

    public void setURL(String var1) throws SQLException;

    public String getURL() throws SQLException;

    public void setUser(String var1) throws SQLException;

    public String getUser();

    public void setPassword(String var1) throws SQLException;

    public String getDescription();

    public void setDescription(String var1) throws SQLException;

    public String getNetworkProtocol();

    public void setNetworkProtocol(String var1) throws SQLException;

    public void setPortNumber(int var1) throws SQLException;

    public int getPortNumber();

    public void setConnectionProperties(Properties var1) throws SQLException;

    public Properties getConnectionProperties() throws SQLException;

    public void setConnectionProperty(String var1, String var2) throws SQLException;

    public String getConnectionProperty(String var1) throws SQLException;

    public void setMaxStatements(int var1) throws SQLException;

    public int getMaxStatements() throws SQLException;

    public void setImplicitCachingEnabled(boolean var1) throws SQLException;

    public boolean getImplicitCachingEnabled() throws SQLException;

    public void setExplicitCachingEnabled(boolean var1) throws SQLException;

    public boolean getExplicitCachingEnabled() throws SQLException;

    public void setRoleName(String var1) throws SQLException;

    public String getRoleName();

    public void setSSLContext(SSLContext var1) throws SQLException;

    public void setSingleShardTransactionSupport(boolean var1) throws SQLException;

    public void setTokenSupplier(Supplier<? extends AccessToken> var1);

    public void setHostnameResolver(OracleHostnameResolver var1);
}

