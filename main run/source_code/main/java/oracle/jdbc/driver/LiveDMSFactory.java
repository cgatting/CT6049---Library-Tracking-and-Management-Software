/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  oracle.dms.console.DMSConsole
 *  oracle.dms.console.EventFactoryIntf
 *  oracle.dms.console.NounFactoryIntf
 *  oracle.dms.console.PhaseEventFactoryIntf
 *  oracle.dms.console.StateFactoryIntf
 *  oracle.dms.context.ECForJDBC
 *  oracle.dms.instrument.DMSConsole
 *  oracle.dms.instrument.EventIntf
 *  oracle.dms.instrument.ExecutionContextForJDBC
 *  oracle.dms.instrument.NounIntf
 *  oracle.dms.instrument.PhaseEventIntf
 *  oracle.dms.instrument.Sensor
 *  oracle.dms.instrument.StateIntf
 */
package oracle.jdbc.driver;

import java.util.Map;
import oracle.dms.console.EventFactoryIntf;
import oracle.dms.console.NounFactoryIntf;
import oracle.dms.console.PhaseEventFactoryIntf;
import oracle.dms.console.StateFactoryIntf;
import oracle.dms.context.ECForJDBC;
import oracle.dms.instrument.DMSConsole;
import oracle.dms.instrument.EventIntf;
import oracle.dms.instrument.ExecutionContextForJDBC;
import oracle.dms.instrument.NounIntf;
import oracle.dms.instrument.PhaseEventIntf;
import oracle.dms.instrument.Sensor;
import oracle.dms.instrument.StateIntf;
import oracle.jdbc.driver.DMSFactory;

