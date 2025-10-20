/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.aq;

import java.sql.SQLException;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.ADVANCED_QUEUING})
public class AQDequeueOptions {
    public static final int DEQUEUE_WAIT_FOREVER = -1;
    public static final int DEQUEUE_NO_WAIT = 0;
    private String attrConsumerName = null;
    private String attrCorrelation = null;
    private DequeueMode attrDeqMode = DequeueMode.REMOVE;
    private byte[] attrDeqMsgId = null;
    private NavigationOption attrNavigation = NavigationOption.NEXT_MESSAGE;
    private VisibilityOption attrVisibility = VisibilityOption.ON_COMMIT;
    private int attrWait = -1;
    private int maxBufferLength = 0x3FFFFB3;
    private DeliveryFilter attrDeliveryMode = DeliveryFilter.PERSISTENT;
    private boolean retrieveMsgId = false;
    private String transformation;
    private String condition;
    public static final int MAX_RAW_PAYLOAD = 0x3FFFFB3;

    public void setConsumerName(String string) throws SQLException {
        this.attrConsumerName = string;
    }

    public String getConsumerName() {
        return this.attrConsumerName;
    }

    public void setCorrelation(String string) throws SQLException {
        this.attrCorrelation = string;
    }

    public String getCorrelation() {
        return this.attrCorrelation;
    }

    public void setDequeueMode(DequeueMode dequeueMode) throws SQLException {
        this.attrDeqMode = dequeueMode;
    }

    public DequeueMode getDequeueMode() {
        return this.attrDeqMode;
    }

    public void setDequeueMessageId(byte[] byArray) throws SQLException {
        this.attrDeqMsgId = byArray;
    }

    public byte[] getDequeueMessageId() {
        return this.attrDeqMsgId;
    }

    public void setNavigation(NavigationOption navigationOption) throws SQLException {
        this.attrNavigation = navigationOption;
    }

    public NavigationOption getNavigation() {
        return this.attrNavigation;
    }

    public void setVisibility(VisibilityOption visibilityOption) throws SQLException {
        this.attrVisibility = visibilityOption;
    }

    public VisibilityOption getVisibility() {
        return this.attrVisibility;
    }

    public void setWait(int n2) throws SQLException {
        this.attrWait = n2;
    }

    public int getWait() {
        return this.attrWait;
    }

    public void setMaximumBufferLength(int n2) throws SQLException {
        if (n2 > 0) {
            this.maxBufferLength = n2;
        }
    }

    public int getMaximumBufferLength() {
        return this.maxBufferLength;
    }

    public void setDeliveryFilter(DeliveryFilter deliveryFilter) throws SQLException {
        this.attrDeliveryMode = deliveryFilter;
    }

    public DeliveryFilter getDeliveryFilter() {
        return this.attrDeliveryMode;
    }

    public void setRetrieveMessageId(boolean bl) {
        this.retrieveMsgId = bl;
    }

    public boolean getRetrieveMessageId() {
        return this.retrieveMsgId;
    }

    public void setTransformation(String string) {
        this.transformation = string;
    }

    public String getTransformation() {
        return this.transformation;
    }

    public void setCondition(String string) {
        this.condition = string;
    }

    public String getCondition() {
        return this.condition;
    }

    public static enum DeliveryFilter {
        PERSISTENT(1),
        BUFFERED(2),
        PERSISTENT_OR_BUFFERED(3);

        private final int mode;

        private DeliveryFilter(int n3) {
            this.mode = n3;
        }

        public final int getCode() {
            return this.mode;
        }
    }

    public static enum VisibilityOption {
        ON_COMMIT(2),
        IMMEDIATE(1);

        private final int mode;

        private VisibilityOption(int n3) {
            this.mode = n3;
        }

        public final int getCode() {
            return this.mode;
        }
    }

    public static enum NavigationOption {
        FIRST_MESSAGE(1),
        NEXT_MESSAGE(3),
        NEXT_TRANSACTION(2);

        private final int mode;

        private NavigationOption(int n3) {
            this.mode = n3;
        }

        public final int getCode() {
            return this.mode;
        }
    }

    public static enum DequeueMode {
        BROWSE(1),
        LOCKED(2),
        REMOVE(3),
        REMOVE_NODATA(4);

        private final int mode;

        private DequeueMode(int n3) {
            this.mode = n3;
        }

        public final int getCode() {
            return this.mode;
        }
    }
}

