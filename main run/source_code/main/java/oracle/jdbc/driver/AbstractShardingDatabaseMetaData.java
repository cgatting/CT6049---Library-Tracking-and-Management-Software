/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import oracle.jdbc.OracleTypeMetaData;
import oracle.jdbc.driver.AbstractShardingConnection;
import oracle.jdbc.driver.AbstractShardingStatement;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.ShardingDriverExtension;
import oracle.jdbc.internal.AdditionalDatabaseMetaData;
import oracle.jdbc.internal.OracleConnection;
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

@ProxyFor(value={AdditionalDatabaseMetaData.class})
@ProxyResult(value=ProxyResultPolicy.MANUAL)
public abstract class AbstractShardingDatabaseMetaData {
    @GetCreator
    protected abstract Object getCreator();

    @GetDelegate
    protected abstract DatabaseMetaData getDelegate();

    @SetDelegate
    protected abstract void setDelegate(DatabaseMetaData var1);

    @Pre
    @Methods(signatures={@Signature(name="getOracleTypeMetaData", args={String.class}), @Signature(name="getLobMaxLength", args={}), @Signature(name="getAuditBanner", args={}), @Signature(name="getAccessBanner", args={}), @Signature(name="allProceduresAreCallable", args={}), @Signature(name="allTablesAreSelectable", args={}), @Signature(name="autoCommitFailureClosesAllResultSets", args={}), @Signature(name="dataDefinitionCausesTransactionCommit", args={}), @Signature(name="dataDefinitionIgnoredInTransactions", args={}), @Signature(name="deletesAreDetected", args={int.class}), @Signature(name="doesMaxRowSizeIncludeBlobs", args={}), @Signature(name="generatedKeyAlwaysReturned", args={}), @Signature(name="getAttributes", args={String.class, String.class, String.class, String.class}), @Signature(name="getBestRowIdentifier", args={String.class, String.class, String.class}), @Signature(name="getCatalogSeparator", args={}), @Signature(name="getCatalogTerm", args={}), @Signature(name="getCatalogs", args={}), @Signature(name="getClientInfoProperties", args={}), @Signature(name="getColumnPrivileges", args={String.class, String.class, String.class, String.class}), @Signature(name="getColumns", args={String.class, String.class, String.class, String.class}), @Signature(name="getCrossReference", args={String.class, String.class, String.class, String.class, String.class, String.class}), @Signature(name="getDatabaseMajorVersion", args={}), @Signature(name="getDatabaseMinorVersion", args={}), @Signature(name="getDatabaseProductName", args={}), @Signature(name="getDatabaseProductVersion", args={}), @Signature(name="getDefaultTransactionIsolation", args={}), @Signature(name="getDriverMajorVersion", args={}), @Signature(name="getDriverMinorVersion", args={}), @Signature(name="getDriverName", args={}), @Signature(name="getDriverVersion", args={}), @Signature(name="getExportedKeys", args={String.class, String.class, String.class}), @Signature(name="getExtraNameCharacters", args={}), @Signature(name="getFunctionColumns", args={String.class, String.class, String.class, String.class}), @Signature(name="getFunctions", args={String.class, String.class, String.class}), @Signature(name="getIdentifierQuoteString", args={}), @Signature(name="getImportedKeys", args={String.class, String.class, String.class}), @Signature(name="getIndexInfo", args={String.class, String.class, String.class, String.class, String.class}), @Signature(name="getJDBCMajorVersion", args={}), @Signature(name="getJDBCMinorVersion", args={}), @Signature(name="getMaxBinaryLiteralLength", args={}), @Signature(name="getMaxCatalogNameLength", args={}), @Signature(name="getMaxCharLiteralLength", args={}), @Signature(name="getMaxColumnNameLength", args={}), @Signature(name="getMaxColumnsInGroupBy", args={}), @Signature(name="getMaxColumnsInIndex", args={}), @Signature(name="getMaxColumnsInOrderBy", args={}), @Signature(name="getMaxColumnsInSelect", args={}), @Signature(name="getMaxColumnsInTable", args={}), @Signature(name="getMaxConnections", args={}), @Signature(name="getMaxCursorNameLength", args={}), @Signature(name="getMaxIndexLength", args={}), @Signature(name="getMaxProcedureNameLength", args={}), @Signature(name="getMaxRowSize", args={}), @Signature(name="getMaxSchemaNameLength", args={}), @Signature(name="getMaxStatementLength", args={}), @Signature(name="getMaxStatements", args={}), @Signature(name="getMaxTableNameLength", args={}), @Signature(name="getMaxTablesInSelect", args={}), @Signature(name="getMaxUserNameLength", args={}), @Signature(name="getNumericFunctions", args={}), @Signature(name="getPrimaryKeys", args={String.class, String.class, String.class}), @Signature(name="getProcedureColumns", args={String.class, String.class, String.class, String.class}), @Signature(name="getProcedureTerm", args={}), @Signature(name="getProcedures", args={String.class, String.class, String.class}), @Signature(name="getPseudoColumns", args={String.class, String.class, String.class, String.class}), @Signature(name="getResultSetHoldability", args={}), @Signature(name="getRowIdLifetime", args={}), @Signature(name="getSQLKeywords", args={}), @Signature(name="getSQLStateType", args={}), @Signature(name="getSchemaTerm", args={}), @Signature(name="getSchemas", args={}), @Signature(name="getSchemas", args={String.class, String.class}), @Signature(name="getSearchStringEscape", args={}), @Signature(name="getStringFunctions", args={}), @Signature(name="getSuperTables", args={String.class, String.class, String.class}), @Signature(name="getSuperTypes", args={String.class, String.class, String.class}), @Signature(name="getSystemFunctions", args={}), @Signature(name="getTablePrivileges", args={String.class, String.class, String.class}), @Signature(name="getTableTypes", args={}), @Signature(name="getTables", args={String.class, String.class, String.class, String[].class}), @Signature(name="getTimeDateFunctions", args={}), @Signature(name="getTypeInfo", args={}), @Signature(name="getUDTs", args={String.class, String.class, String.class, int[].class}), @Signature(name="getURL", args={}), @Signature(name="getUserName", args={}), @Signature(name="getVersionColumns", args={String.class, String.class, String.class}), @Signature(name="insertsAreDetected", args={int.class}), @Signature(name="isCatalogAtStart", args={}), @Signature(name="isReadOnly", args={}), @Signature(name="locatorsUpdateCopy", args={}), @Signature(name="nullPlusNonNullIsNull", args={}), @Signature(name="nullsAreSortedAtEnd", args={}), @Signature(name="nullsAreSortedAtStart", args={}), @Signature(name="nullsAreSortedHigh", args={}), @Signature(name="nullsAreSortedLow", args={}), @Signature(name="othersDeletesAreVisible", args={int.class}), @Signature(name="othersInsertsAreVisible", args={int.class}), @Signature(name="othersUpdatesAreVisible", args={int.class}), @Signature(name="ownDeletesAreVisible", args={int.class}), @Signature(name="ownInsertsAreVisible", args={int.class}), @Signature(name="ownUpdatesAreVisible", args={int.class}), @Signature(name="storesLowerCaseIdentifiers", args={}), @Signature(name="storesLowerCaseQuotedIdentifiers", args={}), @Signature(name="storesMixedCaseIdentifiers", args={}), @Signature(name="storesMixedCaseQuotedIdentifiers", args={}), @Signature(name="storesUpperCaseIdentifiers", args={}), @Signature(name="storesUpperCaseQuotedIdentifiers", args={}), @Signature(name="supportsANSI92EntryLevelSQL", args={}), @Signature(name="supportsANSI92FullSQL", args={}), @Signature(name="supportsANSI92IntermediateSQL", args={}), @Signature(name="supportsAlterTableWithAddColumn", args={}), @Signature(name="supportsAlterTableWithDropColumn", args={}), @Signature(name="supportsBatchUpdates", args={}), @Signature(name="supportsCatalogsInDataManipulation", args={}), @Signature(name="supportsCatalogsInIndexDefinitions", args={}), @Signature(name="supportsCatalogsInPrivilegeDefinitions", args={}), @Signature(name="supportsCatalogsInProcedureCalls", args={}), @Signature(name="supportsCatalogsInTableDefinitions", args={}), @Signature(name="supportsColumnAliasing", args={}), @Signature(name="supportsConvert", args={}), @Signature(name="supportsConvert", args={int.class, int.class}), @Signature(name="supportsCoreSQLGrammar", args={}), @Signature(name="supportsCorrelatedSubqueries", args={}), @Signature(name="supportsDataDefinitionAndDataManipulationTransactions", args={}), @Signature(name="supportsDataManipulationTransactionsOnly", args={}), @Signature(name="supportsDifferentTableCorrelationNames", args={}), @Signature(name="supportsExpressionsInOrderBy", args={}), @Signature(name="supportsExtendedSQLGrammar", args={}), @Signature(name="supportsFullOuterJoins", args={}), @Signature(name="supportsGetGeneratedKeys", args={}), @Signature(name="supportsGroupBy", args={}), @Signature(name="supportsGroupByBeyondSelect", args={}), @Signature(name="supportsGroupByUnrelated", args={}), @Signature(name="supportsIntegrityEnhancementFacility", args={}), @Signature(name="supportsLikeEscapeClause", args={}), @Signature(name="supportsLimitedOuterJoins", args={}), @Signature(name="supportsMinimumSQLGrammar", args={}), @Signature(name="supportsMixedCaseIdentifiers", args={}), @Signature(name="supportsMixedCaseQuotedIdentifiers", args={}), @Signature(name="supportsMultipleOpenResults", args={}), @Signature(name="supportsMultipleResultSets", args={}), @Signature(name="supportsMultipleTransactions", args={}), @Signature(name="supportsNamedParameters", args={}), @Signature(name="supportsNonNullableColumns", args={}), @Signature(name="supportsOpenCursorsAcrossCommit", args={}), @Signature(name="supportsOpenCursorsAcrossRollback", args={}), @Signature(name="supportsOpenStatementsAcrossCommit", args={}), @Signature(name="supportsOpenStatementsAcrossRollback", args={}), @Signature(name="supportsOrderByUnrelated", args={}), @Signature(name="supportsOuterJoins", args={}), @Signature(name="supportsPositionedDelete", args={}), @Signature(name="supportsPositionedUpdate", args={}), @Signature(name="supportsResultSetConcurrency", args={int.class, int.class}), @Signature(name="supportsResultSetHoldability", args={int.class}), @Signature(name="supportsResultSetType", args={int.class}), @Signature(name="supportsSavepoints", args={}), @Signature(name="supportsSchemasInDataManipulation", args={}), @Signature(name="supportsSchemasInIndexDefinitions", args={}), @Signature(name="supportsSchemasInPrivilegeDefinitions", args={}), @Signature(name="supportsSchemasInProcedureCalls", args={}), @Signature(name="supportsSchemasInTableDefinitions", args={}), @Signature(name="supportsSelectForUpdate", args={}), @Signature(name="supportsStatementPooling", args={}), @Signature(name="supportsStoredFunctionsUsingCallSyntax", args={}), @Signature(name="supportsStoredProcedures", args={}), @Signature(name="supportsSubqueriesInComparisons", args={}), @Signature(name="supportsSubqueriesInExists", args={}), @Signature(name="supportsSubqueriesInIns", args={}), @Signature(name="supportsSubqueriesInQuantifieds", args={}), @Signature(name="supportsTableCorrelationNames", args={}), @Signature(name="supportsTransactionIsolationLevel", args={int.class}), @Signature(name="supportsTransactions", args={}), @Signature(name="supportsUnion", args={}), @Signature(name="supportsUnionAll", args={}), @Signature(name="updatesAreDetected", args={int.class}), @Signature(name="usesLocalFilePerTable", args={}), @Signature(name="usesLocalFiles", args={}), @Signature(name="isServerBigSCN", args={})})
    protected void preDatabaseMetaData(Method method, Object object, Object ... objectArray) {
        try {
            AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
            abstractShardingConnection.acquireConnectionLock();
            OracleConnection oracleConnection = (OracleConnection)abstractShardingConnection.getCatalogDatabaseConnection();
            this.setDelegate(oracleConnection.getMetaData());
        }
        catch (SQLException sQLException) {
            ((AbstractShardingConnection)this.getCreator()).releaseConnectionLock();
            throw new RuntimeException(sQLException);
        }
    }

