/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Supplier;
import javax.net.ssl.HttpsURLConnection;
import oracle.jdbc.AccessToken;
import oracle.jdbc.driver.AccessTokenCache;
import oracle.jdbc.driver.DMSFactory;
import oracle.jdbc.driver.OpaqueAccessToken;
import oracle.jdbc.driver.OpaquePrivateKey;
import oracle.jdbc.internal.OpaqueString;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.runtime.TraceControllerImpl;
import oracle.net.nt.CustomSSLSocketFactory;
import oracle.sql.json.OracleJsonException;
import oracle.sql.json.OracleJsonFactory;
import oracle.sql.json.OracleJsonNumber;
import oracle.sql.json.OracleJsonValue;

public final class JsonWebToken
extends OpaqueAccessToken {
    private static final OracleJsonFactory JSON_FACTORY = new OracleJsonFactory();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("E, dd MMM uuuu HH:mm:ss z", Locale.US);
    private static final int CACHES_SIZE = 128;
    private static final Map<Builder, AccessTokenCache<JsonWebToken>> CACHES = Collections.synchronizedMap(new LinkedHashMap<Builder, AccessTokenCache<JsonWebToken>>(16, 0.75f, true){

        @Override
        protected boolean removeEldestEntry(Map.Entry<Builder, AccessTokenCache<JsonWebToken>> entry) {
            return this.size() > 128;
        }
    });

    private JsonWebToken(@Blind OpaqueString opaqueString, OffsetDateTime offsetDateTime, @Blind OpaquePrivateKey opaquePrivateKey) {
        super(opaqueString, offsetDateTime, opaquePrivateKey);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Blind
    static JsonWebToken fromOciFile(Path path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        char[] cArray = JsonWebToken.readTokenFile(path.resolve("token"));
        try {
            OffsetDateTime offsetDateTime = JsonWebToken.parseExp(cArray);
            JsonWebToken jsonWebToken = new JsonWebToken(OpaqueString.newOpaqueString(cArray), offsetDateTime, OpaquePrivateKey.fromPemFile(path.resolve("oci_db_key.pem")));
            return jsonWebToken;
        }
        finally {
            Arrays.fill(cArray, '\u0000');
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Blind
    static JsonWebToken fromFile(Path path) throws IOException {
        char[] cArray = Files.isDirectory(path, new LinkOption[0]) ? JsonWebToken.readTokenFile(path.resolve("token")) : JsonWebToken.readTokenFile(path);
        try {
            OffsetDateTime offsetDateTime = JsonWebToken.parseExp(cArray);
            JsonWebToken jsonWebToken = new JsonWebToken(OpaqueString.newOpaqueString(cArray), offsetDateTime, null);
            return jsonWebToken;
        }
        finally {
            Arrays.fill(cArray, '\u0000');
        }
    }

    static Builder requestBuilder() {
        return new Builder();
    }

    @Blind
    public static JsonWebToken createProofOfPossessionToken(@Blind char[] cArray, @Blind PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new JsonWebToken(OpaqueString.newOpaqueString((char[])cArray.clone()), JsonWebToken.parseExp(cArray), OpaquePrivateKey.fromPrivateKey(privateKey));
    }

    @Blind
    public static JsonWebToken createBearerToken(@Blind char[] cArray) {
        return new JsonWebToken(OpaqueString.newOpaqueString((char[])cArray.clone()), JsonWebToken.parseExp(cArray), null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Blind
    private static char[] readTokenFile(Path path) throws IOException {
        byte[] byArray = Files.readAllBytes(path);
        try {
            char[] cArray;
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(byArray));
            try {
                char[] cArray2 = new char[charBuffer.remaining()];
                charBuffer.get(cArray2);
                cArray = cArray2;
            }
            catch (Throwable throwable) {
                charBuffer.clear();
                charBuffer.put(new char[charBuffer.remaining()]);
                throw throwable;
            }
            charBuffer.clear();
            charBuffer.put(new char[charBuffer.remaining()]);
            return cArray;
        }
        finally {
            Arrays.fill(byArray, (byte)0);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static OffsetDateTime parseExp(@Blind char[] cArray) {
        OracleJsonValue oracleJsonValue;
        int n2;
        int n3;
        if (cArray.length > 16000) {
            throw new IllegalArgumentException("JWT of length " + cArray.length + " exceeds the maximum accepted length of 16,000 characters");
        }
        for (n3 = 0; n3 < cArray.length && cArray[n3] != '.'; ++n3) {
        }
        if (++n3 > cArray.length) {
            throw new IllegalArgumentException("Failed to identify payload of JWT");
        }
        for (n2 = n3; n2 < cArray.length && cArray[n2] != '.'; ++n2) {
        }
        if (n2 == cArray.length) {
            throw new IllegalArgumentException("Failed to identify payload of JWT");
        }
        byte[] byArray = new byte[n2 - n3];
        try {
            for (int i2 = 0; i2 < byArray.length; ++i2) {
                byArray[i2] = (byte)cArray[i2 + n3];
            }
            new TraceControllerImpl().suspend();
            try {
                byte[] byArray2 = Base64.getMimeDecoder().decode(byArray);
                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray2);){
                    oracleJsonValue = (OracleJsonValue)JSON_FACTORY.createJsonTextValue(byteArrayInputStream).asJsonObject().get("exp");
                }
                catch (ClassCastException | OracleJsonException runtimeException) {
                    throw new IllegalArgumentException("JWT payload is not JSON", runtimeException);
                }
                catch (IOException iOException) {
                    throw new IllegalArgumentException("Failed to read JWT payload", iOException);
                }
                finally {
                    Arrays.fill(byArray2, (byte)0);
                }
            }
            finally {
                new TraceControllerImpl().resume();
            }
        }
        finally {
            Arrays.fill(byArray, (byte)0);
        }
        if (oracleJsonValue == null) {
            throw new IllegalArgumentException("JWT is missing an exp claim");
        }
        if (!(oracleJsonValue instanceof OracleJsonNumber)) {
            throw new IllegalArgumentException("JWT has an exp claim with a non-numeric value of type: " + (Object)((Object)oracleJsonValue.getOracleJsonType()));
        }
        return Instant.ofEpochSecond(oracleJsonValue.asJsonNumber().longValue()).atOffset(ZoneOffset.UTC);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Blind
    private static JsonWebToken requestBearerToken(Builder builder) throws IOException {
        URL uRL = new URL(builder.endPoint);
        if (!"https".equalsIgnoreCase(uRL.getProtocol())) {
            throw new IllegalArgumentException("Protocol of endpoint is not https: " + uRL.getProtocol());
        }
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)uRL.openConnection();
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setRequestProperty("Accept", "application/json");
        httpsURLConnection.setSSLSocketFactory(CustomSSLSocketFactory.getSSLSocketFactory(builder.sqlNetOptions, new DMSFactory().new DMSFactory.DMSNoun()));
        httpsURLConnection.setRequestProperty("Date", ZonedDateTime.now(ZoneId.of("Z")).format(DATE_FORMATTER));
        httpsURLConnection.setRequestProperty("Authorization", JsonWebToken.createAuthorization(builder.user, builder.password));
        httpsURLConnection.setDoOutput(true);
        try (Closeable closeable = httpsURLConnection.getOutputStream();){
            ((OutputStream)closeable).write(String.format("{\"scope\": \"urn:oracle:db::id::%s\", \"tenantId\": \"%s\"}", builder.compartment == null ? "*" : (builder.database == null ? builder.compartment : builder.compartment + "::" + builder.database), builder.tenancy).getBytes(StandardCharsets.UTF_8));
            ((OutputStream)closeable).flush();
        }
        closeable = httpsURLConnection.getInputStream();
        var4_4 = null;
        try {
            JsonWebToken jsonWebToken;
            String string = JSON_FACTORY.createJsonTextValue((InputStream)closeable).asJsonObject().getString("token");
            if (string == null) {
                throw new IOException("JSON response does not contain a token");
            }
            char[] cArray = string.toCharArray();
            try {
                jsonWebToken = JsonWebToken.createBearerToken(cArray);
            }
            catch (Throwable throwable) {
                try {
                    Arrays.fill(cArray, '\u0000');
                    throw throwable;
                }
                catch (Throwable throwable2) {
                    var4_4 = throwable2;
                    throw throwable2;
                }
            }
            Arrays.fill(cArray, '\u0000');
            return jsonWebToken;
        }
        finally {
            if (closeable != null) {
                if (var4_4 != null) {
                    try {
                        ((InputStream)closeable).close();
                    }
                    catch (Throwable throwable) {
                        var4_4.addSuppressed(throwable);
                    }
                } else {
                    ((InputStream)closeable).close();
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Blind
    private static String createAuthorization(String string, @Blind OpaqueString opaqueString) {
        byte[] byArray = (string + ":").getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = opaqueString.map(cArray -> StandardCharsets.UTF_8.encode(CharBuffer.wrap(cArray)));
        try {
            String string2;
            byte[] byArray2 = new byte[byArray.length + byteBuffer.remaining()];
            try {
                System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
                byteBuffer.get(byArray2, byArray.length, byteBuffer.remaining());
                string2 = "Basic " + Base64.getEncoder().encodeToString(byArray2);
            }
            catch (Throwable throwable) {
                Arrays.fill(byArray2, (byte)0);
                throw throwable;
            }
            Arrays.fill(byArray2, (byte)0);
            return string2;
        }
        finally {
            byteBuffer.clear();
            byteBuffer.put(new byte[byteBuffer.remaining()]);
        }
    }

    public static AccessTokenCache<JsonWebToken> createCache(Supplier<? extends AccessToken> supplier) {
        return AccessTokenCache.create(() -> {
            AccessToken accessToken = (AccessToken)supplier.get();
            if (!(accessToken instanceof JsonWebToken)) {
                throw new IllegalArgumentException("token supplier has output an unrecognized object type: " + accessToken.getClass());
            }
            return (JsonWebToken)accessToken;
        });
    }

    static final class Builder {
        private String endPoint;
        private String tenancy;
        private String compartment;
        private String database;
        private String user;
        private OpaqueString password;
        private Properties sqlNetOptions;

        private Builder() {
        }

        Builder endPoint(String string) {
            this.endPoint = string;
            return this;
        }

        Builder tenancy(String string) {
            this.tenancy = string;
            return this;
        }

        Builder compartment(String string) {
            this.compartment = string;
            return this;
        }

        Builder database(String string) {
            this.database = string;
            return this;
        }

        Builder user(String string) {
            this.user = string;
            return this;
        }

        Builder password(@Blind OpaqueString opaqueString) {
            this.password = opaqueString;
            return this;
        }

        Builder sqlNetOptions(Properties properties) {
            this.sqlNetOptions = (Properties)properties.clone();
            return this;
        }

        @Blind
        JsonWebToken build() throws IOException {
            try {
                return (JsonWebToken)CACHES.computeIfAbsent(this, builder -> JsonWebToken.createCache(() -> {
                    try {
                        return JsonWebToken.requestBearerToken(this);
                    }
                    catch (IOException iOException) {
                        throw new UncheckedIOException(iOException);
                    }
                })).get();
            }
            catch (UncheckedIOException uncheckedIOException) {
                throw uncheckedIOException.getCause();
            }
        }

        public boolean equals(Object object) {
            return this == object || object instanceof Builder && Objects.equals(this.endPoint, ((Builder)object).endPoint) && Objects.equals(this.tenancy, ((Builder)object).tenancy) && Objects.equals(this.compartment, ((Builder)object).compartment) && Objects.equals(this.database, ((Builder)object).database) && Objects.equals(this.user, ((Builder)object).user) && Objects.equals(this.password, ((Builder)object).password) && Objects.equals(this.sqlNetOptions, ((Builder)object).sqlNetOptions);
        }

        public int hashCode() {
            return Objects.hash(this.endPoint, this.tenancy, this.compartment, this.database, this.user, this.password, this.sqlNetOptions);
        }
    }
}

