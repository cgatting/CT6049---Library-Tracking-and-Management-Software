/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DatumBinder;
import oracle.jdbc.driver.TimestampCopyingBinder;

class OracleTimestampBinder
extends DatumBinder {
    Binder theTimestampCopyingBinder = null;

    static void init(Binder binder, int n2) {
        binder.type = (short)180;
        binder.bytelen = 11;
        binder.scale = (short)n2;
    }

    OracleTimestampBinder(byte[] byArray) {
        super(byArray);
        OracleTimestampBinder.init(this, -1);
    }

    OracleTimestampBinder(byte[] byArray, int n2) {
        super(byArray);
        OracleTimestampBinder.init(this, n2);
    }

    @Override
    Binder copyingBinder() {
        if (this.theTimestampCopyingBinder == null) {
            this.theTimestampCopyingBinder = new TimestampCopyingBinder();
        }
        return this.theTimestampCopyingBinder;
    }
}

