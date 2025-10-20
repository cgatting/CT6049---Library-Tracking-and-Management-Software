/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.datasource;

import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import oracle.jdbc.OraclePooledConnectionBuilder;
import oracle.jdbc.datasource.OracleCommonDataSource;

public interface OracleConnectionPoolDataSource
extends ConnectionPoolDataSource,
OracleCommonDataSource {
    public OraclePooledConnectionBuilder createPooledConnectionBuilder() throws SQLException;
}

