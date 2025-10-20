/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import oracle.jdbc.diagnostics.EncryptionStream;
import oracle.jdbc.diagnostics.MessageFormatUtils;
import oracle.jdbc.diagnostics.Releaser;
import oracle.jdbc.diagnostics.SecuredLogger;
import oracle.jdbc.diagnostics.SecuredLoggerImpl;
import oracle.jdbc.internal.Monitor;

final class SecuredFileLogHandler
extends StreamHandler
implements Monitor {
    private static final int MAX_LOCKS = 100;
    private static final ConcurrentHashMap<String, String> locks = new ConcurrentHashMap();
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();
    private MeteredStream meter;
    private final boolean append = false;
    private int limit;
    private int count;
    private String pattern;
    private String lockFileName;
    private FileChannel lockFileChannel;
    private File[] files;
    private X509Certificate certificate;
    private final String loggerId;
    private boolean isOpen = true;
    private Properties userProperties = null;
    private final SecuredLoggerImpl securedLogger;

    SecuredFileLogHandler(SecuredLoggerImpl securedLoggerImpl) throws RuntimeException {
        this.securedLogger = securedLoggerImpl;
        this.loggerId = securedLoggerImpl.getId();
        this.certificate = securedLoggerImpl.getCertificate();
        this.userProperties = securedLoggerImpl.getUserProperties();
        this.setFormatter(new SimpleFormatter());
        this.setFilter(SecuredLogger.DEFAULT_FILTER);
        this.setLevel(securedLoggerImpl.getLogLevel());
        this.configure();
        try {
            this.openFiles();
        }
        catch (IOException iOException) {
            this.reportError(null, iOException, 0);
        }
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3) {
        if (this.isOpen) {
            this.publishLogRecordToFile(level, l2, l3, string, string2, string3, new Object[0]);
        }
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3, Object object, Releaser releaser) {
        if (this.isOpen) {
            this.publishLogRecordToFile(level, l2, l3, string, string2, string3, object);
        }
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3, Object object, Object object2, Releaser releaser) {
        if (this.isOpen) {
            this.publishLogRecordToFile(level, l2, l3, string, string2, string3, object, object2);
        }
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3, Object object, Object object2, Object object3, Releaser releaser) {
        if (this.isOpen) {
            this.publishLogRecordToFile(level, l2, l3, string, string2, string3, object, object2, object3);
        }
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3, Object object, Object object2, Object object3, Object object4, Releaser releaser) {
        if (this.isOpen) {
            this.publishLogRecordToFile(level, l2, l3, string, string2, string3, object, object2, object3, object4);
        }
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3, Releaser releaser, Object ... objectArray) {
        if (this.isOpen) {
            this.publishLogRecordToFile(level, l2, l3, string, string2, string3, objectArray);
        }
    }

    private void publishLogRecordToFile(Level level, long l2, long l3, String string, String string2, String string3, Object ... objectArray) {
        LogRecord logRecord = new LogRecord(level, string3);
        logRecord.setSequenceNumber(l2);
        logRecord.setSourceClassName(string);
        logRecord.setSourceMethodName(string2);
        logRecord.setThreadID((int)Thread.currentThread().getId());
        logRecord.setMillis(l3);
        if (objectArray != null && objectArray.length > 0) {
            logRecord.setParameters(MessageFormatUtils.formatLogParams(objectArray));
        }
        this.publish(logRecord);
    }

    private void configure() {
        String string = SecuredLogger.class.getName();
        String string2 = SecuredLogger.getStringProperty(string + ".logLocation", SecuredLogger.DEFAULT_LOG_LOCATION, this.userProperties);
        this.pattern = SecuredLogger.getStringProperty(string + ".pattern", string2 + "/jdbc_%u.log", this.userProperties);
        this.limit = SecuredLogger.getIntProperty(string + ".limit", 0, this.userProperties);
        if (this.limit < 0) {
            this.limit = 0;
        }
        this.count = SecuredLogger.getIntProperty(string + ".count", 1, this.userProperties);
        if (this.count <= 0) {
            this.count = 1;
        }
        this.setFilter(SecuredLogger.getFilterProperty(string + ".filter", SecuredLogger.DEFAULT_FILTER, this.userProperties));
        this.setFormatter(SecuredLogger.getFormatterProperty(string + ".formatter", new SimpleFormatter(), this.userProperties));
        try {
            this.setEncoding(SecuredLogger.getStringProperty(string + ".encoding", null, this.userProperties));
        }
        catch (Exception exception) {
            try {
                this.setEncoding(null);
            }
            catch (Exception exception2) {
                // empty catch block
            }
        }
    }

    private void open(File file, boolean bl) throws IOException {
        int n2 = 0;
        if (bl) {
            n2 = (int)file.length();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file.toString(), bl);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        this.meter = new MeteredStream(bufferedOutputStream, n2);
        this.setOutputStream(this.meter);
    }

    private void openFiles() throws IOException {
        int n2;
        if (this.count < 1) {
            throw new IllegalArgumentException("file count = " + this.count);
        }
        if (this.limit < 0) {
            this.limit = 0;
        }
        int n3 = -1;
        while (true) {
            if (++n3 > 100) {
                throw new IOException("Couldn't get lock for " + this.pattern);
            }
            this.lockFileName = this.generate(this.pattern, 0, n3).toString() + ".lck";
            if (locks.get(this.lockFileName) != null) continue;
            try {
                this.lockFileChannel = FileChannel.open(Paths.get(this.lockFileName, new String[0]), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            }
            catch (FileAlreadyExistsException fileAlreadyExistsException) {
                continue;
            }
            try {
                n2 = this.lockFileChannel.tryLock() != null ? 1 : 0;
            }
            catch (IOException iOException) {
                n2 = 1;
            }
            if (n2 != 0) break;
            this.lockFileChannel.close();
        }
        locks.put(this.lockFileName, this.lockFileName);
        this.files = new File[this.count];
        for (n2 = 0; n2 < this.count; ++n2) {
            this.files[n2] = this.generate(this.pattern, n2, n3);
        }
        this.rotate();
    }

    private File generate(String string, int n2, int n3) throws IOException {
        File file = null;
        String string2 = "";
        int n4 = 0;
        boolean bl = false;
        boolean bl2 = false;
        while (n4 < string.length()) {
            char c2 = string.charAt(n4);
            char c3 = '\u0000';
            if (++n4 < string.length()) {
                c3 = Character.toLowerCase(string.charAt(n4));
            }
            if (c2 == '/') {
                file = file == null ? new File(string2) : new File(file, string2);
                string2 = "";
                continue;
            }
            if (c2 == '%') {
                if (c3 == 't') {
                    String string3 = System.getProperty("java.io.tmpdir");
                    if (string3 == null) {
                        string3 = System.getProperty("user.home");
                    }
                    file = new File(string3);
                    ++n4;
                    string2 = "";
                    continue;
                }
                if (c3 == 'g') {
                    string2 = string2 + n2;
                    bl = true;
                    ++n4;
                    continue;
                }
                if (c3 == 'u') {
                    string2 = string2 + "_" + this.loggerId + "_" + n3;
                    bl2 = true;
                    ++n4;
                    continue;
                }
                if (c3 == '%') {
                    string2 = string2 + "%";
                    ++n4;
                    continue;
                }
            }
            string2 = string2 + c2;
        }
        if (this.count > 1 && !bl) {
            string2 = string2 + "." + n2;
        }
        if (n3 > 0 && !bl2) {
            string2 = string2 + "." + n3;
        }
        if (string2.length() > 0) {
            file = file == null ? new File(string2) : new File(file, string2);
        }
        return file;
    }

    private void rotate() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            Level level = this.getLevel();
            this.setLevel(Level.OFF);
            super.close();
            for (int i2 = this.count - 2; i2 >= 0; --i2) {
                File file = this.files[i2];
                File file2 = this.files[i2 + 1];
                if (!file.exists()) continue;
                if (file2.exists()) {
                    file2.delete();
                }
                file.renameTo(file2);
            }
            try {
                this.open(this.files[0], false);
            }
            catch (IOException iOException) {
                this.reportError(null, iOException, 4);
            }
            this.setLevel(level);
        }
    }

    @Override
    public void publish(LogRecord logRecord) {
        if (!this.isLoggable(logRecord)) {
            return;
        }
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            super.publish(logRecord);
            this.flush();
            if (this.limit > 0 && this.meter.written >= this.limit) {
                AccessController.doPrivileged(new PrivilegedAction<Object>(){

                    @Override
                    public Object run() {
                        SecuredFileLogHandler.this.rotate();
                        return null;
                    }
                });
            }
        }
    }

    @Override
    public void close() throws SecurityException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            super.close();
            if (this.lockFileName == null) {
                return;
            }
            try {
                this.lockFileChannel.close();
            }
            catch (Exception exception) {
                this.reportError(null, exception, 3);
            }
            locks.remove(this.lockFileName);
            new File(this.lockFileName).delete();
            this.lockFileName = null;
            this.lockFileChannel = null;
        }
    }

    public void reset() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.close();
            this.openFiles();
        }
        catch (Exception exception) {
            this.reportError(null, exception, 0);
        }
    }

    public void disable() {
        this.close();
    }

    public void enable() {
        this.reset();
    }

    private class MeteredStream
    extends OutputStream {
        final OutputStream out;
        int written;
        private EncryptionStream encStream;

        MeteredStream(OutputStream outputStream, int n2) {
            this.out = outputStream;
            this.written = n2;
            try {
                this.encStream = EncryptionStream.newEncryptionStream(outputStream, SecuredFileLogHandler.this.certificate);
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public void write(int n2) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void write(byte[] byArray) throws IOException {
            this.encStream.write(byArray);
            this.written += byArray.length;
        }

        @Override
        public void write(byte[] byArray, int n2, int n3) throws IOException {
            this.encStream.write(Arrays.copyOfRange(byArray, n2, n2 + n3));
        }

        @Override
        public void flush() throws IOException {
            this.encStream.flush();
        }

        @Override
        public void close() throws IOException {
            this.encStream.close();
        }
    }
}

