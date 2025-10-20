/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.OracleLog;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.HIGH_AVAILABILITY})
class LogicalTransactionId
implements oracle.jdbc.LogicalTransactionId {
    private final byte[] ltxid;
    private static final long serialVersionUID = -1921448734136208393L;

    LogicalTransactionId(byte[] byArray) {
        this.ltxid = byArray;
    }

    byte[] getBytes() throws SQLException {
        return this.ltxid;
    }

    @DisableTrace
    public String toString() {
        return OracleLog.toHex(this.ltxid);
    }
}

