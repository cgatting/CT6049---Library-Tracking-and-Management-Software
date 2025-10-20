/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.dcn;

import java.util.EnumSet;
import oracle.jdbc.dcn.RowChangeDescription;

public interface TableChangeDescription {
    public EnumSet<TableOperation> getTableOperations();

    public String getTableName();

    public int getObjectNumber();

    public RowChangeDescription[] getRowChangeDescription();

    public static enum TableOperation {
        ALL_ROWS(1),
        INSERT(2),
        UPDATE(4),
        DELETE(8),
        ALTER(16),
        DROP(32);

        private final int code;

        private TableOperation(int n3) {
            this.code = n3;
        }

        public final int getCode() {
            return this.code;
        }

        public static final EnumSet<TableOperation> getTableOperations(int n2) {
            EnumSet<TableOperation> enumSet = EnumSet.noneOf(TableOperation.class);
            if ((n2 & ALL_ROWS.getCode()) != 0) {
                enumSet.add(ALL_ROWS);
            }
            if ((n2 & INSERT.getCode()) != 0) {
                enumSet.add(INSERT);
            }
            if ((n2 & UPDATE.getCode()) != 0) {
                enumSet.add(UPDATE);
            }
            if ((n2 & DELETE.getCode()) != 0) {
                enumSet.add(DELETE);
            }
            if ((n2 & ALTER.getCode()) != 0) {
                enumSet.add(ALTER);
            }
            if ((n2 & DROP.getCode()) != 0) {
                enumSet.add(DROP);
            }
            return enumSet;
        }
    }
}

