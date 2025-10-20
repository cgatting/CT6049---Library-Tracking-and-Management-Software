/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.HIGH_AVAILABILITY})
class ServiceMember {
    private String name;
    private String svc;
    private String db;
    private String hst;
    private boolean restarted;
    int connCount;
    private MemberStatus status;
    private ConcurrentHashMap<OracleConnection, OracleConnection> connections;

    ServiceMember(String string, String string2, String string3, String string4) {
        this.name = string2;
        this.svc = string;
        this.db = string3;
        this.hst = string4;
        this.restarted = false;
        this.connCount = 0;
        this.status = MemberStatus.UNKNOWN;
        this.connections = new ConcurrentHashMap();
    }

    void up() {
        this.status = MemberStatus.UP;
    }

    void down() {
        this.status = MemberStatus.DOWN;
    }

    boolean isDown() {
        return this.status == MemberStatus.DOWN;
    }

    boolean isUp() {
        return this.status == MemberStatus.UP;
    }

    void addConnection(OracleConnection oracleConnection) {
        this.connections.put(oracleConnection, oracleConnection);
        ++this.connCount;
    }

    void dropConnection(OracleConnection oracleConnection) {
        this.connections.remove(oracleConnection);
        if (this.connCount > 0) {
            --this.connCount;
        }
    }

    void cleanupConnections() throws SQLException {
        ConcurrentHashMap<OracleConnection, OracleConnection> concurrentHashMap = this.connections;
        this.connections = new ConcurrentHashMap();
        for (OracleConnection oracleConnection : concurrentHashMap.keySet()) {
            oracleConnection.abort();
            oracleConnection.close();
        }
        this.connCount = 0;
        concurrentHashMap.clear();
    }

    public String getName() {
        return this.name;
    }

    String getDatabase() {
        return this.db;
    }

    @DisableTrace
    public String toString() {
        return "Service name: " + this.svc + ", Instance name: " + this.name + ", Database name: " + this.db + ", Host name: " + this.hst;
    }

    private static enum MemberStatus {
        UNKNOWN,
        UP,
        DOWN;

    }
}

