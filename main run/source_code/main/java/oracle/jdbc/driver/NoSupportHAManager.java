/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.HAManager;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.HIGH_AVAILABILITY})
public class NoSupportHAManager
extends HAManager {
    private static final NoSupportHAManager noSupportHAManager = new NoSupportHAManager();

    NoSupportHAManager() {
    }

    public static NoSupportHAManager getInstance() {
        return noSupportHAManager;
    }

    @Override
    void addConnection(OracleConnection oracleConnection) throws SQLException {
        oracleConnection.setHAManager(this);
    }

    @Override
    public void dropConnection(OracleConnection oracleConnection) throws SQLException {
    }

    void plannedDown(String string, String string2, String string3, String string4) {
    }

    @Override
    void unplannedDown(String string, String string2, String string3, String string4) {
    }

    @Override
    void nodeDown(String string) {
    }

    @Override
    void serviceUp(String string, String string2, String string3, String string4, int n2) {
    }

    @Override
    public boolean isServiceMemberDown(OracleConnection oracleConnection) throws SQLException {
        return false;
    }

    @Override
    public boolean checkAndDrain(OracleConnection oracleConnection) throws SQLException {
        return false;
    }

    public boolean checkAndDrain(OracleConnection oracleConnection, String string) throws SQLException {
        return false;
    }

    @Override
    public void close() {
    }
}

