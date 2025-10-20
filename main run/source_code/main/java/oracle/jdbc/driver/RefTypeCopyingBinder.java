/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.RefTypeBinder;
import oracle.jdbc.driver.TypeCopyingBinder;
import oracle.jdbc.oracore.OracleTypeADT;

class RefTypeCopyingBinder
extends TypeCopyingBinder {
    RefTypeCopyingBinder(byte[] byArray, OracleTypeADT oracleTypeADT) {
        super(byArray, oracleTypeADT);
        RefTypeBinder.init(this);
    }
}

