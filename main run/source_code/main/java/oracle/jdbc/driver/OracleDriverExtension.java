/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CompletionStage;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.OracleInputStream;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.internal.AbstractConnectionBuilder;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OraclePreparedStatement;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class OracleDriverExtension {
    OracleDriverExtension() {
    }

    final Connection getConnection(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        return this.getConnection(string, properties, AbstractConnectionBuilder.unconfigured());
    }

    abstract Connection getConnection(String var1, Properties var2, AbstractConnectionBuilder<?, ?> var3) throws SQLException;

    abstract CompletionStage<Connection> getConnectionAsync(String var1, Properties var2, AbstractConnectionBuilder<?, ?> var3);

    abstract oracle.jdbc.internal.OracleStatement allocateStatement(OracleConnection var1, Properties var2) throws SQLException;

    abstract OraclePreparedStatement allocatePreparedStatement(OracleConnection var1, String var2, Properties var3) throws SQLException;

    abstract OracleCallableStatement allocateCallableStatement(OracleConnection var1, String var2, Properties var3) throws SQLException;

    abstract OracleInputStream createInputStream(OracleStatement var1, int var2, Accessor var3) throws SQLException;
}

