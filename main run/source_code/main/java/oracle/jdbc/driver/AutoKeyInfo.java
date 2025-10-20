/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OracleResultSetMetaData;
import oracle.jdbc.driver.OracleSql;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class AutoKeyInfo
extends OracleResultSetMetaData {
    String originalSql;
    String newSql;
    String tableName;
    OracleStatement.SqlKind sqlKind = OracleStatement.SqlKind.UNINITIALIZED;
    int sqlParserParamCount;
    String[] sqlParserParamList;
    boolean useNamedParameter;
    int current_argument;
    String[] columnNames;
    int[] columnIndexes;
    int numColumns;
    String[] tableColumnNames;
    int[] tableColumnTypes;
    int[] tableMaxLengths;
    boolean[] tableNullables;
    short[] tableFormOfUses;
    int[] tablePrecisions;
    int[] tableScales;
    String[] tableTypeNames;
    int autoKeyType;
    static final int KEYFLAG = 0;
    static final int COLUMNAME = 1;
    static final int COLUMNINDEX = 2;
    static final char QMARK = '?';
    int[] returnTypes;
    private boolean isInitialized = false;
    Accessor[] returnAccessors;
    private static final ThreadLocal<OracleSql> SQL_PARSER = new ThreadLocal<OracleSql>(){

        @Override
        protected OracleSql initialValue() {
            return new OracleSql(null);
        }
    };

    AutoKeyInfo(String string) {
        this.originalSql = string;
        this.autoKeyType = 0;
        this.returnTypes = new int[1];
        this.returnTypes[0] = 104;
        this.isInitialized = true;
    }

    AutoKeyInfo(String string, String[] stringArray) {
        this.originalSql = string;
        this.columnNames = stringArray;
        this.autoKeyType = 1;
    }

    AutoKeyInfo(String string, int[] nArray) {
        this.originalSql = string;
        this.columnIndexes = nArray;
        this.autoKeyType = 2;
    }

    private void parseSql() throws SQLException {
        if (this.originalSql == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        OracleSql oracleSql = SQL_PARSER.get();
        oracleSql.initialize(this.originalSql);
        this.sqlKind = oracleSql.getSqlKind();
        if (this.sqlKind == OracleStatement.SqlKind.INSERT) {
            this.sqlParserParamCount = oracleSql.getParameterCount();
            this.sqlParserParamList = oracleSql.getParameterList();
            if (this.sqlParserParamList == OracleSql.EMPTY_LIST) {
                this.useNamedParameter = false;
            } else {
                this.useNamedParameter = true;
                this.current_argument = this.sqlParserParamCount;
            }
        }
    }

    private String generateUniqueNamedParameter() {
        String string;
        boolean bl;
        block0: do {
            bl = false;
            string = Integer.toString(++this.current_argument).intern();
            for (int i2 = 0; i2 < this.sqlParserParamCount; ++i2) {
                if (this.sqlParserParamList[i2] != string) continue;
                bl = true;
                continue block0;
            }
        } while (bl);
        return ":" + string;
    }

    String getNewSql() throws SQLException {
        try {
            if (this.newSql != null) {
                return this.newSql;
            }
            if (this.sqlKind == OracleStatement.SqlKind.UNINITIALIZED) {
                this.parseSql();
            }
            switch (this.autoKeyType) {
                case 0: {
                    this.newSql = this.originalSql + " RETURNING ROWID INTO " + (this.useNamedParameter ? this.generateUniqueNamedParameter() : Character.valueOf('?'));
                    break;
                }
                case 1: {
                    this.getNewSqlByColumnName();
                    break;
                }
                case 2: {
                    this.getNewSqlByColumnIndexes();
                }
            }
            this.sqlKind = OracleStatement.SqlKind.UNINITIALIZED;
            this.sqlParserParamList = null;
            return this.newSql;
        }
        catch (Exception exception) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), exception).fillInStackTrace();
        }
    }

    boolean isInitialized() {
        return this.isInitialized;
    }

    void initialize(PhysicalConnection physicalConnection) throws SQLException {
        if (!this.isInitialized) {
            physicalConnection.doDescribeTable(this);
            this.setInitialized();
        }
    }

    private void setInitialized() throws SQLException {
        if (this.autoKeyType == 1) {
            this.initializeIndexesAndTypesByName();
        }
        this.isInitialized = true;
    }

    private void initializeIndexesAndTypesByName() throws SQLException {
        this.returnTypes = new int[this.columnNames.length];
        this.columnIndexes = new int[this.columnNames.length];
        for (int i2 = 0; i2 < this.columnNames.length; ++i2) {
            int n2;
            this.returnTypes[i2] = n2 = this.getReturnParamTypeCode(i2, this.columnNames[i2], this.columnIndexes);
        }
    }

    private String getNewSqlByColumnName() throws SQLException {
        int n2;
        StringBuffer stringBuffer = new StringBuffer(this.originalSql);
        stringBuffer.append(" RETURNING ");
        for (n2 = 0; n2 < this.columnNames.length; ++n2) {
            stringBuffer.append(this.columnNames[n2]);
            if (n2 >= this.columnNames.length - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(" INTO ");
        for (n2 = 0; n2 < this.columnNames.length - 1; ++n2) {
            stringBuffer.append((this.useNamedParameter ? this.generateUniqueNamedParameter() : Character.valueOf('?')) + ", ");
        }
        stringBuffer.append(this.useNamedParameter ? this.generateUniqueNamedParameter() : Character.valueOf('?'));
        this.newSql = new String(stringBuffer);
        return this.newSql;
    }

    private String getNewSqlByColumnIndexes() throws SQLException {
        int n2;
        this.returnTypes = new int[this.columnIndexes.length];
        StringBuffer stringBuffer = new StringBuffer(this.originalSql);
        stringBuffer.append(" RETURNING ");
        for (n2 = 0; n2 < this.columnIndexes.length; ++n2) {
            int n3 = this.columnIndexes[n2] - 1;
            if (n3 < 0 || n3 > this.tableColumnNames.length) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, null, DatabaseError.createSqlException(3, Integer.toString(n3)).fillInStackTrace()).fillInStackTrace();
            }
            int n4 = this.tableColumnTypes[n3];
            String string = this.tableColumnNames[n3];
            this.returnTypes[n2] = n4;
            stringBuffer.append(string.contains(" ") ? String.format("\"%s\"", string) : string);
            if (n2 >= this.columnIndexes.length - 1) continue;
            stringBuffer.append(", ");
        }
        stringBuffer.append(" INTO ");
        for (n2 = 0; n2 < this.columnIndexes.length - 1; ++n2) {
            stringBuffer.append((this.useNamedParameter ? this.generateUniqueNamedParameter() : Character.valueOf('?')) + ", ");
        }
        stringBuffer.append(this.useNamedParameter ? this.generateUniqueNamedParameter() : Character.valueOf('?'));
        this.newSql = new String(stringBuffer);
        return this.newSql;
    }

    private final int getReturnParamTypeCode(int n2, String string, int[] nArray) throws SQLException {
        string = string.replace("\"", "");
        for (int i2 = 0; i2 < this.tableColumnNames.length; ++i2) {
            if (!string.equalsIgnoreCase(this.tableColumnNames[i2])) continue;
            nArray[n2] = i2 + 1;
            return this.tableColumnTypes[i2];
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, null, DatabaseError.createSqlException(6, string).fillInStackTrace()).fillInStackTrace();
    }

    final boolean isInsertSqlStmt() throws SQLException {
        if (this.sqlKind == OracleStatement.SqlKind.UNINITIALIZED) {
            this.parseSql();
        }
        return this.sqlKind == OracleStatement.SqlKind.INSERT;
    }

    final boolean isUpdateSqlStmt() throws SQLException {
        if (this.sqlKind == OracleStatement.SqlKind.UNINITIALIZED) {
            this.parseSql();
        }
        return this.sqlKind == OracleStatement.SqlKind.UPDATE;
    }

    final boolean isInsertOrUpdateSqlStmt() throws SQLException {
        if (this.sqlKind == OracleStatement.SqlKind.UNINITIALIZED) {
            this.parseSql();
        }
        return this.sqlKind == OracleStatement.SqlKind.INSERT || this.sqlKind == OracleStatement.SqlKind.UPDATE;
    }

    String getTableName() throws SQLException {
        if (this.tableName != null) {
            return this.tableName;
        }
        if (this.isUpdateSqlStmt()) {
            return this.getTableNameForUpdateStmt(this.originalSql);
        }
        if (this.isInsertSqlStmt()) {
            return this.getTableNameForInsertStmt(this.originalSql);
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
    }

    private String getTableNameForInsertStmt(String string) throws SQLException {
        String string2 = string.trim();
        String string3 = string2.toUpperCase();
        int n2 = string3.indexOf("INSERT");
        if ((n2 = string3.indexOf("INTO", n2)) < 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string4 = string2.substring(n2 + 5).trim();
        this.tableName = this.extractTablename(string4);
        return this.tableName;
    }

    private String getTableNameForUpdateStmt(String string) throws SQLException {
        String string2 = string.trim();
        String string3 = string2.toUpperCase();
        int n2 = string3.indexOf("UPDATE");
        if (n2 != 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        String string4 = string2.substring(6).trim();
        this.tableName = this.extractTablename(string4);
        return this.tableName;
    }

    private String extractTablename(String string) throws SQLException {
        int n2;
        int n3 = string.length();
        for (n2 = 0; n2 < n3 && string.charAt(n2) == ' '; ++n2) {
        }
        if (n2 >= n3) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        int n4 = 0;
        String string2 = null;
        String string3 = null;
        boolean bl = false;
        do {
            if (string.charAt(n2) == '.') {
                if (string2 != null) {
                    ++n2;
                } else {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
                }
            }
            if (string.charAt(n2) == '\"') {
                n4 = n2 + 1;
                if (n2 >= (n4 = string.indexOf(34, n4) + 1) - 2) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
                }
                if (string2 == null) {
                    string2 = string.substring(n2, n4);
                    n2 = n4;
                    if (string.charAt(n2) == '.') continue;
                    bl = true;
                    continue;
                }
                string3 = string.substring(n2, n4);
                bl = true;
                continue;
            }
            for (n4 = n2; n4 < n3 && string.charAt(n4) != ' ' && string.charAt(n4) != '(' && string.charAt(n4) != '.'; ++n4) {
            }
            if (n2 >= n4) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
            }
            if (string2 == null) {
                string2 = string.substring(n2, n4);
                n2 = n4;
                if (string.charAt(n2) == '.') continue;
                bl = true;
                continue;
            }
            string3 = string.substring(n2, n4);
            bl = true;
        } while (!bl);
        if (string3 == null) {
            return string2;
        }
        return string2 + "." + string3;
    }

    void allocateSpaceForDescribedData(int n2) throws SQLException {
        this.numColumns = n2;
        this.tableColumnNames = new String[n2];
        this.tableColumnTypes = new int[n2];
        this.tableMaxLengths = new int[n2];
        this.tableNullables = new boolean[n2];
        this.tableFormOfUses = new short[n2];
        this.tablePrecisions = new int[n2];
        this.tableScales = new int[n2];
        this.tableTypeNames = new String[n2];
    }

    void fillDescribedData(int n2, String string, int n3, int n4, boolean bl, short s2, int n5, int n6, String string2) throws SQLException {
        this.tableColumnNames[n2] = string;
        this.tableColumnTypes[n2] = n3;
        this.tableMaxLengths[n2] = n4;
        this.tableNullables[n2] = bl;
        this.tableFormOfUses[n2] = s2;
        this.tablePrecisions[n2] = n5;
        this.tableScales[n2] = n6;
        this.tableTypeNames[n2] = string2;
    }

    void initMetaData(OracleResultSet oracleResultSet) throws SQLException {
        if (this.returnAccessors != null) {
            return;
        }
        int n2 = oracleResultSet.getOracleStatement().numReturnParams;
        if (n2 == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        this.returnAccessors = new Accessor[n2];
        Accessor[] accessorArray = oracleResultSet.getOracleStatement().accessors;
        int n3 = oracleResultSet.getOracleStatement().numberOfBindPositions;
        int n4 = 0;
        for (int i2 = 0; i2 < n3; ++i2) {
            Accessor accessor = accessorArray[i2];
            if (accessor == null) continue;
            this.returnAccessors[n4++] = accessor;
        }
        switch (this.autoKeyType) {
            case 0: {
                this.initMetaDataKeyFlag();
                break;
            }
            case 1: 
            case 2: {
                this.initMetaDataColumnIndexes();
            }
        }
    }

    void initMetaDataKeyFlag() throws SQLException {
        this.returnAccessors[0].columnName = "ROWID";
        this.returnAccessors[0].describeType = 104;
        this.returnAccessors[0].describeMaxLength = 4;
        this.returnAccessors[0].nullable = true;
        this.returnAccessors[0].precision = 0;
        this.returnAccessors[0].scale = 0;
        this.returnAccessors[0].formOfUse = 0;
    }

    void initMetaDataColumnIndexes() throws SQLException {
        for (int i2 = 0; i2 < this.returnAccessors.length; ++i2) {
            Accessor accessor = this.returnAccessors[i2];
            int n2 = this.columnIndexes[i2] - 1;
            accessor.columnName = this.tableColumnNames[n2];
            accessor.describeType = this.tableColumnTypes[n2];
            accessor.describeMaxLength = this.tableMaxLengths[n2];
            accessor.nullable = this.tableNullables[n2];
            accessor.precision = this.tablePrecisions[n2];
            accessor.scale = this.tablePrecisions[n2];
            accessor.formOfUse = this.tableFormOfUses[n2];
        }
    }

    @Override
    int getValidColumnIndex(int n2) throws SQLException {
        this.checkColumnIndex(n2);
        return n2 - 1;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return this.returnAccessors.length;
    }

    @Override
    public String getColumnName(int n2) throws SQLException {
        this.checkColumnIndex(n2);
        return this.returnAccessors[n2 - 1].columnName;
    }

    @Override
    public String getTableName(int n2) throws SQLException {
        this.checkColumnIndex(n2);
        return this.getTableName();
    }

    @Override
    Accessor[] getDescription() throws SQLException {
        return this.returnAccessors;
    }

    private void checkColumnIndex(int n2) throws SQLException {
        if (n2 <= 0 || n2 > this.returnAccessors.length) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 3, Integer.toString(n2)).fillInStackTrace();
        }
    }
}

