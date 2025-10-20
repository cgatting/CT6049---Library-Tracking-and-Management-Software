/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.LobCommonAccessor;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.BFILE;
import oracle.sql.Datum;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class BfileAccessor
extends LobCommonAccessor {
    static final int MAXLENGTH = 530;

    BfileAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(Representation.BFILE, oracleStatement, 530, bl);
        this.init(oracleStatement, 114, 114, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    BfileAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(Representation.BFILE, oracleStatement, 530, false);
        this.init(oracleStatement, 114, 114, s2, false);
        this.initForDescribe(114, n2, bl, n3, n4, n5, l2, n6, s2, null);
        this.initForDataAccess(0, n2, null);
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getBFILE(n2);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.getBFILE(n2);
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getBFILE(n2);
    }

    @Override
    BFILE getBFILE(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        BFILE bFILE = new BFILE(this.statement.connection, this.getBytesInternal(n2));
        if (this.isPrefetched()) {
            bFILE.setLength(this.getPrefetchedLength(n2));
        }
        return bFILE;
    }

    @Override
    InputStream getAsciiStream(int n2) throws SQLException {
        BFILE bFILE = this.getBFILE(n2);
        if (bFILE == null) {
            return null;
        }
        return bFILE.asciiStreamValue();
    }

    @Override
    Reader getCharacterStream(int n2) throws SQLException {
        BFILE bFILE = this.getBFILE(n2);
        if (bFILE == null) {
            return null;
        }
        return bFILE.characterStreamValue();
    }

    @Override
    InputStream getBinaryStream(int n2) throws SQLException {
        BFILE bFILE = this.getBFILE(n2);
        if (bFILE == null) {
            return null;
        }
        return bFILE.getBinaryStream();
    }

    @Override
    byte[] getBytes(int n2) throws SQLException {
        BFILE bFILE = this.getBFILE(n2);
        if (bFILE == null) {
            return null;
        }
        InputStream inputStream = bFILE.getBinaryStream();
        int n3 = 4096;
        int n4 = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(n3);
        byte[] byArray = new byte[n3];
        try {
            while ((n4 = inputStream.read(byArray)) != -1) {
                byteArrayOutputStream.write(byArray, 0, n4);
            }
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 151).fillInStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }
}

