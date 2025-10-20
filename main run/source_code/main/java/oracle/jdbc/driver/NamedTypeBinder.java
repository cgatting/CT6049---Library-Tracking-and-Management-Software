/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.NamedTypeCopyingBinder;
import oracle.jdbc.driver.TypeBinder;
import oracle.jdbc.oracore.OracleTypeADT;

class NamedTypeBinder
extends TypeBinder {
    Binder theNamedTypeCopyingBinder = null;

    NamedTypeBinder(byte[] byArray, OracleTypeADT oracleTypeADT) {
        super(byArray, oracleTypeADT);
        NamedTypeBinder.init(this);
    }

    static void init(Binder binder) {
        binder.type = (short)109;
        binder.bytelen = 24;
    }

    @Override
    Binder copyingBinder() {
        if (this.theNamedTypeCopyingBinder == null) {
            this.theNamedTypeCopyingBinder = new NamedTypeCopyingBinder(this.paramVal, this.paramOtype);
        }
        return this.theNamedTypeCopyingBinder;
    }
}

