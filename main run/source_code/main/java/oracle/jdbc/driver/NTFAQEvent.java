/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.aq.AQNotificationEvent;
import oracle.jdbc.driver.AQAgentI;
import oracle.jdbc.driver.AQMessagePropertiesI;
import oracle.jdbc.driver.NTFConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.TIMESTAMP;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFAQEvent
extends AQNotificationEvent {
    private static final long serialVersionUID = 4073258592022522641L;
    private String registrationString;
    private int namespace;
    private byte[] payload;
    private String queueName = null;
    private byte[] messageId = null;
    private String consumerName = null;
    private NTFConnection conn;
    private AQMessagePropertiesI msgProp;
    private AQNotificationEvent.EventType eventType = AQNotificationEvent.EventType.REGULAR;
    private AQNotificationEvent.AdditionalEventType additionalEventType = AQNotificationEvent.AdditionalEventType.NONE;
    private ByteBuffer dataBuffer;
    private boolean isReady = false;
    private short databaseVersion;

    NTFAQEvent(NTFConnection nTFConnection, short s2) throws IOException, InterruptedException {
        super(nTFConnection);
        this.conn = nTFConnection;
        int n2 = this.conn.readInt();
        byte[] byArray = new byte[n2];
        this.conn.readBuffer(byArray, 0, n2);
        this.dataBuffer = ByteBuffer.wrap(byArray);
        this.databaseVersion = s2;
    }

    private void initEvent() throws SQLException {
        byte by = this.dataBuffer.get();
        int n2 = this.dataBuffer.getInt();
        byte[] byArray = new byte[n2];
        this.dataBuffer.get(byArray, 0, n2);
        this.registrationString = this.conn.charset.toString(byArray, 0, n2);
        byte by2 = this.dataBuffer.get();
        int n3 = this.dataBuffer.getInt();
        byte[] byArray2 = new byte[n3];
        this.dataBuffer.get(byArray2, 0, n3);
        this.namespace = byArray2[0];
        byte by3 = this.dataBuffer.get();
        int n4 = this.dataBuffer.getInt();
        if (n4 > 0) {
            this.payload = new byte[n4];
            this.dataBuffer.get(this.payload, 0, n4);
        } else {
            this.payload = null;
        }
        if (this.dataBuffer.hasRemaining()) {
            int n5;
            byte by4;
            int n6 = 0;
            if (this.databaseVersion >= 10200) {
                by4 = this.dataBuffer.get();
                n5 = this.dataBuffer.getInt();
                n6 = this.dataBuffer.getInt();
            }
            by4 = this.dataBuffer.get();
            n5 = this.dataBuffer.getInt();
            byte[] byArray3 = new byte[n5];
            this.dataBuffer.get(byArray3, 0, n5);
            this.queueName = this.conn.charset.toString(byArray3, 0, n5);
            byte by5 = this.dataBuffer.get();
            int n7 = this.dataBuffer.getInt();
            this.messageId = new byte[n7];
            this.dataBuffer.get(this.messageId, 0, n7);
            byte by6 = this.dataBuffer.get();
            int n8 = this.dataBuffer.getInt();
            byte[] byArray4 = new byte[n8];
            this.dataBuffer.get(byArray4, 0, n8);
            this.consumerName = this.conn.charset.toString(byArray4, 0, n8);
            byte by7 = this.dataBuffer.get();
            int n9 = this.dataBuffer.getInt();
            byte[] byArray5 = new byte[n9];
            this.dataBuffer.get(byArray5, 0, n9);
            byte by8 = this.dataBuffer.get();
            int n10 = this.dataBuffer.getInt();
            int n11 = this.dataBuffer.getInt();
            if (byArray5[0] == 1) {
                n11 = -n11;
            }
            int n12 = n11;
            byte by9 = this.dataBuffer.get();
            int n13 = this.dataBuffer.getInt();
            int n14 = this.dataBuffer.getInt();
            byte by10 = this.dataBuffer.get();
            int n15 = this.dataBuffer.getInt();
            byte[] byArray6 = new byte[n15];
            this.dataBuffer.get(byArray6, 0, n15);
            byte by11 = this.dataBuffer.get();
            int n16 = this.dataBuffer.getInt();
            int n17 = this.dataBuffer.getInt();
            if (byArray6[0] == 1) {
                n17 = -n17;
            }
            int n18 = n17;
            byte by12 = this.dataBuffer.get();
            int n19 = this.dataBuffer.getInt();
            int n20 = this.dataBuffer.getInt();
            byte by13 = this.dataBuffer.get();
            int n21 = this.dataBuffer.getInt();
            byte[] byArray7 = new byte[n21];
            this.dataBuffer.get(byArray7, 0, n21);
            TIMESTAMP tIMESTAMP = new TIMESTAMP(byArray7);
            byte by14 = this.dataBuffer.get();
            int n22 = this.dataBuffer.getInt();
            byte[] byArray8 = new byte[n22];
            this.dataBuffer.get(byArray8, 0, n22);
            byte by15 = byArray8[0];
            byte by16 = this.dataBuffer.get();
            int n23 = this.dataBuffer.getInt();
            byte[] byArray9 = new byte[n23];
            this.dataBuffer.get(byArray9, 0, n23);
            String string = this.conn.charset.toString(byArray9, 0, n23);
            byte by17 = this.dataBuffer.get();
            int n24 = this.dataBuffer.getInt();
            byte[] byArray10 = new byte[n24];
            this.dataBuffer.get(byArray10, 0, n24);
            String string2 = this.conn.charset.toString(byArray10, 0, n24);
            byte by18 = this.dataBuffer.get();
            int n25 = this.dataBuffer.getInt();
            byte[] byArray11 = null;
            if (n25 > 0) {
                byArray11 = new byte[n25];
                this.dataBuffer.get(byArray11, 0, n25);
            }
            byte by19 = this.dataBuffer.get();
            int n26 = this.dataBuffer.getInt();
            byte[] byArray12 = new byte[n26];
            this.dataBuffer.get(byArray12, 0, n26);
            String string3 = this.conn.charset.toString(byArray12, 0, n26);
            byte by20 = this.dataBuffer.get();
            int n27 = this.dataBuffer.getInt();
            byte[] byArray13 = new byte[n27];
            this.dataBuffer.get(byArray13, 0, n27);
            String string4 = this.conn.charset.toString(byArray13, 0, n27);
            byte by21 = this.dataBuffer.get();
            int n28 = this.dataBuffer.getInt();
            byte by22 = this.dataBuffer.get();
            this.msgProp = new AQMessagePropertiesI();
            this.msgProp.setAttempts(n20);
            this.msgProp.setCorrelation(string2);
            this.msgProp.setDelay(n14);
            this.msgProp.setEnqueueTime(tIMESTAMP.timestampValue());
            this.msgProp.setMessageState(AQMessageProperties.MessageState.getMessageState(by15));
            if (this.databaseVersion >= 10200) {
                this.msgProp.setDeliveryMode(AQMessageProperties.DeliveryMode.getDeliveryMode(n6));
            }
            this.msgProp.setPreviousQueueMessageId(byArray11);
            AQAgentI aQAgentI = new AQAgentI();
            aQAgentI.setAddress(string4);
            aQAgentI.setName(string3);
            aQAgentI.setProtocol(by22);
            this.msgProp.setSender(aQAgentI);
            this.msgProp.setPriority(n12);
            this.msgProp.setExpiration(n18);
            this.msgProp.setExceptionQueue(string);
        }
        this.isReady = true;
    }

    @Override
    public AQMessageProperties getMessageProperties() throws SQLException {
        if (!this.isReady) {
            this.initEvent();
        }
        return this.msgProp;
    }

    @Override
    public String getRegistration() throws SQLException {
        if (!this.isReady) {
            this.initEvent();
        }
        return this.registrationString;
    }

    @Override
    public AQNotificationEvent.EventType getEventType() {
        return this.eventType;
    }

    @Override
    public AQNotificationEvent.AdditionalEventType getAdditionalEventType() {
        return this.additionalEventType;
    }

    void setEventType(AQNotificationEvent.EventType eventType) throws IOException {
        this.eventType = eventType;
    }

    void setAdditionalEventType(AQNotificationEvent.AdditionalEventType additionalEventType) {
        this.additionalEventType = additionalEventType;
    }

    @Override
    public byte[] getPayload() throws SQLException {
        if (!this.isReady) {
            this.initEvent();
        }
        return this.payload;
    }

    @Override
    public String getQueueName() throws SQLException {
        if (!this.isReady) {
            this.initEvent();
        }
        return this.queueName;
    }

    @Override
    public byte[] getMessageId() throws SQLException {
        if (!this.isReady) {
            this.initEvent();
        }
        return this.messageId;
    }

    @Override
    public String getConsumerName() throws SQLException {
        if (!this.isReady) {
            this.initEvent();
        }
        return this.consumerName;
    }

    @Override
    public String getConnectionInformation() {
        return this.conn.connectionDescription;
    }

    @Override
    @DisableTrace
    public String toString() {
        if (!this.isReady) {
            try {
                this.initEvent();
            }
            catch (SQLException sQLException) {
                return sQLException.getMessage();
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Connection information  : " + this.conn.connectionDescription + "\n");
        stringBuffer.append("Event type              : " + (Object)((Object)this.eventType) + "\n");
        if (this.additionalEventType != AQNotificationEvent.AdditionalEventType.NONE) {
            stringBuffer.append("Additional event type   : " + (Object)((Object)this.additionalEventType) + "\n");
        }
        stringBuffer.append("Namespace               : " + this.namespace + "\n");
        stringBuffer.append("Registration            : " + this.registrationString + "\n");
        stringBuffer.append("Queue name              : " + this.queueName + "\n");
        stringBuffer.append("Consumer name           : " + this.consumerName + "\n");
        if (this.payload != null) {
            stringBuffer.append("Payload length          : " + this.payload.length + "\n");
            stringBuffer.append("Payload (first 50 bytes): " + NTFAQEvent.byteBufferToHexString(this.payload, 50) + "\n");
        } else {
            stringBuffer.append("Payload                 : null\n");
        }
        stringBuffer.append("Message ID              : " + NTFAQEvent.byteBufferToHexString(this.messageId, 50) + "\n");
        if (this.msgProp != null) {
            stringBuffer.append(this.msgProp.toString());
        }
        return stringBuffer.toString();
    }

    static final String byteBufferToHexString(byte[] byArray, int n2) {
        String string;
        if (byArray == null) {
            return null;
        }
        boolean bl = true;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < byArray.length && i2 < n2; ++i2) {
            if (!bl) {
                stringBuffer.append(' ');
            } else {
                bl = false;
            }
            string = Integer.toHexString(byArray[i2] & 0xFF);
            if (string.length() == 1) {
                string = "0" + string;
            }
            stringBuffer.append(string);
        }
        string = stringBuffer.toString();
        return string;
    }
}

