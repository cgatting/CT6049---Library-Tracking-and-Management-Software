/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.HashMap;
import java.util.Map;

public class DMSFactory {
    public static int SensorIntf_all = 0;
    public static int PhaseEventIntf_all = 0;
    public static int SensorIntf_active = 0;
    protected static int SENSOR_WEIGHT = 0;
    protected static int DMSConole_NORMAL = 0;
    protected static Context.ECForJDBC ecForJdbc;
    protected static ExecutionContextForJDBC executionContextForJDBC;
    protected DMSVersion version = DMSVersion.NONE;
    private static final DMSFactory INSTANCE;

    private static DMSFactory createInstance() {
        try {
            return (DMSFactory)Class.forName("oracle.jdbc.driver.LiveDMSFactory").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (Throwable throwable) {
            return new DMSFactory();
        }
    }

    public static DMSFactory getInstance() {
        return INSTANCE;
    }

    protected DMSFactory() {
        ecForJdbc = new Context.ECForJDBC();
        executionContextForJDBC = new ExecutionContextForJDBC();
    }

    public static ExecutionContextForJDBC getExecutionContextForJDBC() {
        return executionContextForJDBC;
    }

    public static DMSVersion getDMSVersion() {
        return DMSFactory.INSTANCE.version;
    }

    public DMSEvent createEvent(DMSNoun dMSNoun, String string, String string2) {
        return new DMSEvent();
    }

    public DMSNoun createNoun(DMSNoun dMSNoun, String string, String string2) {
        return new DMSNoun();
    }

    public DMSNoun createNoun(String string, String string2) {
        return new DMSNoun();
    }

    public DMSPhase createPhaseEvent(DMSNoun dMSNoun, String string, String string2) {
        return new DMSPhase();
    }

    public DMSState createState(DMSNoun dMSNoun, String string, String string2, String string3, int n2) {
        return new DMSState();
    }

    public DMSState createState(DMSNoun dMSNoun, String string, String string2, String string3, Object object) {
        return new DMSState();
    }

    public DMSNoun getRoot() {
        return new DMSNoun();
    }

    public long getToken() {
        return -1L;
    }

    public DMSNoun get(String string) {
        return new DMSNoun();
    }

    static {
        INSTANCE = DMSFactory.createInstance();
    }

    public static class DMSConsole {
        public static int getSensorWeight() {
            return SENSOR_WEIGHT;
        }
    }

    public class DMSState
    implements Sensor {
        public void update(Object object) {
        }

        @Override
        public void deriveMetric(int n2) {
        }

        @Override
        public long start() {
            return 0L;
        }

        @Override
        public void stop(long l2) {
        }

        @Override
        public void destroy() {
        }
    }

    public class DMSPhase
    implements Sensor {
        @Override
        public void deriveMetric(int n2) {
        }

        @Override
        public long start() {
            return 0L;
        }

        @Override
        public void stop(long l2) {
        }

        public void start(long l2) {
        }

        @Override
        public void destroy() {
        }
    }

    public class DMSNoun {
        public Sensor getSensor(String string) {
            return new DMSState();
        }

        public void destroy() {
        }
    }

    private static interface Sensor {
        public void deriveMetric(int var1);

        public long start();

        public void stop(long var1);

        public void destroy();
    }

    public class DMSEvent {
        public void occurred() {
        }
    }

    protected class ExecutionContextForJDBC {
        protected ExecutionContextForJDBC() {
        }

        public String[] getExecutionContextState() {
            return null;
        }

        public int getECIDSequenceNumber() {
            return 0;
        }
    }

    public static class Context {
        private Context() {
        }

        public static ECForJDBC getECForJDBC() {
            return ecForJdbc;
        }

        protected static class ECForJDBC {
            public static String ACTION = "";
            public static String CLIENTID = "";
            public static String ECID = "";
            public static String MODULE = "";

            protected ECForJDBC() {
            }

            public boolean updateSqlText() {
                return false;
            }

            public Map<String, Map<String, String>> getMap() {
                return new HashMap<String, Map<String, String>>();
            }

            public void finished() {
            }
        }
    }

    public static enum DMSVersion {
        NONE,
        v10G,
        v11;

    }
}

