/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLClientInfoException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLNonTransientException;
import java.sql.SQLRecoverableException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransactionRollbackException;
import java.sql.SQLTransientConnectionException;
import java.sql.SQLTransientException;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.logging.annotations.DisableTrace;

@DisableTrace
class SQLStateMapping {
    static final SQLStateMapping DEFAULT_SQLSTATE = new SQLStateMapping(Integer.MIN_VALUE, Integer.MAX_VALUE, "99999", SqlExceptionType.SQLEXCEPTION);
    final int low;
    final int high;
    public final String sqlState;
    public final SqlExceptionType exception;
    static final String mappingResource = "errorMap.xml";
    static SQLStateMapping[] all;
    private static final int NUMEBER_OF_MAPPINGS_IN_ERRORMAP_XML = 128;

    public SQLStateMapping(int n2, int n3, String string, SqlExceptionType sqlExceptionType) {
        this.low = n2;
        this.sqlState = string;
        this.exception = sqlExceptionType;
        this.high = n3;
    }

    public boolean isIncluded(int n2) {
        return this.low <= n2 && n2 <= this.high;
    }

    public SQLException newSQLException(String string, int n2) {
        return this.exception.newInstance(string, this.sqlState, n2);
    }

    boolean lessThan(SQLStateMapping sQLStateMapping) {
        if (this.low < sQLStateMapping.low) {
            return this.high < sQLStateMapping.high;
        }
        return this.high <= sQLStateMapping.high;
    }

    @DisableTrace
    public String toString() {
        return super.toString() + "(" + this.low + ", " + this.high + ", " + this.sqlState + ", " + (Object)((Object)this.exception) + ")";
    }

    public static void main(String[] stringArray) throws IOException {
        SQLStateMapping[] sQLStateMappingArray = SQLStateMapping.doGetMappings();
        System.out.println("a\t" + sQLStateMappingArray);
        for (int i2 = 0; i2 < sQLStateMappingArray.length; ++i2) {
            System.out.println("low:\t" + sQLStateMappingArray[i2].low + "\thigh:\t" + sQLStateMappingArray[i2].high + "\tsqlState:\t" + sQLStateMappingArray[i2].sqlState + "\tsqlException:\t" + (Object)((Object)sQLStateMappingArray[i2].exception));
        }
    }

    static SQLStateMapping[] getMappings() {
        if (all == null) {
            try {
                all = SQLStateMapping.doGetMappings();
            }
            catch (Throwable throwable) {
                all = new SQLStateMapping[0];
            }
        }
        return all;
    }

    static SQLStateMapping[] doGetMappings() throws IOException {
        InputStream inputStream = SQLStateMapping.class.getResourceAsStream(mappingResource);
        ArrayList<SQLStateMapping> arrayList = new ArrayList<SQLStateMapping>(128);
        SQLStateMapping.load(inputStream, arrayList);
        return arrayList.toArray(new SQLStateMapping[0]);
    }

