/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.ucp.jdbc.PoolDataSource
 *  oracle.ucp.jdbc.UCPConnectionBuilder
 */
package oracle.jdbc.driver;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import javax.transaction.xa.XAResource;
import oracle.jdbc.LogicalTransactionIdEventListener;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleOCIFailover;
import oracle.jdbc.OracleSavepoint;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.aq.AQDequeueOptions;
import oracle.jdbc.aq.AQEnqueueOptions;
import oracle.jdbc.aq.AQMessage;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.aq.AQNotificationRegistration;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.driver.AbstractShardingLob;
import oracle.jdbc.driver.AbstractShardingPreparedStatement;
import oracle.jdbc.driver.AbstractShardingStatement;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.HAManager;
import oracle.jdbc.driver.LogicalConnection;
import oracle.jdbc.driver.OracleDriverExtension;
import oracle.jdbc.driver.ShardingConnectionUtil;
import oracle.jdbc.driver.ShardingDriverExtension;
import oracle.jdbc.driver.T4CConnection;
import oracle.jdbc.internal.AbstractConnectionBuilder;
import oracle.jdbc.internal.AdditionalDatabaseMetaData;
import oracle.jdbc.internal.JMSDequeueOptions;
import oracle.jdbc.internal.JMSEnqueueOptions;
import oracle.jdbc.internal.JMSMessage;
import oracle.jdbc.internal.JMSNotificationRegistration;
import oracle.jdbc.internal.KeywordValueLong;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.NetStat;
import oracle.jdbc.internal.OracleArray;
import oracle.jdbc.internal.OracleBfile;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleLargeObject;
import oracle.jdbc.internal.OraclePreparedStatement;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.internal.PDBChangeEventListener;
import oracle.jdbc.internal.ReplayContext;
import oracle.jdbc.internal.XSEventListener;
import oracle.jdbc.internal.XSKeyval;
import oracle.jdbc.internal.XSNamespace;
import oracle.jdbc.internal.XSPrincipal;
import oracle.jdbc.internal.XSSecureId;
import oracle.jdbc.internal.XSSessionParameters;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.oracore.OracleTypeADT;
import oracle.jdbc.oracore.OracleTypeCLOB;
import oracle.jdbc.pool.OracleConnectionCacheCallback;
import oracle.jdbc.pool.OraclePooledConnection;
import oracle.jdbc.proxy.annotation.GetCreator;
import oracle.jdbc.proxy.annotation.GetDelegate;
import oracle.jdbc.proxy.annotation.Methods;
import oracle.jdbc.proxy.annotation.OnError;
import oracle.jdbc.proxy.annotation.Post;
import oracle.jdbc.proxy.annotation.Pre;
import oracle.jdbc.proxy.annotation.ProxyFor;
import oracle.jdbc.proxy.annotation.ProxyResult;
import oracle.jdbc.proxy.annotation.ProxyResultPolicy;
import oracle.jdbc.proxy.annotation.SetDelegate;
import oracle.jdbc.proxy.annotation.Signature;
import oracle.net.resolver.AddrResolution;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.BFILE;
import oracle.sql.BLOB;
import oracle.sql.BfileDBAccess;
import oracle.sql.BlobDBAccess;
import oracle.sql.CLOB;
import oracle.sql.ClobDBAccess;
import oracle.sql.CustomDatum;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TIMEZONETAB;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.UCPConnectionBuilder;

@ProxyFor(value={oracle.jdbc.internal.OracleConnection.class})
@ProxyResult(value=ProxyResultPolicy.MANUAL)
public abstract class AbstractShardingConnection {
    private final ReentrantLock connectionLock = new ReentrantLock();
    private final Monitor.CloseableLock connectionClosableLock = Monitor.CloseableLock.wrap(this.connectionLock);
    private final int MAX_SHARD_STATEMENT_CACHE_SIZE = 100;
    private String applicationURL;
    private String resolvedApplicationURL;
    private Properties applicationProps;
    private PoolDataSource pdsDirectShardDatabase;
    private PoolDataSource pdsCatalogDatabase;
    private final AtomicInteger totalQueryExecutionOnCatalogDB = new AtomicInteger();
    private final AtomicInteger totalQueryExecutionOnDirectShard = new AtomicInteger();
    Map<AbstractShardingStatement, Boolean> statements;
    private short dbCharSet;
    OracleDriverExtension driverExtension;
    boolean autoCommit = true;
    private oracle.jdbc.internal.OracleConnection catalogDatabaseConnection;
    private oracle.jdbc.internal.OracleConnection stickyDatabaseConnection;
    ConcurrentHashMap<String, AbstractShardingStatement.SetterCallHistoryEntry> setterMap = new ConcurrentHashMap();
    int lifecycle;
    static final int OPEN = 1;
    static final int CLOSING = 2;
    static final int CLOSED = 4;
    static final int ABORTED = 8;
    static final int BLOCKED = 16;
    LogicalConnection logicalConnectionAttached = null;
    boolean shardingReplayEnable;
    String gsmServiceName;
    String userName;
    String schemaName;
    boolean allowSingleShardTransaction;

    @GetCreator
    protected abstract Object getCreator();

    @GetDelegate
    protected abstract oracle.jdbc.internal.OracleConnection getDelegate();

    @SetDelegate
    protected abstract void setDelegate(oracle.jdbc.internal.OracleConnection var1);

    void initialize(String string, @Blind(value=PropertiesBlinder.class) Properties properties, OracleDriverExtension oracleDriverExtension, AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) throws SQLException {
        Object object;
        boolean bl = true;
        this.applicationURL = string;
        this.driverExtension = oracleDriverExtension;
        this.allowSingleShardTransaction = abstractConnectionBuilder != null ? abstractConnectionBuilder.getAllowSingleShardTransaction() : false;
        this.applicationProps = new Properties();
        if (properties != null) {
            this.applicationProps.putAll(properties);
        }
        if (this.applicationURL != null && Pattern.compile(Pattern.quote("SHARDING_KEY"), 2).matcher(this.applicationURL).find()) {
            throw (SQLException)DatabaseError.createSqlException(1708).fillInStackTrace();
        }
        if (properties != null && (object = properties.getProperty("oracle.jdbc.targetShardingKey")) != null) {
            throw (SQLException)DatabaseError.createSqlException(1708).fillInStackTrace();
        }
        this.parseShardingConnectionProperties(string, properties);
        this.gsmServiceName = this.getGsmServiceNameInUrl(this.applicationURL, properties);
        this.applicationProps.setProperty("InternalShardingDriverMode", Boolean.toString(bl));
        if (this.applicationProps.getProperty("oracle.jdbc.implicitStatementCacheSize") == null) {
            this.applicationProps.setProperty("oracle.jdbc.implicitStatementCacheSize", Integer.toString(100));
        }
        object = ShardingConnectionUtil.getShardingDatabasePoolDataSource(this.applicationURL, this.applicationProps, this.gsmServiceName, this.shardingReplayEnable, this.resolvedApplicationURL);
        this.pdsDirectShardDatabase = ((ShardingConnectionUtil.ShardingPoolDataSourceEntry)object).getPds();
        this.userName = ((ShardingConnectionUtil.ShardingPoolDataSourceEntry)object).getUserName();
        this.schemaName = ((ShardingConnectionUtil.ShardingPoolDataSourceEntry)object).getSchemaName();
        this.pdsCatalogDatabase = ShardingConnectionUtil.getCatalogDatabasePoolDataSource();
        this.dbCharSet = ShardingConnectionUtil.getDbCharsSet();
        this.statements = new ConcurrentHashMap<AbstractShardingStatement, Boolean>();
        this.lifecycle = 1;
    }

