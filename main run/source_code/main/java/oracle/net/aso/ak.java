/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.af;
import oracle.net.aso.al;
import oracle.net.aso.am;
import oracle.net.aso.h;
import oracle.net.aso.m;
import oracle.net.aso.z;

final class ak
implements m {
    private static int dh = 8;
    private static int di = 24;
    private static final byte[] bd = new byte[]{1, 35, 69, 103, -119, -85, -51, -17};
    private af aC;
    private boolean aw;
    private byte[] key;
    private byte[] c;
    private int aB = 1;
    private String bi = "3DES168";
    private boolean aA = true;
    private int ay = 1;

    ak(String string, int n2, int n3, boolean bl) {
        this.bi = string;
        this.ay = 1;
        this.aA = this.ay == 1;
        this.aw = bl;
        this.aB = 1;
        this.key = new byte[di];
    }

    @Override
    public final void b(byte[] byArray, byte[] byArray2) {
        if (this.bi == "3DES112") {
            System.arraycopy(byArray, 0, this.key, 0, 16);
            System.arraycopy(byArray, 0, this.key, 16, 8);
        } else {
            System.arraycopy(byArray, 0, this.key, 0, di);
        }
        this.L();
    }

    private void L() {
        this.c = (byte[])bd.clone();
        z z2 = this.aw ? new am() : new al();
        z2.b(this.key, this.c);
        h h2 = h.a(this.aA ? 1 : 0, this.c, z2, dh);
        this.aC = af.a(this.aB, z2, dh, h2);
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
        return dh;
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

