/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.driver.TypeAccessor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.oracore.OracleType;
import oracle.jdbc.oracore.OracleTypeADT;
import oracle.sql.Datum;
import oracle.sql.REF;
import oracle.sql.StructDescriptor;
import oracle.sql.TypeDescriptor;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class RefTypeAccessor
extends TypeAccessor {
    static final int MAXLENGTH = -1;

    RefTypeAccessor(OracleStatement oracleStatement, String string, short s2, int n2, boolean bl) throws SQLException {
        super(Representation.REF_TYPE, oracleStatement, -1, bl);
        this.init(oracleStatement, 111, 111, s2, bl);
        this.initForDataAccess(n2, 0, string);
    }

    RefTypeAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2, String string) throws SQLException {
        super(Representation.REF_TYPE, oracleStatement, -1, false);
        this.init(oracleStatement, 111, 111, s2, false);
        this.initForDescribe(111, n2, bl, n3, n4, n5, l2, n6, s2, string);
        this.initForDataAccess(0, n2, string);
    }

    RefTypeAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2, String string, OracleType oracleType) throws SQLException {
        super(Representation.REF_TYPE, oracleStatement, -1, false);
        this.init(oracleStatement, 111, 111, s2, false);
        this.describeOtype = oracleType;
        this.initForDescribe(111, n2, bl, n3, n4, n5, l2, n6, s2, string);
        this.internalOtype = oracleType;
        this.initForDataAccess(0, n2, string);
    }

    @Override
    final OracleType otypeFromName(String string) throws SQLException {
        if (!this.outBind) {
            return TypeDescriptor.getTypeDescriptor(string, this.statement.connection).getPickler();
        }
        return StructDescriptor.createDescriptor(string, (Connection)this.statement.connection).getOracleTypeADT();
    }

    @Override
    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        super.initForDataAccess(n2, n3, string);
        this.byteLength = this.statement.connection.refTypeAccessorByteLen;
    }

    @Override
    REF getREF(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        byte[] byArray = this.pickledBytes(n2);
        OracleTypeADT oracleTypeADT = (OracleTypeADT)this.internalOtype;
        return new REF(oracleTypeADT.getFullName(), (Connection)this.statement.connection, byArray);
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getObject(n2, this.statement.connection.getTypeMap());
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getREF(n2);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        REF rEF = this.getREF(n2);
        if (rEF == null) {
            return null;
        }
        return rEF.toJdbc(map);
    }
}