    @Pre
    @Methods(signatures={@Signature(name="archive", args={int.class, int.class, String.class}), @Signature(name="getAutoClose", args={}), @Signature(name="getJavaObject", args={String.class}), @Signature(name="getSQLType", args={Object.class}), @Signature(name="pingDatabase", args={}), @Signature(name="pingDatabase", args={int.class}), @Signature(name="setAutoClose", args={boolean.class}), @Signature(name="getUserName", args={}), @Signature(name="getCurrentSchema", args={}), @Signature(name="shutdown", args={OracleConnection.DatabaseShutdownMode.class}), @Signature(name="startup", args={String.class, int.class}), @Signature(name="startup", args={OracleConnection.DatabaseShutdownMode.class}), @Signature(name="startup", args={OracleConnection.DatabaseShutdownMode.class, String.class}), @Signature(name="oracleSetSavepoint", args={}), @Signature(name="oracleSetSavepoint", args={String.class}), @Signature(name="oracleRollback", args={OracleSavepoint.class}), @Signature(name="oracleReleaseSavepoint", args={OracleSavepoint.class}), @Signature(name="releaseSavepoint", args={Savepoint.class}), @Signature(name="rollback", args={Savepoint.class}), @Signature(name="getProperties", args={}), @Signature(name="registerTAFCallback", args={OracleOCIFailover.class, Object.class}), @Signature(name="registerConnectionCacheCallback", args={OracleConnectionCacheCallback.class, Object.class, int.class}), @Signature(name="createArrayOf", args={String.class, Object[].class}), @Signature(name="createARRAY", args={String.class, Object.class}), @Signature(name="createBINARY_DOUBLE", args={double.class}), @Signature(name="createBINARY_FLOAT", args={float.class}), @Signature(name="createDATE", args={Date.class}), @Signature(name="createDATE", args={Time.class}), @Signature(name="createDATE", args={Timestamp.class}), @Signature(name="createDATE", args={Date.class, Calendar.class}), @Signature(name="createDATE", args={Time.class, Calendar.class}), @Signature(name="createDATE", args={Timestamp.class, Calendar.class}), @Signature(name="createDATE", args={String.class}), @Signature(name="createINTERVALDS", args={String.class}), @Signature(name="createINTERVALYM", args={String.class}), @Signature(name="createNUMBER", args={boolean.class}), @Signature(name="createNUMBER", args={byte.class}), @Signature(name="createNUMBER", args={short.class}), @Signature(name="createNUMBER", args={int.class}), @Signature(name="createNUMBER", args={long.class}), @Signature(name="createNUMBER", args={float.class}), @Signature(name="createNUMBER", args={double.class}), @Signature(name="createNUMBER", args={BigDecimal.class}), @Signature(name="createNUMBER", args={BigInteger.class}), @Signature(name="createNUMBER", args={String.class, int.class}), @Signature(name="createOracleArray", args={String.class, Object.class}), @Signature(name="createSQLXML", args={}), @Signature(name="createStruct", args={String.class, Object[].class}), @Signature(name="createTIMESTAMP", args={Date.class}), @Signature(name="createTIMESTAMP", args={DATE.class}), @Signature(name="createTIMESTAMP", args={Time.class}), @Signature(name="createTIMESTAMP", args={Timestamp.class}), @Signature(name="createTIMESTAMP", args={Timestamp.class, Calendar.class}), @Signature(name="createTIMESTAMP", args={String.class}), @Signature(name="createTIMESTAMPTZ", args={Date.class}), @Signature(name="createTIMESTAMPTZ", args={Date.class, Calendar.class}), @Signature(name="createTIMESTAMPTZ", args={Time.class}), @Signature(name="createTIMESTAMPTZ", args={Time.class, Calendar.class}), @Signature(name="createTIMESTAMPTZ", args={Timestamp.class}), @Signature(name="createTIMESTAMPTZ", args={Timestamp.class, Calendar.class}), @Signature(name="createTIMESTAMPTZ", args={Timestamp.class, ZoneId.class}), @Signature(name="createTIMESTAMPTZ", args={String.class}), @Signature(name="createTIMESTAMPTZ", args={String.class, Calendar.class}), @Signature(name="createTIMESTAMPTZ", args={DATE.class}), @Signature(name="createTIMESTAMPLTZ", args={Date.class, Calendar.class}), @Signature(name="createTIMESTAMPLTZ", args={Time.class, Calendar.class}), @Signature(name="createTIMESTAMPLTZ", args={Timestamp.class, Calendar.class}), @Signature(name="createTIMESTAMPLTZ", args={String.class, Calendar.class}), @Signature(name="createTIMESTAMPLTZ", args={DATE.class, Calendar.class}), @Signature(name="getLogicalTransactionId", args={}), @Signature(name="isDRCPEnabled", args={}), @Signature(name="isDRCPMultitagEnabled", args={}), @Signature(name="getDRCPReturnTag", args={}), @Signature(name="getDRCPPLSQLCallbackName", args={}), @Signature(name="attachServerConnection", args={}), @Signature(name="detachServerConnection", args={String.class}), @Signature(name="getDRCPState", args={}), @Signature(name="clearDrcpTagName", args={}), @Signature(name="getSchema", args={}), @Signature(name="getTransactionIsolation", args={}), @Signature(name="setTransactionIsolation", args={int.class}), @Signature(name="nativeSQL", args={String.class}), @Signature(name="setSavepoint", args={}), @Signature(name="setSavepoint", args={String.class}), @Signature(name="registerAQNotification", args={String[].class, Properties[].class, Properties.class}), @Signature(name="unregisterAQNotification", args={AQNotificationRegistration.class}), @Signature(name="dequeue", args={String.class, AQDequeueOptions.class, byte[].class}), @Signature(name="dequeue", args={String.class, AQDequeueOptions.class, String.class}), @Signature(name="enqueue", args={String.class, AQEnqueueOptions.class, AQMessage.class}), @Signature(name="registerDatabaseChangeNotification", args={Properties.class}), @Signature(name="getDatabaseChangeRegistration", args={int.class}), @Signature(name="unregisterDatabaseChangeNotification", args={DatabaseChangeRegistration.class}), @Signature(name="unregisterDatabaseChangeNotification", args={int.class, String.class, int.class}), @Signature(name="unregisterDatabaseChangeNotification", args={int.class}), @Signature(name="unregisterDatabaseChangeNotification", args={long.class, String.class}), @Signature(name="getAllTypeDescriptorsInCurrentSchema", args={}), @Signature(name="getTypeDescriptorsFromListInCurrentSchema", args={String[].class}), @Signature(name="getTypeDescriptorsFromList", args={String[][].class}), @Signature(name="getDataIntegrityAlgorithmName", args={}), @Signature(name="getEncryptionAlgorithmName", args={}), @Signature(name="getAuthenticationAdaptorName", args={}), @Signature(name="addLogicalTransactionIdEventListener", args={LogicalTransactionIdEventListener.class}), @Signature(name="addLogicalTransactionIdEventListener", args={LogicalTransactionIdEventListener.class, Executor.class}), @Signature(name="removeLogicalTransactionIdEventListener", args={LogicalTransactionIdEventListener.class}), @Signature(name="getEncryptionProviderName", args={}), @Signature(name="getChecksumProviderName", args={}), @Signature(name="jmsEnqueue", args={String.class, JMSEnqueueOptions.class, JMSMessage.class, AQMessageProperties.class}), @Signature(name="jmsDequeue", args={String.class, JMSDequeueOptions.class}), @Signature(name="jmsDequeue", args={String.class, JMSDequeueOptions.class, OutputStream.class}), @Signature(name="jmsDequeue", args={String.class, JMSDequeueOptions.class, String.class}), @Signature(name="registerJMSNotification", args={String[].class, Map.class}), @Signature(name="registerJMSNotification", args={String[].class, Map.class, String.class}), @Signature(name="unregisterJMSNotification", args={JMSNotificationRegistration.class}), @Signature(name="startJMSNotification", args={JMSNotificationRegistration.class}), @Signature(name="stopJMSNotification", args={JMSNotificationRegistration.class}), @Signature(name="ackJMSNotification", args={JMSNotificationRegistration.class, byte[].class, JMSNotificationRegistration.Directive.class}), @Signature(name="ackJMSNotification", args={ArrayList.class, byte[][].class, JMSNotificationRegistration.Directive.class}), @Signature(name="abort", args={}), @Signature(name="abort", args={Executor.class}), @Signature(name="setPDBChangeEventListener", args={PDBChangeEventListener.class}), @Signature(name="setPDBChangeEventListener", args={PDBChangeEventListener.class, Executor.class}), @Signature(name="getVersionNumber", args={}), @Signature(name="getBigEndian", args={}), @Signature(name="getDbCsId", args={}), @Signature(name="getJdbcCsId", args={}), @Signature(name="getNCharSet", args={}), @Signature(name="getDriverCharSet", args={}), @Signature(name="getC2SNlsRatio", args={}), @Signature(name="getMaxCharSize", args={}), @Signature(name="getMaxCharbyteSize", args={}), @Signature(name="getMaxNCharbyteSize", args={}), @Signature(name="setTxnMode", args={int.class}), @Signature(name="getTxnMode", args={}), @Signature(name="getVarTypeMaxLenCompat", args={}), @Signature(name="getDatabaseProductVersion", args={}), @Signature(name="getXAResource", args={}), @Signature(name="getDatabaseTimeZone", args={}), @Signature(name="getTimestamptzInGmt", args={}), @Signature(name="getUse1900AsYearForTime", args={}), @Signature(name="isDataInLocatorEnabled", args={}), @Signature(name="isLobStreamPosStandardCompliant", args={}), @Signature(name="getCurrentSCN", args={}), @Signature(name="isConnectionSocketKeepAlive", args={}), @Signature(name="isConnectionBigTZTC", args={}), @Signature(name="getNegotiatedSDU", args={}), @Signature(name="getNegotiatedTTCVersion", args={}), @Signature(name="isNetworkCompressionEnabled", args={}), @Signature(name="getOutboundConnectTimeout", args={}), @Signature(name="getTransactionState", args={}), @Signature(name="setFDO", args={byte[].class}), @Signature(name="getFDO", args={boolean.class}), @Signature(name="getDescriptor", args={byte[].class}), @Signature(name="getDescriptor", args={String.class}), @Signature(name="putDescriptor", args={byte[].class, Object.class}), @Signature(name="putDescriptor", args={String.class, Object.class}), @Signature(name="removeDescriptor", args={String.class}), @Signature(name="removeAllDescriptor", args={}), @Signature(name="numberOfDescriptorCacheEntries", args={}), @Signature(name="descriptorCacheKeys", args={}), @Signature(name="getSessionTimeZoneOffset", args={}), @Signature(name="getStructAttrCsId", args={}), @Signature(name="registerSQLType", args={String.class, Class.class}), @Signature(name="registerSQLType", args={String.class, String.class}), @Signature(name="setWrapper", args={OracleConnection.class}), @Signature(name="getWrapper", args={}), @Signature(name="setPlsqlWarnings", args={String.class}), @Signature(name="getStructAttrNCsId", args={}), @Signature(name="isWrapperFor", args={Class.class}), @Signature(name="IsNCharFixedWith", args={}), @Signature(name="isCharSetMultibyte", args={short.class}), @Signature(name="isV8Compatible", args={}), @Signature(name="getMapDateToTimestamp", args={}), @Signature(name="getJDBCStandardBehavior", args={}), @Signature(name="getTimezoneVersionNumber", args={}), @Signature(name="getTIMEZONETAB", args={}), @Signature(name="setApplicationContext", args={String.class, String.class, String.class}), @Signature(name="clearAllApplicationContext", args={String.class}), @Signature(name="clearWarnings", args={}), @Signature(name="getClientInfo", args={}), @Signature(name="getClientInfo", args={String.class}), @Signature(name="getNetworkTimeout", args={}), @Signature(name="getWarnings", args={}), @Signature(name="isReadOnly", args={}), @Signature(name="setClientInfo", args={Properties.class}), @Signature(name="setClientInfo", args={String.class, String.class}), @Signature(name="setNetworkTimeout", args={Executor.class, int.class}), @Signature(name="setReadOnly", args={boolean.class}), @Signature(name="getTypeMap", args={}), @Signature(name="setTypeMap", args={Map.class}), @Signature(name="getJavaObjectTypeMap", args={}), @Signature(name="setJavaObjectTypeMap", args={Map.class}), @Signature(name="getInstanceProperty", args={OracleConnection.InstanceProperty.class}), @Signature(name="classForNameAndSchema", args={String.class, String.class}), @Signature(name="CHARBytesToJavaChars", args={byte[].class, int.class, char[].class}), @Signature(name="NCHARBytesToJavaChars", args={byte[].class, int.class, char[].class}), @Signature(name="javaCharsToCHARBytes", args={char[].class, int.class, byte[].class}), @Signature(name="javaCharsToNCHARBytes", args={char[].class, int.class, byte[].class}), @Signature(name="getPropertyForPooledConnection", args={OraclePooledConnection.class}), @Signature(name="getServerSessionInfo", args={}), @Signature(name="isDescriptorSharable", args={oracle.jdbc.internal.OracleConnection.class}), @Signature(name="createLightweightSession", args={String.class, KeywordValueLong[].class, int.class, KeywordValueLong[][].class, int[].class}), @Signature(name="getDefaultSchemaNameForNamedTypes", args={}), @Signature(name="getClassForType", args={String.class, Map.class}), @Signature(name="getJavaNetProperties", args={}), @Signature(name="getClientInfoInternal", args={}), @Signature(name="setSafelyClosed", args={boolean.class}), @Signature(name="isSafelyClosed", args={}), @Signature(name="getNetworkStat", args={}), @Signature(name="toDatum", args={CustomDatum.class}), @Signature(name="createBlob", args={}), @Signature(name="createClob", args={}), @Signature(name="createNClob", args={}), @Signature(name="createClob", args={byte[].class}), @Signature(name="createClobWithUnpickledBytes", args={byte[].class}), @Signature(name="createClob", args={byte[].class, short.class}), @Signature(name="createBlob", args={byte[].class}), @Signature(name="createBlobWithUnpickledBytes", args={byte[].class}), @Signature(name="createBfile", args={byte[].class}), @Signature(name="newStructMetaData", args={StructDescriptor.class}), @Signature(name="createBfileDBAccess", args={}), @Signature(name="createBlobDBAccess", args={}), @Signature(name="createClobDBAccess", args={})})
    protected void preMethodsOnCatalogDatabase(Method method, Object object, Object ... objectArray) {
        try {
            this.acquireConnectionLock();
            oracle.jdbc.internal.OracleConnection oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            this.setDelegate(oracleConnection);
        }
        catch (SQLException sQLException) {
            this.releaseConnectionLock();
            throw new RuntimeException(sQLException);
        }
    }

