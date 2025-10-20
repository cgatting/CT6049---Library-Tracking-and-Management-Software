/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.datasource.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.net.ssl.SSLContext;
import oracle.jdbc.AccessToken;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleHostnameResolver;
import oracle.jdbc.datasource.impl.OracleConnectionBuilderImpl;
import oracle.jdbc.driver.ClioSupport;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleDriver;
import oracle.jdbc.internal.AbstractConnectionBuilder;
import oracle.jdbc.internal.CompletionStageUtil;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Log;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.proxy.ProxyFactory;
import oracle.jdbc.replay.ReplayStatistics;
import oracle.jdbc.replay.driver.NonTxnReplayableArray;
import oracle.jdbc.replay.driver.NonTxnReplayableBase;
import oracle.jdbc.replay.driver.NonTxnReplayableBfile;
import oracle.jdbc.replay.driver.NonTxnReplayableBlob;
import oracle.jdbc.replay.driver.NonTxnReplayableClob;
import oracle.jdbc.replay.driver.NonTxnReplayableConnection;
import oracle.jdbc.replay.driver.NonTxnReplayableNClob;
import oracle.jdbc.replay.driver.NonTxnReplayableOpaque;
import oracle.jdbc.replay.driver.NonTxnReplayableOthers;
import oracle.jdbc.replay.driver.NonTxnReplayableRef;
import oracle.jdbc.replay.driver.NonTxnReplayableResultSet;
import oracle.jdbc.replay.driver.NonTxnReplayableStatement;
import oracle.jdbc.replay.driver.NonTxnReplayableStruct;
import oracle.jdbc.replay.driver.ReplayStatisticsMBeanImpl;
import oracle.jdbc.replay.driver.StatisticsTracker;
import oracle.jdbc.replay.driver.TxnReplayableArray;
import oracle.jdbc.replay.driver.TxnReplayableBase;
import oracle.jdbc.replay.driver.TxnReplayableBfile;
import oracle.jdbc.replay.driver.TxnReplayableBlob;
import oracle.jdbc.replay.driver.TxnReplayableClob;
import oracle.jdbc.replay.driver.TxnReplayableConnection;
import oracle.jdbc.replay.driver.TxnReplayableNClob;
import oracle.jdbc.replay.driver.TxnReplayableOpaque;
import oracle.jdbc.replay.driver.TxnReplayableOthers;
import oracle.jdbc.replay.driver.TxnReplayableRef;
import oracle.jdbc.replay.driver.TxnReplayableResultSet;
import oracle.jdbc.replay.driver.TxnReplayableSqlxml;
import oracle.jdbc.replay.driver.TxnReplayableStatement;
import oracle.jdbc.replay.driver.TxnReplayableStruct;
import oracle.jdbc.replay.internal.ConnectionInitializationCallback;
import oracle.jdbc.replay.internal.ReplayableConnection;

