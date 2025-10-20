/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.ArrayList;
import java.util.Properties;
import oracle.jdbc.driver.NTFDCNConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OpaqueString;

class NTFDCNConnectionGroup
implements Monitor {
    private static final int DEFAULT_MAX_NTF_CONNECTIONS = 16;
    private static int MAX_NTF_CONNECTIONS = 16;
    private ArrayList<NTFDCNConnection> dcnListenerConnections = new ArrayList();
    private final String groupId;
    private int noOfRegistrationsInThisGroup = 0;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();

    NTFDCNConnectionGroup(String string) {
        this.groupId = string;
    }

    NTFDCNConnection getNTFDCNConnection() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            ++this.noOfRegistrationsInThisGroup;
            if (this.noOfRegistrationsInThisGroup > MAX_NTF_CONNECTIONS) {
                int n2 = this.noOfRegistrationsInThisGroup;
                NTFDCNConnection nTFDCNConnection = this.dcnListenerConnections.get(0);
                for (NTFDCNConnection nTFDCNConnection2 : this.dcnListenerConnections) {
                    int n3 = nTFDCNConnection2.getNumberOfRegistrations();
                    if (n3 >= n2) continue;
                    nTFDCNConnection = nTFDCNConnection2;
                    n2 = n3;
                }
                nTFDCNConnection.incrementNumberOfRegistrations(1);
                NTFDCNConnection nTFDCNConnection3 = nTFDCNConnection;
                return nTFDCNConnection3;
            }
            NTFDCNConnection nTFDCNConnection = null;
            return nTFDCNConnection;
        }
    }

    void startDCNListenerConnection(String string, String string2, String string3, OpaqueString opaqueString, Properties properties, ArrayList<String> arrayList, String string4, int n2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            assert (string4 != null) : "dcnConnectionId is null";
            String string5 = "notify" + string4;
            NTFDCNConnection nTFDCNConnection = new NTFDCNConnection(string4, arrayList, string, string2, string3, opaqueString, properties, string5, n2);
            this.dcnListenerConnections.add(nTFDCNConnection);
            nTFDCNConnection.setDaemon(true);
            nTFDCNConnection.start();
        }
    }

    void stopNTFDCNConnection(String string) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (string == null || string.length() == 0) {
                return;
            }
            for (NTFDCNConnection nTFDCNConnection : this.dcnListenerConnections) {
                if (nTFDCNConnection == null || !string.equals(nTFDCNConnection.getClientId())) continue;
                nTFDCNConnection.decrementNumberOfRegistrations(1);
                if (nTFDCNConnection.getNumberOfRegistrations() <= 0) {
                    nTFDCNConnection.setNeedToBeClosed(true);
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

