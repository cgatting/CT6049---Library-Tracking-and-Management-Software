/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.OracleShardingKeyBuilder;
import oracle.jdbc.OracleType;
import oracle.jdbc.datasource.OracleDataSource;
import oracle.jdbc.driver.AbstractShardingPreparedStatement;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.SQLUtil;
import oracle.jdbc.internal.OracleStatement;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.CHAR;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.NUMBER;
import oracle.sql.RAW;
import oracle.sql.TIMESTAMP;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.THIN_INTERNAL})
class ShardingKeyInfo {
    static ConcurrentHashMap<Integer, KeyTokenInfo> sqlToShardingKeyTokensMap = new ConcurrentHashMap();
    protected static final int DEPTH = 128;
    public static final int GWS_KEY_RESERVED = 255;
    public static final int GWS_KEY_UNUSED = 0;
    public static final int GWS_KEY_RETURN_TUPLE_20_1 = 82;
    public static final int GWS_KEY_APPEND_KEY_TUPLE_20_1 = 125;
    public static final int GWS_KEY_APPEND_VALUE_KEY_20_1 = 93;
    public static final int GWS_KEY_PUSH_BIND_INDEX_20_1 = 73;
    public static final int GWS_KEY_PUSH_PARAMETER_20_1 = 80;
    public static final int GWS_KEY_PUSH_LITERAL_20_1 = 76;
    public static final int GWS_KEY_PUSH_SQL_TYPE_20_1 = 84;
    public static final int GWS_KEY_PUSH_SHORT_20_1 = 83;
    public static final int GWS_KEY_PUSH_EMPTY_KEY_20_1 = 91;
    public static final int GWS_KEY_PUSH_EMPTY_TUPLE_20_1 = 123;
    protected Stack stack = new Stack(128);

    ShardingKeyInfo() {
    }

    List<List<Object>> evaluateShardingKeys(OracleStatement oracleStatement, byte[] byArray, short s2) throws SQLException, IOException {
        List list = null;
        byte by = 0;
        int n2 = byArray.length;
        int n3 = 0;
        block12: while (n3 < n2) {
            by = byArray[n3++];
            List list2 = null;
            List list3 = null;
            int n4 = 0;
            Object object = null;
            int n5 = 0;
            byte[] byArray2 = null;
            switch (by) {
                case 82: {
                    list = this.stack.pop(List.class);
                    if (n3 == n2) {
                        return list;
                    }
                    throw new SQLException("more than expected sharding key information expression");
                }
                case 125: {
                    list3 = this.stack.pop(List.class);
                    list2 = this.stack.pop(List.class);
                    list2.add(list3);
                    this.stack.push(list2);
                    continue block12;
                }
                case 93: {
                    object = this.stack.pop(Object.class);
                    list3 = this.stack.pop(List.class);
                    list3.add(object);
                    this.stack.push(list3);
                    continue block12;
                }
                case 80: {
                    n4 = this.stack.pop(Integer.class);
                    n5 = this.stack.pop(Integer.class);
                    this.stack.push(((AbstractShardingPreparedStatement)((Object)oracleStatement)).getBindValue(n5, n4));
                    continue block12;
                }
                case 76: {
                    n4 = this.stack.pop(Integer.class);
                    byArray2 = new byte[this.stack.pop(Short.class).shortValue()];
                    System.arraycopy(byArray, n3, byArray2, 0, byArray2.length);
                    n3 += byArray2.length;
                    this.stack.push(this.convertDatumToJavaObject(byArray2, n4, s2));
                    continue block12;
                }
                case 84: {
                    n5 = (short)(((byArray[n3++] & 0xFF) << 8) + (byArray[n3++] & 0xFF));
                    this.stack.push(n5);
                    continue block12;
                }
                case 83: {
                    n5 = (short)(((byArray[n3++] & 0xFF) << 8) + (byArray[n3++] & 0xFF));
                    this.stack.push((short)n5);
                    continue block12;
                }
                case 73: {
                    n5 = ((byArray[n3++] & 0xFF) << 24) + ((byArray[n3++] & 0xFF) << 16) + ((byArray[n3++] & 0xFF) << 8) + (byArray[n3++] & 0xFF);
                    this.stack.push(n5);
                    continue block12;
                }
                case 91: {
                    this.stack.push(new ArrayList());
                    continue block12;
                }
                case 123: {
                    this.stack.push(new ArrayList());
                    continue block12;
                }
            }
            throw (SQLException)DatabaseError.createSqlException(1704).fillInStackTrace();
        }
        throw (SQLException)DatabaseError.createSqlException(1704).fillInStackTrace();
    }

    Object convertDatumToJavaObject(byte[] byArray, int n2, short s2) throws SQLException {
        Datum datum = SQLUtil.makeDatum(null, byArray, n2, null, 0, (short)873, s2);
        Object object = ShardingKeyInfo.SQLToJavaKeyObject(datum, n2);
        return object;
    }

