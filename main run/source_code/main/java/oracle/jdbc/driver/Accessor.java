/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.sql.NClob;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleResultSetMetaData;
import oracle.jdbc.driver.AccessorPrototype;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.CRC64;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.GeneratedAccessor;
import oracle.jdbc.driver.OracleInputStream;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.PlsqlIbtBindInfo;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleType;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET})
abstract class Accessor
extends GeneratedAccessor
implements Cloneable {
    static final int FIXED_CHAR = 999;
    static final int CHAR = 96;
    static final int VARCHAR = 1;
    static final int VCS = 9;
    static final int LONG = 8;
    static final int NUMBER = 2;
    static final int VARNUM = 6;
    static final int BINARY_FLOAT = 100;
    static final int BINARY_DOUBLE = 101;
    static final int RAW = 23;
    static final int VBI = 15;
    static final int LONG_RAW = 24;
    static final int ROWID = 104;
    static final int RESULT_SET = 102;
    static final int RSET = 116;
    static final int DATE = 12;
    static final int BLOB = 113;
    static final int JSON = 119;
    static final int CLOB = 112;
    static final int BFILE = 114;
    static final int NAMED_TYPE = 109;
    static final int REF_TYPE = 111;
    static final int TIMESTAMP = 180;
    static final int TIMESTAMPTZ = 181;
    static final int TIMESTAMPLTZ = 231;
    static final int INTERVALYM = 182;
    static final int INTERVALDS = 183;
    static final int UROWID = 208;
    static final int PLSQL_BOOLEAN = 252;
    static final int PLSQL_INDEX_TABLE = 998;
    static final int T2S_OVERLONG_RAW = 997;
    static final int SET_CHAR_BYTES = 996;
    static final int NULL_TYPE = 995;
    static final int DML_RETURN_PARAM = 994;
    static final int XMLTYPE = 257;
    static final int ONLY_FORM_USABLE = 0;
    static final int NOT_USABLE = 1;
    static final int NO_NEED_TO_PREPARE = 2;
    static final int NEED_TO_PREPARE = 3;
    static final byte DATA_UNAUTHORIZED = 1;
    static final int NO_LOB_PREFETCH = -1;
    OracleStatement statement;
    boolean outBind;
    int internalType;
    boolean isStream = false;
    boolean isColumnNumberAware = false;
    short formOfUse = (short)2;
    OracleType internalOtype;
    int externalType;
    String internalTypeName;
    String columnName;
    int describeType;
    int describeMaxLength;
    int describeMaxLengthChars;
    short describeFormOfUse;
    long contflag;
    boolean nullable;
    int precision;
    int scale;
    int flags;
    int total_elems;
    OracleType describeOtype;
    String describeTypeName;
    int definedColumnType = 0;
    int definedColumnSize = 0;
    int oacmxl = 0;
    short udskpos = (short)-1;
    int lobPrefetchSizeForThisColumn = -1;
    OracleResultSetMetaData.SecurityAttribute securityAttribute;
    protected boolean columnInvisible = false;
    protected boolean columnJSON = false;
    short[] rowSpaceIndicator = null;
    int columnDataOffset = 0;
    int lengthIndex = 0;
    int indicatorIndex = 0;
    int byteLength = 0;
    int charLength = 0;
    int defineType;
    boolean isDMLReturnedParam = false;
    int lastRowProcessed = 0;
    boolean isUseLess = false;
    int physicalColumnIndex = -2;
    boolean isNullByDescribe = false;
    int lastCopyRow = -1;
    long lastCopyRowOffset = 0L;
    int lastCopyRowLength = 0;
    byte lastCopyRowMetaData = 0;
    boolean lastCopyRowIsNull = false;
    static final byte[] NULL_DATA_BYTES = new byte[]{2, 3, 5, 7, 11, 13, 17, 19};
    static final int ROW_METADATA_LENGTH = 1;
    final Representation representation;
    final int representationMaxLength;
    ByteArray rowData;
    long[] rowOffset = null;
    int[] rowLength = null;
    boolean[] rowNull = null;
    byte[] rowMetadata = null;
    Object driverSpecificData;
    int previousRowProcessed = -1;
    final int[] escapeSequenceArr = new int[1];
    final boolean[] readHeaderArr = new boolean[1];
    final boolean[] readAsNonStreamArr = new boolean[1];

    void setNoPrefetch() {
    }

    void setPrefetchLength(int n2) {
    }

    PlsqlIbtBindInfo plsqlIndexTableBindInfo() throws SQLException {
        return null;
    }

    void unimpl(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, string + " not implemented for " + this.getClass()).fillInStackTrace();
    }

    long updateChecksum(long l2, int n2) throws SQLException {
        if (this.isNull(n2)) {
            return CRC64.updateChecksum(l2, NULL_DATA_BYTES, 0, NULL_DATA_BYTES.length);
        }
        return this.rowData.updateChecksum(this.getOffset(n2), this.getLength(n2), PhysicalConnection.CHECKSUM, l2);
    }

    void init(OracleStatement oracleStatement, int n2, int n3, short s2, boolean bl) throws SQLException {
        this.statement = oracleStatement;
        this.outBind = bl;
        this.internalType = n2;
        this.defineType = n3;
        this.formOfUse = s2;
    }

    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        if (n2 != 0) {
            this.externalType = n2;
        }
        this.byteLength = n3 > 0 && n3 < this.representationMaxLength ? n3 : this.representationMaxLength;
    }

    void initForDescribe(int n2, int n3, boolean bl, int n4, int n5, int n6, long l2, int n7, short s2) throws SQLException {
        this.nullable = bl;
        this.precision = n5;
        this.scale = n6;
        this.flags = n4;
        this.contflag = l2;
        this.total_elems = n7;
        this.describeType = n2;
        this.describeMaxLength = n3;
        this.describeFormOfUse = s2;
        if (!this.statement.columnsDefinedByUser) {
            this.formOfUse = s2;
        }
    }

    void initForDescribe(int n2, int n3, boolean bl, int n4, int n5, int n6, long l2, int n7, short s2, String string) throws SQLException {
        this.describeTypeName = string;
        this.describeOtype = null;
        this.initForDescribe(n2, n3, bl, n4, n5, n6, l2, n7, s2);
    }

    void initForDescribe(int n2, int n3, boolean bl, int n4, int n5, int n6, long l2, int n7, short s2, String string, int n8) throws SQLException {
        this.initForDescribe(n2, n3, bl, n4, n5, n6, l2, n7, s2, string);
        this.describeMaxLengthChars = n8;
    }

    OracleInputStream initForNewRow() throws SQLException {
        this.unimpl("initForNewRow");
        return null;
    }

    int useForDataAccessIfPossible(int n2, int n3, int n4, String string) throws SQLException {
        int n5 = 3;
        int n6 = 0;
        int n7 = 0;
        if (this.internalType != 0) {
            if (this.internalType != n2) {
                n5 = 0;
            } else if (this.rowSpaceIndicator != null) {
                n6 = this.byteLength;
                n7 = this.charLength;
            }
        }
        if (n5 == 3) {
            this.initForDataAccess(n3, n4, string);
            if (!this.outBind && n6 >= this.byteLength && n7 >= this.charLength) {
                n5 = 2;
            }
        }
        return n5;
    }

    boolean useForDescribeIfPossible(int n2, int n3, boolean bl, int n4, int n5, int n6, long l2, int n7, short s2, String string) throws SQLException {
        if (!this.statement.columnsDefinedByUser && this.externalType == 0 && this.describeType == 0 && n2 != this.describeType) {
            return false;
        }
        this.initForDescribe(n2, n3, bl, n4, n5, n6, l2, n7, s2, string);
        return true;
    }

    void setFormOfUse(short s2) {
        this.formOfUse = s2;
    }

    void updateColumnNumber(int n2) {
    }

    @DisableTrace
    public String toString() {
        return super.toString() + ", statement=" + this.statement + ", outBind=" + this.outBind + ", internalType=" + this.internalType + ", isStream=" + this.isStream + ", formOfUse=" + this.formOfUse + ", internalOtype=" + this.internalOtype + ", externalType=" + this.externalType + ", internalTypeName=" + this.internalTypeName + ", columnName=" + this.columnName + ", describeType=" + this.describeType + ", describeMaxLength=" + this.describeMaxLength + ", nullable=" + this.nullable + ", precision=" + this.precision + ", scale=" + this.scale + ", flags=" + this.flags + ", contflag=" + this.contflag + ", total_elems=" + this.total_elems + ", describeOtype=" + this.describeOtype + ", describeTypeName=" + this.describeTypeName + ", rowData=" + this.rowData + ", rowOffset=" + this.rowOffset + ", rowLength=" + this.rowLength + ", rowNull=" + this.rowNull + ", rowMetadata=" + this.rowMetadata + ", driverSpecificData=" + this.driverSpecificData + ", describeMaxLengthChars=" + this.describeMaxLengthChars;
    }

    void fetchNextColumns() throws SQLException {
    }

    void calculateSizeTmpByteArray() {
    }

    boolean unmarshalOneRow() throws SQLException, IOException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 148).fillInStackTrace();
    }

    void copyRow() throws SQLException, IOException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 148).fillInStackTrace();
    }

    int readStream(byte[] byArray, int n2) throws SQLException, IOException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 148).fillInStackTrace();
    }

    int getPreviousRowProcessed() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 148).fillInStackTrace();
    }

    void initMetadata() throws SQLException {
    }

    @Override
    protected OracleConnection getConnectionDuringExceptionHandling() {
        return this.statement.getConnectionDuringExceptionHandling();
    }

    protected Accessor(Representation representation, OracleStatement oracleStatement, int n2, boolean bl) {
        this.representation = representation;
        this.statement = oracleStatement;
        this.representationMaxLength = n2;
        this.rowData = bl ? this.statement.bindData : this.statement.rowData;
        this.statement.setDriverSpecificData(this);
    }

    protected final long getOffset(int n2) {
        return this.rowOffset[n2];
    }

    protected final void setOffset(int n2, long l2) {
        this.rowOffset[n2] = l2;
    }

    protected final void setOffset(int n2) {
        long l2 = this.rowData.getPosition();
        this.rowOffset[n2] = l2;
    }

    protected final int getLength(int n2) {
        return this.rowLength[n2];
    }

    protected final void setLength(int n2, int n3) {
        this.rowLength[n2] = n3;
    }

    protected final void setLengthAndNull(int n2, int n3) throws SQLException {
        if (n3 == 0) {
            this.setNull(n2, true);
        } else {
            this.setNull(n2, false);
            this.setLength(n2, n3);
        }
    }

    final byte getRowMetadata(int n2) {
        return this.rowMetadata[n2];
    }

    final void setRowMetadata(int n2, byte by) {
        this.rowMetadata[n2] = by;
    }

    @Override
    boolean isNull(int n2) throws SQLException {
        if (this.rowNull == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 21).fillInStackTrace();
        }
        return this.rowNull[n2];
    }

    void setNull(int n2, boolean bl) throws SQLException {
        if (this.rowNull == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 21).fillInStackTrace();
        }
        this.rowNull[n2] = bl;
        if (bl && this.statement.connection.protocolId != 3) {
            this.setOffset(n2, -1L);
            this.setLength(n2, -1);
        }
    }

    boolean isNullByDescribe() {
        return this.isNullByDescribe;
    }

    boolean isUseless() {
        return this.isUseLess;
    }

    boolean isUnexpected() {
        return this.rowNull == null;
    }

    void setColumnInvisible(boolean bl) {
        this.columnInvisible = bl;
    }

    boolean isColumnInvisible() {
        return this.columnInvisible;
    }

    void setColumnJSON(boolean bl) {
        this.columnJSON = bl;
    }

    boolean isColumnJSON() {
        return this.columnJSON;
    }

    void setCapacity(int n2) {
        if (this.rowNull == null) {
            this.rowLength = new int[n2];
            this.rowOffset = new long[n2];
            this.rowNull = new boolean[n2];
            this.rowMetadata = new byte[n2];
        } else if (this.rowNull.length < n2) {
            Object[] objectArray = new int[n2];
            System.arraycopy(this.rowLength, 0, objectArray, 0, this.rowLength.length);
            this.rowLength = objectArray;
            objectArray = new long[n2];
            System.arraycopy(this.rowOffset, 0, objectArray, 0, this.rowOffset.length);
            this.rowOffset = objectArray;
            objectArray = new boolean[n2];
            System.arraycopy(this.rowNull, 0, objectArray, 0, this.rowNull.length);
            this.rowNull = (boolean[])objectArray;
            objectArray = new byte[n2];
            System.arraycopy(this.rowMetadata, 0, objectArray, 0, this.rowMetadata.length);
            this.rowMetadata = (byte[])objectArray;
        }
    }

    void insertNull(int n2) throws SQLException {
        System.arraycopy(this.rowNull, n2, this.rowNull, n2 + 1, this.rowNull.length - n2 - 1);
        System.arraycopy(this.rowLength, n2, this.rowLength, n2 + 1, this.rowLength.length - n2 - 1);
        System.arraycopy(this.rowOffset, n2, this.rowOffset, n2 + 1, this.rowOffset.length - n2 - 1);
        System.arraycopy(this.rowMetadata, n2, this.rowMetadata, n2 + 1, this.rowMetadata.length - n2 - 1);
        this.setNull(n2, true);
    }

    Accessor copyForDefine(OracleStatement oracleStatement) {
        Accessor accessor = null;
        try {
            accessor = (Accessor)this.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            // empty catch block
        }
        accessor.rowNull = null;
        accessor.rowLength = null;
        accessor.rowOffset = null;
        accessor.rowMetadata = null;
        accessor.statement = oracleStatement;
        accessor.rowData = oracleStatement.rowData;
        oracleStatement.setDriverSpecificData(accessor);
        return accessor;
    }

    OracleResultSet.AuthorizationIndicator getAuthorizationIndicator(int n2) throws SQLException {
        byte by = this.getRowMetadata(n2);
        if ((by & 1) != 0) {
            return OracleResultSet.AuthorizationIndicator.UNAUTHORIZED;
        }
        if (this.securityAttribute == OracleResultSetMetaData.SecurityAttribute.ENABLED || this.securityAttribute == OracleResultSetMetaData.SecurityAttribute.NONE) {
            return OracleResultSet.AuthorizationIndicator.NONE;
        }
        return OracleResultSet.AuthorizationIndicator.UNKNOWN;
    }

    byte[] getBytesInternal(int n2) throws SQLException {
        assert (!this.isNull(n2));
        int n3 = this.getLength(n2);
        long l2 = this.getOffset(n2);
        return this.rowData.get(l2, n3);
    }

    final void getBytesInternal(int n2, byte[] byArray) throws SQLException {
        int n3 = this.getLength(n2);
        assert (byArray.length >= n3) : "data.length: " + byArray.length + " len: " + n3;
        long l2 = this.getOffset(n2);
        this.rowData.get(l2, byArray, 0, n3);
    }

    @Override
    byte[] getBytes(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return this.getBytesInternal(n2);
    }

    @Override
    String getString(int n2) throws SQLException {
        return null;
    }

    <T> T getObject(int n2, Class<T> clazz) throws SQLException {
        if (clazz == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 282).fillInStackTrace();
        }
        return this.representation.getObject(this, n2, clazz);
    }

    @Override
    Object getObject(int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        if (oracleDataFactory == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 281).fillInStackTrace();
        }
        Object object = this.getObject(n2, (Map)null);
        return oracleDataFactory.create(object, 0);
    }

    @Override
    ORAData getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        if (oRADataFactory == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 281).fillInStackTrace();
        }
        Datum datum = this.getOracleObject(n2);
        return oRADataFactory.create(datum, 0);
    }

    Datum[] getOraclePlsqlIndexTable(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOraclePlsqlIndexTable not implemented for " + this.getClass().getName()).fillInStackTrace();
    }

    @Override
    NClob getNClob(int n2) throws SQLException {
        if (this.formOfUse != 2) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
        return (NClob)((Object)this.getCLOB(n2));
    }

    @Override
    String getNString(int n2) throws SQLException {
        return this.getString(n2);
    }

    @Override
    Reader getNCharacterStream(int n2) throws SQLException {
        return this.getCharacterStream(n2);
    }

    int getBytes(int n2, byte[] byArray, int n3) throws SQLException {
        if (this.isNull(n2)) {
            return 0;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBytes not implemented for class " + this.getClass().getName()).fillInStackTrace();
    }

    final void copyFrom(Accessor accessor, int n2, int n3) throws SQLException {
        assert (this.getClass() == accessor.getClass()) : "srcAcc.class: " + accessor.getClass();
        assert (0 <= n2 && n2 < accessor.rowNull.length) : "srcRow: " + n2 + " srcAcc.capacity: " + accessor.rowNull.length;
        assert (0 <= n3 && n3 < this.rowNull.length) : "destRow: " + n3 + " capacity: " + this.rowNull.length;
        if (accessor.isNull(n2)) {
            this.setNull(n3, true);
        } else {
            this.copyFromInternal(accessor, n2, n3);
        }
    }

    protected void copyFromInternal(Accessor accessor, int n2, int n3) throws SQLException {
        boolean bl;
        boolean bl2 = bl = n3 > 0 && this.getOffset(n3 - 1) == this.getOffset(n3) || n3 + 1 < this.statement.storedRowCount && this.getOffset(n3 + 1) == this.getOffset(n3);
        if (bl && this.getLength(n3) == accessor.getLength(n2) && this.rowData.equalBytes(this.getOffset(n3), this.getLength(n3), accessor.rowData, accessor.getOffset(n2))) {
            this.setRowMetadata(n3, accessor.getRowMetadata(n2));
        } else {
            boolean bl3;
            boolean bl4 = bl3 = n3 + 1 == this.statement.storedRowCount;
            if (bl || accessor.getLength(n2) > this.getLength(n3) || bl3) {
                if (bl3) {
                    this.lastCopyRow = n3;
                    this.lastCopyRowOffset = this.getOffset(n3);
                    this.lastCopyRowLength = this.getLength(n3);
                    this.lastCopyRowMetaData = this.getRowMetadata(n3);
                    this.lastCopyRowIsNull = this.isNull(n3);
                }
                this.statement.locationToPutBytes(this, n3, accessor.getLength(n2));
            }
            this.rowData.put(this.getOffset(n3), accessor.rowData, accessor.getOffset(n2), accessor.getLength(n2));
            this.setLength(n3, accessor.getLength(n2));
            this.setRowMetadata(n3, accessor.getRowMetadata(n2));
        }
        this.setNull(n3, false);
    }

    void deleteRow(int n2) throws SQLException {
        this.rowData.freeSpace(this.getOffset(n2), this.getLength(n2));
        this.delete(this.rowNull, n2);
        this.delete(this.rowOffset, n2);
        this.delete(this.rowLength, n2);
        this.delete(this.rowMetadata, n2);
    }

    protected final void delete(Object object, int n2) {
        int n3 = Array.getLength(object);
        assert (n2 >= 0 && n2 < n3) : "row: " + n2 + " len: " + n3;
        System.arraycopy(object, n2 + 1, object, n2, n3 - n2 - 1);
    }

    AccessorPrototype newPrototype(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89, "newPrototype not overridden in " + this.getClass().getName()).fillInStackTrace();
    }

    void reinitForResultSetCache(ByteArray byteArray, OracleStatement oracleStatement) throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89, "reinitForResultSetCache not overridden in " + this.getClass().getName()).fillInStackTrace();
    }

    long previousOffset() {
        if (this.previousRowProcessed == -1) {
            return 0L;
        }
        return this.getOffset(this.previousRowProcessed);
    }

    boolean isLengthSemanticChar() {
        return 0L != (0x1000L & this.contflag);
    }
}

