/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import oracle.net.aso.p;

final class ab
implements p {
    private MessageDigest bK = null;

    ab(String object) {
        try {
            String string = object;
            object = this;
            switch (string) {
                case "MD5": {
                    ((ab)object).bK = MessageDigest.getInstance("MD5");
                    break;
                }
                case "SHA1": {
                    ((ab)object).bK = MessageDigest.getInstance("SHA-1");
                    break;
                }
                case "SHA256": {
                    ((ab)object).bK = MessageDigest.getInstance("SHA-256");
                    break;
                }
                case "SHA384": {
                    ((ab)object).bK = MessageDigest.getInstance("SHA-384");
                    break;
                }
                case "SHA512": {
                    ((ab)object).bK = MessageDigest.getInstance("SHA-512");
                    break;
                }
                default: {
                    throw new RuntimeException("Unsupported Algorithm : " + string);
                }
            }
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
    }

    @Override
    public final int getDigestLength() {
        return this.bK.getDigestLength();
    }

    @Override
    public final void reset() {
        this.bK.reset();
    }

    @Override
    public final void update(byte[] byArray, int n2, int n3) {
        this.bK.update(byArray, 0, n3);
    }

    @Override
    public final int g(byte[] byArray, int n2) {
        try {
            return this.bK.digest(byArray, 0, byArray.length);
        }
        catch (DigestException digestException) {
            throw new RuntimeException(digestException);
        }
    }

    @Override
    public final String getProviderName() {
        return this.bK.getProvider().getName();
    }
}

