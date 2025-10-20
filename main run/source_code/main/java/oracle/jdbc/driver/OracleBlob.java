/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLXML;
import oracle.jdbc.LargeObjectAccessMode;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.BLOB;
import oracle.sql.BlobDBAccess;
import oracle.sql.Datum;
import oracle.sql.DatumWithConnection;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.LOB_DATUM})
@Deprecated
public class OracleBlob
extends DatumWithConnection
implements oracle.jdbc.internal.OracleBlob {
    private static final long serialVersionUID = -5771953664462518027L;
    public static final int MAX_CHUNK_SIZE = 32768;
    public static final int DURATION_SESSION = 10;
    public static final int DURATION_CALL = 12;
    static final int OLD_WRONG_DURATION_SESSION = 1;
    static final int OLD_WRONG_DURATION_CALL = 2;
    public static final int MODE_READONLY = 0;
    public static final int MODE_READWRITE = 1;
    BlobDBAccess dbaccess;
    int dbChunkSize = -1;
    boolean isFree = false;
    boolean fromObject = false;
    private long cachedLobLength = -1L;
    private byte[] prefetchData;
    private int prefetchDataSize = 0;
    private boolean activePrefetch = false;
    static final int KDLCTLSIZE = 16;
    static final int KDF_FLAG = 88;
    static final int KDLIDDAT = 8;
    Object acProxy;

    public OracleBlob() {
    }

    public OracleBlob(OracleConnection oracleConnection) throws SQLException {
        this(oracleConnection, null);
    }

    public OracleBlob(OracleConnection oracleConnection, byte[] byArray, boolean bl) throws SQLException {
        this(oracleConnection, byArray);
        this.fromObject = bl;
    }

    public OracleBlob(OracleConnection oracleConnection, byte[] byArray) throws SQLException {
        super(byArray);
        OracleBlob.assertNotNull(oracleConnection);
        this.setPhysicalConnectionOf(oracleConnection);
        this.dbaccess = this.getPhysicalConnection().createBlobDBAccess();
        this.dbaccess.incrementTempLobReferenceCount(byArray);
        if (byArray != null && !this.isTemporary()) {
            ((oracle.jdbc.internal.OracleConnection)oracleConnection).addLargeObject(this);
        }
    }

    public void setFromobject(boolean bl) {
        this.fromObject = bl;
    }

    @Override
    public long length() throws SQLException {
        return this.lengthInternal();
    }

    @Override
    public final long lengthInternal() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        long l2 = this.activePrefetch && this.cachedLobLength != -1L ? this.cachedLobLength : (this.canReadBasicLobDataInLocator() ? (long)this.dilLength() : this.getDBAccess().length(this));
        return l2;
    }

    @Override
    public byte[] getBytes(long l2, int n2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (n2 < 0 || l2 < 1L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "getBytes()").fillInStackTrace();
        }
        if (this.canReadBasicLobDataInLocator()) {
            return this.dilGetBytes(l2, n2);
        }
        if (n2 == 0) {
            return new byte[0];
        }
        byte[] byArray = null;
        if (this.activePrefetch && (this.cachedLobLength == 0L || this.cachedLobLength > 0L && l2 - 1L >= this.cachedLobLength)) {
            byArray = null;
        } else {
            long l3 = 0L;
            byte[] byArray2 = this.activePrefetch && this.cachedLobLength != -1L ? new byte[Math.min((int)this.cachedLobLength, n2)] : new byte[n2];
            l3 = this.getBytes(l2, n2, byArray2);
            if (l3 > 0L) {
                if (l3 == (long)n2) {
                    byArray = byArray2;
                } else {
                    byArray = new byte[(int)l3];
                    System.arraycopy(byArray2, 0, byArray, 0, (int)l3);
                }
            }
        }
        return byArray;
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        return this.getBinaryStream(false);
    }

    public InputStream getBinaryStream(boolean bl) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (this.canReadBasicLobDataInLocator()) {
            return this.dilGetBinaryStream(1L);
        }
        return this.getDBAccess().newInputStream((oracle.jdbc.internal.OracleBlob)this, this.getBufferSize(), 0L, bl);
    }

    @Override
    public long position(byte[] byArray, long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().position((oracle.jdbc.internal.OracleBlob)this, (Datum)this, byArray, l2);
    }

    @Override
    public long position(Blob blob, long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().position((oracle.jdbc.internal.OracleBlob)this, (Datum)this, (Datum)((Object)blob), l2);
    }

    @Override
    public int getBytes(long l2, int n2, byte[] byArray) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (n2 < 0 || l2 < 1L || byArray == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "getBytes()").fillInStackTrace();
        }
        if (byArray.length < n2) {
            n2 = byArray.length;
        }
        return this.getDBAccess().getBytes(this, l2, n2, byArray);
    }

    @Override
    @Deprecated
    public int putBytes(long l2, byte[] byArray) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.setBytes(l2, byArray);
    }

    @Override
    @Deprecated
    public int putBytes(long l2, byte[] byArray, int n2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.setBytes(l2, byArray, 0, n2);
    }

    @Override
    @Deprecated
    public OutputStream getBinaryOutputStream() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.setBinaryStream(1L);
    }

    @Override
    public byte[] getLocator() {
        return this.getBytes();
    }

    @Override
    public void setLocator(byte[] byArray) {
        super.setBytes(byArray);
    }

    @Override
    public int getChunkSize() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (this.dbChunkSize <= 0) {
            this.dbChunkSize = this.getDBAccess().getChunkSize(this);
        }
        return this.dbChunkSize;
    }

    @Override
    public int getBufferSize() throws SQLException {
        int n2;
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        int n3 = n2 = this.getChunkSize();
        n3 = n2 >= 32768 || n2 <= 0 ? 32768 : 32768 / n2 * n2;
        return n3;
    }

    @Override
    public boolean isEmptyLob() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        boolean bl = (this.shareBytes()[5] & 0x10) != 0;
        return bl;
    }

    @Override
    public boolean isSecureFile() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        boolean bl = (this.shareBytes()[7] & 0xFFFFFF80) != 0;
        return bl;
    }

    @Override
    @Deprecated
    public OutputStream getBinaryOutputStream(long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().newOutputStream(this, this.getBufferSize(), l2, false);
    }

    @Override
    public InputStream getBinaryStream(long l2) throws SQLException {
        return this.getBinaryStream(l2, false);
    }

    public InputStream getBinaryStream(long l2, boolean bl) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "'position data' should be > 0.").fillInStackTrace();
        }
        if (this.canReadBasicLobDataInLocator()) {
            return this.dilGetBinaryStream(l2);
        }
        return this.getDBAccess().newInputStream((oracle.jdbc.internal.OracleBlob)this, this.getBufferSize(), l2, bl);
    }

    @Override
    @Deprecated
    public void trim(long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        this.truncate(l2);
    }

    @Override
    public void freeTemporary() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        int n2 = this.getDBAccess().decrementTempLobReferenceCount(this.shareBytes());
        if (n2 == 0) {
            this.getDBAccess().freeTemporary(this, this, this.fromObject);
        }
    }

    @Override
    public boolean isTemporary() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().isTemporary(this);
    }

    @Override
    public short getDuration() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().getDuration(this);
    }

    @Override
    public void open(LargeObjectAccessMode largeObjectAccessMode) throws SQLException {
        this.open(largeObjectAccessMode.getCode());
    }

    public void open(int n2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        this.getDBAccess().open(this, n2);
    }

    @Override
    public void close() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        this.getDBAccess().close(this);
    }

    @Override
    public boolean isOpen() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().isOpen(this);
    }

    @Override
    public int setBytes(long l2, byte[] byArray) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().putBytes(this, l2, byArray, 0, byArray != null ? byArray.length : 0);
    }

    @Override
    public int setBytes(long l2, byte[] byArray, int n2, int n3) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().putBytes(this, l2, byArray, n2, n3);
    }

    @Override
    public OutputStream setBinaryStream(long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "'position data' should be > 0.").fillInStackTrace();
        }
        return this.getDBAccess().newOutputStream(this, this.getBufferSize(), l2, true);
    }

    @Override
    public void truncate(long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "'len' should be >= 0. ").fillInStackTrace();
        }
        this.getDBAccess().trim(this, l2);
    }

    @Override
    public Object toJdbc() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this;
    }

    @Override
    public boolean isConvertibleTo(Class<?> clazz) {
        String string = clazz.getName();
        return string.compareTo("java.io.InputStream") == 0 || string.compareTo("java.io.Reader") == 0;
    }

    @Override
    public Reader characterStreamValue() throws SQLException {
        return this.characterStreamValue(false);
    }

    public Reader characterStreamValue(boolean bl) throws SQLException {
        return this.getDBAccess().newConversionReader(this, 8, bl);
    }

    @Override
    public InputStream asciiStreamValue() throws SQLException {
        return this.asciiStreamValue(false);
    }

    public InputStream asciiStreamValue(boolean bl) throws SQLException {
        return this.getDBAccess().newConversionInputStream(this, 2, bl);
    }

    @Override
    public InputStream binaryStreamValue() throws SQLException {
        return this.getBinaryStream();
    }

    @Override
    public InputStream binaryStreamValue(boolean bl) throws SQLException {
        return this.getBinaryStream(bl);
    }

    @Override
    public Object makeJdbcArray(int n2) {
        return new BLOB[n2];
    }

    @Override
    public BlobDBAccess getDBAccess() throws SQLException {
        if (this.dbaccess == null) {
            if (this.isEmptyLob()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 98).fillInStackTrace();
            }
            this.dbaccess = this.getInternalConnection().createBlobDBAccess();
        }
        if (this.getPhysicalConnection().isClosed()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        return this.dbaccess;
    }

    public static BlobDBAccess getDBAccess(Connection connection) throws SQLException {
        return ((OracleConnection)connection).physicalConnectionWithin().createBlobDBAccess();
    }

    @Override
    public Connection getJavaSqlConnection() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return super.getJavaSqlConnection();
    }

    @Override
    public final void setLength(long l2) {
        this.cachedLobLength = l2;
    }

    @Override
    public final void setChunkSize(int n2) {
        this.dbChunkSize = n2;
    }

    @Override
    public final void setPrefetchedData(byte[] byArray) {
        if (byArray == null) {
            this.setPrefetchedData(null, 0);
        } else {
            this.setPrefetchedData(byArray, byArray.length);
        }
    }

    @Override
    public final void setPrefetchedData(byte[] byArray, int n2) {
        this.prefetchData = byArray;
        this.prefetchDataSize = n2;
    }

    @Override
    public final byte[] getPrefetchedData() {
        return this.prefetchData;
    }

    @Override
    public final int getPrefetchedDataSize() {
        return this.prefetchDataSize;
    }

    @Override
    public final void setActivePrefetch(boolean bl) {
        if (this.activePrefetch && !bl) {
            this.clearCachedData();
        }
        this.activePrefetch = bl;
    }

    @Override
    public final void clearCachedData() {
        this.cachedLobLength = -1L;
        this.prefetchData = null;
    }

    @Override
    public final boolean isActivePrefetch() {
        return this.activePrefetch;
    }

    @Override
    public boolean canReadBasicLobDataInLocator() throws SQLException {
        int n2;
        byte[] byArray = this.shareBytes();
        if (byArray == null || byArray.length < 102) {
            return false;
        }
        if (!this.getPhysicalConnection().isDataInLocatorEnabled()) {
            return false;
        }
        int n3 = byArray[6] & 0xFF;
        int n4 = byArray[7] & 0xFF;
        boolean bl = (n3 & 8) == 8;
        boolean bl2 = (n4 & 0xFFFFFF80) == -128;
        boolean bl3 = false;
        if (bl && !bl2) {
            n2 = byArray[88] & 0xFF;
            bl3 = (n2 & 8) == 8;
        }
        n2 = bl && !bl2 && bl3 ? 1 : 0;
        return n2 != 0;
    }

    int dilLength() {
        return this.shareBytes().length - 86 - 16;
    }

    byte[] dilGetBytes(long l2, int n2) throws SQLException {
        if (n2 == 0) {
            return new byte[0];
        }
        if (this.dilLength() == 0) {
            return null;
        }
        int n3 = (int)Math.min((long)n2, (long)this.dilLength() - (l2 - 1L));
        if (n3 <= 0) {
            return null;
        }
        byte[] byArray = new byte[n3];
        System.arraycopy(this.shareBytes(), (int)(l2 - 1L) + 86 + 16, byArray, 0, n3);
        return byArray;
    }

    InputStream dilGetBinaryStream(long l2) throws SQLException {
        if (l2 < 0L) {
            throw new IllegalArgumentException("Illegal Arguments");
        }
        byte[] byArray = this.dilGetBytes(l2, this.dilLength());
        if (byArray == null) {
            byArray = new byte[]{};
        }
        return new ByteArrayInputStream(byArray);
    }

    @Override
    public void free() throws SQLException {
        block6: {
            if (this.isFree) {
                return;
            }
            if (this.isTemporary()) {
                if (this.isOpen()) {
                    this.close();
                }
                this.freeTemporary();
            } else {
                try {
                    this.close();
                }
                catch (SQLException sQLException) {
                    if (sQLException.getErrorCode() == 22289) break block6;
                    throw sQLException;
                }
            }
        }
        this.getPhysicalConnection().removeLargeObject(this);
        this.isFree = true;
        this.dbaccess = null;
    }

    @Override
    public InputStream getBinaryStream(long l2, long l3) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (this.canReadBasicLobDataInLocator()) {
            return this.dilGetBinaryStream(l2, l3);
        }
        long l4 = this.length();
        if (l2 < 1L || l3 < 0L || l2 > l4 || l2 - 1L + l3 > l4) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        return this.getDBAccess().newInputStream((oracle.jdbc.internal.OracleBlob)this, this.getChunkSize(), l2, l3);
    }

    InputStream dilGetBinaryStream(long l2, long l3) throws SQLException {
        int n2 = this.dilLength();
        if (l2 < 1L || l3 < 0L || l2 > (long)n2 || l2 - 1L + l3 > (long)n2) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        byte[] byArray = this.dilGetBytes(l2, n2 - (int)(l2 - 1L));
        return new ByteArrayInputStream(byArray, 0, (int)l3);
    }

    @Override
    public void setBytes(byte[] byArray) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setACProxy(Object object) {
        this.acProxy = object;
    }

    @Override
    public Object getACProxy() {
        return this.acProxy;
    }

    @Override
    public SQLXML toSQLXML() throws SQLException {
        return this.toSQLXML(this.getPhysicalConnection().getDbCsId());
    }

    @Override
    public SQLXML toSQLXML(int n2) throws SQLException {
        return null;
    }
}

