/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.RawCommonAccessor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class OutRawAccessor
extends RawCommonAccessor {
    static final int MAXLENGTH = Short.MAX_VALUE;

    OutRawAccessor(OracleStatement oracleStatement, int n2, short s2, int n3) throws SQLException {
        super(oracleStatement, Short.MAX_VALUE, true);
        this.init(oracleStatement, 23, 23, s2, true);
        this.initForDataAccess(n3, n2, null);
    }
}

