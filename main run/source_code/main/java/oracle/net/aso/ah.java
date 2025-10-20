/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.ag;
import oracle.net.aso.r;

final class ah
implements ag {
    private byte[] cn = new byte[256];
    private int co;
    private int cE;

    private ah(r r2) {
    }

    @Override
    public final void a(int n2, byte[] byArray, int n3) {
        for (n2 = 0; n2 < 256; ++n2) {
            this.cn[n2] = (byte)n2;
        }
        this.cE = 0;
        this.co = 0;
        n2 = 0;
        int n4 = 0;
        int n5 = 0;
        while (n2 < 256) {
            byte by = this.cn[n2];
            if (n5 == n3) {
                n5 = 0;
            }
            n4 = n4 + by + byArray[n5] & 0xFF;
            this.cn[n2] = this.cn[n4];
            this.cn[n4] = by;
            ++n2;
            ++n5;
        }
    }

    @Override
    public final void a(byte[] byArray, byte[] byArray2, int n2) {
        int n3 = this.co;
        int n4 = this.cE;
        for (int i2 = 0; i2 < n2; ++i2) {
            n3 = n3 + 1 & 0xFF;
            int n5 = this.cn[n3];
            n4 = n4 + n5 & 0xFF;
            byte by = this.cn[n4];
            this.cn[n3] = by;
            this.cn[n4] = (byte)n5;
            n5 = n5 + by & 0xFF;
            byArray[i2] = (byte)(byArray2[i2] ^ this.cn[n5]);
        }
        this.co = n3;
        this.cE = n4;
    }

    @Override
    public final String getProviderName() {
        return "JavaNet";
    }

    /* synthetic */ ah(r r2, byte by) {
        this(r2);
    }
}

