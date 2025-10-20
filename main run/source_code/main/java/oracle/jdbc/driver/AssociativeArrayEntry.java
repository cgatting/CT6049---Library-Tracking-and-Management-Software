/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.sql.DatumWithConnection;

public class AssociativeArrayEntry<K, V>
extends DatumWithConnection {
    private static final long serialVersionUID = -657355820122347595L;
    private final K key;
    private final V value;

    public AssociativeArrayEntry(K k2, V v2) {
        this.key = k2;
        this.value = v2;
    }

    public Object getValue() {
        return this.value;
    }

    public Object getKey() {
        return this.key;
    }

    @Override
    public boolean isConvertibleTo(Class<?> clazz) {
        return false;
    }

    @Override
    public Object toJdbc() throws SQLException {
        return null;
    }

    @Override
    public Object makeJdbcArray(int n2) {
        return null;
    }
}

