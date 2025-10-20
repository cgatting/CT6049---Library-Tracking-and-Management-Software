/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Executable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import oracle.jdbc.driver.ClioSupport;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Log;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONNECT})
class PropertiesFileUtil {
    private static final String DEFAULT_FILE_NAME = "ojdbc.properties";
    private static final String ALIAS_FILE_PREFIX = "ojdbc_";
    private static final String PROPERTIES_EXTENSION = ".properties";
    private static final String DEFAULT_PATH_LIST_ENTRY = "default";
    private static final String[] DEFAULT_PATH_LIST = new String[]{"default"};
    private static final String FILE_SCHEME = "file://";
    private static final String PREFIXED_CONNECTION_PROPERTY_DATABASE = "oracle.jdbc.database";
    private static final int MAX_PATH_LENGTH = 40000;
    private static final String MAX_PATH_LENGTH_MSG = "File path is too long";
    private static final int MAX_FILES = 20;
    private static final String MAX_FILES_MSG = "Too many files";
    private static final int MAX_FILE_SIZE = 1000000;
    private static final String MAX_FILE_SIZE_MSG = "File is too large";
    private static final String NULL_CHARACTER_PATH_MSG = "Path contains the null character";
    private static final String DOES_NOT_EXIST_MSG = "File does not exist";
    private static final String NOT_REGULAR_MSG = "Not a regular file";
    private static final Pattern VALID_TNS_ALIAS = Pattern.compile("\\w+");

    private PropertiesFileUtil() {
    }

    @Blind(value=PropertiesBlinder.class)
    static Properties loadPropertiesFromFile(String string, String string2, boolean bl, String string3, boolean bl2) throws SQLException {
        String[] stringArray;
        Properties properties = null;
        try {
            stringArray = PropertiesFileUtil.getFileList(string);
        }
        catch (IOException iOException) {
            SQLException sQLException = DatabaseError.createSqlException(1700);
            sQLException.initCause(iOException).fillInStackTrace();
            throw sQLException;
        }
        int n2 = stringArray.length;
        while (n2 > 0) {
            String string4;
            if ((string4 = stringArray[--n2].trim()).isEmpty()) continue;
            try {
                properties = string4.equals(DEFAULT_PATH_LIST_ENTRY) ? PropertiesFileUtil.loadDefaultFiles(string2, string3, bl2, properties) : PropertiesFileUtil.loadUserFile(string4, string2, properties);
            }
            catch (IOException iOException) {
                SQLException sQLException = DatabaseError.createSqlException(1700, string4);
                sQLException.initCause(iOException).fillInStackTrace();
                throw sQLException;
            }
            if (!bl && string2 != null || properties == null) continue;
            string2 = properties.getProperty("oracle.net.tns_admin", string2);
        }
        return properties;
    }

    private static final String[] getFileList(String string) throws IOException {
        String[] stringArray;
        if (string == null) {
            stringArray = DEFAULT_PATH_LIST;
        } else {
            if (string.length() > 40000) {
                throw new IOException(MAX_PATH_LENGTH_MSG);
            }
            stringArray = string.split(",");
            if (stringArray.length > 20) {
                throw new IOException(MAX_FILES_MSG);
            }
        }
        return stringArray;
    }

    @Blind(value=PropertiesBlinder.class)
    private static Properties loadDefaultFiles(String string, String string2, boolean bl, @Blind(value=PropertiesBlinder.class) Properties properties) throws SQLException, IOException {
        Properties properties2 = null;
        if (string != null && !(string = string.trim()).isEmpty()) {
            Path path = PropertiesFileUtil.validatePath(false, string, DEFAULT_FILE_NAME);
            if (path == null) {
            } else {
                properties2 = PropertiesFileUtil.loadProperties(false, path);
                if (properties2 != null) {
                    PropertiesFileUtil.filterTnsAdmin(properties2, DEFAULT_FILE_NAME);
                    PropertiesFileUtil.processExpressions(properties2, string);
                }
            }
            if (bl || string2 == null) {
                string2 = PropertiesFileUtil.resolveConnectIdentifier(properties, properties2, string2);
            }
            properties = PropertiesFileUtil.loadAliasedFile(string, string2, properties);
        }
        return PropertiesFileUtil.mergeProperties(properties, properties2);
    }