    static void load(InputStream inputStream, List<SQLStateMapping> list) throws IOException {
        String string;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        Tokenizer tokenizer = new Tokenizer(bufferedReader);
        int n2 = -1;
        int n3 = -1;
        String string2 = null;
        SqlExceptionType sqlExceptionType = null;
        String string3 = null;
        int n4 = 0;
        block39: while ((string = tokenizer.next()) != null) {
            switch (n4) {
                case 0: {
                    if (!string.equals("<")) continue block39;
                    n4 = 1;
                    continue block39;
                }
                case 1: {
                    if (string.equals("!")) {
                        n4 = 2;
                        continue block39;
                    }
                    if (string.equals("oraErrorSqlStateSqlExceptionMapping")) {
                        n4 = 6;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"oraErrorSqlStateSqlExceptionMapping\".");
                }
                case 2: {
                    if (string.equals("-")) {
                        n4 = 3;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"-\".");
                }
                case 3: {
                    if (!string.equals("-")) continue block39;
                    n4 = 4;
                    continue block39;
                }
                case 4: {
                    if (string.equals("-")) {
                        n4 = 5;
                        continue block39;
                    }
                    n4 = 3;
                    continue block39;
                }
                case 5: {
                    if (string.equals(">")) {
                        n4 = 0;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \">\".");
                }
                case 6: {
                    if (string.equals(">")) {
                        n4 = 7;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \">\".");
                }
                case 7: {
                    if (string.equals("<")) {
                        n4 = 8;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"<\".");
                }
                case 8: {
                    if (string.equals("!")) {
                        n4 = 9;
                        continue block39;
                    }
                    if (string.equals("error")) {
                        n4 = 14;
                        continue block39;
                    }
                    if (string.equals("/")) {
                        n4 = 16;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected one of \"!--\", \"error\", \"/\".");
                }
                case 9: {
                    if (string.equals("-")) {
                        n4 = 10;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"-\".");
                }
                case 10: {
                    if (string.equals("-")) {
                        n4 = 11;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"-\".");
                }
                case 11: {
                    if (!string.equals("-")) continue block39;
                    n4 = 12;
                    continue block39;
                }
                case 12: {
                    if (string.equals("-")) {
                        n4 = 13;
                        continue block39;
                    }
                    n4 = 11;
                    continue block39;
                }
                case 13: {
                    if (string.equals(">")) {
                        n4 = 7;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \">\".");
                }
                case 14: {
                    if (string.equals("/")) {
                        n4 = 15;
                        continue block39;
                    }
                    if (string.equals("oraErrorFrom")) {
                        n4 = 19;
                        continue block39;
                    }
                    if (string.equals("oraErrorTo")) {
                        n4 = 21;
                        continue block39;
                    }
                    if (string.equals("sqlState")) {
                        n4 = 23;
                        continue block39;
                    }
                    if (string.equals("sqlException")) {
                        n4 = 25;
                        continue block39;
                    }
                    if (string.equals("comment")) {
                        n4 = 27;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected one of \"oraErrorFrom\", \"oraErrorTo\", \"sqlState\", \"sqlException\", \"comment\", \"/\".");
                }
                case 15: {
                    if (string.equals(">")) {
                        try {
                            SQLStateMapping.createOne(list, n2, n3, string2, sqlExceptionType, string3);
                        }
                        catch (IOException iOException) {
                            throw new IOException("Invalid error element at line " + tokenizer.lineno + " of errorMap.xml. " + iOException.getMessage());
                        }
                        n2 = -1;
                        n3 = -1;
                        string2 = null;
                        sqlExceptionType = null;
                        string3 = null;
                        n4 = 7;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \">\".");
                }
                case 16: {
                    if (string.equals("oraErrorSqlStateSqlExceptionMapping")) {
                        n4 = 17;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"oraErrorSqlStateSqlExceptionMapping\".");
                }
                case 17: {
                    if (string.equals(">")) {
                        n4 = 18;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \">\".");
                }
                case 18: {
                    continue block39;
                }
                case 19: {
                    if (string.equals("=")) {
                        n4 = 20;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"=\".");
                }
                case 20: {
                    try {
                        n2 = Integer.parseInt(string);
                    }
                    catch (NumberFormatException numberFormatException) {
                        throw new IOException("Unexpected value \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected an integer.");
                    }
                    n4 = 14;
                    continue block39;
                }
                case 21: {
                    if (string.equals("=")) {
                        n4 = 22;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"=\".");
                }
                case 22: {
                    try {
                        n3 = Integer.parseInt(string);
                    }
                    catch (NumberFormatException numberFormatException) {
                        throw new IOException("Unexpected value \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected an integer.");
                    }
                    n4 = 14;
                    continue block39;
                }
                case 23: {
                    if (string.equals("=")) {
                        n4 = 24;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"=\".");
                }
                case 24: {
                    string2 = string;
                    n4 = 14;
                    continue block39;
                }
                case 25: {
                    if (string.equals("=")) {
                        n4 = 26;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"=\".");
                }
                case 26: {
                    try {
                        sqlExceptionType = SqlExceptionType.valueOf(string);
                    }
                    catch (Exception exception) {
                        throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected SQLException subclass name.");
                    }
                    n4 = 14;
                    continue block39;
                }
                case 27: {
                    if (string.equals("=")) {
                        n4 = 28;
                        continue block39;
                    }
                    throw new IOException("Unexpected token \"" + string + "\" at line " + tokenizer.lineno + " of errorMap.xml. Expected \"=\".");
                }
                case 28: {
                    string3 = string;
                    n4 = 14;
                    continue block39;
                }
            }
            throw new IOException("Unknown parser state " + n4 + " at line " + tokenizer.lineno + " of errorMap.xml.");
        }
    }

