/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleParameterMetaDataParserStates;
import oracle.jdbc.driver.OracleSql;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
public class OracleParameterMetaDataParser {
    static final int UNINITIALIZED = -1;
    static final String[] EMPTY_LIST = new String[0];
    String parameterSql;
    OracleStatement.SqlKind sqlKind = OracleStatement.SqlKind.UNINITIALIZED;
    int parameterCount = -1;
    boolean needToParseSql;
    private static final int cMax = 127;
    private static final int[][] TRANSITION = OracleParameterMetaDataParserStates.TRANSITION;
    private static final int[][] ACTION = OracleParameterMetaDataParserStates.ACTION;
    private static final int NO_ACTION = 0;
    private static final int WHERE_ACTION = 2;
    private static final int PARAMETER_ACTION = 3;
    private static final int END_PARAMETER_ACTION = 4;
    private static final int COUNT_BIND_ACTION = 5;
    private static final int START_NCHAR_LITERAL_ACTION = 6;
    private static final int END_NCHAR_LITERAL_ACTION = 7;
    private static final int SAVE_DELIMITER_ACTION = 8;
    private static final int LOOK_FOR_DELIMITER_ACTION = 9;
    private static final int RECORD_TABLE_NAME_ACTION = 10;
    private static final int END_RECORD_TABLE_NAME_ACTION = 11;
    private static final int DONE_RECORD_TABLE_NAME_ACTION = 12;
    private static final int START_RECORD_COLUMN_NAME_ACTION = 13;
    private static final int RECORD_COLUMN_NAME_ACTION = 14;
    private static final int END_RECORD_COLUMN_NAME_ACTION = 15;
    private static final int DONE_RECORD_COLUMN_NAME_ACTION = 16;
    private static final int NO_PARAMETER_METADATA_ACTION = 17;
    private static final int BEGIN_COMMENT_ACTION = 18;
    private static final int END_COMMENT_ACTION = 19;
    private static final int RESET_RECORDING_ACTION = 20;
    private static final int START_JSON_ACTION = 21;
    private static final int END_JSON_ACTION = 22;
    private static final int INITIAL_STATE = 0;
    private static final int RESTART_STATE = 22;
    private static final boolean DEBUG_CBI = false;
    ArrayList<String> tableName = new ArrayList();
    ArrayList<String> columnName = new ArrayList();
    byte[] bindStatusForInsert = null;
    char[] currentParameter = null;

    protected OracleParameterMetaDataParser() {
    }

