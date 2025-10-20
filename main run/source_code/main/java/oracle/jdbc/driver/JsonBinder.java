/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DatumBinder;
import oracle.jdbc.driver.JsonCopyingBinder;

class JsonBinder
extends DatumBinder {
    Binder theJsonCopyingBinder = null;

    static void init(Binder binder) {
        binder.type = (short)119;
        binder.bytelen = 4000;
    }

    JsonBinder(byte[] byArray) {
        super(byArray);
        JsonBinder.init(this);
        this.skipBindChecksumForLobs = true;
    }

    @Override
    Binder copyingBinder() {
        if (this.theJsonCopyingBinder == null) {
            this.theJsonCopyingBinder = new JsonCopyingBinder();
        }
        return this.theJsonCopyingBinder;
    }
}

