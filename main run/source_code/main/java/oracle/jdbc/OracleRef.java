/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.Ref;
import java.sql.SQLException;
import oracle.jdbc.OracleTypeMetaData;

public interface OracleRef
extends Ref {
    public OracleTypeMetaData getOracleMetaData() throws SQLException;
}

