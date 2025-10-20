/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.util.Random;
import oracle.net.aso.af;
import oracle.net.aso.d;
import oracle.net.aso.e;
import oracle.net.aso.h;
import oracle.net.aso.j;
import oracle.net.aso.m;
import oracle.net.aso.o;

public class b
implements m {
    private byte[] aq;
    private af ar;
    private af as;
    private af at;
    private byte[] au;
    private byte[] av;
    private final boolean aw;
    private final boolean ax;

    b(byte[] byArray, byte[] byArray2, boolean bl, boolean bl2) {
        this.aw = bl;
        this.ax = bl2;
        this.b(byArray, byArray2);
    }

    @Override
    public final void b(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = new byte[16];
        byte[] byArray4 = new byte[16];
        if (this.ax) {
            System.arraycopy(byArray, 0, byArray3, 0, 5);
            byArray3[5] = -1;
            System.arraycopy(byArray2, 0, byArray4, 0, 16);
        } else {
            System.arraycopy(byArray, 0, byArray3, 0, 16);
            byArray3[15] = -1;
            System.arraycopy(byArray, 32, byArray4, 0, 16);
        }
        this.aq = new byte[32];
        this.at = this.k(byArray3, byArray4);
        this.A();
        this.au = null;
        this.av = null;
    }

    @Override
    public final void A() {
        byte[] byArray = this.at.g(this.aq);
        this.aq = byArray;
        byte[] byArray2 = new byte[16];
        byte[] byArray3 = new byte[16];
        System.arraycopy(byArray, 0, byArray2, 0, 16);
        System.arraycopy(byArray, 16, byArray3, 0, 16);
        this.at = this.k(byArray2, byArray3);
        int n2 = this.ax ? 5 : 15;
        byArray3 = new byte[16];
        System.arraycopy(byArray, 0, byArray3, 0, 16);
        byArray3[n2] = -76;
        byte[] byArray4 = new byte[16];
        System.arraycopy(byArray, 16, byArray4, 0, 16);
        this.ar = this.k(byArray3, byArray4);
        byArray3 = new byte[16];
        System.arraycopy(byArray, 0, byArray3, 0, 16);
        byArray3[n2] = 90;
        byte[] byArray5 = new byte[16];
        System.arraycopy(byArray, 16, byArray5, 0, 16);
        this.as = this.k(byArray3, byArray5);
    }

    private af k(byte[] object, byte[] byArray) {
        if (this.aw) {
            e e2 = new e(true, true);
            e2.b((byte[])object, byArray);
            object = h.a(0, byArray, e2, 16);
            return af.a(0, e2, 16, (h)object);
        }
        d d2 = new d();
        d2.b((byte[])object, byArray);
        object = h.a(1, byArray, d2, 16);
        return af.a(0, d2, 16, (h)object);
    }

    @Override
    public final void a(byte[] byArray, byte[] byArray2) {
        if (byArray == null || byArray2 == null) {
            return;
        }
        this.b(byArray, byArray2);
    }

    @Override
    public final byte[] f(byte[] byArray) {
        if (this.av == null) {
            this.av = byArray;
        }
        byArray = this.ar.g(this.av);
        this.av = (byte[])byArray.clone();
        return byArray;
    }

    @Override
    public final byte[] g(byte[] byArray) {
        if (this.au == null) {
            this.au = byArray;
        }
        byArray = this.as.g(this.au);
        this.au = (byte[])byArray.clone();
        return byArray;
    }

    @Override
    public final int z() {
        return 16;
    }

    @Override
    public final String getProviderName() {
        af af2 = this.at;
        return af2.bL.getProviderName();
    }

    public static void a(char[] cArray, int n2, byte[] byArray, int n3) {
        int n4 = n3 - 1;
        int n5 = 0;
        int n6 = n2 < n3 / 2 ? n2 : n3 / 2;
        n2 -= n6;
        n3 -= 2 * n6;
        while (n6-- > 0) {
            cArray[n5] = (char)((0xFF & (char)byArray[n4]) + ((0xFF & (char)byArray[n4 - 1]) << 8));
            ++n5;
            n4 -= 2;
        }
        if (n2 > 0 && n3 % 2 == 1) {
            cArray[n5] = (char)(0xFF & (char)byArray[n4]);
            ++n5;
        }
        while (true) {
            int n7 = --n2;
            --n2;
            if (n7 <= 0) break;
            cArray[n5++] = '\u0000';
        }
    }

    public static void a(byte[] byArray, int n2, char[] cArray, int n3) {
        int n4 = n2 - 1;
        int n5 = 0;
        int n6 = n3 < n2 / 2 ? n3 : n2 / 2;
        n3 -= n6;
        n2 -= 2 * n6;
        while (n6-- > 0) {
            byArray[n4--] = (byte)(0xFF & (byte)cArray[n5]);
            byArray[n4--] = (byte)(cArray[n5] >>> 8);
            ++n5;
        }
        if (n3 > 0 && n2 % 2 == 1) {
            byArray[n4--] = (byte)(0xFF & (byte)cArray[n5]);
        }
        while (n4-- > 0) {
            byArray[n4--] = 0;
        }
    }

    public static void a(char[] cArray, char[] cArray2, char[] cArray3, char[] cArray4, int n2) {
        int n3;
        int n4;
        char[] cArray5 = new char[259];
        boolean[] blArray = new boolean[64];
        char[][] cArray6 = new char[16][257];
        char[] cArray7 = new char[257];
        int n5 = n2;
        char[] cArray8 = cArray4;
        char[] cArray9 = cArray5;
        char[] cArray10 = new char[518];
        char[] cArray11 = new char[520];
        char[] cArray12 = new char[260];
        int n6 = b.b(cArray8, n5);
        int n7 = b.i(2 * n6);
        int n8 = n7 / 16;
        int n9 = (n6 - 2) / 16;
        int n10 = n5 + 2;
        int n11 = n7 - n6;
        char[] cArray13 = cArray9;
        for (n4 = 0; n4 < n10; ++n4) {
            cArray13[n4] = '\u0000';
        }
        cArray13[n11 / 16] = (char)(1 << n11 % 16);
        b.c(cArray9, n5 + 2);
        b.d(cArray12, n5 + 3);
        b.a(cArray12, cArray8, n5);
        int n12 = n7 - n6 + 1;
        --n12;
        n11 = 0;
        while (n12 > 0) {
            ++n11;
            n12 >>>= 1;
        }
        for (n3 = 1 + n11; n3 > 0; --n3) {
            b.b(cArray10, cArray9, n5 + 2);
            n6 = n5 + 3;
            n4 = n9;
            char[] cArray14 = cArray10;
            char[] cArray15 = cArray12;
            char[] cArray16 = cArray11;
            b.d(cArray11, 2 * n6);
            int n13 = b.a(cArray14, n4 + 0, n6);
            for (int i2 = 0; i2 < n6; ++i2) {
                cArray16[n13 + i2] = b.a(cArray16, i2, cArray15[i2], cArray14, n4 + 0, n13);
            }
            b.a(cArray9, cArray9, cArray9, n5 + 2);
            b.a(cArray9, cArray9, cArray11, n8 - n9, n5 + 2);
        }
        b.c(cArray9, n5 + 2);
        while (true) {
            b.b(cArray10, cArray9, cArray12, n5 + 2);
            b.e(cArray10, 2 * (n5 + 2));
            if (b.b(cArray10, 2 * (n5 + 2)) <= n7) break;
            b.e(cArray9, n5 + 2);
        }
        int n14 = b.b(cArray3, n2);
        int n15 = n14 < 4 ? 1 : (n14 < 16 ? 2 : (n14 < 64 ? 3 : 4));
        char[] cArray17 = cArray6[0];
        n5 = n2;
        n3 = 1;
        cArray9 = cArray17;
        cArray17[0] = '\u0001';
        for (int i3 = 1; i3 < n5; ++i3) {
            cArray9[i3] = '\u0000';
        }
        b.a(cArray6[1], cArray2, n2);
        blArray[0] = true;
        blArray[1] = true;
        for (n3 = 2; n3 < 64; ++n3) {
            blArray[n3] = false;
        }
        n3 = 0;
        boolean bl = false;
        n5 = (char)(1 << n14 % 16);
        while (n14 >= 0) {
            if (bl) {
                b.b(cArray7, cArray7, cArray4, cArray5, n2);
            }
            if (!blArray[n3 <<= 1]) {
                b.b(cArray6[n3], cArray6[n3 / 2], cArray4, cArray5, n2);
                blArray[n3] = true;
            }
            if ((cArray3[n14 / 16] & n5) > 0) {
                ++n3;
            }
            n5 = n5 == 1 ? 32768 : (int)((char)(n5 >>> 1 & Short.MAX_VALUE));
            if (!blArray[n3]) {
                b.a(cArray6[n3], cArray6[n3 - 1], cArray2, cArray4, cArray5, n2);
                blArray[n3] = true;
            }
            if (n14 == 0 || n3 >= 1 << n15 - 1) {
                if (bl) {
                    b.a(cArray7, cArray7, cArray6[n3], cArray4, cArray5, n2);
                } else {
                    b.a(cArray7, cArray6[n3], n2);
                }
                n3 = 0;
                bl = true;
            }
            --n14;
        }
        b.a(cArray, cArray7, n2);
    }

    private static int a(char[] cArray, int n2) {
        if ((cArray[n2 - 1] & 0x8000) > 0) {
            return -1;
        }
        --n2;
        while (n2 >= 0) {
            if (cArray[n2] > '\u0000') {
                return 1;
            }
            --n2;
        }
        return 0;
    }

    private static int b(char[] cArray, int n2) {
        char c2 = (char)((cArray[n2 - 1] & 0x8000) > 0 ? -1 : 0);
        --n2;
        while (n2 >= 0 && cArray[n2] == c2) {
            --n2;
        }
        if (n2 == -1) {
            return 1;
        }
        int n3 = 16;
        int n4 = 32768;
        while (n3 >= 0 && 0 == (n4 & (c2 ^ cArray[n2]))) {
            --n3;
            n4 >>>= 1;
        }
        return n2 * 16 + n3;
    }

    private static int i(int n2) {
        return 16 * ((n2 + 1 + 15) / 16);
    }

    private static void c(char[] cArray, int n2) {
        int n3;
        boolean bl = true;
        for (n3 = 0; n3 < n2 - 1 && bl; ++n3) {
            cArray[n3] = (char)(cArray[n3] + '\u0001');
            if (cArray[n3] <= '\u0000') continue;
            bl = false;
        }
        if (bl) {
            cArray[n3] = (char)(cArray[n3] + '\u0001');
        }
    }

    private static void d(char[] cArray, int n2) {
        int n3 = 0;
        while (n3 < n2) {
            cArray[n3++] = '\u0000';
        }
    }

    private static void a(char[] cArray, char[] cArray2, int n2) {
        for (int i2 = 0; i2 < n2; ++i2) {
            cArray[i2] = cArray2[i2];
        }
    }

    private static int a(char[] cArray, int n2, int n3) {
        --n3;
        while (n3 >= 0) {
            if (cArray[n3 + n2] > '\u0000') {
                return n3 + 1;
            }
            --n3;
        }
        return 0;
    }

    private static char a(char[] cArray, int n2, char c2, char[] cArray2, int n3, int n4) {
        int n5 = 0;
        if (c2 <= '\u0000') {
            return '\u0000';
        }
        for (int i2 = 0; i2 < n4; ++i2) {
            n5 += c2 * cArray2[i2 + n3];
            cArray[i2 + n2] = (char)(n5 += cArray[i2 + n2]);
            n5 >>>= 16;
        }
        return (char)n5;
    }

    private static void a(char[] cArray, char[] cArray2, char[] cArray3, int n2) {
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            n3 += cArray2[i2];
            cArray[i2] = (char)(n3 += cArray3[i2]);
            n3 >>>= 16;
        }
    }

    private static void a(char[] cArray, char[] cArray2, char[] cArray3, int n2, int n3) {
        int n4 = 1;
        for (int i2 = 0; i2 < n3; ++i2) {
            n4 += cArray2[i2];
            cArray[i2] = (char)(n4 += ~cArray3[i2 + n2] & 0xFFFF);
            n4 >>>= 16;
        }
    }

    private static void b(char[] cArray, char[] cArray2, int n2) {
        int n3;
        int n4 = 0;
        b.d(cArray, 2 * n2);
        int n5 = b.a(cArray2, 0, n2);
        if (n5 <= 0) {
            return;
        }
        for (n3 = 0; n3 < n5 - 1; ++n3) {
            cArray[n5 + n3] = b.a(cArray, 2 * n3 + 1, cArray2[n3], cArray2, n3 + 1, n5 - n3 - 1);
        }
        b.a(cArray, cArray, cArray, 2 * n2);
        for (n3 = 0; n3 < n5; ++n3) {
            n4 += cArray2[n3] * cArray2[n3];
            cArray[2 * n3] = (char)(n4 += cArray[2 * n3]);
            n4 >>>= 16;
            cArray[2 * n3 + 1] = (char)(n4 += cArray[2 * n3 + 1]);
            n4 >>>= 16;
        }
        cArray[2 * n3] = (char)n4;
    }

    private static void e(char[] cArray, int n2) {
        int n3;
        boolean bl = true;
        for (n3 = 0; n3 < n2 - 1 && bl; ++n3) {
            cArray[n3] = (char)(cArray[n3] - '\u0001');
            if (cArray[n3] == '\uffff') continue;
            bl = false;
        }
        if (bl) {
            cArray[n3] = (char)(cArray[n3] - '\u0001');
        }
    }

    private static void b(char[] cArray, char[] cArray2, char[] cArray3, char[] cArray4, int n2) {
        char[] cArray5 = new char[514];
        b.b(cArray5, cArray2, n2);
        b.c(cArray, cArray5, cArray3, cArray4, n2);
    }

    private static void a(char[] cArray, char[] cArray2, char[] cArray3, char[] cArray4, char[] cArray5, int n2) {
        char[] cArray6 = new char[514];
        b.b(cArray6, cArray2, cArray3, n2);
        b.c(cArray, cArray6, cArray4, cArray5, n2);
    }

    private static void b(char[] cArray, char[] cArray2, char[] cArray3, int n2) {
        b.d(cArray, 2 * n2);
        int n3 = b.a(cArray3, 0, n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            cArray[n3 + i2] = b.a(cArray, i2, cArray2[i2], cArray3, 0, n3);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void c(char[] cArray, char[] cArray2, char[] cArray3, char[] cArray4, int n2) {
        char[] cArray5 = new char[257];
        int n3 = n2;
        char[] cArray6 = cArray4;
        cArray4 = cArray3;
        cArray3 = cArray2;
        cArray2 = cArray;
        cArray = cArray5;
        char[] cArray7 = new char[518];
        char[] cArray8 = new char[518];
        char[] cArray9 = new char[516];
        int n4 = b.b(cArray4, n3);
        int n5 = b.i(2 * n4);
        int n6 = n5 / 16;
        if ((n5 = n6 - (n4 = (n4 - 2) / 16) - 3) < 0) {
            n5 = 0;
        }
        b.d(cArray9, 2 * n3 + 2);
        b.a(cArray9, cArray3, 2 * n3);
        int n7 = n3 + 2;
        int n8 = n5;
        int n9 = n4;
        char[] cArray10 = cArray9;
        cArray9 = cArray6;
        cArray6 = cArray8;
        b.d(cArray8, 2 * n7);
        int n10 = b.a(cArray10, n9, n7);
        int n11 = n8 >= n7 - 1 ? n8 - (n7 - 1) : 0;
        for (int i2 = n11; i2 < n7; ++i2) {
            n11 = n8 >= i2 ? n8 - i2 : 0;
            cArray6[n10 + i2] = b.a(cArray6, i2 + n11, cArray9[i2], cArray10, n11 + n9, n10 >= n11 ? n10 - n11 : 0);
        }
        for (int i3 = 0; i3 < n3; ++i3) {
            cArray[i3] = cArray8[i3 + (n6 - n4)];
        }
        n9 = n3;
        cArray10 = cArray4;
        cArray9 = cArray;
        char[] cArray11 = cArray7;
        b.d(cArray7, n9);
        n8 = b.a(cArray10, 0, n9);
        for (n7 = 0; n7 < n9; ++n7) {
            if (n8 < n9 - n7) {
                cArray11[n8 + n7] = b.a(cArray11, n7, cArray9[n7], cArray10, 0, n8);
                continue;
            }
            b.a(cArray11, n7, cArray9[n7], cArray10, 0, n9 - n7);
        }
        b.a(cArray2, cArray3, cArray7, 0, n3);
        while (true) {
            int n12;
            int n13 = n3;
            cArray9 = cArray4;
            cArray11 = cArray2;
            n9 = b.a(cArray2, n13);
            if (n9 > (n8 = b.a(cArray9, n13))) {
                n12 = 1;
            } else {
                if (n9 < n8) return;
                for (n7 = n13 - 1; n7 >= 0 && cArray11[n7] == cArray9[n7]; --n7) {
                }
                if (n7 == -1) {
                    n12 = 0;
                } else {
                    if (cArray11[n7] <= cArray9[n7]) return;
                    n12 = 1;
                }
            }
            if (n12 < 0) return;
            b.a(cArray2, cArray2, cArray4, 0, n3);
            b.c(cArray, n3);
        }
    }

    public static byte[] i(byte[] object) {
        byte[] byArray = new byte[8];
        byte[] byArray2 = new byte[8];
        Object object2 = new Random();
        ((Random)object2).nextBytes(byArray);
        ((Random)object2).nextBytes(byArray2);
        object2 = new byte[]{121, 111, 114, -123, -82, -107, -109, 0};
        byte[] byArray3 = new byte[8];
        for (int i2 = 0; i2 < 8; ++i2) {
            byArray3[i2] = (byte)(byArray[i2] ^ byArray2[i2] ^ object2[i2]);
        }
        j j2 = new j();
        object2 = new byte[8];
        try {
            j2.b(byArray3, (byte[])object2);
            object = j2.g((byte[])object);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        int n2 = ((byte[])object).length - 1;
        byArray3 = new byte[n2 + 16];
        System.arraycopy(byArray, 0, byArray3, 0, 8);
        System.arraycopy(object, 0, byArray3, 8, n2 / 2);
        System.arraycopy(byArray2, 0, byArray3, 8 + n2 / 2, 8);
        System.arraycopy(object, n2 / 2, byArray3, 8 + n2 / 2 + 8, n2 - n2 / 2);
        o o2 = new o();
        object = o2;
        o2.update(byArray3, 0, byArray3.length);
        byArray = new byte[object.getDigestLength()];
        object.g(byArray, 0);
        object = new byte[byArray3.length + byArray.length];
        System.arraycopy(byArray3, 0, object, 0, byArray3.length);
        System.arraycopy(byArray, 0, object, byArray3.length, byArray.length);
        return object;
    }
}

