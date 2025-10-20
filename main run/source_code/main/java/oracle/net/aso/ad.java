/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.util.Arrays;
import oracle.net.aso.af;
import oracle.net.aso.h;
import oracle.net.aso.z;

final class ad
extends af {
    private final byte[] aM;

    ad(int n2, z z2, h h2) {
        super(n2, z2, h2);
        this.aM = new byte[n2];
    }

    @Override
    public final byte[] g(byte[] byArray) {
        int n2 = byArray.length;
        byte by = (byte)(n2 % this.aL == 0 ? 0 : this.aL - n2 % this.aL);
        int n3 = n2 + by;
        byte[] byArray2 = new byte[n3 + 1];
        if (this.bL.j(n3)) {
            byte[] byArray3 = new byte[n3];
            System.arraycopy(byArray, 0, byArray3, 0, n2);
            this.bL.a(byArray3, 0, n3, byArray2);
        } else {
            for (int i2 = 0; i2 < n2; i2 += this.aL) {
                if (i2 <= n2 - this.aL) {
                    System.arraycopy(byArray, i2, this.aM, 0, this.aL);
                } else {
                    System.arraycopy(byArray, i2, this.aM, 0, byArray.length - i2);
                    Arrays.fill(this.aM, byArray.length - i2, this.aL, (byte)0);
                }
                byte[] byArray4 = this.bM.g(this.aM);
                System.arraycopy(byArray4, 0, byArray2, i2, this.aL);
            }
        }
        byArray2[n3] = (byte)(by + 1);
        return byArray2;
    }

    @Override
    public final byte[] f(byte[] byArray) {
        int n2 = byArray.length;
        int n3 = byArray[n2 - 1];
        if (n3 < 0 || n3 > this.aL) {
            throw new RuntimeException("Invalid padding value");
        }
        n3 = n2 - n3;
        byte[] byArray2 = new byte[n3];
        if (this.bL.j(n2 - 1)) {
            byte[] byArray3 = new byte[n2 - 1];
            this.bL.b(byArray, 0, n2 - 1, byArray3);
            System.arraycopy(byArray3, 0, byArray2, 0, n3);
        } else {
            for (int i2 = 0; i2 < n2 - 1; i2 += this.aL) {
                System.arraycopy(byArray, i2, this.aM, 0, this.aL);
                byte[] byArray4 = this.bM.f(this.aM);
                System.arraycopy(byArray4, 0, byArray2, i2, Math.min(this.aL, n3 - i2));
            }
        }
        return byArray2;
    }
}

