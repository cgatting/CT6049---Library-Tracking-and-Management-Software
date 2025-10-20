/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.ag;
import oracle.net.aso.ah;
import oracle.net.aso.ai;
import oracle.net.aso.f;
import oracle.net.aso.m;

final class r
implements m {
    private static final byte[] bN = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
    private ag ch;
    private ag ci;
    private ag cj;
    private boolean ck = true;
    private boolean W = false;
    private final boolean aw;
    private int cl = 40;
    private boolean ax = true;
    private byte[] cm = null;

    r(int n2, boolean bl) {
        switch (n2) {
            case 40: 
            case 56: 
            case 128: 
            case 256: {
                this.cl = n2;
                break;
            }
            default: {
                throw new f(100);
            }
        }
        this.aw = bl;
    }

    r(byte[] byArray, byte[] byArray2, boolean bl, boolean bl2) {
        this.ax = bl2;
        this.cl = bl2 ? 40 : 128;
        this.W = true;
        this.ck = false;
        this.aw = bl;
        try {
            this.b(byArray, byArray2);
            return;
        }
        catch (f f2) {
            throw new RuntimeException(f2);
        }
    }

    @Override
    public final void b(byte[] byArray, byte[] byArray2) {
        if (this.aw) {
            this.ch = new ai(this, 0);
            this.ci = new ai(this, 0);
            this.cj = new ai(this, 0);
        } else {
            this.ch = new ah(this, 0);
            this.ci = new ah(this, 0);
            this.cj = new ah(this, 0);
        }
        this.a(byArray, byArray2);
    }

    @Override
    public final byte[] f(byte[] byArray) {
        byte[] byArray2;
        if (this.ck) {
            byArray2 = new byte[byArray.length - 1];
            this.cj.a(byArray2, byArray, byArray.length - 1);
        } else {
            byArray2 = new byte[byArray.length];
            this.cj.a(byArray2, byArray, byArray.length);
        }
        return byArray2;
    }

    @Override
    public final byte[] g(byte[] byArray) {
        byte[] byArray2;
        if (this.ck) {
            byArray2 = new byte[byArray.length + 1];
            this.ci.a(byArray2, byArray, byArray.length);
            byArray2[byArray.length] = 0;
        } else {
            byArray2 = new byte[byArray.length];
            this.ci.a(byArray2, byArray, byArray.length);
        }
        return byArray2;
    }

    @Override
    public final String getProviderName() {
        if (this.ci != null) {
            return this.ci.getProviderName();
        }
        return null;
    }

    @Override
    public final int z() {
        return 1;
    }

    @Override
    public final void a(byte[] byArray, byte[] byArray2) {
        byte[] byArray3;
        int n2;
        int n3;
        if (byArray == null && byArray2 == null) {
            this.A();
            return;
        }
        if (this.ax) {
            if (this.cm == null) {
                this.cm = (byte[])byArray.clone();
            } else {
                byArray = this.cm;
            }
        }
        if (byArray.length < (n3 = this.cl / 8)) {
            throw new f(102);
        }
        int n4 = n2 = this.W ? -1 : 123;
        if (!this.W || this.ax) {
            int n5 = byArray2 == null ? 0 : byArray2.length;
            byArray3 = new byte[n3 + 1 + n5];
            System.arraycopy(byArray, byArray.length - n3, byArray3, 0, n3);
            byArray3[n3] = n2;
            if (byArray2 != null) {
                System.arraycopy(byArray2, 0, byArray3, n3 + 1, byArray2.length);
            }
        } else {
            byArray3 = new byte[n3 + 32];
            System.arraycopy(byArray, 0, byArray3, 0, n3);
            byArray3[n3 - 1] = n2;
            System.arraycopy(byArray, 32, byArray3, n3, 32);
        }
        this.ch.a(1, byArray3, byArray3.length);
        this.A();
    }

    @Override
    public final void A() {
        int n2 = this.cl / 8;
        if (!this.W) {
            byte[] byArray = new byte[n2];
            this.ch.a(byArray, bN, n2);
            if (this.ck) {
                int n3 = n2 - 1;
                byArray[n3] = (byte)(byArray[n3] ^ 0xAA);
            }
            this.cj.a(2, byArray, n2);
            int n4 = n2 - 1;
            byArray[n4] = (byte)(byArray[n4] ^ 0xAA);
            this.ci.a(1, byArray, n2);
            return;
        }
        byte[] byArray = this.ax ? new byte[n2 + 1] : new byte[n2];
        this.ch.a(byArray, bN, n2);
        byArray[byArray.length - 1] = -76;
        this.cj.a(2, byArray, byArray.length);
        byArray[byArray.length - 1] = 90;
        this.ci.a(1, byArray, byArray.length);
    }
}

