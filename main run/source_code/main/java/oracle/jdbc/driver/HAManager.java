/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.simplefan.FanEventListener
 *  oracle.simplefan.FanManager
 *  oracle.simplefan.FanSubscription
 */
package oracle.jdbc.driver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.driver.NoSupportHAManager;
import oracle.jdbc.driver.OracleDriver;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.ServiceMember;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.net.nt.TimeoutInterruptHandler;
import oracle.simplefan.FanEventListener;
import oracle.simplefan.FanManager;
import oracle.simplefan.FanSubscription;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.HIGH_AVAILABILITY})
public class HAManager {
    private ConcurrentHashMap<String, Map<String, ServiceMember>> services;
    private ConcurrentHashMap<String, Map<String, ServiceMember>> hosts;
    private Map<String, Integer> cardinalities = new HashMap<String, Integer>();
    private static final String FAN_SERVICE_NAME_PROPERTY = "serviceName";
    private FanManager fanMngr = null;
    private FanSubscription fanSubscription = null;
    static final ConcurrentHashMap<String, HAManager> allManagers = new ConcurrentHashMap();
    static final String FAN_CONFIG_PROPERTY = "oracle.jdbc.fanONSConfig";
    private int drainIntervals = 0;
    private final AtomicInteger remainingIntervals = new AtomicInteger();
    private int targetDrainCountTotal = 0;
    private int targetDrainCountPerInterval = 0;
    private final AtomicInteger currentDrainTarget = new AtomicInteger();
    private final AtomicInteger currentDrainCount = new AtomicInteger();
    private DrainingTask drainingTask = null;
    private DrainingTask delayedDrainingTask = null;
    private static AtomicBoolean dependentJarsChecked = new AtomicBoolean(false);
    private static boolean allDependentJarsPresent = true;

    HAManager() {
        this.services = new ConcurrentHashMap();
        this.hosts = new ConcurrentHashMap();
    }

    HAManager(String string) throws SQLException {
        this();
        this.configure(string);
    }

    static HAManager getInstance(String string) throws SQLException {
        return new HAManager(string);
    }

    private void configure(String string) throws SQLException {
        block2: {
            assert (string != null && !"".equals(string)) : "onsConfigStr: " + string;
            if (this.fanMngr != null) break block2;
            this.fanMngr = FanManager.getInstance();
            if (string != null && !"".equals(string)) {
                Properties properties = new Properties();
                properties.setProperty("onsRemoteConfig", string);
                this.fanMngr.configure(properties);
            }
        }
    }

    private static final Constructor<?> getListenerConstructor() {
        return CHolder.c;
    }

    private void addService(String string) throws SQLException {
        block3: {
            assert (string != null && !"".equals(string)) : "currentServiceName: " + string;
            if (this.fanMngr == null) break block3;
            Properties properties = new Properties();
            properties.setProperty(FAN_SERVICE_NAME_PROPERTY, string);
            this.fanSubscription = this.fanMngr.subscribe(properties);
            FanEventListener fanEventListener = null;
            try {
                fanEventListener = (FanEventListener)HAManager.getListenerConstructor().newInstance(this);
            }
            catch (Throwable throwable) {
                throw new SQLException("Metadata error: error while creating FAN listener");
            }
            this.fanSubscription.addListener(fanEventListener);
        }
    }

