/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.CharCopyingBinder;
import oracle.jdbc.driver.FixedCHARBinder;

class FixedCHARCopyingBinder
extends CharCopyingBinder {
    String paramVal;

    FixedCHARCopyingBinder(String string) {
        FixedCHARBinder.init(this);
        this.paramVal = string;
    }
}

