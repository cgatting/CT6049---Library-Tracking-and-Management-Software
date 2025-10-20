/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import oracle.jdbc.OracleData;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.JavaToJavaConverter;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleArray;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.CustomDatum;
import oracle.sql.CustomDatumFactory;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NCLOB;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.RAW;
import oracle.sql.REF;
import oracle.sql.ROWID;
import oracle.sql.STRUCT;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.METADATA})
class ArrayDataResultSet
extends OracleResultSet {
    Datum[] data;
    Map<String, Class<?>> map;
    private int currentIndex;
    private int lastIndex;
    private Boolean wasNull;
    private int fetchSize;
    OracleArray array;

    ArrayDataResultSet(PhysicalConnection physicalConnection, Datum[] datumArray, Map<String, Class<?>> map) throws SQLException {
        super(physicalConnection);
        this.connection = physicalConnection;
        this.data = datumArray;
        this.map = map;
        this.currentIndex = 0;
        this.lastIndex = this.data == null ? 0 : this.data.length;
        this.fetchSize = OracleConnection.DEFAULT_ROW_PREFETCH;
    }

    ArrayDataResultSet(PhysicalConnection physicalConnection, Datum[] datumArray, long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        super(physicalConnection);
        this.connection = physicalConnection;
        this.data = datumArray;
        this.map = map;
        this.currentIndex = (int)l2 - 1;
        int n3 = this.data == null ? 0 : this.data.length;
        this.lastIndex = this.currentIndex + Math.min(n3 - this.currentIndex, n2);
        this.fetchSize = OracleConnection.DEFAULT_ROW_PREFETCH;
    }

    ArrayDataResultSet(PhysicalConnection physicalConnection, OracleArray oracleArray, long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        super(physicalConnection);
        this.connection = physicalConnection;
        this.array = oracleArray;
        this.map = map;
        this.currentIndex = (int)l2 - 1;
        int n3 = this.array == null ? 0 : oracleArray.length();
        this.lastIndex = this.currentIndex + (n2 == -1 ? n3 - this.currentIndex : Math.min(n3 - this.currentIndex, n2));
        this.fetchSize = OracleConnection.DEFAULT_ROW_PREFETCH;
    }

    void ensureOpen() throws SQLException {
        this.ensureOpen(null);
    }

    void ensureOpen(String string) throws SQLException {
        if (this.closed) {
            if (this.connection.isClosed()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, string).fillInStackTrace();
            }
            throw (SQLException)DatabaseError.createSqlException(10, string).fillInStackTrace();
        }
    }

    @Override
    OracleStatement getOracleStatement() throws SQLException {
        this.ensureOpen("getOracleStatement");
        return null;
    }

    @Override
    int refreshRows(long l2, int n2) throws SQLException {
        this.ensureOpen("refreshRows");
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "beforeFirst").fillInStackTrace();
    }

    @Override
    void removeCurrentRowFromCache() throws SQLException {
    }

    @Override
    public boolean isFromResultSetCache() throws SQLException {
        return false;
    }

    @Override
    public byte[] getCompileKey() throws SQLException {
        return null;
    }

    @Override
    public byte[] getRuntimeKey() throws SQLException {
        return null;
    }

    @Override
    int getColumnCount() throws SQLException {
        this.ensureOpen("getColumnCount");
        return 2;
    }

    @Override
    protected void doneFetchingRows(boolean bl) throws SQLException {
    }

    @Override
    public boolean next() throws SQLException {
        this.ensureOpen("next");
        ++this.currentIndex;
        return this.currentIndex <= this.lastIndex;
    }

    @Override
    public void close() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            super.close();
        }
    }

    @Override
    public int getCursorId() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 76, "getCursorId()").fillInStackTrace();
    }

    @Override
    public boolean wasNull() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("wasNull");
            if (this.wasNull == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 24, null).fillInStackTrace();
            }
            boolean bl = this.wasNull;
            return bl;
        }
    }

    @Override
    public void beforeFirst() throws SQLException {
        this.ensureOpen("beforeFirst");
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "beforeFirst").fillInStackTrace();
    }

    @Override
    public void afterLast() throws SQLException {
        this.ensureOpen("afterLast");
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "afterLast").fillInStackTrace();
    }

    @Override
    public boolean first() throws SQLException {
        this.ensureOpen("first");
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "first").fillInStackTrace();
    }

    @Override
    public boolean last() throws SQLException {
        this.ensureOpen("last");
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "last").fillInStackTrace();
    }

    @Override
    public boolean absolute(int n2) throws SQLException {
        this.ensureOpen("absolute");
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "absolute").fillInStackTrace();
    }

    @Override
    public boolean relative(int n2) throws SQLException {
        this.ensureOpen("relative");
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "relative").fillInStackTrace();
    }

    @Override
    public boolean previous() throws SQLException {
        this.ensureOpen("previous");
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 75, "previous").fillInStackTrace();
    }

    @Override
    public String getString(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getString");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof TIMESTAMPTZ || datum instanceof TIMESTAMPLTZ) {
                    String string = datum.stringValue(this.connection);
                    return string;
                }
                String string = JavaToJavaConverter.convert(datum, String.class, this.connection, null, null);
                return string;
            }
            String string = null;
            return string;
        }
    }

    @Override
    public ResultSet getCursor(int n2) throws SQLException {
        Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                this.ensureOpen("getCursor");
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCursor").fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
        catch (Throwable throwable3) {
            if (closeableLock != null) {
                if (throwable != null) {
                    try {
                        closeableLock.close();
                    }
                    catch (Throwable throwable4) {
                        throwable.addSuppressed(throwable4);
                    }
                } else {
                    closeableLock.close();
                }
            }
            throw throwable3;
        }
    }

    @Override
    public Datum getOracleObject(int n2) throws SQLException {
        this.ensureOpen("getOracleObject");
        if (this.currentIndex <= 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 14, null).fillInStackTrace();
        }
        if (this.currentIndex > this.lastIndex) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 289, null).fillInStackTrace();
        }
        if (n2 == 1) {
            this.wasNull = Boolean.FALSE;
            return new NUMBER(this.currentIndex);
        }
        if (n2 == 2) {
            Datum[] datumArray;
            if (this.data != null) {
                this.wasNull = this.data[this.currentIndex - 1] == null ? Boolean.TRUE : Boolean.FALSE;
                return this.data[this.currentIndex - 1];
            }
            if (this.array != null && (datumArray = this.array.getOracleArray(this.currentIndex, 1)) != null && datumArray.length >= 1) {
                this.wasNull = datumArray[0] == null ? Boolean.TRUE : Boolean.FALSE;
                return datumArray[0];
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Out of sync").fillInStackTrace();
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, null).fillInStackTrace();
    }

    @Override
    public ROWID getROWID(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getROWID");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof ROWID) {
                    ROWID rOWID = (ROWID)datum;
                    return rOWID;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getROWID").fillInStackTrace();
            }
            ROWID rOWID = null;
            return rOWID;
        }
    }

    @Override
    public NUMBER getNUMBER(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getNUMBER");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof NUMBER) {
                    NUMBER nUMBER = (NUMBER)datum;
                    return nUMBER;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getNUMBER").fillInStackTrace();
            }
            NUMBER nUMBER = null;
            return nUMBER;
        }
    }

    @Override
    public DATE getDATE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getDATE");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof DATE) {
                    DATE dATE = (DATE)datum;
                    return dATE;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getDATE").fillInStackTrace();
            }
            DATE dATE = null;
            return dATE;
        }
    }

    @Override
    public ARRAY getARRAY(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getARRAY");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof ARRAY) {
                    ARRAY aRRAY = (ARRAY)datum;
                    return aRRAY;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getARRAY").fillInStackTrace();
            }
            ARRAY aRRAY = null;
            return aRRAY;
        }
    }

    @Override
    public STRUCT getSTRUCT(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getSTRUCT");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof STRUCT) {
                    STRUCT sTRUCT = (STRUCT)datum;
                    return sTRUCT;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getSTRUCT").fillInStackTrace();
            }
            STRUCT sTRUCT = null;
            return sTRUCT;
        }
    }

    @Override
    public OPAQUE getOPAQUE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getOPAQUE");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof OPAQUE) {
                    OPAQUE oPAQUE = (OPAQUE)datum;
                    return oPAQUE;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getOPAQUE").fillInStackTrace();
            }
            OPAQUE oPAQUE = null;
            return oPAQUE;
        }
    }

    @Override
    public REF getREF(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getREF");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof REF) {
                    REF rEF = (REF)datum;
                    return rEF;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getREF").fillInStackTrace();
            }
            REF rEF = null;
            return rEF;
        }
    }

    @Override
    public CHAR getCHAR(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getCHAR");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof CHAR) {
                    CHAR cHAR = (CHAR)datum;
                    return cHAR;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCHAR").fillInStackTrace();
            }
            CHAR cHAR = null;
            return cHAR;
        }
    }

    @Override
    public RAW getRAW(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getRAW");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof RAW) {
                    RAW rAW = (RAW)datum;
                    return rAW;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getRAW").fillInStackTrace();
            }
            RAW rAW = null;
            return rAW;
        }
    }

    @Override
    public BLOB getBLOB(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getBLOB");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof BLOB) {
                    BLOB bLOB = (BLOB)datum;
                    return bLOB;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBLOB").fillInStackTrace();
            }
            BLOB bLOB = null;
            return bLOB;
        }
    }

    @Override
    public CLOB getCLOB(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getCLOB");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof CLOB) {
                    CLOB cLOB = (CLOB)datum;
                    return cLOB;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getCLOB").fillInStackTrace();
            }
            CLOB cLOB = null;
            return cLOB;
        }
    }

    @Override
    public BFILE getBFILE(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getBFILE");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof BFILE) {
                    BFILE bFILE = (BFILE)datum;
                    return bFILE;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBFILE").fillInStackTrace();
            }
            BFILE bFILE = null;
            return bFILE;
        }
    }

    @Override
    public INTERVALDS getINTERVALDS(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getINTERVALDS");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof INTERVALDS) {
                    INTERVALDS iNTERVALDS = (INTERVALDS)datum;
                    return iNTERVALDS;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getINTERVALDS").fillInStackTrace();
            }
            INTERVALDS iNTERVALDS = null;
            return iNTERVALDS;
        }
    }

    @Override
    public INTERVALYM getINTERVALYM(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getINTERVALYM");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof INTERVALYM) {
                    INTERVALYM iNTERVALYM = (INTERVALYM)datum;
                    return iNTERVALYM;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getINTERVALYM").fillInStackTrace();
            }
            INTERVALYM iNTERVALYM = null;
            return iNTERVALYM;
        }
    }

    @Override
    public BFILE getBfile(int n2) throws SQLException {
        return this.getBFILE(n2);
    }

    @Override
    public TIMESTAMP getTIMESTAMP(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getTIMESTAMP");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof TIMESTAMP) {
                    TIMESTAMP tIMESTAMP = (TIMESTAMP)datum;
                    return tIMESTAMP;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTIMESTAMP").fillInStackTrace();
            }
            TIMESTAMP tIMESTAMP = null;
            return tIMESTAMP;
        }
    }

    @Override
    public TIMESTAMPTZ getTIMESTAMPTZ(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getTIMESTAMPTZ");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof TIMESTAMPTZ) {
                    TIMESTAMPTZ tIMESTAMPTZ = (TIMESTAMPTZ)datum;
                    return tIMESTAMPTZ;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTIMESTAMPTZ").fillInStackTrace();
            }
            TIMESTAMPTZ tIMESTAMPTZ = null;
            return tIMESTAMPTZ;
        }
    }

    @Override
    public TIMESTAMPLTZ getTIMESTAMPLTZ(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getTIMESTAMPLTZ");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof TIMESTAMPLTZ) {
                    TIMESTAMPLTZ tIMESTAMPLTZ = (TIMESTAMPLTZ)datum;
                    return tIMESTAMPLTZ;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getTIMESTAMPLTZ").fillInStackTrace();
            }
            TIMESTAMPLTZ tIMESTAMPLTZ = null;
            return tIMESTAMPLTZ;
        }
    }

    @Override
    public boolean getBoolean(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getBoolean");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                boolean bl = JavaToJavaConverter.convert(datum, Boolean.class, this.connection, null, null);
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    @Override
    public OracleResultSet.AuthorizationIndicator getAuthorizationIndicator(int n2) throws SQLException {
        this.ensureOpen("getAuthorizationIndicator");
        return null;
    }

    @Override
    public byte getByte(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getByte");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                byte by = JavaToJavaConverter.convert(datum, Byte.class, this.connection, null, null);
                return by;
            }
            byte by = 0;
            return by;
        }
    }

    @Override
    public short getShort(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getShort");
            long l2 = this.getLong(n2);
            if (l2 > 32767L || l2 < -32768L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 26, "getShort").fillInStackTrace();
            }
            short s2 = (short)l2;
            return s2;
        }
    }

    @Override
    public int getInt(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getInt");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                int n3 = JavaToJavaConverter.convert(datum, Integer.class, this.connection, null, null);
                return n3;
            }
            int n4 = 0;
            return n4;
        }
    }

    @Override
    public long getLong(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getLong");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                long l2 = JavaToJavaConverter.convert(datum, Long.class, this.connection, null, null);
                return l2;
            }
            long l3 = 0L;
            return l3;
        }
    }

    @Override
    public float getFloat(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getFloat");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                float f2 = JavaToJavaConverter.convert(datum, Float.class, this.connection, null, null).floatValue();
                return f2;
            }
            float f3 = 0.0f;
            return f3;
        }
    }

    @Override
    public double getDouble(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getDouble");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                double d2 = JavaToJavaConverter.convert(datum, Double.class, this.connection, null, null);
                return d2;
            }
            double d3 = 0.0;
            return d3;
        }
    }

    @Override
    public BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getBigDecimal");
            Datum datum = this.getOracleObject(n2);
            BigDecimal bigDecimal = JavaToJavaConverter.convert(datum, BigDecimal.class, this.connection, null, null);
            return bigDecimal;
        }
    }

    @Override
    public byte[] getBytes(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getBytes");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof RAW) {
                    byte[] byArray = ((RAW)datum).shareBytes();
                    return byArray;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getBytes").fillInStackTrace();
            }
            byte[] byArray = null;
            return byArray;
        }
    }

    @Override
    public Date getDate(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getDate");
            Datum datum = this.getOracleObject(n2);
            Date date = JavaToJavaConverter.convert(datum, Date.class, this.connection, null, null);
            return date;
        }
    }

    @Override
    public Time getTime(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getTime");
            Datum datum = this.getOracleObject(n2);
            Time time = JavaToJavaConverter.convert(datum, Time.class, this.connection, null, null);
            return time;
        }
    }

    @Override
    public Timestamp getTimestamp(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getTimestamp");
            Datum datum = this.getOracleObject(n2);
            Timestamp timestamp = JavaToJavaConverter.convert(datum, Timestamp.class, this.connection, null, null);
            return timestamp;
        }
    }

    @Override
    public InputStream getAsciiStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getAsciiStream");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                InputStream inputStream = datum.asciiStreamValue();
                return inputStream;
            }
            InputStream inputStream = null;
            return inputStream;
        }
    }

    @Override
    public InputStream getUnicodeStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getUnicodeStream");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                DBConversion dBConversion = this.connection.conversion;
                byte[] byArray = datum.shareBytes();
                if (datum instanceof RAW) {
                    InputStream inputStream = dBConversion.ConvertStream(new ByteArrayInputStream(byArray), 3);
                    return inputStream;
                }
                if (datum instanceof CHAR) {
                    InputStream inputStream = dBConversion.ConvertStream(new ByteArrayInputStream(byArray), 1);
                    return inputStream;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getUnicodeStream").fillInStackTrace();
            }
            InputStream inputStream = null;
            return inputStream;
        }
    }

    @Override
    public InputStream getBinaryStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getBinaryStream");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                InputStream inputStream = datum.binaryStreamValue();
                return inputStream;
            }
            InputStream inputStream = null;
            return inputStream;
        }
    }

    @Override
    public Object getObject(int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getObject");
            Object object = this.getObject(n2);
            OracleData oracleData = oracleDataFactory.create(object, 0);
            return oracleData;
        }
    }

    @Override
    public Object getObject(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getObject");
            Object object = this.getObject(n2, this.map);
            return object;
        }
    }

    @Override
    @Deprecated
    public CustomDatum getCustomDatum(int n2, CustomDatumFactory customDatumFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getCustomDatum");
            Datum datum = this.getOracleObject(n2);
            CustomDatum customDatum = customDatumFactory.create(datum, 0);
            return customDatum;
        }
    }

    @Override
    public ORAData getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getORAData");
            Datum datum = this.getOracleObject(n2);
            ORAData oRAData = oRADataFactory.create(datum, 0);
            return oRAData;
        }
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                this.ensureOpen("getMetaData");
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23, "getMetaData").fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
        catch (Throwable throwable3) {
            if (closeableLock != null) {
                if (throwable != null) {
                    try {
                        closeableLock.close();
                    }
                    catch (Throwable throwable4) {
                        throwable.addSuppressed(throwable4);
                    }
                } else {
                    closeableLock.close();
                }
            }
            throw throwable3;
        }
    }

    @Override
    public int findColumn(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("findColumn");
            if (string.equalsIgnoreCase("index")) {
                int n2 = 1;
                return n2;
            }
            if (string.equalsIgnoreCase("value")) {
                int n3 = 2;
                return n3;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6, "findColumn").fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    @Override
    public Statement getStatement() throws SQLException {
        this.ensureOpen("getStatement");
        return null;
    }

    @Override
    public Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getObject");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                if (datum instanceof STRUCT) {
                    Object object = ((STRUCT)datum).toJdbc((Map)map);
                    return object;
                }
                Object object = datum.toJdbc();
                return object;
            }
            Object var6_9 = null;
            return var6_9;
        }
    }

    @Override
    public Ref getRef(int n2) throws SQLException {
        return this.getREF(n2);
    }

    @Override
    public Blob getBlob(int n2) throws SQLException {
        return this.getBLOB(n2);
    }

    @Override
    public Clob getClob(int n2) throws SQLException {
        return this.getCLOB(n2);
    }

    @Override
    public Array getArray(int n2) throws SQLException {
        return this.getARRAY(n2);
    }

    @Override
    public Reader getCharacterStream(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getCharacterStream");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                Reader reader = datum.characterStreamValue();
                return reader;
            }
            Reader reader = null;
            return reader;
        }
    }

    @Override
    public BigDecimal getBigDecimal(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getBigDecimal");
            Datum datum = this.getOracleObject(n2);
            BigDecimal bigDecimal = JavaToJavaConverter.convert(datum, BigDecimal.class, this.connection, null, null);
            return bigDecimal;
        }
    }

    @Override
    public Date getDate(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            DATE dATE;
            this.ensureOpen("getDate");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                dATE = null;
                dATE = datum instanceof DATE ? (DATE)datum : new DATE(JavaToJavaConverter.convert(datum, String.class, this.connection, null, null));
                if (dATE != null) {
                    Date date = JavaToJavaConverter.convert(datum, Date.class, this.connection, calendar, null);
                    return date;
                }
            }
            dATE = null;
            return dATE;
        }
    }

    @Override
    public Time getTime(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            DATE dATE;
            this.ensureOpen("getTime");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                dATE = null;
                dATE = datum instanceof DATE ? (DATE)datum : new DATE(JavaToJavaConverter.convert(datum, String.class, this.connection, null, null));
                if (dATE != null) {
                    Time time = JavaToJavaConverter.convert(datum, Time.class, this.connection, calendar, null);
                    return time;
                }
            }
            dATE = null;
            return dATE;
        }
    }

    @Override
    public Timestamp getTimestamp(int n2, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            DATE dATE;
            this.ensureOpen("getTimestamp");
            Datum datum = this.getOracleObject(n2);
            if (datum != null) {
                dATE = null;
                dATE = datum instanceof DATE ? (DATE)datum : new DATE(JavaToJavaConverter.convert(datum, String.class, this.connection, null, null));
                if (dATE != null) {
                    Timestamp timestamp = JavaToJavaConverter.convert(datum, Timestamp.class, this.connection, calendar, null);
                    return timestamp;
                }
            }
            dATE = null;
            return dATE;
        }
    }

    @Override
    public URL getURL(int n2) throws SQLException {
        Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                this.ensureOpen("getURL");
                throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getURL").fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
        catch (Throwable throwable3) {
            if (closeableLock != null) {
                if (throwable != null) {
                    try {
                        closeableLock.close();
                    }
                    catch (Throwable throwable4) {
                        throwable.addSuppressed(throwable4);
                    }
                } else {
                    closeableLock.close();
                }
            }
            throw throwable3;
        }
    }

    @Override
    public String getCursorName() throws SQLException {
        Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();
        Throwable throwable = null;
        try {
            try {
                this.ensureOpen("getCursorName");
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23, "getCursorName").fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
        catch (Throwable throwable3) {
            if (closeableLock != null) {
                if (throwable != null) {
                    try {
                        closeableLock.close();
                    }
                    catch (Throwable throwable4) {
                        throwable.addSuppressed(throwable4);
                    }
                } else {
                    closeableLock.close();
                }
            }
            throw throwable3;
        }
    }

    @Override
    public NClob getNClob(int n2) throws SQLException {
        this.ensureOpen("getNClob");
        Datum datum = this.getOracleObject(n2);
        if (datum != null) {
            if (datum instanceof NCLOB) {
                return (NCLOB)datum;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getNClob").fillInStackTrace();
        }
        return null;
    }

    @Override
    public String getNString(int n2) throws SQLException {
        this.ensureOpen("getNString");
        Datum datum = this.getOracleObject(n2);
        return JavaToJavaConverter.convert(datum, String.class, this.connection, null, null);
    }

    @Override
    public Reader getNCharacterStream(int n2) throws SQLException {
        this.ensureOpen("getNCharacterStream");
        Datum datum = this.getOracleObject(n2);
        if (datum != null) {
            return datum.characterStreamValue();
        }
        return null;
    }

    @Override
    public RowId getRowId(int n2) throws SQLException {
        return this.getROWID(n2);
    }

    @Override
    public SQLXML getSQLXML(int n2) throws SQLException {
        this.ensureOpen("getSQLXML");
        Datum datum = this.getOracleObject(n2);
        if (datum != null) {
            if (datum instanceof SQLXML) {
                return (SQLXML)((Object)datum);
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getSQLXML").fillInStackTrace();
        }
        return null;
    }

    @Override
    public <T> T getObject(int n2, Class<T> clazz) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen("getObject");
            Object object = this.getObject(n2);
            if (clazz.isInstance(object)) {
                Object object2 = object;
                return (T)object2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4, "getObject").fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        this.ensureOpen("isBeforeFirst");
        return this.currentIndex < 1 && this.lastIndex != 0;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        this.ensureOpen("isAfterLast");
        return this.currentIndex > this.lastIndex && this.lastIndex != 0;
    }

    @Override
    public boolean isFirst() throws SQLException {
        this.ensureOpen("isFirst");
        return this.currentIndex == 1;
    }

    @Override
    public boolean isLast() throws SQLException {
        this.ensureOpen("isLast");
        return this.currentIndex == this.lastIndex;
    }

    @Override
    public int getRow() throws SQLException {
        this.ensureOpen("getRow");
        return this.currentIndex;
    }

    @Override
    public void setFetchSize(int n2) throws SQLException {
        this.ensureOpen("setFetchSize");
        if (n2 < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "setFetchSize").fillInStackTrace();
        }
        this.fetchSize = n2 == 0 ? OracleConnection.DEFAULT_ROW_PREFETCH : n2;
    }

    @Override
    public int getFetchSize() throws SQLException {
        this.ensureOpen("getFetchSize");
        return this.fetchSize;
    }

    @Override
    public int getType() throws SQLException {
        this.ensureOpen("getType");
        return 1003;
    }

    @Override
    public int getConcurrency() throws SQLException {
        this.ensureOpen("getConcurrency");
        return 1007;
    }
}

