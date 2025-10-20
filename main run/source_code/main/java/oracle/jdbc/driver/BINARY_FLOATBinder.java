/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.BINARY_FLOATCopyingBinder;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DatumBinder;

class BINARY_FLOATBinder
extends DatumBinder {
    Binder theBINARY_FLOATCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)100;
        binder.bytelen = 4;
    }

    BINARY_FLOATBinder(byte[] byArray) {
        super(byArray);
        BINARY_FLOATBinder.init(this);
    }

    @Override
    Binder copyingBinder() {
        if (this.theBINARY_FLOATCopyingBinder == null) {
            this.theBINARY_FLOATCopyingBinder = new BINARY_FLOATCopyingBinder();
        }
        return this.theBINARY_FLOATCopyingBinder;
    }
}

