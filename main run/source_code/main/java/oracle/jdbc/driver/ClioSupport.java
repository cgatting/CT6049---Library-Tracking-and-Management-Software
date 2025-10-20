/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.driver.OracleLog;
import oracle.jdbc.logging.annotations.DisableTrace;

@DisableTrace
public class ClioSupport {
    private static ThreadLocal<Long> enterTime = new ThreadLocal();
    private static Logger defaultLogger = Logger.getLogger("oracle.jdbc.logging.ClioSupport");

    private static String toString(Executable executable) {
        return executable instanceof Constructor ? "<init>" : executable.getName();
    }

    public static void log(Logger logger, Level level, Class<?> clazz, Executable executable, String string) {
        ClioSupport.log(logger, level, clazz, executable, string, null);
    }

    public static void log(Logger logger, Level level, Class<?> clazz, Executable executable, String string, Object[] objectArray) {
        Logger logger2 = null != logger ? logger : defaultLogger;
        logger2.logp(null != level ? level : Level.FINEST, null != clazz ? clazz.getName() : "null", null != executable ? ClioSupport.toString(executable) : "null", null != string ? string : "null", null != objectArray ? objectArray : new Object[]{});
    }

    private static String receiverToString(Object object) {
        return null == object ? "        " : Integer.toHexString(object.hashCode()).toUpperCase();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void entering(Logger logger, Level level, Class<?> clazz, Executable executable, Object object, Object ... objectArray) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(ClioSupport.receiverToString(object));
        }
        catch (Exception exception) {
            stringBuilder.append("[Caught receiver exception]");
        }
        stringBuilder.append(" Enter: ");
        if (0 == objectArray.length) {
            stringBuilder.append("void");
        } else {
            boolean bl = false;
            for (Object object2 : objectArray) {
                String string = "";
                try {
                    string = OracleLog.argument(object2);
                    stringBuilder.append(bl ? ", " : "").append(string);
                }
                catch (Throwable throwable) {
                    try {
                        StringWriter stringWriter = new StringWriter();
                        throwable.printStackTrace(new PrintWriter(stringWriter));
                        string = stringWriter.toString();
                        stringBuilder.append(bl ? ", " : "").append(string);
                    }
                    catch (Throwable throwable2) {
                        stringBuilder.append(bl ? ", " : "").append(string);
                        bl = true;
                        throw throwable2;
                    }
                    bl = true;
                    continue;
                }
                bl = true;
            }
        }
        Level level2 = level;
        String string = null == clazz ? "" : clazz.getName();
        String string2 = null == executable ? "" : executable.getName();
        Logger logger2 = null == logger ? Logger.getAnonymousLogger() : logger;
        logger2.logp(level2, string, string2, stringBuilder.toString());
        enterTime.set(System.nanoTime());
    }

    public static void returning(Logger logger, Level level, Class<?> clazz, Executable executable, Object object) {
        Level level2 = level;
        String string = null == clazz ? "" : clazz.getName();
        String string2 = null == executable ? "" : executable.getName();
        Logger logger2 = null == logger ? Logger.getAnonymousLogger() : logger;
        try {
            logger2.logp(level2, string, string2, ClioSupport.receiverToString(object) + " Return: void");
        }
        catch (Exception exception) {
            logger2.logp(level2, string, string2, "[Caught receiver exception]: Return: void");
        }
    }

    public static void returning(Logger logger, Level level, Class<?> clazz, Executable executable, Object object, Object object2) {
        Level level2 = level;
        String string = null == clazz ? "" : clazz.getName();
        String string2 = null == executable ? "" : executable.getName();
        Logger logger2 = null == logger ? Logger.getAnonymousLogger() : logger;
        try {
            logger2.logp(level2, string, string2, ClioSupport.receiverToString(object) + " Return: " + object2);
        }
        catch (Exception exception) {
            logger2.logp(level2, string, string2, "[Caught receiver exception]: Return: " + object2);
        }
    }

    public static void throwing(Logger logger, Level level, Class<?> clazz, Executable executable, Object object, Throwable throwable) {
        Level level2 = level;
        String string = null == clazz ? "" : clazz.getName();
        String string2 = null == executable ? "" : executable.getName();
        Logger logger2 = null == logger ? Logger.getAnonymousLogger() : logger;
        try {
            logger2.logp(level2, string, string2, ClioSupport.receiverToString(object) + " Throw: " + throwable);
        }
        catch (Exception exception) {
            logger2.logp(level2, string, string2, "[Caught receiver exception]: Throw: " + throwable);
        }
    }

    public static void exiting(Logger logger, Level level, Class<?> clazz, Executable executable, Object object, Throwable throwable) {
        Level level2 = level;
        String string = null == clazz ? "" : clazz.getName();
        String string2 = null == executable ? "" : executable.getName();
        Logger logger2 = null == logger ? Logger.getAnonymousLogger() : logger;
        try {
            logger2.logp(level2, string, string2, ClioSupport.receiverToString(object) + " Exit: [" + (double)(System.nanoTime() - enterTime.get()) / 1000000.0 + " ms]");
        }
        catch (Exception exception) {
            logger2.logp(level2, string, string2, "[Caught receiver exception]: Exit [" + (double)(System.nanoTime() - enterTime.get()) / 1000000.0 + " ms]");
        }
    }

    public static boolean publicEnter() {
        return true;
    }

    public static void publicExit() {
    }
}

