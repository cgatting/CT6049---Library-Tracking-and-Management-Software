/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CompletionStage;
import oracle.jdbc.driver.AbstractShardingCallableStatement;
import oracle.jdbc.driver.AbstractShardingConnection;
import oracle.jdbc.driver.AbstractShardingDatabaseMetaData;
import oracle.jdbc.driver.AbstractShardingLob;
import oracle.jdbc.driver.AbstractShardingPreparedStatement;
import oracle.jdbc.driver.AbstractShardingResultSet;
import oracle.jdbc.driver.AbstractShardingStatement;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.OracleDriverExtension;
import oracle.jdbc.driver.OracleInputStream;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.T4CConnection;
import oracle.jdbc.driver.T4CInputStream;
import oracle.jdbc.internal.AbstractConnectionBuilder;
import oracle.jdbc.internal.CompletionStageUtil;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OraclePreparedStatement;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.proxy.ProxyFactory;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.THIN_INTERNAL})
class ShardingDriverExtension
extends OracleDriverExtension {
    static ProxyFactory PROXY_FACTORY = null;
    private static final Monitor proxyFactoryLock = Monitor.newInstance();

    ShardingDriverExtension() {
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    Connection getConnection(String string, @Blind(value=PropertiesBlinder.class) Properties properties, AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) throws SQLException {
        Connection connection = null;
        try {
            connection = (Connection)PROXY_FACTORY.proxyForType(OracleConnection.class);
            ((AbstractShardingConnection)((Object)connection)).initialize(string, properties, this, abstractConnectionBuilder);
        }
        catch (SQLException sQLException) {
            int n2 = sQLException.getErrorCode() - 17000;
            if (n2 == 1708 || n2 == 1709) {
                try {
                    String string2 = "oracle.jdbc.driver.T4CDriverExtension";
                    OracleDriverExtension oracleDriverExtension = (OracleDriverExtension)Class.forName(string2).newInstance();
                    T4CConnection t4CConnection = new T4CConnection(string, properties, oracleDriverExtension);
                    t4CConnection.connect(abstractConnectionBuilder);
                    t4CConnection.protocolId = 0;
                    return t4CConnection;
                }
                catch (Exception exception) {
                    throw new SQLException(exception);
                }
            }
            throw sQLException;
        }
        return connection;
    }

    @Override
    final CompletionStage<Connection> getConnectionAsync(String string, @Blind(value=PropertiesBlinder.class) Properties properties, AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) {
        return CompletionStageUtil.failedStage(new UnsupportedOperationException("Asynchronous connection is not supported by the sharding driver"));
    }

    @Override
    oracle.jdbc.internal.OracleStatement allocateStatement(OracleConnection oracleConnection, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        oracle.jdbc.internal.OracleStatement oracleStatement = null;
        oracleStatement = (oracle.jdbc.internal.OracleStatement)PROXY_FACTORY.proxyForType(oracle.jdbc.internal.OracleStatement.class, oracleConnection);
        ((AbstractShardingStatement)((Object)oracleStatement)).initialize((AbstractShardingConnection)((Object)oracleConnection), properties);
        return oracleStatement;
    }

    @Override
    OraclePreparedStatement allocatePreparedStatement(OracleConnection oracleConnection, String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        OraclePreparedStatement oraclePreparedStatement = null;
        oraclePreparedStatement = (OraclePreparedStatement)PROXY_FACTORY.proxyForType(OraclePreparedStatement.class, oracleConnection);
        ((AbstractShardingPreparedStatement)((Object)oraclePreparedStatement)).initialize((AbstractShardingConnection)((Object)oracleConnection), string, properties);
        return oraclePreparedStatement;
    }

    @Override
    OracleCallableStatement allocateCallableStatement(OracleConnection oracleConnection, String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        OracleCallableStatement oracleCallableStatement = null;
        oracleCallableStatement = (OracleCallableStatement)PROXY_FACTORY.proxyForType(OracleCallableStatement.class, oracleConnection);
        ((AbstractShardingCallableStatement)((Object)oracleCallableStatement)).initialize((AbstractShardingConnection)((Object)oracleConnection), string, properties);
        return oracleCallableStatement;
    }

    @Override
    OracleInputStream createInputStream(OracleStatement oracleStatement, int n2, Accessor accessor) throws SQLException {
        return new T4CInputStream(oracleStatement, n2, accessor);
    }

    static {
        try (Monitor.CloseableLock closeableLock = proxyFactoryLock.acquireCloseableLock();){
            if (PROXY_FACTORY == null) {
                PROXY_FACTORY = ProxyFactory.createProxyFactory(AbstractShardingConnection.class, AbstractShardingStatement.class, AbstractShardingPreparedStatement.class, AbstractShardingCallableStatement.class, AbstractShardingResultSet.class, AbstractShardingDatabaseMetaData.class, AbstractShardingLob.class);
            }
        }
    }
}

