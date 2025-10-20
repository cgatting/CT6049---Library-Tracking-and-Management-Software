/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleConnectionStringBuilder;

class OracleConnectionStringBuilderImpl
implements OracleConnectionStringBuilder {
    private static final String DESCRIPTION_FORMAT = "(DESCRIPTION=%s%s%s%s)";
    private static final String DESCRIPTION_FORMAT_THIN_STYLE = "jdbc:oracle:thin:@(DESCRIPTION=%s%s%s%s)";
    private static final String ADDRESS_LIST_FORMAT = "(ADDRESS_LIST=(LOAD_BALANCE=%s)%s)";
    private static final String ADDRESS_FORMAT = "(ADDRESS=(PROTOCOL=%s)(HOST=%s)(PORT=%s)%s%s)";
    private static final String HTTPS_PROXY_FORMAT = "(HTTPS_PROXY=%s)";
    private static final String HTTPS_PROXY_PORT_FORMAT = "(HTTPS_PROXY_PORT=%s)";
    private static final String WEBSOCKET_URI_FORMAT = "(WEBSOCK_URI=%s)";
    private static final String CONNECT_DATA_FORMAT = "(CONNECT_DATA=%s%s%s)";
    private static final String SERVICE_NAME_FORMAT = "(SERVICE_NAME=%s)";
    private static final String SERVER_MODE_FORMAT = "(SERVER=%s)";
    private static final String INSTANCE_NAME_FORMAT = "(INSTANCE_NAME=%s)";
    private static final String SECURITY_FORMAT = "(SECURITY=(SSL_SERVER_DN_MATCH=%s)%s%s)";
    private static final String SERVER_DN_FORMAT = "(SSL_SERVER_CERT_DN=%s)";
    private static final String MY_WALLET_DIR_FORMAT = "(MY_WALLET_DIRECTORY=%s)";
    private static final String EMPTY_STRING = "";
    private static final String KEY_VALUE_FORMAT = "(%s=%s)";
    private List<OracleConnectionStringBuilder.ConnectInfo> addressInfoList = new ArrayList<OracleConnectionStringBuilder.ConnectInfo>();
    private String serviceName;
    private String serverMode;
    private String instanceName;
    private String serverDN;
    private String walletDirectory;
    private int connectTimeout;
    private int transportConnectTimeout;
    private int retryCount;
    private int retryDelay;
    private boolean loadBalance = true;

    OracleConnectionStringBuilderImpl() {
    }

    @Override
    public OracleConnectionStringBuilder addConnectInfo(OracleConnectionStringBuilder.ConnectInfo connectInfo) {
        this.addressInfoList.add(connectInfo);
        return this;
    }

    @Override
    public OracleConnectionStringBuilder addConnectInfo(String string, int n2) {
        this.addressInfoList.add(new OracleConnectionStringBuilder.ConnectInfo().host(string).port(n2));
        return this;
    }

    @Override
    public OracleConnectionStringBuilder serviceName(String string) {
        this.serviceName = string;
        return this;
    }

    @Override
    public OracleConnectionStringBuilder instanceName(String string) {
        this.instanceName = string;
        return this;
    }

    @Override
    public OracleConnectionStringBuilder serverMode(String string) {
        this.serverMode = string;
        return this;
    }

    @Override
    public OracleConnectionStringBuilder serverDN(String string) {
        this.serverDN = string;
        return this;
    }

    @Override
    public OracleConnectionStringBuilder walletDirectory(String string) {
        this.walletDirectory = string;
        return this;
    }

    @Override
    public OracleConnectionStringBuilder loadBalance(boolean bl) {
        this.loadBalance = bl;
        return this;
    }

    @Override
    public OracleConnectionStringBuilder connectTimeout(int n2) {
        this.connectTimeout = n2;
        return this;
    }

    @Override
    public OracleConnectionStringBuilder transportConnectTimeout(int n2) {
        this.transportConnectTimeout = n2;
        return this;
    }

    @Override
    public OracleConnectionStringBuilder retryDelay(int n2) {
        this.retryDelay = n2;
        return this;
    }

    @Override
    public OracleConnectionStringBuilder retryCount(int n2) {
        this.retryCount = n2;
        return this;
    }

    @Override
    public String build() throws IllegalStateException {
        this.validateParams();
        return this.buildConnectionString();
    }

    @Override
    public String buildThinStyleURL() throws IllegalStateException {
        return String.format(DESCRIPTION_FORMAT_THIN_STYLE, this.getDescriptionParams(), this.getAddressListInfo(), this.getConnectData(), this.getSecurityInfo());
    }

    private String buildConnectionString() {
        return String.format(DESCRIPTION_FORMAT, this.getDescriptionParams(), this.getAddressListInfo(), this.getConnectData(), this.getSecurityInfo());
    }

    private String getAddressListInfo() {
        if (this.addressInfoList.size() == 1) {
            return this.getAddressInfo(this.addressInfoList.get(0));
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (OracleConnectionStringBuilder.ConnectInfo connectInfo : this.addressInfoList) {
            stringBuilder.append(this.getAddressInfo(connectInfo));
        }
        return String.format(ADDRESS_LIST_FORMAT, this.loadBalance ? "ON" : "OFF", stringBuilder.toString());
    }

    private String getAddressInfo(OracleConnectionStringBuilder.ConnectInfo connectInfo) {
        String string = EMPTY_STRING;
        if (connectInfo.getProxyHost() != null && connectInfo.getProxyPort() != -1) {
            string = String.format(HTTPS_PROXY_FORMAT, connectInfo.getProxyHost()) + String.format(HTTPS_PROXY_PORT_FORMAT, connectInfo.getProxyPort());
        }
        String string2 = EMPTY_STRING;
        if (connectInfo.getWebSocketURI() != null) {
            string2 = String.format(WEBSOCKET_URI_FORMAT, connectInfo.getWebSocketURI());
        }
        return String.format(ADDRESS_FORMAT, connectInfo.getProtocol(), connectInfo.getDbHostName(), connectInfo.getDbPort(), string2, string);
    }

    private String getConnectData() {
        return String.format(CONNECT_DATA_FORMAT, String.format(SERVICE_NAME_FORMAT, this.serviceName == null ? EMPTY_STRING : this.serviceName), this.serverMode == null ? EMPTY_STRING : String.format(SERVER_MODE_FORMAT, this.serverMode), this.instanceName == null ? EMPTY_STRING : String.format(INSTANCE_NAME_FORMAT, this.instanceName));
    }

    private String getSecurityInfo() {
        if (this.serverDN == null && this.walletDirectory == null) {
            return EMPTY_STRING;
        }
        String string = this.serverDN == null ? "FALSE" : "TRUE";
        return String.format(SECURITY_FORMAT, string, this.serverDN == null ? EMPTY_STRING : String.format(SERVER_DN_FORMAT, this.serverDN), this.walletDirectory == null ? EMPTY_STRING : String.format(MY_WALLET_DIR_FORMAT, this.walletDirectory));
    }

    private String getDescriptionParams() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.connectTimeout > 0) {
            stringBuilder.append(String.format(KEY_VALUE_FORMAT, "CONNECT_TIMEOUT", this.connectTimeout));
        }
        if (this.transportConnectTimeout > 0) {
            stringBuilder.append(String.format(KEY_VALUE_FORMAT, "TRANSPORT_CONNECT_TIMEOUT", this.transportConnectTimeout));
        }
        if (this.retryCount > 0) {
            stringBuilder.append(String.format(KEY_VALUE_FORMAT, "RETRY_COUNT", this.retryCount));
        }
        if (this.retryDelay > 0) {
            stringBuilder.append(String.format(KEY_VALUE_FORMAT, "RETRY_DELAY", this.retryDelay));
        }
        return stringBuilder.toString();
    }

    private void validateParams() throws IllegalStateException {
        if (this.addressInfoList == null || this.addressInfoList.size() == 0) {
            throw new IllegalStateException("AddressInfo cannot be empty");
        }
        for (OracleConnectionStringBuilder.ConnectInfo connectInfo : this.addressInfoList) {
            connectInfo.validate();
        }
        if (this.serviceName == null) {
            throw new IllegalStateException("ServiceName can not be null");
        }
        if (this.connectTimeout < 0) {
            throw new IllegalStateException("Invalid ConnectTimeout value : " + this.connectTimeout);
        }
        if (this.transportConnectTimeout < 0) {
            throw new IllegalStateException("Invalid TransportConnectTimeout value : " + this.transportConnectTimeout);
        }
        if (this.retryCount < 0) {
            throw new IllegalStateException("Invalid RetryCount value : " + this.retryCount);
        }
        if (this.retryDelay < 0) {
            throw new IllegalStateException("Invalid RetryDelay value : " + this.retryDelay);
        }
    }
}

