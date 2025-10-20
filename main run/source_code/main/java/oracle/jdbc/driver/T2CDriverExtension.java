/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CompletionStage;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleDriverExtension;
import oracle.jdbc.driver.OracleInputStream;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.T2CCallableStatement;
import oracle.jdbc.driver.T2CConnection;
import oracle.jdbc.driver.T2CInputStream;
import oracle.jdbc.driver.T2CPreparedStatement;
import oracle.jdbc.driver.T2CStatement;
import oracle.jdbc.internal.AbstractConnectionBuilder;
import oracle.jdbc.internal.CompletionStageUtil;
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
import oracle.jdbc.oci.OracleOCIConnection;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.OCI_INTERNAL})
class T2CDriverExtension
extends OracleDriverExtension {
    static final int T2C_DEFAULT_BATCHSIZE = 1;

    T2CDriverExtension() {
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    final Connection getConnection(String string, @Blind(value=PropertiesBlinder.class) Properties properties, AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) throws SQLException {
        if (abstractConnectionBuilder.getTokenSupplier() != null) {
            throw (SQLException)DatabaseError.createSqlException(null, 1718, "Type 2 driver does not support token-based authentication").fillInStackTrace();
        }
        T2CConnection t2CConnection = null;
        t2CConnection = properties.getProperty("is_connection_pooling") == "true" ? new OracleOCIConnection(string, properties, (Object)this) : new T2CConnection(string, properties, this);
        t2CConnection.connect(abstractConnectionBuilder);
        return t2CConnection;
    }

    @Override
    final CompletionStage<Connection> getConnectionAsync(String string, @Blind(value=PropertiesBlinder.class) Properties properties, AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) {
        return CompletionStageUtil.failedStage(new UnsupportedOperationException("Asynchronous connection is not supported by the Type 2 OCI driver"));
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    oracle.jdbc.internal.OracleStatement allocateStatement(OracleConnection oracleConnection, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        if (properties != null) {
            properties.setProperty("execute_batch", Integer.toString(1));
            properties.setProperty("row_prefetch", Integer.toString(((PhysicalConnection)oracleConnection).defaultRowPrefetch));
        }
        return new T2CStatement((T2CConnection)oracleConnection, properties);
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    OraclePreparedStatement allocatePreparedStatement(OracleConnection oracleConnection, String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        if (properties != null) {
            properties.setProperty("execute_batch", Integer.toString(((PhysicalConnection)oracleConnection).defaultExecuteBatch));
            properties.setProperty("row_prefetch", Integer.toString(((PhysicalConnection)oracleConnection).defaultRowPrefetch));
        }
        return new T2CPreparedStatement((T2CConnection)oracleConnection, string, properties);
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    OracleCallableStatement allocateCallableStatement(OracleConnection oracleConnection, String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        if (properties != null) {
            properties.setProperty("execute_batch", Integer.toString(((PhysicalConnection)oracleConnection).defaultExecuteBatch));
            properties.setProperty("row_prefetch", Integer.toString(((PhysicalConnection)oracleConnection).defaultRowPrefetch));
        }
        return new T2CCallableStatement((T2CConnection)oracleConnection, string, properties);
    }

    @Override
    OracleInputStream createInputStream(OracleStatement oracleStatement, int n2, Accessor accessor) throws SQLException {
        return new T2CInputStream(oracleStatement, n2, accessor);
    }
}

