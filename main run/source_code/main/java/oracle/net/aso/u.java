/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.p;

final class u
implements p {
    private int[] cp = new int[80];
    private long cq;
    private int[] cr = new int[5];

    u() {
        this.reset();
    }

    @Override
    public final int getDigestLength() {
        return 20;
    }

    private void update(byte by) {
        int n2 = ((int)this.cq & 0x3F) >>> 2;
        int n3 = (~((int)this.cq) & 3) << 3;
        this.cp[n2] = this.cp[n2] & ~(255 << n3) | (by & 0xFF) << n3;
        if (((int)this.cq & 0x3F) == 63) {
            this.ac();
        }
        ++this.cq;
    }

    @Override
    public final void update(byte[] byArray, int n2, int n3) {
        while (n3 > 0) {
            if (((int)this.cq & 3) != 0 || n3 < 4) {
                this.update(byArray[n2]);
                ++n2;
                --n3;
                continue;
            }
            int n4 = ((int)this.cq & 0x3F) >> 2;
            this.cp[n4] = (byArray[n2] & 0xFF) << 24 | (byArray[n2 + 1] & 0xFF) << 16 | (byArray[n2 + 2] & 0xFF) << 8 | byArray[n2 + 3] & 0xFF;
            this.cq += 4L;
            if (((int)this.cq & 0x3F) == 0) {
                this.ac();
            }
            n2 += 4;
            n3 -= 4;
        }
    }

    @Override
    public final void reset() {
        this.cr[0] = 1732584193;
        this.cr[1] = -271733879;
        this.cr[2] = -1732584194;
        this.cr[3] = 271733878;
        this.cr[4] = -1009589776;
        for (int i2 = 0; i2 < 80; ++i2) {
            this.cp[i2] = 0;
        }
        this.cq = 0L;
    }

    @Override
    public final int g(byte[] byArray, int n2) {
        if (byArray.length < 20) {
            return 0;
        }
        long l2 = this.cq << 3;
        this.update((byte)-128);
        while ((int)(this.cq & 0x3FL) != 56) {
            this.update((byte)0);
        }
        this.cp[14] = (int)(l2 >>> 32);
        this.cp[15] = (int)l2;
        this.cq += 8L;
        this.ac();
        n2 = 0;
        int n3 = 0;
        while (n2 < this.cr.length) {
            byArray[n3 + 0] = this.cr[n2] >> 24;
            byArray[n3 + 0 + 1] = (byte)(this.cr[n2] >>> 16);
            byArray[n3 + 0 + 2] = (byte)(this.cr[n2] >>> 8);
            byArray[n3 + 0 + 3] = (byte)this.cr[n2];
            ++n2;
            n3 += 4;
        }
        this.reset();
        return 20;
    }

    private void ac() {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int[] nArray = this.cp;
        for (n8 = 16; n8 < 80; ++n8) {
            n7 = nArray[n8 - 16] ^ nArray[n8 - 14] ^ nArray[n8 - 8] ^ nArray[n8 - 3];
            nArray[n8] = n7 << 1 | n7 >>> 31;
        }
        int n9 = this.cr[0];
        int n10 = this.cr[1];
        int n11 = this.cr[2];
        int n12 = this.cr[3];
        int n13 = this.cr[4];
        int n14 = 0;
        while (n14 < 20) {
            n6 = n14++;
            n5 = this.cr[4];
            n4 = this.cr[3];
            n3 = this.cr[2];
            n7 = this.cr[1];
            n8 = this.cr[0];
            int[] nArray2 = this.cp;
            nArray = this.cr;
            n2 = (n8 << 5 | n8 >>> 27) + (n7 & n3 | ~n7 & n4) + n5 + nArray2[n6] + 1518500249;
            nArray[4] = nArray[3];
            nArray[3] = nArray[2];
            nArray[2] = nArray[1] << 30 | nArray[1] >>> 2;
            nArray[1] = nArray[0];
            nArray[0] = n2;
        }
        n14 = 20;
        while (n14 < 40) {
            n6 = n14++;
            n5 = this.cr[4];
            n4 = this.cr[3];
            n3 = this.cr[2];
            n7 = this.cr[1];
            n8 = this.cr[0];
            int[] nArray3 = this.cp;
            nArray = this.cr;
            n2 = (n8 << 5 | n8 >>> 27) + (n7 ^ n3 ^ n4) + n5 + nArray3[n6] + 1859775393;
            nArray[4] = nArray[3];
            nArray[3] = nArray[2];
            nArray[2] = nArray[1] << 30 | nArray[1] >>> 2;
            nArray[1] = nArray[0];
            nArray[0] = n2;
        }
        n14 = 40;
        while (n14 < 60) {
            n6 = n14++;
            n5 = this.cr[4];
            n4 = this.cr[3];
            n3 = this.cr[2];
            n7 = this.cr[1];
            n8 = this.cr[0];
            int[] nArray4 = this.cp;
            nArray = this.cr;
            n2 = (n8 << 5 | n8 >>> 27) + (n7 & n3 | n7 & n4 | n3 & n4) + n5 + nArray4[n6] + -1894007588;
            nArray[4] = nArray[3];
            nArray[3] = nArray[2];
            nArray[2] = nArray[1] << 30 | nArray[1] >>> 2;
            nArray[1] = nArray[0];
            nArray[0] = n2;
        }
        n14 = 60;
        while (n14 < 80) {
            n6 = n14++;
            n5 = this.cr[4];
            n4 = this.cr[3];
            n3 = this.cr[2];
            n7 = this.cr[1];
            n8 = this.cr[0];
            int[] nArray5 = this.cp;
            nArray = this.cr;
            n2 = (n8 << 5 | n8 >>> 27) + (n7 ^ n3 ^ n4) + n5 + nArray5[n6] + -899497514;
            nArray[4] = nArray[3];
            nArray[3] = nArray[2];
            nArray[2] = nArray[1] << 30 | nArray[1] >>> 2;
            nArray[1] = nArray[0];
            nArray[0] = n2;
        }
        this.cr[0] = this.cr[0] + n9;
        this.cr[1] = this.cr[1] + n10;
        this.cr[2] = this.cr[2] + n11;
        this.cr[3] = this.cr[3] + n12;
        this.cr[4] = this.cr[4] + n13;
    }
}

