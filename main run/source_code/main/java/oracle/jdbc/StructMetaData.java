/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.SQLException;
import oracle.jdbc.OracleResultSetMetaData;

public interface StructMetaData
extends OracleResultSetMetaData {
    public String getAttributeJavaName(int var1) throws SQLException;

    public String getOracleColumnClassName(int var1) throws SQLException;

    public boolean isInherited(int var1) throws SQLException;

    public int getLocalColumnCount() throws SQLException;
}