    OracleShardingKey[] getShardingKeys(OracleStatement oracleStatement, byte[] byArray, OracleDataSource oracleDataSource, short s2) throws SQLException, IOException {
        OracleShardingKey[] oracleShardingKeyArray = new OracleShardingKey[2];
        List<List<Object>> list = this.evaluateShardingKeys(oracleStatement, byArray, s2);
        OracleShardingKeyBuilder oracleShardingKeyBuilder = oracleDataSource.createShardingKeyBuilder();
        if (list != null && !list.isEmpty()) {
            oracleShardingKeyBuilder = this.addSubKeys(oracleShardingKeyBuilder, list.get(0));
            oracleShardingKeyArray[0] = oracleShardingKeyBuilder.build();
            if (list.size() > 1) {
                OracleShardingKeyBuilder oracleShardingKeyBuilder2 = oracleDataSource.createShardingKeyBuilder();
                oracleShardingKeyBuilder2 = this.addSubKeys(oracleShardingKeyBuilder2, list.get(1));
                oracleShardingKeyArray[1] = oracleShardingKeyBuilder2.build();
            }
        }
        return oracleShardingKeyArray;
    }

    OracleShardingKeyBuilder addSubKeys(OracleShardingKeyBuilder oracleShardingKeyBuilder, List<Object> list) throws SQLException {
        for (Object object : list) {
            SQLType sQLType = this.getKeyType(object);
            oracleShardingKeyBuilder.subkey(object, sQLType);
        }
        return oracleShardingKeyBuilder;
    }

    SQLType getKeyType(Object object) throws SQLException {
        int n2 = this.sqlTypeForObject(object);
        OracleType oracleType = OracleType.toOracleType(n2);
        return oracleType;
    }

    private int sqlTypeForObject(Object object) {
        if (object == null) {
            return 0;
        }
        if (!(object instanceof Datum)) {
            if (object instanceof String) {
                return 12;
            }
            if (object instanceof BigDecimal) {
                return 2;
            }
            if (object instanceof BigInteger) {
                return 2;
            }
            if (object instanceof Boolean) {
                return 2;
            }
            if (object instanceof Integer) {
                return 2;
            }
            if (object instanceof Long) {
                return 2;
            }
            if (object instanceof Float) {
                return 6;
            }
            if (object instanceof Double) {
                return 8;
            }
            if (object instanceof byte[]) {
                return -2;
            }
            if (object instanceof Short) {
                return 2;
            }
            if (object instanceof Byte) {
                return 2;
            }
            if (object instanceof Date) {
                return 91;
            }
            if (object instanceof Time) {
                return 92;
            }
            if (object instanceof Timestamp) {
                return 93;
            }
            if (object instanceof URL) {
                return 12;
            }
        } else {
            if (object instanceof BINARY_FLOAT) {
                return 100;
            }
            if (object instanceof BINARY_DOUBLE) {
                return 101;
            }
            if (object instanceof NUMBER) {
                return 2;
            }
            if (object instanceof DATE) {
                return 91;
            }
            if (object instanceof TIMESTAMP) {
                return 93;
            }
            if (object instanceof CHAR) {
                return 1;
            }
            if (object instanceof RAW) {
                return -2;
            }
        }
        return 1111;
    }

    static Object SQLToJavaKeyObject(Datum datum, int n2) throws SQLException {
        Object object = null;
        if (datum == null) {
            return null;
        }
        switch (n2) {
            case 1: 
            case 96: {
                object = datum.stringValue();
                break;
            }
            case 2: 
            case 6: {
                object = datum.bigDecimalValue();
                break;
            }
            case 12: {
                object = datum.dateValue();
                break;
            }
            case 180: {
                object = datum.timestampValue();
                break;
            }
            default: {
                object = datum.toJdbc();
            }
        }
        return object;
    }

    static KeyTokenInfo putKeyRpnTokens(String string, String string2, String string3, String string4, byte[] byArray, OracleStatement.SqlKind sqlKind) {
        KeyTokenInfo keyTokenInfo = new KeyTokenInfo(byArray, sqlKind);
        int n2 = ShardingKeyInfo.calculateTokensHashKey(string, string2, string3, string4);
        return sqlToShardingKeyTokensMap.put(n2, keyTokenInfo);
    }

    static KeyTokenInfo getKeyRpnTokens(String string, String string2, String string3, String string4) {
        int n2 = ShardingKeyInfo.calculateTokensHashKey(string, string2, string3, string4);
        return sqlToShardingKeyTokensMap.get(n2);
    }

    static int calculateTokensHashKey(String ... stringArray) {
        int n2 = 1;
        for (String string : stringArray) {
            n2 = 31 * n2 + string.hashCode();
        }
        return n2;
    }

    protected static final class KeyTokenInfo {
        private byte[] keyTokens;
        private OracleStatement.SqlKind sqlkind;

        public KeyTokenInfo(byte[] byArray, OracleStatement.SqlKind sqlKind) {
            this.keyTokens = byArray;
            this.sqlkind = sqlKind;
        }

        public byte[] getKeyTokens() {
            return this.keyTokens;
        }

        public OracleStatement.SqlKind getSqlKind() {
            return this.sqlkind;
        }
    }

    protected static final class Stack {
        private final Object[] stack;
        private int top = -1;

        public Stack(int n2) {
            this.stack = new Object[n2];
        }

        public boolean isEmpty() {
            return this.top == -1;
        }

        public Stack push(Object object) {
            this.stack[++this.top] = object;
            return this;
        }

        public <T> T pop(Class<T> clazz) {
            Object object = this.stack[this.top];
            this.stack[this.top--] = null;
            return (T)object;
        }
    }
}

