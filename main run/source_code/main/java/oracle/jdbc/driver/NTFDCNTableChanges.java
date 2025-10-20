/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicReference;
import oracle.jdbc.dcn.RowChangeDescription;
import oracle.jdbc.dcn.TableChangeDescription;
import oracle.jdbc.driver.NTFDCNRowChanges;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CharacterSet;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFDCNTableChanges
implements TableChangeDescription {
    private static final byte OPERATION_ANY = 0;
    private static final byte OPERATION_UNKNOWN = 64;
    final EnumSet<TableChangeDescription.TableOperation> opcode;
    String tableName;
    final int objectNumber;
    final int numberOfRows;
    ArrayList<EnumSet<RowChangeDescription.RowOperation>> rowOpcode;
    final int[] rowIdLength;
    final byte[][] rowid;
    final CharacterSet charset;
    private AtomicReference<NTFDCNRowChanges[]> rowsDescriptionRef = new AtomicReference();

    NTFDCNTableChanges(ByteBuffer byteBuffer, int n2) {
        this.charset = CharacterSet.make(n2);
        this.opcode = TableChangeDescription.TableOperation.getTableOperations(byteBuffer.getInt());
        short s2 = byteBuffer.getShort();
        byte[] byArray = new byte[s2];
        byteBuffer.get(byArray, 0, s2);
        this.tableName = this.charset.toStringWithReplacement(byArray, 0, s2);
        this.objectNumber = byteBuffer.getInt();
        if (!this.opcode.contains((Object)TableChangeDescription.TableOperation.ALL_ROWS)) {
            this.numberOfRows = byteBuffer.getShort();
            this.rowOpcode = new ArrayList(this.numberOfRows);
            this.rowIdLength = new int[this.numberOfRows];
            this.rowid = new byte[this.numberOfRows][];
            for (int i2 = 0; i2 < this.numberOfRows; ++i2) {
                this.rowOpcode.add(RowChangeDescription.RowOperation.getRowOperations(byteBuffer.getInt()));
                this.rowIdLength[i2] = byteBuffer.getShort();
                this.rowid[i2] = new byte[this.rowIdLength[i2]];
                byteBuffer.get(this.rowid[i2], 0, this.rowIdLength[i2]);
            }
        } else {
            this.numberOfRows = 0;
            this.rowid = null;
            this.rowOpcode = null;
            this.rowIdLength = null;
        }
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public int getObjectNumber() {
        return this.objectNumber;
    }

    @Override
    public RowChangeDescription[] getRowChangeDescription() {
        return this.rowsDescriptionRef.updateAndGet(nTFDCNRowChangesArray -> {
            if (nTFDCNRowChangesArray == null) {
                NTFDCNRowChanges[] nTFDCNRowChangesArray2 = new NTFDCNRowChanges[this.numberOfRows];
                Arrays.setAll(nTFDCNRowChangesArray2, n2 -> new NTFDCNRowChanges(this.rowOpcode.get(n2), this.rowIdLength[n2], this.rowid[n2]));
                return nTFDCNRowChangesArray2;
            }
            return nTFDCNRowChangesArray;
        });
    }

    @Override
    public EnumSet<TableChangeDescription.TableOperation> getTableOperations() {
        return this.opcode;
    }

    @DisableTrace
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("    operation=" + this.getTableOperations() + ", tableName=" + this.tableName + ", objectNumber=" + this.objectNumber + "\n");
        RowChangeDescription[] rowChangeDescriptionArray = this.getRowChangeDescription();
        if (rowChangeDescriptionArray != null && rowChangeDescriptionArray.length > 0) {
            stringBuffer.append("    Row Change Description (length=" + rowChangeDescriptionArray.length + "):\n");
            for (int i2 = 0; i2 < rowChangeDescriptionArray.length; ++i2) {
                stringBuffer.append(rowChangeDescriptionArray[i2].toString());
            }
        }
        return stringBuffer.toString();
    }
}