    void addConnection(OracleConnection oracleConnection) throws SQLException {
        Properties properties = oracleConnection.getServerSessionInfo();
        String string = properties.getProperty("SERVICE_NAME");
        String string2 = HAManager.toLowerCase(string);
        String string3 = HAManager.toLowerCase(properties.getProperty("INSTANCE_NAME"));
        String string4 = HAManager.toLowerCase(properties.getProperty("DATABASE_NAME"));
        String string5 = HAManager.toLowerCase(properties.getProperty("SERVER_HOST"));
        String string6 = string2 + "###" + string3 + "###" + string4 + "###" + string5;
        ConcurrentHashMap<String, ServiceMember> concurrentHashMap = new ConcurrentHashMap<String, ServiceMember>();
        Map map = this.services.putIfAbsent(string2, concurrentHashMap);
        if (map == null) {
            this.addService(string);
            ServiceMember serviceMember = new ServiceMember(string2, string3, string4, string5);
            serviceMember.up();
            serviceMember.addConnection(oracleConnection);
            concurrentHashMap.put(string6, serviceMember);
        } else {
            ServiceMember serviceMember = (ServiceMember)map.get(string6);
            if (serviceMember != null) {
                serviceMember.up();
                serviceMember.addConnection(oracleConnection);
            } else {
                serviceMember = new ServiceMember(string2, string3, string4, string5);
                serviceMember.up();
                serviceMember.addConnection(oracleConnection);
                map.put(string6, serviceMember);
            }
        }
        oracleConnection.setHAManager(this);
    }

    public void dropConnection(OracleConnection oracleConnection) throws SQLException {
        Properties properties = oracleConnection.getServerSessionInfo();
        String string = HAManager.toLowerCase(properties.getProperty("SERVICE_NAME"));
        String string2 = HAManager.toLowerCase(properties.getProperty("INSTANCE_NAME"));
        String string3 = HAManager.toLowerCase(properties.getProperty("DATABASE_NAME"));
        String string4 = HAManager.toLowerCase(properties.getProperty("SERVER_HOST"));
        String string5 = string + "###" + string2 + "###" + string3 + "###" + string4;
        Map<String, ServiceMember> map = this.services.get(string);
        if (map == null) {
            throw new SQLException("Metadata error: no member instances for service");
        }
        ServiceMember serviceMember = map.get(string5);
        if (serviceMember == null) {
            throw new SQLException("Metadata error: no member instance for service");
        }
        serviceMember.dropConnection(oracleConnection);
    }

    void plannedDown(String string, String string2, String string3, String string4, int n2) {
        try {
            Map<String, ServiceMember> map = this.services.get(string);
            if (map == null) {
                return;
            }
            if (string2 == null) {
                for (ServiceMember serviceMember : map.values()) {
                    if (!serviceMember.getDatabase().equalsIgnoreCase(string3)) continue;
                    serviceMember.down();
                }
            } else {
                String string5 = string + "###" + string2 + "###" + string3 + "###" + string4;
                ServiceMember serviceMember = map.get(string5);
                if (serviceMember != null) {
                    serviceMember.down();
                    this.drainGracefully(serviceMember, map, n2);
                }
            }
        }
        catch (Throwable throwable) {
        }
    }

    private void drainGracefully(ServiceMember serviceMember, Map<String, ServiceMember> map, int n2) {
        if (n2 > 0) {
            this.drainIntervals = (n2 + 10 - 1) / 10;
            this.remainingIntervals.set(this.drainIntervals);
            this.targetDrainCountTotal = serviceMember.connCount;
            this.targetDrainCountPerInterval = (this.targetDrainCountTotal + this.drainIntervals - 1) / this.drainIntervals;
            this.currentDrainCount.set(0);
            this.currentDrainTarget.set(0);
            this.drainingTask = new DrainingTask();
            boolean bl = true;
            for (ServiceMember serviceMember2 : map.values()) {
                if (!serviceMember2.isUp()) continue;
                bl = false;
                break;
            }
            if (bl) {
                this.delayedDrainingTask = this.drainingTask;
            } else {
                this.scheduleDrainingTask(this.drainingTask, this.drainIntervals);
            }
        }
    }

    private void scheduleDrainingTask(DrainingTask drainingTask, int n2) {
        if (n2 > 0) {
            drainingTask.scheduleRepeatedExecution(n2);
        } else {
            drainingTask.scheduleExecution();
        }
    }

