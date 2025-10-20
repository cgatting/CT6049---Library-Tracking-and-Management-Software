/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import oracle.net.ano.CryptoDataPacket;
import oracle.net.ns.NetOutputStream;
import oracle.net.ns.SessionAtts;

public class AnoNetOutputStream
extends NetOutputStream {
    public AnoNetOutputStream(SessionAtts sessionAtts) {
        super(sessionAtts);
        this.daPkt = new CryptoDataPacket(sessionAtts);
    }
}