    private static void createOne(List<SQLStateMapping> list, int n2, int n3, String string, SqlExceptionType sqlExceptionType, String string2) throws IOException {
        if (n2 == -1) {
            throw new IOException("oraErrorFrom is a required attribute");
        }
        if (n3 == -1) {
            n3 = n2;
        }
        if (string == null || string.length() == 0) {
            throw new IOException("sqlState is a required attribute");
        }
        if (sqlExceptionType == null) {
            throw new IOException("sqlException is a required attribute");
        }
        if (string2 == null || string2.length() < 8) {
            throw new IOException("a lengthy comment in required");
        }
        SQLStateMapping sQLStateMapping = new SQLStateMapping(n2, n3, string, sqlExceptionType);
        SQLStateMapping.add(list, sQLStateMapping);
    }

    static void add(List<SQLStateMapping> list, SQLStateMapping sQLStateMapping) {
        int n2;
        for (n2 = list.size(); n2 > 0 && !list.get(n2 - 1).lessThan(sQLStateMapping); --n2) {
        }
        list.add(n2, sQLStateMapping);
    }

    private static final class Tokenizer {
        int lineno = 1;
        Reader r;
        int c;

        Tokenizer(Reader reader) throws IOException {
            this.r = reader;
            this.c = reader.read();
        }

        String next() throws IOException {
            StringBuffer stringBuffer = new StringBuffer(16);
            boolean bl = true;
            while (this.c != -1) {
                if (this.c == 10) {
                    ++this.lineno;
                }
                if (this.c <= 32 && bl) {
                    this.c = this.r.read();
                    continue;
                }
                if (this.c <= 32 && !bl) {
                    this.c = this.r.read();
                    break;
                }
                if (this.c == 34) {
                    while ((this.c = this.r.read()) != 34) {
                        stringBuffer.append((char)this.c);
                    }
                    this.c = this.r.read();
                    break;
                }
                if (48 <= this.c && this.c <= 57 || 65 <= this.c && this.c <= 90 || 97 <= this.c && this.c <= 122 || this.c == 95) {
                    do {
                        stringBuffer.append((char)this.c);
                    } while (48 <= (this.c = this.r.read()) && this.c <= 57 || 65 <= this.c && this.c <= 90 || 97 <= this.c && this.c <= 122 || this.c == 95);
                    break;
                }
                stringBuffer.append((char)this.c);
                this.c = this.r.read();
                break;
            }
            if (stringBuffer.length() > 0) {
                return stringBuffer.toString();
            }
            return null;
        }
    }

    static enum SqlExceptionType {
        SQLEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLException(string, string2, n2);
            }
        }
        ,
        SQLNONTRANSIENTEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLNonTransientException(string, string2, n2);
            }
        }
        ,
        SQLTRANSIENTEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLTransientException(string, string2, n2);
            }
        }
        ,
        SQLDATAEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLDataException(string, string2, n2);
            }
        }
        ,
        SQLFEATURENOTSUPPORTEDEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLFeatureNotSupportedException(string, string2, n2);
            }
        }
        ,
        SQLINTEGRITYCONSTRAINTVIOLATIONEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLIntegrityConstraintViolationException(string, string2, n2);
            }
        }
        ,
        SQLINVALIDAUTHORIZATIONSPECEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLInvalidAuthorizationSpecException(string, string2, n2);
            }
        }
        ,
        SQLNONTRANSIENTCONNECTIONEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLNonTransientConnectionException(string, string2, n2);
            }
        }
        ,
        SQLSYNTAXERROREXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLSyntaxErrorException(string, string2, n2);
            }
        }
        ,
        SQLTIMEOUTEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLTimeoutException(string, string2, n2);
            }
        }
        ,
        SQLTRANSACTIONROLLBACKEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLTransactionRollbackException(string, string2, n2);
            }
        }
        ,
        SQLTRANSIENTCONNECTIONEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLTransientConnectionException(string, string2, n2);
            }
        }
        ,
        SQLCLIENTINFOEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLClientInfoException(string, string2, n2, null);
            }
        }
        ,
        SQLRECOVERABLEEXCEPTION{

            @Override
            SQLException newInstance(String string, String string2, int n2) {
                return new SQLRecoverableException(string, string2, n2);
            }
        };


        abstract SQLException newInstance(String var1, String var2, int var3);
    }
}

