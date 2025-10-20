/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;
import oracle.jdbc.driver.ConsumerExceptionEvent;
import oracle.jdbc.driver.NTFEventListener;
import oracle.jdbc.driver.NTFRegistration;
import oracle.jdbc.internal.JMSConsumerExceptionListener;
import oracle.jdbc.internal.JMSNotificationListener;
import oracle.jdbc.internal.JMSNotificationRegistration;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFJMSRegistration
extends NTFRegistration
implements JMSNotificationRegistration {
    private final String name;
    private final String jmsConnectionId;
    private long jmsRegistrationId;
    private int qosFlag;

    NTFJMSRegistration(long l2, boolean bl, String string, String string2, @Blind(value=PropertiesBlinder.class) Properties properties, String string3, short s2, String string4) {
        super((int)l2, 1, bl, string, null, 0, properties, string2, s2, null);
        this.name = string3;
        this.jmsConnectionId = string4;
    }

    @Override
    public void addListener(JMSNotificationListener jMSNotificationListener, Executor executor) throws SQLException {
        NTFEventListener nTFEventListener = new NTFEventListener(jMSNotificationListener);
        nTFEventListener.setExecutor(executor);
        this.addListener(nTFEventListener);
    }

    @Override
    public void addListener(JMSNotificationListener jMSNotificationListener) throws SQLException {
        NTFEventListener nTFEventListener = new NTFEventListener(jMSNotificationListener);
        this.addListener(nTFEventListener);
    }

    @Override
    public void setConsumerExceptionListener(JMSConsumerExceptionListener jMSConsumerExceptionListener) throws SQLException {
        NTFEventListener nTFEventListener = new NTFEventListener(jMSConsumerExceptionListener);
        this.setNotificationExceptionListener(nTFEventListener);
    }

    @Override
    public void setConsumerExceptionListener(JMSConsumerExceptionListener jMSConsumerExceptionListener, Executor executor) throws SQLException {
        NTFEventListener nTFEventListener = new NTFEventListener(jMSConsumerExceptionListener);
        nTFEventListener.setExecutor(executor);
        this.setNotificationExceptionListener(nTFEventListener);
    }

    @Override
    public JMSConsumerExceptionListener getConsumerExceptionListener() throws SQLException {
        NTFEventListener nTFEventListener = this.getNotificationExceptionListener();
        JMSConsumerExceptionListener jMSConsumerExceptionListener = nTFEventListener.getNtfExceptionListener();
        return jMSConsumerExceptionListener;
    }

    void raiseException(int n2) throws SQLException {
        String string;
        NTFEventListener nTFEventListener = this.getNotificationExceptionListener();
        JMSConsumerExceptionListener jMSConsumerExceptionListener = nTFEventListener.getNtfExceptionListener();
        if (jMSConsumerExceptionListener == null) {
            return;
        }
        switch (n2) {
            case 1: {
                string = "Server Not Reachable";
                break;
            }
            case 2: {
                string = "Consumer Closed by Administrator";
                break;
            }
            case 3: {
                string = "Destination Queue/Topic closed by Administrator";
                break;
            }
            default: {
                return;
            }
        }
        ConsumerExceptionEvent consumerExceptionEvent = new ConsumerExceptionEvent(n2, string);
        jMSConsumerExceptionListener.onConsumerException(consumerExceptionEvent);
    }

    @Override
    public void removeListener(JMSNotificationListener jMSNotificationListener) throws SQLException {
        super.removeListener(jMSNotificationListener);
    }

    @Override
    public String getQueueName() {
        return this.name;
    }

    public String getJMSConnectionId() {
        return this.jmsConnectionId;
    }

    long getJMSRegistrationId() {
        return this.jmsRegistrationId;
    }

    void setJMSRegistrationId(long l2) {
        this.jmsRegistrationId = l2;
    }

    public int getQOSFlag() {
        return this.qosFlag;
    }

    public void setQOSFlag(int n2) {
        this.qosFlag = n2;
    }
}

