/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class ResultSetAccessor
extends Accessor {
    static final int MAXLENGTH = 16;
    OracleStatement currentStmt;

    ResultSetAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(Representation.RESULT_SET, oracleStatement, 16, bl);
        this.init(oracleStatement, 102, 116, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    ResultSetAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(Representation.RESULT_SET, oracleStatement, 16, false);
        this.init(oracleStatement, 102, 116, s2, false);
        this.initForDescribe(102, n2, bl, n3, n4, n5, l2, n6, s2, null);
        this.initForDataAccess(0, n2, null);
    }

    @Override
    ResultSet getCursor(int n2) throws SQLException {
        OracleResultSet oracleResultSet = null;
        if (this.currentStmt != null && this.currentStmt.refCursorRowNumber == n2 && !this.currentStmt.isClosed()) {
            oracleResultSet = this.currentStmt.createResultSet();
        } else {
            byte[] byArray = this.getBytes(n2);
            OracleStatement oracleStatement = this.statement.connection.RefCursorBytesToStatement(byArray, this.statement);
            oracleStatement.refCursorRowNumber = n2;
            oracleStatement.doDescribe(false);
            if (oracleStatement.numberOfDefinePositions > 0) {
                oracleStatement.prepareAccessors();
            }
            oracleStatement.setPrefetchInternal(this.statement.getFetchSize(), false, false);
            oracleStatement.closeOnCompletion();
            oracleStatement.currentResultSet = oracleResultSet = oracleStatement.createResultSet();
            this.currentStmt = oracleStatement;
        }
        return oracleResultSet;
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getCursor(n2);
    }
}

