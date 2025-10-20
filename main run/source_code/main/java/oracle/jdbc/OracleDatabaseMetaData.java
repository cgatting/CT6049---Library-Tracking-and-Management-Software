/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypeMetaData;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleSql;
import oracle.jdbc.internal.AdditionalDatabaseMetaData;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleResultSet;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.SQLName;
import oracle.sql.TypeDescriptor;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.METADATA})
public class OracleDatabaseMetaData
implements AdditionalDatabaseMetaData,
Monitor {
    private static String DRIVER_NAME = "Oracle JDBC driver";
    private static String DRIVER_VERSION = "21.6.0.0.0";
    private static int DRIVER_MAJOR_VERSION = 21;
    private static int DRIVER_MINOR_VERSION = 6;
    private static String LOB_MAXSIZE = "4294967295";
    private static long LOB_MAXLENGTH_32BIT = 0xFFFFFFFFL;
    protected OracleConnection connection;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();
    private static int DRIVER_API_MAJOR_VERSION = 4;
    private static int DRIVER_API_MINOR_VERSION = 2;
    int procedureResultUnknown = 0;
    int procedureNoResult = 1;
    int procedureReturnsResult = 2;
    int procedureColumnUnknown = 0;
    int procedureColumnIn = 1;
    int procedureColumnInOut = 2;
    int procedureColumnOut = 4;
    int procedureColumnReturn = 5;
    int procedureColumnResult = 3;
    int procedureNoNulls = 0;
    int procedureNullable = 1;
    int procedureNullableUnknown = 2;
    int columnNoNulls = 0;
    int columnNullable = 1;
    int columnNullableUnknown = 2;
    static final int bestRowTemporary = 0;
    static final int bestRowTransaction = 1;
    static final int bestRowSession = 2;
    static final int bestRowUnknown = 0;
    static final int bestRowNotPseudo = 1;
    static final int bestRowPseudo = 2;
    int versionColumnUnknown = 0;
    int versionColumnNotPseudo = 1;
    int versionColumnPseudo = 2;
    int importedKeyCascade = 0;
    int importedKeyRestrict = 1;
    int importedKeySetNull = 2;
    int typeNoNulls = 0;
    int typeNullable = 1;
    int typeNullableUnknown = 2;
    int typePredNone = 0;
    int typePredChar = 1;
    int typePredBasic = 2;
    int typeSearchable = 3;
    short tableIndexStatistic = 0;
    short tableIndexClustered = 1;
    short tableIndexHashed = (short)2;
    short tableIndexOther = (short)3;
    short attributeNoNulls = 0;
    short attributeNullable = 1;
    short attributeNullableUnknown = (short)2;
    int sqlStateXOpen = 1;
    int sqlStateSQL99 = 2;
    protected static final String sqlWildcardRegex = "^%|^_|[^/]%|[^/]_";
    protected static Pattern sqlWildcardPattern = null;
    protected static final String sqlEscapeRegex = "/";
    protected static Pattern sqlEscapePattern = null;
    protected Object acProxy;

    public OracleDatabaseMetaData(oracle.jdbc.OracleConnection oracleConnection) {
        this.connection = oracleConnection.physicalConnectionWithin();
    }

    @Override
    public boolean allProceduresAreCallable() throws SQLException {
        return false;
    }

    @Override
    public boolean allTablesAreSelectable() throws SQLException {
        return false;
    }

    @Override
    public String getURL() throws SQLException {
        return this.connection.getURL();
    }

    @Override
    public String getUserName() throws SQLException {
        return this.connection.getUserName();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return false;
    }

    @Override
    public boolean nullsAreSortedHigh() throws SQLException {
        return true;
    }

    @Override
    public boolean nullsAreSortedLow() throws SQLException {
        return false;
    }

    @Override
    public boolean nullsAreSortedAtStart() throws SQLException {
        return false;
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws SQLException {
        return false;
    }

    @Override
    public String getDatabaseProductName() throws SQLException {
        return "Oracle";
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        return this.connection.getDatabaseProductVersion();
    }

    @Override
    public String getDriverName() throws SQLException {
        return DRIVER_NAME;
    }

    @Override
    public String getDriverVersion() throws SQLException {
        return DRIVER_VERSION;
    }

    @Override
    public int getDriverMajorVersion() {
        return DRIVER_MAJOR_VERSION;
    }

    @Override
    public int getDriverMinorVersion() {
        return DRIVER_MINOR_VERSION;
    }

    @Override
    public boolean usesLocalFiles() throws SQLException {
        return false;
    }

    @Override
    public boolean usesLocalFilePerTable() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        return true;
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        return true;
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        return false;
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        return true;
    }

    @Override
    public String getIdentifierQuoteString() throws SQLException {
        return "\"";
    }

    @Override
    public String getSQLKeywords() throws SQLException {
        return "ACCESS, ADD, ALTER, AUDIT, CLUSTER, COLUMN, COMMENT, COMPRESS, CONNECT, DATE, DROP, EXCLUSIVE, FILE, IDENTIFIED, IMMEDIATE, INCREMENT, INDEX, INITIAL, INTERSECT, LEVEL, LOCK, LONG, MAXEXTENTS, MINUS, MODE, NOAUDIT, NOCOMPRESS, NOWAIT, NUMBER, OFFLINE, ONLINE, PCTFREE, PRIOR, all_PL_SQL_reserved_ words";
    }

    @Override
    public String getNumericFunctions() throws SQLException {
        return "ABS,ACOS,ASIN,ATAN,ATAN2,CEILING,COS,EXP,FLOOR,LOG,LOG10,MOD,PI,POWER,ROUND,SIGN,SIN,SQRT,TAN,TRUNCATE";
    }

    @Override
    public String getStringFunctions() throws SQLException {
        return "ASCII,CHAR,CHAR_LENGTH,CHARACTER_LENGTH,CONCAT,LCASE,LENGTH,LTRIM,OCTET_LENGTH,REPLACE,RTRIM,SOUNDEX,SUBSTRING,UCASE";
    }

    @Override
    public String getSystemFunctions() throws SQLException {
        return "USER";
    }

    @Override
    public String getTimeDateFunctions() throws SQLException {
        return "CURRENT_DATE,CURRENT_TIMESTAMP,CURDATE,EXTRACT,HOUR,MINUTE,MONTH,SECOND,YEAR";
    }

    @Override
    public String getSearchStringEscape() throws SQLException {
        return sqlEscapeRegex;
    }

    @Override
    public String getExtraNameCharacters() throws SQLException {
        return "$#";
    }

    @Override
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsColumnAliasing() throws SQLException {
        return true;
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsConvert() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsConvert(int n2, int n3) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsTableCorrelationNames() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsOrderByUnrelated() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsGroupBy() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsGroupByUnrelated() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsLikeEscapeClause() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsMultipleResultSets() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsMultipleTransactions() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsNonNullableColumns() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsANSI92FullSQL() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsOuterJoins() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws SQLException {
        return true;
    }

    @Override
    public String getSchemaTerm() throws SQLException {
        return "schema";
    }

    @Override
    public String getProcedureTerm() throws SQLException {
        return "procedure";
    }

    @Override
    public String getCatalogTerm() throws SQLException {
        return "";
    }

    @Override
    public boolean isCatalogAtStart() throws SQLException {
        return false;
    }

    @Override
    public String getCatalogSeparator() throws SQLException {
        return "";
    }

    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsPositionedDelete() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsPositionedUpdate() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsSelectForUpdate() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsStoredProcedures() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsSubqueriesInExists() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsSubqueriesInIns() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsUnion() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsUnionAll() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        return false;
    }

    @Override
    public int getMaxBinaryLiteralLength() throws SQLException {
        return 1000;
    }

    @Override
    public int getMaxCharLiteralLength() throws SQLException {
        return 2000;
    }

    protected int getIdentifierLength() throws SQLException {
        return this.isCompatible122OrGreater() ? 128 : 30;
    }

    @Override
    public int getMaxColumnNameLength() throws SQLException {
        return this.getIdentifierLength();
    }

    @Override
    public int getMaxColumnsInGroupBy() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxColumnsInIndex() throws SQLException {
        return 32;
    }

    @Override
    public int getMaxColumnsInOrderBy() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxColumnsInSelect() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxColumnsInTable() throws SQLException {
        return 1000;
    }

    @Override
    public int getMaxConnections() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxCursorNameLength() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxIndexLength() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxSchemaNameLength() throws SQLException {
        return this.getIdentifierLength();
    }

    @Override
    public int getMaxProcedureNameLength() throws SQLException {
        return this.getIdentifierLength();
    }

    @Override
    public int getMaxCatalogNameLength() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxRowSize() throws SQLException {
        return 0;
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        return true;
    }

    @Override
    public int getMaxStatementLength() throws SQLException {
        return 65535;
    }

    @Override
    public int getMaxStatements() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxTableNameLength() throws SQLException {
        return this.getIdentifierLength();
    }

    @Override
    public int getMaxTablesInSelect() throws SQLException {
        return 0;
    }

    @Override
    public int getMaxUserNameLength() throws SQLException {
        return this.getIdentifierLength();
    }

    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
        return 2;
    }

    @Override
    public boolean supportsTransactions() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int n2) throws SQLException {
        return n2 == 2 || n2 == 8;
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        return true;
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        return true;
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        return false;
    }

    @Override
    public ResultSet getProcedures(String string, String string2, String string3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleResultSet oracleResultSet;
            String string4 = "SELECT\n  -- Standalone procedures and functions\n  NULL AS procedure_cat,\n  owner AS procedure_schem,\n  object_name AS procedure_name,\n  NULL,\n  NULL,\n  NULL,\n  'Standalone procedure or function' AS remarks,\n  DECODE(object_type, 'PROCEDURE', 1,\n                      'FUNCTION', 2,\n                      0) AS procedure_type\n,  NULL AS specific_name\nFROM all_objects\nWHERE (object_type = 'PROCEDURE' OR object_type = 'FUNCTION')\n  AND owner LIKE :1 ESCAPE '/'\n  AND object_name LIKE :2 ESCAPE '/'\n";
            String string5 = "SELECT\n -- Packaged procedures --\n object_name AS procedure_cat, \n owner AS procedure_schem, \n procedure_name AS procedure_name, \n  NULL,\n  NULL,\n  NULL,\n 'Packaged procedure' AS remarks,\n 1 AS procedure_type\n, NULL AS specific_name\nFROM all_procedures\nWHERE procedure_name IS NOT NULL\n AND ";
            String string6 = "SELECT NULL\nFROM all_arguments\nWHERE argument_name IS NULL\n  AND in_out = 'OUT'\n  AND   data_level = 0\n  AND ";
            String string7 = "SELECT\n  -- Packaged functions\n  package_name AS procedure_cat,\n  owner AS procedure_schem,\n  object_name AS procedure_name,\n  NULL,\n  NULL,\n  NULL,\n  'Packaged function' AS remarks,\n  2 AS procedure_type\n,  NULL AS specific_name\nFROM all_arguments\nWHERE argument_name IS NULL\n  AND in_out = 'OUT'\n  AND   data_level = 0\n  AND ";
            String string8 = " AND all_procedures.object_id = all_arguments.object_id  AND all_procedures.subprogram_id = all_arguments.subprogram_id";
            String string9 = "package_name LIKE :3 ESCAPE '/'\n  AND owner LIKE :4 ESCAPE '/'\n  AND object_name LIKE :5 ESCAPE '/'\n";
            String string10 = "object_name LIKE :3 ESCAPE '/'\n  AND owner LIKE :4 ESCAPE '/'\n  AND procedure_name LIKE :5 ESCAPE '/'\n";
            String string11 = "object_name IS NOT NULL\n  AND owner LIKE :6 ESCAPE '/'\n  AND procedure_name LIKE :7 ESCAPE '/'\n";
            String string12 = "package_name IS NOT NULL\n  AND owner LIKE :6 ESCAPE '/'\n  AND object_name LIKE :7 ESCAPE '/'\n";
            String string13 = "ORDER BY procedure_schem, procedure_name\n";
            PreparedStatement preparedStatement = null;
            String string14 = null;
            String string15 = string2;
            if (string2 == null) {
                string15 = "%";
            } else if (string2.equals("")) {
                string15 = this.getUserName().toUpperCase();
            }
            String string16 = string3;
            if (string3 == null) {
                string16 = "%";
            } else if (string3.equals("")) {
                throw (SQLException)DatabaseError.createSqlException(74).fillInStackTrace();
            }
            if (string == null) {
                string14 = string4 + "UNION ALL " + string5 + string11 + "AND NOT EXISTS (" + string6 + string12 + string8 + ")UNION ALL " + string7 + string12 + string13;
                preparedStatement = this.connection.prepareStatement(string14);
                preparedStatement.setString(1, string15);
                preparedStatement.setString(2, string16);
                preparedStatement.setString(3, string15);
                preparedStatement.setString(4, string16);
                preparedStatement.setString(5, string15);
                preparedStatement.setString(6, string16);
                preparedStatement.setString(7, string15);
                preparedStatement.setString(8, string16);
            } else if (string.equals("")) {
                string14 = string4;
                preparedStatement = this.connection.prepareStatement(string14);
                preparedStatement.setString(1, string15);
                preparedStatement.setString(2, string16);
            } else {
                string14 = string5 + string10 + "AND NOT EXISTS (" + string6 + string9 + string8 + ")UNION ALL " + string7 + string9 + string13;
                preparedStatement = this.connection.prepareStatement(string14);
                preparedStatement.setString(1, string);
                preparedStatement.setString(2, string15);
                preparedStatement.setString(3, string16);
                preparedStatement.setString(4, string);
                preparedStatement.setString(5, string15);
                preparedStatement.setString(6, string16);
                preparedStatement.setString(7, string);
                preparedStatement.setString(8, string15);
                preparedStatement.setString(9, string16);
            }
            preparedStatement.closeOnCompletion();
            OracleResultSet oracleResultSet2 = oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
            return oracleResultSet2;
        }
    }

    @Override
    public ResultSet getProcedureColumns(String string, String string2, String string3, String string4) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            boolean bl = this.connection.getIncludeSynonyms();
            ResultSet resultSet = null;
            resultSet = "".equals(string) && (string2 == null || !this.hasSqlWildcard(string2)) && string3 != null && !this.hasSqlWildcard(string3) ? this.getUnpackagedProcedureColumnsNoWildcards(string2 != null ? this.stripSqlEscapes(string2) : null, string3 != null ? this.stripSqlEscapes(string3) : null, string4) : (string != null && string.length() != 0 && !this.hasSqlWildcard(string) && (string2 == null || !this.hasSqlWildcard(string2)) ? this.getPackagedProcedureColumnsNoWildcards(string != null ? this.stripSqlEscapes(string) : null, string2 != null ? this.stripSqlEscapes(string2) : null, string3, string4) : this.getProcedureColumnsWithWildcards(string, string2, string3, string4, bl));
            ResultSet resultSet2 = resultSet;
            return resultSet2;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    ResultSet getUnpackagedProcedureColumnsNoWildcards(String string, String string2, String string3) throws SQLException {
        ResultSet resultSet;
        block6: {
            if ("".equals(string3)) {
                throw (SQLException)DatabaseError.createSqlException(74).fillInStackTrace();
            }
            String string4 = this.getUnpackagedProcedureColumnsNoWildcardsPlsql(string3);
            Statement statement = null;
            resultSet = null;
            try {
                Properties properties = new Properties();
                properties.setProperty("use_long_fetch", "true");
                statement = this.connection.prepareCall(string4, properties);
                statement.setString(1, string);
                statement.setString(2, string2);
                statement.setString(3, string3 == null ? "%" : string3);
                statement.registerOutParameter(4, -10);
                statement.registerOutParameter(5, 2);
                statement.closeOnCompletion();
                statement.setPoolable(false);
                statement.execute();
                int n2 = statement.getInt(5);
                if (n2 == 0) {
                    resultSet = ((OracleCallableStatement)statement).getCursor(4);
                    break block6;
                }
                throw (SQLException)DatabaseError.createSqlException(258).fillInStackTrace();
            }
            finally {
                if (resultSet == null && statement != null) {
                    statement.close();
                }
            }
        }
        return resultSet;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    ResultSet getPackagedProcedureColumnsNoWildcards(String string, String string2, String string3, String string4) throws SQLException {
        ResultSet resultSet;
        block6: {
            if ("".equals(string4)) {
                throw (SQLException)DatabaseError.createSqlException(74).fillInStackTrace();
            }
            String string5 = this.getPackagedProcedureColumnsNoWildcardsPlsql(string4);
            Statement statement = null;
            resultSet = null;
            try {
                Properties properties = new Properties();
                properties.setProperty("use_long_fetch", "true");
                statement = this.connection.prepareCall(string5, properties);
                statement.setString(1, string);
                statement.setString(2, string2);
                statement.setString(3, string3);
                statement.setString(4, string4 == null ? "%" : string4);
                statement.registerOutParameter(5, -10);
                statement.registerOutParameter(6, 2);
                statement.closeOnCompletion();
                statement.setPoolable(false);
                statement.execute();
                int n2 = statement.getInt(6);
                if (n2 == 0) {
                    resultSet = ((OracleCallableStatement)statement).getCursor(5);
                    break block6;
                }
                throw (SQLException)DatabaseError.createSqlException(258).fillInStackTrace();
            }
            finally {
                if (resultSet == null && statement != null) {
                    statement.close();
                }
            }
        }
        return resultSet;
    }

    ResultSet getProcedureColumnsWithWildcards(String string, String string2, String string3, String string4, boolean bl) throws SQLException {
        String string5 = "SELECT arg.package_name AS procedure_cat,\n       arg.owner AS procedure_schem,\n       arg.object_name AS procedure_name,\n       arg.argument_name AS column_name,\n       DECODE(arg.position, 0, 5,\n                        DECODE(arg.in_out, 'IN', 1,\n                                       'OUT', 4,\n                                       'IN/OUT', 2,\n                                       0)) AS column_type,\n" + this.datatypeQuery(DataTypeSource.ARGS, "arg") + "       DECODE(arg.data_type, 'OBJECT', arg.type_owner || '.' || arg.type_name,               arg.data_type) AS type_name,\n       DECODE (arg.data_precision, NULL, arg.data_length,\n                               arg.data_precision) AS precision,\n       arg.data_length AS length,\n       arg.data_scale AS scale,\n       10 AS radix,\n       1 AS nullable,\n       NULL AS remarks,\n       arg.default_value AS column_def,\n       NULL as sql_data_type,\n       NULL AS sql_datetime_sub,\n       DECODE(arg.data_type,\n                         'CHAR', 32767,\n                         'VARCHAR2', 32767,\n                         'LONG', 32767,\n                         'RAW', 32767,\n                         'LONG RAW', 32767,\n                         NULL) AS char_octet_length,\n       (arg.sequence - 1) AS ordinal_position,\n       'YES' AS is_nullable,\n       NULL AS specific_name,\n       arg.sequence,\n       arg.overload,\n       arg.default_value\n FROM all_arguments arg, all_procedures proc\n WHERE arg.owner LIKE :1 ESCAPE '/'\n AND arg.object_name LIKE :2 ESCAPE '/' AND arg.data_level = 0\n";
        short s2 = this.connection.getVersionNumber();
        String string6 = s2 >= 10200 ? " AND arg.owner = proc.owner\n AND arg.object_id = proc.object_id\n AND arg.subprogram_id = proc.subprogram_id\n" : " AND arg.owner = proc.owner\n AND proc.object_name = arg.package_name\n AND proc.procedure_name = arg.object_name\n";
        String string7 = "  AND arg.package_name LIKE :3 ESCAPE '/'\n";
        String string8 = "  AND arg.package_name IS NULL\n";
        String string9 = "  AND arg.argument_name LIKE :4 ESCAPE '/'\n";
        String string10 = "  AND (arg.argument_name LIKE :5 ESCAPE '/'\n       OR (arg.argument_name IS NULL\n           AND arg.data_type IS NOT NULL))\n";
        String string11 = "ORDER BY procedure_schem, procedure_name, overload, sequence\n";
        String string12 = null;
        PreparedStatement preparedStatement = null;
        String string13 = null;
        String string14 = string2;
        if (string2 == null) {
            string14 = "%";
        } else if (string2.equals("")) {
            string14 = this.getUserName().toUpperCase();
        }
        String string15 = string3;
        if (string3 == null) {
            string15 = "%";
        } else if (string3.equals("")) {
            throw (SQLException)DatabaseError.createSqlException(74).fillInStackTrace();
        }
        String string16 = string4;
        if (string4 == null || string4.equals("%")) {
            string16 = "%";
            string13 = string10;
        } else {
            if (string4.equals("")) {
                throw (SQLException)DatabaseError.createSqlException(74).fillInStackTrace();
            }
            string13 = string9;
        }
        if (string == null) {
            string12 = string5 + string6 + string13 + string11;
            preparedStatement = this.connection.prepareStatement(string12);
            preparedStatement.setString(1, string14);
            preparedStatement.setString(2, string15);
            preparedStatement.setString(3, string16);
        } else if (string.equals("")) {
            string12 = string5 + string6 + string8 + string13 + string11;
            preparedStatement = this.connection.prepareStatement(string12);
            preparedStatement.setString(1, string14);
            preparedStatement.setString(2, string15);
            preparedStatement.setString(3, string16);
        } else {
            string12 = string5 + string6 + string7 + string13 + string11;
            preparedStatement = this.connection.prepareStatement(string12);
            preparedStatement.setString(1, string14);
            preparedStatement.setString(2, string15);
            preparedStatement.setString(3, string);
            preparedStatement.setString(4, string16);
        }
        preparedStatement.closeOnCompletion();
        OracleResultSet oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
        return oracleResultSet;
    }

    @Override
    public ResultSet getFunctionColumns(String string, String string2, String string3, String string4) throws SQLException {
        short s2 = this.connection.getVersionNumber();
        String string5 = "SELECT package_name AS function_cat,\n     arg.owner AS function_schem,\n     arg.object_name AS function_name,\n     arg.argument_name AS column_name,\n     DECODE(arg.position,\n            0, 5,\n            DECODE(arg.in_out,\n                   'IN', 1,\n                   'OUT', 3,\n                   'IN/OUT', 2,\n                   0)) AS column_type,\n" + this.datatypeQuery(DataTypeSource.ARGS, "arg") + "     DECODE(arg.data_type,\n            'OBJECT', arg.type_owner || '.' || arg.type_name,             arg.data_type) AS type_name,\n     DECODE(arg.data_precision,\n            NULL, arg.data_length,\n            arg.data_precision) AS precision,\n     arg.data_length AS length,\n     arg.data_scale AS scale,\n     10 AS radix,\n     1 AS nullable,\n     NULL AS remarks,\n     arg.default_value AS column_def,\n     NULL as sql_data_type,\n     NULL AS sql_datetime_sub,\n     DECODE(arg.data_type,\n            'CHAR', 32767,\n            'VARCHAR2', 32767,\n            'LONG', 32767,\n            'RAW', 32767,\n            'LONG RAW', 32767,\n            NULL) AS char_octet_length,\n     (arg.sequence - 1) AS ordinal_position,\n     'YES' AS is_nullable,\n     NULL AS specific_name,\n     arg.sequence,\n     arg.overload,\n     arg.default_value\n FROM all_arguments arg\n" + (s2 >= 10200 ? "" : ", all_procedures proc\n") + " WHERE arg.owner LIKE ? ESCAPE '/'\n  AND arg.object_name LIKE ? ESCAPE '/'\n";
        String string6 = s2 >= 10200 ? "  AND arg.object_id IN\n  (SELECT object_id FROM all_procedures proc\n   WHERE proc.owner LIKE ? ESCAPE '/'\n    AND\n     ((proc.object_type = 'FUNCTION'\n       AND\n       proc.object_name LIKE ? ESCAPE '/')\n      OR\n      (proc.object_type = 'PACKAGE'\n       AND\n       proc.object_name LIKE ? ESCAPE '/'\n       AND\n       proc.procedure_name IS NOT NULL)\n       AND\n       proc.procedure_name LIKE ? ESCAPE '/'))\n" : "  AND proc.owner = arg.owner\n  AND proc.object_name = arg.object_name\n";
        String string7 = "  AND arg.package_name LIKE ? ESCAPE '/'\n";
        String string8 = "  AND arg.package_name IS NULL\n";
        String string9 = "  AND arg.argument_name LIKE ? ESCAPE '/'\n";
        String string10 = "  AND (arg.argument_name LIKE ? ESCAPE '/'\n     OR (arg.argument_name IS NULL\n         AND arg.data_type IS NOT NULL))\n";
        String string11 = " ORDER BY function_schem, function_name, overload, sequence\n";
        String string12 = null;
        PreparedStatement preparedStatement = null;
        String string13 = null;
        String string14 = string2;
        if (string2 == null) {
            string14 = "%";
        } else if (string2.equals("")) {
            string14 = this.getUserName().toUpperCase();
        }
        String string15 = string;
        if (string == null || string.equals("")) {
            string15 = "%";
        }
        String string16 = string3;
        if (string3 == null) {
            string16 = "%";
        } else if (string3.equals("")) {
            throw (SQLException)DatabaseError.createSqlException(74).fillInStackTrace();
        }
        String string17 = string4;
        if (string4 == null || string4.equals("%")) {
            string17 = "%";
            string13 = string10;
        } else {
            if (string4.equals("")) {
                throw (SQLException)DatabaseError.createSqlException(74).fillInStackTrace();
            }
            string13 = string9;
        }
        Properties properties = new Properties();
        properties.setProperty("use_long_fetch", "true");
        if (string == null) {
            string12 = string5 + string13 + string6 + string11;
            preparedStatement = this.connection.prepareStatement(string12, properties);
            preparedStatement.setString(1, string14);
            preparedStatement.setString(2, string16);
            preparedStatement.setString(3, string17);
            if (s2 >= 10200) {
                preparedStatement.setString(4, string14);
                preparedStatement.setString(5, string16);
                preparedStatement.setString(6, string15);
                preparedStatement.setString(7, string16);
            }
        } else if (string.equals("")) {
            string12 = string5 + string13 + string6 + string8 + string11;
            preparedStatement = this.connection.prepareStatement(string12, properties);
            preparedStatement.setString(1, string14);
            preparedStatement.setString(2, string16);
            preparedStatement.setString(3, string17);
            if (s2 >= 10200) {
                preparedStatement.setString(4, string14);
                preparedStatement.setString(5, string16);
                preparedStatement.setString(6, string15);
                preparedStatement.setString(7, string16);
            }
        } else {
            string12 = string5 + string13 + string6 + string7 + string11;
            preparedStatement = this.connection.prepareStatement(string12, properties);
            preparedStatement.setString(1, string14);
            preparedStatement.setString(2, string16);
            preparedStatement.setString(3, string17);
            if (s2 >= 10200) {
                preparedStatement.setString(4, string14);
                preparedStatement.setString(5, string16);
                preparedStatement.setString(6, string15);
                preparedStatement.setString(7, string16);
                preparedStatement.setString(8, string15);
            } else {
                preparedStatement.setString(4, string15);
            }
        }
        preparedStatement.closeOnCompletion();
        OracleResultSet oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
        return oracleResultSet;
    }

    @Override
    public ResultSet getTables(String string, String string2, String string3, String[] stringArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleResultSet oracleResultSet;
            String string42;
            Object object;
            Object object2;
            String string5 = "SELECT NULL AS table_cat,\n       o.owner AS table_schem,\n       o.object_name AS table_name,\n       o.object_type AS table_type,\n";
            String string6 = "       c.comments AS remarks\n";
            String string7 = "       NULL AS remarks\n";
            String string8 = "  FROM all_objects o, all_tab_comments c\n";
            String string9 = "  FROM all_objects o\n";
            String string10 = "  WHERE o.owner LIKE :1 ESCAPE '/'\n    AND o.object_name LIKE :2 ESCAPE '/'\n";
            String string11 = "    AND o.owner = c.owner (+)\n    AND o.object_name = c.table_name (+)\n";
            boolean bl = false;
            String string12 = "";
            String string13 = "";
            if (stringArray != null) {
                string12 = "    AND o.object_type IN ('xxx'";
                string13 = "    AND o.object_type IN ('xxx'";
                object2 = new ArrayList();
                object = this.getTableTypes();
                while (object.next()) {
                    ((ArrayList)object2).add(object.getString(1));
                }
                object.close();
                for (String string42 : stringArray) {
                    if (!((ArrayList)object2).contains(string42)) {
                        if (!this.connection.getJDBCStandardBehavior()) continue;
                        throw (SQLException)DatabaseError.createSqlException(68).fillInStackTrace();
                    }
                    if (string42.equals("SYNONYM")) {
                        string12 = string12 + ", '" + string42 + "'";
                        bl = true;
                        continue;
                    }
                    string12 = string12 + ", '" + string42 + "'";
                    string13 = string13 + ", '" + string42 + "'";
                }
                string12 = string12 + ")\n";
                string13 = string13 + ")\n";
            } else {
                bl = true;
                string12 = "    AND o.object_type IN ('TABLE', 'SYNONYM', 'VIEW')\n";
                string13 = "    AND o.object_type IN ('TABLE', 'VIEW')\n";
            }
            object2 = "  ORDER BY table_type, table_schem, table_name\n";
            object = "SELECT NULL AS table_cat,\n       s.owner AS table_schem,\n       s.synonym_name AS table_name,\n       'SYNONYM' AS table_table_type,\n";
            String[] stringArray2 = "       c.comments AS remarks\n";
            String string14 = "       NULL AS remarks\n";
            String string15 = "  FROM all_synonyms s, all_objects o, all_tab_comments c\n";
            string42 = "  FROM all_synonyms s, all_objects o\n";
            String string16 = "  WHERE s.owner LIKE :3 ESCAPE '/'\n    AND s.synonym_name LIKE :4 ESCAPE '/'\n    AND s.table_owner = o.owner\n    AND s.table_name = o.object_name\n    AND o.object_type IN ('TABLE', 'VIEW')\n";
            String string17 = "";
            string17 = string17 + string5;
            string17 = this.connection.getRemarksReporting() ? string17 + string6 + string8 : string17 + string7 + string9;
            string17 = string17 + string10;
            string17 = this.connection.getRestrictGetTables() ? string17 + string13 : string17 + string12;
            if (this.connection.getRemarksReporting()) {
                string17 = string17 + string11;
            }
            if (bl && this.connection.getRestrictGetTables()) {
                string17 = string17 + "UNION\n" + (String)object;
                string17 = this.connection.getRemarksReporting() ? string17 + (String)stringArray2 + string15 : string17 + string14 + string42;
                string17 = string17 + string16;
                if (this.connection.getRemarksReporting()) {
                    string17 = string17 + string11;
                }
            }
            string17 = string17 + (String)object2;
            PreparedStatement preparedStatement = this.connection.prepareStatement(string17);
            preparedStatement.setString(1, string2 == null ? "%" : string2);
            preparedStatement.setString(2, string3 == null ? "%" : string3);
            if (bl && this.connection.getRestrictGetTables()) {
                preparedStatement.setString(3, string2 == null ? "%" : string2);
                preparedStatement.setString(4, string3 == null ? "%" : string3);
            }
            preparedStatement.closeOnCompletion();
            OracleResultSet oracleResultSet2 = oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
            return oracleResultSet2;
        }
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        Statement statement = this.connection.createStatement();
        String string = "SELECT username AS table_schem,null as table_catalog  FROM all_users ORDER BY table_schem";
        statement.closeOnCompletion();
        OracleResultSet oracleResultSet = (OracleResultSet)statement.executeQuery(string);
        return oracleResultSet;
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        Statement statement = this.connection.createStatement();
        String string = "select 'nothing' as table_cat from dual where 1 = 2";
        statement.closeOnCompletion();
        OracleResultSet oracleResultSet = (OracleResultSet)statement.executeQuery(string);
        return oracleResultSet;
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
        Statement statement = this.connection.createStatement();
        String string = "select 'TABLE' as table_type from dual\nunion select 'VIEW' as table_type from dual\nunion select 'SYNONYM' as table_type from dual\norder by table_type\n";
        statement.closeOnCompletion();
        OracleResultSet oracleResultSet = (OracleResultSet)statement.executeQuery(string);
        return oracleResultSet;
    }

    @Override
    public ResultSet getColumns(String string, String string2, String string3, String string4) throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    @Override
    public ResultSet getColumnPrivileges(String string, String string2, String string3, String string4) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleResultSet oracleResultSet;
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT NULL AS table_cat,\n       table_schema AS table_schem,\n       table_name,\n       column_name,\n       grantor,\n       grantee,\n       privilege,\n       grantable AS is_grantable\nFROM all_col_privs\nWHERE table_schema LIKE :1 ESCAPE '/'\n  AND table_name LIKE :2 ESCAPE '/'\n  AND column_name LIKE :3 ESCAPE '/'\nORDER BY column_name, privilege\n");
            preparedStatement.setString(1, string2 == null ? "%" : string2);
            preparedStatement.setString(2, string3 == null ? "%" : string3);
            preparedStatement.setString(3, string4 == null ? "%" : string4);
            preparedStatement.closeOnCompletion();
            OracleResultSet oracleResultSet2 = oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
            return oracleResultSet2;
        }
    }

    @Override
    public ResultSet getTablePrivileges(String string, String string2, String string3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleResultSet oracleResultSet;
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT NULL AS table_cat,\n       table_schema AS table_schem,\n       table_name,\n       grantor,\n       grantee,\n       privilege,\n       grantable AS is_grantable\nFROM all_tab_privs\nWHERE table_schema LIKE :1 ESCAPE '/'\n  AND table_name LIKE :2 ESCAPE '/'\nORDER BY table_schem, table_name, privilege\n");
            preparedStatement.setString(1, string2 == null ? "%" : string2);
            preparedStatement.setString(2, string3 == null ? "%" : string3);
            preparedStatement.closeOnCompletion();
            OracleResultSet oracleResultSet2 = oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
            return oracleResultSet2;
        }
    }

    @Override
    public ResultSet getBestRowIdentifier(String string, String string2, String string3, int n2, boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleResultSet oracleResultSet;
            String string4 = " DECODE (t.data_type, 'CHAR', t.char_length, 'VARCHAR', t.char_length, 'VARCHAR2', t.char_length, 'NVARCHAR2', t.char_length, 'NCHAR', t.char_length, t.data_length)";
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT 1 AS scope, 'ROWID' AS column_name, -8 AS data_type,\n 'ROWID' AS type_name, 0 AS column_size, 0 AS buffer_length,\n       0 AS decimal_digits, 2 AS pseudo_column\nFROM DUAL\nWHERE :1 = 1\nUNION\nSELECT 2 AS scope,\n t.column_name,\n" + this.datatypeQuery(DataTypeSource.COLS, "t") + " t.data_type AS type_name,\n DECODE (t.data_precision, null, " + string4 + ", t.data_precision)\n  AS column_size,\n  0 AS buffer_length,\n  t.data_scale AS decimal_digits,\n       1 AS pseudo_column\nFROM all_tab_columns t, all_ind_columns i\nWHERE :2 = 1\n  AND t.table_name = :3\n  AND t.owner like :4 escape '/'\n  AND t.nullable != :5\n  AND t.owner = i.table_owner\n  AND t.table_name = i.table_name\n  AND t.column_name = i.column_name\n");
            switch (n2) {
                case 0: {
                    preparedStatement.setInt(1, 0);
                    preparedStatement.setInt(2, 0);
                    break;
                }
                case 1: {
                    preparedStatement.setInt(1, 1);
                    preparedStatement.setInt(2, 1);
                    break;
                }
                case 2: {
                    preparedStatement.setInt(1, 0);
                    preparedStatement.setInt(2, 1);
                }
            }
            preparedStatement.setString(3, string3);
            preparedStatement.setString(4, string2 == null ? "%" : string2);
            preparedStatement.setString(5, bl ? "X" : "Y");
            preparedStatement.closeOnCompletion();
            OracleResultSet oracleResultSet2 = oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
            return oracleResultSet2;
        }
    }

    @Override
    public ResultSet getVersionColumns(String string, String string2, String string3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleResultSet oracleResultSet;
            String string4 = " DECODE (c.data_type, 'CHAR', c.char_length, 'VARCHAR', c.char_length, 'VARCHAR2', c.char_length, 'NVARCHAR2', c.char_length, 'NCHAR', c.char_length, c.data_length)";
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT 0 AS scope,\n t.column_name,\n" + this.datatypeQuery(DataTypeSource.COLS, "c") + " c.data_type AS type_name,\n DECODE (c.data_precision, null, " + string4 + ", c.data_precision)\n   AS column_size,\n       0 as buffer_length,\n   c.data_scale as decimal_digits,\n   0 as pseudo_column\nFROM all_trigger_cols t, all_tab_columns c\nWHERE t.table_name = :1\n  AND c.owner like :2 escape '/'\n AND t.table_owner = c.owner\n  AND t.table_name = c.table_name\n AND t.column_name = c.column_name\n");
            preparedStatement.setString(1, string3);
            preparedStatement.setString(2, string2 == null ? "%" : string2);
            preparedStatement.closeOnCompletion();
            OracleResultSet oracleResultSet2 = oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
            return oracleResultSet2;
        }
    }

    @Override
    public ResultSet getPrimaryKeys(String string, String string2, String string3) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT NULL AS table_cat,\n       c.owner AS table_schem,\n       c.table_name,\n       c.column_name,\n       c.position AS key_seq,\n       c.constraint_name AS pk_name\nFROM all_cons_columns c, all_constraints k\nWHERE k.constraint_type = 'P'\n  AND k.table_name = :1\n  AND k.owner like :2 escape '/'\n  AND k.constraint_name = c.constraint_name \n  AND k.table_name = c.table_name \n  AND k.owner = c.owner \nORDER BY column_name\n");
        preparedStatement.setString(1, string3);
        preparedStatement.setString(2, string2 == null ? "%" : string2);
        preparedStatement.closeOnCompletion();
        OracleResultSet oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
        return oracleResultSet;
    }

    ResultSet keys_query(String string, String string2, String string3, String string4, String string5) throws SQLException {
        int n2 = 1;
        int n3 = string2 != null ? n2++ : 0;
        int n4 = string4 != null ? n2++ : 0;
        int n5 = string != null && string.length() > 0 ? n2++ : 0;
        int n6 = string3 != null && string3.length() > 0 ? n2++ : 0;
        PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT NULL AS pktable_cat,\n       p.owner as pktable_schem,\n       p.table_name as pktable_name,\n       pc.column_name as pkcolumn_name,\n       NULL as fktable_cat,\n       f.owner as fktable_schem,\n       f.table_name as fktable_name,\n       fc.column_name as fkcolumn_name,\n       fc.position as key_seq,\n       NULL as update_rule,\n       decode (f.delete_rule, 'CASCADE', 0, 'SET NULL', 2, 1) as delete_rule,\n       f.constraint_name as fk_name,\n       p.constraint_name as pk_name,\n       decode(f.deferrable,       'DEFERRABLE',5      ,'NOT DEFERRABLE',7      , 'DEFERRED', 6      ) deferrability \n      FROM all_cons_columns pc, all_constraints p,\n      all_cons_columns fc, all_constraints f\nWHERE 1 = 1\n" + (n3 != 0 ? "  AND p.table_name = :1\n" : "") + (n4 != 0 ? "  AND f.table_name = :2\n" : "") + (n5 != 0 ? "  AND p.owner = :3\n" : "") + (n6 != 0 ? "  AND f.owner = :4\n" : "") + "  AND f.constraint_type = 'R'\n  AND p.owner = f.r_owner\n  AND p.constraint_name = f.r_constraint_name\n  AND p.constraint_type = 'P'\n  AND pc.owner = p.owner\n  AND pc.constraint_name = p.constraint_name\n  AND pc.table_name = p.table_name\n  AND fc.owner = f.owner\n  AND fc.constraint_name = f.constraint_name\n  AND fc.table_name = f.table_name\n  AND fc.position = pc.position\n" + string5);
        if (n3 != 0) {
            preparedStatement.setString(n3, string2);
        }
        if (n4 != 0) {
            preparedStatement.setString(n4, string4);
        }
        if (n5 != 0) {
            preparedStatement.setString(n5, string);
        }
        if (n6 != 0) {
            preparedStatement.setString(n6, string3);
        }
        preparedStatement.closeOnCompletion();
        OracleResultSet oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
        return oracleResultSet;
    }

    @Override
    public ResultSet getImportedKeys(String string, String string2, String string3) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            ResultSet resultSet = this.keys_query(null, null, string2, string3, "ORDER BY pktable_schem, pktable_name, key_seq");
            return resultSet;
        }
    }

    @Override
    public ResultSet getExportedKeys(String string, String string2, String string3) throws SQLException {
        return this.keys_query(string2, string3, null, null, "ORDER BY fktable_schem, fktable_name, key_seq");
    }

    @Override
    public ResultSet getCrossReference(String string, String string2, String string3, String string4, String string5, String string6) throws SQLException {
        return this.keys_query(string2, string3, string5, string6, "ORDER BY fktable_schem, fktable_name, key_seq");
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    private static final String quoteDatabaseObjectName(String string) {
        if (string == null || string.length() == 0) {
            return "''";
        }
        assert (OracleSql.isValidObjectName(string)) : "n is invalid \"" + string + "\"";
        return (string.charAt(0) == '\"' ? "Q'" : "'") + string + "'";
    }

    @Override
    public ResultSet getIndexInfo(String string, String string2, String string3, boolean bl, boolean bl2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            OracleResultSet oracleResultSet;
            String string4;
            Statement statement = this.connection.createStatement();
            if (string2 != null && string2.length() != 0 && !OracleSql.isValidObjectName(string2) || string3 != null && string3.length() != 0 && !OracleSql.isValidObjectName(string3)) {
                throw (SQLException)DatabaseError.createSqlException(68).fillInStackTrace();
            }
            if (!bl2) {
                boolean bl3 = false;
                try {
                    bl3 = this.connection.getTransactionState().contains((Object)OracleConnection.TransactionState.TRANSACTION_STARTED);
                }
                catch (SQLException sQLException) {
                    // empty catch block
                }
                if (!bl3) {
                    string4 = "analyze table " + (string2 == null ? "" : string2 + ".") + string3 + " compute statistics";
                    statement.executeUpdate(string4);
                }
            }
            String string5 = "select null as table_cat,\n       owner as table_schem,\n       table_name,\n       0 as NON_UNIQUE,\n       null as index_qualifier,\n       null as index_name, 0 as type,\n       0 as ordinal_position, null as column_name,\n       null as asc_or_desc,\n       num_rows as cardinality,\n       blocks as pages,\n       null as filter_condition\nfrom all_tables\nwhere table_name = " + OracleDatabaseMetaData.quoteDatabaseObjectName(string3) + "\n";
            string4 = "";
            if (string2 != null && string2.length() > 0) {
                string4 = "  and owner = " + OracleDatabaseMetaData.quoteDatabaseObjectName(string2) + "\n";
            }
            String string6 = "select null as table_cat,\n       i.owner as table_schem,\n       i.table_name,\n       decode (i.uniqueness, 'UNIQUE', 0, 1),\n       null as index_qualifier,\n       i.index_name,\n       1 as type,\n       c.column_position as ordinal_position,\n       c.column_name,\n       null as asc_or_desc,\n       i.distinct_keys as cardinality,\n       i.leaf_blocks as pages,\n       null as filter_condition\nfrom all_indexes i, all_ind_columns c\nwhere i.table_name = " + OracleDatabaseMetaData.quoteDatabaseObjectName(string3) + "\n";
            String string7 = "";
            if (string2 != null && string2.length() > 0) {
                string7 = "  and i.owner = " + OracleDatabaseMetaData.quoteDatabaseObjectName(string2) + "\n";
            }
            String string8 = "";
            if (bl) {
                string8 = "  and i.uniqueness = 'UNIQUE'\n";
            }
            String string9 = "  and i.index_name = c.index_name\n  and i.table_owner = c.table_owner\n  and i.table_name = c.table_name\n  and i.owner = c.index_owner\n";
            String string10 = "order by non_unique, type, index_name, ordinal_position\n";
            String string11 = string5 + string4 + "union\n" + string6 + string7 + string8 + string9 + string10;
            statement.closeOnCompletion();
            OracleResultSet oracleResultSet2 = oracleResultSet = (OracleResultSet)statement.executeQuery(string11);
            return oracleResultSet2;
        }
    }

    SQLException fail() {
        SQLException sQLException = new SQLException("Not implemented yet");
        return sQLException;
    }

    @Override
    public boolean supportsResultSetType(int n2) throws SQLException {
        return true;
    }

    @Override
    public boolean supportsResultSetConcurrency(int n2, int n3) throws SQLException {
        return true;
    }

    @Override
    public boolean ownUpdatesAreVisible(int n2) throws SQLException {
        return n2 != 1003;
    }

    @Override
    public boolean ownDeletesAreVisible(int n2) throws SQLException {
        return n2 != 1003;
    }

    @Override
    public boolean ownInsertsAreVisible(int n2) throws SQLException {
        return false;
    }

    @Override
    public boolean othersUpdatesAreVisible(int n2) throws SQLException {
        return n2 == 1005;
    }

    @Override
    public boolean othersDeletesAreVisible(int n2) throws SQLException {
        return false;
    }

    @Override
    public boolean othersInsertsAreVisible(int n2) throws SQLException {
        return false;
    }

    @Override
    public boolean updatesAreDetected(int n2) throws SQLException {
        return false;
    }

    @Override
    public boolean deletesAreDetected(int n2) throws SQLException {
        return false;
    }

    @Override
    public boolean insertsAreDetected(int n2) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsBatchUpdates() throws SQLException {
        return true;
    }

    @Override
    public ResultSet getUDTs(String string, String string2, String string3, int[] nArray) throws SQLException {
        String[] stringArray;
        boolean bl = false;
        if (string3 == null || string3.length() == 0) {
            bl = false;
        } else if (nArray == null) {
            bl = true;
        } else {
            for (int i2 = 0; i2 < nArray.length; ++i2) {
                if (nArray[i2] != 2002) continue;
                bl = true;
                break;
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT NULL AS TYPE_CAT, owner AS TYPE_SCHEM, type_name, NULL AS CLASS_NAME, 'STRUCT' AS DATA_TYPE, NULL AS REMARKS FROM all_types ");
        if (bl) {
            stringBuffer.append("WHERE typecode = 'OBJECT' AND owner LIKE :1 ESCAPE '/' AND type_name LIKE :2 ESCAPE '/'");
        } else {
            stringBuffer.append("WHERE 1 = 2");
        }
        PreparedStatement preparedStatement = this.connection.prepareStatement(stringBuffer.substring(0, stringBuffer.length()));
        if (bl) {
            stringArray = new String[1];
            String[] stringArray2 = new String[1];
            if (SQLName.parse(string3, stringArray, stringArray2)) {
                preparedStatement.setString(1, stringArray[0]);
                preparedStatement.setString(2, stringArray2[0]);
            } else {
                if (string2 != null) {
                    preparedStatement.setString(1, string2);
                } else {
                    preparedStatement.setNull(1, 12);
                }
                preparedStatement.setString(2, string3);
            }
        }
        preparedStatement.closeOnCompletion();
        stringArray = (OracleResultSet)preparedStatement.executeQuery();
        return stringArray;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.connection.getWrapper();
    }

    @Override
    public boolean supportsSavepoints() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsNamedParameters() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsMultipleOpenResults() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws SQLException {
        return true;
    }

    @Override
    public ResultSet getSuperTypes(String string, String string2, String string3) throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    @Override
    public ResultSet getSuperTables(String string, String string2, String string3) throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    @Override
    public ResultSet getAttributes(String string, String string2, String string3, String string4) throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    @Override
    public boolean supportsResultSetHoldability(int n2) throws SQLException {
        return n2 == 1;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 1;
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
        return this.connection.getVersionNumber() / 1000;
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
        return this.connection.getVersionNumber() % 1000 / 100;
    }

    @Override
    public int getJDBCMajorVersion() throws SQLException {
        return DRIVER_API_MAJOR_VERSION;
    }

    @Override
    public int getJDBCMinorVersion() throws SQLException {
        return DRIVER_API_MINOR_VERSION;
    }

    @Override
    public int getSQLStateType() throws SQLException {
        return 0;
    }

    @Override
    public boolean locatorsUpdateCopy() throws SQLException {
        return true;
    }

    @Override
    public boolean supportsStatementPooling() throws SQLException {
        return true;
    }

    public static String getDriverNameInfo() throws SQLException {
        return DRIVER_NAME;
    }

    public static String getDriverVersionInfo() throws SQLException {
        return DRIVER_VERSION;
    }

    public static int getDriverMajorVersionInfo() {
        return DRIVER_MAJOR_VERSION;
    }

    public static int getDriverMinorVersionInfo() {
        return DRIVER_MINOR_VERSION;
    }

    public static String getLobPrecision() throws SQLException {
        return "-1";
    }

    @Override
    public long getLobMaxLength() throws SQLException {
        return this.connection.getVersionNumber() >= 10000 ? Long.MAX_VALUE : LOB_MAXLENGTH_32BIT;
    }

    @Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        return RowIdLifetime.ROWID_VALID_FOREVER;
    }

    @Override
    public ResultSet getSchemas(String string, String string2) throws SQLException {
        if (string2 == null) {
            return this.getSchemas();
        }
        String string3 = "SELECT username AS table_schem, null as table_catalog FROM all_users WHERE username LIKE ? ORDER BY table_schem";
        PreparedStatement preparedStatement = this.connection.prepareStatement(string3);
        preparedStatement.setString(1, string2);
        preparedStatement.closeOnCompletion();
        OracleResultSet oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
        return oracleResultSet;
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        return true;
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        return false;
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        Statement statement = this.connection.createStatement();
        return statement.executeQuery("select NULL NAME, -1 MAX_LEN, NULL DEFAULT_VALUE, NULL DESCRIPTION  from dual where 0 = 1");
    }

    @Override
    public ResultSet getFunctions(String string, String string2, String string3) throws SQLException {
        String string4 = "SELECT\n  -- Standalone functions\n  NULL AS function_cat,\n  owner AS function_schem,\n  object_name AS function_name,\n  'Standalone function' AS remarks,\n  0 AS function_type,\n  NULL AS specific_name\nFROM all_objects\nWHERE object_type = 'FUNCTION'\n  AND owner LIKE :1 ESCAPE '/'\n  AND object_name LIKE :2 ESCAPE '/'\n";
        String string5 = "SELECT\n  -- Packaged functions\n  package_name AS function_cat,\n  owner AS function_schem,\n  object_name AS function_name,\n  'Packaged function' AS remarks,\n  decode (data_type, 'TABLE', 2, 'PL/SQL TABLE', 2, 1) AS function_type,\n  NULL AS specific_name\nFROM all_arguments\nWHERE argument_name IS NULL\n  AND in_out = 'OUT'\n  AND data_level = 0\n";
        String string6 = "  AND package_name LIKE :3 ESCAPE '/'\n  AND owner LIKE :4 ESCAPE '/'\n  AND object_name LIKE :5 ESCAPE '/'\n";
        String string7 = "  AND package_name IS NOT NULL\n  AND owner LIKE :6 ESCAPE '/'\n  AND object_name LIKE :7 ESCAPE '/'\n";
        String string8 = "ORDER BY function_schem, function_name\n";
        PreparedStatement preparedStatement = null;
        String string9 = null;
        String string10 = string2;
        if (string2 == null) {
            string10 = "%";
        } else if (string2.equals("")) {
            string10 = this.getUserName().toUpperCase();
        }
        String string11 = string3;
        if (string3 == null) {
            string11 = "%";
        } else if (string3.equals("")) {
            throw (SQLException)DatabaseError.createSqlException(74).fillInStackTrace();
        }
        if (string == null) {
            string9 = string4 + "UNION ALL " + string5 + string7 + string8;
            preparedStatement = this.connection.prepareStatement(string9);
            preparedStatement.setString(1, string10);
            preparedStatement.setString(2, string11);
            preparedStatement.setString(3, string10);
            preparedStatement.setString(4, string11);
        } else if (string.equals("")) {
            string9 = string4;
            preparedStatement = this.connection.prepareStatement(string9);
            preparedStatement.setString(1, string10);
            preparedStatement.setString(2, string11);
        } else {
            string9 = string5 + string6 + string8;
            preparedStatement = this.connection.prepareStatement(string9);
            preparedStatement.setString(1, string);
            preparedStatement.setString(2, string10);
            preparedStatement.setString(3, string11);
        }
        preparedStatement.closeOnCompletion();
        OracleResultSet oracleResultSet = (OracleResultSet)preparedStatement.executeQuery();
        return oracleResultSet;
    }

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        if (clazz.isInterface()) {
            return clazz.isInstance(this);
        }
        throw (SQLException)DatabaseError.createSqlException(177).fillInStackTrace();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface() && clazz.isInstance(this)) {
            return (T)this;
        }
        throw (SQLException)DatabaseError.createSqlException(177).fillInStackTrace();
    }

    protected boolean hasSqlWildcard(String string) {
        if (sqlWildcardPattern == null) {
            sqlWildcardPattern = Pattern.compile(sqlWildcardRegex);
        }
        Matcher matcher = sqlWildcardPattern.matcher(string);
        return matcher.find();
    }

    protected String stripSqlEscapes(String string) {
        if (sqlEscapePattern == null) {
            sqlEscapePattern = Pattern.compile(sqlEscapeRegex);
        }
        Matcher matcher = sqlEscapePattern.matcher(string);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, "");
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    String getUnpackagedProcedureColumnsNoWildcardsPlsql(String string) throws SQLException {
        String string2 = "declare\n  in_owner varchar2(32) := null;\n  in_name varchar2(32) := null;\n  my_user_name varchar2(32) := null;\n  cnt number := 0;\n  temp_owner varchar2(32) := null;\n  temp_name  varchar2(32):= null;\n  out_owner varchar2(32) := null;\n  out_name  varchar2(32):= null;\n  loc varchar2(32) := null;\n  status number := 0;\n  TYPE recursion_check_type is table of number index by varchar2(65);\n  recursion_check recursion_check_type;\n  dotted_name varchar2(65);\n  recursion_cnt number := 0;\n  xxx SYS_REFCURSOR;\nbegin\n  in_owner := ?;\n  in_name := ?;\n  select user into my_user_name from dual;\n  if( my_user_name = in_owner ) then\n    select count(*) into cnt from user_procedures where object_name = in_name;\n    if( cnt = 1 ) then\n      out_owner := in_owner;\n      out_name := in_name;\n      loc := 'USER_PROCEDURES';\n    end if;\n  else\n    select count(*) into cnt from all_arguments where owner = in_owner and object_name = in_name;\n    if( cnt >= 1 ) then\n      out_owner := in_owner;\n      out_name := in_name;\n      loc := 'ALL_ARGUMENTS';\n    end if;\n  end if;\n  if loc is null then\n    temp_owner := in_owner;\n    temp_name := in_name;\n    loop\n      begin\n        dotted_name := temp_owner || '.' ||temp_name;\n        begin\n          recursion_cnt := recursion_check(dotted_name );\n          status := -1;\n          exit;\n        exception\n        when NO_DATA_FOUND then\n          recursion_check( dotted_name ) := 1;\n        end;\n        select table_owner, table_name into out_owner, out_name from all_synonyms \n          where  owner = temp_owner and synonym_name = temp_name;\n        cnt := cnt + 1;\n        temp_owner  := out_owner;\n        temp_name := out_name;\n        exception\n        when NO_DATA_FOUND then\n          exit;\n        end;\n      end loop;\n      if( not(out_owner is null) ) then\n        loc := 'ALL_SYNONYMS';\n    end if;\n  end if;\n";
        short s2 = this.connection.getVersionNumber();
        String string3 = "if( status = 0 ) then \n open xxx for \n";
        String string4 = "SELECT package_name AS procedure_cat,\n       owner AS procedure_schem,\n       object_name AS procedure_name,\n       argument_name AS column_name,\n       DECODE(position, 0, 5,\n                        DECODE(in_out, 'IN', 1,\n                                       'OUT', 4,\n                                       'IN/OUT', 2,\n                                       0)) AS column_type,\n" + this.datatypeQuery(DataTypeSource.ARGS) + "       DECODE(data_type, 'OBJECT', type_owner || '.' || type_name,               data_type) AS type_name,\n       DECODE (data_precision, NULL, data_length,\n                               data_precision) AS precision,\n       data_length AS length,\n       data_scale AS scale,\n       10 AS radix,\n       1 AS nullable,\n       NULL AS remarks,\n       default_value AS column_def,\n       NULL as sql_data_type,\n       NULL AS sql_datetime_sub,\n       DECODE(data_type,\n                         'CHAR', 32767,\n                         'VARCHAR2', 32767,\n                         'LONG', 32767,\n                         'RAW', 32767,\n                         'LONG RAW', 32767,\n                         NULL) AS char_octet_length,\n       (sequence - 1) AS ordinal_position,\n       'YES' AS is_nullable,\n       NULL AS specific_name,\n       sequence,\n       overload,\n       default_value\n";
        String string5 = "FROM all_arguments a";
        String string6 = "WHERE a.owner = out_owner \n AND a.object_name = out_name\n AND data_level = 0\n AND package_name is null\n";
        String string7 = string == null || string.equals("%") ? " AND (argument_name LIKE ? ESCAPE '/'\n      OR (argument_name IS NULL\n          AND data_type IS NOT NULL))\n" : " AND a.argument_name LIKE ? ESCAPE '/'\n";
        String string8 = "ORDER BY procedure_schem, procedure_name, overload, sequence\n";
        String string9 = string3;
        string9 = string9 + string4;
        string9 = string9 + string5;
        string9 = string9 + "\n" + string6;
        string9 = string9 + "\n" + string7;
        string9 = string9 + "\n" + string8;
        String string10 = "; \n end if;\n  ? := xxx; ? := status;\n end;";
        String string11 = string2 + string9 + string10;
        return string11;
    }

    String getPackagedProcedureColumnsNoWildcardsPlsql(String string) throws SQLException {
        String string2 = "declare\n  in_package_name varchar2(32) := null;\n  in_owner varchar2(32) := null;\n  in_name varchar2(32) := null;\n  my_user_name varchar2(32) := null;\n  cnt number := 0;\n  temp_package_name varchar2(32) := null;\n  temp_owner varchar2(32) := null;\n  out_package_name varchar2(32) := null;\n  out_owner varchar2(32) := null;\n  loc varchar2(32) := null;\n  status number := 0;\n  TYPE recursion_check_type is table of number index by varchar2(65);\n  recursion_check recursion_check_type;\n  dotted_name varchar2(65);\n  recursion_cnt number := 0;\n  xxx SYS_REFCURSOR;\nbegin\n  in_package_name := ?;\n  in_owner := ?;\n  in_name := ?;\n  select user into my_user_name from dual;\n  if( in_owner is null ) then\n    select count(*) into cnt from all_arguments where package_name = in_package_name;\n    if( cnt >= 1 ) then\n      out_owner := '%';\n      out_package_name := in_package_name;\n      loc := 'ALL_ARGUMENTS';\n    end if;\n  elsif( my_user_name = in_owner ) then\n    select count(*) into cnt from user_arguments where package_name = in_package_name;\n    if( cnt >= 1 ) then\n      out_owner := in_owner;\n      out_package_name := in_package_name;\n      loc := 'USER_ARGUMENTS';\n    end if;\n  else\n    select count(*) into cnt from all_arguments where owner = in_owner and package_name = in_package_name;\n    if( cnt >= 1 ) then\n      out_owner := in_owner;\n      out_package_name := in_package_name;\n      loc := 'ALL_ARGUMENTS';\n    end if;\n  end if;\n  if loc is null then\n  temp_owner := in_owner;\n  temp_package_name := in_package_name;\n  loop\n    begin\n      dotted_name := temp_owner || '.' ||temp_package_name;\n      begin\n        recursion_cnt := recursion_check(dotted_name );\n        status := -1;\n        exit;\n      exception\n      when NO_DATA_FOUND then\n        recursion_check( dotted_name ) := 1;\n      end;\n      select table_owner, table_name into out_owner, out_package_name from all_synonyms \n        where  owner = temp_owner and synonym_name = temp_package_name;\n      cnt := cnt + 1;\n      temp_owner  := out_owner;\n      temp_package_name := out_package_name;\n      exception\n      when NO_DATA_FOUND then\n        exit;\n      end;\n    end loop;\n    if( not(out_owner is null) ) then\n      loc := 'ALL_SYNONYMS';\n    end if;\n  end if;\n";
        short s2 = this.connection.getVersionNumber();
        String string3 = "if( status = 0 ) then \n open xxx for \n";
        String string4 = "SELECT package_name AS procedure_cat,\n       owner AS procedure_schem,\n       object_name AS procedure_name,\n       argument_name AS column_name,\n       DECODE(position, 0, 5,\n                        DECODE(in_out, 'IN', 1,\n                                       'OUT', 4,\n                                       'IN/OUT', 2,\n                                       0)) AS column_type,\n" + this.datatypeQuery(DataTypeSource.ARGS) + "       DECODE(data_type, 'OBJECT', type_owner || '.' || type_name,               data_type) AS type_name,\n       DECODE (data_precision, NULL, data_length,\n                               data_precision) AS precision,\n       data_length AS length,\n       data_scale AS scale,\n       10 AS radix,\n       1 AS nullable,\n       NULL AS remarks,\n       default_value AS column_def,\n       NULL as sql_data_type,\n       NULL AS sql_datetime_sub,\n       DECODE(data_type,\n                         'CHAR', 32767,\n                         'VARCHAR2', 32767,\n                         'LONG', 32767,\n                         'RAW', 32767,\n                         'LONG RAW', 32767,\n                         NULL) AS char_octet_length,\n       (sequence - 1) AS ordinal_position,\n       'YES' AS is_nullable,\n       NULL AS specific_name,\n       sequence,\n       overload,\n       default_value\n";
        String string5 = "FROM all_arguments a";
        String string6 = "WHERE a.owner LIKE out_owner \n  AND a.object_name LIKE in_name ESCAPE '/' \n AND data_level = 0\n AND package_name = out_package_name\n";
        String string7 = string == null || string.equals("%") ? " AND (argument_name LIKE ? ESCAPE '/'\n      OR (argument_name IS NULL\n          AND data_type IS NOT NULL))\n" : " AND a.argument_name LIKE ? ESCAPE '/'\n";
        String string8 = "ORDER BY procedure_schem, procedure_name, overload, sequence\n";
        String string9 = string3;
        string9 = string9 + string4;
        string9 = string9 + string5;
        string9 = string9 + "\n" + string6;
        string9 = string9 + "\n" + string7;
        string9 = string9 + "\n" + string8;
        String string10 = "; \n end if;\n  ? := xxx; ? := status;\n end;";
        String string11 = string2 + string9 + string10;
        return string11;
    }

    @Override
    public OracleTypeMetaData getOracleTypeMetaData(String string) throws SQLException {
        return TypeDescriptor.getTypeDescriptor(string, this.connection);
    }

    protected String datatypeQuery(DataTypeSource dataTypeSource) {
        return this.datatypeQuery(dataTypeSource, null);
    }

    protected String datatypeQuery(DataTypeSource dataTypeSource, String string) {
        String string2 = "";
        String string3 = "";
        if (string != null && string != "") {
            string2 = string + ".";
        }
        if (dataTypeSource == DataTypeSource.COLS) {
            string3 = "       AND ((a.owner IS NULL AND \n" + string2 + "data_type_owner IS NULL)\n         OR (a.owner = " + string2 + "data_type_owner))\n";
        }
        String string4 = "  DECODE(substr(" + string2 + "data_type, 1, 9), \n    'TIMESTAMP', \n      DECODE(substr(" + string2 + "data_type, 10, 1), \n        '(', \n          DECODE(substr(" + string2 + "data_type, 19, 5), \n            'LOCAL', -102, 'TIME ', -101, 93), \n        DECODE(substr(" + string2 + "data_type, 16, 5), \n          'LOCAL', -102, 'TIME ', -101, 93)), \n    'INTERVAL ', \n      DECODE(substr(" + string2 + "data_type, 10, 3), \n       'DAY', -104, 'YEA', -103), \n    DECODE(" + string2 + "data_type, \n      'BINARY_DOUBLE', 101, \n      'BINARY_FLOAT', 100, \n      'BFILE', -13, \n      'BLOB', 2004, \n      'CHAR', 1, \n      'CLOB', 2005, \n      'COLLECTION', 2003, \n      'DATE', " + (this.connection.getMapDateToTimestamp() ? "93" : "91") + ", \n      'FLOAT', 6, \n      'LONG', -1, \n      'LONG RAW', -4, \n      'NCHAR', -15, \n      'NCLOB', 2011, \n      'NUMBER', 2, \n      'NVARCHAR', -9, \n      'NVARCHAR2', -9, \n      'OBJECT', 2002, \n      'OPAQUE/XMLTYPE', 2009, \n      'RAW', -3, \n      'REF', 2006, \n      'ROWID', -8, \n      'SQLXML', 2009, \n      'UROWID', -8, \n      'VARCHAR2', 12, \n      'VARRAY', 2003, \n      'XMLTYPE', 2009, \n      DECODE((SELECT a.typecode \n        FROM ALL_TYPES a \n        WHERE a.type_name = " + string2 + "data_type\n" + string3 + "        ), \n        'OBJECT', 2002, \n        'COLLECTION', 2003, 1111))) \n AS data_type,\n";
        return string4;
    }

    @Override
    public ResultSet getPseudoColumns(String string, String string2, String string3, String string4) throws SQLException {
        String string5;
        String string6 = string5 = string2 == null ? "%" : string2;
        if (string3 != null) {
            if (string3.startsWith("\"") && string3.endsWith("\"")) {
                string3 = string3.substring(1, string3.length() - 1);
            }
            if (string3.indexOf(39) != -1) {
                string3 = string3.replaceAll("'", "''");
            }
        }
        String string7 = this.pseudoColumnRow(string5, string3, "ORA_ROWSCN", 2, 10) + "union\n" + this.pseudoColumnRow(string5, string3, "ROWID", -8, 18) + "union\n" + this.pseudoColumnRow(string5, string3, "ROWNUM", 2, 10) + "union\n" + this.pseudoColumnRealRows(string5, string3);
        String string8 = string4 == null ? "%" : string4;
        String string9 = "select *\nfrom (" + string7 + ")\nwhere column_name like '" + string8 + "'\norder by column_name";
        PreparedStatement preparedStatement = this.connection.prepareStatement(string9);
        preparedStatement.closeOnCompletion();
        return preparedStatement.executeQuery();
    }

    private String pseudoColumnRealRows(String string, String string2) throws SQLException {
        String string3 = " DECODE (data_type, 'CHAR', char_length, 'VARCHAR', char_length, 'VARCHAR2', char_length, 'NVARCHAR2', char_length, 'NCHAR', char_length, data_length)";
        String string4 = "select null as TABLE_CAT,\n  owner as TABLE_SCHEM,\n  table_name as TABLE_NAME,\n  column_name  as COLUMN_NAME,\n" + this.datatypeQuery(DataTypeSource.COLS) + " DECODE (data_precision, null, " + string3 + ", data_precision)\n  as COLUMN_SIZE,\n  data_scale as DECIMAL_DIGITS,\n  10 as NUM_PREC_RADIX,\n  'NO_USAGE_RESTRICTONS' as COLUMN_USAGE,\n  null as REMARKS,\n  null as CHAR_OCTET_LENGTH,\n  decode(nullable, 'Y', 'YES', 'NO') as IS_NULLABLE\nfrom all_tab_cols\nwhere\n  hidden_column = 'YES' and\n  owner like '" + string + "' and\n  table_name like '" + string2 + "' ESCAPE '/'\n";
        return string4;
    }

    private String pseudoColumnRow(String string, String string2, String string3, int n2, int n3) throws SQLException {
        String string4 = "  select null as TABLE_CAT,\n  owner as TABLE_SCHEM,\n  table_name as TABLE_NAME,\n  '" + string3 + "' as COLUMN_NAME,\n  " + n2 + " as DATA_TYPE,\n  " + n3 + " as COLUMN_SIZE,\n  " + (string3.equals("ROWID") ? "null" : "38") + " as DECIMAL_DIGITS,\n  10 as NUM_PREC_RADIX,\n  'NO_USAGE_RESTRICTONS' as COLUMN_USAGE,\n  null as REMARKS,\n  null as CHAR_OCTET_LENGTH,\n  'NO' as IS_NULLABLE\nfrom all_tables\nwhere\n  owner like '" + string + "' and\n  table_name like '" + string2 + "' ESCAPE '/'\n";
        return string4;
    }

    @Override
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        return false;
    }

    @Override
    public String getAuditBanner() throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    @Override
    public String getAccessBanner() throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    @Override
    public boolean isServerBigSCN() throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
    }

    public boolean isCompatible122OrGreater() throws SQLException {
        throw (SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace();
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
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }

    protected static enum DataTypeSource {
        COLS,
        ARGS;

    }
}

