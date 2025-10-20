/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.aq;

import java.sql.SQLException;
import java.sql.Timestamp;
import oracle.jdbc.aq.AQAgent;

public interface AQMessageProperties {
    public static final int MESSAGE_NO_DELAY = 0;
    public static final int MESSAGE_NO_EXPIRATION = -1;

    public int getDequeueAttemptsCount();

    public void setCorrelation(String var1) throws SQLException;

    public String getCorrelation();

    public void setDelay(int var1) throws SQLException;

    public int getDelay();

    public Timestamp getEnqueueTime();

    public void setExceptionQueue(String var1) throws SQLException;

    public String getExceptionQueue();

    public void setExpiration(int var1) throws SQLException;

    public int getExpiration();

    public MessageState getState();

    public void setPriority(int var1) throws SQLException;

    public int getPriority();

    public void setRecipientList(AQAgent[] var1) throws SQLException;

    public AQAgent[] getRecipientList();

    public void setSender(AQAgent var1) throws SQLException;

    public AQAgent getSender();

    public String getTransactionGroup();

    public byte[] getPreviousQueueMessageId();

    public DeliveryMode getDeliveryMode();

    public String toString();

    public static enum DeliveryMode {
        PERSISTENT(1),
        BUFFERED(2);

        private final int code;

        private DeliveryMode(int n3) {
            this.code = n3;
        }

        public final int getCode() {
            return this.code;
        }

        public static final DeliveryMode getDeliveryMode(int n2) {
            if (n2 == BUFFERED.getCode()) {
                return BUFFERED;
            }
            return PERSISTENT;
        }
    }

    public static enum MessageState {
        WAITING(1),
        READY(0),
        PROCESSED(2),
        EXPIRED(3);

        private final int code;

        private MessageState(int n3) {
            this.code = n3;
        }

        public final int getCode() {
            return this.code;
        }

        public static final MessageState getMessageState(int n2) {
            if (n2 == WAITING.getCode()) {
                return WAITING;
            }
            if (n2 == READY.getCode()) {
                return READY;
            }
            if (n2 == PROCESSED.getCode()) {
                return PROCESSED;
            }
            return EXPIRED;
        }
    }
}

