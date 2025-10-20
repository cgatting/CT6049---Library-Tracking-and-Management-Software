/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.Binder;
import oracle.jdbc.driver.DateCommonBinder;

abstract class DateBinder
extends DateCommonBinder {
    static void init(Binder binder) {
        binder.type = (short)12;
        binder.bytelen = 7;
    }

    DateBinder() {
        DateBinder.init(this);
    }
}

