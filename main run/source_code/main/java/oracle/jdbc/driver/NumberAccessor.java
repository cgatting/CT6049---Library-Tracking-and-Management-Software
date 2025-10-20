/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.NumberCommonAccessor;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NumberAccessor
extends NumberCommonAccessor {
    static final int MAXLENGTH = 21;

    NumberAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(oracleStatement, 21, bl);
        this.init(oracleStatement, n2, s2, n3, bl);
    }

    NumberAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(oracleStatement, 21, false);
        this.init(oracleStatement, 2, n2, bl, n3, n4, n5, l2, n6, s2);
    }
}

