/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.json.JsonValue
 *  javax.json.stream.JsonGenerator
 *  javax.json.stream.JsonParser
 */
package oracle.jdbc.driver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Properties;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import oracle.jdbc.OracleData;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.BINARY_DOUBLEBinder;
import oracle.jdbc.driver.BINARY_DOUBLENullBinder;
import oracle.jdbc.driver.BINARY_FLOATBinder;
import oracle.jdbc.driver.BINARY_FLOATNullBinder;
import oracle.jdbc.driver.BfileBinder;
import oracle.jdbc.driver.BfileNullBinder;
import oracle.jdbc.driver.BigDecimalBinder;
import oracle.jdbc.driver.BinaryDoubleBinder;
import oracle.jdbc.driver.BinaryDoubleNullBinder;
import oracle.jdbc.driver.BinaryFloatBinder;
import oracle.jdbc.driver.BinaryFloatNullBinder;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.BlobBinder;
import oracle.jdbc.driver.BlobNullBinder;
import oracle.jdbc.driver.BooleanBinder;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.ByteBinder;
import oracle.jdbc.driver.ByteCopyingBinder;
import oracle.jdbc.driver.ClobBinder;
import oracle.jdbc.driver.ClobNullBinder;
import oracle.jdbc.driver.CopiedByteBinder;
import oracle.jdbc.driver.CopiedCharBinder;
import oracle.jdbc.driver.CopiedDataBinder;
import oracle.jdbc.driver.CopiedNullBinder;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DMSFactory;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.DateNullBinder;
import oracle.jdbc.driver.DatumBinder;
import oracle.jdbc.driver.DoubleBinder;
import oracle.jdbc.driver.FixedCHARBinder;
import oracle.jdbc.driver.FixedCHARCopyingBinder;
import oracle.jdbc.driver.FixedCHARNullBinder;
import oracle.jdbc.driver.FloatBinder;
import oracle.jdbc.driver.IntBinder;
import oracle.jdbc.driver.IntervalDSBinder;
import oracle.jdbc.driver.IntervalDSNullBinder;
import oracle.jdbc.driver.IntervalYMBinder;
import oracle.jdbc.driver.IntervalYMNullBinder;
import oracle.jdbc.driver.JavaToJavaConverter;
import oracle.jdbc.driver.JsonBinder;
import oracle.jdbc.driver.JsonNullBinder;
import oracle.jdbc.driver.LittleEndianRowidBinder;
import oracle.jdbc.driver.LittleEndianSetCHARBinder;
import oracle.jdbc.driver.LogicalTransactionId;
import oracle.jdbc.driver.LongBinder;
import oracle.jdbc.driver.NamedTypeBinder;
import oracle.jdbc.driver.NamedTypeNullBinder;
import oracle.jdbc.driver.OracleBfile;
import oracle.jdbc.driver.OracleBlob;
import oracle.jdbc.driver.OracleBlobOutputStream;
import oracle.jdbc.driver.OracleClob;
import oracle.jdbc.driver.OracleClobWriter;
import oracle.jdbc.driver.OracleDateBinder;
import oracle.jdbc.driver.OracleNumberBinder;
import oracle.jdbc.driver.OracleParameterMetaData;
import oracle.jdbc.driver.OraclePreparedStatementReadOnly;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.OracleTimestampBinder;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.PlsqlBooleanBinder;
import oracle.jdbc.driver.PlsqlBooleanNullBinder;
import oracle.jdbc.driver.PlsqlIbtBindInfo;
import oracle.jdbc.driver.PlsqlRawBinder;
import oracle.jdbc.driver.RawBinder;
import oracle.jdbc.driver.RawNullBinder;
import oracle.jdbc.driver.RefCursorBinder;
import oracle.jdbc.driver.RefCursorNullBinder;
import oracle.jdbc.driver.RefTypeBinder;
import oracle.jdbc.driver.RefTypeNullBinder;
import oracle.jdbc.driver.RowidBinder;
import oracle.jdbc.driver.RowidNullBinder;
import oracle.jdbc.driver.SetCHARBinder;
import oracle.jdbc.driver.SetCHARNullBinder;
import oracle.jdbc.driver.ShortBinder;
import oracle.jdbc.driver.StringBinder;
import oracle.jdbc.driver.T4CDirectPathPreparedStatement;
import oracle.jdbc.driver.T4CRowidAccessor;
import oracle.jdbc.driver.TSLTZBinder;
import oracle.jdbc.driver.TSLTZNullBinder;
import oracle.jdbc.driver.TSTZBinder;
import oracle.jdbc.driver.TSTZNullBinder;
import oracle.jdbc.driver.TimeBinder;
import oracle.jdbc.driver.TimestampBinder;
import oracle.jdbc.driver.TimestampNullBinder;
import oracle.jdbc.driver.TypeBinder;
import oracle.jdbc.driver.TypeCopyingBinder;
import oracle.jdbc.driver.VarcharBinder;
import oracle.jdbc.driver.VarcharCopyingBinder;
import oracle.jdbc.driver.VarcharNullBinder;
import oracle.jdbc.driver.VarnumNullBinder;
import oracle.jdbc.driver.json.binary.OsonConstants;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.ObjectData;
import oracle.jdbc.internal.OracleRef;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.internal.TTCData;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleTypeADT;
import oracle.jdbc.oracore.OracleTypeCOLLECTION;
import oracle.jdbc.oracore.OracleTypeNUMBER;
import oracle.jdbc.proxy.ProxyFactory;
import oracle.jdbc.proxy._Proxy_;
import oracle.sql.ANYDATA;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.BFILE;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.CharacterSet;
import oracle.sql.CustomDatum;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.DatumWithConnection;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
import oracle.sql.OpaqueDescriptor;
import oracle.sql.RAW;
import oracle.sql.REF;
import oracle.sql.ROWID;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TypeDescriptor;
import oracle.sql.json.OracleJsonDatum;
import oracle.sql.json.OracleJsonException;
import oracle.sql.json.OracleJsonFactory;
import oracle.sql.json.OracleJsonGenerator;
import oracle.sql.json.OracleJsonParser;
import oracle.sql.json.OracleJsonValue;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONNECT})
abstract class OraclePreparedStatement
extends OracleStatement
implements oracle.jdbc.internal.OraclePreparedStatement {
    int numberOfBindRowsAllocated;
    static Binder theStaticDBACopyingBinder = OraclePreparedStatementReadOnly.theStaticDBACopyingBinder;
    static Binder theStaticLongStreamBinder = OraclePreparedStatementReadOnly.theStaticLongStreamBinder;
    Binder theLongStreamBinder = theStaticLongStreamBinder;
    static Binder theStaticLongStreamForStringBinder = OraclePreparedStatementReadOnly.theStaticLongStreamForStringBinder;
    Binder theLongStreamForStringBinder = theStaticLongStreamForStringBinder;
    static Binder theStaticLongStreamForStringCopyingBinder = OraclePreparedStatementReadOnly.theStaticLongStreamForStringCopyingBinder;
    static Binder theStaticLongRawStreamBinder = OraclePreparedStatementReadOnly.theStaticLongRawStreamBinder;
    Binder theLongRawStreamBinder = theStaticLongRawStreamBinder;
    static Binder theStaticLongRawStreamForBytesBinder = OraclePreparedStatementReadOnly.theStaticLongRawStreamForBytesBinder;
    Binder theLongRawStreamForBytesBinder = theStaticLongRawStreamForBytesBinder;
    static Binder theStaticLongRawStreamForBytesCopyingBinder = OraclePreparedStatementReadOnly.theStaticLongRawStreamForBytesCopyingBinder;
    static Binder theStaticPlsqlIbtCopyingBinder = OraclePreparedStatementReadOnly.theStaticPlsqlIbtCopyingBinder;
    static Binder theStaticPlsqlIbtBinder = OraclePreparedStatementReadOnly.theStaticPlsqlIbtBinder;
    static Binder theStaticPlsqlIbtNullBinder = OraclePreparedStatementReadOnly.theStaticPlsqlIbtNullBinder;
    Binder thePlsqlIbtBinder = theStaticPlsqlIbtBinder;
    Binder thePlsqlNullBinder = theStaticPlsqlIbtNullBinder;
    static Binder theStaticOutBinder = OraclePreparedStatementReadOnly.theStaticOutBinder;
    Binder theOutBinder = theStaticOutBinder;
    static Binder theStaticReturnParamBinder = OraclePreparedStatementReadOnly.theStaticReturnParamBinder;
    Binder theReturnParamBinder = theStaticReturnParamBinder;
    public static final int TypeBinder_BYTELEN = 24;
    char[] digits = new char[20];
    Binder[][] binders;
    CLOB[] lastBoundClobs;
    BLOB[] lastBoundBlobs;
    PlsqlIbtBindInfo[][] parameterPlsqlIbt;
    Binder[] currentRowBinders;
    int[] currentRowByteLens;
    int[] currentRowCharLens;
    int[] currentRowDataLengths;
    long[] currentRowDataOffsets;
    Accessor[] currentRowBindAccessors;
    short[] currentRowFormOfUse;
    boolean[] currentRowFormOfUseSet;
    boolean currentRowNeedToPrepareBinds = true;
    int[] currentBatchByteLens;
    int[] currentBatchCharLens;
    Accessor[] currentBatchBindAccessors;
    short[] currentBatchFormOfUse;
    boolean currentBatchNeedToPrepareBinds;
    int currentBatchAccumulatedBindsSize;
    static final int BATCH_SIZE_THRESHOLD = 0x200000;
    BatchFIFONode batchFIFOFront;
    BatchFIFONode batchFIFOBack;
    int cachedBindByteSize = 0;
    int cachedBindCharSize = 0;
    int cachedBindIndicatorSize = 0;
    int totalBindByteLength;
    int totalBindCharLength;
    int totalBindIndicatorLength;
    static final int BIND_METADATA_NUMBER_OF_BIND_POSITIONS_OFFSET = 0;
    static final int BIND_METADATA_BIND_BUFFER_CAPACITY_OFFSET_HI = 1;
    static final int BIND_METADATA_BIND_BUFFER_CAPACITY_OFFSET_LO = 2;
    static final int BIND_METADATA_NUMBER_OF_BOUND_ROWS_OFFSET_HI = 3;
    static final int BIND_METADATA_NUMBER_OF_BOUND_ROWS_OFFSET_LO = 4;
    static final int BIND_METADATA_PER_POSITION_DATA_OFFSET = 5;
    static final int BIND_METADATA_TYPE_OFFSET = 0;
    static final int BIND_METADATA_BYTE_PITCH_OFFSET = 1;
    static final int BIND_METADATA_CHAR_PITCH_OFFSET = 2;
    static final int BIND_METADATA_VALUE_DATA_OFFSET_HI = 3;
    static final int BIND_METADATA_VALUE_DATA_OFFSET_LO = 4;
    static final int BIND_METADATA_NULL_INDICATORS_OFFSET_HI = 5;
    static final int BIND_METADATA_NULL_INDICATORS_OFFSET_LO = 6;
    static final int BIND_METADATA_VALUE_LENGTHS_OFFSET_HI = 7;
    static final int BIND_METADATA_VALUE_LENGTHS_OFFSET_LO = 8;
    static final int BIND_METADATA_FORM_OF_USE_OFFSET = 9;
    static final int BIND_METADATA_PER_POSITION_SIZE = 10;
    static final int PLSQL_BOOLEAN_DB_MAJOR_VERSION_SUPPORT = 12;
    static final int PLSQL_BOOLEAN_DB_MINOR_VERSION_SUPPORT = 1;
    static final int SETLOB_NO_LENGTH = -1;
    int bindBufferCapacity;
    int numberOfBoundRows;
    int indicatorsOffset;
    int valueLengthsOffset;
    boolean preparedAllBinds;
    boolean preparedByteBinds;
    boolean preparedCharBinds;
    Binder[] lastBinders;
    byte[] lastBoundBytes;
    int lastBoundByteOffset;
    char[] lastBoundChars;
    int lastBoundCharOffset;
    int[] lastBoundByteOffsets;
    int[] lastBoundCharOffsets;
    int[] lastBoundByteLens;
    int[] lastBoundCharLens;
    short[] lastBoundInds;
    short[] lastBoundLens;
    boolean lastBoundNeeded = false;
    InputStream[] lastBoundStream;
    int[] lastBoundDataLengths;
    long[] lastBoundDataOffsets;
    private static final int STREAM_MAX_BYTES_SQL = Integer.MAX_VALUE;
    int maxRawBytesSql;
    int maxRawBytesPlsql;
    int maxVcsCharsSql;
    int maxVcsNCharsSql;
    int maxVcsBytesPlsql;
    private int maxCharSize = 0;
    private int maxNCharSize = 0;
    private int charMaxCharsSql = 0;
    private int charMaxNCharsSql = 0;
    protected int maxVcsCharsPlsql = 0;
    private int maxVcsNCharsPlsql = 0;
    int maxIbtVarcharElementLength = 0;
    private int maxStreamCharsSql = 0;
    private int maxStreamNCharsSql = 0;
    protected boolean isServerCharSetFixedWidth = false;
    private boolean isServerNCharSetFixedWidth = false;
    int minVcsBindSize;
    long prematureBatchCount;
    boolean checkBindTypes = true;
    boolean scrollRsetTypeSolved;
    private static final double MIN_NUMBER = 1.0E-130;
    private static final double MAX_NUMBER = 1.0E126;
    short defaultFormOfUse = 1;
    int[] indicesOfNotNullLastBinds = null;
    static int call_count = 0;
    int SetBigStringTryClob = 0;
    boolean batchUpdate = false;
    static int batchQueueCounter = 0;

    OraclePreparedStatement(PhysicalConnection physicalConnection, String string, int n2, int n3) throws SQLException {
        this(physicalConnection, string, n2, n3, 1003, 1007);
    }

    OraclePreparedStatement(PhysicalConnection physicalConnection, String string, int n2, int n3, int n4, int n5) throws SQLException {
        super(physicalConnection, n2, n3, n4, n5);
        this.initPreparedStatement(physicalConnection, string);
    }

    OraclePreparedStatement(PhysicalConnection physicalConnection, String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        super(physicalConnection, properties);
        this.initPreparedStatement(physicalConnection, string);
    }

    private void initPreparedStatement(PhysicalConnection physicalConnection, String string) throws SQLException {
        this.cacheState = 1;
        this.statementType = 1;
        this.needToParse = true;
        this.processEscapes = physicalConnection.processEscapes;
        this.sqlObject.initialize(string);
        this.sqlKind = this.sqlObject.getSqlKind();
        this.clearParameters = true;
        this.scrollRsetTypeSolved = false;
        this.prematureBatchCount = 0L;
        this.initializeBinds();
        this.minVcsBindSize = physicalConnection.minVcsBindSize;
        this.maxRawBytesSql = physicalConnection.maxRawBytesSql;
        this.maxRawBytesPlsql = physicalConnection.maxRawBytesPlsql;
        this.maxVcsCharsSql = physicalConnection.maxVcsCharsSql;
        this.maxVcsNCharsSql = physicalConnection.maxVcsNCharsSql;
        this.maxVcsBytesPlsql = physicalConnection.maxVcsBytesPlsql;
        this.maxIbtVarcharElementLength = physicalConnection.maxIbtVarcharElementLength;
        this.maxCharSize = this.connection.conversion.sMaxCharSize;
        this.maxNCharSize = this.connection.conversion.maxNCharSize;
        this.maxVcsCharsPlsql = this.maxVcsBytesPlsql / this.maxCharSize;
        this.maxVcsNCharsPlsql = this.maxVcsBytesPlsql / this.maxNCharSize;
        this.maxStreamCharsSql = Integer.MAX_VALUE / this.maxCharSize;
        this.maxStreamNCharsSql = this.maxRawBytesSql / this.maxNCharSize;
        this.isServerCharSetFixedWidth = this.connection.conversion.isServerCharSetFixedWidth;
        this.isServerNCharSetFixedWidth = this.connection.conversion.isServerNCharSetFixedWidth;
        this.queryCacheState = physicalConnection.isResultSetCacheEnabled ? OracleStatement.QueryCacheState.UNKNOWN : OracleStatement.QueryCacheState.UNCACHEABLE;
    }

    @Override
    void createDMSSensors() throws SQLException {
        if (DMSFactory.DMSConsole.getSensorWeight() > DMSFactory.DMSConole_NORMAL) {
            String string = "STMT-" + OracleStatement.GLOBAL_STATEMENT_NUMBER++;
            DMSFactory dMSFactory = DMSFactory.getInstance();
            DMSFactory.DMSNoun dMSNoun = dMSFactory.get(string);
            if (dMSNoun == null) {
                dMSNoun = dMSFactory.createNoun(this.connection.dmsParent, string, "JDBC_Statement");
                this.dmsSqlText = dMSFactory.createState(dMSNoun, "SQLText", "", "current SQL text", this.connection.dmsUpdateSqlText() ? this.sqlObject.toString() : null);
                this.dmsExecute = dMSFactory.createPhaseEvent(dMSNoun, "Execute", "the time required for all executions of this statement");
                this.dmsExecute.deriveMetric(DMSFactory.PhaseEventIntf_all);
                this.dmsFetch = dMSFactory.createPhaseEvent(dMSNoun, "Fetch", "the time required for all fetches by this statement");
                this.dmsFetch.deriveMetric(DMSFactory.PhaseEventIntf_all);
            } else {
                this.dmsSqlText = (DMSFactory.DMSState)dMSNoun.getSensor("SQLText");
                this.dmsExecute = (DMSFactory.DMSPhase)dMSNoun.getSensor("Execute");
                this.dmsFetch = (DMSFactory.DMSPhase)dMSNoun.getSensor("Fetch");
            }
        } else {
            super.createDMSSensors();
        }
    }

    void allocBinds(int n2) throws SQLException {
        int n3;
        boolean bl = n2 > this.numberOfBindRowsAllocated;
        this.initializeIndicatorSubRange();
        int n4 = this.bindIndicatorSubRange + 5 + this.numberOfBindPositions * 10;
        int n5 = n2 * this.numberOfBindPositions;
        this.bindDataOffsets = new long[n5];
        this.bindDataLengths = new int[n5];
        int n6 = n4 + 2 * n5;
        if (n6 > this.totalBindIndicatorLength) {
            short[] sArray = this.bindIndicators;
            n3 = this.bindIndicatorOffset;
            this.bindIndicatorOffset = 0;
            this.bindIndicators = new short[n6];
            this.totalBindIndicatorLength = n6;
            if (sArray != null && bl) {
                System.arraycopy(sArray, n3, this.bindIndicators, this.bindIndicatorOffset, n4);
            }
        }
        this.bindIndicatorSubRange += this.bindIndicatorOffset;
        this.bindIndicators[this.bindIndicatorSubRange + 0] = (short)this.numberOfBindPositions;
        this.indicatorsOffset = this.bindIndicatorOffset + n4;
        this.valueLengthsOffset = this.indicatorsOffset + n5;
        int n7 = this.indicatorsOffset;
        n3 = this.valueLengthsOffset;
        int n8 = this.bindIndicatorSubRange + 5;
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            this.bindIndicators[n8 + 5] = (short)(n7 >> 16);
            this.bindIndicators[n8 + 6] = (short)(n7 & 0xFFFF);
            this.bindIndicators[n8 + 7] = (short)(n3 >> 16);
            this.bindIndicators[n8 + 8] = (short)(n3 & 0xFFFF);
            n7 += n2;
            n3 += n2;
            n8 += 10;
        }
    }

    void initializeBinds() throws SQLException {
        this.numberOfBindPositions = this.sqlObject.getParameterCount();
        this.numReturnParams = this.sqlObject.getReturnParameterCount();
        if (this.numberOfBindPositions == 0) {
            this.currentRowNeedToPrepareBinds = false;
            return;
        }
        this.numberOfBindRowsAllocated = this.batch;
        this.binders = new Binder[this.numberOfBindRowsAllocated][this.numberOfBindPositions];
        this.currentRowBinders = this.binders[0];
        this.currentRowByteLens = new int[this.numberOfBindPositions];
        this.currentBatchByteLens = new int[this.numberOfBindPositions];
        this.currentRowCharLens = new int[this.numberOfBindPositions];
        this.currentBatchCharLens = new int[this.numberOfBindPositions];
        this.currentRowDataOffsets = new long[this.numberOfBindPositions];
        this.currentRowDataLengths = new int[this.numberOfBindPositions];
        this.currentRowFormOfUse = new short[this.numberOfBindPositions];
        this.currentRowFormOfUseSet = new boolean[this.numberOfBindPositions];
        this.currentBatchFormOfUse = new short[this.numberOfBindPositions];
        this.lastBoundClobs = new CLOB[this.numberOfBindPositions];
        this.lastBoundBlobs = new BLOB[this.numberOfBindPositions];
        if (this.connection.defaultnchar) {
            this.defaultFormOfUse = (short)2;
        }
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            this.currentRowFormOfUse[i2] = this.defaultFormOfUse;
            this.currentRowFormOfUseSet[i2] = false;
            this.currentBatchFormOfUse[i2] = this.defaultFormOfUse;
        }
        this.lastBinders = new Binder[this.numberOfBindPositions];
        this.lastBoundCharLens = new int[this.numberOfBindPositions];
        this.lastBoundByteOffsets = new int[this.numberOfBindPositions];
        this.lastBoundCharOffsets = new int[this.numberOfBindPositions];
        this.lastBoundByteLens = new int[this.numberOfBindPositions];
        this.lastBoundInds = new short[this.numberOfBindPositions];
        this.lastBoundLens = new short[this.numberOfBindPositions];
        this.lastBoundDataLengths = new int[this.numberOfBindPositions];
        this.lastBoundDataOffsets = new long[this.numberOfBindPositions];
        this.allocBinds(this.numberOfBindRowsAllocated);
    }

    void growBinds(int n2) throws SQLException {
        int n3;
        Binder[][] binderArray = this.binders;
        this.binders = new Binder[n2][];
        if (binderArray != null) {
            System.arraycopy(binderArray, 0, this.binders, 0, this.numberOfBindRowsAllocated);
        }
        for (int i2 = this.numberOfBindRowsAllocated; i2 < n2; ++i2) {
            this.binders[i2] = new Binder[this.numberOfBindPositions];
        }
        this.allocBinds(n2);
        if (this.parameterStream != null) {
            InputStream[][] inputStreamArray = this.parameterStream;
            this.parameterStream = new InputStream[n2][];
            System.arraycopy(inputStreamArray, 0, this.parameterStream, 0, this.numberOfBindRowsAllocated);
            for (n3 = this.numberOfBindRowsAllocated; n3 < n2; ++n3) {
                this.parameterStream[n3] = new InputStream[this.numberOfBindPositions];
            }
        }
        if (this.userStream != null) {
            Object[][] objectArray = this.userStream;
            this.userStream = new Object[n2][];
            System.arraycopy(objectArray, 0, this.userStream, 0, this.numberOfBindRowsAllocated);
            for (n3 = this.numberOfBindRowsAllocated; n3 < n2; ++n3) {
                this.userStream[n3] = new Object[this.numberOfBindPositions];
            }
        }
        if (this.parameterPlsqlIbt != null) {
            PlsqlIbtBindInfo[][] plsqlIbtBindInfoArray = this.parameterPlsqlIbt;
            this.parameterPlsqlIbt = new PlsqlIbtBindInfo[n2][];
            System.arraycopy(plsqlIbtBindInfoArray, 0, this.parameterPlsqlIbt, 0, this.numberOfBindRowsAllocated);
            for (n3 = this.numberOfBindRowsAllocated; n3 < n2; ++n3) {
                this.parameterPlsqlIbt[n3] = new PlsqlIbtBindInfo[this.numberOfBindPositions];
            }
        }
        this.numberOfBindRowsAllocated = n2;
        this.currentRowNeedToPrepareBinds = true;
    }

    String dumpBindData(String string) {
        return this.dumpBindData(string, 1000);
    }

    String dumpBindData(String string, int n2) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.println(string);
        long l2 = 0L;
        int n3 = (int)this.bindData.length();
        if (n3 != 0 && this.bindData != null && this.bindData.length() != 0L) {
            n3 = Math.min(n3, n2);
            byte[] byArray = new byte[n3];
            int n4 = 0;
            if (l2 >= 0L && l2 + (long)n3 <= this.bindData.length() && n4 >= 0 && n4 + n3 <= byArray.length) {
                printWriter.println("offset: " + l2 + " length: " + n3 + " bindData.length(): " + this.bindData.length());
                this.bindData.get(l2, byArray, n4, n3);
                for (int i2 = 0; i2 < n3; ++i2) {
                    printWriter.println("index: " + i2 + " value: " + Integer.toHexString(byArray[i2] & 0xFF));
                }
            }
        }
        printWriter.println();
        printWriter.flush();
        return stringWriter.toString();
    }

    void compressLastBoundData() throws SQLException {
        if (this.bindData.length() == 0L) {
            return;
        }
        long l2 = -1L;
        ArrayList<Pair> arrayList = new ArrayList<Pair>();
        for (int i2 = 0; i2 < this.lastBoundDataLengths.length; ++i2) {
            if (this.lastBoundDataLengths[i2] <= 0) continue;
            arrayList.add(new Pair(i2, this.lastBoundDataOffsets[i2]));
        }
        Collections.sort(arrayList);
        this.bindData.setPosition(0L);
        for (Pair pair : arrayList) {
            int n2 = pair.getIndex();
            long l3 = this.lastBoundDataOffsets[n2];
            assert (l2 < l3) : "lastOffset: " + l2 + "\tleastOffset: " + l3 + "\tindexOfLeastOffet: " + n2;
            l2 = l3;
            this.copyLeftOneLastBound(n2);
        }
        this.beyondBindData = this.bindData.getPosition();
    }

    String dumpIndicesOfLastBindsToBeCopied(int n2, int[] nArray) throws SQLException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.println("dump indicesOfLastBindsToBeCopied call_count: " + call_count++ + " numNotNullLastBinds: " + n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            long l2 = this.lastBoundDataOffsets[nArray[i2]];
            printWriter.println("copy order: " + i2 + " index: " + nArray[i2] + " offset: " + l2);
        }
        printWriter.println();
        return stringWriter.toString();
    }

    void copyLeftOneLastBound(int n2) throws SQLException {
        long l2 = this.lastBoundDataOffsets[n2];
        long l3 = this.lastBoundDataOffsets[n2];
        long l4 = this.bindData.getPosition();
        int n3 = this.lastBoundDataLengths[n2];
        byte[] byArray = this.connection.getMethodTempByteBuffer(n3);
        this.bindData.get(l3, byArray, 0, n3);
        this.bindData.put(l4, byArray, 0, n3);
        this.bindData.setPosition(l4 + (long)n3);
        this.lastBoundDataOffsets[n2] = l4;
    }

    byte[] getDatumVal(Binder binder) {
        byte[] byArray = null;
        if (binder instanceof TypeBinder) {
            byArray = ((TypeBinder)binder).paramVal;
        } else if (binder instanceof TypeCopyingBinder) {
            byArray = ((TypeCopyingBinder)binder).paramVal;
        } else if (binder instanceof DatumBinder) {
            byArray = ((DatumBinder)binder).paramVal;
        }
        return byArray;
    }

    void setDatumVal(Binder binder, byte[] byArray) {
        if (binder instanceof TypeBinder) {
            ((TypeBinder)binder).paramVal = byArray;
        } else if (binder instanceof TypeCopyingBinder) {
            ((TypeCopyingBinder)binder).paramVal = byArray;
        } else if (binder instanceof DatumBinder) {
            ((DatumBinder)binder).paramVal = byArray;
        }
    }

    OracleTypeADT getOtype(Binder binder) {
        OracleTypeADT oracleTypeADT = null;
        if (binder instanceof TypeBinder) {
            oracleTypeADT = ((TypeBinder)binder).paramOtype;
        } else if (binder instanceof TypeCopyingBinder) {
            oracleTypeADT = ((TypeCopyingBinder)binder).paramOtype;
        }
        return oracleTypeADT;
    }

    short getScale(Binder binder) {
        return binder.scale;
    }

    String getStringBinderVal(int n2, int n3) {
        String string = null;
        if (this.binders != null && this.binders[n2] != null && this.binders[n2][n3] != null) {
            Binder binder = this.binders[n2][n3];
            if (binder instanceof VarcharBinder) {
                string = ((VarcharBinder)binder).paramVal;
            } else if (binder instanceof VarcharCopyingBinder) {
                string = ((VarcharCopyingBinder)binder).paramVal;
            } else if (binder instanceof FixedCHARBinder) {
                string = ((FixedCHARBinder)binder).paramVal;
            } else if (binder instanceof FixedCHARCopyingBinder) {
                string = ((FixedCHARCopyingBinder)binder).paramVal;
            }
        }
        return string;
    }

    short getBinderType(int n2, int n3) {
        short s2 = 0;
        if (this.binders != null && this.binders[n2] != null && this.binders[n2][n3] != null) {
            s2 = this.binders[n2][n3].type;
        } else if (this.lastBinders != null && this.lastBinders[n3] != null) {
            s2 = this.lastBinders[n3].type;
        }
        return s2;
    }

    short isStringBinder(int n2, int n3) {
        short s2 = 0;
        Binder binder = null;
        if (this.binders != null && this.binders[n2] != null && this.binders[n2][n3] != null) {
            binder = this.binders[n2][n3];
        } else if (this.lastBinders != null && this.lastBinders[n3] != null) {
            binder = this.lastBinders[n3];
        }
        if (binder != null && (binder instanceof VarcharBinder || binder instanceof VarcharCopyingBinder || binder instanceof FixedCHARBinder || binder instanceof FixedCHARCopyingBinder)) {
            s2 = 1;
        }
        return s2;
    }

    short isDatumBinder(int n2, int n3) {
        short s2 = 0;
        Binder binder = null;
        if (this.binders != null && this.binders[n2] != null && this.binders[n2][n3] != null) {
            binder = this.binders[n2][n3];
        } else if (this.lastBinders != null && this.lastBinders[n3] != null) {
            binder = this.lastBinders[n3];
        }
        if (binder != null && (binder instanceof TypeBinder || binder instanceof TypeCopyingBinder || binder instanceof DatumBinder)) {
            s2 = 1;
        }
        return s2;
    }

    void setStringBinderVal(int n2, int n3, String string) {
        if (this.binders != null && this.binders[n2] != null && this.binders[n2][n3] != null) {
            Binder binder = this.binders[n2][n3];
            if (binder instanceof VarcharBinder) {
                ((VarcharBinder)binder).paramVal = string;
            } else if (binder instanceof VarcharCopyingBinder) {
                ((VarcharCopyingBinder)binder).paramVal = string;
            } else if (binder instanceof FixedCHARBinder) {
                ((FixedCHARBinder)binder).paramVal = string;
            } else if (binder instanceof FixedCHARCopyingBinder) {
                ((FixedCHARCopyingBinder)binder).paramVal = string;
            }
        }
    }

    byte[] getDatumBinderVal(int n2, int n3) {
        byte[] byArray = null;
        if (this.binders != null && this.binders[n2] != null && this.binders[n2][n3] != null) {
            Binder binder = this.binders[n2][n3];
            byArray = this.getDatumVal(binder);
        }
        return byArray;
    }

    void setDatumBinderVal(int n2, int n3, byte[] byArray) {
        if (this.binders != null && this.binders[n2] != null && this.binders[n2][n3] != null) {
            Binder binder = this.binders[n2][n3];
            this.setDatumVal(binder, byArray);
        }
    }

    OracleTypeADT getDatumBinderOtype(int n2, int n3) {
        OracleTypeADT oracleTypeADT = null;
        if (this.binders != null && this.binders[n2] != null && this.binders[n2][n3] != null) {
            Binder binder = this.binders[n2][n3];
            oracleTypeADT = this.getOtype(binder);
        }
        return oracleTypeADT;
    }

    void processCompletedBindRow(int n2, boolean bl) throws SQLException {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        short s2;
        boolean bl2;
        boolean bl3;
        boolean bl4;
        block74: {
            Binder binder;
            Object object;
            Binder[] binderArray;
            boolean bl5;
            block73: {
                block75: {
                    if (this.numberOfBindPositions == 0) {
                        return;
                    }
                    bl4 = false;
                    bl5 = false;
                    bl3 = false;
                    bl2 = this.currentRank == this.firstRowInBatch;
                    s2 = 0;
                    n7 = 0;
                    Binder[] binderArray2 = this.currentRank == 0 ? (this.lastBinders[0] == null ? null : this.lastBinders) : (binderArray = this.binders[this.currentRank - 1]);
                    if (this.currentRowBindAccessors != null) break block73;
                    int n8 = n6 = this.isAutoGeneratedKey && this.clearParameters ? 1 : 0;
                    if (binderArray == null) {
                        for (n5 = 0; n5 < this.numberOfBindPositions; ++n5) {
                            if (this.currentRowBinders[n5] != null) continue;
                            if (n6 != 0) {
                                this.registerReturnParamsForAutoKey();
                                n6 = 0;
                                continue;
                            }
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 41, (Object)(n5 + 1)).fillInStackTrace();
                        }
                    } else if (this.checkBindTypes) {
                        for (n5 = 0; n5 < this.numberOfBindPositions; ++n5) {
                            if (this.currentRowBinders[n5] == null && n6 != 0) {
                                this.registerReturnParamsForAutoKey();
                                n6 = 0;
                            }
                            if ((object = this.currentRowBinders[n5]) == null) {
                                if (this.clearParameters) {
                                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 41, (Object)(n5 + 1)).fillInStackTrace();
                                }
                                this.currentRowBinders[n5] = binderArray[n5].copyingBinder();
                                if (this.currentRowBinders[n5].type != binderArray[n5].type) {
                                    bl4 = true;
                                }
                                if (this.currentRank == 0) {
                                    this.currentRowBinders[n5].lastBoundValueCleanup(this, n5);
                                }
                                this.currentRowByteLens[n5] = -1;
                                this.currentRowCharLens[n5] = -1;
                                this.currentRowDataLengths[n5] = -1;
                                bl5 = true;
                            } else {
                                n4 = ((Binder)object).type;
                                if ((n4 == 109 || n4 == 111) && binderArray[n5] != null && !this.getOtype((Binder)object).isInHierarchyOf(this.getOtype(binderArray[n5])) || binderArray[n5] != null && (n4 != binderArray[n5].type || n4 == 9 && ((Binder)object).bytelen == 0 != (binderArray[n5].bytelen == 0))) {
                                    bl4 = true;
                                }
                            }
                            if (this.currentBatchFormOfUse[n5] == this.currentRowFormOfUse[n5]) continue;
                            bl4 = true;
                        }
                    } else {
                        for (n5 = 0; n5 < this.numberOfBindPositions; ++n5) {
                            object = this.currentRowBinders[n5];
                            if (object != null) continue;
                            if (this.clearParameters) {
                                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 41, (Object)(n5 + 1)).fillInStackTrace();
                            }
                            this.currentRowBinders[n5] = binderArray[n5].copyingBinder();
                            if (this.currentRowBinders[n5].type != binderArray[n5].type) {
                                bl4 = true;
                            }
                            if (this.currentRank == 0) {
                                this.currentRowBinders[n5].lastBoundValueCleanup(this, n5);
                            }
                            this.currentRowByteLens[n5] = -1;
                            this.currentRowCharLens[n5] = -1;
                            this.currentRowDataLengths[n5] = -1;
                            bl5 = true;
                        }
                    }
                    if (!bl5) break block74;
                    if (bl2) break block75;
                    if (!this.batchUpdate) break block74;
                }
                this.lastBoundNeeded = true;
                break block74;
            }
            if (binderArray == null) {
                for (n6 = 0; n6 < this.numberOfBindPositions; ++n6) {
                    binder = this.currentRowBinders[n6];
                    object = this.currentRowBindAccessors[n6];
                    if (binder == null) {
                        if (object == null) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 41, (Object)(n6 + 1)).fillInStackTrace();
                        }
                        this.currentRowBinders[n6] = this.theOutBinder;
                        continue;
                    }
                    if (object == null || this.isDefineTypeCompatibleWithBindType(((Accessor)object).defineType, binder.type)) continue;
                    bl3 = true;
                    s2 = binder.type;
                    n7 = ((Accessor)object).defineType;
                }
            } else if (this.checkBindTypes) {
                for (n6 = 0; n6 < this.numberOfBindPositions; ++n6) {
                    binder = this.currentRowBinders[n6];
                    object = this.currentRowBindAccessors[n6];
                    if (binder == null) {
                        if (object != null) {
                            this.currentRowBinders[n6] = binder = this.theOutBinder;
                            if (binderArray[n6] != this.theOutBinder) {
                                bl4 = true;
                            }
                        } else {
                            if (this.clearParameters && binderArray[n6] != this.theOutBinder) {
                                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 41, (Object)(n6 + 1)).fillInStackTrace();
                            }
                            this.currentRowBinders[n6] = binder = binderArray[n6];
                            this.currentRowByteLens[n6] = -1;
                            this.currentRowCharLens[n6] = -1;
                            this.currentRowDataLengths[n6] = -1;
                            if (binder != this.theOutBinder) {
                                bl5 = true;
                            }
                        }
                    } else {
                        short s3 = binder.type;
                        if ((s3 == 109 || s3 == 111) && binderArray[n6] != null && !this.getOtype(binder).isInHierarchyOf(this.getOtype(binderArray[n6])) || binderArray[n6] != null && (s3 != binderArray[n6].type || s3 == 9 && binder.bytelen == 0 != (binderArray[n6].bytelen == 0))) {
                            bl4 = true;
                        }
                    }
                    if (this.currentBatchFormOfUse[n6] != this.currentRowFormOfUse[n6]) {
                        bl4 = true;
                    }
                    Accessor accessor = null;
                    if (this.currentBatchBindAccessors != null) {
                        accessor = this.currentBatchBindAccessors[n6];
                    }
                    if (object == null) {
                        this.currentRowBindAccessors[n6] = object = accessor;
                    } else if (accessor != null && ((Accessor)object).defineType != accessor.defineType) {
                        bl4 = true;
                    }
                    if (object == null || binder == this.theOutBinder || ((Accessor)object).defineType == binder.type || this.connection.permitTimestampDateMismatch && binder.type == 180 && ((Accessor)object).defineType == 12) continue;
                    bl3 = true;
                    s2 = binder.type;
                    n7 = ((Accessor)object).defineType;
                }
            } else {
                for (n6 = 0; n6 < this.numberOfBindPositions; ++n6) {
                    binder = this.currentRowBinders[n6];
                    if (binder == null) {
                        if (this.clearParameters && binderArray[n6] != this.theOutBinder) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 41, (Object)(n6 + 1)).fillInStackTrace();
                        }
                        this.currentRowBinders[n6] = binder = binderArray[n6];
                        this.currentRowByteLens[n6] = -1;
                        this.currentRowCharLens[n6] = -1;
                        this.currentRowDataLengths[n6] = -1;
                        if (binder != this.theOutBinder) {
                            bl5 = true;
                        }
                    }
                    if (this.currentRowBindAccessors[n6] != null) continue;
                    this.currentRowBindAccessors[n6] = this.currentBatchBindAccessors[n6];
                }
            }
            if (bl5 && bl2) {
                this.lastBoundNeeded = true;
            }
        }
        if (bl4) {
            if (!bl2) {
                this.enqueueCurrentBatch(false);
            }
            this.needToParse = true;
            this.currentRowNeedToPrepareBinds = true;
            this.needToPrepareDefineBuffer = true;
        } else if (bl) {
            this.enqueueCurrentBatch(false);
            this.needToParse = false;
            this.currentBatchNeedToPrepareBinds = false;
        }
        if (this.currentBatchAccumulatedBindsSize > 0x200000 && this.currentRank > this.firstRowInBatch) {
            this.enqueueCurrentBatch(false);
            this.needToParse = false;
        }
        for (n6 = 0; n6 < this.numberOfBindPositions; ++n6) {
            int n9;
            n5 = this.currentRowCharLens[n6];
            if (n5 == -1 && this.currentRank == this.firstRowInBatch) {
                n5 = this.lastBoundCharLens[n6];
            }
            if ((n9 = this.currentRowByteLens[n6]) == -1 && this.currentRank == this.firstRowInBatch) {
                n9 = this.lastBoundByteLens[n6];
            }
            if ((n4 = this.currentRowDataLengths[n6]) == -1 && this.currentRank == this.firstRowInBatch) {
                n4 = this.lastBoundDataLengths[n6];
            }
            this.currentBatchAccumulatedBindsSize += n5 + n9 + n4;
        }
        if (bl3) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, null, null, " (input type=" + s2 + " output type=" + n7 + ")").fillInStackTrace();
        }
        for (n6 = 0; n6 < this.numberOfBindPositions; ++n6) {
            int n10;
            n5 = this.currentRowByteLens[n6];
            if (n5 == -1 && this.currentRank == this.firstRowInBatch) {
                n5 = this.lastBoundByteLens[n6];
            }
            if (this.currentBatchByteLens[n6] < n5) {
                this.currentBatchByteLens[n6] = n5;
            }
            if ((n10 = this.currentRowCharLens[n6]) == -1 && this.currentRank == this.firstRowInBatch) {
                n10 = this.lastBoundCharLens[n6];
            }
            if (this.currentBatchCharLens[n6] < n10) {
                this.currentBatchCharLens[n6] = n10;
            }
            if ((n4 = this.currentRowDataLengths[n6]) == -1 && this.currentRank == this.firstRowInBatch) {
                n4 = this.lastBoundDataLengths[n6];
            }
            this.currentRowByteLens[n6] = 0;
            this.currentRowCharLens[n6] = 0;
            this.currentRowDataLengths[n6] = 0;
            this.currentBatchFormOfUse[n6] = this.currentRowFormOfUse[n6];
        }
        if (this.currentRowNeedToPrepareBinds) {
            this.currentBatchNeedToPrepareBinds = true;
        }
        if (this.currentRowBindAccessors != null) {
            Accessor[] accessorArray = this.currentBatchBindAccessors;
            this.currentBatchBindAccessors = this.currentRowBindAccessors;
            if (accessorArray == null) {
                accessorArray = new Accessor[this.numberOfBindPositions];
            } else {
                for (n5 = 0; n5 < this.numberOfBindPositions; ++n5) {
                    accessorArray[n5] = null;
                }
            }
            this.currentRowBindAccessors = accessorArray;
        }
        if ((n3 = this.currentRank + 1) < n2) {
            if (n3 >= this.numberOfBindRowsAllocated) {
                n5 = this.numberOfBindRowsAllocated << 1;
                if (n5 <= n3) {
                    n5 = n3 + 1;
                }
                this.growBinds(n5);
                this.currentBatchNeedToPrepareBinds = true;
                if (this.batchFIFOFront != null) {
                    this.batchFIFOFront.current_batch_need_to_prepare_binds = true;
                }
            }
            this.currentRowBinders = this.binders[n3];
        } else {
            this.setupBindBuffers(0, n2);
            this.currentRowBinders = this.binders[0];
        }
        this.currentRowNeedToPrepareBinds = false;
        this.clearParameters = false;
    }

    private boolean isDefineTypeCompatibleWithBindType(int n2, int n3) {
        boolean bl = false;
        if (n2 == n3) {
            bl = true;
        } else if (this.connection.permitTimestampDateMismatch && n3 == 180 && n2 == 12) {
            bl = true;
        }
        return bl;
    }

    void processPlsqlIndexTabBinds(int n2) throws SQLException {
        int n3;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        Binder[] binderArray = this.binders[n2];
        PlsqlIbtBindInfo[] plsqlIbtBindInfoArray = this.parameterPlsqlIbt == null ? null : this.parameterPlsqlIbt[n2];
        for (n3 = 0; n3 < this.numberOfBindPositions; ++n3) {
            PlsqlIbtBindInfo plsqlIbtBindInfo;
            Binder binder = binderArray[n3];
            Accessor accessor = this.currentBatchBindAccessors == null ? null : this.currentBatchBindAccessors[n3];
            PlsqlIbtBindInfo plsqlIbtBindInfo2 = plsqlIbtBindInfo = accessor == null ? null : accessor.plsqlIndexTableBindInfo();
            if (binder.type == 998) {
                plsqlIbtBindInfo2 = plsqlIbtBindInfoArray[n3];
                if (plsqlIbtBindInfo != null) {
                    if (plsqlIbtBindInfo2.element_internal_type != plsqlIbtBindInfo.element_internal_type) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12).fillInStackTrace();
                    }
                    if (plsqlIbtBindInfo2.maxLen < plsqlIbtBindInfo.maxLen) {
                        plsqlIbtBindInfo2.maxLen = plsqlIbtBindInfo.maxLen;
                    }
                    if (plsqlIbtBindInfo2.elemMaxLen < plsqlIbtBindInfo.elemMaxLen) {
                        plsqlIbtBindInfo2.elemMaxLen = plsqlIbtBindInfo.elemMaxLen;
                    }
                    if (plsqlIbtBindInfo2.ibtByteLength > 0) {
                        plsqlIbtBindInfo2.ibtByteLength = plsqlIbtBindInfo2.elemMaxLen * plsqlIbtBindInfo2.maxLen;
                    } else {
                        plsqlIbtBindInfo2.ibtCharLength = plsqlIbtBindInfo2.elemMaxLen * plsqlIbtBindInfo2.maxLen;
                    }
                }
            }
            if (plsqlIbtBindInfo2 == null) continue;
            ++n4;
            n6 += plsqlIbtBindInfo2.ibtByteLength;
            n7 += plsqlIbtBindInfo2.ibtCharLength;
            n5 += plsqlIbtBindInfo2.maxLen;
        }
        if (n4 == 0) {
            return;
        }
        this.ibtBindIndicatorSize = 6 + n4 * 8 + n5 * 2;
        if (this.ibtBindIndicators == null || this.ibtBindIndicators.length < this.ibtBindIndicatorSize) {
            this.ibtBindIndicators = new short[this.ibtBindIndicatorSize];
        }
        this.ibtBindIndicatorOffset = 0;
        if (n6 > 0 && (this.ibtBindBytes == null || this.ibtBindBytes.length < n6)) {
            this.ibtBindBytes = new byte[n6];
        }
        this.ibtBindByteOffset = 0;
        if (n7 > 0 && (this.ibtBindChars == null || this.ibtBindChars.length < n7)) {
            this.ibtBindChars = new char[n7];
        }
        this.ibtBindCharOffset = 0;
        n3 = this.ibtBindByteOffset;
        int n8 = this.ibtBindCharOffset;
        int n9 = this.ibtBindIndicatorOffset;
        int n10 = n9 + 6 + n4 * 8;
        this.ibtBindIndicators[n9++] = (short)(n4 >> 16);
        this.ibtBindIndicators[n9++] = (short)(n4 & 0xFFFF);
        this.ibtBindIndicators[n9++] = (short)(n6 >> 16);
        this.ibtBindIndicators[n9++] = (short)(n6 & 0xFFFF);
        this.ibtBindIndicators[n9++] = (short)(n7 >> 16);
        this.ibtBindIndicators[n9++] = (short)(n7 & 0xFFFF);
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            int n11;
            PlsqlIbtBindInfo plsqlIbtBindInfo;
            Binder binder = binderArray[i2];
            Accessor accessor = this.currentBatchBindAccessors == null ? null : this.currentBatchBindAccessors[i2];
            PlsqlIbtBindInfo plsqlIbtBindInfo3 = plsqlIbtBindInfo = accessor == null ? null : accessor.plsqlIndexTableBindInfo();
            if (binder.type == 998) {
                plsqlIbtBindInfo3 = plsqlIbtBindInfoArray[i2];
            }
            if (plsqlIbtBindInfo3 == null) continue;
            int n12 = plsqlIbtBindInfo3.maxLen;
            this.ibtBindIndicators[n9++] = (short)plsqlIbtBindInfo3.element_internal_type;
            this.ibtBindIndicators[n9++] = (short)plsqlIbtBindInfo3.elemMaxLen;
            this.ibtBindIndicators[n9++] = (short)(n12 >> 16);
            this.ibtBindIndicators[n9++] = (short)(n12 & 0xFFFF);
            this.ibtBindIndicators[n9++] = (short)(plsqlIbtBindInfo3.curLen >> 16);
            this.ibtBindIndicators[n9++] = (short)(plsqlIbtBindInfo3.curLen & 0xFFFF);
            if (plsqlIbtBindInfo3.ibtByteLength > 0) {
                n11 = n3;
                n3 += plsqlIbtBindInfo3.ibtByteLength;
            } else {
                n11 = n8;
                n8 += plsqlIbtBindInfo3.ibtCharLength;
            }
            this.ibtBindIndicators[n9++] = (short)(n11 >> 16);
            this.ibtBindIndicators[n9++] = (short)(n11 & 0xFFFF);
            plsqlIbtBindInfo3.ibtValueIndex = n11;
            plsqlIbtBindInfo3.ibtIndicatorIndex = n10;
            plsqlIbtBindInfo3.ibtLengthIndex = n10 + n12;
            if (plsqlIbtBindInfo != null) {
                if (plsqlIbtBindInfo != plsqlIbtBindInfo3) {
                    plsqlIbtBindInfo.ibtIndicatorIndex = plsqlIbtBindInfo3.ibtIndicatorIndex;
                    plsqlIbtBindInfo.ibtLengthIndex = plsqlIbtBindInfo3.ibtLengthIndex;
                    plsqlIbtBindInfo.ibtValueIndex = n11;
                }
                this.initializePlsqlIndexByTableAccessor(accessor, n9);
            }
            n10 += 2 * n12;
        }
    }

    void initializePlsqlIndexByTableAccessor(Accessor accessor, int n2) {
    }

    void initializeBindSubRanges(int n2, int n3) {
        this.bindByteSubRange = 0;
        this.bindCharSubRange = 0;
    }

    int calculateIndicatorSubRangeSize() {
        return 0;
    }

    short getInoutIndicator(int n2) {
        return 0;
    }

    @Override
    void initializeIndicatorSubRange() {
        this.bindIndicatorSubRange = this.calculateIndicatorSubRangeSize();
    }

    void prepareBindPreambles(int n2, int n3) {
    }

    protected void configureBindData() throws SQLException {
    }

    void setupBindBuffers(int n2, int n3) throws SQLException {
        int n4;
        assert (this.bindUseDBA) : "bindUseDBA is false for T4C driver.";
        if (this.lastBoundNeeded) {
            this.compressLastBoundData();
        } else {
            if (this.lastBoundDataLengths != null) {
                for (n4 = 0; n4 < this.lastBoundDataLengths.length; ++n4) {
                    this.lastBoundDataLengths[n4] = 0;
                    this.lastBoundDataOffsets[n4] = -1L;
                }
            }
            this.bindData.reset();
        }
        this.beyondBindData = this.bindData.getPosition();
        if (this.bindIndicators == null) {
            this.allocBinds(n3);
        }
        try {
            int n5;
            if (this.numberOfBindPositions == 0) {
                if (n3 != 0) {
                    this.setNumberOfBoundRows(n3);
                }
                return;
            }
            this.currentBatchNeedToPrepareBinds = false;
            this.setNumberOfBoundRows(n3);
            n4 = this.numberOfBoundRows;
            this.setupOutBindAccessors();
            int n6 = n5 = this.bindIndicatorSubRange + 5;
            n6 = this.setupBindMetaData(n2, n6);
            this.initializeBindSubRanges(this.numberOfBoundRows, n4);
            n6 = n5;
            if (this.hasIbtBind) {
                this.processPlsqlIndexTabBinds(n2);
            }
            if (this.numReturnParams > 0 && (this.accessors == null || this.accessors.length < this.numReturnParams)) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 173).fillInStackTrace();
            }
            if (this.numReturnParams > 0) {
                this.processDmlReturningBind();
            }
            this.localCheckSum = this.checkSum;
            n6 = this.doBindValueConversion(n2, n6);
            this.checkSum = this.localCheckSum;
            this.setLastBoundVals(n2);
            this.checkSum = this.localCheckSum;
            this.localCheckSum = 0L;
        }
        catch (NullPointerException nullPointerException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89, null, (Throwable)nullPointerException).fillInStackTrace();
        }
        finally {
            if (this.bindData != null) {
                this.beyondBindData = this.bindData.getPosition();
            }
        }
    }

    private void setNumberOfBoundRows(int n2) {
        this.numberOfBoundRows = n2;
        this.bindIndicators[this.bindIndicatorSubRange + 3] = (short)((this.numberOfBoundRows & 0xFFFF0000) >> 16);
        this.bindIndicators[this.bindIndicatorSubRange + 4] = (short)(this.numberOfBoundRows & 0xFFFF);
    }

    private void setupOutBindAccessors() throws SQLException {
        if (this.currentBatchBindAccessors != null) {
            if (this.outBindAccessors == null) {
                this.outBindAccessors = new Accessor[this.numberOfBindPositions];
            }
            for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
                Accessor accessor;
                this.outBindAccessors[i2] = accessor = this.currentBatchBindAccessors[i2];
                if (accessor == null) continue;
                int n2 = accessor.byteLength;
                int n3 = accessor.charLength;
                if (this.currentBatchByteLens[i2] < n2) {
                    this.currentBatchByteLens[i2] = n2;
                }
                if (n3 != 0 && this.currentBatchCharLens[i2] >= n3) continue;
                this.currentBatchCharLens[i2] = n3;
            }
        }
    }

    private int setupBindMetaData(int n2, int n3) throws SQLException {
        Binder[] binderArray = this.binders[n2];
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            short s2;
            Binder binder = binderArray[i2];
            int n4 = this.currentBatchByteLens[i2];
            int n5 = this.currentBatchCharLens[i2];
            if (binder == this.theOutBinder) {
                if (this.currentBatchBindAccessors != null) {
                    Accessor accessor = this.currentBatchBindAccessors[i2];
                    s2 = (short)accessor.defineType;
                } else {
                    s2 = binder.type;
                }
            } else {
                s2 = binder.type;
            }
            this.bindIndicators[n3 + 0] = s2;
            this.bindIndicators[n3 + 1] = (short)n4;
            this.bindIndicators[n3 + 2] = (short)n5;
            this.bindIndicators[n3 + 9] = this.currentBatchFormOfUse[i2];
            n3 += 10;
        }
        return n3;
    }

    private int doBindValueConversion(int n2, int n3) throws SQLException {
        int n4 = this.indicatorsOffset;
        int n5 = this.valueLengthsOffset;
        int n6 = this.numberOfBoundRows - 1;
        int n7 = n6 + n2;
        Binder[] binderArray = this.binders[n7];
        boolean bl = !this.sqlKind.isPlsqlOrCall() || this.currentRowBindAccessors == null;
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            int n8;
            short s2 = this.currentBatchFormOfUse[i2];
            this.lastBinders[i2] = binderArray[i2];
            this.lastBoundByteLens[i2] = this.currentBatchByteLens[i2];
            for (n8 = 0; n8 < this.numberOfBoundRows; ++n8) {
                int n9 = n2 + n8;
                int n10 = n8 * this.numberOfBindPositions + i2;
                this.localCheckSum = this.binders[n9][i2].bind(this, i2, n8, n9, this.bindBytes, this.bindChars, this.bindIndicators, 0, 0, 0, 0, n5 + n8, n4 + n8, bl, this.localCheckSum, this.bindData, this.bindDataOffsets, this.bindDataLengths, n10, this.bindUseDBA, s2);
                this.binders[n9][i2] = null;
                if (this.userStream == null) continue;
                this.userStream[n8][i2] = null;
                continue;
            }
            if (this.bindChecksumListener != null) {
                n8 = this.bindChecksumListener.shouldContinue(this.checkSum) ? 1 : 0;
                this.bindChecksumListener = null;
                if (n8 == 0) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 290).fillInStackTrace();
                }
            }
            this.lastBoundInds[i2] = this.bindIndicators[n4 + n6];
            this.lastBoundLens[i2] = this.bindIndicators[n5 + n6];
            this.lastBoundByteLens[i2] = 0;
            this.lastBoundCharLens[i2] = 0;
            n4 += this.numberOfBindRowsAllocated;
            n5 += this.numberOfBindRowsAllocated;
            n3 += 10;
        }
        return n3;
    }

    private void setLastBoundVals(int n2) {
        this.lastBoundBytes = this.bindBytes;
        this.lastBoundByteOffset = this.bindByteOffset;
        this.lastBoundChars = this.bindChars;
        this.lastBoundCharOffset = this.bindCharOffset;
        if (this.parameterStream != null) {
            this.lastBoundStream = this.parameterStream[n2 + this.numberOfBoundRows - 1];
        }
        int[] nArray = this.currentBatchByteLens;
        this.currentBatchByteLens = this.lastBoundByteLens;
        this.lastBoundByteLens = nArray;
        int[] nArray2 = this.currentBatchCharLens;
        this.currentBatchCharLens = this.lastBoundCharLens;
        this.lastBoundCharLens = nArray2;
        this.lastBoundNeeded = false;
    }

    @Override
    void releaseBuffers() {
        super.releaseBuffers();
        this.parameterStream = null;
        this.connection.cacheBuffer(this.bindBytes);
        this.bindBytes = null;
        this.totalBindByteLength = 0;
        this.connection.cacheBuffer(this.bindChars);
        this.bindChars = null;
        this.totalBindCharLength = 0;
        this.bindIndicators = null;
        this.totalBindIndicatorLength = 0;
        this.bindBufferCapacity = 0;
        this.numberOfBindRowsAllocated = 1;
        this.lastBoundStream = null;
        this.userStream = null;
        try {
            this.clearParametersCritical();
        }
        catch (SQLException sQLException) {
            // empty catch block
        }
    }

    @Override
    public void enterImplicitCache() throws SQLException {
        this.alwaysOnClose();
        if (!this.connection.isClosed()) {
            this.cleanAllTempLobs();
        }
        if (this.connection.clearStatementMetaData) {
            this.lastBoundBytes = null;
            this.lastBoundChars = null;
        }
        this.cacheState = 2;
        this.creationState = 1;
        this.currentResultSet = null;
        this.lastIndex = 0;
        this.queryTimeout = 0;
        this.rowPrefetchChanged = false;
        this.currentRank = 0;
        this.currentBatchAccumulatedBindsSize = 0;
        this.validRows = 0L;
        this.maxRows = 0L;
        this.maxFieldSize = 0;
        this.gotLastBatch = false;
        this.clearParameters = true;
        this.defaultTimeZone = null;
        this.defaultCalendar = null;
        this.checkSum = 0L;
        this.checkSumComputationFailure = false;
        if (this.sqlKind.isOTHER()) {
            this.needToParse = true;
            this.needToPrepareDefineBuffer = true;
            this.columnsDefinedByUser = false;
        }
        this.releaseBuffers();
        this.definedColumnType = null;
        this.definedColumnSize = null;
        this.definedColumnFormOfUse = null;
        if (this.accessors != null) {
            int n2 = this.accessors.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this.accessors[i2] == null) continue;
                this.accessors[i2].rowSpaceIndicator = null;
                if (!this.columnsDefinedByUser) continue;
                this.accessors[i2].externalType = 0;
            }
        }
        this.fixedString = this.connection.getDefaultFixedString();
        this.defaultRowPrefetch = this.rowPrefetch;
        this.rowPrefetchInLastFetch = -1;
        if (this.connection.clearStatementMetaData) {
            this.needToParse = true;
            this.needToPrepareDefineBuffer = true;
            this.columnsDefinedByUser = false;
            if (this.userRsetType == OracleResultSet.ResultSetType.UNKNOWN) {
                this.userRsetType = DEFAULT_RESULT_SET_TYPE;
                this.realRsetType = OracleResultSet.ResultSetType.FORWARD_READ_ONLY;
            }
            this.currentRowNeedToPrepareBinds = true;
        }
    }

    @Override
    public void enterExplicitCache() throws SQLException {
        this.cacheState = 2;
        this.creationState = 2;
        this.defaultTimeZone = null;
        this.alwaysOnClose();
    }

    @Override
    public void exitImplicitCacheToActive() throws SQLException {
        this.cacheState = 1;
        this.closed = false;
        if (this.rowPrefetch != this.connection.getDefaultRowPrefetch() && this.streamList == null) {
            this.defaultRowPrefetch = this.rowPrefetch = this.connection.getDefaultRowPrefetch();
            this.rowPrefetchChanged = true;
        }
        if (this.batch != this.connection.getDefaultExecuteBatch()) {
            this.resetBatch();
        }
        this.processEscapes = this.connection.processEscapes;
        if (this.accessors != null) {
            this.doInitializationAfterDefineBufferRestore();
        }
        if (this.cachedBindCharSize != 0 || this.cachedBindByteSize != 0) {
            if (this.cachedBindByteSize > 0) {
                this.bindBytes = this.connection.getByteBuffer(this.cachedBindByteSize);
            }
            if (this.cachedBindCharSize > 0) {
                this.bindChars = this.connection.getCharBuffer(this.cachedBindCharSize);
            }
            this.doLocalInitialization();
        }
    }

    void doLocalInitialization() {
    }

    void doInitializationAfterDefineBufferRestore() {
    }

    @Override
    public void exitExplicitCacheToActive() throws SQLException {
        this.cacheState = 1;
        this.closed = false;
    }

    @Override
    public void exitImplicitCacheToClose() throws SQLException {
        this.cacheState = 0;
        this.closed = false;
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.hardClose();
        }
    }

    @Override
    public void exitExplicitCacheToClose() throws SQLException {
        this.cacheState = 0;
        this.closed = false;
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.hardClose();
        }
    }

    @Override
    public void closeWithKey(String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.closeOrCache(string);
        }
    }

    @Override
    protected boolean isQueryResultCached() throws SQLException {
        assert (this.sqlKind.isSELECT()) : "sqlKind.isSELECT() == false";
        switch (this.queryCacheState) {
            case UNKNOWN: {
                return false;
            }
            case CACHEABLE: {
                this.getCachedQueryResult();
                return this.cachedQueryResult != null && this.cachedQueryResult.isValid();
            }
            case UNCACHEABLE: {
                return false;
            }
        }
        return false;
    }

    @Override
    protected void cacheQueryResultIfAppropriate() throws SQLException {
        switch (this.queryCacheState) {
            case UNKNOWN: {
                if (this.getCompileKey() == null) {
                    this.queryCacheState = OracleStatement.QueryCacheState.UNCACHEABLE;
                    return;
                }
                this.queryCacheState = OracleStatement.QueryCacheState.CACHEABLE;
            }
            case CACHEABLE: {
                if (this.cachedQueryResult == null || this.cachedQueryResult.isInvalid()) {
                    this.getCachedQueryResult();
                }
                if (this.cachedQueryResult == null) break;
                if (this.cachedQueryResult.isFetching()) {
                    this.fetchMode = OracleStatement.FetchMode.APPEND;
                    break;
                }
                this.useCachedQueryResult();
                break;
            }
        }
    }

    void getCachedQueryResult() throws SQLException {
        this.cachedQueryResult = this.connection.getResultSetCacheInternal().getResultSetCacheEntry(this);
    }

    long executeInternal() throws SQLException {
        this.prepareForExecuteInternal();
        this.initializeAutoKeyInfo();
        this.processCompletedBindRow(this.sqlKind.isSELECT() ? 1 : this.batch, false);
        try {
            if (!this.isResultSetRegular() && !this.scrollRsetTypeSolved) {
                long l2 = this.doScrollPstmtExecuteUpdate() + this.prematureBatchCount;
                return l2;
            }
            if (this.currentRowNeedToPrepareBinds) {
                this.runtimeKey = null;
            }
            this.doExecuteWithTimeout();
        }
        finally {
            this.resetBindersToNull(0);
        }
        this.handleExecuteInternalCompletion();
        return this.validRows;
    }

    private final boolean isResultSetRegular() {
        return this.userRsetType == DEFAULT_RESULT_SET_TYPE;
    }

    private final void prepareForExecuteInternal() throws SQLException {
        if (this.connection.isDRCPEnabled()) {
            this.prepareForExecuteWithDRCP();
        }
        this.noMoreUpdateCounts = false;
        this.checkSum = 0L;
        this.checkSumComputationFailure = false;
        this.ensureOpen();
        if (this.currentRank > 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 81, "batch must be either executed or cleared").fillInStackTrace();
        }
        this.prepareForNewResults(true, false, true);
        if (this.isCloseOnCompletion) {
            this.ensureOpen();
        }
    }

    protected void prepareForExecuteWithDRCP() throws SQLException {
    }

    private final void handleExecuteInternalCompletion() throws SQLException {
        boolean bl;
        boolean bl2 = bl = this.prematureBatchCount != 0L && this.validRows > 0L;
        if (!this.isResultSetRegular()) {
            this.currentResultSet = this.createResultSet();
            if (!this.connection.accumulateBatchResult) {
                bl = false;
            }
        }
        if (bl) {
            this.validRows += this.prematureBatchCount;
            this.prematureBatchCount = 0L;
        }
        if (this.sqlKind.isOTHER()) {
            this.needToParse = true;
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.prepareForExecuteQuery();
            this.executeInternal();
            this.handleExecuteQueryCompletion();
            OracleResultSet oracleResultSet = this.currentResultSet;
            return oracleResultSet;
        }
    }

    private final void prepareForExecuteQuery() throws SQLException {
        this.executeDoneForDefines = true;
        this.executionType = 1;
    }

    private final void handleExecuteQueryCompletion() throws SQLException {
        if (this.currentResultSet == null) {
            if (this.validRows < 1L && this.validRows != -2L) {
                this.isAllFetched = true;
            }
            this.currentResultSet = this.createResultSet();
        } else {
            this.computeOffsetOfFirstUserColumn();
            this.computeNumberOfUserColumns();
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        return (int)this.executeLargeUpdate();
    }

    @Override
    public long executeLargeUpdate() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.executionType = (byte)2;
            long l2 = this.executeInternal();
            return l2;
        }
    }

    @Override
    public boolean execute() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.prepareForExecute();
            this.executeInternal();
            boolean bl = this.sqlKind.isSELECT();
            return bl;
        }
    }

    private final void prepareForExecute() throws SQLException {
        this.executeDoneForDefines = true;
        this.executionType = (byte)3;
    }

    void slideDownCurrentRow(int n2) {
        Object[] objectArray;
        if (this.binders != null) {
            this.binders[n2] = this.binders[0];
            this.binders[0] = this.currentRowBinders;
        }
        if (this.parameterStream != null) {
            objectArray = this.parameterStream[0];
            this.parameterStream[0] = this.parameterStream[n2];
            this.parameterStream[n2] = objectArray;
        }
        if (this.userStream != null) {
            objectArray = this.userStream[0];
            this.userStream[0] = this.userStream[n2];
            this.userStream[n2] = objectArray;
        }
    }

    void resetBatch() {
        this.batch = this.connection.getDefaultExecuteBatch();
    }

    @Override
    public int sendBatch() throws SQLException {
        return 0;
    }

    @Override
    public void setExecuteBatch(int n2) throws SQLException {
    }

    void set_execute_batch(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (n2 <= 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 42).fillInStackTrace();
            }
            if (n2 == this.batch) {
                return;
            }
            if (this.currentRank > 0) {
                long l2 = this.validRows;
                this.prematureBatchCount = this.sendBatch();
                this.validRows = l2;
            }
            int n3 = this.batch;
            this.batch = n2;
            if (this.numberOfBindRowsAllocated < this.batch) {
                this.growBinds(this.batch);
            }
        }
    }

    @Override
    public final int getExecuteBatch() {
        return 1;
    }

    @Override
    public void defineParameterTypeBytes(int n2, int n3, int n4) throws SQLException {
        if (n4 < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 53).fillInStackTrace();
        }
        int n5 = n2 - 1;
        if (n5 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(3).fillInStackTrace();
        }
        if (this.parameterMaxLength == null) {
            this.parameterMaxLength = new int[this.numberOfBindPositions];
        }
        this.parameterMaxLength[n5] = n4;
    }

    @Override
    public void defineParameterTypeChars(int n2, int n3, int n4) throws SQLException {
        int n5 = this.connection.getNlsRatio();
        if (n3 == 1 || n3 == 12) {
            this.defineParameterTypeBytes(n2, n3, n4 * n5);
        } else {
            this.defineParameterTypeBytes(n2, n3, n4);
        }
    }

    @Override
    public void defineParameterType(int n2, int n3, int n4) throws SQLException {
        this.defineParameterTypeBytes(n2, n3, n4);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        this.ensureOpen();
        if (this.sqlKind.isSELECT()) {
            return this.getResultSetMetaData();
        }
        return null;
    }

    @Override
    public void setNull(int n2, int n3, String string) throws SQLException {
        this.setNullInternal(n2, n3, string);
    }

    void setNullInternal(int n2, int n3, String string) throws SQLException {
        int n4 = n2 - 1;
        if (n4 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (n3 == 2002 || n3 == 2008 || n3 == 2003 || n3 == 2007 || n3 == 2009 || n3 == 2006) {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                this.setNullCritical(n4, n3, string);
            }
        } else {
            this.setNullInternal(n2, n3);
            return;
        }
    }

    void setNullInternal(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setNullCritical(n2, n3);
        }
    }

    void setNullCritical(int n2, int n3, String string) throws SQLException {
        Object object;
        OracleTypeADT oracleTypeADT = null;
        switch (n3) {
            case 2002: 
            case 2006: 
            case 2008: {
                object = StructDescriptor.createDescriptor(string, (Connection)this.connection);
                oracleTypeADT = ((StructDescriptor)object).getOracleTypeADT();
                break;
            }
            case 2003: {
                object = ArrayDescriptor.createDescriptor(string, (Connection)this.connection);
                oracleTypeADT = ((ArrayDescriptor)object).getOracleTypeCOLLECTION();
                break;
            }
            case 2007: 
            case 2009: {
                object = OpaqueDescriptor.createDescriptor(string, (Connection)this.connection);
                oracleTypeADT = (OracleTypeADT)((TypeDescriptor)object).getPickler();
                break;
            }
        }
        if (oracleTypeADT != null) {
            oracleTypeADT.getTOID();
        }
        this.currentRowBinders[n2] = object = n3 == 2006 ? new RefTypeNullBinder(oracleTypeADT) : new NamedTypeNullBinder(oracleTypeADT);
        this.currentRowByteLens[n2] = ((Binder)object).bytelen;
        this.currentRowCharLens[n2] = 0;
    }

    @Override
    public void setNullAtName(String string, int n2, String string2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string3 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n3 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n3; ++i2) {
            if (stringArray[i2] != string3) continue;
            this.setNullInternal(i2 + 1, n2, string2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setNull(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setNullCritical(n2, n3);
        }
    }

    void setNullCritical(int n2, int n3) throws SQLException {
        int n4 = n2 - 1;
        if (n4 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        Binder binder = null;
        int n5 = this.getInternalType(n3);
        boolean bl = true;
        switch (n5) {
            case 252: {
                if (this.connection.databaseMetaData.getDatabaseMajorVersion() < 12 || this.connection.databaseMetaData.getDatabaseMajorVersion() == 12 && this.connection.databaseMetaData.getDatabaseMinorVersion() < 1) {
                    throw (SQLException)DatabaseError.createSqlException(299).fillInStackTrace();
                }
                binder = new PlsqlBooleanNullBinder();
                break;
            }
            case 6: {
                binder = new VarnumNullBinder();
                break;
            }
            case 1: 
            case 8: 
            case 96: 
            case 995: {
                binder = new VarcharNullBinder();
                bl = false;
                break;
            }
            case 999: {
                binder = new FixedCHARNullBinder();
                bl = false;
                break;
            }
            case 12: {
                binder = new DateNullBinder();
                break;
            }
            case 180: {
                binder = new TimestampNullBinder();
                break;
            }
            case 181: {
                binder = new TSTZNullBinder();
                break;
            }
            case 231: {
                binder = new TSLTZNullBinder();
                break;
            }
            case 104: {
                binder = this.getRowidNullBinder(n4);
                break;
            }
            case 183: {
                binder = new IntervalDSNullBinder();
                break;
            }
            case 182: {
                binder = new IntervalYMNullBinder();
                break;
            }
            case 23: 
            case 24: {
                binder = new RawNullBinder();
                break;
            }
            case 100: {
                binder = new BinaryFloatNullBinder();
                break;
            }
            case 101: {
                binder = new BinaryDoubleNullBinder();
                break;
            }
            case 113: {
                binder = new BlobNullBinder();
                break;
            }
            case 119: {
                binder = new JsonNullBinder();
                break;
            }
            case 112: {
                binder = new ClobNullBinder();
                break;
            }
            case 114: {
                binder = new BfileNullBinder();
                break;
            }
            case 109: 
            case 111: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "Use setNull(int parameterIndex, int sqlType, String typeName) for user-defined types and REF types").fillInStackTrace();
            }
            case 102: {
                binder = new RefCursorNullBinder();
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23, "sqlType=" + n3).fillInStackTrace();
            }
        }
        this.currentRowBinders[n4] = binder;
        if (bl) {
            this.currentRowByteLens[n4] = binder.bytelen;
            this.currentRowCharLens[n4] = 0;
        } else {
            this.currentRowByteLens[n4] = 0;
            this.currentRowCharLens[n4] = this.sqlKind.isPlsqlOrCall() ? this.connection.minVcsBindSize : 1;
        }
    }

    Binder getRowidNullBinder(int n2) throws SQLException {
        return this.createRowidNullBinder();
    }

    @Override
    public void setNullAtName(String string, int n2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n3 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n3; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setNull(i2 + 1, n2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBoolean(int n2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBooleanInternal(n2, bl);
        }
    }

    void setBooleanInternal(int n2, boolean bl) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = new BooleanBinder(bl ? 1 : 0);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    public void setPlsqlBoolean(int n2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setPlsqlBooleanInternal(n2, bl);
        }
    }

    void setPlsqlBooleanInternal(int n2, boolean bl) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = new PlsqlBooleanBinder(bl ? 1 : 0);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setByte(int n2, byte by) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setByteInternal(n2, by);
        }
    }

    void setByteInternal(int n2, byte by) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = new ByteBinder(by);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setShort(int n2, short s2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setShortInternal(n2, s2);
        }
    }

    void setShortInternal(int n2, short s2) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = new ShortBinder(s2);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setInt(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setIntInternal(n2, n3);
        }
    }

    void setIntInternal(int n2, int n3) throws SQLException {
        int n4 = n2 - 1;
        if (n4 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n4] = new IntBinder(n3);
        this.currentRowByteLens[n4] = this.currentRowBinders[n4].bytelen;
        this.currentRowCharLens[n4] = 0;
    }

    void setRefCursorInternal(int n2, int n3) throws SQLException {
        int n4 = n2 - 1;
        if (n4 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n4] = new RefCursorBinder(n3);
        this.currentRowByteLens[n4] = this.currentRowBinders[n4].bytelen;
        this.currentRowCharLens[n4] = 0;
    }

    @Override
    public void setLong(int n2, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setLongInternal(n2, l2);
        }
    }

    void setLongInternal(int n2, long l2) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = new LongBinder(l2);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setFloat(int n2, float f2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setFloatInternal(n2, f2);
        }
    }

    void setFloatInternal(int n2, float f2) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (!this.connection.setFloatAndDoubleUseBinary && Float.isNaN(f2)) {
            throw new IllegalArgumentException("NaN");
        }
        this.currentRowBinders[n3] = this.connection.setFloatAndDoubleUseBinary ? new BinaryFloatBinder(f2) : new FloatBinder(f2);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setBinaryFloat(int n2, float f2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBinaryFloatInternal(n2, f2);
        }
    }

    void setBinaryFloatInternal(int n2, float f2) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = new BinaryFloatBinder(f2);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setBinaryFloat(int n2, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBinaryFloatInternal(n2, bINARY_FLOAT);
        }
    }

    void setBinaryFloatInternal(int n2, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = bINARY_FLOAT == null ? new BINARY_FLOATNullBinder() : new BINARY_FLOATBinder(bINARY_FLOAT.getBytes());
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setBinaryDouble(int n2, double d2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBinaryDoubleInternal(n2, d2);
        }
    }

    void setBinaryDoubleInternal(int n2, double d2) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = new BinaryDoubleBinder(d2);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setBinaryDouble(int n2, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBinaryDoubleInternal(n2, bINARY_DOUBLE);
        }
    }

    void setBinaryDoubleInternal(int n2, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = bINARY_DOUBLE == null ? new BINARY_DOUBLENullBinder() : new BINARY_DOUBLEBinder(bINARY_DOUBLE.getBytes());
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setDouble(int n2, double d2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setDoubleInternal(n2, d2);
        }
    }

    void setDoubleInternal(int n2, double d2) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (!this.connection.setFloatAndDoubleUseBinary) {
            if (Double.isNaN(d2)) {
                throw new IllegalArgumentException("NaN");
            }
            double d3 = Math.abs(d2);
            if (d3 != 0.0 && d3 < 1.0E-130) {
                throw new IllegalArgumentException("Underflow");
            }
            if (d3 >= 1.0E126) {
                throw new IllegalArgumentException("Overflow");
            }
        }
        this.currentRowBinders[n3] = this.connection.setFloatAndDoubleUseBinary ? new BinaryDoubleBinder(d2) : new DoubleBinder(d2);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setBigDecimal(int n2, BigDecimal bigDecimal) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBigDecimalInternal(n2, bigDecimal);
        }
    }

    void setBigDecimalInternal(int n2, BigDecimal bigDecimal) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = bigDecimal == null ? new VarnumNullBinder() : new BigDecimalBinder(bigDecimal);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setString(int n2, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setFormOfUseInternal(n2, this.defaultFormOfUse);
            this.setStringInternal(n2, string);
        }
    }

    void setStringInternal(int n2, String string) throws SQLException {
        int n3;
        int n4 = n2 - 1;
        if (n4 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        int n5 = n3 = string != null ? string.length() : 0;
        if (n3 == 0) {
            this.setNull(n2, 12);
        } else if (this.currentRowFormOfUse[n2 - 1] == 1) {
            if (this.sqlKind.isPlsqlOrCall()) {
                if (n3 > this.maxVcsBytesPlsql || n3 > this.maxVcsCharsPlsql && this.isServerCharSetFixedWidth) {
                    this.setStringForClobCritical(n2, string);
                } else if (n3 > this.maxVcsCharsPlsql) {
                    int n6 = this.connection.conversion.encodedByteLength(string, false);
                    if (n6 > this.maxVcsBytesPlsql) {
                        this.setStringForClobCritical(n2, string);
                    } else {
                        this.basicBindString(n2, string);
                    }
                } else {
                    this.basicBindString(n2, string);
                }
            } else if (n3 <= this.maxVcsCharsSql) {
                this.basicBindString(n2, string);
            } else if (n3 <= this.maxStreamCharsSql) {
                this.basicBindCharacterStream(n2, new StringReader(string), n3, true);
            } else {
                this.setStringForClobCritical(n2, string);
            }
        } else if (this.sqlKind.isPlsqlOrCall()) {
            if (n3 > this.maxVcsBytesPlsql || n3 > this.maxVcsNCharsPlsql && this.isServerNCharSetFixedWidth) {
                this.setStringForClobCritical(n2, string);
            } else if (n3 > this.maxVcsNCharsPlsql) {
                int n7 = this.connection.conversion.encodedByteLength(string, true);
                if (n7 > this.maxVcsBytesPlsql) {
                    this.setStringForClobCritical(n2, string);
                } else {
                    this.basicBindString(n2, string);
                }
            } else {
                this.basicBindString(n2, string);
            }
        } else if (n3 <= this.maxVcsCharsSql) {
            this.basicBindString(n2, string);
        } else {
            this.setStringForClobCritical(n2, string);
        }
    }

    void basicBindNullString(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n3 = n2 - 1;
            this.currentRowBinders[n3] = new VarcharNullBinder();
            this.currentRowCharLens[n3] = this.sqlKind.isPlsqlOrCall() ? this.minVcsBindSize : 1;
            this.currentRowByteLens[n3] = 0;
        }
    }

    protected Binder createStringBinder(String string) {
        return new StringBinder(string);
    }

    void basicBindString(int n2, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n3 = n2 - 1;
            this.currentRowBinders[n3] = this.createStringBinder(string);
            int n4 = string.length();
            if (this.sqlKind.isPlsqlOrCall()) {
                int n5 = this.maxFieldSize > 0 && this.maxFieldSize < this.connection.minVcsBindSize ? this.maxFieldSize : this.connection.minVcsBindSize;
                int n6 = n4 + 1;
                this.currentRowCharLens[n3] = n6 < n5 ? n5 : n6;
            } else {
                this.currentRowCharLens[n3] = n4 + 1;
            }
            this.currentRowByteLens[n3] = 0;
        }
    }

    @Override
    public void setStringForClob(int n2, String string) throws SQLException {
        if (string == null) {
            this.setNull(n2, 1);
            return;
        }
        int n3 = string.length();
        if (n3 == 0) {
            this.setNull(n2, 1);
            return;
        }
        if (this.sqlKind.isPlsqlOrCall()) {
            if (n3 <= this.maxVcsCharsPlsql) {
                this.setStringInternal(n2, string);
            } else {
                this.setStringForClobCritical(n2, string);
            }
        } else if (n3 <= this.maxVcsCharsSql) {
            this.setStringInternal(n2, string);
        } else {
            this.setStringForClobCritical(n2, string);
        }
    }

    void setStringForClobCritical(int n2, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            CLOB cLOB = CLOB.createTemporary(this.connection, true, 10, this.currentRowFormOfUse[n2 - 1]);
            cLOB.setString(1L, string);
            this.addToTempLobsToFree(cLOB);
            this.lastBoundClobs[n2 - 1] = cLOB;
            this.setCLOBInternal(n2, cLOB);
        }
    }

    /*
     * Loose catch block
     */
    void setReaderContentsForClobCritical(int n2, Reader reader, long l2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            block23: {
                reader = this.isReaderEmpty(reader);
                if (reader == null) {
                    if (bl) {
                        throw new SQLException(l2 + " char of CLOB data cannot be read");
                    }
                    this.setCLOBInternal(n2, null);
                    return;
                }
                break block23;
                catch (IOException iOException) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
                }
            }
            CLOB cLOB = CLOB.createTemporary(this.connection, true, 10, this.currentRowFormOfUse[n2 - 1]);
            OracleClobWriter oracleClobWriter = (OracleClobWriter)cLOB.setCharacterStream(1L);
            int n3 = cLOB.getBufferSize();
            char[] cArray = new char[n3];
            long l3 = 0L;
            int n4 = 0;
            try {
                for (l3 = bl ? l2 : Long.MAX_VALUE; l3 > 0L; l3 -= (long)n4) {
                    n4 = l3 >= (long)n3 ? reader.read(cArray) : reader.read(cArray, 0, (int)l3);
                    if (n4 == -1) {
                        if (!bl) break;
                        throw new SQLException(l3 + " char of CLOB data cannot be read");
                    }
                    oracleClobWriter.write(cArray, 0, n4);
                }
                oracleClobWriter.flush();
            }
            catch (IOException iOException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
            }
            this.addToTempLobsToFree(cLOB);
            this.lastBoundClobs[n2 - 1] = cLOB;
            this.setCLOBInternal(n2, cLOB);
        }
    }

    /*
     * Loose catch block
     */
    void setAsciiStreamContentsForClobCritical(int n2, InputStream inputStream, long l2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            block23: {
                inputStream = this.isInputStreamEmpty(inputStream);
                if (inputStream == null) {
                    if (bl) {
                        throw new SQLException(l2 + " byte of CLOB data cannot be read");
                    }
                    this.setCLOBInternal(n2, null);
                    return;
                }
                break block23;
                catch (IOException iOException) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
                }
            }
            CLOB cLOB = CLOB.createTemporary(this.connection, true, 10, this.currentRowFormOfUse[n2 - 1]);
            OracleClobWriter oracleClobWriter = (OracleClobWriter)cLOB.setCharacterStream(1L);
            int n3 = cLOB.getBufferSize();
            byte[] byArray = new byte[n3];
            char[] cArray = new char[n3];
            int n4 = 0;
            try {
                for (long i2 = bl ? l2 : Long.MAX_VALUE; i2 > 0L; i2 -= (long)n4) {
                    n4 = i2 >= (long)n3 ? inputStream.read(byArray) : inputStream.read(byArray, 0, (int)i2);
                    if (n4 == -1) {
                        if (!bl) break;
                        throw new SQLException(i2 + " byte of CLOB data cannot be read");
                    }
                    DBConversion.asciiBytesToJavaChars(byArray, n4, cArray);
                    oracleClobWriter.write(cArray, 0, n4);
                }
                oracleClobWriter.flush();
            }
            catch (IOException iOException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
            }
            this.addToTempLobsToFree(cLOB);
            this.lastBoundClobs[n2 - 1] = cLOB;
            this.setCLOBInternal(n2, cLOB);
        }
    }

    @Override
    public void setStringForClobAtName(String string, String string2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string3 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string3) continue;
            this.setStringForClob(i2 + 1, string2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setFixedCHAR(int n2, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setFixedCHARInternal(n2, string);
        }
    }

    protected Binder createFixedCHARBinder(String string) {
        return new FixedCHARBinder(string);
    }

    void setFixedCHARInternal(int n2, String string) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        int n4 = 0;
        if (string != null) {
            n4 = string.length();
        }
        if (n4 > 32766) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 157).fillInStackTrace();
        }
        if (string == null) {
            this.currentRowBinders[n3] = new FixedCHARNullBinder();
            this.currentRowCharLens[n3] = 1;
        } else {
            this.currentRowBinders[n3] = this.createFixedCHARBinder(string);
            this.currentRowCharLens[n3] = n4 + 1;
        }
        this.currentRowByteLens[n3] = 0;
    }

    @Override
    @Deprecated
    public void setCursor(int n2, ResultSet resultSet) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setCursorInternal(n2, resultSet);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    void setCursorInternal(int n2, ResultSet resultSet) throws SQLException {
        if (resultSet == null) {
            this.setNullInternal(n2, -10);
            return;
        } else {
            if (!(resultSet instanceof OracleResultSet)) throw (SQLException)DatabaseError.createSqlException(18).fillInStackTrace();
            PhysicalConnection physicalConnection = ((OracleResultSet)resultSet).connection;
            PhysicalConnection physicalConnection2 = this.connection;
            if ((physicalConnection != null || physicalConnection2 != null) && physicalConnection != physicalConnection2 && !physicalConnection.equals(physicalConnection2)) throw (SQLException)DatabaseError.createSqlException(18).fillInStackTrace();
            this.setRefCursorInternal(n2, ((OracleResultSet)resultSet).getCursorId());
        }
    }

    @Override
    public void setROWID(int n2, ROWID rOWID) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setROWIDInternal(n2, rOWID);
        }
    }

    protected Binder createRowidBinder(byte[] byArray) throws SQLException {
        return this.connection.useLittleEndianSetCHARBinder() ? new LittleEndianRowidBinder(byArray) : new RowidBinder(byArray);
    }

    protected Binder createURowidBinder(byte[] byArray) throws SQLException {
        return this.createRowidBinder(byArray);
    }

    protected Binder createRowidNullBinder() throws SQLException {
        return new RowidNullBinder();
    }

    protected Binder createURowidNullBinder() throws SQLException {
        return this.createRowidNullBinder();
    }

    void setROWIDInternal(int n2, ROWID rOWID) throws SQLException {
        if (this.sqlKind == OracleStatement.SqlKind.CALL_BLOCK) {
            if (rOWID == null) {
                this.setNull(n2, 12);
            } else {
                this.setStringInternal(n2, rOWID.stringValue());
            }
            return;
        }
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (rOWID == null || rOWID.shareBytes() == null) {
            this.currentRowBinders[n3] = this.createRowidNullBinder();
            this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        } else {
            byte[] byArray = rOWID.getBytes();
            this.currentRowBinders[n3] = T4CRowidAccessor.isUROWID(rOWID.shareBytes(), 0) ? this.createURowidBinder(byArray) : this.createRowidBinder(byArray);
            this.currentRowByteLens[n3] = byArray.length + 2;
        }
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setArray(int n2, java.sql.Array array) throws SQLException {
        this.setARRAYInternal(n2, (ARRAY)array);
    }

    void setArrayInternal(int n2, java.sql.Array array) throws SQLException {
        this.setARRAYInternal(n2, (ARRAY)array);
    }

    @Override
    public void setARRAY(int n2, ARRAY aRRAY) throws SQLException {
        this.setARRAYInternal(n2, aRRAY);
    }

    void setARRAYInternal(int n2, ARRAY aRRAY) throws SQLException {
        PhysicalConnection physicalConnection;
        PhysicalConnection physicalConnection2;
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (aRRAY == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        PhysicalConnection physicalConnection3 = (PhysicalConnection)aRRAY.getPhysicalConnection();
        if (physicalConnection3 == null || this.connection == physicalConnection3) {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                this.setArrayCritical(n3, aRRAY);
                this.currentRowCharLens[n3] = 0;
            }
        }
        if (this.connection.hashCode() < physicalConnection3.hashCode()) {
            physicalConnection2 = this.connection;
            physicalConnection = physicalConnection3;
        } else {
            physicalConnection2 = physicalConnection3;
            physicalConnection = this.connection;
        }
        try (Monitor.CloseableLock closeableLock = physicalConnection2.acquireCloseableLock();
             Monitor.CloseableLock closeableLock2 = physicalConnection.acquireCloseableLock();){
            this.setArrayCritical(n3, aRRAY);
            this.currentRowCharLens[n3] = 0;
        }
    }

    void setArrayCritical(int n2, ARRAY aRRAY) throws SQLException {
        ArrayDescriptor arrayDescriptor = aRRAY.getDescriptor();
        if (arrayDescriptor == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 61).fillInStackTrace();
        }
        OracleTypeCOLLECTION oracleTypeCOLLECTION = arrayDescriptor.getOracleTypeCOLLECTION();
        oracleTypeCOLLECTION.getTOID();
        this.currentRowBinders[n2] = new NamedTypeBinder(aRRAY.toBytes(), oracleTypeCOLLECTION);
        this.currentRowByteLens[n2] = this.currentRowBinders[n2].bytelen;
        this.currentRowCharLens[n2] = 0;
    }

    @Override
    public void setOPAQUE(int n2, OPAQUE oPAQUE) throws SQLException {
        this.setOPAQUEInternal(n2, oPAQUE);
    }

    void setOPAQUEInternal(int n2, OPAQUE oPAQUE) throws SQLException {
        PhysicalConnection physicalConnection;
        PhysicalConnection physicalConnection2;
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (oPAQUE == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        PhysicalConnection physicalConnection3 = (PhysicalConnection)oPAQUE.getPhysicalConnection();
        if (physicalConnection3 == null || this.connection == physicalConnection3) {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                this.setOPAQUECritical(n3, oPAQUE);
                this.currentRowCharLens[n3] = 0;
            }
        }
        if (this.connection.hashCode() < physicalConnection3.hashCode()) {
            physicalConnection2 = this.connection;
            physicalConnection = physicalConnection3;
        } else {
            physicalConnection2 = physicalConnection3;
            physicalConnection = this.connection;
        }
        try (Monitor.CloseableLock closeableLock = physicalConnection2.acquireCloseableLock();
             Monitor.CloseableLock closeableLock2 = physicalConnection.acquireCloseableLock();){
            this.setOPAQUECritical(n3, oPAQUE);
            this.currentRowCharLens[n3] = 0;
        }
    }

    void setOPAQUECritical(int n2, OPAQUE oPAQUE) throws SQLException {
        OpaqueDescriptor opaqueDescriptor = oPAQUE.getDescriptor();
        if (opaqueDescriptor == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 61).fillInStackTrace();
        }
        OracleTypeADT oracleTypeADT = (OracleTypeADT)opaqueDescriptor.getPickler();
        oracleTypeADT.getTOID();
        this.currentRowBinders[n2] = new NamedTypeBinder(oPAQUE.toBytes(), oracleTypeADT);
        this.currentRowByteLens[n2] = this.currentRowBinders[n2].bytelen;
        this.currentRowCharLens[n2] = 0;
    }

    void setSQLXMLInternal(int n2, SQLXML sQLXML) throws SQLException {
        if (sQLXML == null) {
            this.setNull(n2, 0);
        } else {
            this.setOPAQUEInternal(n2, (OPAQUE)((Object)sQLXML));
        }
    }

    @Override
    public void setStructDescriptor(int n2, StructDescriptor structDescriptor) throws SQLException {
        this.setStructDescriptorInternal(n2, structDescriptor);
    }

    void setStructDescriptorInternal(int n2, StructDescriptor structDescriptor) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (structDescriptor == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setStructDescriptorCritical(n3, structDescriptor);
            this.currentRowCharLens[n3] = 0;
        }
    }

    void setStructDescriptorCritical(int n2, StructDescriptor structDescriptor) throws SQLException {
        OracleTypeADT oracleTypeADT = structDescriptor.getOracleTypeADT();
        oracleTypeADT.getTOID();
        this.currentRowBinders[n2] = new NamedTypeBinder(null, oracleTypeADT);
        this.currentRowByteLens[n2] = this.currentRowBinders[n2].bytelen;
        this.currentRowCharLens[n2] = 0;
    }

    @Override
    public void setStructDescriptorAtName(String string, StructDescriptor structDescriptor) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setStructDescriptorInternal(i2 + 1, structDescriptor);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    void setPreBindsCompelete() throws SQLException {
    }

    @Override
    public void setSTRUCT(int n2, STRUCT sTRUCT) throws SQLException {
        this.setSTRUCTInternal(n2, sTRUCT);
    }

    void setSTRUCTInternal(int n2, STRUCT sTRUCT) throws SQLException {
        PhysicalConnection physicalConnection;
        PhysicalConnection physicalConnection2;
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (sTRUCT == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        PhysicalConnection physicalConnection3 = (PhysicalConnection)sTRUCT.getPhysicalConnection();
        if (physicalConnection3 == null || this.connection == physicalConnection3) {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                this.setSTRUCTCritical(n3, sTRUCT);
                this.currentRowCharLens[n3] = 0;
            }
        }
        if (this.connection.hashCode() < physicalConnection3.hashCode()) {
            physicalConnection2 = this.connection;
            physicalConnection = physicalConnection3;
        } else {
            physicalConnection2 = physicalConnection3;
            physicalConnection = this.connection;
        }
        try (Monitor.CloseableLock closeableLock = physicalConnection2.acquireCloseableLock();
             Monitor.CloseableLock closeableLock2 = physicalConnection.acquireCloseableLock();){
            this.setSTRUCTCritical(n3, sTRUCT);
            this.currentRowCharLens[n3] = 0;
        }
    }

    void setSTRUCTCritical(int n2, STRUCT sTRUCT) throws SQLException {
        StructDescriptor structDescriptor = sTRUCT.getDescriptor();
        if (structDescriptor == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 61).fillInStackTrace();
        }
        OracleTypeADT oracleTypeADT = structDescriptor.getOracleTypeADT();
        oracleTypeADT.getTOID();
        this.currentRowBinders[n2] = new NamedTypeBinder(sTRUCT.toBytes(), oracleTypeADT);
        this.currentRowByteLens[n2] = this.currentRowBinders[n2].bytelen;
        this.currentRowCharLens[n2] = 0;
    }

    @Override
    public void setRAW(int n2, RAW rAW) throws SQLException {
        this.setRAWInternal(n2, rAW);
    }

    void setRAWInternal(int n2, RAW rAW) throws SQLException {
        boolean bl = false;
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n3 = n2 - 1;
            if (n3 < 0 || n2 > this.numberOfBindPositions) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
            }
            if (rAW == null) {
                this.currentRowBinders[n3] = new RawNullBinder();
                this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
                this.currentRowCharLens[n3] = 0;
            } else {
                bl = true;
            }
        }
        if (bl) {
            this.setBytesInternal(n2, rAW.getBytes());
        }
    }

    @Override
    public void setCHAR(int n2, CHAR cHAR) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setCHARInternal(n2, cHAR);
        }
    }

    void setCHARInternal(int n2, CHAR cHAR) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (cHAR == null || cHAR.getLength() == 0L) {
            this.currentRowBinders[n3] = new SetCHARNullBinder();
            this.currentRowCharLens[n3] = 1;
        } else {
            byte[] byArray;
            Object object;
            short s2 = this.currentRowFormOfUse[n3];
            short s3 = (short)cHAR.oracleId();
            CharacterSet characterSet = this.getCharacterSetForBind(n3, s2);
            if (characterSet != null && characterSet.getOracleId() != s3) {
                object = cHAR.shareBytes();
                byArray = characterSet.convert(cHAR.getCharacterSet(), (byte[])object, 0, ((byte[])object).length);
            } else {
                byArray = cHAR.getBytes();
            }
            object = this.connection.useLittleEndianSetCHARBinder() ? new LittleEndianSetCHARBinder(byArray) : new SetCHARBinder(byArray);
            this.currentRowBinders[n3] = object;
            this.adjustCharLensForSetCHAR(n3, byArray);
        }
        if (this.sqlKind.isPlsqlOrCall() && this.currentRowCharLens[n3] < this.minVcsBindSize) {
            this.currentRowCharLens[n3] = this.minVcsBindSize;
        }
    }

    void adjustCharLensForSetCHAR(int n2, byte[] byArray) {
        this.currentRowCharLens[n2] = (byArray.length + 1 >> 1) + 1;
        this.currentRowByteLens[n2] = 0;
    }

    @Override
    public void setDATE(int n2, DATE dATE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setDATEInternal(n2, dATE);
        }
    }

    void setDATEInternal(int n2, DATE dATE) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = dATE == null ? new DateNullBinder() : new OracleDateBinder(dATE.getBytes());
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setNUMBER(int n2, NUMBER nUMBER) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setNUMBERInternal(n2, nUMBER);
        }
    }

    void setNUMBERInternal(int n2, NUMBER nUMBER) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = nUMBER == null ? new VarnumNullBinder() : new OracleNumberBinder(nUMBER.getBytes());
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setBLOB(int n2, BLOB bLOB) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBLOBInternal(n2, bLOB);
        }
    }

    void setBLOBInternal(int n2, oracle.jdbc.internal.OracleBlob oracleBlob) throws SQLException {
        byte[] byArray;
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = oracleBlob == null ? new BlobNullBinder() : (PhysicalConnection.isQuasiLocator(byArray = oracleBlob.getBytes()) ? new BlobBinder(byArray, ByteBuffer.wrap(oracleBlob.getPrefetchedData())) : new BlobBinder(byArray));
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    void setJsonInternal(int n2, Object object) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = object == null ? new JsonNullBinder() : new JsonBinder(this.convertToOson(object));
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    private byte[] convertToOson(Object object) throws SQLException {
        OracleJsonFactory oracleJsonFactory = this.connection.getOracleJsonFactory();
        if (object instanceof InputStream) {
            return this.convertByteArrayToOson(this.readStream((InputStream)object), oracleJsonFactory);
        }
        if (object instanceof byte[]) {
            return this.convertByteArrayToOson((byte[])object, oracleJsonFactory);
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OracleJsonGenerator oracleJsonGenerator = oracleJsonFactory.createJsonBinaryGenerator(byteArrayOutputStream);
            if (object instanceof OracleJsonValue) {
                oracleJsonGenerator.write((OracleJsonValue)object);
                oracleJsonGenerator.close();
                return byteArrayOutputStream.toByteArray();
            }
            if (object instanceof OracleJsonParser) {
                oracleJsonGenerator.writeParser(object);
                oracleJsonGenerator.close();
                return byteArrayOutputStream.toByteArray();
            }
            if (object instanceof CharSequence) {
                StringReader stringReader = new StringReader(((CharSequence)object).toString());
                OracleJsonParser oracleJsonParser = oracleJsonFactory.createJsonTextParser(stringReader);
                oracleJsonGenerator.writeParser(oracleJsonParser);
                oracleJsonParser.hasNext();
                oracleJsonGenerator.close();
                return byteArrayOutputStream.toByteArray();
            }
            if (object instanceof Reader) {
                OracleJsonParser oracleJsonParser = oracleJsonFactory.createJsonTextParser((Reader)object);
                oracleJsonGenerator.writeParser(oracleJsonParser);
                oracleJsonParser.hasNext();
                oracleJsonGenerator.close();
                return byteArrayOutputStream.toByteArray();
            }
            if (PhysicalConnection.isJsonJarPresent() && this.writeJsonp(oracleJsonGenerator, object)) {
                oracleJsonGenerator.close();
                return byteArrayOutputStream.toByteArray();
            }
            if (object instanceof OracleJsonDatum) {
                OracleJsonDatum oracleJsonDatum = (OracleJsonDatum)object;
                return oracleJsonDatum.shareBytes();
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        catch (OracleJsonException oracleJsonException) {
            throw this.toSQLException(oracleJsonException);
        }
    }

    private boolean writeJsonp(OracleJsonGenerator oracleJsonGenerator, Object object) throws SQLException {
        block4: {
            try {
                if (object instanceof JsonValue) {
                    oracleJsonGenerator.wrap(JsonGenerator.class).write((JsonValue)object);
                    break block4;
                }
                if (object instanceof JsonParser) {
                    oracleJsonGenerator.writeParser(object);
                    break block4;
                }
                return false;
            }
            catch (RuntimeException runtimeException) {
                throw this.toSQLException(runtimeException);
            }
        }
        return true;
    }

    private byte[] convertByteArrayToOson(byte[] byArray, OracleJsonFactory oracleJsonFactory) throws SQLException {
        if (this.isOsonArray(byArray)) {
            return byArray;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OracleJsonGenerator oracleJsonGenerator = oracleJsonFactory.createJsonBinaryGenerator(byteArrayOutputStream);
            OracleJsonParser oracleJsonParser = oracleJsonFactory.createJsonTextParser(new ByteArrayInputStream(byArray));
            oracleJsonGenerator.writeParser(oracleJsonParser);
            oracleJsonParser.hasNext();
            oracleJsonGenerator.close();
            return byteArrayOutputStream.toByteArray();
        }
        catch (RuntimeException runtimeException) {
            throw this.toSQLException(runtimeException);
        }
    }

    private byte[] readStream(InputStream inputStream) throws SQLException {
        try {
            int n2;
            byte[] byArray = new byte[8192];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((n2 = inputStream.read(byArray)) != -1) {
                byteArrayOutputStream.write(byArray, 0, n2);
            }
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException iOException) {
            throw this.toSQLException(iOException);
        }
    }

    private SQLException toSQLException(Exception exception) {
        return (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), exception).fillInStackTrace();
    }

    private boolean isOsonArray(byte[] byArray) {
        return byArray.length > OsonConstants.MAGIC_BYTES.length && byArray[0] == OsonConstants.MAGIC_BYTES[0] && byArray[1] == OsonConstants.MAGIC_BYTES[1] && byArray[2] == OsonConstants.MAGIC_BYTES[2];
    }

    @Override
    public void setBlob(int n2, Blob blob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBLOBInternal(n2, (oracle.jdbc.internal.OracleBlob)blob);
        }
    }

    void setBlobInternal(int n2, Blob blob) throws SQLException {
        this.setBLOBInternal(n2, (oracle.jdbc.internal.OracleBlob)blob);
    }

    @Override
    public void setCLOB(int n2, CLOB cLOB) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setFormOfUseInternal(n2, this.defaultFormOfUse);
            this.setCLOBInternal(n2, cLOB);
        }
    }

    void setCLOBInternal(int n2, oracle.jdbc.internal.OracleClob oracleClob) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (oracleClob == null) {
            this.currentRowBinders[n3] = new ClobNullBinder();
        } else {
            byte[] byArray = oracleClob.getBytes();
            if (PhysicalConnection.isQuasiLocator(byArray)) {
                char[] cArray = oracleClob.getPrefetchedData();
                ByteBuffer byteBuffer = this.connection.convertClobDataInNetworkCharSet(oracleClob, cArray);
                this.currentRowBinders[n3] = new ClobBinder(byArray, byteBuffer);
            } else {
                this.currentRowBinders[n3] = new ClobBinder(byArray);
            }
        }
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setClob(int n2, Clob clob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setFormOfUseInternal(n2, this.defaultFormOfUse);
            this.setCLOBInternal(n2, (oracle.jdbc.internal.OracleClob)clob);
        }
    }

    void setClobInternal(int n2, Clob clob) throws SQLException {
        this.setCLOBInternal(n2, (oracle.jdbc.internal.OracleClob)clob);
    }

    @Override
    public void setBFILE(int n2, BFILE bFILE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBFILEInternal(n2, bFILE);
        }
    }

    void setBFILEInternal(int n2, oracle.jdbc.internal.OracleBfile oracleBfile) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = oracleBfile == null ? new BfileNullBinder() : new BfileBinder(oracleBfile.getBytes());
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setBfile(int n2, BFILE bFILE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBFILEInternal(n2, bFILE);
        }
    }

    void setBfileInternal(int n2, BFILE bFILE) throws SQLException {
        this.setBFILEInternal(n2, bFILE);
    }

    @Override
    public void setBytes(int n2, byte[] byArray) throws SQLException {
        this.setBytesInternal(n2, byArray);
    }

    void setBytesInternal(int n2, byte[] byArray) throws SQLException {
        int n3;
        int n4 = n2 - 1;
        if (n4 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        int n5 = n3 = byArray != null ? byArray.length : 0;
        if (n3 == 0) {
            this.setNullInternal(n2, -2);
        } else if (this.sqlKind == OracleStatement.SqlKind.PLSQL_BLOCK) {
            if (n3 > this.maxRawBytesPlsql) {
                this.setBytesForBlobCritical(n2, byArray);
            } else {
                this.basicBindBytes(n2, byArray);
            }
        } else if (this.sqlKind == OracleStatement.SqlKind.CALL_BLOCK) {
            if (n3 > this.maxRawBytesPlsql) {
                this.setBytesForBlobCritical(n2, byArray);
            } else {
                this.basicBindBytes(n2, byArray);
            }
        } else if (n3 > this.maxRawBytesSql) {
            this.bindBytesAsStream(n2, byArray);
        } else {
            this.basicBindBytes(n2, byArray);
        }
    }

    void bindBytesAsStream(int n2, byte[] byArray) throws SQLException {
        int n3 = byArray.length;
        byte[] byArray2 = new byte[n3];
        System.arraycopy(byArray, 0, byArray2, 0, n3);
        this.basicBindBinaryStream(n2, new ByteArrayInputStream(byArray2), n3, true);
    }

    void basicBindBytes(int n2, byte[] byArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n3 = n2 - 1;
            DatumBinder datumBinder = this.sqlKind.isPlsqlOrCall() ? new PlsqlRawBinder(byArray) : new RawBinder(byArray);
            this.currentRowBinders[n3] = datumBinder;
            this.currentRowByteLens[n3] = byArray.length;
            this.currentRowCharLens[n3] = 0;
        }
    }

    void basicBindBinaryStream(int n2, InputStream inputStream, int n3) throws SQLException {
        this.basicBindBinaryStream(n2, inputStream, n3, false);
    }

    void basicBindBinaryStream(int n2, InputStream inputStream, int n3, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n4 = n2 - 1;
            this.set_execute_batch(1);
            this.currentRowBinders[n4] = bl ? this.theLongRawStreamForBytesBinder : this.theLongRawStreamBinder;
            if (this.parameterStream == null) {
                this.parameterStream = new InputStream[this.numberOfBindRowsAllocated][this.numberOfBindPositions];
            }
            this.parameterStream[this.currentRank][n4] = bl ? this.connection.conversion.ConvertStreamInternal(inputStream, 6, n3) : this.connection.conversion.ConvertStream(inputStream, 6, n3);
            this.currentRowByteLens[n4] = 0;
            this.currentRowCharLens[n4] = 0;
        }
    }

    @Override
    public void setBytesForBlob(int n2, byte[] byArray) throws SQLException {
        if (byArray == null) {
            this.setNull(n2, -2);
            return;
        }
        int n3 = byArray.length;
        if (n3 == 0) {
            this.setNull(n2, -2);
            return;
        }
        if (this.sqlKind.isPlsqlOrCall()) {
            if (n3 <= this.maxRawBytesPlsql) {
                this.setBytes(n2, byArray);
            } else {
                this.setBytesForBlobCritical(n2, byArray);
            }
        } else if (n3 <= this.maxRawBytesSql) {
            this.setBytes(n2, byArray);
        } else {
            this.setBytesForBlobCritical(n2, byArray);
        }
    }

    void setBytesForBlobCritical(int n2, byte[] byArray) throws SQLException {
        if (n2 < 1 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        BLOB bLOB = BLOB.createTemporary(this.connection, true, 10);
        bLOB.putBytes(1L, byArray);
        this.addToTempLobsToFree(bLOB);
        this.lastBoundBlobs[n2 - 1] = bLOB;
        this.setBLOBInternal(n2, bLOB);
    }

    /*
     * Loose catch block
     */
    void setBinaryStreamContentsForBlobCritical(int n2, InputStream inputStream, long l2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            block23: {
                inputStream = this.isInputStreamEmpty(inputStream);
                if (inputStream == null) {
                    if (bl) {
                        throw new SQLException(l2 + " byte of BLOB data cannot be read");
                    }
                    this.setBLOBInternal(n2, null);
                    return;
                }
                break block23;
                catch (IOException iOException) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
                }
            }
            BLOB bLOB = BLOB.createTemporary(this.connection, true, 10);
            OracleBlobOutputStream oracleBlobOutputStream = (OracleBlobOutputStream)bLOB.setBinaryStream(1L);
            int n3 = bLOB.getBufferSize();
            byte[] byArray = new byte[n3];
            long l3 = 0L;
            int n4 = 0;
            try {
                for (l3 = bl ? l2 : Long.MAX_VALUE; l3 > 0L; l3 -= (long)n4) {
                    n4 = l3 >= (long)n3 ? inputStream.read(byArray) : inputStream.read(byArray, 0, (int)l3);
                    if (n4 == -1) {
                        if (!bl) break;
                        throw new SQLException(l3 + " byte of BLOB data cannot be read");
                    }
                    oracleBlobOutputStream.write(byArray, 0, n4);
                }
                oracleBlobOutputStream.flush();
            }
            catch (IOException iOException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
            }
            this.addToTempLobsToFree(bLOB);
            this.lastBoundBlobs[n2 - 1] = bLOB;
            this.setBLOBInternal(n2, bLOB);
        }
    }

    @Override
    public void setBytesForBlobAtName(String string, byte[] byArray) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBytesForBlob(i2 + 1, byArray);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setInternalBytes(int n2, byte[] byArray, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setInternalBytesInternal(n2, byArray, n3);
        }
    }

    void setInternalBytesInternal(int n2, byte[] byArray, int n3) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("setInternalBytesInternal").fillInStackTrace();
    }

    @Override
    public void setDate(int n2, Date date) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setDATEInternal(n2, date == null ? null : new DATE(date, this.getDefaultCalendar()));
        }
    }

    void setDateInternal(int n2, Date date) throws SQLException {
        this.setDATEInternal(n2, date == null ? null : new DATE(date, this.getDefaultCalendar()));
    }

    @Override
    public void setTime(int n2, Time time) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setTimeInternal(n2, time);
        }
    }

    void setTimeInternal(int n2, Time time) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = time == null ? new DateNullBinder() : new TimeBinder(time);
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setTimestamp(int n2, Timestamp timestamp) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setTimestampInternal(n2, timestamp);
        }
    }

    void setTimestampInternal(int n2, Timestamp timestamp) throws SQLException {
        this.setTimestampInternal(n2, timestamp, -1);
    }

    void setTimestampInternal(int n2, Timestamp timestamp, int n3) throws SQLException {
        int n4 = n2 - 1;
        if (n4 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n4] = timestamp == null ? new TimestampNullBinder(n3) : new TimestampBinder(timestamp, n3);
        this.currentRowByteLens[n4] = this.currentRowBinders[n4].bytelen;
        this.currentRowCharLens[n4] = 0;
    }

    @Override
    public void setINTERVALYM(int n2, INTERVALYM iNTERVALYM) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setINTERVALYMInternal(n2, iNTERVALYM);
        }
    }

    void setINTERVALYMInternal(int n2, INTERVALYM iNTERVALYM) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = iNTERVALYM == null ? new IntervalYMNullBinder() : new IntervalYMBinder(iNTERVALYM.getBytes());
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setINTERVALDS(int n2, INTERVALDS iNTERVALDS) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setINTERVALDSInternal(n2, iNTERVALDS);
        }
    }

    void setINTERVALDSInternal(int n2, INTERVALDS iNTERVALDS) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = iNTERVALDS == null ? new IntervalDSNullBinder() : new IntervalDSBinder(iNTERVALDS.getBytes());
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setTIMESTAMP(int n2, TIMESTAMP tIMESTAMP) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setTIMESTAMPInternal(n2, tIMESTAMP, -1);
        }
    }

    void setTIMESTAMPInternal(int n2, TIMESTAMP tIMESTAMP) throws SQLException {
        this.setTIMESTAMPInternal(n2, tIMESTAMP, -1);
    }

    void setTIMESTAMPInternal(int n2, TIMESTAMP tIMESTAMP, int n3) throws SQLException {
        int n4 = n2 - 1;
        if (n4 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n4] = tIMESTAMP == null ? new TimestampNullBinder(n3) : new OracleTimestampBinder(tIMESTAMP.getBytes(), n3);
        this.currentRowByteLens[n4] = this.currentRowBinders[n4].bytelen;
        this.currentRowCharLens[n4] = 0;
    }

    @Override
    public void setTIMESTAMPTZ(int n2, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setTIMESTAMPTZInternal(n2, tIMESTAMPTZ);
        }
    }

    void setTIMESTAMPTZInternal(int n2, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = tIMESTAMPTZ == null ? new TSTZNullBinder() : new TSTZBinder(tIMESTAMPTZ.getBytes());
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setTIMESTAMPLTZ(int n2, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setTIMESTAMPLTZInternal(n2, tIMESTAMPLTZ);
        }
    }

    void setTIMESTAMPLTZInternal(int n2, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        if (this.connection.getSessionTimeZone() == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 105).fillInStackTrace();
        }
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.currentRowBinders[n3] = tIMESTAMPLTZ == null ? new TSLTZNullBinder() : new TSLTZBinder(tIMESTAMPLTZ.getBytes());
        this.currentRowByteLens[n3] = this.currentRowBinders[n3].bytelen;
        this.currentRowCharLens[n3] = 0;
    }

    private Reader isReaderEmpty(Reader reader) throws IOException {
        if (!reader.markSupported()) {
            reader = new BufferedReader(reader, 4096);
        }
        reader.mark(10);
        if (reader.read() == -1) {
            return null;
        }
        reader.reset();
        return reader;
    }

    private InputStream isInputStreamEmpty(InputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            inputStream = new BufferedInputStream(inputStream, 4096);
        }
        inputStream.mark(10);
        if (inputStream.read() == -1) {
            return null;
        }
        inputStream.reset();
        return inputStream;
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (n3 < 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setAsciiStream cannot be negative").fillInStackTrace();
            }
            this.setAsciiStreamInternal(n2, inputStream, n3);
        }
    }

    void setAsciiStreamInternal(int n2, InputStream inputStream, int n3) throws SQLException {
        this.setAsciiStreamInternal(n2, inputStream, n3, true);
    }

    void setAsciiStreamInternal(int n2, InputStream inputStream, long l2, boolean bl) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (bl && l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 43).fillInStackTrace();
        }
        this.checkUserStreamForDuplicates(inputStream, n3);
        if (inputStream == null || bl && l2 == 0L) {
            this.basicBindNullString(n2);
        } else {
            if (!(this.userRsetType == DEFAULT_RESULT_SET_TYPE || l2 <= (long)this.maxVcsCharsSql && bl)) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 169).fillInStackTrace();
            }
            if (!bl) {
                this.setAsciiStreamContentsForClobCritical(n2, inputStream, l2, bl);
            } else if (this.currentRowFormOfUse[n3] == 1) {
                if (this.sqlKind.isPlsqlOrCall()) {
                    if (l2 <= (long)this.maxVcsCharsPlsql) {
                        this.setAsciiStreamContentsForStringInternal(n2, inputStream, (int)l2);
                    } else {
                        this.setAsciiStreamContentsForClobCritical(n2, inputStream, l2, bl);
                    }
                } else if (l2 <= (long)this.maxVcsCharsSql) {
                    this.setAsciiStreamContentsForStringInternal(n2, inputStream, (int)l2);
                } else if (l2 > Integer.MAX_VALUE) {
                    this.setAsciiStreamContentsForClobCritical(n2, inputStream, l2, bl);
                } else {
                    this.basicBindAsciiStream(n2, inputStream, (int)l2);
                }
            } else if (this.sqlKind.isPlsqlOrCall()) {
                if (l2 <= (long)this.maxVcsNCharsPlsql) {
                    this.setAsciiStreamContentsForStringInternal(n2, inputStream, (int)l2);
                } else {
                    this.setAsciiStreamContentsForClobCritical(n2, inputStream, l2, bl);
                }
            } else if (l2 <= (long)this.maxVcsNCharsSql) {
                this.setAsciiStreamContentsForStringInternal(n2, inputStream, (int)l2);
            } else {
                this.setAsciiStreamContentsForClobCritical(n2, inputStream, l2, bl);
            }
        }
    }

    void basicBindAsciiStream(int n2, InputStream inputStream, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.set_execute_batch(1);
            if (this.userRsetType != DEFAULT_RESULT_SET_TYPE) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 169).fillInStackTrace();
            }
            int n4 = n2 - 1;
            this.currentRowBinders[n4] = this.theLongStreamBinder;
            if (this.parameterStream == null) {
                this.parameterStream = new InputStream[this.numberOfBindRowsAllocated][this.numberOfBindPositions];
            }
            int n5 = this.getConversionCodeForAsciiStream(n4);
            this.parameterStream[this.currentRank][n4] = this.connection.conversion.ConvertStream(inputStream, n5, n3);
            this.currentRowByteLens[n4] = this.currentRowBinders[n4].bytelen;
            this.currentRowCharLens[n4] = 0;
        }
    }

    void setAsciiStreamContentsForStringInternal(int n2, InputStream inputStream, int n3) throws SQLException {
        byte[] byArray = new byte[n3];
        int n4 = 0;
        try {
            int n5;
            for (int i2 = n3; i2 > 0 && (n5 = inputStream.read(byArray, n4, i2)) != -1; i2 -= n5) {
                n4 += n5;
            }
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
        char[] cArray = new char[n3];
        DBConversion.asciiBytesToJavaChars(byArray, n4, cArray);
        this.basicBindString(n2, new String(cArray));
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream, int n3) throws SQLException {
        if (n3 < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setBinaryStream cannot be negative").fillInStackTrace();
        }
        this.setBinaryStreamInternal(n2, inputStream, n3);
    }

    void setBinaryStreamInternal(int n2, InputStream inputStream, int n3) throws SQLException {
        this.setBinaryStreamInternal(n2, inputStream, n3, true);
    }

    void checkUserStreamForDuplicates(Object object, int n2) throws SQLException {
        if (object == null) {
            return;
        }
        if (this.userStream != null) {
            Object[][] objectArray = this.userStream;
            int n3 = objectArray.length;
            for (int i2 = 0; i2 < n3; ++i2) {
                Object[] objectArray2;
                for (Object object2 : objectArray2 = objectArray[i2]) {
                    if (object2 != object) continue;
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 270, (Object)(n2 + 1)).fillInStackTrace();
                }
            }
        } else {
            this.userStream = new Object[this.numberOfBindRowsAllocated][this.numberOfBindPositions];
        }
        this.userStream[this.currentRank][n2] = object;
    }

    void setBinaryStreamInternal(int n2, InputStream inputStream, long l2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n3 = n2 - 1;
            if (n3 < 0 || n2 > this.numberOfBindPositions) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
            }
            this.checkUserStreamForDuplicates(inputStream, n3);
            if (inputStream == null) {
                this.setRAWInternal(n2, null);
            } else {
                if (!(this.userRsetType == DEFAULT_RESULT_SET_TYPE || l2 <= (long)this.maxRawBytesSql && bl)) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 169).fillInStackTrace();
                }
                if (!bl) {
                    this.setBinaryStreamContentsForBlobCritical(n2, inputStream, l2, bl);
                } else if (this.sqlKind.isPlsqlOrCall()) {
                    if (l2 > (long)this.maxRawBytesPlsql) {
                        this.setBinaryStreamContentsForBlobCritical(n2, inputStream, l2, bl);
                    } else {
                        this.setBinaryStreamContentsForByteArrayInternal(n2, inputStream, (int)l2);
                    }
                } else if (l2 > Integer.MAX_VALUE) {
                    this.setBinaryStreamContentsForBlobCritical(n2, inputStream, l2, bl);
                } else if (l2 > (long)this.maxRawBytesSql) {
                    this.basicBindBinaryStream(n2, inputStream, (int)l2);
                } else {
                    this.setBinaryStreamContentsForByteArrayInternal(n2, inputStream, (int)l2);
                }
            }
        }
    }

    void setBinaryStreamContentsForByteArrayInternal(int n2, InputStream inputStream, int n3) throws SQLException {
        byte[] byArray = new byte[n3];
        int n4 = 0;
        try {
            int n5;
            for (int i2 = n3; i2 > 0 && (n5 = inputStream.read(byArray, n4, i2)) != -1; i2 -= n5) {
                n4 += n5;
            }
        }
        catch (IOException iOException) {
            SQLException sQLException = (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 266).fillInStackTrace();
            sQLException.initCause(iOException);
            throw sQLException;
        }
        if (n4 != n3) {
            byte[] byArray2 = new byte[n4];
            System.arraycopy(byArray, 0, byArray2, 0, n4);
            byArray = byArray2;
        }
        this.setBytesInternal(n2, byArray);
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int n2, InputStream inputStream, int n3) throws SQLException {
        if (n3 < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setUnicodeStream cannot be negative").fillInStackTrace();
        }
        this.setUnicodeStreamInternal(n2, inputStream, n3);
    }

    void setUnicodeStreamInternal(int n2, InputStream inputStream, int n3) throws SQLException {
        block20: {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                int n4 = n2 - 1;
                if (n4 < 0 || n2 > this.numberOfBindPositions) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
                }
                this.checkUserStreamForDuplicates(inputStream, n4);
                if (inputStream == null) {
                    this.setStringInternal(n2, null);
                    break block20;
                }
                if (this.userRsetType != DEFAULT_RESULT_SET_TYPE && n3 > this.maxVcsCharsSql) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 169).fillInStackTrace();
                }
                if (this.sqlKind.isPlsqlOrCall() || n3 <= this.maxVcsCharsSql) {
                    byte[] byArray = new byte[n3];
                    int n5 = 0;
                    try {
                        int n6;
                        for (int i2 = n3; i2 > 0 && (n6 = inputStream.read(byArray, n5, i2)) != -1; i2 -= n6) {
                            n5 += n6;
                        }
                    }
                    catch (IOException iOException) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
                    }
                    char[] cArray = new char[n5 >> 1];
                    DBConversion.ucs2BytesToJavaChars(byArray, n5, cArray);
                    this.setStringInternal(n2, new String(cArray));
                    break block20;
                }
                this.currentRowBinders[n4] = this.theLongStreamBinder;
                if (this.parameterStream == null) {
                    this.parameterStream = new InputStream[this.numberOfBindRowsAllocated][this.numberOfBindPositions];
                }
                this.parameterStream[this.currentRank][n4] = this.connection.conversion.ConvertStream(inputStream, 4, n3);
                this.currentRowByteLens[n4] = 0;
                this.currentRowCharLens[n4] = 0;
            }
        }
    }

    @Override
    @Deprecated
    public void setCustomDatum(int n2, CustomDatum customDatum) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (customDatum == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            this.setObjectInternal(n2, this.connection.toDatum(customDatum));
        }
    }

    void setCustomDatumInternal(int n2, CustomDatum customDatum) throws SQLException {
        PhysicalConnection physicalConnection;
        PhysicalConnection physicalConnection2;
        if (customDatum == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        PhysicalConnection physicalConnection3 = this.connection;
        if (customDatum instanceof DatumWithConnection) {
            physicalConnection3 = (PhysicalConnection)((DatumWithConnection)((Object)customDatum)).getPhysicalConnection();
        }
        if (physicalConnection3 == null || this.connection == physicalConnection3) {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                Datum datum = this.connection.toDatum(customDatum);
                int n3 = this.sqlTypeForObject(datum);
                this.setObjectCritical(n2, datum, n3, 0);
            }
        }
        if (this.connection.hashCode() < physicalConnection3.hashCode()) {
            physicalConnection2 = this.connection;
            physicalConnection = physicalConnection3;
        } else {
            physicalConnection2 = physicalConnection3;
            physicalConnection = this.connection;
        }
        try (Monitor.CloseableLock closeableLock = physicalConnection2.acquireCloseableLock();
             Monitor.CloseableLock closeableLock2 = physicalConnection.acquireCloseableLock();){
            Datum datum = this.connection.toDatum(customDatum);
            int n4 = this.sqlTypeForObject(datum);
            this.setObjectCritical(n2, datum, n4, 0);
        }
    }

    @Override
    public void setORAData(int n2, ORAData oRAData) throws SQLException {
        if (oRAData == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        this.setORADataInternal(n2, oRAData);
    }

    void setORADataInternal(int n2, ORAData oRAData) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (oRAData == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            Datum datum = oRAData.toDatum(this.connection);
            int n3 = this.sqlTypeForObject(datum);
            this.setObjectCritical(n2, datum, n3, 0);
            if (n3 == 2002 || n3 == 2008 || n3 == 2003) {
                this.currentRowCharLens[n2 - 1] = 0;
            }
        }
    }

    void setOracleDataInternal(int n2, OracleData oracleData) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Object object = oracleData.toJDBCObject(this.connection);
            if (object instanceof _Proxy_) {
                final _Proxy_ _Proxy_2 = (_Proxy_)object;
                object = AccessController.doPrivileged(new PrivilegedAction<Object>(){

                    @Override
                    public Object run() {
                        return ProxyFactory.extractDelegate(_Proxy_2);
                    }
                });
            }
            int n3 = this.sqlTypeForObject(object);
            this.setObjectCritical(n2, object, n3, 0);
            if (n3 == 2002 || n3 == 2008 || n3 == 2003) {
                this.currentRowCharLens[n2 - 1] = 0;
            }
        }
    }

    @Override
    public void setObject(int n2, Object object, int n3, int n4) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setObjectInternal(n2, object, n3, n4);
        }
    }

    void setObjectInternal(int n2, Object object, int n3, int n4) throws SQLException {
        if (object == null && n3 != 2002 && n3 != 2008 && n3 != 2003 && n3 != 2007 && n3 != 2006 && n3 != 93 && n3 != 2009) {
            this.setNullInternal(n2, n3);
        } else if (object instanceof TTCData) {
            this.setTTCData(n2, (TTCData)object);
        } else if (n3 == 2002 || n3 == 2008 || n3 == 2003 || n3 == 2009) {
            this.setObjectCritical(n2, object, n3, n4);
            this.currentRowCharLens[n2 - 1] = 0;
        } else {
            this.setObjectCritical(n2, object, n3, n4);
        }
    }

    void setObjectCritical(int n2, Object object, int n3, int n4) throws SQLException {
        try {
            switch (n3) {
                case -15: {
                    this.setFormOfUseInternal(n2, (short)2);
                }
                case 1: {
                    if (object instanceof CHAR) {
                        this.setCHARInternal(n2, (CHAR)object);
                        break;
                    }
                    if (object instanceof String) {
                        this.setStringInternal(n2, (String)object);
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setStringInternal(n2, "" + ((Boolean)object != false ? 1 : 0));
                        break;
                    }
                    if (object instanceof Integer) {
                        this.setStringInternal(n2, "" + (Integer)object);
                        break;
                    }
                    if (object instanceof Long) {
                        this.setStringInternal(n2, "" + (Long)object);
                        break;
                    }
                    if (object instanceof Float) {
                        this.setStringInternal(n2, "" + ((Float)object).floatValue());
                        break;
                    }
                    if (object instanceof Double) {
                        this.setStringInternal(n2, "" + (Double)object);
                        break;
                    }
                    if (object instanceof BigDecimal) {
                        this.setStringInternal(n2, ((BigDecimal)object).toString());
                        break;
                    }
                    if (object instanceof Date) {
                        this.setStringInternal(n2, "" + ((Date)object).toString());
                        break;
                    }
                    if (object instanceof Time) {
                        this.setStringInternal(n2, "" + ((Time)object).toString());
                        break;
                    }
                    if (object instanceof Timestamp) {
                        this.setStringInternal(n2, "" + ((Timestamp)object).toString());
                        break;
                    }
                    if (object instanceof URL) {
                        this.setStringInternal(n2, "" + ((URL)object).toString());
                        break;
                    }
                    this.setStringInternal(n2, JavaToJavaConverter.convert(object, String.class, this.connection, n4, null));
                    break;
                }
                case -9: {
                    this.setFormOfUseInternal(n2, (short)2);
                }
                case 12: {
                    if (object instanceof String) {
                        this.setStringInternal(n2, (String)object);
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setStringInternal(n2, "" + ((Boolean)object != false ? 1 : 0));
                        break;
                    }
                    if (object instanceof Integer) {
                        this.setStringInternal(n2, "" + (Integer)object);
                        break;
                    }
                    if (object instanceof Long) {
                        this.setStringInternal(n2, "" + (Long)object);
                        break;
                    }
                    if (object instanceof Float) {
                        this.setStringInternal(n2, "" + ((Float)object).floatValue());
                        break;
                    }
                    if (object instanceof Double) {
                        this.setStringInternal(n2, "" + (Double)object);
                        break;
                    }
                    if (object instanceof BigDecimal) {
                        this.setStringInternal(n2, ((BigDecimal)object).toString());
                        break;
                    }
                    if (object instanceof Date) {
                        this.setStringInternal(n2, "" + ((Date)object).toString());
                        break;
                    }
                    if (object instanceof Time) {
                        this.setStringInternal(n2, "" + ((Time)object).toString());
                        break;
                    }
                    if (object instanceof Timestamp) {
                        this.setStringInternal(n2, "" + ((Timestamp)object).toString());
                        break;
                    }
                    if (object instanceof URL) {
                        this.setStringInternal(n2, "" + ((URL)object).toString());
                        break;
                    }
                    this.setStringInternal(n2, JavaToJavaConverter.convert(object, String.class, this.connection, n4, null));
                    break;
                }
                case 999: {
                    this.setFixedCHARInternal(n2, (String)object);
                    break;
                }
                case -16: {
                    this.setFormOfUseInternal(n2, (short)2);
                }
                case -1: {
                    if (object instanceof String) {
                        this.setStringInternal(n2, (String)object);
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setStringInternal(n2, "" + ((Boolean)object != false ? 1 : 0));
                        break;
                    }
                    if (object instanceof Integer) {
                        this.setStringInternal(n2, "" + (Integer)object);
                        break;
                    }
                    if (object instanceof Long) {
                        this.setStringInternal(n2, "" + (Long)object);
                        break;
                    }
                    if (object instanceof Float) {
                        this.setStringInternal(n2, "" + ((Float)object).floatValue());
                        break;
                    }
                    if (object instanceof Double) {
                        this.setStringInternal(n2, "" + (Double)object);
                        break;
                    }
                    if (object instanceof BigDecimal) {
                        this.setStringInternal(n2, ((BigDecimal)object).toString());
                        break;
                    }
                    if (object instanceof Date) {
                        this.setStringInternal(n2, "" + ((Date)object).toString());
                        break;
                    }
                    if (object instanceof Time) {
                        this.setStringInternal(n2, "" + ((Time)object).toString());
                        break;
                    }
                    if (object instanceof Timestamp) {
                        this.setStringInternal(n2, "" + ((Timestamp)object).toString());
                        break;
                    }
                    if (object instanceof URL) {
                        this.setStringInternal(n2, "" + ((URL)object).toString());
                        break;
                    }
                    this.setStringInternal(n2, JavaToJavaConverter.convert(object, String.class, this.connection, n4, null));
                    break;
                }
                case 2: {
                    if (object instanceof NUMBER) {
                        this.setNUMBERInternal(n2, (NUMBER)object);
                        break;
                    }
                    if (object instanceof Integer) {
                        this.setIntInternal(n2, (Integer)object);
                        break;
                    }
                    if (object instanceof Long) {
                        this.setLongInternal(n2, (Long)object);
                        break;
                    }
                    if (object instanceof Float) {
                        this.setFloatInternal(n2, ((Float)object).floatValue());
                        break;
                    }
                    if (object instanceof Double) {
                        this.setDoubleInternal(n2, (Double)object);
                        break;
                    }
                    if (object instanceof BigDecimal) {
                        this.setBigDecimalInternal(n2, (BigDecimal)object);
                        break;
                    }
                    if (object instanceof BigInteger) {
                        this.setBigDecimalInternal(n2, new BigDecimal((BigInteger)object));
                        break;
                    }
                    if (object instanceof String) {
                        this.setNUMBERInternal(n2, new NUMBER((String)object, n4));
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setIntInternal(n2, (Boolean)object != false ? 1 : 0);
                        break;
                    }
                    if (object instanceof Short) {
                        this.setShortInternal(n2, (Short)object);
                        break;
                    }
                    if (object instanceof Byte) {
                        this.setByteInternal(n2, (Byte)object);
                        break;
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
                }
                case 3: {
                    if (object instanceof BigDecimal) {
                        this.setBigDecimalInternal(n2, (BigDecimal)object);
                        break;
                    }
                    if (object instanceof Number) {
                        this.setBigDecimalInternal(n2, new BigDecimal(((Number)object).doubleValue()));
                        break;
                    }
                    if (object instanceof NUMBER) {
                        this.setBigDecimalInternal(n2, ((NUMBER)object).bigDecimalValue());
                        break;
                    }
                    if (object instanceof String) {
                        this.setBigDecimalInternal(n2, new BigDecimal((String)object));
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setBigDecimalInternal(n2, new BigDecimal((Boolean)object != false ? 1.0 : 0.0));
                        break;
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
                }
                case -7: {
                    if (object instanceof Boolean) {
                        this.setByteInternal(n2, (byte)((Boolean)object != false ? 1 : 0));
                        break;
                    }
                    if (object instanceof String) {
                        this.setByteInternal(n2, (byte)("true".equalsIgnoreCase((String)object) || "1".equals(object) ? 1 : 0));
                        break;
                    }
                    if (object instanceof Number) {
                        this.setIntInternal(n2, ((Number)object).byteValue() != 0 ? 1 : 0);
                        break;
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
                }
                case 252: {
                    if (this.connection.databaseMetaData.getDatabaseMajorVersion() < 12 || this.connection.databaseMetaData.getDatabaseMajorVersion() == 12 && this.connection.databaseMetaData.getDatabaseMinorVersion() < 1) {
                        throw (SQLException)DatabaseError.createSqlException(299).fillInStackTrace();
                    }
                    this.setPlsqlBooleanInternal(n2, (Boolean)object);
                    break;
                }
                case -6: {
                    if (object instanceof Number) {
                        this.setByteInternal(n2, ((Number)object).byteValue());
                        break;
                    }
                    if (object instanceof String) {
                        this.setByteInternal(n2, Byte.parseByte((String)object));
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setByteInternal(n2, (byte)((Boolean)object != false ? 1 : 0));
                        break;
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
                }
                case 5: {
                    if (object instanceof Number) {
                        this.setShortInternal(n2, ((Number)object).shortValue());
                        break;
                    }
                    if (object instanceof String) {
                        this.setShortInternal(n2, Short.parseShort((String)object));
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setShortInternal(n2, (short)((Boolean)object != false ? 1 : 0));
                        break;
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
                }
                case 4: {
                    if (object instanceof Number) {
                        this.setIntInternal(n2, ((Number)object).intValue());
                        break;
                    }
                    if (object instanceof String) {
                        this.setIntInternal(n2, Integer.parseInt((String)object));
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setIntInternal(n2, (Boolean)object != false ? 1 : 0);
                        break;
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
                }
                case -5: {
                    if (object instanceof Number) {
                        this.setLongInternal(n2, ((Number)object).longValue());
                        break;
                    }
                    if (object instanceof String) {
                        this.setLongInternal(n2, Long.parseLong((String)object));
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setLongInternal(n2, (Boolean)object != false ? 1L : 0L);
                        break;
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
                }
                case 7: {
                    if (object instanceof Number) {
                        this.setFloatInternal(n2, ((Number)object).floatValue());
                        break;
                    }
                    if (object instanceof String) {
                        this.setFloatInternal(n2, Float.valueOf((String)object).floatValue());
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setFloatInternal(n2, (Boolean)object != false ? 1.0f : 0.0f);
                        break;
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
                }
                case 6: 
                case 8: {
                    if (object instanceof Number) {
                        this.setDoubleInternal(n2, ((Number)object).doubleValue());
                        break;
                    }
                    if (object instanceof String) {
                        this.setDoubleInternal(n2, Double.valueOf((String)object));
                        break;
                    }
                    if (object instanceof Boolean) {
                        this.setDoubleInternal(n2, (Boolean)object != false ? 1.0 : 0.0);
                        break;
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
                }
                case -2: {
                    if (object instanceof RAW) {
                        this.setRAWInternal(n2, (RAW)object);
                        break;
                    }
                    if (object instanceof LogicalTransactionId) {
                        this.setBytesInternal(n2, ((LogicalTransactionId)object).getBytes());
                        break;
                    }
                    this.setBytesInternal(n2, (byte[])object);
                    break;
                }
                case -3: {
                    this.setBytesInternal(n2, (byte[])object);
                    break;
                }
                case -4: {
                    this.setBytesInternal(n2, (byte[])object);
                    break;
                }
                case 91: {
                    if (object instanceof DATE) {
                        this.setDATEInternal(n2, (DATE)object);
                        break;
                    }
                    if (object instanceof Date) {
                        this.setDATEInternal(n2, new DATE(object, this.getDefaultCalendar()));
                        break;
                    }
                    if (object instanceof Timestamp) {
                        this.setDATEInternal(n2, new DATE((Timestamp)object));
                        break;
                    }
                    if (object instanceof String) {
                        this.setDateInternal(n2, Date.valueOf((String)object));
                        break;
                    }
                    this.setDATEInternal(n2, JavaToJavaConverter.convert(object, DATE.class, this.connection, n4, null));
                    break;
                }
                case 92: {
                    if (object instanceof Time) {
                        this.setTimeInternal(n2, (Time)object);
                        break;
                    }
                    if (object instanceof Timestamp) {
                        this.setTimeInternal(n2, new Time(((Timestamp)object).getTime()));
                        break;
                    }
                    if (object instanceof Date) {
                        this.setTimeInternal(n2, new Time(((Date)object).getTime()));
                        break;
                    }
                    if (object instanceof String) {
                        this.setTimeInternal(n2, Time.valueOf((String)object));
                        break;
                    }
                    this.setTimeInternal(n2, JavaToJavaConverter.convert(object, Time.class, this.connection, n4, null));
                    break;
                }
                case 93: {
                    if (object instanceof TIMESTAMP) {
                        this.setTIMESTAMPInternal(n2, (TIMESTAMP)object, n4);
                        break;
                    }
                    if (object instanceof Timestamp) {
                        this.setTimestampInternal(n2, (Timestamp)object, n4);
                        break;
                    }
                    if (object instanceof Date) {
                        this.setTIMESTAMPInternal(n2, new TIMESTAMP((Date)object), n4);
                        break;
                    }
                    if (object instanceof DATE) {
                        this.setTIMESTAMPInternal(n2, new TIMESTAMP(((DATE)object).timestampValue()), n4);
                        break;
                    }
                    if (object instanceof String) {
                        this.setTimestampInternal(n2, Timestamp.valueOf((String)object), n4);
                        break;
                    }
                    this.setTIMESTAMPInternal(n2, JavaToJavaConverter.convert(object, TIMESTAMP.class, this.connection, n4, null), n4);
                    break;
                }
                case -100: {
                    this.setTIMESTAMPInternal(n2, (TIMESTAMP)object, n4);
                    break;
                }
                case -101: 
                case 2013: 
                case 2014: {
                    this.setTIMESTAMPTZInternal(n2, JavaToJavaConverter.convert(object, TIMESTAMPTZ.class, this.connection, n4, null));
                    break;
                }
                case -102: {
                    this.setTIMESTAMPLTZInternal(n2, JavaToJavaConverter.convert(object, TIMESTAMPLTZ.class, this.connection, n4, null));
                    break;
                }
                case -103: {
                    this.setINTERVALYMInternal(n2, JavaToJavaConverter.convert(object, INTERVALYM.class, this.connection, n4, null));
                    break;
                }
                case -104: {
                    this.setINTERVALDSInternal(n2, JavaToJavaConverter.convert(object, INTERVALDS.class, this.connection, n4, null));
                    break;
                }
                case -8: {
                    this.setROWIDInternal(n2, (ROWID)object);
                    break;
                }
                case 100: {
                    if (object instanceof Float) {
                        this.setBinaryFloatInternal(n2, new BINARY_FLOAT((Float)object));
                        break;
                    }
                    this.setBinaryFloatInternal(n2, (BINARY_FLOAT)object);
                    break;
                }
                case 101: {
                    if (object instanceof Double) {
                        this.setBinaryDoubleInternal(n2, new BINARY_DOUBLE((Double)object));
                        break;
                    }
                    this.setBinaryDoubleInternal(n2, (BINARY_DOUBLE)object);
                    break;
                }
                case 2004: {
                    this.setBLOBInternal(n2, (oracle.jdbc.internal.OracleBlob)object);
                    break;
                }
                case 2016: {
                    this.setJsonInternal(n2, object);
                    break;
                }
                case 2005: 
                case 2011: {
                    this.setCLOBInternal(n2, (oracle.jdbc.internal.OracleClob)object);
                    if (((oracle.jdbc.internal.OracleClob)object).isNCLOB()) {
                        this.setFormOfUseInternal(n2, (short)2);
                        break;
                    }
                    this.setFormOfUseInternal(n2, this.defaultFormOfUse);
                    break;
                }
                case -13: {
                    this.setBFILEInternal(n2, (oracle.jdbc.internal.OracleBfile)object);
                    break;
                }
                case 2002: 
                case 2008: {
                    this.setSTRUCTInternal(n2, STRUCT.toSTRUCT(object, this.connection));
                    break;
                }
                case 2003: {
                    this.setARRAYInternal(n2, ARRAY.toARRAY(object, this.connection));
                    break;
                }
                case 2007: {
                    if (object instanceof ANYDATA) {
                        this.setOPAQUEInternal(n2, (OPAQUE)((ANYDATA)object).toDatum(this.getConnection()));
                        break;
                    }
                    if (object instanceof TypeDescriptor) {
                        this.setOPAQUEInternal(n2, (OPAQUE)((TypeDescriptor)object).toDatum(this.getConnection()));
                        break;
                    }
                    this.setOPAQUEInternal(n2, (OPAQUE)object);
                    break;
                }
                case 2006: {
                    this.setREFInternal(n2, (OracleRef)object);
                    break;
                }
                case 2009: {
                    this.setSQLXMLInternal(n2, (SQLXML)object);
                    break;
                }
                case -10: 
                case 2012: {
                    this.setCursorInternal(n2, object != null ? (ResultSet)object : null);
                    break;
                }
                default: {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
                }
            }
        }
        catch (ClassCastException classCastException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
    }

    @Override
    public void setObjectAtName(String string, Object object, int n2, int n3) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n4 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n4; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setObjectInternal(i2 + 1, object, n2, n3);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setObject(int n2, Object object, int n3) throws SQLException {
        this.setObjectInternal(n2, object, n3, 0);
    }

    void setObjectInternal(int n2, Object object, int n3) throws SQLException {
        this.setObjectInternal(n2, object, n3, 0);
    }

    @Override
    public void setRefType(int n2, REF rEF) throws SQLException {
        this.setREFInternal(n2, rEF);
    }

    void setRefTypeInternal(int n2, REF rEF) throws SQLException {
        this.setREFInternal(n2, rEF);
    }

    @Override
    public void setRef(int n2, Ref ref) throws SQLException {
        this.setREFInternal(n2, (OracleRef)ref);
    }

    void setRefInternal(int n2, Ref ref) throws SQLException {
        this.setREFInternal(n2, (OracleRef)ref);
    }

    @Override
    public void setREF(int n2, REF rEF) throws SQLException {
        this.setREFInternal(n2, rEF);
    }

    void setREFInternal(int n2, OracleRef oracleRef) throws SQLException {
        PhysicalConnection physicalConnection;
        PhysicalConnection physicalConnection2;
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (oracleRef == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        PhysicalConnection physicalConnection3 = (PhysicalConnection)oracleRef.getInternalConnection();
        if (physicalConnection3 == null || this.connection == physicalConnection3) {
            try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
                this.setREFCritical(n3, oracleRef);
                this.currentRowCharLens[n3] = 0;
            }
        }
        if (this.connection.hashCode() < physicalConnection3.hashCode()) {
            physicalConnection2 = this.connection;
            physicalConnection = physicalConnection3;
        } else {
            physicalConnection2 = physicalConnection3;
            physicalConnection = this.connection;
        }
        try (Monitor.CloseableLock closeableLock = physicalConnection2.acquireCloseableLock();
             Monitor.CloseableLock closeableLock2 = physicalConnection.acquireCloseableLock();){
            this.setREFCritical(n3, oracleRef);
            this.currentRowCharLens[n3] = 0;
        }
    }

    void setREFCritical(int n2, OracleRef oracleRef) throws SQLException {
        StructDescriptor structDescriptor = oracleRef.getDescriptor();
        if (structDescriptor == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 52).fillInStackTrace();
        }
        OracleTypeADT oracleTypeADT = structDescriptor.getOracleTypeADT();
        oracleTypeADT.getTOID();
        this.currentRowBinders[n2] = new RefTypeBinder(oracleRef.getBytes(), oracleTypeADT);
        this.currentRowByteLens[n2] = this.currentRowBinders[n2].bytelen;
        this.currentRowCharLens[n2] = 0;
    }

    @Override
    public void setObject(int n2, Object object) throws SQLException {
        this.setObjectInternal(n2, object);
    }

    void setObjectInternal(int n2, Object object) throws SQLException {
        if (object instanceof ORAData) {
            this.setORADataInternal(n2, (ORAData)object);
        } else if (object instanceof CustomDatum) {
            this.setCustomDatumInternal(n2, (CustomDatum)object);
        } else if (object instanceof OracleData) {
            this.setOracleDataInternal(n2, (OracleData)object);
        } else if (object instanceof OracleResultSet) {
            this.setCursorInternal(n2, (ResultSet)object);
        } else {
            int n3 = this.sqlTypeForObject(object);
            this.setObjectInternal(n2, object, n3, 0);
        }
    }

    @Override
    public void setOracleObject(int n2, Datum datum) throws SQLException {
        this.setObjectInternal(n2, datum);
    }

    void setOracleObjectInternal(int n2, Datum datum) throws SQLException {
        this.setObjectInternal(n2, datum);
    }

    @Override
    @Deprecated
    public void setPlsqlIndexTable(int n2, Object object, int n3, int n4, int n5, int n6) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setPlsqlIndexTableInternal(n2, object, n3, n4, n5, n6);
        }
    }

    void setPlsqlIndexTableInternal(int n2, Object object, int n3, int n4, int n5, int n6) throws SQLException {
        int n7 = n2 - 1;
        if (n7 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (object == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 271).fillInStackTrace();
        }
        if (n4 > Array.getLength(object)) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, String.format("curLen (%d) is greater than Array Length (%d)", n4, Array.getLength(object))).fillInStackTrace();
        }
        if (n4 > n3) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, String.format("curLen (%d) is greater than maxLen (%d)", n4, n3)).fillInStackTrace();
        }
        if (Array.getLength(object) > n3) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, String.format("Array Length (%d) is greater than maxLen (%d)", Array.getLength(object), n3)).fillInStackTrace();
        }
        int n8 = this.getInternalType(n5);
        Object[] objectArray = null;
        switch (n8) {
            case 1: 
            case 96: {
                String[] stringArray = null;
                int n9 = 0;
                if (object instanceof CHAR[]) {
                    CHAR[] cHARArray = (CHAR[])object;
                    n9 = cHARArray.length;
                    stringArray = new String[n9];
                    for (int i2 = 0; i2 < n9; ++i2) {
                        CHAR cHAR = cHARArray[i2];
                        if (cHAR == null) continue;
                        stringArray[i2] = cHAR.getString();
                    }
                } else if (object instanceof String[]) {
                    stringArray = (String[])object;
                    n9 = stringArray.length;
                } else {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 97).fillInStackTrace();
                }
                if (n6 == 0 && stringArray != null) {
                    for (int i3 = 0; i3 < n9; ++i3) {
                        String string = stringArray[i3];
                        if (string == null || n6 >= string.length()) continue;
                        n6 = string.length();
                    }
                }
                objectArray = stringArray;
                break;
            }
            case 2: 
            case 6: {
                objectArray = OracleTypeNUMBER.toNUMBERArray(object, this.connection, 1L, n4);
                if (n6 == 0 && objectArray != null) {
                    n6 = 22;
                }
                this.currentRowCharLens[n7] = 0;
                break;
            }
            default: {
                int[] nArray = new int[]{0};
                objectArray = this.handleOtherPlsqlTypes(n8, object, n4, nArray);
                if (objectArray == null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 97).fillInStackTrace();
                }
                if (n6 != 0) break;
                n6 = nArray[0];
            }
        }
        if (objectArray.length == 0 && n3 == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 272).fillInStackTrace();
        }
        this.currentRowBinders[n7] = this.thePlsqlIbtBinder;
        this.currentRowByteLens[n7] = this.currentRowBinders[n7].bytelen;
        this.currentRowCharLens[n7] = 0;
        if (this.parameterPlsqlIbt == null) {
            this.parameterPlsqlIbt = new PlsqlIbtBindInfo[this.numberOfBindRowsAllocated][this.numberOfBindPositions];
        }
        this.parameterPlsqlIbt[this.currentRank][n7] = new PlsqlIbtBindInfo(this, objectArray, n3, n4, n8, n6);
        this.hasIbtBind = true;
    }

    Object[] handleOtherPlsqlTypes(int n2, Object object, int n3, int[] nArray) throws SQLException {
        return null;
    }

    @Deprecated
    public void setPlsqlIndexTableAtName(String string, Object object, int n2, int n3, int n4, int n5) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (string == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            String string2 = string.intern();
            String[] stringArray = this.sqlObject.getParameterList();
            boolean bl = false;
            int n6 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
            for (int i2 = 0; i2 < n6; ++i2) {
                if (stringArray[i2] != string2) continue;
                this.setPlsqlIndexTableInternal(i2 + 1, object, n2, n3, n4, n5);
                bl = true;
            }
            if (!bl) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
            }
        }
    }

    @Override
    void endOfResultSet(boolean bl) throws SQLException {
        if (!bl) {
            this.prepareForNewResults(false, false, false);
        }
        this.rowPrefetchInLastFetch = -1;
    }

    int sqlTypeForObject(Object object) {
        if (object == null) {
            return 0;
        }
        if (!(object instanceof Datum)) {
            if (object instanceof String) {
                return this.fixedString ? 999 : 12;
            }
            if (object instanceof Character) {
                return 1;
            }
            if (object instanceof BigDecimal) {
                return 2;
            }
            if (object instanceof BigInteger) {
                return 2;
            }
            if (object instanceof Boolean) {
                return -7;
            }
            if (object instanceof Integer) {
                return 4;
            }
            if (object instanceof Long) {
                return -5;
            }
            if (object instanceof Float) {
                return 7;
            }
            if (object instanceof Double) {
                return 8;
            }
            if (object instanceof byte[]) {
                return -3;
            }
            if (object instanceof Short) {
                return 5;
            }
            if (object instanceof Byte) {
                return -6;
            }
            if (object instanceof Date) {
                return 91;
            }
            if (object instanceof Time) {
                return 92;
            }
            if (object instanceof Timestamp) {
                return 93;
            }
            if (object instanceof SQLData) {
                return 2002;
            }
            if (object instanceof ObjectData) {
                return 2002;
            }
            if (object instanceof URL) {
                return this.fixedString ? 999 : 12;
            }
            if (object instanceof LogicalTransactionId) {
                return -2;
            }
            if (object instanceof LocalDate) {
                return 93;
            }
            if (object instanceof LocalTime) {
                return 93;
            }
            if (object instanceof LocalDateTime) {
                return 93;
            }
            if (object instanceof ZonedDateTime) {
                return -101;
            }
            if (object instanceof OffsetDateTime) {
                return -101;
            }
            if (object instanceof OffsetTime) {
                return -101;
            }
            if (object instanceof Duration) {
                return -104;
            }
            if (object instanceof Period) {
                return -103;
            }
            if (object instanceof OracleJsonValue || object instanceof OracleJsonParser) {
                return 2016;
            }
            if (PhysicalConnection.isJsonJarPresent() && (object instanceof JsonValue || object instanceof JsonParser)) {
                return 2016;
            }
        } else {
            if (object instanceof BINARY_FLOAT) {
                return 100;
            }
            if (object instanceof BINARY_DOUBLE) {
                return 101;
            }
            if (object instanceof BLOB || object instanceof OracleBlob) {
                return 2004;
            }
            if (object instanceof CLOB || object instanceof OracleClob) {
                return 2005;
            }
            if (object instanceof BFILE || object instanceof OracleBfile) {
                return -13;
            }
            if (object instanceof ROWID) {
                return -8;
            }
            if (object instanceof NUMBER) {
                return 2;
            }
            if (object instanceof DATE) {
                return 91;
            }
            if (object instanceof TIMESTAMP) {
                return 93;
            }
            if (object instanceof TIMESTAMPTZ) {
                return -101;
            }
            if (object instanceof TIMESTAMPLTZ) {
                return -102;
            }
            if (object instanceof REF) {
                return 2006;
            }
            if (object instanceof CHAR) {
                return 1;
            }
            if (object instanceof RAW) {
                return -2;
            }
            if (object instanceof ARRAY) {
                return 2003;
            }
            if (object instanceof STRUCT) {
                return 2002;
            }
            if (object instanceof OPAQUE) {
                return 2007;
            }
            if (object instanceof INTERVALYM) {
                return -103;
            }
            if (object instanceof INTERVALDS) {
                return -104;
            }
            if (object instanceof SQLXML) {
                return 2009;
            }
            if (object instanceof OracleJsonDatum) {
                return 2016;
            }
        }
        return 1111;
    }

    @Override
    public void clearParameters() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.clearParametersCritical();
        }
    }

    void clearParametersCritical() throws SQLException {
        this.ibtBindChars = null;
        this.ibtBindBytes = null;
        this.ibtBindIndicators = null;
        this.clearParameters = true;
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            this.currentRowBinders[i2] = null;
        }
    }

    void printByteArray(byte[] byArray) {
        if (byArray == null) {
        } else {
            int n2 = byArray.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                int n3 = byArray[i2] & 0xFF;
                if (n3 >= 16) continue;
            }
        }
    }

    @Override
    public void setCharacterStream(int n2, Reader reader, int n3) throws SQLException {
        if (n3 < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setCharacterStream cannot be negative").fillInStackTrace();
        }
        this.setCharacterStreamInternal(n2, reader, n3);
    }

    void setCharacterStreamInternal(int n2, Reader reader, int n3) throws SQLException {
        this.setCharacterStreamInternal(n2, reader, n3, true);
    }

    void setCharacterStreamInternal(int n2, Reader reader, long l2, boolean bl) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (bl && l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 43).fillInStackTrace();
        }
        this.checkUserStreamForDuplicates(reader, n3);
        if (reader == null || bl && l2 == 0L) {
            this.basicBindNullString(n2);
        } else {
            if (!(this.userRsetType == DEFAULT_RESULT_SET_TYPE || l2 <= (long)this.maxVcsCharsSql && bl)) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 169).fillInStackTrace();
            }
            if (!bl) {
                this.setReaderContentsForClobCritical(n2, reader, l2, bl);
            } else if (this.currentRowFormOfUse[n3] == 1) {
                if (this.sqlKind.isPlsqlOrCall()) {
                    if (l2 > (long)this.maxVcsBytesPlsql || l2 > (long)this.maxVcsCharsPlsql && this.isServerCharSetFixedWidth) {
                        this.setReaderContentsForClobCritical(n2, reader, l2, bl);
                    } else if (l2 <= (long)this.maxVcsCharsPlsql) {
                        this.setReaderContentsForStringInternal(n2, reader, (int)l2);
                    } else {
                        this.setReaderContentsForStringOrClobInVariableWidthCase(n2, reader, (int)l2, false);
                    }
                } else if (l2 <= (long)this.maxVcsCharsSql) {
                    this.setReaderContentsForStringInternal(n2, reader, (int)l2);
                } else if (l2 > Integer.MAX_VALUE) {
                    this.setReaderContentsForClobCritical(n2, reader, l2, bl);
                } else {
                    this.basicBindCharacterStream(n2, reader, (int)l2, false);
                }
            } else if (this.sqlKind.isPlsqlOrCall()) {
                if (l2 > (long)this.maxVcsBytesPlsql || l2 > (long)this.maxVcsNCharsPlsql && this.isServerCharSetFixedWidth) {
                    this.setReaderContentsForClobCritical(n2, reader, l2, bl);
                } else if (l2 <= (long)this.maxVcsNCharsPlsql) {
                    this.setReaderContentsForStringInternal(n2, reader, (int)l2);
                } else {
                    this.setReaderContentsForStringOrClobInVariableWidthCase(n2, reader, (int)l2, true);
                }
            } else if (l2 <= (long)this.maxVcsNCharsSql) {
                this.setReaderContentsForStringInternal(n2, reader, (int)l2);
            } else {
                this.setReaderContentsForClobCritical(n2, reader, l2, bl);
            }
        }
    }

    void basicBindCharacterStream(int n2, Reader reader, int n3, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.set_execute_batch(1);
            int n4 = n2 - 1;
            this.currentRowBinders[n4] = bl ? this.theLongStreamForStringBinder : this.theLongStreamBinder;
            if (this.parameterStream == null) {
                this.parameterStream = new InputStream[this.numberOfBindRowsAllocated][this.numberOfBindPositions];
            }
            int n5 = this.getConversionCodeForCharacterStream(n4);
            short s2 = this.currentRowFormOfUse[n4];
            this.parameterStream[this.currentRank][n4] = bl ? this.connection.conversion.ConvertStreamInternal(reader, n5, n3, s2) : this.connection.conversion.ConvertStream(reader, n5, n3, s2);
            this.currentRowByteLens[n4] = 0;
            this.currentRowCharLens[n4] = 0;
        }
    }

    void setReaderContentsForStringOrClobInVariableWidthCase(int n2, Reader reader, int n3, boolean bl) throws SQLException {
        int n4;
        char[] cArray = new char[n3];
        int n5 = 0;
        try {
            int n6;
            for (int i2 = n3; i2 > 0 && (n6 = reader.read(cArray, n5, i2)) != -1; i2 -= n6) {
                n5 += n6;
            }
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
        if (n5 != n3) {
            char[] cArray2 = new char[n5];
            System.arraycopy(cArray, 0, cArray2, 0, n5);
            cArray = cArray2;
        }
        if ((n4 = this.connection.conversion.encodedByteLength(cArray, bl)) < this.maxVcsBytesPlsql) {
            this.setStringInternal(n2, new String(cArray));
        } else {
            this.setStringForClobCritical(n2, new String(cArray));
        }
    }

    void setReaderContentsForStringInternal(int n2, Reader reader, int n3) throws SQLException {
        char[] cArray = new char[n3];
        int n4 = 0;
        try {
            int n5;
            for (int i2 = n3; i2 > 0 && (n5 = reader.read(cArray, n4, i2)) != -1; i2 -= n5) {
                n4 += n5;
            }
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
        if (n4 != n3) {
            char[] cArray2 = new char[n4];
            System.arraycopy(cArray, 0, cArray2, 0, n4);
            cArray = cArray2;
        }
        this.setStringInternal(n2, new String(cArray));
    }

    @Override
    public void setDate(int n2, Date date, Calendar calendar) throws SQLException {
        this.setDATEInternal(n2, date == null ? null : new DATE(date, calendar));
    }

    void setDateInternal(int n2, Date date, Calendar calendar) throws SQLException {
        this.setDATEInternal(n2, date == null ? null : new DATE(date, calendar));
    }

    @Override
    public void setTime(int n2, Time time, Calendar calendar) throws SQLException {
        this.setDATEInternal(n2, time == null ? null : new DATE(time, calendar));
    }

    void setTimeInternal(int n2, Time time, Calendar calendar) throws SQLException {
        this.setDATEInternal(n2, time == null ? null : new DATE(time, calendar));
    }

    @Override
    public void setTimestamp(int n2, Timestamp timestamp, Calendar calendar) throws SQLException {
        this.setTimestampInternal(n2, timestamp, calendar);
    }

    void setTimestampInternal(int n2, Timestamp timestamp, Calendar calendar) throws SQLException {
        this.setTIMESTAMPInternal(n2, timestamp == null ? null : new TIMESTAMP(timestamp, calendar));
    }

    @Override
    public void setCheckBindTypes(boolean bl) {
        this.checkBindTypes = bl;
    }

    final void setBatchUpdate() {
        this.batchUpdate = true;
    }

    @Override
    final void checkIfBatchExists() throws SQLException {
        if (this.doesBatchExist()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 81, "batch must be either executed or cleared").fillInStackTrace();
        }
    }

    boolean doesBatchExist() {
        return this.currentRank > 0 && this.batchUpdate;
    }

    @Override
    public void addBatch() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.isDmlReturning) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "DML Returning cannot be batched").fillInStackTrace();
            }
            this.setBatchUpdate();
            this.processCompletedBindRow(this.currentRank + 2, this.currentRank > 0 && this.sqlKind.isPlsqlOrCall());
            ++this.currentRank;
        }
    }

    @Override
    public void addBatch(String string) throws SQLException {
        Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("addBatch").fillInStackTrace();
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
    public void clearBatch() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.clearBatchCritical();
        }
    }

    @Override
    void clearBatchCritical() throws SQLException {
        for (int i2 = this.currentRank - 1; i2 >= 0; --i2) {
            for (int i3 = 0; i3 < this.numberOfBindPositions; ++i3) {
                this.binders[i2][i3] = null;
            }
        }
        this.currentRank = 0;
        this.batchFIFOFront = null;
        this.batchFIFOBack = null;
        this.firstRowInBatch = 0;
        this.currentBatchAccumulatedBindsSize = 0;
        if (this.binders != null) {
            this.currentRowBinders = this.binders[0];
        }
        this.clearParametersCritical();
    }

    void executeForRowsWithTimeout(boolean bl) throws SQLException {
        boolean bl2 = this.queryTimeout > 0;
        try {
            this.cancelLock.enterExecuting();
            if (bl2) {
                this.connection.getTimeout().setTimeout((long)this.queryTimeout * 1000L, this);
            }
            this.executeForRows(bl);
        }
        finally {
            if (bl2) {
                this.connection.getTimeout().cancelTimeout();
            }
            this.cancelLock.exitExecuting();
        }
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
            long[] lArray;
            block26: {
                this.checkSum = 0L;
                this.checkSumComputationFailure = false;
                long l2 = 0L;
                if (this.dmsExecute != null) {
                    l2 = this.dmsExecute.start();
                }
                try {
                    this.connection.updateSystemContext();
                    this.cleanOldTempLobs();
                    this.setBatchUpdate();
                    if (this.currentRank > 0) {
                        this.prepareForExecuteBatch();
                        try {
                            try {
                                this.prepareConnectionForExecuteBatch();
                            }
                            catch (SQLException sQLException) {
                                throw this.generateBatchUpdateException(sQLException, new long[this.currentRank], 0);
                            }
                            int n2 = this.currentRank;
                            lArray = this.batchFIFOFront == null ? this.executeBatchWithoutQueue() : this.executeBatchFromQueue();
                            this.slideDownCurrentRow(n2);
                        }
                        catch (BatchUpdateException batchUpdateException) {
                            this.handleExecuteBatchFailure();
                            throw batchUpdateException;
                        }
                        finally {
                            this.handleExecuteBatchCompletionAlways();
                        }
                        this.handleExecuteBatchCompletion(lArray);
                        break block26;
                    }
                    lArray = new long[]{};
                }
                finally {
                    if (this.dmsExecute != null) {
                        this.dmsExecute.stop(l2);
                    }
                }
            }
            long[] lArray2 = lArray;
            return lArray2;
        }
    }

    private final void prepareForExecuteBatch() throws SQLException {
        if (this.connection.isDRCPEnabled()) {
            this.prepareForExecuteWithDRCP();
        }
        this.ensureOpen();
        this.prepareForNewResults(true, true, true);
        if (this.sqlKind.isSELECT()) {
            throw (SQLException)DatabaseError.createBatchUpdateException(80, 0, (int[])null).fillInStackTrace();
        }
        this.noMoreUpdateCounts = false;
    }

    private final void prepareConnectionForExecuteBatch() throws SQLException {
        this.connection.needLine();
        if (this.connection.dmsUpdateSqlText()) {
            this.dmsSqlText.update(this.sqlObject.toString());
        }
        if (!this.isOpen) {
            this.connection.open(this);
            this.isOpen = true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final long[] executeBatchWithoutQueue() throws BatchUpdateException {
        int n2 = this.currentRank;
        long l2 = 0L;
        try {
            this.setupBindBuffers(0, this.currentRank);
            try {
                this.executeForRowsWithTimeout(false);
                l2 = this.validRows;
            }
            finally {
                this.resetBindersToNull(0);
            }
            if (this.batchRowsUpdatedArray != null && this.batchRowsUpdatedArray.length > 0) {
                assert (n2 == this.batchRowsUpdatedArray.length);
                long[] lArray = this.batchRowsUpdatedArray;
                return lArray;
            }
            long[] lArray = new long[n2];
            Arrays.fill(lArray, -2L);
            long[] lArray2 = lArray;
            return lArray2;
        }
        catch (SQLException sQLException) {
            throw this.generateBatchUpdateException(sQLException, new long[n2], 0);
        }
        finally {
            this.setValidRowsAfterExecuteBatch(l2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final long[] executeBatchFromQueue() throws BatchUpdateException {
        long[] lArray = new long[this.currentRank];
        int n2 = 0;
        long l2 = 0L;
        try {
            Object object;
            if (this.currentRank > this.firstRowInBatch) {
                this.enqueueCurrentBatch(true);
            }
            boolean bl = this.needToParse;
            do {
                object = this.batchFIFOFront;
                this.prepareForExecuteBatchFromQueue((BatchFIFONode)object);
                try {
                    this.executeForRowsWithTimeout(false);
                }
                finally {
                    this.resetBindersToNull(((BatchFIFONode)object).first_row_in_batch);
                }
                l2 += this.validRows;
                if (this.batchRowsUpdatedArray != null && this.batchRowsUpdatedArray.length > 0) {
                    this.copyBatchRowsUpdatedArray(lArray);
                } else if (this.sqlKind.isPlsqlOrCall()) {
                    lArray[n2++] = this.validRows;
                } else {
                    for (int i2 = 0; i2 < this.currentRank; ++i2) {
                        lArray[this.firstRowInBatch + i2] = -2L;
                    }
                }
                this.batchFIFOFront = ((BatchFIFONode)object).next;
            } while (this.batchFIFOFront != null);
            this.batchFIFOBack = null;
            this.firstRowInBatch = 0;
            this.needToParse = bl;
            object = lArray;
            return object;
        }
        catch (SQLException sQLException) {
            throw this.generateBatchUpdateException(sQLException, lArray, n2);
        }
        finally {
            this.setValidRowsAfterExecuteBatch(l2);
        }
    }

    private final void prepareForExecuteBatchFromQueue(BatchFIFONode batchFIFONode) throws SQLException {
        this.currentBatchByteLens = batchFIFONode.currentBatchByteLens;
        this.lastBoundByteLens = batchFIFONode.lastBoundByteLens;
        this.currentBatchCharLens = batchFIFONode.currentBatchCharLens;
        this.lastBoundCharLens = batchFIFONode.lastBoundCharLens;
        this.lastBoundNeeded = batchFIFONode.lastBoundNeeded;
        this.currentBatchBindAccessors = batchFIFONode.currentBatchBindAccessors;
        this.needToParse = batchFIFONode.need_to_parse;
        this.currentBatchNeedToPrepareBinds = batchFIFONode.current_batch_need_to_prepare_binds;
        this.firstRowInBatch = batchFIFONode.first_row_in_batch;
        this.setupBindBuffers(batchFIFONode.first_row_in_batch, batchFIFONode.number_of_rows_to_be_bound);
        this.currentRank = batchFIFONode.number_of_rows_to_be_bound;
    }

    private final void copyBatchRowsUpdatedArray(long[] lArray) {
        if (this.batchRowsUpdatedArray.length < this.currentRank) {
            int n2 = this.batchRowsUpdatedArray.length;
            int n3 = this.firstRowInBatch + n2;
            int n4 = n3 + (this.currentRank - n2);
            System.arraycopy(this.batchRowsUpdatedArray, 0, lArray, this.firstRowInBatch, n2);
            for (int i2 = n3; i2 < n4; ++i2) {
                lArray[i2] = -3L;
            }
        } else {
            System.arraycopy(this.batchRowsUpdatedArray, 0, lArray, this.firstRowInBatch, this.currentRank);
        }
    }

    private final void setValidRowsAfterExecuteBatch(long l2) {
        if (this.sqlKind.isPlsqlOrCall() || l2 > this.validRows) {
            this.validRows = l2;
        }
    }

    private final void handleExecuteBatchFailure() throws SQLException {
        this.needToParse = true;
        this.clearBatchCritical();
        this.resetCurrentRowBinders();
    }

    private final void handleExecuteBatchCompletionAlways() throws SQLException {
        this.checkValidRowsStatus();
        this.currentBatchAccumulatedBindsSize = 0;
        this.currentRank = 0;
    }

    private final void handleExecuteBatchCompletion(long[] lArray) throws SQLException {
        if (this.validRows < 0L) {
            for (int i2 = 0; i2 < lArray.length; ++i2) {
                lArray[i2] = -3L;
            }
            throw (SQLException)DatabaseError.createBatchUpdateException(81, 0, lArray).fillInStackTrace();
        }
    }

    private final BatchUpdateException generateBatchUpdateException(SQLException sQLException, long[] lArray, int n2) {
        int n3;
        int n4;
        long[] lArray2;
        if (this.sqlKind.isPlsqlOrCall()) {
            lArray2 = lArray;
            n4 = n2;
        } else {
            lArray2 = this.generateFailedBatchResults(lArray);
            n4 = lArray2.length;
        }
        String string = null;
        if (this instanceof T4CDirectPathPreparedStatement && (n3 = ((T4CDirectPathPreparedStatement)this).getErrorRowNumber()) > 0) {
            string = " Row number " + n3 + " causes batch load failure.";
        }
        BatchUpdateException batchUpdateException = DatabaseError.createBatchUpdateException(sQLException, string, n4, lArray2);
        batchUpdateException.fillInStackTrace();
        return batchUpdateException;
    }

    private final long[] generateFailedBatchResults(long[] lArray) {
        long[] lArray2;
        if (this.batchRowsUpdatedArray != null) {
            if (this.firstRowInBatch == 0) {
                lArray2 = this.batchRowsUpdatedArray;
                if (this.indexOfFailedElementsInBatch != null) {
                    for (int i2 = 0; i2 < this.indexOfFailedElementsInBatch.length; ++i2) {
                        lArray2[this.indexOfFailedElementsInBatch[i2]] = -3L;
                    }
                }
            } else {
                long[] lArray3 = new long[this.firstRowInBatch + this.batchRowsUpdatedArray.length];
                System.arraycopy(lArray, 0, lArray3, 0, this.firstRowInBatch);
                System.arraycopy(this.batchRowsUpdatedArray, 0, lArray3, this.firstRowInBatch, this.batchRowsUpdatedArray.length);
                lArray2 = lArray3;
            }
        } else if (this.numberOfExecutedElementsInBatch != -1 && this.numberOfExecutedElementsInBatch != this.currentRank) {
            lArray2 = new long[this.numberOfExecutedElementsInBatch];
            for (int i3 = 0; i3 < this.numberOfExecutedElementsInBatch; ++i3) {
                lArray2[i3] = -2L;
            }
        } else {
            for (int i4 = 0; i4 < lArray.length; ++i4) {
                lArray[i4] = -3L;
            }
            lArray2 = lArray;
        }
        return lArray2;
    }

    void enqueueCurrentBatch(boolean bl) throws SQLException {
        BatchFIFONode batchFIFONode = new BatchFIFONode();
        batchFIFONode.counter = batchQueueCounter++;
        batchFIFONode.currentBatchByteLens = new int[this.numberOfBindPositions];
        System.arraycopy(this.currentBatchByteLens, 0, batchFIFONode.currentBatchByteLens, 0, this.numberOfBindPositions);
        batchFIFONode.currentBatchCharLens = new int[this.numberOfBindPositions];
        System.arraycopy(this.currentBatchCharLens, 0, batchFIFONode.currentBatchCharLens, 0, this.numberOfBindPositions);
        batchFIFONode.lastBoundByteLens = new int[this.numberOfBindPositions];
        System.arraycopy(this.lastBoundByteLens, 0, batchFIFONode.lastBoundByteLens, 0, this.numberOfBindPositions);
        batchFIFONode.lastBoundCharLens = new int[this.numberOfBindPositions];
        System.arraycopy(this.lastBoundCharLens, 0, batchFIFONode.lastBoundCharLens, 0, this.numberOfBindPositions);
        if (this.currentBatchBindAccessors != null) {
            batchFIFONode.currentBatchBindAccessors = new Accessor[this.numberOfBindPositions];
            System.arraycopy(this.currentBatchBindAccessors, 0, batchFIFONode.currentBatchBindAccessors, 0, this.numberOfBindPositions);
        }
        batchFIFONode.lastBoundNeeded = this.lastBoundNeeded;
        batchFIFONode.need_to_parse = this.needToParse;
        batchFIFONode.current_batch_need_to_prepare_binds = this.currentBatchNeedToPrepareBinds;
        batchFIFONode.first_row_in_batch = this.firstRowInBatch;
        batchFIFONode.number_of_rows_to_be_bound = this.currentRank - this.firstRowInBatch;
        if (this.batchFIFOFront == null) {
            this.batchFIFOFront = batchFIFONode;
        } else {
            this.batchFIFOBack.next = batchFIFONode;
        }
        this.batchFIFOBack = batchFIFONode;
        this.currentBatchAccumulatedBindsSize = 0;
        if (!bl) {
            int n2;
            int[] nArray = this.currentBatchByteLens;
            this.currentBatchByteLens = this.lastBoundByteLens;
            this.lastBoundByteLens = nArray;
            for (n2 = 0; n2 < this.numberOfBindPositions; ++n2) {
                this.currentBatchByteLens[n2] = 0;
            }
            nArray = this.currentBatchCharLens;
            this.currentBatchCharLens = this.lastBoundCharLens;
            this.lastBoundCharLens = nArray;
            for (n2 = 0; n2 < this.numberOfBindPositions; ++n2) {
                this.currentBatchCharLens[n2] = 0;
            }
            this.firstRowInBatch = this.currentRank;
        }
    }

    long doScrollPstmtExecuteUpdate() throws SQLException {
        this.doScrollExecuteCommon();
        if (this.sqlKind.isSELECT()) {
            this.scrollRsetTypeSolved = true;
        }
        return this.validRows;
    }

    @Override
    public int copyBinds(Statement statement, int n2) throws SQLException {
        if (this.numberOfBindPositions > 0) {
            OraclePreparedStatement oraclePreparedStatement = (OraclePreparedStatement)statement;
            int n3 = this.bindIndicatorSubRange + 5;
            int n4 = this.bindByteSubRange;
            int n5 = this.bindCharSubRange;
            int n6 = this.indicatorsOffset;
            int n7 = this.valueLengthsOffset;
            for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
                short s2 = this.bindIndicators[n3 + 0];
                int n8 = this.bindIndicators[n3 + 1];
                int n9 = this.bindIndicators[n3 + 2];
                int n10 = i2 + n2;
                if (this.bindIndicators[n6] == -1) {
                    oraclePreparedStatement.currentRowBinders[n10] = this.copiedNullBinder(s2, n8);
                    if (n9 > 0) {
                        oraclePreparedStatement.currentRowCharLens[n10] = 1;
                    }
                } else if (s2 == 109 || s2 == 111) {
                    Binder binder = this.lastBinders[i2];
                    oraclePreparedStatement.currentRowBinders[n10] = s2 == 109 ? new NamedTypeBinder(((NamedTypeBinder)binder).paramVal, ((NamedTypeBinder)binder).paramOtype) : new RefTypeBinder(((NamedTypeBinder)binder).paramVal, ((NamedTypeBinder)binder).paramOtype);
                    oraclePreparedStatement.currentRowByteLens[n10] = n8;
                } else if (this.bindUseDBA) {
                    oraclePreparedStatement.currentRowBinders[n10] = this.copiedDataBinder(s2, this.bindData, this.bindDataOffsets[i2], this.bindDataLengths[i2]);
                    oraclePreparedStatement.currentRowDataLengths[n10] = this.bindDataLengths[i2];
                    oraclePreparedStatement.currentRowFormOfUse[n10] = this.currentRowFormOfUse[i2];
                } else if (n8 > 0) {
                    oraclePreparedStatement.currentRowBinders[n10] = this.copiedByteBinder(s2, this.bindBytes, n4, n8, this.bindIndicators[n7]);
                    oraclePreparedStatement.currentRowByteLens[n10] = n8;
                } else if (n9 > 0) {
                    oraclePreparedStatement.currentRowBinders[n10] = this.copiedCharBinder(s2, this.bindChars, n5, n9, this.bindIndicators[n7], this.getInoutIndicator(i2));
                    oraclePreparedStatement.currentRowCharLens[n10] = n9;
                } else {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89, "copyBinds doesn't understand type " + s2).fillInStackTrace();
                }
                n4 += this.bindBufferCapacity * n8;
                n5 += this.bindBufferCapacity * n9;
                n6 += this.numberOfBindRowsAllocated;
                n7 += this.numberOfBindRowsAllocated;
                n3 += 10;
            }
        }
        return this.numberOfBindPositions;
    }

    Binder copiedNullBinder(short s2, int n2) throws SQLException {
        return new CopiedNullBinder(s2, n2);
    }

    Binder copiedDataBinder(short s2, ByteArray byteArray, long l2, int n2) throws SQLException {
        byte[] byArray = byteArray.get(l2, n2);
        return new CopiedDataBinder(s2, byArray, n2);
    }

    Binder copiedByteBinder(short s2, byte[] byArray, int n2, int n3, short s3) throws SQLException {
        byte[] byArray2 = new byte[n3];
        System.arraycopy(byArray, n2, byArray2, 0, n3);
        return new CopiedByteBinder(s2, n3, byArray2, s3);
    }

    Binder copiedCharBinder(short s2, char[] cArray, int n2, int n3, short s3, short s4) throws SQLException {
        char[] cArray2 = new char[n3];
        System.arraycopy(cArray, n2, cArray2, 0, n3);
        return new CopiedCharBinder(s2, cArray2, s3, s4);
    }

    @Override
    protected void hardClose() throws SQLException {
        super.hardClose();
        this.releaseBuffers();
        if (!this.connection.isClosed()) {
            this.cleanAllTempLobs();
        }
        this.lastBoundBytes = null;
        this.lastBoundChars = null;
    }

    @Override
    protected void alwaysOnClose() throws SQLException {
        if (this.currentRank > 0 && this.batchUpdate) {
            this.clearBatchCritical();
        }
        if (this.sqlKind.isSELECT()) {
            OracleStatement oracleStatement = this.children;
            while (oracleStatement != null) {
                OracleStatement oracleStatement2 = oracleStatement.nextChild;
                if (oracleStatement.serverCursor) {
                    oracleStatement.cursorId = 0;
                }
                oracleStatement = oracleStatement2;
            }
        }
        super.alwaysOnClose();
    }

    @Override
    public void setDisableStmtCaching(boolean bl) {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (bl) {
                this.cacheState = 3;
            }
        }
    }

    @Override
    public void setFormOfUse(int n2, short s2) {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            try {
                this.setFormOfUseInternal(n2, s2, true);
            }
            catch (SQLException sQLException) {
                throw new RuntimeException(sQLException.getMessage());
            }
        }
    }

    void setFormOfUseInternal(int n2, short s2) throws SQLException {
        this.setFormOfUseInternal(n2, s2, false);
    }

    private void setFormOfUseInternal(int n2, short s2, boolean bl) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (bl) {
            this.currentRowFormOfUseSet[n3] = true;
        }
        if (this.currentRowFormOfUse[n3] != s2 && (!this.currentRowFormOfUseSet[n3] || bl)) {
            Accessor accessor;
            this.currentRowFormOfUse[n3] = s2;
            if (this.currentRowBindAccessors != null && (accessor = this.currentRowBindAccessors[n3]) != null) {
                accessor.setFormOfUse(s2);
            }
            if (this.accessors != null && (accessor = this.accessors[n3]) != null) {
                accessor.setFormOfUse(s2);
            }
        }
    }

    @Override
    public void setURL(int n2, URL uRL) throws SQLException {
        this.setURLInternal(n2, uRL);
    }

    void setURLInternal(int n2, URL uRL) throws SQLException {
        if (uRL == null) {
            this.setNull(n2, 1);
        } else {
            this.setStringInternal(n2, uRL.toString());
        }
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        this.connection.beginNonRequestCalls();
        try {
            this.ensureOpen();
            ParameterMetaData parameterMetaData = OracleParameterMetaData.getParameterMetaData(this.sqlObject, this.connection, this);
            return parameterMetaData;
        }
        finally {
            this.connection.endNonRequestCalls();
        }
    }

    @Override
    public oracle.jdbc.OracleParameterMetaData OracleGetParameterMetaData() throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("OracleGetParameterMetaData").fillInStackTrace();
    }

    @Override
    public void registerReturnParameter(int n2, int n3) throws SQLException {
        int n4;
        if (this.numberOfBindPositions <= 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90).fillInStackTrace();
        }
        if (this.numReturnParams <= 0) {
            this.numReturnParams = this.sqlObject.getReturnParameterCount();
            if (this.numReturnParams <= 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90).fillInStackTrace();
            }
        }
        if ((n4 = n2 - 1) < this.numberOfBindPositions - this.numReturnParams || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        int n5 = this.getInternalTypeForDmlReturning(n3);
        if (n5 == 111 || n5 == 109 && n3 != 2007 && n3 != 2009) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "Use registerReturnParameter(int paramIndex, int externalType, String typeName) for user-defined or REF types").fillInStackTrace();
        }
        short s2 = 0;
        if (this.currentRowFormOfUse != null && this.currentRowFormOfUse[n4] != 0) {
            s2 = this.currentRowFormOfUse[n4];
        }
        this.registerReturnParameterInternal(n4, n5, n3, -1, s2, null);
        this.currentRowBinders[n4] = this.theReturnParamBinder;
    }

    @Override
    public void registerReturnParameter(int n2, int n3, int n4) throws SQLException {
        if (this.numberOfBindPositions <= 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90).fillInStackTrace();
        }
        int n5 = n2 - 1;
        if (n5 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (n3 != 1 && n3 != 12 && n3 != -1 && n3 != -2 && n3 != -3 && n3 != -4 && n3 != 12) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        if (n4 <= 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        int n6 = this.getInternalTypeForDmlReturning(n3);
        short s2 = 0;
        if (this.currentRowFormOfUse != null && this.currentRowFormOfUse[n5] != 0) {
            s2 = this.currentRowFormOfUse[n5];
        }
        this.registerReturnParameterInternal(n5, n6, n3, n4, s2, null);
        this.currentRowBinders[n5] = this.theReturnParamBinder;
    }

    @Override
    public void registerReturnParameter(int n2, int n3, String string) throws SQLException {
        if (this.numberOfBindPositions <= 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90).fillInStackTrace();
        }
        int n4 = n2 - 1;
        if (n4 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        int n5 = this.getInternalTypeForDmlReturning(n3);
        if (n5 != 111 && n5 != 109) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        this.registerReturnParameterInternal(n4, n5, n3, -1, (short)0, string);
        this.currentRowBinders[n4] = this.theReturnParamBinder;
    }

    @Override
    public ResultSet getReturnResultSet() throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.accessors == null || this.numReturnParams == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 144).fillInStackTrace();
        }
        if (this.currentResultSet == null || this.numReturnParams == 0 || !this.isOpen) {
            this.isAllFetched = true;
            this.currentResultSet = this.createResultSet();
        }
        return this.currentResultSet;
    }

    int getInternalTypeForDmlReturning(int n2) throws SQLException {
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
            case -15: 
            case 1: {
                n3 = 96;
                break;
            }
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
            case 70: {
                n3 = 1;
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
            }
        }
        return n3;
    }

    private void initializeAutoKeyInfo() throws SQLException {
        if (this.isAutoGeneratedKey && !this.autoKeyInfo.isInitialized()) {
            this.autoKeyInfo.initialize(this.connection);
            this.registerReturnParamsForAutoKey();
        }
    }

    private void registerReturnParamsForAutoKey() throws SQLException {
        assert (this.autoKeyInfo.isInitialized()) : "autoKeyInfo is not initialized";
        int[] nArray = this.autoKeyInfo.returnTypes;
        short[] sArray = this.autoKeyInfo.tableFormOfUses;
        int[] nArray2 = this.autoKeyInfo.columnIndexes;
        int n2 = nArray.length;
        int n3 = this.numberOfBindPositions - n2;
        this.offsetOfFirstUserColumn = n3 - 1;
        for (int i2 = 0; i2 < n2; ++i2) {
            short s2;
            int n4 = n3 + i2;
            this.currentRowBinders[n4] = this.theReturnParamBinder;
            short s3 = s2 = this.connection.defaultnchar ? (short)2 : 1;
            if (sArray != null && nArray2 != null && sArray[nArray2[i2] - 1] == 2) {
                s2 = 2;
                this.setFormOfUseInternal(n4 + 1, s2);
            }
            this.checkTypeForAutoKey(nArray[i2]);
            String string = null;
            if (nArray[i2] == 111) {
                string = this.autoKeyInfo.tableTypeNames[nArray2[i2] - 1];
            }
            this.registerReturnParameterInternal(n4, nArray[i2], nArray[i2], -1, s2, string);
        }
    }

    @Override
    void resetOnExceptionDuringExecute() {
        super.resetOnExceptionDuringExecute();
        this.currentRank = 0;
        this.currentBatchNeedToPrepareBinds = true;
    }

    @Override
    void resetCurrentRowBinders() {
        Binder[] binderArray = this.currentRowBinders;
        if (this.binders != null && this.currentRowBinders != null && binderArray != this.binders[0]) {
            this.currentRowBinders = this.binders[0];
            this.binders[this.numberOfBoundRows] = binderArray;
        }
    }

    void resetBindersToNull(int n2) {
    }

    boolean isThinDriver() {
        return true;
    }

    CharacterSet getCharacterSetForBind(int n2, short s2) {
        return s2 == 1 ? this.connection.conversion.clientCharSet : this.connection.conversion.serverNCharSet;
    }

    int getConversionCodeForAsciiStream(int n2) {
        return 5;
    }

    int getConversionCodeForCharacterStream(int n2) {
        return 7;
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream) throws SQLException {
        this.setAsciiStreamInternal(n2, inputStream);
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream, long l2) throws SQLException {
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setAsciiStream cannot be negative").fillInStackTrace();
        }
        this.setAsciiStreamInternal(n2, inputStream, l2);
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream) throws SQLException {
        this.setBinaryStreamInternal(n2, inputStream);
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream, long l2) throws SQLException {
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setBinaryStream cannot be negative").fillInStackTrace();
        }
        this.setBinaryStreamInternal(n2, inputStream, l2);
    }

    @Override
    public void setBlob(int n2, InputStream inputStream) throws SQLException {
        this.setBlobInternal(n2, inputStream);
    }

    @Override
    public void setBlob(int n2, InputStream inputStream, long l2) throws SQLException {
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setBlob cannot be negative").fillInStackTrace();
        }
        this.setBlobInternal(n2, inputStream, l2);
    }

    @Override
    public void setCharacterStream(int n2, Reader reader) throws SQLException {
        this.setCharacterStreamInternal(n2, reader);
    }

    @Override
    public void setCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setCharacterStream cannot be negative").fillInStackTrace();
        }
        this.setCharacterStreamInternal(n2, reader, l2);
    }

    @Override
    public void setClob(int n2, Reader reader, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (l2 < 0L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setClob() cannot be negative").fillInStackTrace();
            }
            this.setFormOfUseInternal(n2, this.defaultFormOfUse);
            this.setClobInternal(n2, reader, l2);
        }
    }

    @Override
    public void setClob(int n2, Reader reader) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setFormOfUseInternal(n2, this.defaultFormOfUse);
            this.setClobInternal(n2, reader);
        }
    }

    @Override
    public void setRowId(int n2, RowId rowId) throws SQLException {
        this.setRowIdInternal(n2, rowId);
    }

    @Override
    public void setNCharacterStream(int n2, Reader reader) throws SQLException {
        this.setNCharacterStreamInternal(n2, reader);
    }

    @Override
    public void setNCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setNCharacterStream cannot be negative").fillInStackTrace();
        }
        this.setNCharacterStreamInternal(n2, reader, l2);
    }

    @Override
    public void setNClob(int n2, NClob nClob) throws SQLException {
        this.setNClobInternal(n2, nClob);
    }

    @Override
    public void setNClob(int n2, Reader reader, long l2) throws SQLException {
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setNClob cannot be negative").fillInStackTrace();
        }
        this.setNClobInternal(n2, reader, l2);
    }

    @Override
    public void setNClob(int n2, Reader reader) throws SQLException {
        this.setNClobInternal(n2, reader);
    }

    @Override
    public void setSQLXML(int n2, SQLXML sQLXML) throws SQLException {
        this.setSQLXMLInternal(n2, sQLXML);
    }

    @Override
    public void setNString(int n2, String string) throws SQLException {
        this.setNStringInternal(n2, string);
    }

    void setAsciiStreamInternal(int n2, InputStream inputStream) throws SQLException {
        this.setAsciiStreamInternal(n2, inputStream, 0L, false);
    }

    void setAsciiStreamInternal(int n2, InputStream inputStream, long l2) throws SQLException {
        this.setAsciiStreamInternal(n2, inputStream, l2, true);
    }

    void setBinaryStreamInternal(int n2, InputStream inputStream) throws SQLException {
        this.setBinaryStreamInternal(n2, inputStream, 0L, false);
    }

    void setBinaryStreamInternal(int n2, InputStream inputStream, long l2) throws SQLException {
        this.setBinaryStreamInternal(n2, inputStream, l2, true);
    }

    void setBlobInternal(int n2, InputStream inputStream, long l2) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (inputStream == null) {
            this.setNullInternal(n2, 2004);
        } else {
            this.setBinaryStreamContentsForBlobCritical(n2, inputStream, l2, l2 != -1L);
        }
    }

    void setBlobInternal(int n2, InputStream inputStream) throws SQLException {
        this.setBlobInternal(n2, inputStream, -1L);
    }

    void setCharacterStreamInternal(int n2, Reader reader) throws SQLException {
        this.setCharacterStreamInternal(n2, reader, 0L, false);
    }

    void setCharacterStreamInternal(int n2, Reader reader, long l2) throws SQLException {
        this.setCharacterStreamInternal(n2, reader, l2, true);
    }

    void setClobInternal(int n2, Reader reader) throws SQLException {
        this.setClobInternal(n2, reader, -1L);
    }

    void setClobInternal(int n2, Reader reader, long l2) throws SQLException {
        int n3 = n2 - 1;
        if (n3 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (reader == null) {
            this.setNullInternal(n2, 2005);
        } else {
            this.setReaderContentsForClobCritical(n2, reader, l2, l2 != -1L);
        }
    }

    void setNCharacterStreamInternal(int n2, Reader reader) throws SQLException {
        if (n2 < 1 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.setFormOfUseInternal(n2, (short)2);
        this.setCharacterStreamInternal(n2, reader, 0L, false);
    }

    void setNCharacterStreamInternal(int n2, Reader reader, long l2) throws SQLException {
        if (n2 < 1 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.setFormOfUseInternal(n2, (short)2);
        this.setCharacterStreamInternal(n2, reader, l2);
    }

    void setNClobInternal(int n2, NClob nClob) throws SQLException {
        if (n2 < 1 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.setFormOfUseInternal(n2, (short)2);
        this.setClobInternal(n2, nClob);
    }

    void setNClobInternal(int n2, Reader reader) throws SQLException {
        if (n2 < 1 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.setFormOfUseInternal(n2, (short)2);
        this.setClobInternal(n2, reader);
    }

    void setNClobInternal(int n2, Reader reader, long l2) throws SQLException {
        if (n2 < 1 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 43).fillInStackTrace();
        }
        this.setFormOfUseInternal(n2, (short)2);
        this.setClobInternal(n2, reader, l2);
    }

    void setNStringInternal(int n2, String string) throws SQLException {
        if (n2 < 1 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.setFormOfUseInternal(n2, (short)2);
        this.setStringInternal(n2, string);
    }

    void setRowIdInternal(int n2, RowId rowId) throws SQLException {
        this.setROWIDInternal(n2, (ROWID)rowId);
    }

    void setTTCData(int n2, TTCData tTCData) throws SQLException {
        byte[] byArray;
        if (tTCData == null || (byArray = tTCData.getTTCEncoding()) == null) {
            this.setNullInternal(n2, 0);
            return;
        }
        if (n2 < 1 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.setFormOfUseInternal(n2, tTCData.getFormOfUse());
        DatumBinder datumBinder = new DatumBinder(byArray){
            Binder copyingBinder;
            {
                this.copyingBinder = null;
            }

            @Override
            Binder copyingBinder() {
                if (this.copyingBinder == null) {
                    this.copyingBinder = new ByteCopyingBinder(){};
                    this.copyingBinder.type = this.type;
                }
                return this.copyingBinder;
            }
        };
        datumBinder.type = tTCData.getTTCType();
        datumBinder.bytelen = byArray.length;
        int n3 = n2 - 1;
        this.currentRowBinders[n3] = datumBinder;
        this.currentRowByteLens[n3] = byArray.length;
        this.currentRowCharLens[n3] = 0;
    }

    @Override
    public void setObject(int n2, Object object, SQLType sQLType) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        this.setObject(n2, object, sQLType, 0);
    }

    @Override
    public void setObject(int n2, Object object, SQLType sQLType, int n3) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        if (object == null) {
            this.setObject(n2, object, sQLType.getVendorTypeNumber(), n3);
        } else if (sQLType == JDBCType.DATE) {
            DATE dATE = null;
            dATE = object instanceof DATE ? (DATE)object : (object instanceof Date ? new DATE(object, this.getDefaultCalendar()) : (object instanceof Timestamp ? new DATE((Timestamp)object) : (object instanceof String ? new DATE(Date.valueOf((String)object)) : JavaToJavaConverter.convert(object, DATE.class, this.connection, n3, null))));
            this.setDATE(n2, dATE.zeroTime());
        } else {
            this.setObject(n2, object, sQLType.getVendorTypeNumber(), n3);
        }
    }

    @Override
    public void setArrayAtName(String string, java.sql.Array array) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setArray(i2 + 1, array);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBigDecimalAtName(String string, BigDecimal bigDecimal) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBigDecimal(i2 + 1, bigDecimal);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBlobAtName(String string, Blob blob) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBlob(i2 + 1, blob);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBooleanAtName(String string, boolean bl) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl2 = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBoolean(i2 + 1, bl);
            bl2 = true;
        }
        if (!bl2) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setByteAtName(String string, byte by) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setByte(i2 + 1, by);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBytesAtName(String string, byte[] byArray) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBytes(i2 + 1, byArray);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setClobAtName(String string, Clob clob) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setClob(i2 + 1, clob);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setDateAtName(String string, Date date) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setDate(i2 + 1, date);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setDateAtName(String string, Date date, Calendar calendar) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setDate(i2 + 1, date, calendar);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setDoubleAtName(String string, double d2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setDouble(i2 + 1, d2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setFloatAtName(String string, float f2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setFloat(i2 + 1, f2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setIntAtName(String string, int n2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n3 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n3; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setInt(i2 + 1, n2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setLongAtName(String string, long l2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setLong(i2 + 1, l2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setNClobAtName(String string, NClob nClob) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setNClob(i2 + 1, nClob);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setNStringAtName(String string, String string2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string3 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string3) continue;
            this.setNString(i2 + 1, string2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setObjectAtName(String string, Object object) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setObject(i2 + 1, object);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setObjectAtName(String string, Object object, int n2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n3 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n3; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setObject(i2 + 1, object, n2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setRefAtName(String string, Ref ref) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setRef(i2 + 1, ref);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setRowIdAtName(String string, RowId rowId) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setRowId(i2 + 1, rowId);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setShortAtName(String string, short s2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setShort(i2 + 1, s2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setSQLXMLAtName(String string, SQLXML sQLXML) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setSQLXML(i2 + 1, sQLXML);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setStringAtName(String string, String string2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string3 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string3) continue;
            this.setString(i2 + 1, string2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setTimeAtName(String string, Time time) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setTime(i2 + 1, time);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setTimeAtName(String string, Time time, Calendar calendar) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setTime(i2 + 1, time, calendar);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setTimestampAtName(String string, Timestamp timestamp) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setTimestamp(i2 + 1, timestamp);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setTimestampAtName(String string, Timestamp timestamp, Calendar calendar) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setTimestamp(i2 + 1, timestamp, calendar);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setURLAtName(String string, URL uRL) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setURL(i2 + 1, uRL);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setARRAYAtName(String string, ARRAY aRRAY) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setARRAY(i2 + 1, aRRAY);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBFILEAtName(String string, BFILE bFILE) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBFILE(i2 + 1, bFILE);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBfileAtName(String string, BFILE bFILE) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBfile(i2 + 1, bFILE);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBinaryFloatAtName(String string, float f2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBinaryFloat(i2 + 1, f2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBinaryFloatAtName(String string, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBinaryFloat(i2 + 1, bINARY_FLOAT);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBinaryDoubleAtName(String string, double d2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBinaryDouble(i2 + 1, d2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBinaryDoubleAtName(String string, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBinaryDouble(i2 + 1, bINARY_DOUBLE);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBLOBAtName(String string, BLOB bLOB) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setBLOB(i2 + 1, bLOB);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setCHARAtName(String string, CHAR cHAR) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setCHAR(i2 + 1, cHAR);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setCLOBAtName(String string, CLOB cLOB) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setCLOB(i2 + 1, cLOB);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setCursorAtName(String string, ResultSet resultSet) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setCursor(i2 + 1, resultSet);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setDATEAtName(String string, DATE dATE) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setDATE(i2 + 1, dATE);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setFixedCHARAtName(String string, String string2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string3 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string3) continue;
            this.setFixedCHAR(i2 + 1, string2);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setINTERVALDSAtName(String string, INTERVALDS iNTERVALDS) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setINTERVALDS(i2 + 1, iNTERVALDS);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setINTERVALYMAtName(String string, INTERVALYM iNTERVALYM) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setINTERVALYM(i2 + 1, iNTERVALYM);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setNUMBERAtName(String string, NUMBER nUMBER) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setNUMBER(i2 + 1, nUMBER);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setOPAQUEAtName(String string, OPAQUE oPAQUE) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setOPAQUE(i2 + 1, oPAQUE);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setOracleObjectAtName(String string, Datum datum) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setOracleObject(i2 + 1, datum);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setORADataAtName(String string, ORAData oRAData) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setORAData(i2 + 1, oRAData);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setRAWAtName(String string, RAW rAW) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setRAW(i2 + 1, rAW);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setREFAtName(String string, REF rEF) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setREF(i2 + 1, rEF);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setRefTypeAtName(String string, REF rEF) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setRefType(i2 + 1, rEF);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setROWIDAtName(String string, ROWID rOWID) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setROWID(i2 + 1, rOWID);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setSTRUCTAtName(String string, STRUCT sTRUCT) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setSTRUCT(i2 + 1, sTRUCT);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setTIMESTAMPLTZAtName(String string, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setTIMESTAMPLTZ(i2 + 1, tIMESTAMPLTZ);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setTIMESTAMPTZAtName(String string, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setTIMESTAMPTZ(i2 + 1, tIMESTAMPTZ);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setTIMESTAMPAtName(String string, TIMESTAMP tIMESTAMP) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setTIMESTAMP(i2 + 1, tIMESTAMP);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setCustomDatumAtName(String string, CustomDatum customDatum) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        boolean bl = false;
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            this.setCustomDatum(i2 + 1, customDatum);
            bl = true;
        }
        if (!bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBlobAtName(String string, InputStream inputStream) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setBlob(i2 + 1, inputStream);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBlobAtName(String string, InputStream inputStream, long l2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setBlob(i2 + 1, inputStream, l2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setClobAtName(String string, Reader reader) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setClob(i2 + 1, reader);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setClobAtName(String string, Reader reader, long l2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setClob(i2 + 1, reader, l2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setNClobAtName(String string, Reader reader) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setNClob(i2 + 1, reader);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setNClobAtName(String string, Reader reader, long l2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setNClob(i2 + 1, reader, l2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream inputStream) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setAsciiStream(i2 + 1, inputStream);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream inputStream, int n2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n3 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n3; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setAsciiStream(i2 + 1, inputStream, n2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setAsciiStreamAtName(String string, InputStream inputStream, long l2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setAsciiStream(i2 + 1, inputStream, l2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream inputStream) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setBinaryStream(i2 + 1, inputStream);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream inputStream, int n2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n3 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n3; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setBinaryStream(i2 + 1, inputStream, n2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setBinaryStreamAtName(String string, InputStream inputStream, long l2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setBinaryStream(i2 + 1, inputStream, l2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setCharacterStream(i2 + 1, reader);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader, int n2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n3 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n3; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setCharacterStream(i2 + 1, reader, n2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setCharacterStreamAtName(String string, Reader reader, long l2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setCharacterStream(i2 + 1, reader, l2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setNCharacterStreamAtName(String string, Reader reader) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setNCharacterStream(i2 + 1, reader);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setNCharacterStreamAtName(String string, Reader reader, long l2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n2 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setNCharacterStream(i2 + 1, reader, l2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void setUnicodeStreamAtName(String string, InputStream inputStream, int n2) throws SQLException {
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n3 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n3; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.setUnicodeStream(i2 + 1, inputStream, n2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    private static final class Pair
    implements Comparable<Pair> {
        private final int index;
        private final long offset;

        Pair(int n2, long l2) {
            this.index = n2;
            this.offset = l2;
        }

        @Override
        public int compareTo(Pair pair) {
            return this.offset == pair.offset ? 0 : (this.offset < pair.offset ? -1 : 1);
        }

        public int getIndex() {
            return this.index;
        }
    }

    class BatchFIFONode {
        int counter;
        int[] currentBatchByteLens;
        int[] currentBatchCharLens;
        int[] lastBoundByteLens;
        int[] lastBoundCharLens;
        Accessor[] currentBatchBindAccessors;
        boolean lastBoundNeeded;
        boolean need_to_parse;
        boolean current_batch_need_to_prepare_binds;
        int first_row_in_batch;
        int number_of_rows_to_be_bound;
        BatchFIFONode next;

        BatchFIFONode() {
        }
    }
}

