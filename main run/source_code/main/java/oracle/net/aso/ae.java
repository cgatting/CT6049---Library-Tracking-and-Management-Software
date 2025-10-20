/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.util.Arrays;
import oracle.net.aso.af;
import oracle.net.aso.h;
import oracle.net.aso.z;

final class ae
extends af {
    private final byte[] aM;

    ae(int n2, z z2, h h2) {
        super(n2, z2, h2);
        this.aM = new byte[n2];
    }

    @Override
    public final byte[] g(byte[] byArray) {
        int n2 = byArray.length;
        byte by = (byte)(n2 % this.aL == 0 ? this.aL : this.aL - n2 % this.aL);
        int n3 = n2 + by;
        byte[] byArray2 = new byte[n3];
        if (this.bL.j(n3)) {
            byte[] byArray3 = new byte[n3];
            System.arraycopy(byArray, 0, byArray3, 0, n2);
            Arrays.fill(byArray3, n2, n3, by);
            this.bL.a(byArray3, 0, n3, byArray2);
        } else {
            for (int i2 = 0; i2 < n3; i2 += this.aL) {
                if (i2 <= n2 - this.aL) {
                    System.arraycopy(byArray, i2, this.aM, 0, this.aL);
                } else {
                    System.arraycopy(byArray, i2, this.aM, 0, byArray.length - i2);
                    Arrays.fill(this.aM, byArray.length - i2, this.aL, by);
                }
                byte[] byArray4 = this.bM.g(this.aM);
                System.arraycopy(byArray4, 0, byArray2, i2, this.aL);
            }
        }
        return byArray2;
    }

    @Override
    public final byte[] f(byte[] byArray) {
        byte[] byArray2;
        int n2;
        int n3 = byArray.length;
        byte[] byArray3 = new byte[n3];
        if (this.bL.j(n3)) {
            this.bL.b(byArray, 0, n3, byArray3);
        } else {
            for (n2 = 0; n2 < n3 - 1; n2 += this.aL) {
                System.arraycopy(byArray, n2, this.aM, 0, this.aL);
                byArray2 = this.bM.f(this.aM);
                System.arraycopy(byArray2, 0, byArray3, n2, this.aL);
            }
        }
        n2 = byArray3[n3 - 1];
        byArray2 = Arrays.copyOfRange(byArray3, 0, n3 - n2);
        return byArray2;
    }
}

