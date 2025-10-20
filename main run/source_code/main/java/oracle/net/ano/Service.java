/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import java.util.Vector;
import oracle.net.ano.Ano;
import oracle.net.ano.AnoComm;
import oracle.net.ns.NetException;
import oracle.net.ns.SQLnetDef;
import oracle.net.ns.SessionAtts;

public abstract class Service
implements SQLnetDef {
    static final String[] J = new String[]{"", "SupervisorService", "AuthenticationService", "EncryptionService", "DataIntegrityService"};
    protected Ano ano;
    protected AnoComm K;
    protected SessionAtts sAtts;
    protected int level;
    protected int[] L;
    protected byte[] M;
    protected int N;
    protected long s;
    protected short O;

    int a(SessionAtts sessionAtts) {
        this.sAtts = sessionAtts;
        this.ano = sessionAtts.ano;
        this.K = sessionAtts.ano.a;
        this.level = 0;
        this.M = new byte[0];
        return 1;
    }

    final void h(int n2) {
        this.K.c(this.N);
        this.K.c(n2);
        this.K.b(0L);
    }

    void q() {
        this.h(2);
        this.K.e();
        this.K.d(this.M);
    }

    int r() {
        return 12 + this.M.length;
    }

    abstract void g(int var1);

    abstract void x();

    void y() {
    }

    static int[] a(AnoComm anoComm) {
        int n2 = anoComm.readUB2();
        int n3 = anoComm.readUB2();
        int n4 = (int)anoComm.readUB4();
        int[] nArray = new int[3];
        int[] nArray2 = nArray;
        nArray[0] = n2;
        nArray2[1] = n3;
        nArray2[2] = n4;
        return nArray2;
    }

    static int[] a(int[] nArray, int n2) {
        int[] nArray2;
        switch (n2) {
            case 0: {
                int[] nArray3 = new int[nArray.length + 1];
                nArray2 = nArray3;
                nArray3[0] = 0;
                for (int i2 = 0; i2 < nArray.length; ++i2) {
                    nArray2[i2 + 1] = nArray[i2];
                }
                break;
            }
            case 1: {
                int[] nArray4 = new int[1];
                nArray2 = nArray4;
                nArray4[0] = 0;
                break;
            }
            case 2: {
                nArray2 = new int[nArray.length + 1];
                for (int i3 = 0; i3 < nArray.length; ++i3) {
                    nArray2[i3] = nArray[i3];
                }
                nArray2[i3] = 0;
                break;
            }
            case 3: {
                nArray2 = nArray;
                break;
            }
            default: {
                throw new NetException(304);
            }
        }
        return nArray2;
    }

    static String[] a(String[] stringArray, String[] stringArray2) {
        int n2;
        int n3;
        if (stringArray == null || stringArray.length == 0) {
            if (stringArray2[0] == "") {
                stringArray = new String[stringArray2.length - 1];
                for (int i2 = 0; i2 < stringArray.length; ++i2) {
                    stringArray[i2] = stringArray2[i2 + 1];
                }
            } else {
                stringArray = stringArray2;
            }
        }
        Vector<String> vector = new Vector<String>(10);
        for (n3 = 0; n3 < stringArray.length; ++n3) {
            if (stringArray[n3].equals("")) {
                throw new NetException(303);
            }
            for (n2 = 0; n2 < stringArray2.length; ++n2) {
                if (!stringArray2[n2].equalsIgnoreCase(stringArray[n3])) continue;
                vector.addElement(stringArray2[n2]);
                break;
            }
            if (n2 != stringArray2.length) continue;
            throw new NetException(303);
        }
        n3 = vector.size();
        stringArray = new String[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            stringArray[n2] = (String)vector.elementAt(n2);
        }
        return stringArray;
    }

    static byte a(String[] stringArray, String string) {
        for (byte by = 0; by < stringArray.length; by = (byte)(by + 1)) {
            if (!string.equals(stringArray[by])) continue;
            return by;
        }
        throw new NetException(309);
    }

    public static String getLevelString(int n2) {
        String string;
        switch (n2) {
            case 0: {
                string = "ACCEPTED";
                break;
            }
            case 1: {
                string = "REJECTED";
                break;
            }
            case 2: {
                string = "REQUESTED";
                break;
            }
            case 3: {
                string = "REQUIRED";
                break;
            }
            default: {
                throw new NetException(322);
            }
        }
        return string;
    }

    public static String getServiceName(int n2) {
        String string;
        switch (n2) {
            case 1: {
                string = "AUTHENTICATION";
                break;
            }
            case 2: {
                string = "ENCRYPTION";
                break;
            }
            case 3: {
                string = "DATAINTEGRITY";
                break;
            }
            case 4: {
                string = "SUPERVISOR";
                break;
            }
            default: {
                throw new NetException(323);
            }
        }
        return string;
    }

    public boolean isActive() {
        return false;
    }
}