    @Post
    @Methods(signatures={@Signature(name="getAutoClose", args={}), @Signature(name="getJavaObject", args={String.class}), @Signature(name="getSQLType", args={Object.class}), @Signature(name="pingDatabase", args={}), @Signature(name="pingDatabase", args={int.class}), @Signature(name="getUserName", args={}), @Signature(name="getCurrentSchema", args={}), @Signature(name="oracleSetSavepoint", args={}), @Signature(name="oracleSetSavepoint", args={String.class}), @Signature(name="getProperties", args={}), @Signature(name="createBINARY_DOUBLE", args={double.class}), @Signature(name="createBINARY_FLOAT", args={float.class}), @Signature(name="createDATE", args={Date.class}), @Signature(name="createDATE", args={Time.class}), @Signature(name="createDATE", args={Timestamp.class}), @Signature(name="createDATE", args={Date.class, Calendar.class}), @Signature(name="createDATE", args={Time.class, Calendar.class}), @Signature(name="createDATE", args={Timestamp.class, Calendar.class}), @Signature(name="createDATE", args={String.class}), @Signature(name="createINTERVALDS", args={String.class}), @Signature(name="createINTERVALYM", args={String.class}), @Signature(name="createNUMBER", args={boolean.class}), @Signature(name="createNUMBER", args={byte.class}), @Signature(name="createNUMBER", args={short.class}), @Signature(name="createNUMBER", args={int.class}), @Signature(name="createNUMBER", args={long.class}), @Signature(name="createNUMBER", args={float.class}), @Signature(name="createNUMBER", args={double.class}), @Signature(name="createNUMBER", args={BigDecimal.class}), @Signature(name="createNUMBER", args={BigInteger.class}), @Signature(name="createNUMBER", args={String.class, int.class}), @Signature(name="createSQLXML", args={}), @Signature(name="createTIMESTAMP", args={DATE.class}), @Signature(name="createTIMESTAMP", args={Time.class}), @Signature(name="createTIMESTAMP", args={Timestamp.class}), @Signature(name="createTIMESTAMP", args={Timestamp.class, Calendar.class}), @Signature(name="createTIMESTAMP", args={String.class}), @Signature(name="createTIMESTAMPTZ", args={Date.class}), @Signature(name="createTIMESTAMPTZ", args={Date.class, Calendar.class}), @Signature(name="createTIMESTAMPTZ", args={Time.class}), @Signature(name="createTIMESTAMPTZ", args={Time.class, Calendar.class}), @Signature(name="createTIMESTAMPTZ", args={Timestamp.class}), @Signature(name="createTIMESTAMPTZ", args={Timestamp.class, Calendar.class}), @Signature(name="createTIMESTAMPTZ", args={Timestamp.class, ZoneId.class}), @Signature(name="createTIMESTAMPTZ", args={String.class}), @Signature(name="createTIMESTAMPTZ", args={String.class, Calendar.class}), @Signature(name="createTIMESTAMPTZ", args={DATE.class}), @Signature(name="createTIMESTAMPLTZ", args={Date.class, Calendar.class}), @Signature(name="createTIMESTAMPLTZ", args={Time.class, Calendar.class}), @Signature(name="createTIMESTAMPLTZ", args={Timestamp.class, Calendar.class}), @Signature(name="createTIMESTAMPLTZ", args={String.class, Calendar.class}), @Signature(name="createTIMESTAMPLTZ", args={DATE.class, Calendar.class}), @Signature(name="getLogicalTransactionId", args={}), @Signature(name="isDRCPEnabled", args={}), @Signature(name="isDRCPMultitagEnabled", args={}), @Signature(name="getDRCPReturnTag", args={}), @Signature(name="getDRCPPLSQLCallbackName", args={}), @Signature(name="attachServerConnection", args={}), @Signature(name="getDRCPState", args={}), @Signature(name="getSchema", args={}), @Signature(name="getTransactionIsolation", args={}), @Signature(name="setTransactionIsolation", args={int.class}), @Signature(name="nativeSQL", args={String.class}), @Signature(name="setSavepoint", args={}), @Signature(name="setSavepoint", args={String.class}), @Signature(name="registerAQNotification", args={String[].class, Properties[].class, Properties.class}), @Signature(name="dequeue", args={String.class, AQDequeueOptions.class, byte[].class}), @Signature(name="dequeue", args={String.class, AQDequeueOptions.class, String.class}), @Signature(name="registerDatabaseChangeNotification", args={Properties.class}), @Signature(name="getDatabaseChangeRegistration", args={int.class}), @Signature(name="getAllTypeDescriptorsInCurrentSchema", args={}), @Signature(name="getTypeDescriptorsFromListInCurrentSchema", args={String[].class}), @Signature(name="getTypeDescriptorsFromList", args={String[][].class}), @Signature(name="getDataIntegrityAlgorithmName", args={}), @Signature(name="getEncryptionAlgorithmName", args={}), @Signature(name="getAuthenticationAdaptorName", args={}), @Signature(name="getEncryptionProviderName", args={}), @Signature(name="getChecksumProviderName", args={}), @Signature(name="setTxnMode", args={int.class}), @Signature(name="getDescriptor", args={byte[].class}), @Signature(name="getDescriptor", args={String.class}), @Signature(name="getClientInfo", args={}), @Signature(name="getWarnings", args={})})
    protected Object postMethodsOnCatalogDatabase(Method method, Object object) {
        this.postConnectionMethodsCleanup();
        return object;
    }

    @Post
    @Methods(signatures={@Signature(name="getNegotiatedTTCVersion", args={}), @Signature(name="getInstanceProperty", args={OracleConnection.InstanceProperty.class})})
    protected byte postByteMethodsOnCatalogDatabase(Method method, byte by) {
        this.postConnectionMethodsCleanup();
        return by;
    }

    @Post
    @Methods(signatures={@Signature(name="getFDO", args={boolean.class}), @Signature(name="createLightweightSession", args={String.class, KeywordValueLong[].class, int.class, KeywordValueLong[][].class, int[].class})})
    protected byte[] postByteArrayMethodsOnCatalogDatabase(Method method, byte[] byArray) {
        this.postConnectionMethodsCleanup();
        return byArray;
    }

    @Post
    @Methods(signatures={@Signature(name="getVersionNumber", args={}), @Signature(name="getDbCsId", args={}), @Signature(name="getJdbcCsId", args={}), @Signature(name="getNCharSet", args={}), @Signature(name="getDriverCharSet", args={}), @Signature(name="getStructAttrCsId", args={}), @Signature(name="getStructAttrNCsId", args={})})
    protected short postShortMethodsOnCatalogDatabase(Method method, short s2) {
        this.postConnectionMethodsCleanup();
        return s2;
    }

    @Post
    @Methods(signatures={@Signature(name="getC2SNlsRatio", args={}), @Signature(name="getMaxCharSize", args={}), @Signature(name="getMaxCharbyteSize", args={}), @Signature(name="getMaxNCharbyteSize", args={}), @Signature(name="getTxnMode", args={}), @Signature(name="getVarTypeMaxLenCompat", args={}), @Signature(name="getNegotiatedSDU", args={}), @Signature(name="numberOfDescriptorCacheEntries", args={}), @Signature(name="getTimezoneVersionNumber", args={}), @Signature(name="getNetworkTimeout", args={}), @Signature(name="CHARBytesToJavaChars", args={byte[].class, int.class, char[].class}), @Signature(name="NCHARBytesToJavaChars", args={byte[].class, int.class, char[].class}), @Signature(name="javaCharsToCHARBytes", args={char[].class, int.class, byte[].class}), @Signature(name="javaCharsToNCHARBytes", args={char[].class, int.class, byte[].class}), @Signature(name="getOutboundConnectTimeout", args={})})
    protected int postIntMethodsOnCatalogDatabase(Method method, int n2) {
        this.postConnectionMethodsCleanup();
        return n2;
    }

