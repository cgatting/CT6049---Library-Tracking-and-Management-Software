/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.dcn;

import java.util.EventListener;
import oracle.jdbc.dcn.DatabaseChangeEvent;

public interface DatabaseChangeListener
extends EventListener {
    public void onDatabaseChangeNotification(DatabaseChangeEvent var1);
}

