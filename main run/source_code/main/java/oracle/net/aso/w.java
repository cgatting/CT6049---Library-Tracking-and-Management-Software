/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.security.DigestException;
import java.security.MessageDigest;
import oracle.net.aso.p;

final class w
implements p {
    private MessageDigest cs = MessageDigest.getInstance("SHA-256");

    w() {
        this.reset();
    }

    @Override
    public final int getDigestLength() {
        return this.cs.getDigestLength();
    }

    @Override
    public final void update(byte[] byArray, int n2, int n3) {
        this.cs.update(byArray, 0, n3);
    }

    @Override
    public final void reset() {
        this.cs.reset();
    }

    @Override
    public final int g(byte[] byArray, int n2) {
        n2 = 0;
        try {
            n2 = this.cs.digest(byArray, 0, this.cs.getDigestLength());
        }
        catch (DigestException digestException) {}
        return n2;
    }
}