    @Post
    @Methods(signatures={@Signature(name="getCurrentSCN", args={})})
    protected long postLongMethodsOnCatalogDatabase(Method method, long l2) {
        this.postConnectionMethodsCleanup();
        return l2;
    }

    @Post
    @Methods(signatures={@Signature(name="getBigEndian", args={}), @Signature(name="getTimestamptzInGmt", args={}), @Signature(name="getUse1900AsYearForTime", args={}), @Signature(name="isDataInLocatorEnabled", args={}), @Signature(name="isLobStreamPosStandardCompliant", args={}), @Signature(name="isConnectionSocketKeepAlive", args={}), @Signature(name="isNetworkCompressionEnabled", args={}), @Signature(name="isWrapperFor", args={Class.class}), @Signature(name="IsNCharFixedWith", args={}), @Signature(name="isCharSetMultibyte", args={short.class}), @Signature(name="isV8Compatible", args={}), @Signature(name="getMapDateToTimestamp", args={}), @Signature(name="getJDBCStandardBehavior", args={}), @Signature(name="isReadOnly", args={}), @Signature(name="isDescriptorSharable", args={oracle.jdbc.internal.OracleConnection.class}), @Signature(name="isSafelyClosed", args={})})
    protected boolean postBooleanMethodsOnCatalogDatabase(Method method, boolean bl) {
        this.postConnectionMethodsCleanup();
        return bl;
    }

    @Post
    @Methods(signatures={@Signature(name="getDatabaseProductVersion", args={}), @Signature(name="getDatabaseTimeZone", args={}), @Signature(name="getSessionTimeZoneOffset", args={}), @Signature(name="getClientInfo", args={String.class}), @Signature(name="getDefaultSchemaNameForNamedTypes", args={})})
    protected String postStringMethodsOnCatalogDatabase(Method method, String string) {
        this.postConnectionMethodsCleanup();
        return string;
    }

    @Post
    @Methods(signatures={@Signature(name="getTIMEZONETAB", args={})})
    protected TIMEZONETAB postTIMEZONETABMethodsOnCatalogDatabase(Method method, TIMEZONETAB tIMEZONETAB) {
        this.postConnectionMethodsCleanup();
        return tIMEZONETAB;
    }

    @Post
    @Methods(signatures={@Signature(name="jmsDequeue", args={String.class, JMSDequeueOptions.class}), @Signature(name="jmsDequeue", args={String.class, JMSDequeueOptions.class, OutputStream.class}), @Signature(name="jmsDequeue", args={String.class, JMSDequeueOptions.class, String.class})})
    protected JMSMessage postJmsDequeueMethodsOnCatalogDatabase(Method method, JMSMessage jMSMessage) {
        this.postConnectionMethodsCleanup();
        return jMSMessage;
    }

    @Post
    @Methods(signatures={@Signature(name="registerJMSNotification", args={String[].class, Map.class}), @Signature(name="registerJMSNotification", args={String[].class, Map.class, String.class}), @Signature(name="getTypeMap", args={}), @Signature(name="getJavaObjectTypeMap", args={})})
    protected Map<String, Class<?>> postJmsRegisterMethodsOnCatalogDatabase(Method method, Map<String, Class<?>> map) {
        this.postConnectionMethodsCleanup();
        return map;
    }

    @Post
    @Methods(signatures={@Signature(name="getXAResource", args={})})
    protected XAResource postXAResourceMethodsOnCatalogDatabase(Method method, XAResource xAResource) {
        this.postConnectionMethodsCleanup();
        return xAResource;
    }

    @Post
    @Methods(signatures={@Signature(name="getTransactionState", args={})})
    protected EnumSet<OracleConnection.TransactionState> postTransactionStateMethodsOnCatalogDatabase(Method method, EnumSet<OracleConnection.TransactionState> enumSet) {
        this.postConnectionMethodsCleanup();
        return enumSet;
    }

    @Post
    @Methods(signatures={@Signature(name="getNetworkStat", args={})})
    protected NetStat postNetStatMethodsOnCatalogDatabase(Method method, NetStat netStat) {
        this.postConnectionMethodsCleanup();
        return netStat;
    }

    @Post
    @Methods(signatures={@Signature(name="descriptorCacheKeys", args={})})
    protected <T> Enumeration<T> postEnumerationOnCatalogDatabase(Method method, Enumeration<T> enumeration) {
        this.postConnectionMethodsCleanup();
        return enumeration;
    }

    @Post
    @Methods(signatures={@Signature(name="classForNameAndSchema", args={String.class, String.class}), @Signature(name="getClassForType", args={String.class, Map.class})})
    protected <T> Class<T> postClassOnCatalogDatabase(Method method, Class<T> clazz) {
        this.postConnectionMethodsCleanup();
        return clazz;
    }

    @Post
    @Methods(signatures={@Signature(name="getWrapper", args={})})
    protected OracleConnection postOracleConnectionOnCatalogDatabase(Method method, OracleConnection oracleConnection) {
        this.postConnectionMethodsCleanup();
        return oracleConnection;
    }

    @Post
    @Methods(signatures={@Signature(name="getServerSessionInfo", args={}), @Signature(name="getJavaNetProperties", args={}), @Signature(name="getClientInfoInternal", args={})})
    @Blind(value=PropertiesBlinder.class)
    protected Properties postPropertiesOnCatalogDatabase(Method method, @Blind(value=PropertiesBlinder.class) Properties properties) {
        this.postConnectionMethodsCleanup();
        return properties;
    }

    @Post
    @Methods(signatures={@Signature(name="archive", args={int.class, int.class, String.class}), @Signature(name="setAutoClose", args={boolean.class}), @Signature(name="shutdown", args={OracleConnection.DatabaseShutdownMode.class}), @Signature(name="startup", args={String.class, int.class}), @Signature(name="startup", args={OracleConnection.DatabaseShutdownMode.class}), @Signature(name="startup", args={OracleConnection.DatabaseShutdownMode.class, String.class}), @Signature(name="oracleRollback", args={OracleSavepoint.class}), @Signature(name="oracleReleaseSavepoint", args={OracleSavepoint.class}), @Signature(name="registerTAFCallback", args={OracleOCIFailover.class, Object.class}), @Signature(name="registerConnectionCacheCallback", args={OracleConnectionCacheCallback.class, Object.class, int.class}), @Signature(name="detachServerConnection", args={String.class}), @Signature(name="releaseSavepoint", args={Savepoint.class}), @Signature(name="rollback", args={Savepoint.class}), @Signature(name="clearDrcpTagName", args={}), @Signature(name="unregisterAQNotification", args={AQNotificationRegistration.class}), @Signature(name="enqueue", args={String.class, AQEnqueueOptions.class, AQMessage.class}), @Signature(name="unregisterDatabaseChangeNotification", args={DatabaseChangeRegistration.class}), @Signature(name="unregisterDatabaseChangeNotification", args={int.class, String.class, int.class}), @Signature(name="unregisterDatabaseChangeNotification", args={int.class}), @Signature(name="unregisterDatabaseChangeNotification", args={long.class, String.class}), @Signature(name="addLogicalTransactionIdEventListener", args={LogicalTransactionIdEventListener.class}), @Signature(name="addLogicalTransactionIdEventListener", args={LogicalTransactionIdEventListener.class, Executor.class}), @Signature(name="removeLogicalTransactionIdEventListener", args={LogicalTransactionIdEventListener.class}), @Signature(name="jmsEnqueue", args={String.class, JMSEnqueueOptions.class, JMSMessage.class, AQMessageProperties.class}), @Signature(name="unregisterJMSNotification", args={JMSNotificationRegistration.class}), @Signature(name="startJMSNotification", args={JMSNotificationRegistration.class}), @Signature(name="stopJMSNotification", args={JMSNotificationRegistration.class}), @Signature(name="ackJMSNotification", args={JMSNotificationRegistration.class, byte[].class, JMSNotificationRegistration.Directive.class}), @Signature(name="ackJMSNotification", args={ArrayList.class, byte[][].class, JMSNotificationRegistration.Directive.class}), @Signature(name="abort", args={}), @Signature(name="abort", args={Executor.class}), @Signature(name="setPDBChangeEventListener", args={PDBChangeEventListener.class}), @Signature(name="setPDBChangeEventListener", args={PDBChangeEventListener.class, Executor.class}), @Signature(name="setFDO", args={byte[].class}), @Signature(name="putDescriptor", args={byte[].class, Object.class}), @Signature(name="putDescriptor", args={String.class, Object.class}), @Signature(name="removeDescriptor", args={String.class}), @Signature(name="removeAllDescriptor", args={}), @Signature(name="registerSQLType", args={String.class, Class.class}), @Signature(name="registerSQLType", args={String.class, String.class}), @Signature(name="setWrapper", args={OracleConnection.class}), @Signature(name="setPlsqlWarnings", args={String.class}), @Signature(name="setApplicationContext", args={String.class, String.class, String.class}), @Signature(name="clearAllApplicationContext", args={String.class}), @Signature(name="clearWarnings", args={}), @Signature(name="setClientInfo", args={Properties.class}), @Signature(name="setClientInfo", args={String.class, String.class}), @Signature(name="setNetworkTimeout", args={Executor.class, int.class}), @Signature(name="setReadOnly", args={boolean.class}), @Signature(name="setJavaObjectTypeMap", args={Map.class}), @Signature(name="getPropertyForPooledConnection", args={OraclePooledConnection.class}), @Signature(name="setTypeMap", args={Map.class}), @Signature(name="setSafelyClosed", args={boolean.class})})
    protected void postVoidMethodsOnCatalogDatabase(Method method) {
        this.postConnectionMethodsCleanup();
    }

