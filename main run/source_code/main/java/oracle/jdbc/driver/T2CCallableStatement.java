/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.ShortBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.AggregateByteArray;
import oracle.jdbc.driver.BfileAccessor;
import oracle.jdbc.driver.BinaryDoubleAccessor;
import oracle.jdbc.driver.BinaryFloatAccessor;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.BlobAccessor;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CharAccessor;
import oracle.jdbc.driver.ClioSupport;
import oracle.jdbc.driver.ClobAccessor;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.DateAccessor;
import oracle.jdbc.driver.DynamicByteArray;
import oracle.jdbc.driver.IntervaldsAccessor;
import oracle.jdbc.driver.IntervalymAccessor;
import oracle.jdbc.driver.LobCommonAccessor;
import oracle.jdbc.driver.NamedTypeAccessor;
import oracle.jdbc.driver.NumberAccessor;
import oracle.jdbc.driver.OracleCallableStatement;
import oracle.jdbc.driver.OracleLog;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.OutRawAccessor;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.PlsqlIbtBindInfo;
import oracle.jdbc.driver.PlsqlIndexTableAccessor;
import oracle.jdbc.driver.RawAccessor;
import oracle.jdbc.driver.RefTypeAccessor;
import oracle.jdbc.driver.ResultSetAccessor;
import oracle.jdbc.driver.RowidAccessor;
import oracle.jdbc.driver.T2CCharByteArray;
import oracle.jdbc.driver.T2CConnection;
import oracle.jdbc.driver.T2CLongAccessor;
import oracle.jdbc.driver.T2CLongRawAccessor;
import oracle.jdbc.driver.T2CPlsqlIndexTableAccessor;
import oracle.jdbc.driver.T2CResultSetAccessor;
import oracle.jdbc.driver.T2CStatement;
import oracle.jdbc.driver.T2CVarcharAccessor;
import oracle.jdbc.driver.TimestampAccessor;
import oracle.jdbc.driver.TimestampltzAccessor;
import oracle.jdbc.driver.TimestamptzAccessor;
import oracle.jdbc.driver.VarcharAccessor;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Log;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleTypeADT;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.OCI_INTERNAL})
class T2CCallableStatement
extends OracleCallableStatement {
    T2CConnection t2cConnection = null;
    int userResultSetType = -1;
    int userResultSetConcur = -1;
    static int T2C_EXTEND_BUFFER = -3;
    long[] t2cOutput = new long[10];
    long[] t2cOutputUpdateCountArray = null;
    int[] t2cOutputUpdateCountArraySize = new int[1];
    static final int T2C_OUTPUT_USE_NIO = 5;
    static final int T2C_OUTPUT_STMT_LOB_PREFETCH_SIZE = 6;
    static final int T2C_OUTPUT_USE_OCI_DEFAULT_DEFINE_OFFSET = 7;
    static final boolean T2CDEBUG = false;
    int extractedCharOffset;
    int extractedByteOffset;
    int savedRowPrefetch = 0;
    int OCIPrefetch = 1;
    static final byte T2C_LOB_PREFETCH_SIZE_THIS_COLUMN_OFFSET = 0;
    static final byte T2C_LOB_PREFETCH_LOB_LENGTH_OFFSET = 1;
    static final byte T2C_LOB_PREFETCH_FORM_OFFSET = 2;
    static final byte T2C_LOB_PREFETCH_CHUNK_OFFSET = 3;
    static final byte T2C_LOB_PREFETCH_DATA_OFFSET = 4;
    byte[] lobPrefetchTempBytes;
    boolean needToRetainRows = false;
    byte[] returnParamBytes;
    char[] returnParamChars;
    short[] returnParamIndicators;
    int returnParamRowBytes;
    int returnParamRowChars;
    static int PREAMBLE_PER_POSITION = 5;
    SQLException updateDataException = null;
    int lastProcessedCell = 0;
    static final int PROCESS_DEFINE_DYNAMIC_COLUMNS = 16;
    static final int PROCESS_DEFINE_DEFAULT_COLUMNS = 32;
    static final int PROCESS_ADT_OUT_BINDS = 64;
    int lastProcessedAccessorIndex = 0;
    int accessorsProcessed = 0;
    int previousMode = 0;

    T2CCallableStatement(T2CConnection t2CConnection, String string, int n2, int n3, int n4, int n5) throws SQLException {
        super(t2CConnection, string, n2, n3, n4, n5);
        this.userResultSetType = n4;
        this.userResultSetConcur = n5;
        this.t2cConnection = t2CConnection;
        if (this.t2cConnection.useOCIDefaultDefines) {
            this.savedRowPrefetch = this.rowPrefetch;
            this.OCIPrefetch = this.rowPrefetch;
            this.rowPrefetch = 1;
        }
    }

    T2CCallableStatement(T2CConnection t2CConnection, String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        super((PhysicalConnection)t2CConnection, string, properties);
        if (properties != null) {
            String string2 = properties.getProperty("result_set_type");
            if (string2 != null) {
                this.userResultSetType = Integer.parseInt(string2);
            }
            if ((string2 = properties.getProperty("result_set_concurrency")) != null) {
                this.userResultSetConcur = Integer.parseInt(string2);
            }
        }
        this.t2cConnection = t2CConnection;
        if (this.t2cConnection.useOCIDefaultDefines) {
            this.savedRowPrefetch = this.rowPrefetch;
            this.OCIPrefetch = this.rowPrefetch;
            this.rowPrefetch = 1;
        }
    }

    @Override
    int getPrefetchInternal(boolean bl) {
        if (!this.t2cConnection.useOCIDefaultDefines) {
            return super.getPrefetchInternal(bl);
        }
        int n2 = bl ? this.defaultRowPrefetch : this.savedRowPrefetch;
        return n2;
    }

    @Override
    void setPrefetchInternal(int n2, boolean bl, boolean bl2) throws SQLException {
        int n3 = this.rowPrefetch;
        super.setPrefetchInternal(n2, bl, bl2);
        if (this.t2cConnection.useOCIDefaultDefines && n3 != this.rowPrefetch) {
            this.savedRowPrefetch = this.rowPrefetch;
            this.OCIPrefetch = this.rowPrefetch;
            this.rowPrefetch = 1;
        }
    }

    @Override
    void prepareForNewResults(boolean bl, boolean bl2, boolean bl3) throws SQLException {
        super.prepareForNewResults(bl, bl2, bl3);
        if (this.t2cConnection.useOCIDefaultDefines && this.rowPrefetchChanged) {
            this.savedRowPrefetch = this.rowPrefetch;
            this.OCIPrefetch = this.rowPrefetch;
            this.rowPrefetch = 1;
        }
    }

    @Override
    void prepareAccessors() throws SQLException {
        super.prepareAccessors();
        if (this.rowPrefetchChanged) {
            this.lobPrefetchMetaData = this.getLobPrefetchMetaData();
        }
        if (this.t2cConnection.useOCIDefaultDefines && this.hasStream) {
            this.savedRowPrefetch = 1;
        }
    }

    String bytes2String(byte[] byArray, int n2, int n3) throws SQLException {
        byte[] byArray2 = new byte[n3];
        System.arraycopy(byArray, n2, byArray2, 0, n3);
        return this.connection.conversion.CharBytesToString(byArray2, n3);
    }

    void processDescribeData() throws SQLException {
        this.described = true;
        this.describedWithNames = true;
        if (this.numberOfDefinePositions < 1) {
            return;
        }
        if (this.accessors == null || this.numberOfDefinePositions > this.accessors.length) {
            this.accessors = new Accessor[this.numberOfDefinePositions];
        }
        int n2 = this.t2cConnection.queryMetaData1Offset;
        int n3 = this.t2cConnection.queryMetaData2Offset;
        short[] sArray = this.t2cConnection.queryMetaData1;
        byte[] byArray = this.t2cConnection.queryMetaData2;
        int n4 = 0;
        while (n4 < this.numberOfDefinePositions) {
            Accessor accessor;
            short s2 = sArray[n2 + 0];
            short s3 = sArray[n2 + 1];
            short s4 = sArray[n2 + 11];
            boolean bl = sArray[n2 + 2] != 0;
            short s5 = sArray[n2 + 3];
            short s6 = sArray[n2 + 4];
            int n5 = 0;
            long l2 = 0L;
            int n6 = 0;
            short s7 = sArray[n2 + 5];
            short s8 = sArray[n2 + 6];
            String string = this.bytes2String(byArray, n3, s8);
            short s9 = sArray[n2 + 12];
            short s10 = sArray[n2 + 13];
            boolean bl2 = sArray[n2 + 14] != 0;
            boolean bl3 = sArray[n2 + 15] != 0;
            String string2 = null;
            String string3 = null;
            OracleTypeADT oracleTypeADT = null;
            n3 += s8;
            if (s10 > 0) {
                string2 = this.bytes2String(byArray, n3, s9);
                string3 = this.bytes2String(byArray, n3 += s9, s10);
                n3 += s10;
                string3 = PhysicalConnection.needToQuoteIdentifier(string2) || PhysicalConnection.needToQuoteIdentifier(string3) ? String.format("\"%s\".\"%s\"", string2, string3) : string2 + "." + string3;
                oracleTypeADT = new OracleTypeADT(string3, (Connection)this.connection);
                oracleTypeADT.tdoCState = ((long)sArray[n2 + 7] & 0xFFFFL) << 48 | ((long)sArray[n2 + 8] & 0xFFFFL) << 32 | ((long)sArray[n2 + 9] & 0xFFFFL) << 16 | (long)sArray[n2 + 10] & 0xFFFFL;
            }
            if ((accessor = this.accessors[n4]) == null || accessor.defineType == 0 || accessor.describeType != 0 && accessor.describeType != s2) {
                accessor = this.allocateAccessorForDefines(n4, s2, s3, bl, n5, s5, s6, l2, n6, s7, s4, oracleTypeADT, string3);
                if (this.accessors[n4] != null) {
                    accessor.rowLength = this.accessors[n4].rowLength;
                    accessor.rowOffset = this.accessors[n4].rowOffset;
                    accessor.rowNull = this.accessors[n4].rowNull;
                    accessor.rowMetadata = this.accessors[n4].rowMetadata;
                }
            } else {
                accessor.initForDescribe(s2, s3, bl, n5, s5, s6, l2, n6, s7, string3);
            }
            accessor.describeOtype = oracleTypeADT;
            accessor.columnName = string;
            accessor.columnInvisible = bl2;
            accessor.columnJSON = bl3;
            this.accessors[n4] = accessor;
            ++n4;
            n2 += 16;
        }
    }

    Accessor allocateAccessorForDefines(int n2, int n3, int n4, boolean bl, int n5, int n6, int n7, long l2, int n8, short s2, int n9, OracleTypeADT oracleTypeADT, String string) throws SQLException {
        Accessor accessor;
        switch (n3) {
            case 1: {
                accessor = new VarcharAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2, n9);
                break;
            }
            case 96: {
                accessor = new CharAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2, n9);
                break;
            }
            case 2: {
                accessor = new NumberAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 23: {
                accessor = new RawAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 100: {
                accessor = new BinaryFloatAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 101: {
                accessor = new BinaryDoubleAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 8: {
                accessor = new T2CLongAccessor(this, n2 + 1, n4, bl, n5, n6, n7, l2, n8, s2);
                this.rowPrefetch = 1;
                this.OCIPrefetch = 1;
                this.savedRowPrefetch = 1;
                break;
            }
            case 24: {
                accessor = new T2CLongRawAccessor(this, n2 + 1, n4, bl, n5, n6, n7, l2, n8, s2);
                this.rowPrefetch = 1;
                this.savedRowPrefetch = 1;
                this.OCIPrefetch = 1;
                break;
            }
            case 104: {
                accessor = new RowidAccessor(this, n4, bl, n5, n6, n7, l2, n8, 1);
                break;
            }
            case 102: 
            case 116: {
                if (this.sqlKind.isPlsqlOrCall()) {
                    accessor = new T2CResultSetAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                    break;
                }
                accessor = new ResultSetAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 12: {
                accessor = new DateAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 180: {
                accessor = new TimestampAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 181: {
                accessor = new TimestamptzAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 231: {
                accessor = new TimestampltzAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 182: {
                accessor = new IntervalymAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 183: {
                accessor = new IntervaldsAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 112: {
                accessor = new ClobAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 113: {
                accessor = new BlobAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 114: {
                accessor = new BfileAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2);
                break;
            }
            case 109: {
                accessor = new NamedTypeAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2, string, oracleTypeADT);
                break;
            }
            case 111: {
                accessor = new RefTypeAccessor(this, n4, bl, n5, n6, n7, l2, n8, s2, string, oracleTypeADT);
                break;
            }
            default: {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Unknown or unimplemented accessor type: " + n3).fillInStackTrace();
            }
        }
        return accessor;
    }

    @Override
    void executeForDescribe() throws SQLException {
        boolean bl;
        int n2;
        this.t2cOutput[0] = 0L;
        this.t2cOutput[2] = 0L;
        this.t2cOutput[7] = this.t2cConnection.useOCIDefaultDefines ? 1 : 0;
        this.lobPrefetchMetaData = null;
        boolean bl2 = !this.described;
        boolean bl3 = false;
        int n3 = n2 = this.t2cConnection.useOCIDefaultDefines ? this.savedRowPrefetch : this.rowPrefetch;
        assert (n2 > 0) : "rowsToFetch < 1 (rowsToFetch=" + n2 + ", maxRows=" + this.maxRows + ", rowPrefetch=" + this.rowPrefetch + ", savedRowPrefetch=" + this.savedRowPrefetch + ")";
        this.validRows = 0L;
        do {
            bl = false;
            if (this.connection.endToEndAnyChanged) {
                this.pushEndToEndValues();
                this.connection.endToEndAnyChanged = false;
            }
            byte[] byArray = this.sqlObject.getSqlBytes(this.processEscapes, this.convertNcharLiterals);
            int n4 = 0;
            try {
                this.resetStateBeforeFetch();
                if (this.sqlObject.getSqlKind().isDML() && this.numberOfBoundRows > 0) {
                    this.t2cOutputUpdateCountArray = new long[this.numberOfBoundRows];
                    this.t2cOutputUpdateCountArraySize[0] = this.numberOfBoundRows;
                } else {
                    this.t2cOutputUpdateCountArray = null;
                    this.t2cOutputUpdateCountArraySize[0] = 0;
                }
                n4 = T2CStatement.t2cParseExecuteDescribe(this, this.c_state, this.numberOfBindPositions, this.numberOfBindRowsAllocated, this.firstRowInBatch, this.currentRowBindAccessors != null, this.needToParse, bl2, bl3, byArray, byArray.length, T2CStatement.convertSqlKindEnumToByte(this.sqlKind), n2, this.OCIPrefetch, this.batch, this.bindIndicators, this.bindIndicatorOffset, this.bindBytes, this.bindChars, this.bindByteOffset, this.bindCharOffset, this.ibtBindIndicators, this.ibtBindIndicatorOffset, this.ibtBindIndicatorSize, this.ibtBindBytes, this.ibtBindChars, this.ibtBindByteOffset, this.ibtBindCharOffset, this.returnParamMeta, this.t2cConnection.queryMetaData1, this.t2cConnection.queryMetaData2, this.t2cConnection.queryMetaData1Offset, this.t2cConnection.queryMetaData2Offset, this.t2cConnection.queryMetaData1Size, this.t2cConnection.queryMetaData2Size, this.preparedByteBinds, this.preparedCharBinds, this.outBindAccessors, this.binders, this.t2cOutput, this.t2cOutputUpdateCountArray, this.t2cOutputUpdateCountArraySize, this.t2cConnection.plsqlCompilerWarnings);
            }
            catch (IOException iOException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 266).fillInStackTrace();
            }
            finally {
                if (this.t2cOutputUpdateCountArraySize[0] > 0) {
                    this.batchRowsUpdatedArray = new long[this.t2cOutputUpdateCountArraySize[0]];
                    System.arraycopy(this.t2cOutputUpdateCountArray, 0, this.batchRowsUpdatedArray, 0, this.t2cOutputUpdateCountArraySize[0]);
                } else if (this.batchRowsUpdatedArray != null) {
                    this.batchRowsUpdatedArray = new long[0];
                }
            }
            if (this.bindIndicators != null) {
                this.setLengthForOutAccessors();
            }
            this.validRows = this.t2cOutput[1];
            if (n4 == -1 || n4 == -4) {
                this.t2cConnection.checkError(n4, this.c_state, this.sqlObject);
            } else if (n4 == T2C_EXTEND_BUFFER) {
                n4 = this.t2cConnection.queryMetaData1Size * 2;
            }
            if (this.t2cOutput[3] != 0L) {
                this.foundPlsqlCompilerWarning();
            } else if (this.t2cOutput[2] != 0L) {
                this.sqlWarning = this.t2cConnection.checkError(1, this.sqlWarning);
            }
            this.connection.endToEndECIDSequenceNumber = (short)this.t2cOutput[4];
            this.needToParse = false;
            bl3 = true;
            if (this.sqlKind.isSELECT()) {
                this.numberOfDefinePositions = n4;
                if (this.numberOfDefinePositions > this.t2cConnection.queryMetaData1Size) {
                    bl = true;
                    bl3 = true;
                    this.t2cConnection.reallocateQueryMetaData(this.numberOfDefinePositions, this.numberOfDefinePositions * 8);
                }
            } else {
                this.numberOfDefinePositions = 0;
            }
            if (!this.sqlKind.isPlsqlOrCall()) continue;
            this.checkForImplicitResultSets();
        } while (bl);
        this.isAllFetched = false;
        this.processDescribeData();
    }

    void checkForImplicitResultSets() throws SQLException {
        int n2 = 0;
        n2 = T2CStatement.t2cGetImplicitResultSetCount(this, this.c_state);
        if (n2 > 0) {
            int n3;
            this.implicitResultSetStatements = new ArrayDeque(n3);
            for (n3 = n2; n3 != 0; --n3) {
                OracleStatement oracleStatement = this.connection.createImplicitResultSetStatement(this);
                ((T2CStatement)oracleStatement).doDescribe(true);
                ((T2CStatement)oracleStatement).prepareAccessors();
            }
            this.implicitResultSetIterator = this.implicitResultSetStatements.iterator();
        } else if (n2 != 0) {
            this.t2cConnection.checkError(n2);
        }
    }

    void pushEndToEndValues() throws SQLException {
        T2CConnection t2CConnection = this.t2cConnection;
        byte[] byArray = null;
        byte[] byArray2 = null;
        byte[] byArray3 = null;
        byte[] byArray4 = null;
        byte[] byArray5 = null;
        if (t2CConnection.endToEndValues != null) {
            String string;
            if (t2CConnection.endToEndHasChanged[0]) {
                string = t2CConnection.endToEndValues[0];
                byArray = string != null ? DBConversion.stringToDriverCharBytes(string, t2CConnection.m_clientCharacterSet) : PhysicalConnection.EMPTY_BYTE_ARRAY;
                t2CConnection.endToEndHasChanged[0] = false;
            }
            if (t2CConnection.endToEndHasChanged[1]) {
                string = t2CConnection.endToEndValues[1];
                byArray2 = string != null ? DBConversion.stringToDriverCharBytes(string, t2CConnection.m_clientCharacterSet) : PhysicalConnection.EMPTY_BYTE_ARRAY;
                t2CConnection.endToEndHasChanged[1] = false;
            }
            if (t2CConnection.endToEndHasChanged[2]) {
                string = t2CConnection.endToEndValues[2];
                byArray3 = string != null ? DBConversion.stringToDriverCharBytes(string, t2CConnection.m_clientCharacterSet) : PhysicalConnection.EMPTY_BYTE_ARRAY;
                t2CConnection.endToEndHasChanged[2] = false;
            }
            if (t2CConnection.endToEndHasChanged[3]) {
                string = t2CConnection.endToEndValues[3];
                byArray4 = string != null ? DBConversion.stringToDriverCharBytes(string, t2CConnection.m_clientCharacterSet) : PhysicalConnection.EMPTY_BYTE_ARRAY;
                t2CConnection.endToEndHasChanged[3] = false;
            }
            if (t2CConnection.endToEndHasChanged[4]) {
                string = t2CConnection.endToEndValues[4];
                byArray4 = string != null ? DBConversion.stringToDriverCharBytes(string, t2CConnection.m_clientCharacterSet) : PhysicalConnection.EMPTY_BYTE_ARRAY;
                t2CConnection.endToEndHasChanged[4] = false;
            }
            T2CStatement.t2cEndToEndUpdate(this.c_state, byArray, byArray == null ? -1 : byArray.length, byArray2, byArray2 == null ? -1 : byArray2.length, byArray3, byArray3 == null ? -1 : byArray3.length, byArray4, byArray4 == null ? -1 : byArray4.length, byArray5, byArray5 == null ? -1 : byArray5.length, t2CConnection.endToEndECIDSequenceNumber);
        }
    }

    @Override
    void executeForRows(boolean bl) throws SQLException {
        if (this.connection.endToEndAnyChanged) {
            this.pushEndToEndValues();
            this.connection.endToEndAnyChanged = false;
        }
        if (!bl) {
            if (this.numberOfDefinePositions > 0) {
                this.doDefineExecuteFetch();
            } else {
                this.executeForDescribe();
            }
        } else if (this.numberOfDefinePositions > 0) {
            this.doDefineFetch();
        }
        if (this.returnParamMeta != null) {
            this.fetchDmlReturnParams();
        }
        this.needToPrepareDefineBuffer = false;
    }

    void setupForDefine() throws SQLException {
        if (this.numberOfDefinePositions > this.t2cConnection.queryMetaData1Size) {
            int n2 = this.numberOfDefinePositions / 100 + 1;
            this.t2cConnection.reallocateQueryMetaData(this.t2cConnection.queryMetaData1Size * n2, this.t2cConnection.queryMetaData2Size * n2 * 8);
        }
        short[] sArray = this.t2cConnection.queryMetaData1;
        int n3 = this.t2cConnection.queryMetaData1Offset;
        int n4 = 0;
        while (n4 < this.numberOfDefinePositions) {
            Accessor accessor = this.accessors[n4];
            if (accessor == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 21).fillInStackTrace();
            }
            sArray[n3 + 0] = (short)accessor.defineType;
            if (!this.described && accessor.charLength > 0 && accessor.formOfUse == 1) {
                int n5 = accessor.charLength;
                int n6 = n5 + 1;
                sArray[n3 + 11] = 0;
                sArray[n3 + 1] = (short)n6;
            } else {
                sArray[n3 + 11] = (short)accessor.charLength;
                sArray[n3 + 1] = (short)accessor.byteLength;
            }
            sArray[n3 + 5] = accessor.formOfUse;
            if (accessor.internalOtype != null) {
                long l2 = ((OracleTypeADT)accessor.internalOtype).getTdoCState();
                sArray[n3 + 7] = (short)((l2 & 0xFFFF000000000000L) >> 48);
                sArray[n3 + 8] = (short)((l2 & 0xFFFF00000000L) >> 32);
                sArray[n3 + 9] = (short)((l2 & 0xFFFF0000L) >> 16);
                sArray[n3 + 10] = (short)(l2 & 0xFFFFL);
            }
            switch (accessor.internalType) {
                case 112: 
                case 113: {
                    if (accessor.lobPrefetchSizeForThisColumn == -1) {
                        accessor.setPrefetchLength(this.defaultLobPrefetchSize);
                    }
                    sArray[n3 + 7] = (short)accessor.lobPrefetchSizeForThisColumn;
                }
            }
            ++n4;
            n3 += 16;
        }
    }

    @Override
    protected void configureBindData() throws SQLException {
        if (this.outBindAccessors == null) {
            return;
        }
        AggregateByteArray aggregateByteArray = (AggregateByteArray)this.bindData;
        T2CCharByteArray t2CCharByteArray = (T2CCharByteArray)aggregateByteArray.extension;
        AggregateByteArray aggregateByteArray2 = (AggregateByteArray)t2CCharByteArray.extension;
        T2CCharByteArray t2CCharByteArray2 = (T2CCharByteArray)aggregateByteArray2.extension;
        if (this.bindBytes != null) {
            aggregateByteArray.setBytes(this.bindBytes);
        } else {
            aggregateByteArray.setBytes(PhysicalConnection.EMPTY_BYTE_ARRAY);
        }
        if (this.bindChars != null) {
            t2CCharByteArray.setChars(this.bindChars);
        } else {
            t2CCharByteArray.setChars(PhysicalConnection.EMPTY_CHAR_ARRAY);
        }
        if (this.ibtBindBytes != null) {
            aggregateByteArray2.setBytes(this.ibtBindBytes);
        } else {
            aggregateByteArray2.setBytes(PhysicalConnection.EMPTY_BYTE_ARRAY);
        }
        if (this.ibtBindChars != null) {
            t2CCharByteArray2.setChars(this.ibtBindChars);
        } else {
            t2CCharByteArray2.setChars(PhysicalConnection.EMPTY_CHAR_ARRAY);
        }
        t2CCharByteArray.setDBConversion(this.connection.conversion);
        t2CCharByteArray2.setDBConversion(this.connection.conversion);
        int n2 = this.bindBytes == null ? 0 : this.bindBytes.length;
        int n3 = this.bindChars == null ? 0 : this.bindChars.length;
        int n4 = this.ibtBindBytes == null ? 0 : this.ibtBindBytes.length;
        int n5 = this.ibtBindChars == null ? 0 : this.ibtBindChars.length;
        Accessor accessor = null;
        int n6 = 0;
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            accessor = this.outBindAccessors[i2];
            if (accessor == null) continue;
            int n7 = 0;
            int n8 = accessor.byteLength;
            if (accessor.defineType == 998) {
                PlsqlIndexTableAccessor plsqlIndexTableAccessor = (PlsqlIndexTableAccessor)accessor;
                n7 += n2 + n3;
                n7 += plsqlIndexTableAccessor.ibtBindInfo.ibtValueIndex;
                switch (plsqlIndexTableAccessor.ibtBindInfo.element_internal_type) {
                    case 9: {
                        n7 += n4;
                        break;
                    }
                }
                accessor.setOffset(0, n7);
                continue;
            }
            n7 = accessor.columnDataOffset;
            if (accessor.charLength > 0) {
                n7 += n2;
                n8 = accessor.charLength;
            }
            n6 = accessor.defineType == 15 ? 2 : (accessor.externalType == -8 ? (this.sqlKind == OracleStatement.SqlKind.CALL_BLOCK ? 1 : 2) : (accessor.defineType == 6 || accessor.defineType == 9 ? 1 : 0));
            for (int i3 = 0; i3 < this.binders.length; ++i3) {
                int n9 = this.bindIndicators[accessor.lengthIndex] - n6 & Short.MAX_VALUE;
                accessor.setOffset(i3, n7 += n8 * i3 + n6);
            }
        }
    }

    @Override
    void initializePlsqlIndexByTableAccessor(Accessor accessor, int n2) {
        ((T2CPlsqlIndexTableAccessor)accessor).ibtMetaIndex = n2 - 8;
    }

    @DefaultLevel(value=Logging.FINEST)
    Object[] getLobPrefetchMetaData() {
        Object[] objectArray = null;
        Object var2_2 = null;
        int[] nArray = null;
        int n2 = 0;
        int n3 = 0;
        if (this.accessors != null) {
            int n4;
            block7: for (n4 = 0; n4 < this.numberOfDefinePositions; ++n4) {
                switch (this.accessors[n4].internalType) {
                    case 8: 
                    case 24: {
                        n3 = n4;
                        continue block7;
                    }
                    case 112: 
                    case 113: {
                        if (nArray == null) {
                            nArray = new int[this.accessors.length];
                        }
                        if (this.accessors[n4].lobPrefetchSizeForThisColumn != -1) {
                            ++n2;
                            nArray[n4] = this.accessors[n4].lobPrefetchSizeForThisColumn;
                            continue block7;
                        }
                        nArray[n4] = -1;
                    }
                }
            }
            if (n2 > 0) {
                if (objectArray == null || this.rowPrefetchChanged) {
                    objectArray = new Object[]{null, new long[this.rowPrefetch * n2], new byte[this.accessors.length], new int[this.accessors.length], new Object[this.rowPrefetch * n2]};
                }
                for (n4 = 0; n4 < n3; ++n4) {
                    switch (this.accessors[n4].internalType) {
                        case 112: 
                        case 113: {
                            this.accessors[n4].setPrefetchLength(-1);
                            nArray[n4] = -1;
                        }
                    }
                }
                objectArray[0] = nArray;
            }
        }
        return objectArray;
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    void processLobPrefetchMetaData(Object[] objectArray) {
        int n2 = 0;
        int n3 = (int)this.validRows == -2 ? 1 : (int)this.validRows;
        byte[] byArray = (byte[])objectArray[2];
        int[] nArray = (int[])objectArray[3];
        long[] lArray = (long[])objectArray[1];
        Object[] objectArray2 = (Object[])objectArray[4];
        int[] nArray2 = (int[])objectArray[0];
        if (this.accessors != null) {
            block3: for (int i2 = 0; i2 < this.numberOfDefinePositions; ++i2) {
                switch (this.accessors[i2].internalType) {
                    case 112: 
                    case 113: {
                        if (this.accessors[i2].lobPrefetchSizeForThisColumn < 0) continue block3;
                        LobCommonAccessor lobCommonAccessor = (LobCommonAccessor)this.accessors[i2];
                        if (lobCommonAccessor.prefetchedDataLength == null || lobCommonAccessor.prefetchedDataLength.length < this.rowPrefetch) {
                            if (lobCommonAccessor.internalType == 112) {
                                ((ClobAccessor)lobCommonAccessor).prefetchedDataFormOfUse = new int[this.rowPrefetch];
                            }
                            lobCommonAccessor.prefetchedChunkSize = new int[this.rowPrefetch];
                            lobCommonAccessor.prefetchedDataLength = new int[this.rowPrefetch];
                            lobCommonAccessor.prefetchedLength = new long[this.rowPrefetch];
                            lobCommonAccessor.prefetchedDataOffset = new long[this.rowPrefetch];
                        }
                        int n4 = n3 * n2;
                        int n5 = this.needToRetainRows ? this.storedRowCount : 0;
                        for (int i3 = 0; i3 < n3; ++i3) {
                            int n6;
                            lobCommonAccessor.prefetchedChunkSize[n5 + i3] = nArray[i2];
                            lobCommonAccessor.prefetchedLength[n5 + i3] = lArray[n4 + i3];
                            if (lobCommonAccessor.internalType == 112) {
                                ((ClobAccessor)lobCommonAccessor).prefetchedDataFormOfUse[n5 + i3] = byArray[i2];
                            }
                            lobCommonAccessor.prefetchedDataLength[i3] = 0;
                            lobCommonAccessor.prefetchedDataOffset[i3] = 0L;
                            if (nArray2[i2] <= 0 || lArray[n4 + i3] <= 0L) continue;
                            byte[] byArray2 = (byte[])objectArray2[n4 + i3];
                            int n7 = n6 = byArray2 == null ? 0 : byArray2.length;
                            if (n6 > 0) {
                                lobCommonAccessor.setPrefetchedDataOffset(n5 + i3);
                                lobCommonAccessor.rowData.put(byArray2, 0, n6);
                            }
                            lobCommonAccessor.prefetchedDataLength[n5 + i3] = n6;
                        }
                        ++n2;
                        continue block3;
                    }
                }
            }
        }
    }

    int getRowsToFetch() {
        int n2 = -1;
        if (this.hasStream) {
            n2 = 1;
            if (this.t2cConnection.useOCIDefaultDefines) {
                this.savedRowPrefetch = 1;
            } else {
                this.rowPrefetch = 1;
            }
        } else {
            n2 = this.t2cConnection.useOCIDefaultDefines ? (this.maxRows > 0L && this.maxRows == (long)this.storedRowCount ? 0 : this.rowPrefetch) : (this.maxRows > 0L && this.maxRows < (long)(this.rowPrefetch + this.storedRowCount) ? (this.storedRowCount < 1 && this.maxRows < (long)this.rowPrefetch ? (int)this.maxRows : (int)Math.min((long)this.rowPrefetch, this.maxRows - (long)this.storedRowCount)) : this.rowPrefetch);
        }
        return n2;
    }

    @DefaultLevel(value=Logging.FINEST)
    void doDefineFetch() throws SQLException {
        int n2 = this.getRowsToFetch();
        this.validRows = 0L;
        if (!this.needToPrepareDefineBuffer) {
            throw new Error("doDefineFetch called when needToPrepareDefineBuffer=false " + this.sqlObject.getSql(this.processEscapes, this.convertNcharLiterals));
        }
        assert (n2 > 0) : "rowsToFetch < 1 (rowsToFetch=" + n2 + ", maxRows=" + this.maxRows + ", rowPrefetch=" + this.rowPrefetch + ", savedRowPrefetch=" + this.savedRowPrefetch + ")";
        if (n2 > 0) {
            this.setupForDefine();
            this.t2cOutput[2] = 0L;
            this.t2cOutput[5] = this.connection.useNio ? 1 : 0;
            this.t2cOutput[6] = this.defaultLobPrefetchSize;
            if (this.connection.useNio) {
                this.resetNioAttributesBeforeFetch();
                this.allocateNioBuffersIfRequired(this.defineChars == null ? 0 : this.defineChars.length, this.defineBytes == null ? 0 : this.defineBytes.length, this.defineIndicators == null ? 0 : this.defineIndicators.length);
            }
            if (this.lobPrefetchMetaData == null) {
                this.lobPrefetchMetaData = this.getLobPrefetchMetaData();
            }
            this.resetStateBeforeFetch();
            this.validRows = T2CStatement.t2cDefineFetch(this, this.c_state, n2, this.OCIPrefetch, this.t2cConnection.queryMetaData1, this.t2cConnection.queryMetaData2, this.t2cConnection.queryMetaData1Offset, this.t2cConnection.queryMetaData2Offset, this.accessors, this.t2cOutput, this.nioBuffers, this.lobPrefetchMetaData);
            if (this.validRows == -1L || this.validRows == -4L) {
                this.t2cConnection.checkError((int)this.validRows);
            }
            if (this.t2cOutput[2] != 0L) {
                this.sqlWarning = this.t2cConnection.checkError(1, this.sqlWarning);
            }
            if (this.connection.useNio && (this.validRows > 0L || this.validRows == -2L)) {
                this.extractNioDefineBuffers(0);
            }
            if (this.isFetchStreams && this.validRows == -2L) {
                this.copyStreamDataIntoDBA(0);
            }
            if (this.lobPrefetchMetaData != null) {
                this.processLobPrefetchMetaData(this.lobPrefetchMetaData);
            }
        }
        this.isAllFetched = n2 < 1 || this.validRows >= 0L && this.validRows < (long)n2;
    }

    void copyStreamDataIntoDBA(int n2) throws SQLException {
        assert (this.isFetchStreams && this.validRows == -2L) : "isFetchStreams: " + this.isFetchStreams + "isScrollable(): " + this.realRsetType.isScrollable() + ", isUpdatable(): " + this.realRsetType.isUpdatable() + ", validRows=" + this.validRows;
        this.checkValidRowsStatus();
        if (this.accessors != null) {
            block4: for (Accessor accessor : this.accessors) {
                if (accessor == null) continue;
                switch (accessor.internalType) {
                    case 8: {
                        ((T2CLongAccessor)accessor).copyStreamDataIntoDBA(n2);
                        continue block4;
                    }
                    case 24: {
                        ((T2CLongRawAccessor)accessor).copyStreamDataIntoDBA(n2);
                    }
                }
            }
        }
    }

    void allocateNioBuffersIfRequired(int n2, int n3, int n4) throws SQLException {
        if (this.nioBuffers == null) {
            this.nioBuffers = new ByteBuffer[4];
        }
        if (n3 > 0) {
            if (this.nioBuffers[0] == null || this.nioBuffers[0].capacity() < n3) {
                this.nioBuffers[0] = ByteBuffer.allocateDirect(n3);
            } else if (this.nioBuffers[0] != null) {
                this.nioBuffers[0].rewind();
            }
        }
        if ((n2 *= 2) > 0) {
            if (this.nioBuffers[1] == null || this.nioBuffers[1].capacity() < n2) {
                this.nioBuffers[1] = ByteBuffer.allocateDirect(n2);
            } else if (this.nioBuffers[1] != null) {
                this.nioBuffers[1].rewind();
            }
        }
        if ((n4 *= 2) > 0) {
            if (this.nioBuffers[2] == null || this.nioBuffers[2].capacity() < n4) {
                this.nioBuffers[2] = ByteBuffer.allocateDirect(n4);
            } else if (this.nioBuffers[2] != null) {
                this.nioBuffers[2].rewind();
            }
        }
    }

    void doDefineExecuteFetch() throws SQLException {
        short[] sArray = null;
        if (this.needToPrepareDefineBuffer || this.needToParse) {
            this.setupForDefine();
            sArray = this.t2cConnection.queryMetaData1;
        }
        this.t2cOutput[0] = 0L;
        this.t2cOutput[2] = 0L;
        byte[] byArray = this.sqlObject.getSqlBytes(this.processEscapes, this.convertNcharLiterals);
        this.t2cOutput[5] = this.connection.useNio ? 1 : 0;
        this.t2cOutput[6] = this.defaultLobPrefetchSize;
        this.t2cOutput[7] = this.t2cConnection.useOCIDefaultDefines ? 1 : 0;
        if (this.connection.useNio) {
            this.resetNioAttributesBeforeFetch();
            this.allocateNioBuffersIfRequired(this.defineChars == null ? 0 : this.defineChars.length, this.defineBytes == null ? 0 : this.defineBytes.length, this.defineIndicators == null ? 0 : this.defineIndicators.length);
        }
        if (this.lobPrefetchMetaData == null) {
            this.lobPrefetchMetaData = this.getLobPrefetchMetaData();
        }
        int n2 = this.getRowsToFetch();
        assert (n2 > 0) : "rowsToFetch < 1 (rowsToFetch=" + n2 + ", maxRows=" + this.maxRows + ", rowPrefetch=" + this.rowPrefetch + ", savedRowPrefetch=" + this.savedRowPrefetch + ")";
        this.validRows = 0L;
        try {
            this.resetStateBeforeFetch();
            this.validRows = T2CStatement.t2cDefineExecuteFetch(this, this.c_state, this.numberOfDefinePositions, this.numberOfBindPositions, this.numberOfBindRowsAllocated, this.firstRowInBatch, this.currentRowBindAccessors != null, this.needToParse, byArray, byArray.length, T2CStatement.convertSqlKindEnumToByte(this.sqlKind), n2, this.OCIPrefetch, this.batch, this.bindIndicators, this.bindIndicatorOffset, this.bindBytes, this.bindChars, this.bindByteOffset, this.bindCharOffset, sArray, this.t2cConnection.queryMetaData2, this.t2cConnection.queryMetaData1Offset, this.t2cConnection.queryMetaData2Offset, this.preparedByteBinds, this.preparedCharBinds, this.outBindAccessors, this.binders, this.t2cOutput, this.nioBuffers, this.lobPrefetchMetaData);
            if (this.bindIndicators != null) {
                this.setLengthForOutAccessors();
            }
        }
        catch (IOException iOException) {
            this.validRows = 0L;
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
        if (this.validRows == -1L) {
            this.t2cConnection.checkError((int)this.validRows);
        }
        if (this.t2cOutput[2] != 0L) {
            this.sqlWarning = this.t2cConnection.checkError(1, this.sqlWarning);
        }
        this.connection.endToEndECIDSequenceNumber = (short)this.t2cOutput[4];
        if (this.connection.useNio && (this.validRows > 0L || this.validRows == -2L)) {
            this.extractNioDefineBuffers(0);
        }
        if (this.isFetchStreams && this.validRows == -2L) {
            this.copyStreamDataIntoDBA(0);
        }
        if (this.lobPrefetchMetaData != null) {
            this.processLobPrefetchMetaData(this.lobPrefetchMetaData);
        }
        this.isAllFetched = n2 < 1 || this.validRows >= 0L && this.validRows < (long)n2;
        this.needToParse = false;
    }

    @Override
    protected void fetch(int n2, boolean bl) throws SQLException {
        this.needToRetainRows = bl;
        int n3 = this.getRowsToFetch();
        assert (n3 > 0) : "rowsToFetch < 1 (rowsToFetch=" + n3 + ", maxRows=" + this.maxRows + ", rowPrefetch=" + this.rowPrefetch + ", savedRowPrefetch=" + this.savedRowPrefetch + ")";
        this.validRows = 0L;
        if (n3 > 0 && this.numberOfDefinePositions > 0) {
            if (this.needToPrepareDefineBuffer) {
                this.doDefineFetch();
                this.needToPrepareDefineBuffer = false;
            } else {
                this.t2cOutput[2] = 0L;
                this.t2cOutput[5] = this.connection.useNio ? 1 : 0;
                this.t2cOutput[6] = this.defaultLobPrefetchSize;
                if (this.connection.useNio) {
                    this.resetNioAttributesBeforeFetch();
                    this.allocateNioBuffersIfRequired(this.defineChars == null ? 0 : this.defineChars.length, this.defineBytes == null ? 0 : this.defineBytes.length, this.defineIndicators == null ? 0 : this.defineIndicators.length);
                }
                if (this.lobPrefetchMetaData == null) {
                    this.lobPrefetchMetaData = this.getLobPrefetchMetaData();
                }
                this.resetStateBeforeFetch();
                this.validRows = T2CStatement.t2cFetch(this, this.c_state, this.needToPrepareDefineBuffer, n3, this.OCIPrefetch, this.accessors, this.t2cOutput, this.nioBuffers, this.lobPrefetchMetaData);
                if (this.validRows == -1L || this.validRows == -4L) {
                    this.t2cConnection.checkError((int)this.validRows);
                }
                if (this.t2cOutput[2] != 0L) {
                    this.sqlWarning = this.t2cConnection.checkError(1, this.sqlWarning);
                }
                if (this.lobPrefetchMetaData != null) {
                    this.processLobPrefetchMetaData(this.lobPrefetchMetaData);
                }
                if (this.connection.useNio && (this.validRows > 0L || this.validRows == -2L)) {
                    this.extractNioDefineBuffers(0);
                }
                if (this.isFetchStreams && this.validRows == -2L) {
                    this.copyStreamDataIntoDBA(n2);
                }
            }
        }
        this.isAllFetched = n3 < 1 || this.validRows >= 0L && this.validRows < (long)n3;
        this.needToRetainRows = false;
    }

    void resetNioAttributesBeforeFetch() {
        this.extractedCharOffset = 0;
        this.extractedByteOffset = 0;
    }

    @Override
    void extractNioDefineBuffers(int n2) throws SQLException {
        Buffer buffer;
        if (this.accessors == null || this.defineIndicators == null || n2 == this.numberOfDefinePositions) {
            return;
        }
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        if (!this.hasStream) {
            n3 = this.defineBytes != null ? this.defineBytes.length : 0;
            n4 = this.defineChars != null ? this.defineChars.length : 0;
            n5 = this.defineIndicators.length;
        } else {
            if (this.numberOfDefinePositions > n2) {
                n7 = this.accessors[n2].indicatorIndex;
                n6 = this.accessors[n2].lengthIndex;
            }
            block3: for (int i2 = n2; i2 < this.numberOfDefinePositions; ++i2) {
                switch (this.accessors[i2].internalType) {
                    case 8: 
                    case 24: {
                        break block3;
                    }
                    default: {
                        n3 += this.accessors[i2].byteLength;
                        n4 += this.accessors[i2].charLength;
                        ++n5;
                        continue block3;
                    }
                }
            }
        }
        ByteBuffer byteBuffer = this.nioBuffers[0];
        if (byteBuffer != null && this.defineBytes != null && n3 > 0) {
            byteBuffer.position(this.extractedByteOffset);
            byteBuffer.get(this.defineBytes, this.extractedByteOffset, n3);
            this.extractedByteOffset += n3;
        }
        if (this.nioBuffers[1] != null && this.defineChars != null) {
            byteBuffer = this.nioBuffers[1].order(ByteOrder.LITTLE_ENDIAN);
            buffer = byteBuffer.asCharBuffer();
            if (n4 > 0) {
                buffer.position(this.extractedCharOffset);
                ((CharBuffer)buffer).get(this.defineChars, this.extractedCharOffset, n4);
                this.extractedCharOffset += n4;
            }
        }
        if (this.nioBuffers[2] != null) {
            byteBuffer = this.nioBuffers[2].order(ByteOrder.LITTLE_ENDIAN);
            buffer = byteBuffer.asShortBuffer();
            if (this.hasStream) {
                if (n5 > 0) {
                    buffer.position(n7);
                    ((ShortBuffer)buffer).get(this.defineIndicators, n7, n5);
                    buffer.position(n6);
                    ((ShortBuffer)buffer).get(this.defineIndicators, n6, n5);
                }
            } else {
                ((ShortBuffer)buffer).get(this.defineIndicators);
            }
        }
    }

    @Override
    void doClose() throws SQLException {
        if (this.defineBytes != null) {
            this.defineBytes = null;
        }
        if (this.defineChars != null) {
            this.defineChars = null;
        }
        if (this.defineIndicators != null) {
            this.defineIndicators = null;
        }
        int n2 = T2CStatement.t2cCloseStatement(this.c_state);
        this.nioBuffers = null;
        if (n2 != 0) {
            this.t2cConnection.checkError(n2);
        }
        this.t2cOutput = null;
    }

    @Override
    void closeQuery() throws SQLException {
        this.connection.needLine();
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
    @DefaultLevel(value=Logging.FINEST)
    void closeUsedStreams(int n2) throws SQLException {
        while (this.nextStream != null && this.nextStream.columnIndex < 1 + this.offsetOfFirstUserColumn + n2) {
            try {
                this.nextStream.close();
            }
            catch (IOException iOException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
            }
            this.nextStream = this.nextStream.nextStream;
        }
        if (this.nextStream != null) {
            try {
                this.nextStream.needBytes();
            }
            catch (IOException iOException) {
                this.interalCloseOnIOException(iOException);
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
            }
        }
    }

    void interalCloseOnIOException(IOException iOException) throws SQLException {
        this.closed = true;
        if (this.currentResultSet != null) {
            this.currentResultSet.closed = true;
        }
        this.doClose();
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    void fetchDmlReturnParams() throws SQLException {
        this.rowsDmlReturned = T2CStatement.t2cGetRowsDmlReturned(this.c_state);
        if (this.rowsDmlReturned != 0) {
            this.allocateDmlReturnStorage();
            this.resetStateBeforeFetch();
            int n2 = T2CStatement.t2cFetchDmlReturnParams(this.c_state, this, this.accessors, this.returnParamBytes, this.returnParamChars, this.returnParamIndicators);
            if (n2 == -1 || n2 == -4) {
                this.t2cConnection.checkError(n2);
            }
            if (this.t2cOutput[2] != 0L) {
                this.sqlWarning = this.t2cConnection.checkError(1, this.sqlWarning);
            }
            if (this.connection.useNio && (n2 > 0 || n2 == -2)) {
                this.extractNioDefineBuffers(0);
            }
        }
        AggregateByteArray aggregateByteArray = (AggregateByteArray)this.bindData;
        if (this.returnParamBytes != null) {
            aggregateByteArray.setBytes(this.returnParamBytes);
        }
        ((T2CCharByteArray)aggregateByteArray.extension).setChars(this.returnParamChars);
        ((T2CCharByteArray)aggregateByteArray.extension).setDBConversion(this.connection.conversion);
        int n3 = this.returnParamBytes == null ? 0 : this.returnParamBytes.length;
        int n4 = 0;
        int n5 = this.numReturnParams * this.rowsDmlReturned;
        int n6 = 0;
        int n7 = n3;
        int n8 = 0;
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            Accessor accessor = this.accessors[i2];
            if (accessor == null) continue;
            accessor.setCapacity(this.rowsDmlReturned);
            for (int i3 = 0; i3 < this.rowsDmlReturned; ++i3) {
                if (accessor.internalType == 109 || accessor.internalType == 111) {
                    ++n8;
                    continue;
                }
                int n9 = this.returnParamIndicators[n5++];
                boolean bl = this.returnParamIndicators[n4++] == -1;
                int n10 = 0;
                if (accessor.internalType == 104) {
                    n10 = 2;
                } else if (accessor.defineType == 6 || accessor.defineType == 9 || accessor.defineType == 1) {
                    n10 = 1;
                }
                if (accessor.charLength > 0) {
                    accessor.setOffset(i3, n7 + n10);
                    n7 += accessor.charLength;
                } else {
                    accessor.setOffset(i3, n6 + n10);
                    n6 += accessor.byteLength;
                }
                if (bl || n9 == 0) {
                    accessor.setLengthAndNull(i3, 0);
                    continue;
                }
                if (accessor.internalType == 1) {
                    n9 /= 2;
                }
                accessor.setLengthAndNull(i3, n9);
            }
        }
        this.returnParamsFetched = true;
    }

    @Override
    void processDmlReturningBind() throws SQLException {
        super.processDmlReturningBind();
        this.returnParamRowBytes = 0;
        this.returnParamRowChars = 0;
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            Accessor accessor = this.accessors[i2];
            if (accessor == null) continue;
            if (accessor.charLength > 0) {
                this.returnParamRowChars += accessor.charLength;
                continue;
            }
            this.returnParamRowBytes += accessor.byteLength;
        }
        this.returnParamMeta[1] = this.returnParamRowBytes;
        this.returnParamMeta[2] = this.returnParamRowChars;
    }

    @Override
    void allocateDmlReturnStorage() {
        if (this.rowsDmlReturned == 0) {
            return;
        }
        int n2 = this.returnParamRowBytes * this.rowsDmlReturned;
        int n3 = this.returnParamRowChars * this.rowsDmlReturned;
        int n4 = 2 * this.numReturnParams * this.rowsDmlReturned;
        this.returnParamBytes = new byte[n2];
        this.returnParamChars = new char[n3];
        this.returnParamIndicators = new short[n4];
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            Accessor accessor = this.accessors[i2];
            if (accessor == null) continue;
            accessor.setCapacity(this.rowsDmlReturned);
        }
    }

    void cleanupReturnParameterBuffers() {
        this.returnParamBytes = null;
        this.returnParamChars = null;
        this.returnParamIndicators = null;
    }

    @Override
    void initializeIndicatorSubRange() {
        this.bindIndicatorSubRange = this.numberOfBindPositions * PREAMBLE_PER_POSITION;
    }

    @Override
    int calculateIndicatorSubRangeSize() {
        return this.numberOfBindPositions * PREAMBLE_PER_POSITION;
    }

    @Override
    short getInoutIndicator(int n2) {
        return this.bindIndicators[n2 * PREAMBLE_PER_POSITION];
    }

    void resetStateBeforeFetch() {
        this.lastProcessedCell = 0;
        this.lastProcessedAccessorIndex = 0;
        this.accessorsProcessed = 0;
        this.previousMode = 0;
        if (this.rowData != null) {
            if (this.needToRetainRows) {
                this.rowData.setPosition(this.rowData.length());
            } else {
                this.rowData.reset();
            }
        }
    }

    public int updateData(int n2, int n3, int n4, int[] nArray, byte[] byArray) {
        try {
            if (this.previousMode != n2) {
                this.accessorsProcessed = 0;
            }
            this.previousMode = n2;
            int n5 = 0;
            int n6 = 0;
            int n7 = 0;
            int n8 = 0;
            boolean bl = false;
            switch (n2) {
                case 16: 
                case 32: {
                    for (int i2 = 0; i2 < n3; ++i2) {
                        n6 = nArray[i2 * 4 + 0];
                        n7 = nArray[i2 * 4 + 1];
                        n8 = nArray[i2 * 4 + 2];
                        bl = nArray[i2 * 4 + 3] == -1;
                        Accessor accessor = this.accessors[n7];
                        accessor.setOffset((this.needToRetainRows ? this.storedRowCount : 0) + n6);
                        if (n8 > 0) {
                            switch (accessor.defineType) {
                                case 1: 
                                case 23: 
                                case 96: 
                                case 104: {
                                    n5 += 2;
                                    n8 -= 2;
                                    break;
                                }
                                case 6: {
                                    ++n5;
                                    --n8;
                                }
                            }
                            this.rowData.put(byArray, n5, n8);
                            n5 += n8;
                        }
                        accessor.setLengthAndNull((this.needToRetainRows ? this.storedRowCount : 0) + n6, n8);
                        ++this.accessorsProcessed;
                        ++this.lastProcessedCell;
                    }
                    break;
                }
                case 64: {
                    int n9 = 0;
                    Accessor[] accessorArray = null;
                    if (this.outBindAccessors != null) {
                        n9 = (this.bindBytes == null ? 0 : this.bindBytes.length) + (this.bindChars == null ? 0 : this.bindChars.length) + (this.ibtBindBytes == null ? 0 : this.ibtBindBytes.length) + (this.ibtBindChars == null ? 0 : this.ibtBindChars.length);
                        accessorArray = this.outBindAccessors;
                    } else {
                        n9 = (this.returnParamBytes == null ? 0 : this.returnParamBytes.length) + (this.returnParamChars == null ? 0 : this.returnParamChars.length) + (this.ibtBindBytes == null ? 0 : this.ibtBindBytes.length) + (this.ibtBindChars == null ? 0 : this.ibtBindChars.length);
                        accessorArray = this.accessors;
                    }
                    assert (accessorArray != null) : "No OUT binds or Return Parameters";
                    ByteArray byteArray = this.bindData;
                    while (byteArray instanceof AggregateByteArray && (byteArray = ((AggregateByteArray)byteArray).extension) != null) {
                    }
                    n9 = (int)((long)n9 + byteArray.getPosition());
                    int n10 = this.accessorsProcessed;
                    for (int i3 = 0; i3 < n3; ++i3) {
                        n6 = nArray[i3 * 4 + 0];
                        n7 = nArray[i3 * 4 + 1];
                        n8 = nArray[i3 * 4 + 2];
                        bl = nArray[i3 * 4 + 3] == -1;
                        Accessor accessor = accessorArray[n7];
                        accessor.setOffset((this.needToRetainRows ? this.storedRowCount : 0) + n6, n9);
                        byteArray.put(byArray, n5, n8);
                        accessor.setLengthAndNull((this.needToRetainRows ? this.storedRowCount : 0) + n6, n8);
                        n5 += n8;
                        n9 += n8;
                        ++this.lastProcessedCell;
                    }
                    break;
                }
            }
        }
        catch (SQLException sQLException) {
            this.updateDataException = sQLException;
            return -3;
        }
        return 0;
    }

    final boolean bit(long l2, long l3) {
        return (l2 & l3) == l2;
    }

    public static String toHex(byte[] byArray, int n2) {
        if (byArray == null) {
            return "null";
        }
        if (n2 > byArray.length) {
            return "byte array not long enough";
        }
        String string = "0:";
        int n3 = n2;
        for (int i2 = 0; i2 < n3; ++i2) {
            if (i2 != 0 && i2 % 10 == 0) {
                string = string + "\n" + i2 + ": ";
            }
            string = string + OracleLog.toHex(byArray[i2]) + " ";
        }
        return string;
    }

    @Override
    void locationToPutBytes(Accessor accessor, int n2, int n3) throws SQLException {
        accessor.setOffset(n2, this.allocateRowDataSpace(n3));
    }

    @Override
    long allocateRowDataSpace(int n2) {
        return this.rowData.length();
    }

    @Override
    Accessor allocateAccessor(int n2, int n3, int n4, int n5, short s2, String string, boolean bl) throws SQLException {
        Accessor accessor = null;
        switch (n2) {
            case 102: 
            case 116: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                accessor = new T2CResultSetAccessor(this, n5, s2, n3, bl);
                return accessor;
            }
            case 8: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                if (bl) {
                    return new VarcharAccessor(this, n5, s2, n3, bl);
                }
                return new T2CLongAccessor(this, n4, n5, s2, n3);
            }
            case 1: {
                if (!bl) break;
                if (string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                accessor = new T2CVarcharAccessor(this, n5, s2, n3, bl);
                return accessor;
            }
            case 24: {
                if (bl && string != null) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 12, "sqlType=" + n3).fillInStackTrace();
                }
                if (bl) {
                    return new OutRawAccessor(this, n5, s2, n3);
                }
                return new T2CLongRawAccessor(this, n4, n5, s2, n3);
            }
        }
        return super.allocateAccessor(n2, n3, n4, n5, s2, string, bl);
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    void prepareBindPreambles(int n2, int n3) {
        int n4 = this.calculateIndicatorSubRangeSize();
        int n5 = this.bindIndicatorSubRange - n4;
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            int n6;
            OracleTypeADT oracleTypeADT;
            Binder binder = this.lastBinders[i2];
            if (binder == this.theReturnParamBinder) {
                oracleTypeADT = (OracleTypeADT)this.accessors[i2].internalOtype;
                n6 = 0;
            } else {
                OracleTypeADT oracleTypeADT2 = oracleTypeADT = this.binders[this.firstRowInBatch] == null ? null : this.getOtype(this.binders[this.firstRowInBatch][i2]);
                if (this.outBindAccessors == null) {
                    n6 = 0;
                } else {
                    Accessor accessor = this.outBindAccessors[i2];
                    if (accessor == null) {
                        n6 = 0;
                    } else if (binder == this.theOutBinder) {
                        n6 = 1;
                        if (oracleTypeADT == null) {
                            oracleTypeADT = (OracleTypeADT)accessor.internalOtype;
                        }
                    } else {
                        n6 = 2;
                    }
                }
                n6 = binder.updateInoutIndicatorValue((short)n6);
            }
            this.bindIndicators[n5++] = n6;
            if (oracleTypeADT != null) {
                long l2 = oracleTypeADT.getTdoCState();
                this.bindIndicators[n5 + 0] = (short)(l2 >> 48 & 0xFFFFL);
                this.bindIndicators[n5 + 1] = (short)(l2 >> 32 & 0xFFFFL);
                this.bindIndicators[n5 + 2] = (short)(l2 >> 16 & 0xFFFFL);
                this.bindIndicators[n5 + 3] = (short)(l2 & 0xFFFFL);
            }
            n5 += 4;
        }
    }

    @Override
    @DefaultLevel(value=Logging.FINEST)
    void releaseBuffers() {
        super.releaseBuffers();
    }

    void setLengthForOutAccessors() throws SQLException {
        if (this.outBindAccessors == null) {
            return;
        }
        Accessor accessor = null;
        for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
            accessor = this.outBindAccessors[i2];
            if (accessor == null || accessor.defineType == 998 || accessor.defineType == 111 || accessor.defineType == 109) continue;
            for (int i3 = 0; i3 < this.binders.length; ++i3) {
                int n2;
                boolean bl = accessor.rowSpaceIndicator[accessor.indicatorIndex + i3] == -1;
                int n3 = n2 = bl ? 0 : accessor.rowSpaceIndicator[accessor.lengthIndex + i3] & 0xFFFF;
                if (this.sqlKind != OracleStatement.SqlKind.CALL_BLOCK && accessor.externalType == -8) {
                    accessor.setLengthAndNull(i3, n2);
                    continue;
                }
                if (accessor.defineType == 9 || accessor.defineType == 1) {
                    accessor.setLengthAndNull(i3, n2 / 2);
                    continue;
                }
                if (accessor.defineType == 109 || accessor.defineType == 111) continue;
                accessor.setLengthAndNull(i3, n2);
            }
        }
    }

    @Override
    void doDescribe(boolean bl) throws SQLException {
        boolean bl2;
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.described) {
            return;
        }
        if (!this.isOpen) {
            this.connection.open(this);
            this.isOpen = true;
        }
        do {
            bl2 = false;
            boolean bl3 = this.sqlKind.isSELECT() && this.needToParse && (!this.described || !this.serverCursor);
            byte[] byArray = bl3 ? this.sqlObject.getSqlBytes(this.processEscapes, this.convertNcharLiterals) : PhysicalConnection.EMPTY_BYTE_ARRAY;
            this.numberOfDefinePositions = T2CStatement.t2cDescribe(this.c_state, this.t2cConnection.queryMetaData1, this.t2cConnection.queryMetaData2, this.t2cConnection.queryMetaData1Offset, this.t2cConnection.queryMetaData2Offset, this.t2cConnection.queryMetaData1Size, this.t2cConnection.queryMetaData2Size, byArray, byArray.length, bl3);
            if (!this.described) {
                this.described = true;
            }
            if (this.numberOfDefinePositions == -1) {
                this.t2cConnection.checkError(this.numberOfDefinePositions);
            }
            if (this.numberOfDefinePositions != T2C_EXTEND_BUFFER) continue;
            bl2 = true;
            this.t2cConnection.reallocateQueryMetaData(this.t2cConnection.queryMetaData1Size * 2, this.t2cConnection.queryMetaData2Size * 2);
        } while (bl2);
        this.processDescribeData();
    }

    @Override
    protected void configureRowData() {
        this.rowData = DynamicByteArray.createDynamicByteArray(this.connection.getBlockSource());
        DynamicByteArray dynamicByteArray = DynamicByteArray.createDynamicByteArray(this.connection.getBlockSource());
        T2CCharByteArray t2CCharByteArray = new T2CCharByteArray(PhysicalConnection.EMPTY_CHAR_ARRAY, (ByteArray)dynamicByteArray);
        AggregateByteArray aggregateByteArray = new AggregateByteArray(PhysicalConnection.EMPTY_BYTE_ARRAY, t2CCharByteArray);
        T2CCharByteArray t2CCharByteArray2 = new T2CCharByteArray(PhysicalConnection.EMPTY_CHAR_ARRAY, (ByteArray)aggregateByteArray);
        this.bindData = new AggregateByteArray(PhysicalConnection.EMPTY_BYTE_ARRAY, t2CCharByteArray2);
    }

    @Override
    boolean isThinDriver() {
        return false;
    }

    @Override
    void resetBindersToNull(int n2) {
        if (this.binders != null) {
            for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
                for (int i3 = 0; i3 < this.numberOfBoundRows; ++i3) {
                    int n3 = n2 + i3;
                    this.binders[n3][i2] = null;
                }
            }
        }
    }

    @Override
    void setupBindBuffers(int n2, int n3) throws SQLException {
        assert (!this.bindUseDBA) : "bindUseDBA is true for T2C or T2S driver.";
        if (this.bindIndicators == null) {
            this.allocBinds(n3);
        }
        try {
            int n4;
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            int n10;
            int n11;
            if (this.numberOfBindPositions == 0) {
                if (n3 != 0) {
                    this.numberOfBoundRows = n3;
                    this.bindIndicators[this.bindIndicatorSubRange + 3] = (short)((this.numberOfBoundRows & 0xFFFF0000) >> 16);
                    this.bindIndicators[this.bindIndicatorSubRange + 4] = (short)(this.numberOfBoundRows & 0xFFFF);
                }
                return;
            }
            this.preparedAllBinds = this.currentBatchNeedToPrepareBinds;
            this.preparedByteBinds = false;
            this.preparedCharBinds = false;
            this.currentBatchNeedToPrepareBinds = false;
            this.numberOfBoundRows = n3;
            this.bindIndicators[this.bindIndicatorSubRange + 3] = (short)((this.numberOfBoundRows & 0xFFFF0000) >> 16);
            this.bindIndicators[this.bindIndicatorSubRange + 4] = (short)(this.numberOfBoundRows & 0xFFFF);
            int n12 = this.bindBufferCapacity;
            if (this.numberOfBoundRows > this.bindBufferCapacity) {
                n12 = this.numberOfBoundRows;
                this.preparedAllBinds = true;
            }
            if (this.currentBatchBindAccessors != null) {
                if (this.outBindAccessors == null) {
                    this.outBindAccessors = new Accessor[this.numberOfBindPositions];
                }
                for (n11 = 0; n11 < this.numberOfBindPositions; ++n11) {
                    Accessor accessor;
                    this.outBindAccessors[n11] = accessor = this.currentBatchBindAccessors[n11];
                    if (accessor == null) continue;
                    n10 = accessor.byteLength;
                    n9 = accessor.charLength;
                    if (this.currentBatchByteLens[n11] < n10) {
                        this.currentBatchByteLens[n11] = n10;
                    }
                    if (n9 != 0 && this.currentBatchCharLens[n11] >= n9) continue;
                    this.currentBatchCharLens[n11] = n9;
                }
            }
            n11 = 0;
            int n13 = 0;
            n9 = n10 = this.bindIndicatorSubRange + 5;
            if (this.preparedAllBinds) {
                this.preparedByteBinds = true;
                this.preparedCharBinds = true;
                Binder[] binderArray = this.binders[n2];
                for (n8 = 0; n8 < this.numberOfBindPositions; ++n8) {
                    Binder binder = binderArray[n8];
                    n7 = this.currentBatchByteLens[n8];
                    n6 = this.currentBatchCharLens[n8];
                    if (binder == this.theOutBinder) {
                        Accessor accessor = this.currentBatchBindAccessors[n8];
                        n5 = (short)accessor.defineType;
                    } else {
                        n5 = binder.type;
                    }
                    n13 += n7;
                    n11 += n6;
                    this.bindIndicators[n9 + 0] = n5;
                    this.bindIndicators[n9 + 1] = (short)n7;
                    this.bindIndicators[n9 + 2] = (short)n6;
                    this.bindIndicators[n9 + 9] = this.currentBatchFormOfUse[n8];
                    n9 += 10;
                }
            } else if (this.preparedByteBinds | this.preparedCharBinds) {
                for (int i2 = 0; i2 < this.numberOfBindPositions; ++i2) {
                    n8 = this.currentBatchByteLens[i2];
                    int n14 = this.currentBatchCharLens[i2];
                    n13 += n8;
                    n11 += n14;
                    this.bindIndicators[n9 + 1] = (short)n8;
                    this.bindIndicators[n9 + 2] = (short)n14;
                    n9 += 10;
                }
            } else {
                for (int i3 = 0; i3 < this.numberOfBindPositions; ++i3) {
                    boolean bl;
                    n8 = n9 + 1;
                    int n15 = n9 + 2;
                    n5 = this.currentBatchByteLens[i3];
                    n7 = this.currentBatchCharLens[i3];
                    n6 = this.bindIndicators[n8];
                    int n16 = this.bindIndicators[n15];
                    n4 = (this.bindIndicators[n9 + 5] << 16) + (this.bindIndicators[n9 + 6] & 0xFFFF);
                    boolean bl2 = bl = this.bindIndicators[n4] == -1;
                    if (bl && n5 > 1) {
                        this.preparedByteBinds = true;
                    }
                    if (n6 >= n5 && !this.preparedByteBinds) {
                        this.currentBatchByteLens[i3] = n6;
                        n13 += n6;
                    } else {
                        this.bindIndicators[n8] = (short)n5;
                        n13 += n5;
                        this.preparedByteBinds = true;
                    }
                    if (bl && n7 > 1) {
                        this.preparedCharBinds = true;
                    }
                    if (n16 >= n7 && !this.preparedCharBinds) {
                        this.currentBatchCharLens[i3] = n16;
                        n11 += n16;
                    } else {
                        this.bindIndicators[n15] = (short)n7;
                        n11 += n7;
                        this.preparedCharBinds = true;
                    }
                    n9 += 10;
                }
            }
            if (this.preparedByteBinds | this.preparedCharBinds) {
                this.initializeBindSubRanges(this.numberOfBoundRows, n12);
            }
            if (this.preparedByteBinds) {
                int n17 = this.bindByteSubRange + n13 * n12;
                if (this.lastBoundNeeded || n17 > this.totalBindByteLength) {
                    this.bindByteOffset = 0;
                    this.bindBytes = this.connection.getByteBuffer(n17);
                    this.totalBindByteLength = n17;
                }
                this.bindBufferCapacity = n12;
                this.bindIndicators[this.bindIndicatorSubRange + 1] = (short)((this.bindBufferCapacity & 0xFFFF0000) >> 16);
                this.bindIndicators[this.bindIndicatorSubRange + 2] = (short)(this.bindBufferCapacity & 0xFFFF);
            }
            if (this.preparedCharBinds) {
                int n18 = this.bindCharSubRange + n11 * this.bindBufferCapacity;
                if (this.lastBoundNeeded || n18 > this.totalBindCharLength) {
                    this.bindCharOffset = 0;
                    this.bindChars = this.connection.getCharBuffer(n18);
                    this.totalBindCharLength = n18;
                }
                this.bindByteSubRange += this.bindByteOffset;
                this.bindCharSubRange += this.bindCharOffset;
            }
            int n19 = this.bindByteSubRange;
            n8 = this.bindCharSubRange;
            int n20 = this.indicatorsOffset;
            n5 = this.valueLengthsOffset;
            n9 = n10;
            if (this.preparedByteBinds | this.preparedCharBinds) {
                if (this.currentBatchBindAccessors == null) {
                    for (n7 = 0; n7 < this.numberOfBindPositions; ++n7) {
                        n6 = this.currentBatchByteLens[n7];
                        int n21 = this.currentBatchCharLens[n7];
                        n4 = n21 == 0 ? n19 : n8;
                        this.bindIndicators[n9 + 3] = (short)(n4 >> 16);
                        this.bindIndicators[n9 + 4] = (short)(n4 & 0xFFFF);
                        n19 += n6 * this.bindBufferCapacity;
                        n8 += n21 * this.bindBufferCapacity;
                        n9 += 10;
                    }
                } else {
                    for (n7 = 0; n7 < this.numberOfBindPositions; ++n7) {
                        n6 = this.currentBatchByteLens[n7];
                        int n22 = this.currentBatchCharLens[n7];
                        n4 = n22 == 0 ? n19 : n8;
                        this.bindIndicators[n9 + 3] = (short)(n4 >> 16);
                        this.bindIndicators[n9 + 4] = (short)(n4 & 0xFFFF);
                        Accessor accessor = this.currentBatchBindAccessors[n7];
                        if (accessor != null) {
                            if (n22 > 0) {
                                accessor.columnDataOffset = n8;
                                accessor.charLength = n22;
                            } else {
                                accessor.columnDataOffset = n19;
                                accessor.byteLength = n6;
                            }
                            accessor.lengthIndex = n5;
                            accessor.indicatorIndex = n20;
                            accessor.rowSpaceIndicator = this.bindIndicators;
                            accessor.setCapacity(this.bindBufferCapacity);
                        }
                        n19 += n6 * this.bindBufferCapacity;
                        n8 += n22 * this.bindBufferCapacity;
                        n20 += this.numberOfBindRowsAllocated;
                        n5 += this.numberOfBindRowsAllocated;
                        n9 += 10;
                    }
                }
                n19 = this.bindByteSubRange;
                n8 = this.bindCharSubRange;
                n20 = this.indicatorsOffset;
                n5 = this.valueLengthsOffset;
                n9 = n10;
            }
            n7 = this.bindBufferCapacity - this.numberOfBoundRows;
            n6 = this.numberOfBoundRows - 1;
            int n23 = n6 + n2;
            Binder[] binderArray = this.binders[n23];
            if (this.hasIbtBind) {
                this.processPlsqlIndexTabBinds(n2);
            }
            if (this.numReturnParams > 0 && (this.accessors == null || this.accessors.length < this.numReturnParams)) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 173).fillInStackTrace();
            }
            if (this.numReturnParams > 0) {
                this.processDmlReturningBind();
            }
            boolean bl = !this.sqlKind.isPlsqlOrCall() || this.currentRowBindAccessors == null;
            this.localCheckSum = this.checkSum;
            for (int i4 = 0; i4 < this.numberOfBindPositions; ++i4) {
                int n24;
                int n25 = this.currentBatchByteLens[i4];
                int n26 = this.currentBatchCharLens[i4];
                short s2 = this.currentBatchFormOfUse[i4];
                this.lastBinders[i4] = binderArray[i4];
                this.lastBoundByteLens[i4] = n25;
                for (n24 = 0; n24 < this.numberOfBoundRows; ++n24) {
                    int n27 = n2 + n24;
                    int n28 = n24 * this.numberOfBindPositions + i4;
                    this.localCheckSum = this.binders[n27][i4].bind(this, i4, n24, n27, this.bindBytes, this.bindChars, this.bindIndicators, n25, n26, n19, n8, n5 + n24, n20 + n24, bl, this.localCheckSum, this.bindData, this.bindDataOffsets, this.bindDataLengths, n28, this.bindUseDBA, s2);
                    if (this.userStream != null) {
                        this.userStream[n24][i4] = null;
                    }
                    n19 += n25;
                    n8 += n26;
                    continue;
                }
                if (this.bindChecksumListener != null) {
                    n24 = this.bindChecksumListener.shouldContinue(this.checkSum) ? 1 : 0;
                    this.bindChecksumListener = null;
                    if (n24 == 0) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 290).fillInStackTrace();
                    }
                }
                this.lastBoundByteOffsets[i4] = n19 - n25;
                this.lastBoundCharOffsets[i4] = n8 - n26;
                this.lastBoundInds[i4] = this.bindIndicators[n20 + n6];
                this.lastBoundLens[i4] = this.bindIndicators[n5 + n6];
                this.lastBoundByteLens[i4] = 0;
                this.lastBoundCharLens[i4] = 0;
                n19 += n7 * n25;
                n8 += n7 * n26;
                n20 += this.numberOfBindRowsAllocated;
                n5 += this.numberOfBindRowsAllocated;
                n9 += 10;
            }
            this.checkSum = this.localCheckSum;
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
            this.prepareBindPreambles(this.numberOfBoundRows, this.bindBufferCapacity);
            this.configureBindData();
            this.checkSum = this.localCheckSum;
            this.localCheckSum = 0L;
        }
        catch (NullPointerException nullPointerException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89, null, (Throwable)nullPointerException).fillInStackTrace();
        }
    }

    @Override
    void registerOutParameterInternal(int n2, int n3, int n4, int n5, String string) throws SQLException {
        int n6 = n2 - 1;
        if (n6 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(3).fillInStackTrace();
        }
        int n7 = this.getInternalType(n3);
        if (n7 == 995) {
            throw (SQLException)DatabaseError.createSqlException(4).fillInStackTrace();
        }
        this.resetBatch();
        this.currentRowNeedToPrepareBinds = true;
        if (this.currentRowBindAccessors == null) {
            this.currentRowBindAccessors = new Accessor[this.numberOfBindPositions];
        }
        switch (n3) {
            case -4: 
            case -3: 
            case -1: 
            case 1: 
            case 12: 
            case 70: {
                break;
            }
            case -16: 
            case -15: 
            case -9: {
                this.currentRowFormOfUse[n6] = 2;
                break;
            }
            case 2011: {
                n5 = 0;
                this.currentRowFormOfUse[n6] = 2;
                break;
            }
            case 2009: {
                n5 = 0;
                string = "SYS.XMLTYPE";
                break;
            }
            case 2002: 
            case 2003: {
                if (string == null || string.length() == 0) {
                    throw (SQLException)DatabaseError.createSqlException(68).fillInStackTrace();
                }
                n5 = 0;
                break;
            }
            default: {
                n5 = 0;
            }
        }
        this.currentRowBindAccessors[n6] = this.allocateAccessor(n7, n3, n2, n5, this.currentRowFormOfUse[n6], string, true);
        this.currentRowBindAccessors[n6].setCapacity(1);
    }

    @Override
    Accessor allocateIndexTableAccessor(PlsqlIbtBindInfo plsqlIbtBindInfo, short s2) throws SQLException {
        return new T2CPlsqlIndexTableAccessor(this, plsqlIbtBindInfo, s2);
    }

    @Log
    protected void debug(Logger logger, Level level, Executable executable, String string) {
        ClioSupport.log(logger, level, this.getClass(), executable, string);
    }
}

