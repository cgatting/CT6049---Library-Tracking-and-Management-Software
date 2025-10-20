/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import oracle.jdbc.driver.BuildInfo;
import oracle.net.ns.NetException;

public abstract class AnoComm {
    private static long s;

    protected abstract void flush();

    protected abstract void a(short var1);

    protected final void a(int n2) {
        this.a(2, 3);
        this.c(n2);
    }

    protected final void a(long l2) {
        this.a(4, 4);
        this.b(l2);
    }

    protected final void a(int[] nArray) {
        this.a(10 + (nArray.length << 1), 1);
        this.b(-559038737L);
        this.c(3);
        this.b((long)nArray.length);
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            this.c(nArray[i2] & 0xFFFF);
        }
    }

    protected final void b(int n2) {
        this.a(2, 6);
        this.c(n2);
    }

    protected final void e() {
        this.a(4, 5);
        this.b(s);
    }

    protected abstract void a(String var1);

    protected abstract void d(byte[] var1);

    protected final void a(int n2, int n3) {
        int n4 = n3;
        int n5 = n2;
        if (n4 < 0 || n4 > 7) {
            throw new NetException(313);
        }
        switch (n4) {
            case 0: 
            case 1: {
                break;
            }
            case 2: {
                if (n5 <= 1) break;
                throw new NetException(312);
            }
            case 3: 
            case 6: {
                if (n5 <= 2) break;
                throw new NetException(312);
            }
            case 4: 
            case 5: {
                if (n5 <= 4) break;
                throw new NetException(312);
            }
            case 7: {
                if (n5 >= 10) break;
                throw new NetException(312);
            }
            default: {
                throw new NetException(313);
            }
        }
        this.c(n2);
        this.c(n3);
    }

    protected final void f() {
        this.b(s);
    }

    protected abstract void b(short var1);

    protected abstract void c(int var1);

    protected abstract void b(long var1);

    protected final short g() {
        this.d(2);
        short s2 = this.o();
        return s2;
    }

    protected final int h() {
        this.d(3);
        int n2 = this.readUB2();
        return n2 & 0xFFFF;
    }

    protected final long i() {
        this.d(4);
        long l2 = this.readUB4();
        return l2;
    }

    protected final int[] j() {
        this.d(1);
        long l2 = this.readUB4();
        int n2 = this.readUB2();
        long l3 = this.readUB4();
        int[] nArray = new int[(int)l3];
        if (l2 != -559038737L || n2 != 3) {
            throw new NetException(310);
        }
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            nArray[i2] = this.readUB2();
        }
        return nArray;
    }

    protected final int k() {
        this.d(6);
        return this.readUB2();
    }

    protected final long l() {
        this.d(5);
        return this.readUB4();
    }

    protected final String m() {
        int n2 = this.d(0);
        return new String(this.e(n2));
    }

    protected final byte[] n() {
        int n2 = this.d(1);
        return this.e(n2);
    }

    protected abstract short o();

    protected final int readUB2() {
        byte[] byArray = new byte[2];
        int n2 = (int)this.e(byArray);
        return n2 & 0xFFFF;
    }

    protected final long readUB4() {
        byte[] byArray = new byte[4];
        long l2 = this.e(byArray);
        return l2;
    }

    private int d(int n2) {
        int n3 = this.readUB2();
        int n4 = this.readUB2();
        int n5 = n2;
        n2 = n3;
        if (n4 < 0 || n4 > 7) {
            throw new NetException(313);
        }
        if (n4 != n5) {
            throw new NetException(314);
        }
        switch (n5) {
            case 0: 
            case 1: {
                break;
            }
            case 2: {
                if (n2 <= 1) break;
                throw new NetException(312);
            }
            case 3: 
            case 6: {
                if (n2 <= 2) break;
                throw new NetException(312);
            }
            case 4: 
            case 5: {
                if (n2 <= 4) break;
                throw new NetException(312);
            }
            case 7: {
                if (n2 >= 10) break;
                throw new NetException(312);
            }
            default: {
                throw new NetException(313);
            }
        }
        return n3;
    }

    protected abstract byte[] e(int var1);

    protected abstract long e(byte[] var1);

    static byte a(int n2, byte[] byArray) {
        byte by = 0;
        for (int i2 = byArray.length - 1; i2 >= 0; --i2) {
            byte by2 = by;
            by = (byte)(by + 1);
            byArray[by2] = (byte)(n2 >>> i2 * 8);
        }
        return by;
    }

    static {
        String[] stringArray = BuildInfo.getDriverVersion().split("\\.");
        int n2 = Integer.parseInt(stringArray[2]);
        n2 = n2 == 0 ? 1 : n2;
        s = Integer.parseInt(stringArray[0]) << 24 | Integer.parseInt(stringArray[1]) << 20 | n2 << 12 | Integer.parseInt(stringArray[3]) << 8 | Integer.parseInt(stringArray[4]);
    }
}

