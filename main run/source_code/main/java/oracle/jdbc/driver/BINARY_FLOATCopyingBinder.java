/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.BINARY_FLOATBinder;
import oracle.jdbc.driver.ByteCopyingBinder;

class BINARY_FLOATCopyingBinder
extends ByteCopyingBinder {
    BINARY_FLOATCopyingBinder() {
        BINARY_FLOATBinder.init(this);
    }
}

