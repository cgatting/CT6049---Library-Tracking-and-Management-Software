/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.SQLException;
import oracle.jdbc.OracleData;

public interface OracleDataFactory {
    public OracleData create(Object var1, int var2) throws SQLException;
}

