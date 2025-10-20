/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import oracle.jdbc.dcn.DatabaseChangeRegistration;

public interface OracleStatement
extends Statement {
    public static final int NEW = 0;
    public static final int IMPLICIT = 1;
    public static final int EXPLICIT = 2;

    public void clearDefines() throws SQLException;

    public void defineColumnType(int var1, int var2) throws SQLException;

    public void defineColumnType(int var1, int var2, int var3) throws SQLException;

    public void defineColumnType(int var1, int var2, int var3, short var4) throws SQLException;

    public void defineColumnTypeBytes(int var1, int var2, int var3) throws SQLException;

    public void defineColumnTypeChars(int var1, int var2, int var3) throws SQLException;

    public void defineColumnType(int var1, int var2, String var3) throws SQLException;

    public int getRowPrefetch();

    public void setRowPrefetch(int var1) throws SQLException;

    public int getLobPrefetchSize() throws SQLException;

    public void setLobPrefetchSize(int var1) throws SQLException;

    public void closeWithKey(String var1) throws SQLException;

    public int creationState();

    public boolean isNCHAR(int var1) throws SQLException;

    public void setDatabaseChangeRegistration(DatabaseChangeRegistration var1) throws SQLException;

    public String[] getRegisteredTableNames() throws SQLException;

    public long getRegisteredQueryId() throws SQLException;

    @Override
    public void closeOnCompletion() throws SQLException;

    default public String enquoteLiteral(String string) throws SQLException {
        return "'" + string.replace("'", "''") + "'";
    }

    default public String enquoteNCharLiteral(String string) throws SQLException {
        return "'" + string.replace("'", "''") + "'";
    }

    default public boolean isSimpleIdentifier(String string) throws SQLException {
        boolean bl = false;
        int n2 = string.length();
        if (n2 >= 1 && n2 <= 128 && Pattern.compile("[\\p{Alpha}][\\p{Alnum}_]+").matcher(string).matches()) {
            bl = true;
        }
        return bl;
    }

    default public String enquoteIdentifier(String string, boolean bl) throws SQLException {
        int n2 = string.length();
        if (n2 < 1 || n2 > 128) {
            throw new SQLException("Invalid name");
        }
        if (Pattern.compile("^[\\p{Alpha}][\\p{Alnum}_]+$").matcher(string).matches()) {
            return bl ? "\"" + string + "\"" : string;
        }
        if (string.matches("^\".+\"$")) {
            string = string.substring(1, n2 - 1);
        }
        if (Pattern.compile("[^\u0000\"]+").matcher(string).matches()) {
            return "\"" + string + "\"";
        }
        throw new SQLException("Invalid name");
    }
}

