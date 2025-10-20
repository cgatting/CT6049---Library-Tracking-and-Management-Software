/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.function.Supplier;
import oracle.jdbc.driver.JsonWebToken;
import oracle.jdbc.logging.annotations.Blind;

public interface AccessToken {
    public static AccessToken createJsonWebToken(@Blind char[] cArray, @Blind PrivateKey privateKey) {
        try {
            return JsonWebToken.createProofOfPossessionToken(Objects.requireNonNull(cArray, "token is null"), Objects.requireNonNull(privateKey, "privateKey is null"));
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new IllegalStateException(noSuchAlgorithmException);
        }
        catch (InvalidKeySpecException invalidKeySpecException) {
            throw new IllegalArgumentException(invalidKeySpecException);
        }
    }

    public static AccessToken createJsonWebToken(@Blind char[] cArray) {
        return JsonWebToken.createBearerToken(Objects.requireNonNull(cArray, "token is null"));
    }

    public static Supplier<? extends AccessToken> createJsonWebTokenCache(Supplier<? extends AccessToken> supplier) {
        return JsonWebToken.createCache(Objects.requireNonNull(supplier, "tokenSupplier is null"));
    }
}

