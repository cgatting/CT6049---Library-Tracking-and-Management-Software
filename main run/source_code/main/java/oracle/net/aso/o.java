/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.p;

final class o
implements p {
    private long[] bY = new long[2];
    private long[] bZ = new long[4];
    private char[] ca = new char[64];
    private char[] cb = new char[16];
    private static final byte[] cc = new byte[]{-128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private static long a(long l2, int n2) {
        long l3 = l2 << n2;
        long l4 = -1L << n2;
        long l5 = l4 ^ 0xFFFFFFFFFFFFFFFFL;
        long l6 = l2 >>> 32 - n2 & l5;
        return l6 | l3;
    }

    private long a(long l2, long l3, long l4, long l5, long l6, int n2, long l7) {
        long l8 = l5;
        long l9 = l4;
        long l10 = l3;
        l2 += (l10 & l9 | (l10 ^ 0xFFFFFFFFFFFFFFFFL) & l8) + l6 + l7;
        l2 = o.a(l2, n2);
        return l2 += l3;
    }

    private long b(long l2, long l3, long l4, long l5, long l6, int n2, long l7) {
        long l8 = l5;
        long l9 = l4;
        long l10 = l3;
        l2 += (l10 & l8 | l9 & (l8 ^ 0xFFFFFFFFFFFFFFFFL)) + l6 + l7;
        l2 = o.a(l2, n2);
        return l2 += l3;
    }

    private long c(long l2, long l3, long l4, long l5, long l6, int n2, long l7) {
        long l8 = l5;
        long l9 = l4;
        long l10 = l3;
        l2 += (l10 ^ l9 ^ l8) + l6 + l7;
        l2 = o.a(l2, n2);
        return l2 += l3;
    }

    private long d(long l2, long l3, long l4, long l5, long l6, int n2, long l7) {
        long l8 = l5;
        long l9 = l4;
        long l10 = l3;
        l2 += (l9 ^ (l10 | l8 ^ 0xFFFFFFFFFFFFFFFFL)) + l6 + l7;
        l2 = o.a(l2, n2);
        return l2 += l3;
    }

    @Override
    public final void reset() {
        this.bY[0] = 0L;
        this.bY[1] = 0L;
        this.bZ[0] = 1732584193L;
        this.bZ[1] = -271733879L;
        this.bZ[2] = -1732584194L;
        this.bZ[3] = 271733878L;
    }

    private static void a(long[] lArray, char[] cArray, int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < n2; i2 += 4) {
            lArray[n3] = (long)cArray[i2] & 0xFFL | (long)cArray[i2 + 1] << 8 & 0xFF00L | (long)cArray[i2 + 2] << 16 & 0xFF0000L | (long)cArray[i2 + 3] << 24 & 0xFFFFFFFFFF000000L;
            ++n3;
        }
    }

    private void a(long[] lArray, long[] lArray2) {
        long l2 = lArray[0];
        long l3 = lArray[1];
        long l4 = lArray[2];
        long l5 = lArray[3];
        l2 = this.a(l2, l3, l4, l5, lArray2[0], 7, -680876936L);
        l5 = this.a(l5, l2, l3, l4, lArray2[1], 12, -389564586L);
        l4 = this.a(l4, l5, l2, l3, lArray2[2], 17, 606105819L);
        l3 = this.a(l3, l4, l5, l2, lArray2[3], 22, -1044525330L);
        l2 = this.a(l2, l3, l4, l5, lArray2[4], 7, -176418897L);
        l5 = this.a(l5, l2, l3, l4, lArray2[5], 12, 1200080426L);
        l4 = this.a(l4, l5, l2, l3, lArray2[6], 17, -1473231341L);
        l3 = this.a(l3, l4, l5, l2, lArray2[7], 22, -45705983L);
        l2 = this.a(l2, l3, l4, l5, lArray2[8], 7, 1770035416L);
        l5 = this.a(l5, l2, l3, l4, lArray2[9], 12, -1958414417L);
        l4 = this.a(l4, l5, l2, l3, lArray2[10], 17, -42063L);
        l3 = this.a(l3, l4, l5, l2, lArray2[11], 22, -1990404162L);
        l2 = this.a(l2, l3, l4, l5, lArray2[12], 7, 1804603682L);
        l5 = this.a(l5, l2, l3, l4, lArray2[13], 12, -40341101L);
        l4 = this.a(l4, l5, l2, l3, lArray2[14], 17, -1502002290L);
        l3 = this.a(l3, l4, l5, l2, lArray2[15], 22, 1236535329L);
        l2 = this.b(l2, l3, l4, l5, lArray2[1], 5, -165796510L);
        l5 = this.b(l5, l2, l3, l4, lArray2[6], 9, -1069501632L);
        l4 = this.b(l4, l5, l2, l3, lArray2[11], 14, 643717713L);
        l3 = this.b(l3, l4, l5, l2, lArray2[0], 20, -373897302L);
        l2 = this.b(l2, l3, l4, l5, lArray2[5], 5, -701558691L);
        l5 = this.b(l5, l2, l3, l4, lArray2[10], 9, 38016083L);
        l4 = this.b(l4, l5, l2, l3, lArray2[15], 14, -660478335L);
        l3 = this.b(l3, l4, l5, l2, lArray2[4], 20, -405537848L);
        l2 = this.b(l2, l3, l4, l5, lArray2[9], 5, 568446438L);
        l5 = this.b(l5, l2, l3, l4, lArray2[14], 9, -1019803690L);
        l4 = this.b(l4, l5, l2, l3, lArray2[3], 14, -187363961L);
        l3 = this.b(l3, l4, l5, l2, lArray2[8], 20, 1163531501L);
        l2 = this.b(l2, l3, l4, l5, lArray2[13], 5, -1444681467L);
        l5 = this.b(l5, l2, l3, l4, lArray2[2], 9, -51403784L);
        l4 = this.b(l4, l5, l2, l3, lArray2[7], 14, 1735328473L);
        l3 = this.b(l3, l4, l5, l2, lArray2[12], 20, -1926607734L);
        l2 = this.c(l2, l3, l4, l5, lArray2[5], 4, -378558L);
        l5 = this.c(l5, l2, l3, l4, lArray2[8], 11, -2022574463L);
        l4 = this.c(l4, l5, l2, l3, lArray2[11], 16, 1839030562L);
        l3 = this.c(l3, l4, l5, l2, lArray2[14], 23, -35309556L);
        l2 = this.c(l2, l3, l4, l5, lArray2[1], 4, -1530992060L);
        l5 = this.c(l5, l2, l3, l4, lArray2[4], 11, 1272893353L);
        l4 = this.c(l4, l5, l2, l3, lArray2[7], 16, -155497632L);
        l3 = this.c(l3, l4, l5, l2, lArray2[10], 23, -1094730640L);
        l2 = this.c(l2, l3, l4, l5, lArray2[13], 4, 681279174L);
        l5 = this.c(l5, l2, l3, l4, lArray2[0], 11, -358537222L);
        l4 = this.c(l4, l5, l2, l3, lArray2[3], 16, -722521979L);
        l3 = this.c(l3, l4, l5, l2, lArray2[6], 23, 76029189L);
        l2 = this.c(l2, l3, l4, l5, lArray2[9], 4, -640364487L);
        l5 = this.c(l5, l2, l3, l4, lArray2[12], 11, -421815835L);
        l4 = this.c(l4, l5, l2, l3, lArray2[15], 16, 530742520L);
        l3 = this.c(l3, l4, l5, l2, lArray2[2], 23, -995338651L);
        l2 = this.d(l2, l3, l4, l5, lArray2[0], 6, -198630844L);
        l5 = this.d(l5, l2, l3, l4, lArray2[7], 10, 1126891415L);
        l4 = this.d(l4, l5, l2, l3, lArray2[14], 15, -1416354905L);
        l3 = this.d(l3, l4, l5, l2, lArray2[5], 21, -57434055L);
        l2 = this.d(l2, l3, l4, l5, lArray2[12], 6, 1700485571L);
        l5 = this.d(l5, l2, l3, l4, lArray2[3], 10, -1894986606L);
        l4 = this.d(l4, l5, l2, l3, lArray2[10], 15, -1051523L);
        l3 = this.d(l3, l4, l5, l2, lArray2[1], 21, -2054922799L);
        l2 = this.d(l2, l3, l4, l5, lArray2[8], 6, 1873313359L);
        l5 = this.d(l5, l2, l3, l4, lArray2[15], 10, -30611744L);
        l4 = this.d(l4, l5, l2, l3, lArray2[6], 15, -1560198380L);
        l3 = this.d(l3, l4, l5, l2, lArray2[13], 21, 1309151649L);
        l2 = this.d(l2, l3, l4, l5, lArray2[4], 6, -145523070L);
        l5 = this.d(l5, l2, l3, l4, lArray2[11], 10, -1120210379L);
        l4 = this.d(l4, l5, l2, l3, lArray2[2], 15, 718787259L);
        l3 = this.d(l3, l4, l5, l2, lArray2[9], 21, -343485551L);
        lArray[0] = lArray[0] + l2;
        lArray[1] = lArray[1] + l3;
        lArray[2] = lArray[2] + l4;
        lArray[3] = lArray[3] + l5;
    }

    o() {
        this.reset();
    }

    @Override
    public final int getDigestLength() {
        return 16;
    }

    @Override
    public final void update(byte[] objectArray, int n2, int n3) {
        char[] cArray = new char[n3];
        int n4 = n3;
        int n5 = 0;
        byte[] byArray = objectArray;
        objectArray = cArray;
        for (int i2 = 0; i2 < n4; ++i2) {
            objectArray[i2] = (char)byArray[i2 + n5];
        }
        objectArray = new long[16];
        int n6 = (int)(this.bY[0] >>> 3 & 0x3FL);
        if (this.bY[0] + (long)(n3 << 3) < this.bY[0]) {
            this.bY[1] = this.bY[1] + 1L;
        }
        this.bY[0] = this.bY[0] + ((long)n3 << 3);
        this.bY[1] = this.bY[1] + ((long)n3 >>> 29);
        n5 = 0;
        while (n3-- > 0) {
            this.ca[n6++] = cArray[n5++];
            if (n6 != 64) continue;
            o.a(objectArray, this.ca, 64);
            this.a(this.bZ, (long[])objectArray);
            n6 = 0;
        }
    }

    @Override
    public final int g(byte[] byArray, int n2) {
        if (byArray.length < 16) {
            return 0;
        }
        long[] lArray = new long[16];
        Object[] objectArray = lArray;
        lArray[14] = this.bY[0];
        objectArray[15] = this.bY[1];
        int n3 = (int)(this.bY[0] >>> 3 & 0x3FL);
        n3 = n3 < 56 ? 56 - n3 : 120 - n3;
        this.update(cc, 0, n3);
        o.a(objectArray, this.ca, 56);
        this.a(this.bZ, (long[])objectArray);
        int n4 = 4;
        long[] lArray2 = this.bZ;
        objectArray = this.cb;
        n4 = 0;
        int n5 = 0;
        while (n4 < 4) {
            objectArray[n5] = (char)(lArray2[n4] & 0xFFL);
            objectArray[n5 + 1] = (char)(lArray2[n4] >> 8 & 0xFFL);
            objectArray[n5 + 2] = (char)(lArray2[n4] >> 16 & 0xFFL);
            objectArray[n5 + 3] = (char)(lArray2[n4] >> 24 & 0xFFL);
            ++n4;
            n5 += 4;
        }
        int n6 = this.cb.length;
        char[] cArray = this.cb;
        int n7 = 0;
        objectArray = byArray;
        for (n5 = 0; n5 < n6; ++n5) {
            objectArray[n5 + n7] = (byte)cArray[n5];
        }
        return 16;
    }
}

