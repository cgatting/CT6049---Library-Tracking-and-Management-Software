/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import oracle.jdbc.diagnostics.EncryptionStream;

final class SecureLogHandler
extends Handler {
    private X509Certificate certificate;
    private final String dumpFileLocation;
    private final String certFile;
    private final String certFilePwd;
    private EncryptionStream encStream;
    private final String loggerId;

    SecureLogHandler(String string, String string2, String string3, String string4) throws RuntimeException {
        this.certFilePwd = string2;
        this.certFile = string;
        this.dumpFileLocation = string3;
        this.loggerId = string4;
        this.doFilePermissionChecks();
        this.setFormatter(new SimpleFormatter());
        this.setFilter(logRecord -> true);
    }

    void doFilePermissionChecks() throws RuntimeException {
        File file = new File(this.dumpFileLocation);
        if (!file.exists()) {
            throw new RuntimeException("Secure Log Initialization failed : Dump File Location " + this.dumpFileLocation + " does not exist.");
        }
        if (!file.canWrite()) {
            throw new RuntimeException("Secure Log Initialization failed : Dump File Location " + this.dumpFileLocation + " does not have access to write.");
        }
        this.checkPublicAccess(this.dumpFileLocation);
        File file2 = new File(this.certFile);
        if (!file2.exists()) {
            throw new RuntimeException("Secure Log Initialization failed : Certificate file " + this.certFile + " does not exist.");
        }
        try {
            this.certificate = this.getCertificate();
        }
        catch (Exception exception) {
            throw new RuntimeException("Secure Log Initialization failed : Invalid Certificate file : " + this.certFile, exception);
        }
        this.checkPublicAccess(this.certFile);
    }

    private boolean checkPublicAccess(String string) throws RuntimeException {
        return true;
    }

    private X509Certificate getCertificate() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(this.certFile), this.certFilePwd.toCharArray());
        while (keyStore.aliases().hasMoreElements()) {
            Certificate certificate = keyStore.getCertificate(keyStore.aliases().nextElement());
            if (!(certificate instanceof X509Certificate)) continue;
            return (X509Certificate)certificate;
        }
        throw new RuntimeException("Unable to retrive public key");
    }

    void initializeEncryptionStream() {
        String string = "alert_ojdbc_" + this.loggerId + "_" + System.currentTimeMillis() + ".log";
        try {
            this.encStream = EncryptionStream.newEncryptionStream(new FileOutputStream(this.dumpFileLocation + string), this.certificate);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void publish(LogRecord logRecord) {
        if (!this.getFilter().isLoggable(logRecord)) {
            return;
        }
        try {
            String string = this.getFormatter().format(logRecord);
            this.encStream.write(string.getBytes());
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    @Override
    public void flush() {
        try {
            this.encStream.flush();
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    @Override
    public void close() throws SecurityException {
        try {
            this.encStream.close();
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    @Override
    public void setFormatter(Formatter formatter) throws SecurityException {
        if (formatter != null) {
            super.setFormatter(formatter);
        }
    }

    @Override
    public void setFilter(Filter filter) throws SecurityException {
        if (filter != null) {
            super.setFilter(filter);
        }
    }
}

