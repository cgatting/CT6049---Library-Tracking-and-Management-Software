/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DatumBinder;
import oracle.jdbc.driver.IntervalDSCopyingBinder;

class IntervalDSBinder
extends DatumBinder {
    Binder theIntervalDSCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)183;
        binder.bytelen = 11;
    }

    IntervalDSBinder(byte[] byArray) {
        super(byArray);
        IntervalDSBinder.init(this);
    }

    @Override
    Binder copyingBinder() {
        if (this.theIntervalDSCopyingBinder == null) {
            this.theIntervalDSCopyingBinder = new IntervalDSCopyingBinder();
        }
        return this.theIntervalDSCopyingBinder;
    }
}

