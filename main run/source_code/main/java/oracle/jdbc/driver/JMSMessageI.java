/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.InputStream;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.internal.JMSMessage;
import oracle.jdbc.internal.JMSMessageProperties;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.TypeDescriptor;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class JMSMessageI
implements JMSMessage {
    private byte[] payload;
    private byte[] messageId = null;
    private JMSMessageProperties jmsMessageProperties = null;
    private AQMessageProperties aqMessageProperties = null;
    private byte[] toid = TypeDescriptor.RAWTOID;
    private String typeName;
    private InputStream streamPayload = null;
    private int chunkSize;

    JMSMessageI(JMSMessageProperties jMSMessageProperties) {
        this.jmsMessageProperties = jMSMessageProperties;
    }

    @Override
    public byte[] getMessageId() {
        return this.messageId;
    }

    @Override
    public void setMessageId(byte[] byArray) {
        this.messageId = byArray;
    }

    @Override
    public byte[] getPayload() {
        return this.payload;
    }

    @Override
    public void setPayload(byte[] byArray) {
        this.payload = byArray;
    }

    @Override
    public InputStream getStreamPayload() {
        return this.streamPayload;
    }

    @Override
    public void setPayload(InputStream inputStream) {
        this.streamPayload = inputStream;
    }

    @Override
    public JMSMessageProperties getJMSMessageProperties() {
        return this.jmsMessageProperties;
    }

    @Override
    public void setJMSMessageProperties(JMSMessageProperties jMSMessageProperties) {
        this.jmsMessageProperties = jMSMessageProperties;
    }

    public void setTypeName(String string) {
        this.typeName = string;
    }

    public String getTypeName() {
        return this.typeName;
    }

    @Override
    public byte[] getToid() {
        return this.toid;
    }

    @Override
    public AQMessageProperties getAQMessageProperties() {
        return this.aqMessageProperties;
    }

    @Override
    public void setAQMessageProperties(AQMessageProperties aQMessageProperties) {
        this.aqMessageProperties = aQMessageProperties;
    }

    @Override
    public void setChunkSize(int n2) {
        this.chunkSize = n2;
    }

    @Override
    public int getChunkSize() {
        return this.chunkSize;
    }
}

