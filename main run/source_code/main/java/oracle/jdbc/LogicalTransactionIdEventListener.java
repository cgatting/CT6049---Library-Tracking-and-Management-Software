/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.util.EventListener;
import oracle.jdbc.LogicalTransactionIdEvent;

public interface LogicalTransactionIdEventListener
extends EventListener {
    public void onLogicalTransactionIdEvent(LogicalTransactionIdEvent var1);
}

