/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.util.RepConversion;
import oracle.sql.Datum;
import oracle.sql.RAW;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class RawCommonAccessor
extends Accessor {
    RawCommonAccessor(OracleStatement oracleStatement, int n2, boolean bl) {
        super(Representation.RAW, oracleStatement, n2, bl);
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
    String getString(int n2) throws SQLException {
        byte[] byArray = this.getBytes(n2);
        if (byArray == null) {
            return null;
        }
        int n3 = byArray.length;
        if (n3 == 0) {
            return null;
        }
        return RepConversion.bArray2String(byArray);
    }

    @Override
    InputStream getAsciiStream(int n2) throws SQLException {
        byte[] byArray = this.getBytes(n2);
        if (byArray == null) {
            return null;
        }
        PhysicalConnection physicalConnection = this.statement.connection;
        return physicalConnection.conversion.ConvertStream(new ByteArrayInputStream(byArray), 2);
    }

    @Override
    InputStream getUnicodeStream(int n2) throws SQLException {
        byte[] byArray = this.getBytes(n2);
        if (byArray == null) {
            return null;
        }
        PhysicalConnection physicalConnection = this.statement.connection;
        return physicalConnection.conversion.ConvertStream(new ByteArrayInputStream(byArray), 3);
    }

    @Override
    Reader getCharacterStream(int n2) throws SQLException {
        byte[] byArray = this.getBytes(n2);
        if (byArray == null) {
            return null;
        }
        int n3 = byArray.length;
        char[] cArray = new char[n3 << 1];
        int n4 = DBConversion.RAWBytesToHexChars(byArray, n3, cArray);
        return new CharArrayReader(cArray, 0, n4);
    }

    @Override
    InputStream getBinaryStream(int n2) throws SQLException {
        byte[] byArray = this.getBytes(n2);
        if (byArray == null) {
            return null;
        }
        return new ByteArrayInputStream(byArray);
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getBytes(n2);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.getBytes(n2);
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getRAW(n2);
    }

    @Override
    RAW getRAW(int n2) throws SQLException {
        byte[] byArray = this.getBytes(n2);
        if (byArray == null) {
            return null;
        }
        return new RAW(byArray);
    }
}

