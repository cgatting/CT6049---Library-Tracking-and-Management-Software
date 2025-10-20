/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.b;
import oracle.net.aso.l;
import oracle.net.aso.q;

final class t
extends l {
    private byte[] bo = null;
    private int bp = 0;
    private byte[] bq = null;
    private int br = 0;
    private byte[] bs = null;
    private byte[] bt = null;
    private short bu;
    private short bv;
    private char[] bw = new char[257];
    private char[] by = new char[257];
    private byte[] bz;
    private int bA;
    private int bB;
    private int bC;
    private byte[] bD;

    private t(byte[] byArray, byte[] byArray2, short s2, short s3) {
        if (byArray != null && byArray2 != null) {
            this.bs = byArray;
            this.bt = byArray2;
            this.bv = s3;
            this.bu = s2;
            return;
        }
        int n2 = 40;
        byte[] byArray3 = byArray2;
        byArray2 = byArray;
        t t2 = this;
        this.bo = byArray2;
        t2.bp = byArray2 != null ? byArray2.length : 0;
        t2.bq = byArray3;
        t2.br = byArray3 != null ? byArray3.length : 0;
        short s4 = 40;
        for (int i2 = 0; i2 < l.M().length; ++i2) {
            if (s4 < l.M()[i2] || s4 > l.N()[i2]) continue;
            t2.bu = l.O()[i2];
            t2.bv = l.P()[i2];
            t2.bs = new byte[(t2.bv + 7) / 8];
            t2.bt = new byte[(t2.bv + 7) / 8];
            if (t2.bp << 3 >= t2.bv && t2.br << 3 >= t2.bv) {
                System.arraycopy(t2.bo, 0, t2.bs, 0, t2.bs.length);
                System.arraycopy(t2.bq, 0, t2.bt, 0, t2.bt.length);
                break;
            }
            System.arraycopy(l.Q()[i2], 0, t2.bs, 0, t2.bs.length);
            System.arraycopy(l.R()[i2], 0, t2.bt, 0, t2.bt.length);
            break;
        }
        if (t2.bs != null) {
            byte[] cfr_ignored_0 = t2.bt;
        }
    }

    @Override
    public final byte[] aa() {
        t t2 = this;
        char[] cArray = new char[257];
        char[] cArray2 = new char[257];
        byte[] byArray = new byte[512];
        int n2 = (short)(t2.bu + 7) / 8;
        int n3 = (short)(t2.bv + 7) / 8;
        t2.bA = (short)n3;
        t2.bB = t2.bv / 16 + 1;
        t2.bz = new byte[t2.bA];
        int n4 = n2;
        Object object = byArray;
        int n5 = n4;
        byte[] byArray2 = object;
        object = new q();
        for (int i2 = 0; i2 < n5; ++i2) {
            Object object2 = object;
            byArray2[i2] = (byte)((q)object2).ab();
        }
        byArray[0] = (byte)(byArray[0] & 255 >>> n2 - 8 * t2.bu);
        b.a(cArray, t2.bB, t2.bs, n3);
        b.a(t2.by, t2.bB, byArray, n2);
        b.a(t2.bw, t2.bB, t2.bt, n3);
        b.a(cArray2, cArray, t2.by, t2.bw, t2.bB);
        b.a(t2.bz, t2.bA, cArray2, t2.bB);
        return this.bz;
    }

    @Override
    public final byte[] f(byte[] object, int n2) {
        int n3 = n2;
        byte[] byArray = object;
        object = this;
        char[] cArray = new char[257];
        char[] cArray2 = new char[257];
        object.bC = object.bA;
        object.bD = new byte[object.bC];
        b.a(cArray, object.bB, byArray, n3);
        b.a(cArray2, cArray, object.by, object.bw, object.bB);
        b.a(object.bD, object.bC, cArray2, object.bB);
        return this.bD;
    }

    /* synthetic */ t(byte[] byArray, byte[] byArray2, short s2, short s3, byte by) {
        this(byArray, byArray2, s2, s3);
    }
}

