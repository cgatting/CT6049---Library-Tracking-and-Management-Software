/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.SQLException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLContext;
import oracle.jdbc.AccessToken;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleHostnameResolver;
import oracle.jdbc.OracleShardingKey;
import org.ietf.jgss.GSSCredential;

public interface OracleConnectionBuilder {
    public OracleConnectionBuilder user(String var1);

    public OracleConnectionBuilder password(String var1);

    public OracleConnectionBuilder shardingKey(OracleShardingKey var1);

    public OracleConnectionBuilder superShardingKey(OracleShardingKey var1);

    public OracleConnectionBuilder gssCredential(GSSCredential var1);

    public OracleConnectionBuilder sslContext(SSLContext var1);

    public OracleConnectionBuilder hostnameResolver(OracleHostnameResolver var1);

    public OracleConnectionBuilder readOnlyInstanceAllowed(boolean var1);

    public OracleConnectionBuilder executorOracle(Executor var1);

    public OracleConnectionBuilder accessToken(AccessToken var1);

    default public CompletionStage<OracleConnection> buildAsyncOracle() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public OracleConnection build() throws SQLException;
}

