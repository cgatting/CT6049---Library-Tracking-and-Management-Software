/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.datasource.OracleDataSource;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.driver.AbstractShardingConnection;
import oracle.jdbc.driver.AbstractShardingResultSet;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.ShardingDriverExtension;
import oracle.jdbc.driver.ShardingKeyInfo;
import oracle.jdbc.internal.Monitor;
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

@ProxyFor(value={OracleStatement.class})
@ProxyResult(value=ProxyResultPolicy.MANUAL)
public abstract class AbstractShardingStatement {
    protected OracleDataSource ods;
    protected Properties stmtProps;
    protected int userResultSetType = -1;
    protected int userResultSetConcur = -1;
    private String currentSQL;
    ConcurrentHashMap<String, SetterCallHistoryEntry> setterStmtMap = new ConcurrentHashMap();
    boolean closed;
    static final int PLAIN_STMT = 0;
    static final int PREP_STMT = 1;
    static final int CALL_STMT = 2;
    int statementType = 0;
    boolean isCloseOnCompletion = false;
    AbstractShardingResultSet currentResultSet;
    static int NON_CACHED = 3;
    boolean lastStatementExecutionOnDirectShard = false;
    Vector<String> m_batchItems = null;
    protected CallHistoryEntry head;
    protected CallHistoryEntry tail;

    @GetCreator
    protected abstract Object getCreator();

    @GetDelegate
    protected abstract Statement getDelegate();

    @SetDelegate
    protected abstract void setDelegate(Statement var1);

