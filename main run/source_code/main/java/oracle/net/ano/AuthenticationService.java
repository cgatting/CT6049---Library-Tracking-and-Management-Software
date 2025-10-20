/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.ano;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import oracle.net.ano.AuthenticationService$1;
import oracle.net.ano.Service;
import oracle.net.aso.b;
import oracle.net.ns.NetException;
import oracle.net.ns.SQLnetDef;
import oracle.net.ns.SessionAtts;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.security.krb5.Checksum;
import sun.security.krb5.EncryptedData;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.internal.APReq;
import sun.security.krb5.internal.Authenticator;
import sun.security.krb5.internal.KRBCred;

public class AuthenticationService
extends Service
implements PrivilegedExceptionAction,
SQLnetDef {
    static final String[] o = new String[]{"", "RADIUS", "KERBEROS5", "TCPS"};
    private static final String[] p = new String[]{"", "RADIUS", "KERBEROS5", "tcps"};
    private static final byte[] q = new byte[]{0, 1, 1, 2};
    private static Method r = null;
    private boolean t = false;
    private Subject u = null;
    private String v = null;
    private String w = null;
    private String x = null;
    private int status;
    private GSSCredential z = null;
    private static boolean y = true;

    @Override
    final int a(SessionAtts stringArray) {
        super.a((SessionAtts)stringArray);
        this.N = 1;
        this.status = 64767;
        stringArray = stringArray.profile.getAuthenticationServices();
        AuthenticationService.a(stringArray, o);
        this.L = new int[stringArray.length];
        for (int i2 = 0; i2 < this.L.length; ++i2) {
            this.L[i2] = AuthenticationService.a(o, stringArray[i2]);
        }
        return 1;
    }

    @Override
    final void q() {
        int n2 = 3 + (this.L.length << 1);
        this.h(n2);
        this.K.e();
        this.K.a(57569);
        this.K.b(this.status);
        for (n2 = 0; n2 < this.L.length; ++n2) {
            this.K.a(q[this.L[n2]]);
            this.K.a(p[this.L[n2]]);
        }
    }

    @Override
    final int r() {
        int n2 = 20;
        for (int i2 = 0; i2 < this.L.length; ++i2) {
            n2 += 5;
            n2 += 4 + p[this.L[i2]].length();
        }
        return n2;
    }

    @Override
    final void g(int n2) {
        this.s = this.K.l();
        this.sAtts.profile.setANOVersion(this.s);
        int n3 = this.K.k();
        if (n3 == 64255 && n2 > 2) {
            this.K.g();
            String string = this.K.m();
            this.O = AuthenticationService.a(p, string);
            if (n2 > 4) {
                this.K.l();
                this.K.i();
                this.K.i();
            }
            this.t = true;
            return;
        }
        if (n3 == 64511) {
            this.t = false;
            return;
        }
        throw new NetException(323, "Authentication service received status failure");
    }

    @Override
    public boolean isActive() {
        return this.t;
    }

    final byte[] b() {
        if (this.u == null) {
            return null;
        }
        return Subject.doAs(this.u, () -> {
            byte[] byArray = null;
            KerberosTicket kerberosTicket = this.J();
            if (kerberosTicket != null) {
                byArray = kerberosTicket.getSessionKey().getEncoded();
            }
            return byArray;
        });
    }

    private KerberosTicket J() {
        if (this.u != null) {
            for (Object object : this.u.getPrivateCredentials()) {
                String string;
                if (!(object instanceof KerberosTicket) || !(string = ((KerberosTicket)(object = (KerberosTicket)object)).getServer().getName()).startsWith(this.v) && !string.startsWith(this.w)) continue;
                return object;
            }
        }
        return null;
    }

    final int s() {
        if (this.isActive()) {
            if (this.O == 1) {
                return 32;
            }
            if (this.O == 2) {
                return 37;
            }
            return 0;
        }
        return 0;
    }

    final void t() {
        if (this.t) {
            if (this.O == 1) {
                this.h(3);
                this.K.e();
                this.K.a(2L);
                this.K.a(2L);
                return;
            }
            if (this.O == 2) {
                this.h(4);
                this.K.e();
                this.K.a(2L);
                this.K.a(2L);
                this.K.a((short)0);
            }
        }
    }

    final void a(GSSCredential object) {
        if (this.t) {
            this.sAtts.ano.c();
            Service.a(this.K);
            if (this.O == 1) {
                this.K.readUB2();
                this.K.readUB2();
                return;
            }
            if (this.O == 2) {
                Object object2 = this.K.m();
                String string = this.K.m();
                this.v = (String)object2 + "/" + string;
                this.w = (String)object2 + "@" + string;
                try {
                    object2 = InetAddress.getByName(string).getCanonicalHostName();
                    ((String)object2).toLowerCase().startsWith(string.toLowerCase() + ".");
                }
                catch (UnknownHostException unknownHostException) {
                    string.toLowerCase();
                }
                this.x = (String)this.sAtts.profile.get("oracle.net.KerberosRealm");
                if (this.x != null && this.x.indexOf(64) != -1) {
                    this.x = this.x.substring(this.x.indexOf(64));
                }
                this.z = object;
                object2 = AccessController.getContext();
                if (this.z == null) {
                    if (object2 != null) {
                        this.u = Subject.getSubject((AccessControlContext)object2);
                    }
                    if (this.u == null) {
                        this.u = this.u();
                    }
                }
                try {
                    Subject.doAs(this.u, this);
                    return;
                }
                catch (PrivilegedActionException privilegedActionException) {
                    object = privilegedActionException;
                    object2 = privilegedActionException.getException();
                    if (object2 instanceof NetException) {
                        object2 = (NetException)object2;
                    } else {
                        object2 = new NetException(323, ((Throwable)object).getMessage());
                        ((Throwable)object2).initCause((Throwable)object);
                    }
                    throw object2;
                }
            }
        }
    }

    private final Subject u() {
        Object object = this.sAtts.profile.getProperty("oracle.net.KerberosJaasLoginModule");
        try {
            if (object == null) {
                object = this;
                return AuthenticationService.a(new AuthenticationService$1((AuthenticationService)object), "defaultModule");
            }
            return AuthenticationService.a(Configuration.getConfiguration(), (String)object);
        }
        catch (Exception exception) {
            NetException netException = new NetException(325);
            netException.initCause(exception);
            throw netException;
        }
    }

    private static Subject a(Configuration object, String string) {
        object = new LoginContext(string, null, null, (Configuration)object);
        ((LoginContext)object).login();
        return ((LoginContext)object).getSubject();
    }

    public Object run() {
        try {
            Iterator<Principal> iterator;
            Set<Principal> set;
            GSSManager gSSManager = GSSManager.getInstance();
            Object object = new Oid("1.2.840.113554.1.2.2");
            Object object2 = new Oid("1.2.840.113554.1.2.2.1");
            byte[] byArray = ((Oid)object).getDER();
            Object object3 = null;
            if (this.z == null) {
                set = this.u.getPrincipals();
                iterator = set.iterator();
                if (iterator.hasNext() && (set = iterator.next()) instanceof KerberosPrincipal) {
                    object3 = (KerberosPrincipal)((Object)set);
                }
                if (object3 == null) {
                    throw new NetException(323, "Unable to find valid kerberos principal for authentication");
                }
            }
            set = object3 != null ? ((KerberosPrincipal)object3).getName() : null;
            iterator = this.x != null ? gSSManager.createName(this.v, (Oid)object2) : gSSManager.createName(this.w, GSSName.NT_HOSTBASED_SERVICE);
            if (this.z == null) {
                object2 = gSSManager.createName((String)((Object)set), (Oid)object2);
                set = gSSManager.createCredential((GSSName)object2, 0, (Oid)object, 1);
            } else {
                set = this.z;
            }
            object2 = gSSManager.createContext((GSSName)((Object)iterator), (Oid)object, (GSSCredential)((Object)set), 0);
            boolean bl = true;
            object = (String)this.sAtts.profile.get("oracle.net.kerberos5_mutual_authentication");
            if (object != "true") {
                bl = false;
            }
            object2.requestMutualAuth(bl);
            object2.requestConf(false);
            object2.requestInteg(false);
            if (this.z == null) {
                object2.requestCredDeleg(true);
            } else {
                object2.requestCredDeleg(false);
            }
            object = new byte[0];
            byte[] byArray2 = object2.initSecContext((byte[])object, 0, 0);
            object = byArray2;
            object3 = new byte[byArray2.length - 17];
            System.arraycopy(object, 17, object3, 0, ((Object)object3).length);
            object = InetAddress.getLocalHost();
            object = ((InetAddress)object).getAddress();
            int n2 = 39 + ((Object)object).length + 4 + ((Object)object3).length;
            this.sAtts.ano.a(n2, this.N, (short)0);
            this.h(4);
            this.K.a(2);
            this.K.a(4L);
            this.K.d((byte[])object);
            this.K.d((byte[])object3);
            this.K.flush();
            this.sAtts.ano.c();
            object = Service.a(this.K);
            this.K.g();
            if (bl) {
                if (object[1] < 2) {
                    throw new NetException(323, "Mutual authentication failed during Kerberos5 authentication");
                }
                object = this.K.n();
                byte[] byArray3 = new byte[byArray.length + 2 + ((Object)object).length];
                System.arraycopy(byArray, 0, byArray3, 0, byArray.length);
                byArray3[byArray.length] = 2;
                byArray3[byArray.length + 1] = 0;
                System.arraycopy(object, 0, byArray3, byArray.length + 2, ((Object)object).length);
                int n3 = byArray3.length;
                if (n3 < 128) {
                    byte[] byArray4 = new byte[1];
                    byArray = byArray4;
                    byArray4[0] = (byte)n3;
                } else if (n3 < 256) {
                    byte[] byArray5 = new byte[2];
                    byArray = byArray5;
                    byArray5[0] = -127;
                    byArray[1] = (byte)n3;
                } else if (n3 < 65536) {
                    byte[] byArray6 = new byte[3];
                    byArray = byArray6;
                    byArray6[0] = -126;
                    byArray[1] = (byte)(n3 >> 8);
                    byArray[2] = (byte)n3;
                } else if (n3 < 0x1000000) {
                    byte[] byArray7 = new byte[4];
                    byArray = byArray7;
                    byArray7[0] = -125;
                    byArray[1] = (byte)(n3 >> 16);
                    byArray[2] = (byte)(n3 >> 8);
                    byArray[3] = (byte)n3;
                } else {
                    byte[] byArray8 = new byte[5];
                    byArray = byArray8;
                    byArray8[0] = -124;
                    byArray[1] = n3 >> 24;
                    byArray[2] = (byte)(n3 >> 16);
                    byArray[3] = (byte)(n3 >> 8);
                    byArray[4] = (byte)n3;
                }
                Object object4 = byArray;
                byte[] byArray9 = new byte[1 + ((byte[])object4).length + byArray3.length];
                byArray = byArray9;
                byArray9[0] = 96;
                System.arraycopy(object4, 0, byArray, 1, ((byte[])object4).length);
                System.arraycopy(byArray3, 0, byArray, ((byte[])object4).length + 1, byArray3.length);
                try {
                    object2.initSecContext(byArray, 0, byArray.length);
                }
                catch (GSSException gSSException) {
                    NetException netException = new NetException(323, gSSException.getMessage());
                    object4 = netException;
                    netException.initCause(gSSException);
                    throw object4;
                }
                if (!object2.getMutualAuthState()) {
                    throw new NetException(323, "Mutual authentication failed during Kerberos5 authentication");
                }
            }
            if (!object2.isEstablished()) {
                throw new NetException(323, "Kerberos5 adaptor couldn't create context");
            }
            byte[] byArray10 = this.z == null ? (y ? this.a((GSSContext)object2, (byte[])object3) : null) : null;
            if (byArray10 == null) {
                byArray10 = new byte[]{};
            }
            n2 = 25 + byArray10.length;
            this.sAtts.ano.a(n2, this.N, (short)0);
            this.h(1);
            this.K.d(byArray10);
            this.K.flush();
        }
        catch (GSSException gSSException) {
            NetException netException = new NetException(323, gSSException.getMessage());
            netException.initCause(gSSException);
            throw netException;
        }
        return null;
    }

    private final byte[] a(GSSContext object, byte[] object2) {
        byte[] byArray = null;
        if (object.getCredDelegState() && this.u != null) {
            object = null;
            int n2 = -1;
            KerberosTicket kerberosTicket = this.J();
            if (kerberosTicket != null) {
                object = kerberosTicket.getSessionKey().getEncoded();
                n2 = kerberosTicket.getSessionKeyType();
            }
            object2 = new APReq((byte[])object2);
            object = new EncryptionKey(n2, (byte[])object);
            byte[] byArray2 = object2.authenticator.decrypt((EncryptionKey)object, 11);
            object2 = AuthenticationService.a(object2.authenticator, byArray2, true);
            Authenticator authenticator = new Authenticator((byte[])object2);
            object2 = authenticator;
            Checksum checksum = authenticator.getChecksum();
            object2 = checksum;
            object2 = checksum.getBytes();
            if (((byte[])object2).length >= 26) {
                int n3 = ((object2[27] & 0xFF) << 8) + (object2[26] & 0xFF);
                byArray2 = new byte[n3];
                System.arraycopy(object2, 28, byArray2, 0, n3);
                object2 = new KRBCred(byArray2);
                try {
                    byArray = object2.encPart.decrypt(EncryptionKey.NULL_KEY, 14);
                }
                catch (Exception exception) {
                    byArray = object2.encPart.decrypt((EncryptionKey)object, 14);
                }
                byArray = AuthenticationService.a(object2.encPart, byArray, true);
                object = new EncryptedData((EncryptionKey)object, byArray, 14);
                object = new KRBCred(object2.tickets, (EncryptedData)object);
                byArray = ((KRBCred)object).asn1Encode();
            }
        }
        return byArray;
    }

    private static byte[] a(EncryptedData encryptedData, Object ... objectArray) {
        byte[] byArray = null;
        if (r == null) {
            r = AuthenticationService.v();
        }
        try {
            byArray = r.getParameterTypes().length == 1 ? (byte[])r.invoke(encryptedData, objectArray[0]) : (byte[])r.invoke(encryptedData, objectArray);
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {}
        return byArray;
    }

    private static Method v() {
        Method method = null;
        try {
            Class<?> clazz = Class.forName("sun.security.krb5.EncryptedData");
            Class[] classArray = new Class[]{byte[].class, Boolean.TYPE};
            try {
                method = clazz.getDeclaredMethod("reset", classArray);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                method = clazz.getDeclaredMethod("reset", classArray[0]);
            }
        }
        catch (ClassNotFoundException classNotFoundException) {
        }
        catch (NoSuchMethodException noSuchMethodException) {}
        return method;
    }

    @Override
    final void x() {
    }

    public static final byte[] obfuscatePasswordForRadius(byte[] byArray) {
        return b.i(byArray);
    }

    static {
        try {
            Class.forName("javax.security.auth.kerberos.KerberosCredMessage");
        }
        catch (Exception exception) {}
        try {
            Class.forName("sun.security.krb5.internal.APReq");
            y = true;
        }
        catch (Exception exception) {
            y = false;
        }
    }
}

