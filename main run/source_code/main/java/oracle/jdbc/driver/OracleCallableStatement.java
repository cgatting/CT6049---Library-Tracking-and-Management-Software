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
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OraclePreparedStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.PlsqlIbtBindInfo;
import oracle.jdbc.driver.PlsqlIndexTableAccessor;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.ANYDATA;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
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
import oracle.sql.StructDescriptor;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONNECT})
abstract class OracleCallableStatement
extends OraclePreparedStatement
implements oracle.jdbc.internal.OracleCallableStatement {
    boolean atLeastOneOrdinalParameter = false;
    boolean atLeastOneNamedParameter = false;
    String[] namedParameters = new String[8];
    int parameterCount = 0;
    final String errMsgMixedBind = "Ordinal binding and Named binding cannot be combined!";
    static final int INITREMAININGCURSORS = -1;
    int remainingCursors;

    OracleCallableStatement(PhysicalConnection physicalConnection, String string, int n2, int n3) throws SQLException {
        this(physicalConnection, string, n2, n3, 1003, 1007);
    }

    OracleCallableStatement(PhysicalConnection physicalConnection, String string, int n2, int n3, int n4, int n5) throws SQLException {
        super(physicalConnection, string, 1, n3, n4, n5);
        this.statementType = 2;
        this.remainingCursors = -1;
    }

    OracleCallableStatement(PhysicalConnection physicalConnection, String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        super(physicalConnection, string, properties);
        this.statementType = 2;
        this.remainingCursors = -1;
    }

    void registerOutParameterInternal(int n2, int n3, int n4, int n5, String string) throws SQLException {
        this.ensureOpen();
        int n6 = n2 - 1;
        if (n6 < 0 || n2 > this.numberOfBindPositions) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        if (n3 == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
        int n7 = this.getInternalType(n3);
        if (n3 == 252 && (this.connection.databaseMetaData.getDatabaseMajorVersion() < 12 || this.connection.databaseMetaData.getDatabaseMajorVersion() == 12 && this.connection.databaseMetaData.getDatabaseMinorVersion() < 1)) {
            throw (SQLException)DatabaseError.createSqlException(299).fillInStackTrace();
        }
        this.resetBatch();
        this.currentRowNeedToPrepareBinds = true;
        if (this.currentRowBindAccessors == null) {
            this.currentRowBindAccessors = new Accessor[this.numberOfBindPositions];
        }
        switch (n3) {
            case -4: 
            case -3: 
            case -1: 
            case 1: 
            case 12: 
            case 70: {
                break;
            }
            case -16: 
            case -15: 
            case -9: {
                this.currentRowFormOfUse[n6] = 2;
                break;
            }
            case 2011: {
                n5 = 0;
                this.currentRowFormOfUse[n6] = 2;
                break;
            }
            case 2009: {
                n5 = 0;
                string = "SYS.XMLTYPE";
                break;
            }
            case 2002: 
            case 2003: {
                if (string == null || string.length() == 0) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
                }
                n5 = 0;
                break;
            }
            default: {
                n5 = 0;
            }
        }
        this.currentRowBindAccessors[n6] = this.allocateAccessor(n7, n3, n6 + 1, n5, this.currentRowFormOfUse[n6], string, true);
    }

    @Override
    public void registerOutParameter(int n2, int n3, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.registerOutParameterInternal(n2, n3, 0, -1, string);
            this.atLeastOneOrdinalParameter = true;
        }
    }

    @Override
    @Deprecated
    public void registerOutParameterBytes(int n2, int n3, int n4, int n5) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.registerOutParameterInternal(n2, n3, n4, n5, null);
            this.atLeastOneOrdinalParameter = true;
        }
    }

    @Override
    @Deprecated
    public void registerOutParameterChars(int n2, int n3, int n4, int n5) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.registerOutParameterInternal(n2, n3, n4, n5, null);
            this.atLeastOneOrdinalParameter = true;
        }
    }

    @Override
    public void registerOutParameter(int n2, int n3, int n4, int n5) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.registerOutParameterInternal(n2, n3, n4, n5, null);
            this.atLeastOneOrdinalParameter = true;
        }
    }

    @Override
    public void registerOutParameter(String string, int n2, int n3, int n4) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.registerOutParameterInternal(string, n2, n3, n4, null);
            this.atLeastOneNamedParameter = true;
        }
    }

    @Override
    public void registerOutParameterAtName(String string, int n2) throws SQLException {
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n3 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n3; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.registerOutParameter(i2 + 1, n2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void registerOutParameterAtName(String string, int n2, int n3) throws SQLException {
        String string2 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n4 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n4; ++i2) {
            if (stringArray[i2] != string2) continue;
            if (bl) {
                this.registerOutParameter(i2 + 1, n2, n3);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    public void registerOutParameterAtName(String string, int n2, String string2) throws SQLException {
        String string3 = string.intern();
        String[] stringArray = this.sqlObject.getParameterList();
        int n3 = Math.min(this.sqlObject.getParameterCount(), stringArray.length);
        boolean bl = true;
        for (int i2 = 0; i2 < n3; ++i2) {
            if (stringArray[i2] != string3) continue;
            if (bl) {
                this.registerOutParameter(i2 + 1, n2, string2);
                bl = false;
                continue;
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 135).fillInStackTrace();
        }
        if (bl) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 147, string).fillInStackTrace();
        }
    }

    @Override
    boolean isOracleBatchStyle() {
        return false;
    }

    @Override
    void resetBatch() {
        this.batch = 1;
    }

    @Override
    public void setExecuteBatch(int n2) throws SQLException {
    }

    @Override
    public int sendBatch() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n2 = (int)this.validRows;
            return n2;
        }
    }

    @Override
    public void registerOutParameter(int n2, int n3) throws SQLException {
        this.registerOutParameter(n2, n3, 0, -1);
    }

    @Override
    public void registerOutParameter(int n2, int n3, int n4) throws SQLException {
        this.registerOutParameter(n2, n3, n4, -1);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return this.wasNullValue(0L);
    }

    @Override
    public String getString(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getString(this.currentRank);
    }

    @Override
    public Datum getOracleObject(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getOracleObject(this.currentRank);
    }

    @Override
    public ROWID getROWID(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getROWID(this.currentRank);
    }

    @Override
    public NUMBER getNUMBER(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getNUMBER(this.currentRank);
    }

    @Override
    public DATE getDATE(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getDATE(this.currentRank);
    }

    @Override
    public INTERVALYM getINTERVALYM(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getINTERVALYM(this.currentRank);
    }

    @Override
    public INTERVALDS getINTERVALDS(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getINTERVALDS(this.currentRank);
    }

    @Override
    public TIMESTAMP getTIMESTAMP(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTIMESTAMP(this.currentRank);
    }

    @Override
    public TIMESTAMPTZ getTIMESTAMPTZ(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTIMESTAMPTZ(this.currentRank);
    }

    @Override
    public TIMESTAMPLTZ getTIMESTAMPLTZ(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTIMESTAMPLTZ(this.currentRank);
    }

    @Override
    public REF getREF(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getREF(this.currentRank);
    }

    @Override
    public ARRAY getARRAY(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getARRAY(this.currentRank);
    }

    @Override
    public STRUCT getSTRUCT(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getSTRUCT(this.currentRank);
    }

    @Override
    public OPAQUE getOPAQUE(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getOPAQUE(this.currentRank);
    }

    @Override
    public CHAR getCHAR(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getCHAR(this.currentRank);
    }

    @Override
    public Reader getCharacterStream(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getCharacterStream(this.currentRank);
    }

    @Override
    public RAW getRAW(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getRAW(this.currentRank);
    }

    @Override
    public BLOB getBLOB(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBLOB(this.currentRank);
    }

    @Override
    public CLOB getCLOB(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getCLOB(this.currentRank);
    }

    @Override
    public BFILE getBFILE(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBFILE(this.currentRank);
    }

    @Override
    public BFILE getBfile(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBFILE(this.currentRank);
    }

    @Override
    public boolean getBoolean(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBoolean(this.currentRank);
    }

    @Override
    public byte getByte(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getByte(this.currentRank);
    }

    @Override
    public short getShort(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getShort(this.currentRank);
    }

    @Override
    public int getInt(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getInt(this.currentRank);
    }

    @Override
    public long getLong(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getLong(this.currentRank);
    }

    @Override
    public float getFloat(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getFloat(this.currentRank);
    }

    @Override
    public double getDouble(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getDouble(this.currentRank);
    }

    @Override
    public BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBigDecimal(this.currentRank);
    }

    @Override
    public byte[] getBytes(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBytes(this.currentRank);
    }

    @Override
    public byte[] privateGetBytes(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBytesInternal(this.currentRank);
    }

    @Override
    public Date getDate(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getDate(this.currentRank);
    }

    @Override
    public Time getTime(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTime(this.currentRank);
    }

    @Override
    public Timestamp getTimestamp(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTimestamp(this.currentRank);
    }

    @Override
    public InputStream getAsciiStream(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getAsciiStream(this.currentRank);
    }

    @Override
    public InputStream getUnicodeStream(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getUnicodeStream(this.currentRank);
    }

    @Override
    public InputStream getBinaryStream(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBinaryStream(this.currentRank);
    }

    @Override
    public Object getObject(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getObject(this.currentRank);
    }

    @Override
    public Object getAnyDataEmbeddedObject(int n2) throws SQLException {
        Datum datum;
        Object object = null;
        Object object2 = this.getObject(n2);
        if (object2 instanceof ANYDATA && (datum = ((ANYDATA)object2).accessDatum()) != null) {
            object = datum.toJdbc();
        }
        return object;
    }

    @Override
    public Object getCustomDatum(int n2, CustomDatumFactory customDatumFactory) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getCustomDatum(this.currentRank, customDatumFactory);
    }

    @Override
    public Object getObject(int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getObject(this.currentRank, oracleDataFactory);
    }

    @Override
    public Object getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getORAData(this.currentRank, oRADataFactory);
    }

    @Override
    public ResultSet getCursor(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getCursor(this.currentRank);
    }

    @Override
    public void clearParameters() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            super.clearParameters();
        }
    }

    @Override
    public Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getObject(this.currentRank, map);
    }

    @Override
    public Ref getRef(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getREF(this.currentRank);
    }

    @Override
    public Blob getBlob(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBLOB(this.currentRank);
    }

    @Override
    public Clob getClob(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getCLOB(this.currentRank);
    }

    @Override
    public Array getArray(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getARRAY(this.currentRank);
    }

    @Override
    public BigDecimal getBigDecimal(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBigDecimal(this.currentRank);
    }

    @Override
    public Date getDate(int n2, Calendar calendar) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getDate(this.currentRank, calendar);
    }

    @Override
    public Time getTime(int n2, Calendar calendar) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTime(this.currentRank, calendar);
    }

    @Override
    public Timestamp getTimestamp(int n2, Calendar calendar) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTimestamp(this.currentRank, calendar);
    }

    @Override
    public void addBatch() throws SQLException {
        if (this.currentRowBindAccessors != null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Stored procedure with out or inout parameters cannot be batched").fillInStackTrace();
        }
        super.addBatch();
    }

    @Override
    protected void alwaysOnClose() throws SQLException {
        this.sqlObject.resetNamedParameters();
        this.namedParameters = new String[8];
        this.parameterCount = 0;
        this.atLeastOneOrdinalParameter = false;
        this.atLeastOneNamedParameter = false;
        super.alwaysOnClose();
    }

    @Override
    public void registerOutParameter(String string, int n2) throws SQLException {
        this.registerOutParameterInternal(string, n2, 0, -1, null);
        this.atLeastOneNamedParameter = true;
    }

    @Override
    public void registerOutParameter(String string, int n2, int n3) throws SQLException {
        this.registerOutParameterInternal(string, n2, n3, -1, null);
        this.atLeastOneNamedParameter = true;
    }

    @Override
    public void registerOutParameter(String string, int n2, String string2) throws SQLException {
        this.registerOutParameterInternal(string, n2, 0, -1, string2);
        this.atLeastOneNamedParameter = true;
    }

    void registerOutParameterInternal(String string, int n2, int n3, int n4, String string2) throws SQLException {
        int n5 = this.addNamedPara(string);
        this.registerOutParameterInternal(n5, n2, n3, n4, string2);
    }

    @Override
    public URL getURL(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getURL(this.currentRank);
    }

    @Override
    public void setStringForClob(String string, String string2) throws SQLException {
        int n2 = this.addNamedPara(string);
        if (string2 == null || string2.length() == 0) {
            this.setNull(n2, 2005);
            return;
        }
        this.setStringForClob(n2, string2);
    }

    @Override
    public void setStringForClob(int n2, String string) throws SQLException {
        if (string == null || string.length() == 0) {
            this.setNull(n2, 2005);
            return;
        }
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setStringForClobCritical(n2, string);
        }
    }

    @Override
    public void setBytesForBlob(String string, byte[] byArray) throws SQLException {
        int n2 = this.addNamedPara(string);
        this.setBytesForBlob(n2, byArray);
    }

    @Override
    public void setBytesForBlob(int n2, byte[] byArray) throws SQLException {
        if (byArray == null || byArray.length == 0) {
            this.setNull(n2, 2004);
            return;
        }
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.setBytesForBlobCritical(n2, byArray);
        }
    }

    @Override
    public String getString(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getString(this.currentRank);
    }

    @Override
    public boolean getBoolean(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBoolean(this.currentRank);
    }

    @Override
    public byte getByte(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getByte(this.currentRank);
    }

    @Override
    public short getShort(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getShort(this.currentRank);
    }

    @Override
    public int getInt(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getInt(this.currentRank);
    }

    @Override
    public long getLong(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getLong(this.currentRank);
    }

    @Override
    public float getFloat(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getFloat(this.currentRank);
    }

    @Override
    public double getDouble(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getDouble(this.currentRank);
    }

    @Override
    public byte[] getBytes(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBytes(this.currentRank);
    }

    @Override
    public Date getDate(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getDate(this.currentRank);
    }

    @Override
    public Time getTime(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTime(this.currentRank);
    }

    @Override
    public Timestamp getTimestamp(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTimestamp(this.currentRank);
    }

    @Override
    public Object getObject(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getObject(this.currentRank);
    }

    @Override
    public BigDecimal getBigDecimal(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBigDecimal(this.currentRank);
    }

    @Override
    public BigDecimal getBigDecimal(String string, int n2) throws SQLException {
        int n3;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n3 = 0; n3 < this.parameterCount && string2 != this.namedParameters[n3]; ++n3) {
        }
        Accessor accessor = null;
        if (++n3 <= 0 || n3 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n3 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n3;
        if (this.streamList != null) {
            this.closeUsedStreams(n3);
        }
        return accessor.getBigDecimal(this.currentRank, n2);
    }

    @Override
    public Object getObject(String string, Map<String, Class<?>> map) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getObject(this.currentRank, map);
    }

    @Override
    public Ref getRef(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getREF(this.currentRank);
    }

    @Override
    public Blob getBlob(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBLOB(this.currentRank);
    }

    @Override
    public Clob getClob(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getCLOB(this.currentRank);
    }

    @Override
    public Array getArray(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getARRAY(this.currentRank);
    }

    @Override
    public Date getDate(String string, Calendar calendar) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getDate(this.currentRank, calendar);
    }

    @Override
    public Time getTime(String string, Calendar calendar) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTime(this.currentRank, calendar);
    }

    @Override
    public Timestamp getTimestamp(String string, Calendar calendar) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getTimestamp(this.currentRank, calendar);
    }

    @Override
    public URL getURL(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getURL(this.currentRank);
    }

    @Override
    @Deprecated
    public InputStream getAsciiStream(String string) throws SQLException {
        throw (SQLException)DatabaseError.createSQLFeatureNotSupportedException("getAsciiStream").fillInStackTrace();
    }

    @Override
    @Deprecated
    public void registerIndexTableOutParameter(int n2, int n3, int n4, int n5) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            int n6 = n2 - 1;
            if (n6 < 0 || n2 > this.numberOfBindPositions) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
            }
            int n7 = this.getInternalType(n4);
            if (n7 == 96 || n7 == 1) {
                if (n5 < 0 || n5 > this.maxIbtVarcharElementLength) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 53).fillInStackTrace();
                }
                if (n5 == 0) {
                    n5 = this.maxIbtVarcharElementLength;
                }
            }
            this.resetBatch();
            this.currentRowNeedToPrepareBinds = true;
            if (this.currentRowBindAccessors == null) {
                this.currentRowBindAccessors = new Accessor[this.numberOfBindPositions];
            }
            this.currentRowBindAccessors[n6] = this.allocateIndexTableAccessor(new PlsqlIbtBindInfo(this, null, n3, 0, n7, n5), this.currentRowFormOfUse[n6]);
            this.hasIbtBind = true;
        }
    }

    Accessor allocateIndexTableAccessor(PlsqlIbtBindInfo plsqlIbtBindInfo, short s2) throws SQLException {
        return new PlsqlIndexTableAccessor(this, plsqlIbtBindInfo, s2);
    }

    @Override
    @Deprecated
    public Object getPlsqlIndexTable(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum[] datumArray = this.getOraclePlsqlIndexTable(n2);
            Accessor accessor = this.outBindAccessors[n2 - 1];
            int n3 = accessor.plsqlIndexTableBindInfo().element_internal_type;
            Object[] objectArray = null;
            switch (n3) {
                case 9: {
                    objectArray = new String[datumArray.length];
                    break;
                }
                case 6: {
                    objectArray = new BigDecimal[datumArray.length];
                    break;
                }
                case 12: {
                    objectArray = new Timestamp[datumArray.length];
                    break;
                }
                case 180: {
                    objectArray = new TIMESTAMP[datumArray.length];
                    break;
                }
                default: {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Invalid column type").fillInStackTrace();
                }
            }
            for (int i2 = 0; i2 < objectArray.length; ++i2) {
                objectArray[i2] = datumArray[i2] != null && datumArray[i2].getLength() != 0L ? datumArray[i2].toJdbc() : null;
            }
            Object[] objectArray2 = objectArray;
            return objectArray2;
        }
    }

    @Override
    @Deprecated
    public Object getPlsqlIndexTable(int n2, Class<?> clazz) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            Datum[] datumArray = this.getOraclePlsqlIndexTable(n2);
            if (clazz == null || !clazz.isPrimitive()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            String string = clazz.getName();
            if (string.equals("byte")) {
                byte[] byArray = new byte[datumArray.length];
                for (int i2 = 0; i2 < datumArray.length; ++i2) {
                    byArray[i2] = datumArray[i2] != null ? datumArray[i2].byteValue() : (byte)0;
                }
                byte[] byArray2 = byArray;
                return byArray2;
            }
            if (string.equals("char")) {
                char[] cArray = new char[datumArray.length];
                for (int i3 = 0; i3 < datumArray.length; ++i3) {
                    cArray[i3] = datumArray[i3] != null && datumArray[i3].getLength() != 0L ? (char)datumArray[i3].intValue() : (char)'\u0000';
                }
                char[] cArray2 = cArray;
                return cArray2;
            }
            if (string.equals("double")) {
                double[] dArray = new double[datumArray.length];
                for (int i4 = 0; i4 < datumArray.length; ++i4) {
                    dArray[i4] = datumArray[i4] != null && datumArray[i4].getLength() != 0L ? datumArray[i4].doubleValue() : 0.0;
                }
                double[] dArray2 = dArray;
                return dArray2;
            }
            if (string.equals("float")) {
                float[] fArray = new float[datumArray.length];
                for (int i5 = 0; i5 < datumArray.length; ++i5) {
                    fArray[i5] = datumArray[i5] != null && datumArray[i5].getLength() != 0L ? datumArray[i5].floatValue() : 0.0f;
                }
                float[] fArray2 = fArray;
                return fArray2;
            }
            if (string.equals("int")) {
                int[] nArray = new int[datumArray.length];
                for (int i6 = 0; i6 < datumArray.length; ++i6) {
                    nArray[i6] = datumArray[i6] != null && datumArray[i6].getLength() != 0L ? datumArray[i6].intValue() : 0;
                }
                int[] nArray2 = nArray;
                return nArray2;
            }
            if (string.equals("long")) {
                long[] lArray = new long[datumArray.length];
                for (int i7 = 0; i7 < datumArray.length; ++i7) {
                    lArray[i7] = datumArray[i7] != null && datumArray[i7].getLength() != 0L ? datumArray[i7].longValue() : 0L;
                }
                long[] lArray2 = lArray;
                return lArray2;
            }
            if (string.equals("short")) {
                short[] sArray = new short[datumArray.length];
                for (int i8 = 0; i8 < datumArray.length; ++i8) {
                    sArray[i8] = datumArray[i8] != null && datumArray[i8].getLength() != 0L ? (short)datumArray[i8].intValue() : (short)0;
                }
                short[] sArray2 = sArray;
                return sArray2;
            }
            if (string.equals("boolean")) {
                boolean[] blArray = new boolean[datumArray.length];
                for (int i9 = 0; i9 < datumArray.length; ++i9) {
                    blArray[i9] = datumArray[i9] != null && datumArray[i9].getLength() != 0L ? datumArray[i9].booleanValue() : false;
                }
                boolean[] blArray2 = blArray;
                return blArray2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 23).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    @Override
    @Deprecated
    public Datum[] getOraclePlsqlIndexTable(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            if (this.atLeastOneNamedParameter) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
            }
            Accessor accessor = null;
            if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
            }
            this.lastIndex = n2;
            if (this.streamList != null) {
                this.closeUsedStreams(n2);
            }
            Datum[] datumArray = accessor.getOraclePlsqlIndexTable(this.currentRank);
            return datumArray;
        }
    }

    @Override
    public boolean execute() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.atLeastOneNamedParameter && this.atLeastOneOrdinalParameter) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
            }
            if (this.sqlObject.setNamedParameters(this.parameterCount, this.namedParameters)) {
                this.needToParse = true;
            }
            boolean bl = super.execute();
            return bl;
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            this.ensureOpen();
            if (this.atLeastOneNamedParameter && this.atLeastOneOrdinalParameter) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
            }
            if (this.sqlObject.setNamedParameters(this.parameterCount, this.namedParameters)) {
                this.needToParse = true;
            }
            int n2 = super.executeUpdate();
            return n2;
        }
    }

    @Override
    void releaseBuffers() {
        super.releaseBuffers();
    }

    @Override
    public void setArray(int n2, Array array) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setArrayInternal(n2, array);
    }

    @Override
    public void setBigDecimal(int n2, BigDecimal bigDecimal) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBigDecimalInternal(n2, bigDecimal);
        }
    }

    @Override
    public void setBlob(int n2, Blob blob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBlobInternal(n2, blob);
        }
    }

    @Override
    public void setBoolean(int n2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBooleanInternal(n2, bl);
        }
    }

    @Override
    public void setByte(int n2, byte by) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setByteInternal(n2, by);
        }
    }

    @Override
    public void setBytes(int n2, byte[] byArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBytesInternal(n2, byArray);
        }
    }

    @Override
    public void setClob(int n2, Clob clob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setFormOfUseInternal(n2, this.defaultFormOfUse);
            this.setClobInternal(n2, clob);
        }
    }

    @Override
    public void setDate(int n2, Date date) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setDateInternal(n2, date);
        }
    }

    @Override
    public void setDate(int n2, Date date, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setDateInternal(n2, date, calendar);
        }
    }

    @Override
    public void setDouble(int n2, double d2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setDoubleInternal(n2, d2);
        }
    }

    @Override
    public void setFloat(int n2, float f2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setFloatInternal(n2, f2);
        }
    }

    @Override
    public void setInt(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setIntInternal(n2, n3);
        }
    }

    @Override
    public void setLong(int n2, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setLongInternal(n2, l2);
        }
    }

    @Override
    public void setNClob(int n2, NClob nClob) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setNClobInternal(n2, nClob);
        }
    }

    @Override
    public void setNString(int n2, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setNStringInternal(n2, string);
        }
    }

    @Override
    public void setObject(int n2, Object object) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setObjectInternal(n2, object);
    }

    @Override
    public void setObject(int n2, Object object, int n3) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setObjectInternal(n2, object, n3);
    }

    @Override
    public void setRef(int n2, Ref ref) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setRefInternal(n2, ref);
    }

    @Override
    public void setRowId(int n2, RowId rowId) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setRowIdInternal(n2, rowId);
        }
    }

    @Override
    public void setShort(int n2, short s2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setShortInternal(n2, s2);
        }
    }

    @Override
    public void setSQLXML(int n2, SQLXML sQLXML) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setSQLXMLInternal(n2, sQLXML);
        }
    }

    @Override
    public void setString(int n2, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setStringInternal(n2, string);
        }
    }

    @Override
    public void setTime(int n2, Time time) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setTimeInternal(n2, time);
        }
    }

    @Override
    public void setTime(int n2, Time time, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setTimeInternal(n2, time, calendar);
        }
    }

    @Override
    public void setTimestamp(int n2, Timestamp timestamp) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setTimestampInternal(n2, timestamp);
        }
    }

    @Override
    public void setTimestamp(int n2, Timestamp timestamp, Calendar calendar) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setTimestampInternal(n2, timestamp, calendar);
        }
    }

    @Override
    public void setURL(int n2, URL uRL) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setURLInternal(n2, uRL);
        }
    }

    @Override
    public void setARRAY(int n2, ARRAY aRRAY) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setARRAYInternal(n2, aRRAY);
    }

    @Override
    public void setBFILE(int n2, BFILE bFILE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBFILEInternal(n2, bFILE);
        }
    }

    @Override
    public void setBfile(int n2, BFILE bFILE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBfileInternal(n2, bFILE);
        }
    }

    @Override
    public void setBinaryFloat(int n2, float f2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBinaryFloatInternal(n2, f2);
        }
    }

    @Override
    public void setBinaryFloat(int n2, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBinaryFloatInternal(n2, bINARY_FLOAT);
        }
    }

    @Override
    public void setBinaryDouble(int n2, double d2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBinaryDoubleInternal(n2, d2);
        }
    }

    @Override
    public void setBinaryDouble(int n2, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBinaryDoubleInternal(n2, bINARY_DOUBLE);
        }
    }

    @Override
    public void setBLOB(int n2, BLOB bLOB) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBLOBInternal(n2, bLOB);
        }
    }

    @Override
    public void setCHAR(int n2, CHAR cHAR) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setCHARInternal(n2, cHAR);
        }
    }

    @Override
    public void setCLOB(int n2, CLOB cLOB) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setCLOBInternal(n2, cLOB);
        }
    }

    @Override
    public void setCursor(int n2, ResultSet resultSet) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setCursorInternal(n2, resultSet);
        }
    }

    @Override
    public void setDATE(int n2, DATE dATE) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setDATEInternal(n2, dATE);
        }
    }

    @Override
    public void setFixedCHAR(int n2, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setFixedCHARInternal(n2, string);
        }
    }

    @Override
    public void setINTERVALDS(int n2, INTERVALDS iNTERVALDS) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setINTERVALDSInternal(n2, iNTERVALDS);
        }
    }

    @Override
    public void setINTERVALYM(int n2, INTERVALYM iNTERVALYM) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setINTERVALYMInternal(n2, iNTERVALYM);
        }
    }

    @Override
    public void setNUMBER(int n2, NUMBER nUMBER) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setNUMBERInternal(n2, nUMBER);
        }
    }

    @Override
    public void setOPAQUE(int n2, OPAQUE oPAQUE) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setOPAQUEInternal(n2, oPAQUE);
    }

    @Override
    public void setOracleObject(int n2, Datum datum) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setOracleObjectInternal(n2, datum);
    }

    @Override
    public void setORAData(int n2, ORAData oRAData) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setORADataInternal(n2, oRAData);
    }

    @Override
    public void setRAW(int n2, RAW rAW) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setRAWInternal(n2, rAW);
        }
    }

    @Override
    public void setREF(int n2, REF rEF) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setREFInternal(n2, rEF);
    }

    @Override
    public void setRefType(int n2, REF rEF) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setRefTypeInternal(n2, rEF);
    }

    @Override
    public void setROWID(int n2, ROWID rOWID) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setROWIDInternal(n2, rOWID);
        }
    }

    @Override
    public void setSTRUCT(int n2, STRUCT sTRUCT) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setSTRUCTInternal(n2, sTRUCT);
    }

    @Override
    public void setTIMESTAMPLTZ(int n2, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setTIMESTAMPLTZInternal(n2, tIMESTAMPLTZ);
        }
    }

    @Override
    public void setTIMESTAMPTZ(int n2, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setTIMESTAMPTZInternal(n2, tIMESTAMPTZ);
        }
    }

    @Override
    public void setTIMESTAMP(int n2, TIMESTAMP tIMESTAMP) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setTIMESTAMPInternal(n2, tIMESTAMP);
        }
    }

    @Override
    public void setCustomDatum(int n2, CustomDatum customDatum) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        this.atLeastOneOrdinalParameter = true;
        this.setCustomDatumInternal(n2, customDatum);
    }

    @Override
    public void setBlob(int n2, InputStream inputStream) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBlobInternal(n2, inputStream);
        }
    }

    @Override
    public void setBlob(int n2, InputStream inputStream, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            if (l2 < 0L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setBlob() cannot be negative").fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBlobInternal(n2, inputStream, l2);
        }
    }

    @Override
    public void setClob(int n2, Reader reader) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setFormOfUseInternal(n2, this.defaultFormOfUse);
            this.setClobInternal(n2, reader);
        }
    }

    @Override
    public void setClob(int n2, Reader reader, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            if (l2 < 0L) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setClob() cannot be negative").fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setFormOfUseInternal(n2, this.defaultFormOfUse);
            this.setClobInternal(n2, reader, l2);
        }
    }

    @Override
    public void setNClob(int n2, Reader reader) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setNClobInternal(n2, reader);
        }
    }

    @Override
    public void setNClob(int n2, Reader reader, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setNClobInternal(n2, reader, l2);
        }
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setAsciiStreamInternal(n2, inputStream);
        }
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setAsciiStreamInternal(n2, inputStream, n3);
        }
    }

    @Override
    public void setAsciiStream(int n2, InputStream inputStream, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setAsciiStreamInternal(n2, inputStream, l2);
        }
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBinaryStreamInternal(n2, inputStream);
        }
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBinaryStreamInternal(n2, inputStream, n3);
        }
    }

    @Override
    public void setBinaryStream(int n2, InputStream inputStream, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setBinaryStreamInternal(n2, inputStream, l2);
        }
    }

    @Override
    public void setCharacterStream(int n2, Reader reader) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setCharacterStreamInternal(n2, reader);
        }
    }

    @Override
    public void setCharacterStream(int n2, Reader reader, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setCharacterStreamInternal(n2, reader, n3);
        }
    }

    @Override
    public void setCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setCharacterStreamInternal(n2, reader, l2);
        }
    }

    @Override
    public void setNCharacterStream(int n2, Reader reader) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setNCharacterStreamInternal(n2, reader);
        }
    }

    @Override
    public void setNCharacterStream(int n2, Reader reader, long l2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setNCharacterStreamInternal(n2, reader, l2);
        }
    }

    @Override
    public void setUnicodeStream(int n2, InputStream inputStream, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setUnicodeStreamInternal(n2, inputStream, n3);
        }
    }

    @Override
    public void setArray(String string, Array array) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setArrayInternal(n2, array);
    }

    @Override
    public void setBigDecimal(String string, BigDecimal bigDecimal) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBigDecimalInternal(n2, bigDecimal);
    }

    @Override
    public void setBlob(String string, Blob blob) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBlobInternal(n2, blob);
    }

    @Override
    public void setBoolean(String string, boolean bl) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBooleanInternal(n2, bl);
    }

    @Override
    public void setByte(String string, byte by) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setByteInternal(n2, by);
    }

    @Override
    public void setBytes(String string, byte[] byArray) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBytesInternal(n2, byArray);
    }

    @Override
    public void setClob(String string, Clob clob) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setClobInternal(n2, clob);
    }

    @Override
    public void setDate(String string, Date date) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setDateInternal(n2, date);
    }

    @Override
    public void setDate(String string, Date date, Calendar calendar) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setDateInternal(n2, date, calendar);
    }

    @Override
    public void setDouble(String string, double d2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setDoubleInternal(n2, d2);
    }

    @Override
    public void setFloat(String string, float f2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setFloatInternal(n2, f2);
    }

    @Override
    public void setInt(String string, int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n3 = this.addNamedPara(string);
        this.setIntInternal(n3, n2);
    }

    @Override
    public void setLong(String string, long l2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setLongInternal(n2, l2);
    }

    @Override
    public void setNClob(String string, NClob nClob) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setNClobInternal(n2, nClob);
    }

    @Override
    public void setNString(String string, String string2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setNStringInternal(n2, string2);
    }

    @Override
    public void setObject(String string, Object object) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setObjectInternal(n2, object);
    }

    @Override
    public void setObject(String string, Object object, int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n3 = this.addNamedPara(string);
        this.setObjectInternal(n3, object, n2);
    }

    @Override
    public void setRef(String string, Ref ref) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setRefInternal(n2, ref);
    }

    @Override
    public void setRowId(String string, RowId rowId) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setRowIdInternal(n2, rowId);
    }

    @Override
    public void setShort(String string, short s2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setShortInternal(n2, s2);
    }

    @Override
    public void setSQLXML(String string, SQLXML sQLXML) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setSQLXMLInternal(n2, sQLXML);
    }

    @Override
    public void setString(String string, String string2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setStringInternal(n2, string2);
    }

    @Override
    public void setTime(String string, Time time) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setTimeInternal(n2, time);
    }

    @Override
    public void setTime(String string, Time time, Calendar calendar) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setTimeInternal(n2, time, calendar);
    }

    @Override
    public void setTimestamp(String string, Timestamp timestamp) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setTimestampInternal(n2, timestamp);
    }

    @Override
    public void setTimestamp(String string, Timestamp timestamp, Calendar calendar) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setTimestampInternal(n2, timestamp, calendar);
    }

    @Override
    public void setURL(String string, URL uRL) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setURLInternal(n2, uRL);
    }

    @Override
    public void setARRAY(String string, ARRAY aRRAY) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setARRAYInternal(n2, aRRAY);
    }

    @Override
    public void setBFILE(String string, BFILE bFILE) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBFILEInternal(n2, bFILE);
    }

    @Override
    public void setBfile(String string, BFILE bFILE) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBfileInternal(n2, bFILE);
    }

    @Override
    public void setBinaryFloat(String string, float f2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBinaryFloatInternal(n2, f2);
    }

    @Override
    public void setBinaryFloat(String string, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBinaryFloatInternal(n2, bINARY_FLOAT);
    }

    @Override
    public void setBinaryDouble(String string, double d2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBinaryDoubleInternal(n2, d2);
    }

    @Override
    public void setBinaryDouble(String string, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBinaryDoubleInternal(n2, bINARY_DOUBLE);
    }

    @Override
    public void setBLOB(String string, BLOB bLOB) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBLOBInternal(n2, bLOB);
    }

    @Override
    public void setCHAR(String string, CHAR cHAR) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setCHARInternal(n2, cHAR);
    }

    @Override
    public void setCLOB(String string, CLOB cLOB) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setCLOBInternal(n2, cLOB);
    }

    @Override
    public void setCursor(String string, ResultSet resultSet) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setCursorInternal(n2, resultSet);
    }

    @Override
    public void setDATE(String string, DATE dATE) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setDATEInternal(n2, dATE);
    }

    @Override
    public void setFixedCHAR(String string, String string2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setFixedCHARInternal(n2, string2);
    }

    @Override
    public void setINTERVALDS(String string, INTERVALDS iNTERVALDS) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setINTERVALDSInternal(n2, iNTERVALDS);
    }

    @Override
    public void setINTERVALYM(String string, INTERVALYM iNTERVALYM) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setINTERVALYMInternal(n2, iNTERVALYM);
    }

    @Override
    public void setNUMBER(String string, NUMBER nUMBER) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setNUMBERInternal(n2, nUMBER);
    }

    @Override
    public void setOPAQUE(String string, OPAQUE oPAQUE) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setOPAQUEInternal(n2, oPAQUE);
    }

    @Override
    public void setOracleObject(String string, Datum datum) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setOracleObjectInternal(n2, datum);
    }

    @Override
    public void setORAData(String string, ORAData oRAData) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setORADataInternal(n2, oRAData);
    }

    @Override
    public void setRAW(String string, RAW rAW) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setRAWInternal(n2, rAW);
    }

    @Override
    public void setREF(String string, REF rEF) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setREFInternal(n2, rEF);
    }

    @Override
    public void setRefType(String string, REF rEF) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setRefTypeInternal(n2, rEF);
    }

    @Override
    public void setROWID(String string, ROWID rOWID) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setROWIDInternal(n2, rOWID);
    }

    @Override
    public void setSTRUCT(String string, STRUCT sTRUCT) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setSTRUCTInternal(n2, sTRUCT);
    }

    @Override
    public void setTIMESTAMPLTZ(String string, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setTIMESTAMPLTZInternal(n2, tIMESTAMPLTZ);
    }

    @Override
    public void setTIMESTAMPTZ(String string, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setTIMESTAMPTZInternal(n2, tIMESTAMPTZ);
    }

    @Override
    public void setTIMESTAMP(String string, TIMESTAMP tIMESTAMP) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setTIMESTAMPInternal(n2, tIMESTAMP);
    }

    @Override
    public void setCustomDatum(String string, CustomDatum customDatum) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setCustomDatumInternal(n2, customDatum);
    }

    @Override
    public void setBlob(String string, InputStream inputStream) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBlobInternal(n2, inputStream);
    }

    @Override
    public void setBlob(String string, InputStream inputStream, long l2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setBlob() cannot be negative").fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBlobInternal(n2, inputStream, l2);
    }

    @Override
    public void setClob(String string, Reader reader) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setClobInternal(n2, reader);
    }

    @Override
    public void setClob(String string, Reader reader, long l2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (l2 < 0L) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "length for setClob() cannot be negative").fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setClobInternal(n2, reader, l2);
    }

    @Override
    public void setNClob(String string, Reader reader) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setNClobInternal(n2, reader);
    }

    @Override
    public void setNClob(String string, Reader reader, long l2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setNClobInternal(n2, reader, l2);
    }

    @Override
    public void setAsciiStream(String string, InputStream inputStream) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setAsciiStreamInternal(n2, inputStream);
    }

    @Override
    public void setAsciiStream(String string, InputStream inputStream, int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n3 = this.addNamedPara(string);
        this.setAsciiStreamInternal(n3, inputStream, n2);
    }

    @Override
    public void setAsciiStream(String string, InputStream inputStream, long l2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setAsciiStreamInternal(n2, inputStream, l2);
    }

    @Override
    public void setBinaryStream(String string, InputStream inputStream) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBinaryStreamInternal(n2, inputStream);
    }

    @Override
    public void setBinaryStream(String string, InputStream inputStream, int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n3 = this.addNamedPara(string);
        this.setBinaryStreamInternal(n3, inputStream, n2);
    }

    @Override
    public void setBinaryStream(String string, InputStream inputStream, long l2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setBinaryStreamInternal(n2, inputStream, l2);
    }

    @Override
    public void setCharacterStream(String string, Reader reader) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setCharacterStreamInternal(n2, reader);
    }

    @Override
    public void setCharacterStream(String string, Reader reader, int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n3 = this.addNamedPara(string);
        this.setCharacterStreamInternal(n3, reader, n2);
    }

    @Override
    public void setCharacterStream(String string, Reader reader, long l2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setCharacterStreamInternal(n2, reader, l2);
    }

    @Override
    public void setNCharacterStream(String string, Reader reader) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setNCharacterStreamInternal(n2, reader);
    }

    @Override
    public void setNCharacterStream(String string, Reader reader, long l2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setNCharacterStreamInternal(n2, reader, l2);
    }

    @Override
    public void setUnicodeStream(String string, InputStream inputStream, int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n3 = this.addNamedPara(string);
        this.setUnicodeStreamInternal(n3, inputStream, n2);
    }

    @Override
    public void setNull(String string, int n2, String string2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n3 = this.addNamedPara(string);
        this.setNullInternal(n3, n2, string2);
    }

    @Override
    public void setNull(String string, int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n3 = this.addNamedPara(string);
        this.setNullInternal(n3, n2);
    }

    @Override
    public void setStructDescriptor(String string, StructDescriptor structDescriptor) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n2 = this.addNamedPara(string);
        this.setStructDescriptorInternal(n2, structDescriptor);
    }

    @Override
    public void setObject(String string, Object object, int n2, int n3) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        int n4 = this.addNamedPara(string);
        this.setObjectInternal(n4, object, n2, n3);
    }

    @Override
    @Deprecated
    public void setPlsqlIndexTable(int n2, Object object, int n3, int n4, int n5, int n6) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            this.atLeastOneOrdinalParameter = true;
            this.setPlsqlIndexTableInternal(n2, object, n3, n4, n5, n6);
        }
    }

    int addNamedPara(String string) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (int i2 = 0; i2 < this.parameterCount; ++i2) {
            if (string2 != this.namedParameters[i2]) continue;
            return i2 + 1;
        }
        if (this.parameterCount >= this.namedParameters.length) {
            String[] stringArray = new String[this.namedParameters.length * 2];
            System.arraycopy(this.namedParameters, 0, stringArray, 0, this.namedParameters.length);
            this.namedParameters = stringArray;
        }
        this.namedParameters[this.parameterCount++] = string2;
        this.atLeastOneNamedParameter = true;
        return this.parameterCount;
    }

    @Override
    public Reader getCharacterStream(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getCharacterStream(this.currentRank);
    }

    @Override
    public InputStream getUnicodeStream(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getUnicodeStream(this.currentRank);
    }

    @Override
    public InputStream getBinaryStream(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getBinaryStream(this.currentRank);
    }

    @Override
    public RowId getRowId(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getROWID(this.currentRank);
    }

    @Override
    public RowId getRowId(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getROWID(this.currentRank);
    }

    @Override
    public NClob getNClob(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getNClob(this.currentRank);
    }

    @Override
    public NClob getNClob(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getNClob(this.currentRank);
    }

    @Override
    public SQLXML getSQLXML(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getSQLXML(this.currentRank);
    }

    @Override
    public SQLXML getSQLXML(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getSQLXML(this.currentRank);
    }

    @Override
    public String getNString(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getNString(this.currentRank);
    }

    @Override
    public String getNString(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getNString(this.currentRank);
    }

    @Override
    public Reader getNCharacterStream(int n2) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getNCharacterStream(this.currentRank);
    }

    @Override
    public Reader getNCharacterStream(String string) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getNCharacterStream(this.currentRank);
    }

    @Override
    public <T> T getObject(int n2, Class<T> clazz) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        if (this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        Accessor accessor = null;
        if (n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getObject(this.currentRank, clazz);
    }

    @Override
    public <T> T getObject(String string, Class<T> clazz) throws SQLException {
        int n2;
        if (!this.atLeastOneNamedParameter) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 90, "Ordinal binding and Named binding cannot be combined!").fillInStackTrace();
        }
        if (string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string2 = string.toUpperCase().intern();
        for (n2 = 0; n2 < this.parameterCount && string2 != this.namedParameters[n2]; ++n2) {
        }
        Accessor accessor = null;
        if (++n2 <= 0 || n2 > this.numberOfBindPositions || this.outBindAccessors == null || (accessor = this.outBindAccessors[n2 - 1]) == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 6).fillInStackTrace();
        }
        this.lastIndex = n2;
        if (this.streamList != null) {
            this.closeUsedStreams(n2);
        }
        return accessor.getObject(this.currentRank, clazz);
    }

    @Override
    public void registerOutParameter(int n2, SQLType sQLType) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        this.registerOutParameter(n2, (int)sQLType.getVendorTypeNumber());
    }

    @Override
    public void registerOutParameter(int n2, SQLType sQLType, int n3) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        this.registerOutParameter(n2, (int)sQLType.getVendorTypeNumber(), n3);
    }

    @Override
    public void registerOutParameter(int n2, SQLType sQLType, String string) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        this.registerOutParameter(n2, (int)sQLType.getVendorTypeNumber(), string);
    }

    @Override
    public void registerOutParameter(String string, SQLType sQLType) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        this.registerOutParameter(string, (int)sQLType.getVendorTypeNumber());
    }

    @Override
    public void registerOutParameter(String string, SQLType sQLType, int n2) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        this.registerOutParameter(string, (int)sQLType.getVendorTypeNumber(), n2);
    }

    @Override
    public void registerOutParameter(String string, SQLType sQLType, String string2) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        this.registerOutParameter(string, (int)sQLType.getVendorTypeNumber(), string2);
    }

    @Override
    public void setObject(String string, Object object, SQLType sQLType) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        this.setObject(string, object, (int)sQLType.getVendorTypeNumber());
    }

    @Override
    public void setObject(String string, Object object, SQLType sQLType, int n2) throws SQLException {
        if (sQLType == null) {
            throw new IllegalArgumentException("null SQLType");
        }
        this.setObject(string, object, (int)sQLType.getVendorTypeNumber(), n2);
    }

    boolean isLastCursor() throws SQLException {
        if (this.remainingCursors == -1) {
            this.remainingCursors = 0;
            if (this.outBindAccessors != null) {
                for (int i2 = 0; i2 < this.outBindAccessors.length; ++i2) {
                    if (this.outBindAccessors[i2] == null || this.outBindAccessors[i2].internalType != 102 || this.outBindAccessors[i2].isNull(this.currentRank)) continue;
                    ++this.remainingCursors;
                }
            }
        }
        return --this.remainingCursors == 0;
    }

    @Override
    boolean closeByDependent() throws SQLException {
        if (this.isCloseOnCompletion && this.isLastCursor()) {
            if (this.parent == null || !this.parent.closeByDependent()) {
                this.close();
            }
            return true;
        }
        return false;
    }
}

