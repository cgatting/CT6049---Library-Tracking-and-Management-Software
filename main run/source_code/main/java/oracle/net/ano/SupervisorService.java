/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import oracle.net.ano.Service;
import oracle.net.ns.NetException;
import oracle.net.ns.SQLnetDef;
import oracle.net.ns.SessionAtts;

public class SupervisorService
extends Service
implements SQLnetDef {
    private byte[] P;
    private int[] Q;
    private int[] R;
    private int S;
    private int T;

    @Override
    final int a(SessionAtts object) {
        super.a((SessionAtts)object);
        this.N = 4;
        object = new byte[8];
        for (int i2 = 0; i2 < 8; ++i2) {
            object[i2] = 9;
        }
        this.P = (byte[])object;
        this.S = 0;
        this.T = 2;
        this.Q = new int[4];
        this.Q[0] = 4;
        this.Q[1] = 1;
        this.Q[2] = 2;
        this.Q[3] = 3;
        return 1;
    }

    @Override
    final void q() {
        this.h(3);
        this.K.e();
        this.K.d(this.P);
        this.K.a(this.Q);
    }

    @Override
    final int r() {
        return 12 + this.P.length + 4 + 10 + (this.Q.length << 1);
    }

    @Override
    final void g(int n2) {
        this.s = this.K.l();
        n2 = this.K.k();
        if (n2 != 31) {
            throw new NetException(306);
        }
        this.R = this.K.j();
    }

    @Override
    final void x() {
        for (int i2 = 0; i2 < this.R.length; ++i2) {
            int n2;
            for (n2 = 0; n2 < this.Q.length; ++n2) {
                if (this.R[i2] != this.Q[n2]) continue;
                ++this.S;
                break;
            }
            if (n2 != this.Q.length) continue;
            throw new NetException(320);
        }
        if (this.S != this.T) {
            throw new NetException(321);
        }
    }
}

