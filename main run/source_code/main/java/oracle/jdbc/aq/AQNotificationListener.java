/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.aq;

import java.util.EventListener;
import oracle.jdbc.aq.AQNotificationEvent;

public interface AQNotificationListener
extends EventListener {
    public void onAQNotification(AQNotificationEvent var1);
}

