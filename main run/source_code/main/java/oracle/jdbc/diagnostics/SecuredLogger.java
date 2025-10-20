/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import oracle.jdbc.diagnostics.Releaser;
import oracle.jdbc.diagnostics.SecuredLoggerImpl;

public interface SecuredLogger {
    public static final boolean IS_ENABLED = false;
    public static final int LOG_EVENT_ATTRIBUTE_LENGTH = 9;
    public static final String DEFAULT_LOG_LOCATION = System.getProperty("oracle.jdbc.diagnostics.logLocation");
    public static final String DEFAULT_CERTIFICATE_PATH = System.getProperty("oracle.jdbc.diagnostics.certificate");
    public static final String DEFAULT_CERTIFICATE_PWD = System.getProperty("oracle.jdbc.diagnostics.certificatePassword");
    public static final Filter DEFAULT_FILTER = logRecord -> true;
    public static final ConcurrentHashMap<String, SecuredLogger> ACTIVE_LOGGER_MAP = new ConcurrentHashMap();

    public boolean isLoggable(Level var1);

    public void disableMemoryLogging();

    public void enableMemoryLogging();

    public void disableFileLogging();

    public void enableFileLogging();

    public void reset();

    public String getId();

    public void dumpLog();

    public void close();

    public void setLogLevel(Level var1);

    public Level getLogLevel();

    public void addHeader(String var1, String var2);

    public void addHeader(Properties var1);

    public void add(Level var1, String var2, String var3, String var4);

    default public void add(Level level, String string, String string2, String string3, Object object) {
        this.add(level, string, string2, string3, object, null);
    }

    default public void add(Level level, String string, String string2, String string3, Object object, Object object2) {
        this.add(level, string, string2, string3, object, object2, null);
    }

    default public void add(Level level, String string, String string2, String string3, Object object, Object object2, Object object3) {
        this.add(level, string, string2, string3, object, object2, object3, null);
    }

    default public void add(Level level, String string, String string2, String string3, Object object, Object object2, Object object3, Object object4) {
        this.add(level, string, string2, string3, object, object2, object3, object4, null);
    }

    default public void add(Level level, String string, String string2, String string3, Object ... objectArray) {
        this.add(level, string, string2, string3, null, objectArray);
    }

    public void add(Level var1, String var2, String var3, String var4, Object var5, Releaser var6);

    public void add(Level var1, String var2, String var3, String var4, Object var5, Object var6, Releaser var7);

    public void add(Level var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Releaser var8);

    public void add(Level var1, String var2, String var3, String var4, Object var5, Object var6, Object var7, Object var8, Releaser var9);

    public void add(Level var1, String var2, String var3, String var4, Releaser var5, Object ... var6);

    public static SecuredLogger newLogger(String string, Properties properties) {
        SecuredLoggerImpl securedLoggerImpl = new SecuredLoggerImpl(string, properties);
        ACTIVE_LOGGER_MAP.put(string, securedLoggerImpl);
        return securedLoggerImpl;
    }

    public static SecuredLogger getLogger(String string) {
        return ACTIVE_LOGGER_MAP.get(string);
    }

    public static SecuredLogger releaseLogger(String string) {
        return ACTIVE_LOGGER_MAP.remove(string);
    }

    public static SecuredLogger noOpLogger() {
        return new SecuredLoggerImpl("noop", null);
    }

    public static String getStringProperty(String string, String string2, Properties properties) {
        String string3 = null;
        if (properties != null) {
            string3 = properties.getProperty(string);
        }
        if (string3 == null && (string3 = LogManager.getLogManager().getProperty(string)) == null) {
            string3 = string2;
        }
        return string3;
    }

    public static int getIntProperty(String string, int n2, Properties properties) {
        String string2 = SecuredLogger.getStringProperty(string, null, properties);
        if (string2 == null) {
            return n2;
        }
        try {
            return Integer.parseInt(string2.trim());
        }
        catch (Exception exception) {
            return n2;
        }
    }

    public static boolean getBooleanProperty(String string, boolean bl, Properties properties) {
        String string2 = SecuredLogger.getStringProperty(string, null, properties);
        if (string2 == null) {
            return bl;
        }
        if ((string2 = string2.toLowerCase()).equals("true") || string2.equals("1")) {
            return true;
        }
        if (string2.equals("false") || string2.equals("0")) {
            return false;
        }
        return bl;
    }

    public static Level getLevelProperty(String string, Level level, Properties properties) {
        String string2 = SecuredLogger.getStringProperty(string, null, properties);
        if (string2 == null) {
            return level;
        }
        Level level2 = Level.parse(string2.trim());
        return level2 != null ? level2 : level;
    }

    public static Filter getFilterProperty(String string, Filter filter, Properties properties) {
        String string2 = SecuredLogger.getStringProperty(string, null, properties);
        try {
            if (string2 != null) {
                Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(string2);
                return (Filter)clazz.newInstance();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return filter;
    }

    public static Formatter getFormatterProperty(String string, Formatter formatter, Properties properties) {
        String string2 = SecuredLogger.getStringProperty(string, null, properties);
        try {
            if (string2 != null) {
                Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(string2);
                return (Formatter)clazz.newInstance();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return formatter;
    }

    public static enum Attribute {
        LENGTH(0),
        EVENT_ID(1),
        THREAD_ID(2),
        CLASS_NAME(3),
        METHOD_NAME(4),
        LOG_LEVEL(5),
        EVENT_TIME(6),
        DEBUG_MESSAGE(7),
        RELEASER(8);

        private final int index;

        private Attribute(int n3) {
            this.index = n3;
        }

        public int getIndex() {
            return this.index;
        }

        private int getIndexInBuffer(int n2, int n3) {
            return (n2 + this.index) % n3;
        }

        public Object getAttributeValue(int n2, Object[] objectArray) {
            return objectArray[this.getIndexInBuffer(n2, objectArray.length)];
        }
    }
}

