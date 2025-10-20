/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.h;
import oracle.net.aso.m;
import oracle.net.aso.z;

final class g
implements h {
    private byte[] aJ;
    private final z aK;
    private final int aL;
    private final byte[] aM;

    g(byte[] byArray, z z2, int n2) {
        this.aJ = byArray != null ? (byte[])byArray.clone() : byArray;
        this.aK = z2;
        this.aL = n2;
        this.aM = new byte[n2];
    }

    @Override
    public final byte[] g(byte[] byArray) {
        m.a(this.aM, this.aJ, byArray, 2, this.aL);
        byArray = this.aK.g(this.aM);
        m.a(this.aJ, byArray, null, 3, this.aL);
        return byArray;
    }

    @Override
    public final byte[] f(byte[] byArray) {
        m.a(this.aM, byArray, null, 3, this.aL);
        byArray = this.aK.f(byArray);
        m.a(byArray, this.aJ, byArray, 2, this.aL);
        m.a(this.aJ, this.aM, null, 3, this.aL);
        return byArray;
    }

    @Override
    public final void j(byte[] byArray) {
        this.aJ = byArray != null ? (byte[])byArray.clone() : byArray;
    }
}

