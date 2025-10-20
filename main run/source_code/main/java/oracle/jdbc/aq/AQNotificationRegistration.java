/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.aq;

import java.sql.SQLException;
import java.util.concurrent.Executor;
import oracle.jdbc.NotificationRegistration;
import oracle.jdbc.aq.AQNotificationListener;

public interface AQNotificationRegistration
extends NotificationRegistration {
    public void addListener(AQNotificationListener var1) throws SQLException;

    public void addListener(AQNotificationListener var1, Executor var2) throws SQLException;

    public void removeListener(AQNotificationListener var1) throws SQLException;

    public String getQueueName();
}

