/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import java.util.ArrayList;
import oracle.net.ano.Service;
import oracle.net.aso.m;
import oracle.net.ns.NetException;
import oracle.net.ns.SQLnetDef;
import oracle.net.ns.SessionAtts;

public class EncryptionService
extends Service
implements SQLnetDef {
    static final String[] G = new String[]{"", "RC4_40", "RC4_56", "RC4_128", "RC4_256", "DES40C", "DES56C", "3DES112", "3DES168", "AES128", "AES192", "AES256"};
    private static final String[] V = new String[]{"", "AES128", "AES192", "AES256"};
    private final byte[] H = new byte[]{0, 1, 8, 10, 6, 3, 2, 11, 12, 15, 16, 17};
    private boolean I = false;

    @Override
    final int a(SessionAtts sessionAtts) {
        int n2;
        super.a(sessionAtts);
        this.N = 2;
        this.level = sessionAtts.profile.getEncryptionLevelNum();
        Object object = sessionAtts.profile.getEncryptionServices();
        if (!sessionAtts.profile.isWeakCryptoEnabled()) {
            String[] stringArray;
            String[] stringArray2 = object;
            object = this;
            if (stringArray2 == null || stringArray2.length == 0) {
                stringArray = stringArray2;
            } else {
                ArrayList<String> arrayList = new ArrayList<String>();
                for (String string : stringArray2) {
                    String string2 = string;
                    if (!"AES128".equalsIgnoreCase(string2) && !"AES192".equalsIgnoreCase(string2) && !"AES256".equalsIgnoreCase(string2)) continue;
                    arrayList.add(string);
                }
                if (object.level != 1 && arrayList.size() == 0) {
                    throw new NetException(317, "(Weak encryption algorithms are disabled)");
                }
                stringArray = arrayList.toArray(new String[arrayList.size()]);
            }
            object = stringArray;
        }
        object = EncryptionService.a(object, sessionAtts.profile.isWeakCryptoEnabled() ? G : V);
        this.L = new int[((String[])object).length];
        for (n2 = 0; n2 < this.L.length; ++n2) {
            this.L[n2] = EncryptionService.a(G, object[n2]);
        }
        this.L = EncryptionService.a(this.L, this.level);
        this.M = new byte[this.L.length];
        for (n2 = 0; n2 < this.M.length; ++n2) {
            this.M[n2] = this.H[this.L[n2]];
        }
        n2 = 1;
        if (this.L.length == 0) {
            if (this.level == 3) {
                throw new NetException(315);
            }
            n2 = 9;
        } else if (this.level == 3) {
            n2 = 17;
        }
        return n2;
    }

    @Override
    public boolean isActive() {
        return this.I;
    }

    @Override
    final void g(int n2) {
        if (n2 != 2) {
            throw new NetException(305);
        }
        this.s = this.K.l();
        this.sAtts.profile.setANOVersion(this.s);
        n2 = this.K.g();
        this.O = (short)-1;
        int n3 = 0;
        while (true) {
            if (n3 >= 12) break;
            if (this.H[n3] == n2) {
                this.O = (short)n3;
            }
            ++n3;
        }
        this.I = this.O > 0;
    }

    @Override
    final void x() {
        if (this.O < 0) {
            throw new NetException(316);
        }
        if (this.I) {
            if (this.sAtts.profile.isServerUsingWeakCrypto() && !this.sAtts.profile.isWeakCryptoEnabled()) {
                throw new NetException(12268);
            }
        } else if (this.level == 3) {
            throw new NetException(321, "Encryption is REQUIRED but activation failed.");
        }
        for (int i2 = 0; i2 < this.L.length; ++i2) {
            if (this.L[i2] != this.O) continue;
            return;
        }
        throw new NetException(316);
    }

    @Override
    final void y() {
        if (this.I) {
            this.ano.f = m.a(G[this.O], this.sAtts.ano.d(), this.ano.b(), this.ano.a(), this.sAtts.profile.useWeakCrypto());
            this.sAtts.isEncryptionActive = true;
        }
    }
}

