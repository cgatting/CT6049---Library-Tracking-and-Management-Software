/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.function.Function;
import javax.security.auth.DestroyFailedException;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.logging.annotations.Blind;

class OpaquePrivateKey {
    private final OpaqueString opaqueEncoding;
    private static final byte[] BEGIN_PRIVATE_KEY_UTF8 = "-----BEGIN PRIVATE KEY-----".getBytes(StandardCharsets.UTF_8);
    private static final byte[] END_PRIVATE_KEY_UTF8 = "-----END PRIVATE KEY-----".getBytes(StandardCharsets.UTF_8);
    private static final byte[] LINE_SEPARATOR = "\n".getBytes(StandardCharsets.UTF_8);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static OpaquePrivateKey fromPemFile(Path path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byArray = Files.readAllBytes(path);
        try {
            OpaquePrivateKey opaquePrivateKey;
            int n2 = OpaquePrivateKey.findTag(byArray, 0, BEGIN_PRIVATE_KEY_UTF8);
            if (n2 == -1) {
                throw new IOException(path + " does not contain " + new String(BEGIN_PRIVATE_KEY_UTF8, StandardCharsets.UTF_8));
            }
            int n3 = n2 + BEGIN_PRIVATE_KEY_UTF8.length + LINE_SEPARATOR.length;
            int n4 = OpaquePrivateKey.findTag(byArray, n3, END_PRIVATE_KEY_UTF8);
            if (n4 == -1) {
                throw new IOException(path + " does not contain " + new String(END_PRIVATE_KEY_UTF8, StandardCharsets.UTF_8));
            }
            byte[] byArray2 = Arrays.copyOfRange(byArray, n3, n4);
            try {
                opaquePrivateKey = new OpaquePrivateKey(OpaquePrivateKey.decodeBase64Key(byArray2));
            }
            catch (Throwable throwable) {
                Arrays.fill(byArray2, (byte)0);
                throw throwable;
            }
            Arrays.fill(byArray2, (byte)0);
            return opaquePrivateKey;
        }
        finally {
            Arrays.fill(byArray, (byte)0);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Blind
    private static OpaqueString decodeBase64Key(byte[] byArray) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byArray2 = Base64.getMimeDecoder().decode(byArray);
        try {
            OpaqueString opaqueString;
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(byArray2));
            try {
                opaqueString = OpaquePrivateKey.encodeKey(privateKey);
            }
            catch (Throwable throwable) {
                OpaquePrivateKey.tryDestroyKey(privateKey);
                throw throwable;
            }
            OpaquePrivateKey.tryDestroyKey(privateKey);
            return opaqueString;
        }
        finally {
            Arrays.fill(byArray2, (byte)0);
        }
    }

    private OpaquePrivateKey(@Blind OpaqueString opaqueString) {
        this.opaqueEncoding = opaqueString;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Blind
    public static OpaqueString encodeKey(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byArray = KeyFactory.getInstance("RSA").getKeySpec(privateKey, PKCS8EncodedKeySpec.class).getEncoded();
        try {
            char[] cArray = new char[byArray.length];
            for (int i2 = 0; i2 < byArray.length; ++i2) {
                cArray[i2] = (char)byArray[i2];
            }
            OpaqueString opaqueString = OpaqueString.newOpaqueString(cArray);
            return opaqueString;
        }
        finally {
            Arrays.fill(byArray, (byte)0);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Blind
    private PrivateKey decodeKey(OpaqueString opaqueString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] cArray = opaqueString.getChars();
        try {
            PrivateKey privateKey;
            byte[] byArray = new byte[cArray.length];
            try {
                for (int i2 = 0; i2 < cArray.length; ++i2) {
                    byArray[i2] = (byte)cArray[i2];
                }
                privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(byArray));
            }
            catch (Throwable throwable) {
                Arrays.fill(byArray, (byte)0);
                throw throwable;
            }
            Arrays.fill(byArray, (byte)0);
            return privateKey;
        }
        finally {
            Arrays.fill(cArray, '\u0000');
        }
    }

    private static void tryDestroyKey(PrivateKey privateKey) {
        try {
            privateKey.destroy();
        }
        catch (DestroyFailedException destroyFailedException) {
            // empty catch block
        }
    }

    private static int findTag(byte[] byArray, int n2, byte[] byArray2) {
        while (n2 < byArray.length) {
            if (OpaquePrivateKey.arrayEquals(byArray, n2, byArray2)) {
                return n2;
            }
            int n3 = OpaquePrivateKey.arrayIndexOf(byArray, n2, LINE_SEPARATOR);
            if (n3 == -1) break;
            n2 = n3 + 1;
        }
        return -1;
    }

    private static boolean arrayEquals(byte[] byArray, int n2, byte[] byArray2) {
        if (n2 + byArray2.length > byArray.length) {
            return false;
        }
        for (int i2 = 0; i2 < byArray2.length; ++i2) {
            if (byArray[i2 + n2] == byArray2[i2]) continue;
            return false;
        }
        return true;
    }

    private static int arrayIndexOf(byte[] byArray, int n2, byte[] byArray2) {
        for (int i2 = n2; i2 < byArray.length; ++i2) {
            if (!OpaquePrivateKey.arrayEquals(byArray, i2, byArray2)) continue;
            return i2;
        }
        return -1;
    }

    @Blind
    static OpaquePrivateKey fromPrivateKey(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        OpaqueString opaqueString = OpaquePrivateKey.encodeKey(privateKey);
        return new OpaquePrivateKey(opaqueString);
    }

    public boolean equals(Object object) {
        return object == this || object instanceof OpaquePrivateKey && Objects.equals(this.opaqueEncoding, ((OpaquePrivateKey)object).opaqueEncoding);
    }

    public int hashCode() {
        return Objects.hash(this.opaqueEncoding);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Blind
    <T, E extends Throwable> T map(ThrowingFunction<PrivateKey, T, E> throwingFunction) throws E, NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey privateKey = this.decodeKey(this.opaqueEncoding);
        try {
            T t2 = throwingFunction.applyOrThrow(privateKey);
            return t2;
        }
        finally {
            OpaquePrivateKey.tryDestroyKey(privateKey);
        }
    }

    static interface ThrowingFunction<T, R, E extends Throwable>
    extends Function<T, R> {
        public R applyOrThrow(T var1) throws E;

        @Override
        default public R apply(T t2) {
            try {
                return this.applyOrThrow(t2);
            }
            catch (Throwable throwable) {
                if (throwable instanceof Error) {
                    throw (Error)throwable;
                }
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException)throwable;
                }
                throw new RuntimeException(throwable);
            }
        }
    }
}