    @Post
    @Methods(signatures={@Signature(name="allProceduresAreCallable", args={}), @Signature(name="allTablesAreSelectable", args={}), @Signature(name="autoCommitFailureClosesAllResultSets", args={}), @Signature(name="dataDefinitionCausesTransactionCommit", args={}), @Signature(name="dataDefinitionIgnoredInTransactions", args={}), @Signature(name="deletesAreDetected", args={int.class}), @Signature(name="doesMaxRowSizeIncludeBlobs", args={}), @Signature(name="generatedKeyAlwaysReturned", args={}), @Signature(name="insertsAreDetected", args={int.class}), @Signature(name="isCatalogAtStart", args={}), @Signature(name="isReadOnly", args={}), @Signature(name="locatorsUpdateCopy", args={}), @Signature(name="nullPlusNonNullIsNull", args={}), @Signature(name="nullsAreSortedAtEnd", args={}), @Signature(name="nullsAreSortedAtStart", args={}), @Signature(name="nullsAreSortedHigh", args={}), @Signature(name="nullsAreSortedLow", args={}), @Signature(name="othersDeletesAreVisible", args={int.class}), @Signature(name="othersInsertsAreVisible", args={int.class}), @Signature(name="othersUpdatesAreVisible", args={int.class}), @Signature(name="ownDeletesAreVisible", args={int.class}), @Signature(name="ownInsertsAreVisible", args={int.class}), @Signature(name="ownUpdatesAreVisible", args={int.class}), @Signature(name="storesLowerCaseIdentifiers", args={}), @Signature(name="storesLowerCaseQuotedIdentifiers", args={}), @Signature(name="storesMixedCaseIdentifiers", args={}), @Signature(name="storesMixedCaseQuotedIdentifiers", args={}), @Signature(name="storesUpperCaseIdentifiers", args={}), @Signature(name="storesUpperCaseQuotedIdentifiers", args={}), @Signature(name="supportsANSI92EntryLevelSQL", args={}), @Signature(name="supportsANSI92FullSQL", args={}), @Signature(name="supportsANSI92IntermediateSQL", args={}), @Signature(name="supportsAlterTableWithAddColumn", args={}), @Signature(name="supportsAlterTableWithDropColumn", args={}), @Signature(name="supportsBatchUpdates", args={}), @Signature(name="supportsCatalogsInDataManipulation", args={}), @Signature(name="supportsCatalogsInIndexDefinitions", args={}), @Signature(name="supportsCatalogsInPrivilegeDefinitions", args={}), @Signature(name="supportsCatalogsInProcedureCalls", args={}), @Signature(name="supportsCatalogsInTableDefinitions", args={}), @Signature(name="supportsColumnAliasing", args={}), @Signature(name="supportsConvert", args={}), @Signature(name="supportsConvert", args={int.class, int.class}), @Signature(name="supportsCoreSQLGrammar", args={}), @Signature(name="supportsCorrelatedSubqueries", args={}), @Signature(name="supportsDataDefinitionAndDataManipulationTransactions", args={}), @Signature(name="supportsDataManipulationTransactionsOnly", args={}), @Signature(name="supportsDifferentTableCorrelationNames", args={}), @Signature(name="supportsExpressionsInOrderBy", args={}), @Signature(name="supportsExtendedSQLGrammar", args={}), @Signature(name="supportsFullOuterJoins", args={}), @Signature(name="supportsGetGeneratedKeys", args={}), @Signature(name="supportsGroupBy", args={}), @Signature(name="supportsGroupByBeyondSelect", args={}), @Signature(name="supportsGroupByUnrelated", args={}), @Signature(name="supportsIntegrityEnhancementFacility", args={}), @Signature(name="supportsLikeEscapeClause", args={}), @Signature(name="supportsLimitedOuterJoins", args={}), @Signature(name="supportsMinimumSQLGrammar", args={}), @Signature(name="supportsMixedCaseIdentifiers", args={}), @Signature(name="supportsMixedCaseQuotedIdentifiers", args={}), @Signature(name="supportsMultipleOpenResults", args={}), @Signature(name="supportsMultipleResultSets", args={}), @Signature(name="supportsMultipleTransactions", args={}), @Signature(name="supportsNamedParameters", args={}), @Signature(name="supportsNonNullableColumns", args={}), @Signature(name="supportsOpenCursorsAcrossCommit", args={}), @Signature(name="supportsOpenCursorsAcrossRollback", args={}), @Signature(name="supportsOpenStatementsAcrossCommit", args={}), @Signature(name="supportsOpenStatementsAcrossRollback", args={}), @Signature(name="supportsOrderByUnrelated", args={}), @Signature(name="supportsOuterJoins", args={}), @Signature(name="supportsPositionedDelete", args={}), @Signature(name="supportsPositionedUpdate", args={}), @Signature(name="supportsResultSetConcurrency", args={int.class, int.class}), @Signature(name="supportsResultSetHoldability", args={int.class}), @Signature(name="supportsResultSetType", args={int.class}), @Signature(name="supportsSavepoints", args={}), @Signature(name="supportsSchemasInDataManipulation", args={}), @Signature(name="supportsSchemasInIndexDefinitions", args={}), @Signature(name="supportsSchemasInPrivilegeDefinitions", args={}), @Signature(name="supportsSchemasInProcedureCalls", args={}), @Signature(name="supportsSchemasInTableDefinitions", args={}), @Signature(name="supportsSelectForUpdate", args={}), @Signature(name="supportsStatementPooling", args={}), @Signature(name="supportsStoredFunctionsUsingCallSyntax", args={}), @Signature(name="supportsStoredProcedures", args={}), @Signature(name="supportsSubqueriesInComparisons", args={}), @Signature(name="supportsSubqueriesInExists", args={}), @Signature(name="supportsSubqueriesInIns", args={}), @Signature(name="supportsSubqueriesInQuantifieds", args={}), @Signature(name="supportsTableCorrelationNames", args={}), @Signature(name="supportsTransactionIsolationLevel", args={int.class}), @Signature(name="supportsTransactions", args={}), @Signature(name="supportsUnion", args={}), @Signature(name="supportsUnionAll", args={}), @Signature(name="updatesAreDetected", args={int.class}), @Signature(name="usesLocalFilePerTable", args={}), @Signature(name="usesLocalFiles", args={}), @Signature(name="isServerBigSCN", args={})})
    protected boolean postDatabaseMetaDataBoolean(Method method, boolean bl) {
        try {
            this.postDatabaseMetaDataCleanup();
        }
        catch (SQLException sQLException) {
            throw new RuntimeException(sQLException);
        }
        finally {
            ((AbstractShardingConnection)this.getCreator()).releaseConnectionLock();
        }
        return bl;
    }

