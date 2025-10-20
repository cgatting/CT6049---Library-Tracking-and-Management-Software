/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oracle.jdbc.driver.DBConversion;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OldUpdatableResultSet;
import oracle.jdbc.driver.OracleParameterMetaDataParser;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OracleSqlReadOnly;
import oracle.jdbc.driver.UpdatableResultSet;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
public class OracleSql {
    static final int UNINITIALIZED = -1;
    static final String[] EMPTY_LIST = new String[0];
    static final int MAX_IDENTIFIER_LENGTH = 258;
    static final Pattern CONNECTION_VALIDATION_SQL_PATTERN = Pattern.compile("\\A\\s*/\\*\\+\\s*CLIENT_CONNECTION_VALIDATION\\s*\\*/.*", 2);
    DBConversion conversion;
    String originalSql;
    String parameterSql;
    String utickSql;
    String processedSql;
    String rowidSql;
    String actualSql;
    byte[] sqlBytes;
    OracleStatement.SqlKind sqlKind = OracleStatement.SqlKind.UNINITIALIZED;
    byte sqlKindByte = (byte)-1;
    int parameterCount = -1;
    int returningIntoParameterCount = -1;
    boolean currentConvertNcharLiterals = true;
    boolean currentProcessEscapes = true;
    boolean includeRowid = false;
    String[] parameterList = EMPTY_LIST;
    char[] currentParameter = null;
    int bindParameterCount = -1;
    String[] bindParameterList = null;
    int cachedBindParameterCount = -1;
    String[] cachedBindParameterList = null;
    String cachedParameterSql;
    String cachedUtickSql;
    String cachedProcessedSql;
    String cachedRowidSql;
    String cachedActualSql;
    byte[] cachedSqlBytes;
    int selectEndIndex = -1;
    int orderByStartIndex = -1;
    int orderByEndIndex = -1;
    int whereStartIndex = -1;
    int whereEndIndex = -1;
    int forUpdateStartIndex = -1;
    int forUpdateEndIndex = -1;
    int[] ncharLiteralLocation = new int[513];
    int lastNcharLiteralLocation = -1;
    static final String paramPrefix = "rowid";
    int paramSuffix = 0;
    private boolean isConnectionValidationSql;
    StringBuffer stringBufferForScrollableStatement = null;
    private static final int cMax = 127;
    private static final int[][] TRANSITION = OracleSqlReadOnly.TRANSITION;
    private static final int[][] ACTION = OracleSqlReadOnly.ACTION;
    private static final int NO_ACTION = 0;
    private static final int DELETE_ACTION = 1;
    private static final int INSERT_ACTION = 2;
    private static final int MERGE_ACTION = 3;
    private static final int UPDATE_ACTION = 4;
    private static final int PLSQL_ACTION = 5;
    private static final int CALL_ACTION = 6;
    private static final int SELECT_ACTION = 7;
    private static final int ORDER_ACTION = 10;
    private static final int ORDER_BY_ACTION = 11;
    private static final int WHERE_ACTION = 9;
    private static final int FOR_ACTION = 12;
    private static final int FOR_UPDATE_ACTION = 13;
    private static final int OTHER_ACTION = 8;
    private static final int QUESTION_ACTION = 14;
    private static final int PARAMETER_ACTION = 15;
    private static final int END_PARAMETER_ACTION = 16;
    private static final int START_NCHAR_LITERAL_ACTION = 17;
    private static final int END_NCHAR_LITERAL_ACTION = 18;
    private static final int SAVE_DELIMITER_ACTION = 19;
    private static final int LOOK_FOR_DELIMITER_ACTION = 20;
    private static final int ALTER_SESSION_ACTION = 21;
    private static final int RETURNING_ACTION = 22;
    private static final int INTO_ACTION = 23;
    private static final int START_JSON_ACTION = 24;
    private static final int END_JSON_ACTION = 25;
    private static final int INITIAL_STATE = 0;
    private static final int RESTART_STATE = 67;
    static final int J_NESTED_CLOSE = 204;
    private static final OracleSqlReadOnly.ODBCAction[][] ODBC_ACTION = OracleSqlReadOnly.ODBC_ACTION;
    private static final boolean DEBUG_CBI = false;
    int current_argument;
    int i;
    int length;
    char currentChar;
    boolean first;
    String odbc_sql;
    StringBuffer oracle_sql;
    StringBuffer token_buffer;
    private static final Pattern DATABASE_OBJECT_NAME_RULE = Pattern.compile("\"[^\"\\u0000]+\"|\\p{L}[\\p{L}\\p{N}_$#@]*");

    protected OracleSql(DBConversion dBConversion) {
        this.conversion = dBConversion;
    }

