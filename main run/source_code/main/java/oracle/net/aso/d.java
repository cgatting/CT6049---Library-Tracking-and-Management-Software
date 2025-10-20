/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.a;
import oracle.net.aso.z;

final class d
implements z {
    private a aD;
    private a aF;

    d() {
    }

    @Override
    public final void b(byte[] byArray, byte[] byArray2) {
        this.aD = new a();
        this.aF = new a();
        this.aD.h(byArray);
        this.aF.h(byArray);
    }

    @Override
    public final byte[] g(byte[] byArray) {
        byte[] byArray2 = new byte[byArray.length];
        this.aD.h(byArray, byArray2);
        return byArray2;
    }

    @Override
    public final byte[] f(byte[] byArray) {
        byte[] byArray2 = new byte[byArray.length];
        this.aF.i(byArray, byArray2);
        return byArray2;
    }

    @Override
    public final int a(byte[] byArray, int n2, int n3, byte[] byArray2) {
        throw new RuntimeException("Operation Not Supported");
    }

    @Override
    public final int b(byte[] byArray, int n2, int n3, byte[] byArray2) {
        throw new RuntimeException("Operation Not Supported");
    }

    @Override
    public final boolean j(int n2) {
        return false;
    }

    @Override
    public final String getProviderName() {
        return "JavaNet";
    }
}

