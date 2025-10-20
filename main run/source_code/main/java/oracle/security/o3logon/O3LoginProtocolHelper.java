/*
 * Decompiled with CFR 0.152.
 */
package oracle.security.o3logon;

import java.security.SecureRandom;
import oracle.security.o3logon.a;
import oracle.security.o3logon.b;

public final class O3LoginProtocolHelper {
    private final byte[] cv;
    private final byte[] cw = new byte[8];
    private static long cx = System.currentTimeMillis();
    private static int cy = 0;
    private static b cz;
    private final boolean aw;

    public O3LoginProtocolHelper(boolean bl) {
        this.cv = null;
        this.aw = bl;
    }

    public O3LoginProtocolHelper(byte[] byArray, boolean bl) {
        this.cv = byArray;
        this.aw = bl;
    }

    public final byte[] getVerifier(String string, String string2) {
        return this.getVerifier(string, string2, true);
    }

    public final byte[] getVerifier(String string, String string2, Boolean bl) {
        if (cz == null) {
            cz = new b(this.aw);
        }
        return cz.a(string, string2, bl);
    }

    public final boolean authenticate(String object, String string) {
        try {
            Thread.sleep(cy * 1000);
        }
        catch (InterruptedException interruptedException) {}
        if (cz == null) {
            cz = new b(this.aw);
        }
        if (this.cv.length != ((Object)(object = (Object)cz.a((String)object, string))).length) {
            ++cy;
            return false;
        }
        for (int i2 = 0; i2 < ((Object)object).length; ++i2) {
            if (object[i2] == this.cv[i2]) continue;
            ++cy;
            return false;
        }
        return true;
    }

    public final byte[] getChallenge(byte[] object) {
        object = new SecureRandom((byte[])object);
        object.setSeed(cx += System.currentTimeMillis());
        object.setSeed(this.cv);
        object.nextBytes(this.cw);
        object = new a(this.aw);
        byte[] byArray = object.e(this.cv, this.cw);
        object = byArray;
        return byArray;
    }

    public final String getPassword(byte[] object) {
        Object object2 = new a(this.aw);
        byte by = object[((byte[])object).length - 1];
        byte[] byArray = new byte[((byte[])object).length - 1];
        System.arraycopy(object, 0, byArray, 0, byArray.length);
        try {
            object = ((a)object2).f(this.cw, byArray);
        }
        catch (Exception exception) {
            return null;
        }
        object2 = new byte[((byte[])object).length - by];
        System.arraycopy(object, 0, object2, 0, ((Object)object2).length);
        String string = new String((byte[])object2).toUpperCase();
        object = string;
        return string;
    }

    public static byte[] getResponse(String object, String string, byte[] byArray, boolean bl) {
        if (cz == null) {
            cz = new b(bl);
        }
        object = cz.a((String)object, string);
        a a2 = new a(bl);
        byArray = a2.f((byte[])object, byArray);
        byte[] byArray2 = string.getBytes();
        int n2 = byArray2.length % 8 > 0 ? (int)((byte)(8 - byArray2.length % 8)) : 0;
        object = new byte[byArray2.length + n2];
        System.arraycopy(byArray2, 0, object, 0, byArray2.length);
        byte[] byArray3 = a2.e(byArray, (byte[])object);
        object = byArray3;
        byArray = new byte[byArray3.length + 1];
        System.arraycopy(object, 0, byArray, 0, ((Object)object).length);
        byArray[byArray.length - 1] = n2;
        return byArray;
    }
}

