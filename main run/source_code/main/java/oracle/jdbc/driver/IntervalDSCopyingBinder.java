/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.ByteCopyingBinder;
import oracle.jdbc.driver.IntervalDSBinder;

class IntervalDSCopyingBinder
extends ByteCopyingBinder {
    IntervalDSCopyingBinder() {
        IntervalDSBinder.init(this);
    }
}

