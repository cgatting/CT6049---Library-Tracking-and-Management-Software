/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.aq;

import java.sql.SQLException;
import oracle.jdbc.aq.AQDequeueOptions;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.ADVANCED_QUEUING})
public class AQEnqueueOptions {
    private byte[] attrRelativeMessageId = null;
    private SequenceDeviationOption attrSequenceDeviation = SequenceDeviationOption.BOTTOM;
    private VisibilityOption attrVisibility = VisibilityOption.ON_COMMIT;
    private DeliveryMode attrDeliveryMode = DeliveryMode.PERSISTENT;
    private boolean retrieveMsgId = false;
    private String transformation;

    public void setRelativeMessageId(byte[] byArray) throws SQLException {
        this.attrRelativeMessageId = byArray;
    }

    public byte[] getRelativeMessageId() {
        return this.attrRelativeMessageId;
    }

    public void setSequenceDeviation(SequenceDeviationOption sequenceDeviationOption) throws SQLException {
        this.attrSequenceDeviation = sequenceDeviationOption;
    }

    public SequenceDeviationOption getSequenceDeviation() {
        return this.attrSequenceDeviation;
    }

    public void setVisibility(VisibilityOption visibilityOption) throws SQLException {
        this.attrVisibility = visibilityOption;
    }

    public VisibilityOption getVisibility() {
        return this.attrVisibility;
    }

    public void setDeliveryMode(DeliveryMode deliveryMode) throws SQLException {
        this.attrDeliveryMode = deliveryMode;
    }

    public DeliveryMode getDeliveryMode() {
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

    public static enum DeliveryMode {
        PERSISTENT(AQDequeueOptions.DeliveryFilter.PERSISTENT.getCode()),
        BUFFERED(AQDequeueOptions.DeliveryFilter.BUFFERED.getCode());

        private final int mode;

        private DeliveryMode(int n3) {
            this.mode = n3;
        }

        public final int getCode() {
            return this.mode;
        }
    }

    public static enum SequenceDeviationOption {
        BOTTOM(0),
        BEFORE(2),
        TOP(3);

        private final int mode;

        private SequenceDeviationOption(int n3) {
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
}

