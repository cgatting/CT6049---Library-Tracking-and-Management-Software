/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.StandardMBean;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.DiagnosabilityMXBean;
import oracle.jdbc.driver.OracleLog;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.runtime.TraceController;
import oracle.jdbc.logging.runtime.TraceControllerImpl;

@DisableTrace
public class OracleDiagnosabilityMBean
extends StandardMBean
implements DiagnosabilityMXBean {
    private final TraceController tc = new TraceControllerImpl();

    public OracleDiagnosabilityMBean() {
        super(DiagnosabilityMXBean.class, true);
    }

    @Override
    public boolean getLoggingEnabled() {
        return OracleLog.isEnabled();
    }

    @Override
    public void setLoggingEnabled(boolean bl) {
        OracleLog.setTrace(bl);
    }

    @Override
    public boolean stateManageable() {
        return false;
    }

    @Override
    public boolean statisticsProvider() {
        return false;
    }

    @Override
    protected String getDescription(MBeanInfo mBeanInfo) {
        return DatabaseError.findMessage("DiagnosabilityMBeanDescription", (Object)this);
    }

    @Override
    protected String getDescription(MBeanConstructorInfo mBeanConstructorInfo) {
        return DatabaseError.findMessage("DiagnosabilityMBeanConstructor()", (Object)this);
    }

    @Override
    protected String getDescription(MBeanAttributeInfo mBeanAttributeInfo) {
        String string = mBeanAttributeInfo.getName();
        if (string.equals("LoggingEnabled")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanLoggingEnabledDescription", (Object)this);
        }
        if (string.equals("stateManageable")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanStateManageableDescription", (Object)this);
        }
        if (string.equals("statisticsProvider")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanStatisticsProviderDescription", (Object)this);
        }
        if (string.equals("TraceController")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanTraceControllerDescription", (Object)this);
        }
        if (string.equals("suspend")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanSuspendDescription", (Object)this);
        }
        if (string.equals("resume")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanResumeDescription", (Object)this);
        }
        if (string.equals("trace")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanTraceDescription", (Object)this);
        }
        if (string.equals("enableContinousLogging")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanEnableContinousLoggingDescription", (Object)this);
        }
        if (string.equals("disableContinousLogging")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanDisableContinousLoggingDescription", (Object)this);
        }
        if (string.equals("enableInMemoryLogging")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanEnableInMemoryLoggingDescription", (Object)this);
        }
        if (string.equals("disableInMemoryLogging")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanDisableInMemoryLoggingDescription", (Object)this);
        }
        if (string.equals("ServiceNameFilter")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanServiceNameFilterDescription", (Object)this);
        }
        if (string.equals("UserFilter")) {
            return DatabaseError.findMessage("DiagnosabilityMBeanUserFilterDescription", (Object)this);
        }
        Logger.getLogger("oracle.jdbc.driver").log(Level.SEVERE, "Got a request to describe an unexpected  Attribute: " + string);
        return super.getDescription(mBeanAttributeInfo);
    }

    @Override
    public TraceController getTraceController() {
        return this.tc;
    }

    public void trace(boolean bl, Feature ... featureArray) {
        this.tc.trace(bl, featureArray);
    }

    @Override
    public void suspend() {
        this.tc.suspend();
    }

    @Override
    public void resume() {
        this.tc.resume();
    }

    @Override
    public void trace(boolean bl, String string) {
        for (Feature feature : this.tc.getSupportedFeatures()) {
            if (0 != feature.toString().compareToIgnoreCase(string)) continue;
            this.tc.trace(bl, feature);
            return;
        }
        throw new IllegalArgumentException("unknown \"" + string + "\", supported: " + Arrays.toString((Object[])this.tc.getSupportedFeatures()));
    }

    @Override
    public void enableContinousLogging() {
        OracleLog.enableContinousLogging();
    }

    @Override
    public void disableContinousLogging() {
        OracleLog.disableContinousLogging();
    }

    @Override
    public void enableInMemoryLogging() {
        OracleLog.enableInMemoryLogging();
    }

    @Override
    public void disableInMemoryLogging() {
        OracleLog.disableInMemoryLogging();
    }

    @Override
    public void setServiceNameFilter(String string) {
        OracleLog.setServiceNameFilter(string);
    }

    @Override
    public void setUserFilter(String string) {
        OracleLog.setUserNameFilter(string);
    }
}

