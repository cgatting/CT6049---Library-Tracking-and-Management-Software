/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.LargeObjectAccessMode;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.BFILE;
import oracle.sql.BfileDBAccess;
import oracle.sql.Datum;
import oracle.sql.DatumWithConnection;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.LOB_DATUM})
@Deprecated
public class OracleBfile
extends DatumWithConnection
implements oracle.jdbc.internal.OracleBfile {
    public static final int MAX_CHUNK_SIZE = 32512;
    public static final int MODE_READONLY = 0;
    public static final int MODE_READWRITE = 1;
    BfileDBAccess dbaccess;
    private long bfileLength = -1L;
    Object acProxy;

    public OracleBfile() {
    }

    public OracleBfile(OracleConnection oracleConnection) throws SQLException {
        this(oracleConnection, null);
    }

    public OracleBfile(OracleConnection oracleConnection, byte[] byArray) throws SQLException {
        super(byArray);
        this.setPhysicalConnectionOf(oracleConnection);
        this.dbaccess = this.getInternalConnection().createBfileDBAccess();
        if (byArray != null) {
            ((oracle.jdbc.internal.OracleConnection)oracleConnection).addBfile(this);
        }
    }

    @Override
    public long length() throws SQLException {
        this.bfileLength = this.getDBAccess().length(this);
        return this.bfileLength;
    }

    @Override
    public byte[] getBytes(long l2, int n2) throws SQLException {
        if (l2 < 1L || n2 < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, null).fillInStackTrace();
        }
        byte[] byArray = null;
        if (n2 == 0) {
            byArray = new byte[]{};
        } else {
            long l3 = 0L;
            byte[] byArray2 = new byte[n2];
            l3 = this.getBytes(l2, n2, byArray2);
            if (l3 > 0L) {
                if (l3 == (long)n2) {
                    byArray = byArray2;
                } else {
                    byArray = new byte[(int)l3];
                    System.arraycopy(byArray2, 0, byArray, 0, (int)l3);
                }
            } else {
                byArray = new byte[]{};
            }
        }
        return byArray;
    }

    @Override
    public int getBytes(long l2, int n2, byte[] byArray) throws SQLException {
        if (l2 < 1L || n2 < 0 || byArray == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, null).fillInStackTrace();
        }
        return this.getDBAccess().getBytes(this, l2, n2, byArray);
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        return this.getDBAccess().newInputStream(this, 32512, 0L);
    }

    @Override
    public long position(byte[] byArray, long l2) throws SQLException {
        return this.getDBAccess().position((oracle.jdbc.internal.OracleBfile)this, (Datum)this, byArray, l2);
    }

    @Override
    public long position(BFILE bFILE, long l2) throws SQLException {
        return this.getDBAccess().position((oracle.jdbc.internal.OracleBfile)this, (Datum)this, bFILE, l2);
    }

    @Override
    public long position(oracle.jdbc.OracleBfile oracleBfile, long l2) throws SQLException {
        return this.position((BFILE)oracleBfile, l2);
    }

    @Override
    public String getName() throws SQLException {
        return this.getDBAccess().getName(this);
    }

    @Override
    public String getDirAlias() throws SQLException {
        return this.getDBAccess().getDirAlias(this);
    }

    @Override
    public void openFile() throws SQLException {
        this.getDBAccess().openFile(this);
    }

    @Override
    public boolean isFileOpen() throws SQLException {
        boolean bl = this.getDBAccess().isFileOpen(this);
        return bl;
    }

    @Override
    public boolean fileExists() throws SQLException {
        return this.getDBAccess().fileExists(this);
    }

    @Override
    public void closeFile() throws SQLException {
        this.getDBAccess().closeFile(this);
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
    public InputStream getBinaryStream(long l2) throws SQLException {
        return this.getDBAccess().newInputStream(this, 32512, l2);
    }

    public void open() throws SQLException {
        this.getDBAccess().open(this, 0);
    }

    @Override
    public void open(LargeObjectAccessMode largeObjectAccessMode) throws SQLException {
        this.open(largeObjectAccessMode.getCode());
    }

    public void open(int n2) throws SQLException {
        if (n2 != 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 102).fillInStackTrace();
        }
        this.getDBAccess().open(this, n2);
    }

    @Override
    public void close() throws SQLException {
        this.getDBAccess().close(this);
    }

    @Override
    public boolean isOpen() throws SQLException {
        return this.getDBAccess().isOpen(this);
    }

    @Override
    public Object toJdbc() throws SQLException {
        return this;
    }

    @Override
    public boolean isConvertibleTo(Class<?> clazz) {
        String string = clazz.getName();
        boolean bl = string.compareTo("java.io.InputStream") == 0 || string.compareTo("java.io.Reader") == 0;
        return bl;
    }

    @Override
    public Reader characterStreamValue() throws SQLException {
        return this.getDBAccess().newConversionReader(this, 8);
    }

    @Override
    public InputStream asciiStreamValue() throws SQLException {
        return this.getDBAccess().newConversionInputStream(this, 2);
    }

    @Override
    public InputStream binaryStreamValue() throws SQLException {
        return this.getBinaryStream();
    }

    @Override
    public Object makeJdbcArray(int n2) {
        return new BFILE[n2];
    }

    @Override
    public BfileDBAccess getDBAccess() throws SQLException {
        if (this.dbaccess == null) {
            this.dbaccess = this.getInternalConnection().createBfileDBAccess();
        }
        if (this.getPhysicalConnection().isClosed()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        return this.dbaccess;
    }

    @Override
    public final void setLength(long l2) {
        this.bfileLength = l2;
    }

    @Override
    public Connection getJavaSqlConnection() throws SQLException {
        return super.getJavaSqlConnection();
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
}

