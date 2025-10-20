/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import oracle.net.aso.f;
import oracle.net.aso.z;

final class e
implements z {
    private Cipher aG;
    private Cipher aH;
    private final boolean aA;
    private boolean aI = false;
    private int keySize = 0;

    e(boolean bl) {
        this.aA = bl;
    }

    e(boolean bl, boolean bl2) {
        this.aA = true;
        this.aI = true;
    }

    @Override
    public final void b(byte[] object, byte[] object2) {
        try {
            this.keySize = ((byte[])object).length;
            String string = this.aA ? "CBC" : "ECB";
            string = "AES/" + string + "/NoPadding";
            this.aG = Cipher.getInstance(string);
            this.aH = Cipher.getInstance(string);
            object = new SecretKeySpec((byte[])object, "AES");
            if (!this.aA) {
                this.aG.init(1, (Key)object);
                this.aH.init(2, (Key)object);
                return;
            }
            object2 = new IvParameterSpec((byte[])object2);
            this.aG.init(1, (Key)object, (AlgorithmParameterSpec)object2);
            this.aH.init(2, (Key)object, (AlgorithmParameterSpec)object2);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public final byte[] g(byte[] byArray) {
        try {
            if (this.aI) {
                return this.aG.update(byArray);
            }
            return this.aG.doFinal(byArray);
        }
        catch (Exception exception) {
            throw new f(107, (Throwable)exception);
        }
    }

    @Override
    public final byte[] f(byte[] byArray) {
        try {
            if (this.aI) {
                return this.aH.update(byArray);
            }
            return this.aH.doFinal(byArray);
        }
        catch (Exception exception) {
            throw new f(107, (Throwable)exception);
        }
    }

    @Override
    public final int a(byte[] byArray, int n2, int n3, byte[] byArray2) {
        try {
            if (this.aI) {
                return this.aG.update(byArray, 0, n3, byArray2);
            }
            return this.aG.doFinal(byArray, 0, n3, byArray2);
        }
        catch (Exception exception) {
            throw new f(107, (Throwable)exception);
        }
    }

    @Override
    public final int b(byte[] byArray, int n2, int n3, byte[] byArray2) {
        try {
            if (this.aI) {
                return this.aH.update(byArray, 0, n3, byArray2);
            }
            return this.aH.doFinal(byArray, 0, n3, byArray2);
        }
        catch (Exception exception) {
            throw new f(107, (Throwable)exception);
        }
    }

    @Override
    public final boolean j(int n2) {
        return n2 >= this.keySize;
    }

    @Override
    public final String getProviderName() {
        if (this.aG != null) {
            return this.aG.getProvider().getName();
        }
        return null;
    }
}

