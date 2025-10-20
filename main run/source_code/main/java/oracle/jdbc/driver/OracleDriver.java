/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.security.pki.OraclePKIProvider
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import oracle.jdbc.OracleDatabaseMetaData;
import oracle.jdbc.babelfish.BabelfishCallableStatement;
import oracle.jdbc.babelfish.BabelfishConnection;
import oracle.jdbc.babelfish.BabelfishGenericProxy;
import oracle.jdbc.babelfish.BabelfishPreparedStatement;
import oracle.jdbc.babelfish.BabelfishStatement;
import oracle.jdbc.babelfish.TranslationManager;
import oracle.jdbc.babelfish.Translator;
import oracle.jdbc.datasource.impl.OracleDataSource;
import oracle.jdbc.driver.BlockSource;
import oracle.jdbc.driver.BuildInfo;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.HAManager;
import oracle.jdbc.driver.OracleDiagnosabilityMBean;
import oracle.jdbc.driver.OracleDriverExtension;
import oracle.jdbc.driver.OracleSql;
import oracle.jdbc.driver.OracleTimeoutThreadPerVM;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.AbstractConnectionBuilder;
import oracle.jdbc.internal.CompletionStageUtil;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.proxy.ProxyFactory;
import oracle.jdbc.replay.driver.TxnFailoverManagerImpl;
import oracle.net.nt.TcpMultiplexer;
import oracle.net.nt.TimeoutInterruptHandler;
import oracle.net.resolver.EZConnectResolver;
import oracle.security.pki.OraclePKIProvider;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONNECT})
public class OracleDriver
implements Driver,
Monitor {
    public static final String oracle_string = "oracle";
    public static final String jdbc_string = "jdbc";
    public static final String protocol_string = "protocol";
    public static final String user_string = "user";
    public static final String password_string = "password";
    public static final String database_string = "database";
    public static final String server_string = "server";
    @Deprecated
    public static final String access_string = "access";
    @Deprecated
    public static final String protocolFullName_string = "protocolFullName";
    public static final String logon_as_internal_str = "internal_logon";
    public static final String proxy_client_name = "oracle.jdbc.proxyClientName";
    public static final String prefetch_string = "prefetch";
    public static final String row_prefetch_string = "rowPrefetch";
    public static final String default_row_prefetch_string = "defaultRowPrefetch";
    public static final String batch_string = "batch";
    public static final String execute_batch_string = "executeBatch";
    public static final String default_execute_batch_string = "defaultExecuteBatch";
    public static final String process_escapes_string = "processEscapes";
    public static final String accumulate_batch_result = "AccumulateBatchResult";
    public static final String j2ee_compliance = "oracle.jdbc.J2EE13Compliant";
    public static final String v8compatible_string = "V8Compatible";
    public static final String permit_timestamp_date_mismatch_string = "oracle.jdbc.internal.permitBindDateDefineTimestampMismatch";
    public static final String prelim_auth_string = "prelim_auth";
    public static final String SetFloatAndDoubleUseBinary_string = "SetFloatAndDoubleUseBinary";
    @Deprecated
    public static final String xa_trans_loose = "oracle.jdbc.XATransLoose";
    public static final String tcp_no_delay = "oracle.jdbc.TcpNoDelay";
    public static final String read_timeout = "oracle.jdbc.ReadTimeout";
    public static final String defaultnchar_string = "oracle.jdbc.defaultNChar";
    public static final String defaultncharprop_string = "defaultNChar";
    public static final String useFetchSizeWithLongColumn_prop_string = "useFetchSizeWithLongColumn";
    public static final String useFetchSizeWithLongColumn_string = "oracle.jdbc.useFetchSizeWithLongColumn";
    public static final String remarks_string = "remarks";
    public static final String report_remarks_string = "remarksReporting";
    public static final String synonyms_string = "synonyms";
    public static final String include_synonyms_string = "includeSynonyms";
    public static final String restrict_getTables_string = "restrictGetTables";
    public static final String fixed_string_string = "fixedString";
    public static final String dll_string = "oracle.jdbc.ocinativelibrary";
    public static final String nls_lang_backdoor = "oracle.jdbc.ociNlsLangBackwardCompatible";
    public static final String disable_defineColumnType_string = "disableDefineColumnType";
    public static final String convert_nchar_literals_string = "oracle.jdbc.convertNcharLiterals";
    public static final String dataSizeUnitsPropertyName = "";
    public static final String dataSizeBytes = "";
    public static final String dataSizeChars = "";
    public static final String set_new_password_string = "OCINewPassword";
    public static final String retain_v9_bind_behavior_string = "oracle.jdbc.RetainV9LongBindBehavior";
    public static final String no_caching_buffers = "oracle.jdbc.FreeMemoryOnEnterImplicitCache";
    private final String SIMPLE_URL_FORMAT = "jdbc:oracle:(thin|oci|oci8|kprb|sharding):\\w*/?\\w*@(//)?[A-z0-9-._]+(:\\d+)[:/][A-z0-9-._:]+";
    static final int EXTENSION_TYPE_ORACLE_ERROR = -3;
    static final int EXTENSION_TYPE_GEN_ERROR = -2;
    static final int EXTENSION_TYPE_TYPE4_CLIENT = 0;
    static final int EXTENSION_TYPE_TYPE4_SERVER = 1;
    static final int EXTENSION_TYPE_TYPE2_CLIENT = 2;
    static final int EXTENSION_TYPE_TYPE2_SERVER = 3;
    private static final int NUMBER_OF_EXTENSION_TYPES = 4;
    private OracleDriverExtension[] driverExtensions = new OracleDriverExtension[4];
    private static final String DRIVER_PACKAGE_STRING = "driver";
    private static final String[] driverExtensionClassNames;
    private static Properties driverAccess;
    protected static Connection defaultConn;
    private static final Monitor DEFAULT_CONN_MONITOR;
    private static OracleDriver defaultDriver;
    public static final Map<String, Class<?>> systemTypeMap;
    private static final String DEFAULT_CONNECTION_PROPERTIES_RESOURCE_NAME = "/oracle/jdbc/defaultConnectionProperties.properties";
    protected static final Properties DEFAULT_CONNECTION_PROPERTIES;
    private static ExecutorService threadPool;
    private static final String SERVER_SIDE_DEFAULT_URL_PREFIX = "jdbc:default:connection";
    private static final int SERVER_SIDE_DEFAULT_URL_PREFIX_LENGTH;
    private static ObjectName diagnosticMBeanObjectName;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();

    @DisableTrace
    public static void registerMBeans() {
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
                    Logger.getLogger("oracle.jdbc").log(Level.WARNING, "Found Oracle Apps MBeanServer but not the getMBeanServer method.", noSuchMethodException);
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }
                catch (InstantiationException instantiationException) {
                    Logger.getLogger("oracle.jdbc").log(Level.WARNING, "Found Oracle Apps MBeanServer but could not create an instance.", instantiationException);
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }
                catch (IllegalAccessException illegalAccessException) {
                    Logger.getLogger("oracle.jdbc").log(Level.WARNING, "Found Oracle Apps MBeanServer but could not access the getMBeanServer method.", illegalAccessException);
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }
                catch (InvocationTargetException invocationTargetException) {
                    Logger.getLogger("oracle.jdbc").log(Level.WARNING, "Found Oracle Apps MBeanServer but the getMBeanServer method threw an exception.", invocationTargetException);
                    mBeanServer = ManagementFactory.getPlatformMBeanServer();
                }
                if (mBeanServer != null) {
                    object2 = OracleDriver.class.getClassLoader();
                    object = object2 == null ? "nullLoader" : object2.getClass().getName();
                    int n2 = 0;
                    while (true) {
                        String string = object + "@" + Integer.toHexString((object2 == null ? 0 : object2.hashCode()) + n2++);
                        diagnosticMBeanObjectName = new ObjectName("com.oracle.jdbc:type=diagnosability,name=" + string);
                        try {
                            mBeanServer.registerMBean(new OracleDiagnosabilityMBean(), diagnosticMBeanObjectName);
                            break block14;
                        }
                        catch (InstanceAlreadyExistsException instanceAlreadyExistsException) {
                            continue;
                        }
                        break;
                    }
                }
                Logger.getLogger("oracle.jdbc").log(Level.WARNING, "Unable to find an MBeanServer so no MBears are registered.");
            }
            catch (JMException jMException) {
                Logger.getLogger("oracle.jdbc").log(Level.WARNING, "Error while registering Oracle JDBC Diagnosability MBean.", jMException);
            }
            catch (Throwable throwable) {
                Logger.getLogger("oracle.jdbc").log(Level.WARNING, "Error while registering Oracle JDBC Diagnosability MBean.", throwable);
            }
        }
    }

    @Supports(value={Feature.PLATFORM})
    public static void unRegisterMBeans() {
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
                    object2 = OracleDriver.class.getClassLoader();
                    object = object2 == null ? "nullLoader" : object2.getClass().getName();
                    int n2 = 0;
                    String string = object + "@" + Integer.toHexString((object2 == null ? 0 : object2.hashCode()) + n2++);
                    diagnosticMBeanObjectName = new ObjectName("com.oracle.jdbc:type=diagnosability,name=" + string);
                    mBeanServer.unregisterMBean(diagnosticMBeanObjectName);
                }
                catch (Throwable throwable) {
                    Logger.getLogger("oracle.jdbc").log(Level.INFO, "Unabled to unregister Oracle JDBC Diagnosability MBean: " + throwable.getMessage());
                }
            } else {
                Logger.getLogger("oracle.jdbc").log(Level.WARNING, "Unable to find an MBeanServer to unregister Oracle JDBC Diagnosability MBean.");
            }
        }
        catch (Throwable throwable) {
            Logger.getLogger("oracle.jdbc").log(Level.WARNING, "Error while unregistering Oracle JDBC Diagnosability MBean.", throwable);
        }
    }

    @Supports(value={Feature.PLATFORM})
    private static void deregister() {
        OracleDriver.unRegisterMBeans();
        OracleTimeoutThreadPerVM.stopWatchdog();
        BlockSource.ThreadedCachingBlockSource.stopBlockReleaserThread();
        TimeoutInterruptHandler.stopTimer();
        if (threadPool != null) {
            threadPool.shutdownNow();
        }
        HAManager.shutdownAll();
        OracleDataSource.cleanup();
        TxnFailoverManagerImpl.cleanup();
        TcpMultiplexer.stop();
    }

    @Override
    public Connection connect(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        return this.connect(string, properties, AbstractConnectionBuilder.unconfigured());
    }

    public Connection connect(String string, @Blind(value=PropertiesBlinder.class) Properties properties, AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) throws SQLException {
        Connection connection;
        int n2;
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(296).fillInStackTrace();
        }
        if (string.startsWith(SERVER_SIDE_DEFAULT_URL_PREFIX)) {
            string = OracleDriver.convertServerSideDefaultURL(string);
        }
        if (abstractConnectionBuilder == null) {
            abstractConnectionBuilder = AbstractConnectionBuilder.unconfigured();
        }
        if ((n2 = OracleDriver.parseExtensionType(string)) == -2) {
            return null;
        }
        if (abstractConnectionBuilder.getGSSCredential() != null) {
            OracleDriver.ensureGSSCredentialSupport(n2);
        }
        OracleDriverExtension oracleDriverExtension = this.getDriverExtension(n2);
        if (properties == null) {
            properties = new Properties();
        }
        if (!string.matches("jdbc:oracle:(thin|oci|oci8|kprb|sharding):\\w*/?\\w*@(//)?[A-z0-9-._]+(:\\d+)[:/][A-z0-9-._:]+")) {
            string = OracleDriver.resolveNonSimpleURL(string, properties);
        }
        OracleDriver.ensureSingleRegisteredDriver();
        String string2 = OracleDriver.getTranslationProfile(n2, properties);
        if (string2 != null) {
            connection = this.babelfishConnect(properties, string2, string, oracleDriverExtension, n2);
        } else {
            connection = oracleDriverExtension.getConnection(string, properties, abstractConnectionBuilder);
            OracleDriver.setConnectionProtocolID(connection, n2);
        }
        return connection;
    }

    public final CompletionStage<Connection> _INTERNAL_ORACLE_connectAsync(String string, @Blind(value=PropertiesBlinder.class) Properties properties, AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) {
        OracleDriverExtension oracleDriverExtension;
        int n2;
        if (string == null) {
            return CompletionStageUtil.failedStage(DatabaseError.createSqlException(296).fillInStackTrace());
        }
        if (string.startsWith(SERVER_SIDE_DEFAULT_URL_PREFIX)) {
            return CompletionStageUtil.failedStage(new UnsupportedOperationException("Asynchronous connections are not supported by the server-side internal driver"));
        }
        try {
            n2 = OracleDriver.parseExtensionType(string);
            if (n2 == -2) {
                return CompletionStageUtil.completedStage(null);
            }
            if (abstractConnectionBuilder != null && abstractConnectionBuilder.getGSSCredential() != null) {
                OracleDriver.ensureGSSCredentialSupport(n2);
            }
            oracleDriverExtension = this.getDriverExtension(n2);
            if (properties == null) {
                properties = new Properties();
            }
            if (!string.matches("jdbc:oracle:(thin|oci|oci8|kprb|sharding):\\w*/?\\w*@(//)?[A-z0-9-._]+(:\\d+)[:/][A-z0-9-._:]+")) {
                string = OracleDriver.resolveNonSimpleURL(string, properties);
            }
            OracleDriver.ensureSingleRegisteredDriver();
        }
        catch (SQLException sQLException) {
            return CompletionStageUtil.failedStage(sQLException);
        }
        if (null != OracleDriver.getTranslationProfile(n2, properties)) {
            return CompletionStageUtil.failedStage(new UnsupportedOperationException("Asynchronous connections do not support SQL translation."));
        }
        return oracleDriverExtension.getConnectionAsync(string, properties, abstractConnectionBuilder).thenApply(connection -> {
            OracleDriver.setConnectionProtocolID(connection, n2);
            return connection;
        });
    }

    private static final String convertServerSideDefaultURL(String string) {
        if (string.length() > SERVER_SIDE_DEFAULT_URL_PREFIX_LENGTH) {
            return "jdbc:oracle:kprb".concat(string.substring(SERVER_SIDE_DEFAULT_URL_PREFIX_LENGTH));
        }
        return "jdbc:oracle:kprb".concat(":");
    }

    private static final int parseExtensionType(String string) throws SQLException {
        int n2 = OracleDriver.oracleDriverExtensionTypeFromURL(string);
        if (n2 == -3) {
            throw (SQLException)DatabaseError.createSqlException(67).fillInStackTrace();
        }
        return n2;
    }

    private static final void ensureGSSCredentialSupport(int n2) throws SQLException {
        if (!OracleDriver.isType4ExtensionType(n2)) {
            throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
        }
    }

    private static final boolean isType4ExtensionType(int n2) {
        return n2 == 0;
    }

    private final OracleDriverExtension getDriverExtension(int n2) {
        OracleDriverExtension oracleDriverExtension = this.driverExtensions[n2];
        if (oracleDriverExtension == null) {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                if (oracleDriverExtension == null) {
                    this.driverExtensions[n2] = oracleDriverExtension = (OracleDriverExtension)Class.forName(driverExtensionClassNames[n2]).newInstance();
                } else {
                    oracleDriverExtension = this.driverExtensions[n2];
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return oracleDriverExtension;
    }

    private static final String resolveNonSimpleURL(String string, @Blind(value=PropertiesBlinder.class) Properties properties) {
        EZConnectResolver eZConnectResolver = EZConnectResolver.newInstance(string);
        properties.putAll(eZConnectResolver.getProperties());
        String string2 = eZConnectResolver.getResolvedUrl();
        return string2 == null ? string : string2;
    }

    private static final void ensureSingleRegisteredDriver() throws SQLException {
        Driver driver;
        Enumeration<Driver> enumeration = DriverManager.getDrivers();
        while (enumeration.hasMoreElements() && !((driver = enumeration.nextElement()) instanceof OracleDriver)) {
        }
        while (enumeration.hasMoreElements()) {
            driver = enumeration.nextElement();
            if (!(driver instanceof OracleDriver)) continue;
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Object>(){

                    @Override
                    public Object run() throws SQLException {
                        DriverManager.deregisterDriver(driver);
                        return null;
                    }
                });
            }
            catch (PrivilegedActionException privilegedActionException) {
                throw (SQLException)privilegedActionException.getException();
            }
        }
    }

    private static final String getTranslationProfile(int n2, @Blind(value=PropertiesBlinder.class) Properties properties) {
        if (n2 == 2 && properties.containsKey("connection_pool") && properties.getProperty("connection_pool").equals("connection_pool")) {
            return null;
        }
        return properties != null ? properties.getProperty("oracle.jdbc.sqlTranslationProfile", PhysicalConnection.getSystemPropertySqlTranslationProfile()) : PhysicalConnection.getSystemPropertySqlTranslationProfile();
    }

    private static final void setConnectionProtocolID(Connection connection, int n2) {
        if (connection instanceof PhysicalConnection) {
            ((PhysicalConnection)connection).protocolId = n2;
        }
    }

    @Supports(value={Feature.SQL_TRANSLATION})
    private Connection babelfishConnect(@Blind(value=PropertiesBlinder.class) Properties properties, String string, String string2, OracleDriverExtension oracleDriverExtension, int n2) throws SQLException {
        Object object;
        properties.put("oracle.jdbc.sqlTranslationProfile", string);
        string = null;
        if (properties != null) {
            string = properties.getProperty("oracle.jdbc.sqlErrorTranslationFile");
        }
        if (string == null) {
            string = System.getProperty("oracle.jdbc.sqlErrorTranslationFile", null);
        }
        if (string != null) {
            properties.put("oracle.jdbc.sqlErrorTranslationFile", string);
        }
        string = null;
        if (properties != null && (string = properties.getProperty(user_string)) == null) {
            string = properties.getProperty("oracle.jdbc.user");
        }
        if (string == null) {
            string = System.getProperty("oracle.jdbc.user", null);
        }
        if (string == null) {
            object = PhysicalConnection.parseUrl(string2);
            string = ((Hashtable)object).get(user_string);
        }
        properties.put(user_string, string);
        object = ProxyFactory.createJDBCProxyFactory(BabelfishGenericProxy.class, BabelfishConnection.class, BabelfishStatement.class, BabelfishPreparedStatement.class, BabelfishCallableStatement.class);
        Translator translator = TranslationManager.getTranslator(string2, properties.getProperty(user_string), properties.getProperty("oracle.jdbc.sqlTranslationProfile"), properties.getProperty("oracle.jdbc.sqlErrorTranslationFile"));
        try {
            PhysicalConnection physicalConnection = (PhysicalConnection)oracleDriverExtension.getConnection(string2, properties);
            physicalConnection.protocolId = n2;
            Connection connection = ((ProxyFactory)object).proxyFor(physicalConnection);
            ((BabelfishConnection)((Object)connection)).setTranslator(translator);
            translator.activateServerTranslation(physicalConnection);
            return connection;
        }
        catch (SQLException sQLException) {
            throw translator.translateError(sQLException);
        }
    }

    public Connection defaultConnection() throws SQLException {
        if (defaultConn == null || defaultConn.isClosed()) {
            try (Monitor.CloseableLock closeableLock = DEFAULT_CONN_MONITOR.acquireCloseableLock();){
                if (defaultConn == null || defaultConn.isClosed()) {
                    defaultConn = this.connect("jdbc:oracle:kprb:", new Properties());
                }
            }
        }
        return defaultConn;
    }

    static final int oracleDriverExtensionTypeFromURL(String string) {
        int n2;
        int n3 = string.indexOf(58);
        if (n3 == -1) {
            return -2;
        }
        if (!string.regionMatches(true, 0, jdbc_string, 0, n3)) {
            return -2;
        }
        if ((n2 = string.indexOf(58, ++n3)) == -1) {
            return -2;
        }
        if (!string.regionMatches(true, n3, oracle_string, 0, n2 - n3)) {
            return -2;
        }
        int n4 = string.indexOf(58, ++n2);
        String string2 = null;
        if (n4 == -1) {
            return -3;
        }
        string2 = string.substring(n2, n4);
        if (string2.equals("thin")) {
            return 0;
        }
        if (string2.equals("oci8") || string2.equals("oci")) {
            return 2;
        }
        return -3;
    }

    @Override
    @Supports(value={Feature.PLATFORM})
    public boolean acceptsURL(String string) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(296).fillInStackTrace();
        }
        if (string.startsWith("jdbc:oracle:")) {
            return OracleDriver.oracleDriverExtensionTypeFromURL(string) > -2;
        }
        return false;
    }

    @Override
    @Supports(value={Feature.METADATA})
    public DriverPropertyInfo[] getPropertyInfo(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        String string2;
        Object object;
        Class<?> clazz = null;
        try {
            clazz = Class.forName("oracle.jdbc.OracleConnection");
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        int n2 = 0;
        String[] stringArray = new String[150];
        String[] stringArray2 = new String[150];
        Field[] fieldArray = clazz.getFields();
        for (int i2 = 0; i2 < fieldArray.length; ++i2) {
            if (!fieldArray[i2].getName().startsWith("CONNECTION_PROPERTY_") || fieldArray[i2].getName().endsWith("_DEFAULT") || fieldArray[i2].getName().endsWith("_ACCESSMODE")) continue;
            try {
                object = (String)fieldArray[i2].get(null);
                Field field = clazz.getField(fieldArray[i2].getName() + "_DEFAULT");
                string2 = (String)field.get(null);
                if (n2 == stringArray.length) {
                    String[] stringArray3 = new String[stringArray.length * 2];
                    String[] stringArray4 = new String[stringArray.length * 2];
                    System.arraycopy(stringArray, 0, stringArray3, 0, stringArray.length);
                    System.arraycopy(stringArray2, 0, stringArray4, 0, stringArray.length);
                    stringArray = stringArray3;
                    stringArray2 = stringArray4;
                }
                stringArray[n2] = object;
                stringArray2[n2] = string2;
                ++n2;
                continue;
            }
            catch (IllegalAccessException illegalAccessException) {
                continue;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                // empty catch block
            }
        }
        Hashtable<String, String> hashtable = PhysicalConnection.parseUrl(string);
        object = new DriverPropertyInfo[n2];
        for (int i3 = 0; i3 < n2; ++i3) {
            string2 = stringArray[i3];
            object[i3] = hashtable.containsKey(string2) ? new DriverPropertyInfo(string2, hashtable.get(string2)) : new DriverPropertyInfo(stringArray[i3], stringArray2[i3]);
        }
        return object;
    }

    @Override
    @Supports(value={Feature.METADATA})
    public int getMajorVersion() {
        return OracleDatabaseMetaData.getDriverMajorVersionInfo();
    }

    @Override
    @Supports(value={Feature.METADATA})
    public int getMinorVersion() {
        return OracleDatabaseMetaData.getDriverMinorVersionInfo();
    }

    @Override
    @Supports(value={Feature.METADATA})
    public boolean jdbcCompliant() {
        return true;
    }

    @Supports(value={Feature.PLATFORM})
    public String processSqlEscapes(String string) throws SQLException {
        OracleSql oracleSql = new OracleSql(null);
        oracleSql.initialize(string);
        return oracleSql.parse(string);
    }

    @Supports(value={Feature.METADATA})
    public static String getCompileTime() {
        return BuildInfo.getBuildDate();
    }

    @Supports(value={Feature.METADATA})
    public static String getSystemPropertyFastConnectionFailover(String string) {
        return PhysicalConnection.getSystemPropertyFastConnectionFailover(string);
    }

    @Supports(value={Feature.METADATA})
    public static boolean getSystemPropertyDateZeroTime() {
        String string = PhysicalConnection.getSystemPropertyDateZeroTime("false");
        return string.equalsIgnoreCase("true");
    }

    @Supports(value={Feature.METADATA})
    public static boolean getSystemPropertyDateZeroTimeExtra() {
        String string = PhysicalConnection.getSystemPropertyDateZeroTimeExtra("false");
        return string.equalsIgnoreCase("true");
    }

    @Supports(value={Feature.PLATFORM})
    public static ExecutorService getExecutorService() throws SQLException {
        return threadPool;
    }

    @Supports(value={Feature.PLATFORM})
    public static void setExecutorService(ExecutorService executorService) throws SQLException {
        if (threadPool != null) {
            threadPool.shutdownNow();
        }
        threadPool = executorService;
    }

    @Override
    @Supports(value={Feature.PLATFORM})
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(oracle_string);
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }

    static {
        Object object;
        driverExtensionClassNames = new String[]{"oracle.jdbc.driver.T4CDriverExtension", "oracle.jdbc.driver.T4CDriverExtension", "oracle.jdbc.driver.T2CDriverExtension", "oracle.jdbc.driver.T2SDriverExtension"};
        defaultConn = null;
        DEFAULT_CONN_MONITOR = Monitor.newInstance();
        defaultDriver = null;
        try {
            if (defaultDriver == null) {
                defaultDriver = new oracle.jdbc.OracleDriver();
                DriverManager.registerDriver(defaultDriver, OracleDriver::deregister);
            }
            AccessController.doPrivileged(new PrivilegedAction<Object>(){

                @Override
                public Object run() {
                    OracleDriver.registerMBeans();
                    return null;
                }
            });
            object = Timestamp.valueOf("2000-01-01 00:00:00.0");
        }
        catch (SQLException sQLException) {
            Logger.getLogger("oracle.jdbc.driver").log(Level.SEVERE, "SQLException in static block.", sQLException);
        }
        catch (RuntimeException runtimeException) {
            Logger.getLogger("oracle.jdbc.driver").log(Level.SEVERE, "RuntimeException in static block.", runtimeException);
        }
        try {
            object = new OraclePKIProvider();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        systemTypeMap = new Hashtable(2);
        try {
            systemTypeMap.put("SYS.ANYDATA", Class.forName("oracle.sql.AnyDataFactory"));
            systemTypeMap.put("SYS.ANYTYPE", Class.forName("oracle.sql.TypeDescriptorFactory"));
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        DEFAULT_CONNECTION_PROPERTIES = new Properties();
        try {
            object = PhysicalConnection.class.getResourceAsStream(DEFAULT_CONNECTION_PROPERTIES_RESOURCE_NAME);
            if (object != null) {
                DEFAULT_CONNECTION_PROPERTIES.load((InputStream)object);
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        threadPool = Executors.newCachedThreadPool(new ThreadFactory(){
            private final AtomicInteger numCreatedThreads = new AtomicInteger(0);
            private static final String THREAD_NAME_PREFIX = "OJDBC-WORKER-THREAD-";

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(null, runnable, THREAD_NAME_PREFIX + this.numCreatedThreads.incrementAndGet());
                thread.setPriority(5);
                thread.setDaemon(true);
                return thread;
            }
        });
        SERVER_SIDE_DEFAULT_URL_PREFIX_LENGTH = SERVER_SIDE_DEFAULT_URL_PREFIX.length();
        diagnosticMBeanObjectName = null;
    }
}

