/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.simplefan.FanEventListener
 *  oracle.simplefan.FanUpEventListener
 *  oracle.simplefan.LoadAdvisoryEvent
 *  oracle.simplefan.NodeDownEvent
 *  oracle.simplefan.NodeUpEvent
 *  oracle.simplefan.ServiceDownEvent
 *  oracle.simplefan.ServiceUpEvent
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.HAManager;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.simplefan.FanEventListener;
import oracle.simplefan.FanUpEventListener;
import oracle.simplefan.LoadAdvisoryEvent;
import oracle.simplefan.NodeDownEvent;
import oracle.simplefan.NodeUpEvent;
import oracle.simplefan.ServiceDownEvent;
import oracle.simplefan.ServiceUpEvent;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.HIGH_AVAILABILITY})
final class HAFanListener
implements FanEventListener,
FanUpEventListener {
    HAManager haManager = null;

    HAFanListener(HAManager hAManager) {
        this.haManager = hAManager;
    }

    public void handleEvent(ServiceDownEvent serviceDownEvent) {
        String string = HAFanListener.toLowerCase(serviceDownEvent.getServiceName());
        String string2 = HAFanListener.toLowerCase(serviceDownEvent.getDatabaseUniqueName());
        String string3 = null;
        String string4 = null;
        int n2 = serviceDownEvent.getDrainTimeout();
        block0 : switch (serviceDownEvent.getKind()) {
            case MEMBER: {
                string3 = HAFanListener.toLowerCase(serviceDownEvent.getServiceMemberEvent().getInstanceName());
                string4 = HAFanListener.toLowerCase(serviceDownEvent.getServiceMemberEvent().getNodeName());
            }
            case COMPOSITE: {
                switch (serviceDownEvent.getReason()) {
                    case USER: {
                        this.haManager.plannedDown(string, string3, string2, string4, n2);
                        break block0;
                    }
                }
                this.haManager.unplannedDown(string, string3, string2, string4);
                break;
            }
        }
    }

    public void handleEvent(ServiceUpEvent serviceUpEvent) {
        String string = HAFanListener.toLowerCase(serviceUpEvent.getServiceName());
        String string2 = HAFanListener.toLowerCase(serviceUpEvent.getDatabaseUniqueName());
        String string3 = null;
        String string4 = null;
        int n2 = serviceUpEvent.getCardinality();
        switch (serviceUpEvent.getKind()) {
            case MEMBER: {
                string3 = HAFanListener.toLowerCase(serviceUpEvent.getServiceMemberEvent().getInstanceName());
                string4 = HAFanListener.toLowerCase(serviceUpEvent.getServiceMemberEvent().getNodeName());
            }
            case COMPOSITE: {
                this.haManager.serviceUp(string, string3, string2, string4, n2);
                break;
            }
        }
    }

    public void handleEvent(NodeDownEvent nodeDownEvent) {
        String string = nodeDownEvent.getNodeName();
        this.haManager.nodeDown(string);
    }

    public void handleEvent(LoadAdvisoryEvent loadAdvisoryEvent) {
    }

    public void handleEvent(NodeUpEvent nodeUpEvent) {
    }

    private static String toLowerCase(String string) {
        return null == string ? null : string.toLowerCase();
    }
}

