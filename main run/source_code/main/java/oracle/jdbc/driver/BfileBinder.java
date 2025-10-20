/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.BfileCopyingBinder;
import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DatumBinder;

class BfileBinder
extends DatumBinder {
    Binder theBfileCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)114;
        binder.bytelen = 530;
    }

    BfileBinder(byte[] byArray) {
        super(byArray);
        BfileBinder.init(this);
        this.skipBindChecksumForLobs = true;
    }

    @Override
    Binder copyingBinder() {
        if (this.theBfileCopyingBinder == null) {
            this.theBfileCopyingBinder = new BfileCopyingBinder();
        }
        return this.theBfileCopyingBinder;
    }
}

