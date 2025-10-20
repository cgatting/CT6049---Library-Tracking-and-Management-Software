/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.ByteCopyingBinder;
import oracle.jdbc.driver.DateBinder;

class DateCopyingBinder
extends ByteCopyingBinder {
    DateCopyingBinder() {
        DateBinder.init(this);
    }
}

