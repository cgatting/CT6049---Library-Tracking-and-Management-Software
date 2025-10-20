/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.EventListener;
import java.util.Properties;
import java.util.concurrent.Executor;
import oracle.jdbc.NotificationRegistration;
import oracle.jdbc.aq.AQNotificationEvent;
import oracle.jdbc.aq.AQNotificationListener;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.NTFAQEvent;
import oracle.jdbc.driver.NTFDCNEvent;
import oracle.jdbc.driver.NTFEventListener;
import oracle.jdbc.driver.NTFJMSEvent;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.JMSNotificationListener;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CHANGE_NOTIFICATION})
abstract class NTFRegistration
implements Monitor {
    private final boolean jdbcGetsNotification;
    private final String clientHost;
    private final int clientTCPPort;
    private final Properties options;
    private final boolean isPurgeOnNTF;
    private final String username;
    private final int namespace;
    private final int jdbcRegId;
    private final String dbName;
    private final short databaseVersion;
    private NotificationRegistration.RegistrationState state;
    private NTFEventListener[] listeners = new NTFEventListener[0];
    private NTFEventListener notificationExceptionListener;
    private final Exception[] connectionCreationExceptionArr;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();

    NTFRegistration(int n2, int n3, boolean bl, String string, String string2, int n4, @Blind(value=PropertiesBlinder.class) Properties properties, String string3, short s2, Exception[] exceptionArray) {
        this.namespace = n3;
        this.clientHost = string2;
        this.clientTCPPort = n4;
        this.options = properties;
        this.jdbcRegId = n2;
        this.username = string3;
        this.jdbcGetsNotification = bl;
        this.dbName = string;
        this.state = NotificationRegistration.RegistrationState.ACTIVE;
        this.isPurgeOnNTF = this.options != null && this.options.getProperty("NTF_QOS_PURGE_ON_NTFN", "false").compareToIgnoreCase("true") == 0;
        this.databaseVersion = s2;
        this.connectionCreationExceptionArr = exceptionArray;
    }

    short getDatabaseVersion() {
        return this.databaseVersion;
    }

    void setNotificationExceptionListener(NTFEventListener nTFEventListener) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.state == NotificationRegistration.RegistrationState.CLOSED) {
                throw (SQLException)DatabaseError.createSqlException(251).fillInStackTrace();
            }
            this.notificationExceptionListener = nTFEventListener;
        }
    }

    NTFEventListener getNotificationExceptionListener() {
        return this.notificationExceptionListener;
    }

    void addListener(NTFEventListener nTFEventListener) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.state == NotificationRegistration.RegistrationState.CLOSED) {
                throw (SQLException)DatabaseError.createSqlException(251).fillInStackTrace();
            }
            if (!this.jdbcGetsNotification) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 247).fillInStackTrace();
            }
            int n2 = this.listeners.length;
            for (int i2 = 0; i2 < n2; ++i2) {
                if (this.listeners[i2].getListener() != nTFEventListener.getListener()) continue;
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 248).fillInStackTrace();
            }
            NTFEventListener[] nTFEventListenerArray = new NTFEventListener[n2 + 1];
            System.arraycopy(this.listeners, 0, nTFEventListenerArray, 0, n2);
            nTFEventListenerArray[n2] = nTFEventListener;
            this.listeners = nTFEventListenerArray;
        }
    }

    void removeListener(EventListener eventListener) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n2 = 0;
            int n3 = this.listeners.length;
            for (n2 = 0; n2 < n3 && this.listeners[n2].getListener() != eventListener; ++n2) {
            }
            if (n2 == n3) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 249).fillInStackTrace();
            }
            NTFEventListener[] nTFEventListenerArray = new NTFEventListener[n3 - 1];
            int n4 = 0;
            for (n2 = 0; n2 < n3; ++n2) {
                if (this.listeners[n2].getListener() == eventListener) continue;
                nTFEventListenerArray[n4++] = this.listeners[n2];
            }
            this.listeners = nTFEventListenerArray;
        }
    }

    void notify(final NTFDCNEvent nTFDCNEvent) {
        long l2 = 0L;
        NTFEventListener[] nTFEventListenerArray = this.listeners;
        int n2 = nTFEventListenerArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Executor executor = nTFEventListenerArray[i2].getExecutor();
            if (executor != null) {
                final DatabaseChangeListener databaseChangeListener = nTFEventListenerArray[i2].getDCNListener();
                executor.execute(new Runnable(){

                    @Override
                    public void run() {
                        databaseChangeListener.onDatabaseChangeNotification(nTFDCNEvent);
                    }
                });
                continue;
            }
            nTFEventListenerArray[i2].getDCNListener().onDatabaseChangeNotification(nTFDCNEvent);
        }
        if (nTFDCNEvent.isDeregistrationEvent() || this.isPurgeOnNTF) {
            PhysicalConnection.ntfManager.removeRegistration(this);
            PhysicalConnection.ntfManager.freeJdbcRegId(this.getJdbcRegId());
            PhysicalConnection.ntfManager.cleanListenersT4C(this.getClientTCPPort());
            this.state = NotificationRegistration.RegistrationState.CLOSED;
        }
    }

    void notify(final NTFAQEvent nTFAQEvent) {
        long l2 = 0L;
        NTFEventListener[] nTFEventListenerArray = this.listeners;
        int n2 = nTFEventListenerArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Executor executor = nTFEventListenerArray[i2].getExecutor();
            if (executor != null) {
                final AQNotificationListener aQNotificationListener = nTFEventListenerArray[i2].getAQListener();
                executor.execute(new Runnable(){

                    @Override
                    public void run() {
                        aQNotificationListener.onAQNotification(nTFAQEvent);
                    }
                });
                continue;
            }
            nTFEventListenerArray[i2].getAQListener().onAQNotification(nTFAQEvent);
        }
        if (nTFAQEvent.getEventType() == AQNotificationEvent.EventType.DEREG || this.isPurgeOnNTF) {
            PhysicalConnection.ntfManager.removeRegistration(this);
            PhysicalConnection.ntfManager.freeJdbcRegId(this.getJdbcRegId());
            PhysicalConnection.ntfManager.cleanListenersT4C(this.getClientTCPPort());
            this.state = NotificationRegistration.RegistrationState.CLOSED;
        }
    }

    void notify(final NTFJMSEvent nTFJMSEvent) {
        long l2 = 0L;
        NTFEventListener[] nTFEventListenerArray = this.listeners;
        int n2 = nTFEventListenerArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Executor executor = nTFEventListenerArray[i2].getExecutor();
            if (executor != null) {
                final JMSNotificationListener jMSNotificationListener = nTFEventListenerArray[i2].getJMSListener();
                executor.execute(new Runnable(){

                    @Override
                    public void run() {
                        jMSNotificationListener.onJMSNotification(nTFJMSEvent);
                    }
                });
                continue;
            }
            nTFEventListenerArray[i2].getJMSListener().onJMSNotification(nTFJMSEvent);
        }
    }

    @Blind(value=PropertiesBlinder.class)
    public Properties getRegistrationOptions() {
        return this.options;
    }

    int getJdbcRegId() {
        return this.jdbcRegId;
    }

    public String getUserName() {
        return this.username;
    }

    String getClientHost() {
        return this.clientHost;
    }

    int getClientTCPPort() {
        return this.clientTCPPort;
    }

    public String getDatabaseName() {
        return this.dbName;
    }

    public NotificationRegistration.RegistrationState getState() {
        return this.state;
    }

    protected void setState(NotificationRegistration.RegistrationState registrationState) {
        this.state = registrationState;
    }

    int getNamespace() {
        return this.namespace;
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    public Exception getRegistrationException() {
        if (this.connectionCreationExceptionArr != null && this.connectionCreationExceptionArr.length > 0) {
            return this.connectionCreationExceptionArr[0];
        }
        return null;
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }
}

