/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.security.Key;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import oracle.net.aso.z;

final class am
implements z {
    private byte[] bk = null;
    private Cipher bl = null;

    am() {
    }

    @Override
    public final void b(byte[] byArray, byte[] byArray2) {
        this.bk = byArray;
        this.bl = null;
    }

    private void a(boolean bl) {
        try {
            Object object = new DESedeKeySpec(this.bk);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede");
            object = secretKeyFactory.generateSecret((KeySpec)object);
            this.bl = Cipher.getInstance("DESede/ECB/NoPadding");
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
}

