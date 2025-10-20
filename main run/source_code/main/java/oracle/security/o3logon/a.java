/*
 * Decompiled with CFR 0.152.
 */
package oracle.security.o3logon;

import oracle.net.aso.j;

public final class a {
    private byte[] key;
    private boolean aw;

    public a(boolean bl) {
        this.aw = bl;
    }

    public final byte[] e(byte[] byArray, byte[] byArray2) {
        try {
            j j2 = new j("DES56C", 0, 2, this.aw);
            j2.b(byArray, new byte[0]);
            return j2.g(byArray2);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public final byte[] f(byte[] byArray, byte[] byArray2) {
        try {
            j j2 = new j("DES56C", 0, 2, this.aw);
            j2.b(byArray, new byte[0]);
            return j2.f(byArray2);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public final byte[] a(byte[] byArray, boolean bl) {
        try {
            j j2 = new j("DES56C", 0, 0, this.aw);
            j2.b(this.key, new byte[0]);
            byte[] byArray2 = new byte[8];
            byte[] byArray3 = new byte[byArray.length];
            for (int i2 = 0; i2 < byArray.length; i2 += 8) {
                for (int i3 = 0; i3 < 8; ++i3) {
                    int n2 = i3;
                    byArray2[n2] = (byte)(byArray2[n2] ^ byArray[i2 + i3]);
                }
                byArray2 = j2.g(byArray2);
                if (bl) continue;
                System.arraycopy(byArray2, 0, byArray3, i2, 8);
            }
            if (bl) {
                return byArray2;
            }
            return byArray3;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public final byte[] l(byte[] byArray, byte[] byArray2) {
        try {
            j j2 = new j("DES56C", 0, 0, this.aw);
            j2.b(byArray, new byte[0]);
            return j2.f(byArray2);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public final void m(byte[] byArray) {
        this.key = byArray;
    }
}

