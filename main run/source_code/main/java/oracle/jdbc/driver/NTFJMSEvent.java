/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.driver.T4CTTIoaqnfy;
import oracle.jdbc.internal.JMSMessageProperties;
import oracle.jdbc.internal.JMSNotificationEvent;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
public class NTFJMSEvent
extends JMSNotificationEvent {
    private static final long serialVersionUID = -5443493896649237175L;
    private JMSMessageProperties jmsMessageProperties = null;
    private AQMessageProperties aqMessageProperties = null;
    private String registrationString = null;
    private byte[] payload;
    private String queueName = null;
    private byte[] messageId = null;
    private String consumerName = null;

    NTFJMSEvent(T4CTTIoaqnfy t4CTTIoaqnfy) {
        super(t4CTTIoaqnfy);
    }

    @Override
    public JMSMessageProperties getJMSMessageProperties() throws SQLException {
        return this.jmsMessageProperties;
    }

    @Override
    public AQMessageProperties getMessageProperties() throws SQLException {
        return this.aqMessageProperties;
    }

    @Override
    public byte[] getPayload() throws SQLException {
        return this.payload;
    }

    @Override
    public String getQueueName() throws SQLException {
        return this.queueName;
    }

    @Override
    public byte[] getMessageId() throws SQLException {
        return this.messageId;
    }

    @Override
    public String getConsumerName() throws SQLException {
        return this.consumerName;
    }

    @Override
    public String getRegistrationName() throws SQLException {
        return this.registrationString;
    }

    void setAqMessageProperites(AQMessageProperties aQMessageProperties) {
        this.aqMessageProperties = aQMessageProperties;
    }

    void setConsumerName(String string) {
        this.consumerName = string;
    }

    void setJmsMessageProperties(JMSMessageProperties jMSMessageProperties) {
        this.jmsMessageProperties = jMSMessageProperties;
    }

    void setMessageId(byte[] byArray) {
        this.messageId = byArray;
    }

    void setPayload(byte[] byArray) {
        this.payload = byArray;
    }

    void setQueueName(String string) {
        this.queueName = string;
    }

    void setRegistration(String string) {
        this.registrationString = string;
    }
}

