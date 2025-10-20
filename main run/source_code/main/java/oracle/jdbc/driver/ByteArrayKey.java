/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class ByteArrayKey {
    private byte[] theBytes;
    private int cachedHashCode = -1;

    public ByteArrayKey(byte[] byArray) {
        for (byte by : this.theBytes = byArray) {
            this.cachedHashCode = this.cachedHashCode << 1 & (this.cachedHashCode < 0 ? 1 : 0) ^ by;
        }
    }

    @DisableTrace
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ByteArrayKey)) {
            return false;
        }
        byte[] byArray = ((ByteArrayKey)object).theBytes;
        if (this.theBytes.length != byArray.length) {
            return false;
        }
        for (int i2 = 0; i2 < this.theBytes.length; ++i2) {
            if (this.theBytes[i2] == byArray[i2]) continue;
            return false;
        }
        return true;
    }

    @DisableTrace
    public int hashCode() {
        return this.cachedHashCode;
    }
}

