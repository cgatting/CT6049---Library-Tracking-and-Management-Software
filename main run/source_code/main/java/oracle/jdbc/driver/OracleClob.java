/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLXML;
import oracle.jdbc.LargeObjectAccessMode;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CLOB;
import oracle.sql.CharacterSet;
import oracle.sql.ClobDBAccess;
import oracle.sql.DatumWithConnection;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.LOB_DATUM})
@Deprecated
public class OracleClob
extends DatumWithConnection
implements oracle.jdbc.internal.OracleClob {
    public static final int MAX_CHUNK_SIZE = 32768;
    public static final int DURATION_SESSION = 10;
    public static final int DURATION_CALL = 12;
    public static final int OLD_WRONG_DURATION_SESSION = 1;
    public static final int OLD_WRONG_DURATION_CALL = 2;
    public static final int MODE_READONLY = 0;
    public static final int MODE_READWRITE = 1;
    ClobDBAccess dbaccess;
    private int dbChunkSize = -1;
    private short csform;
    boolean isFree = false;
    boolean fromObject = false;
    long cachedLengthOfClobInChars = -1L;
    char[] prefetchData = null;
    int prefetchDataSize = 0;
    boolean activePrefetch = false;
    static final int KDLCTLSIZE = 16;
    static final int KDF_FLAG = 88;
    static final int KDLIDDAT = 8;
    transient CharacterSet dilCharacterSet = null;
    protected Object acProxy;

    public OracleClob() {
    }

    public OracleClob(OracleConnection oracleConnection) throws SQLException {
        this(oracleConnection, null);
    }

    public OracleClob(OracleConnection oracleConnection, byte[] byArray, boolean bl) throws SQLException {
        this(oracleConnection, byArray);
        this.fromObject = bl;
    }

    public void setCsform(short s2) {
        this.csform = s2;
    }

    public short getCsform() {
        return this.csform;
    }

    public void setFromobject(boolean bl) {
        this.fromObject = bl;
    }

    public OracleClob(OracleConnection oracleConnection, byte[] byArray) throws SQLException {
        super(byArray);
        if (byArray != null) {
            this.csform = CLOB.getFormOfUseFromLocator(byArray);
        }
        OracleClob.assertNotNull(oracleConnection);
        this.setPhysicalConnectionOf(oracleConnection);
        this.dbaccess = ((oracle.jdbc.internal.OracleConnection)oracleConnection).createClobDBAccess();
        this.dbaccess.incrementTempLobReferenceCount(byArray);
        if (byArray != null && !this.isTemporary()) {
            ((oracle.jdbc.internal.OracleConnection)oracleConnection).addLargeObject(this);
        }
    }

    public OracleClob(OracleConnection oracleConnection, byte[] byArray, short s2) throws SQLException {
        this(oracleConnection, byArray);
        short s3 = CLOB.getFormOfUseFromLocator(byArray);
        if (s3 != -1) {
            if (s2 != s3) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 184).fillInStackTrace();
            }
            this.csform = s3;
        } else {
            this.csform = s2;
        }
    }

    @Override
    public boolean isNCLOB() {
        return this.csform == 2;
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
        long l2 = this.activePrefetch && this.cachedLengthOfClobInChars != -1L ? this.cachedLengthOfClobInChars : (this.canReadBasicLobDataInLocator() ? (long)this.dilGetChars().length : this.getDBAccess().length(this));
        return l2;
    }

    @Override
    public String getSubString(long l2, int n2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (n2 < 0 || l2 < 1L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        if (this.canReadBasicLobDataInLocator()) {
            return this.dilGetSubString(l2, n2);
        }
        String string = null;
        if (n2 == 0 || this.activePrefetch && (this.cachedLengthOfClobInChars == 0L || this.cachedLengthOfClobInChars > 0L && l2 - 1L >= this.cachedLengthOfClobInChars)) {
            string = new String();
        } else if (this.prefetchData != null && this.prefetchDataSize > 0 && this.cachedLengthOfClobInChars == (long)this.prefetchDataSize && l2 + (long)n2 - 1L <= this.cachedLengthOfClobInChars) {
            string = new String(this.prefetchData, (int)l2 - 1, n2);
        } else {
            char[] cArray = this.getDBAccess().getCharBufferSync(n2);
            int n3 = this.getChars(l2, n2, cArray);
            string = n3 > 0 ? new String(cArray, 0, n3) : new String();
            this.getDBAccess().cacheBufferSync(cArray);
        }
        return string;
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (this.canReadBasicLobDataInLocator()) {
            return this.dilGetCharacterStream(1L);
        }
        return this.getDBAccess().newReader(this, this.getBufferSize(), 0L);
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        return this.getAsciiStream(false);
    }

    public InputStream getAsciiStream(boolean bl) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (this.canReadBasicLobDataInLocator()) {
            return this.dilGetAsciiStream(1L);
        }
        return this.getDBAccess().newInputStream(this, this.getBufferSize(), 0L, bl);
    }

    @Override
    public long position(String string, long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().position((oracle.jdbc.internal.OracleClob)this, string, l2);
    }

    @Override
    public long position(Clob clob, long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().position((oracle.jdbc.internal.OracleClob)this, (oracle.jdbc.internal.OracleClob)clob, l2);
    }

    @Override
    public int getChars(long l2, int n2, char[] cArray) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().getChars(this, l2, n2, cArray);
    }

    @Override
    @Deprecated
    public Writer getCharacterOutputStream() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.setCharacterStream(1L);
    }

    @Override
    @Deprecated
    public OutputStream getAsciiOutputStream() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.setAsciiStream(1L);
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
    public int putChars(long l2, char[] cArray) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().putChars(this, l2, cArray, 0, cArray != null ? cArray.length : 0);
    }

    public int putChars(long l2, char[] cArray, int n2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().putChars(this, l2, cArray, 0, n2);
    }

    @Override
    public int putChars(long l2, char[] cArray, int n2, int n3) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().putChars(this, l2, cArray, n2, n3);
    }

    @Override
    @Deprecated
    public int putString(long l2, String string) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.setString(l2, string);
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
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        int n2 = this.getChunkSize();
        int n3 = 0;
        n3 = n2 >= 32768 || n2 <= 0 ? 32768 : 32768 / n2 * n2;
        return n3;
    }

    @Override
    public boolean isEmptyLob() throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return (this.shareBytes()[5] & 0x10) != 0;
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
    public OutputStream getAsciiOutputStream(long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().newOutputStream(this, this.getBufferSize(), l2, false);
    }

    @Override
    @Deprecated
    public Writer getCharacterOutputStream(long l2) throws SQLException {
        return this.getDBAccess().newWriter(this, this.getBufferSize(), l2, false);
    }

    @Override
    public InputStream getAsciiStream(long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (l2 == 0L) {
            if (!this.getPhysicalConnection().isLobStreamPosStandardCompliant()) {
                l2 = 1L;
            } else {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
        }
        if (this.canReadBasicLobDataInLocator()) {
            return this.dilGetAsciiStream(l2);
        }
        return this.getDBAccess().newInputStream(this, this.getBufferSize(), l2);
    }

    @Override
    public Reader getCharacterStream(long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (l2 == 0L) {
            if (!this.getPhysicalConnection().isLobStreamPosStandardCompliant()) {
                l2 = 1L;
            } else {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
        }
        if (this.canReadBasicLobDataInLocator()) {
            return this.dilGetCharacterStream(l2);
        }
        return this.getDBAccess().newReader(this, this.getBufferSize(), l2);
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
    public int setString(long l2, String string) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (l2 < 1L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "'pos' should not be < 1").fillInStackTrace();
        }
        int n2 = 0;
        if (string != null && string.length() != 0) {
            n2 = this.putChars(l2, string.toCharArray());
        }
        return n2;
    }

    @Override
    public int setString(long l2, String string, int n2, int n3) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (l2 < 1L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "'pos' should not be < 1").fillInStackTrace();
        }
        if (n2 < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "'offset' should not be < 0").fillInStackTrace();
        }
        if (n2 + n3 > string.length()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, " 'offset + len' should not be exceed string length. ").fillInStackTrace();
        }
        int n4 = 0;
        if (string != null && string.length() != 0) {
            n4 = this.putChars(l2, string.toCharArray(), n2, n3);
        }
        return n4;
    }

    @Override
    public OutputStream setAsciiStream(long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().newOutputStream(this, this.getBufferSize(), l2, true);
    }

    @Override
    public Writer setCharacterStream(long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        return this.getDBAccess().newWriter(this, this.getBufferSize(), l2, true);
    }

    @Override
    public void truncate(long l2) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, " 'len' should not be < 0").fillInStackTrace();
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
        return this.getCharacterStream();
    }

    @Override
    public InputStream asciiStreamValue() throws SQLException {
        return this.getAsciiStream();
    }

    @Override
    public InputStream binaryStreamValue() throws SQLException {
        return this.getAsciiStream();
    }

    @Override
    public String stringValue() throws SQLException {
        Reader reader = this.getCharacterStream();
        int n2 = this.getBufferSize();
        int n3 = 0;
        StringWriter stringWriter = new StringWriter(n2);
        char[] cArray = new char[n2];
        try {
            while ((n3 = reader.read(cArray)) != -1) {
                stringWriter.write(cArray, 0, n3);
            }
        }
        catch (IOException iOException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), iOException).fillInStackTrace();
        }
        catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 151).fillInStackTrace();
        }
        return stringWriter.getBuffer().substring(0);
    }

    @Override
    public Object makeJdbcArray(int n2) {
        return new CLOB[n2];
    }

    @Override
    public ClobDBAccess getDBAccess() throws SQLException {
        if (this.dbaccess == null) {
            if (this.isEmptyLob()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 98).fillInStackTrace();
            }
            this.dbaccess = this.getInternalConnection().createClobDBAccess();
        }
        if (this.getPhysicalConnection().isClosed()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        return this.dbaccess;
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
        this.cachedLengthOfClobInChars = l2;
    }

    @Override
    public final void setChunkSize(int n2) {
        this.dbChunkSize = n2;
    }

    @Override
    public final void setPrefetchedData(char[] cArray) {
        if (cArray == null) {
            this.setPrefetchedData(null, 0);
        } else {
            this.setPrefetchedData(cArray, cArray.length);
        }
    }

    @Override
    public final void setPrefetchedData(char[] cArray, int n2) {
        this.prefetchData = cArray;
        this.prefetchDataSize = n2;
    }

    @Override
    public final char[] getPrefetchedData() {
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
        this.cachedLengthOfClobInChars = -1L;
        this.prefetchData = null;
    }

    @Override
    public final boolean isActivePrefetch() {
        return this.activePrefetch;
    }

    int dilGetCharSetId() throws SQLException {
        byte by = this.shareBytes()[32];
        byte by2 = this.shareBytes()[33];
        int n2 = (by & 0xFF) << 8 | by2 & 0xFF;
        return n2;
    }

    boolean isMigratedAL16UTF16LE() {
        int n2 = this.shareBytes()[7] & 0xFF;
        return (n2 & 0x40) == 64;
    }

    boolean isVariableWidth() {
        int n2;
        int n3 = this.shareBytes()[6] & 0xFF;
        return (n3 & (n2 = 128)) == n2;
    }

    void dilGetCharacterSet() throws SQLException {
        if (this.dilCharacterSet == null) {
            if (this.isMigratedAL16UTF16LE()) {
                this.dilCharacterSet = CharacterSet.make(2002);
            } else if (this.isVariableWidth()) {
                this.dilCharacterSet = CharacterSet.make(2000);
            } else {
                int n2 = this.dilGetCharSetId();
                this.dilCharacterSet = CharacterSet.make(n2);
            }
        }
    }

    int dilLength() {
        return this.shareBytes().length - 86 - 16;
    }

    char[] dilGetChars() throws SQLException {
        int n2 = this.dilLength();
        byte[] byArray = new byte[n2];
        System.arraycopy(this.shareBytes(), 102, byArray, 0, n2);
        String string = this.dilCharacterSet.toStringWithReplacement(byArray, 0, n2);
        char[] cArray = string.toCharArray();
        return cArray;
    }

    InputStream dilGetAsciiStream(long l2) throws SQLException {
        byte[] byArray;
        char[] cArray = this.dilGetChars();
        if (l2 - 1L > (long)cArray.length) {
            byte[] byArray2 = new byte[]{};
            return new ByteArrayInputStream(byArray2);
        }
        if (this.dilGetCharSetId() == 1) {
            byArray = new byte[cArray.length];
            for (int i2 = 0; i2 < cArray.length; ++i2) {
                byArray[i2] = (byte)cArray[i2];
            }
        } else {
            CharacterSet characterSet = CharacterSet.make(1);
            byArray = characterSet.convertWithReplacement(new String(cArray));
        }
        return new ByteArrayInputStream(byArray);
    }

    Reader dilGetCharacterStream(long l2) throws SQLException {
        char[] cArray = this.dilGetChars();
        int n2 = cArray.length;
        if (l2 - 1L > (long)n2) {
            char[] cArray2 = new char[]{};
            return new CharArrayReader(cArray2);
        }
        return new CharArrayReader(cArray, (int)(l2 - 1L), Integer.MAX_VALUE);
    }

    String dilGetSubString(long l2, int n2) throws SQLException {
        char[] cArray = this.dilGetChars();
        if ((int)l2 > cArray.length) {
            return "";
        }
        int n3 = (int)Math.min((long)n2, (long)cArray.length - (l2 - 1L));
        if (n3 == 0) {
            return "";
        }
        return new String(cArray, (int)(l2 - 1L), n3);
    }

    Reader dilGetCharacterStream(long l2, long l3) throws SQLException {
        if (l2 < 1L || l3 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        char[] cArray = this.dilGetChars();
        long l4 = cArray.length;
        if (l2 < 1L || l3 < 0L || l2 > l4 || l2 - 1L + l3 > l4) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        return new CharArrayReader(cArray, (int)(l2 - 1L), (int)l3);
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
        boolean bl4 = false;
        if (n2 != 0) {
            this.dilGetCharacterSet();
            bl4 = !this.dilCharacterSet.isUnknown();
        }
        return bl4;
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
    public Reader getCharacterStream(long l2, long l3) throws SQLException {
        if (this.isFree) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 192).fillInStackTrace();
        }
        if (l2 == 0L) {
            if (!this.getPhysicalConnection().isLobStreamPosStandardCompliant()) {
                l2 = 1L;
            } else {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
        }
        if (this.canReadBasicLobDataInLocator()) {
            return this.dilGetCharacterStream(l2, l3);
        }
        long l4 = this.length();
        if (l2 < 1L || l3 < 0L || l2 > l4 || l2 - 1L + l3 > l4) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        return this.getDBAccess().newReader(this, this.getChunkSize(), l2, l3);
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
        return null;
    }

    @Override
    public SQLXML toSQLXML(String string) throws SQLException {
        return null;
    }
}

