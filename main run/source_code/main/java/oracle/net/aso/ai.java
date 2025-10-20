/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import oracle.net.aso.ag;
import oracle.net.aso.r;

final class ai
implements ag {
    private Cipher cF;

    private ai(r r2) {
    }

    @Override
    public final void a(int n2, byte[] object, int n3) {
        try {
            this.cF = Cipher.getInstance("RC4");
            object = new SecretKeySpec((byte[])object, "RC4");
            this.cF.init(n2, (Key)object);
            return;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public final void a(byte[] byArray, byte[] byArray2, int n2) {
        try {
            this.cF.update(byArray2, 0, n2, byArray);
            return;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public final String getProviderName() {
        if (this.cF != null) {
            return this.cF.getProvider().getName();
        }
        return null;
    }

    /* synthetic */ ai(r r2, byte by) {
        this(r2);
    }
}

