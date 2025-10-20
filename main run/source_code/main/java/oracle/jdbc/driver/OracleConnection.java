/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import oracle.jdbc.OracleConnectionWrapper;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleDriver;
import oracle.jdbc.internal.ClientDataSupport;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
public abstract class OracleConnection
extends OracleConnectionWrapper
implements oracle.jdbc.internal.OracleConnection,
ClientDataSupport {
    static int DEFAULT_ROW_PREFETCH = 10;
    static final String svptPrefix = "ORACLE_SVPT_";
    static final int BINARYSTREAM = 0;
    static final int ASCIISTREAM = 1;
    static final int UNICODESTREAM = 2;
    static final int EOJ_NON = 0;
    static final int EOJ_B_TO_A = 1;
    static final int EOJ_B_TO_U = 2;
    static final int EOJ_A_TO_U = 3;
    static final int EOJ_8_TO_A = 4;
    static final int EOJ_8_TO_U = 5;
    static final int EOJ_U_TO_A = 6;
    static final int ASCII_CHARSET = 0;
    static final int NLS_CHARSET = 1;
    static final int CHAR_TO_ASCII = 0;
    static final int CHAR_TO_UNICODE = 1;
    static final int RAW_TO_ASCII = 2;
    static final int RAW_TO_UNICODE = 3;
    static final int UNICODE_TO_CHAR = 4;
    static final int ASCII_TO_CHAR = 5;
    static final int NONE = 6;
    static final int JAVACHAR_TO_CHAR = 7;
    static final int RAW_TO_JAVACHAR = 8;
    static final int CHAR_TO_JAVACHAR = 9;
    static final int JAVACHAR_TO_ASCII = 10;
    static final int JAVACHAR_TO_UNICODE = 11;
    static final int UNICODE_TO_ASCII = 12;
    static final int ASCII_TO_UTF16 = 13;
    static final int JAVACHAR_TO_UTF16 = 14;
    static final int ASCII_TO_DBCS = 15;
    static final int JAVACHAR_TO_DBCS = 16;
    public static final int KOLBLLENB = 0;
    public static final int KOLBLVSNB = 2;
    public static final byte KOLL1FLG = 4;
    public static final byte KOLL2FLG = 5;
    public static final byte KOLL3FLG = 6;
    public static final byte KOLL4FLG = 7;
    public static final int KOLBLSDURIDB = 22;
    public static final int KOLBLCIDB = 32;
    static final byte ALLFLAGS = -1;
    public static final int KOLBLIMRLL = 86;
    public static final byte KOLBLBLOB = 1;
    public static final byte KOLBLCLOB = 2;
    public static final byte KOLBLNLOB = 4;
    public static final byte KOLBLBFIL = 8;
    public static final byte KOLBLCFIL = 16;
    public static final byte KOLBLNFIL = 32;
    public static final byte KOLBLVBL = 32;
    public static final byte KOLBLABS = 64;
    public static final byte KOLBLPXY = -128;
    public static final byte KOLBLPKEY = 1;
    public static final byte KOLBLIMP = 2;
    public static final byte KOLBLIDX = 4;
    public static final byte KOLBLINI = 8;
    public static final byte KOLBLEMP = 16;
    public static final byte KOLBLVIEW = 32;
    public static final byte KOLBL0FRM = 64;
    public static final byte KOLBL1FRM = -128;
    public static final byte KOLBLRDO = 1;
    public static final byte KOLBLPART = 2;
    public static final byte KOLBLCPD = 4;
    public static final byte KOLBLDIL = 8;
    public static final byte KOLBLBUF = 16;
    public static final byte KOLBLBPS = 32;
    public static final byte KOLBLMOD = 64;
    public static final byte KOLBLVAR = -128;
    public static final byte KOLBLTMP = 1;
    public static final byte KOLBLCACHE = 2;
    public static final byte KOLBLOPEN = 8;
    public static final byte KOLBLRDWR = 16;
    public static final byte KOLBLCLI = 32;
    public static final byte KOLBLVLE = 64;
    public static final byte KOLBLLCL = -128;
    static final int KOLBLLIDB = 10;
    static final int KOLBLPREL = 2;
    static final int KOLBLLIDL = 10;
    static final int KOLBLTLMXL = 40;
    static final int KOLLL_TEMP = 40;
    static final int ZTCH_LEN_SH256 = 32;
    static final int ZTCH_LEN_SH512 = 64;
    static final int KOLBLSIGMTSZ = 4;
    static final int KOLBLSIGSZ = 68;
    static final int KOLLL_TEMPWSIG = 108;
    static final int MAX_LOB_LENGTH = 4000;
    static final int ORA_22289 = 22289;
    static final int QUASI_LOCATOR_VERSION = 4;
    static final List<String> RESERVED_NAMESPACES = Arrays.asList("SYS");
    static final Pattern SUPPORTED_NAMESPACE_PATTERN = Pattern.compile("CLIENTCONTEXT");
    protected Object acProxy;

    protected OracleConnection() {
    }

    OracleConnection(Monitor.CloseableLock closeableLock) {
        super(closeableLock);
    }

    static boolean containsKey(Map<String, Class<?>> map, Object object) {
        return map.get(object) != null;
    }

    @Override
    public abstract Object getClientData(Object var1);

    @Override
    public abstract Object setClientData(Object var1, Object var2);

    @Override
    public abstract Object removeClientData(Object var1);

    @Override
    @Deprecated
    public abstract void setClientIdentifier(String var1) throws SQLException;

    @Override
    @Deprecated
    public abstract void clearClientIdentifier(String var1) throws SQLException;

    @Override
    public boolean isWrapperFor(Class<?> clazz) throws SQLException {
        if (clazz.isInterface()) {
            return clazz.isInstance(this);
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    @Override
    public <T> T unwrap(Class<T> clazz) throws SQLException {
        if (clazz.isInterface() && clazz.isInstance(this)) {
            return (T)this;
        }
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 177).fillInStackTrace();
    }

    @Override
    protected oracle.jdbc.internal.OracleConnection getConnectionDuringExceptionHandling() {
        return this;
    }

    @Override
    public Class<?> getClassForType(String string, Map<String, Class<?>> map) {
        Class<?> clazz;
        Class<?> clazz2 = map.get(string);
        if (clazz2 == null && (clazz = OracleDriver.systemTypeMap.get(string)) != null) {
            clazz2 = clazz;
        }
        return clazz2;
    }

    @Override
    public void setACProxy(Object object) {
        this.acProxy = object;
    }

    @Override
    public Object getACProxy() {
        return this.acProxy;
    }
}

