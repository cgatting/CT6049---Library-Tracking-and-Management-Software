/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.logging.annotations.DisableTrace;

@DisableTrace
public class BuildInfo {
    public static final boolean isDMS() {
        return false;
    }

    public static final boolean isInServer() {
        return false;
    }

    @Deprecated
    public static final boolean isJDK14() {
        return true;
    }

    public static final boolean isDebug() {
        return false;
    }

    public static final boolean isPrivateDebug() {
        return false;
    }

    public static final String getJDBCVersion() {
        return "JDBC 4.2";
    }

    public static final String getDriverVersion() {
        return "21.6.0.0.0";
    }

    public static final String getBuildDate() {
        return "Tue_May_31_04:02:37_PDT_2022";
    }

    public static final String getCompilerVersion() {
        return "javac 1.8.0_331";
    }
}

