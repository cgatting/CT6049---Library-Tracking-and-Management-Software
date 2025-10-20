/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.util.EventObject;
import oracle.jdbc.LogicalTransactionId;

public abstract class LogicalTransactionIdEvent
extends EventObject {
    protected LogicalTransactionIdEvent(Object object) {
        super(object);
    }

    public abstract LogicalTransactionId getLogicalTransactionId();
}

