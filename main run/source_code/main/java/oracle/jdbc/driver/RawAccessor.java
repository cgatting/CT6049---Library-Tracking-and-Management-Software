/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.RawCommonAccessor;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class RawAccessor
extends RawCommonAccessor {
    RawAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(oracleStatement, oracleStatement.sqlKind == OracleStatement.SqlKind.PLSQL_BLOCK ? Math.max(oracleStatement.connection.maxRawBytesPlsql, n2) : oracleStatement.connection.maxRawLength, bl);
        this.init(oracleStatement, 23, 15, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    RawAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(oracleStatement, oracleStatement.sqlKind == OracleStatement.SqlKind.PLSQL_BLOCK ? oracleStatement.connection.maxRawBytesPlsql : oracleStatement.connection.maxRawLength, false);
        this.init(oracleStatement, 23, 15, s2, false);
        this.initForDescribe(23, n2, bl, n3, n4, n5, l2, n6, s2, null);
        int n7 = oracleStatement.maxFieldSize;
        if (n7 > 0 && (n2 == 0 || n7 < n2)) {
            n2 = n7;
        }
        this.initForDataAccess(0, n2, null);
    }

    @Override
    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        super.initForDataAccess(n2, n3, string);
        this.byteLength += 2;
    }
}

