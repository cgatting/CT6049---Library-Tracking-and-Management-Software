/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.sql.SQLException;
import oracle.jdbc.driver.T4CConnection;
import oracle.jdbc.driver.T4CMAREngine;
import oracle.jdbc.driver.XSKeyvalI;
import oracle.jdbc.driver.XSNamespaceI;
import oracle.jdbc.driver.XSPrincipalI;
import oracle.jdbc.driver.XSSessionNamespaceI;
import oracle.jdbc.internal.XSEvent;
import oracle.jdbc.internal.XSKeyval;
import oracle.jdbc.internal.XSNamespace;
import oracle.jdbc.internal.XSPrincipal;
import oracle.jdbc.internal.XSSessionNamespace;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFXSEvent
extends XSEvent {
    private static final long serialVersionUID = 301415165532636629L;
    private final XSNamespaceI[] kpxssyncns;
    private final XSNamespaceI[] kpxssyncinvalidns;
    private final XSPrincipalI[] kpxssyncroles;
    private final XSSessionNamespaceI[] kpxssyncsessns;
    private final int kpxssyncrolever;
    private final int kpxssyncsessflg;
    private final int kpxssynccacheflg;
    private final XSKeyvalI kvl;

    @Override
    public XSNamespace[] getNamespaces() {
        return this.kpxssyncns;
    }

    @Override
    public XSNamespace[] getInvalidNamespaces() {
        return this.kpxssyncinvalidns;
    }

    @Override
    public XSPrincipal[] getSessionRoles() {
        return this.kpxssyncroles;
    }

    @Override
    public XSSessionNamespace[] getSessionNamespaces() {
        return this.kpxssyncsessns;
    }

    @Override
    public int getRoleVersion() {
        return this.kpxssyncrolever;
    }

    @Override
    public long getSessionFlags() {
        return this.kpxssyncsessflg;
    }

    @Override
    public long getCacheFlags() {
        return this.kpxssynccacheflg;
    }

    @Override
    public XSKeyval getKeyval() {
        return this.kvl;
    }

    NTFXSEvent(T4CConnection t4CConnection) throws SQLException, IOException {
        super(t4CConnection);
        int n2;
        int n3;
        int n4;
        int n5;
        T4CMAREngine t4CMAREngine = t4CConnection.getMarshalEngine();
        int n6 = (int)t4CMAREngine.unmarshalUB4();
        if (n6 > 0) {
            t4CMAREngine.unmarshalUB1();
        }
        this.kpxssyncns = new XSNamespaceI[n6];
        for (n5 = 0; n5 < n6; ++n5) {
            this.kpxssyncns[n5] = XSNamespaceI.unmarshal(t4CMAREngine);
        }
        n5 = (int)t4CMAREngine.unmarshalUB4();
        if (n5 > 0) {
            t4CMAREngine.unmarshalUB1();
        }
        this.kpxssyncinvalidns = new XSNamespaceI[n5];
        for (n4 = 0; n4 < n5; ++n4) {
            this.kpxssyncinvalidns[n4] = XSNamespaceI.unmarshal(t4CMAREngine);
        }
        n4 = (int)t4CMAREngine.unmarshalUB4();
        if (n4 > 0) {
            t4CMAREngine.unmarshalUB1();
        }
        this.kpxssyncroles = new XSPrincipalI[n4];
        for (n3 = 0; n3 < n4; ++n3) {
            this.kpxssyncroles[n3] = XSPrincipalI.unmarshal(t4CMAREngine);
        }
        n3 = (int)t4CMAREngine.unmarshalUB4();
        if (n3 > 0) {
            t4CMAREngine.unmarshalUB1();
        }
        this.kpxssyncsessns = new XSSessionNamespaceI[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            t4CMAREngine.unmarshalUB1();
            t4CMAREngine.unmarshalUB1();
            t4CMAREngine.unmarshalUB1();
            this.kpxssyncsessns[n2] = XSSessionNamespaceI.unmarshal(t4CMAREngine);
        }
        this.kpxssyncrolever = (int)t4CMAREngine.unmarshalUB4();
        this.kpxssyncsessflg = (int)t4CMAREngine.unmarshalUB4();
        this.kpxssynccacheflg = (int)t4CMAREngine.unmarshalUB4();
        n2 = t4CMAREngine.unmarshalUB1();
        if (n2 != 0) {
            t4CMAREngine.unmarshalUB1();
            t4CMAREngine.unmarshalUB1();
            this.kvl = XSKeyvalI.unmarshal(t4CMAREngine);
        } else {
            this.kvl = null;
        }
    }
}

