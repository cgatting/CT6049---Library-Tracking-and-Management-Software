/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import oracle.net.ano.AnoComm;
import oracle.net.ano.AnoCommNIO;
import oracle.net.ano.AnoCommStream;
import oracle.net.ano.AnoNetInputStream;
import oracle.net.ano.AnoNetOutputStream;
import oracle.net.ano.AuthenticationService;
import oracle.net.ano.CryptoNIONSDataChannel;
import oracle.net.ano.DataIntegrityService;
import oracle.net.ano.EncryptionService;
import oracle.net.ano.Service;
import oracle.net.aso.k;
import oracle.net.aso.m;
import oracle.net.ns.NetException;
import oracle.net.ns.SQLnetDef;
import oracle.net.ns.SessionAtts;
import org.ietf.jgss.GSSCredential;

public class Ano
implements SQLnetDef {
    private SessionAtts sAtts;
    protected AnoComm a;
    private byte[] b;
    private byte[] c;
    private byte[] d;
    private boolean e = false;
    protected m f;
    protected k g;
    private int h = 1;
    private Service[] i;
    private byte[] j;
    private boolean k;
    private boolean l = false;

    public void init(SessionAtts sessionAtts, boolean n2, boolean bl) {
        this.sAtts = sessionAtts;
        this.sAtts.ano = this;
        this.l = bl;
        this.i = new Service[5];
        this.a = n2 == 0 ? new AnoCommStream(sessionAtts) : new AnoCommNIO(sessionAtts);
        n2 = 1;
        while (true) {
            Service service;
            if (n2 >= 5) break;
            try {
                service = (Service)Class.forName("oracle.net.ano." + Service.J[n2]).newInstance();
            }
            catch (Exception exception) {
                throw new NetException(308);
            }
            this.h |= service.a(sessionAtts);
            this.i[service.N] = service;
            ++n2;
        }
        if ((this.h & 0x10) > 0 && (this.h & 8) > 0) {
            this.h &= 0xFFFFFFEF;
        }
    }

    public void negotiation(boolean bl, boolean bl2, GSSCredential gSSCredential) {
        int n2;
        Service service;
        int n3;
        int n4 = 0;
        for (n3 = 1; n3 < 5; ++n3) {
            service = this.i[n3];
            n4 += 8 + service.r();
        }
        n3 = n4 + 13;
        if (this.sAtts.poolEnabled && bl) {
            n3 += 16;
        }
        this.a(n3, this.i.length - 1, (short)0);
        this.i[4].q();
        this.i[1].q();
        this.i[2].q();
        this.i[3].q();
        this.a.flush();
        int[] nArray = this.c();
        for (n4 = 0; n4 < nArray[2]; ++n4) {
            int[] nArray2 = Service.a(this.a);
            if (nArray2[2] != 0) {
                throw new NetException(nArray2[2]);
            }
            n3 = nArray2[1];
            service = this.i[nArray2[0]];
            service.g(n3);
            service.x();
        }
        for (n4 = 1; n4 < 5; ++n4) {
            this.i[n4].y();
        }
        n4 = 0;
        n3 = 0;
        if (this.b != null) {
            n4 = 0 + (12 + this.b.length);
            ++n3;
        }
        if ((n2 = ((AuthenticationService)this.i[1]).s()) > 0) {
            n4 += n2;
            ++n3;
        }
        if (n4 > 0) {
            this.a(n4 += 13, n3, (short)0);
            if (this.b != null) {
                this.i[3].h(1);
                this.a.d(this.b);
            }
            if (n2 > 0) {
                ((AuthenticationService)this.i[1]).t();
            }
            this.a.flush();
            ((AuthenticationService)this.i[1]).a(gSSCredential);
        }
        boolean bl3 = this.e = this.i[2].isActive() || this.i[3].isActive();
        if (this.e) {
            if (!bl2) {
                this.sAtts.turnEncryptionOn(new AnoNetInputStream(this.sAtts), new AnoNetOutputStream(this.sAtts));
            } else {
                this.sAtts.turnEncryptionOn(new CryptoNIONSDataChannel(this.sAtts));
            }
        }
        if (bl2) {
            this.sAtts.payloadDataBufferForRead.position(this.sAtts.payloadDataBufferForRead.limit());
        }
    }

    public String getEncryptionProvider() {
        if (this.f != null) {
            return this.f.getProviderName();
        }
        return null;
    }

    public String getChecksumProvider() {
        if (this.g != null) {
            return this.g.getProviderName();
        }
        return null;
    }

    public byte[] getExternalAuthSessionKey() {
        if (this.i[1] != null && this.i[1].isActive()) {
            return ((AuthenticationService)this.i[1]).b();
        }
        return null;
    }

    public int getNAFlags() {
        return this.h;
    }

    public void setAuthSessionKey(byte[] byArray) {
        this.j = byArray;
    }

    public byte[] getAuthSessionKey() {
        return this.j;
    }

    public m getEncryptionAlg() {
        return this.f;
    }

    public k getDataIntegrityAlg() {
        return this.g;
    }

    public String getEncryptionName() {
        if (this.i == null || this.i.length <= 2) {
            return "";
        }
        return EncryptionService.G[this.i[2].O];
    }

    public String getDataIntegrityName() {
        if (this.i == null || this.i.length <= 3) {
            return "";
        }
        return DataIntegrityService.D[this.i[3].O];
    }

    public String getAuthenticationAdaptorName() {
        if (this.i == null || this.i.length <= 1) {
            return "";
        }
        return AuthenticationService.o[this.i[1].O];
    }

    public void setRenewKey(boolean bl) {
        this.k = bl;
    }

    public boolean getRenewKey() {
        return this.k;
    }

    protected final void a(byte[] byArray) {
        this.b = byArray;
    }

    protected final void b(byte[] byArray) {
        this.c = byArray;
    }

    protected final void c(byte[] byArray) {
        this.d = byArray;
    }

    protected final byte[] a() {
        return this.c;
    }

    protected final byte[] b() {
        return this.d;
    }

    protected final void a(int n2, int n3, short s2) {
        this.a.b(-559038737L);
        this.a.c(n2);
        this.a.f();
        this.a.c(n3);
        this.a.b((short)0);
    }

    public void checkForAnoNegotiationFailure() {
        int[] nArray;
        int n2 = this.sAtts.payloadDataBufferForRead.position();
        this.sAtts.payloadDataBufferForRead.rewind();
        try {
            nArray = this.c();
        }
        catch (NetException netException) {
            NetException netException2 = netException;
            if (netException.getErrorNumber() == 302) {
                this.sAtts.payloadDataBufferForRead.position(n2);
                return;
            }
            throw netException2;
        }
        for (int i2 = 0; i2 < nArray[2]; ++i2) {
            int[] nArray2 = Service.a(this.a);
            if (nArray2[2] == 0) continue;
            throw new NetException(nArray2[2]);
        }
        return;
        finally {
            this.sAtts.payloadDataBufferForRead.position(n2);
        }
    }

    final int[] c() {
        long l2 = this.a.readUB4();
        if (l2 != -559038737L) {
            throw new NetException(302);
        }
        int n2 = this.a.readUB2();
        int n3 = (int)this.a.readUB4();
        int n4 = this.a.readUB2();
        short s2 = this.a.o();
        int[] nArray = new int[4];
        int[] nArray2 = nArray;
        nArray[0] = n2;
        nArray2[1] = n3;
        nArray2[2] = n4;
        nArray2[3] = s2;
        return nArray2;
    }

    final boolean d() {
        return this.l;
    }

    final byte w() {
        int n2 = 0;
        if (this.j != null) {
            if (this.sAtts.profile.useWeakCrypto()) {
                n2 = Math.min(this.d.length, this.j.length);
                for (int i2 = 0; i2 < n2; ++i2) {
                    int n3 = i2;
                    this.d[n3] = (byte)(this.d[n3] ^ this.j[i2]);
                }
            } else {
                int n4;
                n2 = Math.min(32, this.j.length);
                for (n4 = 0; n4 < n2; ++n4) {
                    int n5 = n4;
                    this.d[n5] = (byte)(this.d[n5] ^ this.j[n4]);
                }
                for (n4 = 0; n4 < n2; ++n4) {
                    int n6 = n4 + 32;
                    this.d[n6] = (byte)(this.d[n6] ^ this.j[n4]);
                }
            }
            if (this.f != null) {
                this.f.a(this.d, this.c);
            }
            if (this.g != null) {
                this.g.d(this.d, this.c);
            }
            n2 = 1;
        }
        return (byte)n2;
    }
}

