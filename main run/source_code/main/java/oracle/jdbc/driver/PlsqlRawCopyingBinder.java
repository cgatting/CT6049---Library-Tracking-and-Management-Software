/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.ByteCopyingBinder;
import oracle.jdbc.driver.PlsqlRawBinder;

class PlsqlRawCopyingBinder
extends ByteCopyingBinder {
    PlsqlRawCopyingBinder() {
        PlsqlRawBinder.init(this);
    }
}

