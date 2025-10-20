/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

public class OracleDatabaseException
extends Exception {
    private static final long serialVersionUID = 2L;
    private static boolean DEBUG = false;
    private final int errorPosition;
    private final int oracleErrorNumber;
    private final String sql;
    private final String originalSql;
    private final boolean isSqlRewritten;

    public OracleDatabaseException(int n2, int n3, String string, String string2, String string3) {
        this(n2, n3, string, string2, string3, false);
    }

    public OracleDatabaseException(int n2, int n3, String string, String string2, String string3, boolean bl) {
        super(string);
        this.errorPosition = n2;
        this.oracleErrorNumber = n3;
        this.sql = string2;
        this.originalSql = string3;
        this.isSqlRewritten = bl;
    }

    public int getErrorPosition() {
        return this.errorPosition;
    }

    public int getOracleErrorNumber() {
        return this.oracleErrorNumber;
    }

    public String getSql() {
        return this.sql;
    }

    public String getOriginalSql() {
        return this.originalSql;
    }

    public boolean isSqlRewritten() {
        return this.isSqlRewritten;
    }

    @Override
    public String toString() {
        String string = "Error : " + this.oracleErrorNumber + ", Position : " + this.errorPosition + ", Sql = " + this.sql + ", OriginalSql = " + this.originalSql + ", Error Msg = " + this.getMessage();
        if (this.isSqlRewritten) {
            string = "[SQL INCLUDES EXPRESSIONS ADDED BY THE ORACLE JDBC DRIVER]," + string;
        }
        return string;
    }
}

