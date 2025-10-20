/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import oracle.jdbc.LogicalTransactionId;
import oracle.jdbc.LogicalTransactionIdEventListener;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleOCIFailover;
import oracle.jdbc.OracleSavepoint;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.aq.AQDequeueOptions;
import oracle.jdbc.aq.AQEnqueueOptions;
import oracle.jdbc.aq.AQMessage;
import oracle.jdbc.aq.AQNotificationRegistration;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.diagnostics.SecuredLogger;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.sql.ARRAY;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.DATE;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TypeDescriptor;

@DisableTrace
public class OracleConnectionWrapper
implements oracle.jdbc.OracleConnection,
Monitor {
    protected oracle.jdbc.OracleConnection connection;
    private final Monitor.CloseableLock monitorLock;
    private Map<Class, Object> proxies = new HashMap<Class, Object>(3);
    private static Map<Class, Class> proxyClasses = new HashMap<Class, Class>();

    public OracleConnectionWrapper() {
        this.monitorLock = this.newDefaultLock();
    }

    protected OracleConnectionWrapper(Monitor.CloseableLock closeableLock) {
        this.monitorLock = closeableLock;
    }

    public OracleConnectionWrapper(oracle.jdbc.OracleConnection oracleConnection) {
        this();
        this.connection = oracleConnection;
        oracleConnection.setWrapper(this);
    }

    @Override
    public oracle.jdbc.OracleConnection unwrap() {
        return this.connection;
    }

    @Override
    public OracleConnection physicalConnectionWithin() {
        return this.connection.physicalConnectionWithin();
    }

    public String getDatabaseTimeZone() throws SQLException {
        return this.physicalConnectionWithin().getDatabaseTimeZone();
    }

    @Override
    public void setWrapper(oracle.jdbc.OracleConnection oracleConnection) {
        this.connection.setWrapper(oracleConnection);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String string) throws SQLException {
        return this.connection.prepareStatement(string);
    }

    @Override
    public CallableStatement prepareCall(String string) throws SQLException {
        return this.connection.prepareCall(string);
    }

    @Override
    public String nativeSQL(String string) throws SQLException {
        return this.connection.nativeSQL(string);
    }

    @Override
    public void setAutoCommit(boolean bl) throws SQLException {
        this.connection.setAutoCommit(bl);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.connection.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        this.connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        this.connection.rollback();
    }

    @Override
    public void close() throws SQLException {
        this.connection.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.connection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean bl) throws SQLException {
        this.connection.setReadOnly(bl);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.connection.isReadOnly();
    }

    @Override
    public void setCatalog(String string) throws SQLException {
        this.connection.setCatalog(string);
    }

    @Override
    public String getCatalog() throws SQLException {
        return this.connection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int n2) throws SQLException {
        this.connection.setTransactionIsolation(n2);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return this.connection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.connection.clearWarnings();
    }

    @Override
    public Statement createStatement(int n2, int n3) throws SQLException {
        return this.connection.createStatement(n2, n3);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, int n3) throws SQLException {
        return this.connection.prepareStatement(string, n2, n3);
    }

    @Override
    public CallableStatement prepareCall(String string, int n2, int n3) throws SQLException {
        return this.connection.prepareCall(string, n2, n3);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.connection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.connection.setTypeMap(map);
    }

    @Override
    public boolean isProxySession() {
        return this.connection.isProxySession();
    }

    @Override
    public void openProxySession(int n2, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        this.connection.openProxySession(n2, properties);
    }

    @Override
    public void archive(int n2, int n3, String string) throws SQLException {
        this.connection.archive(n2, n3, string);
    }

    @Override
    public boolean getAutoClose() throws SQLException {
        return this.connection.getAutoClose();
    }

    @Override
    public CallableStatement getCallWithKey(String string) throws SQLException {
        return this.connection.getCallWithKey(string);
    }

    @Override
    public int getDefaultExecuteBatch() {
        return this.connection.getDefaultExecuteBatch();
    }

    @Override
    public int getDefaultRowPrefetch() {
        return this.connection.getDefaultRowPrefetch();
    }

    @Override
    public Object getDescriptor(String string) {
        return this.connection.getDescriptor(string);
    }

    @Override
    public String[] getEndToEndMetrics() throws SQLException {
        return this.connection.getEndToEndMetrics();
    }

    @Override
    public short getEndToEndECIDSequenceNumber() throws SQLException {
        return this.connection.getEndToEndECIDSequenceNumber();
    }

    @Override
    public boolean getIncludeSynonyms() {
        return this.connection.getIncludeSynonyms();
    }

    @Override
    public boolean getRestrictGetTables() {
        return this.connection.getRestrictGetTables();
    }

    @Override
    public boolean getImplicitCachingEnabled() throws SQLException {
        return this.connection.getImplicitCachingEnabled();
    }

    @Override
    public boolean getExplicitCachingEnabled() throws SQLException {
        return this.connection.getExplicitCachingEnabled();
    }

    @Override
    public Object getJavaObject(String string) throws SQLException {
        return this.connection.getJavaObject(string);
    }

    @Override
    public boolean getRemarksReporting() {
        return this.connection.getRemarksReporting();
    }

    @Override
    public String getSQLType(Object object) throws SQLException {
        return this.connection.getSQLType(object);
    }

    @Override
    public int getStmtCacheSize() {
        return this.connection.getStmtCacheSize();
    }

    @Override
    public int getStatementCacheSize() throws SQLException {
        return this.connection.getStatementCacheSize();
    }

    @Override
    public PreparedStatement getStatementWithKey(String string) throws SQLException {
        return this.connection.getStatementWithKey(string);
    }

    @Override
    public short getStructAttrCsId() throws SQLException {
        return this.connection.getStructAttrCsId();
    }

    @Override
    public String getUserName() throws SQLException {
        return this.connection.getUserName();
    }

    @Override
    public String getCurrentSchema() throws SQLException {
        return this.connection.getCurrentSchema();
    }

    @Override
    public boolean getUsingXAFlag() {
        return this.connection.getUsingXAFlag();
    }

    @Override
    public boolean getXAErrorFlag() {
        return this.connection.getXAErrorFlag();
    }

    @Override
    public OracleSavepoint oracleSetSavepoint() throws SQLException {
        return this.connection.oracleSetSavepoint();
    }

    @Override
    public OracleSavepoint oracleSetSavepoint(String string) throws SQLException {
        return this.connection.oracleSetSavepoint(string);
    }

    @Override
    public void oracleRollback(OracleSavepoint oracleSavepoint) throws SQLException {
        this.connection.oracleRollback(oracleSavepoint);
    }

    @Override
    public void oracleReleaseSavepoint(OracleSavepoint oracleSavepoint) throws SQLException {
        this.connection.oracleReleaseSavepoint(oracleSavepoint);
    }

    @Override
    public int pingDatabase() throws SQLException {
        return this.connection.pingDatabase();
    }

    @Override
    public int pingDatabase(int n2) throws SQLException {
        return this.connection.pingDatabase(n2);
    }

    @Override
    public void purgeExplicitCache() throws SQLException {
        this.connection.purgeExplicitCache();
    }

    @Override
    public void purgeImplicitCache() throws SQLException {
        this.connection.purgeImplicitCache();
    }

    @Override
    public void putDescriptor(String string, Object object) throws SQLException {
        this.connection.putDescriptor(string, object);
    }

    @Override
    public void registerSQLType(String string, Class<?> clazz) throws SQLException {
        this.connection.registerSQLType(string, clazz);
    }

    @Override
    public void registerSQLType(String string, String string2) throws SQLException {
        this.connection.registerSQLType(string, string2);
    }

    @Override
    public void setAutoClose(boolean bl) throws SQLException {
        this.connection.setAutoClose(bl);
    }

    @Override
    public void setDefaultExecuteBatch(int n2) throws SQLException {
        this.connection.setDefaultExecuteBatch(n2);
    }

    @Override
    public void setDefaultRowPrefetch(int n2) throws SQLException {
        this.connection.setDefaultRowPrefetch(n2);
    }

    @Override
    public void setEndToEndMetrics(String[] stringArray, short s2) throws SQLException {
        this.connection.setEndToEndMetrics(stringArray, s2);
    }

    @Override
    public void setExplicitCachingEnabled(boolean bl) throws SQLException {
        this.connection.setExplicitCachingEnabled(bl);
    }

    @Override
    public void setImplicitCachingEnabled(boolean bl) throws SQLException {
        this.connection.setImplicitCachingEnabled(bl);
    }

    @Override
    public void setIncludeSynonyms(boolean bl) {
        this.connection.setIncludeSynonyms(bl);
    }

    @Override
    public void setRemarksReporting(boolean bl) {
        this.connection.setRemarksReporting(bl);
    }

    @Override
    public void setRestrictGetTables(boolean bl) {
        this.connection.setRestrictGetTables(bl);
    }

    @Override
    public void setStmtCacheSize(int n2) throws SQLException {
        this.connection.setStmtCacheSize(n2);
    }

    @Override
    public void setStatementCacheSize(int n2) throws SQLException {
        this.connection.setStatementCacheSize(n2);
    }

    @Override
    public void setStmtCacheSize(int n2, boolean bl) throws SQLException {
        this.connection.setStmtCacheSize(n2, bl);
    }

    @Override
    public void setUsingXAFlag(boolean bl) {
        this.connection.setUsingXAFlag(bl);
    }

    @Override
    public void setXAErrorFlag(boolean bl) {
        this.connection.setXAErrorFlag(bl);
    }

    @Override
    public void shutdown(OracleConnection.DatabaseShutdownMode databaseShutdownMode) throws SQLException {
        this.connection.shutdown(databaseShutdownMode);
    }

    @Override
    public void startup(String string, int n2) throws SQLException {
        this.connection.startup(string, n2);
    }

    @Override
    public void startup(OracleConnection.DatabaseStartupMode databaseStartupMode) throws SQLException {
        this.connection.startup(databaseStartupMode);
    }

    @Override
    public void startup(OracleConnection.DatabaseStartupMode databaseStartupMode, String string) throws SQLException {
        this.connection.startup(databaseStartupMode, string);
    }

    @Override
    public PreparedStatement prepareStatementWithKey(String string) throws SQLException {
        return this.connection.prepareStatementWithKey(string);
    }

    @Override
    public CallableStatement prepareCallWithKey(String string) throws SQLException {
        return this.connection.prepareCallWithKey(string);
    }

    @Override
    public void setCreateStatementAsRefCursor(boolean bl) {
        this.connection.setCreateStatementAsRefCursor(bl);
    }

    @Override
    public boolean getCreateStatementAsRefCursor() {
        return this.connection.getCreateStatementAsRefCursor();
    }

    @Override
    public void setSessionTimeZone(String string) throws SQLException {
        this.connection.setSessionTimeZone(string);
    }

    @Override
    public String getSessionTimeZone() {
        return this.connection.getSessionTimeZone();
    }

    @Override
    public String getSessionTimeZoneOffset() throws SQLException {
        return this.connection.getSessionTimeZoneOffset();
    }

    @Override
    public Connection _getPC() {
        return this.connection._getPC();
    }

    @Override
    public boolean isLogicalConnection() {
        return this.connection.isLogicalConnection();
    }

    @Override
    public void registerTAFCallback(OracleOCIFailover oracleOCIFailover, Object object) throws SQLException {
        this.connection.registerTAFCallback(oracleOCIFailover, object);
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getProperties() {
        return this.connection.getProperties();
    }

    @Override
    public void close(int n2) throws SQLException {
        this.connection.close(n2);
    }

    @Override
    public void setPlsqlWarnings(String string) throws SQLException {
        this.connection.setPlsqlWarnings(string);
    }

    @Override
    public void setHoldability(int n2) throws SQLException {
        this.connection.setHoldability(n2);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this.connection.getHoldability();
    }

    @Override
    public Statement createStatement(int n2, int n3, int n4) throws SQLException {
        return this.connection.createStatement(n2, n3, n4);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2, int n3, int n4) throws SQLException {
        return this.connection.prepareStatement(string, n2, n3, n4);
    }

    @Override
    public CallableStatement prepareCall(String string, int n2, int n3, int n4) throws SQLException {
        return this.connection.prepareCall(string, n2, n3, n4);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Savepoint savepoint = this.connection.setSavepoint();
            return savepoint;
        }
    }

    @Override
    public Savepoint setSavepoint(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Savepoint savepoint = this.connection.setSavepoint(string);
            return savepoint;
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.connection.rollback(savepoint);
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.connection.releaseSavepoint(savepoint);
        }
    }

    @Override
    public PreparedStatement prepareStatement(String string, int n2) throws SQLException {
        return this.connection.prepareStatement(string, n2);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int[] nArray) throws SQLException {
        return this.connection.prepareStatement(string, nArray);
    }

    @Override
    public PreparedStatement prepareStatement(String string, String[] stringArray) throws SQLException {
        return this.connection.prepareStatement(string, stringArray);
    }

    @Override
    public ARRAY createARRAY(String string, Object object) throws SQLException {
        return this.connection.createARRAY(string, object);
    }

    @Override
    public Array createOracleArray(String string, Object object) throws SQLException {
        return this.connection.createOracleArray(string, object);
    }

    @Override
    public BINARY_DOUBLE createBINARY_DOUBLE(double d2) throws SQLException {
        return this.connection.createBINARY_DOUBLE(d2);
    }

    @Override
    public BINARY_FLOAT createBINARY_FLOAT(float f2) throws SQLException {
        return this.connection.createBINARY_FLOAT(f2);
    }

    @Override
    public DATE createDATE(Date date) throws SQLException {
        return this.connection.createDATE(date);
    }

    @Override
    public DATE createDATE(Time time) throws SQLException {
        return this.connection.createDATE(time);
    }

    @Override
    public DATE createDATE(Timestamp timestamp) throws SQLException {
        return this.connection.createDATE(timestamp);
    }

    @Override
    public DATE createDATE(Date date, Calendar calendar) throws SQLException {
        return this.connection.createDATE(date, calendar);
    }

    @Override
    public DATE createDATE(Time time, Calendar calendar) throws SQLException {
        return this.connection.createDATE(time, calendar);
    }

    @Override
    public DATE createDATE(Timestamp timestamp, Calendar calendar) throws SQLException {
        return this.connection.createDATE(timestamp, calendar);
    }

    @Override
    public DATE createDATE(String string) throws SQLException {
        return this.connection.createDATE(string);
    }

    @Override
    public INTERVALDS createINTERVALDS(String string) throws SQLException {
        return this.connection.createINTERVALDS(string);
    }

    @Override
    public INTERVALYM createINTERVALYM(String string) throws SQLException {
        return this.connection.createINTERVALYM(string);
    }

    @Override
    public NUMBER createNUMBER(boolean bl) throws SQLException {
        return this.connection.createNUMBER(bl);
    }

    @Override
    public NUMBER createNUMBER(byte by) throws SQLException {
        return this.connection.createNUMBER(by);
    }

    @Override
    public NUMBER createNUMBER(short s2) throws SQLException {
        return this.connection.createNUMBER(s2);
    }

    @Override
    public NUMBER createNUMBER(int n2) throws SQLException {
        return this.connection.createNUMBER(n2);
    }

    @Override
    public NUMBER createNUMBER(long l2) throws SQLException {
        return this.connection.createNUMBER(l2);
    }

    @Override
    public NUMBER createNUMBER(float f2) throws SQLException {
        return this.connection.createNUMBER(f2);
    }

    @Override
    public NUMBER createNUMBER(double d2) throws SQLException {
        return this.connection.createNUMBER(d2);
    }

    @Override
    public NUMBER createNUMBER(BigDecimal bigDecimal) throws SQLException {
        return this.connection.createNUMBER(bigDecimal);
    }

    @Override
    public NUMBER createNUMBER(BigInteger bigInteger) throws SQLException {
        return this.connection.createNUMBER(bigInteger);
    }

    @Override
    public NUMBER createNUMBER(String string, int n2) throws SQLException {
        return this.connection.createNUMBER(string, n2);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Date date) throws SQLException {
        return this.connection.createTIMESTAMP(date);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(DATE dATE) throws SQLException {
        return this.connection.createTIMESTAMP(dATE);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Time time) throws SQLException {
        return this.connection.createTIMESTAMP(time);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Timestamp timestamp) throws SQLException {
        return this.connection.createTIMESTAMP(timestamp);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(Timestamp timestamp, Calendar calendar) throws SQLException {
        return this.connection.createTIMESTAMP(timestamp, calendar);
    }

    @Override
    public TIMESTAMP createTIMESTAMP(String string) throws SQLException {
        return this.connection.createTIMESTAMP(string);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Date date) throws SQLException {
        return this.connection.createTIMESTAMPTZ(date);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Date date, Calendar calendar) throws SQLException {
        return this.connection.createTIMESTAMPTZ(date, calendar);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Time time) throws SQLException {
        return this.connection.createTIMESTAMPTZ(time);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Time time, Calendar calendar) throws SQLException {
        return this.connection.createTIMESTAMPTZ(time, calendar);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp timestamp) throws SQLException {
        return this.connection.createTIMESTAMPTZ(timestamp);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp timestamp, Calendar calendar) throws SQLException {
        return this.connection.createTIMESTAMPTZ(timestamp, calendar);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp timestamp, ZoneId zoneId) throws SQLException {
        return this.connection.createTIMESTAMPTZ(timestamp, zoneId);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(String string) throws SQLException {
        return this.connection.createTIMESTAMPTZ(string);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(String string, Calendar calendar) throws SQLException {
        return this.connection.createTIMESTAMPTZ(string, calendar);
    }

    @Override
    public TIMESTAMPTZ createTIMESTAMPTZ(DATE dATE) throws SQLException {
        return this.connection.createTIMESTAMPTZ(dATE);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(Date date, Calendar calendar) throws SQLException {
        return this.connection.createTIMESTAMPLTZ(date, calendar);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(Time time, Calendar calendar) throws SQLException {
        return this.connection.createTIMESTAMPLTZ(time, calendar);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(Timestamp timestamp, Calendar calendar) throws SQLException {
        return this.connection.createTIMESTAMPLTZ(timestamp, calendar);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(String string, Calendar calendar) throws SQLException {
        return this.connection.createTIMESTAMPLTZ(string, calendar);
    }

    @Override
    public TIMESTAMPLTZ createTIMESTAMPLTZ(DATE dATE, Calendar calendar) throws SQLException {
        return this.connection.createTIMESTAMPLTZ(dATE, calendar);
    }

    @Override
    public Array createArrayOf(String string, Object[] objectArray) throws SQLException {
        return this.connection.createArrayOf(string, objectArray);
    }

    @Override
    public Blob createBlob() throws SQLException {
        return this.connection.createBlob();
    }

    @Override
    public Clob createClob() throws SQLException {
        return this.connection.createClob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return this.connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return this.connection.createSQLXML();
    }

    @Override
    public Struct createStruct(String string, Object[] objectArray) throws SQLException {
        return this.connection.createStruct(string, objectArray);
    }

    @Override
    public boolean isValid(int n2) throws SQLException {
        return this.connection.isValid(n2);
    }

    @Override
    public void setClientInfo(String string, String string2) throws SQLClientInfoException {
        this.connection.setClientInfo(string, string2);
    }

    @Override
    public void setClientInfo(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLClientInfoException {
        this.connection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String string) throws SQLException {
        return this.connection.getClientInfo(string);
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getClientInfo() throws SQLException {
        return this.connection.getClientInfo();
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        if (clazz.isInterface()) {
            return clazz.isInstance(this) || this.connection.isWrapperFor(clazz);
        }
        throw (SQLException)DatabaseError.createSqlException(null, 177).fillInStackTrace();
    }

    protected <T> T proxyFor(Object object, Class<T> clazz) throws SQLException {
        try {
            Object object2 = this.proxies.get(clazz);
            if (object2 == null) {
                Class<?> clazz2 = proxyClasses.get(clazz);
                if (clazz2 == null) {
                    clazz2 = Proxy.getProxyClass(clazz.getClassLoader(), clazz);
                    proxyClasses.put(clazz, clazz2);
                }
                object2 = clazz2.getConstructor(InvocationHandler.class).newInstance(new CloseInvocationHandler(this));
                this.proxies.put(clazz, object2);
            }
            return (T)object2;
        }
        catch (Exception exception) {
            throw (SQLException)DatabaseError.createSqlException(null, 1, "Cannot construct proxy").fillInStackTrace();
        }
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface()) {
            if (clazz.isInstance(this)) {
                return (T)this;
            }
            return this.proxyFor(this.connection.unwrap(clazz), clazz);
        }
        throw (SQLException)DatabaseError.createSqlException(null, 177).fillInStackTrace();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        this.connection.abort(executor);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return this.connection.getNetworkTimeout();
    }

    @Override
    public String getSchema() throws SQLException {
        return this.connection.getSchema();
    }

    @Override
    public void setNetworkTimeout(Executor executor, int n2) throws SQLException {
        this.connection.setNetworkTimeout(executor, n2);
    }

    @Override
    public void setSchema(String string) throws SQLException {
        this.connection.setSchema(string);
    }

    @Override
    public DatabaseChangeRegistration registerDatabaseChangeNotification(@Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        return this.connection.registerDatabaseChangeNotification(properties);
    }

    @Override
    public DatabaseChangeRegistration getDatabaseChangeRegistration(int n2) throws SQLException {
        return this.connection.getDatabaseChangeRegistration(n2);
    }

    @Override
    public void unregisterDatabaseChangeNotification(DatabaseChangeRegistration databaseChangeRegistration) throws SQLException {
        this.connection.unregisterDatabaseChangeNotification(databaseChangeRegistration);
    }

    @Override
    public void unregisterDatabaseChangeNotification(int n2, String string, int n3) throws SQLException {
        this.connection.unregisterDatabaseChangeNotification(n2, string, n3);
    }

    @Override
    public void unregisterDatabaseChangeNotification(int n2) throws SQLException {
        this.connection.unregisterDatabaseChangeNotification(n2);
    }

    @Override
    public void unregisterDatabaseChangeNotification(long l2, String string) throws SQLException {
        this.connection.unregisterDatabaseChangeNotification(l2, string);
    }

    @Override
    public AQNotificationRegistration[] registerAQNotification(String[] stringArray, Properties[] propertiesArray, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        return this.connection.registerAQNotification(stringArray, propertiesArray, properties);
    }

    @Override
    public void unregisterAQNotification(AQNotificationRegistration aQNotificationRegistration) throws SQLException {
        this.connection.unregisterAQNotification(aQNotificationRegistration);
    }

    @Override
    public AQMessage dequeue(String string, AQDequeueOptions aQDequeueOptions, byte[] byArray) throws SQLException {
        return this.connection.dequeue(string, aQDequeueOptions, byArray);
    }

    @Override
    public AQMessage dequeue(String string, AQDequeueOptions aQDequeueOptions, byte[] byArray, int n2) throws SQLException {
        return this.connection.dequeue(string, aQDequeueOptions, byArray, n2);
    }

    @Override
    public AQMessage dequeue(String string, AQDequeueOptions aQDequeueOptions, String string2) throws SQLException {
        return this.connection.dequeue(string, aQDequeueOptions, string2);
    }

    @Override
    public AQMessage[] dequeue(String string, AQDequeueOptions aQDequeueOptions, String string2, int n2) throws SQLException {
        return this.connection.dequeue(string, aQDequeueOptions, string2, n2);
    }

    @Override
    public AQMessage[] dequeue(String string, AQDequeueOptions aQDequeueOptions, byte[] byArray, int n2, int n3) throws SQLException {
        return this.connection.dequeue(string, aQDequeueOptions, byArray, n2, n3);
    }

    @Override
    public void enqueue(String string, AQEnqueueOptions aQEnqueueOptions, AQMessage aQMessage) throws SQLException {
        this.connection.enqueue(string, aQEnqueueOptions, aQMessage);
    }

    @Override
    public int enqueue(String string, AQEnqueueOptions aQEnqueueOptions, AQMessage[] aQMessageArray) throws SQLException {
        return this.connection.enqueue(string, aQEnqueueOptions, aQMessageArray);
    }

    @Override
    public void commit(EnumSet<OracleConnection.CommitOption> enumSet) throws SQLException {
        this.connection.commit(enumSet);
    }

    @Override
    public void cancel() throws SQLException {
        this.connection.cancel();
    }

    @Override
    public void abort() throws SQLException {
        this.connection.abort();
    }

    @Override
    public TypeDescriptor[] getAllTypeDescriptorsInCurrentSchema() throws SQLException {
        return this.connection.getAllTypeDescriptorsInCurrentSchema();
    }

    @Override
    public TypeDescriptor[] getTypeDescriptorsFromListInCurrentSchema(String[] stringArray) throws SQLException {
        return this.connection.getTypeDescriptorsFromListInCurrentSchema(stringArray);
    }

    @Override
    public TypeDescriptor[] getTypeDescriptorsFromList(String[][] stringArray) throws SQLException {
        return this.connection.getTypeDescriptorsFromList(stringArray);
    }

    @Override
    public String getDataIntegrityAlgorithmName() throws SQLException {
        return this.connection.getDataIntegrityAlgorithmName();
    }

    @Override
    public String getEncryptionAlgorithmName() throws SQLException {
        return this.connection.getEncryptionAlgorithmName();
    }

    @Override
    public String getAuthenticationAdaptorName() throws SQLException {
        return this.connection.getAuthenticationAdaptorName();
    }

    @Override
    public boolean isUsable() {
        return this.connection.isUsable();
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    @Override
    public void setDefaultTimeZone(TimeZone timeZone) throws SQLException {
        this.connection.setDefaultTimeZone(timeZone);
    }

    @Override
    public TimeZone getDefaultTimeZone() throws SQLException {
        return this.connection.getDefaultTimeZone();
    }

    @Override
    public void setApplicationContext(String string, String string2, String string3) throws SQLException {
        this.connection.setApplicationContext(string, string2, string3);
    }

    @Override
    public void clearAllApplicationContext(String string) throws SQLException {
        this.connection.clearAllApplicationContext(string);
    }

    @Override
    public void addLogicalTransactionIdEventListener(LogicalTransactionIdEventListener logicalTransactionIdEventListener) throws SQLException {
        this.connection.addLogicalTransactionIdEventListener(logicalTransactionIdEventListener);
    }

    @Override
    public void addLogicalTransactionIdEventListener(LogicalTransactionIdEventListener logicalTransactionIdEventListener, Executor executor) throws SQLException {
        this.connection.addLogicalTransactionIdEventListener(logicalTransactionIdEventListener, executor);
    }

    @Override
    public void removeLogicalTransactionIdEventListener(LogicalTransactionIdEventListener logicalTransactionIdEventListener) throws SQLException {
        this.connection.removeLogicalTransactionIdEventListener(logicalTransactionIdEventListener);
    }

    @Override
    public LogicalTransactionId getLogicalTransactionId() throws SQLException {
        return this.connection.getLogicalTransactionId();
    }

    @Override
    public boolean needToPurgeStatementCache() throws SQLException {
        return this.connection.needToPurgeStatementCache();
    }

    @Override
    public boolean attachServerConnection() throws SQLException {
        return this.connection.attachServerConnection();
    }

    @Override
    public void detachServerConnection(String string) throws SQLException {
        this.connection.detachServerConnection(string);
    }

    @Override
    public boolean isDRCPEnabled() throws SQLException {
        return this.connection.isDRCPEnabled();
    }

    @Override
    public boolean isDRCPMultitagEnabled() throws SQLException {
        return this.connection.isDRCPMultitagEnabled();
    }

    @Override
    public String getDRCPReturnTag() throws SQLException {
        return this.connection.getDRCPReturnTag();
    }

    @Override
    public String getDRCPPLSQLCallbackName() throws SQLException {
        return this.connection.getDRCPPLSQLCallbackName();
    }

    @Override
    public void beginRequest() throws SQLException {
        this.connection.beginRequest();
    }

    @Override
    public void endRequest() throws SQLException {
        this.connection.endRequest();
    }

    @Override
    public void setShardingKey(OracleShardingKey oracleShardingKey, OracleShardingKey oracleShardingKey2) throws SQLException {
        this.connection.setShardingKey(oracleShardingKey, oracleShardingKey2);
    }

    @Override
    public boolean setShardingKeyIfValid(OracleShardingKey oracleShardingKey, OracleShardingKey oracleShardingKey2, int n2) throws SQLException {
        return this.connection.setShardingKeyIfValid(oracleShardingKey, oracleShardingKey2, n2);
    }

    @Override
    public boolean setShardingKeyIfValid(OracleShardingKey oracleShardingKey, int n2) throws SQLException {
        return this.connection.setShardingKeyIfValid(oracleShardingKey, n2);
    }

    @Override
    public void setShardingKey(OracleShardingKey oracleShardingKey) throws SQLException {
        this.connection.setShardingKey(oracleShardingKey);
    }

    @Override
    public OracleConnection.DRCPState getDRCPState() throws SQLException {
        return this.connection.getDRCPState();
    }

    @Override
    public boolean isValid(OracleConnection.ConnectionValidation connectionValidation, int n2) throws SQLException {
        return this.connection.isValid(connectionValidation, n2);
    }

    @Override
    public String getEncryptionProviderName() throws SQLException {
        return this.connection.getEncryptionProviderName();
    }

    @Override
    public String getChecksumProviderName() throws SQLException {
        return this.connection.getChecksumProviderName();
    }

    @Override
    public String getNetConnectionId() throws SQLException {
        return this.connection.getNetConnectionId();
    }

    @Override
    public void disableLogging() throws SQLException {
        this.connection.disableLogging();
    }

    @Override
    public void enableLogging() throws SQLException {
        this.connection.enableLogging();
    }

    @Override
    public void dumpLog() throws SQLException {
        this.connection.dumpLog();
    }

    @Override
    public SecuredLogger getLogger() throws SQLException {
        return this.connection.getLogger();
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }

    protected class CloseInvocationHandler
    implements InvocationHandler {
        private OracleConnectionWrapper wrapper;

        protected CloseInvocationHandler(OracleConnectionWrapper oracleConnectionWrapper2) {
            this.wrapper = oracleConnectionWrapper2;
        }

        @Override
        public Object invoke(Object object, Method method, Object[] objectArray) throws Throwable {
            try {
                return method.invoke(this.wrapper, objectArray);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                return method.invoke(this.wrapper.connection, objectArray);
            }
        }
    }
}

