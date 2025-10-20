/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.Datum;
import oracle.sql.ROWID;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class RowidAccessor
extends Accessor {
    static final int MAXLENGTH = 4000;
    static final int EXTENDED_ROWID_MAX_LENGTH = 18;

    RowidAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(Representation.ROWID, oracleStatement, 4000, bl);
        this.init(oracleStatement, 104, 9, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    RowidAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(Representation.ROWID, oracleStatement, 4000, false);
        this.init(oracleStatement, 104, 9, s2, false);
        this.initForDescribe(104, n2, bl, n3, n4, n5, l2, n6, s2, null);
        this.initForDataAccess(0, n2, null);
    }

    @Override
    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        if (n2 != 0) {
            this.externalType = n2;
        }
        this.byteLength = this.representationMaxLength + 2;
    }

    @Override
    String getString(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return this.rowData.getString(this.getOffset(n2), this.getLength(n2), this.statement.connection.conversion.getCharacterSet((short)1));
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getROWID(n2);
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getROWID(n2);
    }

    @Override
    ROWID getROWID(int n2) throws SQLException {
        byte[] byArray = this.getBytes(n2);
        return byArray == null ? null : new ROWID(byArray);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.getROWID(n2);
    }
}

