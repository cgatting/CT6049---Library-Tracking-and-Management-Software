/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import oracle.jdbc.OracleConnectionStringBuilderImpl;

public interface OracleConnectionStringBuilder {
    public static final String SERVER_MODE_DEDICATED = "dedicated";
    public static final String SERVER_MODE_SHARED = "shared";
    public static final String SERVER_MODE_POOLED = "pooled";
    public static final String PROTOCOL_TCP = "TCP";
    public static final String PROTOCOL_TCPS = "TCPS";
    public static final String PROTOCOL_WSS = "WSS";

    public static OracleConnectionStringBuilder newInstance() {
        return new OracleConnectionStringBuilderImpl();
    }

    public OracleConnectionStringBuilder addConnectInfo(ConnectInfo var1);

    public OracleConnectionStringBuilder addConnectInfo(String var1, int var2);

    public OracleConnectionStringBuilder serviceName(String var1);

    public OracleConnectionStringBuilder serverMode(String var1);

    public OracleConnectionStringBuilder instanceName(String var1);

    public OracleConnectionStringBuilder serverDN(String var1);

    public OracleConnectionStringBuilder walletDirectory(String var1);

    public OracleConnectionStringBuilder loadBalance(boolean var1);

    public OracleConnectionStringBuilder connectTimeout(int var1);

    public OracleConnectionStringBuilder transportConnectTimeout(int var1);

    public OracleConnectionStringBuilder retryCount(int var1);

    public OracleConnectionStringBuilder retryDelay(int var1);

    public String build() throws IllegalStateException;

    public String buildThinStyleURL() throws IllegalStateException;

    public static class ConnectInfo {
        private String host;
        private int port;
        private String protocol = "TCP";
        private String proxyHost;
        private int proxyPort;
        private String webSocketURI;

        public static ConnectInfo newInstance() {
            return new ConnectInfo();
        }

        public ConnectInfo host(String string) {
            this.host = string;
            return this;
        }

        public ConnectInfo port(int n2) {
            this.port = n2;
            return this;
        }

        public ConnectInfo protocol(String string) {
            this.protocol = string;
            return this;
        }

        public ConnectInfo proxyHost(String string) {
            this.proxyHost = string;
            return this;
        }

        public ConnectInfo proxyPort(int n2) {
            this.proxyPort = n2;
            return this;
        }

        public ConnectInfo webSocketURI(String string) {
            this.webSocketURI = string;
            return this;
        }

        String getDbHostName() {
            return this.host;
        }

        int getDbPort() {
            return this.port;
        }

        String getProtocol() {
            return this.protocol;
        }

        String getProxyHost() {
            return this.proxyHost;
        }

        int getProxyPort() {
            return this.proxyPort;
        }

        String getWebSocketURI() {
            return this.webSocketURI;
        }

        void validate() throws IllegalStateException {
            if (this.host == null || this.host.isEmpty()) {
                throw new IllegalStateException("Host value cannot be empty");
            }
            if (this.port <= 0) {
                throw new IllegalStateException("Invalid port value : " + this.port);
            }
            if (this.protocol == null || this.protocol.isEmpty()) {
                throw new IllegalStateException("Invalid protocol : " + this.protocol);
            }
        }
    }
}