    @Blind(value=PropertiesBlinder.class)
    private static Properties loadAliasedFile(String string, String string2, @Blind(value=PropertiesBlinder.class) Properties properties) throws IOException, SQLException {
        Properties properties2 = null;
        if (string2 != null && !(string2 = string2.trim()).isEmpty()) {
            if (string2.length() > 40000) {
            } else if (!VALID_TNS_ALIAS.matcher(string2).matches()) {
            } else {
                String string3 = string2;
                Path path = PropertiesFileUtil.validatePath(false, string, ALIAS_FILE_PREFIX + string3 + PROPERTIES_EXTENSION);
                if (path == null) {
                } else {
                    properties2 = PropertiesFileUtil.loadProperties(false, path);
                    if (properties2 != null) {
                        String string4 = path.getFileName().toString();
                        PropertiesFileUtil.filterTnsAdmin(properties2, string4);
                        PropertiesFileUtil.filterConnectIdentifier(properties2, string4);
                        PropertiesFileUtil.processExpressions(properties2, string);
                    }
                }
            }
        }
        return PropertiesFileUtil.mergeProperties(properties, properties2);
    }

    @Blind(value=PropertiesBlinder.class)
    private static Properties loadUserFile(String string, String string2, @Blind(value=PropertiesBlinder.class) Properties properties) throws IOException, SQLException {
        Path path = string.regionMatches(true, 0, FILE_SCHEME, 0, FILE_SCHEME.length()) ? PropertiesFileUtil.validateURI(string) : PropertiesFileUtil.validatePath(true, string, new String[0]);
        Properties properties2 = PropertiesFileUtil.loadProperties(true, path);
        PropertiesFileUtil.processExpressions(properties2, string2);
        return PropertiesFileUtil.mergeProperties(properties, properties2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Blind(value=PropertiesBlinder.class)
    private static Properties loadProperties(boolean bl, Path path) throws IOException {
        try {
            if (!PropertiesFileUtil.validateFile(bl, path)) {
                return null;
            }
            try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);){
                Properties properties = new Properties();
                properties.load(inputStream);
                Properties properties2 = properties;
                return properties2;
            }
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new IOException(illegalArgumentException);
        }
    }

    private static boolean validateFile(boolean bl, Path path) throws IOException {
        IOException iOException = null;
        if (!Files.exists(path, new LinkOption[0])) {
            iOException = new IOException(DOES_NOT_EXIST_MSG);
        } else if (!Files.isRegularFile(path, new LinkOption[0])) {
            iOException = new IOException(NOT_REGULAR_MSG);
        } else if (Files.size(path) > 1000000L) {
            iOException = new IOException(MAX_FILE_SIZE_MSG);
        }
        if (iOException == null) {
            return true;
        }
        if (bl) {
            throw iOException;
        }
        return false;
    }

    private static Path validatePath(boolean bl, String string, String ... stringArray) throws IOException {
        IOException iOException = null;
        String string2 = string;
        int n2 = 0;
        int n3 = 0;
        while (iOException == null && string2 != null) {
            if (string2.indexOf(0) >= 0) {
                iOException = new IOException(NULL_CHARACTER_PATH_MSG);
                break;
            }
            if ((n3 += string2.length()) > 40000) {
                iOException = new IOException(MAX_PATH_LENGTH_MSG);
                break;
            }
            string2 = stringArray != null && n2 < stringArray.length ? stringArray[n2++] : null;
        }
        if (iOException == null) {
            return Paths.get(string, stringArray);
        }
        if (bl) {
            throw iOException;
        }
        return null;
    }

    private static Path validateURI(String string) throws IOException {
        if (string.indexOf(0) >= 0) {
            throw new IOException(NULL_CHARACTER_PATH_MSG);
        }
        if (string.length() > 40000) {
            throw new IOException(MAX_PATH_LENGTH_MSG);
        }
        return Paths.get(URI.create(string));
    }

    @Blind(value=PropertiesBlinder.class)
    private static Properties mergeProperties(@Blind(value=PropertiesBlinder.class) Properties properties, @Blind(value=PropertiesBlinder.class) Properties properties2) {
        if (properties2 == null) {
            return properties;
        }
        if (properties == null) {
            return properties2;
        }
        properties2.putAll(properties);
        return properties2;
    }

    private static void filterTnsAdmin(@Blind(value=PropertiesBlinder.class) Properties properties, String string) {
        if (null != properties.remove("oracle.net.tns_admin")) {
        }
    }

    private static void filterConnectIdentifier(@Blind(value=PropertiesBlinder.class) Properties properties, String string) {
        if (null != properties.remove("database")) {
        }
        if (null != properties.remove(PREFIXED_CONNECTION_PROPERTY_DATABASE)) {
        }
    }

