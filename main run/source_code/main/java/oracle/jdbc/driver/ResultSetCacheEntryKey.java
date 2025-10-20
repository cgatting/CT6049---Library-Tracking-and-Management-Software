/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.Arrays;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
final class ResultSetCacheEntryKey {
    private final byte[] key = new byte[32];

    ResultSetCacheEntryKey(byte[] byArray, byte[] byArray2) {
        System.arraycopy(byArray, 0, this.key, 0, 16);
        System.arraycopy(byArray2, 0, this.key, 16, 16);
    }

    @DisableTrace
    public boolean equals(Object object) {
        if (!(object instanceof ResultSetCacheEntryKey)) {
            return false;
        }
        return Arrays.equals(this.key, ((ResultSetCacheEntryKey)object).key);
    }

    @DisableTrace
    public int hashCode() {
        return Arrays.hashCode(this.key);
    }
}