    @Post
    @Methods(signatures={@Signature(name="toDatum", args={CustomDatum.class})})
    protected Datum postDatumMethodsOnCatalogDatabase(Method method, Datum datum) {
        this.postLobConnectionMethodsCleanup();
        return datum;
    }

    @Post
    @Methods(signatures={@Signature(name="createBlob", args={})})
    protected Blob postBlobMethodsOnCatalogDatabase(Method method, Blob blob) {
        try {
            blob = (Blob)this.createLobProxy(blob);
            this.postLobConnectionMethodsCleanup();
        }
        catch (SQLException sQLException) {
            this.releaseConnectionLock();
            throw new RuntimeException(sQLException);
        }
        return blob;
    }

    @Post
    @Methods(signatures={@Signature(name="createClob", args={})})
    protected Clob postClobMethodsOnCatalogDatabase(Method method, Clob clob) {
        try {
            clob = (Clob)this.createLobProxy(clob);
            this.postLobConnectionMethodsCleanup();
        }
        catch (SQLException sQLException) {
            this.releaseConnectionLock();
            throw new RuntimeException(sQLException);
        }
        return clob;
    }

    @Post
    @Methods(signatures={@Signature(name="createClobDBAccess", args={})})
    protected ClobDBAccess postClobDBAccessMethodsOnCatalogDatabase(Method method, ClobDBAccess clobDBAccess) {
        this.postLobConnectionMethodsCleanup();
        return clobDBAccess;
    }

    @Post
    @Methods(signatures={@Signature(name="createBlobDBAccess", args={})})
    protected BlobDBAccess postBlobDBAccessMethodsOnCatalogDatabase(Method method, BlobDBAccess blobDBAccess) {
        this.postLobConnectionMethodsCleanup();
        return blobDBAccess;
    }

    @Post
    @Methods(signatures={@Signature(name="createBfileDBAccess", args={})})
    protected BfileDBAccess postBfileDBAccessMethodsOnCatalogDatabase(Method method, BfileDBAccess bfileDBAccess) {
        this.postLobConnectionMethodsCleanup();
        return bfileDBAccess;
    }

    @Post
    @Methods(signatures={@Signature(name="createNClob", args={})})
    protected NClob postNClobMethodsOnCatalogDatabase(Method method, NClob nClob) {
        try {
            nClob = (NClob)this.createLobProxy(nClob);
            this.postLobConnectionMethodsCleanup();
        }
        catch (SQLException sQLException) {
            this.releaseConnectionLock();
            throw new RuntimeException(sQLException);
        }
        return nClob;
    }

    @Post
    @Methods(signatures={@Signature(name="createArrayOf", args={String.class, Object[].class}), @Signature(name="createOracleArray", args={String.class, Object.class})})
    protected Array postArrayMethodsOnCatalogDatabase(Method method, Array array) {
        try {
            array = (Array)this.createLobProxy(array);
            this.postLobConnectionMethodsCleanup();
        }
        catch (SQLException sQLException) {
            this.releaseConnectionLock();
            throw new RuntimeException(sQLException);
        }
        return array;
    }

    @Post
    @Methods(signatures={@Signature(name="createStruct", args={String.class, Object[].class})})
    protected Struct postArrayMethodsOnCatalogDatabase(Method method, Struct struct) {
        try {
            struct = (Struct)this.createLobProxy(struct);
            this.postLobConnectionMethodsCleanup();
        }
        catch (SQLException sQLException) {
            this.releaseConnectionLock();
            throw new RuntimeException(sQLException);
        }
        return struct;
    }

    @Post
    @Methods(signatures={@Signature(name="createClob", args={byte[].class}), @Signature(name="createClobWithUnpickledBytes", args={byte[].class}), @Signature(name="createClob", args={byte[].class, short.class})})
    protected CLOB postCLOBMethodsOnCatalogDatabase(Method method, CLOB cLOB) {
        cLOB.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
        this.postLobConnectionMethodsCleanup();
        return cLOB;
    }

    @Post
    @Methods(signatures={@Signature(name="createBlob", args={byte[].class}), @Signature(name="createBlobWithUnpickledBytes", args={byte[].class})})
    protected BLOB postBLOBMethodsOnCatalogDatabase(Method method, BLOB bLOB) {
        bLOB.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
        this.postLobConnectionMethodsCleanup();
        return bLOB;
    }

    @Post
    @Methods(signatures={@Signature(name="createBfile", args={byte[].class})})
    protected BFILE postBFILEMethodsOnCatalogDatabase(Method method, BFILE bFILE) {
        bFILE.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
        this.postLobConnectionMethodsCleanup();
        return bFILE;
    }

    @Post
    @Methods(signatures={@Signature(name="createARRAY", args={String.class, Object.class})})
    protected ARRAY postARRAYMethodsOnCatalogDatabase(Method method, ARRAY aRRAY) {
        aRRAY.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
        this.postLobConnectionMethodsCleanup();
        return aRRAY;
    }

    @Post
    @Methods(signatures={@Signature(name="newStructMetaData", args={StructDescriptor.class})})
    protected ResultSetMetaData postDatabaseMetaDataForResultSetMetaData(Method method, ResultSetMetaData resultSetMetaData) {
        this.postConnectionMethodsCleanup();
        return resultSetMetaData;
    }

    @OnError(value=SQLException.class)
    protected Object onError(Method method, SQLException sQLException) throws SQLException {
        this.checkAndReleaseConnectionLock();
        throw sQLException;
    }

