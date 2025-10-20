/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.EnumSet;
import java.util.Iterator;
import oracle.jdbc.dcn.RowChangeDescription;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.ROWID;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFDCNRowChanges
implements RowChangeDescription {
    EnumSet<RowChangeDescription.RowOperation> opcode;
    int rowidLength;
    byte[] rowid;
    ROWID rowidObj;

    NTFDCNRowChanges(EnumSet<RowChangeDescription.RowOperation> enumSet, int n2, byte[] byArray) {
        this.opcode = enumSet;
        this.rowidLength = n2;
        this.rowid = byArray;
        this.rowidObj = null;
    }

    @Override
    public ROWID getRowid() {
        if (this.rowidObj == null) {
            this.rowidObj = new ROWID(this.rowid);
        }
        return this.rowidObj;
    }

    @Override
    @Deprecated
    public RowChangeDescription.RowOperation getRowOperation() {
        Iterator iterator = this.opcode.iterator();
        if (iterator.hasNext()) {
            RowChangeDescription.RowOperation rowOperation = (RowChangeDescription.RowOperation)((Object)iterator.next());
            return rowOperation;
        }
        return null;
    }

    @Override
    public EnumSet<RowChangeDescription.RowOperation> getRowOperations() {
        return this.opcode;
    }

    @DisableTrace
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("      ROW:  operation=" + this.getRowOperations() + ", ROWID=" + new String(this.rowid) + "\n");
        return stringBuffer.toString();
    }
}

