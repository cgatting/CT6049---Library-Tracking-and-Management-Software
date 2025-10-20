/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.io.ByteArrayOutputStream;
import javax.crypto.Cipher;
import oracle.jdbc.driver.BuildInfo;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@Supports(value={Feature.METADATA})
@DefaultLogger(value="oracle.jdbc")
public class OracleDriver
extends oracle.jdbc.driver.OracleDriver {
    public static final boolean isDMS() {
        return BuildInfo.isDMS();
    }

    public static final boolean isInServer() {
        return BuildInfo.isInServer();
    }

    public static final boolean isJDK14() {
        return BuildInfo.isJDK14();
    }

    public static final boolean isDebug() {
        return BuildInfo.isDebug();
    }

    public static final boolean isPrivateDebug() {
        return BuildInfo.isPrivateDebug();
    }

    public static final String getJDBCVersion() {
        return BuildInfo.getJDBCVersion();
    }

    public static final String getDriverVersion() {
        return BuildInfo.getDriverVersion();
    }

    public static final String getBuildDate() {
        return BuildInfo.getBuildDate();
    }

    public static void main(String[] stringArray) throws Exception {
        System.out.println("Oracle " + OracleDriver.getDriverVersion() + " " + OracleDriver.getJDBCVersion() + (OracleDriver.isDMS() ? " DMS" : "") + (OracleDriver.isPrivateDebug() ? " private" : "") + (OracleDriver.isDebug() ? " debug" : "") + (OracleDriver.isInServer() ? " for JAVAVM" : "") + " compiled with " + BuildInfo.getCompilerVersion() + " on " + OracleDriver.getBuildDate());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(128);
        DEFAULT_CONNECTION_PROPERTIES.store(byteArrayOutputStream, "Default Connection Properties Resource");
        System.out.println(byteArrayOutputStream.toString("ISO-8859-1"));
        int n2 = Cipher.getMaxAllowedKeyLength("AES");
        if (n2 < 256) {
            System.out.println("***** JCE UNLIMITED STRENGTH NOT INSTALLED ****");
        } else {
            System.out.println("***** JCE UNLIMITED STRENGTH IS INSTALLED ****");
        }
    }
}

