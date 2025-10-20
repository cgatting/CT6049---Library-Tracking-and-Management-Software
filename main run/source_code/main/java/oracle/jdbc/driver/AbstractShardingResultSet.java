/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.driver.AbstractShardingConnection;
import oracle.jdbc.driver.AbstractShardingLob;
import oracle.jdbc.driver.AbstractShardingStatement;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.ShardingDriverExtension;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleResultSet;
import oracle.jdbc.proxy.annotation.GetCreator;
import oracle.jdbc.proxy.annotation.GetDelegate;
import oracle.jdbc.proxy.annotation.Methods;
import oracle.jdbc.proxy.annotation.Pre;
import oracle.jdbc.proxy.annotation.ProxyFor;
import oracle.jdbc.proxy.annotation.ProxyResult;
import oracle.jdbc.proxy.annotation.ProxyResultPolicy;
import oracle.jdbc.proxy.annotation.SetDelegate;
import oracle.jdbc.proxy.annotation.Signature;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import oracle.sql.REF;
import oracle.sql.STRUCT;

@ProxyFor(value={OracleResultSet.class})
@ProxyResult(value=ProxyResultPolicy.MANUAL)
public abstract class AbstractShardingResultSet {
    boolean closed;

    @GetCreator
    protected abstract Object getCreator();

    @GetDelegate
    protected abstract ResultSet getDelegate();

    @SetDelegate
    protected abstract void setDelegate(ResultSet var1);