    @Post
    @Methods(signatures={@Signature(name="getLobMaxLength", args={}), @Signature(name="getAuditBanner", args={}), @Signature(name="getAccessBanner", args={}), @Signature(name="getCatalogSeparator", args={}), @Signature(name="getCatalogTerm", args={}), @Signature(name="getDatabaseMajorVersion", args={}), @Signature(name="getDatabaseMinorVersion", args={}), @Signature(name="getDatabaseProductName", args={}), @Signature(name="getDatabaseProductVersion", args={}), @Signature(name="getDefaultTransactionIsolation", args={}), @Signature(name="getDriverMajorVersion", args={}), @Signature(name="getDriverMinorVersion", args={}), @Signature(name="getDriverName", args={}), @Signature(name="getDriverVersion", args={}), @Signature(name="getIdentifierQuoteString", args={}), @Signature(name="getMaxBinaryLiteralLength", args={}), @Signature(name="getMaxCatalogNameLength", args={}), @Signature(name="getMaxCharLiteralLength", args={}), @Signature(name="getMaxColumnNameLength", args={}), @Signature(name="getMaxColumnsInGroupBy", args={}), @Signature(name="getMaxColumnsInIndex", args={}), @Signature(name="getMaxColumnsInOrderBy", args={}), @Signature(name="getMaxColumnsInSelect", args={}), @Signature(name="getMaxColumnsInTable", args={}), @Signature(name="getMaxConnections", args={}), @Signature(name="getMaxCursorNameLength", args={}), @Signature(name="getMaxIndexLength", args={}), @Signature(name="getMaxProcedureNameLength", args={}), @Signature(name="getMaxRowSize", args={}), @Signature(name="getMaxSchemaNameLength", args={}), @Signature(name="getMaxStatementLength", args={}), @Signature(name="getMaxStatements", args={}), @Signature(name="getMaxTableNameLength", args={}), @Signature(name="getMaxTablesInSelect", args={}), @Signature(name="getMaxUserNameLength", args={}), @Signature(name="getNumericFunctions", args={}), @Signature(name="getProcedureTerm", args={}), @Signature(name="getSQLKeywords", args={}), @Signature(name="getSQLStateType", args={}), @Signature(name="getSchemaTerm", args={}), @Signature(name="getSearchStringEscape", args={}), @Signature(name="getStringFunctions", args={}), @Signature(name="getSystemFunctions", args={}), @Signature(name="getTimeDateFunctions", args={}), @Signature(name="getURL", args={}), @Signature(name="getUserName", args={})})
    protected Object postDatabaseMetaDataObject(Method method, Object object) {
        try {
            this.postDatabaseMetaDataCleanup();
        }
        catch (SQLException sQLException) {
            throw new RuntimeException(sQLException);
        }
        finally {
            ((AbstractShardingConnection)this.getCreator()).releaseConnectionLock();
        }
        return object;
    }

