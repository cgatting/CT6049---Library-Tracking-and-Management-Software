/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import oracle.jdbc.driver.NTFJMSConnection;
import oracle.jdbc.driver.NTFJMSRegistration;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
public class NTFJMSConnectionGroup
implements Monitor {
    ArrayList<NTFJMSConnection> jmsListenerConnections = new ArrayList();
    private HashMap<Long, Integer> jmsRegIdToJDBCRegId = new HashMap();
    private int noOfRegistrationsInThisGroup = 0;
    String uniqueIdentifier;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();
    static final int DEFAULT_MAX_NTF_CONNECTIONS = 16;
    private static int MAX_NTF_CONNECTIONS = 0;
    private static final Monitor MAX_NTF_CONNECTIONS_MONITOR = Monitor.newInstance();
    private int aliveConnections = 0;
    private HashMap<Long, NTFJMSRegistration> jmsRegIdToNtfReg = new HashMap();

    public static void setMaxNtfConnection(int n2) {
        try (Monitor.CloseableLock closeableLock = MAX_NTF_CONNECTIONS_MONITOR.acquireCloseableLock();){
            if (n2 <= 0) {
                n2 = 16;
            }
            if (MAX_NTF_CONNECTIONS > 0) {
                return;
            }
            MAX_NTF_CONNECTIONS = n2;
        }
    }

    public static int getMaxNtfConnection() {
        if (MAX_NTF_CONNECTIONS == 0) {
            return 16;
        }
        return MAX_NTF_CONNECTIONS;
    }

    NTFJMSConnectionGroup(String string) {
        this.uniqueIdentifier = string;
    }

    void checkForActiveRegistrations() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.noOfRegistrationsInThisGroup == 0) {
                if (!this.jmsListenerConnections.isEmpty()) {
                    for (NTFJMSConnection nTFJMSConnection : this.jmsListenerConnections) {
                        if (nTFJMSConnection == null) continue;
                        nTFJMSConnection.setNeedToBeClosed(true);
                        nTFJMSConnection.interrupt();
                        nTFJMSConnection.closeThisListener();
                        --this.aliveConnections;
                    }
                    this.jmsListenerConnections.clear();
                }
                PhysicalConnection.ntfManager.removeJMSConnectionGroup(this.uniqueIdentifier);
            }
        }
    }

    void startJMSListenerConnection(String string, String string2, String string3, OpaqueString opaqueString, Properties properties, ArrayList<String> arrayList, String string4, int n2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            assert (string4 != null) : "jmsConnectionId is null";
            String string5 = "notify" + string4;
            NTFJMSConnection nTFJMSConnection = new NTFJMSConnection(string4, arrayList, string, string2, string3, opaqueString, properties, string5, n2);
            this.jmsListenerConnections.add(nTFJMSConnection);
            nTFJMSConnection.setDaemon(true);
            nTFJMSConnection.start();
            ++this.aliveConnections;
        }
    }

    void decrementNumberOfRegistrations() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            --this.noOfRegistrationsInThisGroup;
        }
    }

    NTFJMSConnection getNTFJMSConnection(int n2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.noOfRegistrationsInThisGroup += n2;
            if (this.noOfRegistrationsInThisGroup > NTFJMSConnectionGroup.getMaxNtfConnection()) {
                int n3 = this.noOfRegistrationsInThisGroup;
                NTFJMSConnection nTFJMSConnection = this.jmsListenerConnections.get(0);
                for (NTFJMSConnection nTFJMSConnection2 : this.jmsListenerConnections) {
                    int n4 = nTFJMSConnection2.getNumberOfRegistrations();
                    if (n4 >= n3) continue;
                    nTFJMSConnection = nTFJMSConnection2;
                    n3 = n4;
                }
                nTFJMSConnection.incrementNumberOfRegistrations(n2);
                NTFJMSConnection nTFJMSConnection3 = nTFJMSConnection;
                return nTFJMSConnection3;
            }
            NTFJMSConnection nTFJMSConnection = null;
            return nTFJMSConnection;
        }
    }

    void resetRegistrationNumbers(int n2, NTFJMSConnection nTFJMSConnection) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.noOfRegistrationsInThisGroup -= n2;
            if (nTFJMSConnection != null) {
                nTFJMSConnection.decrementNumberOfRegistrations(n2);
            }
        }
    }

    void raiseException() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            --this.aliveConnections;
            if (this.aliveConnections == 0) {
                NTFJMSRegistration nTFJMSRegistration = null;
                Iterator<Long> iterator = this.jmsRegIdToNtfReg.keySet().iterator();
                while (iterator.hasNext()) {
                    nTFJMSRegistration = this.jmsRegIdToNtfReg.get(iterator.next());
                    try {
                        nTFJMSRegistration.raiseException(1);
                    }
                    catch (Exception exception) {}
                }
            }
        }
    }

    void addNtfRegistrationByRegId(long l2, NTFJMSRegistration nTFJMSRegistration) {
        this.jmsRegIdToNtfReg.put(l2, nTFJMSRegistration);
    }

    NTFJMSRegistration getNtfRegistrationByRegId(long l2) {
        return this.jmsRegIdToNtfReg.get(l2);
    }

    NTFJMSRegistration removeNtfRegistrationByRegId(long l2) {
        return this.jmsRegIdToNtfReg.remove(l2);
    }

    void stopNTFJMSConnection(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (string == null || string.length() == 0) {
                return;
            }
            for (NTFJMSConnection nTFJMSConnection : this.jmsListenerConnections) {
                if (nTFJMSConnection == null || !string.equals(nTFJMSConnection.getJMSConnectionId())) continue;
                nTFJMSConnection.decrementNumberOfRegistrations(1);
                if (nTFJMSConnection.getNumberOfRegistrations() <= 0) {
                    nTFJMSConnection.setNeedToBeClosed(true);
                }
                break;
            }
        }
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }
}

