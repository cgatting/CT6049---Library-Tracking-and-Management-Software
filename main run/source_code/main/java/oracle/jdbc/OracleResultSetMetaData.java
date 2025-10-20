/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public interface OracleResultSetMetaData
extends ResultSetMetaData {
    public boolean isNCHAR(int var1) throws SQLException;

    public SecurityAttribute getSecurityAttribute(int var1) throws SQLException;

    public boolean isColumnInvisible(int var1) throws SQLException;

    public boolean isVariableScale(int var1) throws SQLException;

    public boolean isColumnJSON(int var1) throws SQLException;

    public static enum SecurityAttribute {
        NONE,
        ENABLED,
        UNKNOWN;

    }
}

