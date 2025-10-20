/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DatumBinder;
import oracle.jdbc.driver.IntervalYMCopyingBinder;

class IntervalYMBinder
extends DatumBinder {
    Binder theIntervalYMCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)182;
        binder.bytelen = 5;
    }

    IntervalYMBinder(byte[] byArray) {
        super(byArray);
        IntervalYMBinder.init(this);
    }

    @Override
    Binder copyingBinder() {
        if (this.theIntervalYMCopyingBinder == null) {
            this.theIntervalYMCopyingBinder = new IntervalYMCopyingBinder();
        }
        return this.theIntervalYMCopyingBinder;
    }
}

