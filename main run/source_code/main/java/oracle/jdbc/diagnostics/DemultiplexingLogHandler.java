/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import oracle.jdbc.logging.annotations.DisableTrace;

@DisableTrace
public class DemultiplexingLogHandler
extends FileHandler {
    static final String PROPERTY_PATTERN = "oracle.jdbc.diagnostics.DemultiplexingLogHandler.pattern";
    static final String PROPERTY_LIMIT = "oracle.jdbc.diagnostics.DemultiplexingLogHandler.limit";
    static final String PROPERTY_COUNT = "oracle.jdbc.diagnostics.DemultiplexingLogHandler.count";
    static final String PROPERTY_APPEND = "oracle.jdbc.diagnostics.DemultiplexingLogHandler.append";
    static final String DEFAULT_PATTERN = "%h/ojdbc_%s.trc";
    static final String DEFAULT_APPEND = String.valueOf(false);
    static final String DEFAULT_LIMIT = String.valueOf(Integer.MAX_VALUE);
    static final String DEFAULT_COUNT = String.valueOf(1);
    String localPattern;
    boolean localAppend;
    int localLimit;
    int localCount;
    Hashtable<Object, Handler> handlerList = new Hashtable(50);

    public DemultiplexingLogHandler() throws IOException {
        super(DemultiplexingLogHandler.getFilename(DemultiplexingLogHandler.getProperty(PROPERTY_PATTERN, DEFAULT_PATTERN), "MAIN"), Integer.parseInt(DemultiplexingLogHandler.getProperty(PROPERTY_LIMIT, DEFAULT_LIMIT)), Integer.parseInt(DemultiplexingLogHandler.getProperty(PROPERTY_COUNT, DEFAULT_COUNT)), Boolean.getBoolean(DemultiplexingLogHandler.getProperty(PROPERTY_APPEND, DEFAULT_APPEND)));
    }

    public DemultiplexingLogHandler(String string) throws IOException {
        super(DemultiplexingLogHandler.getFilename(string, "MAIN"), Integer.parseInt(DemultiplexingLogHandler.getProperty(PROPERTY_LIMIT, DEFAULT_LIMIT)), Integer.parseInt(DemultiplexingLogHandler.getProperty(PROPERTY_COUNT, DEFAULT_COUNT)), Boolean.getBoolean(DemultiplexingLogHandler.getProperty(PROPERTY_APPEND, DEFAULT_APPEND)));
    }

    public DemultiplexingLogHandler(String string, boolean bl) throws IOException {
        super(DemultiplexingLogHandler.getFilename(string, "MAIN"), Integer.parseInt(DemultiplexingLogHandler.getProperty(PROPERTY_LIMIT, DEFAULT_LIMIT)), Integer.parseInt(DemultiplexingLogHandler.getProperty(PROPERTY_COUNT, DEFAULT_COUNT)), bl);
    }

    public DemultiplexingLogHandler(String string, int n2, int n3) throws IOException {
        super(DemultiplexingLogHandler.getFilename(string, "MAIN"), n2, n3, Boolean.getBoolean(DemultiplexingLogHandler.getProperty(PROPERTY_APPEND, DEFAULT_APPEND)));
    }

    public DemultiplexingLogHandler(String string, int n2, int n3, boolean bl) throws IOException {
        super(DemultiplexingLogHandler.getFilename(string, "MAIN"), n2, n3, bl);
    }

    void initValues() {
        this.localPattern = DemultiplexingLogHandler.getProperty(PROPERTY_PATTERN, DEFAULT_PATTERN);
        this.localLimit = Integer.parseInt(DemultiplexingLogHandler.getProperty(PROPERTY_LIMIT, DEFAULT_LIMIT));
        this.localCount = Integer.parseInt(DemultiplexingLogHandler.getProperty(PROPERTY_COUNT, DEFAULT_COUNT));
        this.localAppend = Boolean.getBoolean(DemultiplexingLogHandler.getProperty(PROPERTY_APPEND, DEFAULT_APPEND));
    }

    static final String getFilename(String string, String string2) {
        if (string == null) {
            string = DEFAULT_PATTERN;
        }
        return string.replaceAll("%s", string2).trim();
    }

    static String getProperty(String string, String string2) {
        String string3 = LogManager.getLogManager().getProperty(string);
        return string3 != null ? string3 : string2;
    }

    @Override
    public void publish(LogRecord logRecord) {
        Object[] objectArray = logRecord.getParameters();
        if (objectArray != null && objectArray.length > 0) {
            Handler handler = this.handlerList.get(objectArray[0]);
            if (handler == null) {
                if (this.localPattern == null) {
                    this.initValues();
                }
                try {
                    handler = new FileHandler(DemultiplexingLogHandler.getFilename(this.localPattern, (String)objectArray[0]), this.localLimit, this.localCount, this.localAppend);
                    handler.setFormatter(this.getFormatter());
                    handler.setFilter(this.getFilter());
                    handler.setLevel(this.getLevel());
                    handler.setEncoding(this.getEncoding());
                    handler.setErrorManager(this.getErrorManager());
                }
                catch (IOException iOException) {
                    this.reportError("Unable open FileHandler", iOException, 0);
                }
                this.handlerList.put(objectArray[0], handler);
            }
            handler.publish(logRecord);
        } else {
            super.publish(logRecord);
        }
    }

    @Override
    public void close() {
        for (Handler handler : this.handlerList.values()) {
            handler.close();
        }
        super.close();
    }
}

