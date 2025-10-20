/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

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
import oracle.sql.BLOB;
import oracle.sql.Datum;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class BlobAccessor
extends LobCommonAccessor {
    static final int MAXLENGTH = 4000;

    BlobAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(Representation.BLOB, oracleStatement, 4000, bl);
        this.init(oracleStatement, 113, 113, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    BlobAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(Representation.BLOB, oracleStatement, 4000, false);
        this.init(oracleStatement, 113, 113, s2, false);
        this.initForDescribe(113, n2, bl, n3, n4, n5, l2, n6, s2, null);
        this.initForDataAccess(0, n2, null);
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getBLOB(n2);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.getBLOB(n2);
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getBLOB(n2);
    }

    @Override
    BLOB getBLOB(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        BLOB bLOB = new BLOB(this.statement.connection, this.getBytesInternal(n2));
        if (this.isPrefetched()) {
            bLOB.setActivePrefetch(true);
            bLOB.setLength(this.getPrefetchedLength(n2));
            bLOB.setChunkSize(this.getPrefetchedChunkSize(n2));
            bLOB.setPrefetchedData(this.getPrefetchedData(n2));
        }
        if (bLOB.isTemporary()) {
            this.statement.connection.addTemporaryLob(bLOB);
        }
        return bLOB;
    }

    @Override
    InputStream getAsciiStream(int n2) throws SQLException {
        BLOB bLOB = this.getBLOB(n2);
        if (bLOB == null) {
            return null;
        }
        if (bLOB.isTemporary()) {
            this.statement.addToTempLobsToFree(bLOB);
        }
        return bLOB.asciiStreamValue();
    }

    @Override
    Reader getCharacterStream(int n2) throws SQLException {
        BLOB bLOB = this.getBLOB(n2);
        if (bLOB == null) {
            return null;
        }
        if (bLOB.isTemporary()) {
            this.statement.addToTempLobsToFree(bLOB);
        }
        return bLOB.characterStreamValue(true);
    }

    @Override
    InputStream getBinaryStream(int n2) throws SQLException {
        BLOB bLOB = this.getBLOB(n2);
        if (bLOB == null) {
            return null;
        }
        if (bLOB.isTemporary()) {
            this.statement.addToTempLobsToFree(bLOB);
        }
        return bLOB.getBinaryStream(true);
    }

    @Override
    byte[] getBytes(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        if (this.isPrefetched() && this.getPrefetchedLength(n2) > Integer.MAX_VALUE) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 151).fillInStackTrace();
        }
        if (this.isPrefetched() && (long)this.getPrefetchedDataLength(n2) == this.getPrefetchedLength(n2)) {
            return this.getPrefetchedData(n2);
        }
        BLOB bLOB = this.getBLOB(n2);
        if (bLOB == null) {
            return null;
        }
        if (bLOB.isTemporary()) {
            this.statement.addToTempLobsToFree(bLOB);
        }
        return bLOB.getBytes(1L, (int)bLOB.length());
    }

    @Override
    String getString(int n2) throws SQLException {
        this.unimpl("getString/getNString");
        return null;
    }

    @Override
    String getNString(int n2) throws SQLException {
        this.unimpl("getNString");
        return null;
    }

    @Override
    long updateChecksum(long l2, int n2) throws SQLException {
        this.unimpl("updateChecksum");
        return -1L;
    }
}

