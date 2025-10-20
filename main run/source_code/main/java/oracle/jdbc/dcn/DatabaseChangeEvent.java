/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.dcn;

import java.util.EventObject;
import oracle.jdbc.dcn.QueryChangeDescription;
import oracle.jdbc.dcn.TableChangeDescription;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CHANGE_NOTIFICATION})
public abstract class DatabaseChangeEvent
extends EventObject {
    protected DatabaseChangeEvent(Object object) {
        super(object);
    }

    public abstract EventType getEventType();

    public abstract AdditionalEventType getAdditionalEventType();

    public abstract TableChangeDescription[] getTableChangeDescription();

    public abstract QueryChangeDescription[] getQueryChangeDescription();

    public abstract String getConnectionInformation();

    public abstract String getDatabaseName();

    public abstract int getRegistrationId();

    public abstract long getRegId();

    public abstract byte[] getTransactionId();

    public abstract String getTransactionId(boolean var1);

    @Override
    public abstract String toString();

    public static enum AdditionalEventType {
        NONE(0),
        TIMEOUT(1),
        GROUPING(2);

        private final int code;

        private AdditionalEventType(int n3) {
            this.code = n3;
        }

        public final int getCode() {
            return this.code;
        }

        public static final AdditionalEventType getEventType(int n2) {
            if (n2 == TIMEOUT.getCode()) {
                return TIMEOUT;
            }
            if (n2 == GROUPING.getCode()) {
                return GROUPING;
            }
            return NONE;
        }
    }

    public static enum EventType {
        NONE(0),
        STARTUP(1),
        SHUTDOWN(2),
        SHUTDOWN_ANY(3),
        DEREG(5),
        OBJCHANGE(6),
        QUERYCHANGE(7);

        private final int code;

        private EventType(int n3) {
            this.code = n3;
        }

        public final int getCode() {
            return this.code;
        }

        public static final EventType getEventType(int n2) {
            if (n2 == STARTUP.getCode()) {
                return STARTUP;
            }
            if (n2 == SHUTDOWN.getCode()) {
                return SHUTDOWN;
            }
            if (n2 == SHUTDOWN_ANY.getCode()) {
                return SHUTDOWN_ANY;
            }
            if (n2 == DEREG.getCode()) {
                return DEREG;
            }
            if (n2 == OBJCHANGE.getCode()) {
                return OBJCHANGE;
            }
            if (n2 == QUERYCHANGE.getCode()) {
                return QUERYCHANGE;
            }
            return NONE;
        }
    }
}

