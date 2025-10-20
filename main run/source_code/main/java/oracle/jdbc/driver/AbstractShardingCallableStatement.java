/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
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
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.driver.AbstractShardingConnection;
import oracle.jdbc.driver.AbstractShardingPreparedStatement;
import oracle.jdbc.driver.AbstractShardingStatement;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.proxy.annotation.GetCreator;
import oracle.jdbc.proxy.annotation.GetDelegate;
import oracle.jdbc.proxy.annotation.Methods;
import oracle.jdbc.proxy.annotation.OnError;
import oracle.jdbc.proxy.annotation.Post;
import oracle.jdbc.proxy.annotation.Pre;
import oracle.jdbc.proxy.annotation.ProxyFor;
import oracle.jdbc.proxy.annotation.ProxyResult;
import oracle.jdbc.proxy.annotation.ProxyResultPolicy;
import oracle.jdbc.proxy.annotation.SetDelegate;
import oracle.jdbc.proxy.annotation.Signature;
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

@ProxyFor(value={OracleCallableStatement.class})
@ProxyResult(value=ProxyResultPolicy.MANUAL)
public abstract class AbstractShardingCallableStatement
extends AbstractShardingPreparedStatement {
    @Override
    @GetCreator
    protected abstract Object getCreator();

    @Override
    @GetDelegate
    protected abstract Statement getDelegate();

    @Override
    @SetDelegate
    protected abstract void setDelegate(Statement var1);

    @Override
    void initialize(AbstractShardingConnection abstractShardingConnection, String string, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        super.initialize(abstractShardingConnection, string, properties);
        this.statementType = 2;
    }

    public void setArray(String string, Array array) throws SQLException {
        this.setArrayAtName(string, array);
    }

    public void setBigDecimal(String string, BigDecimal bigDecimal) throws SQLException {
        this.setBigDecimalAtName(string, bigDecimal);
    }

    public void setBlob(String string, Blob blob) throws SQLException {
        this.setBlobAtName(string, blob);
    }

    public void setBoolean(String string, boolean bl) throws SQLException {
        this.setBooleanAtName(string, bl);
    }

    public void setByte(String string, byte by) throws SQLException {
        this.setByteAtName(string, by);
    }

    public void setBytes(String string, byte[] byArray) throws SQLException {
        this.setBytesAtName(string, byArray);
    }

    public void setClob(String string, Clob clob) throws SQLException {
        this.setClobAtName(string, clob);
    }

    public void setDate(String string, Date date) throws SQLException {
        this.setDateAtName(string, date);
    }

    public void setDate(String string, Date date, Calendar calendar) throws SQLException {
        this.setDateAtName(string, date, calendar);
    }

    public void setDouble(String string, double d2) throws SQLException {
        this.setDoubleAtName(string, d2);
    }

    public void setFloat(String string, float f2) throws SQLException {
        this.setFloatAtName(string, f2);
    }

    public void setInt(String string, int n2) throws SQLException {
        this.setIntAtName(string, n2);
    }

    public void setLong(String string, long l2) throws SQLException {
        this.setLongAtName(string, l2);
    }

    public void setNClob(String string, NClob nClob) throws SQLException {
        this.setNClobAtName(string, nClob);
    }

    public void setNString(String string, String string2) throws SQLException {
        this.setNStringAtName(string, string2);
    }

    public void setObject(String string, Object object) throws SQLException {
        this.setObjectAtName(string, object);
    }

    public void setObject(String string, Object object, int n2) throws SQLException {
        this.setObjectAtName(string, object, n2);
    }

    public void setRef(String string, Ref ref) throws SQLException {
        this.setRefAtName(string, ref);
    }

    public void setRowId(String string, RowId rowId) throws SQLException {
        this.setRowIdAtName(string, rowId);
    }

    public void setShort(String string, short s2) throws SQLException {
        this.setShortAtName(string, s2);
    }

    public void setSQLXML(String string, SQLXML sQLXML) throws SQLException {
        this.setSQLXMLAtName(string, sQLXML);
    }

    public void setString(String string, String string2) throws SQLException {
        this.setStringAtName(string, string2);
    }

    public void setTime(String string, Time time) throws SQLException {
        this.setTimeAtName(string, time);
    }

    public void setTime(String string, Time time, Calendar calendar) throws SQLException {
        this.setTimeAtName(string, time, calendar);
    }

    public void setTimestamp(String string, Timestamp timestamp) throws SQLException {
        this.setTimestampAtName(string, timestamp);
    }

    public void setTimestamp(String string, Timestamp timestamp, Calendar calendar) throws SQLException {
        this.setTimestampAtName(string, timestamp, calendar);
    }

    public void setURL(String string, URL uRL) throws SQLException {
        this.setURLAtName(string, uRL);
    }

    public void setARRAY(String string, ARRAY aRRAY) throws SQLException {
        this.setARRAYAtName(string, aRRAY);
    }

    public void setBFILE(String string, BFILE bFILE) throws SQLException {
        this.setBFILEAtName(string, bFILE);
    }

    public void setBfile(String string, BFILE bFILE) throws SQLException {
        this.setBfileAtName(string, bFILE);
    }

    public void setBinaryFloat(String string, float f2) throws SQLException {
        this.setBinaryFloatAtName(string, f2);
    }

    public void setBinaryFloat(String string, BINARY_FLOAT bINARY_FLOAT) throws SQLException {
        this.setBinaryFloatAtName(string, bINARY_FLOAT);
    }

    public void setBinaryDouble(String string, double d2) throws SQLException {
        this.setBinaryDoubleAtName(string, d2);
    }

    public void setBinaryDouble(String string, BINARY_DOUBLE bINARY_DOUBLE) throws SQLException {
        this.setBinaryDoubleAtName(string, bINARY_DOUBLE);
    }

    public void setBLOB(String string, BLOB bLOB) throws SQLException {
        this.setBLOBAtName(string, bLOB);
    }

    public void setCHAR(String string, CHAR cHAR) throws SQLException {
        this.setCHARAtName(string, cHAR);
    }

    public void setCLOB(String string, CLOB cLOB) throws SQLException {
        this.setCLOBAtName(string, cLOB);
    }

    public void setCursor(String string, ResultSet resultSet) throws SQLException {
        this.setCursorAtName(string, resultSet);
    }

    public void setDATE(String string, DATE dATE) throws SQLException {
        this.setDATEAtName(string, dATE);
    }

    public void setFixedCHAR(String string, String string2) throws SQLException {
        this.setFixedCHARAtName(string, string2);
    }

    public void setINTERVALDS(String string, INTERVALDS iNTERVALDS) throws SQLException {
        this.setINTERVALDSAtName(string, iNTERVALDS);
    }

    public void setINTERVALYM(String string, INTERVALYM iNTERVALYM) throws SQLException {
        this.setINTERVALYMAtName(string, iNTERVALYM);
    }

    public void setNUMBER(String string, NUMBER nUMBER) throws SQLException {
        this.setNUMBERAtName(string, nUMBER);
    }

    public void setOPAQUE(String string, OPAQUE oPAQUE) throws SQLException {
        this.setOPAQUEAtName(string, oPAQUE);
    }

    public void setOracleObject(String string, Datum datum) throws SQLException {
        this.setOracleObjectAtName(string, datum);
    }

    public void setORAData(String string, ORAData oRAData) throws SQLException {
        this.setORADataAtName(string, oRAData);
    }

    public void setRAW(String string, RAW rAW) throws SQLException {
        this.setRAWAtName(string, rAW);
    }

    public void setREF(String string, REF rEF) throws SQLException {
        this.setREFAtName(string, rEF);
    }

    public void setRefType(String string, REF rEF) throws SQLException {
        this.setRefTypeAtName(string, rEF);
    }

    public void setROWID(String string, ROWID rOWID) throws SQLException {
        this.setROWIDAtName(string, rOWID);
    }

    public void setSTRUCT(String string, STRUCT sTRUCT) throws SQLException {
        this.setSTRUCTAtName(string, sTRUCT);
    }

    public void setTIMESTAMPLTZ(String string, TIMESTAMPLTZ tIMESTAMPLTZ) throws SQLException {
        this.setTIMESTAMPLTZAtName(string, tIMESTAMPLTZ);
    }

    public void setTIMESTAMPTZ(String string, TIMESTAMPTZ tIMESTAMPTZ) throws SQLException {
        this.setTIMESTAMPTZAtName(string, tIMESTAMPTZ);
    }

    public void setTIMESTAMP(String string, TIMESTAMP tIMESTAMP) throws SQLException {
        this.setTIMESTAMPAtName(string, tIMESTAMP);
    }

    public void setCustomDatum(String string, CustomDatum customDatum) throws SQLException {
        this.setCustomDatumAtName(string, customDatum);
    }

    public void setBlob(String string, InputStream inputStream) throws SQLException {
        this.setBlobAtName(string, inputStream);
    }

    public void setBlob(String string, InputStream inputStream, long l2) throws SQLException {
        this.setBlobAtName(string, inputStream, l2);
    }

    public void setClob(String string, Reader reader) throws SQLException {
        this.setClobAtName(string, reader);
    }

    public void setClob(String string, Reader reader, long l2) throws SQLException {
        this.setClobAtName(string, reader, l2);
    }

    public void setNClob(String string, Reader reader) throws SQLException {
        this.setNClobAtName(string, reader);
    }

    public void setNClob(String string, Reader reader, long l2) throws SQLException {
        this.setNClobAtName(string, reader, l2);
    }

    public void setAsciiStream(String string, InputStream inputStream) throws SQLException {
        this.setAsciiStreamAtName(string, inputStream);
    }

    public void setAsciiStream(String string, InputStream inputStream, int n2) throws SQLException {
        this.setAsciiStreamAtName(string, inputStream, n2);
    }

    public void setAsciiStream(String string, InputStream inputStream, long l2) throws SQLException {
        this.setAsciiStreamAtName(string, inputStream, l2);
    }

    public void setBinaryStream(String string, InputStream inputStream) throws SQLException {
        this.setBinaryStreamAtName(string, inputStream);
    }

    public void setBinaryStream(String string, InputStream inputStream, int n2) throws SQLException {
        this.setBinaryStreamAtName(string, inputStream, n2);
    }

    public void setBinaryStream(String string, InputStream inputStream, long l2) throws SQLException {
        this.setBinaryStreamAtName(string, inputStream, l2);
    }

    public void setCharacterStream(String string, Reader reader) throws SQLException {
        this.setCharacterStreamAtName(string, reader);
    }

    public void setCharacterStream(String string, Reader reader, int n2) throws SQLException {
        this.setCharacterStreamAtName(string, reader, n2);
    }

    public void setCharacterStream(String string, Reader reader, long l2) throws SQLException {
        this.setCharacterStreamAtName(string, reader, l2);
    }

    public void setNCharacterStream(String string, Reader reader) throws SQLException {
        this.setNCharacterStreamAtName(string, reader);
    }

    public void setNCharacterStream(String string, Reader reader, long l2) throws SQLException {
        this.setNCharacterStreamAtName(string, reader, l2);
    }

    public void setUnicodeStream(String string, InputStream inputStream, int n2) throws SQLException {
        this.setUnicodeStreamAtName(string, inputStream, n2);
    }

    public void setNull(String string, int n2, String string2) throws SQLException {
        this.setNullAtName(string, n2, string2);
    }

    public void setNull(String string, int n2) throws SQLException {
        this.setNullAtName(string, n2);
    }

    public void setStructDescriptor(String string, StructDescriptor structDescriptor) throws SQLException {
        this.setStructDescriptorAtName(string, structDescriptor);
    }

    public void setStringForClob(String string, String string2) throws SQLException {
        this.setStringForClobAtName(string, string2);
    }

    public void setBytesForBlob(String string, byte[] byArray) throws SQLException {
        this.setBytesForBlobAtName(string, byArray);
    }

    public void registerOutParameter(int n2, int n3, int n4, int n5) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string = "registerOutParameter";
            Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE};
            Object[] objectArray = new Object[]{n2, n3, n4, n5};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string, classArray, objectArray);
            this.bindMap.put(n2, callHistoryEntry);
        }
    }

    public void registerOutParameterBytes(int n2, int n3, int n4, int n5) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string = "registerOutParameterBytes";
            Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE};
            Object[] objectArray = new Object[]{n2, n3, n4, n5};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string, classArray, objectArray);
            this.bindMap.put(n2, callHistoryEntry);
        }
    }

    public void registerOutParameterChars(int n2, int n3, int n4, int n5) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string = "registerOutParameterChars";
            Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE};
            Object[] objectArray = new Object[]{n2, n3, n4, n5};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string, classArray, objectArray);
            this.bindMap.put(n2, callHistoryEntry);
        }
    }

    public void registerOutParameter(int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string = "registerOutParameter";
            Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE};
            Object[] objectArray = new Object[]{n2, n3};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string, classArray, objectArray);
            this.bindMap.put(n2, callHistoryEntry);
        }
    }

    public void registerOutParameter(int n2, int n3, int n4) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string = "registerOutParameter";
            Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE};
            Object[] objectArray = new Object[]{n2, n3, n4};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string, classArray, objectArray);
            this.bindMap.put(n2, callHistoryEntry);
        }
    }

    public void registerOutParameter(int n2, int n3, String string) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string2 = "registerOutParameter";
            Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, String.class};
            Object[] objectArray = new Object[]{n2, n3, string};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string2, classArray, objectArray);
            this.bindMap.put(n2, callHistoryEntry);
        }
    }

    public void registerIndexTableOutParameter(int n2, int n3, int n4, int n5) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string = "registerIndexTableOutParameter";
            Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE};
            Object[] objectArray = new Object[]{n2, n3, n4, n5};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string, classArray, objectArray);
            this.bindMap.put(n2, callHistoryEntry);
        }
    }

    public void registerOutParameter(String string, int n2, int n3, int n4) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string2 = "registerOutParameter";
            Class[] classArray = new Class[]{String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE};
            Object[] objectArray = new Object[]{string, n2, n3, n4};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string2, classArray, objectArray);
            int n5 = this.addNamedPara(string);
            this.bindMap.put(n5, callHistoryEntry);
        }
    }

    public void registerOutParameterAtName(String string, int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string2 = "registerOutParameterAtName";
            Class[] classArray = new Class[]{String.class, Integer.TYPE};
            Object[] objectArray = new Object[]{string, n2};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string2, classArray, objectArray);
            int n3 = this.addNamedPara(string);
            this.bindMap.put(n3, callHistoryEntry);
        }
    }

    public void registerOutParameterAtName(String string, int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string2 = "registerOutParameterAtName";
            Class[] classArray = new Class[]{String.class, Integer.TYPE, Integer.TYPE};
            Object[] objectArray = new Object[]{string, n2, n3};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string2, classArray, objectArray);
            int n4 = this.addNamedPara(string);
            this.bindMap.put(n4, callHistoryEntry);
        }
    }

    public void registerOutParameterAtName(String string, int n2, String string2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string3 = "registerOutParameterAtName";
            Class[] classArray = new Class[]{String.class, Integer.TYPE, String.class};
            Object[] objectArray = new Object[]{string, n2, string2};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string3, classArray, objectArray);
            int n3 = this.addNamedPara(string);
            this.bindMap.put(n3, callHistoryEntry);
        }
    }

    public void registerOutParameter(String string, int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string2 = "registerOutParameter";
            Class[] classArray = new Class[]{String.class, Integer.TYPE};
            Object[] objectArray = new Object[]{string, n2};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string2, classArray, objectArray);
            int n3 = this.addNamedPara(string);
            this.bindMap.put(n3, callHistoryEntry);
        }
    }

    public void registerOutParameter(String string, int n2, int n3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string2 = "registerOutParameter";
            Class[] classArray = new Class[]{String.class, Integer.TYPE, Integer.TYPE};
            Object[] objectArray = new Object[]{string, n2, n3};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string2, classArray, objectArray);
            int n4 = this.addNamedPara(string);
            this.bindMap.put(n4, callHistoryEntry);
        }
    }

    public void registerOutParameter(String string, int n2, String string2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
            }
            String string3 = "registerOutParameter";
            Class[] classArray = new Class[]{String.class, Integer.TYPE, String.class};
            Object[] objectArray = new Object[]{string, n2, string2};
            AbstractShardingStatement.CallHistoryEntry callHistoryEntry = new AbstractShardingStatement.CallHistoryEntry(string3, classArray, objectArray);
            int n3 = this.addNamedPara(string);
            this.bindMap.put(n3, callHistoryEntry);
        }
    }

    public ARRAY getARRAY(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                ARRAY aRRAY = oracleCallableStatement.getARRAY(n2);
                return aRRAY;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public InputStream getAsciiStream(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                InputStream inputStream = oracleCallableStatement.getAsciiStream(n2);
                return inputStream;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public BFILE getBFILE(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                BFILE bFILE = oracleCallableStatement.getBFILE(n2);
                return bFILE;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public BFILE getBfile(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                BFILE bFILE = oracleCallableStatement.getBfile(n2);
                return bFILE;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public InputStream getBinaryStream(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                InputStream inputStream = oracleCallableStatement.getBinaryStream(n2);
                return inputStream;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public BLOB getBLOB(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                BLOB bLOB = oracleCallableStatement.getBLOB(n2);
                return bLOB;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public CHAR getCHAR(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                CHAR cHAR = oracleCallableStatement.getCHAR(n2);
                return cHAR;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Reader getCharacterStream(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Reader reader = oracleCallableStatement.getCharacterStream(n2);
                return reader;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public CLOB getCLOB(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                CLOB cLOB = oracleCallableStatement.getCLOB(n2);
                return cLOB;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Object getCustomDatum(int n2, CustomDatumFactory customDatumFactory) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Object object = oracleCallableStatement.getCustomDatum(n2, customDatumFactory);
                return object;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Object getORAData(int n2, ORADataFactory oRADataFactory) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Object object = oracleCallableStatement.getORAData(n2, oRADataFactory);
                return object;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Object getObject(int n2, OracleDataFactory oracleDataFactory) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Object object = oracleCallableStatement.getObject(n2, oracleDataFactory);
                return object;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Object getAnyDataEmbeddedObject(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Object object = oracleCallableStatement.getAnyDataEmbeddedObject(n2);
                return object;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public DATE getDATE(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                DATE dATE = oracleCallableStatement.getDATE(n2);
                return dATE;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public NUMBER getNUMBER(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                NUMBER nUMBER = oracleCallableStatement.getNUMBER(n2);
                return nUMBER;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public OPAQUE getOPAQUE(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                OPAQUE oPAQUE = oracleCallableStatement.getOPAQUE(n2);
                return oPAQUE;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Datum getOracleObject(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Datum datum = oracleCallableStatement.getOracleObject(n2);
                return datum;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public RAW getRAW(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                RAW rAW = oracleCallableStatement.getRAW(n2);
                return rAW;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public REF getREF(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                REF rEF = oracleCallableStatement.getREF(n2);
                return rEF;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public ROWID getROWID(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                ROWID rOWID = oracleCallableStatement.getROWID(n2);
                return rOWID;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public STRUCT getSTRUCT(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                STRUCT sTRUCT = oracleCallableStatement.getSTRUCT(n2);
                return sTRUCT;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public INTERVALYM getINTERVALYM(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                INTERVALYM iNTERVALYM = oracleCallableStatement.getINTERVALYM(n2);
                return iNTERVALYM;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public INTERVALDS getINTERVALDS(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                INTERVALDS iNTERVALDS = oracleCallableStatement.getINTERVALDS(n2);
                return iNTERVALDS;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public TIMESTAMP getTIMESTAMP(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                TIMESTAMP tIMESTAMP = oracleCallableStatement.getTIMESTAMP(n2);
                return tIMESTAMP;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public TIMESTAMPTZ getTIMESTAMPTZ(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                TIMESTAMPTZ tIMESTAMPTZ = oracleCallableStatement.getTIMESTAMPTZ(n2);
                return tIMESTAMPTZ;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public TIMESTAMPLTZ getTIMESTAMPLTZ(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                TIMESTAMPLTZ tIMESTAMPLTZ = oracleCallableStatement.getTIMESTAMPLTZ(n2);
                return tIMESTAMPLTZ;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public InputStream getUnicodeStream(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                InputStream inputStream = oracleCallableStatement.getUnicodeStream(n2);
                return inputStream;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Object getPlsqlIndexTable(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Object object = oracleCallableStatement.getPlsqlIndexTable(n2);
                return object;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Object getPlsqlIndexTable(int n2, Class<?> clazz) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Object object = oracleCallableStatement.getPlsqlIndexTable(n2, clazz);
                return object;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Datum[] getOraclePlsqlIndexTable(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Datum[] datumArray = oracleCallableStatement.getOraclePlsqlIndexTable(n2);
                return datumArray;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Array getArray(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Array array = oracleCallableStatement.getArray(n2);
                return array;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public BigDecimal getBigDecimal(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                BigDecimal bigDecimal = oracleCallableStatement.getBigDecimal(n2);
                return bigDecimal;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                BigDecimal bigDecimal = oracleCallableStatement.getBigDecimal(n2, n3);
                return bigDecimal;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Blob getBlob(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Blob blob = oracleCallableStatement.getBlob(n2);
                return blob;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public boolean getBoolean(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                boolean bl = oracleCallableStatement.getBoolean(n2);
                return bl;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public byte getByte(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                byte by = oracleCallableStatement.getByte(n2);
                return by;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public byte[] getBytes(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                byte[] byArray = oracleCallableStatement.getBytes(n2);
                return byArray;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Clob getClob(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Clob clob = oracleCallableStatement.getClob(n2);
                return clob;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Date getDate(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Date date = oracleCallableStatement.getDate(n2);
                return date;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Date getDate(int n2, Calendar calendar) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Date date = oracleCallableStatement.getDate(n2, calendar);
                return date;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public double getDouble(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                double d2 = oracleCallableStatement.getDouble(n2);
                return d2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public float getFloat(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                float f2 = oracleCallableStatement.getFloat(n2);
                return f2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public int getInt(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                int n3 = oracleCallableStatement.getInt(n2);
                return n3;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public long getLong(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                long l2 = oracleCallableStatement.getLong(n2);
                return l2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Reader getNCharacterStream(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Reader reader = oracleCallableStatement.getNCharacterStream(n2);
                return reader;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public NClob getNClob(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                NClob nClob = oracleCallableStatement.getNClob(n2);
                return nClob;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public String getNString(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                String string = oracleCallableStatement.getNString(n2);
                return string;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Object getObject(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Object object = oracleCallableStatement.getObject(n2);
                return object;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Object object = oracleCallableStatement.getObject(n2, map);
                return object;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Ref getRef(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Ref ref = oracleCallableStatement.getRef(n2);
                return ref;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public RowId getRowId(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                RowId rowId = oracleCallableStatement.getRowId(n2);
                return rowId;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public SQLXML getSQLXML(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                SQLXML sQLXML = oracleCallableStatement.getSQLXML(n2);
                return sQLXML;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public short getShort(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                short s2 = oracleCallableStatement.getShort(n2);
                return s2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public String getString(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                String string = oracleCallableStatement.getString(n2);
                return string;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Time getTime(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Time time = oracleCallableStatement.getTime(n2);
                return time;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Time getTime(int n2, Calendar calendar) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Time time = oracleCallableStatement.getTime(n2, calendar);
                return time;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Timestamp getTimestamp(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Timestamp timestamp = oracleCallableStatement.getTimestamp(n2);
                return timestamp;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Timestamp getTimestamp(int n2, Calendar calendar) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Timestamp timestamp = oracleCallableStatement.getTimestamp(n2, calendar);
                return timestamp;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public URL getURL(int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                URL uRL = oracleCallableStatement.getURL(n2);
                return uRL;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public InputStream getAsciiStream(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                InputStream inputStream = oracleCallableStatement.getAsciiStream(string);
                return inputStream;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public InputStream getBinaryStream(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                InputStream inputStream = oracleCallableStatement.getBinaryStream(string);
                return inputStream;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Reader getCharacterStream(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Reader reader = oracleCallableStatement.getCharacterStream(string);
                return reader;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public InputStream getUnicodeStream(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                InputStream inputStream = oracleCallableStatement.getUnicodeStream(string);
                return inputStream;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Array getArray(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Array array = oracleCallableStatement.getArray(string);
                return array;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public BigDecimal getBigDecimal(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                BigDecimal bigDecimal = oracleCallableStatement.getBigDecimal(string);
                return bigDecimal;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public BigDecimal getBigDecimal(String string, int n2) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                BigDecimal bigDecimal = oracleCallableStatement.getBigDecimal(string, n2);
                return bigDecimal;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Blob getBlob(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Blob blob = oracleCallableStatement.getBlob(string);
                return blob;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public boolean getBoolean(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                boolean bl = oracleCallableStatement.getBoolean(string);
                return bl;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public byte getByte(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                byte by = oracleCallableStatement.getByte(string);
                return by;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public byte[] getBytes(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                byte[] byArray = oracleCallableStatement.getBytes(string);
                return byArray;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Clob getClob(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Clob clob = oracleCallableStatement.getClob(string);
                return clob;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Date getDate(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Date date = oracleCallableStatement.getDate(string);
                return date;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Date getDate(String string, Calendar calendar) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Date date = oracleCallableStatement.getDate(string, calendar);
                return date;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public double getDouble(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                double d2 = oracleCallableStatement.getDouble(string);
                return d2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public float getFloat(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                float f2 = oracleCallableStatement.getFloat(string);
                return f2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public int getInt(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                int n2 = oracleCallableStatement.getInt(string);
                return n2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public long getLong(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                long l2 = oracleCallableStatement.getLong(string);
                return l2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Reader getNCharacterStream(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Reader reader = oracleCallableStatement.getNCharacterStream(string);
                return reader;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public NClob getNClob(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                NClob nClob = oracleCallableStatement.getNClob(string);
                return nClob;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public String getNString(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                String string2 = oracleCallableStatement.getNString(string);
                return string2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Object getObject(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Object object = oracleCallableStatement.getObject(string);
                return object;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Object getObject(String string, Map<String, Class<?>> map) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Object object = oracleCallableStatement.getObject(string, map);
                return object;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Ref getRef(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Ref ref = oracleCallableStatement.getRef(string);
                return ref;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public RowId getRowId(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                RowId rowId = oracleCallableStatement.getRowId(string);
                return rowId;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public SQLXML getSQLXML(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                SQLXML sQLXML = oracleCallableStatement.getSQLXML(string);
                return sQLXML;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public short getShort(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                short s2 = oracleCallableStatement.getShort(string);
                return s2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public String getString(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                String string2 = oracleCallableStatement.getString(string);
                return string2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Time getTime(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Time time = oracleCallableStatement.getTime(string);
                return time;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Time getTime(String string, Calendar calendar) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Time time = oracleCallableStatement.getTime(string, calendar);
                return time;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Timestamp getTimestamp(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Timestamp timestamp = oracleCallableStatement.getTimestamp(string);
                return timestamp;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public Timestamp getTimestamp(String string, Calendar calendar) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                Timestamp timestamp = oracleCallableStatement.getTimestamp(string, calendar);
                return timestamp;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public URL getURL(String string) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                URL uRL = oracleCallableStatement.getURL(string);
                return uRL;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public byte[] privateGetBytes(int n2) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                byte[] byArray = oracleCallableStatement.privateGetBytes(n2);
                return byArray;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public boolean wasNull() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                boolean bl = oracleCallableStatement.wasNull();
                return bl;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public void setObject(String string, Object object, int n2, int n3) throws SQLException {
        this.setObjectAtName(string, object, n2, n3);
    }

    public <T> T getObject(int n2, Class<T> clazz) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                T t2 = oracleCallableStatement.getObject(n2, clazz);
                return t2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public <T> T getObject(String string, Class<T> clazz) throws SQLException {
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
            OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
            if (oracleCallableStatement != null) {
                T t2 = oracleCallableStatement.getObject(string, clazz);
                return t2;
            }
            try {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
        }
    }

    public ResultSet getCursor(int n2) throws SQLException {
        ResultSet resultSet;
        block14: {
            resultSet = null;
            try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)this.getCreator()).acquireConnectionCloseableLock();){
                OracleCallableStatement oracleCallableStatement = (OracleCallableStatement)this.getDelegate();
                if (oracleCallableStatement != null) {
                    resultSet = oracleCallableStatement.getCursor(n2);
                    if (resultSet != null) {
                        resultSet = this.createResultSetProxy(resultSet);
                    }
                    break block14;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
        }
        return resultSet;
    }

    @Override
    @Pre
    @Methods(signatures={@Signature(name="executeQuery", args={}), @Signature(name="execute", args={}), @Signature(name="executeUpdate", args={})})
    protected void prePstmtExecuteQuery(Method method, Object object, Object ... objectArray) {
        super.prePstmtExecuteQuery(method, object, objectArray);
    }

    @Override
    @Post
    @Methods(signatures={@Signature(name="executeQuery", args={})})
    protected ResultSet postPstmtExecuteQuery(Method method, ResultSet resultSet) {
        return super.postPstmtExecuteQuery(method, resultSet);
    }

    @Override
    @Post
    @Methods(signatures={@Signature(name="execute", args={}), @Signature(name="executeUpdate", args={})})
    protected Object postPstmtExecuteUpdate(Method method, Object object) {
        return super.postPstmtExecuteUpdate(method, object);
    }

    @OnError(value=SQLException.class)
    protected Object onErrorCstmt(Method method, SQLException sQLException) throws SQLException {
        return super.onErrorPstmt(method, sQLException);
    }

    @Override
    @Pre
    @Methods(signatures={@Signature(name="executeQuery", args={String.class}), @Signature(name="execute", args={String.class}), @Signature(name="execute", args={String.class, int.class}), @Signature(name="execute", args={String.class, int[].class}), @Signature(name="execute", args={String.class, String[].class}), @Signature(name="executeUpdate", args={String.class}), @Signature(name="executeUpdate", args={String.class, int.class}), @Signature(name="executeUpdate", args={String.class, int[].class}), @Signature(name="executeUpdate", args={String.class, String[].class})})
    protected void preStmtExecuteQuery(Method method, Object object, Object ... objectArray) {
        super.preStmtExecuteQuery(method, object, objectArray);
    }

    @Override
    @Post
    @Methods(signatures={@Signature(name="executeQuery", args={String.class})})
    protected ResultSet postStmtExecuteQuery(Method method, ResultSet resultSet) {
        return super.postStmtExecuteQuery(method, resultSet);
    }

    @Override
    @Post
    @Methods(signatures={@Signature(name="execute", args={String.class}), @Signature(name="execute", args={String.class, int.class}), @Signature(name="execute", args={String.class, int[].class}), @Signature(name="execute", args={String.class, String[].class}), @Signature(name="executeUpdate", args={String.class}), @Signature(name="executeUpdate", args={String.class, int.class}), @Signature(name="executeUpdate", args={String.class, int[].class}), @Signature(name="executeUpdate", args={String.class, String[].class})})
    protected Object postStmtExecuteUpdate(Method method, Object object) {
        return super.postStmtExecuteUpdate(method, object);
    }

    @Override
    @Pre
    @Methods(signatures={@Signature(name="creationState", args={}), @Signature(name="setACProxy", args={Object.class}), @Signature(name="getACProxy", args={}), @Signature(name="setShardingKeyRpnTokens", args={byte[].class}), @Signature(name="getShardingKeyRpnTokens", args={}), @Signature(name="setCursorName", args={String.class}), @Signature(name="getserverCursor", args={})})
    protected void preUnsupportedStatementMethods(Method method, Object object, Object ... objectArray) {
        super.preUnsupportedStatementMethods(method, object, objectArray);
    }

    @Override
    protected OracleStatement createDatabaseStatement(OracleConnection oracleConnection, String string) throws SQLException {
        return (OracleStatement)((Object)oracleConnection.prepareCall(string, this.userResultSetType, this.userResultSetConcur));
    }
}

