/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.dcn;

import java.sql.SQLException;
import java.util.concurrent.Executor;
import oracle.jdbc.NotificationRegistration;
import oracle.jdbc.dcn.DatabaseChangeListener;

public interface DatabaseChangeRegistration
extends NotificationRegistration {
    public int getRegistrationId();

    public long getRegId();

    public String[] getTables();

    public void addListener(DatabaseChangeListener var1) throws SQLException;

    public void addListener(DatabaseChangeListener var1, Executor var2) throws SQLException;

    public void removeListener(DatabaseChangeListener var1) throws SQLException;
}

