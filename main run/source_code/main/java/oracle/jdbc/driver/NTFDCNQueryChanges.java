/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.nio.ByteBuffer;
import oracle.jdbc.dcn.QueryChangeDescription;
import oracle.jdbc.dcn.TableChangeDescription;
import oracle.jdbc.driver.NTFDCNTableChanges;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFDCNQueryChanges
implements QueryChangeDescription {
    private final long queryId;
    private final QueryChangeDescription.QueryChangeEventType queryopflags;
    private final int numberOfTables;
    private final NTFDCNTableChanges[] tcdesc;

    NTFDCNQueryChanges(ByteBuffer byteBuffer, int n2) {
        long l2 = byteBuffer.getInt() & 0xFFFFFFFF;
        long l3 = byteBuffer.getInt() & 0xFFFFFFFF;
        this.queryId = l2 | l3 << 32;
        this.queryopflags = QueryChangeDescription.QueryChangeEventType.getQueryChangeEventType(byteBuffer.getInt());
        this.numberOfTables = byteBuffer.getShort();
        this.tcdesc = new NTFDCNTableChanges[this.numberOfTables];
        for (int i2 = 0; i2 < this.tcdesc.length; ++i2) {
            this.tcdesc[i2] = new NTFDCNTableChanges(byteBuffer, n2);
        }
    }

    @Override
    public long getQueryId() {
        return this.queryId;
    }

    @Override
    public QueryChangeDescription.QueryChangeEventType getQueryChangeEventType() {
        return this.queryopflags;
    }

    @Override
    public TableChangeDescription[] getTableChangeDescription() {
        return this.tcdesc;
    }

    @DisableTrace
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("  query ID=" + this.queryId + ", query change event type=" + (Object)((Object)this.queryopflags) + "\n");
        TableChangeDescription[] tableChangeDescriptionArray = this.getTableChangeDescription();
        if (tableChangeDescriptionArray != null) {
            stringBuffer.append("  Table Change Description (length=" + tableChangeDescriptionArray.length + "):");
            for (int i2 = 0; i2 < tableChangeDescriptionArray.length; ++i2) {
                stringBuffer.append(tableChangeDescriptionArray[i2].toString());
            }
        }
        return stringBuffer.toString();
    }
}