    protected void initialize(String string) throws SQLException {
        if (string == null || string.length() == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 104).fillInStackTrace();
        }
        this.originalSql = string;
        this.utickSql = null;
        this.processedSql = null;
        this.rowidSql = null;
        this.actualSql = null;
        this.sqlBytes = null;
        this.sqlKind = OracleStatement.SqlKind.UNINITIALIZED;
        this.parameterCount = -1;
        this.parameterList = EMPTY_LIST;
        this.includeRowid = false;
        this.parameterSql = this.originalSql;
        this.bindParameterCount = -1;
        this.bindParameterList = null;
        this.cachedBindParameterCount = -1;
        this.cachedBindParameterList = null;
        this.cachedParameterSql = null;
        this.cachedActualSql = null;
        this.cachedProcessedSql = null;
        this.cachedRowidSql = null;
        this.cachedSqlBytes = null;
        this.selectEndIndex = -1;
        this.orderByStartIndex = -1;
        this.orderByEndIndex = -1;
        this.whereStartIndex = -1;
        this.whereEndIndex = -1;
        this.forUpdateStartIndex = -1;
        this.forUpdateEndIndex = -1;
        Matcher matcher = CONNECTION_VALIDATION_SQL_PATTERN.matcher(this.originalSql);
        this.isConnectionValidationSql = matcher.matches();
    }

    String getOriginalSql() {
        return this.originalSql;
    }

    boolean isConnectionValidationSql() {
        return this.isConnectionValidationSql;
    }

    boolean setNamedParameters(int n2, String[] stringArray) throws SQLException {
        boolean bl = false;
        if (n2 == 0) {
            this.bindParameterCount = -1;
            bl = this.bindParameterCount != this.cachedBindParameterCount;
        } else {
            this.bindParameterCount = n2;
            this.bindParameterList = stringArray;
            boolean bl2 = bl = this.bindParameterCount != this.cachedBindParameterCount || this.cachedBindParameterList == null;
            if (!bl) {
                for (int i2 = 0; i2 < n2; ++i2) {
                    if (this.bindParameterList[i2] == this.cachedBindParameterList[i2]) continue;
                    bl = true;
                    break;
                }
            }
            if (bl) {
                if (this.bindParameterCount != this.getParameterCount()) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 197).fillInStackTrace();
                }
                char[] cArray = this.originalSql.toCharArray();
                StringBuffer stringBuffer = new StringBuffer();
                int n3 = 0;
                for (int i3 = 0; i3 < cArray.length; ++i3) {
                    if (cArray[i3] != '?') {
                        stringBuffer.append(cArray[i3]);
                        continue;
                    }
                    stringBuffer.append(this.bindParameterList[n3++]);
                    stringBuffer.append("=>" + this.nextArgument());
                }
                this.parameterSql = stringBuffer.toString();
                this.actualSql = null;
                this.utickSql = null;
                this.processedSql = null;
                this.rowidSql = null;
                this.sqlBytes = null;
            } else {
                this.parameterSql = this.cachedParameterSql;
                this.actualSql = this.cachedActualSql;
                this.utickSql = this.cachedUtickSql;
                this.processedSql = this.cachedProcessedSql;
                this.rowidSql = this.cachedRowidSql;
                this.sqlBytes = this.cachedSqlBytes;
            }
        }
        this.cachedBindParameterList = null;
        this.cachedParameterSql = null;
        this.cachedActualSql = null;
        this.cachedUtickSql = null;
        this.cachedProcessedSql = null;
        this.cachedRowidSql = null;
        this.cachedSqlBytes = null;
        return bl;
    }

    void resetNamedParameters() {
        this.cachedBindParameterCount = this.bindParameterCount;
        if (this.bindParameterCount != -1) {
            if (this.cachedBindParameterList == null || this.cachedBindParameterList == this.bindParameterList || this.cachedBindParameterList.length < this.bindParameterCount) {
                this.cachedBindParameterList = new String[this.bindParameterCount];
            }
            System.arraycopy(this.bindParameterList, 0, this.cachedBindParameterList, 0, this.bindParameterCount);
            this.cachedParameterSql = this.parameterSql;
            this.cachedActualSql = this.actualSql;
            this.cachedUtickSql = this.utickSql;
            this.cachedProcessedSql = this.processedSql;
            this.cachedRowidSql = this.rowidSql;
            this.cachedSqlBytes = this.sqlBytes;
            this.bindParameterCount = -1;
            this.bindParameterList = null;
            this.parameterSql = this.originalSql;
            this.actualSql = null;
            this.utickSql = null;
            this.processedSql = null;
            this.rowidSql = null;
            this.sqlBytes = null;
        }
    }

    protected String getSql(boolean bl, boolean bl2) throws SQLException {
        if (this.sqlKind == OracleStatement.SqlKind.UNINITIALIZED) {
            this.computeBasicInfo(this.parameterSql);
        }
        if (bl != this.currentProcessEscapes || bl2 != this.currentConvertNcharLiterals) {
            if (bl2 != this.currentConvertNcharLiterals) {
                this.utickSql = null;
            }
            this.processedSql = null;
            this.rowidSql = null;
            this.actualSql = null;
            this.sqlBytes = null;
        }
        this.currentConvertNcharLiterals = bl2;
        this.currentProcessEscapes = bl;
        if (this.actualSql == null) {
            if (this.utickSql == null) {
                String string = this.utickSql = this.currentConvertNcharLiterals ? this.convertNcharLiterals(this.parameterSql) : this.parameterSql;
            }
            if (this.processedSql == null) {
                String string = this.processedSql = this.currentProcessEscapes ? this.parse(this.utickSql) : this.utickSql;
            }
            if (this.rowidSql == null) {
                this.rowidSql = this.includeRowid ? this.addRowid(this.processedSql) : this.processedSql;
            }
            this.actualSql = this.rowidSql;
        }
        return this.actualSql;
    }

    String getRevisedSql() throws SQLException {
        String string = null;
        if (this.sqlKind == OracleStatement.SqlKind.UNINITIALIZED) {
            this.computeBasicInfo(this.parameterSql);
        }
        string = this.removeOrderByForUpdate(this.parameterSql);
        return this.addRowid(string);
    }

    String removeForUpdate(String string) throws SQLException {
        if (this.forUpdateStartIndex != -1) {
            return string.substring(0, this.forUpdateStartIndex);
        }
        return string;
    }

    String removeOrderByForUpdate(String string) throws SQLException {
        if (this.orderByStartIndex != -1 && (this.forUpdateStartIndex == -1 || this.forUpdateStartIndex > this.orderByStartIndex)) {
            string = string.substring(0, this.orderByStartIndex);
        } else if (this.forUpdateStartIndex != -1) {
            string = string.substring(0, this.forUpdateStartIndex);
        }
        return string;
    }

    void appendForUpdate(StringBuffer stringBuffer) throws SQLException {
        if (this.orderByStartIndex != -1 && (this.forUpdateStartIndex == -1 || this.forUpdateStartIndex > this.orderByStartIndex)) {
            stringBuffer.append(this.originalSql.substring(this.orderByStartIndex));
        } else if (this.forUpdateStartIndex != -1) {
            stringBuffer.append(this.originalSql.substring(this.forUpdateStartIndex));
        }
    }

    String getInsertSqlForUpdatableResultSet(List<String> list) throws SQLException {
        String string = this.getOriginalSql();
        boolean bl = this.generatedSqlNeedEscapeProcessing();
        if (this.stringBufferForScrollableStatement == null) {
            this.stringBufferForScrollableStatement = new StringBuffer(string.length() + 30 + list.size() * 10);
        } else {
            this.stringBufferForScrollableStatement.delete(0, this.stringBufferForScrollableStatement.length());
        }
        this.stringBufferForScrollableStatement.append("insert into (");
        this.stringBufferForScrollableStatement.append(this.removeOrderByForUpdate(string));
        this.stringBufferForScrollableStatement.append(") (");
        String string2 = "";
        for (String string3 : list) {
            this.stringBufferForScrollableStatement.append(string2);
            this.stringBufferForScrollableStatement.append("\"");
            this.stringBufferForScrollableStatement.append(string3);
            this.stringBufferForScrollableStatement.append("\"");
            string2 = ", ";
        }
        this.stringBufferForScrollableStatement.append(") values ( ");
        string2 = "";
        for (String string3 : list) {
            this.stringBufferForScrollableStatement.append(string2);
            string2 = ", ";
            if (bl) {
                this.stringBufferForScrollableStatement.append("?");
                continue;
            }
            this.stringBufferForScrollableStatement.append(":" + this.generateParameterName());
        }
        this.stringBufferForScrollableStatement.append(")");
        this.paramSuffix = 0;
        return this.stringBufferForScrollableStatement.substring(0, this.stringBufferForScrollableStatement.length());
    }

    String getRefetchSqlForScrollableResultSet(OracleResultSet oracleResultSet, int n2) throws SQLException {
        throw new SQLException("no longer used");
    }

    String getRefetchSql() throws SQLException {
        String string = this.removeForUpdate(this.parameterSql);
        StringBuilder stringBuilder = new StringBuilder(string.length() + 240);
        stringBuilder.append("WITH \"__JDBC_ROWIDS__\" AS (SELECT COLUMN_VALUE ID, ROWNUM NUM FROM TABLE(");
        stringBuilder.append(this.generatedSqlNeedEscapeProcessing() ? "?" : ":" + this.generateParameterName());
        stringBuilder.append("))\n");
        stringBuilder.append("SELECT \"__JDBC_ORIGINAL__\".*\n");
        stringBuilder.append("FROM (");
        stringBuilder.append(this.addRowid(string));
        stringBuilder.append(") \"__JDBC_ORIGINAL__\", \"__JDBC_ROWIDS__\"\n");
        stringBuilder.append("WHERE \"__JDBC_ORIGINAL__\".\"__Oracle_JDBC_internal_ROWID__\"(+) = \"__JDBC_ROWIDS__\".ID\n");
        stringBuilder.append("ORDER BY \"__JDBC_ROWIDS__\".NUM");
        this.paramSuffix = 0;
        return stringBuilder.toString();
    }

    String getUpdateSqlForUpdatableResultSet(UpdatableResultSet updatableResultSet, int n2, Object[] objectArray, int[] nArray) throws SQLException {
        String string = this.getRevisedSql();
        boolean bl = this.generatedSqlNeedEscapeProcessing();
        if (this.stringBufferForScrollableStatement == null) {
            this.stringBufferForScrollableStatement = new StringBuffer(string.length() + 100);
        } else {
            this.stringBufferForScrollableStatement.delete(0, this.stringBufferForScrollableStatement.length());
        }
        this.stringBufferForScrollableStatement.append("update (");
        this.stringBufferForScrollableStatement.append(string);
        this.stringBufferForScrollableStatement.append(") set ");
        if (objectArray != null) {
            for (int i2 = 0; i2 < n2; ++i2) {
                if (i2 > 0) {
                    this.stringBufferForScrollableStatement.append(", ");
                }
                this.stringBufferForScrollableStatement.append("\"");
                this.stringBufferForScrollableStatement.append(updatableResultSet.getInternalMetadata().getColumnName(nArray[i2] + 1));
                this.stringBufferForScrollableStatement.append("\"");
                if (bl) {
                    this.stringBufferForScrollableStatement.append(" = ?");
                    continue;
                }
                this.stringBufferForScrollableStatement.append(" = :" + this.generateParameterName());
            }
        }
        this.stringBufferForScrollableStatement.append(" WHERE ");
        if (bl) {
            this.stringBufferForScrollableStatement.append(" ROWID = ?");
        } else {
            this.stringBufferForScrollableStatement.append(" ROWID = :" + this.generateParameterName());
        }
        this.paramSuffix = 0;
        return this.stringBufferForScrollableStatement.substring(0, this.stringBufferForScrollableStatement.length());
    }

    String getDeleteSqlForUpdatableResultSet(UpdatableResultSet updatableResultSet) throws SQLException {
        String string = this.getRevisedSql();
        boolean bl = this.generatedSqlNeedEscapeProcessing();
        if (this.stringBufferForScrollableStatement == null) {
            this.stringBufferForScrollableStatement = new StringBuffer(string.length() + 100);
        } else {
            this.stringBufferForScrollableStatement.delete(0, this.stringBufferForScrollableStatement.length());
        }
        this.stringBufferForScrollableStatement.append("delete from (");
        this.stringBufferForScrollableStatement.append(string);
        this.stringBufferForScrollableStatement.append(") where ");
        if (bl) {
            this.stringBufferForScrollableStatement.append(" ROWID = ?");
        } else {
            this.stringBufferForScrollableStatement.append(" ROWID = :" + this.generateParameterName());
        }
        this.paramSuffix = 0;
        return this.stringBufferForScrollableStatement.substring(0, this.stringBufferForScrollableStatement.length());
    }

    String getInsertSqlForUpdatableResultSet(OldUpdatableResultSet oldUpdatableResultSet) throws SQLException {
        String string = this.getOriginalSql();
        boolean bl = this.generatedSqlNeedEscapeProcessing();
        if (this.stringBufferForScrollableStatement == null) {
            this.stringBufferForScrollableStatement = new StringBuffer(string.length() + 100);
        } else {
            this.stringBufferForScrollableStatement.delete(0, this.stringBufferForScrollableStatement.length());
        }
        this.stringBufferForScrollableStatement.append("insert into (");
        this.stringBufferForScrollableStatement.append(this.removeOrderByForUpdate(string));
        this.stringBufferForScrollableStatement.append(") values ( ");
        for (int i2 = 0; i2 < oldUpdatableResultSet.getColumnCount(); ++i2) {
            if (i2 != 0) {
                this.stringBufferForScrollableStatement.append(", ");
            }
            if (bl) {
                this.stringBufferForScrollableStatement.append("?");
                continue;
            }
            this.stringBufferForScrollableStatement.append(":" + this.generateParameterName());
        }
        this.stringBufferForScrollableStatement.append(")");
        this.paramSuffix = 0;
        return this.stringBufferForScrollableStatement.substring(0, this.stringBufferForScrollableStatement.length());
    }

    String getUpdateSqlForUpdatableResultSet(OldUpdatableResultSet oldUpdatableResultSet, int n2, Object[] objectArray, int[] nArray) throws SQLException {
        String string = this.getRevisedSql();
        boolean bl = this.generatedSqlNeedEscapeProcessing();
        if (this.stringBufferForScrollableStatement == null) {
            this.stringBufferForScrollableStatement = new StringBuffer(string.length() + 100);
        } else {
            this.stringBufferForScrollableStatement.delete(0, this.stringBufferForScrollableStatement.length());
        }
        this.stringBufferForScrollableStatement.append("update (");
        this.stringBufferForScrollableStatement.append(string);
        this.stringBufferForScrollableStatement.append(") set ");
        if (objectArray != null) {
            for (int i2 = 0; i2 < n2; ++i2) {
                if (i2 > 0) {
                    this.stringBufferForScrollableStatement.append(", ");
                }
                this.stringBufferForScrollableStatement.append("\"");
                this.stringBufferForScrollableStatement.append(oldUpdatableResultSet.getInternalMetadata().getColumnName(nArray[i2] + 1));
                this.stringBufferForScrollableStatement.append("\"");
                if (bl) {
                    this.stringBufferForScrollableStatement.append(" = ?");
                    continue;
                }
                this.stringBufferForScrollableStatement.append(" = :" + this.generateParameterName());
            }
        }
        this.stringBufferForScrollableStatement.append(" WHERE ");
        if (bl) {
            this.stringBufferForScrollableStatement.append(" ROWID = ?");
        } else {
            this.stringBufferForScrollableStatement.append(" ROWID = :" + this.generateParameterName());
        }
        this.paramSuffix = 0;
        return this.stringBufferForScrollableStatement.substring(0, this.stringBufferForScrollableStatement.length());
    }

    String getDeleteSqlForUpdatableResultSet(OldUpdatableResultSet oldUpdatableResultSet) throws SQLException {
        String string = this.getRevisedSql();
        boolean bl = this.generatedSqlNeedEscapeProcessing();
        if (this.stringBufferForScrollableStatement == null) {
            this.stringBufferForScrollableStatement = new StringBuffer(string.length() + 100);
        } else {
            this.stringBufferForScrollableStatement.delete(0, this.stringBufferForScrollableStatement.length());
        }
        this.stringBufferForScrollableStatement.append("delete from (");
        this.stringBufferForScrollableStatement.append(string);
        this.stringBufferForScrollableStatement.append(") where ");
        if (bl) {
            this.stringBufferForScrollableStatement.append(" ROWID = ?");
        } else {
            this.stringBufferForScrollableStatement.append(" ROWID = :" + this.generateParameterName());
        }
        this.paramSuffix = 0;
        return this.stringBufferForScrollableStatement.substring(0, this.stringBufferForScrollableStatement.length());
    }

    final boolean generatedSqlNeedEscapeProcessing() {
        return this.parameterCount > 0 && this.parameterList == EMPTY_LIST;
    }

    byte[] getSqlBytes(boolean bl, boolean bl2) throws SQLException {
        if (this.sqlBytes == null || bl != this.currentProcessEscapes) {
            this.sqlBytes = this.conversion.StringToCharBytes(this.getSql(bl, bl2));
        }
        return this.sqlBytes;
    }

    OracleStatement.SqlKind getSqlKind() throws SQLException {
        if (this.parameterSql == null) {
            return OracleStatement.SqlKind.UNINITIALIZED;
        }
        if (this.sqlKind == OracleStatement.SqlKind.UNINITIALIZED) {
            this.computeBasicInfo(this.parameterSql);
        }
        return this.sqlKind;
    }

    protected int getParameterCount() throws SQLException {
        if (this.parameterCount == -1) {
            this.computeBasicInfo(this.parameterSql);
        }
        return this.parameterCount;
    }

    protected String[] getParameterList() throws SQLException {
        if (this.parameterCount == -1) {
            this.computeBasicInfo(this.parameterSql);
        }
        return this.parameterList;
    }

    void setIncludeRowid(boolean bl) {
        if (bl != this.includeRowid) {
            this.includeRowid = bl;
            this.rowidSql = null;
            this.actualSql = null;
            this.sqlBytes = null;
        }
    }

    public String toString() {
        return this.parameterSql == null ? "null" : this.parameterSql;
    }

    private String hexUnicode(int n2) throws SQLException {
        String string = Integer.toHexString(n2);
        switch (string.length()) {
            case 0: {
                return "\\0000";
            }
            case 1: {
                return "\\000" + string;
            }
            case 2: {
                return "\\00" + string;
            }
            case 3: {
                return "\\0" + string;
            }
            case 4: {
                return "\\" + string;
            }
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 89, "Unexpected case in OracleSql.hexUnicode: " + n2).fillInStackTrace();
    }

    String convertNcharLiterals(String string) throws SQLException {
        if (this.lastNcharLiteralLocation <= 2) {
            return string;
        }
        String string2 = "";
        int n2 = 0;
        block0: while (true) {
            int n3 = this.ncharLiteralLocation[n2++];
            int n4 = this.ncharLiteralLocation[n2++];
            string2 = string2 + string.substring(n3, n4);
            if (n2 >= this.lastNcharLiteralLocation) break;
            n3 = this.ncharLiteralLocation[n2];
            string2 = string2 + "u'";
            int n5 = n4 + 2;
            while (true) {
                if (n5 >= n3) continue block0;
                char c2 = string.charAt(n5);
                string2 = c2 == '\\' ? string2 + "\\\\" : (c2 < '\u0080' ? string2 + c2 : string2 + this.hexUnicode(c2));
                ++n5;
            }
            break;
        }
        return string2;
    }

    void computeBasicInfo(String string) throws SQLException {
        this.parameterCount = 0;
        boolean bl = false;
        boolean bl2 = false;
        this.returningIntoParameterCount = 0;
        this.lastNcharLiteralLocation = 0;
        this.ncharLiteralLocation[this.lastNcharLiteralLocation++] = 0;
        int n2 = 0;
        int n3 = 0;
        ArrayDeque<Character> arrayDeque = new ArrayDeque<Character>();
        int n4 = 0;
        int n5 = string.length();
        int n6 = -1;
        int n7 = -1;
        int n8 = n5 + 1;
        for (int i2 = 0; i2 < n8; ++i2) {
            int n9 = i2 < n5 ? (int)string.charAt(i2) : 32;
            this.currentChar = (char)n9;
            if (n9 > 127) {
                this.currentChar = Character.isLetterOrDigit((char)n9) ? (char)88 : (char)32;
            }
            switch (ACTION[n4][this.currentChar]) {
                case 0: {
                    break;
                }
                case 1: {
                    this.sqlKind = OracleStatement.SqlKind.DELETE;
                    break;
                }
                case 2: {
                    this.sqlKind = OracleStatement.SqlKind.INSERT;
                    break;
                }
                case 3: {
                    this.sqlKind = OracleStatement.SqlKind.MERGE;
                    break;
                }
                case 4: {
                    this.sqlKind = OracleStatement.SqlKind.UPDATE;
                    break;
                }
                case 5: {
                    this.sqlKind = OracleStatement.SqlKind.PLSQL_BLOCK;
                    break;
                }
                case 6: {
                    this.sqlKind = OracleStatement.SqlKind.CALL_BLOCK;
                    break;
                }
                case 7: {
                    this.sqlKind = OracleStatement.SqlKind.SELECT;
                    this.selectEndIndex = i2;
                    break;
                }
                case 8: {
                    this.sqlKind = OracleStatement.SqlKind.OTHER;
                    break;
                }
                case 9: {
                    this.whereStartIndex = i2 - 5;
                    this.whereEndIndex = i2;
                    break;
                }
                case 10: {
                    n6 = i2 - 5;
                    break;
                }
                case 11: {
                    this.orderByStartIndex = n6;
                    this.orderByEndIndex = i2;
                    break;
                }
                case 12: {
                    n7 = i2 - 3;
                    break;
                }
                case 13: {
                    this.forUpdateStartIndex = n7;
                    this.forUpdateEndIndex = i2;
                    if (this.sqlKind != OracleStatement.SqlKind.SELECT) break;
                    this.sqlKind = OracleStatement.SqlKind.SELECT_FOR_UPDATE;
                    break;
                }
                case 21: {
                    this.sqlKind = OracleStatement.SqlKind.ALTER_SESSION;
                    break;
                }
                case 14: {
                    ++this.parameterCount;
                    if (!bl2) break;
                    ++this.returningIntoParameterCount;
                    break;
                }
                case 15: {
                    if (this.currentParameter == null) {
                        this.currentParameter = new char[258];
                    }
                    if (n3 >= this.currentParameter.length) {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 134, new String(this.currentParameter)).fillInStackTrace();
                    }
                    this.currentParameter[n3++] = n9;
                    break;
                }
                case 16: {
                    String[] stringArray;
                    if (n3 <= 0) break;
                    if (this.parameterList == EMPTY_LIST) {
                        this.parameterList = new String[Math.max(8, this.parameterCount * 4)];
                    } else if (this.parameterList.length <= this.parameterCount) {
                        stringArray = new String[this.parameterList.length * 4];
                        System.arraycopy(this.parameterList, 0, stringArray, 0, this.parameterList.length);
                        this.parameterList = stringArray;
                    }
                    this.parameterList[this.parameterCount] = new String(this.currentParameter, 0, n3).intern();
                    n3 = 0;
                    ++this.parameterCount;
                    if (!bl2) break;
                    ++this.returningIntoParameterCount;
                    break;
                }
                case 17: {
                    this.ncharLiteralLocation[this.lastNcharLiteralLocation++] = i2 - 1;
                    if (this.lastNcharLiteralLocation < this.ncharLiteralLocation.length) break;
                    this.growNcharLiteralLocation(this.ncharLiteralLocation.length << 2);
                    break;
                }
                case 18: {
                    this.ncharLiteralLocation[this.lastNcharLiteralLocation++] = i2;
                    if (this.lastNcharLiteralLocation < this.ncharLiteralLocation.length) break;
                    this.growNcharLiteralLocation(this.ncharLiteralLocation.length << 2);
                    break;
                }
                case 19: {
                    if (n9 == 91) {
                        n2 = 93;
                        break;
                    }
                    if (n9 == 123) {
                        n2 = 125;
                        break;
                    }
                    if (n9 == 60) {
                        n2 = 62;
                        break;
                    }
                    if (n9 == 40) {
                        n2 = 41;
                        break;
                    }
                    n2 = n9;
                    break;
                }
                case 20: {
                    if (n9 != n2) break;
                    ++n4;
                    break;
                }
                case 22: {
                    bl = true;
                    break;
                }
                case 23: {
                    if (!bl) break;
                    bl2 = true;
                    break;
                }
                case 24: {
                    arrayDeque.push(Character.valueOf(n9 == 123 ? (char)'}' : ']'));
                    break;
                }
                case 25: {
                    String[] stringArray;
                    if (arrayDeque.isEmpty()) break;
                    if (((Character)arrayDeque.peek()).charValue() == n9) {
                        arrayDeque.pop();
                        if (!arrayDeque.isEmpty()) {
                            n4 = 204;
                        }
                    }
                    if (n3 <= 0) break;
                    if (this.parameterList == EMPTY_LIST) {
                        this.parameterList = new String[Math.max(8, this.parameterCount * 4)];
                    } else if (this.parameterList.length <= this.parameterCount) {
                        stringArray = new String[this.parameterList.length * 4];
                        System.arraycopy(this.parameterList, 0, stringArray, 0, this.parameterList.length);
                        this.parameterList = stringArray;
                    }
                    this.parameterList[this.parameterCount] = new String(this.currentParameter, 0, n3).intern();
                    n3 = 0;
                    ++this.parameterCount;
                    if (!bl2) break;
                    ++this.returningIntoParameterCount;
                }
            }
            n4 = TRANSITION[n4][this.currentChar];
        }
        if (this.lastNcharLiteralLocation + 2 >= this.ncharLiteralLocation.length) {
            this.growNcharLiteralLocation(this.lastNcharLiteralLocation + 2);
        }
        this.ncharLiteralLocation[this.lastNcharLiteralLocation++] = n5;
        this.ncharLiteralLocation[this.lastNcharLiteralLocation] = n5;
    }

    void growNcharLiteralLocation(int n2) {
        int[] nArray = new int[n2];
        System.arraycopy(this.ncharLiteralLocation, 0, nArray, 0, this.ncharLiteralLocation.length);
        this.ncharLiteralLocation = null;
        this.ncharLiteralLocation = nArray;
    }

    private String addRowid(String string) throws SQLException {
        if (this.selectEndIndex == -1) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 88).fillInStackTrace();
        }
        String string2 = "select rowid as \"__Oracle_JDBC_internal_ROWID__\"," + string.substring(this.selectEndIndex);
        return string2;
    }

    String parse(String string) throws SQLException {
        this.first = true;
        this.current_argument = 1;
        this.i = 0;
        this.odbc_sql = string;
        this.length = this.odbc_sql.length();
        if (this.oracle_sql == null) {
            this.oracle_sql = new StringBuffer(this.length);
            this.token_buffer = new StringBuffer(32);
        } else {
            this.oracle_sql.ensureCapacity(this.length);
        }
        this.oracle_sql.delete(0, this.oracle_sql.length());
        this.skipSpace();
        this.handleODBC(ParseMode.NORMAL);
        if (this.i < this.length) {
            Integer n2 = this.i;
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 33, n2).fillInStackTrace();
        }
        return this.oracle_sql.substring(0, this.oracle_sql.length());
    }

    void handleODBC(ParseMode parseMode) throws SQLException {
        int n2 = parseMode == ParseMode.NORMAL ? 0 : 67;
        char c2 = '\u0000';
        int n3 = 0;
        ArrayDeque<Character> arrayDeque = new ArrayDeque<Character>();
        while (this.i < this.length) {
            char c3 = this.i < this.length ? (char)this.odbc_sql.charAt(this.i) : (char)' ';
            this.currentChar = c3;
            if (c3 > '\u007f') {
                this.currentChar = Character.isLetterOrDigit(c3) ? (char)88 : (char)32;
            }
            switch (ODBC_ACTION[n2][this.currentChar]) {
                case NONE: {
                    break;
                }
                case COPY: {
                    this.oracle_sql.append(c3);
                    break;
                }
                case QUESTION: {
                    this.oracle_sql.append(this.nextArgument());
                    this.oracle_sql.append(' ');
                    break;
                }
                case SAVE_DELIMITER: {
                    c2 = c3 == '[' ? (char)']' : (c3 == '{' ? (char)'}' : (c3 == '<' ? (char)'>' : (c3 == '(' ? (char)')' : c3)));
                    this.oracle_sql.append(c3);
                    break;
                }
                case LOOK_FOR_DELIMITER: {
                    if (c3 == c2) {
                        ++n2;
                    }
                    this.oracle_sql.append(c3);
                    break;
                }
                case FUNCTION: {
                    this.handleFunction();
                    break;
                }
                case CALL: {
                    this.handleCall();
                    break;
                }
                case TIME: {
                    this.handleTime();
                    break;
                }
                case TIMESTAMP: {
                    this.handleTimestamp();
                    break;
                }
                case DATE: {
                    this.handleDate();
                    break;
                }
                case ESCAPE: {
                    this.handleEscape();
                    break;
                }
                case SCALAR_FUNCTION: {
                    this.handleScalarFunction();
                    break;
                }
                case OUTER_JOIN: {
                    this.handleOuterJoin();
                    break;
                }
                case UNKNOWN_ESCAPE: {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, (Object)this.i).fillInStackTrace();
                }
                case END_ODBC_ESCAPE: {
                    if (parseMode == ParseMode.SCALAR) {
                        n2 = TRANSITION[n2][this.currentChar];
                        return;
                    }
                }
                case COMMA: {
                    if (parseMode == ParseMode.LOCATE_1 && n3 > 1) {
                        this.oracle_sql.append(c3);
                        break;
                    }
                    if (parseMode == ParseMode.LOCATE_1) {
                        n2 = TRANSITION[n2][this.currentChar];
                        return;
                    }
                    if (parseMode == ParseMode.LOCATE_2) break;
                    this.oracle_sql.append(c3);
                    break;
                }
                case OPEN_PAREN: {
                    if (parseMode == ParseMode.LOCATE_1) {
                        if (n3 > 0) {
                            this.oracle_sql.append(c3);
                        }
                        ++n3;
                        break;
                    }
                    if (parseMode == ParseMode.LOCATE_2) {
                        ++n3;
                        this.oracle_sql.append(c3);
                        break;
                    }
                    this.oracle_sql.append(c3);
                    break;
                }
                case CLOSE_PAREN: {
                    if (parseMode == ParseMode.LOCATE_1) {
                        --n3;
                        this.oracle_sql.append(c3);
                        break;
                    }
                    if (parseMode == ParseMode.LOCATE_2 && n3 > 1) {
                        --n3;
                        this.oracle_sql.append(c3);
                        break;
                    }
                    if (parseMode == ParseMode.LOCATE_2) {
                        ++this.i;
                        n2 = TRANSITION[n2][this.currentChar];
                        return;
                    }
                    this.oracle_sql.append(c3);
                    break;
                }
                case BEGIN: {
                    this.first = false;
                    this.oracle_sql.append(c3);
                    break;
                }
                case LIMIT: {
                    this.handleLimit();
                    break;
                }
                case START_JSON: {
                    arrayDeque.push(Character.valueOf(c3 == '{' ? (char)'}' : ']'));
                    this.oracle_sql.append(c3);
                    break;
                }
                case END_JSON: {
                    if (arrayDeque.isEmpty()) break;
                    if (((Character)arrayDeque.peek()).charValue() == c3) {
                        arrayDeque.pop();
                        if (!arrayDeque.isEmpty()) {
                            n2 = 204;
                        }
                    }
                    this.oracle_sql.append(c3);
                }
            }
            n2 = TRANSITION[n2][this.currentChar];
            ++this.i;
        }
    }

    void handleFunction() throws SQLException {
        boolean bl = this.first;
        this.first = false;
        if (bl) {
            this.oracle_sql.append("BEGIN ");
        }
        this.appendChar(this.oracle_sql, '?');
        this.skipSpace();
        if (this.currentChar != '=') {
            String string = this.i + ". Expecting \"=\" got \"" + this.currentChar + "\"";
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 33, string).fillInStackTrace();
        }
        ++this.i;
        this.skipSpace();
        if (!this.odbc_sql.startsWith("call", this.i)) {
            String string = this.i + ". Expecting \"call\"";
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 33, string).fillInStackTrace();
        }
        this.i += 4;
        this.oracle_sql.append(" := ");
        this.skipSpace();
        this.handleODBC(ParseMode.SCALAR);
        if (bl) {
            this.oracle_sql.append("; END;");
        }
    }

    void handleCall() throws SQLException {
        boolean bl = this.first;
        this.first = false;
        if (bl) {
            this.oracle_sql.append("BEGIN ");
        }
        this.skipSpace();
        this.handleODBC(ParseMode.SCALAR);
        this.skipSpace();
        if (bl) {
            this.oracle_sql.append("; END;");
        }
    }

    void handleTimestamp() throws SQLException {
        this.oracle_sql.append("TO_TIMESTAMP (");
        this.skipSpace();
        this.handleODBC(ParseMode.SCALAR);
        this.oracle_sql.append(", 'YYYY-MM-DD HH24:MI:SS.FF')");
    }

    void handleTime() throws SQLException {
        this.skipSpace();
        this.oracle_sql.append("TO_DATE('1-JAN-1970 '||TO_CHAR(TO_DATE(");
        this.handleODBC(ParseMode.SCALAR);
        this.oracle_sql.append(",'HH24:MI:SS'),'HH24:MI:SS'),'DD-MON-YYYY HH24:MI:SS')");
    }

    void handleDate() throws SQLException {
        this.oracle_sql.append("TO_DATE (");
        this.skipSpace();
        this.handleODBC(ParseMode.SCALAR);
        this.oracle_sql.append(", 'YYYY-MM-DD')");
    }

    void handleEscape() throws SQLException {
        this.oracle_sql.append("ESCAPE ");
        this.skipSpace();
        this.handleODBC(ParseMode.SCALAR);
    }

    void handleLimit() throws SQLException {
        this.oracle_sql.append("ROWNUM <= ");
        this.skipSpace();
        this.handleODBC(ParseMode.SCALAR);
    }

    void handleScalarFunction() throws SQLException {
        this.token_buffer.delete(0, this.token_buffer.length());
        ++this.i;
        this.skipSpace();
        while (this.i < this.length && (Character.isJavaIdentifierPart(this.currentChar = this.odbc_sql.charAt(this.i)) || this.currentChar == '?')) {
            this.token_buffer.append(this.currentChar);
            ++this.i;
        }
        String string = this.token_buffer.substring(0, this.token_buffer.length()).toUpperCase().intern();
        if (string == "ABS") {
            this.usingFunctionName(string);
        } else if (string == "ACOS") {
            this.usingFunctionName(string);
        } else if (string == "ASIN") {
            this.usingFunctionName(string);
        } else if (string == "ATAN") {
            this.usingFunctionName(string);
        } else if (string == "ATAN2") {
            this.usingFunctionName(string);
        } else if (string == "CEILING") {
            this.usingFunctionName("CEIL");
        } else if (string == "COS") {
            this.usingFunctionName(string);
        } else {
            if (string == "COT") {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
            }
            if (string == "DEGREES") {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
            }
            if (string == "EXP") {
                this.usingFunctionName(string);
            } else if (string == "FLOOR") {
                this.usingFunctionName(string);
            } else if (string == "LOG") {
                this.usingFunctionName("LN");
            } else if (string == "LOG10") {
                this.replacingFunctionPrefix("LOG ( 10, ");
            } else if (string == "MOD") {
                this.usingFunctionName(string);
            } else if (string == "PI") {
                this.replacingFunctionPrefix("( 3.141592653589793238462643383279502884197169399375 ");
            } else if (string == "POWER") {
                this.usingFunctionName(string);
            } else {
                if (string == "RADIANS") {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                }
                if (string == "RAND") {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                }
                if (string == "ROUND") {
                    this.usingFunctionName(string);
                } else if (string == "SIGN") {
                    this.usingFunctionName(string);
                } else if (string == "SIN") {
                    this.usingFunctionName(string);
                } else if (string == "SQRT") {
                    this.usingFunctionName(string);
                } else if (string == "TAN") {
                    this.usingFunctionName(string);
                } else if (string == "TRUNCATE") {
                    this.usingFunctionName("TRUNC");
                } else if (string == "ASCII") {
                    this.usingFunctionName(string);
                } else if (string == "CHAR") {
                    this.usingFunctionName("CHR");
                } else if (string == "CHAR_LENGTH") {
                    this.usingFunctionName("LENGTH");
                } else if (string == "CHARACTER_LENGTH") {
                    this.usingFunctionName("LENGTH");
                } else if (string == "CONCAT") {
                    this.usingFunctionName(string);
                } else {
                    if (string == "DIFFERENCE") {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                    }
                    if (string == "INSERT") {
                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                    }
                    if (string == "LCASE") {
                        this.usingFunctionName("LOWER");
                    } else {
                        if (string == "LEFT") {
                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                        }
                        if (string == "LENGTH") {
                            this.usingFunctionName(string);
                        } else if (string == "LOCATE") {
                            StringBuffer stringBuffer = this.oracle_sql;
                            this.oracle_sql = new StringBuffer();
                            this.handleODBC(ParseMode.LOCATE_1);
                            StringBuffer stringBuffer2 = this.oracle_sql;
                            this.oracle_sql = stringBuffer;
                            this.oracle_sql.append("INSTR(");
                            this.handleODBC(ParseMode.LOCATE_2);
                            this.oracle_sql.append(',');
                            this.oracle_sql.append(stringBuffer2);
                            this.oracle_sql.append(')');
                            this.handleODBC(ParseMode.SCALAR);
                        } else if (string == "LTRIM") {
                            this.usingFunctionName(string);
                        } else if (string == "OCTET_LENGTH") {
                            this.usingFunctionName("LENGTHB");
                        } else {
                            if (string == "POSITION") {
                                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                            }
                            if (string == "REPEAT") {
                                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                            }
                            if (string == "REPLACE") {
                                this.usingFunctionName(string);
                            } else {
                                if (string == "RIGHT") {
                                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                }
                                if (string == "RTRIM") {
                                    this.usingFunctionName(string);
                                } else if (string == "SOUNDEX") {
                                    this.usingFunctionName(string);
                                } else {
                                    if (string == "SPACE") {
                                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                    }
                                    if (string == "SUBSTRING") {
                                        this.usingFunctionName("SUBSTR");
                                    } else if (string == "UCASE") {
                                        this.usingFunctionName("UPPER");
                                    } else if (string == "CURRENT_DATE") {
                                        this.replacingFunctionPrefix("(CURRENT_DATE");
                                    } else {
                                        if (string == "CURRENT_TIME") {
                                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                        }
                                        if (string == "CURRENT_TIMESTAMP") {
                                            this.replacingFunctionPrefix("(CURRENT_TIMESTAMP");
                                        } else if (string == "CURDATE") {
                                            this.replacingFunctionPrefix("(CURRENT_DATE");
                                        } else if (string == "CURTIME") {
                                            this.replacingFunctionPrefix("(CURRENT_TIMESTAMP");
                                        } else {
                                            if (string == "DAYNAME") {
                                                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                            }
                                            if (string == "DAYOFMONTH") {
                                                this.replacingFunctionPrefix("EXTRACT ( DAY FROM ");
                                            } else {
                                                if (string == "DAYOFWEEK") {
                                                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                                }
                                                if (string == "DAYOFYEAR") {
                                                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                                }
                                                if (string == "EXTRACT") {
                                                    this.usingFunctionName("EXTRACT");
                                                } else if (string == "HOUR") {
                                                    this.replacingFunctionPrefix("EXTRACT ( HOUR FROM ");
                                                } else if (string == "MINUTE") {
                                                    this.replacingFunctionPrefix("EXTRACT ( MINUTE FROM ");
                                                } else if (string == "MONTH") {
                                                    this.replacingFunctionPrefix("EXTRACT ( MONTH FROM ");
                                                } else {
                                                    if (string == "MONTHNAME") {
                                                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                                    }
                                                    if (string == "NOW") {
                                                        this.replacingFunctionPrefix("(CURRENT_TIMESTAMP");
                                                    } else {
                                                        if (string == "QUARTER") {
                                                            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                                        }
                                                        if (string == "SECOND") {
                                                            this.replacingFunctionPrefix("EXTRACT ( SECOND FROM ");
                                                        } else {
                                                            if (string == "TIMESTAMPADD") {
                                                                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                                            }
                                                            if (string == "TIMESTAMPDIFF") {
                                                                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                                            }
                                                            if (string == "WEEK") {
                                                                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                                            }
                                                            if (string == "YEAR") {
                                                                this.replacingFunctionPrefix("EXTRACT ( YEAR FROM ");
                                                            } else {
                                                                if (string == "DATABASE") {
                                                                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                                                }
                                                                if (string == "IFNULL") {
                                                                    this.usingFunctionName("NVL");
                                                                } else if (string == "USER") {
                                                                    this.replacingFunctionPrefix("(USER");
                                                                } else {
                                                                    if (string == "CONVERT") {
                                                                        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                                                    }
                                                                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 34, string).fillInStackTrace();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void usingFunctionName(String string) throws SQLException {
        this.oracle_sql.append(string);
        this.skipSpace();
        this.handleODBC(ParseMode.SCALAR);
    }

    void replacingFunctionPrefix(String string) throws SQLException {
        this.skipSpace();
        if (this.i < this.length && (this.currentChar = this.odbc_sql.charAt(this.i)) == '(') {
            ++this.i;
        } else {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 33).fillInStackTrace();
        }
        this.oracle_sql.append(string);
        this.skipSpace();
        this.handleODBC(ParseMode.SCALAR);
    }

    void handleOuterJoin() throws SQLException {
        this.oracle_sql.append(" ( ");
        this.skipSpace();
        this.handleODBC(ParseMode.SCALAR);
        this.oracle_sql.append(" ) ");
    }

    String nextArgument() {
        String string = ":" + this.current_argument;
        ++this.current_argument;
        return string;
    }

    void appendChar(StringBuffer stringBuffer, char c2) {
        if (c2 == '?') {
            stringBuffer.append(this.nextArgument());
        } else {
            stringBuffer.append(c2);
        }
    }

    void skipSpace() {
        while (this.i < this.length && (this.currentChar = this.odbc_sql.charAt(this.i)) == ' ') {
            ++this.i;
        }
    }

    String generateParameterName() {
        String string;
        if (this.parameterCount == 0 || this.parameterList == null) {
            return paramPrefix + this.paramSuffix++;
        }
        block0: while (true) {
            string = paramPrefix + this.paramSuffix++;
            for (int i2 = 0; i2 < this.parameterList.length; ++i2) {
                if (string.equals(this.parameterList[i2])) continue block0;
            }
            break;
        }
        return string;
    }

    static boolean isValidPlsqlWarning(String string) throws SQLException {
        return string.matches("('\\s*([a-zA-Z0-9:,\\(\\)\\s])*')\\s*(,\\s*'([a-zA-Z0-9:,\\(\\)\\s])*')*");
    }

    public static boolean isValidObjectName(String string) {
        assert (string != null && string.length() > 0) : "name is null or empty";
        return DATABASE_OBJECT_NAME_RULE.matcher(string).matches();
    }

    public static void main(String[] stringArray) {
        String[] stringArray2;
        if (stringArray.length < 2) {
            System.err.println("ERROR: incorrect usage. OracleSql (-transition <file> | <process_escapes> <convert_nchars> { <sql> } )");
            return;
        }
        if (stringArray[0].equals("-dump")) {
            OracleSql.dumpTransitionMatrix(stringArray[1]);
            return;
        }
        boolean bl = stringArray[0].equals("true");
        boolean bl2 = stringArray[1].equals("true");
        if (stringArray.length > 2) {
            stringArray2 = new String[stringArray.length - 2];
            System.arraycopy(stringArray, 2, stringArray2, 0, stringArray2.length);
        } else {
            stringArray2 = new String[]{"select ? from dual", "insert into dual values (?)", "delete from dual", "update dual set dummy = ?", "merge tab into dual", " select ? from dual where ? = ?", "select ?from dual where?=?for update", "select '?', n'?', q'???', q'{?}', q'{cat's}' from dual", "select'?',n'?',q'???',q'{?}',q'{cat's}'from dual", "select--line\n? from dual", "select --line\n? from dual", "--line\nselect ? from dual", " --line\nselect ? from dual", "--line\n select ? from dual", "begin proc4in4out (:x1, :x2, :x3, :x4); end;", "{CALL tkpjpn01(:pin, :pinout, :pout)}", "select :NumberBindVar as the_number from dual", "select {fn locate(bob(carol(),ted(alice,sue)), 'xfy')} from dual", "CREATE USER vijay6 IDENTIFIED BY \"vjay?\"", "ALTER SESSION SET TIME", "SELECT ename FROM emp WHERE hiredate BETWEEN {ts'1980-12-17'} AND {ts '1981-09-28'} "};
        }
        for (String string : stringArray2) {
            try {
                int n2;
                System.out.println("\n\n-----------------------");
                System.out.println(string);
                System.out.println();
                OracleSql oracleSql = new OracleSql(null);
                oracleSql.initialize(string);
                String string2 = oracleSql.getSql(bl, bl2);
                System.out.println((Object)((Object)oracleSql.sqlKind) + ", " + oracleSql.parameterCount);
                String[] stringArray3 = oracleSql.getParameterList();
                if (stringArray3 == EMPTY_LIST) {
                    System.out.println("parameterList is empty");
                } else {
                    for (n2 = 0; n2 < stringArray3.length; ++n2) {
                        System.out.println("parameterList[" + n2 + "] = " + stringArray3[n2]);
                    }
                }
                if (oracleSql.getSqlKind().isDML()) {
                    n2 = oracleSql.getReturnParameterCount();
                    if (n2 == -1) {
                        System.out.println("no return parameters");
                    } else {
                        System.out.println(n2 + " return parameters");
                    }
                }
                if (oracleSql.lastNcharLiteralLocation == 2) {
                    System.out.println("No NCHAR literals");
                } else {
                    System.out.println("NCHAR Literals");
                    n2 = 1;
                    while (n2 < oracleSql.lastNcharLiteralLocation - 1) {
                        System.out.println(string.substring(oracleSql.ncharLiteralLocation[n2++], oracleSql.ncharLiteralLocation[n2++]));
                    }
                }
                System.out.println("Keywords");
                if (oracleSql.selectEndIndex == -1) {
                    System.out.println("no select");
                } else {
                    System.out.println("'" + string.substring(oracleSql.selectEndIndex - 6, oracleSql.selectEndIndex) + "'");
                }
                if (oracleSql.orderByStartIndex == -1) {
                    System.out.println("no order by");
                } else {
                    System.out.println("'" + string.substring(oracleSql.orderByStartIndex, oracleSql.orderByEndIndex) + "'");
                }
                if (oracleSql.whereStartIndex == -1) {
                    System.out.println("no where");
                } else {
                    System.out.println("'" + string.substring(oracleSql.whereStartIndex, oracleSql.whereEndIndex) + "'");
                }
                if (oracleSql.forUpdateStartIndex == -1) {
                    System.out.println("no for update");
                } else {
                    System.out.println("'" + string.substring(oracleSql.forUpdateStartIndex, oracleSql.forUpdateEndIndex) + "'");
                }
                System.out.println("isPlsqlOrCall(): " + oracleSql.getSqlKind().isPlsqlOrCall());
                System.out.println("isDML(): " + oracleSql.getSqlKind().isDML());
                System.out.println("isSELECT(): " + oracleSql.getSqlKind().isSELECT());
                System.out.println("isOTHER(): " + oracleSql.getSqlKind().isOTHER());
                System.out.println("\"" + string2 + "\"");
                System.out.println("\"" + oracleSql.getRevisedSql() + "\"");
                System.out.println("\"" + oracleSql.getRefetchSql() + "\"");
                String[] stringArray4 = new String[]{string2};
                OracleParameterMetaDataParser.main(stringArray4);
            }
            catch (Exception exception) {
                System.out.println(exception);
            }
        }
    }

    private static final void dumpTransitionMatrix(String string) {
        try {
            PrintWriter printWriter = new PrintWriter(string);
            printWriter.print(",");
            for (int i2 = 0; i2 < 128; ++i2) {
                printWriter.print("'" + (i2 < 32 ? "0x" + Integer.toHexString(i2) : Character.toString((char)i2)) + (i2 < 127 ? "'," : "'"));
            }
            printWriter.println();
            int[][] nArray = OracleSqlReadOnly.TRANSITION;
            String[] stringArray = OracleSqlReadOnly.PARSER_STATE_NAME;
            for (int i3 = 0; i3 < TRANSITION.length; ++i3) {
                printWriter.print(stringArray[i3] + ",");
                for (int i4 = 0; i4 < nArray[i3].length; ++i4) {
                    printWriter.print(stringArray[nArray[i3][i4]] + (i4 < 127 ? "," : ""));
                }
                printWriter.println();
            }
            printWriter.close();
        }
        catch (Throwable throwable) {
            System.err.println(throwable);
        }
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }

    int getReturnParameterCount() throws SQLException {
        if (this.sqlKind == OracleStatement.SqlKind.UNINITIALIZED) {
            this.computeBasicInfo(this.parameterSql);
        }
        if (!this.sqlKind.isDML()) {
            return -1;
        }
        return this.returningIntoParameterCount;
    }

    private int getSubstrPos(String string, String string2) throws SQLException {
        int n2;
        int n3 = -1;
        int n4 = string.indexOf(string2);
        if (n4 >= 1 && Character.isWhitespace(string.charAt(n4 - 1)) && (n2 = n4 + string2.length()) < string.length() && Character.isWhitespace(string.charAt(n2))) {
            n3 = n4;
        }
        return n3;
    }

    static enum ParseMode {
        NORMAL,
        SCALAR,
        LOCATE_1,
        LOCATE_2;

    }
}

