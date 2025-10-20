/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import oracle.net.aso.ak;
import oracle.net.aso.c;
import oracle.net.aso.j;
import oracle.net.aso.r;
import oracle.net.ns.NetException;

public interface m {
    public static m a(String object, boolean bl, byte[] byArray, byte[] byArray2, boolean bl2) {
        int n2 = -1;
        switch (((String)object).hashCode()) {
            case -1883858920: {
                if (!((String)object).equals("RC4_40")) break;
                n2 = 0;
                break;
            }
            case -1883858883: {
                if (!((String)object).equals("RC4_56")) break;
                n2 = 1;
                break;
            }
            case 1729912859: {
                if (!((String)object).equals("RC4_128")) break;
                n2 = 2;
                break;
            }
            case 1729913911: {
                if (!((String)object).equals("RC4_256")) break;
                n2 = 3;
                break;
            }
            case 2013029397: {
                if (!((String)object).equals("DES40C")) break;
                n2 = 4;
                break;
            }
            case 2013030544: {
                if (!((String)object).equals("DES56C")) break;
                n2 = 5;
                break;
            }
            case 31074003: {
                if (!((String)object).equals("3DES112")) break;
                n2 = 6;
                break;
            }
            case 31074164: {
                if (!((String)object).equals("3DES168")) break;
                n2 = 7;
                break;
            }
            case 1927139112: {
                if (!((String)object).equals("AES128")) break;
                n2 = 8;
                break;
            }
            case 1927139323: {
                if (!((String)object).equals("AES192")) break;
                n2 = 9;
                break;
            }
            case 1927140164: {
                if (!((String)object).equals("AES256")) break;
                n2 = 10;
            }
        }
        switch (n2) {
            case 0: {
                object = new r(40, bl);
                break;
            }
            case 1: {
                object = new r(56, bl);
                break;
            }
            case 2: {
                object = new r(128, bl);
                break;
            }
            case 3: {
                object = new r(256, bl);
                break;
            }
            case 4: {
                object = new j("DES40C", 1, 1, bl);
                break;
            }
            case 5: {
                object = new j("DES56C", 1, 1, bl);
                break;
            }
            case 6: {
                object = new ak("3DES112", 1, 1, bl);
                break;
            }
            case 7: {
                object = new ak("3DES168", 1, 1, bl);
                break;
            }
            case 8: {
                object = new c(1, 1, 1, bl, bl2);
                break;
            }
            case 9: {
                object = new c(1, 2, 1, bl, bl2);
                break;
            }
            case 10: {
                object = new c(1, 3, 1, bl, bl2);
                break;
            }
            default: {
                throw new NetException(317);
            }
        }
        object.b(byArray, byArray2);
        return object;
    }

    public static void a(byte[] byArray, byte[] byArray2, byte[] byArray3, int n2, int n3) {
        if (n2 == 1) {
            for (n2 = 0; n2 < n3; ++n2) {
                byArray[n2] = (byte)(byArray2[n2] & byArray3[n2]);
            }
            return;
        }
        if (n2 == 2) {
            for (n2 = 0; n2 < n3; ++n2) {
                byArray[n2] = (byte)(byArray2[n2] ^ byArray3[n2]);
            }
            return;
        }
        if (n2 == 3) {
            System.arraycopy(byArray2, 0, byArray, 0, n3);
        }
    }

    public void b(byte[] var1, byte[] var2);

    public byte[] f(byte[] var1);

    public byte[] g(byte[] var1);

    public int z();

    public void a(byte[] var1, byte[] var2);

    public String getProviderName();

    default public void A() {
        throw new RuntimeException("Unsupported Operation");
    }
}

