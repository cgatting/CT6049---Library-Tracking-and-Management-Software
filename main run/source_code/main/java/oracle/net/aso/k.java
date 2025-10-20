/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.security.NoSuchAlgorithmException;
import oracle.net.aso.aa;
import oracle.net.aso.ab;
import oracle.net.aso.b;
import oracle.net.aso.f;
import oracle.net.aso.m;
import oracle.net.aso.o;
import oracle.net.aso.p;
import oracle.net.aso.r;
import oracle.net.aso.u;
import oracle.net.aso.w;
import oracle.net.aso.y;

public final class k {
    private final m bm;
    private byte[] aq;
    private final String bx;
    private p bn;

    public k(byte[] byArray, byte[] byArray2, String string, boolean bl, boolean bl2) {
        this.bx = string;
        this.bn = bl ? new ab(string) : k.c(string);
        k k2 = this;
        this.bm = k2.bx == "SHA256" || k2.bx == "SHA384" || k2.bx == "SHA512" ? new b(byArray, byArray2, bl, bl2) : new r(byArray, byArray2, bl, string.equals("MD5") ? true : bl2);
        this.aq = new byte[this.size()];
    }

    public final boolean c(byte[] byArray, byte[] byArray2) {
        byte[] byArray3 = this.bm.f(this.aq);
        this.bn.reset();
        this.bn.update(byArray, 0, byArray.length);
        this.bn.update(byArray3, 0, byArray3.length);
        byArray = new byte[this.size()];
        this.bn.g(byArray, 0);
        boolean bl = false;
        for (int i2 = 0; i2 < this.size(); ++i2) {
            if (byArray[i2] == byArray2[i2]) continue;
            bl = true;
            break;
        }
        return bl;
    }

    public final byte[] e(byte[] byArray, int n2) {
        if (byArray.length < n2) {
            return null;
        }
        byte[] byArray2 = this.bm.g(this.aq);
        this.bn.reset();
        this.bn.update(byArray, 0, n2);
        this.bn.update(byArray2, 0, byArray2.length);
        this.bn.g(byArray2, 0);
        return byArray2;
    }

    public final int d(byte[] byArray, byte[] byArray2) {
        try {
            this.bm.a(byArray, byArray2);
        }
        catch (f f2) {
            throw new RuntimeException(f2);
        }
        return 0;
    }

    public final void Z() {
        this.bm.A();
    }

    public final int size() {
        return this.bn.getDigestLength();
    }

    public final String getProviderName() {
        return this.bn.getProviderName();
    }

    private static p c(String string) {
        p p2 = null;
        try {
            switch (string) {
                case "MD5": {
                    p2 = new o();
                    break;
                }
                case "SHA1": {
                    p2 = new u();
                    break;
                }
                case "SHA256": {
                    p2 = new w();
                    break;
                }
                case "SHA384": {
                    p2 = new y();
                    break;
                }
                case "SHA512": {
                    p2 = new aa();
                }
            }
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
        if (p2 != null) {
            return p2;
        }
        throw new RuntimeException("Unsupported Algorithm : " + string);
    }
}

