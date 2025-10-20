/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.babelfish;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import oracle.jdbc.babelfish.TranslationCache;
import oracle.jdbc.babelfish.Translator;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.SQL_TRANSLATION})
public class TranslationManager {
    private static final ConcurrentHashMap<String, TranslationCache> translationCacheRegistry = new ConcurrentHashMap();
    private static Map<String, String> defaultErrorFile = new ConcurrentHashMap<String, String>();
    private static final String SEPARATOR = "\u0000";

    public static Translator getTranslator(String string, String string2, String string3, String string4) throws SQLException {
        if (string4 == null && defaultErrorFile.containsKey(string3)) {
            string4 = defaultErrorFile.get(string3);
        }
        File file = null;
        if (string4 != null && !(file = new File(string4)).exists()) {
            throw (SQLException)DatabaseError.createSqlException(277).fillInStackTrace();
        }
        assert (!(".*\u0000.*".matches(string) || ".*\u0000.*".matches(string2) || ".*\u0000.*".matches(string3)));
        String string5 = string + SEPARATOR + string2 + SEPARATOR + string3;
        TranslationCache translationCache = translationCacheRegistry.get(string5);
        if (translationCache == null) {
            translationCache = new TranslationCache(file);
            translationCacheRegistry.putIfAbsent(string5, translationCache);
        }
        Translator translator = new Translator(string3, file, translationCache);
        return translator;
    }
}

