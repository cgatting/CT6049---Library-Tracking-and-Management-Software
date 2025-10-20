/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import oracle.jdbc.diagnostics.Releaser;
import oracle.jdbc.diagnostics.SecuredFileLogHandler;
import oracle.jdbc.diagnostics.SecuredLogger;
import oracle.jdbc.diagnostics.SecuredMemoryLogHandler;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.PropertiesBlinder;

final class SecuredLoggerImpl
implements SecuredLogger,
Monitor {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1L);
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();
    private final String loggerId;
    private String certFile;
    private String certFilePwd;
    private X509Certificate certificate;
    private ConcurrentHashMap<String, String> headers;
    private Level currentLogLevel = Level.ALL;
    private SecuredFileLogHandler fileLogger;
    private SecuredMemoryLogHandler memoryLogger;
    private Properties userProperties = null;

    SecuredLoggerImpl(String string, @Blind(value=PropertiesBlinder.class) Properties properties) {
        this.loggerId = string;
        this.userProperties = properties;
        this.headers = new ConcurrentHashMap();
        this.configure();
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }

    @Override
    public boolean isLoggable(Level level) {
        return this.currentLogLevel != Level.OFF && level.intValue() >= this.currentLogLevel.intValue();
    }

    @Override
    public void disableMemoryLogging() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.memoryLogger != null) {
                this.memoryLogger.close();
                this.memoryLogger = null;
            }
        }
    }

    @Override
    public void enableMemoryLogging() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.memoryLogger == null) {
                this.memoryLogger = new SecuredMemoryLogHandler(this);
            }
        }
    }

    @Override
    public void disableFileLogging() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.fileLogger != null) {
                this.fileLogger.close();
                this.fileLogger = null;
            }
        }
    }

    @Override
    public void enableFileLogging() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.fileLogger == null) {
                this.fileLogger = new SecuredFileLogHandler(this);
            }
        }
    }

    @Override
    public void reset() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.fileLogger != null) {
                this.fileLogger.reset();
            }
            if (this.memoryLogger != null) {
                this.memoryLogger.reset();
            }
        }
    }

    @Override
    public String getId() {
        return this.loggerId;
    }

    @Override
    public void dumpLog() {
        if (this.memoryLogger != null) {
            this.memoryLogger.dumpLog();
        }
    }

    @Override
    public void close() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            if (this.fileLogger != null) {
                this.fileLogger.close();
            }
            if (this.memoryLogger != null) {
                this.memoryLogger.close();
            }
        }
    }

    @Override
    public void setLogLevel(Level level) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.currentLogLevel = level;
            if (this.fileLogger != null) {
                this.fileLogger.setLevel(level);
            }
            if (this.memoryLogger != null) {
                this.memoryLogger.setLevel(level);
            }
        }
    }

    @Override
    public Level getLogLevel() {
        return this.currentLogLevel;
    }

    @Override
    public void addHeader(String string, String string2) {
        this.headers.put(string, string2);
    }

    @Override
    public void addHeader(@Blind(value=PropertiesBlinder.class) Properties properties) {
        properties.forEach((object, object2) -> this.headers.put((String)object, (String)object2));
    }

    @Override
    public void add(Level level, String string, String string2, String string3) {
        if (!this.isLoggable(level)) {
            return;
        }
        long l2 = System.currentTimeMillis();
        long l3 = ID_GENERATOR.incrementAndGet();
        if (this.memoryLogger != null) {
            this.memoryLogger.add(level, l3, l2, string, string2, string3);
        }
        if (this.fileLogger != null) {
            this.fileLogger.add(level, l3, l2, string, string2, string3);
        }
    }

    @Override
    public void add(Level level, String string, String string2, String string3, Object object, Releaser releaser) {
        if (!this.isLoggable(level)) {
            return;
        }
        long l2 = System.currentTimeMillis();
        long l3 = ID_GENERATOR.incrementAndGet();
        if (this.memoryLogger != null) {
            this.memoryLogger.add(level, l3, l2, string, string2, string3, object, releaser);
        }
        if (this.fileLogger != null) {
            this.fileLogger.add(level, l3, l2, string, string2, string3, object, null);
        }
    }

    @Override
    public void add(Level level, String string, String string2, String string3, Object object, Object object2, Releaser releaser) {
        if (!this.isLoggable(level)) {
            return;
        }
        long l2 = System.currentTimeMillis();
        long l3 = ID_GENERATOR.incrementAndGet();
        if (this.memoryLogger != null) {
            this.memoryLogger.add(level, l3, l2, string, string2, string3, object, object2, releaser);
        }
        if (this.fileLogger != null) {
            this.fileLogger.add(level, l3, l2, string, string2, string3, object, object2, null);
        }
    }

    @Override
    public void add(Level level, String string, String string2, String string3, Object object, Object object2, Object object3, Releaser releaser) {
        if (!this.isLoggable(level)) {
            return;
        }
        long l2 = System.currentTimeMillis();
        long l3 = ID_GENERATOR.incrementAndGet();
        if (this.memoryLogger != null) {
            this.memoryLogger.add(level, l3, l2, string, string2, string3, object, object2, object3, releaser);
        }
        if (this.fileLogger != null) {
            this.fileLogger.add(level, l3, l2, string, string2, string3, object, object2, object3, null);
        }
    }

    @Override
    public void add(Level level, String string, String string2, String string3, Object object, Object object2, Object object3, Object object4, Releaser releaser) {
        if (!this.isLoggable(level)) {
            return;
        }
        long l2 = System.currentTimeMillis();
        long l3 = ID_GENERATOR.incrementAndGet();
        if (this.memoryLogger != null) {
            this.memoryLogger.add(level, l3, l2, string, string2, string3, object, object2, object3, object4, releaser);
        }
        if (this.fileLogger != null) {
            this.fileLogger.add(level, l3, l2, string, string2, string3, object, object2, object3, object4, null);
        }
    }

    @Override
    public void add(Level level, String string, String string2, String string3, Releaser releaser, Object ... objectArray) {
        if (!this.isLoggable(level)) {
            return;
        }
        long l2 = System.currentTimeMillis();
        long l3 = ID_GENERATOR.incrementAndGet();
        if (this.memoryLogger != null) {
            this.memoryLogger.add(level, l3, l2, string, string2, string3, releaser, objectArray);
        }
        if (this.fileLogger != null) {
            this.fileLogger.add(level, l3, l2, string, string2, string3, null, objectArray);
        }
    }

    X509Certificate getCertificate() {
        return this.certificate;
    }

    @Blind(value=PropertiesBlinder.class)
    Properties getUserProperties() {
        return this.userProperties;
    }

    ConcurrentHashMap<String, String> getHeaders() {
        return this.headers;
    }

    private void readPublicKeyCertificate() throws RuntimeException {
        if (this.certFile == null) {
            return;
        }
        File file = new File(this.certFile);
        if (!file.exists()) {
            throw new RuntimeException("Secure Log Initialization failed : Certificate file " + this.certFile + " does not exist.");
        }
        try {
            this.certificate = this.loadCertificate();
        }
        catch (Exception exception) {
            throw new RuntimeException("Secure Log Initialization failed : Invalid Certificate file : " + this.certFile, exception);
        }
        this.checkPublicAccess(this.certFile);
    }

    private boolean checkPublicAccess(String string) throws RuntimeException {
        return true;
    }

    private X509Certificate loadCertificate() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(this.certFile), this.certFilePwd.toCharArray());
        while (keyStore.aliases().hasMoreElements()) {
            Certificate certificate = keyStore.getCertificate(keyStore.aliases().nextElement());
            if (!(certificate instanceof X509Certificate)) continue;
            return (X509Certificate)certificate;
        }
        throw new RuntimeException("Unable to retrive public key");
    }

    private void configure() {
        String string = this.getClass().getName();
        this.certFile = SecuredLogger.getStringProperty(string + ".certificate", DEFAULT_CERTIFICATE_PATH, this.userProperties);
        this.certFilePwd = SecuredLogger.getStringProperty(string + ".certificatePassword", DEFAULT_CERTIFICATE_PWD, this.userProperties);
        this.readPublicKeyCertificate();
    }
}

