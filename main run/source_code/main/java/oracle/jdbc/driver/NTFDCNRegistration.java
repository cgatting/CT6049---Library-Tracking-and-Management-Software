/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Executor;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.driver.NTFEventListener;
import oracle.jdbc.driver.NTFRegistration;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFDCNRegistration
extends NTFRegistration
implements DatabaseChangeRegistration {
    private final long regid;
    private final byte[][] contextArr;
    private final String clientId;
    private final boolean isClientInitiated;
    private String[] tables = new String[10];
    private int nbOfStringsInTable = 0;

    NTFDCNRegistration(int n2, boolean bl, String string, long l2, String string2, String string3, int n3, @Blind(value=PropertiesBlinder.class) Properties properties, short s2, Exception[] exceptionArray, byte[][] byArray, String string4, boolean bl2) {
        super(n2, 2, bl, string, string3, n3, properties, string2, s2, exceptionArray);
        this.regid = l2;
        this.contextArr = byArray;
        this.clientId = string4;
        this.isClientInitiated = bl2;
    }

    NTFDCNRegistration(String string, long l2, String string2, short s2) {
        super(0, 2, false, string, null, 0, null, string2, s2, null);
        this.regid = l2;
        this.contextArr = null;
        this.clientId = null;
        this.isClientInitiated = false;
    }

    public boolean isClientInitiated() {
        return this.isClientInitiated;
    }

    public byte[][] getContext() {
        return this.contextArr;
    }

    public String getClientId() {
        return this.clientId;
    }

    @Override
    public int getRegistrationId() {
        return (int)this.regid;
    }

    @Override
    public long getRegId() {
        return this.regid;
    }

    @Override
    public void addListener(DatabaseChangeListener databaseChangeListener, Executor executor) throws SQLException {
        NTFEventListener nTFEventListener = new NTFEventListener(databaseChangeListener);
        nTFEventListener.setExecutor(executor);
        this.addListener(nTFEventListener);
    }

    @Override
    public void addListener(DatabaseChangeListener databaseChangeListener) throws SQLException {
        NTFEventListener nTFEventListener = new NTFEventListener(databaseChangeListener);
        this.addListener(nTFEventListener);
    }

    @Override
    public void removeListener(DatabaseChangeListener databaseChangeListener) throws SQLException {
        super.removeListener(databaseChangeListener);
    }

    void addTablesName(String[] stringArray, int n2) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.nbOfStringsInTable + n2 > this.tables.length) {
                String[] stringArray2 = new String[(this.nbOfStringsInTable + n2) * 2];
                System.arraycopy(this.tables, 0, stringArray2, 0, this.tables.length);
                this.tables = stringArray2;
            }
            System.arraycopy(stringArray, 0, this.tables, this.nbOfStringsInTable, n2);
            this.nbOfStringsInTable += n2;
        }
    }

    @Override
    public String[] getTables() {
        String[] stringArray = new String[this.nbOfStringsInTable];
        System.arraycopy(this.tables, 0, stringArray, 0, this.nbOfStringsInTable);
        return stringArray;
    }
}

