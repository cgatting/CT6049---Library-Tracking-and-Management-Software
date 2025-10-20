/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.dcn;

import java.util.EnumSet;
import oracle.jdbc.dcn.TableChangeDescription;
import oracle.sql.ROWID;

public interface RowChangeDescription {
    @Deprecated
    public RowOperation getRowOperation();

    public EnumSet<RowOperation> getRowOperations();

    public ROWID getRowid();

    public static enum RowOperation {
        INSERT(TableChangeDescription.TableOperation.INSERT.getCode()),
        UPDATE(TableChangeDescription.TableOperation.UPDATE.getCode()),
        DELETE(TableChangeDescription.TableOperation.DELETE.getCode());

        private final int code;

        private RowOperation(int n3) {
            this.code = n3;
        }

        public final int getCode() {
            return this.code;
        }

        @Deprecated
        public static final RowOperation getRowOperation(int n2) {
            if ((n2 & INSERT.getCode()) != 0) {
                return INSERT;
            }
            if ((n2 & UPDATE.getCode()) != 0) {
                return UPDATE;
            }
            if ((n2 & DELETE.getCode()) != 0) {
                return DELETE;
            }
            return DELETE;
        }

        public static final EnumSet<RowOperation> getRowOperations(int n2) {
            EnumSet<RowOperation> enumSet = EnumSet.noneOf(RowOperation.class);
            if ((n2 & INSERT.getCode()) != 0) {
                enumSet.add(INSERT);
            }
            if ((n2 & UPDATE.getCode()) != 0) {
                enumSet.add(UPDATE);
            }
            if ((n2 & DELETE.getCode()) != 0) {
                enumSet.add(DELETE);
            }
            return enumSet;
        }
    }
}

