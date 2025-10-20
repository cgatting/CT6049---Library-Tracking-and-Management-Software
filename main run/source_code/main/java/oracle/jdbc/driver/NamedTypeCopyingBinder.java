/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.NamedTypeBinder;
import oracle.jdbc.driver.TypeCopyingBinder;
import oracle.jdbc.oracore.OracleTypeADT;

class NamedTypeCopyingBinder
extends TypeCopyingBinder {
    NamedTypeCopyingBinder(byte[] byArray, OracleTypeADT oracleTypeADT) {
        super(byArray, oracleTypeADT);
        NamedTypeBinder.init(this);
    }
}

