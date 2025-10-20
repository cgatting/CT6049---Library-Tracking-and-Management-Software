/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.af;
import oracle.net.aso.d;
import oracle.net.aso.e;
import oracle.net.aso.f;
import oracle.net.aso.h;
import oracle.net.aso.m;
import oracle.net.aso.z;

public final class c
implements m {
    private int ay = 1;
    private int az = 1;
    private boolean aA = this.ay == 1;
    private int aB = 1;
    private int keySize;
    private byte[] key;
    private byte[] c;
    private af aC;
    private boolean aw;
    private final boolean ax;

    public c(int n2, int n3, int n4, boolean bl, boolean bl2) {
        this.az = n3;
        this.aw = bl;
        this.ax = bl2;
        this.aB = n4;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    @Override
    public final void b(byte[] var1_1, byte[] var2_2) {
        block10: {
            if (this.az == 1) {
                this.keySize = 16;
            } else if (this.az == 2) {
                this.keySize = 24;
            } else if (this.az == 3) {
                this.keySize = 32;
            }
            if (var1_1 /* !! */ .length < this.keySize) {
                throw new f(102);
            }
            this.key = new byte[this.keySize];
            this.c = new byte[16];
            System.arraycopy(var1_1 /* !! */ , 0, this.key, 0, this.keySize);
            if (!this.ax) {
                if (var1_1 /* !! */ .length < 48) {
                    throw new f(102);
                }
                System.arraycopy(var1_1 /* !! */ , 32, this.c, 0, 16);
            }
            if (!this.aw) break block10;
            var1_1 /* !! */  = (byte[])this;
            v0 = new e(var1_1 /* !! */ .aA);
            var2_2 /* !! */  = (byte[])v0;
            v0.b(var1_1 /* !! */ .key, var1_1 /* !! */ .c);
            ** GOTO lbl-1000
        }
        var1_1 /* !! */  = (byte[])this;
        v1 = new d();
        var2_2 /* !! */  = (byte[])v1;
        v1.b(var1_1 /* !! */ .key, var1_1 /* !! */ .c);
        if (var1_1 /* !! */ .aA) {
            v2 = 1;
        } else lbl-1000:
        // 2 sources

        {
            v2 = 0;
        }
        var3_3 = h.a(v2, var1_1 /* !! */ .c, (z)var2_2 /* !! */ , 16);
        var1_1 /* !! */ .aC = af.a(var1_1 /* !! */ .aB, (z)var2_2 /* !! */ , 16, var3_3);
    }

    @Override
    public final byte[] f(byte[] byArray) {
        af af2 = this.aC;
        af2.bM.j(this.c);
        return this.aC.f(byArray);
    }

    @Override
    public final byte[] g(byte[] byArray) {
        af af2 = this.aC;
        af2.bM.j(this.c);
        return this.aC.g(byArray);
    }

    @Override
    public final int z() {
        return 16;
    }

    @Override
    public final void a(byte[] byArray, byte[] byArray2) {
        if (byArray == null || byArray2 == null) {
            return;
        }
        this.b(byArray, byArray2);
    }

    @Override
    public final String getProviderName() {
        if (this.aC == null) {
            return null;
        }
        af af2 = this.aC;
        return af2.bL.getProviderName();
    }
}

