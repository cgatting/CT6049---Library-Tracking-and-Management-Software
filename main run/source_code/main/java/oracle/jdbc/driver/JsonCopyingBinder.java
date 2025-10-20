/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.ByteCopyingBinder;
import oracle.jdbc.driver.JsonBinder;

class JsonCopyingBinder
extends ByteCopyingBinder {
    JsonCopyingBinder() {
        JsonBinder.init(this);
    }
}