    public Blob getBlob(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        Blob blob = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            blob = oracleResultSet.getBlob(n2);
            Blob blob2 = blob = (Blob)this.createLobProxy(blob);
            return blob2;
        }
    }

    public Blob getBlob(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        Blob blob = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            blob = oracleResultSet.getBlob(string);
            Blob blob2 = blob = (Blob)this.createLobProxy(blob);
            return blob2;
        }
    }

    public Clob getClob(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        Clob clob = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            clob = oracleResultSet.getClob(n2);
            Clob clob2 = clob = (Clob)this.createLobProxy(clob);
            return clob2;
        }
    }

    public Clob getClob(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        Clob clob = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            clob = oracleResultSet.getClob(string);
            Clob clob2 = clob = (Clob)this.createLobProxy(clob);
            return clob2;
        }
    }

    public NClob getNClob(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        NClob nClob = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            nClob = oracleResultSet.getNClob(n2);
            NClob nClob2 = nClob = (NClob)this.createLobProxy(nClob);
            return nClob2;
        }
    }

    public NClob getNClob(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        NClob nClob = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            nClob = oracleResultSet.getNClob(string);
            NClob nClob2 = nClob = (NClob)this.createLobProxy(nClob);
            return nClob2;
        }
    }

    public Ref getRef(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        Ref ref = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            ref = oracleResultSet.getRef(n2);
            Ref ref2 = ref = (Ref)this.createLobProxy(ref);
            return ref2;
        }
    }

    public Ref getRef(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        Ref ref = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            ref = oracleResultSet.getRef(string);
            Ref ref2 = ref = (Ref)this.createLobProxy(ref);
            return ref2;
        }
    }

    public Array getArray(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        Array array = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            array = oracleResultSet.getArray(n2);
            Array array2 = array = (Array)this.createLobProxy(array);
            return array2;
        }
    }

    public Array getArray(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        Array array = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            array = oracleResultSet.getArray(string);
            Array array2 = array = (Array)this.createLobProxy(array);
            return array2;
        }
    }

    public BLOB getBLOB(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        BLOB bLOB = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            bLOB = oracleResultSet.getBLOB(n2);
            bLOB.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            BLOB bLOB2 = bLOB;
            return bLOB2;
        }
    }

    public BLOB getBLOB(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        BLOB bLOB = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            bLOB = oracleResultSet.getBLOB(string);
            bLOB.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            BLOB bLOB2 = bLOB;
            return bLOB2;
        }
    }

    public CLOB getCLOB(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        CLOB cLOB = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            cLOB = oracleResultSet.getCLOB(n2);
            cLOB.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            CLOB cLOB2 = cLOB;
            return cLOB2;
        }
    }

    public CLOB getCLOB(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        CLOB cLOB = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            cLOB = oracleResultSet.getCLOB(string);
            cLOB.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            CLOB cLOB2 = cLOB;
            return cLOB2;
        }
    }

    public BFILE getBFILE(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        BFILE bFILE = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            bFILE = oracleResultSet.getBFILE(n2);
            bFILE.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            BFILE bFILE2 = bFILE;
            return bFILE2;
        }
    }

    public BFILE getBFILE(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        BFILE bFILE = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            bFILE = oracleResultSet.getBFILE(string);
            bFILE.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            BFILE bFILE2 = bFILE;
            return bFILE2;
        }
    }

    public BFILE getBfile(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        BFILE bFILE = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            bFILE = oracleResultSet.getBfile(n2);
            bFILE.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            BFILE bFILE2 = bFILE;
            return bFILE2;
        }
    }

    public BFILE getBfile(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        BFILE bFILE = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            bFILE = oracleResultSet.getBfile(string);
            bFILE.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            BFILE bFILE2 = bFILE;
            return bFILE2;
        }
    }

    public REF getREF(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        REF rEF = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            rEF = oracleResultSet.getREF(n2);
            rEF.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            REF rEF2 = rEF;
            return rEF2;
        }
    }

    public REF getREF(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        REF rEF = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            rEF = oracleResultSet.getREF(string);
            rEF.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            REF rEF2 = rEF;
            return rEF2;
        }
    }

    public STRUCT getSTRUCT(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        STRUCT sTRUCT = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            sTRUCT = oracleResultSet.getSTRUCT(n2);
            sTRUCT.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            STRUCT sTRUCT2 = sTRUCT;
            return sTRUCT2;
        }
    }

    public STRUCT getSTRUCT(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        STRUCT sTRUCT = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            sTRUCT = oracleResultSet.getSTRUCT(string);
            sTRUCT.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            STRUCT sTRUCT2 = sTRUCT;
            return sTRUCT2;
        }
    }

    public ARRAY getARRAY(int n2) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        ARRAY aRRAY = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            aRRAY = oracleResultSet.getARRAY(n2);
            aRRAY.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            ARRAY aRRAY2 = aRRAY;
            return aRRAY2;
        }
    }

    public ARRAY getARRAY(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        ARRAY aRRAY = null;
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            aRRAY = oracleResultSet.getARRAY(string);
            aRRAY.createAndSetShardingLobProxy(AbstractShardingLob.class, this);
            ARRAY aRRAY2 = aRRAY;
            return aRRAY2;
        }
    }

    public Statement getStatement() throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            this.ensureOpen("getStatement");
            Statement statement = (Statement)((Object)abstractShardingStatement);
            return statement;
        }
    }

    @Pre
    @Methods(signatures={@Signature(name="setACProxy", args={Object.class}), @Signature(name="getACProxy", args={})})
    protected void preUnsupportedResultSetMethods(Method method, Object object, Object ... objectArray) {
        throw new RuntimeException((SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace());
    }

    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        if (clazz.isInterface()) {
            return clazz.isInstance(this);
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface() && clazz.isInstance(this)) {
            return (T)this;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    public ResultSet getCursor(int n2) throws SQLException {
        ResultSet resultSet = null;
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            resultSet = oracleResultSet.getCursor(n2);
            if (resultSet != null) {
                resultSet = this.createResultSetProxy(resultSet);
            }
        }
        return resultSet;
    }

    public ResultSet getCursor(String string) throws SQLException {
        ResultSet resultSet = null;
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            resultSet = oracleResultSet.getCursor(string);
            if (resultSet != null) {
                resultSet = this.createResultSetProxy(resultSet);
            }
        }
        return resultSet;
    }

    public void close() throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        try (Monitor.CloseableLock closeableLock = ((AbstractShardingConnection)abstractShardingStatement.getCreator()).acquireConnectionCloseableLock();){
            if (this.closed) {
                return;
            }
            OracleResultSet oracleResultSet = (OracleResultSet)this.getDelegate();
            OracleConnection oracleConnection = (OracleConnection)oracleResultSet.getStatement().getConnection();
            oracleResultSet.close();
            abstractShardingStatement.currentResultSet = null;
            this.closed = true;
            abstractShardingStatement.closeByDependent(oracleConnection);
        }
    }

    boolean isComplete() throws SQLException {
        return this.closed;
    }

    public boolean isClosed() throws SQLException {
        return this.closed;
    }

    void ensureOpen(String string) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        if (this.closed) {
            if (abstractShardingStatement.getConnection().isClosed()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8, string).fillInStackTrace();
            }
            throw (SQLException)DatabaseError.createSqlException(10, string).fillInStackTrace();
        }
        if (abstractShardingStatement.closed) {
            throw (SQLException)DatabaseError.createSqlException(9, string).fillInStackTrace();
        }
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        return abstractShardingStatement.getConnectionDuringExceptionHandling();
    }

    protected ResultSet createResultSetProxy(ResultSet resultSet) throws SQLException {
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)this.getCreator();
        return ShardingDriverExtension.PROXY_FACTORY.proxyFor(resultSet, abstractShardingStatement);
    }

    protected Object createLobProxy(Object object) throws SQLException {
        return ShardingDriverExtension.PROXY_FACTORY.proxyFor(object, this);
    }
}