    void unplannedDown(String string, String string2, String string3, String string4) {
        try {
            Map<String, ServiceMember> map = this.services.get(string);
            if (map == null) {
                return;
            }
            if (string2 == null) {
                for (ServiceMember serviceMember : map.values()) {
                    if (!serviceMember.getDatabase().equalsIgnoreCase(string3)) continue;
                    serviceMember.down();
                    serviceMember.cleanupConnections();
                }
            } else {
                String string5 = string + "###" + string2 + "###" + string3 + "###" + string4;
                ServiceMember serviceMember = map.get(string5);
                if (serviceMember != null) {
                    serviceMember.down();
                    serviceMember.cleanupConnections();
                }
            }
        }
        catch (Throwable throwable) {
        }
    }

    void nodeDown(String string) {
        try {
            Map<String, ServiceMember> map = this.hosts.get(string);
            if (map == null) {
                return;
            }
            for (ServiceMember serviceMember : map.values()) {
                serviceMember.down();
                serviceMember.cleanupConnections();
            }
        }
        catch (Throwable throwable) {
        }
    }

    void serviceUp(String string, String string2, String string3, String string4, int n2) {
        try {
            String string5;
            if (!this.services.containsKey(string)) {
                return;
            }
            if (string2 == null) {
                this.cardinalities.put(string, n2);
                return;
            }
            Map<String, ServiceMember> map = this.services.get(string);
            ServiceMember serviceMember = map.get(string5 = string + "###" + string2 + "###" + string3 + "###" + string4);
            if (serviceMember != null) {
                serviceMember.up();
                this.cardinalities.put(string, n2);
                if (this.delayedDrainingTask != null) {
                    this.scheduleDrainingTask(this.drainingTask, this.drainIntervals);
                }
            }
        }
        catch (Throwable throwable) {
        }
    }

    public boolean isServiceMemberDown(OracleConnection oracleConnection) throws SQLException {
        Properties properties = oracleConnection.getServerSessionInfo();
        String string = HAManager.toLowerCase(properties.getProperty("SERVICE_NAME"));
        String string2 = HAManager.toLowerCase(properties.getProperty("INSTANCE_NAME"));
        String string3 = HAManager.toLowerCase(properties.getProperty("DATABASE_NAME"));
        String string4 = HAManager.toLowerCase(properties.getProperty("SERVER_HOST"));
        String string5 = string + "###" + string2 + "###" + string3 + "###" + string4;
        Map<String, ServiceMember> map = this.services.get(string);
        if (map != null) {
            ServiceMember serviceMember = map.get(string5);
            return serviceMember.isDown();
        }
        return false;
    }

    public boolean checkAndDrain(OracleConnection oracleConnection) throws SQLException {
        boolean bl = false;
        if (this.drainIntervals > 0) {
            if (this.remainingIntervals.get() == 0) {
                if (this.drainingTask != null) {
                    this.drainingTask.cancel();
                    this.drainingTask = null;
                }
                if (this.delayedDrainingTask != null) {
                    this.delayedDrainingTask.cancel();
                    this.delayedDrainingTask = null;
                }
                this.drainIntervals = 0;
                bl = true;
            } else {
                bl = false;
            }
        } else {
            bl = true;
        }
        if (this.isServiceMemberDown(oracleConnection) && (bl || this.currentDrainCount.get() < this.currentDrainTarget.get())) {
            ((PhysicalConnection)oracleConnection).closeConnectionSafely();
            this.currentDrainCount.incrementAndGet();
            return true;
        }
        return false;
    }

    public static void enableHAIfNecessary(final String string, final OracleConnection oracleConnection) throws SQLException {
        try {
            OracleDriver.getExecutorService().submit(new Runnable(){

                @Override
                public void run() {
                    try {
                        HAManager.enableHAIfNecessaryAsync(string, oracleConnection);
                    }
                    catch (Throwable throwable) {
                    }
                }
            });
        }
        catch (Throwable throwable) {
        }
    }

