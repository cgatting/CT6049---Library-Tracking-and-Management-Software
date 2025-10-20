/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

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
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.OracleInputStream;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.OracleConnection;
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
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class GeneratedStatement {
    PhysicalConnection connection;
    Accessor[] accessors = null;
    int lastIndex = -1;
    OracleInputStream streamList;
    protected int offsetOfFirstUserColumn = -1;

    protected GeneratedStatement(PhysicalConnection physicalConnection) {
        this.connection = physicalConnection;
    }

    abstract void closeUsedStreams(int var1) throws SQLException;

    abstract int physicalRowIndex(long var1);

    Array getArray(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getArray(this.physicalRowIndex(l2));
    }

    BigDecimal getBigDecimal(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getBigDecimal(this.physicalRowIndex(l2));
    }

    BigDecimal getBigDecimal(long l2, int n2, int n3) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getBigDecimal(this.physicalRowIndex(l2), n3);
    }

    Blob getBlob(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getBlob(this.physicalRowIndex(l2));
    }

    boolean getBoolean(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getBoolean(this.physicalRowIndex(l2));
    }

    byte getByte(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getByte(this.physicalRowIndex(l2));
    }

    byte[] getBytes(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getBytes(this.physicalRowIndex(l2));
    }

    Clob getClob(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getClob(this.physicalRowIndex(l2));
    }

    Date getDate(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getDate(this.physicalRowIndex(l2));
    }

    Date getDate(long l2, int n2, Calendar calendar) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getDate(this.physicalRowIndex(l2), calendar);
    }

    double getDouble(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getDouble(this.physicalRowIndex(l2));
    }

    float getFloat(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getFloat(this.physicalRowIndex(l2));
    }

    int getInt(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getInt(this.physicalRowIndex(l2));
    }

    long getLong(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getLong(this.physicalRowIndex(l2));
    }

    NClob getNClob(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getNClob(this.physicalRowIndex(l2));
    }

    String getNString(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getNString(this.physicalRowIndex(l2));
    }

    Object getObject(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getObject(this.physicalRowIndex(l2));
    }

    Object getObject(long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getObject(this.physicalRowIndex(l2), map);
    }

    Ref getRef(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getRef(this.physicalRowIndex(l2));
    }

    RowId getRowId(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getRowId(this.physicalRowIndex(l2));
    }

    short getShort(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getShort(this.physicalRowIndex(l2));
    }

    SQLXML getSQLXML(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getSQLXML(this.physicalRowIndex(l2));
    }

    String getString(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getString(this.physicalRowIndex(l2));
    }

    Time getTime(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getTime(this.physicalRowIndex(l2));
    }

    Time getTime(long l2, int n2, Calendar calendar) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getTime(this.physicalRowIndex(l2), calendar);
    }

    Timestamp getTimestamp(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getTimestamp(this.physicalRowIndex(l2));
    }

    Timestamp getTimestamp(long l2, int n2, Calendar calendar) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getTimestamp(this.physicalRowIndex(l2), calendar);
    }

    URL getURL(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getURL(this.physicalRowIndex(l2));
    }

    ARRAY getARRAY(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getARRAY(this.physicalRowIndex(l2));
    }

    BFILE getBFILE(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getBFILE(this.physicalRowIndex(l2));
    }

    BFILE getBfile(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getBfile(this.physicalRowIndex(l2));
    }

    BLOB getBLOB(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getBLOB(this.physicalRowIndex(l2));
    }

    CHAR getCHAR(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getCHAR(this.physicalRowIndex(l2));
    }

    CLOB getCLOB(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getCLOB(this.physicalRowIndex(l2));
    }

    ResultSet getCursor(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getCursor(this.physicalRowIndex(l2));
    }

    DATE getDATE(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getDATE(this.physicalRowIndex(l2));
    }

    INTERVALDS getINTERVALDS(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getINTERVALDS(this.physicalRowIndex(l2));
    }

    INTERVALYM getINTERVALYM(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getINTERVALYM(this.physicalRowIndex(l2));
    }

    NUMBER getNUMBER(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getNUMBER(this.physicalRowIndex(l2));
    }

    OPAQUE getOPAQUE(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getOPAQUE(this.physicalRowIndex(l2));
    }

    Datum getOracleObject(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getOracleObject(this.physicalRowIndex(l2));
    }

    ORAData getORAData(long l2, int n2, ORADataFactory oRADataFactory) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getORAData(this.physicalRowIndex(l2), oRADataFactory);
    }

    Object getObject(long l2, int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getObject(this.physicalRowIndex(l2), oracleDataFactory);
    }

    RAW getRAW(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getRAW(this.physicalRowIndex(l2));
    }

    REF getREF(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getREF(this.physicalRowIndex(l2));
    }

    ROWID getROWID(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getROWID(this.physicalRowIndex(l2));
    }

    STRUCT getSTRUCT(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getSTRUCT(this.physicalRowIndex(l2));
    }

    TIMESTAMPLTZ getTIMESTAMPLTZ(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getTIMESTAMPLTZ(this.physicalRowIndex(l2));
    }

    TIMESTAMPTZ getTIMESTAMPTZ(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getTIMESTAMPTZ(this.physicalRowIndex(l2));
    }

    TIMESTAMP getTIMESTAMP(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getTIMESTAMP(this.physicalRowIndex(l2));
    }

    CustomDatum getCustomDatum(long l2, int n2, CustomDatumFactory customDatumFactory) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getCustomDatum(this.physicalRowIndex(l2), customDatumFactory);
    }

    InputStream getAsciiStream(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getAsciiStream(this.physicalRowIndex(l2));
    }

    InputStream getBinaryStream(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getBinaryStream(this.physicalRowIndex(l2));
    }

    Reader getCharacterStream(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getCharacterStream(this.physicalRowIndex(l2));
    }

    Reader getNCharacterStream(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getNCharacterStream(this.physicalRowIndex(l2));
    }

    InputStream getUnicodeStream(long l2, int n2) throws SQLException {
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return this.accessors[n2 + this.offsetOfFirstUserColumn].getUnicodeStream(this.physicalRowIndex(l2));
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return this.connection;
    }
}

