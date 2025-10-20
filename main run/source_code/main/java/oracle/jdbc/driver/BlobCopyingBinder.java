/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.BlobBinder;
import oracle.jdbc.driver.ByteCopyingBinder;
import oracle.jdbc.driver.OraclePreparedStatement;

class BlobCopyingBinder
extends ByteCopyingBinder {
    BlobCopyingBinder() {
        BlobBinder.init(this);
    }

    @Override
    void lastBoundValueCleanup(OraclePreparedStatement oraclePreparedStatement, int n2) {
        if (oraclePreparedStatement.lastBoundBlobs != null) {
            oraclePreparedStatement.moveTempLobsToFree(oraclePreparedStatement.lastBoundBlobs[n2]);
        }
    }
}

