/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.SQLException;
import javax.net.ssl.SSLContext;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.datasource.OracleXAConnection;
import org.ietf.jgss.GSSCredential;

public interface OracleXAConnectionBuilder {
    public OracleXAConnectionBuilder user(String var1);

    public OracleXAConnectionBuilder password(String var1);

    public OracleXAConnectionBuilder shardingKey(OracleShardingKey var1);

    public OracleXAConnectionBuilder superShardingKey(OracleShardingKey var1);

    public OracleXAConnectionBuilder gssCredential(GSSCredential var1);

    public OracleXAConnectionBuilder sslContext(SSLContext var1);

    public OracleXAConnectionBuilder readOnlyInstanceAllowed(boolean var1);

    public OracleXAConnection build() throws SQLException;
}