    public void setStmtCacheSize(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            this.setStatementCacheSize(n2);
        }
    }

    public void setStmtCacheSize(int n2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            this.setStatementCacheSize(n2);
        }
    }

    public int getStmtCacheSize() {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            int n2 = 0;
            try {
                n2 = this.getStatementCacheSize();
            }
            catch (SQLException sQLException) {
                // empty catch block
            }
            if (n2 == -1) {
                n2 = 0;
            }
            int n3 = n2;
            return n3;
        }
    }

    public void setStatementCacheSize(int n2) throws SQLException {
    }

    public int getStatementCacheSize() throws SQLException {
        return -1;
    }

    public void setImplicitCachingEnabled(boolean bl) throws SQLException {
    }

    public boolean getImplicitCachingEnabled() throws SQLException {
        return false;
    }

    public void setExplicitCachingEnabled(boolean bl) throws SQLException {
    }

    public boolean getExplicitCachingEnabled() throws SQLException {
        return false;
    }

    public void purgeImplicitCache() throws SQLException {
    }

    public void purgeExplicitCache() throws SQLException {
    }

    public boolean isStatementCacheInitialized() {
        return false;
    }

    PreparedStatement getStatementWithKey(String string) throws SQLException {
        return null;
    }

    CallableStatement getCallWithKey(String string) throws SQLException {
        return null;
    }

    public PreparedStatement prepareStatementWithKey(String string) throws SQLException {
        return null;
    }

    public CallableStatement prepareCallWithKey(String string) throws SQLException {
        return null;
    }

    public boolean isProxySession() {
        return false;
    }

    public Connection getLogicalConnection(OraclePooledConnection oraclePooledConnection, boolean bl) throws SQLException {
        LogicalConnection logicalConnection;
        if (this.logicalConnectionAttached != null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 143).fillInStackTrace();
        }
        this.logicalConnectionAttached = logicalConnection = new LogicalConnection(oraclePooledConnection, (oracle.jdbc.internal.OracleConnection)((Object)this), bl);
        return logicalConnection;
    }

    public void closeLogicalConnection() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            if (this.lifecycle == 1 || this.lifecycle == 16 || this.lifecycle == 2) {
                this.releaseAllDatabaseStatements();
                this.logicalConnectionAttached = null;
                this.lifecycle = 1;
            }
        }
    }

    public oracle.jdbc.internal.OracleConnection physicalConnectionWithin() {
        return (oracle.jdbc.internal.OracleConnection)((Object)this);
    }

    public void setAbandonedTimeoutEnabled(boolean bl) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 152).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
        catch (Throwable throwable3) {
            if (closeableLock != null) {
                if (throwable != null) {
                    try {
                        closeableLock.close();
                    }
                    catch (Throwable throwable4) {
                        throwable.addSuppressed(throwable4);
                    }
                } else {
                    closeableLock.close();
                }
            }
            throw throwable3;
        }
    }

    public int getHeartbeatNoChangeCount() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 152).fillInStackTrace();
    }

    public void closeInternal(boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("closeInternal").fillInStackTrace();
    }

    public void cleanupAndClose(boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("cleanupAndClose").fillInStackTrace();
    }

    public void cleanupAndClose() throws SQLException {
        if (this.lifecycle != 1) {
            return;
        }
        this.lifecycle = 16;
        this.cancel();
    }

    @Pre
    @Methods(signatures={@Signature(name="setShardingKey", args={OracleShardingKey.class, OracleShardingKey.class}), @Signature(name="setShardingKey", args={OracleShardingKey.class}), @Signature(name="setShardingKeyIfValid", args={OracleShardingKey.class, OracleShardingKey.class, int.class}), @Signature(name="setShardingKeyIfValid", args={OracleShardingKey.class, int.class}), @Signature(name="setChunkInfo", args={OracleShardingKey.class, OracleShardingKey.class, String.class}), @Signature(name="getOCIHandles", args={}), @Signature(name="getOCIEnvHeapAllocSize", args={}), @Signature(name="doXSNamespaceOp", args={OracleConnection.XSOperationCode.class, byte[].class, XSNamespace[].class, XSNamespace[][].class}), @Signature(name="doXSNamespaceOp", args={OracleConnection.XSOperationCode.class, byte[].class, XSNamespace[].class, XSSecureId.class}), @Signature(name="doXSSessionCreateOp", args={OracleConnection.XSSessionOperationCode.class, XSSecureId.class, byte[].class, XSPrincipal.class, String.class, XSNamespace[].class, OracleConnection.XSSessionModeFlag.class, XSKeyval.class}), @Signature(name="doXSSessionDestroyOp", args={byte[].class, XSSecureId.class, byte[].class}), @Signature(name="doXSSessionAttachOp", args={int.class, byte[].class, XSSecureId.class, byte[].class, XSPrincipal.class, String[].class, String[].class, String[].class, XSNamespace[].class, XSNamespace[].class, XSNamespace[].class, TIMESTAMPTZ.class, TIMESTAMPTZ.class, int.class, long.class, XSKeyval.class, int[].class}), @Signature(name="doXSSessionDetachOp", args={int.class, byte[].class, XSSecureId.class, boolean.class}), @Signature(name="doXSSessionChangeOp", args={OracleConnection.XSSessionSetOperationCode.class, byte[].class, XSSecureId.class, XSSessionParameters.class}), @Signature(name="getTdoCState", args={String.class, String.class}), @Signature(name="getTdoCState", args={String.class}), @Signature(name="addXSEventListener", args={XSEventListener.class}), @Signature(name="addXSEventListener", args={XSEventListener.class, Executor.class}), @Signature(name="removeXSEventListener", args={XSEventListener.class}), @Signature(name="removeAllXSEventListener", args={}), @Signature(name="getConnectionCacheCallbackObj", args={}), @Signature(name="getConnectionCacheCallbackPrivObj", args={}), @Signature(name="getConnectionCacheCallbackFlag", args={}), @Signature(name="getHeapAllocSize", args={}), @Signature(name="prepareDirectPath", args={String.class, String.class, String[].class}), @Signature(name="prepareDirectPath", args={String.class, String.class, String[].class, Properties.class}), @Signature(name="prepareDirectPath", args={String.class, String.class, String[].class, String.class}), @Signature(name="prepareDirectPath", args={String.class, String.class, String[].class, String.class, Properties.class}), @Signature(name="setEndToEndMetrics", args={String[].class, short.class}), @Signature(name="getEndToEndMetrics", args={}), @Signature(name="getEndToEndECIDSequenceNumber", args={}), @Signature(name="needToPurgeStatementCache", args={}), @Signature(name="setACProxy", args={Object.class}), @Signature(name="getACProxy", args={}), @Signature(name="getByteBufferCacheStatistics", args={}), @Signature(name="getCharBufferCacheStatistics", args={}), @Signature(name="executeLightweightSessionPiggyback", args={int.class, byte[].class, KeywordValueLong[].class, int.class}), @Signature(name="setReplayOperations", args={EnumSet.class}), @Signature(name="setReplayingMode", args={boolean.class}), @Signature(name="setReplayContext", args={ReplayContext[].class}), @Signature(name="getReplayContext", args={}), @Signature(name="getLastReplayContext", args={}), @Signature(name="setLastReplayContext", args={ReplayContext.class}), @Signature(name="registerEndReplayCallback", args={OracleConnection.EndReplayCallback.class}), @Signature(name="getEOC", args={}), @Signature(name="getDerivedKeyInternal", args={byte[].class, int.class}), @Signature(name="getExecutingRPCFunctionCode", args={}), @Signature(name="getExecutingRPCSQL", args={}), @Signature(name="sendRequestFlags", args={}), @Signature(name="getAutoCommitInternal", args={}), @Signature(name="openProxySession", args={int.class, Properties.class}), @Signature(name="applyConnectionAttributes", args={Properties.class}), @Signature(name="getConnectionAttributes", args={Properties.class}), @Signature(name="getUnMatchedConnectionAttributes", args={Properties.class}), @Signature(name="getDBAccessProperties", args={}), @Signature(name="getForm", args={OracleTypeADT.class, OracleTypeCLOB.class, int.class}), @Signature(name="setClientIdentifier", args={String.class}), @Signature(name="clearClientIdentifier", args={String.class}), @Signature(name="getStateSignatures", args={}), @Signature(name="hasNoOpenHandles", args={}), @Signature(name="setChecksumMode", args={OracleConnection.ChecksumMode.class}), @Signature(name="addLargeObject", args={OracleLargeObject.class}), @Signature(name="removeLargeObject", args={OracleLargeObject.class}), @Signature(name="addBfile", args={OracleBfile.class}), @Signature(name="removeBfile", args={OracleBfile.class}), @Signature(name="freeTemporaryBlobsAndClobs", args={}), @Signature(name="getResultSetCache", args={}), @Signature(name="refCursorCursorToStatement", args={int.class}), @Signature(name="newArrayDataResultSet", args={Datum[].class, long.class, int.class, Map.class}), @Signature(name="newArrayDataResultSet", args={OracleArray.class, long.class, int.class, Map.class}), @Signature(name="newArrayDataResultSet", args={ArrayDescriptor.class, byte[].class, long.class, int.class, Map.class})})
    protected void preUnsupportedConnectionMethods(Method method, Object object, Object ... objectArray) {
        throw new RuntimeException((SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace());
    }

    public Connection _getPC() {
        return null;
    }

    public boolean isLogicalConnection() {
        return false;
    }

    public Statement createStatement() throws SQLException {
        return this.createStatement(-1, -1);
    }

    public Statement createStatement(int n2, int n3, int n4) throws SQLException {
        return this.createStatement(n2, n3);
    }

    public Statement createStatement(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            Properties properties = new Properties();
            properties.setProperty("result_set_type", Integer.toString(n2));
            properties.setProperty("result_set_concurrency", Integer.toString(n3));
            OracleStatement oracleStatement = this.driverExtension.allocateStatement((oracle.jdbc.internal.OracleConnection)((Object)this), properties);
            this.statements.put((AbstractShardingStatement)((Object)oracleStatement), true);
            OracleStatement oracleStatement2 = oracleStatement;
            return oracleStatement2;
        }
    }

    public PreparedStatement prepareStatement(String string) throws SQLException {
        return this.prepareStatement(string, -1, -1);
    }

    public PreparedStatement prepareStatement(String string, int n2) throws SQLException {
        OraclePreparedStatement oraclePreparedStatement = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            Properties properties = new Properties();
            properties.setProperty("autoGeneratedKeys", Integer.toString(n2));
            oraclePreparedStatement = this.driverExtension.allocatePreparedStatement((oracle.jdbc.internal.OracleConnection)((Object)this), string, properties);
            this.statements.put((AbstractShardingStatement)((Object)oraclePreparedStatement), true);
        }
        return oraclePreparedStatement;
    }

    public PreparedStatement prepareStatement(String string, int[] nArray) throws SQLException {
        OraclePreparedStatement oraclePreparedStatement = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            oraclePreparedStatement = this.driverExtension.allocatePreparedStatement((oracle.jdbc.internal.OracleConnection)((Object)this), string, null);
            ((AbstractShardingPreparedStatement)((Object)oraclePreparedStatement)).setColumnIndexes(nArray);
            this.statements.put((AbstractShardingStatement)((Object)oraclePreparedStatement), true);
        }
        return oraclePreparedStatement;
    }

    public PreparedStatement prepareStatement(String string, String[] stringArray) throws SQLException {
        OraclePreparedStatement oraclePreparedStatement = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            oraclePreparedStatement = this.driverExtension.allocatePreparedStatement((oracle.jdbc.internal.OracleConnection)((Object)this), string, null);
            ((AbstractShardingPreparedStatement)((Object)oraclePreparedStatement)).setColumnNames(stringArray);
            this.statements.put((AbstractShardingStatement)((Object)oraclePreparedStatement), true);
        }
        return oraclePreparedStatement;
    }

    public PreparedStatement prepareStatement(String string, int n2, int n3, int n4) throws SQLException {
        return this.prepareStatement(string, n2, n3);
    }

    public PreparedStatement prepareStatement(String string, int n2, int n3) throws SQLException {
        OraclePreparedStatement oraclePreparedStatement = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            Properties properties = new Properties();
            properties.setProperty("result_set_type", Integer.toString(n2));
            properties.setProperty("result_set_concurrency", Integer.toString(n3));
            oraclePreparedStatement = this.driverExtension.allocatePreparedStatement((oracle.jdbc.internal.OracleConnection)((Object)this), string, properties);
            this.statements.put((AbstractShardingStatement)((Object)oraclePreparedStatement), true);
        }
        return oraclePreparedStatement;
    }

    public PreparedStatement prepareStatement(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        OraclePreparedStatement oraclePreparedStatement = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            oraclePreparedStatement = this.driverExtension.allocatePreparedStatement((oracle.jdbc.internal.OracleConnection)((Object)this), string, properties);
            this.statements.put((AbstractShardingStatement)((Object)oraclePreparedStatement), true);
        }
        return oraclePreparedStatement;
    }

    public CallableStatement prepareCall(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        OracleCallableStatement oracleCallableStatement = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            oracleCallableStatement = this.driverExtension.allocateCallableStatement((oracle.jdbc.internal.OracleConnection)((Object)this), string, properties);
            this.statements.put((AbstractShardingStatement)((Object)oracleCallableStatement), true);
        }
        return oracleCallableStatement;
    }

    public CallableStatement prepareCall(String string) throws SQLException {
        return this.prepareCall(string, -1, -1);
    }

    public CallableStatement prepareCall(String string, int n2, int n3, int n4) throws SQLException {
        return this.prepareCall(string, n2, n3);
    }

    public CallableStatement prepareCall(String string, int n2, int n3) throws SQLException {
        OracleCallableStatement oracleCallableStatement = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            Properties properties = new Properties();
            properties.setProperty("result_set_type", Integer.toString(n2));
            properties.setProperty("result_set_concurrency", Integer.toString(n3));
            oracleCallableStatement = this.driverExtension.allocateCallableStatement((oracle.jdbc.internal.OracleConnection)((Object)this), string, properties);
            this.statements.put((AbstractShardingStatement)((Object)oracleCallableStatement), true);
        }
        return oracleCallableStatement;
    }

    public double getPercentageQueryExecutionOnDirectShard() {
        int n2 = this.totalQueryExecutionOnCatalogDB.get() + this.totalQueryExecutionOnDirectShard.get();
        if (n2 == 0) {
            return 0.0;
        }
        return (double)this.totalQueryExecutionOnDirectShard.get() * 100.0 / ((double)n2 * 1.0);
    }

    public boolean getAutoCommit() throws SQLException {
        return this.autoCommit;
    }

    public void setAutoCommit(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            this.autoCommit = bl;
        }
    }

    public void commit() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            if (this.stickyDatabaseConnection != null) {
                this.stickyDatabaseConnection.commit();
                this.stickyDatabaseConnection.close();
                this.stickyDatabaseConnection = null;
            } else if (this.inTransaction(this.catalogDatabaseConnection)) {
                this.catalogDatabaseConnection.commit();
            }
        }
    }

    public void commit(EnumSet<OracleConnection.CommitOption> enumSet) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            if (this.stickyDatabaseConnection != null) {
                this.stickyDatabaseConnection.commit(enumSet);
                this.stickyDatabaseConnection.close();
                this.stickyDatabaseConnection = null;
            } else if (this.inTransaction(this.catalogDatabaseConnection)) {
                this.catalogDatabaseConnection.commit(enumSet);
            }
        }
    }

    public void rollback() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            if (this.stickyDatabaseConnection != null) {
                this.stickyDatabaseConnection.rollback();
                this.stickyDatabaseConnection.close();
                this.stickyDatabaseConnection = null;
            } else if (this.inTransaction(this.catalogDatabaseConnection)) {
                this.catalogDatabaseConnection.rollback();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void close() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            this.releaseAllDatabaseStatementsAndConnection();
        }
        finally {
            this.lifecycle = 4;
        }
    }

    public void close(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            if ((n2 & 0x1000) != 0) {
                this.close();
                return;
            }
        }
    }

    public void close(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 152).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
        catch (Throwable throwable3) {
            if (closeableLock != null) {
                if (throwable != null) {
                    try {
                        closeableLock.close();
                    }
                    catch (Throwable throwable4) {
                        throwable.addSuppressed(throwable4);
                    }
                } else {
                    closeableLock.close();
                }
            }
            throw throwable3;
        }
    }

    public void beginRequest() throws SQLException {
    }

    public void endRequest() throws SQLException {
        this.releaseAllDatabaseStatementsAndConnection();
    }

    public void endRequest(boolean bl) throws SQLException {
        this.endRequest();
    }

    public void beginNonRequestCalls() throws SQLException {
    }

    public void endNonRequestCalls() throws SQLException {
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return (DatabaseMetaData)ShardingDriverExtension.PROXY_FACTORY.proxyForType(AdditionalDatabaseMetaData.class, this);
    }

    public void cancel() throws SQLException {
        for (AbstractShardingStatement abstractShardingStatement : this.statements.keySet()) {
            try {
                ((OracleStatement)((Object)abstractShardingStatement)).cancel();
            }
            catch (SQLException sQLException) {}
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDefaultExecuteBatch(int n2) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setDefaultExecuteBatch";
            Class[] classArray = new Class[]{Integer.TYPE};
            Object[] objectArray = new Object[]{n2};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Integer n3 = oracleConnection.getDefaultExecuteBatch();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(n3, string, classArray, objectArray);
            this.setterMap.put("DefaultExecuteBatch", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public int getDefaultExecuteBatch() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDefaultRowPrefetch(int n2) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setDefaultRowPrefetch";
            Class[] classArray = new Class[]{Integer.TYPE};
            Object[] objectArray = new Object[]{n2};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Integer n3 = oracleConnection.getDefaultRowPrefetch();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(n3, string, classArray, objectArray);
            this.setterMap.put("DefaultRowPrefetch", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public int getDefaultRowPrefetch() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setIncludeSynonyms(boolean bl) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setIncludeSynonyms";
            Class[] classArray = new Class[]{Boolean.TYPE};
            Object[] objectArray = new Object[]{bl};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Boolean bl2 = oracleConnection.getIncludeSynonyms();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(bl2, string, classArray, objectArray);
            this.setterMap.put("IncludeSynonyms", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public boolean getIncludeSynonyms() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setRestrictGetTables(boolean bl) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setRestrictGetTables";
            Class[] classArray = new Class[]{Boolean.TYPE};
            Object[] objectArray = new Object[]{bl};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Boolean bl2 = oracleConnection.getRestrictGetTables();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(bl2, string, classArray, objectArray);
            this.setterMap.put("RestrictGetTables", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public boolean getRestrictGetTables() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setUsingXAFlag(boolean bl) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setUsingXAFlag";
            Class[] classArray = new Class[]{Boolean.TYPE};
            Object[] objectArray = new Object[]{bl};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Boolean bl2 = oracleConnection.getUsingXAFlag();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(bl2, string, classArray, objectArray);
            this.setterMap.put("UsingXAFlag", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public boolean getUsingXAFlag() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setXAErrorFlag(boolean bl) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setXAErrorFlag";
            Class[] classArray = new Class[]{Boolean.TYPE};
            Object[] objectArray = new Object[]{bl};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Boolean bl2 = oracleConnection.getXAErrorFlag();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(bl2, string, classArray, objectArray);
            this.setterMap.put("XAErrorFlag", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public boolean getXAErrorFlag() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setCreateStatementAsRefCursor(boolean bl) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setCreateStatementAsRefCursor";
            Class[] classArray = new Class[]{Boolean.TYPE};
            Object[] objectArray = new Object[]{bl};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Boolean bl2 = oracleConnection.getCreateStatementAsRefCursor();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(bl2, string, classArray, objectArray);
            this.setterMap.put("CreateStatementAsRefCursor", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public boolean getCreateStatementAsRefCursor() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setSessionTimeZone(String string) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string2 = "setSessionTimeZone";
            Class[] classArray = new Class[]{String.class};
            Object[] objectArray = new Object[]{string};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            String string3 = oracleConnection.getSessionTimeZone();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(string3, string2, classArray, objectArray);
            this.setterMap.put("SessionTimeZone", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public String getSessionTimeZone() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setConnectionReleasePriority(int n2) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setConnectionReleasePriority";
            Class[] classArray = new Class[]{Integer.TYPE};
            Object[] objectArray = new Object[]{n2};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Integer n3 = oracleConnection.getConnectionReleasePriority();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(n3, string, classArray, objectArray);
            this.setterMap.put("ConnectionReleasePriority", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public int getConnectionReleasePriority() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDefaultTimeZone(TimeZone timeZone) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setDefaultTimeZone";
            Class[] classArray = new Class[]{TimeZone.class};
            Object[] objectArray = new Object[]{timeZone};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            TimeZone timeZone2 = oracleConnection.getDefaultTimeZone();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(timeZone2, string, classArray, objectArray);
            this.setterMap.put("DefaultTimeZone", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public TimeZone getDefaultTimeZone() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setHoldability(int n2) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setHoldability";
            Class[] classArray = new Class[]{Integer.TYPE};
            Object[] objectArray = new Object[]{n2};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Integer n3 = oracleConnection.getHoldability();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(n3, string, classArray, objectArray);
            this.setterMap.put("Holdability", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public int getHoldability() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setRemarksReporting(boolean bl) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setRemarksReporting";
            Class[] classArray = new Class[]{Boolean.TYPE};
            Object[] objectArray = new Object[]{bl};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Boolean bl2 = oracleConnection.getRemarksReporting();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(bl2, string, classArray, objectArray);
            this.setterMap.put("RemarksReporting", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public boolean getRemarksReporting() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setCatalog(String string) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string2 = "setCatalog";
            Class[] classArray = new Class[]{String.class};
            Object[] objectArray = new Object[]{string};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            String string3 = oracleConnection.getCatalog();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(string3, string2, classArray, objectArray);
            this.setterMap.put("Catalog", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public String getCatalog() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setSchema(String string) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string2 = "setSchema";
            Class[] classArray = new Class[]{String.class};
            Object[] objectArray = new Object[]{string};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            String string3 = oracleConnection.getSchema();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(string3, string2, classArray, objectArray);
            this.setterMap.put("Schema", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public String getSchema() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setDefaultFixedString(boolean bl) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setDefaultFixedString";
            Class[] classArray = new Class[]{Boolean.TYPE};
            Object[] objectArray = new Object[]{bl};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Boolean bl2 = oracleConnection.getDefaultFixedString();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(bl2, string, classArray, objectArray);
            this.setterMap.put("DefaultFixedString", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public boolean getDefaultFixedString() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setStartTime(long l2) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setStartTime";
            Class[] classArray = new Class[]{Long.TYPE};
            Object[] objectArray = new Object[]{l2};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            Long l3 = oracleConnection.getStartTime();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(l3, string, classArray, objectArray);
            this.setterMap.put("StartTime", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public long getStartTime() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setHAManager(HAManager hAManager) throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            String string = "setHAManager";
            Class[] classArray = new Class[]{HAManager.class};
            Object[] objectArray = new Object[]{hAManager};
            oracleConnection = (oracle.jdbc.internal.OracleConnection)this.getCatalogDatabaseConnection();
            HAManager hAManager2 = oracleConnection.getHAManager();
            AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry = new AbstractShardingStatement.SetterCallHistoryEntry(hAManager2, string, classArray, objectArray);
            this.setterMap.put("HAManager", setterCallHistoryEntry);
        }
        catch (Throwable throwable) {
            this.closeDatabaseConnection(oracleConnection);
            throw throwable;
        }
        this.closeDatabaseConnection(oracleConnection);
    }

    /*
     * Exception decompiling
     */
    public HAManager getHAManager() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public OracleConnection unwrap() {
        return null;
    }

    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface() && clazz.isInstance(this)) {
            return (T)this;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    public boolean isValid(int n2) throws SQLException {
        return this.isValid(null, n2);
    }

    public boolean isValid(OracleConnection.ConnectionValidation connectionValidation, int n2) throws SQLException {
        return this.lifecycle == 1;
    }

    public boolean isUsable() {
        return this.lifecycle == 1;
    }

    public boolean isUsable(boolean bl) {
        return this.isUsable();
    }

    public void setUsable(boolean bl) {
    }

    public boolean isClosed() throws SQLException {
        return this.lifecycle != 1;
    }

    public String getURL() {
        return this.applicationURL;
    }

    public oracle.jdbc.internal.OracleConnection getPhysicalConnection() {
        return (oracle.jdbc.internal.OracleConnection)((Object)this);
    }

    public String getProtocolType() {
        return "sharding";
    }

    public boolean isLifecycleOpen() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            boolean bl = this.lifecycle == 1;
            return bl;
        }
    }

    oracle.jdbc.internal.OracleConnection getConnectionDuringExceptionHandling() {
        return (oracle.jdbc.internal.OracleConnection)((Object)this);
    }

    void postConnectionMethodsCleanup() {
        try {
            this.closeDatabaseConnection(this.getDelegate());
            this.setDelegate(null);
        }
        catch (SQLException sQLException) {
            throw new RuntimeException(sQLException);
        }
        finally {
            this.releaseConnectionLock();
        }
    }

    void postLobConnectionMethodsCleanup() {
        try {
            this.setDelegate(null);
        }
        finally {
            this.releaseConnectionLock();
        }
    }

    Connection getCatalogDatabaseConnection() throws SQLException {
        Connection connection;
        if (this.catalogDatabaseConnection == null) {
            this.pdsCatalogDatabase.setConnectionProperties(this.applicationProps);
            String string = null;
            String string2 = null;
            if (this.applicationProps != null) {
                string = this.applicationProps.getProperty("user");
                string2 = this.applicationProps.getProperty("password");
            }
            UCPConnectionBuilder uCPConnectionBuilder = this.pdsCatalogDatabase.createConnectionBuilder();
            if (string != null) {
                uCPConnectionBuilder.user(string);
            }
            if (string2 != null) {
                uCPConnectionBuilder.password(string2);
            }
            connection = uCPConnectionBuilder.build();
            if (!this.autoCommit) {
                this.catalogDatabaseConnection = connection;
            }
        } else {
            connection = this.catalogDatabaseConnection;
        }
        connection.setAutoCommit(this.autoCommit);
        this.totalQueryExecutionOnCatalogDB.incrementAndGet();
        return connection;
    }

    Connection getCatalogDatabaseConnectionWithSetterReplay() throws SQLException {
        Connection connection = this.getCatalogDatabaseConnection();
        if (connection != null) {
            this.delegatesSetterCalls((oracle.jdbc.internal.OracleConnection)connection, true);
        }
        return connection;
    }

    Connection getShardConnection(OracleShardingKey oracleShardingKey, OracleShardingKey oracleShardingKey2) throws SQLException {
        Connection connection = this.pdsDirectShardDatabase.createConnectionBuilder().shardingKey(oracleShardingKey).superShardingKey(oracleShardingKey2).build();
        connection.setAutoCommit(this.autoCommit);
        this.totalQueryExecutionOnDirectShard.incrementAndGet();
        return connection;
    }

    Connection getShardConnectionWithSetterReplay(OracleShardingKey oracleShardingKey, OracleShardingKey oracleShardingKey2) throws SQLException {
        Connection connection = this.getShardConnection(oracleShardingKey, oracleShardingKey2);
        if (connection != null) {
            this.delegatesSetterCalls((oracle.jdbc.internal.OracleConnection)connection, true);
        }
        return connection;
    }

    boolean inTransaction() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireConnectionCloseableLock();){
            boolean bl = this.inTransaction(this.catalogDatabaseConnection);
            if (!bl) {
                oracle.jdbc.internal.OracleConnection oracleConnection;
                AbstractShardingStatement abstractShardingStatement;
                Statement statement;
                Iterator<AbstractShardingStatement> iterator = this.statements.keySet().iterator();
                while (iterator.hasNext() && ((statement = (abstractShardingStatement = iterator.next()).getDelegate()) == null || !(bl = this.inTransaction(oracleConnection = (oracle.jdbc.internal.OracleConnection)statement.getConnection())))) {
                }
            }
            boolean bl2 = bl;
            return bl2;
        }
    }

    boolean inTransaction(oracle.jdbc.internal.OracleConnection oracleConnection) throws SQLException {
        boolean bl = false;
        if (oracleConnection != null) {
            bl = oracleConnection.getTransactionState().contains((Object)OracleConnection.TransactionState.TRANSACTION_STARTED);
        }
        return bl;
    }

    void removeStatement(AbstractShardingStatement abstractShardingStatement) {
        this.statements.remove(abstractShardingStatement);
    }

    short getDbCharSet() {
        return this.dbCharSet;
    }

    void closeDatabaseConnection(oracle.jdbc.internal.OracleConnection oracleConnection) throws SQLException {
        if (this.getAutoCommit() && oracleConnection != null) {
            oracleConnection.close();
        }
    }

    void closeDatabaseConnectionWithSetterReplay(oracle.jdbc.internal.OracleConnection oracleConnection, boolean bl) throws SQLException {
        if (oracleConnection == this.stickyDatabaseConnection) {
            return;
        }
        if ((this.getAutoCommit() || bl) && oracleConnection != null) {
            this.delegatesSetterCalls(oracleConnection, false);
            oracleConnection.close();
        }
    }

    Monitor.CloseableLock acquireConnectionCloseableLock() {
        this.acquireConnectionLock();
        return this.connectionClosableLock;
    }

    void acquireConnectionLock() {
        this.connectionLock.lock();
    }

    void releaseConnectionLock() {
        this.connectionLock.unlock();
    }

    void checkAndReleaseConnectionLock() {
        if (this.connectionLock.isHeldByCurrentThread()) {
            this.connectionLock.unlock();
        }
    }

    void delegatesSetterCalls(oracle.jdbc.internal.OracleConnection oracleConnection, boolean bl) throws SQLException {
        try {
            for (AbstractShardingStatement.SetterCallHistoryEntry setterCallHistoryEntry : this.setterMap.values()) {
                AbstractShardingStatement.CallHistoryEntry callHistoryEntry = setterCallHistoryEntry.callHistoryEnrty;
                Method method = oracleConnection.getClass().getMethod(callHistoryEntry.methodName, callHistoryEntry.argsType);
                if (bl) {
                    method.invoke(oracleConnection, callHistoryEntry.args);
                    continue;
                }
                method.invoke(oracleConnection, setterCallHistoryEntry.originalValue);
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException exception) {
            throw new SQLException(exception);
        }
    }

    void parseShardingConnectionProperties(String string, @Blind(value=PropertiesBlinder.class) Properties properties) {
        String string2 = null;
        string2 = null;
        if (properties != null) {
            string2 = properties.getProperty("oracle.jdbc.shardingReplayEnable");
        }
        if (string2 == null) {
            string2 = AbstractShardingConnection.getSystemProperty("oracle.jdbc.shardingReplayEnable", null);
        }
        if (string2 == null) {
            string2 = "false";
        }
        this.shardingReplayEnable = string2 != null && string2.equalsIgnoreCase("true");
    }

    private static String getSystemProperty(String string, String string2) {
        if (string != null) {
            final String string3 = string;
            final String string4 = string2;
            final String[] stringArray = new String[]{string2};
            AccessController.doPrivileged(new PrivilegedAction<Object>(){

                @Override
                public Object run() {
                    stringArray[0] = System.getProperty(string3, string4);
                    return null;
                }
            });
            return stringArray[0];
        }
        return string2;
    }

    void releaseAllDatabaseStatementsAndConnection() throws SQLException {
        this.releaseAllDatabaseStatements();
        if (this.catalogDatabaseConnection != null) {
            this.catalogDatabaseConnection.close();
            this.catalogDatabaseConnection = null;
        }
        if (this.stickyDatabaseConnection != null) {
            this.stickyDatabaseConnection.close();
            this.stickyDatabaseConnection = null;
        }
    }

    void releaseAllDatabaseStatements() throws SQLException {
        for (AbstractShardingStatement abstractShardingStatement : this.statements.keySet()) {
            abstractShardingStatement.closeDatabaseStatement();
        }
        this.statements.clear();
    }

    protected Object createLobProxy(Object object) throws SQLException {
        return ShardingDriverExtension.PROXY_FACTORY.proxyFor(object, this);
    }

    String getGsmServiceNameInUrl(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        String string2 = AddrResolution.getServiceName(string);
        if (string2 == null) {
            String string3;
            String string4 = properties.getProperty("oracle.net.tns_admin");
            if (string4 == null && (string4 = T4CConnection.getSystemPropertyTnsAdmin(null)) == null) {
                string4 = T4CConnection.getTnsAdminFromEnv();
            }
            if ((string3 = AddrResolution.resolveTNSAlias(string, properties, string4)) != null) {
                string2 = AddrResolution.getServiceName(string3);
                this.resolvedApplicationURL = string3;
            }
            if (string2 == null) {
                throw (SQLException)DatabaseError.createSqlException(67).fillInStackTrace();
            }
        }
        return string2;
    }

    String gsmServiceName() {
        return this.gsmServiceName;
    }

    String userName() {
        return this.userName;
    }

    String schemaName() {
        return this.schemaName;
    }

    boolean allowSingleShardTransaction() {
        return this.allowSingleShardTransaction;
    }

    oracle.jdbc.internal.OracleConnection getStickyDatabaseConnection() {
        return this.stickyDatabaseConnection;
    }

    void makeDatabaseConnectionSticky(oracle.jdbc.internal.OracleConnection oracleConnection) throws SQLException {
        if (this.stickyDatabaseConnection == null && !this.autoCommit && this.allowSingleShardTransaction() && this.inTransaction(oracleConnection)) {
            if (this.catalogDatabaseConnection == oracleConnection) {
                this.catalogDatabaseConnection = null;
            }
            this.stickyDatabaseConnection = oracleConnection;
        }
    }
}

