/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.sql.Datum;

abstract class Binder {
    short type;
    int bytelen;
    short scale;

    Binder() {
    }

    abstract Binder copyingBinder();

    abstract long bind(OraclePreparedStatement var1, int var2, int var3, int var4, byte[] var5, char[] var6, short[] var7, int var8, int var9, int var10, int var11, int var12, int var13, boolean var14, long var15, ByteArray var17, long[] var18, int[] var19, int var20, boolean var21, int var22) throws SQLException;

    @DisableTrace
    public String toString() {
        return this.getClass() + " [type = " + this.type + ", bytelen = " + this.bytelen + "]";
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    short updateInoutIndicatorValue(short s2) {
        return s2;
    }

    void lastBoundValueCleanup(OraclePreparedStatement oraclePreparedStatement, int n2) {
    }

    Datum getDatum(OraclePreparedStatement oraclePreparedStatement, int n2, int n3, int n4) throws SQLException {
        return null;
    }
}

