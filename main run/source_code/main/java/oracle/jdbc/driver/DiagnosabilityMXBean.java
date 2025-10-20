/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.logging.runtime.TraceController;

public interface DiagnosabilityMXBean {
    public boolean stateManageable();

    public boolean statisticsProvider();

    public boolean getLoggingEnabled();

    public void setLoggingEnabled(boolean var1);

    public TraceController getTraceController();

    public void suspend();

    public void resume();

    public void trace(boolean var1, String var2);

    public void enableContinousLogging();

    public void disableContinousLogging();

    public void enableInMemoryLogging();

    public void disableInMemoryLogging();

    public void setUserFilter(String var1);

    public void setServiceNameFilter(String var1);
}

