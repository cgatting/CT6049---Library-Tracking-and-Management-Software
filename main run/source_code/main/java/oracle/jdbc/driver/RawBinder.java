/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DatumBinder;
import oracle.jdbc.driver.RawCopyingBinder;

class RawBinder
extends DatumBinder {
    Binder theRawCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)23;
    }

    RawBinder(byte[] byArray) {
        super(byArray);
        RawBinder.init(this);
    }

    @Override
    Binder copyingBinder() {
        if (this.theRawCopyingBinder == null) {
            this.theRawCopyingBinder = new RawCopyingBinder();
        }
        return this.theRawCopyingBinder;
    }
}

