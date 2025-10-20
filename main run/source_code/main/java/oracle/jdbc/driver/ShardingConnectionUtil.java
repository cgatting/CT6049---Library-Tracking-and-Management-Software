/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.ucp.jdbc.PoolDataSource
 *  oracle.ucp.jdbc.PoolDataSourceFactory
 */
package oracle.jdbc.driver;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.replay.OracleDataSourceImpl;
import oracle.net.resolver.AddrResolution;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

class ShardingConnectionUtil {
    private static final Monitor shardingConnectionUtilLock = Monitor.newInstance();
    private static final int DB_SHARDING_ENABLED = 1;
    private static final int DB_SHARD_CATALOG = 4;
    static ConcurrentHashMap<Integer, ShardingPoolDataSourceEntry> shardDatabasePoolDataSourceMap = new ConcurrentHashMap();
    static PoolDataSource catalogDatabasePoolDataSource;
    static short dbCharSet;

    ShardingConnectionUtil() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static ShardingPoolDataSourceEntry getShardingDatabasePoolDataSource(String string, @Blind(value=PropertiesBlinder.class) Properties properties, String string2, boolean bl, String string3) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = shardingConnectionUtilLock.acquireCloseableLock();){
            ShardingPoolDataSourceEntry shardingPoolDataSourceEntry;
            block27: {
                ShardingPoolDataSourceEntry shardingPoolDataSourceEntry2;
                int n2;
                Connection connection;
                block25: {
                    ShardingPoolDataSourceEntry shardingPoolDataSourceEntry3;
                    block26: {
                        connection = null;
                        try {
                            n2 = ShardingConnectionUtil.calculateConnectionInfoHashKey(string, properties);
                            shardingPoolDataSourceEntry2 = null;
                            if (!shardDatabasePoolDataSourceMap.containsKey(n2)) break block25;
                            shardingPoolDataSourceEntry3 = shardingPoolDataSourceEntry2 = shardDatabasePoolDataSourceMap.get(n2);
                            if (connection == null) break block26;
                        }
                        catch (Throwable throwable2) {
                            try {
                                if (connection != null) {
                                    connection.close();
                                }
                                throw throwable2;
                            }
                            catch (Throwable throwable3) {
                                throwable = throwable3;
                                throw throwable3;
                            }
                        }
                        connection.close();
                    }
                    return shardingPoolDataSourceEntry3;
                }
                PoolDataSource poolDataSource = PoolDataSourceFactory.getPoolDataSource();
                if (bl) {
                    poolDataSource.setConnectionFactoryClassName(OracleDataSourceImpl.class.getName());
                } else {
                    poolDataSource.setConnectionFactoryClassName(OracleDataSource.class.getName());
                }
                poolDataSource.setURL(string);
                poolDataSource.setConnectionProperties(properties);
                OracleDataSource oracleDataSource = new OracleDataSource();
                oracleDataSource.setURL(string);
                oracleDataSource.setConnectionProperties(properties);
                connection = oracleDataSource.getConnection();
                ((OracleConnection)connection).addFeature(OracleConnection.ClientFeature.SHARDING_DRIVER);
                short s2 = ((OracleConnection)connection).getVersionNumber();
                if (s2 < 20000) {
                    throw (SQLException)DatabaseError.createSqlException(1710).fillInStackTrace();
                }
                if (catalogDatabasePoolDataSource == null) {
                    dbCharSet = ((OracleConnection)connection).getDbCsId();
                    catalogDatabasePoolDataSource = ShardingConnectionUtil.getCatalogDatabasePoolDataSource(connection, string3 == null ? string : string3, properties, string2, bl);
                } else {
                    ShardingConnectionUtil.validateConnectionToShardedDatabase(connection);
                }
                shardingPoolDataSourceEntry2 = new ShardingPoolDataSourceEntry(poolDataSource, ((OracleConnection)connection).getUserName(), ((OracleConnection)connection).getCurrentSchema(), ((OracleConnection)connection).getServerSessionInfo());
                shardDatabasePoolDataSourceMap.put(n2, shardingPoolDataSourceEntry2);
                shardingPoolDataSourceEntry = shardingPoolDataSourceEntry2;
                if (connection == null) break block27;
                connection.close();
            }
            return shardingPoolDataSourceEntry;
        }
    }

    static int calculateConnectionInfoHashKey(String string, @Blind(value=PropertiesBlinder.class) Properties properties) {
        int n2 = 1;
        if (string != null) {
            n2 = 31 * n2 + string.hashCode();
        }
        if (properties != null) {
            Set<String> set = properties.stringPropertyNames();
            for (String string2 : set) {
                String string3 = properties.getProperty(string2);
                n2 = 31 * n2 + string2.hashCode();
                n2 = 31 * n2 + (string3 != null ? string3.hashCode() : 0);
            }
        }
        return n2;
    }

    static PoolDataSource getCatalogDatabasePoolDataSource() throws SQLException {
        return catalogDatabasePoolDataSource;
    }

    static short getDbCharsSet() {
        return dbCharSet;
    }

    static PoolDataSource getCatalogDatabasePoolDataSource(Connection connection, String string, @Blind(value=PropertiesBlinder.class) Properties properties, String string2, boolean bl) throws SQLException {
        String string3 = ShardingConnectionUtil.getCatalogServiceUrl(connection, string, string2);
        PoolDataSource poolDataSource = PoolDataSourceFactory.getPoolDataSource();
        if (bl) {
            poolDataSource.setConnectionFactoryClassName(OracleDataSourceImpl.class.getName());
        } else {
            poolDataSource.setConnectionFactoryClassName(OracleDataSource.class.getName());
        }
        poolDataSource.setURL(string3);
        poolDataSource.setConnectionProperties(properties);
        return poolDataSource;
    }

    static String getCatalogServiceUrl(Connection connection, String string, String string2) throws SQLException {
        String string3 = null;
        String string4 = null;
        try (CallableStatement callableStatement = connection.prepareCall("{call GSMADMIN_INTERNAL.getShardingParams(?,?)}");){
            callableStatement.registerOutParameter(1, 2);
            callableStatement.registerOutParameter(2, 12);
            callableStatement.execute();
            int n2 = callableStatement.getInt(1);
            string4 = callableStatement.getString(2);
            if (string4 == null) {
                throw (SQLException)DatabaseError.createSqlException(1705).fillInStackTrace();
            }
            if ((n2 & 4) != 0) {
                throw (SQLException)DatabaseError.createSqlException(1709).fillInStackTrace();
            }
            if ((n2 & 1) == 0) {
                throw (SQLException)DatabaseError.createSqlException(1705).fillInStackTrace();
            }
        }
        string3 = !string4.equals(string2) ? ShardingConnectionUtil.replaceGsmServiceNameWithCatalogServiceName(string, string2, string4) : string;
        return string3;
    }

    private static String replaceGsmServiceNameWithCatalogServiceName(String string, String string2, String string3) {
        return AddrResolution.replaceServiceNameInUrl(string, string2, string3);
    }

    private static void validateConnectionToShardedDatabase(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select GSMADMIN_INTERNAL.GETSHARDINGMODE from dual");){
            if (resultSet.next()) {
                int n2 = resultSet.getInt(1);
                if ((n2 & 4) != 0) {
                    throw (SQLException)DatabaseError.createSqlException(1709).fillInStackTrace();
                }
                if ((n2 & 1) == 0) {
                    throw (SQLException)DatabaseError.createSqlException(1705).fillInStackTrace();
                }
            }
        }
    }

    protected static final class ShardingPoolDataSourceEntry {
        private PoolDataSource pds;
        private String userName;
        private String schemaName;
        private Properties serverSessionInfo;

        public ShardingPoolDataSourceEntry(PoolDataSource poolDataSource, String string, String string2, @Blind(value=PropertiesBlinder.class) Properties properties) {
            this.pds = poolDataSource;
            this.userName = string;
            this.schemaName = string2;
            this.serverSessionInfo = properties;
        }

        public PoolDataSource getPds() {
            return this.pds;
        }

        public String getUserName() {
            return this.userName;
        }

        public String getSchemaName() {
            return this.schemaName;
        }

        @Blind(value=PropertiesBlinder.class)
        public Properties getServerSessionInfo() {
            return this.serverSessionInfo;
        }
    }
}