    private static String resolveConnectIdentifier(@Blind(value=PropertiesBlinder.class) Properties properties, @Blind(value=PropertiesBlinder.class) Properties properties2, String string) {
        String string2 = null;
        if (properties != null && (string2 = properties.getProperty("database")) == null) {
            string2 = properties.getProperty(PREFIXED_CONNECTION_PROPERTY_DATABASE);
        }
        if (string2 == null && properties2 != null && (string2 = properties2.getProperty("database")) == null) {
            string2 = properties2.getProperty(PREFIXED_CONNECTION_PROPERTY_DATABASE);
        }
        return string2 == null ? string : string2;
    }

    private static void processExpressions(@Blind(value=PropertiesBlinder.class) Properties properties, String string) throws SQLException {
        if (properties == null || properties.isEmpty()) {
            return;
        }
        Set<String> set = properties.stringPropertyNames();
        String[] stringArray = new String[set.size()];
        Arrays.sort(set.toArray(stringArray));
        for (String string2 : stringArray) {
            String string3 = properties.getProperty(string2);
            try {
                properties.setProperty(string2, Interpreter.interpret(string3, string));
            }
            catch (Exception exception) {
                SQLException sQLException = DatabaseError.createSqlException(1701, string2);
                sQLException.initCause(exception).fillInStackTrace();
                throw sQLException;
            }
        }
    }

    @Log
    private static void debug(Logger logger, Level level, Executable executable, String string) {
        ClioSupport.log(logger, level, PropertiesFileUtil.class, executable, string);
    }

    private static class Interpreter {
        private static final char DOLLAR = '$';
        private static final char QUESTION_MARK = '?';
        private static final char OPEN_BRACE = '{';
        private static final char CLOSE_BRACE = '}';
        private static final String FILE_SEPERATOR_PROPERTY = "/";
        private static final String ORACLE_HOME = "ORACLE_HOME";
        private static final Object TNS_ADMIN = "TNS_ADMIN";

        private Interpreter() {
        }

        private static String interpret(String string, String string2) throws IOException {
            StringBuilder stringBuilder = new StringBuilder(string.length());
            int n2 = 0;
            block4: while (n2 < string.length()) {
                char c2 = string.charAt(n2);
                switch (c2) {
                    case '$': {
                        n2 = Interpreter.readDollar(string, n2 + 1, stringBuilder, string2);
                        continue block4;
                    }
                    case '?': {
                        n2 = Interpreter.readQuestionMark(string, n2 + 1, stringBuilder);
                        continue block4;
                    }
                }
                stringBuilder.append(c2);
                ++n2;
            }
            return stringBuilder.toString();
        }

        private static int readQuestionMark(String string, int n2, StringBuilder stringBuilder) throws IOException {
            if (n2 < string.length() && '?' == string.charAt(n2)) {
                stringBuilder.append('?');
                return n2 + 1;
            }
            String string2 = System.getenv(ORACLE_HOME);
            if (string2 == null) {
                throw new IOException("Environment variable is not set: ORACLE_HOME. ('?' is interpreted as $ORACLE_HOME)");
            }
            stringBuilder.append(string2);
            return n2;
        }

        private static int readDollar(String string, int n2, StringBuilder stringBuilder, String string2) throws IOException {
            if (n2 == string.length()) {
                stringBuilder.append('$');
                return n2;
            }
            char c2 = string.charAt(n2);
            switch (c2) {
                case '{': {
                    return Interpreter.readDollarExpression(string, n2 + 1, stringBuilder, string2);
                }
                case '$': {
                    stringBuilder.append('$');
                    return n2 + 1;
                }
            }
            stringBuilder.append('$');
            stringBuilder.append(c2);
            return n2 + 1;
        }

        private static int readDollarExpression(String string, int n2, StringBuilder stringBuilder, String string2) throws IOException {
            String string3;
            int n3 = string.indexOf(125, n2);
            if (n3 == n2) {
                throw new IOException("${identifier} expression has a zero length identifier");
            }
            if (n3 == -1) {
                throw new IOException("${identifier} expression is missing a closing brace");
            }
            String string4 = string.substring(n2, n3);
            if (string4.equals(FILE_SEPERATOR_PROPERTY)) {
                string3 = File.separator;
            } else if (string4.equals(TNS_ADMIN)) {
                string3 = string2;
            } else {
                string3 = System.getProperty(string4);
                if (string3 == null) {
                    string3 = System.getenv(string4);
                }
            }
            if (string3 == null) {
                throw new IOException(string4 + " is not defined as a system property or environment variable");
            }
            stringBuilder.append(string3);
            return n3 + 1;
        }
    }
}

