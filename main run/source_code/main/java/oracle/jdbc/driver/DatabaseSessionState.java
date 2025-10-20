/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.driver.StateSignatures;
import oracle.jdbc.driver.TemplateOverflow;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;

@DefaultLogger(value="oracle.jdbc")
class DatabaseSessionState
implements oracle.jdbc.internal.DatabaseSessionState {
    StateSignatures stateSignatures = null;
    long templateId = 0L;
    byte[] fullOverflow = null;
    List<byte[]> deltaOverflows = new ArrayList<byte[]>();

    DatabaseSessionState() {
    }

    @Override
    public oracle.jdbc.internal.StateSignatures getStateSignatures() {
        return this.stateSignatures;
    }

    @Override
    public long getId() {
        return this.templateId;
    }

    @Override
    public byte[] getCheckpoint() {
        return this.fullOverflow;
    }

    @Override
    public List<byte[]> getUpdates() {
        return this.deltaOverflows;
    }

    DatabaseSessionState copy() {
        DatabaseSessionState databaseSessionState = new DatabaseSessionState();
        databaseSessionState.stateSignatures = this.stateSignatures.copy();
        databaseSessionState.templateId = this.templateId;
        if (this.fullOverflow != null && this.fullOverflow.length > 0) {
            databaseSessionState.fullOverflow = new byte[this.fullOverflow.length];
            System.arraycopy(this.fullOverflow, 0, databaseSessionState.fullOverflow, 0, this.fullOverflow.length);
        }
        databaseSessionState.deltaOverflows = new ArrayList<byte[]>();
        for (byte[] byArray : this.deltaOverflows) {
            if (byArray == null || byArray.length <= 0) continue;
            byte[] byArray2 = new byte[byArray.length];
            System.arraycopy(byArray, 0, byArray2, 0, byArray.length);
            databaseSessionState.deltaOverflows.add(byArray2);
        }
        return databaseSessionState;
    }

    final void update(StateSignatures stateSignatures, TemplateOverflow templateOverflow) {
        this.stateSignatures = stateSignatures;
        this.stateSignatures.signatureFlags &= 0xFFFFFFFFFFFFFFBFL;
        if (templateOverflow != null) {
            this.templateId = templateOverflow.getTemplateId();
            byte[] byArray = templateOverflow.getOverflow();
            boolean bl = templateOverflow.isOverflowComplete();
            if (bl && byArray != null) {
                this.fullOverflow = byArray;
            } else if (!bl && byArray != null) {
                if (this.fullOverflow == null) {
                } else {
                    this.deltaOverflows.add(byArray);
                }
            } else if (bl && byArray == null) {
                this.fullOverflow = null;
                this.deltaOverflows = new ArrayList<byte[]>();
            }
        }
    }

    @DisableTrace
    public String toString() {
        return "DatabaseSessionState[StateSignatures=" + this.stateSignatures + ", TemplateID=" + Long.toHexString(this.getId()) + ", Full-overflow: " + (this.fullOverflow == null ? 0 : 1) + ", Delta-overflow: " + this.deltaOverflows.size() + "]";
    }
}

