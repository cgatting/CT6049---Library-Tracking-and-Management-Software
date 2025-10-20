/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.aq;

import java.sql.SQLException;
import java.util.EventObject;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CHANGE_NOTIFICATION})
public abstract class AQNotificationEvent
extends EventObject {
    protected AQNotificationEvent(Object object) {
        super(object);
    }

    public abstract AQMessageProperties getMessageProperties() throws SQLException;

    public abstract String getRegistration() throws SQLException;

    public abstract byte[] getPayload() throws SQLException;

    public abstract String getQueueName() throws SQLException;

    public abstract byte[] getMessageId() throws SQLException;

    public abstract String getConsumerName() throws SQLException;

    public abstract String getConnectionInformation();

    public abstract EventType getEventType();

    public abstract AdditionalEventType getAdditionalEventType();

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
        REGULAR(0),
        DEREG(1);

        private final int code;

        private EventType(int n3) {
            this.code = n3;
        }

        public final int getCode() {
            return this.code;
        }
    }
}

