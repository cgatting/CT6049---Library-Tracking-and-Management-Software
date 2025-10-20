/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.NTFDCNConnectionGroup;
import oracle.jdbc.driver.NTFDCNRegistration;
import oracle.jdbc.driver.NTFJMSConnectionGroup;
import oracle.jdbc.driver.NTFListener;
import oracle.jdbc.driver.NTFRegistration;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CHANGE_NOTIFICATION})
class NTFManager
implements Monitor {
    private Hashtable<Integer, NTFListener> nsListeners = new Hashtable();
    private Hashtable<Integer, NTFRegistration> ntfRegistrations = new Hashtable();
    private ConcurrentHashMap<Long, NTFDCNRegistration> dcnRegistrations = new ConcurrentHashMap();
    private byte[] listOfJdbcRegId = new byte[20];
    private HashMap<Long, Integer> jmsRegIdToJDBCRegId = new HashMap();
    private HashMap<String, NTFJMSConnectionGroup> jmsConnectionGroups = new HashMap();
    private HashMap<String, NTFDCNConnectionGroup> dcnConnectionGroups = new HashMap();
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();

    NTFManager() {
    }

    boolean listenOnPortT4C(int[] nArray, boolean bl, @Blind(value=PropertiesBlinder.class) Properties properties, Exception[] exceptionArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n2 = nArray[0];
            boolean bl2 = false;
            while (true) {
                Object object;
                NTFListener nTFListener;
                if ((nTFListener = this.nsListeners.get(n2)) != null) {
                    if (exceptionArray == null || exceptionArray.length <= 0) break;
                    exceptionArray[0] = object = nTFListener.getRegistrationException();
                    break;
                }
                try {
                    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                    serverSocketChannel.configureBlocking(false);
                    object = serverSocketChannel.socket();
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(n2);
                    try {
                        ((ServerSocket)object).bind(inetSocketAddress);
                        bl2 = true;
                        nTFListener = new NTFListener(this, serverSocketChannel, n2, properties, exceptionArray);
                        this.nsListeners.put(n2, nTFListener);
                        nTFListener.start();
                        break;
                    }
                    catch (BindException bindException) {
                        if (!bl) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 250).fillInStackTrace();
                        }
                        ((ServerSocket)object).close();
                        ++n2;
                    }
                    catch (IOException iOException) {
                        if (!bl) {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 250).fillInStackTrace();
                        }
                        ((ServerSocket)object).close();
                        ++n2;
                    }
                }
                catch (IOException iOException) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
                }
            }
            nArray[0] = n2;
            boolean bl3 = bl2;
            return bl3;
        }
    }

    int getNextJdbcRegId() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n2;
            for (n2 = 1; n2 < this.listOfJdbcRegId.length && this.listOfJdbcRegId[n2] != 0; ++n2) {
            }
            if (n2 == this.listOfJdbcRegId.length - 1) {
                byte[] byArray = new byte[this.listOfJdbcRegId.length * 2];
                System.arraycopy(this.listOfJdbcRegId, 0, byArray, 0, this.listOfJdbcRegId.length);
                this.listOfJdbcRegId = byArray;
            }
            this.listOfJdbcRegId[n2] = 2;
            int n3 = n2;
            return n3;
        }
    }

    void addRegistration(NTFRegistration nTFRegistration) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Integer n2 = nTFRegistration.getJdbcRegId();
            Hashtable hashtable = (Hashtable)this.ntfRegistrations.clone();
            hashtable.put(n2, nTFRegistration);
            this.ntfRegistrations = hashtable;
        }
    }

    boolean removeRegistration(NTFRegistration nTFRegistration) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Integer n2 = nTFRegistration.getJdbcRegId();
            Hashtable hashtable = (Hashtable)this.ntfRegistrations.clone();
            Object v2 = hashtable.remove(n2);
            this.ntfRegistrations = hashtable;
            boolean bl = false;
            if (v2 != null) {
                bl = true;
            }
            boolean bl2 = bl;
            return bl2;
        }
    }

    void freeJdbcRegId(int n2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.listOfJdbcRegId != null && this.listOfJdbcRegId.length > n2) {
                this.listOfJdbcRegId[n2] = 0;
            }
        }
    }

    void cleanListenersT4C(int n2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Object object;
            Enumeration<Integer> enumeration = this.ntfRegistrations.keys();
            boolean bl = false;
            while (!bl && enumeration.hasMoreElements()) {
                object = enumeration.nextElement();
                NTFRegistration nTFRegistration = this.ntfRegistrations.get(object);
                if (nTFRegistration.getClientTCPPort() != n2) continue;
                bl = true;
            }
            if (!bl && (object = this.nsListeners.get(n2)) != null) {
                ((NTFListener)object).closeThisListener();
                ((Thread)object).interrupt();
                this.nsListeners.remove(n2);
            }
        }
    }

    NTFRegistration getRegistration(int n2) {
        Integer n3 = n2;
        Hashtable<Integer, NTFRegistration> hashtable = this.ntfRegistrations;
        NTFRegistration nTFRegistration = hashtable.get(n3);
        return nTFRegistration;
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    void removeJMSRegistrationId(Long l2) {
        this.jmsRegIdToJDBCRegId.remove(l2);
    }

    int getJDBCRegId(Long l2) {
        return this.jmsRegIdToJDBCRegId.get(l2);
    }

    void mapJMSRegIdToJDBCRegId(Long l2, int n2) {
        this.jmsRegIdToJDBCRegId.put(l2, n2);
    }

    NTFJMSConnectionGroup getJMSConnectionGroup(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            NTFJMSConnectionGroup nTFJMSConnectionGroup = this.jmsConnectionGroups.get(string);
            if (nTFJMSConnectionGroup == null) {
                nTFJMSConnectionGroup = new NTFJMSConnectionGroup(string);
            }
            this.jmsConnectionGroups.put(string, nTFJMSConnectionGroup);
            NTFJMSConnectionGroup nTFJMSConnectionGroup2 = nTFJMSConnectionGroup;
            return nTFJMSConnectionGroup2;
        }
    }

    void removeJMSConnectionGroup(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.jmsConnectionGroups.remove(string);
        }
    }

    NTFDCNConnectionGroup getDCNConnectionGroup(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            NTFDCNConnectionGroup nTFDCNConnectionGroup = this.dcnConnectionGroups.get(string);
            if (nTFDCNConnectionGroup == null) {
                nTFDCNConnectionGroup = new NTFDCNConnectionGroup(string);
            }
            this.dcnConnectionGroups.put(string, nTFDCNConnectionGroup);
            NTFDCNConnectionGroup nTFDCNConnectionGroup2 = nTFDCNConnectionGroup;
            return nTFDCNConnectionGroup2;
        }
    }

    void removeDCNConnectionGroup(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.dcnConnectionGroups.remove(string);
        }
    }

    void addDCNRegistration(NTFDCNRegistration nTFDCNRegistration) {
        this.dcnRegistrations.put(nTFDCNRegistration.getRegId(), nTFDCNRegistration);
    }

    NTFDCNRegistration getDCNRegistration(long l2) {
        return this.dcnRegistrations.get(l2);
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }
}

