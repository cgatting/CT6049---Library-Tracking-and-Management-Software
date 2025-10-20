/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.CompletionStage;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDatabaseException;
import oracle.jdbc.OracleOCIFailover;
import oracle.jdbc.driver.AutoKeyInfo;
import oracle.jdbc.driver.BuildInfo;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.NTFPDBChangeEvent;
import oracle.jdbc.driver.OracleBlobInputStream;
import oracle.jdbc.driver.OracleBlobOutputStream;
import oracle.jdbc.driver.OracleClobInputStream;
import oracle.jdbc.driver.OracleClobOutputStream;
import oracle.jdbc.driver.OracleClobReader;
import oracle.jdbc.driver.OracleClobWriter;
import oracle.jdbc.driver.OracleConversionInputStream;
import oracle.jdbc.driver.OracleConversionReader;
import oracle.jdbc.driver.OracleDriverExtension;
import oracle.jdbc.driver.OracleSql;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.T2CBanner;
import oracle.jdbc.driver.T2CError;
import oracle.jdbc.driver.T2CStatement;
import oracle.jdbc.internal.AbstractConnectionBuilder;
import oracle.jdbc.internal.CompletionStageUtil;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.internal.OracleBfile;
import oracle.jdbc.internal.OracleBlob;
import oracle.jdbc.internal.OracleClob;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleTypeADT;
import oracle.jdbc.oracore.OracleTypeCLOB;
import oracle.jdbc.pool.OracleOCIConnectionPool;
import oracle.jdbc.pool.OraclePooledConnection;
import oracle.net.resolver.NameResolver;
import oracle.net.resolver.NameResolverFactory;
import oracle.sql.BFILE;
import oracle.sql.BLOB;
import oracle.sql.BfileDBAccess;
import oracle.sql.BlobDBAccess;
import oracle.sql.CLOB;
import oracle.sql.ClobDBAccess;
import oracle.sql.Datum;
import oracle.sql.LobPlsqlUtil;
import oracle.sql.NCLOB;
import oracle.sql.SQLName;
import oracle.sql.ZONEIDMAP;
import oracle.sql.converter.CharacterSetMetaData;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.OCI_INTERNAL})
public class T2CConnection
extends PhysicalConnection
implements BfileDBAccess,
BlobDBAccess,
ClobDBAccess {
    static final long JDBC_OCI_LIBRARY_VERSION = Long.parseLong("21.6.0.0.0".replaceAll("\\.", ""));
    short[] queryMetaData1 = null;
    byte[] queryMetaData2 = null;
    int queryMetaData1Offset = 0;
    int queryMetaData2Offset = 0;
    private OpaqueString password;
    int fatalErrorNumber = 0;
    String fatalErrorMessage = null;
    static final int QMD_dbtype = 0;
    static final int QMD_dbsize = 1;
    static final int QMD_nullok = 2;
    static final int QMD_precision = 3;
    static final int QMD_scale = 4;
    static final int QMD_formOfUse = 5;
    static final int QMD_columnNameLength = 6;
    static final int QMD_tdo0 = 7;
    static final int QMD_tdo1 = 8;
    static final int QMD_tdo2 = 9;
    static final int QMD_tdo3 = 10;
    static final int QMD_charLength = 11;
    static final int QMD_schemaNameLength = 12;
    static final int QMD_typeNameLength = 13;
    static final int QMD_columnInvisible = 14;
    static final int QMD_columnJSON = 15;
    static final int T2C_LOCATOR_MAX_LEN = 16;
    static final int T2C_LINEARIZED_LOCATOR_MAX_LEN = 4000;
    static final int T2C_LINEARIZED_BFILE_LOCATOR_MAX_LEN = 530;
    static final int METADATA1_INDICES_PER_COLUMN = 16;
    protected static final int SIZEOF_QUERYMETADATA2 = 8;
    static final String defaultDriverNameAttribute = "jdbcoci";
    int queryMetaData1Size = 100;
    int queryMetaData2Size = 800;
    long m_nativeState;
    short m_clientCharacterSet;
    byte byteAlign;
    static final byte[] EMPTY_BYTES = new byte[0];
    private static final int EOJ_SUCCESS = 0;
    private static final int EOJ_ERROR = -1;
    private static final int EOJ_WARNING = 1;
    private static final int EOJ_GET_STORAGE_ERROR = -4;
    private static final int EOJ_ORA3113_SERVER_NORMAL = -6;
    private static final String OCILIBRARY = "ocijdbc21";
    private int logon_mode = 0;
    static final int LOGON_MODE_DEFAULT = 0;
    static final int LOGON_MODE_SYSDBA = 2;
    static final int LOGON_MODE_SYSOPER = 4;
    static final int LOGON_MODE_SYSASM = 32768;
    static final int LOGON_MODE_SYSBKP = 131072;
    static final int LOGON_MODE_SYSDGD = 262144;
    static final int LOGON_MODE_SYSKMT = 524288;
    static final int LOGON_MODE_CONNECTION_POOL = 5;
    static final int LOGON_MODE_CONNPOOL_CONNECTION = 6;
    static final int LOGON_MODE_CONNPOOL_PROXY_CONNECTION = 7;
    static final int LOGON_MODE_CONNPOOL_ALIASED_CONNECTION = 8;
    static final int T2C_PROXYTYPE_NONE = 0;
    static final int T2C_PROXYTYPE_USER_NAME = 1;
    static final int T2C_PROXYTYPE_DISTINGUISHED_NAME = 2;
    static final int T2C_PROXYTYPE_CERTIFICATE = 3;
    static final int T2C_CONNECTION_FLAG_DEFAULT_LOB_PREFETCH = 0;
    static final int T2C_CONNECTION_FLAG_PRELIM_AUTH = 1;
    static final int T2C_CONNECTION_FLAG_CHARSET = 2;
    static final int T2C_CONNECTION_FLAG_NCHARSET = 3;
    static final int T2C_CONNECTION_FLAG_BYTE_ALIGN = 4;
    static final int T2C_CONNECTION_FLAG_SERVER_TZ_VERSION = 5;
    static final int T2C_CONNECTION_FLAG_TAF_ENABLED = 6;
    static final int T2C_CONNECTION_TAG_MATCHED = 7;
    static final int T2C_CONNECTION_FLAG_MAXLEN_COMPAT = 8;
    static final int T2C_MAX_SCHEMA_NAME_LENGTH = 258;
    private static boolean isLibraryLoaded;
    private static final Monitor LOAD_LIBRARY_MONITOR;
    private static final Monitor SET_TIMEZONE_MONITOR;
    static final Map<String, String> cachedVersionTable;
    static final int T2C_LOGON_OUT_BUFFER_LENGTH = 256;
    static final int EOO_LOGIN_OUT_TYPE_DBID = 1;
    static final int EOO_LOGIN_OUT_INST_START_TIME = 2;
    String databaseUniqueIdentifier;
    OracleOCIFailover appCallback = null;
    Object appCallbackObject = null;
    private Properties nativeInfo;
    ByteBuffer nioBufferForLob;
    boolean[] tagMatched;
    static final String CONNECT_DATA_KEYWORD = "CONNECT_DATA";
    static final int OCI_SESSRLS_DROPSESS = 1;
    static final int OCI_SESSRLS_RETAG = 2;

    protected T2CConnection(String string, @Blind(value=PropertiesBlinder.class) Properties properties, OracleDriverExtension oracleDriverExtension) throws SQLException {
        super(string, properties, oracleDriverExtension);
        this.allocQueryMetaDataBuffers();
    }

    @Override
    final void initializePassword(OpaqueString opaqueString) throws SQLException {
        this.password = opaqueString;
    }

    private void allocQueryMetaDataBuffers() {
        this.queryMetaData1Offset = 0;
        this.queryMetaData1 = new short[this.queryMetaData1Size * 16];
        this.queryMetaData2Offset = 0;
        this.queryMetaData2 = new byte[this.queryMetaData2Size];
        this.namedTypeAccessorByteLen = 0;
        this.refTypeAccessorByteLen = 0;
    }

    void reallocateQueryMetaData(int n2, int n3) {
        this.queryMetaData1 = null;
        this.queryMetaData2 = null;
        this.queryMetaData1Size = Math.max(n2, this.queryMetaData1Size);
        this.queryMetaData2Size = Math.max(n3, this.queryMetaData2Size);
        this.allocQueryMetaDataBuffers();
    }

    @Override
    protected void readConnectionProperties(String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        super.readConnectionProperties(string, properties);
        this.database = this.translateConnStr(this.database);
        if (null != this.targetInstanceName && !"".equals(this.targetInstanceName)) {
            this.database = this.createNamedInstanceUrl(this.database, this.targetInstanceName);
        }
        this.readOCIConnectionPoolProperties(properties);
    }

    private String translateConnStr(String string) throws SQLException {
        if (string == null || string.equals("")) {
            return string;
        }
        if (string.indexOf(41) != -1) {
            return string;
        }
        if ((string = string.trim()).startsWith("//") || string.matches("[[\\w-]\\.]*:[\\d]*/[[\\w\\$\\#]\\.]*(?i)(:[\\w]*)?(?-i)")) {
            string = string.replaceAll("#", "\\\\#");
            try {
                NameResolver nameResolver = NameResolverFactory.getNameResolver(null, "", "");
                return nameResolver.resolveName(string);
            }
            catch (Exception exception) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 67, string).fillInStackTrace();
            }
        }
        return this.resolveSimple(string);
    }

    private String resolveSimple(String string) throws SQLException {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        String string2 = null;
        boolean bl = false;
        if (string.indexOf(91) != -1) {
            n2 = string.indexOf(93);
            if (n2 == -1) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 67, string).fillInStackTrace();
            }
            bl = true;
        }
        if ((n2 = string.indexOf(58, n2)) == -1) {
            return string;
        }
        n3 = string.indexOf(58, n2 + 1);
        if (n3 == -1) {
            return string;
        }
        n4 = string.indexOf(58, n3 + 1);
        if (n4 != -1) {
            string2 = string.substring(n4);
        } else {
            n4 = n3;
        }
        if (string.indexOf(58, n4 + 1) != -1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 67, string).fillInStackTrace();
        }
        String string3 = null;
        string3 = bl ? string.substring(1, n2 - 1) : string.substring(0, n2);
        String string4 = string.substring(n2 + 1, n3);
        String string5 = string2 == null ? string.substring(n3 + 1, string.length()) : string.substring(n3 + 1, n4);
        String string6 = string2 == null ? "(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=" + string3 + ")(PORT=" + string4 + "))(CONNECT_DATA=(SID=" + string5 + ")))" : "(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=" + string3 + ")(PORT=" + string4 + "))(CONNECT_DATA=(SID=" + string5 + ")(SERVER=" + string2 + ")))";
        return string6;
    }

    private String createNamedInstanceUrl(String string, String string2) {
        int n2;
        StringBuffer stringBuffer = new StringBuffer(string);
        int n3 = stringBuffer.indexOf(CONNECT_DATA_KEYWORD);
        if (n3 != -1 && (n2 = stringBuffer.indexOf("=", n3 + CONNECT_DATA_KEYWORD.length())) != -1) {
            stringBuffer.insert(n2 + 1, "(INSTANCE_NAME=" + string2 + ")");
        }
        String string3 = stringBuffer.toString();
        return string3;
    }

    @Override
    protected void logon(AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) throws SQLException {
        this.tagMatched = new boolean[]{false};
        if (this.database == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 64).fillInStackTrace();
        }
        if (!isLibraryLoaded) {
            T2CConnection.loadNativeLibrary();
        }
        byte[] byArray = new byte[256];
        if (this.ociConnectionPoolIsPooling) {
            this.processOCIConnectionPooling();
        } else {
            int n2;
            int n3;
            byte[] byArray2;
            Object object;
            long l2 = this.ociSvcCtxHandle;
            long l3 = this.ociEnvHandle;
            long l4 = this.ociErrHandle;
            if (l2 != 0L && l3 != 0L) {
                if (this.ociDriverCharset == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89).fillInStackTrace();
                }
                this.m_clientCharacterSet = new Integer(this.ociDriverCharset).shortValue();
                this.conversion = new DBConversion(this.m_clientCharacterSet, this.m_clientCharacterSet, this.m_clientCharacterSet);
                long[] lArray = new long[]{this.defaultLobPrefetchSize, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, this.enableOCIFAN ? 1 : 0};
                this.sqlWarning = this.checkError(this.t2cUseConnection(this.m_nativeState, l3, l2, l4, byArray, lArray), this.sqlWarning);
                this.conversion = new DBConversion((short)(lArray[2] & 0xFFFFL), this.m_clientCharacterSet, (short)(lArray[3] & 0xFFFFL));
                this.byteAlign = (byte)(lArray[4] & 0xFFL);
                this.timeZoneVersionNumber = (int)lArray[5];
                if (lArray[6] != 0L) {
                    this.useOCIDefaultDefines = true;
                }
                this.tagMatched[0] = lArray[7] != 0L;
                this.varTypeMaxLenCompat = (int)lArray[8];
                return;
            }
            if (this.internalLogon == null) {
                this.logon_mode = 0;
            } else if (this.internalLogon.equalsIgnoreCase("SYSDBA")) {
                this.logon_mode = 2;
            } else if (this.internalLogon.equalsIgnoreCase("SYSOPER")) {
                this.logon_mode = 4;
            } else if (this.internalLogon.equalsIgnoreCase("SYSASM")) {
                this.logon_mode = 32768;
            } else if (this.internalLogon.equalsIgnoreCase("SYSBACKUP")) {
                this.logon_mode = 131072;
            } else if (this.internalLogon.equalsIgnoreCase("SYSDG")) {
                this.logon_mode = 262144;
            } else if (this.internalLogon.equalsIgnoreCase("SYSKM")) {
                this.logon_mode = 524288;
            }
            byte[] byArray3 = null;
            byte[] byArray4 = null;
            byte[] byArray5 = null;
            byte[] byArray6 = EMPTY_BYTES;
            byte[] byArray7 = EMPTY_BYTES;
            String string = this.newPasswordValue.get();
            byte[] byArray8 = EMPTY_BYTES;
            byte[] byArray9 = EMPTY_BYTES;
            byte[] byArray10 = EMPTY_BYTES;
            if (string == null && this.setNewPassword != null) {
                string = this.setNewPassword.get();
            }
            this.m_clientCharacterSet = this.nlsLangBackdoor ? T2CConnection.getDriverCharSetIdFromNLS_LANG() : T2CConnection.getClientCharSetId();
            if (string != null) {
                byArray8 = DBConversion.stringToDriverCharBytes(string, this.m_clientCharacterSet);
            }
            if (this.editionName != null) {
                byArray9 = DBConversion.stringToDriverCharBytes(this.editionName, this.m_clientCharacterSet);
            }
            if (this.driverNameAttribute == null) {
                object = "jdbcoci : " + BuildInfo.getDriverVersion();
                byArray10 = DBConversion.stringToDriverCharBytes((String)object, this.m_clientCharacterSet);
            } else {
                byArray10 = DBConversion.stringToDriverCharBytes(this.driverNameAttribute, this.m_clientCharacterSet);
            }
            byArray3 = this.userName == null ? EMPTY_BYTES : DBConversion.stringToDriverCharBytes(this.userName, this.m_clientCharacterSet);
            byArray4 = this.proxyClientName == null ? EMPTY_BYTES : DBConversion.stringToDriverCharBytes(this.proxyClientName, this.m_clientCharacterSet);
            byte[] byArray11 = byArray5 = this.password == null || this.password.isNull() ? EMPTY_BYTES : DBConversion.stringToDriverCharBytes(this.password.get().trim(), this.m_clientCharacterSet);
            if (this.drcpEnabled && this.drcpConnectionClass != null && this.drcpConnectionClass != "") {
                byArray6 = DBConversion.stringToDriverCharBytes(this.drcpConnectionClass, this.m_clientCharacterSet);
            }
            if (this.drcpTagName != null) {
                byArray7 = DBConversion.stringToDriverCharBytes(this.drcpTagName, this.m_clientCharacterSet);
            }
            object = DBConversion.stringToDriverCharBytes(this.database, this.m_clientCharacterSet);
            String string2 = null;
            string2 = CharacterSetMetaData.getNLSLanguage(Locale.getDefault(Locale.Category.FORMAT));
            byte[] byArray12 = string2 != null ? string2.getBytes() : null;
            string2 = CharacterSetMetaData.getNLSTerritory(Locale.getDefault(Locale.Category.FORMAT));
            byte[] byArray13 = byArray2 = string2 != null ? string2.getBytes() : null;
            if (byArray12 == null || byArray2 == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 176).fillInStackTrace();
            }
            TimeZone timeZone = TimeZone.getDefault();
            String string3 = timeZone.getID();
            if (!ZONEIDMAP.isValidRegion(string3) || !this.timezoneAsRegion) {
                int n4 = timeZone.getOffset(System.currentTimeMillis());
                n3 = n4 / 3600000;
                int n5 = n4 / 60000 % 60;
                string3 = (n3 < 0 ? "" + n3 : "+" + n3) + (n5 < 10 ? ":0" + n5 : ":" + n5);
            }
            T2CConnection.doSetSessionTimeZone(string3);
            this.sessionTimeZone = string3;
            this.conversion = new DBConversion(this.m_clientCharacterSet, this.m_clientCharacterSet, this.m_clientCharacterSet);
            long[] lArray = new long[]{this.defaultLobPrefetchSize, this.prelimAuth ? 1 : 0, 0L, 0L, 0L, 0L, 0L, 0L, 0L, this.enableOCIFAN ? 1 : 0};
            this.sqlWarning = this.m_nativeState == 0L ? this.checkError(this.t2cCreateState(byArray3, byArray3.length, byArray4, byArray4.length, byArray5, byArray5.length, byArray8, byArray8.length, byArray9, byArray9.length, byArray10, byArray10.length, (byte[])object, ((Object)object).length, this.drcpEnabled, byArray6, byArray6.length, byArray7, byArray7.length, this.m_clientCharacterSet, this.logon_mode, byArray12, byArray2, byArray, lArray), this.sqlWarning) : this.checkError(this.t2cLogon(this.m_nativeState, byArray3, byArray3.length, byArray4, byArray4.length, byArray5, byArray5.length, byArray8, byArray8.length, byArray9, byArray9.length, byArray10, byArray10.length, (byte[])object, ((Object)object).length, this.drcpEnabled, byArray6, byArray6.length, byArray7, byArray7.length, this.logon_mode, byArray12, byArray2, byArray, lArray), this.sqlWarning);
            if (this.drcpEnabled) {
                this.drcpState = OracleConnection.DRCPState.ATTACHED_IMPLICIT;
            }
            this.conversion = new DBConversion((short)(lArray[2] & 0xFFFFL), this.m_clientCharacterSet, (short)(lArray[3] & 0xFFFFL));
            this.byteAlign = (byte)(lArray[4] & 0xFFL);
            this.timeZoneVersionNumber = (int)lArray[5];
            if (lArray[6] != 0L) {
                this.useOCIDefaultDefines = true;
            }
            this.tagMatched[0] = lArray[7] != 0L;
            this.varTypeMaxLenCompat = (int)lArray[8];
            n3 = 0;
            String string4 = null;
            while ((n2 = this.readShort(byArray, n3)) != 0) {
                int n6 = this.readShort(byArray, n3 += 2);
                n3 += 2;
                switch (n2) {
                    case 1: {
                        this.databaseUniqueIdentifier = new String(byArray, n3, n6);
                        n3 += n6;
                        break;
                    }
                    case 2: {
                        string4 = new String(byArray, n3, n6);
                        n3 += n6;
                    }
                }
            }
            if (this.databaseUniqueIdentifier != null) {
                String string5 = cachedVersionTable.get(this.databaseUniqueIdentifier + string4);
                if (string5 == null) {
                    cachedVersionTable.put(this.databaseUniqueIdentifier + string4, String.valueOf(this.getVersionNumber()));
                } else {
                    this.versionNumber = Short.parseShort(string5);
                    this.t2cSetCachedServerVersion(this.m_nativeState, this.versionNumber);
                }
            }
        }
    }

    @Override
    final CompletionStage<Void> logonAsync(AbstractConnectionBuilder<?, ?> abstractConnectionBuilder) {
        return CompletionStageUtil.failedStage(new UnsupportedOperationException("Asynchronous connection is not supported by the Type 2 OCI driver"));
    }

    int readShort(byte[] byArray, int n2) {
        return (byArray[n2] & 0xFF) << 8 | byArray[n2 + 1] & 0xFF;
    }

    @Override
    protected void logoff() throws SQLException {
        try {
            if (this.lifecycle == 8 || this.lifecycle == 2) {
                this.checkError(this.t2cLogoff(this.m_nativeState));
            }
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        this.m_nativeState = 0L;
    }

    @Override
    public void open(OracleStatement oracleStatement) throws SQLException {
        byte[] byArray = oracleStatement.sqlObject.getSql(oracleStatement.processEscapes, oracleStatement.convertNcharLiterals).getBytes();
        this.checkError(this.t2cCreateStatement(this.m_nativeState, 0L, byArray, byArray.length, oracleStatement, false, oracleStatement.rowPrefetch));
    }

    @Override
    OracleStatement createImplicitResultSetStatement(OracleStatement oracleStatement) throws SQLException {
        T2CStatement t2CStatement = new T2CStatement(this, 1, this.defaultRowPrefetch, -1, -1);
        this.checkError(this.t2cGetImplicitResultSetStatement(this.m_nativeState, oracleStatement.c_state, t2CStatement));
        t2CStatement.needToParse = false;
        t2CStatement.isOpen = true;
        t2CStatement.processEscapes = false;
        t2CStatement.sqlKind = OracleStatement.SqlKind.SELECT;
        t2CStatement.prepareForNewResults(true, false, true);
        oracleStatement.addImplicitResultSetStmt(t2CStatement);
        return t2CStatement;
    }

    @Override
    void cancelOperationOnServer(boolean bl) throws SQLException {
        this.checkError(this.t2cCancel(this.m_nativeState));
    }

    native int t2cAbort(long var1);

    @Override
    void doAbort() throws SQLException {
        this.checkError(this.t2cAbort(this.m_nativeState));
    }

    native int t2cBeginRequest(long var1);

    @Override
    public void beginRequest() throws SQLException {
        super.beginRequest();
        this.checkError(this.t2cBeginRequest(this.m_nativeState));
    }

    native int t2cEndRequest(long var1);

    @Override
    public void endRequest(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.checkError(this.t2cEndRequest(this.m_nativeState));
            super.endRequest(bl);
        }
    }

    @Override
    void setDriverSpecificAutoCommit(boolean bl) throws SQLException {
        this.checkError(this.t2cSetAutoCommit(this.m_nativeState, bl));
        this.autocommit = bl;
    }

    @Override
    protected void doSetAutoCommit(boolean bl) throws SQLException {
        if (this.autoCommitSpecCompliant && !this.getAutoCommit() && bl) {
            this.commit();
        }
        this.setDriverSpecificAutoCommit(bl);
    }

    @Override
    protected void doCommit(int n2) throws SQLException {
        this.checkError(this.t2cCommit(this.m_nativeState, n2));
    }

    @Override
    protected void doRollback() throws SQLException {
        this.checkError(this.t2cRollback(this.m_nativeState));
    }

    @Override
    int doPingDatabase() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.drcpEnabled && this.drcpState == OracleConnection.DRCPState.DETACHED) {
                this.attachServerConnection();
            }
            if (this.t2cPingDatabase(this.m_nativeState) == 0) {
                int n2 = 0;
                return n2;
            }
            int n3 = -1;
            return n3;
        }
    }

    @Override
    protected String doGetDatabaseProductVersion() throws SQLException {
        long[] lArray = new long[1];
        return this.doGetDatabaseProductVersion(lArray);
    }

    private String doGetDatabaseProductVersion(long[] lArray) throws SQLException {
        byte[] byArray = this.t2cGetProductionVersion(this.m_nativeState, lArray);
        return this.conversion.CharBytesToString(byArray, byArray.length);
    }

    @Override
    protected short doGetVersionNumber() throws SQLException {
        short s2 = 0;
        try {
            long[] lArray = new long[]{0L};
            String string = this.doGetDatabaseProductVersion(lArray);
            s2 = (short)lArray[0];
        }
        catch (IllegalStateException illegalStateException) {
            // empty catch block
        }
        return s2;
    }

    @Override
    public ClobDBAccess createClobDBAccess() {
        return this;
    }

    @Override
    public BlobDBAccess createBlobDBAccess() {
        return this;
    }

    @Override
    public BfileDBAccess createBfileDBAccess() {
        return this;
    }

    protected SQLWarning checkError(int n2) throws SQLException {
        return this.checkError(n2, null, 0L, null);
    }

    protected SQLWarning checkError(int n2, long l2, OracleSql oracleSql) throws SQLException {
        return this.checkError(n2, null, l2, oracleSql);
    }

    protected SQLWarning checkError(int n2, SQLWarning sQLWarning) throws SQLException {
        return this.checkError(n2, sQLWarning, 0L, null);
    }

    protected SQLWarning checkError(int n2, SQLWarning sQLWarning, long l2, OracleSql oracleSql) throws SQLException {
        switch (n2) {
            case 0: {
                break;
            }
            case -1: 
            case 1: {
                T2CError t2CError = new T2CError();
                int n3 = -1;
                if (this.lifecycle != 1 && this.lifecycle != 16) {
                    if (this.fatalErrorNumber != 0) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 269).fillInStackTrace();
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
                }
                n3 = this.t2cDescribeError(this.m_nativeState, t2CError, t2CError.m_errorMessage, l2);
                String string = null;
                if (n3 != -1) {
                    int n4;
                    for (n4 = 0; n4 < t2CError.m_errorMessage.length && t2CError.m_errorMessage[n4] != 0; ++n4) {
                    }
                    if (this.conversion == null) {
                        throw new Error("conversion == null");
                    }
                    if (t2CError == null) {
                        throw new Error("l_error == null");
                    }
                    string = this.conversion.CharBytesToString(t2CError.m_errorMessage, n4, true);
                }
                DBConversion dBConversion = null;
                switch (t2CError.m_errorNumber) {
                    case 28: 
                    case 600: 
                    case 1012: 
                    case 1041: {
                        this.internalClose();
                        break;
                    }
                    case 902: 
                    case 21700: {
                        this.removeAllDescriptor();
                        break;
                    }
                    case 3113: 
                    case 3114: {
                        this.setUsable(false);
                        dBConversion = this.conversion;
                        this.close();
                        break;
                    }
                    case -6: {
                        t2CError.m_errorNumber = 3113;
                    }
                }
                if (n3 == -1) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Fetch error message failed!").fillInStackTrace();
                }
                if (n2 == -1) {
                    SQLException sQLException = (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), string, t2CError.m_errorNumber).fillInStackTrace();
                    if (t2CError.m_errorPosition >= 0) {
                        int n5;
                        for (n5 = 0; n5 < t2CError.m_errorMessage.length && t2CError.m_errorMessage[n5] != 0; ++n5) {
                        }
                        if (this.conversion == null) {
                            this.conversion = dBConversion;
                        }
                        String string2 = this.conversion.CharBytesToString(t2CError.m_errorMessage, n5, true);
                        sQLException.initCause(new OracleDatabaseException(t2CError.m_errorPosition, t2CError.m_errorNumber, string2, oracleSql.actualSql, oracleSql.originalSql));
                    }
                    throw sQLException;
                }
                sQLWarning = DatabaseError.addSqlWarning(sQLWarning, string, t2CError.m_errorNumber);
                break;
            }
            case -4: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 254).fillInStackTrace();
            }
        }
        return sQLWarning;
    }

    @Override
    OracleStatement RefCursorBytesToStatement(byte[] byArray, OracleStatement oracleStatement) throws SQLException {
        T2CStatement t2CStatement = new T2CStatement(this, 1, this.defaultRowPrefetch, -1, -1);
        t2CStatement.needToParse = false;
        t2CStatement.serverCursor = true;
        t2CStatement.isOpen = true;
        t2CStatement.processEscapes = false;
        t2CStatement.prepareForNewResults(true, false, true);
        if (this.useOCIDefaultDefines) {
            t2CStatement.savedRowPrefetch = this.defaultRowPrefetch;
            t2CStatement.rowPrefetch = 1;
        }
        t2CStatement.sqlObject.initialize("select unknown as ref cursor from whatever");
        t2CStatement.sqlKind = OracleStatement.SqlKind.SELECT;
        this.checkError(this.t2cCreateStatement(this.m_nativeState, oracleStatement.c_state, byArray, byArray.length, t2CStatement, true, this.defaultRowPrefetch));
        oracleStatement.addChild(t2CStatement);
        return t2CStatement;
    }

    @Override
    public void getForm(OracleTypeADT oracleTypeADT, OracleTypeCLOB oracleTypeCLOB, int n2) throws SQLException {
        boolean bl = false;
        if (oracleTypeCLOB != null) {
            String[] stringArray = new String[1];
            String[] stringArray2 = new String[1];
            SQLName.parse(oracleTypeADT.getFullName(), stringArray, stringArray2, true);
            String string = "\"" + stringArray[0] + "\".\"" + stringArray2[0] + "\"";
            byte[] byArray = this.conversion.StringToCharBytes(string);
            int n3 = this.t2cGetFormOfUse(this.m_nativeState, oracleTypeCLOB, byArray, byArray.length, n2);
            if (n3 < 0) {
                this.checkError(n3);
            }
            oracleTypeCLOB.setForm(n3);
        }
    }

    @Override
    public long getTdoCState(String string, String string2) throws SQLException {
        int[] nArray;
        String string3 = "\"" + string + "\".\"" + string2 + "\"";
        byte[] byArray = this.conversion.StringToCharBytes(string3);
        long l2 = this.t2cGetTDO(this.m_nativeState, byArray, byArray.length, nArray = new int[1]);
        if (l2 == 0L) {
            this.checkError(nArray[0]);
        }
        return l2;
    }

    @Override
    public long getTdoCState(String string) throws SQLException {
        int[] nArray;
        byte[] byArray = this.conversion.StringToCharBytes(string);
        long l2 = this.t2cGetTDO(this.m_nativeState, byArray, byArray.length, nArray = new int[1]);
        if (l2 == 0L) {
            this.checkError(nArray[0]);
        }
        return l2;
    }

    @Override
    @Deprecated
    @Blind(value=PropertiesBlinder.class)
    public Properties getDBAccessProperties() throws SQLException {
        return this.getOCIHandles();
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getOCIHandles() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Object object;
            if (this.lifecycle != 1) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
            }
            if (this.nativeInfo == null) {
                object = new long[3];
                this.checkError(this.t2cGetHandles(this.m_nativeState, (long[])object));
                this.nativeInfo = new Properties();
                this.nativeInfo.put("OCIEnvHandle", String.valueOf(object[0]));
                this.nativeInfo.put("OCISvcCtxHandle", String.valueOf(object[1]));
                this.nativeInfo.put("OCIErrHandle", String.valueOf(object[2]));
                this.nativeInfo.put("ClientCharSet", String.valueOf(this.m_clientCharacterSet));
            }
            object = this.nativeInfo;
            return object;
        }
    }

    @Override
    @Blind(value=PropertiesBlinder.class)
    public Properties getServerSessionInfo() throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        if (this.sessionProperties == null) {
            this.sessionProperties = new Properties();
        }
        if (this.getVersionNumber() < 10200) {
            this.queryFCFProperties(this.sessionProperties);
        } else {
            this.checkError(T2CConnection.t2cGetServerSessionInfo(this.m_nativeState, this.sessionProperties));
        }
        return this.sessionProperties;
    }

    @Override
    public byte getInstanceProperty(OracleConnection.InstanceProperty instanceProperty) throws SQLException {
        byte by = 0;
        if (instanceProperty == OracleConnection.InstanceProperty.ASM_VOLUME_SUPPORTED) {
            by = this.t2cGetAsmVolProperty(this.m_nativeState);
        } else if (instanceProperty == OracleConnection.InstanceProperty.INSTANCE_TYPE) {
            by = this.t2cGetInstanceType(this.m_nativeState);
        }
        return by;
    }

    @Blind(value=PropertiesBlinder.class)
    public Properties getConnectionPoolInfo() throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        Properties properties = new Properties();
        this.checkError(this.t2cGetConnPoolInfo(this.m_nativeState, properties));
        return properties;
    }

    public void setConnectionPoolInfo(int n2, int n3, int n4, int n5, int n6, int n7) throws SQLException {
        this.checkError(this.t2cSetConnPoolInfo(this.m_nativeState, n2, n3, n4, n5, n6, n7));
    }

    public void ociPasswordChange(String string, @Blind String string2, @Blind String string3) throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        byte[] byArray = string == null ? new byte[]{} : DBConversion.stringToDriverCharBytes(string, this.m_clientCharacterSet);
        byte[] byArray2 = string2 == null ? new byte[]{} : DBConversion.stringToDriverCharBytes(string2, this.m_clientCharacterSet);
        byte[] byArray3 = string3 == null ? new byte[]{} : DBConversion.stringToDriverCharBytes(string3, this.m_clientCharacterSet);
        this.sqlWarning = this.checkError(this.t2cPasswordChange(this.m_nativeState, byArray, byArray.length, byArray2, byArray2.length, byArray3, byArray3.length), this.sqlWarning);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void processOCIConnectionPooling() throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        T2CConnection t2CConnection = null;
        if (this.ociConnectionPoolLogonMode == "connection_pool") {
            this.m_clientCharacterSet = this.nlsLangBackdoor ? T2CConnection.getDriverCharSetIdFromNLS_LANG() : T2CConnection.getClientCharSetId();
        } else {
            t2CConnection = (T2CConnection)this.ociConnectionPoolObject;
            this.m_clientCharacterSet = t2CConnection.m_clientCharacterSet;
        }
        byte[] byArray = null;
        byte[] byArray2 = this.password == null || this.password.isNull() ? new byte[]{} : DBConversion.stringToDriverCharBytes(this.password.get().trim(), this.m_clientCharacterSet);
        byte[] byArray3 = this.editionName == null ? new byte[]{} : DBConversion.stringToDriverCharBytes(this.editionName, this.m_clientCharacterSet);
        byte[] byArray4 = DBConversion.stringToDriverCharBytes(this.driverNameAttribute == null ? "jdbcoci : " + BuildInfo.getDriverVersion() : this.driverNameAttribute, this.m_clientCharacterSet);
        byte[] byArray5 = DBConversion.stringToDriverCharBytes(this.database, this.m_clientCharacterSet);
        byte[] byArray6 = CharacterSetMetaData.getNLSLanguage(Locale.getDefault(Locale.Category.FORMAT)).getBytes();
        byte[] byArray7 = CharacterSetMetaData.getNLSTerritory(Locale.getDefault(Locale.Category.FORMAT)).getBytes();
        if (byArray6 == null || byArray7 == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 176).fillInStackTrace();
        }
        long[] lArray = new long[]{this.defaultLobPrefetchSize, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L};
        if (this.ociConnectionPoolLogonMode == "connection_pool") {
            byArray = this.userName == null ? new byte[]{} : DBConversion.stringToDriverCharBytes(this.userName, this.m_clientCharacterSet);
            this.conversion = new DBConversion(this.m_clientCharacterSet, this.m_clientCharacterSet, this.m_clientCharacterSet);
            this.logon_mode = 5;
            if (this.lifecycle != 1) throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 0, "Internal Error: ").fillInStackTrace();
            int[] nArray = new int[6];
            OracleOCIConnectionPool.readPoolConfig(this.ociConnectionPoolMinLimit, this.ociConnectionPoolMaxLimit, this.ociConnectionPoolIncrement, this.ociConnectionPoolTimeout, this.ociConnectionPoolNoWait, this.ociConnectionPoolTransactionDistributed, nArray);
            this.sqlWarning = this.checkError(this.t2cCreateConnPool(byArray, byArray.length, byArray2, byArray2.length, byArray5, byArray5.length, this.m_clientCharacterSet, this.logon_mode, nArray[0], nArray[1], nArray[2], nArray[3], nArray[4], nArray[5]), this.sqlWarning);
            this.versionNumber = (short)10000;
        } else if (this.ociConnectionPoolLogonMode == "connpool_connection") {
            this.logon_mode = 6;
            byArray = this.userName == null ? new byte[]{} : DBConversion.stringToDriverCharBytes(this.userName, this.m_clientCharacterSet);
            this.conversion = new DBConversion(this.m_clientCharacterSet, this.m_clientCharacterSet, this.m_clientCharacterSet);
            this.sqlWarning = this.checkError(this.t2cConnPoolLogon(t2CConnection.m_nativeState, byArray, byArray.length, byArray2, byArray2.length, byArray3, byArray3.length, byArray4, byArray4.length, byArray5, byArray5.length, this.logon_mode, 0, 0, null, null, 0, null, 0, null, 0, null, 0, null, 0, byArray6, byArray7, lArray), this.sqlWarning);
        } else if (this.ociConnectionPoolLogonMode == "connpool_alias_connection") {
            this.logon_mode = 8;
            byte[] byArray8 = null;
            byArray8 = (byte[])this.ociConnectionPoolConnID;
            byArray = this.userName == null ? new byte[]{} : DBConversion.stringToDriverCharBytes(this.userName, this.m_clientCharacterSet);
            this.conversion = new DBConversion(this.m_clientCharacterSet, this.m_clientCharacterSet, this.m_clientCharacterSet);
            this.sqlWarning = this.checkError(this.t2cConnPoolLogon(t2CConnection.m_nativeState, byArray, byArray.length, byArray2, byArray2.length, byArray3, byArray3.length, byArray4, byArray4.length, byArray5, byArray5.length, this.logon_mode, 0, 0, null, null, 0, null, 0, null, 0, null, 0, byArray8, byArray8 == null ? 0 : byArray8.length, byArray6, byArray7, lArray), this.sqlWarning);
        } else {
            if (this.ociConnectionPoolLogonMode != "connpool_proxy_connection") throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23, "connection-pool-logon").fillInStackTrace();
            this.logon_mode = 7;
            String string = this.ociConnectionPoolProxyType;
            int n2 = this.ociConnectionPoolProxyNumRoles;
            String[] stringArray = null;
            if (n2 > 0) {
                stringArray = (String[])this.ociConnectionPoolProxyRoles;
            }
            byte[] byArray9 = null;
            byte[] byArray10 = null;
            byte[] byArray11 = null;
            byte[] byArray12 = null;
            int n3 = 0;
            if (string == "proxytype_user_name") {
                n3 = 1;
                String string2 = this.ociConnectionPoolProxyUserName;
                if (string2 != null) {
                    byArray9 = string2.getBytes();
                }
                if ((string2 = this.ociConnectionPoolProxyPassword.get()) != null) {
                    byArray10 = string2.getBytes();
                }
            } else if (string == "proxytype_distinguished_name") {
                n3 = 2;
                String string3 = this.ociConnectionPoolProxyDistinguishedName;
                if (string3 != null) {
                    byArray11 = string3.getBytes();
                }
            } else {
                if (string != "proxytype_certificate") throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 107).fillInStackTrace();
                n3 = 3;
                byArray12 = (byte[])this.ociConnectionPoolProxyCertificate;
            }
            byArray = this.userName == null ? new byte[]{} : DBConversion.stringToDriverCharBytes(this.userName, this.m_clientCharacterSet);
            this.conversion = new DBConversion(this.m_clientCharacterSet, this.m_clientCharacterSet, this.m_clientCharacterSet);
            this.sqlWarning = this.checkError(this.t2cConnPoolLogon(t2CConnection.m_nativeState, byArray, byArray.length, byArray2, byArray2.length, byArray3, byArray3.length, byArray4, byArray4.length, byArray5, byArray5.length, this.logon_mode, n3, n2, stringArray, byArray9, byArray9 == null ? 0 : byArray9.length, byArray10, byArray10 == null ? 0 : byArray10.length, byArray11, byArray11 == null ? 0 : byArray11.length, byArray12, byArray12 == null ? 0 : byArray12.length, null, 0, byArray6, byArray7, lArray), this.sqlWarning);
        }
        this.conversion = new DBConversion((short)(lArray[2] & 0xFFFFL), this.m_clientCharacterSet, (short)(lArray[3] & 0xFFFFL));
        this.byteAlign = (byte)(lArray[4] & 0xFFL);
        this.timeZoneVersionNumber = (int)lArray[5];
        if (lArray[6] != 0L) {
            this.useOCIDefaultDefines = true;
        }
        this.tagMatched[0] = lArray[7] != 0L;
        this.varTypeMaxLenCompat = (int)lArray[8];
    }

    @Override
    public boolean isDescriptorSharable(OracleConnection oracleConnection) throws SQLException {
        T2CConnection t2CConnection = this;
        PhysicalConnection physicalConnection = (PhysicalConnection)oracleConnection.getPhysicalConnection();
        return t2CConnection == physicalConnection;
    }

    native int t2cBlobRead(long var1, byte[] var3, int var4, long var5, int var7, byte[] var8, int var9, boolean var10, ByteBuffer var11);

    native int t2cClobRead(long var1, byte[] var3, int var4, long var5, int var7, char[] var8, int var9, boolean var10, boolean var11, ByteBuffer var12);

    native int t2cBlobWrite(long var1, byte[] var3, int var4, long var5, int var7, byte[] var8, int var9, byte[][] var10);

    native int t2cClobWrite(long var1, byte[] var3, int var4, long var5, int var7, char[] var8, int var9, byte[][] var10, boolean var11);

    native long t2cLobGetLength(long var1, byte[] var3, int var4);

    native int t2cBfileOpen(long var1, byte[] var3, int var4, byte[][] var5);

    native int t2cBfileIsOpen(long var1, byte[] var3, int var4, boolean[] var5);

    native int t2cBfileExists(long var1, byte[] var3, int var4, boolean[] var5);

    native String t2cBfileGetName(long var1, byte[] var3, int var4);

    native String t2cBfileGetDirAlias(long var1, byte[] var3, int var4);

    native int t2cBfileClose(long var1, byte[] var3, int var4, byte[][] var5);

    native int t2cLobGetChunkSize(long var1, byte[] var3, int var4);

    native int t2cLobTrim(long var1, int var3, long var4, byte[] var6, int var7, byte[][] var8);

    native int t2cLobCreateTemporary(long var1, int var3, boolean var4, int var5, short var6, byte[][] var7);

    native int t2cLobFreeTemporary(long var1, int var3, byte[] var4, int var5, byte[][] var6);

    native int t2cLobIsTemporary(long var1, int var3, byte[] var4, int var5, boolean[] var6);

    native int t2cLobOpen(long var1, int var3, byte[] var4, int var5, int var6, byte[][] var7);

    native int t2cLobIsOpen(long var1, int var3, byte[] var4, int var5, boolean[] var6);

    native int t2cLobClose(long var1, int var3, byte[] var4, int var5, byte[][] var6);

    private long lobLength(byte[] byArray) throws SQLException {
        long l2 = 0L;
        l2 = this.t2cLobGetLength(this.m_nativeState, byArray, byArray.length);
        this.checkError((int)l2);
        return l2;
    }

    private int blobRead(byte[] byArray, long l2, int n2, byte[] byArray2, boolean bl, ByteBuffer byteBuffer) throws SQLException {
        int n3 = 0;
        n3 = this.t2cBlobRead(this.m_nativeState, byArray, byArray.length, l2, n2, byArray2, byArray2.length, bl, byteBuffer);
        this.checkError(n3);
        return n3;
    }

    private int blobWrite(byte[] byArray, long l2, byte[] byArray2, byte[][] byArray3, int n2, int n3) throws SQLException {
        int n4 = 0;
        n4 = this.t2cBlobWrite(this.m_nativeState, byArray, byArray.length, l2, n3, byArray2, n2, byArray3);
        this.checkError(n4);
        return n4;
    }

    private int clobWrite(byte[] byArray, long l2, char[] cArray, byte[][] byArray2, boolean bl, int n2, int n3) throws SQLException {
        int n4 = 0;
        n4 = this.t2cClobWrite(this.m_nativeState, byArray, byArray.length, l2, n3, cArray, n2, byArray2, bl);
        this.checkError(n4);
        return n4;
    }

    private int lobGetChunkSize(byte[] byArray) throws SQLException {
        int n2 = 0;
        n2 = this.t2cLobGetChunkSize(this.m_nativeState, byArray, byArray.length);
        this.checkError(n2);
        return n2;
    }

    @Override
    public long length(OracleBfile oracleBfile) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray = oracleBfile.getLocator()) != null, 54);
            long l2 = this.lobLength(byArray);
            return l2;
        }
    }

    @Override
    public long position(OracleBfile oracleBfile, Datum datum, byte[] byArray, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (l2 < 1L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "position()").fillInStackTrace();
            }
            long l3 = LobPlsqlUtil.hasPattern(oracleBfile, datum, byArray, l2);
            long l4 = l3 = l3 == 0L ? -1L : l3;
            return l4;
        }
    }

    @Override
    public long position(OracleBfile oracleBfile, Datum datum, Datum datum2, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (l2 < 1L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "position()").fillInStackTrace();
            }
            long l3 = LobPlsqlUtil.isSubLob(oracleBfile, datum, datum2, l2);
            long l4 = l3 = l3 == 0L ? -1L : l3;
            return l4;
        }
    }

    @Override
    public int getBytes(OracleBfile oracleBfile, long l2, int n2, byte[] byArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n3;
            if (l2 < 1L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "getBytes()").fillInStackTrace();
            }
            byte[] byArray2 = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray2 = oracleBfile.getLocator()) != null, 54);
            if (n2 <= 0 || byArray == null) {
                int n4 = 0;
                return n4;
            }
            if (n2 > byArray.length) {
                n2 = byArray.length;
            }
            if (this.useNio) {
                n3 = byArray.length;
                if (this.nioBufferForLob == null || this.nioBufferForLob.capacity() < n3) {
                    this.nioBufferForLob = ByteBuffer.allocateDirect(n3);
                } else {
                    this.nioBufferForLob.rewind();
                }
            }
            n3 = this.blobRead(byArray2, l2, n2, byArray, this.useNio, this.nioBufferForLob);
            if (this.useNio) {
                this.nioBufferForLob.get(byArray);
            }
            int n5 = n3;
            return n5;
        }
    }

    @Override
    public String getName(OracleBfile oracleBfile) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            String string = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray = oracleBfile.getLocator()) != null, 54);
            string = this.t2cBfileGetName(this.m_nativeState, byArray, byArray.length);
            this.checkError(string.length());
            String string2 = string;
            return string2;
        }
    }

    @Override
    public String getDirAlias(OracleBfile oracleBfile) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            String string = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray = oracleBfile.getLocator()) != null, 54);
            string = this.t2cBfileGetDirAlias(this.m_nativeState, byArray, byArray.length);
            this.checkError(string.length());
            String string2 = string;
            return string2;
        }
    }

    @Override
    public void openFile(OracleBfile oracleBfile) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray = oracleBfile.getLocator()) != null, 54);
            byte[][] byArrayArray = new byte[1][];
            this.checkError(this.t2cBfileOpen(this.m_nativeState, byArray, byArray.length, byArrayArray));
            oracleBfile.setLocator(byArrayArray[0]);
            return;
        }
    }

    @Override
    public boolean isFileOpen(OracleBfile oracleBfile) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray = oracleBfile.getLocator()) != null, 54);
            boolean[] blArray = new boolean[1];
            this.checkError(this.t2cBfileIsOpen(this.m_nativeState, byArray, byArray.length, blArray));
            boolean bl = blArray[0];
            return bl;
        }
    }

    @Override
    public boolean fileExists(OracleBfile oracleBfile) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray = oracleBfile.getLocator()) != null, 54);
            boolean[] blArray = new boolean[1];
            this.checkError(this.t2cBfileExists(this.m_nativeState, byArray, byArray.length, blArray));
            boolean bl = blArray[0];
            return bl;
        }
    }

    @Override
    public void closeFile(OracleBfile oracleBfile) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray = oracleBfile.getLocator()) != null, 54);
            byte[][] byArrayArray = new byte[1][];
            this.checkError(this.t2cBfileClose(this.m_nativeState, byArray, byArray.length, byArrayArray));
            oracleBfile.setLocator(byArrayArray[0]);
            return;
        }
    }

    @Override
    public void open(OracleBfile oracleBfile, int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray = oracleBfile.shareBytes()) != null, 54);
            byte[][] byArrayArray = new byte[1][];
            this.checkError(this.t2cLobOpen(this.m_nativeState, 114, byArray, byArray.length, n2, byArrayArray));
            oracleBfile.setShareBytes(byArrayArray[0]);
            return;
        }
    }

    @Override
    public void close(OracleBfile oracleBfile) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray = oracleBfile.shareBytes()) != null, 54);
            byte[][] byArrayArray = new byte[1][];
            this.checkError(this.t2cLobClose(this.m_nativeState, 114, byArray, byArray.length, byArrayArray));
            oracleBfile.setShareBytes(byArrayArray[0]);
            return;
        }
    }

    @Override
    public boolean isOpen(OracleBfile oracleBfile) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBfile != null && (byArray = oracleBfile.shareBytes()) != null, 54);
            boolean[] blArray = new boolean[1];
            this.checkError(this.t2cLobIsOpen(this.m_nativeState, 114, byArray, byArray.length, blArray));
            boolean bl = blArray[0];
            return bl;
        }
    }

    @Override
    public InputStream newInputStream(BFILE bFILE, int n2, long l2) throws SQLException {
        return this.newInputStream((OracleBfile)bFILE, n2, l2);
    }

    @Override
    public InputStream newInputStream(OracleBfile oracleBfile, int n2, long l2) throws SQLException {
        if (l2 == 0L) {
            return new OracleBlobInputStream(oracleBfile, n2);
        }
        return new OracleBlobInputStream(oracleBfile, n2, l2);
    }

    @Override
    public InputStream newConversionInputStream(BFILE bFILE, int n2) throws SQLException {
        return this.newConversionInputStream((OracleBfile)bFILE, n2);
    }

    @Override
    public InputStream newConversionInputStream(OracleBfile oracleBfile, int n2) throws SQLException {
        this.checkTrue(oracleBfile != null && oracleBfile.shareBytes() != null, 54);
        OracleConversionInputStream oracleConversionInputStream = new OracleConversionInputStream(this.conversion, oracleBfile.getBinaryStream(), n2);
        return oracleConversionInputStream;
    }

    @Override
    public Reader newConversionReader(BFILE bFILE, int n2) throws SQLException {
        return this.newConversionReader((OracleBfile)bFILE, n2);
    }

    @Override
    public Reader newConversionReader(OracleBfile oracleBfile, int n2) throws SQLException {
        this.checkTrue(oracleBfile != null && oracleBfile.shareBytes() != null, 54);
        OracleConversionReader oracleConversionReader = new OracleConversionReader(this.conversion, oracleBfile.getBinaryStream(), n2);
        return oracleConversionReader;
    }

    @Override
    public long length(OracleBlob oracleBlob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBlob != null && (byArray = oracleBlob.getLocator()) != null, 54);
            long l2 = this.lobLength(byArray);
            return l2;
        }
    }

    @Override
    public long position(OracleBlob oracleBlob, Datum datum, byte[] byArray, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBlob != null && oracleBlob.shareBytes() != null, 54);
            if (l2 < 1L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "position()").fillInStackTrace();
            }
            long l3 = LobPlsqlUtil.hasPattern(oracleBlob, datum, byArray, l2);
            long l4 = l3 = l3 == 0L ? -1L : l3;
            return l4;
        }
    }

    @Override
    public long position(OracleBlob oracleBlob, Datum datum, Datum datum2, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBlob != null && oracleBlob.shareBytes() != null, 54);
            this.checkTrue(datum2 != null && datum2.shareBytes() != null, 54);
            if (l2 < 1L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "position()").fillInStackTrace();
            }
            long l3 = LobPlsqlUtil.isSubLob(oracleBlob, datum, datum2, l2);
            long l4 = l3 = l3 == 0L ? -1L : l3;
            return l4;
        }
    }

    @Override
    public int getBytes(OracleBlob oracleBlob, long l2, int n2, byte[] byArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n3;
            byte[] byArray2;
            if (l2 < 1L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "getBytes()").fillInStackTrace();
            }
            byte[] byArray3 = null;
            int n4 = 0;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBlob != null && (byArray3 = oracleBlob.getLocator()) != null, 54);
            if (n2 <= 0 || byArray == null) {
                int n5 = 0;
                return n5;
            }
            if (n2 > byArray.length) {
                n2 = byArray.length;
            }
            long l3 = -1L;
            if (oracleBlob.isActivePrefetch()) {
                byArray2 = oracleBlob.getPrefetchedData();
                l3 = oracleBlob.length();
                if (byArray2 != null && byArray2.length > 0 && l2 > 0L && l2 <= (long)byArray2.length) {
                    n3 = Math.min(byArray2.length - (int)l2 + 1, n2);
                    System.arraycopy(byArray2, (int)l2 - 1, byArray, 0, n3);
                    n4 += n3;
                }
            }
            if (n4 < n2 && (l3 == -1L || l2 - 1L + (long)n4 < l3)) {
                byArray2 = byArray;
                n3 = n4;
                int n6 = (l3 > 0L && l3 < (long)n2 ? (int)l3 : n2) - n4;
                if (n4 > 0) {
                    byArray2 = new byte[n6];
                }
                if (this.useNio) {
                    int n7 = byArray.length;
                    if (this.nioBufferForLob == null || this.nioBufferForLob.capacity() < n7) {
                        this.nioBufferForLob = ByteBuffer.allocateDirect(n7);
                    } else {
                        this.nioBufferForLob.rewind();
                    }
                }
                n4 += this.blobRead(byArray3, l2 + (long)n4, n6, byArray2, this.useNio, this.nioBufferForLob);
                if (this.useNio) {
                    this.nioBufferForLob.get(byArray2);
                }
                if (n3 > 0) {
                    System.arraycopy(byArray2, 0, byArray, n3, byArray2.length);
                }
            }
            int n8 = n4;
            return n8;
        }
    }

    @Override
    public int putBytes(OracleBlob oracleBlob, long l2, byte[] byArray, int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.checkTrue(l2 != 0L || n3 > 0, 68);
            this.checkTrue(l2 >= 0L, 68);
            if (byArray == null || n3 <= 0) {
                int n4 = 0;
                return n4;
            }
            int n5 = 0;
            if (byArray == null || byArray.length == 0 || n3 <= 0) {
                n5 = 0;
            } else {
                byte[] byArray2 = null;
                this.checkTrue(this.lifecycle == 1, 8);
                this.checkTrue(oracleBlob != null && (byArray2 = oracleBlob.getLocator()) != null, 54);
                byte[][] byArrayArray = new byte[1][];
                oracleBlob.setActivePrefetch(false);
                oracleBlob.clearCachedData();
                n5 = this.blobWrite(byArray2, l2, byArray, byArrayArray, n2, n3);
                oracleBlob.setLocator(byArrayArray[0]);
            }
            int n6 = n5;
            return n6;
        }
    }

    @Override
    public int getChunkSize(OracleBlob oracleBlob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBlob != null && (byArray = oracleBlob.getLocator()) != null, 54);
            int n2 = this.lobGetChunkSize(byArray);
            return n2;
        }
    }

    @Override
    public void trim(OracleBlob oracleBlob, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBlob != null && (byArray = oracleBlob.shareBytes()) != null, 54);
            byte[][] byArrayArray = new byte[1][];
            oracleBlob.setActivePrefetch(false);
            oracleBlob.clearCachedData();
            this.checkError(this.t2cLobTrim(this.m_nativeState, 113, l2, byArray, byArray.length, byArrayArray));
            oracleBlob.setShareBytes(byArrayArray[0]);
            return;
        }
    }

    @Override
    public BLOB createTemporaryBlob(Connection connection, boolean bl, int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            BLOB bLOB = null;
            this.checkTrue(this.lifecycle == 1, 8);
            bLOB = new BLOB((PhysicalConnection)connection);
            byte[][] byArrayArray = new byte[1][];
            this.checkError(this.t2cLobCreateTemporary(this.m_nativeState, 113, bl, n2, (short)0, byArrayArray));
            bLOB.setShareBytes(byArrayArray[0]);
            this.addTemporaryLob(bLOB);
            BLOB bLOB2 = bLOB;
            return bLOB2;
        }
    }

    @Override
    public void freeTemporary(OracleBlob oracleBlob, Datum datum, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            try {
                byte[] byArray = null;
                this.checkTrue(this.lifecycle == 1, 8);
                this.checkTrue(oracleBlob != null && (byArray = oracleBlob.shareBytes()) != null, 54);
                byte[][] byArrayArray = new byte[1][];
                this.checkError(this.t2cLobFreeTemporary(this.m_nativeState, 113, byArray, byArray.length, byArrayArray));
                oracleBlob.setShareBytes(byArrayArray[0]);
            }
            catch (SQLException sQLException) {
                if (bl & sQLException.getErrorCode() == 64201) {
                    LobPlsqlUtil.freeTemporaryLob(this, datum, 2004);
                }
                throw sQLException;
            }
            this.removeFromTemporaryLobs(oracleBlob);
        }
    }

    @Override
    public boolean isTemporary(OracleBlob oracleBlob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(oracleBlob != null && (byArray = oracleBlob.shareBytes()) != null, 54);
            boolean[] blArray = new boolean[1];
            this.checkError(this.t2cLobIsTemporary(this.m_nativeState, 113, byArray, byArray.length, blArray));
            boolean bl = blArray[0];
            return bl;
        }
    }

    @Override
    public short getDuration(OracleBlob oracleBlob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            short s2 = -1;
            return s2;
        }
    }

    @Override
    public void open(OracleBlob oracleBlob, int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBlob != null && (byArray = oracleBlob.shareBytes()) != null, 54);
            byte[][] byArrayArray = new byte[1][];
            this.checkError(this.t2cLobOpen(this.m_nativeState, 113, byArray, byArray.length, n2, byArrayArray));
            oracleBlob.setShareBytes(byArrayArray[0]);
            return;
        }
    }

    @Override
    public void close(OracleBlob oracleBlob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBlob != null && (byArray = oracleBlob.shareBytes()) != null, 54);
            byte[][] byArrayArray = new byte[1][];
            this.checkError(this.t2cLobClose(this.m_nativeState, 113, byArray, byArray.length, byArrayArray));
            oracleBlob.setShareBytes(byArrayArray[0]);
            return;
        }
    }

    @Override
    public boolean isOpen(OracleBlob oracleBlob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleBlob != null && (byArray = oracleBlob.shareBytes()) != null, 54);
            boolean[] blArray = new boolean[1];
            this.checkError(this.t2cLobIsOpen(this.m_nativeState, 113, byArray, byArray.length, blArray));
            boolean bl = blArray[0];
            return bl;
        }
    }

    @Override
    public InputStream newInputStream(BLOB bLOB, int n2, long l2, boolean bl) throws SQLException {
        return this.newInputStream((OracleBlob)bLOB, n2, l2, bl);
    }

    @Override
    public InputStream newInputStream(BLOB bLOB, int n2, long l2) throws SQLException {
        return this.newInputStream(bLOB, n2, l2, false);
    }

    @Override
    public InputStream newInputStream(OracleBlob oracleBlob, int n2, long l2) throws SQLException {
        return this.newInputStream(oracleBlob, n2, l2, false);
    }

    @Override
    public InputStream newInputStream(OracleBlob oracleBlob, int n2, long l2, boolean bl) throws SQLException {
        if (l2 == 0L) {
            return new OracleBlobInputStream(oracleBlob, n2, bl);
        }
        return new OracleBlobInputStream(oracleBlob, n2, l2, bl);
    }

    @Override
    public InputStream newInputStream(BLOB bLOB, int n2, long l2, long l3) throws SQLException {
        return this.newInputStream(bLOB, n2, l2, l3, false);
    }

    @Override
    public InputStream newInputStream(BLOB bLOB, int n2, long l2, long l3, boolean bl) throws SQLException {
        return this.newInputStream((OracleBlob)bLOB, n2, l2, l3, bl);
    }

    @Override
    public InputStream newInputStream(OracleBlob oracleBlob, int n2, long l2, long l3) throws SQLException {
        return this.newInputStream(oracleBlob, n2, l2, l3, false);
    }

    @Override
    public InputStream newInputStream(OracleBlob oracleBlob, int n2, long l2, long l3, boolean bl) throws SQLException {
        return new OracleBlobInputStream(oracleBlob, n2, l2, l3, bl);
    }

    @Override
    public OutputStream newOutputStream(BLOB bLOB, int n2, long l2, boolean bl) throws SQLException {
        return this.newOutputStream((OracleBlob)bLOB, n2, l2, bl);
    }

    @Override
    public OutputStream newOutputStream(OracleBlob oracleBlob, int n2, long l2, boolean bl) throws SQLException {
        if (l2 == 0L) {
            if (bl & this.lobStreamPosStandardCompliant) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            return new OracleBlobOutputStream(oracleBlob, n2);
        }
        return new OracleBlobOutputStream(oracleBlob, n2, l2);
    }

    @Override
    public InputStream newConversionInputStream(BLOB bLOB, int n2) throws SQLException {
        return this.newConversionInputStream(bLOB, n2, false);
    }

    @Override
    public InputStream newConversionInputStream(BLOB bLOB, int n2, boolean bl) throws SQLException {
        return this.newConversionInputStream((OracleBlob)bLOB, n2, bl);
    }

    @Override
    public InputStream newConversionInputStream(OracleBlob oracleBlob, int n2) throws SQLException {
        return this.newConversionInputStream(oracleBlob, n2, false);
    }

    @Override
    public InputStream newConversionInputStream(OracleBlob oracleBlob, int n2, boolean bl) throws SQLException {
        this.checkTrue(oracleBlob != null && oracleBlob.shareBytes() != null, 54);
        OracleConversionInputStream oracleConversionInputStream = new OracleConversionInputStream(this.conversion, oracleBlob.binaryStreamValue(bl), n2);
        return oracleConversionInputStream;
    }

    @Override
    public Reader newConversionReader(BLOB bLOB, int n2) throws SQLException {
        return this.newConversionReader(bLOB, n2, false);
    }

    @Override
    public Reader newConversionReader(BLOB bLOB, int n2, boolean bl) throws SQLException {
        return this.newConversionReader((OracleBlob)bLOB, n2, bl);
    }

    @Override
    public Reader newConversionReader(OracleBlob oracleBlob, int n2) throws SQLException {
        return this.newConversionReader(oracleBlob, n2, false);
    }

    @Override
    public Reader newConversionReader(OracleBlob oracleBlob, int n2, boolean bl) throws SQLException {
        this.checkTrue(oracleBlob != null && oracleBlob.shareBytes() != null, 54);
        OracleConversionReader oracleConversionReader = new OracleConversionReader(this.conversion, oracleBlob.binaryStreamValue(bl), n2);
        return oracleConversionReader;
    }

    @Override
    public long length(OracleClob oracleClob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.checkTrue(this.lifecycle == 1, 8);
            byte[] byArray = oracleClob.getLocator();
            this.checkTrue(byArray != null, 54);
            long l2 = this.lobLength(byArray);
            return l2;
        }
    }

    @Override
    public long position(OracleClob oracleClob, String string, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (string == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleClob != null && oracleClob.shareBytes() != null, 54);
            if (l2 < 1L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "position()").fillInStackTrace();
            }
            char[] cArray = new char[string.length()];
            string.getChars(0, cArray.length, cArray, 0);
            long l3 = LobPlsqlUtil.hasPattern(oracleClob, cArray, l2);
            long l4 = l3 = l3 == 0L ? -1L : l3;
            return l4;
        }
    }

    @Override
    public long position(OracleClob oracleClob, OracleClob oracleClob2, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleClob != null && oracleClob.shareBytes() != null, 54);
            this.checkTrue(oracleClob2 != null && oracleClob2.shareBytes() != null, 54);
            if (l2 < 1L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "position()").fillInStackTrace();
            }
            long l3 = LobPlsqlUtil.isSubLob(oracleClob, oracleClob2, l2);
            long l4 = l3 = l3 == 0L ? -1L : l3;
            return l4;
        }
    }

    @Override
    public int getChars(OracleClob oracleClob, long l2, int n2, char[] cArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n3;
            char[] cArray2;
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleClob != null && (byArray = oracleClob.getLocator()) != null, 54);
            if (n2 <= 0 || cArray == null) {
                int n4 = 0;
                return n4;
            }
            if (n2 > cArray.length) {
                n2 = cArray.length;
            }
            int n5 = 0;
            long l3 = -1L;
            if (oracleClob.isActivePrefetch()) {
                l3 = oracleClob.length();
                cArray2 = oracleClob.getPrefetchedData();
                long l4 = oracleClob.getPrefetchedDataSize();
                if (cArray2 != null && l2 <= l4) {
                    n3 = Math.min((int)l4 - (int)l2 + 1, n2);
                    System.arraycopy(cArray2, (int)l2 - 1, cArray, 0, n3);
                    n5 += n3;
                }
            }
            if (n5 < n2 && (l3 == -1L || l2 - 1L + (long)n5 < l3)) {
                cArray2 = cArray;
                int n6 = n5;
                int n7 = (l3 > 0L && l3 < (long)n2 ? (int)l3 : n2) - n5;
                if (n5 > 0) {
                    cArray2 = new char[n7];
                }
                if (this.useNio) {
                    n3 = cArray.length * 2;
                    if (this.nioBufferForLob == null || this.nioBufferForLob.capacity() < n3) {
                        this.nioBufferForLob = ByteBuffer.allocateDirect(n3);
                    } else {
                        this.nioBufferForLob.rewind();
                    }
                }
                n5 += this.t2cClobRead(this.m_nativeState, byArray, byArray.length, l2 + (long)n5, n7, cArray2, cArray2.length, oracleClob.isNCLOB(), this.useNio, this.nioBufferForLob);
                if (this.useNio) {
                    ByteBuffer byteBuffer = this.nioBufferForLob.order(ByteOrder.LITTLE_ENDIAN);
                    CharBuffer charBuffer = byteBuffer.asCharBuffer();
                    charBuffer.get(cArray2);
                }
                if (n6 > 0) {
                    System.arraycopy(cArray2, 0, cArray, n6, cArray2.length);
                }
                this.checkError(n5);
            }
            int n8 = n5;
            return n8;
        }
    }

    @Override
    public int putChars(OracleClob oracleClob, long l2, char[] cArray, int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(l2 >= 0L, 68);
            this.checkTrue(oracleClob != null && (byArray = oracleClob.getLocator()) != null, 54);
            if (cArray == null || n3 <= 0) {
                int n4 = 0;
                return n4;
            }
            byte[][] byArrayArray = new byte[1][];
            oracleClob.setActivePrefetch(false);
            oracleClob.clearCachedData();
            int n5 = this.clobWrite(byArray, l2, cArray, byArrayArray, oracleClob.isNCLOB(), n2, n3);
            oracleClob.setLocator(byArrayArray[0]);
            int n6 = n5;
            return n6;
        }
    }

    @Override
    public int getChunkSize(OracleClob oracleClob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleClob != null && (byArray = oracleClob.getLocator()) != null, 54);
            int n2 = this.lobGetChunkSize(byArray);
            return n2;
        }
    }

    @Override
    public void trim(OracleClob oracleClob, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleClob != null && (byArray = oracleClob.shareBytes()) != null, 54);
            byte[][] byArrayArray = new byte[1][];
            oracleClob.setActivePrefetch(false);
            oracleClob.clearCachedData();
            this.checkError(this.t2cLobTrim(this.m_nativeState, 112, l2, byArray, byArray.length, byArrayArray));
            oracleClob.setShareBytes(byArrayArray[0]);
            return;
        }
    }

    @Override
    public CLOB createTemporaryClob(Connection connection, boolean bl, int n2, short s2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            CLOB cLOB = null;
            this.checkTrue(this.lifecycle == 1, 8);
            cLOB = s2 == 1 ? new CLOB((PhysicalConnection)connection) : new NCLOB((PhysicalConnection)connection);
            byte[][] byArrayArray = new byte[1][];
            this.checkError(this.t2cLobCreateTemporary(this.m_nativeState, 112, bl, n2, s2, byArrayArray));
            cLOB.setShareBytes(byArrayArray[0]);
            this.addTemporaryLob(cLOB);
            CLOB cLOB2 = cLOB;
            return cLOB2;
        }
    }

    @Override
    public void freeTemporary(OracleClob oracleClob, Datum datum, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            try {
                byte[] byArray = null;
                this.checkTrue(this.lifecycle == 1, 8);
                this.checkTrue(oracleClob != null && (byArray = oracleClob.shareBytes()) != null, 54);
                byte[][] byArrayArray = new byte[1][];
                this.checkError(this.t2cLobFreeTemporary(this.m_nativeState, 112, byArray, byArray.length, byArrayArray));
                oracleClob.setShareBytes(byArrayArray[0]);
            }
            catch (SQLException sQLException) {
                if (bl & sQLException.getErrorCode() == 64201) {
                    LobPlsqlUtil.freeTemporaryLob(this, datum, 2005);
                }
                throw sQLException;
            }
            this.removeFromTemporaryLobs(oracleClob);
        }
    }

    @Override
    public boolean isTemporary(OracleClob oracleClob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(oracleClob != null && (byArray = oracleClob.shareBytes()) != null, 54);
            boolean[] blArray = new boolean[1];
            this.checkError(this.t2cLobIsTemporary(this.m_nativeState, 112, byArray, byArray.length, blArray));
            boolean bl = blArray[0];
            return bl;
        }
    }

    @Override
    public short getDuration(OracleClob oracleClob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            short s2 = -1;
            return s2;
        }
    }

    @Override
    public void open(OracleClob oracleClob, int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleClob != null && (byArray = oracleClob.shareBytes()) != null, 54);
            byte[][] byArrayArray = new byte[1][];
            this.checkError(this.t2cLobOpen(this.m_nativeState, 112, byArray, byArray.length, n2, byArrayArray));
            oracleClob.setShareBytes(byArrayArray[0]);
            return;
        }
    }

    @Override
    public void close(OracleClob oracleClob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleClob != null && (byArray = oracleClob.shareBytes()) != null, 54);
            byte[][] byArrayArray = new byte[1][];
            this.checkError(this.t2cLobClose(this.m_nativeState, 112, byArray, byArray.length, byArrayArray));
            oracleClob.setShareBytes(byArrayArray[0]);
            return;
        }
    }

    @Override
    public boolean isOpen(OracleClob oracleClob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            byte[] byArray = null;
            this.checkTrue(this.lifecycle == 1, 8);
            this.checkTrue(oracleClob != null && (byArray = oracleClob.shareBytes()) != null, 54);
            boolean[] blArray = new boolean[1];
            this.checkError(this.t2cLobIsOpen(this.m_nativeState, 112, byArray, byArray.length, blArray));
            boolean bl = blArray[0];
            return bl;
        }
    }

    @Override
    public InputStream newInputStream(CLOB cLOB, int n2, long l2) throws SQLException {
        return this.newInputStream(cLOB, n2, l2, false);
    }

    @Override
    public InputStream newInputStream(CLOB cLOB, int n2, long l2, boolean bl) throws SQLException {
        return this.newInputStream((OracleClob)cLOB, n2, l2, bl);
    }

    @Override
    public InputStream newInputStream(OracleClob oracleClob, int n2, long l2) throws SQLException {
        return this.newInputStream(oracleClob, n2, l2, false);
    }

    @Override
    public InputStream newInputStream(OracleClob oracleClob, int n2, long l2, boolean bl) throws SQLException {
        if (l2 == 0L) {
            return new OracleClobInputStream(oracleClob, n2, bl);
        }
        return new OracleClobInputStream(oracleClob, n2, l2, bl);
    }

    @Override
    public OutputStream newOutputStream(CLOB cLOB, int n2, long l2, boolean bl) throws SQLException {
        return this.newOutputStream((OracleClob)cLOB, n2, l2, bl);
    }

    @Override
    public OutputStream newOutputStream(OracleClob oracleClob, int n2, long l2, boolean bl) throws SQLException {
        if (l2 == 0L) {
            if (bl & this.lobStreamPosStandardCompliant) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            return new OracleClobOutputStream(oracleClob, n2);
        }
        return new OracleClobOutputStream(oracleClob, n2, l2);
    }

    @Override
    public Reader newReader(CLOB cLOB, int n2, long l2) throws SQLException {
        return this.newReader((OracleClob)cLOB, n2, l2);
    }

    @Override
    public Reader newReader(OracleClob oracleClob, int n2, long l2) throws SQLException {
        if (l2 == 0L) {
            return new OracleClobReader(oracleClob, n2);
        }
        return new OracleClobReader(oracleClob, n2, l2);
    }

    @Override
    public Reader newReader(CLOB cLOB, int n2, long l2, long l3) throws SQLException {
        return this.newReader((OracleClob)cLOB, n2, l2, l3);
    }

    @Override
    public Reader newReader(OracleClob oracleClob, int n2, long l2, long l3) throws SQLException {
        return new OracleClobReader(oracleClob, n2, l2, l3);
    }

    @Override
    public Writer newWriter(CLOB cLOB, int n2, long l2, boolean bl) throws SQLException {
        return this.newWriter((OracleClob)cLOB, n2, l2, bl);
    }

    @Override
    public Writer newWriter(OracleClob oracleClob, int n2, long l2, boolean bl) throws SQLException {
        if (l2 == 0L) {
            if (bl & this.lobStreamPosStandardCompliant) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            return new OracleClobWriter(oracleClob, n2);
        }
        return new OracleClobWriter(oracleClob, n2, l2);
    }

    @Override
    public void registerTAFCallback(OracleOCIFailover oracleOCIFailover, Object object) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.appCallback = oracleOCIFailover;
            this.appCallbackObject = object;
            this.checkError(this.t2cRegisterTAFCallback(this.m_nativeState));
        }
    }

    int callTAFCallbackMethod(int n2, int n3) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n4 = 0;
            if (this.appCallback != null) {
                n4 = this.appCallback.callbackFn(this, this.appCallbackObject, n2, n3);
            }
            int n5 = n4;
            return n5;
        }
    }

    int callPDBChangeCallbackMethod(int n2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n3 = 0;
            try {
                this.onPDBChange(null);
                NTFPDBChangeEvent nTFPDBChangeEvent = new NTFPDBChangeEvent(this);
                this.notify(nTFPDBChangeEvent);
            }
            catch (SQLException sQLException) {
                n3 = -1;
            }
            int n4 = n3;
            return n4;
        }
    }

    @Override
    void onPDBChange(OracleStatement oracleStatement) throws SQLException {
        super.onPDBChange(oracleStatement);
    }

    @Override
    public boolean isValidCursorId(int n2) {
        return true;
    }

    @Override
    boolean isValidLight(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.checkAndDrain()) {
                boolean bl = false;
                return bl;
            }
            boolean bl = true;
            return bl;
        }
    }

    @Override
    boolean drainOnInbandNotification() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.t2cIsServerStatusValid(this.m_nativeState);
            if (!bl) {
                this.closeConnectionSafely();
                boolean bl2 = true;
                return bl2;
            }
            boolean bl3 = false;
            return bl3;
        }
    }

    @Override
    public int getHeapAllocSize() throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        int n2 = this.t2cGetHeapAllocSize(this.m_nativeState);
        if (n2 < 0) {
            if (n2 == -999) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23).fillInStackTrace();
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89).fillInStackTrace();
        }
        return n2;
    }

    @Override
    public int getOCIEnvHeapAllocSize() throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        int n2 = this.t2cGetOciEnvHeapAllocSize(this.m_nativeState);
        if (n2 < 0) {
            if (n2 == -999) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23).fillInStackTrace();
            }
            this.checkError(n2);
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89).fillInStackTrace();
        }
        return n2;
    }

    public static final short getClientCharSetId() {
        return 871;
    }

    public static short getDriverCharSetIdFromNLS_LANG() throws SQLException {
        short s2;
        if (!isLibraryLoaded) {
            T2CConnection.loadNativeLibrary();
        }
        if ((s2 = T2CConnection.t2cGetDriverCharSetFromNlsLang()) < 0) {
            throw (SQLException)DatabaseError.createSqlException(null, 8).fillInStackTrace();
        }
        return s2;
    }

    @Override
    void doProxySession(int n2, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        String[] stringArray;
        byte[] byArray;
        Object object = null;
        int n3 = 0;
        this.savedUser = this.userName;
        this.userName = null;
        byte[] byArray2 = byArray = new byte[0];
        byte[] byArray3 = byArray;
        byte[] byArray4 = byArray;
        byte[] byArray5 = byArray;
        switch (n2) {
            case 1: {
                this.userName = properties.getProperty("PROXY_USER_NAME");
                String string = properties.getProperty("PROXY_USER_PASSWORD");
                if (this.userName != null) {
                    byArray5 = DBConversion.stringToDriverCharBytes(this.userName, this.m_clientCharacterSet);
                }
                if (string == null) break;
                byArray4 = DBConversion.stringToDriverCharBytes(string, this.m_clientCharacterSet);
                break;
            }
            case 2: {
                String string = properties.getProperty("PROXY_DISTINGUISHED_NAME");
                if (string == null) break;
                byArray3 = DBConversion.stringToDriverCharBytes(string, this.m_clientCharacterSet);
                break;
            }
            case 3: {
                Object v2 = properties.get("PROXY_CERTIFICATE");
                byArray2 = (byte[])v2;
            }
        }
        if (this.editionName != null) {
            byArray = DBConversion.stringToDriverCharBytes(this.editionName, this.m_clientCharacterSet);
        }
        if ((stringArray = (String[])properties.get("PROXY_ROLES")) != null) {
            n3 = stringArray.length;
            object = new byte[n3][];
            for (int i2 = 0; i2 < n3; ++i2) {
                if (stringArray[i2] == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 150).fillInStackTrace();
                }
                object[i2] = DBConversion.stringToDriverCharBytes(stringArray[i2], this.m_clientCharacterSet);
            }
        }
        this.currentSchema = null;
        this.sqlWarning = this.checkError(this.t2cDoProxySession(this.m_nativeState, n2, byArray5, byArray5.length, byArray4, byArray4.length, byArray3, byArray3.length, byArray2, byArray2.length, byArray, byArray.length, n3, (byte[][])object), this.sqlWarning);
        this.isProxy = true;
    }

    @Override
    void closeProxySession() throws SQLException {
        this.checkError(this.t2cCloseProxySession(this.m_nativeState));
        this.currentSchema = null;
        this.userName = this.savedUser;
    }

    @Override
    protected void doDescribeTable(AutoKeyInfo autoKeyInfo) throws SQLException {
        int n2;
        boolean bl;
        String string = autoKeyInfo.getTableName();
        byte[] byArray = DBConversion.stringToDriverCharBytes(string, this.m_clientCharacterSet);
        do {
            bl = false;
            n2 = T2CConnection.t2cDescribeTable(this.m_nativeState, byArray, byArray.length, this.queryMetaData1, this.queryMetaData2, this.queryMetaData1Offset, this.queryMetaData2Offset, this.queryMetaData1Size, this.queryMetaData2Size);
            if (n2 == -1) {
                this.checkError(n2);
            }
            if (n2 != T2CStatement.T2C_EXTEND_BUFFER) continue;
            bl = true;
            this.reallocateQueryMetaData(this.queryMetaData1Size * 2, this.queryMetaData2Size * 2);
        } while (bl);
        this.processDescribeTableData(n2, autoKeyInfo);
    }

    private void processDescribeTableData(int n2, AutoKeyInfo autoKeyInfo) throws SQLException {
        short[] sArray = this.queryMetaData1;
        byte[] byArray = this.queryMetaData2;
        int n3 = this.queryMetaData1Offset;
        int n4 = this.queryMetaData2Offset;
        autoKeyInfo.allocateSpaceForDescribedData(n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            short s2 = sArray[n3 + 0];
            short s3 = sArray[n3 + 6];
            String string = T2CConnection.bytes2String(byArray, n4, s3, this.conversion);
            short s4 = sArray[n3 + 1];
            short s5 = sArray[n3 + 11];
            boolean bl = sArray[n3 + 2] != 0;
            short s6 = sArray[n3 + 5];
            short s7 = sArray[n3 + 3];
            short s8 = sArray[n3 + 4];
            short s9 = sArray[n3 + 13];
            boolean bl2 = sArray[n3 + 14] != 0;
            boolean bl3 = sArray[n3 + 15] != 0;
            n4 += s3;
            n3 += 16;
            String string2 = null;
            if (s9 > 0) {
                string2 = T2CConnection.bytes2String(byArray, n4, s9, this.conversion);
                n4 += s9;
            }
            autoKeyInfo.fillDescribedData(i2, string, s2, s5 > 0 ? s5 : s4, bl, s6, s7, s8, string2);
        }
    }

    @Override
    void doSetApplicationContext(String string, String string2, String string3) throws SQLException {
        if (this.m_nativeState != 0L) {
            this.checkError(this.t2cSetApplicationContext(this.m_nativeState, string, string2, string3));
        }
    }

    @Override
    void doClearAllApplicationContext(String string) throws SQLException {
        if (this.m_nativeState != 0L) {
            this.checkError(this.t2cClearAllApplicationContext(this.m_nativeState, string));
        }
    }

    @Override
    void doStartup(int n2) throws SQLException {
        this.checkError(this.t2cStartupDatabase(this.m_nativeState, n2, null));
    }

    @Override
    void doStartup(int n2, String string) throws SQLException {
        this.checkError(this.t2cStartupDatabase(this.m_nativeState, n2, string));
    }

    @Override
    void doShutdown(int n2) throws SQLException {
        this.checkError(this.t2cShutdownDatabase(this.m_nativeState, n2));
    }

    private static final void loadNativeLibrary() throws SQLException {
        if (!isLibraryLoaded) {
            try (Monitor.CloseableLock closeableLock = LOAD_LIBRARY_MONITOR.acquireCloseableLock();){
                if (!isLibraryLoaded) {
                    AccessController.doPrivileged(new PrivilegedAction<Object>(){

                        @Override
                        public Object run() {
                            System.loadLibrary(T2CConnection.OCILIBRARY);
                            int n2 = T2CConnection.getLibraryVersionNumber();
                            if ((long)n2 != JDBC_OCI_LIBRARY_VERSION) {
                                throw new Error("Incompatible version of libocijdbc[Jdbc:" + JDBC_OCI_LIBRARY_VERSION + ", Jdbc-OCI:" + n2);
                            }
                            return null;
                        }
                    });
                    isLibraryLoaded = true;
                }
            }
        }
    }

    private final void checkTrue(boolean bl, int n2) throws SQLException {
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), n2).fillInStackTrace();
        }
    }

    @Override
    boolean useLittleEndianSetCHARBinder() throws SQLException {
        return this.t2cPlatformIsLittleEndian(this.m_nativeState);
    }

    @Override
    public void getPropertyForPooledConnection(OraclePooledConnection oraclePooledConnection) throws SQLException {
        super.getPropertyForPooledConnection(oraclePooledConnection, this.password.get());
    }

    static final char[] getCharArray(String string) {
        char[] cArray = null;
        if (string == null) {
            cArray = new char[]{};
        } else {
            cArray = new char[string.length()];
            string.getChars(0, string.length(), cArray, 0);
        }
        return cArray;
    }

    static String bytes2String(byte[] byArray, int n2, int n3, DBConversion dBConversion) throws SQLException {
        byte[] byArray2 = new byte[n3];
        System.arraycopy(byArray, n2, byArray2, 0, n3);
        return dBConversion.CharBytesToString(byArray2, n3);
    }

    @DefaultLevel(value=Logging.FINEST)
    void disableNio() {
        this.useNio = false;
    }

    @Override
    public boolean isConnectionBigTZTC() throws SQLException {
        return this.getVersionNumber() >= 20000;
    }

    private static void doSetSessionTimeZone(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = SET_TIMEZONE_MONITOR.acquireCloseableLock();){
            T2CConnection.t2cSetSessionTimeZone(string);
        }
    }

    @Override
    public void incrementTempLobReferenceCount(byte[] byArray) throws SQLException {
    }

    @Override
    public int decrementTempLobReferenceCount(byte[] byArray) throws SQLException {
        return 0;
    }

    @Override
    void releaseConnectionToPool() throws SQLException {
        if (!this.drcpEnabled || this.drcpState == OracleConnection.DRCPState.DETACHED) {
            return;
        }
        this.drcpState = OracleConnection.DRCPState.DETACHED;
        this.closeStatements(false);
        this.purgeStatementCache();
        int n2 = 0;
        byte[] byArray = EMPTY_BYTES;
        if (this.drcpTagName != null) {
            this.tagMatched[0] = false;
            byArray = DBConversion.stringToDriverCharBytes(this.drcpTagName, this.m_clientCharacterSet);
            n2 = 2;
        } else {
            this.tagMatched[0] = true;
        }
        this.checkError(this.t2cCloseDrcpConnection(this.m_nativeState, byArray, byArray.length, n2), this.sqlWarning);
    }

    @Override
    boolean reusePooledConnection() throws SQLException {
        if (this.drcpState == OracleConnection.DRCPState.DETACHED) {
            this.drcpState = OracleConnection.DRCPState.ATTACHED_EXPLICIT;
            byte[] byArray = EMPTY_BYTES;
            if (this.drcpTagName != null) {
                byArray = DBConversion.stringToDriverCharBytes(this.drcpTagName, this.m_clientCharacterSet);
            }
            this.checkError(this.t2cOpenDrcpConnection(this.m_nativeState, byArray, byArray.length, this.tagMatched), this.sqlWarning);
            this.resetAfterReusePooledConnection();
        }
        return this.tagMatched[0];
    }

    @Override
    public boolean needToPurgeStatementCache() throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        if (!this.drcpEnabled) {
            return true;
        }
        return true;
    }

    @Override
    public String getCurrentSchema() throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        byte[] byArray = new byte[258];
        if (this.currentSchema == null && this.getVersionNumber() < 11100) {
            String string = super.getCurrentSchema();
        } else {
            String string;
            int n2 = this.t2cGetSchemaName(this.m_nativeState, byArray);
            this.checkError(n2);
            if (n2 > 0) {
                string = this.conversion.CharBytesToString(byArray, n2);
            } else {
                string = super.getCurrentSchema();
                if (string == null || string.length() == 0) {
                    string = this.userName;
                }
            }
            this.currentSchema = string;
        }
        return this.currentSchema;
    }

    @Override
    String getAuditBanner() throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        String string = null;
        if (this.getVersionNumber() >= 11100) {
            T2CBanner t2CBanner = new T2CBanner(this.conversion);
            this.checkError(this.t2cGetAuditBanner(this.m_nativeState, t2CBanner));
            string = t2CBanner.getBanner();
        }
        return string;
    }

    @Override
    String getAccessBanner() throws SQLException {
        if (this.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        String string = null;
        if (this.getVersionNumber() >= 11100) {
            T2CBanner t2CBanner = new T2CBanner(this.conversion);
            this.checkError(this.t2cGetAccessBanner(this.m_nativeState, t2CBanner));
            string = t2CBanner.getBanner();
        }
        return string;
    }

    static native int getLibraryVersionNumber();

    static native short t2cGetServerSessionInfo(long var0, Properties var2);

    static native short t2cGetDriverCharSetFromNlsLang();

    native int t2cDescribeError(long var1, T2CError var3, byte[] var4, long var5);

    native boolean t2cIsServerStatusValid(long var1);

    native int t2cCreateState(byte[] var1, int var2, byte[] var3, int var4, byte[] var5, int var6, byte[] var7, int var8, byte[] var9, int var10, byte[] var11, int var12, byte[] var13, int var14, boolean var15, byte[] var16, int var17, byte[] var18, int var19, short var20, int var21, byte[] var22, byte[] var23, byte[] var24, long[] var25);

    native int t2cLogon(long var1, byte[] var3, int var4, byte[] var5, int var6, byte[] var7, int var8, byte[] var9, int var10, byte[] var11, int var12, byte[] var13, int var14, byte[] var15, int var16, boolean var17, byte[] var18, int var19, byte[] var20, int var21, int var22, byte[] var23, byte[] var24, byte[] var25, long[] var26);

    private native int t2cLogoff(long var1);

    private native int t2cCancel(long var1);

    private native byte t2cGetAsmVolProperty(long var1);

    private native byte t2cGetInstanceType(long var1);

    private native int t2cCreateStatement(long var1, long var3, byte[] var5, int var6, OracleStatement var7, boolean var8, int var9);

    private native int t2cSetAutoCommit(long var1, boolean var3);

    private native int t2cCommit(long var1, int var3);

    private native int t2cRollback(long var1);

    private native int t2cPingDatabase(long var1);

    private native byte[] t2cGetProductionVersion(long var1, long[] var3);

    private native int t2cGetVersionNumber(long var1);

    private native int t2cGetDefaultStreamChunkSize(long var1);

    native int t2cGetFormOfUse(long var1, OracleTypeCLOB var3, byte[] var4, int var5, int var6);

    native long t2cGetTDO(long var1, byte[] var3, int var4, int[] var5);

    native int t2cCreateConnPool(byte[] var1, int var2, byte[] var3, int var4, byte[] var5, int var6, short var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14);

    native int t2cConnPoolLogon(long var1, byte[] var3, int var4, byte[] var5, int var6, byte[] var7, int var8, byte[] var9, int var10, byte[] var11, int var12, int var13, int var14, int var15, String[] var16, byte[] var17, int var18, byte[] var19, int var20, byte[] var21, int var22, byte[] var23, int var24, byte[] var25, int var26, byte[] var27, byte[] var28, long[] var29);

    native int t2cGetConnPoolInfo(long var1, Properties var3);

    native int t2cSetConnPoolInfo(long var1, int var3, int var4, int var5, int var6, int var7, int var8);

    native int t2cPasswordChange(long var1, byte[] var3, int var4, byte[] var5, int var6, byte[] var7, int var8);

    protected native byte[] t2cGetConnectionId(long var1);

    native int t2cGetHandles(long var1, long[] var3);

    native int t2cUseConnection(long var1, long var3, long var5, long var7, byte[] var9, long[] var10);

    native boolean t2cPlatformIsLittleEndian(long var1);

    native int t2cRegisterTAFCallback(long var1);

    native int t2cGetHeapAllocSize(long var1);

    native int t2cGetOciEnvHeapAllocSize(long var1);

    native int t2cDoProxySession(long var1, int var3, byte[] var4, int var5, byte[] var6, int var7, byte[] var8, int var9, byte[] var10, int var11, byte[] var12, int var13, int var14, byte[][] var15);

    native int t2cCloseProxySession(long var1);

    static native int t2cDescribeTable(long var0, byte[] var2, int var3, short[] var4, byte[] var5, int var6, int var7, int var8, int var9);

    native int t2cSetApplicationContext(long var1, String var3, String var4, String var5);

    native int t2cClearAllApplicationContext(long var1, String var3);

    native int t2cStartupDatabase(long var1, int var3, String var4);

    native int t2cShutdownDatabase(long var1, int var3);

    static native void t2cSetSessionTimeZone(String var0);

    native int t2cCloseDrcpConnection(long var1, byte[] var3, int var4, int var5);

    native int t2cOpenDrcpConnection(long var1, byte[] var3, int var4, boolean[] var5);

    native int t2cSetCachedServerVersion(long var1, short var3);

    native int t2cGetImplicitResultSetStatement(long var1, long var3, OracleStatement var5);

    native int t2cGetSchemaName(long var1, byte[] var3);

    native int t2cGetAccessBanner(long var1, T2CBanner var3);

    native int t2cGetAuditBanner(long var1, T2CBanner var3);

    static {
        LOAD_LIBRARY_MONITOR = Monitor.newInstance();
        SET_TIMEZONE_MONITOR = Monitor.newInstance();
        cachedVersionTable = new Hashtable<String, String>();
    }
}