public class LiveDMSFactory
extends DMSFactory {
    protected final EventFactoryIntf eventFactory;
    protected final NounFactoryIntf nounFactory;
    protected final PhaseEventFactoryIntf phaseEventFactory;
    protected final StateFactoryIntf stateFactory;

    protected LiveDMSFactory() throws ClassNotFoundException {
        block2: {
            this.eventFactory = oracle.dms.console.DMSConsole.getConsole().newEventFactory();
            this.nounFactory = oracle.dms.console.DMSConsole.getConsole().newNounFactory();
            this.phaseEventFactory = oracle.dms.console.DMSConsole.getConsole().newPhaseEventFactory();
            this.stateFactory = oracle.dms.console.DMSConsole.getConsole().newStateFactory();
            SensorIntf_all = 511;
            PhaseEventIntf_all = 511;
            SensorIntf_active = 64;
            SENSOR_WEIGHT = DMSConsole.getSensorWeight();
            DMSConole_NORMAL = 5;
            DMSFactory.Context.ECForJDBC.ACTION = "Action";
            DMSFactory.Context.ECForJDBC.CLIENTID = "client_identifier";
            DMSFactory.Context.ECForJDBC.ECID = "ECID_UID";
            DMSFactory.Context.ECForJDBC.MODULE = "Module";
            ecForJdbc = new LiveECForJDBC();
            executionContextForJDBC = new LiveExecutionContextForJDBC();
            Class.forName("oracle.core.ojdl.MessageType");
            try {
                Class.forName("oracle.dms.context.ECForJDBC");
                this.version = DMSFactory.DMSVersion.v11;
            }
            catch (ClassNotFoundException classNotFoundException) {
                if (executionContextForJDBC.getExecutionContextState() == null) break block2;
                this.version = DMSFactory.DMSVersion.v10G;
            }
        }
    }

    @Override
    public DMSFactory.DMSEvent createEvent(DMSFactory.DMSNoun dMSNoun, String string, String string2) {
        NounIntf nounIntf = dMSNoun == null ? null : ((LiveDMSNoun)dMSNoun).target;
        return new LiveDMSEvent(this.eventFactory.create(nounIntf, string, string2));
    }

    @Override
    public DMSFactory.DMSNoun createNoun(DMSFactory.DMSNoun dMSNoun, String string, String string2) {
        NounIntf nounIntf = dMSNoun == null ? null : ((LiveDMSNoun)dMSNoun).target;
        return new LiveDMSNoun(this.nounFactory.create(nounIntf == null ? ((LiveDMSNoun)this.getRoot()).target : nounIntf, string, string2 == null ? "n/a" : string2));
    }

    @Override
    public DMSFactory.DMSNoun createNoun(String string, String string2) {
        return new LiveDMSNoun(this.nounFactory.create(string, string2 == null ? "n/a" : string2));
    }

    @Override
    public DMSFactory.DMSPhase createPhaseEvent(DMSFactory.DMSNoun dMSNoun, String string, String string2) {
        NounIntf nounIntf = dMSNoun == null ? null : ((LiveDMSNoun)dMSNoun).target;
        return new LiveDMSPhase(this.phaseEventFactory.create(nounIntf, string, string2));
    }

    @Override
    public DMSFactory.DMSState createState(DMSFactory.DMSNoun dMSNoun, String string, String string2, String string3, int n2) {
        NounIntf nounIntf = dMSNoun == null ? null : ((LiveDMSNoun)dMSNoun).target;
        return new LiveDMSState(this.stateFactory.create(nounIntf, string, string2, string3, n2));
    }

    @Override
    public DMSFactory.DMSState createState(DMSFactory.DMSNoun dMSNoun, String string, String string2, String string3, Object object) {
        NounIntf nounIntf = dMSNoun == null ? null : ((LiveDMSNoun)dMSNoun).target;
        return new LiveDMSState(this.stateFactory.create(nounIntf, string, string2, string3, object));
    }

    @Override
    public DMSFactory.DMSNoun getRoot() {
        return new LiveDMSNoun(this.nounFactory.getRoot());
    }

    @Override
    public long getToken() {
        return this.phaseEventFactory.getToken();
    }

    @Override
    public DMSFactory.DMSNoun get(String string) {
        return new LiveDMSNoun(this.nounFactory.get(string));
    }

    protected class LiveDMSState
    extends DMSFactory.DMSState {
        protected final StateIntf target;

        protected LiveDMSState(StateIntf stateIntf) {
            this.target = stateIntf;
        }

        @Override
        public void deriveMetric(int n2) {
            this.target.deriveMetric(n2);
        }

        @Override
        public void destroy() {
            this.target.destroy();
        }

        @Override
        public void update(Object object) {
            this.target.update(object);
        }
    }

    protected class LiveDMSPhase
    extends DMSFactory.DMSPhase {
        protected final PhaseEventIntf target;

        protected LiveDMSPhase(PhaseEventIntf phaseEventIntf) {
            this.target = phaseEventIntf;
        }

        @Override
        public void deriveMetric(int n2) {
            this.target.deriveMetric(n2);
        }

        @Override
        public long start() {
            return this.target.start();
        }

        @Override
        public void start(long l2) {
            this.target.start(l2);
        }

        @Override
        public void stop(long l2) {
            this.target.stop(l2);
        }

        @Override
        public void destroy() {
            this.target.destroy();
        }
    }

    protected class LiveDMSEvent
    extends DMSFactory.DMSEvent {
        protected final EventIntf target;

        protected LiveDMSEvent(EventIntf eventIntf) {
            this.target = eventIntf;
        }

        @Override
        public void occurred() {
            this.target.occurred();
        }
    }

    protected class LiveDMSNoun
    extends DMSFactory.DMSNoun {
        protected final NounIntf target;

        protected LiveDMSNoun(NounIntf nounIntf) {
            this.target = nounIntf;
        }

        @Override
        public DMSFactory.DMSState getSensor(String string) {
            Sensor sensor = this.target.getSensor(string);
            return new LiveDMSState((StateIntf)sensor);
        }

        @Override
        public void destroy() {
            this.target.destroy();
        }
    }

    private class LiveExecutionContextForJDBC
    extends DMSFactory.ExecutionContextForJDBC {
        private LiveExecutionContextForJDBC() {
        }

        @Override
        public String[] getExecutionContextState() {
            return ExecutionContextForJDBC.getExecutionContextState();
        }

        @Override
        public int getECIDSequenceNumber() {
            return ExecutionContextForJDBC.getECIDSequenceNumber();
        }
    }

    protected class LiveECForJDBC
    extends DMSFactory.Context.ECForJDBC {
        protected LiveECForJDBC() {
        }

        @Override
        public boolean updateSqlText() {
            return ECForJDBC.updateSqlText();
        }

        @Override
        public Map<String, Map<String, String>> getMap() {
            return ECForJDBC.getMap();
        }

        @Override
        public void finished() {
            ECForJDBC.finished();
        }
    }
}