@DefaultLogger(value="oracle.jdbc.internal.replay")
@Supports(value={Feature.HIGH_AVAILABILITY, Feature.CONN_POOL, Feature.APPLICATION_CONTINUITY})
public class OracleDataSource
implements oracle.jdbc.datasource.OracleDataSource,
oracle.jdbc.replay.internal.OracleDataSource,
Serializable,
Referenceable,
Monitor {
    static final long serialVersionUID = 3349652938965166731L;
    protected static final String DEFAULT_SERVICE_NAME = "SYS$USERS";
    protected PrintWriter logWriter = null;
    protected int loginTimeout = 0;
    protected String databaseName = null;
    protected String serviceName = null;
    protected String dataSourceName = "OracleDataSource";
    protected String description = null;
    protected String networkProtocol = "tcp";
    protected int portNumber = 0;
    protected String user = null;
    protected OpaqueString password = null;
    protected String serverName = null;
    protected String url = null;
    protected String driverType = null;
    protected String tnsEntry = null;
    protected int maxStatements = 0;
    protected boolean implicitCachingEnabled = false;
    protected boolean explicitCachingEnabled = false;
    protected boolean maxStatementsSet = false;
    protected boolean implicitCachingEnabledSet = false;
    protected boolean explicitCachingEnabledSet = false;
    protected Properties connectionProperties = null;
    public boolean isOracleDataSource = true;
    private String roleName = null;
    private boolean urlExplicit = false;
    private boolean useDefaultConnection = false;
    protected transient OracleDriver driver = new OracleDriver();
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();
    private SSLContext sslContext;
    private boolean allowSingleShardTransaction;
    private OracleHostnameResolver hostnameResolver;
    private Supplier<? extends AccessToken> tokenSupplier;
    private static ProxyFactory PROXY_FACTORY = null;
    private static ProxyFactory NON_TXN_PROXY_FACTORY = null;
    private static final Monitor proxyFactoryLock = Monitor.newInstance();
    private ConnectionInitializationCallback connectionInitializationCallback = null;
    protected AtomicBoolean isFirstConnection = new AtomicBoolean(true);
    protected static final String RECONNECT_DELAY_PROPERTY = "AUTH_FAILOVER_DELAY";
    protected static final String RECONNECT_RETRIES_PROPERTY = "AUTH_FAILOVER_RETRIES";
    protected int reconnectDelay = 10;
    protected int reconnectRetries = 30;
    protected static final String FAILOVER_TYPE_PROPERTY = "AUTH_FAILOVER_TYPE";
    protected static final int FAILOVER_TYPE_TRANSACTION = 8;
    protected static final int SESSION_STATE_CONSISTENCY_STATIC = 16;
    protected static final int FAILOVER_TYPE_AUTO = 32;
    protected boolean isTransactionReplayEnabled = false;
    protected boolean isAutoACEnabled = false;
    protected static final String SESSION_STATE_PROPERTY = "AUTH_SESSION_STATE_CONSISTENCY";
    protected boolean isReplayInDynamicMode = true;
    protected static final String FAILOVER_RESTORE_PROPERTY = "AUTH_FAILOVER_RESTORE";
    protected static final int FAILOVER_RESTORE_NONE = 0;
    protected static final int FAILOVER_RESTORE_LEVEL1 = 1;
    protected static final int FAILOVER_RESTORE_LEVEL2 = 2;
    protected static final int FAILOVER_RESTORE_AUTO = 3;
    protected ReplayableConnection.StateRestorationType stateRestorationType = ReplayableConnection.StateRestorationType.NONE;
    protected boolean isStateRestorationAuto = false;
    protected static final String INITIATION_TIMEOUT_PROPERTY = "AUTH_FAILOVER_REPLAYTIMEOUT";
    protected int replayInitiationTimeout = 300;
    protected static final String CHECKSUM_PROPERTY = "oracle.jdbc.calculateChecksum";
    protected final String clientChecksum12x = OracleConnection.ChecksumMode.CALCULATE_CHECKSUM_BINDS.toString();
    protected final String clientChecksum11203x = OracleConnection.ChecksumMode.CALCULATE_CHECKSUM_ALL.toString();
    protected static final String IGNORE_AC_CONTEXT_PROPERTY = "oracle.jdbc.ignoreReplayContextFromAuthentication";
    private static final String AC_11203_COMPATIBLE_SYSTEM_PROPERTY = "oracle.jdbc.AC11203Compatible";
    private static final String IMPLICIT_BEGIN_REQUEST_SYSTEM_PROPERTY = "oracle.jdbc.beginRequestAtConnectionCreation";
    protected static final String ENABLE_AC_SUPPORT_PROPERTY = "oracle.jdbc.enableACSupport";
    protected static final String REQUEST_SIZE_LIMIT_PROPERTY = "oracle.jdbc.replay.protectedRequestSizeLimit";
    private StatisticsTracker tracker = null;
    protected AtomicBoolean doneDumpOnMemoryPressure = new AtomicBoolean(false);
    private static final String registeredName = "com.oracle.jdbc:type=ReplayStatistics,name=";
    private static ObjectName mbeanName;
    private static final String _Copyright_2014_Oracle_All_Rights_Reserved_;
    public static final boolean TRACE = false;

    public OracleDataSource() throws SQLException {
        this.tracker = new StatisticsTracker();
        ReplayStatisticsMBeanImpl.SOLE_INSTANCE.addTrackerForDS(this.tracker);
    }

    @Override
    public Connection getConnection() throws SQLException {
        String string = null;
        OpaqueString opaqueString = OpaqueString.NULL;
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            string = this.user;
            opaqueString = this.password;
        }
        return this.getConnection(string, opaqueString);
    }

    @Override
    public Connection getConnection(String string, @Blind String string2) throws SQLException {
        return this.getConnection(string, OpaqueString.newOpaqueString(string2));
    }

    protected Connection getConnection(String string, OpaqueString opaqueString) throws SQLException {
        OracleConnectionBuilderImpl oracleConnectionBuilderImpl = (OracleConnectionBuilderImpl)((OracleConnectionBuilderImpl)this.createConnectionBuilder().user(string)).password(opaqueString);
        boolean bl = this.isACSupportPropertySet();
        return this.getConnectionInternal(oracleConnectionBuilderImpl, bl);
    }

    protected Connection getConnection(OracleConnectionBuilderImpl oracleConnectionBuilderImpl) throws SQLException {
        Properties properties;
        try (AutoCloseable autoCloseable = this.acquireCloseableLock();){
            OracleDataSource.validateGSSCredentialConfiguration(oracleConnectionBuilderImpl);
            this.makeURL();
            properties = this.connectionProperties == null ? new Properties() : (Properties)this.connectionProperties.clone();
            this.applyDataSourcePropertiesForGetConnectionWithBuilder(properties);
            OracleDataSource.applyBuilderProperties(oracleConnectionBuilderImpl, properties);
        }
        autoCloseable = this.getPhysicalConnection(properties, oracleConnectionBuilderImpl);
        if (autoCloseable == null) {
            throw (SQLException)DatabaseError.createSqlException(67).fillInStackTrace();
        }
        return autoCloseable;
    }

    private final CompletionStage<OracleConnection> getConnectionAsync(OracleConnectionBuilderImpl oracleConnectionBuilderImpl) {
        Properties properties;
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleDataSource.validateGSSCredentialConfiguration(oracleConnectionBuilderImpl);
            this.makeURL();
            properties = this.connectionProperties == null ? new Properties() : (Properties)this.connectionProperties.clone();
            this.applyDataSourcePropertiesForGetConnectionWithBuilder(properties);
            OracleDataSource.applyBuilderProperties(oracleConnectionBuilderImpl, properties);
        }
        catch (SQLException sQLException) {
            return CompletionStageUtil.failedStage(sQLException);
        }
        return this.getPhysicalConnectionAsync(properties, oracleConnectionBuilderImpl).thenApply(oracleConnection -> {
            if (oracleConnection == null) {
                throw new CompletionException(DatabaseError.createSqlException(67).fillInStackTrace());
            }
            return oracleConnection;
        });
    }

    private static final void validateGSSCredentialConfiguration(OracleConnectionBuilderImpl oracleConnectionBuilderImpl) throws SQLException {
        if (oracleConnectionBuilderImpl.getGSSCredential() != null && (oracleConnectionBuilderImpl.getUser() != null || oracleConnectionBuilderImpl.getPassword() != null && oracleConnectionBuilderImpl.getPassword() != OpaqueString.NULL)) {
            throw (SQLException)DatabaseError.createSqlException(68, "GSSCredential and user/password cannot both be set in a connection builder.").fillInStackTrace();
        }
    }

    private final void applyDataSourcePropertiesForGetConnectionWithBuilder(@Blind(value=PropertiesBlinder.class) Properties properties) {
        if (this.url != null) {
            properties.setProperty("connection_url", this.url);
        }
        if (this.loginTimeout != 0) {
            properties.setProperty("oracle.jdbc.loginTimeout", String.valueOf(this.loginTimeout));
        }
        if (this.maxStatementsSet) {
            properties.setProperty("stmt_cache_size", String.valueOf(this.maxStatements));
        }
    }

    private static final void applyBuilderProperties(OracleConnectionBuilderImpl oracleConnectionBuilderImpl, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        String string;
        OpaqueString opaqueString = oracleConnectionBuilderImpl.getPassword();
        if (oracleConnectionBuilderImpl.getUser() != null && opaqueString != null && opaqueString != OpaqueString.NULL) {
            properties.setProperty("user", oracleConnectionBuilderImpl.getUser());
            properties.setProperty("password", opaqueString.get());
        }
        if (oracleConnectionBuilderImpl.getInstanceName() != null) {
            properties.setProperty("oracle.jdbc.targetInstanceName", oracleConnectionBuilderImpl.getInstanceName());
        }
        if (oracleConnectionBuilderImpl.getServiceName() != null && !oracleConnectionBuilderImpl.getServiceName().equalsIgnoreCase(DEFAULT_SERVICE_NAME)) {
            properties.setProperty("oracle.jdbc.targetServiceName", oracleConnectionBuilderImpl.getServiceName());
        }
        if (oracleConnectionBuilderImpl.getShardingKey() != null) {
            string = oracleConnectionBuilderImpl.getShardingKey().encodeKeyinB64Format();
            properties.setProperty("oracle.jdbc.targetShardingKey", string);
        }
        if (oracleConnectionBuilderImpl.getSuperShardingKey() != null) {
            string = oracleConnectionBuilderImpl.getSuperShardingKey().encodeKeyinB64Format();
            properties.setProperty("oracle.jdbc.targetSuperShardingKey", string);
        }
        if (oracleConnectionBuilderImpl.getReadOnlyInstanceAllowed()) {
            properties.setProperty("oracle.jdbc.readOnlyInstanceAllowed", String.valueOf(oracleConnectionBuilderImpl.getReadOnlyInstanceAllowed()));
        }
    }

    private void applyDataSourcePropertiesToBuilder(AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) throws SQLException {
        if (abstractConnectionBuilder == null) {
            return;
        }
        if (this.sslContext != null && abstractConnectionBuilder.getSSLContext() == null) {
            abstractConnectionBuilder.sslContext(this.sslContext);
        }
        if (this.allowSingleShardTransaction && !abstractConnectionBuilder.getAllowSingleShardTransaction()) {
            abstractConnectionBuilder.singleShardTransactionSupport(this.allowSingleShardTransaction);
        }
        if (this.hostnameResolver != null && abstractConnectionBuilder.getHostnameResolver() == null) {
            abstractConnectionBuilder.hostnameResolver(this.hostnameResolver);
        }
        this.configureTokenSupplier(abstractConnectionBuilder);
    }

    private void configureTokenSupplier(AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) throws SQLException {
        Supplier<? extends AccessToken> supplier = this.tokenSupplier;
        if (supplier == null) {
            return;
        }
        if (this.isUserOrPasswordConfigured()) {
            throw (SQLException)DatabaseError.createSqlException(null, 1718, "DataSource configured with setTokenSupplier(Supplier) is also configured with a user name or password").fillInStackTrace();
        }
        if (abstractConnectionBuilder == null || abstractConnectionBuilder.getTokenSupplier() != null || abstractConnectionBuilder.getUser() != null || abstractConnectionBuilder.getPassword() != null && !abstractConnectionBuilder.getPassword().isNull()) {
            return;
        }
        abstractConnectionBuilder.setTokenSupplier(supplier);
    }

    private boolean isUserOrPasswordConfigured() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.user != null || this.password != null && !this.password.isNull() || this.connectionProperties != null && (this.connectionProperties.containsKey("user") || this.connectionProperties.containsKey("oracle.jdbc.user") || this.connectionProperties.containsKey("password") || this.connectionProperties.containsKey("oracle.jdbc.password"));
            return bl;
        }
    }

    protected Connection getPhysicalConnection(@Blind(value=PropertiesBlinder.class) Properties properties, AbstractConnectionBuilder abstractConnectionBuilder) throws SQLException {
        boolean bl;
        String string = properties.getProperty("connection_url", this.url);
        String string2 = properties.getProperty("user");
        boolean bl2 = abstractConnectionBuilder != null && abstractConnectionBuilder.getGSSCredential() != null;
        Properties properties2 = this.createPropertiesForPhysicalConnection(properties, bl2);
        try (AutoCloseable autoCloseable = this.acquireCloseableLock();){
            bl = this.useDefaultConnection;
            if (this.driver == null) {
                this.driver = new OracleDriver();
            }
            this.applyDataSourcePropertiesToBuilder(abstractConnectionBuilder);
        }
        autoCloseable = bl ? this.driver.defaultConnection() : this.driver.connect(string, properties2, abstractConnectionBuilder);
        if (autoCloseable == null) {
            throw (SQLException)DatabaseError.createSqlException(67).fillInStackTrace();
        }
        this.initializeStatementCacheForPhysicalConnection((oracle.jdbc.internal.OracleConnection)autoCloseable, properties);
        return autoCloseable;
    }

    private final CompletionStage<OracleConnection> getPhysicalConnectionAsync(@Blind(value=PropertiesBlinder.class) Properties properties, AbstractConnectionBuilder abstractConnectionBuilder) {
        String string = properties.getProperty("connection_url", this.url);
        String string2 = properties.getProperty("user");
        boolean bl = abstractConnectionBuilder != null && abstractConnectionBuilder.getGSSCredential() != null;
        Properties properties2 = this.createPropertiesForPhysicalConnection(properties, bl);
        if (this.useDefaultConnection) {
            return CompletionStageUtil.failedStage(new UnsupportedOperationException("Asynchronous connections are not supported by the server-side internal driver"));
        }
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.driver == null) {
                this.driver = new OracleDriver();
            }
            try {
                this.applyDataSourcePropertiesToBuilder(abstractConnectionBuilder);
            }
            catch (SQLException sQLException) {
                CompletionStage<OracleConnection> completionStage = CompletionStageUtil.failedStage(sQLException);
                if (closeableLock != null) {
                    if (var8_8 != null) {
                        try {
                            closeableLock.close();
                        }
                        catch (Throwable throwable) {
                            var8_8.addSuppressed(throwable);
                        }
                    } else {
                        closeableLock.close();
                    }
                }
                return completionStage;
            }
        }
        return this.driver._INTERNAL_ORACLE_connectAsync(string, properties2, abstractConnectionBuilder).thenApply(CompletionStageUtil.normalCompletionHandler(connection -> {
            if (connection == null) {
                throw (SQLException)DatabaseError.createSqlException(67).fillInStackTrace();
            }
            oracle.jdbc.internal.OracleConnection oracleConnection = (oracle.jdbc.internal.OracleConnection)connection;
            this.initializeStatementCacheForPhysicalConnection(oracleConnection, properties);
            return oracleConnection;
        }));
    }

    @Blind(value=PropertiesBlinder.class)
    private final Properties createPropertiesForPhysicalConnection(@Blind(value=PropertiesBlinder.class) Properties properties, boolean bl) {
        String string;
        String string2;
        String string3;
        String string4;
        String string5;
        String string6;
        Properties properties2;
        Object object = this.acquireCloseableLock();
        Object object2 = null;
        try {
            if (this.connectionProperties == null) {
                Properties properties3 = properties;
                return properties3;
            }
            properties2 = (Properties)this.connectionProperties.clone();
        }
        catch (Throwable throwable) {
            object2 = throwable;
            throw throwable;
        }
        finally {
            if (object != null) {
                if (object2 != null) {
                    try {
                        ((Monitor.CloseableLock)object).close();
                    }
                    catch (Throwable throwable) {
                        ((Throwable)object2).addSuppressed(throwable);
                    }
                } else {
                    ((Monitor.CloseableLock)object).close();
                }
            }
        }
        object = properties.getProperty("user");
        if (object != null) {
            properties2.setProperty("user", (String)object);
        }
        if ((object2 = properties.getProperty("password")) != null) {
            properties2.put("password", object2);
        }
        if ((string6 = properties.getProperty("oracle.jdbc.targetInstanceName")) != null) {
            properties2.put("oracle.jdbc.targetInstanceName", string6);
        }
        if ((string5 = properties.getProperty("oracle.jdbc.targetServiceName")) != null) {
            properties2.put("oracle.jdbc.targetServiceName", string5);
        }
        if ((string4 = properties.getProperty("oracle.jdbc.targetShardingKey")) != null) {
            properties2.put("oracle.jdbc.targetShardingKey", string4);
        }
        if ((string3 = properties.getProperty("oracle.jdbc.targetSuperShardingKey")) != null) {
            properties2.put("oracle.jdbc.targetSuperShardingKey", string3);
        }
        if ("true".equalsIgnoreCase(string2 = properties.getProperty("oracle.jdbc.readOnlyInstanceAllowed"))) {
            properties2.put("oracle.jdbc.readOnlyInstanceAllowed", string2);
        }
        if ((string = properties.getProperty("oracle.jdbc.loginTimeout")) == null) {
            string = properties2.getProperty("LoginTimeout");
        }
        if (string != null) {
            properties2.setProperty("oracle.jdbc.loginTimeout", string);
        }
        if (!bl) {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                if (object == null && this.user != null) {
                    properties2.put("user", this.user);
                }
                if (object2 == null && this.password != null && this.password != OpaqueString.NULL) {
                    properties2.put("password", this.password.get());
                }
            }
        }
        return properties2;
    }

    private final void initializeStatementCacheForPhysicalConnection(oracle.jdbc.internal.OracleConnection oracleConnection, Properties properties) throws SQLException {
        int n2;
        String string = properties.getProperty("stmt_cache_size");
        int n3 = n2 = string == null ? 0 : Integer.parseInt(string);
        if (string != null) {
            oracleConnection.setStatementCacheSize(n2);
        }
        String string2 = properties.getProperty("ExplicitStatementCachingEnabled");
        boolean bl = "true".equals(string2);
        if (string2 != null) {
            oracleConnection.setExplicitCachingEnabled(bl);
        } else if (this.explicitCachingEnabled) {
            oracleConnection.setExplicitCachingEnabled(true);
        }
        String string3 = properties.getProperty("ImplicitStatementCachingEnabled");
        boolean bl2 = "true".equals(string3);
        if (string3 != null) {
            oracleConnection.setImplicitCachingEnabled(bl2);
        } else if (this.implicitCachingEnabled) {
            oracleConnection.setImplicitCachingEnabled(true);
        }
        if (n2 > 0 && !bl && !bl2) {
            oracleConnection.setImplicitCachingEnabled(true);
            oracleConnection.setExplicitCachingEnabled(true);
        }
    }

    @Override
    public int getLoginTimeout() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n2 = this.loginTimeout;
            return n2;
        }
    }

    @Override
    public void setLoginTimeout(int n2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.loginTimeout = n2;
        }
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.logWriter = printWriter;
        }
    }

    @Override
    public PrintWriter getLogWriter() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            PrintWriter printWriter = this.logWriter;
            return printWriter;
        }
    }

    public void setTNSEntryName(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.tnsEntry = string;
        }
    }

    public String getTNSEntryName() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string = this.tnsEntry;
            return string;
        }
    }

    @Override
    public void setDataSourceName(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.dataSourceName = string;
        }
    }

    @Override
    public String getDataSourceName() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string = this.dataSourceName;
            return string;
        }
    }

    @Override
    public String getDatabaseName() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string = this.databaseName;
            return string;
        }
    }

    @Override
    public void setDatabaseName(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.databaseName = string;
        }
    }

    public void setServiceName(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.serviceName = string;
        }
    }

    public String getServiceName() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string = this.serviceName;
            return string;
        }
    }

    @Override
    public void setServerName(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.serverName = string;
        }
    }

    @Override
    public String getServerName() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string = this.serverName;
            return string;
        }
    }

    @Override
    public void setURL(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.url = string;
            if (this.url != null) {
                this.urlExplicit = true;
            }
        }
    }

    @Override
    public String getURL() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (!this.urlExplicit) {
                this.makeURL();
            }
            String string = this.url;
            return string;
        }
    }

    @Override
    public void setUser(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.user = string;
        }
    }

    @Override
    public String getUser() {
        return this.user;
    }

    private void setPassword(OpaqueString opaqueString) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.password = opaqueString;
        }
    }

    @Override
    public void setPassword(@Blind String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.setPassword(OpaqueString.newOpaqueString(string));
        }
    }

    protected OpaqueString getPassword() {
        return this.password;
    }

    @Override
    public String getDescription() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string = this.description;
            return string;
        }
    }

    @Override
    public void setDescription(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.description = string;
        }
    }

    public String getDriverType() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string = this.driverType;
            return string;
        }
    }

    public void setDriverType(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.driverType = string;
        }
    }

    @Override
    public String getNetworkProtocol() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string = this.networkProtocol;
            return string;
        }
    }

    @Override
    public void setNetworkProtocol(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.networkProtocol = string;
        }
    }

    @Override
    public void setPortNumber(int n2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.portNumber = n2;
        }
    }

    @Override
    public int getPortNumber() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n2 = this.portNumber;
            return n2;
        }
    }

    @Override
    public Reference getReference() throws NamingException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Reference reference = new Reference(this.getClass().getName(), "oracle.jdbc.datasource.impl.OracleDataSourceFactory", null);
            this.addRefProperties(reference);
            Reference reference2 = reference;
            return reference2;
        }
    }

    protected void addRefProperties(Reference reference) {
        if (this.url != null) {
            reference.add(new StringRefAddr("url", this.url));
        }
        if (this.user != null) {
            reference.add(new StringRefAddr("userName", this.user));
        }
        if (this.password != null && this.password != OpaqueString.NULL) {
            reference.add(new StringRefAddr("passWord", this.password.get()));
        }
        if (this.description != null) {
            reference.add(new StringRefAddr("description", this.description));
        }
        if (this.driverType != null) {
            reference.add(new StringRefAddr("driverType", this.driverType));
        }
        if (this.serverName != null) {
            reference.add(new StringRefAddr("serverName", this.serverName));
        }
        if (this.databaseName != null) {
            reference.add(new StringRefAddr("databaseName", this.databaseName));
        }
        if (this.serviceName != null) {
            reference.add(new StringRefAddr("serviceName", this.serviceName));
        }
        if (this.networkProtocol != null) {
            reference.add(new StringRefAddr("networkProtocol", this.networkProtocol));
        }
        if (this.portNumber != 0) {
            reference.add(new StringRefAddr("portNumber", Integer.toString(this.portNumber)));
        }
        if (this.tnsEntry != null) {
            reference.add(new StringRefAddr("tnsentryname", this.tnsEntry));
        }
        if (this.connectionProperties != null && this.connectionProperties.size() > 0) {
            reference.add(new StringRefAddr("connectionProperties", this.connectionProperties.toString()));
        }
        if (this.maxStatementsSet) {
            reference.add(new StringRefAddr("maxStatements", Integer.toString(this.maxStatements)));
        }
        if (this.implicitCachingEnabledSet) {
            reference.add(new StringRefAddr("implicitCachingEnabled", this.implicitCachingEnabled ? "true" : "false"));
        }
        if (this.explicitCachingEnabledSet) {
            reference.add(new StringRefAddr("explicitCachingEnabled", this.explicitCachingEnabled ? "true" : "false"));
        }
    }

    protected void makeURL() throws SQLException {
        if (this.urlExplicit) {
            return;
        }
        if (this.driverType == null || !this.driverType.equals("oci8") && !this.driverType.equals("oci") && !this.driverType.equals("thin") && !this.driverType.equals("kprb")) {
            throw (SQLException)DatabaseError.createSqlException(67, "OracleDataSource.makeURL").fillInStackTrace();
        }
        if (this.driverType.equals("kprb")) {
            this.useDefaultConnection = true;
            this.url = "jdbc:oracle:kprb:@";
            return;
        }
        if ((this.driverType.equals("oci8") || this.driverType.equals("oci")) && this.networkProtocol != null && this.networkProtocol.equals("ipc")) {
            this.url = "jdbc:oracle:oci:@";
            return;
        }
        if (this.tnsEntry != null) {
            this.url = "jdbc:oracle:" + this.driverType + ":@" + this.tnsEntry;
            return;
        }
        if (this.serviceName != null) {
            this.url = "jdbc:oracle:" + this.driverType + ":@(DESCRIPTION=(ADDRESS=(PROTOCOL=" + this.networkProtocol + ")(PORT=" + this.portNumber + ")(HOST=" + this.serverName + "))(CONNECT_DATA=(SERVICE_NAME=" + this.serviceName + ")))";
        } else {
            this.url = "jdbc:oracle:" + this.driverType + ":@(DESCRIPTION=(ADDRESS=(PROTOCOL=" + this.networkProtocol + ")(PORT=" + this.portNumber + ")(HOST=" + this.serverName + "))(CONNECT_DATA=(SID=" + this.databaseName + ")))";
            DatabaseError.addSqlWarning(null, new SQLWarning("URL with SID jdbc:subprotocol:@host:port:sid will be deprecated in 10i\nPlease use URL with SERVICE_NAME as jdbc:subprotocol:@//host:port/service_name"));
        }
    }

    protected void trace(String string) {
        if (this.logWriter != null) {
        }
    }

    @Override
    public void setMaxStatements(int n2) throws SQLException {
        if (n2 < 0) {
            throw (SQLException)DatabaseError.createSqlException(68).fillInStackTrace();
        }
        this.maxStatementsSet = true;
        this.maxStatements = n2;
    }

    @Override
    public int getMaxStatements() throws SQLException {
        return this.maxStatements;
    }

    @Override
    public void setImplicitCachingEnabled(boolean bl) throws SQLException {
        this.implicitCachingEnabledSet = true;
        this.implicitCachingEnabled = bl;
    }

    @Override
    public boolean getImplicitCachingEnabled() throws SQLException {
        return this.implicitCachingEnabled;
    }

    @Override
    public void setExplicitCachingEnabled(boolean bl) throws SQLException {
        this.explicitCachingEnabledSet = true;
        this.explicitCachingEnabled = bl;
    }

    @Override
    public boolean getExplicitCachingEnabled() throws SQLException {
        return this.explicitCachingEnabled;
    }

    @Override
    public void setConnectionProperties(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        if (properties == null || properties.size() == 0) {
            return;
        }
        if (this.connectionProperties == null || this.connectionProperties.size() == 0) {
            this.connectionProperties = (Properties)properties.clone();
            return;
        }
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            Object obj = enumeration.nextElement();
            this.connectionProperties.put(obj, properties.get(obj));
        }
    }

    @Override
    public void setRoleName(String string) throws SQLException {
        this.roleName = string;
    }

    @Override
    public String getRoleName() {
        return this.roleName;
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getConnectionProperties() throws SQLException {
        return OracleDataSource.filterConnectionProperties(this.connectionProperties);
    }

    @Override
    public String getConnectionProperty(String string) throws SQLException {
        if (OracleDataSource.isSensitiveProperty(string)) {
            return null;
        }
        return this.connectionProperties == null ? null : this.connectionProperties.getProperty(string);
    }

    @Override
    public void setConnectionProperty(String string, String string2) throws SQLException {
        if (string2 == null || string2.equals("")) {
            throw new IllegalArgumentException();
        }
        if (this.connectionProperties == null) {
            this.connectionProperties = new Properties();
        }
        this.connectionProperties.setProperty(string, string2);
    }

    @Blind(value=PropertiesBlinder.class)
    public static final Properties filterConnectionProperties(@Blind(value=PropertiesBlinder.class) Properties properties) {
        Properties properties2 = null;
        if (properties != null) {
            properties2 = (Properties)properties.clone();
            Enumeration<?> enumeration = properties2.propertyNames();
            Object var3_3 = null;
            while (enumeration.hasMoreElements()) {
                String string = (String)enumeration.nextElement();
                if (!OracleDataSource.isSensitiveProperty(string)) continue;
                properties2.remove(string);
            }
        }
        return properties2;
    }

    private static boolean isSensitiveProperty(String string) {
        if (string == null) {
            return false;
        }
        switch (string) {
            case "oracle.jdbc.passwordAuthentication": {
                return false;
            }
            case "oracle.jdbc.accessToken": {
                return true;
            }
        }
        return string.matches(".*[Pp][Aa][Ss][Ss][Ww][Oo][Rr][Dd].*");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, SQLException {
        objectInputStream.defaultReadObject();
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        if (clazz.isInterface()) {
            return clazz.isInstance(this);
        }
        throw (SQLException)DatabaseError.createSqlException(177).fillInStackTrace();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface() && clazz.isInstance(this)) {
            return (T)this;
        }
        throw (SQLException)DatabaseError.createSqlException(177).fillInStackTrace();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger("oracle");
    }

    @Override
    public final void setSSLContext(SSLContext sSLContext) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.sslContext = sSLContext;
        }
    }

    @Override
    public void setSingleShardTransactionSupport(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.allowSingleShardTransaction = bl;
        }
    }

    @Override
    public void setHostnameResolver(OracleHostnameResolver oracleHostnameResolver) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.hostnameResolver = oracleHostnameResolver;
        }
    }

    protected oracle.jdbc.internal.OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    @Override
    public OracleConnectionBuilderImpl createConnectionBuilder() {
        return new OracleConnectionBuilderImpl(){

            @Override
            public CompletionStage<OracleConnection> buildAsyncOracle() throws SQLException {
                this.ensureMutableState();
                this.verifyBuildConfiguration();
                CompletableFuture<OracleConnection> completableFuture = new CompletableFuture<OracleConnection>();
                OracleDataSource.this.getConnectionAsync(this).whenComplete((oracleConnection, throwable) -> {
                    if (throwable == null) {
                        completableFuture.complete((OracleConnection)oracleConnection);
                    } else {
                        Throwable throwable2 = CompletionStageUtil.unwrapCompletionException(throwable);
                        completableFuture.completeExceptionally(throwable2);
                    }
                });
                return completableFuture;
            }

            @Override
            public oracle.jdbc.internal.OracleConnection build() throws SQLException {
                this.ensureMutableState();
                this.verifyBuildConfiguration();
                boolean bl = OracleDataSource.this.isACSupportPropertySet();
                return (oracle.jdbc.internal.OracleConnection)OracleDataSource.this.getConnectionInternal(this, bl);
            }
        };
    }

    private final boolean isACSupportPropertySet() throws SQLException {
        String string = OracleDataSource.getSystemProperty(ENABLE_AC_SUPPORT_PROPERTY, null);
        if (string == null) {
            string = this.getConnectionProperty(ENABLE_AC_SUPPORT_PROPERTY);
        }
        if (string == null) {
            string = "true";
        }
        return string != null && string.equalsIgnoreCase("true");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public Connection getConnectionNoProxy(OracleConnectionBuilderImpl oracleConnectionBuilderImpl) throws SQLException {
        int n2 = 1;
        Connection connection = null;
        Exception exception = null;
        do {
            try {
                exception = null;
                oracleConnectionBuilderImpl.instanceName(null);
                connection = this.getConnectionInternal(oracleConnectionBuilderImpl, false);
                oracle.jdbc.internal.OracleConnection oracleConnection = (oracle.jdbc.internal.OracleConnection)connection;
                if (oracleConnection.isDRCPEnabled()) {
                    oracleConnection.attachServerConnection();
                }
            }
            catch (Exception exception2) {
                connection = null;
                exception = exception2;
            }
            finally {
                if (connection != null && exception == null) {
                    return connection;
                }
                ++n2;
            }
            try {
                if (this.reconnectDelay <= 0) continue;
                Thread.sleep(this.reconnectDelay * 1000);
            }
            catch (InterruptedException interruptedException) {
            }
        } while (n2 <= this.reconnectRetries);
        return null;
    }

    private Connection getConnectionInternal(OracleConnectionBuilderImpl oracleConnectionBuilderImpl, boolean bl) throws SQLException {
        Connection connection = null;
        if (this.isFirstConnection.get()) {
            this.setupACSpecificProperties(bl);
        }
        connection = this.getConnection(oracleConnectionBuilderImpl);
        if (bl) {
            connection = this.enableACAndProxifyIfNecessary(connection, oracleConnectionBuilderImpl);
        }
        this.isFirstConnection.set(false);
        return connection;
    }

    protected void setupACSpecificProperties(boolean bl) throws SQLException {
        if (bl) {
            this.setConnectionProperty(CHECKSUM_PROPERTY, this.clientChecksum12x);
        } else {
            this.setConnectionProperty(IGNORE_AC_CONTEXT_PROPERTY, "true");
        }
    }

    protected Connection enableACAndProxifyIfNecessary(Connection connection, OracleConnectionBuilderImpl oracleConnectionBuilderImpl) throws SQLException {
        Object object;
        String string;
        String string2;
        String string3;
        int n2;
        String string4;
        int n3;
        boolean bl = true;
        short s2 = 0;
        oracle.jdbc.internal.OracleConnection oracleConnection = (oracle.jdbc.internal.OracleConnection)connection;
        Properties properties = oracleConnection.getServerSessionInfo();
        String string5 = properties.getProperty(RECONNECT_DELAY_PROPERTY);
        if (string5 != null && !"".equals(string5) && (n3 = Integer.parseInt(string5)) > 0) {
            this.reconnectDelay = n3;
        }
        if ((string4 = properties.getProperty(RECONNECT_RETRIES_PROPERTY)) != null && !"".equals(string4) && (n2 = Integer.parseInt(string4)) > 0) {
            this.reconnectRetries = n2;
        }
        if ((string3 = properties.getProperty(FAILOVER_TYPE_PROPERTY)) != null && !"".equals(string3)) {
            int n4;
            try {
                n4 = Integer.parseInt(string3);
            }
            catch (NumberFormatException numberFormatException) {
                n4 = 0;
            }
            this.isAutoACEnabled = (n4 & 0x20) == 32;
            this.isTransactionReplayEnabled = this.isAutoACEnabled || (n4 & 8) == 8;
            boolean bl2 = this.isReplayInDynamicMode = (n4 & 0x10) == 0;
        }
        if ((string2 = properties.getProperty(FAILOVER_RESTORE_PROPERTY)) != null && !"".equals(string2) && "thin".equals(oracleConnection.getProtocolType())) {
            int n5 = Integer.parseInt(string2);
            this.stateRestorationType = ReplayableConnection.StateRestorationType.values()[n5];
            if (this.stateRestorationType.compareTo(ReplayableConnection.StateRestorationType.NONE) > 0) {
                boolean bl3 = this.isStateRestorationAuto = this.stateRestorationType.compareTo(ReplayableConnection.StateRestorationType.LEVEL2) >= 0;
                if (this.isAutoACEnabled && !this.isStateRestorationAuto) {
                    bl = false;
                }
            }
        }
        if ((string = properties.getProperty(INITIATION_TIMEOUT_PROPERTY)) != null && !"".equals(string)) {
            this.replayInitiationTimeout = Integer.parseInt(string);
        }
        if ((s2 = oracleConnection.getVersionNumber()) < 11203) {
            bl = false;
        } else if (s2 >= 12100 && !this.isTransactionReplayEnabled) {
            bl = false;
        } else if (s2 >= 11203 && s2 < 12100) {
            boolean bl4 = "true".equalsIgnoreCase(OracleDataSource.getSystemProperty(AC_11203_COMPATIBLE_SYSTEM_PROPERTY, "false"));
            if (bl4) {
                connection.close();
                this.setConnectionProperty(CHECKSUM_PROPERTY, this.clientChecksum11203x);
                connection = this.getConnection(oracleConnectionBuilderImpl);
                object = proxyFactoryLock.acquireCloseableLock();
                Throwable throwable = null;
                try {
                    PROXY_FACTORY = NON_TXN_PROXY_FACTORY;
                }
                catch (Throwable throwable2) {
                    throwable = throwable2;
                    throw throwable2;
                }
                finally {
                    if (object != null) {
                        if (throwable != null) {
                            try {
                                ((Monitor.CloseableLock)object).close();
                            }
                            catch (Throwable throwable3) {
                                throwable.addSuppressed(throwable3);
                            }
                        } else {
                            ((Monitor.CloseableLock)object).close();
                        }
                    }
                }
            } else {
                bl = false;
            }
        }
        if (bl) {
            Connection connection2 = PROXY_FACTORY.proxyFor(connection);
            object = (ReplayableConnection)((Object)connection2);
            object.initialize(this, oracleConnectionBuilderImpl);
            if (s2 >= 12100) {
                object.setReplayInitiationTimeout(this.replayInitiationTimeout);
                object.setAutoAC(this.isAutoACEnabled);
                object.setSessionStateConsistency(!this.isReplayInDynamicMode);
                object.setSessionStateRestoration(this.stateRestorationType);
            }
            ((oracle.jdbc.internal.OracleConnection)connection).getReplayContext();
            boolean bl5 = "true".equalsIgnoreCase(OracleDataSource.getSystemProperty(IMPLICIT_BEGIN_REQUEST_SYSTEM_PROPERTY, "true"));
            if (this.isAutoACEnabled && bl5 && !oracleConnection.isDRCPEnabled()) {
                object.beginRequest();
            }
            return connection2;
        }
        return connection;
    }

    @Override
    public void registerConnectionInitializationCallback(ConnectionInitializationCallback connectionInitializationCallback) throws SQLException {
        if (connectionInitializationCallback == null) {
            throw DatabaseError.createSqlException(68);
        }
        this.connectionInitializationCallback = connectionInitializationCallback;
    }

    @Override
    public void unregisterConnectionInitializationCallback(ConnectionInitializationCallback connectionInitializationCallback) throws SQLException {
        if (connectionInitializationCallback == null || this.connectionInitializationCallback != connectionInitializationCallback) {
            throw DatabaseError.createSqlException(68);
        }
        this.connectionInitializationCallback = null;
    }

    @Override
    public ConnectionInitializationCallback getConnectionInitializationCallback() {
        return this.connectionInitializationCallback;
    }

    @Override
    public ReplayStatistics getReplayStatistics() {
        return this.tracker.getReplayStatistics();
    }

    @Override
    public void clearDoneDumpOnMemoryPressure() {
        this.doneDumpOnMemoryPressure.set(false);
    }

    @Override
    @DisableTrace
    public String getReplayStatisticsString() {
        if (this.doneDumpOnMemoryPressure.compareAndSet(false, true)) {
            return null;
        }
        return this.tracker.getReplayStatisticsString();
    }

    @Override
    public void clearReplayStatistics() {
        this.tracker.clearReplayStatistics();
    }

    @Override
    public void updateReplayStatistics(oracle.jdbc.replay.internal.ReplayStatistics replayStatistics) {
        this.tracker.updateReplayStatistics(this.getDataSourceName(), replayStatistics);
    }

    protected static String getSystemProperty(String string, String string2) {
        if (string != null) {
            final String string3 = string;
            final String string4 = string2;
            final String[] stringArray = new String[]{string2};
            AccessController.doPrivileged(new PrivilegedAction(){

                public Object run() {
                    stringArray[0] = System.getProperty(string3, string4);
                    return null;
                }
            });
            return stringArray[0];
        }
        return string2;
    }

    @Override
    public int getRequestSizeLimit() throws SQLException {
        int n2 = Integer.MAX_VALUE;
        String string = OracleDataSource.getSystemProperty(REQUEST_SIZE_LIMIT_PROPERTY, null);
        if (string == null) {
            string = this.getConnectionProperty(REQUEST_SIZE_LIMIT_PROPERTY);
        }
        if (string == null) {
            string = Integer.toString(Integer.MAX_VALUE);
        }
        try {
            n2 = Integer.parseInt(string);
        }
        catch (NumberFormatException numberFormatException) {
            n2 = Integer.MAX_VALUE;
        }
        return n2;
    }

    @Override
    public ProxyFactory getProxyFactory() throws SQLException {
        return PROXY_FACTORY;
    }

    @DisableTrace
    public static void registerMBean() {
        block14: {
            try {
                Object object;
                Object object2;
                MBeanServer mBeanServer = null;
                try {
                    object2 = Class.forName("oracle.as.jmx.framework.PortableMBeanFactory");
                    object = ((Class)object2).newInstance();
                    Method method = ((Class)object2).getMethod("getMBeanServer", new Class[0]);
                    mBeanServer = (MBeanServer)method.invoke(object, new Object[0]);
                }
                catch (NoClassDefFoundError noClassDefFoundError) {
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }
                catch (ClassNotFoundException classNotFoundException) {
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    Logger.getLogger("oracle.jdbc.internal.replay").log(Level.WARNING, "Found Oracle Apps MBeanServer but not the getMBeanServer method.", noSuchMethodException);
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }
                catch (InstantiationException instantiationException) {
                    Logger.getLogger("oracle.jdbc.internal.replay").log(Level.WARNING, "Found Oracle Apps MBeanServer but could not create an instance.", instantiationException);
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }
                catch (IllegalAccessException illegalAccessException) {
                    Logger.getLogger("oracle.jdbc.internal.replay").log(Level.WARNING, "Found Oracle Apps MBeanServer but could not access the getMBeanServer method.", illegalAccessException);
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }
                catch (InvocationTargetException invocationTargetException) {
                    Logger.getLogger("oracle.jdbc.internal.replay").log(Level.WARNING, "Found Oracle Apps MBeanServer but the getMBeanServer method threw an exception.", invocationTargetException);
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }
                if (mBeanServer != null) {
                    object2 = ReplayStatisticsMBeanImpl.class.getClassLoader();
                    object = object2 == null ? "nullLoader" : object2.getClass().getName();
                    int n2 = 0;
                    while (true) {
                        String string = object + "@" + Integer.toHexString((object2 == null ? 0 : object2.hashCode()) + n2++);
                        mbeanName = new ObjectName(registeredName + string);
                        try {
                            mBeanServer.registerMBean(ReplayStatisticsMBeanImpl.SOLE_INSTANCE, mbeanName);
                            break block14;
                        }
                        catch (InstanceAlreadyExistsException instanceAlreadyExistsException) {
                            Logger.getLogger("oracle.jdbc.internal.replay").log(Level.INFO, "AC statistics MBean with the same name already registered.");
                            continue;
                        }
                        break;
                    }
                }
                Logger.getLogger("oracle.jdbc.internal.replay").log(Level.WARNING, "Unable to find an MBeanServer so no MBears are registered.");
            }
            catch (JMException jMException) {
                Logger.getLogger("oracle.jdbc.internal.replay").log(Level.WARNING, "Error while registering Oracle JDBC AC statistics MBean.", jMException);
            }
            catch (Throwable throwable) {
                Logger.getLogger("oracle.jdbc.internal.replay").log(Level.WARNING, "Error while registering Oracle JDBC AC statistics MBean.", throwable);
            }
        }
    }

    @Supports(value={Feature.PLATFORM})
    public static void unregisterMBean() {
        try {
            Object object;
            Object object2;
            MBeanServer mBeanServer = null;
            try {
                object2 = Class.forName("oracle.as.jmx.framework.PortableMBeanFactory");
                object = ((Class)object2).newInstance();
                Method method = ((Class)object2).getMethod("getMBeanServer", new Class[0]);
                mBeanServer = (MBeanServer)method.invoke(object, new Object[0]);
            }
            catch (Throwable throwable) {
                mBeanServer = ManagementFactory.getPlatformMBeanServer();
            }
            if (mBeanServer != null) {
                try {
                    object2 = ReplayStatisticsMBeanImpl.class.getClassLoader();
                    object = object2 == null ? "nullLoader" : object2.getClass().getName();
                    int n2 = 0;
                    String string = object + "@" + Integer.toHexString((object2 == null ? 0 : object2.hashCode()) + n2++);
                    mbeanName = new ObjectName(registeredName + string);
                    mBeanServer.unregisterMBean(mbeanName);
                }
                catch (Throwable throwable) {
                    Logger.getLogger("oracle.jdbc.internal.replay").log(Level.INFO, "Unabled to unregister Oracle JDBC AC statistics MBean: " + throwable.getMessage());
                }
            } else {
                Logger.getLogger("oracle.jdbc.internal.replay").log(Level.WARNING, "Unable to find an MBeanServer to unregister Oracle JDBC AC statistics MBean.");
            }
        }
        catch (Throwable throwable) {
            Logger.getLogger("oracle.jdbc.internal.replay").log(Level.WARNING, "Error while unregistering Oracle JDBC AC statistics MBean.", throwable);
        }
    }

    public static void cleanup() {
        OracleDataSource.unregisterMBean();
    }

    @Log
    protected void debug(Logger logger, Level level, Executable executable, String string) {
        ClioSupport.log(logger, level, this.getClass(), executable, string);
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }

    @Override
    public final void setTokenSupplier(Supplier<? extends AccessToken> supplier) {
        this.tokenSupplier = Objects.requireNonNull(supplier, "tokenSuplier is null");
    }

    static {
        try (Monitor.CloseableLock closeableLock = proxyFactoryLock.acquireCloseableLock();){
            if (PROXY_FACTORY == null) {
                NON_TXN_PROXY_FACTORY = ProxyFactory.createProxyFactory(NonTxnReplayableBase.class, NonTxnReplayableConnection.class, NonTxnReplayableStatement.class, NonTxnReplayableResultSet.class, NonTxnReplayableArray.class, NonTxnReplayableBfile.class, NonTxnReplayableBlob.class, NonTxnReplayableClob.class, NonTxnReplayableNClob.class, NonTxnReplayableOpaque.class, NonTxnReplayableRef.class, NonTxnReplayableStruct.class, NonTxnReplayableOthers.class);
                PROXY_FACTORY = ProxyFactory.createProxyFactory(TxnReplayableBase.class, TxnReplayableConnection.class, TxnReplayableStatement.class, TxnReplayableResultSet.class, TxnReplayableArray.class, TxnReplayableBfile.class, TxnReplayableBlob.class, TxnReplayableClob.class, TxnReplayableNClob.class, TxnReplayableOpaque.class, TxnReplayableRef.class, TxnReplayableSqlxml.class, TxnReplayableStruct.class, TxnReplayableOthers.class);
            }
        }
        mbeanName = null;
        AccessController.doPrivileged(new PrivilegedAction<Object>(){

            @Override
            public Object run() {
                OracleDataSource.registerMBean();
                return null;
            }
        });
        _Copyright_2014_Oracle_All_Rights_Reserved_ = null;
    }
}

