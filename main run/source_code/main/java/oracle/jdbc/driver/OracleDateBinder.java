/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DateCopyingBinder;
import oracle.jdbc.driver.DatumBinder;

class OracleDateBinder
extends DatumBinder {
    Binder theDateCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)12;
        binder.bytelen = 7;
    }

    OracleDateBinder(byte[] byArray) {
        super(byArray);
        OracleDateBinder.init(this);
    }

    @Override
    Binder copyingBinder() {
        if (this.theDateCopyingBinder == null) {
            this.theDateCopyingBinder = new DateCopyingBinder();
        }
        return this.theDateCopyingBinder;
    }
}

