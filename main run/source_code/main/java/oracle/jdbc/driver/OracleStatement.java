/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.dms.instrument.ExecutionContext
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.sql.Array;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Wrapper;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.dms.instrument.ExecutionContext;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.ArrayDataResultSet;
import oracle.jdbc.driver.AutoKeyInfo;
import oracle.jdbc.driver.BfileAccessor;
import oracle.jdbc.driver.BinaryDoubleAccessor;
import oracle.jdbc.driver.BinaryFloatAccessor;
import oracle.jdbc.driver.BlobAccessor;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.CancelLock;
import oracle.jdbc.driver.CharAccessor;
import oracle.jdbc.driver.ClobAccessor;
import oracle.jdbc.driver.DMSFactory;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.DateAccessor;
import oracle.jdbc.driver.GeneratedStatement;
import oracle.jdbc.driver.IntervaldsAccessor;
import oracle.jdbc.driver.IntervalymAccessor;
import oracle.jdbc.driver.LongAccessor;
import oracle.jdbc.driver.LongRawAccessor;
import oracle.jdbc.driver.NTFDCNRegistration;
import oracle.jdbc.driver.NamedTypeAccessor;
import oracle.jdbc.driver.NumberAccessor;
import oracle.jdbc.driver.OracleInputStream;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OracleResultSetMetaData;
import oracle.jdbc.driver.OracleReturnResultSet;
import oracle.jdbc.driver.OracleSql;
import oracle.jdbc.driver.OracleStatementWrapper;
import oracle.jdbc.driver.OutRawAccessor;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.PlsqlBooleanAccessor;
import oracle.jdbc.driver.RawAccessor;
import oracle.jdbc.driver.ReadOnlyByteArray;
import oracle.jdbc.driver.RefTypeAccessor;
import oracle.jdbc.driver.ResultSetAccessor;
import oracle.jdbc.driver.ResultSetCache;
import oracle.jdbc.driver.ResultSetCacheEntry;
import oracle.jdbc.driver.RowidAccessor;
import oracle.jdbc.driver.T4CTTIoac;
import oracle.jdbc.driver.TimestampAccessor;
import oracle.jdbc.driver.TimestampltzAccessor;
import oracle.jdbc.driver.TimestamptzAccessor;
import oracle.jdbc.driver.VarcharAccessor;
import oracle.jdbc.driver.VarnumAccessor;
import oracle.jdbc.driver.Wrappable;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Log;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.ARRAY;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

