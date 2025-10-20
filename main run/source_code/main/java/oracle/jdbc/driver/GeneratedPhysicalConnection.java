/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.SQLException;
import java.util.Properties;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class GeneratedPhysicalConnection
extends OracleConnection {
    boolean autocommit;
    int commitOption;
    int ociConnectionPoolMinLimit = 0;
    int ociConnectionPoolMaxLimit = 0;
    int ociConnectionPoolIncrement = 0;
    int ociConnectionPoolTimeout = 0;
    boolean ociConnectionPoolNoWait = false;
    boolean ociConnectionPoolTransactionDistributed = false;
    String ociConnectionPoolLogonMode = null;
    boolean ociConnectionPoolIsPooling = false;
    Object ociConnectionPoolObject = null;
    Object ociConnectionPoolConnID = null;
    String ociConnectionPoolProxyType = null;
    Integer ociConnectionPoolProxyNumRoles = 0;
    Object ociConnectionPoolProxyRoles = null;
    String ociConnectionPoolProxyUserName = null;
    OpaqueString ociConnectionPoolProxyPassword = null;
    String ociConnectionPoolProxyDistinguishedName = null;
    Object ociConnectionPoolProxyCertificate = null;
    boolean retainV9BindBehavior;
    String userName;
    OpaqueString newPasswordValue;
    String database;
    boolean defaultautocommit;
    boolean bindUseDBA;
    String protocol;
    int streamChunkSize;
    boolean setFloatAndDoubleUseBinary;
    String thinVsessionTerminal;
    String thinVsessionMachine;
    String thinVsessionOsuser;
    String thinVsessionProgram;
    String thinVsessionProcess;
    String thinVsessionIname;
    String thinVsessionEname;
    String thinNetProfile;
    String thinNetAuthenticationServices;
    String thinNetAuthenticationKrb5Mutual;
    String thinNetAuthenticationKrb5CcName;
    String thinNetAuthenticationKrbRealm;
    String thinNetAuthenticationKrbJaasLoginModule;
    String thinNetSetFIPSMode;
    boolean thinNetAllowWeakCrypto;
    String thinNetEncryptionLevel;
    String thinNetEncryptionTypes;
    String thinNetChecksumLevel;
    String thinNetChecksumTypes;
    String thinNetCryptoSeed;
    boolean thinUseJCEAPI;
    boolean thinTcpNoDelay;
    String thinReadTimeout;
    String thinOutboundConnectTimeout;
    String thinNetConnectTimeout;
    boolean thinNetDisableOutOfBandBreak;
    boolean thinNetUseZeroCopyIO;
    boolean use1900AsYearForTime;
    boolean timestamptzInGmt;
    boolean timezoneAsRegion;
    String thinSslCertificateAlias;
    String thinSslServerDnMatch;
    String thinSslServerCertDn;
    String thinSslVersion;
    String thinSslCipherSuites;
    String thinJavaxNetSslKeystore;
    String thinJavaxNetSslKeystoretype;
    OpaqueString thinJavaxNetSslKeystorepassword;
    String thinJavaxNetSslTruststore;
    String thinJavaxNetSslTruststoretype;
    OpaqueString thinJavaxNetSslTruststorepassword;
    String thinSslKeymanagerfactoryAlgorithm;
    String thinSslTrustmanagerfactoryAlgorithm;
    String thinNetOldsyntax;
    String thinJndiLdapConnectTimeout;
    String thinJndiLdapReadTimeout;
    String walletLocation;
    OpaqueString walletPassword;
    String thinLdapSslCipherSuites;
    String thinLdapSslVersions;
    String thinLdapSslKeyStore;
    String thinLdapSslKeyStoreType;
    OpaqueString thinLdapSslKeyStorePwd;
    String thinLdapSslKeyManagerFactoryAlgo;
    String thinLdapSslTrustStore;
    String thinLdapSslTrustStoreType;
    OpaqueString thinLdapSslTrustStorePassword;
    String thinLdapSslTrustManagerFactoryAlgo;
    String thinLdapSslWalletLocation;
    OpaqueString thinLdapSslWalletPassword;
    String thinLdapSecurityAuthentication;
    String thinLdapSecurityPrincipal;
    OpaqueString thinLdapSecurityCredetials;
    String thinLdapsslContextProtocol;
    String proxyClientName;
    boolean useNio;
    String thinHttpsProxyHost;
    int thinHttpsProxyPort;
    String thinNetConnectionIdPrefix;
    String ociDriverCharset;
    String editionName;
    String logonCap;
    boolean useOCIDefaultDefines;
    String internalLogon;
    boolean createDescriptorUseCurrentSchemaForSchemaName;
    long ociSvcCtxHandle;
    long ociEnvHandle;
    long ociErrHandle;
    boolean prelimAuth;
    boolean jmsNotificationConnection;
    boolean nlsLangBackdoor;
    OpaqueString setNewPassword;
    int defaultExecuteBatch;
    int defaultRowPrefetch;
    int defaultLobPrefetchSize;
    boolean enableDataInLocator;
    boolean enableReadDataInLocator;
    boolean overrideEnableReadDataInLocator;
    boolean reportRemarks;
    boolean includeSynonyms;
    boolean restrictGettables;
    boolean accumulateBatchResult;
    boolean useFetchSizeWithLongColumn;
    boolean processEscapes;
    boolean fixedString;
    boolean defaultnchar;
    boolean permitTimestampDateMismatch;
    String resourceManagerId;
    boolean disableDefinecolumntype;
    boolean convertNcharLiterals;
    boolean autoCommitSpecCompliant;
    boolean jdbcStandardBehavior;
    boolean j2ee13Compliant;
    String dmsParentName;
    String dmsParentType;
    boolean dmsStmtMetrics;
    boolean dmsStmtCachingMetrics;
    boolean mapDateToTimestamp;
    boolean useThreadLocalBufferCache;
    String driverNameAttribute;
    int maxCachedBufferSize;
    int implicitStatementCacheSize;
    boolean lobStreamPosStandardCompliant;
    boolean isStrictAsciiConversion;
    boolean isQuickAsciiConversion;
    String drcpConnectionClass;
    String drcpTagName;
    String drcpConnectionPurity;
    boolean useDRCPMultipletag;
    String drcpPLSQLCallback;
    String blockSourceImpl;
    boolean thinForceDnsLoadBalancing;
    boolean enableTempLobRefCnt;
    boolean keepAlive;
    String sqlTranslationProfile;
    String sqlErrorTranslationFile;
    boolean ignoreReplayContextFromAuthentication;
    boolean javaNetNio;
    boolean nsDirectBuffer;
    boolean plsqlVarcharParameter4KOnly;
    String targetInstanceName;
    String targetServiceName;
    String targetShardingKey;
    String targetSuperShardingKey;
    boolean readOnlyInstanceAllowed;
    boolean enableOCIFAN;
    boolean isResultSetCacheEnabled_IGNORED;
    boolean isResultSetCacheEnabled_IGNORED2;
    boolean isResultSetCacheEnabled;
    boolean isOldUpdateableResultSet;
    String allowedLogonVersion;
    String commitOptionProperty;
    String calculateChecksumProperty;
    String javaNetLocalIPForMsgq;
    String javaNetMsgqTransport;
    int javaNetMsgqBusyWait;
    int javaNetMsgqKernelWait;
    int downHostsTimeout;
    boolean fanEnabled;
    String tnsAdmin;
    String networkCompression;
    String networkCompressionLevels;
    int networkCompressionThreshold;
    String configFile;
    String websocketUser;
    OpaqueString websocketPassword;
    String socksProxyHost;
    int socksProxyPort;
    boolean socksRemoteDNS;
    OracleConnection.ConnectionValidation defaultConnectionValidation;
    boolean enableACSupport;
    boolean enableTGSupport;
    boolean enableImplicitRequests;
    boolean drcpMultiplexingInRequestAPIs;
    boolean continueBatchOnError;
    int tcpKeepIdle;
    int tcpKeepInterval;
    boolean useShardingDriverConnection;
    int tcpKeepCount;
    int requestSizeLimit;
    String onsWalletFile;
    OpaqueString onsWalletPassword;
    String onsProtocol;
    int loginTimeout;
    boolean inbandNotification;
    String sslContextProtocol;
    String tokenAuthentication;
    String tokenLocation;
    String passwordAuthentication;
    String ociIamUrl;
    String ociTenancy;
    String ociCompartment;
    String ociDatabase;
    OpaqueString accessToken;

    GeneratedPhysicalConnection() {
    }

    GeneratedPhysicalConnection(Monitor.CloseableLock closeableLock) {
        super(closeableLock);
    }

    static String getSystemPropertyPollInterval() {
        return GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.TimeoutPollInterval", "1000");
    }

    static String getSystemPropertySqlTranslationProfile() {
        return GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.sqlTranslationProfile", null);
    }

    static String getSystemPropertyFastConnectionFailover(String string) {
        return GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.FastConnectionFailover", string);
    }

    static String getSystemPropertyUserName() {
        return GeneratedPhysicalConnection.getSystemProperty("user.name", null);
    }

    static String getSystemPropertyTrace() {
        return GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.Trace", null);
    }

    static String getSystemPropertyDateZeroTime(String string) {
        return GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.DateZeroTime", string);
    }

    static String getSystemPropertyDateZeroTimeExtra(String string) {
        return GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.DateZeroTimeExtra", string);
    }

    static String getSystemPropertyConfigFile(String string) {
        return GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.config.file", string);
    }

    static String getSystemPropertyTnsAdmin(String string) {
        return GeneratedPhysicalConnection.getSystemProperty("oracle.net.tns_admin", string);
    }

    static String getSystemPropertyDatabase(String string) {
        String string2 = GeneratedPhysicalConnection.getSystemProperty("database", null);
        return string2 != null ? string2 : GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.database", string);
    }

    static String getSystemPropertyUserHome(String string) {
        return GeneratedPhysicalConnection.getSystemProperty("user.home", string);
    }

    private static String getSystemProperty(final String string, final String string2) {
        if (string != null) {
            return AccessController.doPrivileged(new PrivilegedAction<String>(){

                @Override
                public String run() {
                    return System.getProperty(string, string2);
                }
            });
        }
        return string2;
    }

    private void readDeprecatedConnectionProperties(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        this.includeSynonyms = this.parseConnectionProperty(properties, "synonyms", (byte)3, this.includeSynonyms);
        this.reportRemarks = this.parseConnectionProperty(properties, "remarks", (byte)3, this.reportRemarks);
        this.defaultRowPrefetch = this.parseConnectionProperty(properties, "prefetch", (byte)3, this.defaultRowPrefetch);
        this.defaultRowPrefetch = this.parseConnectionProperty(properties, "rowPrefetch", (byte)3, this.defaultRowPrefetch);
        this.defaultExecuteBatch = this.parseConnectionProperty(properties, "batch", (byte)3, this.defaultExecuteBatch);
        this.defaultExecuteBatch = this.parseConnectionProperty(properties, "executeBatch", (byte)3, this.defaultExecuteBatch);
        this.proxyClientName = this.parseConnectionProperty(properties, "PROXY_CLIENT_NAME", (byte)1, this.proxyClientName);
    }

    private String parseConnectionProperty(@Blind(value=PropertiesBlinder.class) Properties properties, String string, byte by, String string2) throws SQLException {
        String string3 = null;
        if (!(by != 1 && by != 3 || properties == null || (string3 = properties.getProperty(string)) != null || string.startsWith("oracle.") || string.startsWith("java.") || string.startsWith("javax."))) {
            string3 = properties.getProperty("oracle.jdbc." + string);
        }
        if (string3 == null && (by == 2 || by == 3)) {
            string3 = string.startsWith("oracle.") || string.startsWith("java.") || string.startsWith("javax.") ? GeneratedPhysicalConnection.getSystemProperty(string, null) : GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc." + string, null);
        }
        if (string3 == null) {
            string3 = string2;
        }
        return string3;
    }

    private int parseConnectionProperty(@Blind(value=PropertiesBlinder.class) Properties properties, String string, byte by, int n2) throws SQLException {
        int n3 = n2;
        String string2 = this.parseConnectionProperty(properties, string, by, null);
        if (string2 != null) {
            try {
                n3 = Integer.parseInt(string2);
            }
            catch (NumberFormatException numberFormatException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is '" + string + "' and value is '" + string2 + "'").fillInStackTrace();
            }
        }
        return n3;
    }

    private long parseConnectionProperty(@Blind(value=PropertiesBlinder.class) Properties properties, String string, byte by, long l2) throws SQLException {
        long l3 = l2;
        String string2 = this.parseConnectionProperty(properties, string, by, null);
        if (string2 != null) {
            try {
                l3 = Long.parseLong(string2);
            }
            catch (NumberFormatException numberFormatException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is '" + string + "' and value is '" + string2 + "'").fillInStackTrace();
            }
        }
        return l3;
    }

    private boolean parseConnectionProperty(@Blind(value=PropertiesBlinder.class) Properties properties, String string, byte by, boolean bl) throws SQLException {
        boolean bl2 = bl;
        String string2 = this.parseConnectionProperty(properties, string, by, null);
        if (string2 != null) {
            if (string2.equalsIgnoreCase("false")) {
                bl2 = false;
            } else if (string2.equalsIgnoreCase("true")) {
                bl2 = true;
            }
        }
        return bl2;
    }

    protected void readOCIConnectionPoolProperties(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        this.ociConnectionPoolMinLimit = this.parseConnectionProperty(properties, "connpool_min_limit", (byte)1, 0);
        this.ociConnectionPoolMaxLimit = this.parseConnectionProperty(properties, "connpool_max_limit", (byte)1, 0);
        this.ociConnectionPoolIncrement = this.parseConnectionProperty(properties, "connpool_increment", (byte)1, 0);
        this.ociConnectionPoolTimeout = this.parseConnectionProperty(properties, "connpool_timeout", (byte)1, 0);
        this.ociConnectionPoolNoWait = this.parseConnectionProperty(properties, "connpool_nowait", (byte)1, false);
        this.ociConnectionPoolTransactionDistributed = this.parseConnectionProperty(properties, "transactions_distributed", (byte)1, false);
        this.ociConnectionPoolLogonMode = this.parseConnectionProperty(properties, "connection_pool", (byte)1, null);
        this.ociConnectionPoolIsPooling = this.parseConnectionProperty(properties, "is_connection_pooling", (byte)1, false);
        this.ociConnectionPoolObject = this.parseOCIConnectionPoolProperty(properties, "connpool_object", null);
        this.ociConnectionPoolConnID = this.parseOCIConnectionPoolProperty(properties, "connection_id", null);
        this.ociConnectionPoolProxyType = this.parseConnectionProperty(properties, "proxytype", (byte)1, null);
        this.ociConnectionPoolProxyNumRoles = (Integer)this.parseOCIConnectionPoolProperty(properties, "proxy_num_roles", 0);
        this.ociConnectionPoolProxyRoles = this.parseOCIConnectionPoolProperty(properties, "proxy_roles", null);
        this.ociConnectionPoolProxyUserName = this.parseConnectionProperty(properties, "proxy_user_name", (byte)1, null);
        this.ociConnectionPoolProxyPassword = OpaqueString.newOpaqueString(this.parseConnectionProperty(properties, "proxy_password", (byte)1, null));
        this.ociConnectionPoolProxyDistinguishedName = this.parseConnectionProperty(properties, "proxy_distinguished_name", (byte)1, null);
        this.ociConnectionPoolProxyCertificate = this.parseOCIConnectionPoolProperty(properties, "proxy_certificate", null);
    }

    private Object parseOCIConnectionPoolProperty(@Blind(value=PropertiesBlinder.class) Properties properties, String string, Object object) throws SQLException {
        Object v2;
        Object object2 = object;
        if (properties != null && (v2 = properties.get(string)) != null) {
            object2 = v2;
        }
        return object2;
    }

    protected void readConnectionProperties(String string, @Blind(value=PropertiesBlinder.class) Properties properties, @Blind(value=PropertiesBlinder.class) Properties properties2) throws SQLException {
        String string2 = null;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.RetainV9LongBindBehavior");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.RetainV9LongBindBehavior", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.RetainV9LongBindBehavior");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.retainV9BindBehavior = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("user")) == null) {
            string2 = properties.getProperty("oracle.jdbc.user");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.user", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("user")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.user");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.userName = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.newPassword");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.newPasswordValue = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("database")) == null) {
            string2 = properties.getProperty("oracle.jdbc.database");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.database", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("database")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.database");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.database = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("autoCommit")) == null) {
            string2 = properties.getProperty("oracle.jdbc.autoCommit");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.autoCommit", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("autoCommit")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.autoCommit");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.defaultautocommit = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.bindUseDBA");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.bindUseDBA", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.bindUseDBA");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.bindUseDBA = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("protocol")) == null) {
            string2 = properties.getProperty("oracle.jdbc.protocol");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.protocol", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("protocol")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.protocol");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.protocol = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.StreamChunkSize");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.StreamChunkSize", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.StreamChunkSize");
        }
        if (string2 == null) {
            string2 = "32767";
        }
        try {
            this.streamChunkSize = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'streamChunkSize'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("SetFloatAndDoubleUseBinary")) == null) {
            string2 = properties.getProperty("oracle.jdbc.SetFloatAndDoubleUseBinary");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.SetFloatAndDoubleUseBinary", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("SetFloatAndDoubleUseBinary")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.SetFloatAndDoubleUseBinary");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.setFloatAndDoubleUseBinary = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("v$session.terminal")) == null) {
            string2 = properties.getProperty("oracle.jdbc.v$session.terminal");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.v$session.terminal", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("v$session.terminal")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.v$session.terminal");
        }
        if (string2 == null) {
            string2 = "unknown";
        }
        this.thinVsessionTerminal = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("v$session.machine")) == null) {
            string2 = properties.getProperty("oracle.jdbc.v$session.machine");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.v$session.machine", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("v$session.machine")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.v$session.machine");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinVsessionMachine = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("v$session.osuser")) == null) {
            string2 = properties.getProperty("oracle.jdbc.v$session.osuser");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.v$session.osuser", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("v$session.osuser")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.v$session.osuser");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinVsessionOsuser = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("v$session.program")) == null) {
            string2 = properties.getProperty("oracle.jdbc.v$session.program");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.v$session.program", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("v$session.program")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.v$session.program");
        }
        if (string2 == null) {
            string2 = "JDBC Thin Client";
        }
        this.thinVsessionProgram = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("v$session.process")) == null) {
            string2 = properties.getProperty("oracle.jdbc.v$session.process");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.v$session.process", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("v$session.process")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.v$session.process");
        }
        if (string2 == null) {
            string2 = "1234";
        }
        this.thinVsessionProcess = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("v$session.iname")) == null) {
            string2 = properties.getProperty("oracle.jdbc.v$session.iname");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.v$session.iname", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("v$session.iname")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.v$session.iname");
        }
        if (string2 == null) {
            string2 = "jdbc_ttc_impl";
        }
        this.thinVsessionIname = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("v$session.ename")) == null) {
            string2 = properties.getProperty("oracle.jdbc.v$session.ename");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.v$session.ename", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("v$session.ename")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.v$session.ename");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinVsessionEname = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.profile");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.profile", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.profile");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetProfile = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.authentication_services");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.authentication_services", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.authentication_services");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetAuthenticationServices = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.kerberos5_mutual_authentication");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.kerberos5_mutual_authentication", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.kerberos5_mutual_authentication");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetAuthenticationKrb5Mutual = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.kerberos5_cc_name");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.kerberos5_cc_name", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.kerberos5_cc_name");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetAuthenticationKrb5CcName = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.KerberosRealm");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.KerberosRealm", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.KerberosRealm");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetAuthenticationKrbRealm = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.KerberosJaasLoginModule");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.KerberosJaasLoginModule", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.KerberosJaasLoginModule");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetAuthenticationKrbJaasLoginModule = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.setFIPSMode");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.setFIPSMode", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.setFIPSMode");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetSetFIPSMode = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.allow_weak_crypto");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.allow_weak_crypto", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.allow_weak_crypto");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.thinNetAllowWeakCrypto = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.encryption_client");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.encryption_client", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.encryption_client");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetEncryptionLevel = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.encryption_types_client");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.encryption_types_client", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.encryption_types_client");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetEncryptionTypes = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.crypto_checksum_client");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.crypto_checksum_client", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.crypto_checksum_client");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetChecksumLevel = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.crypto_checksum_types_client");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.crypto_checksum_types_client", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.crypto_checksum_types_client");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetChecksumTypes = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.crypto_seed");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.crypto_seed", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.crypto_seed");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetCryptoSeed = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.useJCEAPI");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.useJCEAPI", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.useJCEAPI");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.thinUseJCEAPI = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.TcpNoDelay");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.TcpNoDelay", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.TcpNoDelay");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.thinTcpNoDelay = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.ReadTimeout");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ReadTimeout", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.ReadTimeout");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinReadTimeout = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.OUTBOUND_CONNECT_TIMEOUT");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.OUTBOUND_CONNECT_TIMEOUT", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.OUTBOUND_CONNECT_TIMEOUT");
        }
        if (string2 == null) {
            string2 = "0";
        }
        this.thinOutboundConnectTimeout = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.CONNECT_TIMEOUT");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.CONNECT_TIMEOUT", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.CONNECT_TIMEOUT");
        }
        if (string2 == null) {
            string2 = "0";
        }
        this.thinNetConnectTimeout = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.disableOob");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.disableOob", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.disableOob");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.thinNetDisableOutOfBandBreak = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.useZeroCopyIO");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.useZeroCopyIO", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.useZeroCopyIO");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.thinNetUseZeroCopyIO = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.use1900AsYearForTime");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.use1900AsYearForTime", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.use1900AsYearForTime");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.use1900AsYearForTime = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.timestampTzInGmt");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.timestampTzInGmt", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.timestampTzInGmt");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.timestamptzInGmt = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.timezoneAsRegion");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.timezoneAsRegion", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.timezoneAsRegion");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.timezoneAsRegion = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ssl_certificate_alias");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ssl_certificate_alias", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ssl_certificate_alias");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinSslCertificateAlias = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ssl_server_dn_match");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ssl_server_dn_match", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ssl_server_dn_match");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinSslServerDnMatch = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ssl_server_cert_dn");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ssl_server_cert_dn", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ssl_server_cert_dn");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinSslServerCertDn = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ssl_version");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ssl_version", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ssl_version");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinSslVersion = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ssl_cipher_suites");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ssl_cipher_suites", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ssl_cipher_suites");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinSslCipherSuites = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("javax.net.ssl.keyStore");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("javax.net.ssl.keyStore", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("javax.net.ssl.keyStore");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinJavaxNetSslKeystore = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("javax.net.ssl.keyStoreType");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("javax.net.ssl.keyStoreType", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("javax.net.ssl.keyStoreType");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinJavaxNetSslKeystoretype = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("javax.net.ssl.keyStorePassword");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("javax.net.ssl.keyStorePassword", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("javax.net.ssl.keyStorePassword");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinJavaxNetSslKeystorepassword = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("javax.net.ssl.trustStore");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("javax.net.ssl.trustStore", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("javax.net.ssl.trustStore");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinJavaxNetSslTruststore = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("javax.net.ssl.trustStoreType");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("javax.net.ssl.trustStoreType", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("javax.net.ssl.trustStoreType");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinJavaxNetSslTruststoretype = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("javax.net.ssl.trustStorePassword");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("javax.net.ssl.trustStorePassword", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("javax.net.ssl.trustStorePassword");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinJavaxNetSslTruststorepassword = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("ssl.keyManagerFactory.algorithm")) == null) {
            string2 = properties.getProperty("oracle.jdbc.ssl.keyManagerFactory.algorithm");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ssl.keyManagerFactory.algorithm", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("ssl.keyManagerFactory.algorithm")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.ssl.keyManagerFactory.algorithm");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinSslKeymanagerfactoryAlgorithm = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("ssl.trustManagerFactory.algorithm")) == null) {
            string2 = properties.getProperty("oracle.jdbc.ssl.trustManagerFactory.algorithm");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ssl.trustManagerFactory.algorithm", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("ssl.trustManagerFactory.algorithm")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.ssl.trustManagerFactory.algorithm");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinSslTrustmanagerfactoryAlgorithm = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.oldSyntax");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.oldSyntax", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.oldSyntax");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetOldsyntax = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("com.sun.jndi.ldap.connect.timeout")) == null) {
            string2 = properties.getProperty("oracle.jdbc.com.sun.jndi.ldap.connect.timeout");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.com.sun.jndi.ldap.connect.timeout", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("com.sun.jndi.ldap.connect.timeout")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.com.sun.jndi.ldap.connect.timeout");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinJndiLdapConnectTimeout = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("com.sun.jndi.ldap.read.timeout")) == null) {
            string2 = properties.getProperty("oracle.jdbc.com.sun.jndi.ldap.read.timeout");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.com.sun.jndi.ldap.read.timeout", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("com.sun.jndi.ldap.read.timeout")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.com.sun.jndi.ldap.read.timeout");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinJndiLdapReadTimeout = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.wallet_location");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.wallet_location", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.wallet_location");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.walletLocation = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.wallet_password");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.wallet_password", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.wallet_password");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.walletPassword = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.supportedCiphers");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.supportedCiphers", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.supportedCiphers");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslCipherSuites = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.supportedVersions");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.supportedVersions", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.supportedVersions");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslVersions = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.keyStore");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.keyStore", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.keyStore");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslKeyStore = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.keyStoreType");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.keyStoreType", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.keyStoreType");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslKeyStoreType = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.keyStorePassword");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.keyStorePassword", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.keyStorePassword");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslKeyStorePwd = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.keyManagerFactory.algorithm");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.keyManagerFactory.algorithm", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.keyManagerFactory.algorithm");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslKeyManagerFactoryAlgo = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.trustStore");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.trustStore", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.trustStore");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslTrustStore = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.trustStoreType");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.trustStoreType", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.trustStoreType");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslTrustStoreType = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.trustStorePassword");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.trustStorePassword", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.trustStorePassword");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslTrustStorePassword = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.trustManagerFactory.algorithm");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.trustManagerFactory.algorithm", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.trustManagerFactory.algorithm");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslTrustManagerFactoryAlgo = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.walletLocation");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.walletLocation", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.walletLocation");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslWalletLocation = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.walletPassword");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.walletPassword", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.walletPassword");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSslWalletPassword = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.security.authentication");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.security.authentication", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.security.authentication");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSecurityAuthentication = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.security.principal");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.security.principal", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.security.principal");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSecurityPrincipal = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.security.credentials");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.security.credentials", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.security.credentials");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapSecurityCredetials = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ldap.ssl.ssl_context_protocol");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ldap.ssl.ssl_context_protocol", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ldap.ssl.ssl_context_protocol");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinLdapsslContextProtocol = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.proxyClientName");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.proxyClientName", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.proxyClientName");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.proxyClientName = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.useNio");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.useNio", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.useNio");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.useNio = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.httpsProxyHost");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.httpsProxyHost", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.httpsProxyHost");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinHttpsProxyHost = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.httpsProxyPort");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.httpsProxyPort", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.httpsProxyPort");
        }
        if (string2 == null) {
            string2 = "0";
        }
        try {
            this.thinHttpsProxyPort = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'thinHttpsProxyPort'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.connectionIdPrefix");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.connectionIdPrefix", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.connectionIdPrefix");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.thinNetConnectionIdPrefix = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("JDBCDriverCharSetId")) == null) {
            string2 = properties.getProperty("oracle.jdbc.JDBCDriverCharSetId");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.JDBCDriverCharSetId", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("JDBCDriverCharSetId")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.JDBCDriverCharSetId");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.ociDriverCharset = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.editionName");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.editionName", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.editionName");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.editionName = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.thinLogonCapability");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.thinLogonCapability", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.thinLogonCapability");
        }
        if (string2 == null) {
            string2 = "o5";
        }
        this.logonCap = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.useOCIDefaultDefines");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.useOCIDefaultDefines", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.useOCIDefaultDefines");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.useOCIDefaultDefines = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("internal_logon")) == null) {
            string2 = properties.getProperty("oracle.jdbc.internal_logon");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.internal_logon", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("internal_logon")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.internal_logon");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.internalLogon = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.createDescriptorUseCurrentSchemaForSchemaName");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.createDescriptorUseCurrentSchemaForSchemaName", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.createDescriptorUseCurrentSchemaForSchemaName");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.createDescriptorUseCurrentSchemaForSchemaName = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("OCISvcCtxHandle")) == null) {
            string2 = properties.getProperty("oracle.jdbc.OCISvcCtxHandle");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.OCISvcCtxHandle", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("OCISvcCtxHandle")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.OCISvcCtxHandle");
        }
        if (string2 == null) {
            string2 = "0";
        }
        try {
            this.ociSvcCtxHandle = Long.parseLong(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'ociSvcCtxHandle'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("OCIEnvHandle")) == null) {
            string2 = properties.getProperty("oracle.jdbc.OCIEnvHandle");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.OCIEnvHandle", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("OCIEnvHandle")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.OCIEnvHandle");
        }
        if (string2 == null) {
            string2 = "0";
        }
        try {
            this.ociEnvHandle = Long.parseLong(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'ociEnvHandle'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("OCIErrHandle")) == null) {
            string2 = properties.getProperty("oracle.jdbc.OCIErrHandle");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.OCIErrHandle", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("OCIErrHandle")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.OCIErrHandle");
        }
        if (string2 == null) {
            string2 = "0";
        }
        try {
            this.ociErrHandle = Long.parseLong(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'ociErrHandle'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("prelim_auth")) == null) {
            string2 = properties.getProperty("oracle.jdbc.prelim_auth");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.prelim_auth", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("prelim_auth")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.prelim_auth");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.prelimAuth = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.jmsNotification");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.jmsNotification", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.jmsNotification");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.jmsNotificationConnection = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.ociNlsLangBackwardCompatible");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ociNlsLangBackwardCompatible", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.ociNlsLangBackwardCompatible");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.nlsLangBackdoor = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("OCINewPassword")) == null) {
            string2 = properties.getProperty("oracle.jdbc.OCINewPassword");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.OCINewPassword", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("OCINewPassword")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.OCINewPassword");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.setNewPassword = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("defaultExecuteBatch")) == null) {
            string2 = properties.getProperty("oracle.jdbc.defaultExecuteBatch");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.defaultExecuteBatch", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("defaultExecuteBatch")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.defaultExecuteBatch");
        }
        if (string2 == null) {
            string2 = "1";
        }
        try {
            this.defaultExecuteBatch = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'defaultExecuteBatch'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("defaultRowPrefetch")) == null) {
            string2 = properties.getProperty("oracle.jdbc.defaultRowPrefetch");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.defaultRowPrefetch", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("defaultRowPrefetch")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.defaultRowPrefetch");
        }
        if (string2 == null) {
            string2 = "10";
        }
        try {
            this.defaultRowPrefetch = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'defaultRowPrefetch'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.defaultLobPrefetchSize");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.defaultLobPrefetchSize", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.defaultLobPrefetchSize");
        }
        if (string2 == null) {
            string2 = "32768";
        }
        try {
            this.defaultLobPrefetchSize = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'defaultLobPrefetchSize'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.enableDataInLocator");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.enableDataInLocator", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.enableDataInLocator");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.enableDataInLocator = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.enableReadDataInLocator");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.enableReadDataInLocator", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.enableReadDataInLocator");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.enableReadDataInLocator = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.overrideEnableReadDataInLocator");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.overrideEnableReadDataInLocator", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.overrideEnableReadDataInLocator");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.overrideEnableReadDataInLocator = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("remarksReporting")) == null) {
            string2 = properties.getProperty("oracle.jdbc.remarksReporting");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.remarksReporting", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("remarksReporting")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.remarksReporting");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.reportRemarks = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("includeSynonyms")) == null) {
            string2 = properties.getProperty("oracle.jdbc.includeSynonyms");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.includeSynonyms", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("includeSynonyms")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.includeSynonyms");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.includeSynonyms = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("restrictGetTables")) == null) {
            string2 = properties.getProperty("oracle.jdbc.restrictGetTables");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.restrictGetTables", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("restrictGetTables")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.restrictGetTables");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.restrictGettables = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("AccumulateBatchResult")) == null) {
            string2 = properties.getProperty("oracle.jdbc.AccumulateBatchResult");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.AccumulateBatchResult", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("AccumulateBatchResult")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.AccumulateBatchResult");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.accumulateBatchResult = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("useFetchSizeWithLongColumn")) == null) {
            string2 = properties.getProperty("oracle.jdbc.useFetchSizeWithLongColumn");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.useFetchSizeWithLongColumn", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("useFetchSizeWithLongColumn")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.useFetchSizeWithLongColumn");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.useFetchSizeWithLongColumn = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("processEscapes")) == null) {
            string2 = properties.getProperty("oracle.jdbc.processEscapes");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.processEscapes", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("processEscapes")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.processEscapes");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.processEscapes = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("fixedString")) == null) {
            string2 = properties.getProperty("oracle.jdbc.fixedString");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.fixedString", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("fixedString")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.fixedString");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.fixedString = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("defaultNChar")) == null) {
            string2 = properties.getProperty("oracle.jdbc.defaultNChar");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.defaultNChar", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("defaultNChar")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.defaultNChar");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.defaultnchar = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.internal.permitBindDateDefineTimestampMismatch");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.internal.permitBindDateDefineTimestampMismatch", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.internal.permitBindDateDefineTimestampMismatch");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.permitTimestampDateMismatch = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("RessourceManagerId")) == null) {
            string2 = properties.getProperty("oracle.jdbc.RessourceManagerId");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.RessourceManagerId", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("RessourceManagerId")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.RessourceManagerId");
        }
        if (string2 == null) {
            string2 = "0000";
        }
        this.resourceManagerId = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("disableDefineColumnType")) == null) {
            string2 = properties.getProperty("oracle.jdbc.disableDefineColumnType");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.disableDefineColumnType", null);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("disableDefineColumnType")) == null) {
            string2 = properties2.getProperty("oracle.jdbc.disableDefineColumnType");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.disableDefinecolumntype = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.convertNcharLiterals");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.convertNcharLiterals", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.convertNcharLiterals");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.convertNcharLiterals = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.autoCommitSpecCompliant");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.autoCommitSpecCompliant", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.autoCommitSpecCompliant");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.autoCommitSpecCompliant = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.JDBCStandardBehavior");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.JDBCStandardBehavior", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.JDBCStandardBehavior");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.jdbcStandardBehavior = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.J2EE13Compliant");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.J2EE13Compliant", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.J2EE13Compliant");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.j2ee13Compliant = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("DMSName")) == null) {
            string2 = properties.getProperty("oracle.jdbc.DMSName");
        }
        if (string2 == null) {
            string2 = "Driver";
        }
        this.dmsParentName = string2;
        string2 = null;
        if (properties != null && (string2 = properties.getProperty("DMSType")) == null) {
            string2 = properties.getProperty("oracle.jdbc.DMSType");
        }
        if (string2 == null) {
            string2 = "JDBC_Driver";
        }
        this.dmsParentType = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.DMSStatementMetrics");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.dmsStmtMetrics = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.DMSStatementCachingMetrics");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.dmsStmtCachingMetrics = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.mapDateToTimestamp");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.mapDateToTimestamp", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.mapDateToTimestamp");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.mapDateToTimestamp = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.useThreadLocalBufferCache");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.useThreadLocalBufferCache", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.useThreadLocalBufferCache");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.useThreadLocalBufferCache = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.driverNameAttribute");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.driverNameAttribute", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.driverNameAttribute");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.driverNameAttribute = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.maxCachedBufferSize");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.maxCachedBufferSize", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.maxCachedBufferSize");
        }
        if (string2 == null) {
            string2 = "30";
        }
        try {
            this.maxCachedBufferSize = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'maxCachedBufferSize'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.implicitStatementCacheSize");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.implicitStatementCacheSize", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.implicitStatementCacheSize");
        }
        if (string2 == null) {
            string2 = "0";
        }
        try {
            this.implicitStatementCacheSize = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'implicitStatementCacheSize'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.LobStreamPosStandardCompliant");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.LobStreamPosStandardCompliant", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.LobStreamPosStandardCompliant");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.lobStreamPosStandardCompliant = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.strictASCIIConversion");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.strictASCIIConversion", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.strictASCIIConversion");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.isStrictAsciiConversion = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.quickASCIIConversion");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.quickASCIIConversion", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.quickASCIIConversion");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.isQuickAsciiConversion = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.DRCPConnectionClass");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.DRCPConnectionClass", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.DRCPConnectionClass");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.drcpConnectionClass = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.DRCPTagName");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.drcpTagName = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.DRCPConnectionPurity");
        }
        if (string2 == null) {
            string2 = "SELF";
        }
        this.drcpConnectionPurity = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.UseDRCPMultipletag");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.useDRCPMultipletag = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.DRCPPLSQLCallback");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.drcpPLSQLCallback = string2;
        string2 = null;
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.blockSourceImplementation", null);
        }
        if (string2 == null) {
            string2 = "THREADED";
        }
        this.blockSourceImpl = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.thinForceDNSLoadBalancing");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.thinForceDNSLoadBalancing", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.thinForceDNSLoadBalancing");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.thinForceDnsLoadBalancing = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.enableTempLobRefCnt");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.enableTempLobRefCnt", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.enableTempLobRefCnt");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.enableTempLobRefCnt = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.keepAlive");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.keepAlive", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.keepAlive");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.keepAlive = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.sqlTranslationProfile");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.sqlTranslationProfile", null);
        }
        if (string2 == null) {
            string2 = null;
        }
        this.sqlTranslationProfile = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.sqlErrorTranslationFile");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.sqlErrorTranslationFile", null);
        }
        if (string2 == null) {
            string2 = null;
        }
        this.sqlErrorTranslationFile = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.ignoreReplayContextFromAuthentication");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.ignoreReplayContextFromAuthentication = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.javaNetNio");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.javaNetNio", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.javaNetNio");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.javaNetNio = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.nsDirectBuffer");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.nsDirectBuffer", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.nsDirectBuffer");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.nsDirectBuffer = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.plsqlVarcharParameter4KOnly");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.plsqlVarcharParameter4KOnly", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.plsqlVarcharParameter4KOnly");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.plsqlVarcharParameter4KOnly = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.targetInstanceName");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.targetInstanceName", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.targetInstanceName");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.targetInstanceName = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.targetServiceName");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.targetServiceName = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.targetShardingKey");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.targetShardingKey = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.targetSuperShardingKey");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.targetSuperShardingKey = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.readOnlyInstanceAllowed");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.readOnlyInstanceAllowed", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.readOnlyInstanceAllowed");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.readOnlyInstanceAllowed = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.enableOCIFastApplicationNotification");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.enableOCIFastApplicationNotification", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.enableOCIFastApplicationNotification");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.enableOCIFAN = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.enableResultSetCache");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.enableResultSetCache", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.enableResultSetCache");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.isResultSetCacheEnabled_IGNORED = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.enableResultSetCache");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.enableResultSetCache", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.enableResultSetCache");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.isResultSetCacheEnabled_IGNORED2 = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.enableQueryResultCache");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.enableQueryResultCache", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.enableQueryResultCache");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.isResultSetCacheEnabled = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.backwardCompatibileUpdateableResultSet");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.backwardCompatibileUpdateableResultSet", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.backwardCompatibileUpdateableResultSet");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.isOldUpdateableResultSet = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.allowedLogonVersion");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.allowedLogonVersion", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.allowedLogonVersion");
        }
        if (string2 == null) {
            string2 = "8";
        }
        this.allowedLogonVersion = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.commitOption");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.commitOption", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.commitOption");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.commitOptionProperty = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.calculateChecksum");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.calculateChecksum", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.calculateChecksum");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.calculateChecksumProperty = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.javaNetLocalIPForMsgq");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.javaNetLocalIPForMsgq", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.javaNetLocalIPForMsgq");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.javaNetLocalIPForMsgq = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.javaNetMsgqTransport");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.javaNetMsgqTransport", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.javaNetMsgqTransport");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.javaNetMsgqTransport = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.javaNetMsgqBusyWait");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.javaNetMsgqBusyWait", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.javaNetMsgqBusyWait");
        }
        if (string2 == null) {
            string2 = "40";
        }
        try {
            this.javaNetMsgqBusyWait = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'javaNetMsgqBusyWait'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.javaNetMsgqKernelWait");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.javaNetMsgqKernelWait", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.javaNetMsgqKernelWait");
        }
        if (string2 == null) {
            string2 = "50";
        }
        try {
            this.javaNetMsgqKernelWait = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'javaNetMsgqKernelWait'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.DOWN_HOSTS_TIMEOUT");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.DOWN_HOSTS_TIMEOUT", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.DOWN_HOSTS_TIMEOUT");
        }
        if (string2 == null) {
            string2 = "600";
        }
        try {
            this.downHostsTimeout = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'downHostsTimeout'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.fanEnabled");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.fanEnabled", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.fanEnabled");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.fanEnabled = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.tns_admin");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.tns_admin", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.tns_admin");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.tnsAdmin = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.networkCompression");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.networkCompression", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.networkCompression");
        }
        if (string2 == null) {
            string2 = "off";
        }
        this.networkCompression = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.networkCompressionLevels");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.networkCompressionLevels", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.networkCompressionLevels");
        }
        if (string2 == null) {
            string2 = "(high)";
        }
        this.networkCompressionLevels = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.networkCompressionThreshold");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.networkCompressionThreshold", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.networkCompressionThreshold");
        }
        if (string2 == null) {
            string2 = "1024";
        }
        try {
            this.networkCompressionThreshold = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'networkCompressionThreshold'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.config.file");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.config.file", null);
        }
        if (string2 == null) {
            string2 = null;
        }
        this.configFile = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.websocketUser");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.websocketUser", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.websocketUser");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.websocketUser = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.websocketPassword");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.websocketPassword", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.websocketPassword");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.websocketPassword = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.socksProxyHost");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.socksProxyHost", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.socksProxyHost");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.socksProxyHost = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.socksProxyPort");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.socksProxyPort", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.socksProxyPort");
        }
        if (string2 == null) {
            string2 = "1080";
        }
        try {
            this.socksProxyPort = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'socksProxyPort'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.socksRemoteDNS");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.socksRemoteDNS", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.socksRemoteDNS");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.socksRemoteDNS = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.defaultConnectionValidation");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.defaultConnectionValidation", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.defaultConnectionValidation");
        }
        if (string2 == null) {
            string2 = "NETWORK";
        }
        try {
            this.defaultConnectionValidation = OracleConnection.ConnectionValidation.valueOf(string2);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'defaultConnectionValidation'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.enableACSupport");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.enableACSupport", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.enableACSupport");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.enableACSupport = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.enableTGSupport");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.enableTGSupport", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.enableTGSupport");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.enableTGSupport = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.enableImplicitRequests");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.enableImplicitRequests", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.enableImplicitRequests");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.enableImplicitRequests = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.DRCPMultiplexingInRequestAPIs");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.DRCPMultiplexingInRequestAPIs", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.DRCPMultiplexingInRequestAPIs");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.drcpMultiplexingInRequestAPIs = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.continueBatchOnError");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.continueBatchOnError", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.continueBatchOnError");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.continueBatchOnError = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.TCP_KEEPIDLE");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.TCP_KEEPIDLE", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.TCP_KEEPIDLE");
        }
        if (string2 == null) {
            string2 = "-1";
        }
        try {
            this.tcpKeepIdle = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'tcpKeepIdle'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.TCP_KEEPINTERVAL");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.TCP_KEEPINTERVAL", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.TCP_KEEPINTERVAL");
        }
        if (string2 == null) {
            string2 = "-1";
        }
        try {
            this.tcpKeepInterval = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'tcpKeepInterval'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.useShardingDriverConnection");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.useShardingDriverConnection", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.useShardingDriverConnection");
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.useShardingDriverConnection = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.TCP_KEEPCOUNT");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.TCP_KEEPCOUNT", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.TCP_KEEPCOUNT");
        }
        if (string2 == null) {
            string2 = "-1";
        }
        try {
            this.tcpKeepCount = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'tcpKeepCount'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.replay.protectedRequestSizeLimit");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.replay.protectedRequestSizeLimit", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.replay.protectedRequestSizeLimit");
        }
        if (string2 == null) {
            string2 = "2147483647";
        }
        try {
            this.requestSizeLimit = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'requestSizeLimit'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.ons.walletfile");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ons.walletfile", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.ons.walletfile");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.onsWalletFile = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.ons.walletpassword");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ons.walletpassword", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.ons.walletpassword");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.onsWalletPassword = OpaqueString.newOpaqueString(string2);
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.ons.protocol");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ons.protocol", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.ons.protocol");
        }
        if (string2 == null) {
            string2 = "TCP";
        }
        this.onsProtocol = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.loginTimeout");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.loginTimeout", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.loginTimeout");
        }
        if (string2 == null) {
            string2 = "0";
        }
        try {
            this.loginTimeout = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "Property is 'loginTimeout'").fillInStackTrace();
        }
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.inbandNotification");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.inbandNotification", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.inbandNotification");
        }
        if (string2 == null) {
            string2 = "true";
        }
        this.inbandNotification = string2 != null && string2.equalsIgnoreCase("true");
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.net.ssl_context_protocol");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.net.ssl_context_protocol", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.net.ssl_context_protocol");
        }
        if (string2 == null) {
            string2 = "TLS";
        }
        this.sslContextProtocol = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.tokenAuthentication");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.tokenAuthentication", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.tokenAuthentication");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.tokenAuthentication = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.tokenLocation");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.tokenLocation", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.tokenLocation");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.tokenLocation = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.passwordAuthentication");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.passwordAuthentication", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.passwordAuthentication");
        }
        if (string2 == null) {
            string2 = "PASSWORD_VERIFIER";
        }
        this.passwordAuthentication = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.ociIamUrl");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ociIamUrl", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.ociIamUrl");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.ociIamUrl = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.ociTenancy");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ociTenancy", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.ociTenancy");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.ociTenancy = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.ociCompartment");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ociCompartment", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.ociCompartment");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.ociCompartment = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.ociDatabase");
        }
        if (string2 == null) {
            string2 = GeneratedPhysicalConnection.getSystemProperty("oracle.jdbc.ociDatabase", null);
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.ociDatabase");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.ociDatabase = string2;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.accessToken");
        }
        if (string2 == null && properties2 != null) {
            string2 = properties2.getProperty("oracle.jdbc.accessToken");
        }
        if (string2 == null) {
            string2 = null;
        }
        this.accessToken = OpaqueString.newOpaqueString(string2);
        this.readDeprecatedConnectionProperties(properties);
    }
}

