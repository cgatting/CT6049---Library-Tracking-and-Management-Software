/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.util.Properties;

public interface NotificationRegistration {
    public Properties getRegistrationOptions();

    public String getDatabaseName();

    public String getUserName();

    public RegistrationState getState();

    public Exception getRegistrationException();

    public static enum RegistrationState {
        ACTIVE,
        CLOSED,
        DISABLED;

    }
}

