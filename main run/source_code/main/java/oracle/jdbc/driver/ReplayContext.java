/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.Arrays;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class ReplayContext
implements oracle.jdbc.internal.ReplayContext {
    long flags_kpdxcAppContCtl;
    short queue_kpdxcAppContCtl;
    byte[] replayctx_kpdxcAppContCtl;
    long errcode_kpdxcAppContCtl;

    ReplayContext(long l2, short s2, byte[] byArray, long l3) {
        this.flags_kpdxcAppContCtl = l2;
        this.queue_kpdxcAppContCtl = s2;
        this.errcode_kpdxcAppContCtl = l3;
        this.replayctx_kpdxcAppContCtl = byArray;
    }

    @Override
    public byte[] getContext() {
        return this.replayctx_kpdxcAppContCtl;
    }

    @Override
    public short getQueue() {
        return this.queue_kpdxcAppContCtl;
    }

    @Override
    public long getDirectives() {
        return this.flags_kpdxcAppContCtl;
    }

    @Override
    public long getErrorCode() {
        return this.errcode_kpdxcAppContCtl;
    }

    private String getDirectivesAsString() {
        String string = "[0";
        if ((this.flags_kpdxcAppContCtl & 1L) == 1L) {
            string = string + "|DIRECTIVE_ENQUEUE_CALL";
        }
        if ((this.flags_kpdxcAppContCtl & 2L) == 2L) {
            string = string + "|DIRECTIVE_REQ_SCOPE_CRSR";
        }
        if ((this.flags_kpdxcAppContCtl & 4L) == 4L) {
            string = string + "|DIRECTIVE_REPLAY_ENABLED";
        }
        if ((this.flags_kpdxcAppContCtl & 8L) == 8L) {
            string = string + "|DIRECTIVE_EMPTY_QUEUE";
        }
        return string + "]";
    }

    @DisableTrace
    boolean isDuplicate(ReplayContext replayContext) {
        if (replayContext == null) {
            return false;
        }
        return this.flags_kpdxcAppContCtl == replayContext.flags_kpdxcAppContCtl && this.queue_kpdxcAppContCtl == replayContext.queue_kpdxcAppContCtl && this.errcode_kpdxcAppContCtl == replayContext.errcode_kpdxcAppContCtl && Arrays.equals(this.replayctx_kpdxcAppContCtl, replayContext.replayctx_kpdxcAppContCtl);
    }

    @DisableTrace
    public String toString() {
        return "ReplayContext[Directives=" + this.getDirectivesAsString() + ",Queue=" + this.queue_kpdxcAppContCtl + ",ErrorCode=" + this.errcode_kpdxcAppContCtl + ",Context=" + this.replayctx_kpdxcAppContCtl + "]";
    }
}

