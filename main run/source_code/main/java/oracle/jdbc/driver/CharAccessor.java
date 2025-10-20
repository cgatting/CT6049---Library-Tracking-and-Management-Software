/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.CharCommonAccessor;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class CharAccessor
extends CharCommonAccessor {
    static final int MAXLENGTH = 2000;

    CharAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(oracleStatement, oracleStatement.sqlKind == OracleStatement.SqlKind.PLSQL_BLOCK ? oracleStatement.connection.maxVcsBytesPlsql : (n2 > 2000 ? n2 : 2000), s2, bl);
        this.init(oracleStatement, 96, 9, n2, s2, n3, bl, n2 > this.representationMaxLength ? n2 : this.representationMaxLength);
    }

    CharAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2, int n7) throws SQLException {
        super(oracleStatement, oracleStatement.sqlKind == OracleStatement.SqlKind.PLSQL_BLOCK ? oracleStatement.connection.maxVcsBytesPlsql : (n2 > 2000 ? n2 : 2000), s2, false);
        this.init(oracleStatement, 96, 9, n2, bl, n3, n4, n5, l2, n6, s2, n7);
    }
}