    @Post
    @Methods(signatures={@Signature(name="getAttributes", args={String.class, String.class, String.class, String.class}), @Signature(name="getBestRowIdentifier", args={String.class, String.class, String.class}), @Signature(name="getCatalogs", args={}), @Signature(name="getClientInfoProperties", args={}), @Signature(name="getColumnPrivileges", args={String.class, String.class, String.class, String.class}), @Signature(name="getColumns", args={String.class, String.class, String.class, String.class}), @Signature(name="getCrossReference", args={String.class, String.class, String.class, String.class, String.class, String.class}), @Signature(name="getExportedKeys", args={String.class, String.class, String.class}), @Signature(name="getFunctionColumns", args={String.class, String.class, String.class, String.class}), @Signature(name="getFunctions", args={String.class, String.class, String.class}), @Signature(name="getImportedKeys", args={String.class, String.class, String.class}), @Signature(name="getIndexInfo", args={String.class, String.class, String.class, String.class, String.class}), @Signature(name="getPrimaryKeys", args={String.class, String.class, String.class}), @Signature(name="getProcedureColumns", args={String.class, String.class, String.class, String.class}), @Signature(name="getProcedures", args={String.class, String.class, String.class}), @Signature(name="getPseudoColumns", args={String.class, String.class, String.class, String.class}), @Signature(name="getSchemas", args={}), @Signature(name="getSchemas", args={String.class, String.class}), @Signature(name="getSuperTables", args={String.class, String.class, String.class}), @Signature(name="getSuperTypes", args={String.class, String.class, String.class}), @Signature(name="getTablePrivileges", args={String.class, String.class, String.class}), @Signature(name="getTableTypes", args={}), @Signature(name="getTables", args={String.class, String.class, String.class, String[].class}), @Signature(name="getTypeInfo", args={}), @Signature(name="getUDTs", args={String.class, String.class, String.class, int[].class}), @Signature(name="getVersionColumns", args={String.class, String.class, String.class})})
    protected ResultSet postDatabaseMetaDataForResultSet(Method method, ResultSet resultSet) {
        try {
            AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
            if (resultSet != null) {
                OracleStatement oracleStatement = (OracleStatement)resultSet.getStatement();
                AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)((Object)abstractShardingConnection.createStatement());
                abstractShardingStatement.setDelegate(oracleStatement);
                resultSet = ShardingDriverExtension.PROXY_FACTORY.proxyFor(resultSet, abstractShardingStatement);
                abstractShardingStatement.closeOnCompletion();
            }
            this.setDelegate(null);
        }
        catch (SQLException sQLException) {
            throw new RuntimeException(sQLException);
        }
        finally {
            ((AbstractShardingConnection)this.getCreator()).releaseConnectionLock();
        }
        return resultSet;
    }

    @Post
    @Methods(signatures={@Signature(name="getOracleTypeMetaData", args={String.class})})
    protected OracleTypeMetaData postOracleTypeMetaData(Method method, OracleTypeMetaData oracleTypeMetaData) {
        try {
            this.postDatabaseMetaDataCleanup();
        }
        catch (SQLException sQLException) {
            throw new RuntimeException(sQLException);
        }
        finally {
            ((AbstractShardingConnection)this.getCreator()).releaseConnectionLock();
        }
        return oracleTypeMetaData;
    }

    @Post
    @Methods(signatures={@Signature(name="getRowIdLifetime", args={})})
    protected RowIdLifetime postRowIdLifetimeMetaData(Method method, RowIdLifetime rowIdLifetime) {
        try {
            this.postDatabaseMetaDataCleanup();
        }
        catch (SQLException sQLException) {
            throw new RuntimeException(sQLException);
        }
        finally {
            ((AbstractShardingConnection)this.getCreator()).releaseConnectionLock();
        }
        return rowIdLifetime;
    }

    @OnError(value=SQLException.class)
    protected Object onErrorStmt(Method method, SQLException sQLException) throws SQLException {
        ((AbstractShardingConnection)this.getCreator()).checkAndReleaseConnectionLock();
        throw sQLException;
    }

    public Connection getConnection() throws SQLException {
        return (Connection)this.getCreator();
    }

    @Pre
    @Methods(signatures={@Signature(name="setACProxy", args={Object.class}), @Signature(name="getACProxy", args={})})
    protected void preUnsupportedConnectionMethods(Method method, Object object, Object ... objectArray) {
        throw new RuntimeException((SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace());
    }

    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        if (clazz.isInterface()) {
            return clazz.isInstance(this);
        }
        throw (SQLException)DatabaseError.createSqlException(177).fillInStackTrace();
    }

    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface() && clazz.isInstance(this)) {
            return (T)this;
        }
        throw (SQLException)DatabaseError.createSqlException(177).fillInStackTrace();
    }

    void postDatabaseMetaDataCleanup() throws SQLException {
        AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
        DatabaseMetaData databaseMetaData = this.getDelegate();
        abstractShardingConnection.closeDatabaseConnection((OracleConnection)databaseMetaData.getConnection());
        this.setDelegate(null);
    }
}