    public static void enableHAIfNecessaryAsync(String string, OracleConnection oracleConnection) throws SQLException {
        Object object;
        boolean bl = true;
        if (dependentJarsChecked.compareAndSet(false, true)) {
            try {
                object = oracleConnection.getClass().getClassLoader();
                Class.forName("oracle.simplefan.FanManager", false, (ClassLoader)object);
                Class.forName("oracle.ons.ONS", false, (ClassLoader)object);
                allDependentJarsPresent = true;
            }
            catch (Throwable throwable) {
                allDependentJarsPresent = false;
                bl = false;
            }
        }
        if (!allDependentJarsPresent) {
        } else {
            object = allManagers.get(string);
            if (object == null) {
                String string2 = System.getProperty(FAN_CONFIG_PROPERTY);
                short s2 = oracleConnection.getVersionNumber();
                String string3 = null;
                if (s2 < 11100) {
                    bl = false;
                } else if (string2 != null && !"".equals(string2)) {
                    bl = true;
                    string3 = string2;
                } else {
                    Properties properties = oracleConnection.getServerSessionInfo();
                    String string4 = properties.getProperty("AUTH_ONS_CONFIG");
                    if (s2 >= 12101) {
                        if (string4 == null) {
                            bl = false;
                        } else {
                            bl = true;
                            string3 = string4;
                        }
                    } else {
                        bl = false;
                    }
                }
                if (bl) {
                    try {
                        object = HAManager.getInstance(string3);
                    }
                    catch (Throwable throwable) {
                        bl = false;
                        object = NoSupportHAManager.getInstance();
                    }
                } else {
                    object = NoSupportHAManager.getInstance();
                }
                allManagers.putIfAbsent(string, (HAManager)object);
            }
            ((HAManager)object).addConnection(oracleConnection);
        }
    }

    public static void shutdownAll() {
        for (HAManager hAManager : allManagers.values()) {
            if (hAManager == null) continue;
            hAManager.close();
        }
    }

    public void close() {
        if (this.fanSubscription != null) {
            this.fanSubscription.close();
        }
        if (this.drainingTask != null) {
            this.drainingTask.cancel();
        }
        if (this.delayedDrainingTask != null) {
            this.delayedDrainingTask.cancel();
        }
    }

    static final String getStackTraceString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter(1024);
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return ((Object)stringWriter).toString();
    }

    private static String toLowerCase(String string) {
        return null == string ? null : string.toLowerCase();
    }

    private final class DrainingTask {
        private TimerTask scheduledTimerTask = null;

        private DrainingTask() {
        }

        private void updateDrainTarget() {
            if (HAManager.this.remainingIntervals.get() > 0) {
                int n2 = HAManager.this.targetDrainCountTotal - HAManager.this.currentDrainCount.get();
                int n3 = Math.min(HAManager.this.targetDrainCountPerInterval, n2);
                HAManager.this.currentDrainTarget.addAndGet(n3);
                HAManager.this.remainingIntervals.decrementAndGet();
            }
        }

        boolean cancel() {
            if (this.scheduledTimerTask == null) {
                return false;
            }
            return this.scheduledTimerTask.cancel();
        }

        void scheduleExecution() {
            assert (this.scheduledTimerTask == null) : "Task is already scheduled";
            this.scheduledTimerTask = TimeoutInterruptHandler.scheduleTask(this::updateDrainTarget, 0L);
        }

        void scheduleRepeatedExecution(int n2) {
            assert (this.scheduledTimerTask == null) : "Task is already scheduled";
            this.scheduledTimerTask = TimeoutInterruptHandler.scheduleFixedDelayRepeatingTask(this::updateDrainTarget, (long)n2 * 1000L);
        }
    }

    private static final class CHolder {
        private static Class<?> cls;
        private static Constructor<?> c;

        private CHolder() {
        }

        static {
            try {
                cls = Class.forName("oracle.jdbc.driver.HAFanListener");
                c = cls.getDeclaredConstructor(HAManager.class);
            }
            catch (Throwable throwable) {
                Logger.getLogger("oracle.jdbc.driver").log(Level.SEVERE, "========= FAN listener constructor error: ", throwable);
                c = null;
                cls = null;
            }
        }
    }
}

