/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

import java.lang.reflect.Executable;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.diagnostics.SecuredLogger;
import oracle.jdbc.internal.Loggable;
import oracle.jdbc.logging.annotations.DisableTrace;

@DisableTrace
public class DebugFirstFailureClioSupport {
    public static void exiting(Logger logger, Level level, Class<Object> clazz, Executable executable, Object object, Throwable throwable) {
        SecuredLogger securedLogger;
        if (throwable != null && object instanceof Loggable && DebugFirstFailureClioSupport.isExitingTheDriverCodebase(Thread.currentThread().getStackTrace()) && (securedLogger = ((Loggable)object).getLogger()) != null) {
            securedLogger.dumpLog();
        }
    }

    private static boolean isExitingTheDriverCodebase(StackTraceElement[] stackTraceElementArray) {
        if (stackTraceElementArray.length < 3) {
            return true;
        }
        for (int i2 = 3; i2 < stackTraceElementArray.length; ++i2) {
            if (!stackTraceElementArray[i2].getClassName().startsWith("oracle.jdbc.")) continue;
            return false;
        }
        return true;
    }

    public static void entering(Logger logger, Level level, Class<Object> clazz, Executable executable, Object object, Object ... objectArray) {
    }

    public static void throwing(Logger logger, Level level, Class<Object> clazz, Executable executable, Object object, Throwable throwable) {
    }

    public static boolean publicEnter() {
        return true;
    }

    public static void publicExit() {
    }

    public static void returning(Logger logger, Level level, Class<Object> clazz, Executable executable, Object object) {
    }

    public static void returning(Logger logger, Level level, Class<Object> clazz, Executable executable, Object object, Object object2) {
    }
}

