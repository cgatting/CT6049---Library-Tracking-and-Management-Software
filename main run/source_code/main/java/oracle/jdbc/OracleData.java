/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface OracleData {
    public Object toJDBCObject(Connection var1) throws SQLException;
}

