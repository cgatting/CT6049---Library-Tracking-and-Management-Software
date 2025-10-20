/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.security.pki.OracleSecretStore
 *  oracle.security.pki.OracleWallet
 *  oracle.xdb.XMLType
 */
package oracle.jdbc.driver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.security.spec.InvalidKeySpecException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.ClientInfoStatus;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLPermission;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.transaction.xa.XAResource;
import oracle.jdbc.LogicalTransactionId;
import oracle.jdbc.LogicalTransactionIdEventListener;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleOCIFailover;
import oracle.jdbc.OracleSQLPermission;
import oracle.jdbc.OracleSavepoint;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.aq.AQDequeueOptions;
import oracle.jdbc.aq.AQEnqueueOptions;
import oracle.jdbc.aq.AQMessage;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.aq.AQNotificationRegistration;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.diagnostics.SecuredLogger;
import oracle.jdbc.driver.AQMessageI;
import oracle.jdbc.driver.AQMessagePropertiesI;
import oracle.jdbc.driver.ArrayDataResultSet;
import oracle.jdbc.driver.ArrayLocatorResultSet;
import oracle.jdbc.driver.AutoKeyInfo;
import oracle.jdbc.driver.BlockSource;
import oracle.jdbc.driver.BufferCache;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DMSFactory;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.GeneratedPhysicalConnection;
import oracle.jdbc.driver.HAManager;
import oracle.jdbc.driver.JMSMessagePropertiesI;
import oracle.jdbc.driver.LRUStatementCache;
import oracle.jdbc.driver.LogicalConnection;
import oracle.jdbc.driver.NTFAQRegistration;
import oracle.jdbc.driver.NTFDCNRegistration;
import oracle.jdbc.driver.NTFEventListener;
import oracle.jdbc.driver.NTFJMSRegistration;
import oracle.jdbc.driver.NTFManager;
import oracle.jdbc.driver.NTFPDBChangeEvent;
import oracle.jdbc.driver.NoSupportHAManager;
import oracle.jdbc.driver.OracleCallableStatement;
import oracle.jdbc.driver.OracleCallableStatementWrapper;
import oracle.jdbc.driver.OracleCloseCallback;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleDatabaseMetaData;
import oracle.jdbc.driver.OracleDriver;
import oracle.jdbc.driver.OracleDriverExtension;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OraclePreparedStatementWrapper;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OracleSql;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.OracleStatementWrapper;
import oracle.jdbc.driver.OracleTimeout;
import oracle.jdbc.driver.PropertiesFileUtil;
import oracle.jdbc.driver.ReplayContext;
import oracle.jdbc.driver.ResultSetCache;
import oracle.jdbc.driver.StructMetaData;
import oracle.jdbc.internal.AbstractConnectionBuilder;
import oracle.jdbc.internal.CompletionStageUtil;
import oracle.jdbc.internal.DatabaseSessionState;
import oracle.jdbc.internal.JMSDequeueOptions;
import oracle.jdbc.internal.JMSEnqueueOptions;
import oracle.jdbc.internal.JMSFactory;
import oracle.jdbc.internal.JMSMessage;
import oracle.jdbc.internal.JMSMessageProperties;
import oracle.jdbc.internal.JMSNotificationRegistration;
import oracle.jdbc.internal.KeywordValueLong;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.NetStat;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.internal.OracleArray;
import oracle.jdbc.internal.OracleBfile;
import oracle.jdbc.internal.OracleClob;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleLargeObject;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.internal.PDBChangeEventListener;
import oracle.jdbc.internal.XSEventListener;
import oracle.jdbc.internal.XSKeyval;
import oracle.jdbc.internal.XSNamespace;
import oracle.jdbc.internal.XSPrincipal;
import oracle.jdbc.internal.XSSecureId;
import oracle.jdbc.internal.XSSessionParameters;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleTypeADT;
import oracle.jdbc.oracore.OracleTypeCLOB;
import oracle.jdbc.oracore.Util;
import oracle.jdbc.pool.OraclePooledConnection;
import oracle.net.jdbc.nl.NLException;
import oracle.net.jdbc.nl.NVFactory;
import oracle.net.jdbc.nl.NVNavigator;
import oracle.net.jdbc.nl.NVPair;
import oracle.net.ns.NetException;
import oracle.net.nt.CustomSSLSocketFactory;
import oracle.net.resolver.TNSNamesNamingAdapter;
import oracle.security.pki.OracleSecretStore;
import oracle.security.pki.OracleWallet;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.BFILE;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.BLOB;
import oracle.sql.BfileDBAccess;
import oracle.sql.BlobDBAccess;
import oracle.sql.CLOB;
import oracle.sql.ClobDBAccess;
import oracle.sql.CustomDatum;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NCLOB;
import oracle.sql.NUMBER;
import oracle.sql.SQLName;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TIMEZONETAB;
import oracle.sql.TypeDescriptor;
import oracle.sql.json.OracleJsonFactory;
import oracle.xdb.XMLType;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class PhysicalConnection
extends GeneratedPhysicalConnection {
    public static final String SECRET_STORE_CONNECT = "oracle.security.client.connect_string";
    public static final String SECRET_STORE_USERNAME = "oracle.security.client.username";
    public static final String SECRET_STORE_PASSWORD = "oracle.security.client.password";
    public static final String SECRET_STORE_DEFAULT_USERNAME = "oracle.security.client.default_username";
    public static final String SECRET_STORE_DEFAULT_PASSWORD = "oracle.security.client.default_password";
    static final CRC64 CHECKSUM = new CRC64();
    public static final char slash_character = '/';
    public static final char at_sign_character = '@';
    public static final char left_square_bracket_character = '[';
    public static final char right_square_bracket_character = ']';
    public static final char left_round_bracket_character = '(';
    public static final char right_round_bracket_character = ')';
    static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    static final char[] EMPTY_CHAR_ARRAY = new char[0];
    static final int STREAM_CHUNK_SIZE = 32768;
    private static final boolean DEBUG = false;
    private static final String DEFAULT_CONNECTION_VALIDATION_QUERY = "SELECT 'x' FROM DUAL";
    private static final Predicate<String> IS_VALID_PROXY_USERNAME = Pattern.compile("\\[[a-zA-Z][a-zA-Z0-9\\-_\\.]+\\]", 2).asPredicate();
    static final int DEFAULT_JSON_PREFETCH_SIZE = 0x2000000;
    long outScn = 0L;
    char[][] charOutput = new char[1][];
    byte[][] byteOutput = new byte[1][];
    short[][] shortOutput = new short[1][];
    byte[] methodTempLittleByteBuffer = new byte[128];
    byte[] methodTempLargeByteBuffer = new byte[4096];
    byte[] tmpByteBuf = null;
    char[] tmpCharBuf = null;
    static final int[] TempCharLengths = new int[]{4000, 32768};
    static final int[] TempByteLengths = new int[]{8000, 98304};
    NTFEventListener pdbChangeListener = null;
    Properties sessionProperties = null;
    private static final String[] END_TO_END_CLIENTINFO_KEYS = new String[]{"OCSID.ACTION", "OCSID.CLIENTID", "OCSID.ECID", "OCSID.MODULE", "OCSID.DBOP", "OCSID.CLIENT_INFO"};
    private static final String END_TO_END_CLIENTINFO_KEY_SEQ_NO = "OCSID.SEQUENCE_NUMBER";
    public OracleConnection.ChecksumMode checksumMode;
    boolean isPDBChanged;
    String url;
    String savedUser;
    String currentSchema = null;
    static NTFManager ntfManager = new NTFManager();
    public int protocolId = -3;
    OracleTimeout timeout;
    DBConversion conversion;
    boolean xaWantsError;
    boolean usingXA;
    int txnMode = 0;
    byte[] fdo;
    Boolean bigEndian;
    OracleStatement statements;
    int lifecycle;
    static final int OPEN = 1;
    static final int CLOSING = 2;
    static final int CLOSED = 4;
    static final int ABORTED = 8;
    static final int BLOCKED = 16;
    boolean cancelInProgressFlag = false;
    boolean clientIdSet = false;
    String clientId = null;
    int txnLevel = 2;
    Map<String, Class<?>> sqlTypeToJavaClassMap = null;
    Map<String, String> javaClassNameToSqlTypeMap = null;
    Map<String, Class<?>> javaObjectMap = new Hashtable(10);
    final Hashtable<String, Object>[] descriptorCacheStack = new Hashtable[2];
    int descriptorCacheTop = 0;
    OracleStatement statementHoldingLine;
    oracle.jdbc.OracleDatabaseMetaData databaseMetaData = null;
    LogicalConnection logicalConnectionAttached = null;
    boolean isProxy = false;
    OracleSql sqlObj = null;
    SQLWarning sqlWarning = null;
    boolean readOnly = false;
    LRUStatementCache statementCache = null;
    boolean clearStatementMetaData = false;
    OracleCloseCallback closeCallback = null;
    Object privateData = null;
    boolean isUsable = true;
    TimeZone defaultTimeZone = null;
    static final String DMS_ROOT_NAME = "JDBC";
    static final String DMS_DEFAULT_PARENT_NAME = "Driver";
    static final String DMS_DEFAULT_PARENT_TYPE = "JDBC_Driver";
    static final String DMS_CONNECTION_PREFIX = "CONNECTION_";
    static final String DMS_CONNECTION_TYPE = "JDBC_Connection";
    public static final String DMS_CONNECTION_URL = "JDBC_Connection_Url";
    public static final String DMS_CONNECTION_USER_NAME = "JDBC_Connection_Username";
    static final String DMS_OPEN_COUNT_NAME = "ConnectionOpenCount";
    static final String DMS_OPEN_COUNT_DESCRIPTION = "number of connections that have been opened";
    static final String DMS_CLOSE_COUNT_NAME = "ConnectionCloseCount";
    static final String DMS_CLOSE_COUNT_DESCRIPTION = "number of connections that have been closed";
    static final String DMS_GETCONNECTION_NAME = "ConnectionCreate";
    static final String DMS_GETCONNECTION_DESCRIPTION = "time spent creating a connection";
    static final String DMS_LOGICAL_CONNECTION_NAME = "LogicalConnection";
    static final String DMS_LOGICAL_CONNECTION_DESCRIPTION = "logical connection holding this physical connection";
    static final String DMS_EMPTY_UNITS = "";
    static final String DMS_LOGICAL_CONNECTION_UNITS = "";
    static final String DMS_NEW_STATEMENT_NAME = "CreateNewStatement";
    static final String DMS_NEW_STATEMENT_DESCRIPTION = "time spent creating a new statement";
    static final String DMS_GET_STATEMENT_NAME = "CreateStatement";
    static final String DMS_GET_STATEMENT_DESCRIPTION = "time spent retrieving a statement from cache or creating it anew";
    static final String DMS_CONNECTION_URL_DESCRIPTION = "url specified for the connection";
    static final String DMS_CONNECTION_USER_DESCRIPTION = "user name used for the connection";
    static final String DMS_STATEMENT_PARENT_NAME = "Statement";
    static final String DMS_STATEMENT_PARENT_TYPE = "JDBC_Statement";
    static final String DMS_SQLTEXT_NAME = "SQLText";
    static final String DMS_SQLTEXT_DESCRIPTION = "current SQL text";
    static final String DMS_SQLTEXT_UNITS = "";
    static final String DMS_EXECUTE_NAME = "Execute";
    static final String DMS_EXECUTE_DESCRIPTION = "the time required for all executions of this statement";
    static final String DMS_FETCH_NAME = "Fetch";
    static final String DMS_FETCH_DESCRIPTION = "the time required for all fetches by this statement";
    static AtomicLong DMS_CONNECTION_COUNT = new AtomicLong(0L);
    DMSFactory.DMSNoun dmsParent = null;
    DMSFactory.DMSEvent dmsOpenCount = null;
    DMSFactory.DMSEvent dmsCloseCount = null;
    DMSFactory.DMSPhase dmsGetConnection = null;
    DMSFactory.DMSState dmsLogicalConnection = null;
    DMSFactory.DMSPhase dmsCreateNewStatement = null;
    DMSFactory.DMSPhase dmsCreateStatement = null;
    DMSFactory.DMSNoun commonDmsParent = null;
    DMSFactory.DMSState commonDmsSqlText = null;
    DMSFactory.DMSPhase commonDmsExecute = null;
    DMSFactory.DMSPhase commonDmsFetch = null;
    DMSFactory.DMSState dmsUrl;
    DMSFactory.DMSState dmsUser;
    static final int END_TO_END_DBOP_INDEX = 4;
    static final int END_TO_END_CLIENTINFO_INDEX = 5;
    static final int END_TO_END_STATE_INDEX_MAX_POST_1200 = 6;
    final int[] endToEndMaxLength = new int[6];
    boolean endToEndAnyChanged = false;
    final boolean[] endToEndHasChanged = new boolean[6];
    short endToEndECIDSequenceNumber = Short.MIN_VALUE;
    final String[] arrayOfNullStrings = new String[6];
    String[] endToEndValues = this.arrayOfNullStrings;
    final DMSFactory.DMSVersion dmsVersion;
    Map<String, Map<String, String>> currentSystemContext = new IdentityHashMap<String, Map<String, String>>();
    private static final String[] e2eKeys = new String[]{DMSFactory.Context.ECForJDBC.ACTION, DMSFactory.Context.ECForJDBC.CLIENTID, DMSFactory.Context.ECForJDBC.ECID, DMSFactory.Context.ECForJDBC.MODULE};
    oracle.jdbc.OracleConnection wrapper = null;
    int minVcsBindSize;
    int maxRawBytesSql;
    int maxRawBytesPlsql;
    int maxVcsCharsSql;
    int maxVcsNCharsSql;
    int maxVcsBytesPlsql;
    int maxVcsBytesPlsqlOut;
    int maxIbtVarcharElementLength;
    int maxVarcharLength;
    int maxNVarcharLength;
    int maxRawLength;
    String instanceName = null;
    String dbName = null;
    OracleDriverExtension driverExtension;
    static final String uninitializedMarker = "";
    String databaseProductVersion = "";
    short versionNumber = (short)-1;
    int namedTypeAccessorByteLen;
    int refTypeAccessorByteLen;
    protected final Monitor cancelInProgressLockForThin = Monitor.newInstance();
    boolean plsqlCompilerWarnings = false;
    private boolean savedAutoCommitFlag;
    private int savedTxnMode;
    private BlockSource blockSource;
    int thinACLastLtxidHash = 0;
    oracle.jdbc.driver.LogicalTransactionId thinACCurrentLTXID;
    ReplayContext[] thinACReplayContextReceived = new ReplayContext[10];
    int thinACReplayContextReceivedCurrent = 0;
    ReplayContext thinACLastReplayContextReceived = null;
    OracleConnection.DRCPState drcpState = OracleConnection.DRCPState.DETACHED;
    boolean currentlyInTransaction = false;
    boolean drcpEnabled = false;
    HAManager haManager = NoSupportHAManager.getInstance();
    protected boolean safelyClosed = false;
    private String cachedCompatibleString = null;
    private static final String TNS_ADMIN = "TNS_ADMIN";
    private static final Pattern URL_PATTERN = Pattern.compile("(?i)jdbc:(oracle|default):(thin|oci[8]?|kprb|sharding|connection)(?-i)(:(((([\\w\\[\\]$#]*)|(\"[^\u0000\"]+\"))/(([\\w$#\\+%\\^\\&\\*_~=\\;\\:\\<\\>\\[\\]\\(\\)\\!]*)|(\"[^\u0000\"]+\")))?@(.*)?)?)?", 40);
    private static final Pattern USERNAME_PATTERN = Pattern.compile("(?iU)(([\\w$#]+)|(\"[^\u0000\"]+\"))(\\[(([\\w$#]+)|(\"[^\u0000\"]+\"))\\])?((\\s+AS\\s+)(SYSDBA|SYSOPER|SYSASM|SYSBACKUP|SYSDG|SYSKM))?");
    private static final AtomicLong CONNECTIONID_SEQ = new AtomicLong();
    protected SecuredLogger securedLogger;
    private Executor asyncExecutor;
    private static final Pattern driverNameAttributePattern = Pattern.compile("[\\x20-\\x7e]{0,30}");
    private static final OracleSQLPermission CALL_ORACLE_ABORT_PERMISSION = new OracleSQLPermission("callAbort");
    static final String DATABASE_NAME = "DATABASE_NAME";
    static final String SERVER_HOST = "SERVER_HOST";
    static final String INSTANCE_NAME = "INSTANCE_NAME";
    static final String SERVICE_NAME = "SERVICE_NAME";
    Hashtable<Object, Object> clientData;
    private BufferCacheStore connectionBufferCacheStore;
    private static ThreadLocal<BufferCacheStore> threadLocalBufferCacheStore;
    private int pingResult;
    String sessionTimeZone = null;
    String databaseTimeZone = null;
    Calendar dbTzCalendar = null;
    private volatile OracleJsonFactory jsonFactory;
    private static final boolean IS_JSON_JAR_LOADED;
    static final String SETCLIENTINFO_PERMISSION_NAME = "clientInfo.";
    static final List<String> RESERVED_NAMESPACES;
    static final Pattern SUPPORTED_NAME_PATTERN;
    protected final Properties clientInfo = new Properties();
    private short lastEndToEndSequenceNumber = (short)-1;
    static final String RAW_STR = "RAW";
    static final String SYS_RAW_STR = "SYS.RAW";
    static final String SYS_ANYDATA_STR = "SYS.ANYDATA";
    static final String SYS_XMLTYPE_STR = "SYS.XMLTYPE";
    static final String JSON_STR = "JSON";
    int timeZoneVersionNumber = -1;
    TIMEZONETAB timeZoneTab = null;
    private static final SQLPermission CALL_ABORT_PERMISSION;
    private static final SQLPermission CALL_SETNETWORKTIMEOUT_PERMISSION;
    private Executor closeExecutor = null;
    int varTypeMaxLenCompat = 0;
    private static final Pattern nonQuotedIdentifierPattern;
    protected AtomicLong lobCount = new AtomicLong();
    protected AtomicLong bfileCount = new AtomicLong();
    protected List<OracleLargeObject> temporaryLobs = new ArrayList<OracleLargeObject>();
    boolean isServerBigSCN = false;
    private static final Predicate<String> IS_SIMPLE_IDENTIFIER;
    private static final Predicate<String> IS_QUOTED_IDENTIFIER;
    private static final Predicate<String> IS_VALID_IDENTIFIER;
    private static final String _Copyright_2014_Oracle_All_Rights_Reserved_;
    public static final boolean TRACE = false;

    char[] getMethodTempCharBuffer(int n2) {
        int n3 = 0;
        if (this.tmpCharBuf == null || this.tmpCharBuf.length < n2) {
            for (int n4 : TempCharLengths) {
                if (n4 >= n2) {
                    n3 = n4;
                    break;
                }
                n3 = n2;
            }
            this.tmpCharBuf = new char[n3];
            int n5 = n3 * (this.conversion.sMaxCharSize > this.conversion.maxNCharSize ? this.conversion.sMaxCharSize : this.conversion.maxNCharSize);
            this.tmpByteBuf = new byte[n5];
        }
        return this.tmpCharBuf;
    }

    byte[] getMethodTempByteBuffer(int n2) {
        int n3 = 0;
        if (this.tmpByteBuf == null || this.tmpByteBuf.length < n2) {
            for (int n4 : TempByteLengths) {
                if (n4 >= n2) {
                    n3 = n4;
                    break;
                }
                n3 = n2;
            }
            this.tmpByteBuf = new byte[n3];
        }
        return this.tmpByteBuf;
    }

    protected BlockSource setBlockSource() {
        return BlockSource.createBlockSource(this.useThreadLocalBufferCache, BlockSource.Implementation.THREADED);
    }

    protected PhysicalConnection() {
        this(new ReentrantLock());
    }

    private PhysicalConnection(Lock lock) {
        super(Monitor.CloseableLock.wrap(lock));
        this.dmsVersion = DMSFactory.DMSVersion.NONE;
        this.securedLogger = this.createSecuredLogger();
    }

    PhysicalConnection(String string, @Blind(value=PropertiesBlinder.class) Properties properties, OracleDriverExtension oracleDriverExtension) throws SQLException {
        this(string, properties, oracleDriverExtension, new ReentrantLock());
    }

    private PhysicalConnection(String string, @Blind(value=PropertiesBlinder.class) Properties properties, OracleDriverExtension oracleDriverExtension, Lock lock) throws SQLException {
        super(Monitor.CloseableLock.wrap(lock));
        this.securedLogger = this.createSecuredLogger();
        this.readConnectionProperties(string, properties);
        this.createDMSSensors();
        this.dmsUrl.update(this.url);
        this.dmsUser.update(this.userName);
        this.dmsVersion = DMSFactory.getDMSVersion();
        this.driverExtension = oracleDriverExtension;
        this.blockSource = this.setBlockSource();
        this.descriptorCacheStack[this.descriptorCacheTop] = new Hashtable(10);
    }

    private SecuredLogger createSecuredLogger() {
        return null;
    }

    void setDriverSpecificAutoCommit(boolean bl) throws SQLException {
    }

    boolean isShardingDriverMode() {
        return false;
    }

    @Override
    public boolean isConnectionBigTZTC() throws SQLException {
        return false;
    }

    @Override
    public boolean serverSupportsRequestBoundaries() throws SQLException {
        return false;
    }

    @Override
    public boolean serverSupportsExplicitBoundaryBit() throws SQLException {
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void connect(AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) throws SQLException {
        boolean bl = true;
        long l2 = this.dmsGetConnection.start();
        try {
            try {
                this.lifecycle = 1;
                this.needLine();
                this.initializeAsyncExecutor(abstractConnectionBuilder);
                if (this.isDRCPConnection(this.url)) {
                    this.drcpEnabled = true;
                    if (this.drcpConnectionClass != null) {
                        this.drcpConnectionClass = this.drcpConnectionClass.trim();
                    }
                }
                this.logon(abstractConnectionBuilder);
                this.setAutoCommit(this.autocommit);
                this.setDriverSpecificAutoCommit(this.autocommit);
                this.versionDependentInit(this.getVersionNumber());
                if (this.implicitStatementCacheSize > 0) {
                    this.setStatementCacheSize(this.implicitStatementCacheSize);
                    this.setImplicitCachingEnabled(true);
                }
                this.dmsOpenCount.occurred();
                bl = false;
                if (this.fanEnabled) {
                    HAManager.enableHAIfNecessary(this.url, this);
                }
            }
            catch (SQLException sQLException) {
                this.lifecycle = 2;
                try {
                    this.logoff();
                }
                catch (SQLException sQLException2) {
                    // empty catch block
                }
                this.lifecycle = 4;
                throw sQLException;
            }
            this.txnMode = 0;
        }
        finally {
            this.dmsGetConnection.stop(l2);
            if (bl) {
                this.dmsParent.destroy();
                this.dmsParent = null;
            }
        }
    }

    final CompletionStage<Void> connectAsync(AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) {
        long l2 = this.dmsGetConnection.start();
        try {
            this.initializeAsyncExecutor(abstractConnectionBuilder);
            this.lifecycle = 1;
            this.needLine();
            if (this.isDRCPConnection(this.url)) {
                this.drcpEnabled = true;
                if (this.drcpConnectionClass != null) {
                    this.drcpConnectionClass = this.drcpConnectionClass.trim();
                }
            }
        }
        catch (SQLException sQLException2) {
            return CompletionStageUtil.failedStage(sQLException2);
        }
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            CompletionStage<Void> completionStage = this.logonAsync(abstractConnectionBuilder).thenApply(CompletionStageUtil.normalCompletionHandler(void_ -> {
                this.setAutoCommit(this.autocommit);
                this.setDriverSpecificAutoCommit(this.autocommit);
                this.versionDependentInit(this.getVersionNumber());
                if (this.implicitStatementCacheSize > 0) {
                    this.setStatementCacheSize(this.implicitStatementCacheSize);
                    this.setImplicitCachingEnabled(true);
                }
                this.dmsOpenCount.occurred();
                if (this.fanEnabled) {
                    HAManager.enableHAIfNecessary(this.url, this);
                }
                this.txnMode = 0;
                return null;
            })).exceptionally(CompletionStageUtil.exceptionalCompletionHandler(SQLException.class, sQLException -> {
                this.lifecycle = 2;
                try {
                    this.logoff();
                }
                catch (SQLException sQLException2) {
                    // empty catch block
                }
                this.lifecycle = 4;
                throw sQLException;
            })).handle((void_, throwable) -> {
                this.dmsGetConnection.stop(l2);
                if (throwable != null) {
                    this.dmsParent.destroy();
                    this.dmsParent = null;
                    if (throwable instanceof CompletionException) {
                        throw (CompletionException)throwable;
                    }
                    throw new CompletionException((Throwable)throwable);
                }
                return void_;
            });
            CompletionStage<Void> completionStage2 = completionStage.whenCompleteAsync((void_, throwable) -> {}, this.createUserCodeExecutor());
            return completionStage2;
        }
    }

    private final void initializeAsyncExecutor(AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) {
        Executor executor = abstractConnectionBuilder == null ? null : abstractConnectionBuilder.getExecutor();
        this.asyncExecutor = executor == null ? ForkJoinPool.commonPool() : executor;
    }

    protected void versionDependentInit(short s2) {
        if (s2 >= 12100) {
            this.endToEndMaxLength[0] = 64;
            this.endToEndMaxLength[1] = 64;
            this.endToEndMaxLength[2] = 64;
            this.endToEndMaxLength[3] = 64;
            this.endToEndMaxLength[4] = 64;
            this.endToEndMaxLength[5] = 64;
        } else if (s2 >= 11202) {
            this.endToEndMaxLength[0] = 64;
            this.endToEndMaxLength[1] = 64;
            this.endToEndMaxLength[2] = 64;
            this.endToEndMaxLength[3] = 64;
            this.endToEndMaxLength[4] = 64;
            this.endToEndMaxLength[5] = 64;
        } else if (s2 >= 11000) {
            this.endToEndMaxLength[0] = 32;
            this.endToEndMaxLength[1] = 64;
            this.endToEndMaxLength[2] = 64;
            this.endToEndMaxLength[3] = 48;
            this.endToEndMaxLength[4] = 64;
            this.endToEndMaxLength[5] = 64;
        } else if (s2 >= 10000) {
            this.endToEndMaxLength[0] = 32;
            this.endToEndMaxLength[1] = 64;
            this.endToEndMaxLength[2] = 64;
            this.endToEndMaxLength[3] = 48;
            this.endToEndMaxLength[4] = 64;
            this.endToEndMaxLength[5] = 64;
        } else {
            this.endToEndMaxLength[0] = 32;
            this.endToEndMaxLength[1] = 64;
            this.endToEndMaxLength[2] = 64;
            this.endToEndMaxLength[3] = 48;
            this.endToEndMaxLength[4] = 64;
            this.endToEndMaxLength[5] = 64;
        }
        if (s2 >= 12000 & this.varTypeMaxLenCompat == 2) {
            this.minVcsBindSize = 32766;
            this.maxRawBytesSql = 32766;
            this.maxRawBytesPlsql = 32766;
            this.maxVcsCharsSql = 32766;
            this.maxVcsNCharsSql = 32766;
            this.maxVcsBytesPlsql = 32766;
            this.maxVcsBytesPlsqlOut = Short.MAX_VALUE;
            this.maxIbtVarcharElementLength = 32766;
            this.maxVarcharLength = Short.MAX_VALUE;
            this.maxNVarcharLength = 32766;
            this.maxRawLength = Short.MAX_VALUE;
        } else if (s2 >= 11202) {
            this.minVcsBindSize = 4001;
            this.maxRawBytesSql = 4000;
            this.maxRawBytesPlsql = 32766;
            this.maxVcsCharsSql = 32766;
            this.maxVcsNCharsSql = 32766;
            this.maxVcsBytesPlsql = 32766;
            this.maxVcsBytesPlsqlOut = Short.MAX_VALUE;
            this.maxIbtVarcharElementLength = 32766;
            this.maxVarcharLength = 4000;
            this.maxNVarcharLength = 4000;
            this.maxRawLength = 2000;
        } else if (s2 >= 11000) {
            this.minVcsBindSize = 4001;
            this.maxRawBytesSql = 4000;
            this.maxRawBytesPlsql = 32766;
            this.maxVcsCharsSql = 32766;
            this.maxVcsNCharsSql = 32766;
            this.maxVcsBytesPlsql = 32766;
            this.maxVcsBytesPlsqlOut = Short.MAX_VALUE;
            this.maxIbtVarcharElementLength = 32766;
            this.maxVarcharLength = 4000;
            this.maxNVarcharLength = 4000;
            this.maxRawLength = 2000;
        } else if (s2 >= 10000) {
            this.minVcsBindSize = 4001;
            this.maxRawBytesSql = 4000;
            this.maxRawBytesPlsql = 32512;
            this.maxVcsCharsSql = 32766;
            this.maxVcsNCharsSql = 32766;
            this.maxVcsBytesPlsql = 32512;
            this.maxVcsBytesPlsqlOut = 32512;
            this.maxIbtVarcharElementLength = 32766;
            this.maxVarcharLength = 4000;
            this.maxNVarcharLength = 4000;
            this.maxRawLength = 2000;
        }
    }

    int getMaxSizeForVarchar(OracleStatement.SqlKind sqlKind, int n2, boolean bl) throws SQLException {
        int n3 = 0;
        n3 = sqlKind == OracleStatement.SqlKind.PLSQL_BLOCK ? (n2 > 0 ? Math.max(this.maxVcsBytesPlsql, n2) : (bl ? 4000 : this.maxVcsBytesPlsqlOut)) : this.maxVarcharLength;
        return n3;
    }

    private static final String propertyVariableName(String string) {
        if (string.equals("commitOptionProperty")) {
            return "COMMIT_OPTION";
        }
        if (string.equals("calculateChecksumProperty")) {
            return "CALCULATE_CHECKSUM";
        }
        if (string.equals("isResultSetCacheEnabled")) {
            return "ENABLE_QUERY_RESULT_CACHE";
        }
        char[] cArray = new char[string.length()];
        string.getChars(0, string.length(), cArray, 0);
        String string2 = "";
        for (int i2 = 0; i2 < cArray.length; ++i2) {
            if (Character.isUpperCase(cArray[i2])) {
                string2 = string2 + "_";
            }
            string2 = string2 + Character.toUpperCase(cArray[i2]);
        }
        return string2;
    }

    private void initializeUserDefaults(@Blind(value=PropertiesBlinder.class) Properties properties) {
        for (String string : OracleDriver.DEFAULT_CONNECTION_PROPERTIES.stringPropertyNames()) {
            if (properties.containsKey(string)) continue;
            properties.setProperty(string, OracleDriver.DEFAULT_CONNECTION_PROPERTIES.getProperty(string));
        }
    }

    protected void readConnectionProperties(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        String[] stringArray;
        int n2;
        String[] stringArray2;
        int n3;
        Object object;
        boolean bl;
        this.initializeUserDefaults(properties);
        this.url = string;
        Hashtable<String, String> hashtable = PhysicalConnection.parseUrl(this.url);
        String string2 = properties.getProperty("oracle.net.tns_admin");
        if (string2 == null) {
            string2 = PhysicalConnection.getSystemPropertyTnsAdmin(null);
        }
        boolean bl2 = bl = string2 == null;
        if (bl) {
            string2 = PhysicalConnection.getTnsAdminFromEnv();
        }
        Properties properties2 = this.getConnectionPropertiesFromFile(properties, hashtable, string2, bl);
        super.readConnectionProperties(string, properties, properties2);
        if (this.tnsAdmin == null && string2 != null) {
            this.tnsAdmin = string2;
        }
        if (this.commitOptionProperty != null) {
            this.commitOption = 0;
            object = this.commitOptionProperty.split(",");
            if (object != null && ((String[])object).length > 0) {
                String[] stringArray3 = object;
                n3 = stringArray3.length;
                for (int i2 = 0; i2 < n3; ++i2) {
                    stringArray2 = stringArray3[i2];
                    if (stringArray2.trim() == "") continue;
                    this.commitOption |= OracleConnection.CommitOption.valueOf(stringArray2.trim()).getCode();
                }
            }
        }
        this.checksumMode = this.calculateChecksumProperty == null ? OracleConnection.ChecksumMode.NO_CHECKSUM : OracleConnection.ChecksumMode.valueOf(this.calculateChecksumProperty);
        if (this.defaultRowPrefetch <= 0) {
            this.defaultRowPrefetch = Integer.parseInt("10");
        }
        this.defaultExecuteBatch = 1;
        if (this.defaultLobPrefetchSize < -1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 267).fillInStackTrace();
        }
        if (this.thinVsessionOsuser == null) {
            this.thinVsessionOsuser = PhysicalConnection.getSystemPropertyUserName();
            if (this.thinVsessionOsuser == null) {
                this.thinVsessionOsuser = "jdbcuser";
            }
        }
        if (this.thinNetConnectTimeout == "0" && (n2 = DriverManager.getLoginTimeout()) != 0) {
            this.thinNetConnectTimeout = "" + n2 * 1000;
        }
        this.autocommit = this.defaultautocommit;
        if (this.userName == null) {
            this.userName = hashtable.get("user");
        }
        if ((object = properties.getProperty("password")) == null) {
            object = properties.getProperty("oracle.jdbc.password");
        }
        if (object == null && properties2 != null && (object = properties2.getProperty("password")) == null) {
            object = properties2.getProperty("oracle.jdbc.password");
        }
        if (object == null) {
            object = hashtable.get("password");
        }
        if (this.database == CONNECTION_PROPERTY_DATABASE_DEFAULT) {
            this.database = properties.getProperty("server", CONNECTION_PROPERTY_DATABASE_DEFAULT);
        }
        if (this.database == CONNECTION_PROPERTY_DATABASE_DEFAULT) {
            this.database = hashtable.get("database");
        }
        boolean bl3 = this.userName == null || this.userName.length() == 0;
        int n4 = n3 = object == null || ((String)object).length() == 0 ? 1 : 0;
        if (!(!bl3 && n3 == 0 || this.database == null || this.database.length() <= 0 || (stringArray = PhysicalConnection.getSecretStoreCredentials(this.database, this.walletLocation, this.walletPassword))[0] == null && stringArray[1] == null)) {
            if (bl3) {
                this.userName = stringArray[0];
            } else if (n3 != 0 && IS_VALID_PROXY_USERNAME.test(this.userName)) {
                this.userName = stringArray[0] + this.userName;
            }
            if (n3 != 0) {
                object = stringArray[1];
            }
        }
        this.initializePassword(OpaqueString.newOpaqueString((String)object));
        String[] stringArray4 = new String[1];
        stringArray2 = new String[1];
        this.userName = PhysicalConnection.parseLoginOption(this.userName, properties, stringArray4, stringArray2);
        if (stringArray4[0] != null) {
            this.internalLogon = stringArray4[0];
        }
        if (stringArray2[0] != null) {
            this.proxyClientName = stringArray2[0];
        }
        this.protocol = hashtable.get("protocol");
        if (this.protocol == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 40, "Protocol is not specified in URL").fillInStackTrace();
        }
        if (properties.getProperty("is_connection_pooling") == "true" && this.database == null) {
            this.database = "";
        }
        if (this.userName != null && !this.userName.startsWith("\"")) {
            char[] cArray = this.userName.toCharArray();
            for (int i3 = 0; i3 < cArray.length; ++i3) {
                cArray[i3] = Character.toUpperCase(cArray[i3]);
            }
            this.userName = String.copyValueOf(cArray);
        }
        this.xaWantsError = false;
        this.usingXA = false;
        if (this.networkCompression != null) {
            this.networkCompression = this.networkCompression.toLowerCase();
        }
        if (this.drcpTagName != null && this.drcpTagName.isEmpty()) {
            this.drcpTagName = null;
        }
        this.validateConnectionProperties();
    }

    void validateConnectionProperties() throws SQLException {
        if (this.driverNameAttribute != null && !driverNameAttributePattern.matcher(this.driverNameAttribute).matches()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 257).fillInStackTrace();
        }
        if (this.loginTimeout < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 190, "oracle.jdbc.loginTimeout is less than 0: " + this.loginTimeout).fillInStackTrace();
        }
    }

    private static String parseLoginOption(String string, @Blind(value=PropertiesBlinder.class) Properties properties, String[] stringArray, String[] stringArray2) {
        String string2;
        if (string == null || string.length() == 0) {
            return null;
        }
        String string3 = string.trim();
        Matcher matcher = USERNAME_PATTERN.matcher(string3);
        if (matcher.matches()) {
            String string4;
            string2 = matcher.group(1);
            String string5 = matcher.group(5);
            if (string5 != null && string5.length() != 0) {
                stringArray2[0] = string5;
            }
            if ((string4 = matcher.group(10)) != null && string4.length() != 0) {
                stringArray[0] = string4.toLowerCase();
            }
        } else {
            string2 = string3;
        }
        return string2;
    }

    static final Hashtable<String, String> parseUrl(String string) throws SQLException {
        Hashtable<String, String> hashtable = new Hashtable<String, String>(5);
        if (string == null || string.length() == 0) {
            return hashtable;
        }
        Matcher matcher = URL_PATTERN.matcher(string);
        if (matcher.matches()) {
            String string2 = matcher.group(1);
            String string3 = matcher.group(2);
            hashtable.put("protocol", string3);
            String string4 = matcher.group(6);
            String string5 = matcher.group(9);
            String string6 = matcher.group(12);
            if (string4 != null && string4.length() > 0 && string5 != null && string5.length() > 0) {
                hashtable.put("user", string4);
                hashtable.put("password", string5);
            }
            if (string6 != null) {
                hashtable.put("database", string6);
            }
        } else {
            throw (SQLException)DatabaseError.createSqlException(67).fillInStackTrace();
        }
        return hashtable;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static final String[] getSecretStoreCredentials(String string, String string2, OpaqueString opaqueString) throws SQLException {
        String[] stringArray = new String[]{null, null};
        if (string2 == null) return stringArray;
        try {
            OracleSecretStore oracleSecretStore;
            OracleWallet oracleWallet;
            block12: {
                if (string2.startsWith("(")) {
                    string2 = "file:" + CustomSSLSocketFactory.processWalletLocation(string2);
                }
                if (!(oracleWallet = new OracleWallet()).exists(string2)) return stringArray;
                char[] cArray = null;
                if (opaqueString != null && opaqueString != OpaqueString.NULL) {
                    cArray = opaqueString.getChars();
                }
                try {
                    oracleWallet.open(string2, cArray);
                    if (cArray == null) break block12;
                }
                catch (Throwable throwable) {
                    if (cArray == null) throw throwable;
                    int n2 = 0;
                    while (n2 < cArray.length) {
                        cArray[n2] = '\u0000';
                        ++n2;
                    }
                    throw throwable;
                }
                for (int i2 = 0; i2 < cArray.length; ++i2) {
                    cArray[i2] = '\u0000';
                }
            }
            if ((oracleSecretStore = oracleWallet.getSecretStore()).containsAlias(SECRET_STORE_DEFAULT_USERNAME)) {
                stringArray[0] = new String(oracleSecretStore.getSecret(SECRET_STORE_DEFAULT_USERNAME));
            }
            if (oracleSecretStore.containsAlias(SECRET_STORE_DEFAULT_PASSWORD)) {
                stringArray[1] = new String(oracleSecretStore.getSecret(SECRET_STORE_DEFAULT_PASSWORD));
            }
            Enumeration enumeration = oracleWallet.getSecretStore().internalAliases();
            String string3 = null;
            do {
                if (!enumeration.hasMoreElements()) return stringArray;
            } while (!(string3 = (String)enumeration.nextElement()).startsWith(SECRET_STORE_CONNECT) || !string.equalsIgnoreCase(new String(oracleSecretStore.getSecret(string3))));
            String string4 = string3.substring(SECRET_STORE_CONNECT.length());
            stringArray[0] = new String(oracleSecretStore.getSecret(SECRET_STORE_USERNAME + string4));
            stringArray[1] = new String(oracleSecretStore.getSecret(SECRET_STORE_PASSWORD + string4));
            return stringArray;
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            throw (SQLException)DatabaseError.createSqlException(167, noClassDefFoundError).fillInStackTrace();
        }
        catch (Exception exception) {
            if (!(exception instanceof RuntimeException)) throw (SQLException)DatabaseError.createSqlException(168, (Object)exception).fillInStackTrace();
            throw (RuntimeException)exception;
        }
    }

    abstract void initializePassword(OpaqueString var1) throws SQLException;

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getProperties() {
        Properties properties = new Properties();
        try {
            Class<oracle.jdbc.OracleConnection> clazz = oracle.jdbc.OracleConnection.class;
            Class<GeneratedPhysicalConnection> clazz2 = GeneratedPhysicalConnection.class;
            Field[] fieldArray = clazz2.getDeclaredFields();
            for (int i2 = 0; i2 < fieldArray.length; ++i2) {
                String string;
                int n2 = fieldArray[i2].getModifiers();
                if (Modifier.isStatic(n2)) continue;
                String string2 = fieldArray[i2].getName();
                String string3 = "CONNECTION_PROPERTY_" + PhysicalConnection.propertyVariableName(string2);
                Field field = null;
                try {
                    field = clazz.getField(string3);
                }
                catch (NoSuchFieldException noSuchFieldException) {
                    continue;
                }
                if (string3.matches(".*PASSWORD.*") && !string3.equals("CONNECTION_PROPERTY_PASSWORD_AUTHENTICATION") || string3.equals("CONNECTION_PROPERTY_ACCESS_TOKEN")) continue;
                String string4 = (String)field.get(null);
                String string5 = fieldArray[i2].getType().getName();
                if (string5.equals("boolean")) {
                    boolean bl = fieldArray[i2].getBoolean(this);
                    if (bl) {
                        properties.setProperty(string4, "true");
                        continue;
                    }
                    properties.setProperty(string4, "false");
                    continue;
                }
                if (string5.equals("int")) {
                    int n3 = fieldArray[i2].getInt(this);
                    properties.setProperty(string4, Integer.toString(n3));
                    continue;
                }
                if (string5.equals("long")) {
                    long l2 = fieldArray[i2].getLong(this);
                    properties.setProperty(string4, Long.toString(l2));
                    continue;
                }
                if (!string5.equals("java.lang.String") || (string = (String)fieldArray[i2].get(this)) == null) continue;
                properties.setProperty(string4, string);
            }
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        return properties;
    }

    @Override
    @Deprecated
    public Connection _getPC() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Connection connection = null;
            return connection;
        }
    }

    @Override
    public oracle.jdbc.internal.OracleConnection getPhysicalConnection() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            PhysicalConnection physicalConnection = this;
            return physicalConnection;
        }
    }

    @Override
    public boolean isLogicalConnection() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = false;
            return bl;
        }
    }

    void createDMSSensors() {
        DMSFactory dMSFactory = DMSFactory.getInstance();
        this.dmsParent = dMSFactory.createNoun(null, DMS_ROOT_NAME, null);
        this.dmsParent = dMSFactory.createNoun(this.dmsParent, this.dmsParentName, this.dmsParentType);
        this.dmsOpenCount = dMSFactory.createEvent(this.dmsParent, DMS_OPEN_COUNT_NAME, DMS_OPEN_COUNT_DESCRIPTION);
        this.dmsCloseCount = dMSFactory.createEvent(this.dmsParent, DMS_CLOSE_COUNT_NAME, DMS_CLOSE_COUNT_DESCRIPTION);
        this.dmsGetConnection = dMSFactory.createPhaseEvent(this.dmsParent, DMS_GETCONNECTION_NAME, DMS_GETCONNECTION_DESCRIPTION);
        this.dmsGetConnection.deriveMetric(DMSFactory.SensorIntf_all);
        this.dmsParent = dMSFactory.createNoun(this.dmsParent, DMS_CONNECTION_PREFIX + DMS_CONNECTION_COUNT.incrementAndGet(), DMS_CONNECTION_TYPE);
        this.dmsUrl = dMSFactory.createState(this.dmsParent, DMS_CONNECTION_URL, "", DMS_CONNECTION_URL_DESCRIPTION, null);
        this.dmsUser = dMSFactory.createState(this.dmsParent, DMS_CONNECTION_USER_NAME, "", DMS_CONNECTION_USER_DESCRIPTION, null);
        this.dmsLogicalConnection = dMSFactory.createState(this.dmsParent, DMS_LOGICAL_CONNECTION_NAME, "", DMS_LOGICAL_CONNECTION_DESCRIPTION, null);
        this.dmsCreateNewStatement = dMSFactory.createPhaseEvent(this.dmsParent, DMS_NEW_STATEMENT_NAME, DMS_NEW_STATEMENT_DESCRIPTION);
        this.dmsCreateNewStatement.deriveMetric(DMSFactory.SensorIntf_all - DMSFactory.SensorIntf_active);
        this.dmsCreateStatement = dMSFactory.createPhaseEvent(this.dmsParent, DMS_GET_STATEMENT_NAME, DMS_GET_STATEMENT_DESCRIPTION);
        this.dmsCreateStatement.deriveMetric(DMSFactory.SensorIntf_all - DMSFactory.SensorIntf_active);
        this.commonDmsParent = dMSFactory.createNoun(this.dmsParent, DMS_STATEMENT_PARENT_NAME, DMS_STATEMENT_PARENT_TYPE);
        this.commonDmsSqlText = dMSFactory.createState(this.commonDmsParent, DMS_SQLTEXT_NAME, "", DMS_SQLTEXT_DESCRIPTION, null);
        this.commonDmsExecute = dMSFactory.createPhaseEvent(this.commonDmsParent, DMS_EXECUTE_NAME, DMS_EXECUTE_DESCRIPTION);
        this.commonDmsExecute.deriveMetric(DMSFactory.PhaseEventIntf_all);
        this.commonDmsFetch = dMSFactory.createPhaseEvent(this.commonDmsParent, DMS_FETCH_NAME, DMS_FETCH_DESCRIPTION);
        this.commonDmsFetch.deriveMetric(DMSFactory.PhaseEventIntf_all);
    }

    boolean dmsUpdateSqlText() {
        if (this.dmsVersion.equals((Object)DMSFactory.DMSVersion.v11)) {
            return DMSFactory.Context.getECForJDBC().updateSqlText();
        }
        return this.dmsStmtMetrics;
    }

    OracleTimeout getTimeout() throws SQLException {
        if (this.timeout == null) {
            this.timeout = OracleTimeout.newTimeout(this.url);
        }
        return this.timeout;
    }

    @Override
    public Statement createStatement() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Statement statement = this.createStatement(-1, -1);
            return statement;
        }
    }

    @Override
    public Statement createStatement(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Properties properties = new Properties();
            properties.setProperty("result_set_type", Integer.toString(n2));
            properties.setProperty("result_set_concurrency", Integer.toString(n3));
            properties.setProperty("use_long_fetch", this.useFetchSizeWithLongColumn ? "true" : "false");
            Statement statement = this.createStatement(properties);
            return statement;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Statement createStatement(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            OracleStatement oracleStatement = null;
            long l2 = this.dmsCreateNewStatement.start();
            try {
                oracleStatement = (OracleStatement)this.driverExtension.allocateStatement(this, properties);
            }
            finally {
                this.dmsCreateNewStatement.stop(l2);
            }
            OracleStatementWrapper oracleStatementWrapper = new OracleStatementWrapper(oracleStatement);
            return oracleStatementWrapper;
        }
    }

    @Override
    public PreparedStatement prepareStatement(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            PreparedStatement preparedStatement = this.prepareStatement(string, -1, -1);
            return preparedStatement;
        }
    }

    @Override
    @Deprecated
    public PreparedStatement prepareStatementWithKey(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            if (string == null) {
                PreparedStatement preparedStatement = null;
                return preparedStatement;
            }
            if (!this.isStatementCacheInitialized()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 95).fillInStackTrace();
            }
            long l2 = DMSFactory.getInstance().getToken();
            DMSFactory.DMSPhase dMSPhase = this.dmsCreateNewStatement;
            oracle.jdbc.internal.OraclePreparedStatement oraclePreparedStatement = null;
            oraclePreparedStatement = (OraclePreparedStatement)this.statementCache.searchExplicitCache(string);
            if (oraclePreparedStatement != null) {
                dMSPhase = this.dmsCreateStatement;
            }
            dMSPhase.start(l2);
            dMSPhase.stop(l2);
            if (oraclePreparedStatement != null) {
                oraclePreparedStatement = new OraclePreparedStatementWrapper(oraclePreparedStatement);
            }
            OraclePreparedStatement oraclePreparedStatement2 = oraclePreparedStatement;
            return oraclePreparedStatement2;
        }
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Properties properties = new Properties();
            properties.setProperty("result_set_type", Integer.toString(n2));
            properties.setProperty("result_set_concurrency", Integer.toString(n3));
            properties.setProperty("use_long_fetch", this.useFetchSizeWithLongColumn ? "true" : "false");
            PreparedStatement preparedStatement = this.prepareStatement(string, properties);
            return preparedStatement;
        }
    }

    @Override
    public PreparedStatement prepareStatement(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OraclePreparedStatement oraclePreparedStatement = this.prepareStatementInternal(string, properties);
            OraclePreparedStatementWrapper oraclePreparedStatementWrapper = new OraclePreparedStatementWrapper(oraclePreparedStatement);
            return oraclePreparedStatementWrapper;
        }
    }

    OraclePreparedStatement prepareStatementInternal(String string, int n2, int n3) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("result_set_type", Integer.toString(n2));
        properties.setProperty("result_set_concurrency", Integer.toString(n3));
        properties.setProperty("use_long_fetch", this.useFetchSizeWithLongColumn ? "true" : "false");
        return this.prepareStatementInternal(string, properties);
    }

    OraclePreparedStatement prepareStatementInternal(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        long l2 = DMSFactory.getInstance().getToken();
        DMSFactory.DMSPhase dMSPhase = this.dmsCreateNewStatement;
        if (string == null || string.length() == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 104).fillInStackTrace();
        }
        this.requireOpenConnection();
        OraclePreparedStatement oraclePreparedStatement = null;
        int n2 = -1;
        int n3 = -1;
        if (properties != null) {
            n2 = Integer.parseInt(properties.getProperty("result_set_type", "-1"));
            n3 = Integer.parseInt(properties.getProperty("result_set_concurrency", "-1"));
        }
        if (this.statementCache != null) {
            OracleResultSet.ResultSetType resultSetType = OracleResultSet.ResultSetType.typeFor(n2, n3);
            if (resultSetType == OracleResultSet.ResultSetType.UNKNOWN) {
                resultSetType = OracleStatement.DEFAULT_RESULT_SET_TYPE;
            }
            oraclePreparedStatement = (OraclePreparedStatement)this.statementCache.searchImplicitCache(string, 1, resultSetType.ordinal(), this);
        }
        if (oraclePreparedStatement == null) {
            oraclePreparedStatement = (OraclePreparedStatement)this.driverExtension.allocatePreparedStatement(this, string, properties);
        } else {
            dMSPhase = this.dmsCreateStatement;
        }
        dMSPhase.start(l2);
        dMSPhase.stop(l2);
        return oraclePreparedStatement;
    }

    @Override
    public CallableStatement prepareCall(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            CallableStatement callableStatement = this.prepareCall(string, -1, -1);
            return callableStatement;
        }
    }

    @Override
    public CallableStatement prepareCall(String string, int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Properties properties = new Properties();
            properties.setProperty("result_set_type", Integer.toString(n2));
            properties.setProperty("result_set_concurrency", Integer.toString(n3));
            properties.setProperty("use_long_fetch", this.useFetchSizeWithLongColumn ? "true" : "false");
            CallableStatement callableStatement = this.prepareCall(string, properties);
            return callableStatement;
        }
    }

    @Override
    public CallableStatement prepareCall(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Object object;
            long l2 = DMSFactory.getInstance().getToken();
            DMSFactory.DMSPhase dMSPhase = this.dmsCreateNewStatement;
            if (string == null || string.length() == 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 104).fillInStackTrace();
            }
            this.requireOpenConnection();
            oracle.jdbc.internal.OracleCallableStatement oracleCallableStatement = null;
            int n2 = -1;
            int n3 = -1;
            if (properties != null) {
                n2 = Integer.parseInt(properties.getProperty("result_set_type", "-1"));
                n3 = Integer.parseInt(properties.getProperty("result_set_concurrency", "-1"));
            }
            if (this.statementCache != null) {
                object = OracleResultSet.ResultSetType.typeFor(n2, n3);
                if (object == OracleResultSet.ResultSetType.UNKNOWN) {
                    object = OracleStatement.DEFAULT_RESULT_SET_TYPE;
                }
                oracleCallableStatement = (OracleCallableStatement)this.statementCache.searchImplicitCache(string, 2, object.ordinal(), this);
            }
            if (oracleCallableStatement == null) {
                oracleCallableStatement = this.driverExtension.allocateCallableStatement(this, string, properties);
            } else {
                dMSPhase = this.dmsCreateStatement;
            }
            dMSPhase.start(l2);
            dMSPhase.stop(l2);
            object = new OracleCallableStatementWrapper(oracleCallableStatement);
            return object;
        }
    }

    @Override
    public CallableStatement prepareCallWithKey(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            if (string == null) {
                CallableStatement callableStatement = null;
                return callableStatement;
            }
            if (!this.isStatementCacheInitialized()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 95).fillInStackTrace();
            }
            long l2 = DMSFactory.getInstance().getToken();
            DMSFactory.DMSPhase dMSPhase = this.dmsCreateNewStatement;
            oracle.jdbc.internal.OracleCallableStatement oracleCallableStatement = null;
            oracleCallableStatement = (OracleCallableStatement)this.statementCache.searchExplicitCache(string);
            if (oracleCallableStatement != null) {
                dMSPhase = this.dmsCreateStatement;
            }
            dMSPhase.start(l2);
            dMSPhase.stop(l2);
            if (oracleCallableStatement != null) {
                oracleCallableStatement = new OracleCallableStatementWrapper(oracleCallableStatement);
            }
            OracleCallableStatement oracleCallableStatement2 = oracleCallableStatement;
            return oracleCallableStatement2;
        }
    }

    @Override
    public String nativeSQL(String string) throws SQLException {
        if (this.sqlObj == null) {
            this.sqlObj = new OracleSql(this.conversion);
        }
        this.sqlObj.initialize(string);
        String string2 = this.sqlObj.getSql(this.processEscapes, this.convertNcharLiterals);
        return string2;
    }

    @Override
    public void setAutoCommit(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            if (this.autocommit == bl) {
                return;
            }
            if (bl) {
                this.disallowGlobalTxnMode(116);
            }
            this.needLine();
            this.doSetAutoCommit(bl);
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        this.requireOpenConnection();
        return this.autocommit;
    }

    @Override
    public boolean getAutoCommitInternal() throws SQLException {
        return this.autocommit;
    }

    @Override
    public void cancel() throws SQLException {
        OracleStatement oracleStatement = this.statements;
        if (this.lifecycle != 1 && this.lifecycle != 16) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        boolean bl = false;
        while (oracleStatement != null) {
            try {
                if (oracleStatement.doCancel()) {
                    bl = true;
                }
            }
            catch (SQLException sQLException) {
            }
            oracleStatement = oracleStatement.next;
        }
        if (!bl) {
            this.cancelOperationOnServer(false);
        }
    }

    @Override
    public void commit(EnumSet<OracleConnection.CommitOption> enumSet) throws SQLException {
        int n2 = 0;
        if (enumSet != null) {
            if (enumSet.contains((Object)OracleConnection.CommitOption.WRITEBATCH) && enumSet.contains((Object)OracleConnection.CommitOption.WRITEIMMED) || enumSet.contains((Object)OracleConnection.CommitOption.WAIT) && enumSet.contains((Object)OracleConnection.CommitOption.NOWAIT)) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 191).fillInStackTrace();
            }
            for (OracleConnection.CommitOption commitOption : enumSet) {
                n2 |= commitOption.getCode();
            }
        }
        this.commit(n2);
    }

    void commit(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.disallowGlobalTxnMode(114);
            if (this.autoCommitSpecCompliant && this.getAutoCommit()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 273).fillInStackTrace();
            }
            this.requireOpenConnection();
            this.sendOracleStyleBatchesBeforeCommit();
            this.validateCommitOptionFlags(n2);
            this.needLine();
            this.doCommit(n2);
        }
    }

    private final void sendOracleStyleBatchesBeforeCommit() throws SQLException {
        OracleStatement oracleStatement = this.statements;
        while (oracleStatement != null) {
            if (!oracleStatement.closed) {
                oracleStatement.sendBatch();
            }
            oracleStatement = oracleStatement.next;
        }
    }

    private final void validateCommitOptionFlags(int n2) throws SQLException {
        if ((n2 & OracleConnection.CommitOption.WRITEBATCH.getCode()) != 0 && (n2 & OracleConnection.CommitOption.WRITEIMMED.getCode()) != 0 || (n2 & OracleConnection.CommitOption.WAIT.getCode()) != 0 && (n2 & OracleConnection.CommitOption.NOWAIT.getCode()) != 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 191).fillInStackTrace();
        }
    }

    @Override
    public void commit() throws SQLException {
        this.commit(this.commitOption);
    }

    @Override
    public void rollback() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.disallowGlobalTxnMode(115);
            if (this.autoCommitSpecCompliant && this.getAutoCommit()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 274).fillInStackTrace();
            }
            this.requireOpenConnection();
            this.needLine();
            this.doRollback();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.lifecycle == 2 || this.lifecycle == 4) {
                return;
            }
            this.needLineUnchecked();
            try {
                if (this.lifecycle != 8) {
                    this.getHAManager().dropConnection(this);
                }
                if (this.closeCallback != null) {
                    this.closeCallback.beforeClose(this, this.privateData);
                }
                this.closeStatementCache();
                this.closeStatements(false);
                this.freeTemporaryBlobsAndClobs();
                if (this.lifecycle == 1) {
                    this.lifecycle = 2;
                }
                if (this.isProxy) {
                    this.close(1);
                }
                if (this.timeZoneTab != null) {
                    this.timeZoneTab.freeInstance();
                }
                this.logoff();
                this.cleanup();
                if (this.timeout != null) {
                    this.timeout.close();
                }
                if (this.closeCallback != null) {
                    this.closeCallback.afterClose(this.privateData);
                }
            }
            finally {
                this.lifecycle = 4;
                this.isUsable = false;
                this.dmsCloseCount.occurred();
                this.dmsParent.destroy();
                this.dmsParent = null;
            }
        }
    }

    @Override
    public String getDataIntegrityAlgorithmName() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getDataIntegrityAlgorithmName").fillInStackTrace();
    }

    @Override
    public String getEncryptionAlgorithmName() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getEncryptionAlgorithmName").fillInStackTrace();
    }

    @Override
    public String getAuthenticationAdaptorName() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getAuthenticationAdaptorName").fillInStackTrace();
    }

    @Override
    public void closeInternal(boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("closeInternal").fillInStackTrace();
    }

    @Override
    public void cleanupAndClose(boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("cleanupAndClose").fillInStackTrace();
    }

    @Override
    public void cleanupAndClose() throws SQLException {
        if (this.lifecycle != 1) {
            return;
        }
        this.lifecycle = 16;
        this.cancel();
    }

    @Override
    public void closeLogicalConnection() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.lifecycle == 1 || this.lifecycle == 16 || this.lifecycle == 2) {
                this.closeStatements(true);
                this.freeTemporaryBlobsAndClobs();
                if (this.clientIdSet) {
                    this.clearClientIdentifier(this.clientId);
                }
                this.logicalConnectionAttached = null;
                this.lifecycle = 1;
                this.dmsLogicalConnection.update(null);
            }
        }
    }

    @Override
    public void close(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.lifecycle == 2 || this.lifecycle == 4) {
                return;
            }
            if ((n2 & 0x1000) != 0) {
                this.close();
                return;
            }
            if ((n2 & 1) != 0 && this.isProxy) {
                this.purgeStatementCache();
                this.closeStatements(false);
                this.descriptorCacheStack[this.descriptorCacheTop--] = null;
                this.closeProxySession();
                this.isProxy = false;
                this.autocommit = this.savedAutoCommitFlag;
                this.txnMode = this.savedTxnMode;
            }
            return;
        }
    }

    @Override
    public void abort() throws SQLException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(CALL_ORACLE_ABORT_PERMISSION);
        }
        if (this.lifecycle == 4 || this.lifecycle == 8) {
            return;
        }
        this.lifecycle = 8;
        this.doAbort();
        this.isUsable = false;
    }

    abstract void doAbort() throws SQLException;

    void closeProxySession() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("closeProxySession").fillInStackTrace();
    }

    @Override
    public PreparedStatement prepareDirectPath(String string, String string2, String[] stringArray) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("prepareDirectPath").fillInStackTrace();
    }

    @Override
    public PreparedStatement prepareDirectPath(String string, String string2, String[] stringArray, String string3) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("prepareDirectPath").fillInStackTrace();
    }

    @Override
    public PreparedStatement prepareDirectPath(String string, String string2, String[] stringArray, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("prepareDirectPath").fillInStackTrace();
    }

    @Override
    public PreparedStatement prepareDirectPath(String string, String string2, String[] stringArray, String string3, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("prepareDirectPath").fillInStackTrace();
    }

    @Override
    public Properties getServerSessionInfo() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getServerSessionInfo").fillInStackTrace();
    }

    @Override
    public final boolean isClosed() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.isClosedInternal();
            return bl;
        }
    }

    final boolean isClosedInternal() {
        return this.lifecycle != 1;
    }

    @Override
    public boolean isProxySession() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.isProxy;
            return bl;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void openProxySession(int n2, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string;
            boolean bl = true;
            if (this.isProxy) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 149).fillInStackTrace();
            }
            properties = (Properties)properties.clone();
            if (n2 == 1) {
                string = properties.getProperty("PROXY_USER_NAME");
                if (string == null || string.length() == 0) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 150, "PROXY_USER_NAME cannot be null or empty.").fillInStackTrace();
                }
                Matcher matcher = USERNAME_PATTERN.matcher(string);
                if (!matcher.lookingAt()) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 150, "Unrecognized format for PROXY_USER_NAME").fillInStackTrace();
                }
                int n3 = matcher.end();
                if (n3 < string.length()) {
                    if (!string.substring(n3, n3 + 1).equals("/")) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 150, "Unrecognized format for PROXY_USER_NAME").fillInStackTrace();
                    }
                    String string2 = properties.getProperty("PROXY_USER_PASSWORD");
                    if (string2 != null && string2.length() > 0) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 150, "Password cannot be specified in both PROXY_USER_NAME and PROXY_USER_PASSWORD").fillInStackTrace();
                    }
                    properties.setProperty("PROXY_USER_NAME", string.substring(0, n3));
                    properties.setProperty("PROXY_USER_PASSWORD", string.substring(n3 + 1));
                }
            } else if (n2 == 2) {
                string = properties.getProperty("PROXY_DISTINGUISHED_NAME");
                if (string == null || string.length() == 0) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 150, "PROXY_DISTINGUISHED_NAME cannot be null or empty.").fillInStackTrace();
                }
            } else if (n2 == 3) {
                string = properties.get("PROXY_CERTIFICATE");
                if (!(string instanceof byte[])) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 150, "PROXY_CERTIFICATE must be a provided as a byte[]").fillInStackTrace();
                }
                properties.put("PROXY_CERTIFICATE", ((byte[])string).clone());
            } else {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 150, "Unrecognized type argument: " + n2).fillInStackTrace();
            }
            string = properties.get("PROXY_ROLES");
            if (string instanceof String[]) {
                properties.put("PROXY_ROLES", ((String[])string).clone());
            } else if (string != null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 150, "PROXY_ROLES must be provided as a String[]").fillInStackTrace();
            }
            this.purgeStatementCache();
            this.closeStatements(false);
            try {
                this.doProxySession(n2, properties);
                ++this.descriptorCacheTop;
                this.savedAutoCommitFlag = this.autocommit;
                this.autocommit = this.defaultautocommit;
                this.savedTxnMode = this.txnMode;
                this.txnMode = 0;
                bl = false;
                this.currentSchema = null;
            }
            finally {
                if (bl && !this.isClosed()) {
                    this.closeProxySession();
                }
            }
        }
    }

    void doProxySession(int n2, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doProxySession").fillInStackTrace();
    }

    void cleanup() {
        this.fdo = null;
        this.conversion = null;
        this.statements = null;
        this.descriptorCacheStack[this.descriptorCacheTop] = null;
        this.sqlTypeToJavaClassMap = null;
        this.javaClassNameToSqlTypeMap = null;
        this.javaObjectMap = null;
        this.statementHoldingLine = null;
        this.sqlObj = null;
        this.isProxy = false;
        this.blockSource = null;
        this.connectionBufferCacheStore = null;
        threadLocalBufferCacheStore = null;
        this.tmpByteBuf = null;
        this.tmpCharBuf = null;
        this.currentSchema = null;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            if (this.databaseMetaData == null) {
                this.databaseMetaData = new OracleDatabaseMetaData(this);
            }
            oracle.jdbc.OracleDatabaseMetaData oracleDatabaseMetaData = this.databaseMetaData;
            return oracleDatabaseMetaData;
        }
    }

    @Override
    public void setReadOnly(boolean bl) throws SQLException {
        this.requireOpenConnection();
        this.readOnly = bl;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        this.requireOpenConnection();
        return this.readOnly;
    }

    @Override
    public void setCatalog(String string) throws SQLException {
        this.requireOpenConnection();
    }

    @Override
    public String getCatalog() throws SQLException {
        this.requireOpenConnection();
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void setTransactionIsolation(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            if (this.txnLevel == n2) {
                return;
            }
            try (Statement statement = this.createStatement();){
                switch (n2) {
                    case 2: {
                        statement.execute("ALTER SESSION SET ISOLATION_LEVEL = READ COMMITTED");
                        this.txnLevel = 2;
                        return;
                    }
                    case 8: {
                        this.isResultSetCacheEnabled = false;
                        statement.execute("ALTER SESSION SET ISOLATION_LEVEL = SERIALIZABLE");
                        this.txnLevel = 8;
                        return;
                    }
                    default: {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 30).fillInStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        this.requireOpenConnection();
        return this.txnLevel;
    }

    @Override
    public void setAutoClose(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (!bl) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 31).fillInStackTrace();
            }
        }
    }

    @Override
    public boolean getAutoClose() throws SQLException {
        return true;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        this.requireOpenConnection();
        return this.sqlWarning;
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.requireOpenConnection();
        this.sqlWarning = null;
    }

    void setWarnings(SQLWarning sQLWarning) {
        this.sqlWarning = sQLWarning;
    }

    @Override
    public void setDefaultRowPrefetch(int n2) throws SQLException {
        if (n2 <= 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 20).fillInStackTrace();
        }
        this.defaultRowPrefetch = n2;
    }

    @Override
    public int getDefaultRowPrefetch() {
        return this.defaultRowPrefetch;
    }

    @Override
    public boolean getTimestamptzInGmt() {
        return this.timestamptzInGmt;
    }

    @Override
    public boolean getUse1900AsYearForTime() {
        return this.use1900AsYearForTime;
    }

    @Override
    public void setDefaultExecuteBatch(int n2) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        if (closeableLock != null) {
            if (throwable != null) {
                try {
                    closeableLock.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
            } else {
                closeableLock.close();
            }
        }
    }

    @Override
    public int getDefaultExecuteBatch() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n2 = 1;
            return n2;
        }
    }

    @Override
    public void setRemarksReporting(boolean bl) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.reportRemarks = bl;
        }
    }

    @Override
    public boolean getRemarksReporting() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.reportRemarks;
            return bl;
        }
    }

    @Override
    public void setIncludeSynonyms(boolean bl) {
        this.includeSynonyms = bl;
    }

    @Override
    public String[] getEndToEndMetrics() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String[] stringArray;
            if (this.endToEndValues == null) {
                stringArray = null;
            } else {
                stringArray = new String[4];
                System.arraycopy(this.endToEndValues, 0, stringArray, 0, 4);
            }
            String[] stringArray2 = stringArray;
            return stringArray2;
        }
    }

    @Override
    public short getEndToEndECIDSequenceNumber() throws SQLException {
        return this.endToEndECIDSequenceNumber;
    }

    @Override
    public void setEndToEndMetrics(String[] stringArray, short s2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.dmsVersion.equals((Object)DMSFactory.DMSVersion.NONE)) {
                String[] stringArray2 = new String[stringArray.length];
                System.arraycopy(stringArray, 0, stringArray2, 0, stringArray.length);
                this.setEndToEndMetricsInternal(stringArray2, s2);
            }
        }
    }

    void setEndToEndMetricsInternal(String[] stringArray, short s2) throws SQLException {
        if (stringArray != this.endToEndValues) {
            String string;
            int n2;
            if (stringArray.length != 4) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 156).fillInStackTrace();
            }
            for (n2 = 0; n2 < 4; ++n2) {
                string = stringArray[n2];
                if (string == null || string.length() <= this.endToEndMaxLength[n2]) continue;
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 159, string).fillInStackTrace();
            }
            if (this.endToEndValues != null) {
                for (n2 = 0; n2 < 4; ++n2) {
                    string = stringArray[n2];
                    if ((string != null || this.endToEndValues[n2] == null) && (string == null || string.equals(this.endToEndValues[n2]))) continue;
                    this.endToEndHasChanged[n2] = true;
                    this.endToEndAnyChanged = true;
                }
                this.endToEndHasChanged[0] = this.endToEndHasChanged[0] | this.endToEndHasChanged[3];
            } else {
                for (n2 = 0; n2 < 4; ++n2) {
                    this.endToEndHasChanged[n2] = true;
                }
                this.endToEndAnyChanged = true;
            }
            System.arraycopy(stringArray, 0, this.endToEndValues, 0, 4);
            for (n2 = 0; n2 < 4; ++n2) {
                if (stringArray[n2] == null) {
                    this.clientInfo.remove(END_TO_END_CLIENTINFO_KEYS[n2]);
                    continue;
                }
                this.clientInfo.put(END_TO_END_CLIENTINFO_KEYS[n2], stringArray[n2]);
            }
        }
        this.endToEndECIDSequenceNumber = s2;
        this.clientInfo.put(END_TO_END_CLIENTINFO_KEY_SEQ_NO, Short.toString(s2));
    }

    void updateSystemContext() throws SQLException {
        if (this.dmsVersion.equals((Object)DMSFactory.DMSVersion.v10G)) {
            String[] stringArray = DMSFactory.getExecutionContextForJDBC().getExecutionContextState();
            this.setEndToEndMetricsInternal(stringArray, (short)DMSFactory.getExecutionContextForJDBC().getECIDSequenceNumber());
        } else if (this.dmsVersion.equals((Object)DMSFactory.DMSVersion.v11)) {
            if (this.getVersionNumber() >= 11000) {
                this.updateSystemContext11();
            } else {
                Map<String, Map<String, String>> map = DMSFactory.Context.getECForJDBC().getMap();
                Map<String, String> map2 = map.get("E2E_CONTEXT");
                if (map2 != null && map2.size() != 0) {
                    String[] stringArray = new String[4];
                    for (int i2 = 0; i2 < e2eKeys.length; ++i2) {
                        stringArray[i2] = map2.get(e2eKeys[i2]);
                    }
                    this.setEndToEndMetricsInternal(stringArray, (short)0);
                }
            }
        }
    }

    void resetSystemContext() {
        for (Map.Entry<String, Map<String, String>> entry : this.currentSystemContext.entrySet()) {
            String string = entry.getKey();
            if (string == null || string.isEmpty()) continue;
            try {
                this.doClearAllApplicationContext(string);
            }
            catch (SQLException sQLException) {}
        }
        this.currentSystemContext = new IdentityHashMap<String, Map<String, String>>();
    }

    void updateSystemContext11() throws SQLException {
        IdentityHashMap<String, Map<String, String>> identityHashMap = new IdentityHashMap<String, Map<String, String>>(DMSFactory.Context.getECForJDBC().getMap());
        IdentityHashMap<String, Map<String, String>> identityHashMap2 = new IdentityHashMap<String, Map<String, String>>(this.currentSystemContext);
        for (Map.Entry object : identityHashMap.entrySet()) {
            String string;
            String string2 = (String)object.getKey();
            Map map = (Map)object.getValue();
            IdentityHashMap<Object, String> identityHashMap3 = (IdentityHashMap<Object, String>)identityHashMap2.remove(string2);
            if (identityHashMap3 == null) {
                if (map != null && !map.isEmpty()) {
                    identityHashMap3 = new IdentityHashMap<Object, String>();
                    for (Map.Entry entry : map.entrySet()) {
                        Object object2 = (String)entry.getKey();
                        string = (String)entry.getValue();
                        this.doSetApplicationContext(string2, (String)object2, string);
                        identityHashMap3.put(object2, string);
                    }
                }
            } else {
                IdentityHashMap identityHashMap4 = new IdentityHashMap(identityHashMap3);
                for (Object object2 : map.entrySet()) {
                    String string3;
                    string = (String)object2.getKey();
                    String string4 = (String)object2.getValue();
                    if (string4 == (string3 = (String)identityHashMap4.remove(string))) continue;
                    this.doSetApplicationContext(string2, string, string4);
                    identityHashMap3.put(string, string4);
                }
                if (map.isEmpty()) {
                    this.doClearAllApplicationContext(string2);
                    this.currentSystemContext.remove(string2);
                } else {
                    for (Object object2 : identityHashMap4.keySet()) {
                        this.doSetApplicationContext(string2, (String)object2, "");
                        identityHashMap3.remove(object2);
                    }
                }
            }
            if (identityHashMap3 == null || identityHashMap3.isEmpty()) continue;
            this.currentSystemContext.put(string2, identityHashMap3);
        }
        for (String string : identityHashMap2.keySet()) {
            this.doClearAllApplicationContext(string);
            this.currentSystemContext.remove(string);
        }
    }

    @Override
    public boolean getIncludeSynonyms() {
        return this.includeSynonyms;
    }

    @Override
    public void setRestrictGetTables(boolean bl) {
        this.restrictGettables = bl;
    }

    @Override
    public boolean getRestrictGetTables() {
        return this.restrictGettables;
    }

    @Override
    public void setDefaultFixedString(boolean bl) {
        this.fixedString = bl;
    }

    void setDefaultNChar(boolean bl) {
        this.defaultnchar = bl;
    }

    @Override
    public boolean getDefaultFixedString() {
        return this.fixedString;
    }

    int getNlsRatio() {
        return 1;
    }

    @Override
    public int getC2SNlsRatio() {
        return 1;
    }

    void addStatement(OracleStatement oracleStatement) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (oracleStatement.next != null) {
                throw new Error("add_statement called twice on " + oracleStatement);
            }
            oracleStatement.next = this.statements;
            if (this.statements != null) {
                this.statements.prev = oracleStatement;
            }
            this.statements = oracleStatement;
        }
    }

    void removeStatement(OracleStatement oracleStatement) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleStatement oracleStatement2 = oracleStatement.prev;
            OracleStatement oracleStatement3 = oracleStatement.next;
            if (oracleStatement2 == null) {
                if (this.statements != oracleStatement) {
                    return;
                }
                this.statements = oracleStatement3;
            } else {
                oracleStatement2.next = oracleStatement3;
            }
            if (oracleStatement3 != null) {
                oracleStatement3.prev = oracleStatement2;
            }
            oracleStatement.next = null;
            oracleStatement.prev = null;
        }
    }

    void closeStatements(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.closeStatements(bl, true);
        }
    }

    void closeStatements(boolean bl, boolean bl2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleStatement oracleStatement;
            OracleStatement oracleStatement2 = this.statements;
            while (oracleStatement2 != null) {
                oracleStatement = oracleStatement2.nextChild;
                if (oracleStatement2.serverCursor) {
                    oracleStatement2.close();
                    if (bl2) {
                        this.removeStatement(oracleStatement2);
                    }
                }
                oracleStatement2 = oracleStatement;
            }
            oracleStatement2 = this.statements;
            while (oracleStatement2 != null) {
                oracleStatement = oracleStatement2.next;
                if (bl) {
                    oracleStatement2.closeWrapper(this.isClosed());
                    oracleStatement2.close();
                } else {
                    oracleStatement2.hardClose();
                    oracleStatement2.closeWrapper(this.isClosed());
                }
                if (bl2) {
                    this.removeStatement(oracleStatement2);
                }
                oracleStatement2 = oracleStatement;
            }
        }
    }

    final void purgeStatementCache() throws SQLException {
        if (this.isStatementCacheInitialized()) {
            this.statementCache.purgeImplicitCache();
            this.statementCache.purgeExplicitCache();
        }
    }

    final void closeStatementCache() throws SQLException {
        if (this.isStatementCacheInitialized()) {
            this.statementCache.close();
            this.statementCache = null;
            this.clearStatementMetaData = true;
        }
    }

    void needLine() throws SQLException {
        this.requireOpenConnection();
        this.needLineUnchecked();
    }

    void needLineUnchecked() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementHoldingLine != null) {
                this.statementHoldingLine.freeLine();
            }
        }
    }

    void holdLine(oracle.jdbc.internal.OracleStatement oracleStatement) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.holdLine((OracleStatement)oracleStatement);
        }
    }

    void holdLine(OracleStatement oracleStatement) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.statementHoldingLine = oracleStatement;
        }
    }

    void releaseLine() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.releaseLineForCancel();
        }
    }

    void releaseLineForCancel() {
        this.statementHoldingLine = null;
    }

    @Override
    public void startup(String string, int n2) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                this.requireOpenConnection();
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("startup").fillInStackTrace();
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

    @Override
    public void startup(OracleConnection.DatabaseStartupMode databaseStartupMode) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            if (databaseStartupMode == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.needLine();
            this.doStartup(databaseStartupMode.getMode());
        }
    }

    @Override
    public void startup(OracleConnection.DatabaseStartupMode databaseStartupMode, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            if (databaseStartupMode == null || string == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.needLine();
            this.doStartup(databaseStartupMode.getMode(), string);
        }
    }

    void doStartup(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doStartup").fillInStackTrace();
    }

    void doStartup(int n2, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doStartup").fillInStackTrace();
    }

    @Override
    public void shutdown(OracleConnection.DatabaseShutdownMode databaseShutdownMode) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            if (databaseShutdownMode == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.needLine();
            this.doShutdown(databaseShutdownMode.getMode());
        }
    }

    void doShutdown(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doShutdown").fillInStackTrace();
    }

    @Override
    public void archive(int n2, int n3, String string) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                this.requireOpenConnection();
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("archive").fillInStackTrace();
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

    @Override
    public void registerSQLType(String string, String string2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (string == null || string2 == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            try {
                this.registerSQLType(string, Class.forName(string2));
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Class not found: " + string2).fillInStackTrace();
            }
        }
    }

    @Override
    public void registerSQLType(String string, Class<?> clazz) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (string == null || clazz == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            if (this.sqlTypeToJavaClassMap == null) {
                this.sqlTypeToJavaClassMap = new Hashtable(10);
            }
            this.sqlTypeToJavaClassMap.put(string, clazz);
            if (this.javaClassNameToSqlTypeMap == null) {
                this.javaClassNameToSqlTypeMap = new Hashtable<String, String>(10);
            }
            this.javaClassNameToSqlTypeMap.put(clazz.getName(), string);
        }
    }

    @Override
    public String getSQLType(Object object) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (object != null && this.javaClassNameToSqlTypeMap != null) {
                String string = object.getClass().getName();
                String string2 = this.javaClassNameToSqlTypeMap.get(string);
                return string2;
            }
            String string = null;
            return string;
        }
    }

    @Override
    public Object getJavaObject(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Class<?> clazz;
            Class<?> clazz2 = null;
            try {
                if (string != null && this.sqlTypeToJavaClassMap != null) {
                    clazz = this.sqlTypeToJavaClassMap.get(string);
                    clazz2 = clazz.newInstance();
                }
            }
            catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
            catch (InstantiationException instantiationException) {
                instantiationException.printStackTrace();
            }
            clazz = clazz2;
            return clazz;
        }
    }

    @Override
    public void putDescriptor(String string, Object object) throws SQLException {
        block14: {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                if (string != null && object != null) {
                    if (this.descriptorCacheStack[this.descriptorCacheTop] == null) {
                        this.descriptorCacheStack[this.descriptorCacheTop] = new Hashtable(10);
                    }
                    ((TypeDescriptor)object).fixupConnection(this);
                    this.descriptorCacheStack[this.descriptorCacheTop].put(string, object);
                    break block14;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
        }
    }

    @Override
    public Object getDescriptor(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Object object = null;
            if (string != null) {
                if (this.descriptorCacheStack[this.descriptorCacheTop] != null) {
                    object = this.descriptorCacheStack[this.descriptorCacheTop].get(string);
                }
                if (object == null && this.descriptorCacheTop == 1 && this.descriptorCacheStack[0] != null) {
                    object = this.descriptorCacheStack[0].get(string);
                }
            }
            Object object2 = object;
            return object2;
        }
    }

    @Override
    public void removeDescriptor(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (string != null && this.descriptorCacheStack[this.descriptorCacheTop] != null) {
                this.descriptorCacheStack[this.descriptorCacheTop].remove(string);
            }
            if (string != null && this.descriptorCacheTop == 1 && this.descriptorCacheStack[0] != null) {
                this.descriptorCacheStack[0].remove(string);
            }
        }
    }

    @Override
    public void removeAllDescriptor() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            for (int i2 = 0; i2 <= this.descriptorCacheTop; ++i2) {
                if (this.descriptorCacheStack[i2] == null) continue;
                this.descriptorCacheStack[i2].clear();
            }
        }
    }

    @Override
    public int numberOfDescriptorCacheEntries() {
        int n2 = 0;
        for (int i2 = 0; i2 <= this.descriptorCacheTop; ++i2) {
            if (this.descriptorCacheStack[i2] == null) continue;
            n2 += this.descriptorCacheStack[i2].size();
        }
        return n2;
    }

    @Override
    public Enumeration<String> descriptorCacheKeys() {
        if (this.descriptorCacheTop == 0) {
            if (this.descriptorCacheStack[this.descriptorCacheTop] != null) {
                return this.descriptorCacheStack[this.descriptorCacheTop].keys();
            }
            return null;
        }
        if (this.descriptorCacheStack[0] == null && this.descriptorCacheStack[1] != null) {
            return this.descriptorCacheStack[1].keys();
        }
        if (this.descriptorCacheStack[1] == null && this.descriptorCacheStack[0] != null) {
            return this.descriptorCacheStack[0].keys();
        }
        if (this.descriptorCacheStack[0] == null && this.descriptorCacheStack[1] == null) {
            return null;
        }
        Vector<String> vector = new Vector<String>(this.descriptorCacheStack[1].keySet());
        vector.addAll(this.descriptorCacheStack[0].keySet());
        return vector.elements();
    }

    @Override
    public void putDescriptor(byte[] byArray, Object object) throws SQLException {
        block14: {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                if (byArray != null && object != null) {
                    if (this.descriptorCacheStack[this.descriptorCacheTop] == null) {
                        this.descriptorCacheStack[this.descriptorCacheTop] = new Hashtable(10);
                    }
                    this.descriptorCacheStack[this.descriptorCacheTop].put(PhysicalConnection.toidToString(byArray), object);
                    break block14;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
        }
    }

    private static final String toidToString(byte[] byArray) {
        return "TOID\u0000" + new String(byArray, 0);
    }

    @Override
    public Object getDescriptor(byte[] byArray) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            String string;
            Object object = null;
            if (byArray != null) {
                string = PhysicalConnection.toidToString(byArray);
                if (this.descriptorCacheStack[this.descriptorCacheTop] != null) {
                    object = this.descriptorCacheStack[this.descriptorCacheTop].get(string);
                }
                if (object == null && this.descriptorCacheTop == 1 && this.descriptorCacheStack[0] != null) {
                    object = this.descriptorCacheStack[0].get(string);
                }
            }
            string = object;
            return string;
        }
    }

    @Override
    public short getJdbcCsId() throws SQLException {
        if (this.conversion == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 65).fillInStackTrace();
        }
        return this.conversion.getClientCharSet();
    }

    @Override
    public short getDbCsId() throws SQLException {
        if (this.conversion == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 65).fillInStackTrace();
        }
        return this.conversion.getServerCharSetId();
    }

    short getNCsId() throws SQLException {
        if (this.conversion == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 65).fillInStackTrace();
        }
        return this.conversion.getNCharSetId();
    }

    @Override
    public short getStructAttrCsId() throws SQLException {
        return this.getDbCsId();
    }

    @Override
    public short getStructAttrNCsId() throws SQLException {
        return this.getNCsId();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            if (this.sqlTypeToJavaClassMap == null) {
                this.sqlTypeToJavaClassMap = new Hashtable(10);
            }
            Map<String, Class<?>> map = this.sqlTypeToJavaClassMap;
            return map;
        }
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.requireOpenConnection();
            this.sqlTypeToJavaClassMap = map;
        }
    }

    @Override
    public void setUsingXAFlag(boolean bl) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.usingXA = bl;
        }
    }

    @Override
    public boolean getUsingXAFlag() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.usingXA;
            return bl;
        }
    }

    @Override
    public void setXAErrorFlag(boolean bl) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.xaWantsError = bl;
        }
    }

    @Override
    public boolean getXAErrorFlag() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.xaWantsError;
            return bl;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    String getPropertyFromDatabase(String string) throws SQLException {
        String string2 = null;
        Statement statement = null;
        ResultSet resultSet = null;
        this.beginNonRequestCalls();
        try {
            statement = this.createStatement();
            statement.setFetchSize(1);
            resultSet = statement.executeQuery(string);
            if (resultSet.next()) {
                string2 = resultSet.getString(1);
            }
        }
        finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            this.endNonRequestCalls();
        }
        return string2;
    }

    @Override
    public String getUserName() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.proxyClientName != null) {
                String string = this.proxyClientName;
                return string;
            }
            if (this.userName == null) {
                this.userName = this.getPropertyFromDatabase("SELECT USER FROM DUAL");
            }
            String string = this.userName;
            return string;
        }
    }

    @Override
    public String getCurrentSchema() throws SQLException {
        this.currentSchema = this.getPropertyFromDatabase("SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') FROM DUAL");
        return this.currentSchema;
    }

    @Override
    public String getDefaultSchemaNameForNamedTypes() throws SQLException {
        String string = null;
        string = this.createDescriptorUseCurrentSchemaForSchemaName ? this.getCurrentSchema() : this.getUserName();
        return string;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte[] getFDO(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Object object;
            if (this.fdo == null && bl) {
                object = null;
                this.beginNonRequestCalls();
                try {
                    object = this.prepareCall("begin :1 := sys.dbms_pickler.get_format (:2); end;");
                    object.registerOutParameter(1, 2);
                    object.registerOutParameter(2, -4);
                    object.execute();
                    this.fdo = object.getBytes(2);
                }
                finally {
                    if (object != null) {
                        object.close();
                    }
                    object = null;
                    this.endNonRequestCalls();
                }
            }
            object = this.fdo;
            return object;
        }
    }

    @Override
    public void setFDO(byte[] byArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.fdo = byArray;
        }
    }

    @Override
    public boolean getBigEndian() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.bigEndian == null) {
                int[] nArray = Util.toJavaUnsignedBytes(this.getFDO(true));
                int n2 = nArray[6 + nArray[5] + nArray[6] + 5];
                int n3 = n2 & 0x10;
                if (n3 < 0) {
                    n3 += 256;
                }
                this.bigEndian = n3 > 0 ? Boolean.TRUE : Boolean.FALSE;
            }
            boolean bl = this.bigEndian;
            return bl;
        }
    }

    @Override
    public void setHoldability(int n2) throws SQLException {
        this.requireOpenConnection();
        if (!this.getMetaData().supportsResultSetHoldability(n2)) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 162).fillInStackTrace();
        }
    }

    @Override
    public int getHoldability() throws SQLException {
        this.requireOpenConnection();
        return 1;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleSavepoint oracleSavepoint = this.oracleSetSavepoint();
            return oracleSavepoint;
        }
    }

    @Override
    public Savepoint setSavepoint(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleSavepoint oracleSavepoint = this.oracleSetSavepoint(string);
            return oracleSavepoint;
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.disallowGlobalTxnMode(115);
            if (this.autocommit) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 121).fillInStackTrace();
            }
            String string = null;
            if (savepoint != null) {
                try {
                    string = savepoint.getSavepointName();
                }
                catch (SQLException sQLException) {
                    string = "ORACLE_SVPT_" + savepoint.getSavepointId();
                }
            }
            try (Statement statement = this.createStatement();){
                statement.executeUpdate("ROLLBACK TO " + string);
            }
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("releaseSavepoint").fillInStackTrace();
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

    @Override
    public Statement createStatement(int n2, int n3, int n4) throws SQLException {
        if (!this.getMetaData().supportsResultSetHoldability(n4)) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 162).fillInStackTrace();
        }
        return this.createStatement(n2, n3);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, int n3, int n4) throws SQLException {
        if (!this.getMetaData().supportsResultSetHoldability(n4)) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 162).fillInStackTrace();
        }
        return this.prepareStatement(string, n2, n3);
    }

    @Override
    public CallableStatement prepareCall(String string, int n2, int n3, int n4) throws SQLException {
        if (!this.getMetaData().supportsResultSetHoldability(n4)) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 162).fillInStackTrace();
        }
        return this.prepareCall(string, n2, n3);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2) throws SQLException {
        AutoKeyInfo autoKeyInfo = new AutoKeyInfo(string);
        if (n2 == 2 || !autoKeyInfo.isInsertOrUpdateSqlStmt()) {
            return this.prepareStatement(string);
        }
        if (n2 != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        return this.prepareGeneratedKeysStatement(autoKeyInfo);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int[] nArray) throws SQLException {
        AutoKeyInfo autoKeyInfo = new AutoKeyInfo(string, nArray);
        if (!autoKeyInfo.isInsertOrUpdateSqlStmt()) {
            return this.prepareStatement(string);
        }
        if (nArray == null || nArray.length == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        autoKeyInfo.initialize(this);
        return this.prepareGeneratedKeysStatement(autoKeyInfo);
    }

    @Override
    public PreparedStatement prepareStatement(String string, String[] stringArray) throws SQLException {
        AutoKeyInfo autoKeyInfo = new AutoKeyInfo(string, stringArray);
        if (!autoKeyInfo.isInsertOrUpdateSqlStmt()) {
            return this.prepareStatement(string);
        }
        if (stringArray == null || stringArray.length == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        autoKeyInfo.getTableName();
        return this.prepareGeneratedKeysStatement(autoKeyInfo);
    }

    private PreparedStatement prepareGeneratedKeysStatement(AutoKeyInfo autoKeyInfo) throws SQLException {
        oracle.jdbc.OraclePreparedStatement oraclePreparedStatement = (oracle.jdbc.OraclePreparedStatement)this.prepareStatement(autoKeyInfo.getNewSql());
        OraclePreparedStatement oraclePreparedStatement2 = (OraclePreparedStatement)((OraclePreparedStatementWrapper)oraclePreparedStatement).preparedStatement;
        oraclePreparedStatement2.isAutoGeneratedKey = true;
        oraclePreparedStatement2.autoKeyInfo = autoKeyInfo;
        return oraclePreparedStatement;
    }

    @Override
    public OracleSavepoint oracleSetSavepoint() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.disallowGlobalTxnMode(117);
            if (this.autocommit) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 120).fillInStackTrace();
            }
            oracle.jdbc.driver.OracleSavepoint oracleSavepoint = new oracle.jdbc.driver.OracleSavepoint();
            String string = "SAVEPOINT ORACLE_SVPT_" + oracleSavepoint.getSavepointId();
            try (Object object = this.createStatement();){
                object.executeUpdate(string);
            }
            object = oracleSavepoint;
            return object;
        }
    }

    @Override
    public OracleSavepoint oracleSetSavepoint(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.disallowGlobalTxnMode(117);
            if (this.autocommit) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 120).fillInStackTrace();
            }
            oracle.jdbc.driver.OracleSavepoint oracleSavepoint = new oracle.jdbc.driver.OracleSavepoint(string);
            String string2 = oracleSavepoint.getType() == 1 ? "SAVEPOINT ORACLE_SVPT_" + oracleSavepoint.getSavepointId() : "SAVEPOINT " + oracleSavepoint.getSavepointName();
            try (Object object = this.createStatement();){
                object.executeUpdate(string2);
            }
            object = oracleSavepoint;
            return object;
        }
    }

    @Override
    public void oracleRollback(OracleSavepoint oracleSavepoint) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.disallowGlobalTxnMode(115);
            if (this.autocommit) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 121).fillInStackTrace();
            }
            String string = null;
            if (oracleSavepoint != null) {
                try {
                    string = oracleSavepoint.getSavepointName();
                }
                catch (SQLException sQLException) {
                    string = "ORACLE_SVPT_" + oracleSavepoint.getSavepointId();
                }
            }
            try (Statement statement = this.createStatement();){
                statement.executeUpdate("ROLLBACK TO " + string);
            }
        }
    }

    @Override
    public void oracleReleaseSavepoint(OracleSavepoint oracleSavepoint) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("oracleReleaseSavepoint").fillInStackTrace();
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

    void disallowGlobalTxnMode(int n2) throws SQLException {
        if (this.txnMode == 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), n2).fillInStackTrace();
        }
    }

    @Override
    public void setTxnMode(int n2) {
        this.txnMode = n2;
    }

    @Override
    public int getTxnMode() {
        return this.txnMode;
    }

    @Override
    public Object getClientData(Object object) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.clientData == null) {
                Object var4_4 = null;
                return var4_4;
            }
            Object object2 = this.clientData.get(object);
            return object2;
        }
    }

    @Override
    public Object setClientData(Object object, Object object2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.clientData == null) {
                this.clientData = new Hashtable();
            }
            Object object3 = this.clientData.put(object, object2);
            return object3;
        }
    }

    @Override
    public Object removeClientData(Object object) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.clientData == null) {
                Object var4_4 = null;
                return var4_4;
            }
            Object object2 = this.clientData.remove(object);
            return object2;
        }
    }

    @Override
    public BlobDBAccess createBlobDBAccess() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("createBlobDBAccess").fillInStackTrace();
    }

    @Override
    public ClobDBAccess createClobDBAccess() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("createClobDBAccess").fillInStackTrace();
    }

    @Override
    public BfileDBAccess createBfileDBAccess() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("createBfileDBAccess").fillInStackTrace();
    }

    void printState() {
        try {
            short s2 = this.getJdbcCsId();
            short s3 = this.getDbCsId();
            short s4 = this.getStructAttrCsId();
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
    }

    @Override
    public String getProtocolType() {
        return this.protocol;
    }

    @Override
    public String getURL() {
        return this.url;
    }

    @Override
    @Deprecated
    public void setStmtCacheSize(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.setStatementCacheSize(n2);
            this.setImplicitCachingEnabled(true);
            this.setExplicitCachingEnabled(true);
        }
    }

    @Override
    @Deprecated
    public void setStmtCacheSize(int n2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.setStatementCacheSize(n2);
            this.setImplicitCachingEnabled(true);
            this.setExplicitCachingEnabled(true);
            this.clearStatementMetaData = bl;
        }
    }

    @Override
    @Deprecated
    public int getStmtCacheSize() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
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

    @Override
    public void setStatementCacheSize(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache == null) {
                this.statementCache = new LRUStatementCache(n2);
                this.statementCache.createDMSSensors(this.dmsParent);
            } else {
                this.statementCache.resize(n2);
            }
        }
    }

    @Override
    public int getStatementCacheSize() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache == null) {
                int n2 = -1;
                return n2;
            }
            int n3 = this.statementCache.getCacheSize();
            return n3;
        }
    }

    @Override
    public void setImplicitCachingEnabled(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache == null) {
                this.statementCache = new LRUStatementCache(0);
                this.statementCache.createDMSSensors(this.dmsParent);
            }
            this.statementCache.setImplicitCachingEnabled(bl);
        }
    }

    @Override
    public boolean getImplicitCachingEnabled() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache == null) {
                boolean bl = false;
                return bl;
            }
            boolean bl = this.statementCache.getImplicitCachingEnabled();
            return bl;
        }
    }

    @Override
    public void setExplicitCachingEnabled(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache == null) {
                this.statementCache = new LRUStatementCache(0);
                this.statementCache.createDMSSensors(this.dmsParent);
            }
            this.statementCache.setExplicitCachingEnabled(bl);
        }
    }

    @Override
    public boolean getExplicitCachingEnabled() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache == null) {
                boolean bl = false;
                return bl;
            }
            boolean bl = this.statementCache.getExplicitCachingEnabled();
            return bl;
        }
    }

    @Override
    public void purgeImplicitCache() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache != null) {
                this.statementCache.purgeImplicitCache();
            }
        }
    }

    @Override
    public void purgeExplicitCache() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache != null) {
                this.statementCache.purgeExplicitCache();
            }
        }
    }

    @Override
    public PreparedStatement getStatementWithKey(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache != null) {
                OracleStatement oracleStatement = this.statementCache.searchExplicitCache(string);
                if (oracleStatement == null || oracleStatement.statementType == 1) {
                    PreparedStatement preparedStatement = (PreparedStatement)((Object)oracleStatement);
                    return preparedStatement;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 125).fillInStackTrace();
            }
            PreparedStatement preparedStatement = null;
            return preparedStatement;
        }
    }

    @Override
    public CallableStatement getCallWithKey(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache != null) {
                OracleStatement oracleStatement = this.statementCache.searchExplicitCache(string);
                if (oracleStatement == null || oracleStatement.statementType == 2) {
                    CallableStatement callableStatement = (CallableStatement)((Object)oracleStatement);
                    return callableStatement;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 125).fillInStackTrace();
            }
            CallableStatement callableStatement = null;
            return callableStatement;
        }
    }

    void cacheImplicitStatement(OraclePreparedStatement oraclePreparedStatement, String string, int n2, OracleResultSet.ResultSetType resultSetType) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 95).fillInStackTrace();
            }
            this.statementCache.addToImplicitCache(oraclePreparedStatement, string, n2, resultSetType.ordinal());
        }
    }

    void cacheExplicitStatement(OraclePreparedStatement oraclePreparedStatement, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 95).fillInStackTrace();
            }
            this.statementCache.addToExplicitCache(oraclePreparedStatement, string);
        }
    }

    @Override
    public boolean isStatementCacheInitialized() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.statementCache == null) {
                boolean bl = false;
                return bl;
            }
            if (this.statementCache.getCacheSize() == 0) {
                boolean bl = false;
                return bl;
            }
            boolean bl = true;
            return bl;
        }
    }

    BlockSource getBlockSource() {
        return this.blockSource;
    }

    private BufferCacheStore getBufferCacheStore() {
        if (this.useThreadLocalBufferCache) {
            if (threadLocalBufferCacheStore == null) {
                BufferCacheStore.MAX_CACHED_BUFFER_SIZE = this.maxCachedBufferSize;
                threadLocalBufferCacheStore = new ThreadLocal<BufferCacheStore>(){

                    @Override
                    protected BufferCacheStore initialValue() {
                        return new BufferCacheStore();
                    }
                };
            }
            return threadLocalBufferCacheStore.get();
        }
        if (this.connectionBufferCacheStore == null) {
            this.connectionBufferCacheStore = new BufferCacheStore(this.maxCachedBufferSize);
        }
        return this.connectionBufferCacheStore;
    }

    void cacheBuffer(byte[] byArray) {
        if (byArray != null) {
            BufferCacheStore bufferCacheStore = this.getBufferCacheStore();
            bufferCacheStore.byteBufferCache.put(byArray);
        }
    }

    void cacheBuffer(char[] cArray) {
        if (cArray != null) {
            BufferCacheStore bufferCacheStore = this.getBufferCacheStore();
            bufferCacheStore.charBufferCache.put(cArray);
        }
    }

    public void cacheBufferSync(char[] cArray) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.cacheBuffer(cArray);
        }
    }

    byte[] getByteBuffer(int n2) {
        BufferCacheStore bufferCacheStore = this.getBufferCacheStore();
        return bufferCacheStore.byteBufferCache.get(Byte.TYPE, n2);
    }

    char[] getCharBuffer(int n2) {
        BufferCacheStore bufferCacheStore = this.getBufferCacheStore();
        return bufferCacheStore.charBufferCache.get(Character.TYPE, n2);
    }

    public char[] getCharBufferSync(int n2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            char[] cArray = this.getCharBuffer(n2);
            return cArray;
        }
    }

    @Override
    public OracleConnection.BufferCacheStatistics getByteBufferCacheStatistics() {
        BufferCacheStore bufferCacheStore = this.getBufferCacheStore();
        return bufferCacheStore.byteBufferCache.getStatistics();
    }

    @Override
    public OracleConnection.BufferCacheStatistics getCharBufferCacheStatistics() {
        BufferCacheStore bufferCacheStore = this.getBufferCacheStore();
        return bufferCacheStore.charBufferCache.getStatistics();
    }

    @Override
    public void registerTAFCallback(OracleOCIFailover oracleOCIFailover, Object object) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("registerTAFCallback").fillInStackTrace();
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

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        if (this.databaseProductVersion == "") {
            this.needLine();
            this.databaseProductVersion = this.doGetDatabaseProductVersion();
        }
        return this.databaseProductVersion;
    }

    boolean getReportRemarks() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.reportRemarks;
            return bl;
        }
    }

    @Override
    public short getVersionNumber() throws SQLException {
        if (this.versionNumber == -1) {
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                if (this.versionNumber == -1) {
                    this.needLine();
                    this.versionNumber = this.doGetVersionNumber();
                }
            }
        }
        return this.versionNumber;
    }

    void registerCloseCallback(OracleCloseCallback oracleCloseCallback, Object object) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.closeCallback = oracleCloseCallback;
            this.privateData = object;
        }
    }

    @Override
    public void setCreateStatementAsRefCursor(boolean bl) {
    }

    @Override
    public boolean getCreateStatementAsRefCursor() {
        return false;
    }

    @Override
    public int pingDatabase() throws SQLException {
        if (this.lifecycle != 1) {
            return -1;
        }
        if (this.checkAndDrain()) {
            return -1;
        }
        return this.doPingDatabase();
    }

    @Override
    public int pingDatabase(int n2) throws SQLException {
        if (this.lifecycle != 1) {
            return -1;
        }
        if (n2 == 0) {
            return this.pingDatabase();
        }
        if (this.checkAndDrain()) {
            return -1;
        }
        try {
            this.pingResult = -2;
            Thread thread = new Thread(new Runnable(){

                @Override
                public void run() {
                    try {
                        PhysicalConnection.this.pingResult = PhysicalConnection.this.doPingDatabase();
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
            });
            thread.start();
            thread.join(TimeUnit.SECONDS.toMillis(n2));
            thread.interrupt();
            return this.pingResult;
        }
        catch (InterruptedException interruptedException) {
            return -3;
        }
    }

    int doPingDatabase() throws SQLException {
        return this.executeDefaultConnectionValidationQuery(0);
    }

    @Override
    public Map<String, Class<?>> getJavaObjectTypeMap() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Map<String, Class<?>> map = this.javaObjectMap;
            return map;
        }
    }

    @Override
    public void setJavaObjectTypeMap(Map<String, Class<?>> map) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.javaObjectMap = map;
        }
    }

    @Override
    @Deprecated
    public void clearClientIdentifier(String string) throws SQLException {
        String[] stringArray;
        if (this.dmsVersion.equals((Object)DMSFactory.DMSVersion.NONE) && string != null && string.length() != 0 && (stringArray = this.getEndToEndMetrics()) != null && string.equals(stringArray[1])) {
            stringArray[1] = null;
            this.setEndToEndMetrics(stringArray, this.getEndToEndECIDSequenceNumber());
        }
    }

    @Override
    @Deprecated
    public void setClientIdentifier(String string) throws SQLException {
        if (this.dmsVersion.equals((Object)DMSFactory.DMSVersion.NONE)) {
            String[] stringArray = this.getEndToEndMetrics();
            if (stringArray == null) {
                stringArray = new String[4];
            }
            stringArray[1] = string;
            this.setEndToEndMetrics(stringArray, this.getEndToEndECIDSequenceNumber());
        }
    }

    @Override
    public void setSessionTimeZone(String string) throws SQLException {
        Object var3_3 = null;
        try (Statement statement = null;){
            statement = this.createStatement();
            statement.executeUpdate("ALTER SESSION SET TIME_ZONE = '" + string + "'");
            if (this.dbTzCalendar == null) {
                this.setDbTzCalendar(this.getDatabaseTimeZone());
            }
        }
        this.sessionTimeZone = string;
    }

    @Override
    public String getDatabaseTimeZone() throws SQLException {
        if (this.databaseTimeZone == null) {
            this.databaseTimeZone = this.getPropertyFromDatabase("SELECT DBTIMEZONE FROM DUAL");
        }
        return this.databaseTimeZone;
    }

    @Override
    public String getSessionTimeZone() {
        return this.sessionTimeZone;
    }

    private static String to2DigitString(int n2) {
        String string = n2 < 10 ? "0" + n2 : "" + n2;
        return string;
    }

    String tzToOffset(String string) {
        if (string == null) {
            return string;
        }
        char c2 = string.charAt(0);
        if (c2 != '-' && c2 != '+') {
            TimeZone timeZone = TimeZone.getTimeZone(string);
            int n2 = timeZone.getOffset(System.currentTimeMillis());
            if (n2 != 0) {
                int n3 = n2 / 60000;
                int n4 = n3 / 60;
                string = n2 > 0 ? "+" + PhysicalConnection.to2DigitString(n4) + ":" + PhysicalConnection.to2DigitString(n3) : "-" + PhysicalConnection.to2DigitString(-n4) + ":" + PhysicalConnection.to2DigitString(-(n3 -= n4 * 60));
            } else {
                string = "+00:00";
            }
        }
        return string;
    }

    @Override
    public String getSessionTimeZoneOffset() throws SQLException {
        String string = this.getPropertyFromDatabase("SELECT SESSIONTIMEZONE FROM DUAL");
        if (string != null) {
            string = this.tzToOffset(string.trim());
        }
        return string;
    }

    private void setDbTzCalendar(String string) {
        char c2 = string.charAt(0);
        if (c2 == '-' || c2 == '+') {
            string = "GMT" + string;
        }
        TimeZone timeZone = TimeZone.getTimeZone(string);
        this.dbTzCalendar = new GregorianCalendar(timeZone);
    }

    Calendar getDbTzCalendar() throws SQLException {
        if (this.dbTzCalendar == null) {
            this.setDbTzCalendar(this.getDatabaseTimeZone());
        }
        Calendar calendar = null;
        if (this.dbTzCalendar != null) {
            calendar = (Calendar)this.dbTzCalendar.clone();
        }
        return calendar;
    }

    void setAccumulateBatchResult(boolean bl) {
        this.accumulateBatchResult = bl;
    }

    boolean isAccumulateBatchResult() {
        return this.accumulateBatchResult;
    }

    void setJ2EE13Compliant(boolean bl) {
        this.j2ee13Compliant = bl;
    }

    boolean getJ2EE13Compliant() {
        return this.j2ee13Compliant;
    }

    @Override
    public boolean getJDBCStandardBehavior() {
        return this.jdbcStandardBehavior;
    }

    @Override
    public Class<?> classForNameAndSchema(String string, String string2) throws ClassNotFoundException {
        return Class.forName(string);
    }

    Class<?> safelyGetClassForName(String string) throws ClassNotFoundException {
        return Class.forName(string);
    }

    static boolean isJsonJarPresent() {
        return IS_JSON_JAR_LOADED;
    }

    @Override
    public int getHeapAllocSize() throws SQLException {
        this.requireOpenConnection();
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getHeapAllocSize").fillInStackTrace();
    }

    @Override
    public int getOCIEnvHeapAllocSize() throws SQLException {
        this.requireOpenConnection();
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getOCIEnvHeapAllocSize").fillInStackTrace();
    }

    static OracleConnection unwrapCompletely(oracle.jdbc.OracleConnection oracleConnection) {
        oracle.jdbc.OracleConnection oracleConnection2;
        oracle.jdbc.OracleConnection oracleConnection3 = oracleConnection2 = oracleConnection;
        while (oracleConnection3 != null) {
            oracleConnection2 = oracleConnection3;
            oracleConnection3 = oracleConnection2.unwrap();
        }
        return (OracleConnection)oracleConnection2;
    }

    @Override
    public void setWrapper(oracle.jdbc.OracleConnection oracleConnection) {
        this.wrapper = oracleConnection;
    }

    @Override
    public oracle.jdbc.OracleConnection unwrap() {
        return null;
    }

    @Override
    public oracle.jdbc.OracleConnection getWrapper() {
        if (this.wrapper != null) {
            return this.wrapper;
        }
        return this;
    }

    static oracle.jdbc.internal.OracleConnection _physicalConnectionWithin(Connection connection) {
        OracleConnection oracleConnection = null;
        if (connection != null) {
            oracleConnection = PhysicalConnection.unwrapCompletely((oracle.jdbc.OracleConnection)connection);
        }
        return oracleConnection;
    }

    @Override
    public oracle.jdbc.internal.OracleConnection physicalConnectionWithin() {
        return this;
    }

    @Override
    public long getTdoCState(String string, String string2) throws SQLException {
        return 0L;
    }

    @Override
    public long getTdoCState(String string) throws SQLException {
        return 0L;
    }

    void getOracleTypeADT(OracleTypeADT oracleTypeADT) throws SQLException {
    }

    @Override
    public Datum toDatum(CustomDatum customDatum) throws SQLException {
        return customDatum.toDatum(this);
    }

    @Override
    public short getNCharSet() {
        return this.conversion.getNCharSetId();
    }

    @Override
    public ResultSet newArrayDataResultSet(Datum[] datumArray, long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        return new ArrayDataResultSet(this, datumArray, l2, n2, map);
    }

    @Override
    public ResultSet newArrayDataResultSet(OracleArray oracleArray, long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        return new ArrayDataResultSet(this, oracleArray, l2, n2, map);
    }

    @Override
    public ResultSet newArrayLocatorResultSet(ArrayDescriptor arrayDescriptor, byte[] byArray, long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        return ArrayLocatorResultSet.create(this, arrayDescriptor, byArray, l2, n2, map);
    }

    @Override
    public ResultSetMetaData newStructMetaData(StructDescriptor structDescriptor) throws SQLException {
        return new StructMetaData(structDescriptor);
    }

    @Override
    public int CHARBytesToJavaChars(byte[] byArray, int n2, char[] cArray) throws SQLException {
        int[] nArray = new int[]{n2};
        return this.conversion.CHARBytesToJavaChars(byArray, 0, cArray, 0, nArray, cArray.length);
    }

    @Override
    public int NCHARBytesToJavaChars(byte[] byArray, int n2, char[] cArray) throws SQLException {
        int[] nArray = new int[1];
        return this.conversion.NCHARBytesToJavaChars(byArray, 0, cArray, 0, nArray, cArray.length);
    }

    @Override
    public boolean IsNCharFixedWith() {
        return this.conversion.IsNCharFixedWith();
    }

    @Override
    public short getDriverCharSet() {
        return this.conversion.getClientCharSet();
    }

    @Override
    public int getMaxCharSize() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 58).fillInStackTrace();
    }

    @Override
    public int getMaxCharbyteSize() {
        return this.conversion.getMaxCharbyteSize();
    }

    @Override
    public int getMaxNCharbyteSize() {
        return this.conversion.getMaxNCharbyteSize();
    }

    @Override
    public boolean isCharSetMultibyte(short s2) {
        return DBConversion.isCharSetMultibyte(s2);
    }

    @Override
    public int javaCharsToCHARBytes(char[] cArray, int n2, byte[] byArray) throws SQLException {
        return this.conversion.javaCharsToCHARBytes(cArray, n2, byArray);
    }

    @Override
    public int javaCharsToNCHARBytes(char[] cArray, int n2, byte[] byArray) throws SQLException {
        return this.conversion.javaCharsToNCHARBytes(cArray, n2, byArray);
    }

    @Override
    public abstract void getPropertyForPooledConnection(OraclePooledConnection var1) throws SQLException;

    final void getPropertyForPooledConnection(OraclePooledConnection oraclePooledConnection, @Blind String string) throws SQLException {
        Hashtable hashtable = new Hashtable();
        hashtable.put("obj_type_map", this.javaObjectMap);
        Properties properties = new Properties();
        properties.put("user", this.userName);
        properties.put("password", string);
        properties.put("connection_url", this.url);
        properties.put("connect_auto_commit", "" + this.autocommit);
        properties.put("trans_isolation", "" + this.txnLevel);
        if (this.getStatementCacheSize() != -1) {
            properties.put("stmt_cache_size", "" + this.getStatementCacheSize());
            properties.put("implicit_cache_enabled", "" + this.getImplicitCachingEnabled());
            properties.put("explict_cache_enabled", "" + this.getExplicitCachingEnabled());
        }
        properties.put("defaultExecuteBatch", "" + this.defaultExecuteBatch);
        properties.put("defaultRowPrefetch", "" + this.defaultRowPrefetch);
        properties.put("remarksReporting", "" + this.reportRemarks);
        properties.put("AccumulateBatchResult", "" + this.accumulateBatchResult);
        properties.put("oracle.jdbc.J2EE13Compliant", "" + this.j2ee13Compliant);
        properties.put("processEscapes", "" + this.processEscapes);
        properties.put("restrictGetTables", "" + this.restrictGettables);
        properties.put("includeSynonyms", "" + this.includeSynonyms);
        properties.put("fixedString", "" + this.fixedString);
        hashtable.put("connection_properties", properties);
        oraclePooledConnection.setProperties(hashtable);
    }

    @Override
    public Properties getDBAccessProperties() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getDBAccessProperties").fillInStackTrace();
    }

    @Override
    public Properties getOCIHandles() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getOCIHandles").fillInStackTrace();
    }

    abstract void logon(AbstractConnectionBuilder<?, ?> var1) throws SQLException;

    abstract CompletionStage<Void> logonAsync(AbstractConnectionBuilder<?, ?> var1);

    void logoff() throws SQLException {
    }

    abstract void open(OracleStatement var1) throws SQLException;

    abstract void cancelOperationOnServer(boolean var1) throws SQLException;

    abstract void doSetAutoCommit(boolean var1) throws SQLException;

    abstract void doCommit(int var1) throws SQLException;

    abstract void doRollback() throws SQLException;

    abstract String doGetDatabaseProductVersion() throws SQLException;

    abstract short doGetVersionNumber() throws SQLException;

    int getDefaultStreamChunkSize() {
        return 32768;
    }

    abstract OracleStatement RefCursorBytesToStatement(byte[] var1, OracleStatement var2) throws SQLException;

    abstract OracleStatement createImplicitResultSetStatement(OracleStatement var1) throws SQLException;

    @Override
    public oracle.jdbc.internal.OracleStatement refCursorCursorToStatement(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("refCursorCursorToStatement").fillInStackTrace();
    }

    @Override
    public Connection getLogicalConnection(OraclePooledConnection oraclePooledConnection, boolean bl) throws SQLException {
        if (this.logicalConnectionAttached != null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 143).fillInStackTrace();
        }
        LogicalConnection logicalConnection = new LogicalConnection(oraclePooledConnection, this, bl);
        this.dmsLogicalConnection.update(logicalConnection);
        this.logicalConnectionAttached = logicalConnection;
        return logicalConnection;
    }

    @Override
    public void getForm(OracleTypeADT oracleTypeADT, OracleTypeCLOB oracleTypeCLOB, int n2) throws SQLException {
    }

    @Override
    public CLOB createClob(byte[] byArray) throws SQLException {
        CLOB cLOB = new CLOB(this, byArray);
        if (cLOB.isNCLOB()) {
            cLOB = new NCLOB(cLOB);
        }
        return cLOB;
    }

    @Override
    public CLOB createClobWithUnpickledBytes(byte[] byArray) throws SQLException {
        CLOB cLOB = new CLOB((oracle.jdbc.OracleConnection)this, byArray, true);
        if (cLOB.isNCLOB()) {
            cLOB = new NCLOB(cLOB);
        }
        return cLOB;
    }

    @Override
    public CLOB createClob(byte[] byArray, short s2) throws SQLException {
        if (s2 == 2) {
            return new NCLOB(this, byArray);
        }
        return new CLOB((oracle.jdbc.OracleConnection)this, byArray, s2);
    }

    @Override
    public BLOB createBlob(byte[] byArray) throws SQLException {
        return new BLOB(this, byArray);
    }

    @Override
    public BLOB createBlobWithUnpickledBytes(byte[] byArray) throws SQLException {
        return new BLOB(this, byArray, true);
    }

    @Override
    public BFILE createBfile(byte[] byArray) throws SQLException {
        return new BFILE(this, byArray);
    }

    @Override
    public ARRAY createARRAY(String string, Object object) throws SQLException {
        ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor(string, (Connection)this);
        return new ARRAY(arrayDescriptor, this, object);
    }

    @Override
    public Array createOracleArray(String string, Object object) throws SQLException {
        return this.createARRAY(string, object);
    }

    @Override
    public BINARY_DOUBLE createBINARY_DOUBLE(double d2) throws SQLException {
        return new BINARY_DOUBLE(d2);
    }

    @Override
    public BINARY_FLOAT createBINARY_FLOAT(float f2) throws SQLException {
        return new BINARY_FLOAT(f2);
    }

    @Override
    public DATE createDATE(Date date) throws SQLException {
        return new DATE(date);
    }

    @Override
    public DATE createDATE(Time time) throws SQLException {
        return new DATE(time);
    }

    @Override
    public DATE createDATE(Timestamp timestamp) throws SQLException {
        return new DATE(timestamp);
    }

    @Override
    public DATE createDATE(Date date, Calendar calendar) throws SQLException {
        return new DATE(date, calendar);
    }

    @Override
    public DATE createDATE(Time time, Calendar calendar) throws SQLException {
        return new DATE(time, calendar);
    }

    @Override
    public DATE createDATE(Timestamp timestamp, Calendar calendar) throws SQLException {
        return new DATE(timestamp, calendar);
    }

    @Override
    public DATE createDATE(String string) throws SQLException {
        return new DATE(string);
    }

    @Override
    public INTERVALDS createINTERVALDS(String string) throws SQLException {
        return new INTERVALDS(string);
    }

    @Override
    public INTERVALYM createINTERVALYM(String string) throws SQLException {
        return new INTERVALYM(string);
    }

    @Override
    public NUMBER createNUMBER(boolean bl) throws SQLException {
        return new NUMBER(bl);
    }

    @Override
    public NUMBER createNUMBER(byte by) throws SQLException {
        return new NUMBER(by);
    }

    @Override
    public NUMBER createNUMBER(short s2) throws SQLException {
        return new NUMBER(s2);
    }

    @Override
    public NUMBER createNUMBER(int n2) throws SQLException {
        return new NUMBER(n2);
    }

    @Override
    public NUMBER createNUMBER(long l2) throws SQLException {
        return new NUMBER(l2);
    }

    @Override
    public NUMBER createNUMBER(float f2) throws SQLException {
        return new NUMBER(f2);
    }

    @Override
    public NUMBER createNUMBER(double d2) throws SQLException {
        return new NUMBER(d2);
    }

    @Override
    public NUMBER createNUMBER(BigDecimal bigDecimal) throws SQLException {
        return new NUMBER(bigDecimal);
    }

    @Override
    public NUMBER createNUMBER(BigInteger bigInteger) throws SQLException {
        return new NUMBER(bigInteger);
    }

    @Override
    public NUMBER createNUMBER(String string, int n2) throws SQLException {
        return new NUMBER(string, n2);
    }

    @Override
    public Array createArrayOf(String string, Object[] objectArray) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("createArrayOf").fillInStackTrace();
    }

    @Override
    public Struct createStruct(String string, Object[] objectArray) throws SQLException {
        try {
            StructDescriptor structDescriptor = StructDescriptor.createDescriptor(string, (Connection)this);
            return new STRUCT(structDescriptor, (Connection)this, objectArray);
        }
        catch (SQLException sQLException) {
            if (sQLException.getErrorCode() == 17049) {
                this.removeAllDescriptor();
            }
            throw sQLException;
        }
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Date date) throws SQLException {
        return new TIMESTAMP(date);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(DATE dATE) throws SQLException {
        return new TIMESTAMP(dATE);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Time time) throws SQLException {
        return new TIMESTAMP(time);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Timestamp timestamp) throws SQLException {
        return new TIMESTAMP(timestamp);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Timestamp timestamp, Calendar calendar) throws SQLException {
        return new TIMESTAMP(timestamp, calendar);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(String string) throws SQLException {
        return new TIMESTAMP(string);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Date date) throws SQLException {
        return new TIMESTAMPTZ((Connection)this, date);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Date date, Calendar calendar) throws SQLException {
        return new TIMESTAMPTZ((Connection)this, date, calendar);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Time time) throws SQLException {
        return new TIMESTAMPTZ((Connection)this, time);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Time time, Calendar calendar) throws SQLException {
        return new TIMESTAMPTZ((Connection)this, time, calendar);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp timestamp) throws SQLException {
        return new TIMESTAMPTZ((Connection)this, timestamp);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp timestamp, Calendar calendar) throws SQLException {
        return new TIMESTAMPTZ((Connection)this, timestamp, calendar);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp timestamp, ZoneId zoneId) throws SQLException {
        return new TIMESTAMPTZ((Connection)this, timestamp, zoneId);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(String string) throws SQLException {
        return new TIMESTAMPTZ((Connection)this, string);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(String string, Calendar calendar) throws SQLException {
        return new TIMESTAMPTZ((Connection)this, string, calendar);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(DATE dATE) throws SQLException {
        return new TIMESTAMPTZ((Connection)this, dATE);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(Date date, Calendar calendar) throws SQLException {
        return new TIMESTAMPLTZ((Connection)this, calendar, date);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(Time time, Calendar calendar) throws SQLException {
        return new TIMESTAMPLTZ((Connection)this, calendar, time);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(Timestamp timestamp, Calendar calendar) throws SQLException {
        return new TIMESTAMPLTZ((Connection)this, calendar, timestamp);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(String string, Calendar calendar) throws SQLException {
        return new TIMESTAMPLTZ((Connection)this, calendar, string);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(DATE dATE, Calendar calendar) throws SQLException {
        return new TIMESTAMPLTZ((Connection)this, calendar, dATE);
    }

    public abstract BLOB createTemporaryBlob(Connection var1, boolean var2, int var3) throws SQLException;

    public abstract CLOB createTemporaryClob(Connection var1, boolean var2, int var3, short var4) throws SQLException;

    @Override
    public Blob createBlob() throws SQLException {
        this.requireOpenConnection();
        return this.createTemporaryBlob(this, true, 10);
    }

    @Override
    public Clob createClob() throws SQLException {
        this.requireOpenConnection();
        return this.createTemporaryClob(this, true, 10, (short)1);
    }

    @Override
    public NClob createNClob() throws SQLException {
        this.requireOpenConnection();
        return (NClob)((Object)this.createTemporaryClob(this, true, 10, (short)2));
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        this.requireOpenConnection();
        return new XMLType((Connection)this, (String)null);
    }

    @Override
    public boolean isDescriptorSharable(oracle.jdbc.internal.OracleConnection oracleConnection) throws SQLException {
        PhysicalConnection physicalConnection = this;
        PhysicalConnection physicalConnection2 = (PhysicalConnection)oracleConnection.getPhysicalConnection();
        return physicalConnection == physicalConnection2 || physicalConnection.url.equals(physicalConnection2.url) || physicalConnection2.protocol != null && physicalConnection2.protocol.equals("kprb");
    }

    boolean useLittleEndianSetCHARBinder() throws SQLException {
        return false;
    }

    @Override
    public void setPlsqlWarnings(String string) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        if (string != null && (string = string.trim()).length() > 0 && !OracleSql.isValidPlsqlWarning(string)) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        try (Statement statement = this.createStatement(-1, -1);){
            statement.execute("ALTER SESSION SET PLSQL_WARNINGS=" + string);
            boolean bl = this.plsqlCompilerWarnings = !string.equals("'DISABLE:ALL'");
            if (this.plsqlCompilerWarnings) {
                try {
                    statement.executeQuery("ALTER SESSION SET EVENTS='10933 TRACE NAME CONTEXT LEVEL 32768'");
                }
                catch (SQLException sQLException) {
                }
            }
        }
    }

    void internalClose() throws SQLException {
        OracleStatement oracleStatement;
        this.lifecycle = 4;
        OracleStatement oracleStatement2 = this.statements;
        while (oracleStatement2 != null) {
            oracleStatement = oracleStatement2.nextChild;
            if (oracleStatement2.serverCursor) {
                oracleStatement2.internalClose();
                this.removeStatement(oracleStatement2);
            }
            oracleStatement2 = oracleStatement;
        }
        oracleStatement2 = this.statements;
        while (oracleStatement2 != null) {
            oracleStatement = oracleStatement2.next;
            oracleStatement2.internalClose();
            oracleStatement2 = oracleStatement;
        }
        this.statements = null;
    }

    @Override
    public XAResource getXAResource() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 164).fillInStackTrace();
    }

    protected void doDescribeTable(AutoKeyInfo autoKeyInfo) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doDescribeTable").fillInStackTrace();
    }

    @Override
    public void setClientInfo(String string, String string2) throws SQLClientInfoException {
        if (this.lifecycle != 1) {
            throw (SQLClientInfoException)DatabaseError.createSQLClientInfoException(8, null, null).fillInStackTrace();
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            OracleSQLPermission oracleSQLPermission = new OracleSQLPermission(SETCLIENTINFO_PERMISSION_NAME + string);
            securityManager.checkPermission(oracleSQLPermission);
        }
        this.setClientInfoInternal(string, string2);
    }

    @Override
    public void setClientInfo(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLClientInfoException {
        Serializable serializable;
        if (this.lifecycle != 1) {
            throw (SQLClientInfoException)DatabaseError.createSQLClientInfoException(8, null, null).fillInStackTrace();
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            serializable = new OracleSQLPermission("clientInfo.*");
            securityManager.checkPermission((Permission)serializable);
        }
        this.clientInfo.clear();
        serializable = new HashMap();
        for (String string : properties.stringPropertyNames()) {
            String string2 = properties.getProperty(string);
            try {
                this.setClientInfoInternal(string, string2);
            }
            catch (SQLClientInfoException sQLClientInfoException) {
                serializable.put(string, ClientInfoStatus.REASON_UNKNOWN_PROPERTY);
            }
        }
        if (!serializable.isEmpty()) {
            throw (SQLClientInfoException)DatabaseError.createSQLClientInfoException(253, (Map<String, ClientInfoStatus>)((Object)serializable), null).fillInStackTrace();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    void setClientInfoInternal(String string, String string2) throws SQLClientInfoException {
        if (!SUPPORTED_NAME_PATTERN.matcher(string).matches()) {
            throw (SQLClientInfoException)DatabaseError.createSQLClientInfoException(253, null, null).fillInStackTrace();
        }
        String[] stringArray = string.split("\\.", 2);
        if (RESERVED_NAMESPACES.contains(stringArray[0])) {
            throw (SQLClientInfoException)DatabaseError.createSQLClientInfoException(276, null, null).fillInStackTrace();
        }
        String string3 = stringArray[0];
        String string4 = stringArray[1];
        try {
            if (string3.equals("OCSID")) {
                if (string4.equals("ACTION")) {
                    if (string2 != null && string2.length() > this.endToEndMaxLength[0]) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 159, string2).fillInStackTrace();
                    }
                    if (string2 == null && this.endToEndValues[0] != null || string2 != null) {
                        this.endToEndValues[0] = string2;
                        this.endToEndHasChanged[0] = true;
                        this.endToEndAnyChanged = true;
                    }
                } else if (string4.equals("CLIENTID")) {
                    if (string2 != null && string2.length() > this.endToEndMaxLength[1]) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 159, string2).fillInStackTrace();
                    }
                    if (string2 == null && this.endToEndValues[1] != null || string2 != null) {
                        this.endToEndValues[1] = string2;
                        this.endToEndHasChanged[1] = true;
                        this.endToEndAnyChanged = true;
                    }
                } else if (string4.equals("ECID")) {
                    if (string2 != null && string2.length() > this.endToEndMaxLength[2]) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 159, string2).fillInStackTrace();
                    }
                    if (string2 == null && this.endToEndValues[2] != null || string2 != null) {
                        this.endToEndValues[2] = string2;
                        this.endToEndHasChanged[2] = true;
                        this.endToEndAnyChanged = true;
                    }
                } else if (string4.equals("MODULE")) {
                    if (string2 != null && string2.length() > this.endToEndMaxLength[3]) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 159, string2).fillInStackTrace();
                    }
                    if (string2 == null && this.endToEndValues[3] != null || string2 != null) {
                        this.endToEndValues[3] = string2;
                        this.endToEndHasChanged[3] = true;
                        this.endToEndHasChanged[0] = true;
                        this.endToEndAnyChanged = true;
                    }
                } else if (string4.equals("SEQUENCE_NUMBER")) {
                    short s2 = 0;
                    if (string2 != null) {
                        try {
                            s2 = Short.valueOf(string2);
                        }
                        catch (NumberFormatException numberFormatException) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 160, string2).fillInStackTrace();
                        }
                    }
                    this.endToEndECIDSequenceNumber = s2;
                    this.endToEndAnyChanged = true;
                } else if (string4.equals("DBOP")) {
                    if (string2 != null && string2.length() > this.endToEndMaxLength[4]) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 159, string2).fillInStackTrace();
                    }
                    if (string2 == null && this.endToEndValues[4] != null || string2 != null) {
                        this.endToEndValues[4] = string2;
                        this.endToEndHasChanged[4] = true;
                        this.endToEndAnyChanged = true;
                    }
                } else {
                    if (!string4.equals("CLIENT_INFO")) throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 253, "OCSID." + string4).fillInStackTrace();
                    if (string2 != null && string2.length() > this.endToEndMaxLength[5]) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 159, string2).fillInStackTrace();
                    }
                    if (string2 == null && this.endToEndValues[5] != null || string2 != null) {
                        this.endToEndValues[5] = string2;
                        this.endToEndHasChanged[5] = true;
                        this.endToEndAnyChanged = true;
                    }
                }
            } else {
                if (string4.length() > 30) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 171).fillInStackTrace();
                }
                if (string2 != null && string2.length() > 4000) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 172).fillInStackTrace();
                }
                this.doSetApplicationContext(string3, string4, string2 == null ? "" : string2);
            }
            if (string2 == null) {
                this.clientInfo.remove(string);
                return;
            } else {
                this.clientInfo.put(string, string2);
            }
            return;
        }
        catch (SQLException sQLException) {
            throw (SQLClientInfoException)DatabaseError.createSQLClientInfoException(1, null, sQLException).fillInStackTrace();
        }
    }

    @Override
    public String getClientInfo(String string) throws SQLException {
        this.requireOpenConnection();
        if (string == null) {
            return null;
        }
        if (!SUPPORTED_NAME_PATTERN.matcher(string).matches()) {
            throw (SQLClientInfoException)DatabaseError.createSQLClientInfoException(253, null, null).fillInStackTrace();
        }
        return this.clientInfo.getProperty(string);
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getClientInfo() throws SQLException {
        this.requireOpenConnection();
        return (Properties)this.clientInfo.clone();
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getClientInfoInternal() throws SQLException {
        return (Properties)this.clientInfo.clone();
    }

    @Override
    public void setApplicationContext(String string, String string2, String string3) throws SQLException {
        if (string2 == null) {
            throw new NullPointerException();
        }
        if (string == null || string.equals("")) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 170).fillInStackTrace();
        }
        this.setClientInfoInternal(string + "." + string2, string3);
    }

    void doSetApplicationContext(String string, String string2, String string3) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doSetApplicationContext").fillInStackTrace();
    }

    @Override
    public void clearAllApplicationContext(String string) throws SQLException {
        if (string == null) {
            throw new NullPointerException();
        }
        if (string.equals("")) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 170).fillInStackTrace();
        }
        this.doClearAllApplicationContext(string);
        if (!string.equals("OCSID")) {
            for (String string2 : this.clientInfo.stringPropertyNames()) {
                if (!string2.startsWith(string + ".")) continue;
                this.clientInfo.remove(string2);
            }
        }
    }

    void doClearAllApplicationContext(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doClearAllApplicationContext").fillInStackTrace();
    }

    @Override
    public byte[] createLightweightSession(String string, KeywordValueLong[] keywordValueLongArray, int n2, KeywordValueLong[][] keywordValueLongArray2, int[] nArray) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("createLightweightSession").fillInStackTrace();
    }

    void executeLightweightSessionRoundtrip(int n2, byte[] byArray, KeywordValueLong[] keywordValueLongArray, int n3, KeywordValueLong[][] keywordValueLongArray2, int[] nArray) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("executeLightweightSessionRoundtrip").fillInStackTrace();
    }

    @Override
    public void executeLightweightSessionPiggyback(int n2, byte[] byArray, KeywordValueLong[] keywordValueLongArray, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("executeLightweightSessionPiggyback").fillInStackTrace();
    }

    @Override
    public void doXSNamespaceOp(OracleConnection.XSOperationCode xSOperationCode, byte[] byArray, XSNamespace[] xSNamespaceArray, XSNamespace[][] xSNamespaceArray2, XSSecureId xSSecureId) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doXSNamespaceOp").fillInStackTrace();
    }

    @Override
    public void doXSNamespaceOp(OracleConnection.XSOperationCode xSOperationCode, byte[] byArray, XSNamespace[] xSNamespaceArray, XSSecureId xSSecureId) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doXSNamespaceOp").fillInStackTrace();
    }

    @Override
    public byte[] doXSSessionCreateOp(OracleConnection.XSSessionOperationCode xSSessionOperationCode, XSSecureId xSSecureId, byte[] byArray, XSPrincipal xSPrincipal, String string, XSNamespace[] xSNamespaceArray, OracleConnection.XSSessionModeFlag xSSessionModeFlag, XSKeyval xSKeyval) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doXSSessionCreateOp").fillInStackTrace();
    }

    @Override
    public void doXSSessionDestroyOp(byte[] byArray, XSSecureId xSSecureId, byte[] byArray2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doXSSessionDestroyOp").fillInStackTrace();
    }

    @Override
    public void doXSSessionDetachOp(int n2, byte[] byArray, XSSecureId xSSecureId, boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doXSSessionDetachOp").fillInStackTrace();
    }

    @Override
    public void doXSSessionChangeOp(OracleConnection.XSSessionSetOperationCode xSSessionSetOperationCode, byte[] byArray, XSSecureId xSSecureId, XSSessionParameters xSSessionParameters) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doXSSessionChangeOp").fillInStackTrace();
    }

    @Override
    public void doXSSessionAttachOp(int n2, byte[] byArray, XSSecureId xSSecureId, byte[] byArray2, XSPrincipal xSPrincipal, String[] stringArray, String[] stringArray2, String[] stringArray3, XSNamespace[] xSNamespaceArray, XSNamespace[] xSNamespaceArray2, XSNamespace[] xSNamespaceArray3, TIMESTAMPTZ tIMESTAMPTZ, TIMESTAMPTZ tIMESTAMPTZ2, int n3, long l2, XSKeyval xSKeyval, int[] nArray) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doXSSessionAttachOp").fillInStackTrace();
    }

    @Override
    public void enqueue(String string, AQEnqueueOptions aQEnqueueOptions, AQMessage aQMessage) throws SQLException {
        AQMessageI aQMessageI = (AQMessageI)aQMessage;
        byte[][] byArrayArray = new byte[1][];
        try {
            this.updateSystemContext();
            this.doEnqueue(string, aQEnqueueOptions, aQMessageI.getMessagePropertiesI(), aQMessageI.getPayloadTOID(), aQMessageI.getPayloadVersion(), aQMessageI.getPayload(), byArrayArray, aQMessageI.isRAWPayload());
        }
        catch (SQLException sQLException) {
            this.resetSystemContext();
            throw sQLException;
        }
        if (byArrayArray[0] != null) {
            aQMessageI.setMessageId(byArrayArray[0]);
        }
    }

    @Override
    public AQMessage dequeue(String string, AQDequeueOptions aQDequeueOptions, byte[] byArray) throws SQLException {
        String string2 = OracleTypeADT.toid2typename(this, byArray);
        return this.dequeue(string, aQDequeueOptions, string2);
    }

    @Override
    public AQMessage dequeue(String string, AQDequeueOptions aQDequeueOptions, byte[] byArray, int n2) throws SQLException {
        byte[][] byArrayArray = new byte[1][];
        AQMessagePropertiesI aQMessagePropertiesI = new AQMessagePropertiesI();
        byte[][] byArrayArray2 = new byte[1][];
        boolean bl = false;
        try {
            this.updateSystemContext();
            bl = this.doDequeue(string, aQDequeueOptions, aQMessagePropertiesI, byArray, n2, byArrayArray2, byArrayArray, AQMessageI.compareToid(byArray, TypeDescriptor.RAWTOID));
        }
        catch (SQLException sQLException) {
            this.resetSystemContext();
            throw sQLException;
        }
        AQMessageI aQMessageI = null;
        if (bl) {
            aQMessageI = new AQMessageI(aQMessagePropertiesI, this);
            aQMessageI.setPayload(byArrayArray2[0], byArray);
            aQMessageI.setMessageId(byArrayArray[0]);
        }
        return aQMessageI;
    }

    @Override
    public AQMessage dequeue(String string, AQDequeueOptions aQDequeueOptions, String string2) throws SQLException {
        byte[] byArray = null;
        int n2 = 1;
        TypeDescriptor typeDescriptor = null;
        if (RAW_STR.equals(string2) || SYS_RAW_STR.equals(string2)) {
            byArray = TypeDescriptor.RAWTOID;
        } else if (SYS_ANYDATA_STR.equals(string2)) {
            byArray = TypeDescriptor.ANYDATATOID;
        } else if (SYS_XMLTYPE_STR.equals(string2)) {
            byArray = TypeDescriptor.XMLTYPETOID;
        } else if (JSON_STR.equals(string2)) {
            byArray = TypeDescriptor.JSONTOID;
        } else {
            typeDescriptor = TypeDescriptor.getTypeDescriptor(string2, this);
            byArray = ((OracleTypeADT)typeDescriptor.getPickler()).getTOID();
            n2 = ((OracleTypeADT)typeDescriptor.getPickler()).getTypeVersion();
        }
        AQMessageI aQMessageI = (AQMessageI)this.dequeue(string, aQDequeueOptions, byArray, n2);
        if (aQMessageI != null) {
            aQMessageI.setTypeName(string2);
            aQMessageI.setTypeDescriptor(typeDescriptor);
        }
        return aQMessageI;
    }

    @Override
    public int enqueue(String string, AQEnqueueOptions aQEnqueueOptions, AQMessage[] aQMessageArray) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("enqueue[]").fillInStackTrace();
    }

    @Override
    public AQMessage[] dequeue(String string, AQDequeueOptions aQDequeueOptions, String string2, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("dequeue[]").fillInStackTrace();
    }

    @Override
    public AQMessage[] dequeue(String string, AQDequeueOptions aQDequeueOptions, byte[] byArray, int n2, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("dequeue[]").fillInStackTrace();
    }

    void doEnqueue(String string, AQEnqueueOptions aQEnqueueOptions, AQMessagePropertiesI aQMessagePropertiesI, byte[] byArray, int n2, byte[] byArray2, byte[][] byArray3, boolean bl) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doEnqueue").fillInStackTrace();
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

    boolean doDequeue(String string, AQDequeueOptions aQDequeueOptions, AQMessagePropertiesI aQMessagePropertiesI, byte[] byArray, int n2, byte[][] byArray2, byte[][] byArray3, boolean bl) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doDequeue").fillInStackTrace();
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

    @Override
    public void jmsEnqueue(String string, JMSEnqueueOptions jMSEnqueueOptions, JMSMessage jMSMessage, AQMessageProperties aQMessageProperties) throws SQLException {
        byte[][] byArrayArray = new byte[1][];
        AQMessagePropertiesI aQMessagePropertiesI = (AQMessagePropertiesI)aQMessageProperties;
        if (jMSMessage.getStreamPayload() != null) {
            this.doJMSEnqueue(string, jMSEnqueueOptions, aQMessagePropertiesI, jMSMessage.getJMSMessageProperties(), jMSMessage.getToid(), jMSMessage.getStreamPayload(), byArrayArray, jMSMessage.getChunkSize());
        } else {
            this.doJMSEnqueue(string, jMSEnqueueOptions, aQMessagePropertiesI, jMSMessage.getJMSMessageProperties(), jMSMessage.getToid(), jMSMessage.getPayload(), byArrayArray);
        }
        if (byArrayArray[0] != null) {
            jMSMessage.setMessageId(byArrayArray[0]);
        }
    }

    @Override
    public void jmsEnqueue(String string, JMSEnqueueOptions jMSEnqueueOptions, JMSMessage[] jMSMessageArray, AQMessageProperties[] aQMessagePropertiesArray) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("jmsEnqueue").fillInStackTrace();
    }

    void doJMSEnqueue(String string, JMSEnqueueOptions jMSEnqueueOptions, AQMessagePropertiesI aQMessagePropertiesI, JMSMessageProperties jMSMessageProperties, byte[] byArray, InputStream inputStream, byte[][] byArray2, int n2) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doJMSEnqueue").fillInStackTrace();
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

    void doJMSEnqueue(String string, JMSEnqueueOptions jMSEnqueueOptions, AQMessagePropertiesI aQMessagePropertiesI, JMSMessageProperties jMSMessageProperties, byte[] byArray, byte[] byArray2, byte[][] byArray3) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doJMSEnqueue").fillInStackTrace();
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

    @Override
    public JMSMessage jmsDequeue(String string, JMSDequeueOptions jMSDequeueOptions, String string2) throws SQLException {
        JMSMessage jMSMessage = this.jmsDequeue(string, jMSDequeueOptions);
        return jMSMessage;
    }

    @Override
    public JMSMessage jmsDequeue(String string, JMSDequeueOptions jMSDequeueOptions) throws SQLException {
        byte[] byArray = TypeDescriptor.RAWTOID;
        byte[][] byArrayArray = new byte[1][];
        AQMessagePropertiesI aQMessagePropertiesI = new AQMessagePropertiesI();
        JMSMessagePropertiesI jMSMessagePropertiesI = new JMSMessagePropertiesI();
        byte[][] byArrayArray2 = new byte[1][];
        boolean bl = false;
        bl = this.doJmsDequeue(string, jMSDequeueOptions, aQMessagePropertiesI, jMSMessagePropertiesI, byArray, byArrayArray2, (byte[][])byArrayArray);
        JMSMessage jMSMessage = null;
        if (bl) {
            jMSMessage = JMSFactory.createJMSMessage(jMSMessagePropertiesI);
            jMSMessage.setPayload(byArrayArray2[0]);
            jMSMessage.setMessageId(byArrayArray[0]);
            jMSMessage.setJMSMessageProperties(jMSMessagePropertiesI);
            jMSMessage.setAQMessageProperties(aQMessagePropertiesI);
        }
        return jMSMessage;
    }

    @Override
    public JMSMessage[] jmsDequeue(String string, JMSDequeueOptions jMSDequeueOptions, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("jmsDequeue").fillInStackTrace();
    }

    @Override
    public JMSMessage jmsDequeue(String string, JMSDequeueOptions jMSDequeueOptions, OutputStream outputStream) throws SQLException {
        byte[] byArray = TypeDescriptor.RAWTOID;
        byte[][] byArrayArray = new byte[1][];
        AQMessagePropertiesI aQMessagePropertiesI = new AQMessagePropertiesI();
        JMSMessagePropertiesI jMSMessagePropertiesI = new JMSMessagePropertiesI();
        boolean bl = false;
        bl = this.doJmsDequeue(string, jMSDequeueOptions, aQMessagePropertiesI, jMSMessagePropertiesI, byArray, outputStream, (byte[][])byArrayArray);
        JMSMessage jMSMessage = null;
        if (bl) {
            jMSMessage = JMSFactory.createJMSMessage(jMSMessagePropertiesI);
            jMSMessage.setMessageId(byArrayArray[0]);
            jMSMessage.setJMSMessageProperties(jMSMessagePropertiesI);
            jMSMessage.setAQMessageProperties(aQMessagePropertiesI);
        }
        return jMSMessage;
    }

    boolean doJmsDequeue(String string, JMSDequeueOptions jMSDequeueOptions, AQMessagePropertiesI aQMessagePropertiesI, JMSMessagePropertiesI jMSMessagePropertiesI, byte[] byArray, OutputStream outputStream, byte[][] byArray2) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doJmsDequeue").fillInStackTrace();
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

    boolean doJmsDequeue(String string, JMSDequeueOptions jMSDequeueOptions, AQMessagePropertiesI aQMessagePropertiesI, JMSMessagePropertiesI jMSMessagePropertiesI, byte[] byArray, byte[][] byArray2, byte[][] byArray3) throws SQLException {
        Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doJmsDequeue").fillInStackTrace();
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

    @Override
    @Deprecated
    public boolean isV8Compatible() throws SQLException {
        return this.mapDateToTimestamp;
    }

    @Override
    public boolean getMapDateToTimestamp() {
        return this.mapDateToTimestamp;
    }

    @Override
    public byte getInstanceProperty(OracleConnection.InstanceProperty instanceProperty) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getInstanceProperty").fillInStackTrace();
    }

    @Override
    public Map<String, JMSNotificationRegistration> registerJMSNotification(String[] stringArray, Map<String, Properties> map, String string) throws SQLException {
        if (stringArray == null || map == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "name and options cannot be null").fillInStackTrace();
        }
        Map<String, JMSNotificationRegistration> map2 = this.doRegisterJMSNotification(stringArray, map, string);
        return map2;
    }

    @Override
    public Map<String, JMSNotificationRegistration> registerJMSNotification(String[] stringArray, Map<String, Properties> map) throws SQLException {
        if (stringArray == null || map == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "name and options cannot be null").fillInStackTrace();
        }
        Map<String, JMSNotificationRegistration> map2 = this.doRegisterJMSNotification(stringArray, map, null);
        return map2;
    }

    Map<String, JMSNotificationRegistration> doRegisterJMSNotification(String[] stringArray, Map<String, Properties> map, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("JMSNotificationRegistration").fillInStackTrace();
    }

    @Override
    public void unregisterJMSNotification(JMSNotificationRegistration jMSNotificationRegistration) throws SQLException {
        if (jMSNotificationRegistration == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "registration cannot be null").fillInStackTrace();
        }
        NTFJMSRegistration nTFJMSRegistration = (NTFJMSRegistration)jMSNotificationRegistration;
        this.doUnregisterJMSNotification(nTFJMSRegistration);
    }

    @Override
    public void startJMSNotification(JMSNotificationRegistration jMSNotificationRegistration) throws SQLException {
        if (jMSNotificationRegistration == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "registration cannot be null").fillInStackTrace();
        }
        NTFJMSRegistration nTFJMSRegistration = (NTFJMSRegistration)jMSNotificationRegistration;
        this.doStartJMSNotification(nTFJMSRegistration);
    }

    @Override
    public void stopJMSNotification(JMSNotificationRegistration jMSNotificationRegistration) throws SQLException {
        if (jMSNotificationRegistration == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "registration cannot be null").fillInStackTrace();
        }
        NTFJMSRegistration nTFJMSRegistration = (NTFJMSRegistration)jMSNotificationRegistration;
        this.doStopJMSNotification(nTFJMSRegistration);
    }

    void doUnregisterJMSNotification(NTFJMSRegistration nTFJMSRegistration) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doUnregisterJMSNotification").fillInStackTrace();
    }

    void doStartJMSNotification(NTFJMSRegistration nTFJMSRegistration) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doStartJMSNotification").fillInStackTrace();
    }

    void doStopJMSNotification(NTFJMSRegistration nTFJMSRegistration) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doStopJMSNotification").fillInStackTrace();
    }

    @Override
    public void ackJMSNotification(JMSNotificationRegistration jMSNotificationRegistration, byte[] byArray, JMSNotificationRegistration.Directive directive) throws SQLException {
        if (directive == null || jMSNotificationRegistration == null || byArray == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "directive,registration or lastMessageID cannot be null").fillInStackTrace();
        }
        short s2 = directive.getCode();
        ArrayList<JMSNotificationRegistration> arrayList = new ArrayList<JMSNotificationRegistration>(1);
        arrayList.add(jMSNotificationRegistration);
        byte[][] byArrayArray = new byte[][]{byArray};
        this.doAckJMSNtfn(arrayList, byArrayArray, s2);
    }

    @Override
    public void ackJMSNotification(ArrayList<JMSNotificationRegistration> arrayList, byte[][] byArray, JMSNotificationRegistration.Directive directive) throws SQLException {
        if (directive == null || arrayList == null || byArray == null) {
            throw (SQLException)DatabaseError.createSqlException(68, "directive,registration or lastMessageID cannot be null").fillInStackTrace();
        }
        short s2 = directive.getCode();
        this.doAckJMSNtfn(arrayList, byArray, s2);
    }

    void doAckJMSNtfn(ArrayList<JMSNotificationRegistration> arrayList, byte[][] byArray, short s2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doAckJMSNtfn").fillInStackTrace();
    }

    @Override
    public AQNotificationRegistration[] registerAQNotification(String[] stringArray, Properties[] propertiesArray, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        String string = this.readNTFlocalhost(properties);
        int n2 = this.readNTFtcpport(properties);
        boolean bl = this.readNTFuseSSL(properties);
        NTFAQRegistration[] nTFAQRegistrationArray = this.doRegisterAQNotification(stringArray, string, n2, bl, propertiesArray);
        return nTFAQRegistrationArray;
    }

    NTFAQRegistration[] doRegisterAQNotification(String[] stringArray, String string, int n2, boolean bl, Properties[] propertiesArray) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doRegisterAQNotification").fillInStackTrace();
    }

    @Override
    public void unregisterAQNotification(AQNotificationRegistration aQNotificationRegistration) throws SQLException {
        NTFAQRegistration nTFAQRegistration = (NTFAQRegistration)aQNotificationRegistration;
        this.doUnregisterAQNotification(nTFAQRegistration);
    }

    void doUnregisterAQNotification(NTFAQRegistration nTFAQRegistration) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doUnregisterAQNotification").fillInStackTrace();
    }

    private String readNTFlocalhost(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        String string = null;
        try {
            string = properties.getProperty("NTF_LOCAL_HOST", InetAddress.getLocalHost().getHostAddress());
        }
        catch (UnknownHostException unknownHostException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 240).fillInStackTrace();
        }
        catch (SecurityException securityException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 241).fillInStackTrace();
        }
        return string;
    }

    private int readNTFtcpport(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        int n2 = 0;
        try {
            n2 = Integer.parseInt(properties.getProperty("NTF_LOCAL_TCP_PORT", "0"));
            if (n2 < 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 242).fillInStackTrace();
            }
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 242).fillInStackTrace();
        }
        return n2;
    }

    int readNTFtimeout(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        int n2 = 0;
        try {
            n2 = Integer.parseInt(properties.getProperty("NTF_TIMEOUT", "0"));
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 243).fillInStackTrace();
        }
        return n2;
    }

    boolean readNTFuseSSL(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        boolean bl = false;
        bl = Boolean.parseBoolean(properties.getProperty("NTF_USE_SSL", "false"));
        return bl;
    }

    @Override
    public DatabaseChangeRegistration registerDatabaseChangeNotification(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        String string = this.readNTFlocalhost(properties);
        int n2 = this.readNTFtcpport(properties);
        int n3 = this.readNTFtimeout(properties);
        int n4 = 0;
        try {
            n4 = Integer.parseInt(properties.getProperty("DCN_NOTIFY_CHANGELAG", "0"));
        }
        catch (NumberFormatException numberFormatException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 244).fillInStackTrace();
        }
        NTFDCNRegistration nTFDCNRegistration = this.doRegisterDatabaseChangeNotification(string, n2, properties, n3, n4);
        ntfManager.addRegistration(nTFDCNRegistration);
        return nTFDCNRegistration;
    }

    NTFDCNRegistration doRegisterDatabaseChangeNotification(String string, int n2, @Blind(value=PropertiesBlinder.class) Properties properties, int n3, int n4) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doRegisterDatabaseChangeNotification").fillInStackTrace();
    }

    @Override
    public DatabaseChangeRegistration getDatabaseChangeRegistration(int n2) throws SQLException {
        NTFDCNRegistration nTFDCNRegistration = new NTFDCNRegistration(this.dbName, n2, this.userName, this.versionNumber);
        return nTFDCNRegistration;
    }

    @Override
    public void unregisterDatabaseChangeNotification(DatabaseChangeRegistration databaseChangeRegistration) throws SQLException {
        NTFDCNRegistration nTFDCNRegistration = (NTFDCNRegistration)databaseChangeRegistration;
        if (nTFDCNRegistration.getDatabaseName().compareToIgnoreCase(this.dbName) != 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 245).fillInStackTrace();
        }
        this.doUnregisterDatabaseChangeNotification(nTFDCNRegistration);
    }

    void doUnregisterDatabaseChangeNotification(NTFDCNRegistration nTFDCNRegistration) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doUnregisterDatabaseChangeNotification").fillInStackTrace();
    }

    @Override
    public void unregisterDatabaseChangeNotification(int n2) throws SQLException {
        String string = null;
        try {
            string = InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.unregisterDatabaseChangeNotification(n2, string, 47632);
    }

    @Override
    public void unregisterDatabaseChangeNotification(int n2, String string, int n3) throws SQLException {
        String string2 = "(ADDRESS=(PROTOCOL=tcp)(HOST=" + string + ")(PORT=" + n3 + "))?PR=0";
        this.unregisterDatabaseChangeNotification(n2, string2);
    }

    @Override
    public void unregisterDatabaseChangeNotification(long l2, String string) throws SQLException {
        this.doUnregisterDatabaseChangeNotification(l2, string);
    }

    void doUnregisterDatabaseChangeNotification(long l2, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doUnregisterDatabaseChangeNotification").fillInStackTrace();
    }

    @Override
    public void addXSEventListener(XSEventListener xSEventListener) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("addXSEventListener").fillInStackTrace();
    }

    @Override
    public void addXSEventListener(XSEventListener xSEventListener, Executor executor) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("addXSEventListener").fillInStackTrace();
    }

    @Override
    public void removeXSEventListener(XSEventListener xSEventListener) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("removeXSEventListener").fillInStackTrace();
    }

    @Override
    public void removeAllXSEventListener() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("removeAllXSEventListener").fillInStackTrace();
    }

    @Override
    public void setPDBChangeEventListener(PDBChangeEventListener pDBChangeEventListener, Executor executor) throws SQLException {
        this.requireOpenConnection();
        NTFEventListener nTFEventListener = new NTFEventListener(pDBChangeEventListener);
        nTFEventListener.setExecutor(executor);
        if (this.pdbChangeListener != null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 248).fillInStackTrace();
        }
        this.pdbChangeListener = nTFEventListener;
    }

    @Override
    public void setPDBChangeEventListener(PDBChangeEventListener pDBChangeEventListener) throws SQLException {
        this.setPDBChangeEventListener(pDBChangeEventListener, null);
    }

    void notify(final NTFPDBChangeEvent nTFPDBChangeEvent) {
        if (this.pdbChangeListener != null) {
            Executor executor = this.pdbChangeListener.getExecutor();
            if (executor != null) {
                final PDBChangeEventListener pDBChangeEventListener = this.pdbChangeListener.getPDBChangeEventListener();
                executor.execute(new Runnable(){

                    @Override
                    public void run() {
                        pDBChangeEventListener.pdbChanged(nTFPDBChangeEvent);
                    }
                });
            } else {
                this.pdbChangeListener.getPDBChangeEventListener().pdbChanged(nTFPDBChangeEvent);
            }
        }
    }

    @Override
    public void addLogicalTransactionIdEventListener(LogicalTransactionIdEventListener logicalTransactionIdEventListener) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("addLogicalTransactionIdEventListener").fillInStackTrace();
    }

    @Override
    public void addLogicalTransactionIdEventListener(LogicalTransactionIdEventListener logicalTransactionIdEventListener, Executor executor) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("addLogicalTransactionIdEventListener").fillInStackTrace();
    }

    @Override
    public void removeLogicalTransactionIdEventListener(LogicalTransactionIdEventListener logicalTransactionIdEventListener) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("removeLogicalTransactionIdEventListener").fillInStackTrace();
    }

    @Override
    public LogicalTransactionId getLogicalTransactionId() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getLogicalTransactionId").fillInStackTrace();
    }

    @Override
    public TypeDescriptor[] getAllTypeDescriptorsInCurrentSchema() throws SQLException {
        TypeDescriptor[] typeDescriptorArray = null;
        Statement statement = null;
        this.beginNonRequestCalls();
        try {
            statement = this.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT schema_name, typename, typoid, typecode, version, tds  FROM TABLE(private_jdbc.Get_Type_Shape_Info())");
            typeDescriptorArray = this.getTypeDescriptorsFromResultSet(resultSet);
            resultSet.close();
        }
        catch (SQLException sQLException) {
            if (sQLException.getErrorCode() == 904) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 165).fillInStackTrace();
            }
            throw sQLException;
        }
        finally {
            if (statement != null) {
                statement.close();
            }
            this.endNonRequestCalls();
        }
        return typeDescriptorArray;
    }

    @Override
    public TypeDescriptor[] getTypeDescriptorsFromListInCurrentSchema(String[] stringArray) throws SQLException {
        String string = "SELECT schema_name, typename, typoid, typecode, version, tds  FROM TABLE(private_jdbc.Get_Type_Shape_Info(?))";
        TypeDescriptor[] typeDescriptorArray = null;
        PreparedStatement preparedStatement = null;
        this.beginNonRequestCalls();
        try {
            preparedStatement = this.prepareStatement(string);
            int n2 = stringArray.length;
            StringBuffer stringBuffer = new StringBuffer(n2 * 8);
            for (int i2 = 0; i2 < n2; ++i2) {
                stringBuffer.append(stringArray[i2]);
                if (i2 >= n2 - 1) continue;
                stringBuffer.append(',');
            }
            preparedStatement.setString(1, stringBuffer.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            typeDescriptorArray = this.getTypeDescriptorsFromResultSet(resultSet);
            resultSet.close();
        }
        catch (SQLException sQLException) {
            if (sQLException.getErrorCode() == 904) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 165).fillInStackTrace();
            }
            throw sQLException;
        }
        finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            this.endNonRequestCalls();
        }
        return typeDescriptorArray;
    }

    @Override
    public TypeDescriptor[] getTypeDescriptorsFromList(String[][] stringArray) throws SQLException {
        TypeDescriptor[] typeDescriptorArray = null;
        PreparedStatement preparedStatement = null;
        int n2 = stringArray.length;
        StringBuffer stringBuffer = new StringBuffer(n2 * 8);
        StringBuffer stringBuffer2 = new StringBuffer(n2 * 8);
        for (int i2 = 0; i2 < n2; ++i2) {
            stringBuffer.append(stringArray[i2][0]);
            stringBuffer2.append(stringArray[i2][1]);
            if (i2 >= n2 - 1) continue;
            stringBuffer.append(',');
            stringBuffer2.append(',');
        }
        this.beginNonRequestCalls();
        try {
            String string = "SELECT schema_name, typename, typoid, typecode, version, tds FROM TABLE(private_jdbc.Get_All_Type_Shape_Info(?,?))";
            preparedStatement = this.prepareStatement(string);
            preparedStatement.setString(1, stringBuffer.toString());
            preparedStatement.setString(2, stringBuffer2.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            typeDescriptorArray = this.getTypeDescriptorsFromResultSet(resultSet);
            resultSet.close();
        }
        catch (SQLException sQLException) {
            if (sQLException.getErrorCode() == 904) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 165).fillInStackTrace();
            }
            throw sQLException;
        }
        finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            this.endNonRequestCalls();
        }
        return typeDescriptorArray;
    }

    TypeDescriptor[] getTypeDescriptorsFromResultSet(ResultSet resultSet) throws SQLException {
        Object object;
        TypeDescriptor[] typeDescriptorArray;
        ArrayList<TypeDescriptor> arrayList = new ArrayList<TypeDescriptor>();
        while (resultSet.next()) {
            TypeDescriptor typeDescriptor;
            typeDescriptorArray = resultSet.getString(1);
            String string = resultSet.getString(2);
            object = resultSet.getBytes(3);
            String string2 = resultSet.getString(4);
            int n2 = resultSet.getInt(5);
            byte[] byArray = resultSet.getBytes(6);
            SQLName sQLName = new SQLName((String)typeDescriptorArray, string, this);
            if (string2.equals("OBJECT")) {
                typeDescriptor = StructDescriptor.createDescriptor(sQLName, object, n2, byArray, this);
                this.putDescriptor((byte[])object, (Object)typeDescriptor);
                this.putDescriptor(typeDescriptor.getName(), (Object)typeDescriptor);
                arrayList.add(typeDescriptor);
                continue;
            }
            if (!string2.equals("COLLECTION")) continue;
            typeDescriptor = ArrayDescriptor.createDescriptor(sQLName, object, n2, byArray, this);
            this.putDescriptor((byte[])object, (Object)typeDescriptor);
            this.putDescriptor(typeDescriptor.getName(), (Object)typeDescriptor);
            arrayList.add(typeDescriptor);
        }
        typeDescriptorArray = new TypeDescriptor[arrayList.size()];
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            object = (TypeDescriptor)arrayList.get(i2);
            typeDescriptorArray[i2] = object;
        }
        return typeDescriptorArray;
    }

    @Override
    public boolean isUsable() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.isUsable(true);
            return bl;
        }
    }

    @Override
    public boolean isUsable(boolean bl) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.isUsable && bl) {
                try {
                    if (this.checkAndDrain()) {
                        boolean bl2 = false;
                        return bl2;
                    }
                }
                catch (SQLException sQLException) {
                }
            }
            boolean bl3 = this.isUsable;
            return bl3;
        }
    }

    @Override
    public void setUsable(boolean bl) {
        this.isUsable = bl;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void queryFCFProperties(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        String string = "select sys_context('userenv', 'instance_name'),sys_context('userenv', 'server_host'),sys_context('userenv', 'service_name'),sys_context('userenv', 'db_unique_name') from dual";
        try {
            statement = this.createStatement();
            statement.setFetchSize(1);
            resultSet = statement.executeQuery(string);
            while (resultSet.next()) {
                String string2 = null;
                string2 = resultSet.getString(1);
                if (string2 != null) {
                    properties.put(INSTANCE_NAME, string2.trim());
                }
                if ((string2 = resultSet.getString(2)) != null) {
                    properties.put(SERVER_HOST, string2.trim());
                }
                if ((string2 = resultSet.getString(3)) != null) {
                    properties.put(SERVICE_NAME, string2.trim());
                }
                if ((string2 = resultSet.getString(4)) == null) continue;
                properties.put(DATABASE_NAME, string2.trim());
            }
        }
        finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    @Override
    public void setDefaultTimeZone(TimeZone timeZone) throws SQLException {
        this.defaultTimeZone = timeZone;
    }

    @Override
    public TimeZone getDefaultTimeZone() throws SQLException {
        return this.defaultTimeZone;
    }

    @Override
    public int getTimezoneVersionNumber() throws SQLException {
        return this.timeZoneVersionNumber;
    }

    @Override
    public TIMEZONETAB getTIMEZONETAB() throws SQLException {
        if (this.timeZoneTab == null) {
            this.timeZoneTab = TIMEZONETAB.getInstance(this.getTimezoneVersionNumber());
        }
        return this.timeZoneTab;
    }

    @Override
    public boolean isDataInLocatorEnabled() throws SQLException {
        return this.getVersionNumber() >= 10200 & this.getVersionNumber() < 11000 & this.enableReadDataInLocator | this.overrideEnableReadDataInLocator;
    }

    @Override
    public boolean isLobStreamPosStandardCompliant() throws SQLException {
        return this.lobStreamPosStandardCompliant;
    }

    @Override
    public long getCurrentSCN() throws SQLException {
        return this.doGetCurrentSCN();
    }

    long doGetCurrentSCN() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doGetCurrentSCN").fillInStackTrace();
    }

    void doSetSnapshotSCN(long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doSetSnapshotSCN").fillInStackTrace();
    }

    @Override
    public EnumSet<OracleConnection.TransactionState> getTransactionState() throws SQLException {
        return this.doGetTransactionState();
    }

    EnumSet<OracleConnection.TransactionState> doGetTransactionState() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doGetTransactionState").fillInStackTrace();
    }

    @Override
    public boolean isConnectionSocketKeepAlive() throws SocketException, SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("isConnectionSocketKeepAlive").fillInStackTrace();
    }

    @Override
    public void setReplayOperations(EnumSet<OracleConnection.ReplayOperation> enumSet) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setReplayOperations").fillInStackTrace();
    }

    @Override
    public void beginNonRequestCalls() throws SQLException {
    }

    @Override
    public void endNonRequestCalls() throws SQLException {
    }

    @Override
    public void setReplayContext(oracle.jdbc.internal.ReplayContext[] replayContextArray) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setReplayContext").fillInStackTrace();
    }

    @Override
    public void setReplayingMode(boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setReplayingMode").fillInStackTrace();
    }

    @Override
    public void registerEndReplayCallback(OracleConnection.EndReplayCallback endReplayCallback) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("registerEndReplayCallback").fillInStackTrace();
    }

    @Override
    public int getEOC() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getEOC").fillInStackTrace();
    }

    @Override
    public oracle.jdbc.internal.ReplayContext[] getReplayContext() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getReplayContext").fillInStackTrace();
    }

    @Override
    public oracle.jdbc.internal.ReplayContext getLastReplayContext() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getLastReplayContext").fillInStackTrace();
    }

    @Override
    public void setLastReplayContext(oracle.jdbc.internal.ReplayContext replayContext) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setLastReplayContext").fillInStackTrace();
    }

    @Override
    public byte[] getDerivedKeyInternal(byte[] byArray, int n2) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getDerivedKeyInternal").fillInStackTrace();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(CALL_ABORT_PERMISSION);
        }
        if (executor == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 284).fillInStackTrace();
        }
        final PhysicalConnection physicalConnection = this;
        if (physicalConnection.lifecycle == 4 || physicalConnection.lifecycle == 8) {
            return;
        }
        physicalConnection.lifecycle = 8;
        physicalConnection.doAbort();
        executor.execute(new Runnable(){

            @Override
            public void run() {
                try {
                    physicalConnection.close();
                }
                catch (Exception exception) {
                }
                finally {
                    PhysicalConnection.this.lifecycle = 4;
                }
            }
        });
    }

    @Override
    public String getSchema() throws SQLException {
        return this.getCurrentSchema();
    }

    @Override
    public final void setNetworkTimeout(Executor executor, int n2) throws SQLException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(CALL_SETNETWORKTIMEOUT_PERMISSION);
        }
        if (n2 < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 285).fillInStackTrace();
        }
        if (n2 > 0 && executor == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 284).fillInStackTrace();
        }
        this.closeExecutor = n2 == 0 ? null : executor;
        this.doSetNetworkTimeout(n2);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getNetworkTimeout").fillInStackTrace();
    }

    protected void doSetNetworkTimeout(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doSetNetworkTimeout").fillInStackTrace();
    }

    void doAsynchronousClose() {
        if (this.closeExecutor != null) {
            final PhysicalConnection physicalConnection = this;
            this.closeExecutor.execute(new Runnable(){

                @Override
                public void run() {
                    try {
                        physicalConnection.close();
                    }
                    catch (Throwable throwable) {
                        // empty catch block
                    }
                }
            });
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setSchema(String string) throws SQLException {
        String string2 = "\"[^\u0000\"]{0,28}\"";
        String string3 = "(\\p{javaLowerCase}|\\p{javaUpperCase})(\\p{javaLowerCase}|\\p{javaUpperCase}|\\d|_|\\$|#){0,29}";
        String string4 = "(" + string2 + ")|(" + string3 + ")";
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        if (!string.matches(string4)) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string5 = "alter session set current_schema = " + string;
        try (Statement statement = null;){
            statement = this.createStatement();
            statement.execute(string5);
        }
    }

    void releaseConnectionToPool() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("releaseConnectionToPool").fillInStackTrace();
    }

    boolean reusePooledConnection() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("reusePooledConnection").fillInStackTrace();
    }

    void resetAfterReusePooledConnection() throws SQLException {
        if (this.needToPurgeStatementCache()) {
            this.purgeStatementCache();
            this.closeStatements(false);
        }
    }

    @Override
    public boolean attachServerConnection() throws SQLException {
        this.requireOpenConnection();
        if (!this.drcpEnabled) {
            return true;
        }
        return this.reusePooledConnection();
    }

    @Override
    public boolean isDRCPEnabled() throws SQLException {
        return this.drcpEnabled;
    }

    @Override
    public boolean isDRCPMultitagEnabled() throws SQLException {
        return false;
    }

    @Override
    public String getDRCPReturnTag() throws SQLException {
        return null;
    }

    @Override
    public String getDRCPPLSQLCallbackName() throws SQLException {
        return this.drcpPLSQLCallback;
    }

    private boolean isDRCPConnection(String string) throws SQLException {
        boolean bl = false;
        if (this.getProtocolType().equals("kprb")) {
            return false;
        }
        if (string.matches("(?i:.*:POOLED)")) {
            bl = true;
        } else {
            int n2 = string.length();
            int n3 = string.indexOf(64);
            if (n3 < n2) {
                Object object;
                Object object2;
                Object object3 = n3 >= 0 ? string.substring(n3 + 1) : string;
                int n4 = ((String)object3).indexOf(40);
                if (n4 < 0 && this.tnsAdmin != null) {
                    object2 = new TNSNamesNamingAdapter(this.tnsAdmin);
                    try {
                        object = ((TNSNamesNamingAdapter)object2).resolve((String)object3);
                        if (object != null) {
                            object3 = object;
                        }
                    }
                    catch (NetException netException) {
                        // empty catch block
                    }
                }
                if (((String)object3).matches("(?i:.*:POOLED)\\s*((\\?)(.*))*")) {
                    bl = true;
                }
                object2 = new NVFactory();
                object = new NVNavigator();
                NVPair nVPair = null;
                try {
                    nVPair = ((NVNavigator)object).findNVPairRecurse(((NVFactory)object2).createNVPair((String)object3), "connect_data");
                }
                catch (NLException nLException) {
                    // empty catch block
                }
                if (nVPair != null) {
                    String string2;
                    NVPair nVPair2 = null;
                    nVPair2 = ((NVNavigator)object).findNVPair(nVPair, "SERVER");
                    if (nVPair2 != null && (string2 = nVPair2.getAtom()) != null && (string2.equalsIgnoreCase("POOLED") || string2.equalsIgnoreCase("EMON"))) {
                        bl = true;
                    }
                }
            }
        }
        return bl;
    }

    @Override
    public void detachServerConnection(String string) throws SQLException {
        this.requireOpenConnection();
        if (!this.drcpEnabled) {
            return;
        }
        this.drcpTagName = string != null && !string.isEmpty() ? string : null;
        this.releaseConnectionToPool();
    }

    @Override
    public boolean needToPurgeStatementCache() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("needToPurgeStatementCache").fillInStackTrace();
    }

    @Override
    public short getExecutingRPCFunctionCode() {
        return 0;
    }

    @Override
    public String getExecutingRPCSQL() {
        return "";
    }

    @Override
    public oracle.jdbc.internal.ResultSetCache getResultSetCache() throws SQLException {
        return this.getResultSetCacheInternal();
    }

    ResultSetCache getResultSetCacheInternal() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getResultSetCacheInternal").fillInStackTrace();
    }

    @Override
    public int getNegotiatedSDU() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getNegotiatedSDU").fillInStackTrace();
    }

    @Override
    public byte getNegotiatedTTCVersion() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getNegotiatedTTCVersion").fillInStackTrace();
    }

    @Override
    public void setChecksumMode(OracleConnection.ChecksumMode checksumMode) throws SQLException {
        this.checksumMode = checksumMode;
    }

    @Override
    public int getVarTypeMaxLenCompat() throws SQLException {
        return this.varTypeMaxLenCompat;
    }

    static final boolean bit(long l2, long l3) {
        return (l2 & l3) == l3;
    }

    static final boolean bit(int n2, int n3) {
        return (n2 & n3) == n3;
    }

    ArrayList<Long> getResultSetCacheLocalInvalidations() {
        return null;
    }

    void onPDBChange(OracleStatement oracleStatement) throws SQLException {
        this.removeAllDescriptor();
        this.closeResultsets(oracleStatement);
        this.cachedCompatibleString = null;
    }

    public boolean isValidCursorId(int n2) {
        return n2 != 0;
    }

    private void closeResultsets(OracleStatement oracleStatement) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            ResultSet resultSet;
            OracleStatement oracleStatement2;
            OracleStatement oracleStatement3 = this.statements;
            while (oracleStatement3 != null) {
                oracleStatement2 = oracleStatement3.nextChild;
                if (!oracleStatement3.isClosed() && oracleStatement3 != oracleStatement && this.isValidCursorId(oracleStatement3.cursorId) && (resultSet = oracleStatement3.getResultSet()) != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
                oracleStatement3 = oracleStatement2;
            }
            oracleStatement3 = this.statements;
            while (oracleStatement3 != null) {
                oracleStatement2 = oracleStatement3.next;
                if (!oracleStatement3.isClosed() && oracleStatement3 != oracleStatement && this.isValidCursorId(oracleStatement3.cursorId) && (resultSet = oracleStatement3.getResultSet()) != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
                oracleStatement3 = oracleStatement2;
            }
        }
    }

    static final boolean needToQuoteIdentifier(String string) {
        return !nonQuotedIdentifierPattern.matcher(string).matches();
    }

    @Override
    public boolean isLifecycleOpen() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.lifecycle == 1;
            return bl;
        }
    }

    @Override
    public void clearDrcpTagName() throws SQLException {
        this.drcpTagName = null;
    }

    @Override
    public void beginRequest() throws SQLException {
        if (this.drcpMultiplexingInRequestAPIs && this.lifecycle == 1 && this.isDRCPEnabled() && this.drcpState == OracleConnection.DRCPState.DETACHED) {
            this.attachServerConnection();
        }
    }

    @Override
    public void endRequest(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.lifecycle == 1 && !bl) {
                this.checkAndDrain();
            }
            if (!bl && this.drcpMultiplexingInRequestAPIs && this.lifecycle == 1 && this.isDRCPEnabled() && this.drcpState != OracleConnection.DRCPState.DETACHED) {
                this.detachServerConnection(this.getDRCPReturnTag());
            }
        }
    }

    @Override
    public void endRequest() throws SQLException {
        this.endRequest(false);
    }

    @Override
    public void sendRequestFlags() throws SQLException {
    }

    String getAuditBanner() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getAuditBanner").fillInStackTrace();
    }

    String getAccessBanner() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getAccessBanner").fillInStackTrace();
    }

    @Override
    public void addLargeObject(OracleLargeObject oracleLargeObject) throws SQLException {
    }

    @Override
    public void removeLargeObject(OracleLargeObject oracleLargeObject) throws SQLException {
    }

    @Override
    public void addBfile(OracleBfile oracleBfile) throws SQLException {
    }

    @Override
    public void removeBfile(OracleBfile oracleBfile) throws SQLException {
    }

    void addTemporaryLob(OracleLargeObject oracleLargeObject) {
        this.temporaryLobs.add(oracleLargeObject);
    }

    @Override
    public int freeTemporaryBlobsAndClobs() throws SQLException {
        if (this.lifecycle == 8) {
            return 0;
        }
        int n2 = 0;
        List<OracleLargeObject> list = this.temporaryLobs;
        this.temporaryLobs = new ArrayList<OracleLargeObject>();
        for (OracleLargeObject oracleLargeObject : list) {
            try {
                oracleLargeObject.freeTemporary();
                ++n2;
            }
            catch (SQLException sQLException) {}
        }
        return n2;
    }

    public void removeFromTemporaryLobs(OracleLargeObject oracleLargeObject) {
        try {
            Iterator<OracleLargeObject> iterator = this.temporaryLobs.iterator();
            while (iterator.hasNext()) {
                OracleLargeObject oracleLargeObject2 = iterator.next();
                if (!oracleLargeObject2.equals(oracleLargeObject)) continue;
                iterator.remove();
                break;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public boolean isServerBigSCN() throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    @Override
    public void setChunkInfo(OracleShardingKey oracleShardingKey, OracleShardingKey oracleShardingKey2, String string) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setChunkInfo").fillInStackTrace();
    }

    @Override
    public void setShardingKey(OracleShardingKey oracleShardingKey, OracleShardingKey oracleShardingKey2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setShardingKey(key, group)").fillInStackTrace();
    }

    @Override
    public boolean setShardingKeyIfValid(OracleShardingKey oracleShardingKey, OracleShardingKey oracleShardingKey2, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setShardingKeyIfValid(key, group, timeout)").fillInStackTrace();
    }

    @Override
    public boolean setShardingKeyIfValid(OracleShardingKey oracleShardingKey, int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setShardingKeyIfValid(key, timeout)").fillInStackTrace();
    }

    @Override
    public void setShardingKey(OracleShardingKey oracleShardingKey) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setShardingKey(key)").fillInStackTrace();
    }

    @Override
    public HAManager getHAManager() {
        return this.haManager;
    }

    @Override
    public void setHAManager(HAManager hAManager) throws SQLException {
        if (this.haManager != null && this.haManager != NoSupportHAManager.getInstance()) {
            throw new SQLException("Invalid HAManager in connection");
        }
        this.haManager = hAManager;
    }

    protected String getCompatibleString() throws SQLException {
        String string;
        block25: {
            string = this.cachedCompatibleString;
            if (string == null) {
                try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                    string = this.cachedCompatibleString;
                    if (string != null) break block25;
                    try (CallableStatement callableStatement = this.prepareCall("begin ? := private_jdbc.get_compatible(); end;");){
                        callableStatement.registerOutParameter(1, 12);
                        callableStatement.execute();
                        this.cachedCompatibleString = string = callableStatement.getString(1);
                    }
                }
            }
        }
        return string;
    }

    boolean isCompatible122OrGreater() throws SQLException {
        return this.getVersionNumber() >= 12200 && this.getCompatible() >= 12200;
    }

    protected int getCompatible() throws SQLException {
        return this.decodeCompatibleDottedString(this.getCompatibleString());
    }

    protected int decodeCompatibleDottedString(String string) throws SQLException {
        int n2 = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(string.trim(), " .", false);
        for (int i2 = 0; i2 < 4; ++i2) {
            int n3 = 0;
            try {
                if (stringTokenizer.hasMoreTokens()) {
                    String string2 = stringTokenizer.nextToken();
                    try {
                        n3 = Integer.decode(string2);
                    }
                    catch (NumberFormatException numberFormatException) {}
                } else {
                    n3 = 0;
                }
                n2 = n2 * 10 + n3;
                continue;
            }
            catch (NoSuchElementException noSuchElementException) {
                // empty catch block
            }
        }
        return n2;
    }

    protected void cleanStatementCache() {
        if (!this.isStatementCacheInitialized()) {
            return;
        }
        this.statementCache.clearCursorIds();
    }

    @Override
    public OracleConnection.DRCPState getDRCPState() throws SQLException {
        return this.drcpState;
    }

    @Override
    public boolean isNetworkCompressionEnabled() {
        return false;
    }

    @Override
    public int getOutboundConnectTimeout() {
        return 0;
    }

    @Override
    public NetStat getNetworkStat() {
        return null;
    }

    @Override
    public boolean hasNoOpenHandles() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("hasNoOpenHandles").fillInStackTrace();
    }

    @Override
    public DatabaseSessionState getDatabaseSessionState() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getDatabaseSessionState").fillInStackTrace();
    }

    @Override
    public void setDatabaseSessionState(DatabaseSessionState databaseSessionState) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setDatabaseSessionState").fillInStackTrace();
    }

    @Override
    public boolean isSafelyClosed() throws SQLException {
        return this.safelyClosed;
    }

    @Override
    public void setSafelyClosed(boolean bl) throws SQLException {
        this.safelyClosed = bl;
    }

    @Blind(value=PropertiesBlinder.class)
    private Properties getConnectionPropertiesFromFile(@Blind(value=PropertiesBlinder.class) Properties properties, Hashtable<String, ?> hashtable, String string, boolean bl) throws SQLException {
        boolean bl2;
        String string2;
        String string3 = properties.getProperty("oracle.jdbc.config.file");
        if (string3 == null) {
            string3 = PhysicalConnection.getSystemPropertyConfigFile(null);
        }
        if ((string2 = properties.getProperty("database")) == null) {
            string2 = properties.getProperty("oracle.jdbc.database");
        }
        if (string2 == null) {
            string2 = PhysicalConnection.getSystemPropertyDatabase(CONNECTION_PROPERTY_DATABASE_DEFAULT);
        }
        if (string2 == null) {
            bl2 = true;
            string2 = properties.getProperty("server");
            if (string2 == null) {
                string2 = (String)hashtable.get("database");
            }
        } else {
            bl2 = false;
        }
        return PropertiesFileUtil.loadPropertiesFromFile(string3, string, bl, string2, bl2);
    }

    boolean checkAndDrain() throws SQLException {
        if (this.inbandNotification && this.drainOnInbandNotification()) {
            return true;
        }
        if (this.fanEnabled) {
            return this.getHAManager().checkAndDrain(this);
        }
        return false;
    }

    @Override
    public boolean isValid(int n2) throws SQLException {
        return this.isValid(this.defaultConnectionValidation, n2);
    }

    @Override
    public boolean isValid(OracleConnection.ConnectionValidation connectionValidation, int n2) throws SQLException {
        if (n2 < 0) {
            throw new SQLException("isValid timeout cannot be negative.");
        }
        if (this.lifecycle != 1) {
            return false;
        }
        boolean bl = false;
        switch (connectionValidation) {
            case NONE: {
                bl = this.lifecycle == 1;
                break;
            }
            case LOCAL: {
                bl = this.isUsable();
                break;
            }
            case SOCKET: {
                bl = this.isValidLight(n2);
                break;
            }
            case NETWORK: {
                bl = this.pingDatabase(n2) == 0;
                break;
            }
            case SERVER: 
            case COMPLETE: {
                bl = this.checkSQLEngineStatus(n2) == 0;
            }
        }
        return bl;
    }

    int checkSQLEngineStatus(int n2) throws SQLException {
        if (this.checkAndDrain()) {
            return -1;
        }
        return this.executeDefaultConnectionValidationQuery(n2);
    }

    private int executeDefaultConnectionValidationQuery(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            try (Statement statement = this.createStatement();){
                statement.setQueryTimeout(n2);
                ((oracle.jdbc.OracleStatement)statement).defineColumnType(1, 12, 1);
                statement.executeQuery(DEFAULT_CONNECTION_VALIDATION_QUERY);
            }
            catch (SQLException sQLException) {
                int n3 = -1;
                if (closeableLock != null) {
                    if (var3_3 != null) {
                        try {
                            closeableLock.close();
                        }
                        catch (Throwable throwable) {
                            var3_3.addSuppressed(throwable);
                        }
                    } else {
                        closeableLock.close();
                    }
                }
                return n3;
            }
            int n4 = 0;
            return n4;
        }
    }

    boolean isValidLight(int n2) throws SQLException {
        return this.pingDatabase(n2) == 0;
    }

    boolean drainOnInbandNotification() throws SQLException {
        return false;
    }

    void closeConnectionSafely() throws SQLException {
        this.abort();
        this.close();
        this.setSafelyClosed(true);
    }

    static final String getTnsAdminFromEnv() {
        String string = AccessController.doPrivileged(() -> {
            String string = System.getProperty(TNS_ADMIN);
            return string == null ? System.getenv(TNS_ADMIN) : string;
        });
        return string;
    }

    @Override
    public String getEncryptionProviderName() throws SQLException {
        return null;
    }

    @Override
    public String getChecksumProviderName() throws SQLException {
        return null;
    }

    @Override
    public String getNetConnectionId() throws SQLException {
        return null;
    }

    @Override
    public Properties getJavaNetProperties() throws SQLException {
        return null;
    }

    private String throughDbCharset(String string) throws SQLException {
        byte[] byArray = this.conversion.getDbCharSetObj().convert(string);
        return this.conversion.getDbCharSetObj().toString(byArray, 0, byArray.length);
    }

    public boolean isSimpleIdentifier(String string) throws SQLException {
        if (string == null) {
            throw new NullPointerException();
        }
        String string2 = this.throughDbCharset(string);
        return IS_SIMPLE_IDENTIFIER.test(string2) || IS_QUOTED_IDENTIFIER.test(string2);
    }

    public String enquoteLiteral(String string) throws SQLException {
        if (string == null) {
            throw new NullPointerException();
        }
        String string2 = this.throughDbCharset(string);
        return "'" + string2.replace("'", "''") + "'";
    }

    public String enquoteIdentifier(String string, boolean bl) throws SQLException {
        if (string == null) {
            throw new NullPointerException();
        }
        String string2 = this.throughDbCharset(string);
        if (IS_SIMPLE_IDENTIFIER.test(string2)) {
            return bl ? "\"" + string2 + "\"" : this.throughDbCharset(string2.toUpperCase());
        }
        if (IS_QUOTED_IDENTIFIER.test(string2)) {
            return string2;
        }
        if (IS_VALID_IDENTIFIER.test(string2)) {
            return "\"" + string2 + "\"";
        }
        throw (SQLException)DatabaseError.createSqlException(133).fillInStackTrace();
    }

    @Override
    public void disableLogging() throws SQLException {
        if (this.securedLogger != null) {
            this.securedLogger.disableMemoryLogging();
            this.securedLogger.disableFileLogging();
        }
    }

    @Override
    public void enableLogging() throws SQLException {
        if (this.securedLogger != null) {
            this.securedLogger.enableMemoryLogging();
            this.securedLogger.enableFileLogging();
        }
    }

    @Override
    public void dumpLog() throws SQLException {
        if (this.securedLogger != null) {
            this.securedLogger.dumpLog();
        }
    }

    @Override
    public SecuredLogger getLogger() throws SQLException {
        return this.securedLogger;
    }

    OracleJsonFactory getOracleJsonFactory() {
        if (this.jsonFactory == null) {
            this.jsonFactory = new OracleJsonFactory();
        }
        return this.jsonFactory;
    }

    @Override
    public double getPercentageQueryExecutionOnDirectShard() {
        return 0.0;
    }

    @Override
    public void addFeature(OracleConnection.ClientFeature clientFeature) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("addFeature").fillInStackTrace();
    }

    ByteBuffer convertClobDataInNetworkCharSet(OracleClob oracleClob, char[] cArray) throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    static boolean isValueBasedLocator(byte[] byArray) {
        if (byArray == null) {
            return false;
        }
        return (byArray[4] & 0x20) == 32;
    }

    static boolean isQuasiLocator(byte[] byArray) {
        if (byArray == null) {
            return false;
        }
        return byArray[3] == 4;
    }

    static boolean isReadOnly(byte[] byArray) {
        if (byArray == null) {
            return false;
        }
        return (byArray[6] & 1) == 1;
    }

    static boolean isTemporary(byte[] byArray) {
        if (byArray == null) {
            return false;
        }
        return (byArray[7] & 1) > 0 || (byArray[4] & 0x40) > 0 || PhysicalConnection.isValueBasedLocator(byArray) || PhysicalConnection.isQuasiLocator(byArray);
    }

    @Override
    public final Executor createUserCodeExecutor() {
        if (System.getSecurityManager() == null) {
            return this.asyncExecutor;
        }
        AccessControlContext accessControlContext = AccessController.getContext();
        return runnable -> this.asyncExecutor.execute(() -> AccessController.doPrivileged(() -> {
            runnable.run();
            return null;
        }, accessControlContext));
    }

    void requireOpenConnection() throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
    }

    void suspendLogging() {
    }

    void resumeLogging() {
    }

    static {
        boolean bl = false;
        try {
            Class.forName("javax.json.JsonValue");
            bl = true;
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        IS_JSON_JAR_LOADED = bl;
        RESERVED_NAMESPACES = Arrays.asList("SYS");
        SUPPORTED_NAME_PATTERN = Pattern.compile("\\w+\\.\\w+");
        CALL_ABORT_PERMISSION = new SQLPermission("callAbort");
        CALL_SETNETWORKTIMEOUT_PERMISSION = new SQLPermission("setNetworkTimeout");
        nonQuotedIdentifierPattern = Pattern.compile("[a-zA-Z]\\w*");
        IS_SIMPLE_IDENTIFIER = Pattern.compile("\\A\\p{IsAlphabetic}[\\p{IsAlphabetic}\\p{IsDigit}_$#]*\\z").asPredicate();
        IS_QUOTED_IDENTIFIER = Pattern.compile("\\A\"[^\"\\u0000]+\"\\z").asPredicate();
        IS_VALID_IDENTIFIER = Pattern.compile("\\A[^\"\\u0000]+\\z").asPredicate();
        _Copyright_2014_Oracle_All_Rights_Reserved_ = null;
    }

    private static final class BufferCacheStore {
        static int MAX_CACHED_BUFFER_SIZE = Integer.MAX_VALUE;
        final BufferCache<byte[]> byteBufferCache;
        final BufferCache<char[]> charBufferCache;

        BufferCacheStore() {
            this(MAX_CACHED_BUFFER_SIZE);
        }

        BufferCacheStore(int n2) {
            this.byteBufferCache = new BufferCache(n2);
            this.charBufferCache = new BufferCache(n2);
        }
    }
}

