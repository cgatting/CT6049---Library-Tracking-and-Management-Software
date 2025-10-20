/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.nio.ByteBuffer;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.ClobCopyingBinder;
import oracle.jdbc.driver.DatumBinder;
import oracle.jdbc.driver.OraclePreparedStatement;

class ClobBinder
extends DatumBinder {
    Binder theClobCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)112;
        binder.bytelen = 4000;
    }

    ClobBinder(byte[] byArray) {
        this(byArray, null);
    }

    ClobBinder(byte[] byArray, ByteBuffer byteBuffer) {
        super(byArray, byteBuffer);
        ClobBinder.init(this);
        this.skipBindChecksumForLobs = true;
    }

    @Override
    Binder copyingBinder() {
        if (this.theClobCopyingBinder == null) {
            this.theClobCopyingBinder = new ClobCopyingBinder();
        }
        return this.theClobCopyingBinder;
    }

    @Override
    void lastBoundValueCleanup(OraclePreparedStatement oraclePreparedStatement, int n2) {
        if (oraclePreparedStatement.lastBoundClobs != null) {
            oraclePreparedStatement.moveTempLobsToFree(oraclePreparedStatement.lastBoundClobs[n2]);
        }
    }
}

