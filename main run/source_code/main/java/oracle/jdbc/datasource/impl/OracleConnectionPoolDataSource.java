/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.datasource.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.PooledConnection;
import oracle.jdbc.OraclePooledConnectionBuilder;
import oracle.jdbc.datasource.OraclePooledConnection;
import oracle.jdbc.datasource.impl.OracleConnectionBuilderImpl;
import oracle.jdbc.datasource.impl.OracleDataSource;
import oracle.jdbc.datasource.impl.OraclePooledConnectionBuilderImpl;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc.internal.replay")
@Supports(value={Feature.HIGH_AVAILABILITY, Feature.CONN_POOL, Feature.APPLICATION_CONTINUITY})
public class OracleConnectionPoolDataSource
extends OracleDataSource
implements oracle.jdbc.datasource.OracleConnectionPoolDataSource,
oracle.jdbc.replay.internal.OracleConnectionPoolDataSource {
    public OracleConnectionPoolDataSource() throws SQLException {
        this.dataSourceName = "OracleConnectionPoolDataSource";
        this.isOracleDataSource = false;
    }

    @Override
    public PooledConnection getPooledConnection() throws SQLException {
        String string = null;
        OpaqueString opaqueString = OpaqueString.NULL;
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            string = this.user;
            opaqueString = this.password != null ? this.password : OpaqueString.NULL;
        }
        return this.getPooledConnection(string, opaqueString);
    }

    @Override
    public PooledConnection getPooledConnection(String string, @Blind String string2) throws SQLException {
        return this.getPooledConnection(string, OpaqueString.newOpaqueString(string2));
    }

    private PooledConnection getPooledConnection(String string, OpaqueString opaqueString) throws SQLException {
        Connection connection = this.getPhysicalConnection(string, opaqueString);
        oracle.jdbc.pool.OraclePooledConnection oraclePooledConnection = new oracle.jdbc.pool.OraclePooledConnection(connection);
        return oraclePooledConnection;
    }

    PooledConnection getPooledConnection(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        Connection connection = this.getPhysicalConnection(properties, null);
        oracle.jdbc.pool.OraclePooledConnection oraclePooledConnection = new oracle.jdbc.pool.OraclePooledConnection(connection);
        return oraclePooledConnection;
    }

    protected Connection getPhysicalConnection() throws SQLException {
        return super.getConnection(this.user, this.password);
    }

    protected Connection getPhysicalConnection(String string, String string2, @Blind String string3) throws SQLException {
        this.url = string;
        return super.getConnection(string2, OpaqueString.newOpaqueString(string3));
    }

    protected Connection getPhysicalConnection(String string, @Blind String string2) throws SQLException {
        return super.getConnection(string, OpaqueString.newOpaqueString(string2));
    }

    protected Connection getPhysicalConnection(String string, String string2, OpaqueString opaqueString) throws SQLException {
        this.url = string;
        return super.getConnection(string2, opaqueString);
    }

    protected Connection getPhysicalConnection(String string, OpaqueString opaqueString) throws SQLException {
        return super.getConnection(string, opaqueString);
    }

    protected oracle.jdbc.pool.OraclePooledConnection getPooledConnection(OracleConnectionBuilderImpl oracleConnectionBuilderImpl) throws SQLException {
        Connection connection = super.getConnection(oracleConnectionBuilderImpl);
        oracle.jdbc.pool.OraclePooledConnection oraclePooledConnection = new oracle.jdbc.pool.OraclePooledConnection(connection);
        return oraclePooledConnection;
    }

    @Override
    public OraclePooledConnectionBuilder createPooledConnectionBuilder() throws SQLException {
        return new OraclePooledConnectionBuilderImpl(){

            @Override
            public OraclePooledConnection build() throws SQLException {
                OracleConnectionBuilderImpl oracleConnectionBuilderImpl = (OracleConnectionBuilderImpl)OracleConnectionPoolDataSource.this.createConnectionBuilder().copy(this);
                oracleConnectionBuilderImpl.verifyBuildConfiguration();
                return OracleConnectionPoolDataSource.this.getPooledConnection(oracleConnectionBuilderImpl);
            }
        };
    }

    @Override
    public Connection getConnection() throws SQLException {
        throw DatabaseError.createSqlException(23);
    }

    @Override
    public Connection getConnection(String string, String string2) throws SQLException {
        throw DatabaseError.createSqlException(23);
    }

    public Connection getConnection(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        throw DatabaseError.createSqlException(23);
    }
}

