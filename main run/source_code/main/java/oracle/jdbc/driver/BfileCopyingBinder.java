/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.BfileBinder;
import oracle.jdbc.driver.ByteCopyingBinder;

class BfileCopyingBinder
extends ByteCopyingBinder {
    BfileCopyingBinder() {
        BfileBinder.init(this);
    }
}

