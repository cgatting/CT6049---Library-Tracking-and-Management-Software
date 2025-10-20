/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.security.Key;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import oracle.net.aso.j;
import oracle.net.aso.m;
import oracle.net.aso.z;

final class s
implements z {
    private byte[] bk = null;
    private Cipher bl = null;

    s() {
    }

    @Override
    public final void b(byte[] byArray, byte[] byArray2) {
        this.bk = byArray;
        this.bl = null;
    }

    private void a(boolean bl) {
        try {
            Object object = new DESKeySpec(this.bk);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            object = secretKeyFactory.generateSecret((KeySpec)object);
            this.bl = Cipher.getInstance("DES/ECB/NoPadding");
            if (!bl) {
                this.bl.init(2, (Key)object);
                return;
            }
            this.bl.init(1, (Key)object);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public final byte[] f(byte[] byArray) {
        if (this.bl == null) {
            this.a(false);
        }
        try {
            return this.bl.doFinal(byArray);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public final byte[] g(byte[] byArray) {
        if (this.bl == null) {
            this.a(true);
        }
        try {
            return this.bl.doFinal(byArray);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
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
        if (this.bl != null) {
            return this.bl.getProvider().getName();
        }
        return null;
    }

    static void l(byte[] byArray) {
        byte[] byArray2 = new byte[8];
        byte[] byArray3 = new byte[8];
        byte[] byArray4 = new byte[8];
        m.a(byArray2, byArray, j.be, 1, j.DES_KEY_SIZE);
        s.a(j.bg, byArray2, byArray4);
        s.a(j.bf, byArray2, byArray3);
        m.a(byArray2, byArray4, byArray3, 2, j.DES_KEY_SIZE);
        m.a(byArray, byArray2, j.bh, 1, j.DES_KEY_SIZE);
    }

    private static void a(byte[] object, byte[] byArray, byte[] byArray2) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            object = new DESKeySpec((byte[])object);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            object = secretKeyFactory.generateSecret((KeySpec)object);
            cipher.init(1, (Key)object);
            cipher.doFinal(byArray, 0, 8, byArray2, 0);
            return;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
            return;
        }
    }
}

