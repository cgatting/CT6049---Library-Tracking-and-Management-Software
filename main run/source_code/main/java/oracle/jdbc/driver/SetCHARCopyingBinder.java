/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.CharCopyingBinder;
import oracle.jdbc.driver.SetCHARBinder;

class SetCHARCopyingBinder
extends CharCopyingBinder {
    SetCHARCopyingBinder() {
        SetCHARBinder.init(this);
    }
}