@Supports(value={Feature.SQL_EXECUTION, Feature.COLUMN_GET, Feature.RESULT_FETCH})
@DefaultLogger(value="oracle.jdbc")
abstract class OracleStatement
extends GeneratedStatement
implements oracle.jdbc.internal.OracleStatement,
Wrappable<OracleStatementWrapper> {
    private static final int DEFAULT_ROW_PREFETCH_SIZE = Integer.parseInt("10");
    byte[] defineBytes;
    char[] defineChars;
    short[] defineIndicators;
    int[] returnParamMeta;
    byte[] shardingKeyRpnTokens;
    static final int PLAIN_STMT = 0;
    static final int PREP_STMT = 1;
    static final int CALL_STMT = 2;
    static final int METADATALENGTH = 1;
    static final int VALID_ROWS_UNINIT = -999;
    static final long VALID_ROWS_STREAM = -2L;
    static final byte EXECUTE_NONE = -1;
    static final byte EXECUTE_QUERY = 1;
    static final byte EXECUTE_UPDATE = 2;
    static final byte EXECUTE_NORMAL = 3;
    static final int DMLR_METADATA_PREFIX_SIZE = 3;
    static final int DMLR_METADATA_NUM_OF_RETURN_PARAMS = 0;
    static final int DMLR_METADATA_ROW_BIND_BYTES = 1;
    static final int DMLR_METADATA_ROW_BIND_CHARS = 2;
    static final int DMLR_METADATA_TYPE_OFFSET = 0;
    static final int DMLR_METADATA_IS_CHAR_TYPE_OFFSET = 1;
    static final int DMLR_METADATA_BIND_SIZE_OFFSET = 2;
    static final int DMLR_METADATA_FORM_OF_USE_OFFSET = 3;
    static final int DMLR_METADATA_PER_POSITION_SIZE = 4;
    static final String SYS_ODCIVARCHAR2LIST = "SYS.ODCIVARCHAR2LIST";
    static final OracleResultSet.ResultSetType DEFAULT_RESULT_SET_TYPE = OracleResultSet.ResultSetType.FORWARD_READ_ONLY;
    boolean closed;
    protected boolean isAllFetched;
    int cursorId;
    int refCursorRowNumber;
    ByteArray rowData = null;
    ByteArray bindData = null;
    boolean bindUseDBA = false;
    long[] bindDataOffsets = null;
    int[] bindDataLengths = null;
    long beyondBindData = 0L;
    int[] parameterMaxLength = null;
    int numberOfDefinePositions;
    int definesBatchSize;
    boolean described = false;
    boolean describedWithNames = false;
    boolean executeDoneForDefines = false;
    protected FetchMode fetchMode = FetchMode.OVERWRITE;
    protected long indexOfFirstRow = 0L;
    long rowsProcessed;
    protected long validRows;
    protected int storedRowCount;
    protected int currentCapacity = -1;
    private int numberOfUserColumns = -1;
    boolean isStreaming;
    boolean isFetchStreams;
    OracleStatement children = null;
    OracleStatement parent = null;
    OracleStatement nextChild = null;
    OracleStatement next;
    OracleStatement prev;
    long c_state;
    int numberOfBindPositions;
    int bindDBAOffset;
    byte[] bindBytes;
    char[] bindChars;
    short[] bindIndicators;
    int bindByteOffset;
    int bindCharOffset;
    int bindIndicatorOffset;
    int bindByteSubRange;
    int bindCharSubRange;
    int bindIndicatorSubRange;
    Accessor[] outBindAccessors;
    InputStream[][] parameterStream;
    Object[][] userStream;
    int firstRowInBatch;
    boolean hasIbtBind = false;
    byte[] ibtBindBytes;
    char[] ibtBindChars;
    short[] ibtBindIndicators;
    int ibtBindByteOffset;
    int ibtBindCharOffset;
    int ibtBindIndicatorOffset;
    int ibtBindIndicatorSize;
    OracleInputStream nextStream;
    OracleResultSet currentResultSet;
    ArrayDeque<OracleStatement> implicitResultSetStatements = null;
    ArrayDeque<OracleResultSet> openImplicitResultSets = null;
    Iterator<OracleStatement> implicitResultSetIterator = null;
    boolean processEscapes;
    boolean convertNcharLiterals;
    int queryTimeout;
    int maxFieldSize;
    long maxRows;
    int batch;
    boolean batchWasExecuted = false;
    int numberOfExecutedElementsInBatch = -1;
    int[] indexOfFailedElementsInBatch = null;
    int currentRank;
    boolean bsendBatchInProgress = false;
    long[] batchRowsUpdatedArray;
    int rowPrefetch;
    int rowPrefetchInLastFetch = -1;
    int defaultRowPrefetch;
    boolean rowPrefetchChanged;
    int defaultLobPrefetchSize;
    OracleSql sqlObject;
    boolean needToParse;
    boolean needToPrepareDefineBuffer;
    boolean columnsDefinedByUser;
    boolean gotLastBatch;
    boolean clearParameters;
    OracleStatement.SqlKind sqlKind = OracleStatement.SqlKind.SELECT;
    byte sqlKindByte = 1;
    boolean serverCursor;
    boolean fixedString = false;
    boolean noMoreUpdateCounts = false;
    protected CancelLock cancelLock = new CancelLock(this);
    OracleStatementWrapper wrapper;
    byte executionType = (byte)-1;
    OracleResultSet.ResultSetType userRsetType;
    OracleResultSet.ResultSetType realRsetType;
    boolean isRowidPrepended = false;
    SQLWarning sqlWarning;
    int cacheState = 3;
    int creationState = 0;
    boolean isOpen = false;
    int statementType = 0;
    boolean columnSetNull = false;
    boolean isDmlReturning = false;
    boolean returnParamsFetched;
    int rowsDmlReturned;
    int numReturnParams;
    boolean isAutoGeneratedKey;
    AutoKeyInfo autoKeyInfo;
    TimeZone defaultTimeZone = null;
    String defaultTimeZoneName = null;
    Calendar defaultCalendar = null;
    Calendar gmtCalendar = null;
    long inScn = 0L;
    OraclePreparedStatement refreshStatement = null;
    protected ResultSetCacheEntry cachedQueryResult = null;
    boolean resultFromCache = false;
    protected QueryCacheState queryCacheState = QueryCacheState.UNKNOWN;
    private static int COLUMN_NAME_CACHE_INITIAL_SIZE = 4;
    private IdentityHashMap<String, Integer> columnNameCache = new IdentityHashMap(COLUMN_NAME_CACHE_INITIAL_SIZE);
    static int GLOBAL_STATEMENT_NUMBER = 1;
    DMSFactory.DMSState dmsSqlText = null;
    DMSFactory.DMSPhase dmsExecute = null;
    DMSFactory.DMSPhase dmsFetch = null;
    ByteBuffer[] nioBuffers = null;
    Object[] lobPrefetchMetaData = null;
    boolean hasStream;
    byte[] tmpByteArray;
    int sizeTmpByteArray = 0;
    byte[] tmpBindsByteArray;
    boolean needToSendOalToFetch = false;
    int[] definedColumnType = null;
    int[] definedColumnSize = null;
    int[] definedColumnFormOfUse = null;
    T4CTTIoac[] oacdefSent = null;
    int[] nbPostPonedColumns = null;
    int[][] indexOfPostPonedColumn = null;
    boolean aFetchWasDoneDuringDescribe = false;
    boolean implicitDefineForLobPrefetchDone = false;
    long checkSum = 0L;
    boolean checkSumComputationFailure = false;
    Vector<String> m_batchItems = null;
    ArrayList<CLOB> tempStmtClobsToFree = null;
    ArrayList<BLOB> tempStmtBlobsToFree = null;
    ArrayList<CLOB> oldTempClobsToFree = null;
    ArrayList<BLOB> oldTempBlobsToFree = null;
    ArrayList<CLOB> tempRowClobsToFree = new ArrayList();
    ArrayList<BLOB> tempRowBlobsToFree = new ArrayList();
    NTFDCNRegistration registration = null;
    String[] dcnTableName = null;
    long dcnQueryId = -1L;
    long localCheckSum = 0L;
    OracleStatement.BindChecksumListener bindChecksumListener;
    boolean isCloseOnCompletion = false;
    protected Object acProxy;
    private byte[] querycacheCompileKey;
    private long queryId;
    byte[] runtimeKey = null;
    MessageDigest md = null;

    abstract void doDescribe(boolean var1) throws SQLException;

    abstract void executeForDescribe() throws SQLException;

    abstract void executeForRows(boolean var1) throws SQLException;

    protected abstract void fetch(int var1, boolean var2) throws SQLException;

    abstract void doClose() throws SQLException;

    abstract void closeQuery() throws SQLException;

    public int cursorIfRefCursor() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "cursorIfRefCursor not implemented").fillInStackTrace();
    }

    void continueReadRow(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "continueReadRow is only implemented by the T4C statements.").fillInStackTrace();
    }

    void closeCursorOnPlainStatement() throws SQLException {
    }

    static final byte convertSqlKindEnumToByte(OracleStatement.SqlKind sqlKind) {
        return sqlKind.getKind();
    }

    static final OracleStatement.SqlKind convertSqlKindByteToEnum(byte by) {
        return OracleStatement.SqlKind.valueOf(by);
    }

    OracleStatement(PhysicalConnection physicalConnection, int n2, int n3) throws SQLException {
        this(physicalConnection, n2, n3, -1, -1);
    }

    OracleStatement(PhysicalConnection physicalConnection, int n2, int n3, int n4, int n5) throws SQLException {
        super(physicalConnection);
        this.initStatement(n2, n3, n4, n5, physicalConnection.useFetchSizeWithLongColumn);
    }

    OracleStatement(PhysicalConnection physicalConnection, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        super(physicalConnection);
        int n2 = physicalConnection.defaultExecuteBatch;
        int n3 = physicalConnection.defaultRowPrefetch;
        boolean bl = physicalConnection.useFetchSizeWithLongColumn;
        int n4 = -1;
        int n5 = -1;
        if (properties != null) {
            String string = properties.getProperty("result_set_type");
            if (string != null) {
                n4 = Integer.parseInt(string);
            }
            if ((string = properties.getProperty("result_set_concurrency")) != null) {
                n5 = Integer.parseInt(string);
            }
            if ((string = properties.getProperty("use_long_fetch")) != null) {
                boolean bl2 = bl = string.compareToIgnoreCase("true") == 0;
            }
            if ((string = properties.getProperty("execute_batch")) != null) {
                n2 = Integer.parseInt(string);
            }
            if ((string = properties.getProperty("row_prefetch")) != null) {
                n3 = Integer.parseInt(string);
            }
        }
        this.initStatement(n2, n3, n4, n5, bl);
    }

    private void initStatement(int n2, int n3, int n4, int n5, boolean bl) throws SQLException {
        this.connection.needLine();
        this.sqlObject = new OracleSql(this.connection.conversion);
        this.processEscapes = this.connection.processEscapes;
        this.convertNcharLiterals = this.connection.convertNcharLiterals;
        this.gotLastBatch = false;
        this.closed = false;
        this.clearParameters = true;
        this.serverCursor = this.connection.getCreateStatementAsRefCursor();
        this.fixedString = this.connection.getDefaultFixedString();
        this.rowPrefetchChanged = false;
        this.currentCapacity = this.rowPrefetch = n3;
        this.defaultRowPrefetch = n3;
        this.batch = n2;
        this.userRsetType = OracleResultSet.ResultSetType.typeFor(n4, n5);
        if (this.userRsetType == OracleResultSet.ResultSetType.UNKNOWN) {
            this.userRsetType = DEFAULT_RESULT_SET_TYPE;
            this.realRsetType = DEFAULT_RESULT_SET_TYPE;
        } else {
            this.realRsetType = OracleResultSet.ResultSetType.UNKNOWN;
        }
        this.isFetchStreams = bl || this.userRsetType != DEFAULT_RESULT_SET_TYPE;
        this.defaultLobPrefetchSize = this.connection.getVersionNumber() >= 11000 ? this.connection.defaultLobPrefetchSize : -1;
        this.needToParse = true;
        this.needToPrepareDefineBuffer = true;
        this.columnsDefinedByUser = false;
        this.configureRowData();
        this.connection.addStatement(this);
        this.createDMSSensors();
    }

    @Override
    public void setWrapper(OracleStatementWrapper oracleStatementWrapper) {
        this.wrapper = oracleStatementWrapper;
    }

    @Override
    public void setSnapshotSCN(long l2) throws SQLException {
        this.connection.isResultSetCacheEnabled = false;
        this.doSetSnapshotSCN(l2);
    }

    void doSetSnapshotSCN(long l2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("doSetSnapshotSCN").fillInStackTrace();
    }

    void createDMSSensors() throws SQLException {
        this.dmsSqlText = this.connection.commonDmsSqlText;
        if (this.connection.dmsUpdateSqlText()) {
            this.dmsSqlText.update(this.sqlObject.toString());
        }
        this.dmsExecute = this.connection.commonDmsExecute;
        this.dmsFetch = this.connection.commonDmsFetch;
    }

    void destroyDMSSensors() throws SQLException {
        if (this.dmsSqlText != null && this.dmsSqlText != this.connection.commonDmsSqlText) {
            this.dmsSqlText.destroy();
            this.dmsSqlText = null;
        }
        if (this.dmsExecute != null && this.dmsExecute != this.connection.commonDmsExecute) {
            this.dmsExecute.destroy();
            this.dmsExecute = null;
        }
        if (this.dmsFetch != null && this.dmsFetch != this.connection.commonDmsFetch) {
            this.dmsFetch.destroy();
            this.dmsFetch = null;
        }
    }

    protected abstract void configureRowData();

    void prepareAccessors() throws SQLException {
        int n2;
        if (this.accessors == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 21).fillInStackTrace();
        }
        for (n2 = 0; n2 < this.numberOfDefinePositions; ++n2) {
            Accessor accessor = this.accessors[n2];
            if (accessor == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 21).fillInStackTrace();
            }
            switch (accessor.internalType) {
                case 8: 
                case 24: {
                    this.hasStream = true;
                }
            }
        }
        if (this.streamList != null && !this.isFetchStreams) {
            this.rowPrefetch = 1;
        }
        this.definesBatchSize = n2 = this.rowPrefetch;
        for (int i2 = 0; i2 < this.numberOfDefinePositions; ++i2) {
            Accessor accessor = this.accessors[i2];
            accessor.setCapacity(n2);
        }
    }

    boolean checkAccessorsUsable() throws SQLException {
        int n2 = this.accessors.length;
        if (n2 < this.numberOfDefinePositions) {
            return false;
        }
        boolean bl = true;
        boolean bl2 = false;
        boolean bl3 = false;
        for (int i2 = 0; i2 < this.numberOfDefinePositions; ++i2) {
            Accessor accessor = this.accessors[i2];
            if (accessor == null || accessor.externalType == 0) {
                bl = false;
                continue;
            }
            bl2 = true;
        }
        if (bl) {
            bl3 = true;
        } else {
            if (bl2) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 21).fillInStackTrace();
            }
            this.columnsDefinedByUser = false;
        }
        return bl3;
    }

    void executeMaybeDescribe() throws SQLException {
        boolean bl = true;
        this.needToPrepareDefineBuffer = !this.isDefineBufferPreparedForExecute();
        this.rowPrefetchChanged = false;
        try {
            this.cancelLock.enterExecuting();
            if (this.needToPrepareDefineBuffer) {
                this.prepareDefineBufferAndExecute();
            } else {
                this.markDanglingAccessors();
                this.executeForRows(false);
            }
            this.handleExecuteMaybeDescribeCompletion();
        }
        catch (SQLException sQLException) {
            this.needToParse = true;
            throw sQLException;
        }
        finally {
            this.cancelLock.exitExecuting();
        }
    }

    private final boolean isDefineBufferPreparedForExecute() throws SQLException {
        if (this.needToPrepareDefineBuffer) {
            return false;
        }
        if (this.rowPrefetchChanged && this.streamList == null && this.rowPrefetch > this.definesBatchSize) {
            return false;
        }
        if (this.accessors == null) {
            return false;
        }
        if (this.columnsDefinedByUser) {
            return this.checkAccessorsUsable();
        }
        return true;
    }

    private final void prepareDefineBufferAndExecute() throws SQLException {
        boolean bl = false;
        boolean bl2 = true;
        if (!this.columnsDefinedByUser) {
            this.executeForDescribe();
            bl = true;
            boolean bl3 = bl2 = !this.aFetchWasDoneDuringDescribe;
        }
        if (this.needToPrepareDefineBuffer) {
            this.prepareAccessors();
        }
        this.markDanglingAccessors();
        if (bl2) {
            this.executeForRows(bl);
        }
    }

    private final void markDanglingAccessors() {
        int n2 = this.accessors.length;
        for (int i2 = this.numberOfDefinePositions; i2 < n2; ++i2) {
            Accessor accessor = this.accessors[i2];
            if (accessor == null) continue;
            accessor.rowSpaceIndicator = null;
        }
    }

    private final void handleExecuteMaybeDescribeCompletion() {
        this.currentCapacity = this.rowPrefetch;
        this.storedRowCount = this.validRows == -2L ? 1 : (int)this.validRows;
        this.indexOfFirstRow = 0L;
    }

    void adjustGotLastBatch() {
    }

    abstract void locationToPutBytes(Accessor var1, int var2, int var3) throws SQLException;

    protected boolean isQueryResultCached() throws SQLException {
        return false;
    }

    protected void cacheQueryResultIfAppropriate() throws SQLException {
    }

    protected void useCachedQueryResult() throws SQLException {
        this.rowData.free();
        this.rowData = this.cachedQueryResult.getRowData();
        this.accessors = this.cachedQueryResult.newAccessors(this);
        this.storedRowCount = this.cachedQueryResult.getNumberOfRows();
        this.validRows = this.cachedQueryResult.getNumberOfRows();
        this.cachedQueryResult = null;
        this.isAllFetched = true;
        this.resultFromCache = true;
    }

    @Log
    private void logSQL(Logger logger, String string) {
        if (logger == null || string == null) {
            return;
        }
        logger.log(Level.CONFIG, Integer.toHexString(this.hashCode()).toUpperCase() + " SQL: " + this.sqlObject.getOriginalSql());
    }

    void doExecuteWithTimeout() throws SQLException {
        this.prepareForExecuteWithTimeout();
        long l2 = this.prepareDmsForExecution();
        try {
            this.prepareDmsSystemForExecution();
            this.cleanOldTempLobs();
            this.rowsProcessed = 0L;
            if (this.sqlKind.isSELECT()) {
                this.executeSQLSelect();
            } else {
                this.executeSQLStatement();
            }
            this.updateDmsSystemAfterExecution();
        }
        catch (SQLException sQLException) {
            this.connection.resetSystemContext();
            this.resetOnExceptionDuringExecute();
            throw sQLException;
        }
        finally {
            this.updateDmsAfterExecution(l2);
        }
    }

    private final void prepareForExecuteWithTimeout() throws SQLException {
        if (this.sqlObject.isConnectionValidationSql()) {
            this.connection.checkAndDrain();
        }
        if (this.realRsetType == OracleResultSet.ResultSetType.UNKNOWN) {
            this.realRsetType = this.userRsetType;
        }
    }

    private final long prepareDmsForExecution() {
        if (this.dmsExecute != null) {
            return this.dmsExecute.start();
        }
        return 0L;
    }

    private final void updateDmsAfterExecution(long l2) {
        if (this.dmsExecute != null) {
            this.dmsExecute.stop(l2);
        }
    }

    private void prepareDmsSystemForExecution() throws SQLException {
        this.connection.updateSystemContext();
        if (this.connection.dmsUpdateSqlText()) {
            this.dmsSqlText.update(this.sqlObject.toString());
        }
    }

    private void updateDmsSystemAfterExecution() {
        if (this.connection.dmsVersion.equals((Object)DMSFactory.DMSVersion.v10G)) {
            ExecutionContext.get().setECIDSequenceNumber((int)this.connection.endToEndECIDSequenceNumber);
        } else if (this.connection.dmsVersion.equals((Object)DMSFactory.DMSVersion.v11)) {
            DMSFactory.Context.getECForJDBC().finished();
        }
    }

    private final void executeSQLSelect() throws SQLException {
        if (this.connection.j2ee13Compliant) {
            this.ensureJ2EE13ComplianceForSelectSQL();
        }
        if (this.isQueryResultCached()) {
            this.useCachedQueryResult();
        } else {
            if (this.rowData instanceof ReadOnlyByteArray) {
                this.restoreRowDataFromBindData();
            }
            try {
                this.prepareConnectionForExecution();
                this.executeMaybeDescribe();
            }
            finally {
                this.resetConnectionAfterExecution();
            }
            this.handleExecuteSQLSelectCompletion();
        }
        if (this.serverCursor) {
            this.adjustGotLastBatch();
        }
    }

    private final void ensureJ2EE13ComplianceForSelectSQL() throws SQLException {
        if (this.executionType == 2) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 129).fillInStackTrace();
        }
    }

    private final void restoreRowDataFromBindData() throws SQLException {
        this.rowData = this.bindData;
        if (this.accessors == null) {
            return;
        }
        for (Accessor accessor : this.accessors) {
            if (accessor == null) continue;
            accessor.reinitForResultSetCache(this.rowData, this);
        }
    }

    private final void prepareConnectionForExecution() throws SQLException {
        this.connection.needLine();
        if (!this.isOpen) {
            this.connection.open(this);
            this.isOpen = true;
        }
        if (this.queryTimeout != 0) {
            this.connection.getTimeout().setTimeout((long)this.queryTimeout * 1000L, this);
        }
    }

    private final void resetConnectionAfterExecution() throws SQLException {
        if (this.queryTimeout != 0) {
            this.connection.getTimeout().cancelTimeout();
        }
    }

    private final void handleExecuteSQLSelectCompletion() throws SQLException {
        this.cacheQueryResultIfAppropriate();
        this.checkValidRowsStatus();
    }

    private final void executeSQLStatement() throws SQLException {
        if (this.connection.j2ee13Compliant) {
            this.ensureJ2EE13ComplianceForNonSelectSQL();
        }
        ++this.currentRank;
        if (this.currentRank >= this.batch) {
            try {
                this.cancelLock.enterExecuting();
                this.prepareConnectionForExecution();
                this.executeForRows(false);
            }
            catch (SQLException sQLException) {
                this.handleExecuteSQLStatementFailure(sQLException);
                throw sQLException;
            }
            finally {
                this.cancelLock.exitExecuting();
                this.handleExecuteSQLStatementCompletionAlways();
            }
        }
    }

    private final void ensureJ2EE13ComplianceForNonSelectSQL() throws SQLException {
        if (!this.sqlKind.isPlsqlOrCall() && this.executionType == 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 128).fillInStackTrace();
        }
    }

    private final void handleExecuteSQLStatementFailure(SQLException sQLException) throws SQLException {
        this.needToParse = true;
        if (this.batch > 1) {
            this.clearBatchCritical();
            throw this.createBatchUpdateException(sQLException);
        }
        this.resetCurrentRowBinders();
    }

    private final void handleExecuteSQLStatementCompletionAlways() throws SQLException {
        this.resetConnectionAfterExecution();
        this.currentRank = 0;
        this.checkValidRowsStatus();
    }

    private BatchUpdateException createBatchUpdateException(SQLException sQLException) {
        int n2;
        int[] nArray;
        if (this.numberOfExecutedElementsInBatch != -1 && this.numberOfExecutedElementsInBatch < this.batch) {
            nArray = new int[this.numberOfExecutedElementsInBatch];
            for (n2 = 0; n2 < nArray.length; ++n2) {
                nArray[n2] = -2;
            }
        } else {
            nArray = new int[this.batch];
            for (n2 = 0; n2 < nArray.length; ++n2) {
                nArray[n2] = -3;
            }
        }
        BatchUpdateException batchUpdateException = DatabaseError.createBatchUpdateException(sQLException, nArray.length, nArray);
        batchUpdateException.fillInStackTrace();
        return batchUpdateException;
    }

    void resetOnExceptionDuringExecute() {
        this.needToParse = true;
    }

    void resetCurrentRowBinders() {
    }

    void open() throws SQLException {
        if (!this.isOpen) {
            this.connection.needLine();
            this.connection.open(this);
            this.isOpen = true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public ResultSet executeQuery(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.cleanUpBeforeExecute();
            OracleResultSet oracleResultSet = null;
            this.realRsetType = OracleResultSet.ResultSetType.UNKNOWN;
            try {
                this.executionType = 1;
                this.noMoreUpdateCounts = false;
                this.ensureOpen();
                this.checkIfBatchExists();
                this.sendBatch();
                this.hasStream = false;
                this.sqlObject.initialize(string);
                this.sqlKind = this.sqlObject.getSqlKind();
                this.needToParse = true;
                this.prepareForNewResults(true, true, true);
                if (this.userRsetType == DEFAULT_RESULT_SET_TYPE) {
                    this.doExecuteWithTimeout();
                    if (this.implicitResultSetStatements == null) {
                        if (this.sqlKind.isPlsqlOrCall()) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 128).fillInStackTrace();
                        }
                        if (this.validRows < 1L && this.validRows != -2L) {
                            this.isAllFetched = true;
                        }
                        oracleResultSet = this.currentResultSet = this.createResultSet();
                    }
                } else {
                    oracleResultSet = this.doScrollStmtExecuteQuery();
                    if (oracleResultSet == null && this.implicitResultSetStatements == null) {
                        if (this.sqlKind.isPlsqlOrCall()) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 128).fillInStackTrace();
                        }
                        if (this.validRows < 1L && this.validRows != -2L) {
                            this.isAllFetched = true;
                        }
                        oracleResultSet = this.currentResultSet = this.createResultSet();
                    }
                }
            }
            finally {
                this.executionType = (byte)-1;
            }
            OracleResultSet oracleResultSet2 = oracleResultSet;
            return oracleResultSet2;
        }
    }

    @Override
    public void closeWithKey(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("closeWithKey").fillInStackTrace();
    }

    @Override
    public void close() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.closeOrCache(null);
        }
    }

    void closeWrapper(boolean bl) throws SQLException {
        if (this.wrapper != null) {
            this.wrapper.beClosed(bl);
        }
    }

    protected void closeOrCache(String string) throws SQLException {
        if (this.closed) {
            return;
        }
        if (this.connection.lifecycle == 2) {
            this.connection.needLineUnchecked();
        } else {
            this.connection.needLine();
        }
        if (this.statementType != 0 && this.cacheState != 0 && this.cacheState != 3 && this.connection.isStatementCacheInitialized()) {
            if (string == null) {
                if (this.connection.getImplicitCachingEnabled()) {
                    this.connection.cacheImplicitStatement((OraclePreparedStatement)this, this.sqlObject.getOriginalSql(), this.statementType, this.userRsetType);
                } else {
                    this.cacheState = 0;
                    this.hardClose();
                }
            } else if (this.connection.getExplicitCachingEnabled()) {
                this.connection.cacheExplicitStatement((OraclePreparedStatement)this, string);
            } else {
                this.cacheState = 0;
                this.hardClose();
            }
        } else {
            this.hardClose();
        }
    }

    protected void hardClose() throws SQLException {
        this.hardClose(true);
    }

    private void hardClose(boolean bl) throws SQLException {
        this.alwaysOnClose();
        this.describedWithNames = false;
        this.described = false;
        this.connection.removeStatement(this);
        this.clearDefines();
        if (this.isOpen && bl && (this.connection.lifecycle == 1 || this.connection.lifecycle == 16 || this.connection.lifecycle == 2)) {
            this.doClose();
            this.isOpen = false;
            if (this.refreshStatement != null) {
                this.refreshStatement.close();
            }
        }
        this.sqlObject = null;
        this.destroyDMSSensors();
    }

    protected void alwaysOnClose() throws SQLException {
        if (this.implicitResultSetStatements != null) {
            for (OracleStatement wrapper : this.implicitResultSetStatements) {
                wrapper.close();
            }
            if (this.openImplicitResultSets != null) {
                for (OracleResultSet oracleResultSet : this.openImplicitResultSets) {
                    oracleResultSet.close();
                }
            }
        }
        Iterator<OracleResultSet> iterator = this.children;
        while (iterator != null) {
            OracleStatement oracleStatement = ((OracleStatement)((Object)iterator)).nextChild;
            ((OracleStatement)((Object)iterator)).close();
            iterator = oracleStatement;
        }
        if (this.parent != null) {
            this.parent.removeChild(this);
        }
        this.closed = true;
        if (this.connection != null && (this.connection.lifecycle == 1 || this.connection.lifecycle == 2 || this.connection.lifecycle == 8) && this.currentResultSet != null) {
            this.currentResultSet.doneFetchingRows(false);
            this.currentResultSet.close();
            this.currentResultSet = null;
        }
        this.sqlWarning = null;
        this.m_batchItems = null;
    }

    void closeLeaveCursorOpen() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                return;
            }
            this.hardClose(false);
        }
    }

    @Override
    public int executeUpdate(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n2 = (int)this.executeLargeUpdate(string);
            return n2;
        }
    }

    @Override
    public long executeLargeUpdate(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.cleanUpBeforeExecute();
            long l2 = this.executeUpdateInternal(string);
            return l2;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    long executeUpdateInternal(String string) throws SQLException {
        try {
            if (this.executionType == -1) {
                this.executionType = (byte)2;
            }
            this.noMoreUpdateCounts = false;
            this.ensureOpen();
            this.checkIfBatchExists();
            this.sendBatch();
            this.hasStream = false;
            this.sqlObject.initialize(string);
            this.sqlKind = this.sqlObject.getSqlKind();
            this.needToParse = true;
            this.prepareForNewResults(true, true, true);
            if (this.userRsetType == DEFAULT_RESULT_SET_TYPE) {
                this.doExecuteWithTimeout();
            } else {
                this.doScrollStmtExecuteQuery();
            }
            long l2 = this.validRows;
            return l2;
        }
        finally {
            this.executionType = (byte)-1;
        }
    }

    @Override
    public boolean execute(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.cleanUpBeforeExecute();
            boolean bl = this.executeInternal(string);
            return bl;
        }
    }

    boolean executeInternal(String string) throws SQLException {
        try {
            this.executionType = (byte)3;
            this.checkSum = 0L;
            this.checkSumComputationFailure = false;
            this.noMoreUpdateCounts = false;
            this.ensureOpen();
            this.checkIfBatchExists();
            this.sendBatch();
            this.hasStream = false;
            this.sqlObject.initialize(string);
            this.sqlKind = this.sqlObject.getSqlKind();
            this.needToParse = true;
            this.prepareForNewResults(true, true, true);
            if (this.isCloseOnCompletion) {
                this.ensureOpen();
            }
            this.runtimeKey = null;
            if (this.userRsetType == DEFAULT_RESULT_SET_TYPE) {
                this.doExecuteWithTimeout();
            } else {
                this.doScrollStmtExecuteQuery();
            }
            boolean bl = this.sqlKind.isSELECT() || this.implicitResultSetStatements != null;
            return bl;
        }
        finally {
            this.executionType = (byte)-1;
        }
    }

    OracleResultSet createResultSet() throws SQLException {
        if (this.sqlKind.isSELECT() && this.batchWasExecuted) {
            return null;
        }
        this.computeOffsetOfFirstUserColumn();
        this.computeNumberOfUserColumns();
        return OracleResultSet.createResultSet(this);
    }

    final int getNumberOfUserColumns() throws SQLException {
        return this.numberOfUserColumns;
    }

    protected final void computeNumberOfUserColumns() throws SQLException {
        if (this.serverCursor) {
            this.numberOfUserColumns = this.accessors == null ? 0 : this.accessors.length;
        } else if (this.sqlKind.isSELECT()) {
            this.ensureOpen();
            if (!this.described) {
                try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                    this.doDescribe(false);
                    this.described = true;
                }
            }
            this.numberOfUserColumns = this.numberOfDefinePositions - (1 + this.offsetOfFirstUserColumn);
        } else {
            this.numberOfUserColumns = this.numReturnParams;
        }
    }

    Accessor[] getDescription() throws SQLException {
        this.ensureOpen();
        if (!this.described) {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                this.doDescribe(false);
                this.described = true;
            }
        }
        return this.accessors;
    }

    Accessor[] getDescriptionWithNames() throws SQLException {
        this.ensureOpen();
        if (!this.describedWithNames) {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                this.doDescribe(true);
                this.described = true;
                this.describedWithNames = true;
            }
        }
        return this.accessors;
    }

    @Override
    public OracleStatement.SqlKind getSqlKind() throws SQLException {
        return this.sqlObject.getSqlKind();
    }

    @Override
    public void clearDefines() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.freeLine();
            this.streamList = null;
            this.columnsDefinedByUser = false;
            this.needToPrepareDefineBuffer = true;
            this.numberOfDefinePositions = 0;
            this.definesBatchSize = 0;
            this.described = false;
            this.describedWithNames = false;
            this.cleanupDefines();
        }
    }

    void reparseOnRedefineIfNeeded() throws SQLException {
    }

    void defineColumnTypeInternal(int n2, int n3, int n4, boolean bl, String string) throws SQLException {
        this.defineColumnTypeInternal(n2, n3, n4, (short)1, bl, string);
    }

    void defineColumnTypeInternal(int n2, int n3, int n4, short s2, boolean bl, String string) throws SQLException {
        int n5;
        if (this.connection.disableDefinecolumntype) {
            return;
        }
        if (n2 < 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (n3 == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
        int n6 = n2 - 1;
        int n7 = n5 = this.maxFieldSize > 0 ? this.maxFieldSize : -1;
        if (!bl) {
            if (n4 < 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 53).fillInStackTrace();
            }
            if (n3 == 2005 || n3 == 2004) {
                if (n5 == -1 && n4 > 0 || n5 > 0 && n4 < n5) {
                    n5 = n4;
                }
            } else {
                n5 = -1;
            }
        }
        if (this.currentResultSet != null && !this.currentResultSet.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 28).fillInStackTrace();
        }
        if (!this.columnsDefinedByUser) {
            this.clearDefines();
            this.columnsDefinedByUser = true;
        }
        if (this.numberOfDefinePositions < n2) {
            if (this.accessors == null || this.accessors.length < n2) {
                Accessor[] accessorArray = new Accessor[n2 << 1];
                if (this.accessors != null) {
                    System.arraycopy(this.accessors, 0, accessorArray, 0, this.numberOfDefinePositions);
                }
                this.accessors = accessorArray;
            }
            this.numberOfDefinePositions = n2;
        }
        switch (n3) {
            case -16: 
            case -15: 
            case -9: 
            case 2011: {
                s2 = (short)2;
                break;
            }
            case 2009: {
                string = "SYS.XMLTYPE";
                break;
            }
        }
        int n8 = this.getInternalType(n3);
        if (!(n8 != 109 && n8 != 111 || string != null && !string.equals(""))) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 60, "Invalid arguments").fillInStackTrace();
        }
        Accessor accessor = this.accessors[n6];
        boolean bl2 = true;
        if (accessor != null) {
            int n9 = accessor.useForDataAccessIfPossible(n8, n3, n5, string);
            if (n9 == 0) {
                s2 = accessor.formOfUse;
                accessor = null;
                this.reparseOnRedefineIfNeeded();
            } else if (n9 == 1) {
                accessor = null;
                this.reparseOnRedefineIfNeeded();
            } else if (n9 == 2) {
                bl2 = false;
            }
        }
        if (bl2) {
            this.needToPrepareDefineBuffer = true;
        }
        if (accessor == null) {
            this.accessors[n6] = this.allocateAccessor(n8, n3, n2, n5, s2, string, false);
            this.described = false;
            this.describedWithNames = false;
        }
        this.executeDoneForDefines = false;
    }

    Accessor allocateAccessor(int n2, int n3, int n4, int n5, short s2, String string, boolean bl) throws SQLException {
        switch (n2) {
            case 96: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new CharAccessor(this, n5, s2, n3, bl);
            }
            case 8: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                if (!bl) {
                    return new LongAccessor(this, n4, n5, s2, n3);
                }
            }
            case 1: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new VarcharAccessor(this, n5, s2, n3, bl);
            }
            case 2: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new NumberAccessor(this, n5, s2, n3, bl);
            }
            case 252: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new PlsqlBooleanAccessor(this, n5, s2, n3, bl);
            }
            case 6: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new VarnumAccessor(this, n5, s2, n3, bl);
            }
            case 24: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                if (!bl) {
                    return new LongRawAccessor(this, n4, n5, s2, n3);
                }
            }
            case 23: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                if (bl) {
                    return new OutRawAccessor(this, n5, s2, n3);
                }
                return new RawAccessor(this, n5, s2, n3, false);
            }
            case 100: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new BinaryFloatAccessor(this, n5, s2, n3, bl);
            }
            case 101: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new BinaryDoubleAccessor(this, n5, s2, n3, bl);
            }
            case 104: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                if (this.sqlKind == OracleStatement.SqlKind.CALL_BLOCK) {
                    n5 = 18;
                    VarcharAccessor varcharAccessor = new VarcharAccessor(this, n5, s2, n3, bl);
                    varcharAccessor.definedColumnType = -8;
                    return varcharAccessor;
                }
                return new RowidAccessor(this, n5, s2, n3, bl);
            }
            case 102: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new ResultSetAccessor(this, n5, s2, n3, bl);
            }
            case 12: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new DateAccessor(this, n5, s2, n3, bl);
            }
            case 113: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                BlobAccessor blobAccessor = new BlobAccessor(this, -1, s2, n3, bl);
                return blobAccessor;
            }
            case 112: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                ClobAccessor clobAccessor = new ClobAccessor(this, -1, s2, n3, bl);
                return clobAccessor;
            }
            case 114: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                BfileAccessor bfileAccessor = new BfileAccessor(this, -1, s2, n3, bl);
                return bfileAccessor;
            }
            case 109: {
                if (string == null) {
                    if (bl) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 60, "Unable to resolve type \"null\"").fillInStackTrace();
                }
                NamedTypeAccessor namedTypeAccessor = new NamedTypeAccessor(this, string, s2, n3, bl);
                ((Accessor)namedTypeAccessor).initMetadata();
                return namedTypeAccessor;
            }
            case 111: {
                if (string == null) {
                    if (bl) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 60, "Unable to resolve type \"null\"").fillInStackTrace();
                }
                RefTypeAccessor refTypeAccessor = new RefTypeAccessor(this, string, s2, n3, bl);
                ((Accessor)refTypeAccessor).initMetadata();
                return refTypeAccessor;
            }
            case 180: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new TimestampAccessor(this, n5, s2, n3, bl);
            }
            case 181: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new TimestamptzAccessor(this, n5, s2, n3, bl);
            }
            case 231: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new TimestampltzAccessor(this, n5, s2, n3, bl);
            }
            case 182: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new IntervalymAccessor(this, n5, s2, n3, bl);
            }
            case 183: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                return new IntervaldsAccessor(this, n5, s2, n3, bl);
            }
            case 995: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89).fillInStackTrace();
            }
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
    }

    void setDriverSpecificData(Accessor accessor) {
    }

    @Override
    public void defineColumnType(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.defineColumnTypeInternal(n2, n3, -1, true, null);
        }
    }

    @Override
    public void defineColumnType(int n2, int n3, int n4) throws SQLException {
        this.defineColumnTypeInternal(n2, n3, n4, false, null);
    }

    @Override
    public void defineColumnType(int n2, int n3, int n4, short s2) throws SQLException {
        this.defineColumnTypeInternal(n2, n3, n4, s2, false, null);
    }

    @Override
    @Deprecated
    public void defineColumnTypeBytes(int n2, int n3, int n4) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.defineColumnTypeInternal(n2, n3, n4, false, null);
        }
    }

    @Override
    @Deprecated
    public void defineColumnTypeChars(int n2, int n3, int n4) throws SQLException {
        this.defineColumnTypeInternal(n2, n3, n4, false, null);
    }

    @Override
    public void defineColumnType(int n2, int n3, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.defineColumnTypeInternal(n2, n3, -1, true, string);
        }
    }

    void setCursorId(int n2) throws SQLException {
        this.cursorId = n2;
    }

    void setPrefetchInternal(int n2, boolean bl, boolean bl2) throws SQLException {
        if (bl) {
            if (n2 <= 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 20).fillInStackTrace();
            }
        } else {
            if (n2 < 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "setFetchSize").fillInStackTrace();
            }
            if (n2 == 0) {
                n2 = this.connection.getDefaultRowPrefetch();
            }
        }
        if (bl2) {
            if (n2 != this.defaultRowPrefetch) {
                this.defaultRowPrefetch = n2;
                if (this.currentResultSet == null || this.currentResultSet.closed) {
                    this.rowPrefetchChanged = true;
                }
            }
        } else if (n2 != this.rowPrefetch && (this.streamList == null || this.isFetchStreams)) {
            this.rowPrefetch = n2;
            this.rowPrefetchChanged = true;
        }
    }

    @Override
    public void setRowPrefetch(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setPrefetchInternal(n2, true, true);
        }
    }

    @Override
    public void setLobPrefetchSize(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (n2 < -1) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 267).fillInStackTrace();
            }
            this.defaultLobPrefetchSize = n2;
        }
    }

    @Override
    public int getLobPrefetchSize() throws SQLException {
        return this.defaultLobPrefetchSize;
    }

    int getPrefetchInternal(boolean bl) {
        int n2 = bl ? this.defaultRowPrefetch : this.rowPrefetch;
        return n2;
    }

    @Override
    public int getRowPrefetch() {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n2 = this.getPrefetchInternal(true);
            return n2;
        }
    }

    @Override
    public void setFixedString(boolean bl) {
        this.fixedString = bl;
    }

    @Override
    public boolean getFixedString() {
        return this.fixedString;
    }

    void check_row_prefetch_changed() throws SQLException {
        if (this.rowPrefetchChanged) {
            if (this.streamList == null) {
                this.prepareAccessors();
                this.needToPrepareDefineBuffer = true;
            }
            this.rowPrefetchChanged = false;
        }
    }

    void setDefinesInitialized(boolean bl) {
    }

    void printState(String string) throws SQLException {
    }

    void checkValidRowsStatus() throws SQLException {
        if (this.validRows == -2L) {
            this.validRows = 1L;
            this.connection.holdLine(this);
            OracleInputStream oracleInputStream = this.streamList;
            while (oracleInputStream != null) {
                if (oracleInputStream.hasBeenOpen) {
                    oracleInputStream = oracleInputStream.accessor.initForNewRow();
                }
                oracleInputStream.closed = false;
                oracleInputStream.hasBeenOpen = true;
                oracleInputStream = oracleInputStream.nextStream;
            }
            this.nextStream = this.streamList;
        } else if (this.sqlKind.isSELECT()) {
            if (this.validRows < (long)this.rowPrefetch) {
                this.gotLastBatch = true;
                this.initializeCacheEntryIfApplicable();
            }
        } else if (!this.sqlKind.isPlsqlOrCall()) {
            this.rowsProcessed = this.validRows;
        }
    }

    void initializeCacheEntryIfApplicable() throws SQLException {
        int n2 = this.storedRowCount + (int)this.validRows;
        if (this.cachedQueryResult != null) {
            long l2 = this.rowData.length();
            for (Accessor object : this.accessors) {
                if (object == null) continue;
                l2 += (long)(24 + n2 * 14);
            }
            ResultSetCache resultSetCache = this.connection.getResultSetCacheInternal();
            long l3 = resultSetCache.getMaxCacheSize() - resultSetCache.getCurrentCacheSize();
            if (l2 <= l3) {
                ByteArray byteArray;
                this.rowData = byteArray = this.rowData.compact();
                this.rowData = ReadOnlyByteArray.newReadOnlyByteArray(this.rowData);
                this.cachedQueryResult.initialize(n2, this.rowData, this.accessors, l2);
                resultSetCache.updateCurrentCacheSize(l2);
                this.cachedQueryResult = null;
            }
        }
    }

    void cleanupDefines() {
        this.accessors = null;
        if (this.columnNameCache.size() > 0) {
            this.columnNameCache = new IdentityHashMap(COLUMN_NAME_CACHE_INITIAL_SIZE);
        }
        if (this.rowData != null) {
            this.rowData.free();
        }
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            int n2 = this.maxFieldSize;
            return n2;
        }
    }

    @Override
    public void setMaxFieldSize(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (n2 < 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.maxFieldSize = n2;
        }
    }

    @Override
    public int getMaxRows() throws SQLException {
        return (int)this.getLargeMaxRows();
    }

    @Override
    public long getLargeMaxRows() throws SQLException {
        this.ensureOpen();
        return this.maxRows;
    }

    @Override
    public void setMaxRows(int n2) throws SQLException {
        this.setLargeMaxRows(n2);
    }

    @Override
    public void setLargeMaxRows(long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (l2 < 0L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.maxRows = l2;
        }
    }

    @Override
    public void setEscapeProcessing(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.processEscapes = bl;
        }
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            int n2 = this.queryTimeout;
            return n2;
        }
    }

    @Override
    public void setQueryTimeout(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (n2 < 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.queryTimeout = n2;
        }
    }

    @Override
    public void cancel() throws SQLException {
        this.ensureOpen();
        this.doCancel();
    }

    boolean doCancel() throws SQLException {
        boolean bl = false;
        if (this.closed) {
            return bl;
        }
        if (this.connection.statementHoldingLine != null) {
            this.freeLine();
        } else if (this.cancelLock.enterCanceling()) {
            try {
                bl = true;
                this.connection.cancelOperationOnServer(true);
            }
            finally {
                this.cancelLock.exitCanceling();
            }
        } else {
            return bl;
        }
        OracleStatement oracleStatement = this.children;
        while (oracleStatement != null) {
            bl = bl || oracleStatement.doCancel();
            oracleStatement = oracleStatement.nextChild;
        }
        this.connection.releaseLineForCancel();
        return bl;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        this.ensureOpen();
        return this.sqlWarning;
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.ensureOpen();
        this.sqlWarning = null;
    }

    void foundPlsqlCompilerWarning() throws SQLException {
        SQLWarning sQLWarning = DatabaseError.addSqlWarning(this.sqlWarning, "Found Plsql compiler warnings.", 24439);
        if (this.sqlWarning != null) {
            this.sqlWarning.setNextWarning(sQLWarning);
        } else {
            this.sqlWarning = sQLWarning;
        }
    }

    @Override
    public void setCursorName(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setCursorName").fillInStackTrace();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.implicitResultSetStatements != null) {
                if (this.currentResultSet != null) {
                    OracleResultSet oracleResultSet = this.currentResultSet;
                    return oracleResultSet;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 283).fillInStackTrace();
            }
            if (this.sqlKind.isSELECT()) {
                if (this.currentResultSet == null) {
                    this.currentResultSet = this.createResultSet();
                }
                OracleResultSet oracleResultSet = this.currentResultSet;
                return oracleResultSet;
            }
            ResultSet resultSet = null;
            return resultSet;
        }
    }

    @Override
    public int getUpdateCount() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n2 = (int)this.getLargeUpdateCount();
            return n2;
        }
    }

    @Override
    public long getLargeUpdateCount() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            long l2 = 0L;
            switch (this.sqlKind) {
                case UNINITIALIZED: 
                case SELECT_FOR_UPDATE: 
                case SELECT: {
                    l2 = -1L;
                    break;
                }
                case ALTER_SESSION: 
                case OTHER: {
                    l2 = !this.noMoreUpdateCounts ? this.rowsProcessed : -1L;
                    this.noMoreUpdateCounts = true;
                    break;
                }
                case PLSQL_BLOCK: 
                case CALL_BLOCK: {
                    l2 = -1L;
                    this.noMoreUpdateCounts = true;
                    break;
                }
                case DELETE: 
                case INSERT: 
                case MERGE: 
                case UPDATE: {
                    l2 = !this.noMoreUpdateCounts ? this.rowsProcessed : -1L;
                    this.noMoreUpdateCounts = true;
                }
            }
            long l3 = l2;
            return l3;
        }
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        this.ensureOpen();
        return this.getMoreResults(1);
    }

    @Override
    public int sendBatch() throws SQLException {
        return 0;
    }

    protected void increaseCapacity(int n2) {
        if (this.storedRowCount + n2 > this.currentCapacity) {
            int n3;
            if (this.currentCapacity < 1024) {
                n3 = this.currentCapacity * 4;
            }
            n3 = this.currentCapacity < 16384 ? (int)((double)this.currentCapacity * 1.5) : (int)((double)this.currentCapacity * 1.2);
            n3 = Math.max(this.storedRowCount + n2, n3);
            n3 = (n3 / this.rowPrefetch + 1) * this.rowPrefetch;
            for (Accessor accessor : this.accessors) {
                if (accessor == null) continue;
                accessor.setCapacity(n3);
            }
            this.currentCapacity = n3;
        }
        assert (this.currentCapacity >= this.storedRowCount + n2) : "currentCapacity: " + this.currentCapacity + " storedRowCount: " + this.storedRowCount + ", numberOfRows: " + n2;
    }

    protected void drainStreams() throws SQLException {
        if (this.streamList != null) {
            while (this.nextStream != null) {
                try {
                    this.nextStream.close();
                }
                catch (IOException iOException) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
                }
                this.nextStream = this.nextStream.nextStream;
            }
        }
    }

    @Override
    final int physicalRowIndex(long l2) {
        return (int)(l2 - this.indexOfFirstRow);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    long fetchMoreRows(long l2) throws SQLException {
        assert (!this.isAllFetched) : "isAllFetched: " + this.isAllFetched;
        this.cleanTempLobsBeforeFetchMoreRows();
        long l3 = 0L;
        if (this.dmsFetch != null) {
            l3 = this.dmsFetch.start();
        }
        try {
            this.connection.updateSystemContext();
            assert (this.fetchMode != FetchMode.APPEND || l2 <= Integer.MAX_VALUE) : "firstRow:" + l2;
            this.prepareAccessorsBeforeFetchMoreRows();
            this.drainStreams();
            this.connection.needLine();
            try {
                this.cancelLock.enterExecuting();
                boolean bl = this.fetchMode == FetchMode.APPEND;
                int n2 = bl ? (int)l2 : 0;
                this.fetch(n2, bl);
                assert (this.validRows != -2L || this.rowPrefetch == 1) : "validRows: " + this.validRows + " rowPrefetch: " + this.rowPrefetch;
            }
            finally {
                this.cancelLock.exitExecuting();
            }
            this.checkValidRowsStatus();
            this.updateRowStorageCountAfterFetchMoreRows();
            this.updateIsAllFetchedAfterFetchMoreRows();
            assert (this.physicalRowIndex(l2) >= 0) : "firstRow: " + l2 + " indexOfFirstRow: " + this.indexOfFirstRow;
            assert (this.physicalRowIndex(l2) < this.currentCapacity) : "firstRow: " + l2 + " indexOfFirstRow: " + this.indexOfFirstRow + " currentCapacity: " + this.currentCapacity;
            assert (this.validRows >= 0L) : "validRows: " + this.validRows;
            assert (this.validRows > 0L || this.isAllFetched) : "validRows: " + this.validRows + ", isAllFetched: " + this.isAllFetched;
            long l4 = this.validRows;
            return l4;
        }
        finally {
            if (this.dmsFetch != null) {
                this.dmsFetch.stop(l3);
            }
        }
    }

    private final void cleanTempLobsBeforeFetchMoreRows() throws SQLException {
        if (this.currentResultSet != null && this.currentResultSet.getType() == 1003) {
            if (!this.tempRowClobsToFree.isEmpty()) {
                this.cleanTempClobs(this.tempRowClobsToFree);
                this.tempRowClobsToFree.clear();
            }
            if (!this.tempRowBlobsToFree.isEmpty()) {
                this.cleanTempBlobs(this.tempRowBlobsToFree);
                this.tempRowBlobsToFree.clear();
            }
        }
    }

    private final void prepareAccessorsBeforeFetchMoreRows() throws SQLException {
        if (this.fetchMode == FetchMode.APPEND) {
            this.increaseCapacity(this.rowPrefetch);
        }
        this.check_row_prefetch_changed();
    }

    private final void updateRowStorageCountAfterFetchMoreRows() {
        if (this.fetchMode == FetchMode.APPEND) {
            this.storedRowCount += (int)this.validRows;
        } else {
            this.indexOfFirstRow += (long)this.storedRowCount;
            this.storedRowCount = (int)this.validRows;
        }
    }

    private final void updateIsAllFetchedAfterFetchMoreRows() {
        if (this.maxRows > 0L && this.indexOfFirstRow + (long)this.storedRowCount >= this.maxRows) {
            this.isAllFetched = true;
        }
    }

    int storedRowCount() {
        return this.storedRowCount;
    }

    int refreshRows(long l2, int n2) throws SQLException {
        ARRAY aRRAY = this.connection.createARRAY(SYS_ODCIVARCHAR2LIST, this.getRowKeys(this.physicalRowIndex(l2), n2));
        return this.refreshRowsInternal(aRRAY, this.physicalRowIndex(l2), n2);
    }

    void insertRow(long l2, RowId rowId) throws SQLException {
        if (this.currentCapacity < this.storedRowCount + 1) {
            this.increaseCapacity(this.storedRowCount + 1);
        }
        RowId[] rowIdArray = new RowId[]{rowId};
        ARRAY aRRAY = this.connection.createARRAY(SYS_ODCIVARCHAR2LIST, rowIdArray);
        for (Accessor accessor : this.accessors) {
            if (accessor == null) continue;
            accessor.insertNull(this.physicalRowIndex(l2));
        }
        int n2 = this.refreshRowsInternal(aRRAY, this.physicalRowIndex(l2), 1);
        assert (n2 == 1) : "count: " + n2;
        this.storedRowCount += n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected int refreshRowsInternal(Array array, int n2, int n3) throws SQLException {
        if (this.refreshStatement == null) {
            this.refreshStatement = this.connection.prepareStatementInternal(this.sqlObject.getRefetchSql(), 1003, 1007);
            this.refreshStatement.isRowidPrepended = true;
            this.refreshStatement.isFetchStreams = true;
            this.refreshStatement.copyDefines(this, n3);
            this.copyBinds(this.refreshStatement, 1);
        }
        this.refreshStatement.setArray(1, array);
        this.refreshStatement.setFetchSize(n3);
        ResultSet resultSet = null;
        try {
            resultSet = this.refreshStatement.executeQuery();
            int n4 = 0;
            int n5 = 0;
            while (resultSet.next()) {
                if (this.accessors[0].getROWID(n2 + n5) == null || this.accessors[0].getROWID(n2 + n5).equals(this.refreshStatement.accessors[0].getROWID(n5))) {
                    int n6 = this.accessors[0].getROWID(n2 + n5) == null ? 0 : 1;
                    ++n4;
                    for (int i2 = n6; i2 < this.accessors.length; ++i2) {
                        if (this.accessors[i2] == null) continue;
                        this.accessors[i2].copyFrom(this.refreshStatement.accessors[i2], n5, n2 + n5);
                    }
                }
                ++n5;
            }
            n5 = n4;
            return n5;
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (Exception exception) {}
            }
            if (this.refreshStatement != null) {
                try {
                    this.refreshStatement.drainStreams();
                    this.refreshStatement.close();
                }
                catch (Exception exception) {
                }
                finally {
                    this.refreshStatement = null;
                }
            }
        }
    }

    protected String[] getRowKeys(int n2, int n3) throws SQLException {
        ArrayList<String> arrayList = new ArrayList<String>(n3);
        int n4 = Math.min(n3, this.storedRowCount - n2);
        for (int i2 = 0; i2 < n4; ++i2) {
            arrayList.add(this.accessors[0].getString(n2 + i2));
        }
        return arrayList.toArray(new String[0]);
    }

    void removeRowFromCache(long l2) throws SQLException {
        if (l2 < this.indexOfFirstRow) {
            --this.indexOfFirstRow;
        } else if (l2 >= this.indexOfFirstRow) {
            assert (l2 < this.indexOfFirstRow + (long)this.storedRowCount) : "row: " + l2 + " indexOfFirstRow: " + this.indexOfFirstRow + " storedRowCount: " + this.storedRowCount;
            for (Accessor accessor : this.accessors) {
                if (accessor == null) continue;
                this.deleteRow(accessor, this.physicalRowIndex(l2));
            }
            --this.storedRowCount;
        }
    }

    void deleteRow(Accessor accessor, int n2) throws SQLException {
        accessor.deleteRow(n2);
    }

    void prepareForNewResults(boolean bl, boolean bl2, boolean bl3) throws SQLException {
        if (!this.closed) {
            this.clearWarnings();
        }
        if (bl3 && this.implicitResultSetStatements != null) {
            for (OracleStatement wrapper : this.implicitResultSetStatements) {
                wrapper.close();
            }
            this.implicitResultSetStatements = null;
            this.implicitResultSetIterator = null;
            if (this.openImplicitResultSets != null) {
                for (OracleResultSet oracleResultSet : this.openImplicitResultSets) {
                    oracleResultSet.close();
                }
                this.openImplicitResultSets = null;
            }
        }
        this.closeAllStreams(bl2);
        if (this.currentResultSet != null) {
            this.currentResultSet.close();
            this.currentResultSet = null;
        }
        if (this.bindUseDBA) {
            this.resetBindData();
        } else {
            this.rowData.reset();
        }
        this.storedRowCount = 0;
        this.indexOfFirstRow = 0L;
        this.isAllFetched = false;
        this.checkSum = 0L;
        this.checkSumComputationFailure = false;
        this.validRows = 0L;
        this.batchRowsUpdatedArray = null;
        this.gotLastBatch = false;
        if (bl) {
            this.offsetOfFirstUserColumn = -1;
            this.numberOfUserColumns = -1;
        }
        if (this.needToParse && !this.columnsDefinedByUser) {
            if (bl2 && this.numberOfDefinePositions != 0) {
                this.numberOfDefinePositions = 0;
            }
            this.needToPrepareDefineBuffer = true;
        }
        if (bl && this.rowPrefetch != this.defaultRowPrefetch && this.streamList == null) {
            this.rowPrefetch = this.defaultRowPrefetch;
            this.rowPrefetchChanged = true;
        }
    }

    void resetBindData() {
        this.rowData.setPosition(this.beyondBindData);
    }

    void closeAllStreams(boolean bl) throws SQLException {
        this.drainStreams();
        if (bl) {
            OracleInputStream oracleInputStream = this.streamList;
            OracleInputStream oracleInputStream2 = null;
            this.streamList = null;
            while (oracleInputStream != null) {
                if (!oracleInputStream.hasBeenOpen) {
                    if (oracleInputStream2 == null) {
                        this.streamList = oracleInputStream;
                    } else {
                        oracleInputStream2.nextStream = oracleInputStream;
                    }
                    oracleInputStream2 = oracleInputStream;
                }
                oracleInputStream = oracleInputStream.nextStream;
            }
        }
    }

    void reopenStreams() throws SQLException {
        OracleInputStream oracleInputStream = this.streamList;
        while (oracleInputStream != null) {
            if (oracleInputStream.hasBeenOpen) {
                oracleInputStream = oracleInputStream.accessor.initForNewRow();
            }
            oracleInputStream.closed = false;
            oracleInputStream.hasBeenOpen = true;
            oracleInputStream = oracleInputStream.nextStream;
        }
        this.nextStream = this.streamList;
    }

    void endOfResultSet(boolean bl) throws SQLException {
        if (!bl) {
            this.prepareForNewResults(false, false, false);
        }
        this.clearDefines();
        this.rowPrefetchInLastFetch = -1;
    }

    boolean wasNullValue(long l2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9, "wasNull").fillInStackTrace();
        }
        if (this.lastIndex < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 24).fillInStackTrace();
        }
        return this.isNull(l2, this.lastIndex);
    }

    boolean isNull(long l2, int n2) throws SQLException {
        if (this.sqlKind.isSELECT() || this.isDmlReturning) {
            return this.accessors[n2 + this.offsetOfFirstUserColumn].isNull(this.physicalRowIndex(l2));
        }
        assert (l2 == (long)this.currentRank) : "rowIndex: " + l2 + " currentRank: " + this.currentRank;
        return this.outBindAccessors[n2 - 1].isNull(this.currentRank);
    }

    int getColumnIndex(String string) throws SQLException {
        this.ensureOpen();
        Integer n2 = this.columnNameCache.get(string);
        if (n2 == null) {
            String string2 = string;
            if (string2 != null) {
                string2 = this.enquoteIdentifier(string2, true);
                string2 = string2.substring(1, string2.length() - 1);
            }
            n2 = this.getColumnIndexPrimitive(string2);
            if (this.columnNameCache.size() <= this.accessors.length) {
                this.columnNameCache.put(string, n2);
            }
        }
        return n2;
    }

    private int getColumnIndexPrimitive(String string) throws SQLException {
        if (!this.describedWithNames) {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                this.doDescribe(true);
                this.described = true;
                this.describedWithNames = true;
            }
        }
        for (int i2 = 1 + this.offsetOfFirstUserColumn; i2 < this.numberOfDefinePositions; ++i2) {
            if (!this.accessors[i2].columnName.equalsIgnoreCase(string)) continue;
            return i2 - this.offsetOfFirstUserColumn;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
    }

    int getJDBCType(int n2) throws SQLException {
        int n3 = 0;
        switch (n2) {
            case 6: {
                n3 = 2;
                break;
            }
            case 100: {
                n3 = 100;
                break;
            }
            case 101: {
                n3 = 101;
                break;
            }
            case 999: {
                n3 = 999;
                break;
            }
            case 96: {
                n3 = 1;
                break;
            }
            case 1: {
                n3 = 12;
                break;
            }
            case 8: {
                n3 = -1;
                break;
            }
            case 12: {
                n3 = 91;
                break;
            }
            case 180: {
                n3 = 93;
                break;
            }
            case 181: {
                n3 = -101;
                break;
            }
            case 231: {
                n3 = -102;
                break;
            }
            case 182: {
                n3 = -103;
                break;
            }
            case 183: {
                n3 = -104;
                break;
            }
            case 23: {
                n3 = -2;
                break;
            }
            case 24: {
                n3 = -4;
                break;
            }
            case 104: {
                n3 = -8;
                break;
            }
            case 113: {
                n3 = 2004;
                break;
            }
            case 119: {
                n3 = 2016;
                break;
            }
            case 112: {
                n3 = 2005;
                break;
            }
            case 114: {
                n3 = -13;
                break;
            }
            case 102: {
                n3 = -10;
                break;
            }
            case 109: {
                n3 = 2002;
                break;
            }
            case 111: {
                n3 = 2006;
                break;
            }
            case 998: {
                n3 = -14;
                break;
            }
            case 995: {
                n3 = 0;
                break;
            }
            default: {
                n3 = n2;
            }
        }
        return n3;
    }

    int getInternalType(int n2) throws SQLException {
        int n3 = 0;
        switch (n2) {
            case -7: 
            case -6: 
            case -5: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: {
                n3 = 6;
                break;
            }
            case 100: {
                n3 = 100;
                break;
            }
            case 101: {
                n3 = 101;
                break;
            }
            case 252: {
                n3 = 252;
                break;
            }
            case 999: {
                n3 = 999;
                break;
            }
            case 1: {
                n3 = 96;
                break;
            }
            case -15: 
            case -9: 
            case 12: {
                n3 = 1;
                break;
            }
            case -16: 
            case -1: {
                n3 = 8;
                break;
            }
            case 91: 
            case 92: {
                n3 = 12;
                break;
            }
            case -100: 
            case 93: {
                n3 = 180;
                break;
            }
            case -101: 
            case 2013: 
            case 2014: {
                n3 = 181;
                break;
            }
            case -102: {
                n3 = 231;
                break;
            }
            case -103: {
                n3 = 182;
                break;
            }
            case -104: {
                n3 = 183;
                break;
            }
            case -3: 
            case -2: {
                n3 = 23;
                break;
            }
            case -4: {
                n3 = 24;
                break;
            }
            case -8: {
                n3 = 104;
                break;
            }
            case 2004: {
                n3 = 113;
                break;
            }
            case 2016: {
                n3 = 119;
                break;
            }
            case 2005: 
            case 2011: {
                n3 = 112;
                break;
            }
            case -13: {
                n3 = 114;
                break;
            }
            case -10: 
            case 2012: {
                n3 = 102;
                break;
            }
            case 2002: 
            case 2003: 
            case 2007: 
            case 2008: 
            case 2009: {
                n3 = 109;
                break;
            }
            case 2006: {
                n3 = 111;
                break;
            }
            case -14: {
                n3 = 998;
                break;
            }
            case 70: {
                n3 = 1;
                break;
            }
            case 0: {
                n3 = 995;
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, Integer.toString(n2)).fillInStackTrace();
            }
        }
        return n3;
    }

    ResultSetMetaData getResultSetMetaData() throws SQLException {
        return new OracleResultSetMetaData(this.connection, this, this.offsetOfFirstUserColumn);
    }

    void describe() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (!this.described) {
                this.doDescribe(false);
            }
        }
    }

    void freeLine() throws SQLException {
        if (this.streamList != null) {
            while (this.nextStream != null) {
                try {
                    this.nextStream.close();
                }
                catch (IOException iOException) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
                }
                this.nextStream = this.nextStream.nextStream;
            }
        }
    }

    @Override
    void closeUsedStreams(int n2) throws SQLException {
        while (this.nextStream != null && this.nextStream.columnIndex < n2) {
            try {
                this.nextStream.close();
            }
            catch (IOException iOException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
            }
            this.nextStream = this.nextStream.nextStream;
        }
    }

    final void ensureOpen() throws SQLException {
        if (this.connection.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
    }

    void allocateTmpByteArray() {
    }

    @Override
    public void setFetchDirection(int n2) throws SQLException {
        block14: {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                this.ensureOpen();
                if (n2 == 1000) {
                    break block14;
                }
                if (n2 == 1001 || n2 == 1002) {
                    this.sqlWarning = DatabaseError.addSqlWarning(this.sqlWarning, 87);
                    break block14;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "setFetchDirection").fillInStackTrace();
            }
        }
    }

    @Override
    public int getFetchDirection() throws SQLException {
        this.ensureOpen();
        return 1000;
    }

    @Override
    public void setFetchSize(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.setPrefetchInternal(n2, false, true);
        }
    }

    @Override
    public int getFetchSize() throws SQLException {
        this.ensureOpen();
        return this.getPrefetchInternal(true);
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        this.ensureOpen();
        return this.userRsetType.getConcur();
    }

    @Override
    public int getResultSetType() throws SQLException {
        this.ensureOpen();
        return this.userRsetType.getType();
    }

    @Override
    public Connection getConnection() throws SQLException {
        this.ensureOpen();
        return this.connection.getWrapper();
    }

    boolean isOracleBatchStyle() {
        return false;
    }

    void initBatch() {
    }

    int getBatchSize() {
        if (this.m_batchItems == null) {
            return 0;
        }
        return this.m_batchItems.size();
    }

    void addBatchItem(String string) {
        if (this.m_batchItems == null) {
            this.m_batchItems = new Vector();
        }
        this.m_batchItems.addElement(string);
    }

    String getBatchItem(int n2) {
        if (this.m_batchItems == null) {
            return null;
        }
        return this.m_batchItems.elementAt(n2);
    }

    void clearBatchItems() {
        if (this.m_batchItems != null) {
            this.m_batchItems.removeAllElements();
        }
    }

    void checkIfBatchExists() throws SQLException {
        if (this.getBatchSize() > 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 81, "batch must be either executed or cleared").fillInStackTrace();
        }
    }

    @Override
    public void addBatch(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.addBatchItem(string);
        }
    }

    @Override
    public void clearBatch() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.clearBatchCritical();
        }
    }

    void clearBatchCritical() throws SQLException {
        this.ensureOpen();
        this.clearBatchItems();
    }

    int[] toIntArray(long[] lArray) {
        int[] nArray = new int[lArray.length];
        for (int i2 = 0; i2 < lArray.length; ++i2) {
            nArray[i2] = (int)lArray[i2];
        }
        return nArray;
    }

    @Override
    public int[] executeBatch() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int[] nArray = this.toIntArray(this.executeLargeBatch());
            return nArray;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public long[] executeLargeBatch() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            this.cleanUpBeforeExecute();
            this.cleanOldTempLobs();
            int n2 = 0;
            int n3 = this.getBatchSize();
            this.checkSum = 0L;
            this.checkSumComputationFailure = false;
            if (n3 <= 0) {
                long[] lArray = new long[]{};
                return lArray;
            }
            long[] lArray = new long[n3];
            long l2 = 0L;
            if (this.dmsExecute != null) {
                l2 = this.dmsExecute.start();
            }
            try {
                this.connection.updateSystemContext();
                this.ensureOpen();
                this.prepareForNewResults(true, true, true);
                int n4 = this.numberOfDefinePositions;
                String string = this.sqlObject.getOriginalSql();
                OracleStatement.SqlKind sqlKind = this.sqlKind;
                this.noMoreUpdateCounts = false;
                int n5 = 0;
                try {
                    this.connection.needLine();
                    for (n2 = 0; n2 < n3; ++n2) {
                        this.sqlObject.initialize(this.getBatchItem(n2));
                        this.sqlKind = this.sqlObject.getSqlKind();
                        if (this.connection.dmsUpdateSqlText()) {
                            this.dmsSqlText.update(this.sqlObject.toString());
                        }
                        this.needToParse = true;
                        this.numberOfDefinePositions = 0;
                        this.rowsProcessed = 0L;
                        this.currentRank = 1;
                        if (this.sqlKind.isSELECT()) {
                            throw (SQLException)DatabaseError.createBatchUpdateException(80, (Object)("invalid SELECT batch command " + n2), n2, lArray).fillInStackTrace();
                        }
                        if (!this.isOpen) {
                            this.connection.open(this);
                            this.isOpen = true;
                        }
                        long l3 = -1L;
                        try {
                            this.cancelLock.enterExecuting();
                            if (this.queryTimeout != 0) {
                                this.connection.getTimeout().setTimeout((long)this.queryTimeout * 1000L, this);
                            }
                            this.executeForRows(false);
                            if (this.validRows > 0L) {
                                n5 = (int)((long)n5 + this.validRows);
                            }
                            l3 = this.validRows;
                        }
                        catch (SQLException sQLException) {
                            this.needToParse = true;
                            this.resetCurrentRowBinders();
                            throw sQLException;
                        }
                        finally {
                            this.cancelLock.exitExecuting();
                            if (this.queryTimeout != 0) {
                                this.connection.getTimeout().cancelTimeout();
                            }
                            this.validRows = n5;
                            this.checkValidRowsStatus();
                        }
                        lArray[n2] = l3;
                        if (lArray[n2] >= 0L) continue;
                        throw (SQLException)DatabaseError.createBatchUpdateException(81, (Object)("command return value " + lArray[n2]), n2, lArray).fillInStackTrace();
                    }
                }
                catch (SQLException sQLException) {
                    if (sQLException instanceof BatchUpdateException) {
                        throw sQLException;
                    }
                    throw (SQLException)DatabaseError.createBatchUpdateException(81, (Object)sQLException.getMessage(), n2, lArray).fillInStackTrace();
                }
                finally {
                    this.clearBatchItems();
                    this.numberOfDefinePositions = n4;
                    if (string != null) {
                        this.sqlObject.initialize(string);
                        this.sqlKind = sqlKind;
                    }
                    this.currentRank = 0;
                    this.batchWasExecuted = true;
                }
            }
            finally {
                if (this.dmsExecute != null) {
                    this.dmsExecute.stop(l2);
                }
            }
            long[] lArray2 = lArray;
            return lArray2;
        }
    }

    void copyDefines(OracleStatement oracleStatement, int n2) throws SQLException {
        if (oracleStatement.columnsDefinedByUser) {
            Accessor[] accessorArray = oracleStatement.accessors;
            this.accessors = new Accessor[accessorArray.length];
            for (int i2 = 0; i2 < accessorArray.length; ++i2) {
                if (accessorArray[i2] == null) continue;
                this.accessors[i2] = accessorArray[i2].copyForDefine(this);
                this.accessors[i2].setCapacity(n2);
            }
            this.numberOfDefinePositions = oracleStatement.numberOfDefinePositions;
            this.definedColumnType = oracleStatement.definedColumnType;
            this.definedColumnSize = oracleStatement.definedColumnSize;
            this.definedColumnFormOfUse = oracleStatement.definedColumnFormOfUse;
            this.columnsDefinedByUser = true;
        }
    }

    int copyBinds(Statement statement, int n2) throws SQLException {
        return 0;
    }

    public void notifyCloseRset() throws SQLException {
        this.endOfResultSet(false);
    }

    public String getOriginalSql() throws SQLException {
        return this.sqlObject.getOriginalSql();
    }

    boolean isRowidPrepended() {
        return this.isRowidPrepended;
    }

    void computeOffsetOfFirstUserColumn() {
        this.offsetOfFirstUserColumn = -1;
        if (this.sqlKind.isSELECT()) {
            if (this.isRowidPrepended) {
                ++this.offsetOfFirstUserColumn;
            }
        } else if (this.numReturnParams > 0) {
            this.offsetOfFirstUserColumn = this.numberOfBindPositions - this.numReturnParams - 1;
        }
    }

    void doScrollExecuteCommon() throws SQLException {
        if (!this.sqlKind.isSELECT()) {
            this.doExecuteWithTimeout();
            return;
        }
        boolean bl = (this.realRsetType == OracleResultSet.ResultSetType.UNKNOWN ? this.userRsetType : this.realRsetType).isIdentifierRequired();
        if (!bl) {
            this.doExecuteWithTimeout();
            this.currentResultSet = this.createResultSet();
            this.realRsetType = this.userRsetType;
            if (this.realRsetType.isScrollable()) {
                this.fetchMode = FetchMode.APPEND;
            }
        } else {
            try {
                this.sqlObject.setIncludeRowid(true);
                this.isRowidPrepended = true;
                this.needToParse = true;
                this.prepareForNewResults(true, false, true);
                if (this.columnsDefinedByUser) {
                    Accessor[] accessorArray = this.accessors;
                    if (this.accessors == null || this.accessors.length <= this.numberOfDefinePositions) {
                        this.accessors = new Accessor[this.numberOfDefinePositions + 1];
                    }
                    if (accessorArray != null) {
                        for (int i2 = this.numberOfDefinePositions; i2 > 0; --i2) {
                            Accessor accessor;
                            this.accessors[i2] = accessor = accessorArray[i2 - 1];
                            if (!accessor.isColumnNumberAware) continue;
                            accessor.updateColumnNumber(i2);
                        }
                    }
                    this.allocateRowidAccessor();
                    ++this.numberOfDefinePositions;
                }
                this.doExecuteWithTimeout();
                this.currentResultSet = this.createResultSet();
                this.realRsetType = this.userRsetType;
                if (this.realRsetType.isScrollable()) {
                    this.fetchMode = FetchMode.APPEND;
                }
            }
            catch (SQLException sQLException) {
                this.realRsetType = this.userRsetType.downgrade();
                this.fetchMode = this.cachedQueryResult != null ? FetchMode.APPEND : (this.realRsetType.isScrollable() ? FetchMode.APPEND : FetchMode.OVERWRITE);
                this.isRowidPrepended = this.realRsetType.isIdentifierRequired();
                this.sqlObject.setIncludeRowid(this.isRowidPrepended);
                this.needToParse = true;
                this.prepareForNewResults(true, false, true);
                if (this.columnsDefinedByUser) {
                    this.needToPrepareDefineBuffer = true;
                    --this.numberOfDefinePositions;
                    System.arraycopy(this.accessors, 1, this.accessors, 0, this.numberOfDefinePositions);
                    this.accessors[this.numberOfDefinePositions] = null;
                    for (int i3 = 0; i3 < this.numberOfDefinePositions; ++i3) {
                        Accessor accessor = this.accessors[i3];
                        if (!accessor.isColumnNumberAware) continue;
                        accessor.updateColumnNumber(i3);
                    }
                }
                this.moveAllTempLobsToFree();
                this.doExecuteWithTimeout();
                this.currentResultSet = this.createResultSet();
                this.sqlWarning = DatabaseError.addSqlWarning(this.sqlWarning, 91, sQLException.getMessage());
            }
        }
    }

    void allocateRowidAccessor() throws SQLException {
        this.accessors[0] = new RowidAccessor(this, 128, 1, -8, false);
    }

    OracleResultSet doScrollStmtExecuteQuery() throws SQLException {
        this.doScrollExecuteCommon();
        return this.currentResultSet;
    }

    void processDmlReturningBind() throws SQLException {
        this.returnParamsFetched = false;
        int n2 = 0;
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            if (this.accessors[i2] == null) continue;
            ++n2;
        }
        if (this.isAutoGeneratedKey) {
            this.numReturnParams = n2;
        } else {
            if (this.numReturnParams <= 0) {
                this.numReturnParams = this.sqlObject.getReturnParameterCount();
            }
            if (this.numReturnParams != n2) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 173).fillInStackTrace();
            }
        }
        this.returnParamMeta[0] = this.numReturnParams;
    }

    void allocateDmlReturnStorage() {
    }

    void fetchDmlReturnParams() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("fetchDmlReturnParams").fillInStackTrace();
    }

    void registerReturnParameterInternal(int n2, int n3, int n4, int n5, short s2, String string) throws SQLException {
        this.isDmlReturning = true;
        if (this.accessors == null) {
            this.accessors = new Accessor[this.numberOfBindPositions];
        }
        if (this.returnParamMeta == null) {
            this.returnParamMeta = new int[3 + this.numberOfBindPositions * 4];
        }
        switch (n4) {
            case -16: 
            case -15: 
            case -9: 
            case 2011: {
                s2 = (short)2;
                break;
            }
            case 2009: {
                string = "SYS.XMLTYPE";
                break;
            }
        }
        Accessor accessor = this.allocateAccessor(n3, n4, n2 + 1, n5, s2, string, true);
        accessor.isDMLReturnedParam = true;
        accessor.setCapacity(this.currentCapacity);
        this.accessors[n2] = accessor;
        boolean bl = accessor.charLength > 0;
        this.returnParamMeta[3 + n2 * 4 + 0] = accessor.defineType;
        this.returnParamMeta[3 + n2 * 4 + 1] = bl ? 1 : 0;
        this.returnParamMeta[3 + n2 * 4 + 2] = bl ? accessor.charLength : accessor.byteLength;
        this.returnParamMeta[3 + n2 * 4 + 3] = s2;
    }

    @Override
    @Deprecated
    public int creationState() {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n2 = this.creationState;
            return n2;
        }
    }

    public boolean isColumnSetNull(int n2) {
        return this.columnSetNull;
    }

    @Override
    public boolean isNCHAR(int n2) throws SQLException {
        int n3;
        if (!this.described) {
            this.describe();
        }
        if ((n3 = n2 - 1) < 0 || n3 >= this.numberOfDefinePositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        boolean bl = this.accessors[n3].formOfUse == 2;
        return bl;
    }

    void addChild(OracleStatement oracleStatement) {
        oracleStatement.nextChild = this.children;
        this.children = oracleStatement;
        oracleStatement.parent = this;
    }

    void addImplicitResultSetStmt(OracleStatement oracleStatement) {
        this.implicitResultSetStatements.add(oracleStatement);
    }

    void removeChild(OracleStatement oracleStatement) {
        if (oracleStatement == this.children) {
            this.children = oracleStatement.nextChild;
        } else {
            OracleStatement oracleStatement2 = this.children;
            while (oracleStatement2.nextChild != oracleStatement) {
                oracleStatement2 = oracleStatement2.nextChild;
            }
            oracleStatement2.nextChild = oracleStatement.nextChild;
        }
        oracleStatement.parent = null;
        oracleStatement.nextChild = null;
    }

    @Override
    public boolean getMoreResults(int n2) throws SQLException {
        Wrapper wrapper;
        this.ensureOpen();
        switch (n2) {
            case 1: {
                if (this.currentResultSet == null || this.currentResultSet.isClosed()) break;
                this.currentResultSet.close();
                break;
            }
            case 2: {
                if (this.currentResultSet == null || this.currentResultSet.isClosed()) break;
                if (this.openImplicitResultSets == null) {
                    this.openImplicitResultSets = new ArrayDeque(this.implicitResultSetStatements == null ? 1 : this.implicitResultSetStatements.size());
                }
                this.openImplicitResultSets.add(this.currentResultSet);
                this.currentResultSet = null;
                break;
            }
            case 3: {
                while (this.openImplicitResultSets != null && this.openImplicitResultSets.size() != 0) {
                    wrapper = this.openImplicitResultSets.remove();
                    ((OracleResultSet)wrapper).close();
                }
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getMoreResults").fillInStackTrace();
            }
        }
        if (this.implicitResultSetIterator != null && this.implicitResultSetIterator.hasNext() && (wrapper = this.implicitResultSetIterator.next()) != null) {
            this.currentResultSet = ((OracleStatement)wrapper).createResultSet();
            return true;
        }
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            if (!this.isAutoGeneratedKey) {
                ArrayDataResultSet arrayDataResultSet;
                ArrayDataResultSet arrayDataResultSet2 = arrayDataResultSet = new ArrayDataResultSet(this.connection, null, null);
                return arrayDataResultSet2;
            }
            if (this.accessors == null || this.numReturnParams == 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 144).fillInStackTrace();
            }
            if (this.currentResultSet == null) {
                this.isAllFetched = true;
                this.currentResultSet = new OracleReturnResultSet(this.connection, this);
                this.computeOffsetOfFirstUserColumn();
                this.computeNumberOfUserColumns();
            }
            OracleResultSet oracleResultSet = this.currentResultSet;
            return oracleResultSet;
        }
    }

    @Override
    public int executeUpdate(String string, int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n3 = (int)this.executeLargeUpdate(string, n2);
            return n3;
        }
    }

    @Override
    public long executeLargeUpdate(String string, int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.cleanUpBeforeExecute();
            this.autoKeyInfo = new AutoKeyInfo(string);
            if (n2 == 2 || !this.autoKeyInfo.isInsertOrUpdateSqlStmt()) {
                this.autoKeyInfo = null;
                long l2 = this.executeLargeUpdate(string);
                return l2;
            }
            if (n2 != 1) {
                this.autoKeyInfo = null;
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.isAutoGeneratedKey = true;
            this.autoKeyInfo.initialize(this.connection);
            String string2 = this.autoKeyInfo.getNewSql();
            this.numberOfBindPositions = 1;
            this.autoKeyRegisterReturnParams();
            this.processDmlReturningBind();
            long l3 = this.executeUpdateInternal(string2);
            return l3;
        }
    }

    @Override
    public int executeUpdate(String string, int[] nArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n2 = (int)this.executeLargeUpdate(string, nArray);
            return n2;
        }
    }

    @Override
    public long executeLargeUpdate(String string, int[] nArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.cleanUpBeforeExecute();
            if (nArray == null || nArray.length == 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.autoKeyInfo = new AutoKeyInfo(string, nArray);
            if (!this.autoKeyInfo.isInsertOrUpdateSqlStmt()) {
                this.autoKeyInfo = null;
                long l2 = this.executeLargeUpdate(string);
                return l2;
            }
            this.isAutoGeneratedKey = true;
            this.autoKeyInfo.initialize(this.connection);
            String string2 = this.autoKeyInfo.getNewSql();
            this.numberOfBindPositions = nArray.length;
            this.autoKeyRegisterReturnParams();
            this.processDmlReturningBind();
            long l3 = this.executeUpdateInternal(string2);
            return l3;
        }
    }

    @Override
    public int executeUpdate(String string, String[] stringArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n2 = (int)this.executeLargeUpdate(string, stringArray);
            return n2;
        }
    }

    @Override
    public long executeLargeUpdate(String string, String[] stringArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.cleanUpBeforeExecute();
            if (stringArray == null || stringArray.length == 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.autoKeyInfo = new AutoKeyInfo(string, stringArray);
            if (!this.autoKeyInfo.isInsertOrUpdateSqlStmt()) {
                this.autoKeyInfo = null;
                long l2 = this.executeLargeUpdate(string);
                return l2;
            }
            this.isAutoGeneratedKey = true;
            this.autoKeyInfo.initialize(this.connection);
            String string2 = this.autoKeyInfo.getNewSql();
            this.numberOfBindPositions = stringArray.length;
            this.autoKeyRegisterReturnParams();
            this.processDmlReturningBind();
            long l3 = this.executeUpdateInternal(string2);
            return l3;
        }
    }

    @Override
    public boolean execute(String string, int n2) throws SQLException {
        this.autoKeyInfo = new AutoKeyInfo(string);
        if (n2 == 2 || !this.autoKeyInfo.isInsertOrUpdateSqlStmt()) {
            this.autoKeyInfo = null;
            return this.execute(string);
        }
        if (n2 != 1) {
            this.autoKeyInfo = null;
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.cleanUpBeforeExecute();
            this.isAutoGeneratedKey = true;
            this.autoKeyInfo.initialize(this.connection);
            String string2 = this.autoKeyInfo.getNewSql();
            this.numberOfBindPositions = 1;
            this.autoKeyRegisterReturnParams();
            this.processDmlReturningBind();
            boolean bl = this.executeInternal(string2);
            return bl;
        }
    }

    @Override
    public boolean execute(String string, int[] nArray) throws SQLException {
        if (nArray == null || nArray.length == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        this.autoKeyInfo = new AutoKeyInfo(string, nArray);
        if (!this.autoKeyInfo.isInsertOrUpdateSqlStmt()) {
            this.autoKeyInfo = null;
            return this.execute(string);
        }
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.cleanUpBeforeExecute();
            this.isAutoGeneratedKey = true;
            this.autoKeyInfo.initialize(this.connection);
            String string2 = this.autoKeyInfo.getNewSql();
            this.numberOfBindPositions = nArray.length;
            this.autoKeyRegisterReturnParams();
            this.processDmlReturningBind();
            boolean bl = this.executeInternal(string2);
            return bl;
        }
    }

    @Override
    public boolean execute(String string, String[] stringArray) throws SQLException {
        if (stringArray == null || stringArray.length == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        this.autoKeyInfo = new AutoKeyInfo(string, stringArray);
        if (!this.autoKeyInfo.isInsertOrUpdateSqlStmt()) {
            this.autoKeyInfo = null;
            return this.execute(string);
        }
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.cleanUpBeforeExecute();
            this.isAutoGeneratedKey = true;
            this.autoKeyInfo.initialize(this.connection);
            String string2 = this.autoKeyInfo.getNewSql();
            this.numberOfBindPositions = stringArray.length;
            this.autoKeyRegisterReturnParams();
            this.processDmlReturningBind();
            boolean bl = this.executeInternal(string2);
            return bl;
        }
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        this.ensureOpen();
        return 1;
    }

    @Override
    public int getcacheState() {
        return this.cacheState;
    }

    @Override
    public int getstatementType() {
        return this.statementType;
    }

    @Override
    public boolean getserverCursor() {
        return this.serverCursor;
    }

    void initializeIndicatorSubRange() {
        this.bindIndicatorSubRange = 0;
    }

    private void autoKeyRegisterReturnParams() throws SQLException {
        if (this.currentResultSet != null) {
            this.currentResultSet.close();
        }
        this.initializeIndicatorSubRange();
        int n2 = this.bindIndicatorSubRange + 5 + this.numberOfBindPositions * 10;
        int n3 = n2 + 2 * this.numberOfBindPositions;
        this.bindIndicators = new short[n3];
        int n4 = this.bindIndicatorSubRange;
        this.bindIndicators[n4 + 0] = (short)this.numberOfBindPositions;
        this.bindIndicators[n4 + 1] = 0;
        this.bindIndicators[n4 + 2] = 1;
        this.bindIndicators[n4 + 3] = 0;
        this.bindIndicators[n4 + 4] = 1;
        n4 += 5;
        short[] sArray = this.autoKeyInfo.tableFormOfUses;
        int[] nArray = this.autoKeyInfo.columnIndexes;
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            short s2;
            this.bindIndicators[n4 + 0] = 994;
            short s3 = s2 = this.connection.defaultnchar ? (short)2 : 1;
            if (sArray != null && nArray != null && sArray[nArray[i2] - 1] == 2) {
                this.bindIndicators[n4 + 9] = s2 = 2;
            }
            n4 += 10;
            this.checkTypeForAutoKey(this.autoKeyInfo.returnTypes[i2]);
            String string = null;
            if (this.autoKeyInfo.returnTypes[i2] == 111) {
                string = this.autoKeyInfo.tableTypeNames[nArray[i2] - 1];
            }
            this.registerReturnParameterInternal(i2, this.autoKeyInfo.returnTypes[i2], this.autoKeyInfo.returnTypes[i2], -1, s2, string);
        }
    }

    private final void cleanUpBeforeExecute() throws SQLException {
        if (this.currentResultSet != null && this.isCloseOnCompletion) {
            this.currentResultSet.close();
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.isAutoGeneratedKey = false;
        this.numberOfBindPositions = 0;
        this.bindIndicators = null;
        this.returnParamMeta = null;
        if (this.executeDoneForDefines) {
            this.clearDefines();
        } else {
            this.executeDoneForDefines = true;
        }
        this.batchWasExecuted = false;
    }

    final void checkTypeForAutoKey(int n2) throws SQLException {
        if (n2 == 109) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 5).fillInStackTrace();
        }
    }

    void moveAllTempLobsToFree() {
        if (this.oldTempClobsToFree != null) {
            if (this.tempStmtClobsToFree == null) {
                this.tempStmtClobsToFree = this.oldTempClobsToFree;
            } else {
                this.tempStmtClobsToFree.addAll(this.oldTempClobsToFree);
            }
            this.oldTempClobsToFree = null;
        }
        if (this.oldTempBlobsToFree != null) {
            if (this.tempStmtBlobsToFree == null) {
                this.tempStmtBlobsToFree = this.oldTempBlobsToFree;
            } else {
                this.tempStmtBlobsToFree.addAll(this.oldTempBlobsToFree);
            }
            this.oldTempBlobsToFree = null;
        }
    }

    void moveTempLobsToFree(CLOB cLOB) {
        int n2;
        if (this.oldTempClobsToFree != null && (n2 = this.oldTempClobsToFree.indexOf(cLOB)) != -1) {
            this.addToTempLobsToFree(cLOB);
            this.oldTempClobsToFree.remove(n2);
        }
    }

    void moveTempLobsToFree(BLOB bLOB) {
        int n2;
        if (this.oldTempBlobsToFree != null && (n2 = this.oldTempBlobsToFree.indexOf(bLOB)) != -1) {
            this.addToTempLobsToFree(bLOB);
            this.oldTempBlobsToFree.remove(n2);
        }
    }

    void addToTempLobsToFree(CLOB cLOB) {
        try {
            if (this.currentResultSet != null && this.currentResultSet.getType() == 1003 && !this.sqlKind.isPlsqlOrCall()) {
                this.tempRowClobsToFree.add(cLOB);
            } else {
                if (this.tempStmtClobsToFree == null) {
                    this.tempStmtClobsToFree = new ArrayList();
                }
                this.tempStmtClobsToFree.add(cLOB);
            }
        }
        catch (SQLException sQLException) {
        }
    }

    void addToTempLobsToFree(BLOB bLOB) {
        try {
            if (this.currentResultSet != null && this.currentResultSet.getType() == 1003 && !this.sqlKind.isPlsqlOrCall()) {
                this.tempRowBlobsToFree.add(bLOB);
            } else {
                if (this.tempStmtBlobsToFree == null) {
                    this.tempStmtBlobsToFree = new ArrayList();
                }
                this.tempStmtBlobsToFree.add(bLOB);
            }
        }
        catch (SQLException sQLException) {
        }
    }

    void cleanAllTempLobs() {
        this.cleanTempClobs(this.tempStmtClobsToFree);
        this.tempStmtClobsToFree = null;
        this.cleanTempBlobs(this.tempStmtBlobsToFree);
        this.tempStmtBlobsToFree = null;
        this.cleanTempClobs(this.oldTempClobsToFree);
        this.oldTempClobsToFree = null;
        this.cleanTempBlobs(this.oldTempBlobsToFree);
        this.oldTempBlobsToFree = null;
        this.cleanAllRowLobs();
    }

    void cleanAllRowLobs() {
        if (!this.tempRowClobsToFree.isEmpty()) {
            this.cleanTempClobs(this.tempRowClobsToFree);
            this.tempRowClobsToFree.clear();
        }
        if (!this.tempRowBlobsToFree.isEmpty()) {
            this.cleanTempBlobs(this.tempRowBlobsToFree);
            this.tempRowBlobsToFree.clear();
        }
    }

    void cleanOldTempLobs() {
        this.cleanTempClobs(this.oldTempClobsToFree);
        this.cleanTempBlobs(this.oldTempBlobsToFree);
        this.oldTempClobsToFree = this.tempStmtClobsToFree;
        this.tempStmtClobsToFree = null;
        this.oldTempBlobsToFree = this.tempStmtBlobsToFree;
        this.tempStmtBlobsToFree = null;
    }

    void cleanTempClobs(ArrayList<CLOB> arrayList) {
        if (arrayList != null) {
            Iterator<CLOB> iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                try {
                    iterator.next().freeTemporary();
                }
                catch (SQLException sQLException) {
                }
            }
        }
    }

    void cleanTempBlobs(ArrayList<BLOB> arrayList) {
        if (arrayList != null) {
            Iterator<BLOB> iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                try {
                    iterator.next().freeTemporary();
                }
                catch (SQLException sQLException) {
                }
            }
        }
    }

    TimeZone getDefaultTimeZone() throws SQLException {
        return this.getDefaultTimeZone(false);
    }

    TimeZone getDefaultTimeZone(boolean bl) throws SQLException {
        if (this.defaultTimeZone == null) {
            try {
                this.defaultTimeZone = this.connection.getDefaultTimeZone();
            }
            catch (SQLException sQLException) {
            }
            if (this.defaultTimeZone == null) {
                this.defaultTimeZone = TimeZone.getDefault();
            }
        }
        return this.defaultTimeZone;
    }

    @Override
    public void setDatabaseChangeRegistration(DatabaseChangeRegistration databaseChangeRegistration) throws SQLException {
        this.registration = (NTFDCNRegistration)databaseChangeRegistration;
    }

    @Override
    public String[] getRegisteredTableNames() throws SQLException {
        return this.dcnTableName;
    }

    @Override
    public long getRegisteredQueryId() throws SQLException {
        return this.dcnQueryId;
    }

    Calendar getDefaultCalendar() throws SQLException {
        if (this.defaultCalendar == null) {
            this.defaultCalendar = Calendar.getInstance(this.getDefaultTimeZone(), Locale.US);
        }
        return this.defaultCalendar;
    }

    void releaseBuffers() {
        this.rowData.free();
        if (this.bindData != null && this.bindData != this.rowData) {
            this.bindData.free();
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }

    @Override
    public boolean isPoolable() throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        return this.cacheState != 3;
    }

    @Override
    public void setPoolable(boolean bl) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.cacheState = bl ? 1 : 3;
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        if (clazz.isInterface()) {
            return clazz.isInstance(this);
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface() && clazz.isInstance(this)) {
            return (T)this;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    <T> T getObject(long l2, int n2, Class<T> clazz) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getObject(this.physicalRowIndex(l2), clazz);
    }

    int getBytes(long l2, int n2, byte[] byArray, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.lastIndex = n2;
            if (this.streamList != null) {
                this.closeUsedStreams(n2);
            }
            int n4 = this.accessors[n2 + this.offsetOfFirstUserColumn].getBytes(this.physicalRowIndex(l2), byArray, n3);
            return n4;
        }
    }

    RowId getPrependedRowId(long l2) throws SQLException {
        assert (this.isRowidPrepended) : "no rowid";
        return this.accessors[0].getROWID(this.physicalRowIndex(l2));
    }

    OracleResultSet.AuthorizationIndicator getAuthorizationIndicator(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 - 1].getAuthorizationIndicator(this.physicalRowIndex(l2));
    }

    @Override
    protected OracleConnection getConnectionDuringExceptionHandling() {
        return this.connection;
    }

    Calendar getGMTCalendar() {
        if (this.gmtCalendar == null) {
            this.gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.US);
        }
        return this.gmtCalendar;
    }

    void extractNioDefineBuffers(int n2) throws SQLException {
    }

    void processLobPrefetchMetaData(Object[] objectArray) {
    }

    void internalClose() throws SQLException {
        this.closed = true;
        if (this.currentResultSet != null) {
            this.currentResultSet.closed = true;
        }
        this.cleanupDefines();
        this.bindBytes = null;
        this.bindChars = null;
        this.bindIndicators = null;
        this.outBindAccessors = null;
        this.parameterStream = null;
        this.userStream = null;
        this.ibtBindBytes = null;
        this.ibtBindChars = null;
        this.ibtBindIndicators = null;
        this.lobPrefetchMetaData = null;
        this.tmpByteArray = null;
        this.definedColumnType = null;
        this.definedColumnSize = null;
        this.definedColumnFormOfUse = null;
        if (this.wrapper != null) {
            this.wrapper.close();
        }
    }

    void calculateCheckSum() throws SQLException {
        if (!this.connection.checksumMode.needToCalculateFetchChecksum()) {
            return;
        }
        this.localCheckSum = this.checkSum;
        if (this.accessors != null && !this.isDmlReturning) {
            this.accessorChecksum(this.accessors);
        }
        if (this.outBindAccessors != null) {
            this.accessorChecksum(this.outBindAccessors);
        }
        if (this.accessors != null && this.returnParamsFetched && this.isDmlReturning) {
            this.accessorChecksum(this.accessors);
        }
        this.checkSum = this.localCheckSum = CRC64.updateChecksum(this.localCheckSum, this.validRows);
        this.localCheckSum = 0L;
    }

    void accessorChecksum(Accessor[] accessorArray) throws SQLException {
        int n2 = 0;
        boolean bl = false;
        block4: for (Accessor accessor : accessorArray) {
            if (accessor == null) continue;
            switch (accessor.internalType) {
                case 112: 
                case 113: 
                case 114: 
                case 119: {
                    if (n2 != 0) continue block4;
                    bl = true;
                    continue block4;
                }
                case 8: 
                case 24: {
                    bl = false;
                    break block4;
                }
                default: {
                    bl = false;
                    ++n2;
                    int n3 = 0;
                    while ((long)n3 < this.validRows) {
                        this.localCheckSum = accessor.updateChecksum(this.localCheckSum, n3);
                        ++n3;
                    }
                    break block0;
                }
            }
        }
        if (bl) {
            this.checkSumComputationFailure = true;
        }
    }

    @Override
    public long getChecksum() throws SQLException {
        if (this.checkSumComputationFailure) {
            throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getChecksum").fillInStackTrace();
        }
        return this.checkSum;
    }

    @Override
    public void registerBindChecksumListener(OracleStatement.BindChecksumListener bindChecksumListener) throws SQLException {
        this.bindChecksumListener = bindChecksumListener;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        this.ensureOpen();
        this.isCloseOnCompletion = true;
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        this.ensureOpen();
        return this.isCloseOnCompletion;
    }

    boolean closeByDependent() throws SQLException {
        if (this.isCloseOnCompletion && (this.currentResultSet == null || this.currentResultSet.isComplete())) {
            if (this.parent == null || !this.parent.closeByDependent()) {
                this.close();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isSimpleIdentifier(String string) throws SQLException {
        return this.connection.isSimpleIdentifier(string);
    }

    @Override
    public String enquoteLiteral(String string) throws SQLException {
        return this.connection.enquoteLiteral(string);
    }

    @Override
    public String enquoteIdentifier(String string, boolean bl) throws SQLException {
        return this.connection.enquoteIdentifier(string, bl);
    }

    @Override
    public void setACProxy(Object object) {
        this.acProxy = object;
    }

    @Override
    public Object getACProxy() {
        return this.acProxy;
    }

    @Override
    public final long getQueryId() throws SQLException {
        return this.queryId;
    }

    @Override
    public final byte[] getCompileKey() throws SQLException {
        return this.querycacheCompileKey;
    }

    final void setQueryCompileKey(byte[] byArray) {
        if (this.connection.isResultSetCacheEnabled && byArray != null) {
            this.querycacheCompileKey = byArray;
        }
    }

    final void setQueryId(long l2) {
        this.queryId = l2;
    }

    byte[] getRuntimeKey() throws SQLException {
        return null;
    }

    void clearCursorId() {
        this.cursorId = 0;
        this.needToParse = true;
    }

    abstract long allocateRowDataSpace(int var1);

    @Override
    public void setShardingKeyRpnTokens(byte[] byArray) throws SQLException {
        this.shardingKeyRpnTokens = byArray;
    }

    @Override
    public byte[] getShardingKeyRpnTokens() throws SQLException {
        return this.shardingKeyRpnTokens;
    }

    final boolean isSqlRewritten() {
        return this.isAutoGeneratedKey || this.sqlObject.includeRowid;
    }

    static enum QueryCacheState {
        UNKNOWN,
        CACHEABLE,
        UNCACHEABLE;

    }

    static enum FetchMode {
        OVERWRITE,
        APPEND;

    }
}

