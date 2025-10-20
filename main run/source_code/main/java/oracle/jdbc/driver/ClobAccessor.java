/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.LobCommonAccessor;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CLOB;
import oracle.sql.CharacterSet;
import oracle.sql.Datum;
import oracle.sql.NCLOB;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class ClobAccessor
extends LobCommonAccessor {
    static final int MAXLENGTH = 4000;
    int[] prefetchedDataCharset;
    int[] prefetchedDataFormOfUse;
    private CharacterSet cachedCharSet = null;
    private CharacterSet ucs2CompatibleCharSet = null;

    ClobAccessor(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        super(s2 == 2 ? Representation.NCLOB : Representation.CLOB, oracleStatement, 4000, bl);
        this.init(oracleStatement, 112, 112, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    ClobAccessor(OracleStatement oracleStatement, int n2, boolean bl, int n3, int n4, int n5, long l2, int n6, short s2) throws SQLException {
        super(s2 == 2 ? Representation.NCLOB : Representation.CLOB, oracleStatement, 4000, false);
        this.init(oracleStatement, 112, 112, s2, false);
        this.initForDescribe(112, n2, bl, n3, n4, n5, l2, n6, s2, null);
        this.initForDataAccess(0, n2, null);
    }

    @Override
    void setCapacity(int n2) {
        super.setCapacity(n2);
        if (this.prefetchedDataCharset == null) {
            this.prefetchedDataCharset = new int[n2];
            this.prefetchedDataFormOfUse = new int[n2];
        } else if (n2 > this.prefetchedDataCharset.length) {
            int[] nArray = new int[n2];
            System.arraycopy(this.prefetchedDataCharset, 0, nArray, 0, this.prefetchedDataCharset.length);
            this.prefetchedDataCharset = nArray;
            nArray = new int[n2];
            System.arraycopy(this.prefetchedDataFormOfUse, 0, nArray, 0, this.prefetchedDataFormOfUse.length);
            this.prefetchedDataFormOfUse = nArray;
        }
    }

    @Override
    void insertNull(int n2) throws SQLException {
        System.arraycopy(this.prefetchedDataCharset, n2, this.prefetchedDataCharset, n2 + 1, this.prefetchedDataCharset.length - n2 - 1);
        System.arraycopy(this.prefetchedDataFormOfUse, n2, this.prefetchedDataFormOfUse, n2 + 1, this.prefetchedDataFormOfUse.length - n2 - 1);
        super.insertNull(n2);
    }

    @Override
    Accessor copyForDefine(OracleStatement oracleStatement) {
        ClobAccessor clobAccessor = (ClobAccessor)super.copyForDefine(oracleStatement);
        clobAccessor.prefetchedDataCharset = null;
        clobAccessor.prefetchedDataFormOfUse = null;
        return clobAccessor;
    }

    @Override
    protected void copyFromInternal(Accessor accessor, int n2, int n3) throws SQLException {
        super.copyFromInternal(accessor, n2, n3);
        if (this.isPrefetched()) {
            ClobAccessor clobAccessor = (ClobAccessor)accessor;
            this.setPrefetchedDataCharset(n3, clobAccessor.getPrefetchedDataCharset(n2));
            this.setPrefetchedDataFormOfUse(n3, clobAccessor.getPrefetchedDataFormOfUse(n2));
        }
    }

    @Override
    void deleteRow(int n2) throws SQLException {
        super.deleteRow(n2);
        if (this.isPrefetched()) {
            this.delete(this.prefetchedDataCharset, n2);
            this.delete(this.prefetchedDataFormOfUse, n2);
        }
    }

    final int getPrefetchedDataCharset(int n2) {
        return this.prefetchedDataCharset[n2];
    }

    final void setPrefetchedDataCharset(int n2, int n3) {
        this.prefetchedDataCharset[n2] = n3;
    }

    final int getPrefetchedDataFormOfUse(int n2) {
        return this.prefetchedDataFormOfUse[n2];
    }

    final void setPrefetchedDataFormOfUse(int n2, int n3) {
        this.prefetchedDataFormOfUse[n2] = n3;
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getCLOB(n2);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.getCLOB(n2);
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getCLOB(n2);
    }

    protected void normalizeFormOfUse(byte[] byArray) {
        short s2 = oracle.sql.CLOB.getFormOfUseFromLocator(byArray);
        if (s2 != -1) {
            this.formOfUse = s2;
        }
    }

    private CLOB getCLOB_(int n2, byte[] byArray) throws SQLException {
        CLOB cLOB = this.formOfUse == 1 ? new CLOB((OracleConnection)this.statement.connection, byArray, this.formOfUse) : new NCLOB(this.statement.connection, byArray);
        if (this.isPrefetched()) {
            cLOB.setActivePrefetch(true);
            cLOB.setLength(this.getPrefetchedLength(n2));
            cLOB.setChunkSize(this.getPrefetchedChunkSize(n2));
            int[] nArray = new int[1];
            cLOB.setPrefetchedData(this.getPrefetchedCharData(n2, nArray), nArray[0]);
        }
        if (cLOB.isTemporary()) {
            this.statement.connection.addTemporaryLob(cLOB);
        }
        return cLOB;
    }

    @Override
    CLOB getCLOB(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        byte[] byArray = this.getBytesInternal(n2);
        this.normalizeFormOfUse(byArray);
        CLOB cLOB = this.getCLOB_(n2, byArray);
        return cLOB;
    }

    @Override
    NCLOB getNCLOB(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        byte[] byArray = this.getBytesInternal(n2);
        this.normalizeFormOfUse(byArray);
        if (this.formOfUse != 2) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 132).fillInStackTrace();
        }
        return (NCLOB)this.getCLOB_(n2, byArray);
    }

    final char[] XgetPrefetchedCharData(int n2, int[] nArray) throws SQLException {
        if (this.getPrefetchLength() == -1) {
            return null;
        }
        int n3 = -1;
        int n4 = this.getPrefetchedDataLength(n2);
        char[] cArray = new char[this.getPrefetchedDataLength(n2)];
        byte[] byArray = this.rowData.get(this.getPrefetchedDataOffset(n2), this.getPrefetchedDataLength(n2));
        if (this.getPrefetchedDataCharset(n2) != 0) {
            n3 = this.getPrefetchedDataCharset(n2) == 2000 ? CharacterSet.convertAL16UTF16BytesToJavaChars(byArray, 0, cArray, 0, n4, true) : CharacterSet.convertAL16UTF16LEBytesToJavaChars(byArray, 0, cArray, 0, n4, true);
        } else {
            int[] nArray2 = new int[]{n4};
            n3 = this.formOfUse == 1 ? this.statement.connection.conversion.CHARBytesToJavaChars(byArray, 0, cArray, 0, nArray2, cArray.length) : this.statement.connection.conversion.NCHARBytesToJavaChars(byArray, 0, cArray, 0, nArray2, cArray.length);
        }
        nArray[0] = n3;
        return cArray;
    }

    final char[] getPrefetchedCharData(int n2, int[] nArray) throws SQLException {
        if (this.getPrefetchLength() == -1) {
            return null;
        }
        if (this.getPrefetchedDataCharset(n2) == 0) {
            char[] cArray = this.rowData.getChars(this.getPrefetchedDataOffset(n2), this.getPrefetchedDataLength(n2), this.statement.connection.conversion.getCharacterSet((short)this.getPrefetchedDataFormOfUse(n2)), nArray);
            return cArray;
        }
        if (this.cachedCharSet == null || this.cachedCharSet.getOracleId() != this.getPrefetchedDataCharset(n2)) {
            this.cachedCharSet = CharacterSet.make(this.getPrefetchedDataCharset(n2));
        }
        char[] cArray = this.rowData.getChars(this.getPrefetchedDataOffset(n2), this.getPrefetchedDataLength(n2), this.cachedCharSet, nArray);
        return cArray;
    }

    @Override
    InputStream getAsciiStream(int n2) throws SQLException {
        CLOB cLOB = this.getCLOB(n2);
        if (cLOB == null) {
            return null;
        }
        return cLOB.getAsciiStream(true);
    }

    @Override
    Reader getCharacterStream(int n2) throws SQLException {
        CLOB cLOB = this.getCLOB(n2);
        if (cLOB == null) {
            return null;
        }
        return cLOB.getCharacterStream();
    }

    @Override
    InputStream getBinaryStream(int n2) throws SQLException {
        CLOB cLOB = this.getCLOB(n2);
        if (cLOB == null) {
            return null;
        }
        return cLOB.getAsciiStream();
    }

    @Override
    String getString(int n2) throws SQLException {
        if (this.isNull(n2)) {
            return null;
        }
        if (this.isPrefetched() && this.getPrefetchedLength(n2) > Integer.MAX_VALUE) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 151).fillInStackTrace();
        }
        CLOB cLOB = this.getCLOB(n2);
        if (cLOB == null) {
            return null;
        }
        if (cLOB.isTemporary()) {
            this.statement.addToTempLobsToFree(cLOB);
        }
        if (this.isPrefetched()) {
            if (this.statement.definedColumnSize != null && this.statement.definedColumnSize.length > n2 && this.getPrefetchedLength(n2) <= (long)this.statement.definedColumnSize[n2] && this.getPrefetchedDataCharset(n2) != 0) {
                if (this.ucs2CompatibleCharSet == null || this.ucs2CompatibleCharSet.getOracleId() != this.getPrefetchedDataCharset(n2)) {
                    this.ucs2CompatibleCharSet = this.getPrefetchedDataCharset(n2) == 2000 ? CharacterSet.make(2000) : CharacterSet.make(2002);
                }
                return this.rowData.getString(this.getPrefetchedDataOffset(n2), this.getPrefetchedDataLength(n2), this.ucs2CompatibleCharSet);
            }
            return cLOB.getSubString(1L, (int)this.getPrefetchedLength(n2));
        }
        return this.getStringNoPrefetch(n2);
    }

    String getStringNoPrefetch(int n2) throws SQLException {
        CLOB cLOB = this.getCLOB(n2);
        if (cLOB == null) {
            return null;
        }
        Reader reader = cLOB.getCharacterStream();
        int n3 = cLOB.getBufferSize();
        int n4 = 0;
        StringWriter stringWriter = new StringWriter(n3);
        char[] cArray = new char[n3];
        try {
            while ((n4 = reader.read(cArray)) != -1) {
                stringWriter.write(cArray, 0, n4);
            }
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 151).fillInStackTrace();
        }
        if (cLOB.isTemporary()) {
            this.statement.addToTempLobsToFree(cLOB);
        }
        return stringWriter.getBuffer().substring(0);
    }

    @Override
    byte[] getBytes(int n2) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getBytes").fillInStackTrace();
    }

    @Override
    long updateChecksum(long l2, int n2) throws SQLException {
        this.unimpl("updateChecksum");
        return -1L;
    }
}

