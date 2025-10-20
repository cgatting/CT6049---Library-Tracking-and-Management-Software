/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.i;
import oracle.net.aso.j;
import oracle.net.aso.m;
import oracle.net.aso.z;

final class n
implements z {
    private final i bj = new i();

    @Override
    public final void b(byte[] byArray, byte[] byArray2) {
        this.bj.k(byArray);
    }

    @Override
    public final byte[] f(byte[] byArray) {
        return this.bj.f(byArray);
    }

    @Override
    public final byte[] g(byte[] byArray) {
        return this.bj.g(byArray);
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

    static void l(byte[] byArray) {
        byte[] byArray2 = new byte[8];
        byte[] byArray3 = new byte[8];
        byte[] byArray4 = new byte[8];
        i i2 = new i();
        m.a(byArray2, byArray, j.be, 1, j.DES_KEY_SIZE);
        int[] nArray = i2.b(j.bg, (byte)0);
        n.a(i2, nArray, byArray2, byArray4);
        nArray = i2.b(j.bf, (byte)0);
        n.a(i2, nArray, byArray2, byArray3);
        m.a(byArray2, byArray4, byArray3, 2, j.DES_KEY_SIZE);
        m.a(byArray, byArray2, j.bh, 1, j.DES_KEY_SIZE);
    }

    private static void a(i object, int[] nArray, byte[] byArray, byte[] byArray2) {
        object = new int[2];
        i.a(byArray, (int[])object);
        i.a((int[])object, nArray);
        i.a((int[])object, byArray2);
    }
}

