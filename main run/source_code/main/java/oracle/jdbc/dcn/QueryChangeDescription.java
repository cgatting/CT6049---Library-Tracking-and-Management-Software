/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.dcn;

import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.TableChangeDescription;

public interface QueryChangeDescription {
    public long getQueryId();

    public QueryChangeEventType getQueryChangeEventType();

    public TableChangeDescription[] getTableChangeDescription();

    public static enum QueryChangeEventType {
        DEREG(DatabaseChangeEvent.EventType.DEREG.getCode()),
        QUERYCHANGE(DatabaseChangeEvent.EventType.QUERYCHANGE.getCode());

        private final int code;

        private QueryChangeEventType(int n3) {
            this.code = n3;
        }

        public final int getCode() {
            return this.code;
        }

        public static final QueryChangeEventType getQueryChangeEventType(int n2) {
            if (n2 == DEREG.getCode()) {
                return DEREG;
            }
            return QUERYCHANGE;
        }
    }
}

