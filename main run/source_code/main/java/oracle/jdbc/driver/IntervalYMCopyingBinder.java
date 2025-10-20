/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.ByteCopyingBinder;
import oracle.jdbc.driver.IntervalYMBinder;

class IntervalYMCopyingBinder
extends ByteCopyingBinder {
    IntervalYMCopyingBinder() {
        IntervalYMBinder.init(this);
    }
}

