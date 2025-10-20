/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import oracle.jdbc.driver.NTFJMSConnectionGroup;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.T4CConnection;
import oracle.jdbc.driver.T4CTTIoaqnfy;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.pool.OracleDataSource;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFJMSConnection
extends Thread
implements Monitor {
    private String jmsConnectionId;
    private String instanceName;
    private String serviceName;
    private String userName;
    private OpaqueString password;
    private Properties extrAuthProp;
    private String connClass;
    private ArrayList<String> listenerAddresses;
    private static final int MAX_NUMBER_OF_TRIES = 5;
    private Connection conn = null;
    private T4CTTIoaqnfy oaqnfy = null;
    private volatile boolean needToBeClosed = false;
    private boolean safeToClose = true;
    private int numberOfRegistrations = 0;
    private NTFJMSConnectionGroup connectionGroup = null;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();

    NTFJMSConnection(String string, ArrayList<String> arrayList, String string2, String string3, String string4, OpaqueString opaqueString, Properties properties, String string5, int n2) {
        this.userName = string4;
        this.password = opaqueString;
        this.extrAuthProp = properties;
        this.serviceName = string3;
        this.jmsConnectionId = string;
        this.listenerAddresses = arrayList;
        this.instanceName = string2;
        this.connClass = string5;
        this.numberOfRegistrations = n2;
        assert (arrayList != null) : "listenerAddresses is null";
        assert (string2 != null) : "instancename is null";
    }

    @Override
    public void run() {
        boolean bl = false;
        boolean bl2 = false;
        int n2 = 0;
        int n3 = 0;
        for (int i2 = 0; i2 < 2 && !this.needToBeClosed; ++i2) {
            try {
                if (i2 == 0 || bl2) {
                    this.conn = this.getConnection(n2);
                    this.oaqnfy = new T4CTTIoaqnfy((T4CConnection)this.conn, this.jmsConnectionId);
                }
                if (bl || !this.needToBeClosed) {
                    bl = false;
                    bl2 = false;
                    i2 = 0;
                }
                this.oaqnfy.doRPC();
                continue;
            }
            catch (IOException iOException) {
                this.needToBeClosed = true;
                continue;
            }
            catch (Exception exception) {
                if (this.needToBeClosed) break;
                if (bl) {
                    this.getConnectionGroup().raiseException();
                    break;
                }
                if (exception instanceof SQLException) {
                    n3 = ((SQLException)exception).getErrorCode();
                }
                if (n3 == 17410) {
                    bl2 = true;
                    n3 = 0;
                    try {
                        Thread.sleep(5000L);
                    }
                    catch (Exception exception2) {}
                } else {
                    bl2 = false;
                }
                bl = true;
            }
        }
    }

    Connection getConnection(int n2) throws SQLException, InterruptedException {
        Connection connection = null;
        OracleDataSource oracleDataSource = new OracleDataSource();
        Properties properties = new Properties();
        if (this.extrAuthProp != null) {
            properties.putAll(this.extrAuthProp);
        }
        if (this.userName != null) {
            oracleDataSource.setUser(this.userName);
            oracleDataSource.setPassword(this.password.get());
        }
        if (this.connClass != null) {
            properties.put("oracle.jdbc.DRCPConnectionClass", this.connClass);
            properties.put("oracle.jdbc.jmsNotification", "true");
            properties.put("oracle.jdbc.ReadTimeout", 0);
            properties.put("oracle.net.CONNECT_TIMEOUT", 0);
        }
        oracleDataSource.setConnectionProperties(properties);
        while (connection == null && n2++ < 5 && !this.needToBeClosed) {
            connection = this.tryListenerAddressesToGetConnection(oracleDataSource);
            if (connection != null || n2 >= 5) continue;
            Thread.sleep(n2 * 5000);
        }
        if (connection == null && !this.needToBeClosed) {
            throw new SQLException("Failed to create notification connection to emon server");
        }
        return connection;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    Connection tryListenerAddressesToGetConnection(OracleDataSource oracleDataSource) {
        Iterator<String> iterator = this.listenerAddresses.iterator();
        while (iterator.hasNext()) {
            String string = iterator.next();
            String string2 = "jdbc:oracle:thin:@(DESCRIPTION=" + string + "(CONNECT_DATA=(SERVICE_NAME=" + this.serviceName + ")(SERVER=EMON)(INSTANCE_NAME=" + this.instanceName + ")))";
            oracleDataSource.setURL(string2);
            try {
                Monitor.CloseableLock closeableLock = this.acquireCloseableLock();
                Throwable throwable = null;
                try {
                    Connection connection;
                    if (this.needToBeClosed) {
                        connection = null;
                        return connection;
                    }
                    connection = oracleDataSource.getConnection();
                    if (connection == null) continue;
                    Connection connection2 = connection;
                    return connection2;
                }
                catch (Throwable throwable2) {
                    throwable = throwable2;
                    throw throwable2;
                }
                finally {
                    if (closeableLock == null) continue;
                    if (throwable != null) {
                        try {
                            closeableLock.close();
                        }
                        catch (Throwable throwable3) {
                            throwable.addSuppressed(throwable3);
                        }
                        continue;
                    }
                    closeableLock.close();
                }
            }
            catch (SQLException sQLException) {}
        }
        return null;
    }

    void closeThisListener() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.needToBeClosed = true;
            try {
                if (this.oaqnfy != null) {
                    this.oaqnfy.stopListening();
                }
                if (this.conn != null) {
                    this.conn.close();
                }
            }
            catch (SQLException sQLException) {
            }
        }
    }

    void setNeedToBeClosed(boolean bl) {
        this.needToBeClosed = bl;
    }

    String getJMSConnectionId() {
        return this.jmsConnectionId;
    }

    int getNumberOfRegistrations() {
        return this.numberOfRegistrations;
    }

    void incrementNumberOfRegistrations(int n2) {
        this.numberOfRegistrations += n2;
    }

    void decrementNumberOfRegistrations(int n2) {
        this.numberOfRegistrations -= n2;
    }

    void setConnectionGroup(NTFJMSConnectionGroup nTFJMSConnectionGroup) {
        this.connectionGroup = nTFJMSConnectionGroup;
    }

    NTFJMSConnectionGroup getConnectionGroup() {
        if (this.connectionGroup == null) {
            this.connectionGroup = PhysicalConnection.ntfManager.getJMSConnectionGroup(this.userName + this.instanceName);
        }
        return this.connectionGroup;
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }
}

