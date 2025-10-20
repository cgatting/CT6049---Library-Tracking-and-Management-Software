/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.sql.Timestamp;
import oracle.jdbc.aq.AQAgent;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.driver.AQAgentI;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class AQMessagePropertiesI
implements AQMessageProperties {
    private int attrAttempts = -1;
    private String attrCorrelation = null;
    private int attrDelay = 0;
    private Timestamp attrEnqTime = null;
    private String attrExceptionQueue = null;
    private int attrExpiration = -1;
    private AQMessageProperties.MessageState attrMsgState = null;
    private int attrPriority = 0;
    private AQAgentI[] attrRecipientList = null;
    private AQAgentI attrSenderId = null;
    private String attrTransactionGroup = null;
    private byte[] attrPreviousQueueMsgId = null;
    private AQMessageProperties.DeliveryMode deliveryMode = null;

    AQMessagePropertiesI() {
    }

    @Override
    public int getDequeueAttemptsCount() {
        return this.attrAttempts;
    }

    @Override
    public void setCorrelation(String string) throws SQLException {
        this.attrCorrelation = string;
    }

    @Override
    public String getCorrelation() {
        return this.attrCorrelation;
    }

    @Override
    public void setDelay(int n2) throws SQLException {
        this.attrDelay = n2;
    }

    @Override
    public int getDelay() {
        return this.attrDelay;
    }

    @Override
    public Timestamp getEnqueueTime() {
        return this.attrEnqTime;
    }

    @Override
    public void setExceptionQueue(String string) throws SQLException {
        this.attrExceptionQueue = string;
    }

    @Override
    public String getExceptionQueue() {
        return this.attrExceptionQueue;
    }

    @Override
    public void setExpiration(int n2) throws SQLException {
        this.attrExpiration = n2;
    }

    @Override
    public int getExpiration() {
        return this.attrExpiration;
    }

    @Override
    public AQMessageProperties.MessageState getState() {
        return this.attrMsgState;
    }

    @Override
    public void setPriority(int n2) throws SQLException {
        this.attrPriority = n2;
    }

    @Override
    public int getPriority() {
        return this.attrPriority;
    }

    @Override
    public void setRecipientList(AQAgent[] aQAgentArray) throws SQLException {
        if (aQAgentArray == null) {
            this.attrRecipientList = null;
        } else {
            this.attrRecipientList = new AQAgentI[aQAgentArray.length];
            for (int i2 = 0; i2 < aQAgentArray.length; ++i2) {
                this.attrRecipientList[i2] = (AQAgentI)aQAgentArray[i2];
            }
        }
    }

    @Override
    public AQAgent[] getRecipientList() {
        return this.attrRecipientList;
    }

    @Override
    public void setSender(AQAgent aQAgent) throws SQLException {
        this.attrSenderId = (AQAgentI)aQAgent;
    }

    @Override
    public AQAgent getSender() {
        return this.attrSenderId;
    }

    @Override
    public String getTransactionGroup() {
        return this.attrTransactionGroup;
    }

    void setTransactionGroup(String string) {
        this.attrTransactionGroup = string;
    }

    void setPreviousQueueMessageId(byte[] byArray) {
        this.attrPreviousQueueMsgId = byArray;
    }

    @Override
    public byte[] getPreviousQueueMessageId() {
        return this.attrPreviousQueueMsgId;
    }

    @Override
    public AQMessageProperties.DeliveryMode getDeliveryMode() {
        return this.deliveryMode;
    }

    void setDeliveryMode(AQMessageProperties.DeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    @Override
    @DisableTrace
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Correlation             : " + this.getCorrelation() + "\n");
        Timestamp timestamp = this.getEnqueueTime();
        if (timestamp != null) {
            stringBuffer.append("Enqueue time            : " + timestamp + "\n");
        }
        stringBuffer.append("Exception Queue         : " + this.getExceptionQueue() + "\n");
        stringBuffer.append("Sender                  : (" + this.getSender() + ")\n");
        int n2 = this.getDequeueAttemptsCount();
        if (n2 != -1) {
            stringBuffer.append("Attempts                : " + n2 + "\n");
        }
        stringBuffer.append("Delay                   : " + this.getDelay() + "\n");
        stringBuffer.append("Expiration              : " + this.getExpiration() + "\n");
        AQMessageProperties.MessageState messageState = this.getState();
        if (messageState != null) {
            stringBuffer.append("State                   : " + (Object)((Object)messageState) + "\n");
        }
        stringBuffer.append("Priority                : " + this.getPriority() + "\n");
        AQMessageProperties.DeliveryMode deliveryMode = this.getDeliveryMode();
        if (deliveryMode != null) {
            stringBuffer.append("Delivery Mode           : " + (Object)((Object)deliveryMode) + "\n");
        }
        stringBuffer.append("Recipient List          : {");
        AQAgent[] aQAgentArray = this.getRecipientList();
        if (aQAgentArray != null) {
            for (int i2 = 0; i2 < aQAgentArray.length; ++i2) {
                stringBuffer.append(aQAgentArray[i2]);
                if (i2 == aQAgentArray.length - 1) continue;
                stringBuffer.append("; ");
            }
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }

    void setAttempts(int n2) throws SQLException {
        this.attrAttempts = n2;
    }

    void setEnqueueTime(Timestamp timestamp) throws SQLException {
        this.attrEnqTime = timestamp;
    }

    void setMessageState(AQMessageProperties.MessageState messageState) throws SQLException {
        this.attrMsgState = messageState;
    }
}

