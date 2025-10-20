/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.ByteCopyingBinder;
import oracle.jdbc.driver.RawBinder;

class RawCopyingBinder
extends ByteCopyingBinder {
    RawCopyingBinder() {
        RawBinder.init(this);
    }
}

