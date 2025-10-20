/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.af;
import oracle.net.aso.h;
import oracle.net.aso.m;
import oracle.net.aso.n;
import oracle.net.aso.s;
import oracle.net.aso.z;

public final class j
implements m {
    static int DES_KEY_SIZE = 8;
    private static byte[] bd = new byte[]{1, 35, 69, 103, -119, -85, -51, -17};
    static final byte[] be = new byte[]{-2, -2, -2, -2, -2, -2, -2, -2};
    static final byte[] bf = new byte[]{88, -46, 26, -119, 7, 0, -59, -68};
    static final byte[] bg = new byte[]{103, 98, -82, -38, 116, -21, -92, -87};
    static final byte[] bh = new byte[]{14, -2, 14, -2, 14, -2, 14, -2};
    private int aB = 1;
    private byte[] key;
    private byte[] c;
    private String bi = "DES40C";
    private af aC;
    private boolean aw;
    private boolean aA = true;
    private int ay = 1;

    public j(String string, int n2, int n3, boolean bl) {
        this.bi = string;
        this.ay = n2;
        this.aw = bl;
        this.aB = n3;
    }

    j() {
    }

    @Override
    public final void b(byte[] byArray, byte[] byArray2) {
        this.aA = this.ay == 1;
        this.key = new byte[8];
        System.arraycopy(byArray, 0, this.key, 0, 8);
        this.L();
    }

    private void L() {
        if (this.bi == "DES40C") {
            if (this.aw) {
                s.l(this.key);
            } else {
                n.l(this.key);
            }
        }
        this.c = (byte[])bd.clone();
        z z2 = this.aw ? new s() : new n();
        z2.b(this.key, this.c);
        h h2 = h.a(this.aA ? 1 : 0, this.c, z2, 8);
        this.aC = af.a(this.aB, z2, 8, h2);
    }

    @Override
    public final byte[] f(byte[] byArray) {
        return this.aC.f(byArray);
    }

    @Override
    public final byte[] g(byte[] byArray) {
        return this.aC.g(byArray);
    }

    @Override
    public final int z() {
        return 8;
    }

    @Override
    public final void a(byte[] byArray, byte[] byArray2) {
        if (byArray != null && byArray2 != null) {
            this.b(byArray, byArray2);
            return;
        }
        this.L();
    }

    @Override
    public final String getProviderName() {
        af af2 = this.aC;
        return af2.bL.getProviderName();
    }
}

