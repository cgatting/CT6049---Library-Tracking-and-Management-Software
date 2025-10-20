/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.t;
import oracle.net.aso.v;

public abstract class l {
    private static final short[] bO = new short[]{40, 41, 56, 128, 256};
    private static final short[] bP = new short[]{40, 64, 56, 128, 256};
    private static final short[] bQ = new short[]{80, 112, 112, 512, 512};
    private static final short[] bR = new short[]{300, 512, 512, 512, 512};
    private static final byte[] bS = new byte[]{2, 83, -77, -14, -90, -115, 61, -69, 106, -61, -103, 9, -64, -41, 4, 5, -14, 91, -126, 97, 107, 122, -24, -36, 29, 123, 3, -106, 53, -30, -37, -17, 67, 102, -6, -48, 76, -63};
    private static final byte[] bT = new byte[]{12, 54, -127, -73, 4, 71, 3, -96, 120, 96, 81, 38, -116, -22, -101, -68, -93, 62, 124, 1, -85, 54, -117, 34, 117, -104, 119, 102, 53, -59, -128, -43, 36, -46, 80, 99, -72, -13};
    private static final byte[] bU = new byte[]{-126, -104, -34, 73, -34, -9, 9, -27, -32, 13, -80, -96, -91, -100, -87, -14, 61, -10, -58, -89, -23, 74, 68, -93, -31, -121, 46, -11, 76, 31, -95, 122, -33, 92, -14, 117, -127, -19, 81, -61, 38, -18, -117, -31, 4, 3, 30, 103, 80, 83, -75, 124, 75, 69, 111, 21, 74, 23, 86, 11, 90, 21, -107, -91};
    private static final byte[] bV = new byte[]{-36, -114, -93, 27, 8, 96, 105, -118, -52, -10, -47, -98, -121, 14, 52, -4, 103, -59, 89, 11, 78, -90, -79, 60, -43, -3, -17, 21, -84, -99, 95, 63, 33, 76, -36, 7, -52, -121, 74, -77, 1, -41, 127, 44, 67, 51, 81, 60, -34, 11, 30, -50, 100, 71, 118, 87, 92, 81, -52, -104, -77, -2, -25, -17};
    private static final byte[][] bW = new byte[][]{bS, bU, bU, bU, bU};
    private static final byte[][] bX = new byte[][]{bT, bV, bV, bV, bV};

    public abstract byte[] f(byte[] var1, int var2);

    public abstract byte[] aa();

    public static final l a(byte[] byArray, byte[] byArray2, short s2, short s3, boolean bl, boolean bl2) {
        if (bl) {
            return new v(byArray, byArray2, s2, s3, bl2);
        }
        return new t(byArray, byArray2, s2, s3, 0);
    }

    static /* synthetic */ short[] M() {
        return bO;
    }

    static /* synthetic */ short[] N() {
        return bP;
    }

    static /* synthetic */ short[] O() {
        return bQ;
    }

    static /* synthetic */ short[] P() {
        return bR;
    }

    static /* synthetic */ byte[][] Q() {
        return bW;
    }

    static /* synthetic */ byte[][] R() {
        return bX;
    }
}

