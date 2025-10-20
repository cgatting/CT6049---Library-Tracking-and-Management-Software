/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.datasource;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import javax.sql.DataSource;
import oracle.jdbc.OracleConnectionBuilder;
import oracle.jdbc.datasource.OracleCommonDataSource;
import oracle.jdbc.driver.OracleDriver;

public interface OracleDataSource
extends DataSource,
OracleCommonDataSource {
    public OracleConnectionBuilder createConnectionBuilder() throws SQLException;

    public static void setExecutorService(ExecutorService executorService) throws SQLException {
        OracleDriver.setExecutorService(executorService);
    }
}

