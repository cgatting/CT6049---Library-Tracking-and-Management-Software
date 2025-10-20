/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public interface OracleTranslatingConnection {
    public Statement createStatement(boolean var1) throws SQLException;

    public Statement createStatement(int var1, int var2, boolean var3) throws SQLException;

    public Statement createStatement(int var1, int var2, int var3, boolean var4) throws SQLException;

    public PreparedStatement prepareStatement(String var1, boolean var2) throws SQLException;

    public PreparedStatement prepareStatement(String var1, int var2, boolean var3) throws SQLException;

    public PreparedStatement prepareStatement(String var1, int[] var2, boolean var3) throws SQLException;

    public PreparedStatement prepareStatement(String var1, String[] var2, boolean var3) throws SQLException;

    public PreparedStatement prepareStatement(String var1, int var2, int var3, boolean var4) throws SQLException;

    public PreparedStatement prepareStatement(String var1, int var2, int var3, int var4, boolean var5) throws SQLException;

    public CallableStatement prepareCall(String var1, boolean var2) throws SQLException;

    public CallableStatement prepareCall(String var1, int var2, int var3, boolean var4) throws SQLException;

    public CallableStatement prepareCall(String var1, int var2, int var3, int var4, boolean var5) throws SQLException;

    public Map<SqlTranslationVersion, String> getSqlTranslationVersions(String var1, boolean var2) throws SQLException;

    public static enum SqlTranslationVersion {
        ORIGINAL_SQL,
        JDBC_MARKER_CONVERTED,
        TRANSLATED;

    }
}