    protected void initialize(String string, OracleStatement.SqlKind sqlKind, int n2) throws SQLException {
        if (string == null || string.length() == 0) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 104).fillInStackTrace();
        }
        this.sqlKind = sqlKind;
        this.parameterSql = string;
        this.parameterCount = n2;
        this.needToParseSql = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    void computeBasicInfo(String string) throws SQLException {
        int n2 = 0;
        boolean bl = false;
        int n3 = 0;
        int n4 = 0;
        int n5 = string.length();
        int n6 = n5 + 1;
        char[] cArray = new char[512];
        int n7 = 0;
        int n8 = 0;
        this.columnName.clear();
        this.tableName.clear();
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        int n9 = 0;
        int n10 = 0;
        boolean bl5 = true;
        int n11 = 0;
        ArrayDeque<Character> arrayDeque = new ArrayDeque<Character>();
        this.bindStatusForInsert = null;
        block20: for (int i2 = 0; i2 < n6; ++i2) {
            char c2;
            char c3 = c2 = i2 < n5 ? (char)string.charAt(i2) : (char)' ';
            if (c2 > '\u007f') {
                c3 = Character.isLetterOrDigit(c2) ? (char)'X' : ' ';
            }
            switch (ACTION[n4][c3]) {
                case 18: {
                    n11 = n4;
                    n4 = c3 == '/' ? 18 : 20;
                    break;
                }
                case 19: {
                    n4 = c3 == '/' ? 19 : 20;
                    n11 = 0;
                    continue block20;
                }
                case 20: {
                    n8 = 0;
                    n7 = 0;
                    n11 = 68;
                    break;
                }
                case 0: {
                    break;
                }
                case 2: {
                    if (n7 <= 0) break;
                    n7 -= 5;
                    break;
                }
                case 3: {
                    Object object;
                    if (n8 > 0 && ((String)(object = new String(cArray, 0, n8))).trim().length() > 0) {
                        this.columnName.add((String)object);
                    }
                    n8 = 0;
                    if (n3 == 0) {
                        n9 = i2;
                    }
                    ++n3;
                    bl2 = true;
                    break;
                }
                case 5: {
                    Object object;
                    if (bl2) {
                        if (this.bindStatusForInsert == null) {
                            int n12 = Math.max(50, n2 >> 1);
                            this.bindStatusForInsert = new byte[n12];
                        } else if (n2 >= this.bindStatusForInsert.length) {
                            object = new byte[this.bindStatusForInsert.length << 1];
                            System.arraycopy(this.bindStatusForInsert, 0, object, 0, this.bindStatusForInsert.length);
                            this.bindStatusForInsert = (byte[])object;
                        }
                        this.bindStatusForInsert[n2] = 1;
                    }
                    n10 = i2;
                    ++n2;
                    bl2 = false;
                }
                case 4: {
                    bl2 = false;
                    n3 = 0;
                    break;
                }
                case 10: {
                    cArray = OracleParameterMetaDataParser.checkAndResizeBuffer(cArray, n7);
                    cArray[n7++] = c2;
                    break;
                }
                case 11: {
                    Object object;
                    if (n7 > 0 && ((String)(object = new String(cArray, 0, n7).trim())).length() > 0) {
                        this.tableName.add((String)object);
                    }
                    n7 = 0;
                    break;
                }
                case 12: {
                    if (n7 <= 0) break;
                    Object object = new String(cArray, 0, n7).trim();
                    if (((String)object).length() > 0) {
                        this.tableName.add((String)object);
                    }
                    n7 = 0;
                    break;
                }
                case 13: {
                    Object object;
                    if (n7 > 0) {
                        object = new String(cArray, 0, n7).trim();
                        if (((String)object).length() > 0) {
                            this.tableName.add((String)object);
                        }
                        n7 = 0;
                    }
                    if (this.tableName.isEmpty()) {
                        bl5 = false;
                        break;
                    }
                    if (this.sqlKind == OracleStatement.SqlKind.INSERT) break;
                    n8 = 0;
                    cArray = OracleParameterMetaDataParser.checkAndResizeBuffer(cArray, n8);
                    cArray[n8++] = c2;
                    break;
                }
                case 14: {
                    cArray = OracleParameterMetaDataParser.checkAndResizeBuffer(cArray, n8);
                    cArray[n8++] = c2;
                    break;
                }
                case 15: {
                    Object object;
                    if (!bl5) break;
                    if (n8 > 0 && ((String)(object = new String(cArray, 0, n8))).trim().length() > 0) {
                        this.columnName.add((String)object);
                    }
                    n8 = 0;
                    break;
                }
                case 16: {
                    Object object;
                    if (!bl5) break;
                    if (n8 > 0 && ((String)(object = new String(cArray, 0, n8))).trim().length() > 0) {
                        this.columnName.add((String)object);
                    }
                    n8 = 0;
                    break;
                }
                case 17: {
                    bl5 = false;
                    break;
                }
                case 21: {
                    arrayDeque.push(Character.valueOf(c2 == '{' ? (char)'}' : ']'));
                    break;
                }
                case 22: {
                    if (arrayDeque.isEmpty()) break;
                    if (((Character)arrayDeque.peek()).charValue() == c2) {
                        arrayDeque.pop();
                    }
                    bl2 = false;
                    n3 = 0;
                }
            }
            n4 = TRANSITION[n4][c3];
        }
        if (bl5) {
            if (this.sqlKind == OracleStatement.SqlKind.INSERT && n10 < n9) {
                if (this.bindStatusForInsert == null) {
                    this.bindStatusForInsert = new byte[50];
                } else if (n2 >= this.bindStatusForInsert.length) {
                    byte[] byArray = new byte[this.bindStatusForInsert.length << 1];
                    System.arraycopy(this.bindStatusForInsert, 0, byArray, 0, this.bindStatusForInsert.length);
                    this.bindStatusForInsert = byArray;
                }
                this.bindStatusForInsert[n2++] = 1;
            }
        } else {
            if (!this.tableName.isEmpty()) {
                this.tableName.clear();
            }
            if (!this.columnName.isEmpty()) {
                this.columnName.clear();
            }
            this.bindStatusForInsert = null;
            n2 = -1;
        }
        this.needToParseSql = false;
    }

    private static char[] checkAndResizeBuffer(char[] cArray, int n2) {
        if (cArray.length <= n2) {
            int n3 = cArray.length * 2;
            char[] cArray2 = new char[n3];
            System.arraycopy(cArray, 0, cArray2, 0, cArray.length);
            cArray = cArray2;
        }
        return cArray;
    }

    String[] getColumnNames() {
        String[] stringArray = new String[this.columnName.size()];
        return this.columnName.toArray(stringArray);
    }

    String[] getTableNames() {
        String[] stringArray = new String[this.tableName.size()];
        return this.tableName.toArray(stringArray);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getParameterMetaDataSql() throws SQLException {
        int n2;
        if (this.needToParseSql) {
            this.computeBasicInfo(this.parameterSql);
        }
        if (this.sqlKind.isPlsqlOrCall() || this.parameterCount == 0) {
            return null;
        }
        String[] stringArray = this.getTableNames();
        if (stringArray == null || stringArray.length == 0) {
            return null;
        }
        String[] stringArray2 = this.getColumnNames();
        StringBuilder stringBuilder = new StringBuilder(100);
        stringBuilder.append("SELECT ");
        if (stringArray2.length == 0) {
            if (this.sqlKind != OracleStatement.SqlKind.INSERT) return null;
            stringBuilder.append("* ");
        } else {
            n2 = this.bindStatusForInsert != null ? 1 : 0;
            int n3 = 0;
            for (int i2 = 0; i2 < stringArray2.length; ++i2) {
                if (n2 != 0 && this.bindStatusForInsert[i2] != 1) continue;
                if (n3++ != 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(stringArray2[i2]);
            }
        }
        stringBuilder.append(" FROM ");
        for (n2 = 0; n2 < stringArray.length; ++n2) {
            if (n2 != 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(stringArray[n2]);
        }
        return stringBuilder.substring(0, stringBuilder.length());
    }

    boolean needBindStatusForParameterMetaData() throws SQLException {
        return this.columnName.size() > 0 && this.parameterCount > 0 && this.bindStatusForInsert != null;
    }

    byte[] getBindStatusForInsert() {
        return this.bindStatusForInsert;
    }

    public static void main(String[] stringArray) {
        String[] stringArray2 = null;
        if (stringArray.length < 1) {
            System.err.println("ERROR: incorrect usage. OracleParameterMetaDataParser <-test| sql >");
            return;
        }
        stringArray2 = "-test".equals(stringArray[0]) ? new String[]{"insert into JAVA_KEYWORDS (\"ABSTRACT\",\"ASSERT\",\"BOOLEAN\",\"BREAK\",\"BYTE\",\"CASE\",\"CATCH\",\"CHAR\",\"CLASS\",\"CONST \",\"CONTINUE\",\"DEFAULT\",\"DO\",\"DOUBLE\",\"ELSE\",\"ENUM \",\"EXTENDS\",\"FINAL\",\"FINALLY\",\"FLOAT\",\"FOR\",\"GOTO \",\"IF\",\"IMPLEMENTS\",\"IMPORT\",\"INSTANCEOF\",\"INT\",\"INTERFACE\",\"LONG\",\"NATIVE\",\"NEW\",\"PACKAGE\",\"PRIVATE\",\"PROTECTED\",\"PUBLIC\",\"RETU RN\",\"SHORT\",\"STATIC\",\"STRICTFP \",\"SUPER\",\"SWITCH\",\"SYNCHRONIZED\",\"THIS\",\"THROW\",\"THROWS\",\"TRANS IENT\",\"TRY\",\"VOID\",\"VOLATILE\",\"WHILE\", \"ID\") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?  ,?,?,?,?,?,?,?,?,?,?,?,?)", "INSERT INTO emp(empno,ename,sal) VALUES(:B1, :b2, :b3)", "INSERT INTO T1 VALUES(:BIND1, :bind2)", "begin INSERT INTO T1 VALUES(:BIND1, :bind2); end;", "UPDATE T1 SET  C1 = :B1 and c2 = :b2 and c3 = 'abc'", "UPDATE T1 SET C1 = :B1 and  c2 = :b2 and c3 = 'abc'", "UPDATE T1 SET    C1 = :B1 and    c2 = :b2 and    c3 = 'abc' and c4 = :b4", "SELECT ename from emp where empno = :a1 and sal = :a2", "DELETE FROM EMP WHERE EMPNO>:x", "DELETE FROM EMP WHERE EMPNO   >   :1", "DELETE FROM EMP WHERE EMPNO\n>\n:2", "DELETE FROM EMP WHERE EMPNO\n<>\n:3", "DELETE FROM EMP WHERE EMPNO\n<>\n'abc'", "SELECT ename, d.deptno from emp e, dept d where empno = ?  and sal = ? and e.deptno = d.deptno", "SELECT ename, d.deptno from emp e, dept d where empno = :a1 and sal = :a2 and e.deptno = d.deptno", "SELECT ename, deptno   from    emp   , dept    where    empno =    :a1 and   sal = :a2", "SELECT * FROM TKPJST58_TAB WHERE C1 = :2", "SELECT * FROM TKPJST58_TAB WHERE C1 is null and c2 = :1 and c3 = :4", "SELECT * FROM TKPJST58_TAB WHERE C1 is NULL  AND C2 = :1   AND C3 = :2   AND C4 = :3   AND C5 = :4   AND C6 = :5   AND C7 = :6   AND C8 = :7   AND C9 = :8   AND C10 = :9   AND C11 = :10   AND C12 = :11   AND C13 = :12   AND C14 = :13   AND C15 = :14   AND C16 is not null  AND C17 <> :15", "SELECT * FROM TKPJST58_TAB WHERE C1 = ?  AND C2 = ?  AND C3 = ?  AND C4 = ?  AND C5 = ?  AND C6 = ?  AND C7 = ?  AND C8 = ?  AND C9 = ?  AND C10 = ?  AND C11 = ?  AND C12 = ?  AND C13 = ?  AND C14 = ?  AND C15 = ?  AND C16 = ?  AND C17 = ?", "INSERT INTO TKPJST58_TAB(c1, c2, c3, c4, c5, c9, c14, c10) values (?,?,?,?,?,?,?,?)", "INSERT INTO TKPJST58_TAB values (12,'abc',?,?,?,?,?,?)", "INSERT INTO TKPJST58_TAB values (12,'abc',:1,:2,:3,:4,:5)", "INSERT INTO TKPJST58_TAB(c1,c2,c3,c4,c5,c6,c7) values (12,'abc',:1,:2,:3,:4,:5)", "INSERT INTO TKPJST58_TAB(c1,c2,c3,c4,c5,c6,c7) values (12,'abc',:1,:2,55,:4,:5)", "insert into rawtab values ('010203040506', '0708090a0b0c0d')", "begin insert into asciitab values (200,'21-sep-71',?,?,?); end;", "select col from dummy_tab where rowid=?", "SELECT * FROM test2 WHERE key >= ? ORDER BY key", "SELECT * FROM test2 WHERE key>=? ORDER BY key", "INSERT INTO tkpjb2354325_tab VALUES (111, {ts '1999-12-31 12:59:59'})", "SELECT user FROM dual WHERE  ? < { fn LOCATE('TEST123TEST', 1) }", "INSERT INTO tkpjb2354325_tab VALUES (111, {ts '1999-12-31 12:59:59'}, :3)", "delete from tkpjdg02_view where id >? returning id, name into ?, ?", "SELECT * FROM TABLE( CAST(? AS TYPE_VARCHAR_NT) )", "insert into (select t.col1 as column1, t.col2 as column2 from tkpjsc37 t  where t.col1 in (?,?,?,?)) values ( ?, ?)", "delete from tkpjdg02_view where id >? returning_id = ?", "insert into tkpjir93_tab values (?,q'!name LIKE '%DBMS_%%'!')", "insert into tkpjir93_tab values (?,q'{SELECT * FROM employees WHERE last_name = 'Smith';}'", "insert into xml_test values ('adf', '<?xml version=\"1.0\" encoding=\"UTF-8\"?><a></a>')", "SELECT * FROM test2 WHERE key>=? and ORDER_id=?  order BY key", "insert into emp(empno, ename, sal) values (?, N'abc', ?)", "UPDATE tkpjb5752856_tab SET c2=N'????C Mother''s Maiden Name????'", "INSERT INTO TKPJST58_TAB(c1, c2, c3, c4, c5, c9, c14, c10) values (12,'abc',?,?,?,?,?,?)", "UPDATE /*abc*/T1 SET/*xyz*/ C1 = :B1 /*nyl*/and/*bac*/ c2 = :b2 and c3 = 'abc'", "SELECT * FROM TKPJST58_TAB WHERE C1 is/*abc*/ null and c2 = :1 and c3 = :4", "SELECT * FROM TKPJST58_TAB WHERE C1 is/*abc*/not--xyz\n null and c2 = :1 and c3 = :4", "UPDATE TKPJST58_TAB/*comment1*/set/*comment2*/ C1 = ?  WHERE  C4 = /*abc*/? ", "UPDATE TKPJST58_TAB set C1 = ?  and c2 = ? WHERE  C4 = /*abc*/? and c5 = ?"} : stringArray;
        for (String string : stringArray2) {
            try {
                OracleSql oracleSql = new OracleSql(null);
                oracleSql.initialize(string);
                String string2 = oracleSql.getSql(true, true);
                System.out.println("SQL:" + string2);
                System.out.println("  SqlKind:" + (Object)((Object)oracleSql.sqlKind) + ", Parameter Count=" + oracleSql.parameterCount);
                if (!oracleSql.sqlKind.isPlsqlOrCall() && oracleSql.parameterCount > 0) {
                    OracleParameterMetaDataParser oracleParameterMetaDataParser = new OracleParameterMetaDataParser();
                    oracleParameterMetaDataParser.initialize(string2, oracleSql.sqlKind, oracleSql.parameterCount);
                    System.out.println("  Parameter SQL: " + oracleParameterMetaDataParser.getParameterMetaDataSql());
                } else {
                    System.out.println("  Cannot get Parameter MetaData");
                }
                System.out.println("\n");
            }
            catch (Exception exception) {
                System.out.println(exception);
                exception.printStackTrace();
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
            int[][] nArray = OracleParameterMetaDataParserStates.TRANSITION;
            String[] stringArray = OracleParameterMetaDataParserStates.PARSER_STATE_NAME;
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
}

