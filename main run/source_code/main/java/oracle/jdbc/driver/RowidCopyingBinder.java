/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.ByteCopyingBinder;
import oracle.jdbc.driver.RowidBinder;

class RowidCopyingBinder
extends ByteCopyingBinder {
    RowidCopyingBinder() {
        RowidBinder.init(this);
    }
}

