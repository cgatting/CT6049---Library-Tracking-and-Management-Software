/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Arrays;
import oracle.jdbc.OracleResultSetMetaData;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleType;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class AccessorPrototype {
    private final short formOfUse;
    private final String columnName;
    private final int describeType;
    private final int describeMaxLength;
    private final int describeMaxLengthChars;
    private final boolean nullable;
    private final int precision;
    private final int scale;
    private final OracleType describeOtype;
    private final OracleResultSetMetaData.SecurityAttribute securityAttribute;
    private final boolean columnInvisible;
    private final boolean columnJSON;
    private final int oacmxl;
    private final ByteArray rowData;
    private final long[] rowOffset;
    private final int[] rowLength;
    private final boolean[] rowNull;
    private final byte[] rowMetadata;

    abstract Accessor newAccessor(OracleStatement var1) throws SQLException;

    protected AccessorPrototype(int n2, Accessor accessor, ByteArray byteArray) {
        this.formOfUse = accessor.formOfUse;
        this.columnName = accessor.columnName;
        this.describeType = accessor.describeType;
        this.describeMaxLength = accessor.describeMaxLength;
        this.describeMaxLengthChars = accessor.describeMaxLengthChars;
        this.nullable = accessor.nullable;
        this.precision = accessor.precision;
        this.scale = accessor.scale;
        this.describeOtype = accessor.describeOtype;
        this.securityAttribute = accessor.securityAttribute;
        this.columnInvisible = accessor.columnInvisible;
        this.columnJSON = accessor.columnJSON;
        this.oacmxl = accessor.oacmxl;
        assert (n2 >= 0) : "numRows: " + n2;
        assert (accessor != null) : "null acc";
        assert (accessor.rowOffset != null) : "null acc.rowOffset";
        assert (accessor.rowOffset.length >= n2) : "accRowOffset.length: " + accessor.rowOffset.length + " numRows: " + n2;
        assert (accessor.rowLength != null) : "null acc.rowLength";
        assert (accessor.rowLength.length >= n2) : "accRowLength.length: " + accessor.rowLength.length + " numRows: " + n2;
        assert (accessor.rowNull != null) : "null acc.rowNull";
        assert (accessor.rowNull.length >= n2) : "accRowNull.length: " + accessor.rowLength.length + " numRows: " + n2;
        assert (accessor.rowMetadata != null) : "null acc.rowMetadata";
        assert (accessor.rowMetadata.length >= n2) : "accRowMetadata.length: " + accessor.rowMetadata.length + " numRows: " + n2;
        this.rowData = byteArray;
        this.rowOffset = Arrays.copyOfRange(accessor.rowOffset, 0, n2);
        this.rowLength = Arrays.copyOfRange(accessor.rowLength, 0, n2);
        this.rowNull = Arrays.copyOfRange(accessor.rowNull, 0, n2);
        this.rowMetadata = Arrays.copyOfRange(accessor.rowMetadata, 0, n2);
    }

    protected void initializeRowData(Accessor accessor) {
        assert (accessor != null) : "null acc";
        accessor.columnName = this.columnName;
        accessor.rowData = this.rowData;
        accessor.rowOffset = this.rowOffset;
        accessor.rowLength = this.rowLength;
        accessor.rowNull = this.rowNull;
        accessor.rowMetadata = this.rowMetadata;
    }
}

