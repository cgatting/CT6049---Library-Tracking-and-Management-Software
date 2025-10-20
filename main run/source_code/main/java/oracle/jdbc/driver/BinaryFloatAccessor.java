/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.Datum;
import oracle.sql.NUMBER;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class BinaryFloatAccessor
extends Accessor {
    static final int MAXLENGTH = 4;
    private final byte[] tmpBytes;

    BinaryFloatAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(Representation.BINARY_FLOAT, oracleStatement, 4, bl);
        this.tmpBytes = new byte[this.representationMaxLength];
        this.init(oracleStatement, 100, 100, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    BinaryFloatAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(Representation.BINARY_FLOAT, oracleStatement, 4, false);
        this.tmpBytes = new byte[this.representationMaxLength];
        this.init(oracleStatement, 100, 100, s2, false);
        this.initForDescribe(100, n2, bl, n3, n4, n5, l2, n6, s2, null);
        int n7 = oracleStatement.maxFieldSize;
        if (n7 > 0 && (n2 == 0 || n7 < n2)) {
            n2 = n7;
        }
        this.initForDataAccess(0, n2, null);
    }

    void init(OracleStatement oracleStatement, int n2, int n3, int n4, short s2, int n5) throws SQLException {
        this.init(oracleStatement, n2, n3, s2, false);
        this.initForDataAccess(n5, n4, null);
    }

    void init(OracleStatement oracleStatement, int n2, int n3, int n4, boolean bl, int n5, int n6, int n7, long l2, int n8, short s2) throws SQLException {
        this.init(oracleStatement, n2, n3, s2, false);
        this.initForDescribe(n2, n4, bl, n5, n6, n7, l2, n8, s2, null);
        int n9 = oracleStatement.maxFieldSize;
        if (n9 > 0 && (n4 == 0 || n9 < n4)) {
            n4 = n9;
        }
        this.initForDataAccess(0, n4, null);
    }

    @Override
    float getFloat(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return 0.0f;
        }
        this.rowData.get(this.getOffset(n2), this.tmpBytes, 0, this.representationMaxLength);
        int n3 = this.tmpBytes[0];
        int n4 = this.tmpBytes[1];
        int n5 = this.tmpBytes[2];
        int n6 = this.tmpBytes[3];
        if ((n3 & 0x80) != 0) {
            n3 &= 0x7F;
            n4 &= 0xFF;
            n5 &= 0xFF;
            n6 &= 0xFF;
        } else {
            n3 = ~n3 & 0xFF;
            n4 = ~n4 & 0xFF;
            n5 = ~n5 & 0xFF;
            n6 = ~n6 & 0xFF;
        }
        int n7 = n3 << 24 | n4 << 16 | n5 << 8 | n6;
        return Float.intBitsToFloat(n7);
    }

    @Override
    String getString(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return Float.toString(this.getFloat(n2));
    }

    @Override
    Object getObject(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return new Float(this.getFloat(n2));
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return new Float(this.getFloat(n2));
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getBINARY_FLOAT(n2);
    }

    @Override
    BINARY_FLOAT getBINARY_FLOAT(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return new BINARY_FLOAT(this.rowData.get(this.getOffset(n2), this.getLength(n2)));
    }

    @Override
    NUMBER getNUMBER(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return new NUMBER(this.getFloat(n2));
    }

    @Override
    BigInteger getBigInteger(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return this.getBigDecimal(n2).toBigInteger();
    }

    @Override
    BigDecimal getBigDecimal(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        return new BigDecimal(this.getString(n2));
    }

    @Override
    byte getByte(int n2) throws SQLException {
        float f2 = this.getFloat(n2);
        if (f2 >= -128.0f && f2 <= 127.0f) {
            return (byte)f2;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 26).fillInStackTrace();
    }

    @Override
    short getShort(int n2) throws SQLException {
        float f2 = this.getFloat(n2);
        if (f2 >= -32768.0f && f2 <= 32767.0f) {
            return (short)f2;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 26).fillInStackTrace();
    }

    @Override
    int getInt(int n2) throws SQLException {
        float f2 = this.getFloat(n2);
        if (f2 >= -2.14748365E9f && f2 <= 2.14748365E9f) {
            return (int)f2;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 26).fillInStackTrace();
    }

    @Override
    long getLong(int n2) throws SQLException {
        return (long)this.getFloat(n2);
    }

    @Override
    double getDouble(int n2) throws SQLException {
        return this.getFloat(n2);
    }
}

