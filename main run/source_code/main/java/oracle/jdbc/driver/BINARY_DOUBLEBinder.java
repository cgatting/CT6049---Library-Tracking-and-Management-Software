/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.BINARY_DOUBLECopyingBinder;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DatumBinder;

class BINARY_DOUBLEBinder
extends DatumBinder {
    Binder theBINARY_DOUBLECopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)101;
        binder.bytelen = 8;
    }

    BINARY_DOUBLEBinder(byte[] byArray) {
        super(byArray);
        BINARY_DOUBLEBinder.init(this);
    }

    @Override
    Binder copyingBinder() {
        if (this.theBINARY_DOUBLECopyingBinder == null) {
            this.theBINARY_DOUBLECopyingBinder = new BINARY_DOUBLECopyingBinder();
        }
        return this.theBINARY_DOUBLECopyingBinder;
    }
}

