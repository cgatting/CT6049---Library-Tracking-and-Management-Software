/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import oracle.jdbc.OracleTypeMetaData;

public interface AdditionalDatabaseMetaData
extends DatabaseMetaData {
    public OracleTypeMetaData getOracleTypeMetaData(String var1) throws SQLException;

    public long getLobMaxLength() throws SQLException;

    public String getAuditBanner() throws SQLException;

    public String getAccessBanner() throws SQLException;
}

