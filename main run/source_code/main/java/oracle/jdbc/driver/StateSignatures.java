/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;

@DefaultLogger(value="oracle.jdbc")
class StateSignatures
implements oracle.jdbc.internal.StateSignatures {
    long signatureFlags;
    long clientSignature;
    long serverSignature;
    long version;

    StateSignatures(long l2, long l3, long l4) {
        this(l2, l3, l4, 0L);
    }

    StateSignatures(long l2, long l3, long l4, long l5) {
        this.signatureFlags = l2;
        this.clientSignature = l3;
        this.serverSignature = l4;
        this.version = l5;
    }

    @Override
    public long getSignatureFlags() {
        return this.signatureFlags;
    }

    @Override
    public long getClientSignature() {
        return this.clientSignature;
    }

    @Override
    public long getServerSignature() {
        return this.serverSignature;
    }

    @Override
    public long getVersion() {
        return this.version;
    }

    StateSignatures copy() {
        return new StateSignatures(this.signatureFlags, this.clientSignature, this.serverSignature, this.version);
    }

    @DisableTrace
    public String toString() {
        return "StateSignatures[SignatureFlags=" + Long.toHexString(this.getSignatureFlags()) + ", ClientSignature=" + Long.toHexString(this.getClientSignature()) + ", ServerSignature=" + Long.toHexString(this.getServerSignature()) + ", Version=" + Long.toHexString(this.getVersion()) + "]";
    }
}

