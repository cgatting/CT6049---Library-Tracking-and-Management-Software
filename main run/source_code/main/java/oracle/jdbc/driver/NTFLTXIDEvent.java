/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.LogicalTransactionIdEvent;
import oracle.jdbc.driver.LogicalTransactionId;
import oracle.jdbc.driver.T4CConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFLTXIDEvent
extends LogicalTransactionIdEvent {
    private static final long serialVersionUID = -8368320032018596231L;
    LogicalTransactionId ltxid;

    NTFLTXIDEvent(T4CConnection t4CConnection, LogicalTransactionId logicalTransactionId) {
        super(t4CConnection);
        this.ltxid = logicalTransactionId;
    }

    @Override
    public oracle.jdbc.LogicalTransactionId getLogicalTransactionId() {
        return this.ltxid;
    }
}

