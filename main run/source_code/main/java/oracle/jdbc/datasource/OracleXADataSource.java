/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.datasource;

import java.sql.SQLException;
import javax.sql.XADataSource;
import oracle.jdbc.OracleXAConnectionBuilder;
import oracle.jdbc.datasource.OracleCommonDataSource;

public interface OracleXADataSource
extends XADataSource,
OracleCommonDataSource {
    public OracleXAConnectionBuilder createXAConnectionBuilder() throws SQLException;
}

