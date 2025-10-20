/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import oracle.net.aso.l;

final class v
extends l {
    private final boolean bE;
    private BigInteger bF = null;
    private BigInteger bG = null;
    private short bH;
    private short bI;
    private KeyPair bJ;

    v(byte[] byArray, byte[] byArray2, short s2, short s3, boolean bl) {
        this.bE = bl;
        if (byArray != null && byArray2 != null) {
            this.bF = new BigInteger(1, byArray);
            this.bG = new BigInteger(1, byArray2);
            this.bI = s3;
            this.bH = s2;
            return;
        }
        this.a(40, byArray, byArray == null ? 0 : byArray.length, byArray2, byArray2 == null ? 0 : byArray2.length);
    }

    @Override
    public final byte[] aa() {
        try {
            AlgorithmParameterSpec algorithmParameterSpec;
            Object object;
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DiffieHellman");
            if (this.bE) {
                object = this;
                BigInteger bigInteger = ((v)object).bG.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2L));
                algorithmParameterSpec = new DSAParameterSpec(((v)object).bG, bigInteger, ((v)object).bF);
            } else {
                object = this;
                algorithmParameterSpec = new DHParameterSpec(((v)object).bG, ((v)object).bF, ((v)object).bH);
            }
            object = algorithmParameterSpec;
            keyPairGenerator.initialize((AlgorithmParameterSpec)object);
            this.bJ = keyPairGenerator.generateKeyPair();
            return ((DHPublicKey)this.bJ.getPublic()).getY().toByteArray();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public final byte[] f(byte[] object, int n2) {
        try {
            object = new DHPublicKeySpec(new BigInteger(1, (byte[])object), this.bG, this.bF);
            Object object2 = KeyFactory.getInstance("DiffieHellman");
            object = ((KeyFactory)object2).generatePublic((KeySpec)object);
            object2 = KeyAgreement.getInstance("DiffieHellman");
            ((KeyAgreement)object2).init(this.bJ.getPrivate());
            ((KeyAgreement)object2).doPhase((Key)object, true);
            return ((KeyAgreement)object2).generateSecret();
        }
        catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void a(int n2, byte[] byArray, int n3, byte[] byArray2, int n4) {
        byte[] byArray3 = null;
        byte[] byArray4 = null;
        for (n2 = 0; n2 < l.M().length; ++n2) {
            if (40 < l.M()[n2] || 40 > l.N()[n2]) continue;
            this.bH = l.O()[n2];
            this.bI = l.P()[n2];
            byArray3 = new byte[(this.bI + 7) / 8];
            byArray4 = new byte[(this.bI + 7) / 8];
            if (n3 << 3 >= this.bI && n4 << 3 >= this.bI) {
                System.arraycopy(byArray, 0, byArray3, 0, byArray3.length);
                System.arraycopy(byArray2, 0, byArray4, 0, byArray4.length);
                break;
            }
            System.arraycopy(l.Q()[n2], 0, byArray3, 0, byArray3.length);
            System.arraycopy(l.R()[n2], 0, byArray4, 0, byArray4.length);
            break;
        }
        if (byArray3 == null || byArray4 == null) {
            throw new RuntimeException("Unable to intialize base/modulus value");
        }
        this.bF = new BigInteger(1, byArray3);
        this.bG = new BigInteger(1, byArray4);
    }
}

