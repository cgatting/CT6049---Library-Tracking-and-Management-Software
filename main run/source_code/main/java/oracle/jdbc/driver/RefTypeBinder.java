/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.RefTypeCopyingBinder;
import oracle.jdbc.driver.TypeBinder;
import oracle.jdbc.oracore.OracleTypeADT;

class RefTypeBinder
extends TypeBinder {
    Binder theRefTypeCopyingBinder = null;

    RefTypeBinder(byte[] byArray, OracleTypeADT oracleTypeADT) {
        super(byArray, oracleTypeADT);
        RefTypeBinder.init(this);
    }

    static void init(Binder binder) {
        binder.type = (short)111;
        binder.bytelen = 24;
    }

    @Override
    Binder copyingBinder() {
        if (this.theRefTypeCopyingBinder == null) {
            this.theRefTypeCopyingBinder = new RefTypeCopyingBinder(this.paramVal, this.paramOtype);
        }
        return this.theRefTypeCopyingBinder;
    }
}

