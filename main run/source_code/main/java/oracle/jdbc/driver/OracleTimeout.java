/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.OracleTimeoutThreadPerVM;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class OracleTimeout {
    OracleTimeout() {
    }

    static OracleTimeout newTimeout(String string) throws SQLException {
        OracleTimeoutThreadPerVM oracleTimeoutThreadPerVM = new OracleTimeoutThreadPerVM(string);
        return oracleTimeoutThreadPerVM;
    }

    abstract void setTimeout(long var1, OracleStatement var3) throws SQLException;

    abstract void cancelTimeout() throws SQLException;

    abstract void close() throws SQLException;
}

