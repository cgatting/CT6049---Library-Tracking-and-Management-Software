/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import oracle.sql.SQLName;

public interface OracleTypeMetaData {
    public Kind getKind();

    public String getName() throws SQLException;

    public SQLName getSQLName() throws SQLException;

    public String getSchemaName() throws SQLException;

    public int getTypeCode() throws SQLException;

    public String getTypeCodeName() throws SQLException;

    public static interface Struct
    extends OracleTypeMetaData {
        public int getTypeVersion() throws SQLException;

        public int getLength() throws SQLException;

        public ResultSetMetaData getMetaData() throws SQLException;

        public boolean isFinalType() throws SQLException;

        public boolean isSubtype() throws SQLException;

        public boolean isInstantiable() throws SQLException;

        public String getSupertypeName() throws SQLException;

        public int getLocalAttributeCount() throws SQLException;

        public String[] getSubtypeNames() throws SQLException;
    }

    public static interface Opaque
    extends OracleTypeMetaData {
        public long getMaxLength() throws SQLException;

        public boolean isTrustedLibrary() throws SQLException;

        public boolean isModeledInC() throws SQLException;

        public boolean hasUnboundedSize() throws SQLException;

        public boolean hasFixedSize() throws SQLException;
    }

    public static interface Array
    extends OracleTypeMetaData {
        public int getBaseType() throws SQLException;

        public String getBaseName() throws SQLException;

        public ArrayStorage getArrayStorage() throws SQLException;

        public long getMaxLength() throws SQLException;
    }

    public static enum ArrayStorage {
        VARRAY(3),
        NESTED_TABLE(2);

        private static final Map<Integer, ArrayStorage> lookup;
        private final int code;

        public static ArrayStorage withCode(int n2) {
            return lookup.get(n2);
        }

        private ArrayStorage(int n3) {
            this.code = n3;
        }

        public int getCode() {
            return this.code;
        }

        static {
            lookup = new HashMap<Integer, ArrayStorage>(2);
            for (ArrayStorage arrayStorage : ArrayStorage.values()) {
                lookup.put(arrayStorage.getCode(), arrayStorage);
            }
        }
    }

    public static enum Kind {
        ARRAY,
        OPAQUE,
        STRUCT,
        TYPE;

    }
}

