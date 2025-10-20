/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.aq;

import java.sql.SQLException;

public interface AQAgent {
    public void setAddress(String var1) throws SQLException;

    public String getAddress();

    public void setName(String var1) throws SQLException;

    public String getName();

    public void setProtocol(int var1) throws SQLException;

    public int getProtocol();

    public String toString();
}

