/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.io.IOException;

public final class f
extends IOException {
    private int aE;

    public f(int n2) {
        this.aE = n2;
    }

    public f(int n2, Throwable throwable) {
        super(throwable);
        this.aE = 107;
    }

    @Override
    public final String getMessage() {
        return Integer.toString(this.aE);
    }
}

