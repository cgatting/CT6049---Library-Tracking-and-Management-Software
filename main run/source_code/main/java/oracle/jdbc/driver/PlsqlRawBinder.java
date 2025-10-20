/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DatumBinder;
import oracle.jdbc.driver.PlsqlRawCopyingBinder;

class PlsqlRawBinder
extends DatumBinder {
    Binder thePlsqlRawCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)23;
    }

    PlsqlRawBinder(byte[] byArray) {
        super(byArray);
        PlsqlRawBinder.init(this);
    }

    @Override
    Binder copyingBinder() {
        if (this.thePlsqlRawCopyingBinder == null) {
            this.thePlsqlRawCopyingBinder = new PlsqlRawCopyingBinder();
        }
        return this.thePlsqlRawCopyingBinder;
    }
}

