/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.BINARY_DOUBLEBinder;
import oracle.jdbc.driver.ByteCopyingBinder;

class BINARY_DOUBLECopyingBinder
extends ByteCopyingBinder {
    BINARY_DOUBLECopyingBinder() {
        BINARY_DOUBLEBinder.init(this);
    }
}

