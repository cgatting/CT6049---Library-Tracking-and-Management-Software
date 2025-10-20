/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.SQLException;
import oracle.jdbc.OracleTypeMetaData;

public interface OracleOpaque {
    public OracleTypeMetaData getOracleMetaData() throws SQLException;

    public String getSQLTypeName() throws SQLException;

    public Object getValue() throws SQLException;
}

