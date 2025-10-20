/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.aj;
import oracle.net.aso.z;

final class al
implements z {
    private aj dj;

    al() {
    }

    @Override
    public final void b(byte[] byArray, byte[] byArray2) {
        this.dj = new aj();
        this.dj.b(byArray, byArray2);
    }

    @Override
    public final byte[] f(byte[] byArray) {
        return this.dj.f(byArray);
    }

    @Override
    public final byte[] g(byte[] byArray) {
        return this.dj.g(byArray);
    }

    @Override
    public final int a(byte[] byArray, int n2, int n3, byte[] byArray2) {
        throw new RuntimeException("Unsupported Operation");
    }

    @Override
    public final int b(byte[] byArray, int n2, int n3, byte[] byArray2) {
        throw new RuntimeException("Unsupported Operation");
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

