/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.internal.JMSMessageProperties;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class JMSMessagePropertiesI
implements JMSMessageProperties {
    private String headerProperties = "0";
    private String userProperties = "0";
    private JMSMessageProperties.JMSMessageType jmsMessageType = JMSMessageProperties.JMSMessageType.TEXT_MESSAGE;

    JMSMessagePropertiesI() {
    }

    @Override
    public String getHeaderProperties() {
        return this.headerProperties;
    }

    @Override
    public void setHeaderProperties(String string) {
        this.headerProperties = string;
    }

    @Override
    public String getUserProperties() {
        return this.userProperties;
    }

    @Override
    public void setUserProperties(String string) {
        this.userProperties = string;
    }

    @Override
    public JMSMessageProperties.JMSMessageType getJMSMessageType() {
        return this.jmsMessageType;
    }

    @Override
    public void setJMSMessageType(JMSMessageProperties.JMSMessageType jMSMessageType) {
        this.jmsMessageType = jMSMessageType;
    }
}

