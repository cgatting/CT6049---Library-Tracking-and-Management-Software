/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.ByteCopyingBinder;
import oracle.jdbc.driver.ClobBinder;
import oracle.jdbc.driver.OraclePreparedStatement;

class ClobCopyingBinder
extends ByteCopyingBinder {
    ClobCopyingBinder() {
        ClobBinder.init(this);
    }

    @Override
    void lastBoundValueCleanup(OraclePreparedStatement oraclePreparedStatement, int n2) {
        if (oraclePreparedStatement.lastBoundClobs != null) {
            oraclePreparedStatement.moveTempLobsToFree(oraclePreparedStatement.lastBoundClobs[n2]);
        }
    }
}