    void initialize(AbstractShardingConnection abstractShardingConnection, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException {
        this.ods = new oracle.jdbc.pool.OracleDataSource();
        this.stmtProps = properties;
        if (properties != null) {
            String string = properties.getProperty("result_set_type");
            if (string != null) {
                this.userResultSetType = Integer.parseInt(string);
            }
            if ((string = properties.getProperty("result_set_concurrency")) != null) {
                this.userResultSetConcur = Integer.parseInt(string);
            }
        }
        this.currentSQL = null;
        this.closed = false;
    }

    @Pre
    @Methods(signatures={@Signature(name="executeQuery", args={String.class}), @Signature(name="execute", args={String.class}), @Signature(name="execute", args={String.class, int.class}), @Signature(name="execute", args={String.class, int[].class}), @Signature(name="execute", args={String.class, String[].class}), @Signature(name="executeUpdate", args={String.class}), @Signature(name="executeUpdate", args={String.class, int.class}), @Signature(name="executeUpdate", args={String.class, int[].class}), @Signature(name="executeUpdate", args={String.class, String[].class})})
    protected void preStmtExecuteQuery(Method method, Object object, Object ... objectArray) {
        try {
            ((AbstractShardingConnection)this.getCreator()).acquireConnectionLock();
            this.ensureOpen();
            if (this.currentResultSet != null) {
                this.currentResultSet.close();
                this.currentResultSet = null;
            }
            this.closeDatabaseStatementWithSetterReplay();
            this.currentSQL = (String)objectArray[0];
            OracleStatement oracleStatement = this.checkForKeyTokensAndGetUCPStatement(this.currentSQL);
            this.delegatesBeforeExecuteCalls(oracleStatement);
        }
        catch (SQLException sQLException) {
            this.currentSQL = null;
            ((AbstractShardingConnection)this.getCreator()).releaseConnectionLock();
            throw new RuntimeException(sQLException);
        }
    }

    @Post
    @Methods(signatures={@Signature(name="executeQuery", args={String.class})})
    protected ResultSet postStmtExecuteQuery(Method method, ResultSet resultSet) {
        try {
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            resultSet = this.createResultSetProxy(resultSet);
            this.currentResultSet = (AbstractShardingResultSet)((Object)resultSet);
            this.setShardingKeyRpnTokens(oracleStatement, this.currentSQL);
            this.makeDatabaseConnectionSticky((AbstractShardingConnection)this.getCreator(), (OracleConnection)oracleStatement.getConnection());
        }
        catch (SQLException sQLException) {
            throw new RuntimeException(sQLException);
        }
        finally {
            this.currentSQL = null;
            ((AbstractShardingConnection)this.getCreator()).releaseConnectionLock();
        }
        return resultSet;
    }

    @Post
    @Methods(signatures={@Signature(name="execute", args={String.class}), @Signature(name="execute", args={String.class, int.class}), @Signature(name="execute", args={String.class, int[].class}), @Signature(name="execute", args={String.class, String[].class}), @Signature(name="executeUpdate", args={String.class}), @Signature(name="executeUpdate", args={String.class, int.class}), @Signature(name="executeUpdate", args={String.class, int[].class}), @Signature(name="executeUpdate", args={String.class, String[].class})})
    protected Object postStmtExecuteUpdate(Method method, Object object) {
        try {
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            this.setShardingKeyRpnTokens(oracleStatement, this.currentSQL);
            this.makeDatabaseConnectionSticky((AbstractShardingConnection)this.getCreator(), (OracleConnection)oracleStatement.getConnection());
            Object object2 = object;
            return object2;
        }
        catch (SQLException sQLException) {
            throw new RuntimeException(sQLException);
        }
        finally {
            this.currentSQL = null;
            ((AbstractShardingConnection)this.getCreator()).releaseConnectionLock();
        }
    }

    @OnError(value=SQLException.class)
    protected Object onErrorStmt(Method method, SQLException sQLException) throws SQLException {
        this.currentSQL = null;
        ((AbstractShardingConnection)this.getCreator()).checkAndReleaseConnectionLock();
        throw sQLException;
    }

    public void close() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            this.closeDatabaseStatementWithSetterReplay();
            this.purgeCallEntries();
            abstractShardingConnection.removeStatement(this);
            this.closed = true;
        }
    }

    public void clearDefines() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.clearDefines();
            } else {
                Class[] classArray = new Class[]{};
                Object[] objectArray = new Object[]{};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("clearDefines", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void defineColumnType(int n2, int n3) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.defineColumnType(n2, n3);
            } else {
                Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE};
                Object[] objectArray = new Object[]{n2, n3};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("defineColumnType", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void defineColumnType(int n2, int n3, int n4) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.defineColumnType(n2, n3, n4);
            } else {
                Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE};
                Object[] objectArray = new Object[]{n2, n3, n4};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("defineColumnType", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void defineColumnType(int n2, int n3, String string) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.defineColumnType(n2, n3, string);
            } else {
                Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, String.class};
                Object[] objectArray = new Object[]{n2, n3, string};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("defineColumnType", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void defineColumnType(int n2, int n3, int n4, short s2) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.defineColumnType(n2, n3, n4, s2);
            } else {
                Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE, Short.TYPE};
                Object[] objectArray = new Object[]{n2, n3, n4, s2};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("defineColumnType", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void defineColumnTypeBytes(int n2, int n3, int n4) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.defineColumnTypeBytes(n2, n3, n4);
            } else {
                Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE};
                Object[] objectArray = new Object[]{n2, n3, n4};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("defineColumnTypeBytes", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void defineColumnTypeChars(int n2, int n3, int n4) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.defineColumnTypeChars(n2, n3, n4);
            } else {
                Class[] classArray = new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE};
                Object[] objectArray = new Object[]{n2, n3, n4};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("defineColumnTypeChars", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void setDatabaseChangeRegistration(DatabaseChangeRegistration databaseChangeRegistration) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.setDatabaseChangeRegistration(databaseChangeRegistration);
            } else {
                Class[] classArray = new Class[]{DatabaseChangeRegistration.class};
                Object[] objectArray = new Object[]{databaseChangeRegistration};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("setDatabaseChangeRegistration", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void setSnapshotSCN(long l2) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.setSnapshotSCN(l2);
            } else {
                Class[] classArray = new Class[]{Long.TYPE};
                Object[] objectArray = new Object[]{l2};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("setSnapshotSCN", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void registerBindChecksumListener(OracleStatement.BindChecksumListener bindChecksumListener) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.registerBindChecksumListener(bindChecksumListener);
            } else {
                Class[] classArray = new Class[]{OracleStatement.BindChecksumListener.class};
                Object[] objectArray = new Object[]{bindChecksumListener};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("registerBindChecksumListener", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void setEscapeProcessing(boolean bl) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.setEscapeProcessing(bl);
            } else {
                Class[] classArray = new Class[]{Boolean.TYPE};
                Object[] objectArray = new Object[]{bl};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("setEscapeProcessing", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    public void clearWarnings() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.clearWarnings();
            } else {
                Class[] classArray = new Class[]{};
                Object[] objectArray = new Object[]{};
                CallHistoryEntry callHistoryEntry = new CallHistoryEntry("clearWarnings", classArray, objectArray);
                this.appendCallEntry(callHistoryEntry);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setRowPrefetch(int n2) throws SQLException {
        block19: {
            OracleConnection oracleConnection;
            AbstractShardingConnection abstractShardingConnection;
            block20: {
                abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
                oracleConnection = null;
                Statement statement = null;
                boolean bl = true;
                try {
                    try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                        String string = "setRowPrefetch";
                        Class[] classArray = new Class[]{Integer.TYPE};
                        Object[] objectArray = new Object[]{n2};
                        statement = (OracleStatement)this.getDelegate();
                        if (statement != null) {
                            bl = false;
                            statement.setRowPrefetch(n2);
                        } else {
                            oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnection();
                            statement = this.createDatabaseStatement(oracleConnection);
                            Integer n3 = statement.getRowPrefetch();
                            SetterCallHistoryEntry setterCallHistoryEntry = new SetterCallHistoryEntry(n3, string, classArray, objectArray);
                            this.setterStmtMap.put("RowPrefetch", setterCallHistoryEntry);
                        }
                    }
                    if (!bl) break block19;
                    if (statement == null) break block20;
                }
                catch (Throwable throwable) {
                    if (bl) {
                        if (statement != null) {
                            statement.close();
                        }
                        abstractShardingConnection.closeDatabaseConnection(oracleConnection);
                    }
                    throw throwable;
                }
                statement.close();
            }
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    /*
     * Exception decompiling
     */
    public int getRowPrefetch() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setLobPrefetchSize(int n2) throws SQLException {
        block19: {
            OracleConnection oracleConnection;
            AbstractShardingConnection abstractShardingConnection;
            block20: {
                abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
                oracleConnection = null;
                Statement statement = null;
                boolean bl = true;
                try {
                    try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                        String string = "setLobPrefetchSize";
                        Class[] classArray = new Class[]{Integer.TYPE};
                        Object[] objectArray = new Object[]{n2};
                        statement = (OracleStatement)this.getDelegate();
                        if (statement != null) {
                            bl = false;
                            statement.setLobPrefetchSize(n2);
                        } else {
                            oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnection();
                            statement = this.createDatabaseStatement(oracleConnection);
                            Integer n3 = statement.getLobPrefetchSize();
                            SetterCallHistoryEntry setterCallHistoryEntry = new SetterCallHistoryEntry(n3, string, classArray, objectArray);
                            this.setterStmtMap.put("LobPrefetchSize", setterCallHistoryEntry);
                        }
                    }
                    if (!bl) break block19;
                    if (statement == null) break block20;
                }
                catch (Throwable throwable) {
                    if (bl) {
                        if (statement != null) {
                            statement.close();
                        }
                        abstractShardingConnection.closeDatabaseConnection(oracleConnection);
                    }
                    throw throwable;
                }
                statement.close();
            }
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    /*
     * Exception decompiling
     */
    public int getLobPrefetchSize() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setFetchDirection(int n2) throws SQLException {
        block19: {
            OracleConnection oracleConnection;
            AbstractShardingConnection abstractShardingConnection;
            block20: {
                abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
                oracleConnection = null;
                Statement statement = null;
                boolean bl = true;
                try {
                    try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                        String string = "setFetchDirection";
                        Class[] classArray = new Class[]{Integer.TYPE};
                        Object[] objectArray = new Object[]{n2};
                        statement = (OracleStatement)this.getDelegate();
                        if (statement != null) {
                            bl = false;
                            statement.setFetchDirection(n2);
                        } else {
                            oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnection();
                            statement = this.createDatabaseStatement(oracleConnection);
                            Integer n3 = statement.getFetchDirection();
                            SetterCallHistoryEntry setterCallHistoryEntry = new SetterCallHistoryEntry(n3, string, classArray, objectArray);
                            this.setterStmtMap.put("FetchDirection", setterCallHistoryEntry);
                        }
                    }
                    if (!bl) break block19;
                    if (statement == null) break block20;
                }
                catch (Throwable throwable) {
                    if (bl) {
                        if (statement != null) {
                            statement.close();
                        }
                        abstractShardingConnection.closeDatabaseConnection(oracleConnection);
                    }
                    throw throwable;
                }
                statement.close();
            }
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    /*
     * Exception decompiling
     */
    public int getFetchDirection() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setFetchSize(int n2) throws SQLException {
        block19: {
            OracleConnection oracleConnection;
            AbstractShardingConnection abstractShardingConnection;
            block20: {
                abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
                oracleConnection = null;
                Statement statement = null;
                boolean bl = true;
                try {
                    try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                        String string = "setFetchSize";
                        Class[] classArray = new Class[]{Integer.TYPE};
                        Object[] objectArray = new Object[]{n2};
                        statement = (OracleStatement)this.getDelegate();
                        if (statement != null) {
                            bl = false;
                            statement.setFetchSize(n2);
                        } else {
                            oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnection();
                            statement = this.createDatabaseStatement(oracleConnection);
                            Integer n3 = statement.getFetchSize();
                            SetterCallHistoryEntry setterCallHistoryEntry = new SetterCallHistoryEntry(n3, string, classArray, objectArray);
                            this.setterStmtMap.put("FetchSize", setterCallHistoryEntry);
                        }
                    }
                    if (!bl) break block19;
                    if (statement == null) break block20;
                }
                catch (Throwable throwable) {
                    if (bl) {
                        if (statement != null) {
                            statement.close();
                        }
                        abstractShardingConnection.closeDatabaseConnection(oracleConnection);
                    }
                    throw throwable;
                }
                statement.close();
            }
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    /*
     * Exception decompiling
     */
    public int getFetchSize() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setQueryTimeout(int n2) throws SQLException {
        block19: {
            OracleConnection oracleConnection;
            AbstractShardingConnection abstractShardingConnection;
            block20: {
                abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
                oracleConnection = null;
                Statement statement = null;
                boolean bl = true;
                try {
                    try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                        String string = "setQueryTimeout";
                        Class[] classArray = new Class[]{Integer.TYPE};
                        Object[] objectArray = new Object[]{n2};
                        statement = (OracleStatement)this.getDelegate();
                        if (statement != null) {
                            bl = false;
                            statement.setQueryTimeout(n2);
                        } else {
                            oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnection();
                            statement = this.createDatabaseStatement(oracleConnection);
                            Integer n3 = statement.getQueryTimeout();
                            SetterCallHistoryEntry setterCallHistoryEntry = new SetterCallHistoryEntry(n3, string, classArray, objectArray);
                            this.setterStmtMap.put("QueryTimeout", setterCallHistoryEntry);
                        }
                    }
                    if (!bl) break block19;
                    if (statement == null) break block20;
                }
                catch (Throwable throwable) {
                    if (bl) {
                        if (statement != null) {
                            statement.close();
                        }
                        abstractShardingConnection.closeDatabaseConnection(oracleConnection);
                    }
                    throw throwable;
                }
                statement.close();
            }
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    /*
     * Exception decompiling
     */
    public int getQueryTimeout() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setMaxRows(int n2) throws SQLException {
        block19: {
            OracleConnection oracleConnection;
            AbstractShardingConnection abstractShardingConnection;
            block20: {
                abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
                oracleConnection = null;
                Statement statement = null;
                boolean bl = true;
                try {
                    try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                        String string = "setMaxRows";
                        Class[] classArray = new Class[]{Integer.TYPE};
                        Object[] objectArray = new Object[]{n2};
                        statement = (OracleStatement)this.getDelegate();
                        if (statement != null) {
                            bl = false;
                            statement.setMaxRows(n2);
                        } else {
                            oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnection();
                            statement = this.createDatabaseStatement(oracleConnection);
                            Integer n3 = statement.getMaxRows();
                            SetterCallHistoryEntry setterCallHistoryEntry = new SetterCallHistoryEntry(n3, string, classArray, objectArray);
                            this.setterStmtMap.put("MaxRows", setterCallHistoryEntry);
                        }
                    }
                    if (!bl) break block19;
                    if (statement == null) break block20;
                }
                catch (Throwable throwable) {
                    if (bl) {
                        if (statement != null) {
                            statement.close();
                        }
                        abstractShardingConnection.closeDatabaseConnection(oracleConnection);
                    }
                    throw throwable;
                }
                statement.close();
            }
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    /*
     * Exception decompiling
     */
    public int getMaxRows() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setMaxFieldSize(int n2) throws SQLException {
        block19: {
            OracleConnection oracleConnection;
            AbstractShardingConnection abstractShardingConnection;
            block20: {
                abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
                oracleConnection = null;
                Statement statement = null;
                boolean bl = true;
                try {
                    try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                        String string = "setMaxFieldSize";
                        Class[] classArray = new Class[]{Integer.TYPE};
                        Object[] objectArray = new Object[]{n2};
                        statement = (OracleStatement)this.getDelegate();
                        if (statement != null) {
                            bl = false;
                            statement.setMaxFieldSize(n2);
                        } else {
                            oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnection();
                            statement = this.createDatabaseStatement(oracleConnection);
                            Integer n3 = statement.getMaxFieldSize();
                            SetterCallHistoryEntry setterCallHistoryEntry = new SetterCallHistoryEntry(n3, string, classArray, objectArray);
                            this.setterStmtMap.put("MaxFieldSize", setterCallHistoryEntry);
                        }
                    }
                    if (!bl) break block19;
                    if (statement == null) break block20;
                }
                catch (Throwable throwable) {
                    if (bl) {
                        if (statement != null) {
                            statement.close();
                        }
                        abstractShardingConnection.closeDatabaseConnection(oracleConnection);
                    }
                    throw throwable;
                }
                statement.close();
            }
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    /*
     * Exception decompiling
     */
    public int getMaxFieldSize() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setFixedString(boolean bl) throws SQLException {
        block19: {
            OracleConnection oracleConnection;
            AbstractShardingConnection abstractShardingConnection;
            block20: {
                abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
                oracleConnection = null;
                Statement statement = null;
                boolean bl2 = true;
                try {
                    try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                        String string = "setFixedString";
                        Class[] classArray = new Class[]{Boolean.TYPE};
                        Object[] objectArray = new Object[]{bl};
                        statement = (OracleStatement)this.getDelegate();
                        if (statement != null) {
                            bl2 = false;
                            statement.setFixedString(bl);
                        } else {
                            oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnection();
                            statement = this.createDatabaseStatement(oracleConnection);
                            Boolean bl3 = statement.getFixedString();
                            SetterCallHistoryEntry setterCallHistoryEntry = new SetterCallHistoryEntry(bl3, string, classArray, objectArray);
                            this.setterStmtMap.put("FixedString", setterCallHistoryEntry);
                        }
                    }
                    if (!bl2) break block19;
                    if (statement == null) break block20;
                }
                catch (Throwable throwable) {
                    if (bl2) {
                        if (statement != null) {
                            statement.close();
                        }
                        abstractShardingConnection.closeDatabaseConnection(oracleConnection);
                    }
                    throw throwable;
                }
                statement.close();
            }
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    /*
     * Exception decompiling
     */
    public boolean getFixedString() throws SQLException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 3 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public void closeWithKey(String string) throws SQLException {
    }

    public int getcacheState() {
        return NON_CACHED;
    }

    public boolean isPoolable() throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
        return false;
    }

    public void setPoolable(boolean bl) throws SQLException {
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
    }

    @Pre
    @Methods(signatures={@Signature(name="creationState", args={}), @Signature(name="setACProxy", args={Object.class}), @Signature(name="getACProxy", args={}), @Signature(name="setShardingKeyRpnTokens", args={byte[].class}), @Signature(name="getShardingKeyRpnTokens", args={}), @Signature(name="setCursorName", args={String.class}), @Signature(name="getserverCursor", args={})})
    protected void preUnsupportedStatementMethods(Method method, Object object, Object ... objectArray) {
        throw new RuntimeException((SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace());
    }

    public Connection getConnection() throws SQLException {
        return (Connection)this.getCreator();
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        ResultSet resultSet;
        block15: {
            resultSet = null;
            AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
            try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
                if (oracleStatement != null) {
                    resultSet = oracleStatement.getGeneratedKeys();
                    if (resultSet != null) {
                        resultSet = this.createResultSetProxy(resultSet);
                        if (this.currentResultSet == null) {
                            this.currentResultSet = (AbstractShardingResultSet)((Object)resultSet);
                        }
                    }
                    break block15;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
        }
        return resultSet;
    }

    public ResultSet getResultSet() throws SQLException {
        ResultSet resultSet;
        block14: {
            resultSet = null;
            AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
            try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
                if (oracleStatement != null) {
                    resultSet = oracleStatement.getResultSet();
                    if (resultSet != null) {
                        resultSet = this.createResultSetProxy(resultSet);
                        this.currentResultSet = (AbstractShardingResultSet)((Object)resultSet);
                    }
                    break block14;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1707).fillInStackTrace();
            }
        }
        return resultSet;
    }

    public boolean getMoreResults() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                boolean bl = oracleStatement.getMoreResults();
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

    public boolean getMoreResults(int n2) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                boolean bl = oracleStatement.getMoreResults(n2);
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

    public boolean isNCHAR(int n2) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                boolean bl = oracleStatement.isNCHAR(n2);
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

    public String[] getRegisteredTableNames() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                String[] stringArray = oracleStatement.getRegisteredTableNames();
                return stringArray;
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

    public long getRegisteredQueryId() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                long l2 = oracleStatement.getRegisteredQueryId();
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

    public int getUpdateCount() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                int n2 = oracleStatement.getUpdateCount();
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

    public long getQueryId() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                long l2 = oracleStatement.getQueryId();
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

    public byte[] getCompileKey() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                byte[] byArray = oracleStatement.getCompileKey();
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

    public long getChecksum() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                long l2 = oracleStatement.getChecksum();
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

    public OracleStatement.SqlKind getSqlKind() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                OracleStatement.SqlKind sqlKind = oracleStatement.getSqlKind();
                return sqlKind;
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

    public SQLWarning getWarnings() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Throwable throwable = null;
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                SQLWarning sQLWarning = oracleStatement.getWarnings();
                return sQLWarning;
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

    public boolean isClosed() throws SQLException {
        return this.closed;
    }

    public int getResultSetConcurrency() throws SQLException {
        this.ensureOpen();
        return this.userResultSetConcur;
    }

    public int getResultSetType() throws SQLException {
        this.ensureOpen();
        return this.userResultSetType;
    }

    public int getResultSetHoldability() throws SQLException {
        this.ensureOpen();
        return 1;
    }

    public int getstatementType() {
        return this.statementType;
    }

    public void cancel() throws SQLException {
        OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
        if (oracleStatement != null) {
            oracleStatement.cancel();
        }
    }

    public void closeOnCompletion() throws SQLException {
        this.ensureOpen();
        this.isCloseOnCompletion = true;
    }

    public boolean isCloseOnCompletion() throws SQLException {
        this.ensureOpen();
        return this.isCloseOnCompletion;
    }

    public int sendBatch() throws SQLException {
        return 0;
    }

    public void addBatch(String string) throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            this.ensureOpen();
            this.addBatchItem(string);
        }
    }

    public void clearBatch() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
            OracleStatement oracleStatement = (OracleStatement)this.getDelegate();
            if (oracleStatement != null) {
                oracleStatement.clearBatch();
            } else {
                this.clearBatchCritical();
            }
        }
    }

    /*
     * Loose catch block
     */
    public int[] executeBatch() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        OracleConnection oracleConnection = null;
        OracleStatement oracleStatement = null;
        try {
            try (Monitor.CloseableLock closeableLock = abstractShardingConnection.acquireConnectionCloseableLock();){
                Object object;
                oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnection();
                oracleStatement = this.createDatabaseStatement(oracleConnection);
                this.setDelegate(oracleStatement);
                if (this.m_batchItems != null) {
                    object = this.m_batchItems.iterator();
                    while (object.hasNext()) {
                        String string = (String)object.next();
                        oracleStatement.addBatch(string);
                    }
                }
                object = oracleStatement.executeBatch();
                return object;
            }
            {
                catch (Throwable throwable) {
                    throw throwable;
                }
            }
        }
        finally {
            this.clearBatchItems();
        }
    }

    void closeDatabaseStatement() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Statement statement = this.getDelegate();
        if (statement != null && !statement.isClosed()) {
            OracleConnection oracleConnection = (OracleConnection)statement.getConnection();
            statement.close();
            abstractShardingConnection.closeDatabaseConnectionWithSetterReplay(oracleConnection, this.lastStatementExecutionOnDirectShard);
            this.setDelegate(null);
        }
    }

    void closeDatabaseStatementWithSetterReplay() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        Statement statement = this.getDelegate();
        if (statement != null && !statement.isClosed()) {
            OracleConnection oracleConnection = (OracleConnection)statement.getConnection();
            this.delegatesStmtSetterCalls((OracleStatement)statement, false);
            statement.close();
            abstractShardingConnection.closeDatabaseConnectionWithSetterReplay(oracleConnection, this.lastStatementExecutionOnDirectShard);
            this.setDelegate(null);
        }
    }

    protected OracleConnection checkForKeyTokensAndGetUCPConnection(String string) throws SQLException {
        OracleConnection oracleConnection = null;
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        if (abstractShardingConnection.getStickyDatabaseConnection() != null) {
            oracleConnection = abstractShardingConnection.getStickyDatabaseConnection();
        } else if (!abstractShardingConnection.allowSingleShardTransaction() && !abstractShardingConnection.getAutoCommit() && abstractShardingConnection.inTransaction()) {
            oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnectionWithSetterReplay();
            this.lastStatementExecutionOnDirectShard = false;
        } else {
            try {
                OracleShardingKey oracleShardingKey = null;
                OracleShardingKey oracleShardingKey2 = null;
                OracleStatement.SqlKind sqlKind = OracleStatement.SqlKind.UNINITIALIZED;
                ShardingKeyInfo.KeyTokenInfo keyTokenInfo = ShardingKeyInfo.getKeyRpnTokens(string, abstractShardingConnection.gsmServiceName(), abstractShardingConnection.userName(), abstractShardingConnection.schemaName());
                if (keyTokenInfo != null) {
                    byte[] byArray = keyTokenInfo.getKeyTokens();
                    sqlKind = keyTokenInfo.getSqlKind();
                    if (byArray != null) {
                        OracleShardingKey[] oracleShardingKeyArray = new ShardingKeyInfo().getShardingKeys((OracleStatement)((Object)this), byArray, this.ods, ((AbstractShardingConnection)this.getCreator()).getDbCharSet());
                        oracleShardingKey = oracleShardingKeyArray[0];
                        oracleShardingKey2 = oracleShardingKeyArray[1];
                    }
                }
                if (oracleShardingKey != null && (abstractShardingConnection.getAutoCommit() || sqlKind == OracleStatement.SqlKind.SELECT || abstractShardingConnection.allowSingleShardTransaction())) {
                    oracleConnection = (OracleConnection)abstractShardingConnection.getShardConnectionWithSetterReplay(oracleShardingKey, oracleShardingKey2);
                    this.lastStatementExecutionOnDirectShard = true;
                } else {
                    oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnectionWithSetterReplay();
                    this.lastStatementExecutionOnDirectShard = false;
                }
            }
            catch (IOException iOException) {
                throw new SQLException(iOException);
            }
        }
        return oracleConnection;
    }

    private OracleStatement checkForKeyTokensAndGetUCPStatement(String string) throws SQLException {
        OracleStatement oracleStatement = null;
        OracleConnection oracleConnection = this.checkForKeyTokensAndGetUCPConnection(string);
        oracleStatement = this.createDatabaseStatement(oracleConnection);
        this.setDelegate(oracleStatement);
        return oracleStatement;
    }

    protected void setShardingKeyRpnTokens(OracleStatement oracleStatement, String string) throws SQLException {
        byte[] byArray = oracleStatement.getShardingKeyRpnTokens();
        if (byArray != null) {
            AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
            ShardingKeyInfo.putKeyRpnTokens(string, abstractShardingConnection.gsmServiceName(), abstractShardingConnection.userName(), abstractShardingConnection.schemaName(), byArray, oracleStatement.getSqlKind());
            oracleStatement.setShardingKeyRpnTokens(null);
        }
    }

    protected void makeDatabaseConnectionSticky(AbstractShardingConnection abstractShardingConnection, OracleConnection oracleConnection) throws SQLException {
        abstractShardingConnection.makeDatabaseConnectionSticky(oracleConnection);
    }

    protected ResultSet createResultSetProxy(ResultSet resultSet) throws SQLException {
        return ShardingDriverExtension.PROXY_FACTORY.proxyFor(resultSet, this);
    }

    protected OracleStatement createDatabaseStatement(OracleConnection oracleConnection) throws SQLException {
        return (OracleStatement)oracleConnection.createStatement(this.userResultSetType, this.userResultSetConcur);
    }

    protected OracleStatement createDatabaseStatementWithReplay(OracleConnection oracleConnection) throws SQLException {
        OracleStatement oracleStatement = (OracleStatement)oracleConnection.createStatement(this.userResultSetType, this.userResultSetConcur);
        if (oracleStatement != null) {
            this.delegatesStmtSetterCalls(oracleStatement, true);
        }
        return oracleStatement;
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return (OracleConnection)this.getCreator();
    }

    final void ensureOpen() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        if (abstractShardingConnection.lifecycle != 1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 8).fillInStackTrace();
        }
        if (this.closed) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 9).fillInStackTrace();
        }
    }

    void addBatchItem(String string) {
        if (this.m_batchItems == null) {
            this.m_batchItems = new Vector();
        }
        this.m_batchItems.addElement(string);
    }

    void clearBatchCritical() throws SQLException {
        this.ensureOpen();
        this.clearBatchItems();
    }

    void clearBatchItems() {
        if (this.m_batchItems != null) {
            this.m_batchItems.removeAllElements();
        }
    }

    protected void appendCallEntry(CallHistoryEntry callHistoryEntry) {
        callHistoryEntry.prevEntry = this.tail;
        callHistoryEntry.nextEntry = null;
        if (this.tail != null) {
            this.tail.nextEntry = callHistoryEntry;
        }
        this.tail = callHistoryEntry;
        if (this.head == null) {
            this.head = callHistoryEntry;
        }
    }

    protected void removeCallEntry(CallHistoryEntry callHistoryEntry) {
        if (callHistoryEntry.nextEntry != null) {
            callHistoryEntry.nextEntry.prevEntry = callHistoryEntry.prevEntry;
        }
        if (callHistoryEntry.prevEntry != null) {
            callHistoryEntry.prevEntry.nextEntry = callHistoryEntry.nextEntry;
        }
        if (this.head == callHistoryEntry) {
            this.head = callHistoryEntry.nextEntry;
        }
        if (this.tail == callHistoryEntry) {
            this.tail = callHistoryEntry.prevEntry;
        }
    }

    protected void purgeCallEntries() {
        this.head = null;
        this.tail = null;
        this.setterStmtMap = null;
    }

    void delegatesBeforeExecuteCalls(OracleStatement oracleStatement) throws SQLException {
        try {
            CallHistoryEntry callHistoryEntry = this.head;
            while (callHistoryEntry != this.tail) {
                Method method = oracleStatement.getClass().getMethod(callHistoryEntry.methodName, callHistoryEntry.argsType);
                method.invoke(oracleStatement, callHistoryEntry.args);
                callHistoryEntry = callHistoryEntry.nextEntry;
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException exception) {
            throw new SQLException(exception);
        }
    }

    void delegatesStmtSetterCalls(OracleStatement oracleStatement, boolean bl) throws SQLException {
        try {
            for (SetterCallHistoryEntry setterCallHistoryEntry : this.setterStmtMap.values()) {
                CallHistoryEntry callHistoryEntry = setterCallHistoryEntry.callHistoryEnrty;
                Method method = oracleStatement.getClass().getMethod(callHistoryEntry.methodName, callHistoryEntry.argsType);
                if (bl) {
                    method.invoke(oracleStatement, callHistoryEntry.args);
                    continue;
                }
                method.invoke(oracleStatement, setterCallHistoryEntry.originalValue);
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException exception) {
            throw new SQLException(exception);
        }
    }

    boolean closeByDependent(OracleConnection oracleConnection) throws SQLException {
        if (this.isCloseOnCompletion && (this.currentResultSet == null || this.currentResultSet.isComplete())) {
            Statement statement = this.getDelegate();
            if (statement != null && !statement.isClosed()) {
                this.close();
            } else {
                AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
                abstractShardingConnection.closeDatabaseConnectionWithSetterReplay(oracleConnection, this.lastStatementExecutionOnDirectShard);
                this.setDelegate(null);
                this.purgeCallEntries();
                abstractShardingConnection.removeStatement(this);
                this.closed = true;
            }
            return true;
        }
        return false;
    }

    static class SetterCallHistoryEntry {
        CallHistoryEntry callHistoryEnrty;
        Object originalValue;

        SetterCallHistoryEntry(Object object, String string, Class<?>[] classArray, Object[] objectArray) {
            this.originalValue = object;
            this.callHistoryEnrty = new CallHistoryEntry(string, classArray, objectArray);
        }
    }

    static class CallHistoryEntry {
        String methodName;
        Class<?>[] argsType;
        Object[] args;
        CallHistoryEntry nextEntry = null;
        CallHistoryEntry prevEntry = null;

        CallHistoryEntry(String string, Class<?>[] classArray, Object[] objectArray) {
            this.methodName = string;
            this.argsType = classArray;
            this.args = objectArray;
        }
    }
}

