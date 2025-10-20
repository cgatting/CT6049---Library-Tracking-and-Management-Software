/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.SQLException;
import java.sql.SQLType;
import oracle.jdbc.OracleShardingKey;

public interface OracleShardingKeyBuilder {
    public OracleShardingKeyBuilder subkey(Object var1, SQLType var2);

    public OracleShardingKey build() throws SQLException;
}

