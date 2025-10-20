/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.NumberCommonAccessor;
import oracle.jdbc.driver.OracleStatement;

class PlsqlBooleanAccessor
extends NumberCommonAccessor {
    static final int MAXLENGTH = 4;

    PlsqlBooleanAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(oracleStatement, 4, bl);
        this.init(oracleStatement, n2, s2, n3, bl);
    }

    PlsqlBooleanAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(oracleStatement, 4, false);
    }

    @Override
    void init(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        this.init(oracleStatement, 252, 252, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    @Override
    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        if (n2 != 0) {
            this.externalType = n2;
        }
        int n4 = this.representationMaxLength;
        if (n3 > 0 && n3 < n4) {
            n4 = n3;
        }
        this.byteLength = n4;
    }

    @Override
    boolean getBoolean(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return false;
        }
        return this.rowData.get(this.getOffset(n2)) != 0 || this.rowData.get(this.getOffset(n2) + 4L - 1L) != 0;
    }

    @Override
    String getString(int n2) throws SQLException {
        return String.valueOf(this.getBoolean(n2));
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getBoolean(n2);
    }
}

