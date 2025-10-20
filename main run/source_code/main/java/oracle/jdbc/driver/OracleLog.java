/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Executable;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import oracle.jdbc.driver.ClioSupport;
import oracle.jdbc.driver.GeneratedPhysicalConnection;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Log;

@DisableTrace
public class OracleLog {
    private static final String DEFAULT_LOGGING_CONFIG_RESOURCE_NAME = "/oracle/jdbc/defaultLoggingConfig.properties";
    private static boolean isContinuousLoggingEnabled = false;
    private static boolean isInMemoryLoggingEnabled = true;
    private static String userNameFilter = null;
    private static String serviceNameFilter = null;
    private static final int maxPrintBytes = 512;
    public static final boolean TRACE = false;
    public static final Level INTERNAL_ERROR = OracleLevel.INTERNAL_ERROR;
    public static final Level TRACE_1 = OracleLevel.TRACE_1;
    public static final Level TRACE_10 = OracleLevel.TRACE_10;
    public static final Level TRACE_16 = OracleLevel.TRACE_16;
    public static final Level TRACE_20 = OracleLevel.TRACE_20;
    public static final Level TRACE_30 = OracleLevel.TRACE_30;
    public static final Level TRACE_32 = OracleLevel.TRACE_32;
    static boolean securityExceptionWhileGettingSystemProperties;

    @Log
    public static void debug(Logger logger, Level level, Class<?> clazz, Executable executable, String string) {
        ClioSupport.log(logger, level, clazz, executable, string);
    }

    @Log
    public static void warning(Logger logger, Class<?> clazz, Executable executable, String string) {
        ClioSupport.log(logger, Level.WARNING, clazz, executable, string);
    }

    @Log
    public static void log(Logger logger, Level level, Class<?> clazz, Executable executable, String string) {
        ClioSupport.log(logger, level, clazz, executable, string);
    }

    @Log
    public static void log(Logger logger, Level level, Class<?> clazz, Executable executable, String string, Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        OracleLog.debug(logger, level, clazz, executable, string + "\n" + stringWriter.toString());
    }

    @Log
    public static void log(Logger logger, Level level, Class<?> clazz, Executable executable, String string, Object[] objectArray) {
        ClioSupport.log(logger, level, clazz, executable, string, objectArray);
    }

    @Log
    public static void log(Logger logger, Level level, Class<?> clazz, Executable executable, String string, Object object) {
        ClioSupport.log(logger, level, clazz, executable, string, new Object[]{object});
    }

    public static void enableContinousLogging() {
        isContinuousLoggingEnabled = true;
    }

    public static void disableContinousLogging() {
        isContinuousLoggingEnabled = false;
    }

    public static boolean isContinousLoggingEnabled() {
        return isContinuousLoggingEnabled;
    }

    public static void enableInMemoryLogging() {
        isInMemoryLoggingEnabled = true;
    }

    public static void disableInMemoryLogging() {
        isInMemoryLoggingEnabled = false;
    }

    public static boolean isInMemoryLoggingEnabled() {
        return isInMemoryLoggingEnabled;
    }

    public static void setUserNameFilter(String string) {
        userNameFilter = string;
    }

    public static void setServiceNameFilter(String string) {
        serviceNameFilter = string;
    }

    public static boolean isDebugZip() {
        boolean bl = true;
        bl = false;
        return bl;
    }

    public static boolean isPrivateLogAvailable() {
        boolean bl = false;
        return bl;
    }

    public static boolean isEnabled() {
        return false;
    }

    public static boolean registerClassNameAndGetCurrentTraceSetting(Class<?> clazz) {
        return false;
    }

    public static void setTrace(boolean bl) {
    }

    private static void initialize() {
        OracleLog.setupFromSystemProperties();
        OracleLog.enableDefaultTrace();
    }

