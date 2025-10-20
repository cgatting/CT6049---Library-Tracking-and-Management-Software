/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.g;
import oracle.net.aso.x;
import oracle.net.aso.z;

public interface h {
    public static h a(int n2, byte[] byArray, z z2, int n3) {
        if (n2 == 1) {
            return new g(byArray, z2, n3);
        }
        return new x(z2);
    }

    public byte[] g(byte[] var1);

    public byte[] f(byte[] var1);

    public void j(byte[] var1);
}

