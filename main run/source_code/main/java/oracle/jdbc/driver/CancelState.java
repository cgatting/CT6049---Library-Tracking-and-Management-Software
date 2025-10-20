/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.concurrent.locks.Condition;
import oracle.jdbc.internal.Monitor;

class CancelState
implements Monitor.WaitableMonitor {
    private eState state = null;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();
    private final Condition monitorCondition = this.newMonitorCondition();

    CancelState() {
        this.state = eState.IDLE;
    }

    boolean isIdle() {
        return this.state == eState.IDLE;
    }

    void setIdle() {
        this.state = eState.IDLE;
    }

    boolean isExecuting() {
        return this.state == eState.EXECUTING;
    }

    void setExecuting() {
        this.state = eState.EXECUTING;
    }

    boolean isCanceling() {
        return this.state == eState.CANCELING;
    }

    void setCanceling() {
        this.state = eState.CANCELING;
    }

    boolean isCanceled() {
        return this.state == eState.CANCELED;
    }

    void setCanceled() {
        this.state = eState.CANCELED;
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }

    @Override
    public Condition getMonitorCondition() {
        return this.monitorCondition;
    }

    private static enum eState {
        IDLE,
        EXECUTING,
        CANCELING,
        CANCELED;

    }
}