    public static void setupFromSystemProperties() {
        boolean bl = false;
        securityExceptionWhileGettingSystemProperties = false;
        try {
            String string = null;
            string = GeneratedPhysicalConnection.getSystemPropertyTrace();
            if ("true".equals(string)) {
                bl = true;
            }
        }
        catch (SecurityException securityException) {
            securityExceptionWhileGettingSystemProperties = true;
        }
        OracleLog.setTrace(bl);
    }

    public static void enableDefaultTrace() {
        try {
            InputStream inputStream = PhysicalConnection.class.getResourceAsStream(DEFAULT_LOGGING_CONFIG_RESOURCE_NAME);
            if (inputStream != null) {
                LogManager.getLogManager().readConfiguration(inputStream);
                OracleLog.setTrace(true);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static String argument() {
        return "";
    }

    public static String argument(boolean bl) {
        return Boolean.toString(bl);
    }

    public static String argument(byte by) {
        return Byte.toString(by);
    }

    public static String argument(short s2) {
        return Short.toString(s2);
    }

    public static String argument(int n2) {
        return Integer.toString(n2);
    }

    public static String argument(long l2) {
        return Long.toString(l2);
    }

    public static String argument(float f2) {
        return Float.toString(f2);
    }

    public static String argument(double d2) {
        return Double.toString(d2);
    }

    public static String argument(Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof String) {
            return "\"" + (String)object + "\"";
        }
        return object.toString();
    }

    @Deprecated
    public static String byteToHexString(byte by) {
        StringBuffer stringBuffer = new StringBuffer("");
        int n2 = 0xFF & by;
        if (n2 <= 15) {
            stringBuffer.append("0x0");
        } else {
            stringBuffer.append("0x");
        }
        stringBuffer.append(Integer.toHexString(n2));
        return stringBuffer.toString();
    }

    @Deprecated
    public static String bytesToPrintableForm(String string, byte[] byArray) {
        int n2 = byArray == null ? 0 : byArray.length;
        return OracleLog.bytesToPrintableForm(string, byArray, n2);
    }

    @Deprecated
    public static String bytesToPrintableForm(String string, byte[] byArray, int n2) {
        String string2 = null;
        string2 = byArray == null ? string + ": null" : string + " (" + byArray.length + " bytes):\n" + OracleLog.bytesToFormattedStr(byArray, n2, "  ");
        return string2;
    }

    @Deprecated
    public static String bytesToFormattedStr(byte[] byArray, int n2, String string) {
        StringBuffer stringBuffer = new StringBuffer("");
        if (string == null) {
            string = new String("");
        }
        stringBuffer.append(string);
        if (byArray == null) {
            stringBuffer.append("byte [] is null");
            return stringBuffer.toString();
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3;
            if (i2 >= 512) {
                stringBuffer.append("\n" + string + "... last " + (n2 - 512) + " bytes were not printed to limit the output size");
                break;
            }
            if (i2 > 0 && i2 % 20 == 0) {
                stringBuffer.append("\n" + string);
            }
            if (i2 % 20 == 10) {
                stringBuffer.append(" ");
            }
            if ((n3 = 0xFF & byArray[i2]) <= 15) {
                stringBuffer.append("0");
            }
            stringBuffer.append(Integer.toHexString(n3) + " ");
        }
        return stringBuffer.toString();
    }

    @Deprecated
    public static byte[] strToUcs2Bytes(String string) {
        if (string == null) {
            return null;
        }
        return OracleLog.charsToUcs2Bytes(string.toCharArray());
    }

    @Deprecated
    public static byte[] charsToUcs2Bytes(char[] cArray) {
        if (cArray == null) {
            return null;
        }
        return OracleLog.charsToUcs2Bytes(cArray, cArray.length);
    }

    @Deprecated
    public static byte[] charsToUcs2Bytes(char[] cArray, int n2) {
        if (cArray == null) {
            return null;
        }
        if (n2 < 0) {
            return null;
        }
        return OracleLog.charsToUcs2Bytes(cArray, n2, 0);
    }

    @Deprecated
    public static byte[] charsToUcs2Bytes(char[] cArray, int n2, int n3) {
        if (cArray == null) {
            return null;
        }
        if (n2 > cArray.length - n3) {
            n2 = cArray.length - n3;
        }
        if (n2 < 0) {
            return null;
        }
        byte[] byArray = new byte[2 * n2];
        int n4 = 0;
        for (int i2 = n3; i2 < n2; ++i2) {
            byArray[n4++] = (byte)(cArray[i2] >> 8 & 0xFF);
            byArray[n4++] = (byte)(cArray[i2] & 0xFF);
        }
        return byArray;
    }

    @Deprecated
    public static String toPrintableStr(String string, int n2) {
        if (string == null) {
            return "null";
        }
        if (string.length() > n2) {
            return string.substring(0, n2 - 1) + "\n ... the actual length was " + string.length();
        }
        return string;
    }

    public static String toHex(long l2, int n2) {
        String string;
        switch (n2) {
            case 1: {
                string = "00" + Long.toString(l2 & 0xFFL, 16);
                break;
            }
            case 2: {
                string = "0000" + Long.toString(l2 & 0xFFFFL, 16);
                break;
            }
            case 3: {
                string = "000000" + Long.toString(l2 & 0xFFFFFFL, 16);
                break;
            }
            case 4: {
                string = "00000000" + Long.toString(l2 & 0xFFFFFFFFL, 16);
                break;
            }
            case 5: {
                string = "0000000000" + Long.toString(l2 & 0xFFFFFFFFFFL, 16);
                break;
            }
            case 6: {
                string = "000000000000" + Long.toString(l2 & 0xFFFFFFFFFFFFL, 16);
                break;
            }
            case 7: {
                string = "00000000000000" + Long.toString(l2 & 0xFFFFFFFFFFFFFFL, 16);
                break;
            }
            case 8: {
                return OracleLog.toHex(l2 >> 32, 4) + OracleLog.toHex(l2, 4).substring(2);
            }
            default: {
                return "more than 8 bytes";
            }
        }
        return "0x" + string.substring(string.length() - 2 * n2);
    }

    public static String toHex(byte by) {
        String string = "00" + Integer.toHexString(by & 0xFF);
        return "0x" + string.substring(string.length() - 2);
    }

    public static String toHex(short s2) {
        return OracleLog.toHex(s2, 2);
    }

    public static String toHex(int n2) {
        return OracleLog.toHex(n2, 4);
    }

    public static String toHex(byte[] byArray, int n2) {
        if (byArray == null) {
            return "null";
        }
        if (n2 > byArray.length) {
            return "byte array not long enough";
        }
        String string = "[";
        int n3 = Math.min(64, n2);
        for (int i2 = 0; i2 < n3; ++i2) {
            string = string + OracleLog.toHex(byArray[i2]) + " ";
        }
        if (n3 < n2) {
            string = string + "...";
        }
        return string + "]";
    }

    public static String toHex(byte[] byArray) {
        if (byArray == null) {
            return "null";
        }
        return OracleLog.toHex(byArray, byArray.length);
    }

    static {
        OracleLog.initialize();
    }

    private static class OracleLevel
    extends Level {
        private static final long serialVersionUID = -2613875615050961941L;
        static final OracleLevel INTERNAL_ERROR = new OracleLevel("INTERNAL_ERROR", 1100);
        static final OracleLevel TRACE_1 = new OracleLevel("TRACE_1", Level.FINE.intValue());
        static final OracleLevel TRACE_10 = new OracleLevel("TRACE_10", 446);
        static final OracleLevel TRACE_16 = new OracleLevel("TRACE_16", Level.FINER.intValue());
        static final OracleLevel TRACE_20 = new OracleLevel("TRACE_20", 376);
        static final OracleLevel TRACE_30 = new OracleLevel("TRACE_30", 316);
        static final OracleLevel TRACE_32 = new OracleLevel("TRACE_32", Level.FINEST.intValue());

        OracleLevel(String string, int n2) {
            super(string, n2);
        }
    }
}

