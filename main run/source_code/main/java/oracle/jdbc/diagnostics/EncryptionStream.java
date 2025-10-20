/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

final class EncryptionStream
implements AutoCloseable {
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 16;
    private static final String ENCRYPTION_TRANSFORMATION_STR = "AES/CBC/PKCS5Padding";
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String KEY_ENCRYPTION_TRANSFORMATION_STR = "RSA/ECB/PKCS1Padding";
    private final DataOutputStream outStream;
    private CipherOutputStream cipherOutStream;
    private byte[] sessionKey;
    private byte[] iv;
    private byte[] encryptedSessionKey;
    private byte[] encryptedIV;
    private final X509Certificate certificate;

    private EncryptionStream(OutputStream outputStream, X509Certificate x509Certificate) throws RuntimeException {
        try {
            this.outStream = new DataOutputStream(outputStream);
            this.certificate = x509Certificate;
            this.initializeKeyandIV();
            this.writeLogFileHeader();
            this.cipherOutStream = new CipherOutputStream(outputStream, this.newCipher());
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    static EncryptionStream newEncryptionStream(OutputStream outputStream, X509Certificate x509Certificate) throws RuntimeException {
        return new EncryptionStream(outputStream, x509Certificate);
    }

    private void writeLogFileHeader() throws IOException {
        this.outStream.writeInt(this.encryptedSessionKey.length);
        this.outStream.writeInt(this.encryptedIV.length);
        this.outStream.write(this.encryptedSessionKey);
        this.outStream.write(this.encryptedIV);
    }

    private Cipher newCipher() throws Exception {
        return null;
    }

    private void initializeKeyandIV() {
        throw new Error("EncryptionStream not supported");
    }

    public void write(byte[] byArray) throws IOException {
        this.cipherOutStream.write(byArray);
    }

    public void flush() throws IOException {
        this.cipherOutStream.flush();
        this.outStream.flush();
    }

    @Override
    public void close() throws IOException {
        this.cipherOutStream.flush();
        this.cipherOutStream.close();
        this.outStream.flush();
        this.outStream.close();
    }
}

