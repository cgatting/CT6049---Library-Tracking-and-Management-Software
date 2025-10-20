/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.SQLException;
import javax.net.ssl.SSLContext;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.datasource.OraclePooledConnection;
import org.ietf.jgss.GSSCredential;

public interface OraclePooledConnectionBuilder {
    public OraclePooledConnectionBuilder user(String var1);

    public OraclePooledConnectionBuilder password(String var1);

    public OraclePooledConnectionBuilder shardingKey(OracleShardingKey var1);

    public OraclePooledConnectionBuilder superShardingKey(OracleShardingKey var1);

    public OraclePooledConnectionBuilder gssCredential(GSSCredential var1);

    public OraclePooledConnectionBuilder sslContext(SSLContext var1);

    public OraclePooledConnectionBuilder readOnlyInstanceAllowed(boolean var1);

    public OraclePooledConnection build() throws SQLException;
}

