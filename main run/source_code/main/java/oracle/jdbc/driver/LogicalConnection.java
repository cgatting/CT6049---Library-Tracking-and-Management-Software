/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.OutputStream;
import java.net.SocketException;
import java.security.AccessController;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import javax.transaction.xa.XAResource;
import oracle.jdbc.LogicalTransactionId;
import oracle.jdbc.LogicalTransactionIdEventListener;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.driver.ClosedConnection;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.HAManager;
import oracle.jdbc.driver.OracleCloseCallback;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.internal.ClientDataSupport;
import oracle.jdbc.internal.DatabaseSessionState;
import oracle.jdbc.internal.JMSDequeueOptions;
import oracle.jdbc.internal.JMSEnqueueOptions;
import oracle.jdbc.internal.JMSMessage;
import oracle.jdbc.internal.JMSNotificationRegistration;
import oracle.jdbc.internal.KeywordValueLong;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.NetStat;
import oracle.jdbc.internal.OracleArray;
import oracle.jdbc.internal.OracleBfile;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleLargeObject;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.internal.PDBChangeEventListener;
import oracle.jdbc.internal.ReplayContext;
import oracle.jdbc.internal.ResultSetCache;
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
import oracle.jdbc.pool.OraclePooledConnection;
import oracle.sql.ArrayDescriptor;
import oracle.sql.BFILE;
import oracle.sql.BLOB;
import oracle.sql.BfileDBAccess;
import oracle.sql.BlobDBAccess;
import oracle.sql.CLOB;
import oracle.sql.ClobDBAccess;
import oracle.sql.CustomDatum;
import oracle.sql.Datum;
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TIMEZONETAB;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
public class LogicalConnection
extends OracleConnection {
    static final ClosedConnection closedConnection = new ClosedConnection();
    protected oracle.jdbc.internal.OracleConnection internalConnection;
    OraclePooledConnection pooledConnection;
    boolean closed;
    OracleCloseCallback closeCallback = null;
    Object privateData = null;
    LogicalTransactionId ltxidBeforeLogicalClose = null;

    protected LogicalConnection(OraclePooledConnection oraclePooledConnection, oracle.jdbc.internal.OracleConnection oracleConnection, boolean bl) throws SQLException {
        this.internalConnection = oracleConnection;
        this.pooledConnection = oraclePooledConnection;
        this.connection = this.internalConnection;
        this.connection.setWrapper(this);
        this.closed = false;
        this.internalConnection.setAutoCommit(bl);
    }

    @Override
    public oracle.jdbc.internal.OracleConnection physicalConnectionWithin() {
        return this.internalConnection;
    }

    public void registerCloseCallback(OracleCloseCallback oracleCloseCallback, Object object) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.closeCallback = oracleCloseCallback;
            this.privateData = object;
        }
    }

    @Override
    public Connection _getPC() {
        return this.internalConnection;
    }

    @Override
    public boolean isLogicalConnection() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = true;
            return bl;
        }
    }

    @Override
    public oracle.jdbc.internal.OracleConnection getPhysicalConnection() {
        return this.internalConnection;
    }

    @Override
    public Connection getLogicalConnection(OraclePooledConnection oraclePooledConnection, boolean bl) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 153).fillInStackTrace();
    }

    @Override
    public void getPropertyForPooledConnection(OraclePooledConnection oraclePooledConnection) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 153).fillInStackTrace();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.beginNonRequestCalls();
            boolean bl = true;
            try {
                bl = this.internalConnection.isUsable();
            }
            catch (Throwable throwable) {
                bl = false;
            }
            finally {
                this.endNonRequestCalls();
            }
            this.closeInternal(bl);
        }
    }

    @Override
    public void closeInternal(boolean bl) throws SQLException {
        String string;
        if (this.closed) {
            return;
        }
        if (this.closeCallback != null) {
            this.closeCallback.beforeClose(this, this.privateData);
        }
        if ((string = LogicalConnection.getSystemProperty("oracle.jdbc.noImplicitBeginRequest", "false")).equalsIgnoreCase("false") && !this.internalConnection.isDRCPEnabled()) {
            this.internalConnection.endRequest();
        }
        this.internalConnection.closeLogicalConnection();
        this.closed = true;
        if (this.pooledConnection != null) {
            this.pooledConnection.logicalClose(bl);
        }
        try {
            this.ltxidBeforeLogicalClose = this.internalConnection.getLogicalTransactionId();
        }
        catch (SQLFeatureNotSupportedException sQLFeatureNotSupportedException) {
            // empty catch block
        }
        this.internalConnection = closedConnection;
        this.connection = closedConnection;
        if (this.closeCallback != null) {
            this.closeCallback.afterClose(this.privateData);
        }
    }

    @Override
    public void cleanupAndClose(boolean bl) throws SQLException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        oracle.jdbc.internal.OracleConnection oracleConnection = this.internalConnection;
        OraclePooledConnection oraclePooledConnection = this.pooledConnection;
        this.internalConnection = closedConnection;
        this.connection = closedConnection;
        if (this.closeCallback != null) {
            this.closeCallback.beforeClose(this, this.privateData);
        }
        oracleConnection.cleanupAndClose();
        oracleConnection.closeLogicalConnection();
        if (oraclePooledConnection != null && bl) {
            oraclePooledConnection.logicalClose();
        }
        if (this.closeCallback != null) {
            this.closeCallback.afterClose(this.privateData);
        }
    }

    @Override
    public void abort() throws SQLException {
        if (this.closed) {
            return;
        }
        this.internalConnection.abort();
        this.closed = true;
        this.internalConnection = closedConnection;
        this.connection = closedConnection;
    }

    @Override
    public void close(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if ((n2 & 0x1000) != 0) {
                if (this.pooledConnection != null) {
                    this.pooledConnection.closeOption = n2;
                }
                this.close();
                return;
            }
            if ((n2 & 1) != 0) {
                this.internalConnection.close(1);
            }
            return;
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.closed || this.internalConnection.isClosed();
            return bl;
        }
    }

    @Override
    public String getDatabaseTimeZone() throws SQLException {
        return this.internalConnection.getDatabaseTimeZone();
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getServerSessionInfo() throws SQLException {
        return this.internalConnection.getServerSessionInfo();
    }

    @Override
    public Object getClientData(Object object) {
        return ((ClientDataSupport)((Object)this.internalConnection)).getClientData(object);
    }

    @Override
    public Object setClientData(Object object, Object object2) {
        return ((ClientDataSupport)((Object)this.internalConnection)).setClientData(object, object2);
    }

    @Override
    public Object removeClientData(Object object) {
        return ((ClientDataSupport)((Object)this.internalConnection)).removeClientData(object);
    }

    @Override
    public void setClientIdentifier(String string) throws SQLException {
        this.internalConnection.setClientIdentifier(string);
    }

    @Override
    public void clearClientIdentifier(String string) throws SQLException {
        this.internalConnection.clearClientIdentifier(string);
    }

    @Override
    public short getStructAttrNCsId() throws SQLException {
        return this.internalConnection.getStructAttrNCsId();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.internalConnection.getTypeMap();
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getDBAccessProperties() throws SQLException {
        return this.internalConnection.getDBAccessProperties();
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getOCIHandles() throws SQLException {
        return this.internalConnection.getOCIHandles();
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        return this.internalConnection.getDatabaseProductVersion();
    }

    @Override
    public void cancel() throws SQLException {
        this.internalConnection.cancel();
    }

    @Override
    public String getURL() throws SQLException {
        return this.internalConnection.getURL();
    }

    @Override
    public boolean getIncludeSynonyms() {
        return this.internalConnection.getIncludeSynonyms();
    }

    @Override
    public boolean getRemarksReporting() {
        return this.internalConnection.getRemarksReporting();
    }

    @Override
    public boolean getRestrictGetTables() {
        return this.internalConnection.getRestrictGetTables();
    }

    @Override
    public short getVersionNumber() throws SQLException {
        return this.internalConnection.getVersionNumber();
    }

    @Override
    public Map<String, Class<?>> getJavaObjectTypeMap() {
        return this.internalConnection.getJavaObjectTypeMap();
    }

    @Override
    public void setJavaObjectTypeMap(Map<String, Class<?>> map) {
        this.internalConnection.setJavaObjectTypeMap(map);
    }

    @Override
    public BfileDBAccess createBfileDBAccess() throws SQLException {
        return this.internalConnection.createBfileDBAccess();
    }

    @Override
    public BlobDBAccess createBlobDBAccess() throws SQLException {
        return this.internalConnection.createBlobDBAccess();
    }

    @Override
    public ClobDBAccess createClobDBAccess() throws SQLException {
        return this.internalConnection.createClobDBAccess();
    }

    @Override
    public void setDefaultFixedString(boolean bl) {
        this.internalConnection.setDefaultFixedString(bl);
    }

    @Override
    public boolean getTimestamptzInGmt() {
        return this.internalConnection.getTimestamptzInGmt();
    }

    @Override
    public boolean getUse1900AsYearForTime() {
        return this.internalConnection.getUse1900AsYearForTime();
    }

    @Override
    public boolean getDefaultFixedString() {
        return this.internalConnection.getDefaultFixedString();
    }

    @Override
    public oracle.jdbc.OracleConnection getWrapper() {
        return this;
    }

    @Override
    public Class<?> classForNameAndSchema(String string, String string2) throws ClassNotFoundException {
        return this.internalConnection.classForNameAndSchema(string, string2);
    }

    @Override
    public void setFDO(byte[] byArray) throws SQLException {
        this.internalConnection.setFDO(byArray);
    }

    @Override
    public byte[] getFDO(boolean bl) throws SQLException {
        return this.internalConnection.getFDO(bl);
    }

    @Override
    public boolean getBigEndian() throws SQLException {
        return this.internalConnection.getBigEndian();
    }

    @Override
    public Object getDescriptor(byte[] byArray) {
        return this.internalConnection.getDescriptor(byArray);
    }

    @Override
    public void putDescriptor(byte[] byArray, Object object) throws SQLException {
        this.internalConnection.putDescriptor(byArray, object);
    }

    @Override
    public void removeDescriptor(String string) {
        this.internalConnection.removeDescriptor(string);
    }

    @Override
    public void removeAllDescriptor() {
        this.internalConnection.removeAllDescriptor();
    }

    @Override
    public int numberOfDescriptorCacheEntries() {
        return this.internalConnection.numberOfDescriptorCacheEntries();
    }

    @Override
    public Enumeration<String> descriptorCacheKeys() {
        return this.internalConnection.descriptorCacheKeys();
    }

    @Override
    public short getDbCsId() throws SQLException {
        return this.internalConnection.getDbCsId();
    }

    @Override
    public short getJdbcCsId() throws SQLException {
        return this.internalConnection.getJdbcCsId();
    }

    @Override
    public short getNCharSet() {
        return this.internalConnection.getNCharSet();
    }

    @Override
    public ResultSet newArrayDataResultSet(Datum[] datumArray, long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        return this.internalConnection.newArrayDataResultSet(datumArray, l2, n2, map);
    }

    @Override
    public ResultSet newArrayDataResultSet(OracleArray oracleArray, long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        return this.internalConnection.newArrayDataResultSet(oracleArray, l2, n2, map);
    }

    @Override
    public ResultSet newArrayLocatorResultSet(ArrayDescriptor arrayDescriptor, byte[] byArray, long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        return this.internalConnection.newArrayLocatorResultSet(arrayDescriptor, byArray, l2, n2, map);
    }

    @Override
    public ResultSetMetaData newStructMetaData(StructDescriptor structDescriptor) throws SQLException {
        return this.internalConnection.newStructMetaData(structDescriptor);
    }

    @Override
    public void getForm(OracleTypeADT oracleTypeADT, OracleTypeCLOB oracleTypeCLOB, int n2) throws SQLException {
        this.internalConnection.getForm(oracleTypeADT, oracleTypeCLOB, n2);
    }

    @Override
    public int CHARBytesToJavaChars(byte[] byArray, int n2, char[] cArray) throws SQLException {
        return this.internalConnection.CHARBytesToJavaChars(byArray, n2, cArray);
    }

    @Override
    public int NCHARBytesToJavaChars(byte[] byArray, int n2, char[] cArray) throws SQLException {
        return this.internalConnection.NCHARBytesToJavaChars(byArray, n2, cArray);
    }

    @Override
    public boolean IsNCharFixedWith() {
        return this.internalConnection.IsNCharFixedWith();
    }

    @Override
    public short getDriverCharSet() {
        return this.internalConnection.getDriverCharSet();
    }

    @Override
    public int getC2SNlsRatio() {
        return this.internalConnection.getC2SNlsRatio();
    }

    @Override
    public int getMaxCharSize() throws SQLException {
        return this.internalConnection.getMaxCharSize();
    }

    @Override
    public int getMaxCharbyteSize() {
        return this.internalConnection.getMaxCharbyteSize();
    }

    @Override
    public int getMaxNCharbyteSize() {
        return this.internalConnection.getMaxNCharbyteSize();
    }

    @Override
    public boolean isCharSetMultibyte(short s2) {
        return this.internalConnection.isCharSetMultibyte(s2);
    }

    @Override
    public int javaCharsToCHARBytes(char[] cArray, int n2, byte[] byArray) throws SQLException {
        return this.internalConnection.javaCharsToCHARBytes(cArray, n2, byArray);
    }

    @Override
    public int javaCharsToNCHARBytes(char[] cArray, int n2, byte[] byArray) throws SQLException {
        return this.internalConnection.javaCharsToNCHARBytes(cArray, n2, byArray);
    }

    @Override
    public int getStmtCacheSize() {
        return this.internalConnection.getStmtCacheSize();
    }

    @Override
    public int getStatementCacheSize() throws SQLException {
        return this.internalConnection.getStatementCacheSize();
    }

    @Override
    public boolean getImplicitCachingEnabled() throws SQLException {
        return this.internalConnection.getImplicitCachingEnabled();
    }

    @Override
    public boolean getExplicitCachingEnabled() throws SQLException {
        return this.internalConnection.getExplicitCachingEnabled();
    }

    @Override
    public void purgeImplicitCache() throws SQLException {
        this.internalConnection.purgeImplicitCache();
    }

    @Override
    public void purgeExplicitCache() throws SQLException {
        this.internalConnection.purgeExplicitCache();
    }

    @Override
    public PreparedStatement getStatementWithKey(String string) throws SQLException {
        return this.internalConnection.getStatementWithKey(string);
    }

    @Override
    public CallableStatement getCallWithKey(String string) throws SQLException {
        return this.internalConnection.getCallWithKey(string);
    }

    @Override
    public boolean isStatementCacheInitialized() {
        return this.internalConnection.isStatementCacheInitialized();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.internalConnection.setTypeMap(map);
    }

    @Override
    public String getProtocolType() {
        return this.internalConnection.getProtocolType();
    }

    @Override
    public void setTxnMode(int n2) {
        this.internalConnection.setTxnMode(n2);
    }

    @Override
    public int getTxnMode() {
        return this.internalConnection.getTxnMode();
    }

    @Override
    public int getHeapAllocSize() throws SQLException {
        return this.internalConnection.getHeapAllocSize();
    }

    @Override
    public int getOCIEnvHeapAllocSize() throws SQLException {
        return this.internalConnection.getOCIEnvHeapAllocSize();
    }

    @Override
    public CLOB createClob(byte[] byArray) throws SQLException {
        return this.internalConnection.createClob(byArray);
    }

    @Override
    public CLOB createClobWithUnpickledBytes(byte[] byArray) throws SQLException {
        return this.internalConnection.createClobWithUnpickledBytes(byArray);
    }

    @Override
    public CLOB createClob(byte[] byArray, short s2) throws SQLException {
        return this.internalConnection.createClob(byArray, s2);
    }

    @Override
    public BLOB createBlob(byte[] byArray) throws SQLException {
        return this.internalConnection.createBlob(byArray);
    }

    @Override
    public BLOB createBlobWithUnpickledBytes(byte[] byArray) throws SQLException {
        return this.internalConnection.createBlobWithUnpickledBytes(byArray);
    }

    @Override
    public BFILE createBfile(byte[] byArray) throws SQLException {
        return this.internalConnection.createBfile(byArray);
    }

    @Override
    public boolean isDescriptorSharable(oracle.jdbc.internal.OracleConnection oracleConnection) throws SQLException {
        return this.internalConnection.isDescriptorSharable(oracleConnection);
    }

    @Override
    public OracleStatement refCursorCursorToStatement(int n2) throws SQLException {
        return this.internalConnection.refCursorCursorToStatement(n2);
    }

    @Override
    public long getTdoCState(String string, String string2) throws SQLException {
        return this.internalConnection.getTdoCState(string, string2);
    }

    @Override
    public long getTdoCState(String string) throws SQLException {
        return this.internalConnection.getTdoCState(string);
    }

    @Override
    public Datum toDatum(CustomDatum customDatum) throws SQLException {
        return this.internalConnection.toDatum(customDatum);
    }

    @Override
    public XAResource getXAResource() throws SQLException {
        return this.pooledConnection.getXAResource();
    }

    @Override
    public void setApplicationContext(String string, String string2, String string3) throws SQLException {
        this.internalConnection.setApplicationContext(string, string2, string3);
    }

    @Override
    public void clearAllApplicationContext(String string) throws SQLException {
        this.internalConnection.clearAllApplicationContext(string);
    }

    @Override
    @Deprecated
    public boolean isV8Compatible() throws SQLException {
        return this.getMapDateToTimestamp();
    }

    @Override
    public boolean getMapDateToTimestamp() {
        return this.internalConnection.getMapDateToTimestamp();
    }

    @Override
    public boolean getJDBCStandardBehavior() {
        return this.internalConnection.getJDBCStandardBehavior();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        this.internalConnection.abort(executor);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return this.internalConnection.getNetworkTimeout();
    }

    @Override
    public String getSchema() throws SQLException {
        return this.internalConnection.getSchema();
    }

    @Override
    public void setNetworkTimeout(Executor executor, int n2) throws SQLException {
        this.internalConnection.setNetworkTimeout(executor, n2);
    }

    @Override
    public void setSchema(String string) throws SQLException {
        this.internalConnection.setSchema(string);
    }

    @Override
    public byte[] createLightweightSession(String string, KeywordValueLong[] keywordValueLongArray, int n2, KeywordValueLong[][] keywordValueLongArray2, int[] nArray) throws SQLException {
        return this.internalConnection.createLightweightSession(string, keywordValueLongArray, n2, keywordValueLongArray2, nArray);
    }

    @Override
    public void executeLightweightSessionPiggyback(int n2, byte[] byArray, KeywordValueLong[] keywordValueLongArray, int n3) throws SQLException {
        this.internalConnection.executeLightweightSessionPiggyback(n2, byArray, keywordValueLongArray, n3);
    }

    @Override
    public void doXSNamespaceOp(OracleConnection.XSOperationCode xSOperationCode, byte[] byArray, XSNamespace[] xSNamespaceArray, XSNamespace[][] xSNamespaceArray2, XSSecureId xSSecureId) throws SQLException {
        this.internalConnection.doXSNamespaceOp(xSOperationCode, byArray, xSNamespaceArray, xSNamespaceArray2, xSSecureId);
    }

    @Override
    public void doXSNamespaceOp(OracleConnection.XSOperationCode xSOperationCode, byte[] byArray, XSNamespace[] xSNamespaceArray, XSSecureId xSSecureId) throws SQLException {
        this.internalConnection.doXSNamespaceOp(xSOperationCode, byArray, xSNamespaceArray, xSSecureId);
    }

    @Override
    public void doXSSessionAttachOp(int n2, byte[] byArray, XSSecureId xSSecureId, byte[] byArray2, XSPrincipal xSPrincipal, String[] stringArray, String[] stringArray2, String[] stringArray3, XSNamespace[] xSNamespaceArray, XSNamespace[] xSNamespaceArray2, XSNamespace[] xSNamespaceArray3, TIMESTAMPTZ tIMESTAMPTZ, TIMESTAMPTZ tIMESTAMPTZ2, int n3, long l2, XSKeyval xSKeyval, int[] nArray) throws SQLException {
        this.internalConnection.doXSSessionAttachOp(n2, byArray, xSSecureId, byArray2, xSPrincipal, stringArray, stringArray2, stringArray3, xSNamespaceArray, xSNamespaceArray2, xSNamespaceArray3, tIMESTAMPTZ, tIMESTAMPTZ2, n3, l2, xSKeyval, nArray);
    }

    @Override
    public void doXSSessionChangeOp(OracleConnection.XSSessionSetOperationCode xSSessionSetOperationCode, byte[] byArray, XSSecureId xSSecureId, XSSessionParameters xSSessionParameters) throws SQLException {
        this.internalConnection.doXSSessionChangeOp(xSSessionSetOperationCode, byArray, xSSecureId, xSSessionParameters);
    }

    @Override
    public byte[] doXSSessionCreateOp(OracleConnection.XSSessionOperationCode xSSessionOperationCode, XSSecureId xSSecureId, byte[] byArray, XSPrincipal xSPrincipal, String string, XSNamespace[] xSNamespaceArray, OracleConnection.XSSessionModeFlag xSSessionModeFlag, XSKeyval xSKeyval) throws SQLException {
        return this.internalConnection.doXSSessionCreateOp(xSSessionOperationCode, xSSecureId, byArray, xSPrincipal, string, xSNamespaceArray, xSSessionModeFlag, xSKeyval);
    }

    @Override
    public void doXSSessionDestroyOp(byte[] byArray, XSSecureId xSSecureId, byte[] byArray2) throws SQLException {
        this.internalConnection.doXSSessionDestroyOp(byArray, xSSecureId, byArray2);
    }

    @Override
    public void doXSSessionDetachOp(int n2, byte[] byArray, XSSecureId xSSecureId, boolean bl) throws SQLException {
        this.internalConnection.doXSSessionDetachOp(n2, byArray, xSSecureId, bl);
    }

    public BLOB createTemporaryBlob(Connection connection, boolean bl, int n2) throws SQLException {
        return this.internalConnection.createBlobDBAccess().createTemporaryBlob(connection, bl, n2);
    }

    public CLOB createTemporaryClob(Connection connection, boolean bl, int n2, short s2) throws SQLException {
        return this.internalConnection.createClobDBAccess().createTemporaryClob(connection, bl, n2, s2);
    }

    @Override
    public String getDefaultSchemaNameForNamedTypes() throws SQLException {
        return this.internalConnection.getDefaultSchemaNameForNamedTypes();
    }

    @Override
    public boolean isUsable() {
        return this.isUsable(true);
    }

    @Override
    public boolean isUsable(boolean bl) {
        return !this.closed && this.internalConnection.isUsable(bl);
    }

    @Override
    public byte getInstanceProperty(OracleConnection.InstanceProperty instanceProperty) throws SQLException {
        return this.internalConnection.getInstanceProperty(instanceProperty);
    }

    @Override
    public void setUsable(boolean bl) {
        this.internalConnection.setUsable(bl);
    }

    @Override
    public int getTimezoneVersionNumber() throws SQLException {
        return this.internalConnection.getTimezoneVersionNumber();
    }

    @Override
    public TIMEZONETAB getTIMEZONETAB() throws SQLException {
        return this.internalConnection.getTIMEZONETAB();
    }

    @Override
    public void setPDBChangeEventListener(PDBChangeEventListener pDBChangeEventListener) throws SQLException {
        this.internalConnection.setPDBChangeEventListener(pDBChangeEventListener);
    }

    @Override
    public void setPDBChangeEventListener(PDBChangeEventListener pDBChangeEventListener, Executor executor) throws SQLException {
        this.internalConnection.setPDBChangeEventListener(pDBChangeEventListener, executor);
    }

    @Override
    public void addXSEventListener(XSEventListener xSEventListener) throws SQLException {
        this.internalConnection.addXSEventListener(xSEventListener);
    }

    @Override
    public void addXSEventListener(XSEventListener xSEventListener, Executor executor) throws SQLException {
        this.internalConnection.addXSEventListener(xSEventListener, executor);
    }

    @Override
    public void removeXSEventListener(XSEventListener xSEventListener) throws SQLException {
        this.internalConnection.removeXSEventListener(xSEventListener);
    }

    @Override
    public void removeAllXSEventListener() throws SQLException {
        this.internalConnection.removeAllXSEventListener();
    }

    @Override
    public OracleConnection.BufferCacheStatistics getByteBufferCacheStatistics() {
        return this.internalConnection.getByteBufferCacheStatistics();
    }

    @Override
    public OracleConnection.BufferCacheStatistics getCharBufferCacheStatistics() {
        return this.internalConnection.getCharBufferCacheStatistics();
    }

    @Override
    public boolean isDataInLocatorEnabled() throws SQLException {
        return this.internalConnection.isDataInLocatorEnabled();
    }

    @Override
    public boolean isLobStreamPosStandardCompliant() throws SQLException {
        return this.internalConnection.isLobStreamPosStandardCompliant();
    }

    @Override
    public long getCurrentSCN() throws SQLException {
        return this.internalConnection.getCurrentSCN();
    }

    @Override
    public EnumSet<OracleConnection.TransactionState> getTransactionState() throws SQLException {
        return this.internalConnection.getTransactionState();
    }

    @Override
    public boolean isConnectionSocketKeepAlive() throws SocketException, SQLException {
        return this.internalConnection.isConnectionSocketKeepAlive();
    }

    @Override
    public boolean isConnectionBigTZTC() throws SQLException {
        return this.internalConnection.isConnectionBigTZTC();
    }

    @Override
    public boolean serverSupportsRequestBoundaries() throws SQLException {
        return this.internalConnection.serverSupportsRequestBoundaries();
    }

    @Override
    public boolean serverSupportsExplicitBoundaryBit() throws SQLException {
        return this.internalConnection.serverSupportsExplicitBoundaryBit();
    }

    @Override
    public void removeLogicalTransactionIdEventListener(LogicalTransactionIdEventListener logicalTransactionIdEventListener) throws SQLException {
        this.connection.removeLogicalTransactionIdEventListener(logicalTransactionIdEventListener);
    }

    @Override
    public void setReplayOperations(EnumSet<OracleConnection.ReplayOperation> enumSet) throws SQLException {
        this.internalConnection.setReplayOperations(enumSet);
    }

    @Override
    public void beginNonRequestCalls() throws SQLException {
        this.internalConnection.beginNonRequestCalls();
    }

    @Override
    public void endNonRequestCalls() throws SQLException {
        this.internalConnection.endNonRequestCalls();
    }

    @Override
    public void setReplayContext(ReplayContext[] replayContextArray) throws SQLException {
        this.internalConnection.setReplayContext(replayContextArray);
    }

    @Override
    public void registerEndReplayCallback(OracleConnection.EndReplayCallback endReplayCallback) throws SQLException {
        this.internalConnection.registerEndReplayCallback(endReplayCallback);
    }

    @Override
    public int getEOC() throws SQLException {
        return this.internalConnection.getEOC();
    }

    @Override
    public ReplayContext[] getReplayContext() throws SQLException {
        return this.internalConnection.getReplayContext();
    }

    @Override
    public ReplayContext getLastReplayContext() throws SQLException {
        return this.internalConnection.getLastReplayContext();
    }

    @Override
    public void setLastReplayContext(ReplayContext replayContext) throws SQLException {
        this.internalConnection.setLastReplayContext(replayContext);
    }

    @Override
    public byte[] getDerivedKeyInternal(byte[] byArray, int n2) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
        return this.internalConnection.getDerivedKeyInternal(byArray, n2);
    }

    @Override
    public short getExecutingRPCFunctionCode() {
        return this.internalConnection.getExecutingRPCFunctionCode();
    }

    @Override
    public String getExecutingRPCSQL() {
        return this.internalConnection.getExecutingRPCSQL();
    }

    @Override
    public void setReplayingMode(boolean bl) throws SQLException {
        this.internalConnection.setReplayingMode(bl);
    }

    @Override
    public void jmsEnqueue(String string, JMSEnqueueOptions jMSEnqueueOptions, JMSMessage jMSMessage, AQMessageProperties aQMessageProperties) throws SQLException {
        this.internalConnection.jmsEnqueue(string, jMSEnqueueOptions, jMSMessage, aQMessageProperties);
    }

    @Override
    public void jmsEnqueue(String string, JMSEnqueueOptions jMSEnqueueOptions, JMSMessage[] jMSMessageArray, AQMessageProperties[] aQMessagePropertiesArray) throws SQLException {
        this.internalConnection.jmsEnqueue(string, jMSEnqueueOptions, jMSMessageArray, aQMessagePropertiesArray);
    }

    @Override
    public JMSMessage jmsDequeue(String string, JMSDequeueOptions jMSDequeueOptions) throws SQLException {
        JMSMessage jMSMessage = this.internalConnection.jmsDequeue(string, jMSDequeueOptions);
        return jMSMessage;
    }

    @Override
    public JMSMessage[] jmsDequeue(String string, JMSDequeueOptions jMSDequeueOptions, int n2) throws SQLException {
        JMSMessage[] jMSMessageArray = this.internalConnection.jmsDequeue(string, jMSDequeueOptions, n2);
        return jMSMessageArray;
    }

    @Override
    public JMSMessage jmsDequeue(String string, JMSDequeueOptions jMSDequeueOptions, OutputStream outputStream) throws SQLException {
        JMSMessage jMSMessage = this.internalConnection.jmsDequeue(string, jMSDequeueOptions, outputStream);
        return jMSMessage;
    }

    @Override
    public JMSMessage jmsDequeue(String string, JMSDequeueOptions jMSDequeueOptions, String string2) throws SQLException {
        JMSMessage jMSMessage = this.internalConnection.jmsDequeue(string, jMSDequeueOptions, string2);
        return jMSMessage;
    }

    @Override
    public Map<String, JMSNotificationRegistration> registerJMSNotification(String[] stringArray, Map<String, Properties> map, String string) throws SQLException {
        Map<String, JMSNotificationRegistration> map2 = this.internalConnection.registerJMSNotification(stringArray, map, string);
        return map2;
    }

    @Override
    public Map<String, JMSNotificationRegistration> registerJMSNotification(String[] stringArray, Map<String, Properties> map) throws SQLException {
        Map<String, JMSNotificationRegistration> map2 = this.internalConnection.registerJMSNotification(stringArray, map);
        return map2;
    }

    @Override
    public void unregisterJMSNotification(JMSNotificationRegistration jMSNotificationRegistration) throws SQLException {
        this.internalConnection.unregisterJMSNotification(jMSNotificationRegistration);
    }

    @Override
    public void startJMSNotification(JMSNotificationRegistration jMSNotificationRegistration) throws SQLException {
        this.internalConnection.startJMSNotification(jMSNotificationRegistration);
    }

    @Override
    public void stopJMSNotification(JMSNotificationRegistration jMSNotificationRegistration) throws SQLException {
        this.internalConnection.stopJMSNotification(jMSNotificationRegistration);
    }

    @Override
    public void ackJMSNotification(JMSNotificationRegistration jMSNotificationRegistration, byte[] byArray, JMSNotificationRegistration.Directive directive) throws SQLException {
        this.internalConnection.ackJMSNotification(jMSNotificationRegistration, byArray, directive);
    }

    @Override
    public void ackJMSNotification(ArrayList<JMSNotificationRegistration> arrayList, byte[][] byArray, JMSNotificationRegistration.Directive directive) throws SQLException {
        this.internalConnection.ackJMSNotification(arrayList, byArray, directive);
    }

    @Override
    public boolean isDRCPEnabled() throws SQLException {
        return this.internalConnection.isDRCPEnabled();
    }

    @Override
    public boolean isDRCPMultitagEnabled() throws SQLException {
        return this.internalConnection.isDRCPMultitagEnabled();
    }

    @Override
    public String getDRCPReturnTag() throws SQLException {
        return this.internalConnection.getDRCPReturnTag();
    }

    @Override
    public String getDRCPPLSQLCallbackName() throws SQLException {
        return this.internalConnection.getDRCPPLSQLCallbackName();
    }

    @Override
    public boolean attachServerConnection() throws SQLException {
        return this.internalConnection.attachServerConnection();
    }

    @Override
    public void detachServerConnection(String string) throws SQLException {
        this.internalConnection.detachServerConnection(string);
    }

    @Override
    public PreparedStatement prepareDirectPath(String string, String string2, String[] stringArray) throws SQLException {
        return this.internalConnection.prepareDirectPath(string, string2, stringArray);
    }

    @Override
    public PreparedStatement prepareDirectPath(String string, String string2, String[] stringArray, String string3) throws SQLException {
        return this.internalConnection.prepareDirectPath(string, string2, stringArray, string3);
    }

    @Override
    public PreparedStatement prepareDirectPath(String string, String string2, String[] stringArray, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        return this.internalConnection.prepareDirectPath(string, string2, stringArray, properties);
    }

    @Override
    public PreparedStatement prepareDirectPath(String string, String string2, String[] stringArray, String string3, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        return this.internalConnection.prepareDirectPath(string, string2, stringArray, properties);
    }

    @Override
    public boolean needToPurgeStatementCache() throws SQLException {
        return this.internalConnection.needToPurgeStatementCache();
    }

    @Override
    public int getNegotiatedSDU() throws SQLException {
        return this.internalConnection.getNegotiatedSDU();
    }

    @Override
    public byte getNegotiatedTTCVersion() throws SQLException {
        return this.internalConnection.getNegotiatedTTCVersion();
    }

    @Override
    public int getVarTypeMaxLenCompat() throws SQLException {
        return this.internalConnection.getVarTypeMaxLenCompat();
    }

    @Override
    public void setChecksumMode(OracleConnection.ChecksumMode checksumMode) throws SQLException {
        this.internalConnection.setChecksumMode(checksumMode);
    }

    @Override
    public ResultSetCache getResultSetCache() throws SQLException {
        return this.internalConnection.getResultSetCache();
    }

    @Override
    public void cleanupAndClose() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 153).fillInStackTrace();
    }

    @Override
    public void closeLogicalConnection() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 153).fillInStackTrace();
    }

    @Override
    public boolean isLifecycleOpen() throws SQLException {
        return this.internalConnection.isLifecycleOpen();
    }

    @Override
    public void clearDrcpTagName() throws SQLException {
        this.internalConnection.clearDrcpTagName();
    }

    @Override
    public void beginRequest() throws SQLException {
        this.internalConnection.beginRequest();
    }

    @Override
    public void endRequest() throws SQLException {
        this.internalConnection.endRequest();
    }

    @Override
    public void endRequest(boolean bl) throws SQLException {
        this.internalConnection.endRequest(bl);
    }

    @Override
    public void sendRequestFlags() throws SQLException {
        this.internalConnection.sendRequestFlags();
    }

    @Override
    public int freeTemporaryBlobsAndClobs() throws SQLException {
        return this.internalConnection.freeTemporaryBlobsAndClobs();
    }

    @Override
    public HAManager getHAManager() {
        return this.internalConnection.getHAManager();
    }

    @Override
    public void setHAManager(HAManager hAManager) throws SQLException {
        this.internalConnection.setHAManager(hAManager);
    }

    @Override
    public LogicalTransactionId getLogicalTransactionId() throws SQLException {
        return this.internalConnection != closedConnection ? this.internalConnection.getLogicalTransactionId() : this.ltxidBeforeLogicalClose;
    }

    @Override
    public void setChunkInfo(OracleShardingKey oracleShardingKey, OracleShardingKey oracleShardingKey2, String string) throws SQLException {
        this.internalConnection.setChunkInfo(oracleShardingKey, oracleShardingKey2, string);
    }

    @Override
    public void setShardingKey(OracleShardingKey oracleShardingKey, OracleShardingKey oracleShardingKey2) throws SQLException {
        this.internalConnection.setShardingKey(oracleShardingKey, oracleShardingKey2);
    }

    @Override
    public boolean setShardingKeyIfValid(OracleShardingKey oracleShardingKey, OracleShardingKey oracleShardingKey2, int n2) throws SQLException {
        return this.internalConnection.setShardingKeyIfValid(oracleShardingKey, oracleShardingKey2, n2);
    }

    @Override
    public boolean setShardingKeyIfValid(OracleShardingKey oracleShardingKey, int n2) throws SQLException {
        return this.internalConnection.setShardingKeyIfValid(oracleShardingKey, n2);
    }

    @Override
    public void setShardingKey(OracleShardingKey oracleShardingKey) throws SQLException {
        this.internalConnection.setShardingKey(oracleShardingKey);
    }

    @Override
    public boolean isNetworkCompressionEnabled() {
        return this.internalConnection.isNetworkCompressionEnabled();
    }

    @Override
    public NetStat getNetworkStat() {
        return this.internalConnection.getNetworkStat();
    }

    @Override
    public int getOutboundConnectTimeout() {
        return this.internalConnection.getOutboundConnectTimeout();
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

    @Override
    public boolean hasNoOpenHandles() throws SQLException {
        return this.internalConnection.hasNoOpenHandles();
    }

    @Override
    public DatabaseSessionState getDatabaseSessionState() throws SQLException {
        return this.internalConnection.getDatabaseSessionState();
    }

    @Override
    public void setDatabaseSessionState(DatabaseSessionState databaseSessionState) throws SQLException {
        this.internalConnection.setDatabaseSessionState(databaseSessionState);
    }

    @Override
    public boolean isSafelyClosed() throws SQLException {
        return this.internalConnection.isSafelyClosed();
    }

    @Override
    public void setSafelyClosed(boolean bl) throws SQLException {
        this.internalConnection.setSafelyClosed(bl);
    }

    @Override
    public PreparedStatement prepareStatement(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        return this.internalConnection.prepareStatement(string, properties);
    }

    @Override
    public CallableStatement prepareCall(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        return this.internalConnection.prepareCall(string, properties);
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getClientInfoInternal() throws SQLException {
        return this.internalConnection.getClientInfoInternal();
    }

    @Override
    public boolean getAutoCommitInternal() throws SQLException {
        return this.internalConnection.getAutoCommitInternal();
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getJavaNetProperties() throws SQLException {
        return this.internalConnection.getJavaNetProperties();
    }

    @Override
    public double getPercentageQueryExecutionOnDirectShard() {
        return this.internalConnection.getPercentageQueryExecutionOnDirectShard();
    }

    @Override
    public void addLargeObject(OracleLargeObject oracleLargeObject) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 153).fillInStackTrace();
    }

    @Override
    public void removeLargeObject(OracleLargeObject oracleLargeObject) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 153).fillInStackTrace();
    }

    @Override
    public void addBfile(OracleBfile oracleBfile) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 153).fillInStackTrace();
    }

    @Override
    public void removeBfile(OracleBfile oracleBfile) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 153).fillInStackTrace();
    }

    @Override
    public void addFeature(OracleConnection.ClientFeature clientFeature) throws SQLException {
        this.internalConnection.addFeature(clientFeature);
    }

    @Override
    public String getNetConnectionId() throws SQLException {
        return this.internalConnection.getNetConnectionId();
    }
}

