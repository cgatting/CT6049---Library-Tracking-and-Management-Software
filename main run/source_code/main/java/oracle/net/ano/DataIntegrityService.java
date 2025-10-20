/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import java.io.IOException;
import java.util.ArrayList;
import oracle.net.ano.Service;
import oracle.net.aso.k;
import oracle.net.aso.l;
import oracle.net.ns.NetException;
import oracle.net.ns.SQLnetDef;
import oracle.net.ns.SessionAtts;

public class DataIntegrityService
extends Service
implements SQLnetDef {
    static final String[] D = new String[]{"", "MD5", "SHA1", "SHA512", "SHA256", "SHA384"};
    private static final byte[] E = new byte[]{0, 1, 3, 4, 5, 6};
    private static final String[] U = new String[]{"", "SHA1", "SHA512", "SHA256", "SHA384"};
    private boolean F = false;
    private byte[] b;

    @Override
    final int a(SessionAtts sessionAtts) {
        int n2;
        super.a(sessionAtts);
        this.N = 3;
        this.level = sessionAtts.profile.getDataIntegrityLevelNum();
        Object object = sessionAtts.profile.getDataIntegrityServices();
        if (!sessionAtts.profile.isWeakCryptoEnabled()) {
            String[] stringArray;
            String[] stringArray2 = object;
            object = this;
            if (stringArray2 == null || stringArray2.length == 0) {
                stringArray = stringArray2;
            } else {
                ArrayList<String> arrayList = new ArrayList<String>();
                for (String string : stringArray2) {
                    if ("MD5".equalsIgnoreCase(string)) continue;
                    arrayList.add(string);
                }
                if (object.level != 1 && arrayList.size() == 0) {
                    throw new NetException(318, "(Weak checksumming algorithms are disabled)");
                }
                stringArray = arrayList.toArray(new String[arrayList.size()]);
            }
            object = stringArray;
        }
        object = DataIntegrityService.a(object, sessionAtts.profile.isWeakCryptoEnabled() ? D : U);
        this.L = new int[((String[])object).length];
        for (n2 = 0; n2 < this.L.length; ++n2) {
            this.L[n2] = DataIntegrityService.a(D, object[n2]);
        }
        this.L = DataIntegrityService.a(this.L, this.level);
        this.M = new byte[this.L.length];
        for (n2 = 0; n2 < this.M.length; ++n2) {
            this.M[n2] = E[this.L[n2]];
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
    final void g(int n2) {
        this.s = this.K.l();
        this.sAtts.profile.setANOVersion(this.s);
        short s2 = this.K.g();
        this.O = (short)-1;
        short s3 = 0;
        while (true) {
            if (s3 >= 6) break;
            if (E[s3] == s2) {
                this.O = s3;
            }
            ++s3;
        }
        if (n2 != 2 && n2 == 8) {
            s3 = (short)this.K.h();
            n2 = (short)this.K.h();
            byte[] byArray = this.K.n();
            byte[] byArray2 = this.K.n();
            byte[] byArray3 = this.K.n();
            byte[] byArray4 = this.K.n();
            if (s3 <= 0 || n2 <= 0) {
                throw new IOException("Bad parameters from server");
            }
            int n3 = (n2 + 7) / 8;
            if (byArray3.length != n3 || byArray2.length != n3) {
                throw new IOException("DiffieHellman negotiation out of synch");
            }
            Object object = l.a(byArray, byArray2, s3, (short)n2, this.sAtts.ano.d(), this.sAtts.profile.isFIPSMode());
            this.b = ((l)object).aa();
            this.sAtts.ano.a(this.b);
            object = ((l)object).f(byArray3, byArray3.length);
            this.sAtts.ano.b(byArray4);
            this.sAtts.ano.c((byte[])object);
        }
        this.F = this.O > 0;
    }

    @Override
    final void x() {
        if (this.O < 0) {
            throw new NetException(319);
        }
        if (this.F) {
            if (this.sAtts.profile.isServerUsingWeakCrypto() && !this.sAtts.profile.isWeakCryptoEnabled()) {
                throw new NetException(12268);
            }
        } else if (this.level == 3) {
            throw new NetException(321, "Checksumming is REQUIRED but activation failed.");
        }
        for (int i2 = 0; i2 < this.L.length; ++i2) {
            if (this.L[i2] != this.O) continue;
            return;
        }
        throw new NetException(319);
    }

    @Override
    public boolean isActive() {
        return this.F;
    }

    @Override
    final void y() {
        if (this.F) {
            this.ano.g = new k(this.ano.b(), this.ano.a(), this.ano.getDataIntegrityName(), this.ano.d(), this.sAtts.profile.useWeakCrypto());
            this.sAtts.isChecksumActive = true;
        }
    }

    public static void printInHex(int n2) {
        byte[] byArray = DataIntegrityService.toHex(n2);
        System.out.print(new String(byArray));
    }

    public static byte[] toHex(int n2) {
        byte[] byArray = new byte[8];
        for (int i2 = 7; i2 >= 0; --i2) {
            byArray[i2] = DataIntegrityService.nibbleToHex((byte)(n2 & 0xF));
            n2 >>= 4;
        }
        return byArray;
    }

    public static byte nibbleToHex(byte by) {
        return (byte)((by = (byte)(by & 0xF)) < 10 ? by + 48 : by - 10 + 65);
    }

    public static String bArray2String(byte[] byArray) {
        StringBuffer stringBuffer = new StringBuffer(byArray.length << 1);
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            stringBuffer.append((char)DataIntegrityService.nibbleToHex((byte)((byArray[i2] & 0xF0) >> 4)));
            stringBuffer.append((char)DataIntegrityService.nibbleToHex((byte)(byArray[i2] & 0xF)));
        }
        return stringBuffer.toString();
    }
}

