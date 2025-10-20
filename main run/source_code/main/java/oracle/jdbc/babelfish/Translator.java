/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.babelfish;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.babelfish.TranslatedErrorInfo;
import oracle.jdbc.babelfish.TranslationCache;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLevel;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Logging;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.SQL_TRANSLATION})
@DefaultLevel(value=Logging.FINEST)
public class Translator {
    private final File localErrorTranslationFile;
    private final String translationProfile;
    private Connection conn;
    private CallableStatement queryTranslationStatement;
    private CallableStatement errorTranslationStatement;
    private final Map<String, String> queryCache;
    private final Map<Integer, TranslatedErrorInfo> errorCache;
    private final Map<Integer, TranslatedErrorInfo> localErrorCache;

    Translator(String string, File file, TranslationCache translationCache) throws SQLException {
        this.translationProfile = string;
        this.localErrorTranslationFile = file;
        this.queryCache = translationCache.getQueryCache();
        this.errorCache = translationCache.getErrorCache();
        this.localErrorCache = translationCache.getLocalErrorCache();
    }

    public SQLException translateError(SQLException sQLException) throws SQLException {
        if (this.conn == null) {
            return this.translateErrorLocal(sQLException);
        }
        TranslatedErrorInfo translatedErrorInfo = this.errorCache.get(sQLException.getErrorCode());
        if (translatedErrorInfo != null) {
            SQLException sQLException2 = new SQLException("[Translated Error Codes] " + sQLException.getMessage(), translatedErrorInfo.getSqlState(), translatedErrorInfo.getErrorCode(), sQLException);
            sQLException2.setStackTrace(sQLException.getStackTrace());
            return sQLException2;
        }
        try {
            String string;
            this.errorTranslationStatement.clearParameters();
            this.errorTranslationStatement.setInt(1, sQLException.getErrorCode());
            this.errorTranslationStatement.registerOutParameter(2, 4);
            this.errorTranslationStatement.registerOutParameter(3, 12);
            this.errorTranslationStatement.execute();
            int n2 = this.errorTranslationStatement.getInt(2);
            if (this.errorTranslationStatement.wasNull()) {
                n2 = sQLException.getErrorCode();
            }
            if ((string = this.errorTranslationStatement.getString(3)) == null) {
                string = sQLException.getSQLState();
            }
            translatedErrorInfo = new TranslatedErrorInfo(n2, string);
        }
        catch (SQLException sQLException3) {
            throw (SQLException)DatabaseError.createSqlException(null, 280, null, (Throwable)sQLException3).fillInStackTrace();
        }
        this.errorCache.put(sQLException.getErrorCode(), translatedErrorInfo);
        SQLException sQLException4 = new SQLException("[Translated Error Codes] " + sQLException.getMessage(), translatedErrorInfo.getSqlState(), translatedErrorInfo.getErrorCode(), sQLException);
        sQLException4.setStackTrace(sQLException.getStackTrace());
        return sQLException4;
    }

    SQLException translateErrorLocal(SQLException sQLException) throws SQLException {
        if (this.localErrorCache == null) {
            return sQLException;
        }
        TranslatedErrorInfo translatedErrorInfo = this.localErrorCache.get(sQLException.getErrorCode());
        if (translatedErrorInfo != null) {
            String string = "[Translated Error Codes] " + sQLException.getMessage();
            SQLException sQLException2 = new SQLException(string, translatedErrorInfo.getSqlState(), translatedErrorInfo.getErrorCode(), sQLException);
            sQLException2.setStackTrace(sQLException.getStackTrace());
            return sQLException2;
        }
        SQLException sQLException3 = new SQLException("[Error Translation Not Available] " + sQLException.getMessage(), sQLException.getSQLState(), sQLException.getErrorCode(), sQLException);
        sQLException3.setStackTrace(sQLException.getStackTrace());
        return sQLException3;
    }

    String translateQuery(String string) throws SQLException {
        if (this.conn != null) {
            String string2 = this.queryCache.get(string);
            if (string2 != null) {
                return string2;
            }
            String string3 = this.convertParameterMarkersToOracleStyle(string);
            try {
                this.queryTranslationStatement.clearParameters();
                this.queryTranslationStatement.setString(1, string3);
                this.queryTranslationStatement.registerOutParameter(2, 12);
                this.queryTranslationStatement.execute();
                string2 = this.queryTranslationStatement.getString(2);
            }
            catch (SQLException sQLException) {
                throw (SQLException)DatabaseError.createSqlException(null, 280, null, (Throwable)sQLException).fillInStackTrace();
            }
            if (string2 == null) {
                string2 = string3;
            }
            this.queryCache.put(string, string2);
            return string2;
        }
        throw (SQLException)DatabaseError.createSqlException(279).fillInStackTrace();
    }

    public void activateServerTranslation(Connection connection) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall("begin execute immediate 'alter session set sql_translation_profile = ' || sys.dbms_assert.qualified_sql_name(?); end;");
        callableStatement.setString(1, this.translationProfile);
        callableStatement.execute();
        this.queryTranslationStatement = connection.prepareCall("begin sys.dbms_sql_translator.translate_sql(?, ? ); end;");
        this.errorTranslationStatement = connection.prepareCall("begin sys.dbms_sql_translator.translate_error(?, ?, ? ); end;");
        this.conn = connection;
    }

    void deactivateServerTranslation() throws SQLException {
        this.queryTranslationStatement.close();
        this.errorTranslationStatement.close();
        this.conn = null;
    }

    String convertParameterMarkersToOracleStyle(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        int n2 = 1;
        block8: for (int i2 = 0; i2 < string.length(); ++i2) {
            char c2 = string.charAt(i2);
            if (bl2) {
                stringBuilder.append(c2);
                continue;
            }
            switch (c2) {
                case '\\': {
                    stringBuilder.append(c2);
                    if (i2 >= string.length() - 1) continue block8;
                    stringBuilder.append(string.charAt(++i2));
                    continue block8;
                }
                case '?': {
                    if (!bl && !bl3) {
                        stringBuilder.append(":").append(n2++);
                        continue block8;
                    }
                    stringBuilder.append(c2);
                    continue block8;
                }
                case '\'': {
                    if (!bl3) {
                        bl = !bl;
                    }
                    stringBuilder.append(c2);
                    continue block8;
                }
                case '-': {
                    stringBuilder.append(c2);
                    if (i2 >= string.length() - 1 || string.charAt(i2 + 1) != '-') continue block8;
                    stringBuilder.append(string.charAt(++i2));
                    bl2 = true;
                    continue block8;
                }
                case '/': {
                    stringBuilder.append(c2);
                    if (bl || i2 >= string.length() - 1 || string.charAt(i2 + 1) != '*') continue block8;
                    stringBuilder.append(string.charAt(++i2));
                    bl3 = true;
                    continue block8;
                }
                case '*': {
                    stringBuilder.append(c2);
                    if (bl || !bl3 || i2 >= string.length() - 1 || string.charAt(i2 + 1) != '/') continue block8;
                    stringBuilder.append(string.charAt(++i2));
                    bl3 = false;
                    continue block8;
                }
                default: {
                    stringBuilder.append(c2);
                }
            }
        }
        return stringBuilder.toString();
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }
}

