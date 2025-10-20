/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.af;
import oracle.net.aso.h;
import oracle.net.aso.z;

final class ac
extends af {
    private final byte[] aM;

    ac(int n2, z z2, h h2) {
        super(n2, z2, h2);
        this.aM = new byte[n2];
    }

    @Override
    public final byte[] g(byte[] byArray) {
        int n2 = byArray.length;
        byte[] byArray2 = new byte[n2];
        if (this.bL.j(n2)) {
            this.bL.a(byArray, 0, n2, byArray2);
        } else {
            for (int i2 = 0; i2 < n2; i2 += this.aL) {
                System.arraycopy(byArray, i2, this.aM, 0, this.aL);
                byte[] byArray3 = this.bM.g(this.aM);
                System.arraycopy(byArray3, 0, byArray2, i2, this.aL);
            }
        }
        return byArray2;
    }

    @Override
    public final byte[] f(byte[] byArray) {
        int n2 = byArray.length;
        byte[] byArray2 = new byte[n2];
        if (this.bL.j(n2)) {
            this.bL.b(byArray, 0, n2, byArray2);
        } else {
            for (int i2 = 0; i2 < n2; i2 += this.aL) {
                System.arraycopy(byArray, i2, this.aM, 0, this.aL);
                byte[] byArray3 = this.bM.f(this.aM);
                System.arraycopy(byArray3, 0, byArray2, i2, this.aL);
            }
        }
        return byArray2;
    }
}

