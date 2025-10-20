/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;
import oracle.jdbc.aq.AQNotificationListener;
import oracle.jdbc.aq.AQNotificationRegistration;
import oracle.jdbc.driver.NTFEventListener;
import oracle.jdbc.driver.NTFRegistration;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFAQRegistration
extends NTFRegistration
implements AQNotificationRegistration {
    private final String name;
    private final boolean useSSL;

    NTFAQRegistration(int n2, boolean bl, boolean bl2, String string, String string2, String string3, int n3, @Blind(value=PropertiesBlinder.class) Properties properties, String string4, short s2, Exception[] exceptionArray) {
        super(n2, 1, bl2, string, string3, n3, properties, string2, s2, exceptionArray);
        this.name = string4;
        this.useSSL = bl;
    }

    @Override
    public void addListener(AQNotificationListener aQNotificationListener, Executor executor) throws SQLException {
        NTFEventListener nTFEventListener = new NTFEventListener(aQNotificationListener);
        nTFEventListener.setExecutor(executor);
        this.addListener(nTFEventListener);
    }

    @Override
    public void addListener(AQNotificationListener aQNotificationListener) throws SQLException {
        NTFEventListener nTFEventListener = new NTFEventListener(aQNotificationListener);
        this.addListener(nTFEventListener);
    }

    @Override
    public void removeListener(AQNotificationListener aQNotificationListener) throws SQLException {
        super.removeListener(aQNotificationListener);
    }

    @Override
    public String getQueueName() {
        return this.name;
    }

    public boolean getUseSSL() {
        return this.useSSL;
    }
}

