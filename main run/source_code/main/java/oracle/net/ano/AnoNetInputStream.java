/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import oracle.net.ano.CryptoDataPacket;
import oracle.net.ns.NetInputStream;
import oracle.net.ns.SessionAtts;

public class AnoNetInputStream
extends NetInputStream {
    public AnoNetInputStream(SessionAtts sessionAtts) {
        super(sessionAtts);
        this.daPkt = new CryptoDataPacket(sessionAtts);
    }

    @Override
    protected void processMarker() {
        this.sAtts.ano.setRenewKey(true);
    }
}

