/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.SQLException;
import java.sql.Struct;
import oracle.jdbc.OracleTypeMetaData;

public interface OracleStruct
extends Struct {
    public OracleTypeMetaData getOracleMetaData() throws SQLException;
}

