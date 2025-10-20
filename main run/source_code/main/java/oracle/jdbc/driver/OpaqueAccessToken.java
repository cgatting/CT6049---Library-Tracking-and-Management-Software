/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.time.OffsetDateTime;
import java.util.Objects;
import oracle.jdbc.AccessToken;
import oracle.jdbc.driver.OpaquePrivateKey;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.logging.annotations.Blind;

public class OpaqueAccessToken
implements AccessToken {
    private final OpaqueString token;
    private final OpaquePrivateKey privateKey;
    private final OffsetDateTime expiration;

    @Blind
    public static OpaqueAccessToken create(@Blind char[] cArray, OffsetDateTime offsetDateTime) {
        return new OpaqueAccessToken(OpaqueString.newOpaqueString((char[])cArray.clone()), offsetDateTime, null);
    }

    protected OpaqueAccessToken(@Blind OpaqueString opaqueString, OffsetDateTime offsetDateTime, @Blind OpaquePrivateKey opaquePrivateKey) {
        this.token = opaqueString;
        this.expiration = offsetDateTime;
        this.privateKey = opaquePrivateKey;
    }

    @Blind
    final OpaqueString token() {
        return this.token;
    }

    @Blind
    final OpaquePrivateKey privateKey() {
        return this.privateKey;
    }

    final OffsetDateTime expiration() {
        return this.expiration;
    }

    @Blind
    public final String toString() {
        return super.toString();
    }

    public boolean equals(Object object) {
        return object == this || object instanceof OpaqueAccessToken && Objects.equals(this.token(), ((OpaqueAccessToken)object).token()) && Objects.equals(this.privateKey(), ((OpaqueAccessToken)object).privateKey());
    }

    public int hashCode() {
        return Objects.hash(this.token(), this.privateKey);
    }
}

