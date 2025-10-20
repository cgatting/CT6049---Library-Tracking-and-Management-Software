/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import oracle.jdbc.diagnostics.EncryptionStream;
import oracle.jdbc.diagnostics.MessageFormatUtils;
import oracle.jdbc.diagnostics.Releaser;
import oracle.jdbc.diagnostics.SecuredLogger;
import oracle.jdbc.diagnostics.SecuredLoggerImpl;
import oracle.jdbc.internal.Monitor;

final class SecuredMemoryLogHandler
extends Handler
implements Monitor {
    private final String loggerId;
    private final X509Certificate certificate;
    private String dumpFileLocation;
    private int bufferMaxSize = 10000;
    private boolean isOpen = false;
    private Object[] buffer;
    private int head;
    private int tail;
    private int available;
    private ConcurrentHashMap<String, String> headers;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();
    private Properties userProperties = null;

    SecuredMemoryLogHandler(SecuredLoggerImpl securedLoggerImpl) {
        this.loggerId = securedLoggerImpl.getId();
        this.certificate = securedLoggerImpl.getCertificate();
        this.headers = securedLoggerImpl.getHeaders();
        this.userProperties = securedLoggerImpl.getUserProperties();
        this.configure();
        this.available = this.bufferMaxSize;
        if (this.dumpFileLocation != null) {
            this.buffer = new Object[this.bufferMaxSize];
            this.isOpen = true;
        } else {
            this.isOpen = false;
        }
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }

    public boolean isEmpty() {
        return this.available == this.bufferMaxSize;
    }

    private void configure() {
        String string = SecuredLogger.class.getName();
        this.dumpFileLocation = SecuredLogger.getStringProperty(string + ".logLocation", SecuredLogger.DEFAULT_LOG_LOCATION, this.userProperties);
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

    public void add(Level level, long l2, long l3, String string, String string2, String string3) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.addEvent(level, l2, l3, string, string2, string3, null, 0);
        }
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3, Object object, Releaser releaser) {
        boolean bl;
        if (!this.isOpen) {
            return;
        }
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            bl = this.addEvent(level, l2, l3, string, string2, string3, releaser, 1);
            if (bl) {
                this.addToBuffer(object);
            }
        }
        if (!bl && releaser != null) {
            this.releaseParam(releaser, 1, object);
        }
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3, Object object, Object object2, Releaser releaser) {
        boolean bl;
        if (!this.isOpen) {
            return;
        }
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            bl = this.addEvent(level, l2, l3, string, string2, string3, releaser, 2);
            if (bl) {
                this.addToBuffer(object);
                this.addToBuffer(object2);
            }
        }
        if (!bl && releaser != null) {
            this.releaseParam(releaser, 1, object);
            this.releaseParam(releaser, 2, object2);
        }
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3, Object object, Object object2, Object object3, Releaser releaser) {
        boolean bl;
        if (!this.isOpen) {
            return;
        }
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            bl = this.addEvent(level, l2, l3, string, string2, string3, releaser, 3);
            if (bl) {
                this.addToBuffer(object);
                this.addToBuffer(object2);
                this.addToBuffer(object3);
            }
        }
        if (!bl && releaser != null) {
            this.releaseParam(releaser, 1, object);
            this.releaseParam(releaser, 2, object2);
            this.releaseParam(releaser, 3, object3);
        }
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3, Object object, Object object2, Object object3, Object object4, Releaser releaser) {
        boolean bl;
        if (!this.isOpen) {
            return;
        }
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            bl = this.addEvent(level, l2, l3, string, string2, string3, releaser, 4);
            if (bl) {
                this.addToBuffer(object);
                this.addToBuffer(object2);
                this.addToBuffer(object3);
                this.addToBuffer(object4);
            }
        }
        if (!bl && releaser != null) {
            this.releaseParam(releaser, 1, object);
            this.releaseParam(releaser, 2, object2);
            this.releaseParam(releaser, 3, object3);
            this.releaseParam(releaser, 4, object4);
        }
    }

    public void add(Level level, long l2, long l3, String string, String string2, String string3, Releaser releaser, Object ... objectArray) {
        boolean bl;
        if (!this.isOpen) {
            return;
        }
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            bl = this.addEvent(level, l2, l3, string, string2, string3, releaser, 1);
            if (bl) {
                for (int i2 = 0; i2 < objectArray.length; ++i2) {
                    this.addToBuffer(objectArray[i2]);
                }
            }
        }
        if (!bl && releaser != null) {
            for (int i3 = 0; i3 < objectArray.length; ++i3) {
                this.releaseParam(releaser, i3 + 1, objectArray[i3]);
            }
        }
    }

    private boolean addEvent(Level level, long l2, long l3, String string, String string2, String string3, Releaser releaser, int n2) {
        int n3 = 9 + n2;
        if (n3 > this.bufferMaxSize) {
            throw new RuntimeException("Data length more than buffer length : " + n3);
        }
        if (n3 > this.available) {
            this.free(n3 - this.available);
        }
        this.addToBuffer(n3);
        this.addToBuffer(l2);
        this.addToBuffer(Thread.currentThread().getId());
        this.addToBuffer(string);
        this.addToBuffer(string2);
        this.addToBuffer(level);
        this.addToBuffer(l3);
        this.addToBuffer(string3);
        this.addToBuffer(releaser);
        this.available -= n3;
        return true;
    }

    private void releaseResources(Releaser releaser, Object[] objectArray, int n2, int n3) {
        int n4 = 1;
        int n5 = n2;
        while (n4 <= n3) {
            this.releaseParam(releaser, n4++, objectArray[n5++]);
            if (n5 != this.bufferMaxSize) continue;
            n5 = 0;
        }
    }

    private void releaseParam(Releaser releaser, int n2, Object object) {
        releaser.release(n2, object);
    }

    private void addToBuffer(Object object) {
        this.buffer[this.tail++] = object;
        if (this.tail == this.bufferMaxSize) {
            this.tail = 0;
        }
    }

    private void free(int n2) {
        int n3;
        int n4;
        for (n3 = 0; n3 < n2; n3 += n4) {
            n4 = (Integer)this.buffer[this.head];
            this.head = (this.head + n4) % this.bufferMaxSize;
            Object object = this.buffer[(this.head + 9 - 1) % this.bufferMaxSize];
            if (object == null) continue;
            this.releaseResources((Releaser)object, this.buffer, (this.head + 9) % this.bufferMaxSize, n4 - 9);
        }
        this.available += n3;
    }

    @Override
    public void close() {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.isOpen = false;
        }
        if (!this.isEmpty()) {
            this.free(this.bufferMaxSize - this.available);
        }
        this.buffer = null;
        this.headers.clear();
    }

    public void dumpLog() {
        if (!this.isOpen || this.isEmpty()) {
            return;
        }
        try {
            int n2;
            ConcurrentHashMap<String, String> concurrentHashMap;
            int n3;
            int n4;
            Object[] objectArray;
            EncryptionStream encryptionStream = this.initializeEncryptionStream();
            try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
                objectArray = this.buffer;
                n4 = this.head;
                n3 = this.tail;
                concurrentHashMap = this.headers;
                n2 = this.bufferMaxSize - this.available;
                this.reset();
            }
            this.publishHeader(concurrentHashMap, encryptionStream);
            this.publishDebugEvents(objectArray, n4, n3, encryptionStream, n2);
            encryptionStream.close();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void publishHeader(ConcurrentHashMap<String, String> concurrentHashMap, EncryptionStream encryptionStream) throws Exception {
        StringBuilder stringBuilder = new StringBuilder(System.lineSeparator());
        concurrentHashMap.forEach((string, string2) -> stringBuilder.append(string + " = " + string2 + System.lineSeparator()));
        LogRecord logRecord = new LogRecord(Level.CONFIG, stringBuilder.toString());
        logRecord.setSourceClassName("Configurations");
        this.publish(logRecord, encryptionStream);
    }

    private void publishDebugEvents(Object[] objectArray, int n2, int n3, EncryptionStream encryptionStream, int n4) throws Exception {
        int n5;
        int n6 = n2;
        for (int i2 = n4; i2 > 0; i2 -= n5) {
            n5 = this.publishEvent(encryptionStream, objectArray, n6);
            n6 = (n6 + n5) % objectArray.length;
        }
    }

    private int publishEvent(EncryptionStream encryptionStream, Object[] objectArray, int n2) throws Exception {
        int n3 = (Integer)objectArray[n2];
        this.publish(this.createLogRecord(n2, objectArray, n3), encryptionStream);
        Releaser releaser = (Releaser)objectArray[(n2 + 9 - 1) % this.bufferMaxSize];
        if (releaser != null) {
            this.releaseResources(releaser, objectArray, (n2 + 9) % objectArray.length, n3 - 9);
        }
        return n3;
    }

    private LogRecord createLogRecord(int n2, Object[] objectArray, int n3) {
        LogRecord logRecord = new LogRecord((Level)SecuredLogger.Attribute.LOG_LEVEL.getAttributeValue(n2, objectArray), (String)SecuredLogger.Attribute.DEBUG_MESSAGE.getAttributeValue(n2, objectArray));
        logRecord.setSequenceNumber((Long)SecuredLogger.Attribute.EVENT_ID.getAttributeValue(n2, objectArray));
        logRecord.setSourceClassName((String)SecuredLogger.Attribute.CLASS_NAME.getAttributeValue(n2, objectArray));
        logRecord.setSourceMethodName((String)SecuredLogger.Attribute.METHOD_NAME.getAttributeValue(n2, objectArray));
        logRecord.setThreadID(((Long)SecuredLogger.Attribute.THREAD_ID.getAttributeValue(n2, objectArray)).intValue());
        logRecord.setMillis((Long)SecuredLogger.Attribute.EVENT_TIME.getAttributeValue(n2, objectArray));
        if (n3 > 9) {
            logRecord.setParameters(this.getDebugMessageParams(objectArray, n2 + 9, n3 - 9));
        }
        return logRecord;
    }

    EncryptionStream initializeEncryptionStream() {
        String string = "alert_ojdbc_" + this.loggerId + "_" + System.currentTimeMillis() + ".log";
        try {
            return EncryptionStream.newEncryptionStream(new FileOutputStream(this.dumpFileLocation + string), this.certificate);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void publish(LogRecord logRecord) {
        throw new UnsupportedOperationException();
    }

    public void publish(LogRecord logRecord, EncryptionStream encryptionStream) {
        if (!this.getFilter().isLoggable(logRecord)) {
            return;
        }
        try {
            String string = this.getFormatter().format(logRecord);
            encryptionStream.write(string.getBytes());
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    @Override
    public void flush() {
    }

    private Object[] getDebugMessageParams(Object[] objectArray, int n2, int n3) {
        int n4 = objectArray.length;
        Object[] objectArray2 = new Object[n3];
        int n5 = n3;
        int n6 = n2;
        int n7 = 0;
        while (n5-- > 0) {
            Object object = objectArray[n6++ % n4];
            objectArray2[n7++] = MessageFormatUtils.formatLogParam(object);
        }
        return objectArray2;
    }

    public void reset() {
        if (this.dumpFileLocation == null) {
            return;
        }
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            this.buffer = new Object[this.bufferMaxSize];
            this.tail = 0;
            this.head = 0;
            this.available = this.bufferMaxSize;
            this.headers = new ConcurrentHashMap();
            this.isOpen = true;
        }
    }

    public void disable() {
        this.close();
    }

    public void enable() {
        this.reset();
    }
}

