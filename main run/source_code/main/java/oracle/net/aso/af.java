/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.ac;
import oracle.net.aso.ad;
import oracle.net.aso.ae;
import oracle.net.aso.h;
import oracle.net.aso.z;

public abstract class af {
    final int aL;
    final z bL;
    final h bM;

    af(int n2, z z2, h h2) {
        this.aL = n2;
        this.bL = z2;
        this.bM = h2;
    }

    static af a(int n2, z z2, int n3, h h2) {
        if (n2 == 1) {
            return new ad(n3, z2, h2);
        }
        if (n2 == 2) {
            return new ae(n3, z2, h2);
        }
        return new ac(n3, z2, h2);
    }

    abstract byte[] g(byte[] var1);

    abstract byte[] f(byte[] var1);
}

