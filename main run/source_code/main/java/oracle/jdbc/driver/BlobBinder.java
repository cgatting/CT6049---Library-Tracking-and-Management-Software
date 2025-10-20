/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.nio.ByteBuffer;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.BlobCopyingBinder;
import oracle.jdbc.driver.DatumBinder;
import oracle.jdbc.driver.OraclePreparedStatement;

class BlobBinder
extends DatumBinder {
    Binder theBlobCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)113;
        binder.bytelen = 4000;
    }

    BlobBinder(byte[] byArray) {
        this(byArray, null);
    }

    BlobBinder(byte[] byArray, ByteBuffer byteBuffer) {
        super(byArray, byteBuffer);
        BlobBinder.init(this);
        this.skipBindChecksumForLobs = true;
    }

    @Override
    Binder copyingBinder() {
        if (this.theBlobCopyingBinder == null) {
            this.theBlobCopyingBinder = new BlobCopyingBinder();
        }
        return this.theBlobCopyingBinder;
    }

    @Override
    void lastBoundValueCleanup(OraclePreparedStatement oraclePreparedStatement, int n2) {
        if (oraclePreparedStatement.lastBoundBlobs != null) {
            oraclePreparedStatement.moveTempLobsToFree(oraclePreparedStatement.lastBoundBlobs[n2]);
        }
    }
}

