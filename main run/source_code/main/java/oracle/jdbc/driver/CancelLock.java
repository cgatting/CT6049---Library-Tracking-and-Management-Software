/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.driver.CancelState;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class CancelLock {
    private CancelState state = null;
    private OracleStatement statement = null;

    CancelLock(OracleStatement oracleStatement) {
        this.statement = oracleStatement;
        this.state = new CancelState();
    }

    boolean isIdle() {
        try (Monitor.CloseableLock closeableLock = this.state.acquireCloseableLock();){
            boolean bl = this.state.isIdle();
            return bl;
        }
    }

    boolean isCanceled() {
        try (Monitor.CloseableLock closeableLock = this.state.acquireCloseableLock();){
            boolean bl = this.state.isCanceled();
            return bl;
        }
    }

    void enterExecuting() {
        try (Monitor.CloseableLock closeableLock = this.state.acquireCloseableLock();){
            assert (this.state.isIdle());
            this.state.setExecuting();
        }
    }

    /*
     * Unable to fully structure code
     */
    void exitExecuting() {
        while (true) lbl-1000:
        // 7 sources

        {
            block28: {
                block26: {
                    block27: {
                        var1_1 = this.state.acquireCloseableLock();
                        var2_2 = null;
                        if (!this.state.isExecuting()) break block26;
                        this.state.setIdle();
                        if (var1_1 == null) break;
                        if (var2_2 == null) break block27;
                        try {
                            var1_1.close();
                        }
                        catch (Throwable var3_4) {
                            var2_2.addSuppressed(var3_4);
                        }
                        break;
                    }
                    var1_1.close();
                    break;
                }
                if (!this.state.isCanceling()) break block28;
                try {
                    this.state.monitorWait();
                }
                finally {
                    if (var1_1 == null) ** GOTO lbl-1000
                    if (var2_2 != null) {
                        try {
                            var1_1.close();
                        }
                        catch (Throwable var3_5) {
                            var2_2.addSuppressed(var3_5);
                        }
                    }
                    var1_1.close();
                }
            }
            try {
                if (!this.state.isCanceled()) continue;
                if (this.statement != null && this.statement.connection.cancelInProgressFlag) {
                }
                this.state.setIdle();
            }
            catch (Throwable var3_9) {
                var2_2 = var3_9;
                throw var3_9;
            }
            catch (Throwable var5_11) {
                throw var5_11;
            }
            finally {
                if (var1_1 == null) continue;
                if (var2_2 != null) {
                    try {
                        var1_1.close();
                    }
                    catch (Throwable var3_7) {
                        var2_2.addSuppressed(var3_7);
                    }
                    continue;
                }
                var1_1.close();
                continue;
            }
            break;
        }
    }

    boolean enterCanceling() {
        try (Monitor.CloseableLock closeableLock = this.state.acquireCloseableLock();){
            if (this.state.isExecuting()) {
                this.state.setCanceling();
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    void exitCanceling() {
        try (Monitor.CloseableLock closeableLock = this.state.acquireCloseableLock();){
            assert (this.state.isCanceling());
            this.state.setCanceled();
            this.state.monitorNotify();
        }
    }
}

