/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.lang.reflect.Executable;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.driver.ClioSupport;
import oracle.jdbc.driver.OracleTimeoutThreadPerVM;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Log;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.EXCEPTIONAL_EXECUTION})
class OracleTimeoutPollingThread
extends Thread
implements Monitor {
    protected static final String threadName = "OracleTimeoutPollingThread";
    public static final String pollIntervalProperty = "oracle.jdbc.TimeoutPollInterval";
    public static final String pollIntervalDefault = "1000";
    private OracleTimeoutThreadPerVM[] knownTimeouts;
    private int count;
    private long sleepMillis;
    private final Monitor.CloseableLock monitorLock = this.newDefaultLock();

    public OracleTimeoutPollingThread() {
        super(threadName);
        this.setDaemon(true);
        this.setPriority(10);
        this.knownTimeouts = new OracleTimeoutThreadPerVM[2];
        this.count = 0;
        this.sleepMillis = Long.parseLong(PhysicalConnection.getSystemPropertyPollInterval());
        this.start();
    }

    public void addTimeout(OracleTimeoutThreadPerVM oracleTimeoutThreadPerVM) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            int n2 = 0;
            if (this.count >= this.knownTimeouts.length) {
                OracleTimeoutThreadPerVM[] oracleTimeoutThreadPerVMArray = new OracleTimeoutThreadPerVM[this.knownTimeouts.length * 4];
                System.arraycopy(this.knownTimeouts, 0, oracleTimeoutThreadPerVMArray, 0, this.knownTimeouts.length);
                n2 = this.knownTimeouts.length;
                this.knownTimeouts = oracleTimeoutThreadPerVMArray;
            }
            while (n2 < this.knownTimeouts.length) {
                if (this.knownTimeouts[n2] == null) {
                    this.knownTimeouts[n2] = oracleTimeoutThreadPerVM;
                    ++this.count;
                    break;
                }
                ++n2;
            }
        }
    }

    public void removeTimeout(OracleTimeoutThreadPerVM oracleTimeoutThreadPerVM) {
        try (Monitor.CloseableLock closeableLock = this.acquireCloseableLock();){
            for (int i2 = 0; i2 < this.knownTimeouts.length; ++i2) {
                if (this.knownTimeouts[i2] != oracleTimeoutThreadPerVM) continue;
                this.knownTimeouts[i2] = null;
                --this.count;
                break;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(this.sleepMillis);
            }
            catch (InterruptedException interruptedException) {
                return;
            }
            this.pollOnce();
        }
    }

    private void pollOnce() {
        if (this.count > 0) {
            long l2 = System.currentTimeMillis();
            for (int i2 = 0; i2 < this.knownTimeouts.length; ++i2) {
                try {
                    if (this.knownTimeouts[i2] == null) continue;
                    this.knownTimeouts[i2].interruptIfAppropriate(l2);
                    continue;
                }
                catch (NullPointerException nullPointerException) {
                    // empty catch block
                }
            }
        }
    }

    @Log
    protected void debug(Logger logger, Level level, Executable executable, String string) {
        ClioSupport.log(logger, level, this.getClass(), executable, string);
    }

    @Override
    public final Monitor.CloseableLock getMonitorLock() {
        return this.monitorLock;
    }
}

