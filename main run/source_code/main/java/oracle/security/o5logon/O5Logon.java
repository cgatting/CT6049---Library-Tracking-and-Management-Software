/*
 * Decompiled with CFR 0.152.
 */
package oracle.security.o5logon;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.internal.OracleConnection;
import oracle.net.aso.c;
import oracle.security.o3logon.O3LoginProtocolHelper;

public final class O5Logon {
    public static final int AUTH_FLAG_SHA2 = 1;
    private static final char[] cH = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private boolean cI;
    private boolean l = false;
    private static final SecureRandom cJ = O5Logon.getSecureRandom();
    private static final Monitor dk = Monitor.newInstance();
    private final MessageDigest cK = O5Logon.ad();
    private final MessageDigest cL = O5Logon.ae();
    private final MessageDigest cM = O5Logon.af();
    private byte[] cN = null;
    private String cO = null;
    private String cP = null;
    private String cQ = null;

    public O5Logon(OracleConnection oracleConnection, boolean bl, boolean bl2) {
        this.cI = bl;
        this.l = bl2;
    }

    private static final SecureRandom getSecureRandom() {
        SecureRandom secureRandom = null;
        try {
            secureRandom = new SecureRandom();
            byte[] byArray = new byte[32];
            secureRandom.nextBytes(byArray);
        }
        catch (Exception exception) {}
        return secureRandom;
    }

    private static final void o(byte[] object) {
        Monitor.CloseableLock closeableLock = dk.acquireCloseableLock();
        Throwable throwable = null;
        try {
            cJ.nextBytes((byte[])object);
            if (closeableLock != null) {
                closeableLock.close();
                return;
            }
        }
        catch (Throwable throwable2) {
            try {
                object = throwable2;
                throwable = throwable2;
                throw object;
            }
            catch (Throwable throwable3) {
                if (closeableLock != null) {
                    if (throwable != null) {
                        try {
                            closeableLock.close();
                        }
                        catch (Throwable throwable4) {
                            throwable.addSuppressed(throwable4);
                        }
                    } else {
                        closeableLock.close();
                    }
                }
                throw throwable3;
            }
        }
    }

    private static MessageDigest ad() {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        }
        catch (Exception exception) {}
        return messageDigest;
    }

    private static MessageDigest ae() {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA1");
        }
        catch (Exception exception) {}
        return messageDigest;
    }

    private static MessageDigest af() {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        }
        catch (Exception exception) {}
        return messageDigest;
    }

    public static boolean isOL7MRCapable() {
        try {
            O5Logon.ag();
            return true;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return false;
        }
    }

    private final byte[] a(int n2, byte[] object, int n3, byte[] byArray, int n4, int n5, byte[] byArray2) {
        Object object2;
        n3 = 0;
        if (this.cI) {
            switch (n2) {
                case 2361: 
                case 40674: 
                case 59694: {
                    n3 = 16;
                    break;
                }
                case 6949: 
                case 45394: {
                    n3 = 24;
                    break;
                }
                case 18453: {
                    n3 = 32;
                }
            }
            n4 = n5;
            byte[] byArray3 = new byte[2 * n3];
            System.arraycopy(byArray, 0, byArray3, 0, n3);
            System.arraycopy(object, 0, byArray3, n3, n3);
            object2 = new byte[byArray3.length << 1];
            this.j(byArray3, (byte[])object2);
            object = O5Logon.b(new String(byArray2, "US-ASCII"));
            object2 = new PBEKeySpec(new String((byte[])object2, "US-ASCII").toCharArray(), (byte[])object, n4, n3 << 3);
            object = O5Logon.ag();
            object2 = O5Logon.a((KeySpec)object2, (SecretKeyFactory)object);
        } else {
            switch (n2) {
                case 2361: 
                case 40674: 
                case 59694: {
                    byte[] byArray4 = new byte[16];
                    for (n2 = 0; n2 < 16; ++n2) {
                        byArray4[n2] = (byte)(object[n2 + 16] ^ byArray[n2 + 16]);
                    }
                    this.cK.reset();
                    object2 = this.cK.digest(byArray4);
                    break;
                }
                case 6949: 
                case 45394: {
                    byte[] byArray5 = new byte[24];
                    for (n2 = 0; n2 < 24; ++n2) {
                        byArray5[n2] = (byte)(object[n2 + 16] ^ byArray[n2 + 16]);
                    }
                    object2 = new byte[24];
                    this.cK.reset();
                    this.cK.update(byArray5, 0, 16);
                    byte[] byArray6 = this.cK.digest();
                    System.arraycopy(byArray6, 0, object2, 0, 16);
                    this.cK.reset();
                    this.cK.update(byArray5, 16, 8);
                    byArray6 = this.cK.digest();
                    System.arraycopy(byArray6, 0, object2, 16, 8);
                    break;
                }
                case 18453: {
                    byte[] byArray7 = new byte[32];
                    for (n2 = 0; n2 < 32; ++n2) {
                        byArray7[n2] = (byte)(object[n2 + 16] ^ byArray[n2 + 16]);
                    }
                    object2 = new byte[32];
                    this.cK.reset();
                    this.cK.update(byArray7, 0, 16);
                    byte[] byArray8 = this.cK.digest();
                    System.arraycopy(byArray8, 0, object2, 0, 16);
                    this.cK.reset();
                    this.cK.update(byArray7, 16, 16);
                    byArray8 = this.cK.digest();
                    System.arraycopy(byArray8, 0, object2, 16, 16);
                    break;
                }
                default: {
                    object2 = new byte[]{};
                }
            }
        }
        return object2;
    }

    public final byte[] getO5LogonKey() {
        return this.cN;
    }

    public final byte[] getDerivedKey(byte[] object, int n2) {
        String string = "PBKDF2WithHmacSHA512";
        int n3 = 512;
        byte[] byArray = this.cN;
        if ((n2 & 1) != 1) {
            string = "PBKDF2WithHmacSHA1";
            n3 = 160;
        }
        CharSequence charSequence = new StringBuffer(byArray.length << 1);
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            for (int i3 = 1; i3 >= 0; --i3) {
                ((StringBuffer)charSequence).append(cH[byArray[i2] >> (i3 << 2) & 0xF]);
            }
        }
        charSequence = ((StringBuffer)charSequence).toString();
        PBEKeySpec pBEKeySpec = new PBEKeySpec(((String)charSequence).toCharArray(), (byte[])object, 1000, n3);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(string);
        try {
            object = O5Logon.a(pBEKeySpec, secretKeyFactory);
            SecretKeySpec secretKeySpec = new SecretKeySpec((byte[])object, "AES");
            object = secretKeySpec;
            return (byte[])secretKeySpec.getEncoded().clone();
        }
        catch (Exception exception) {
            throw (InvalidKeySpecException)new InvalidKeySpecException().initCause(exception);
        }
    }

    public final boolean validateServerIdentity(String object) {
        boolean bl = false;
        try {
            String string = this.cP;
            String string2 = object;
            object = this;
            object = ((O5Logon)object).a(((O5Logon)object).cN, string2, string);
            object = new String((byte[])object, 16, ((Object)object).length - 16, "US-ASCII");
            bl = ((String)object).compareTo("SERVER_TO_CLIENT") == 0;
        }
        catch (Exception exception) {}
        return bl;
    }

    private final byte[] a(byte[] byArray, String object, String string) {
        int n2;
        if (byArray == null) {
            return new byte[0];
        }
        byte[] byArray2 = new byte[16];
        for (n2 = 0; n2 < 16; ++n2) {
            byArray2[0] = 0;
        }
        object = O5Logon.b((String)object);
        n2 = byArray.length == 16 ? 1 : (byArray.length == 24 ? 2 : 3);
        int n3 = string.endsWith("PKCS5Padding") ? 2 : 0;
        c c2 = new c(1, n2, n3, this.l, true);
        c2.b(byArray, byArray2);
        byte[] byArray3 = c2.f((byte[])object);
        return byArray3;
    }

    private final byte[] a(byte[] byArray, String string) {
        return this.a(this.cN, byArray, string);
    }

    private final byte[] a(byte[] byArray, byte[] byArray2, String string) {
        int n2;
        if (byArray == null) {
            return new byte[0];
        }
        byte[] byArray3 = new byte[16];
        for (n2 = 0; n2 < 16; ++n2) {
            byArray3[0] = 0;
        }
        n2 = byArray.length == 16 ? 1 : (byArray.length == 24 ? 2 : 3);
        int n3 = string.endsWith("PKCS5Padding") ? 2 : 0;
        c c2 = new c(1, n2, n3, this.l, true);
        c2.b(byArray, byArray3);
        byte[] byArray4 = c2.g(byArray2);
        return byArray4;
    }

    /*
     * WARNING - void declaration
     */
    public final void generateOAuthResponse(int n2, byte[] object, String object2, String object3, String object4, byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4, byte[] byArray5, byte[] byArray6, int[] nArray, int[] nArray2, boolean bl, byte by, byte[] byArray7, int n3, int n4, byte[] byArray8, int[] nArray3) {
        void var19_32;
        void var20_33;
        void var16_27;
        void var18_31;
        object4 = null;
        if (cJ == null || this.cL == null || this.cK == null && !this.cI || !O5Logon.isOL7MRCapable() && this.cI || this.cM == null) {
            throw new Exception("Resource A missing.");
        }
        if (nArray.length != 1) {
            throw new Exception("Resource B missing.");
        }
        if (n2 == 18453) {
            void var17_28;
            Object object5 = object;
            Object object6 = object3;
            void object62 = var17_28;
            object3 = this;
            this.cO = "AES/CBC/NoPadding";
            ((O5Logon)object3).cP = "AES/CBC/PKCS5Padding";
            ((O5Logon)object3).cQ = "AES/CBC/NoPadding";
            void var21_34 = object62;
            byte[] object7 = O5Logon.b(new String((byte[])object5, "US-ASCII"));
            object2 = "AUTH_PBKDF2_SPEEDY_KEY".getBytes("US-ASCII");
            object3 = new byte[object7.length + ((Object)object2).length];
            System.arraycopy(object7, 0, object3, 0, object7.length);
            System.arraycopy(object2, 0, object3, object7.length, ((Object)object2).length);
            object2 = new PBEKeySpec(((String)object6).toCharArray(), (byte[])object3, (int)var21_34, 512);
            object3 = O5Logon.ag();
            object4 = O5Logon.a((KeySpec)object2, (SecretKeyFactory)object3);
            object6 = object;
            Object n7 = object4;
            object3 = this;
            ((O5Logon)object3).cM.reset();
            ((O5Logon)object3).cM.update((byte[])n7);
            if (object6 != null) {
                ((O5Logon)object3).cM.update(O5Logon.b(new String((byte[])object6, "US-ASCII")));
            }
            byte[] byArray9 = ((O5Logon)object3).cM.digest();
            object5 = byArray9;
            object = byArray9;
        } else {
            void n6;
            object = this.a(n2, (String)object2, (String)object3, bl, (byte[])object, (byte)n6);
        }
        object2 = this.a((byte[])object, new String(byArray3, "US-ASCII"), this.cO);
        byte[] byArray10 = byArray3;
        byte[] byArray11 = byArray4;
        Object object7 = object;
        Object byArray12 = object2;
        object3 = this;
        object = new byte[((Object)byArray12).length];
        O5Logon.o(object);
        byte[] byArray13 = super.a((byte[])object7, (byte[])object, ((O5Logon)object3).cO);
        if (byArray11 == null || byArray11.length != byArray10.length) {
            throw new Exception("Resource D missing.");
        }
        super.j(byArray13, byArray11);
        this.cN = this.a(n2, (byte[])object2, 16, (byte[])object, 16, (int)var18_31, (byte[])var16_27);
        object = object4;
        void var21_36 = var20_33;
        byArray11 = var19_32;
        int n5 = n2;
        int n6 = 16;
        object3 = this;
        byte[] byArray14 = new byte[16];
        if (n5 == 18453) {
            O5Logon.o(byArray14);
            byte[] byArray132 = new byte[16 + ((byte[])object).length];
            System.arraycopy(byArray14, 0, byArray132, 0, 16);
            System.arraycopy(object, 0, byArray132, 16, ((byte[])object).length);
            object2 = super.a(byArray132, ((O5Logon)object3).cQ);
            var21_36[0] = super.j((byte[])object2, byArray11);
        }
        if (byArray2 != null) {
            this.a(byArray2, byArray6, 16, nArray2);
        }
        this.a(byArray, byArray5, 16, nArray);
    }

    public final void generateOAuthResponse(int n2, byte[] byArray, String string, String string2, byte[] byArray2, byte[] byArray3, byte[] byArray4, byte[] byArray5, int[] nArray, boolean bl, byte by, byte[] byArray6, int n3, int n4, byte[] byArray7, int[] nArray2) {
        this.generateOAuthResponse(n2, byArray, string, string2, null, byArray2, null, byArray3, byArray4, byArray5, null, nArray, null, bl, by, byArray6, n3, n4, byArray7, nArray2);
    }

    private byte[] a(int n2, String object, String string, boolean bl, byte[] object2, byte by) {
        byte[] byArray;
        if (n2 == 2361) {
            this.cO = "AES/CBC/NoPadding";
            this.cP = "AES/CBC/PKCS5Padding";
            O3LoginProtocolHelper o3LoginProtocolHelper = new O3LoginProtocolHelper(this.l);
            object2 = o3LoginProtocolHelper;
            object = o3LoginProtocolHelper.getVerifier((String)object, string, bl);
            byArray = new byte[16];
            System.arraycopy(object, 0, byArray, 0, 8);
            for (int i2 = 8; i2 < 16; ++i2) {
                byArray[i2] = 0;
            }
        } else if (n2 == 6949 || n2 == 45394) {
            this.cO = (by & 2) != 0 ? "AES/CBC/NoPadding" : "AES/CBC/PKCS5Padding";
            this.cP = "AES/CBC/PKCS5Padding";
            this.cL.reset();
            this.cL.update(string.getBytes("UTF-8"));
            if (n2 == 6949 && object2 != null) {
                this.cL.update(O5Logon.b(new String((byte[])object2, "US-ASCII")));
            }
            object2 = this.cL.digest();
            byArray = new byte[24];
            for (int i3 = 0; i3 < 24; ++i3) {
                byArray[i3] = 0;
            }
            System.arraycopy(object2, 0, byArray, 0, ((byte[])object2).length);
        } else if (n2 == 40674 || n2 == 59694) {
            this.cO = "AES/CBC/NoPadding";
            this.cP = "AES/CBC/PKCS5Padding";
            this.cK.reset();
            this.cK.update(string.getBytes("UTF-8"));
            if (n2 == 59694) {
                this.cK.update(O5Logon.b(new String((byte[])object2, "US-ASCII")));
            }
            byArray = this.cK.digest();
        } else {
            throw new Exception("Resource C missing.");
        }
        return byArray;
    }

    private void a(byte[] byArray, byte[] byArray2, int n2, int[] nArray) {
        if (byArray2 == null) {
            throw new Exception("Resource E missing.");
        }
        byte[] byArray3 = new byte[16];
        O5Logon.o(byArray3);
        byte[] byArray4 = new byte[16 + byArray.length];
        System.arraycopy(byArray3, 0, byArray4, 0, 16);
        System.arraycopy(byArray, 0, byArray4, 16, byArray.length);
        byArray = this.a(byArray4, this.cP);
        nArray[0] = this.j(byArray, byArray2);
    }

    private static byte[] a(KeySpec object, SecretKeyFactory object2) {
        if (object2 == null) {
            throw new Exception("Resource Z missing.");
        }
        try {
            object = ((SecretKeyFactory)object2).generateSecret((KeySpec)object);
            object2 = (byte[])object.getEncoded().clone();
            if (object != null) {
                // empty if block
            }
        }
        catch (Exception exception) {
            throw new Exception("Resource Y missing.");
        }
        return object2;
    }

    private static byte nibbleToHex(byte by) {
        return (byte)((by = (byte)(by & 0xF)) < 10 ? by + 48 : by - 10 + 65);
    }

    private final int j(byte[] byArray, byte[] byArray2) {
        int n2;
        for (n2 = 0; n2 < byArray.length; ++n2) {
            byArray2[n2 << 1] = O5Logon.nibbleToHex((byte)((byArray[n2] & 0xF0) >> 4));
            byArray2[(n2 << 1) + 1] = O5Logon.nibbleToHex((byte)(byArray[n2] & 0xF));
        }
        return n2 << 1;
    }

    private static byte[] b(String string) {
        byte[] byArray = new byte[string.length() / 2];
        for (int i2 = 0; i2 < string.length() / 2; ++i2) {
            int n2 = Byte.parseByte(string.substring(2 * i2, 2 * i2 + 1), 16);
            byte by = Byte.parseByte(string.substring(2 * i2 + 1, 2 * i2 + 2), 16);
            n2 = by | n2 << 4;
            byArray[i2] = (byte)n2;
        }
        return byArray;
    }

    private static final SecretKeyFactory ag() {
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            NoSuchAlgorithmException noSuchAlgorithmException2 = noSuchAlgorithmException;
            noSuchAlgorithmException2 = noSuchAlgorithmException;
            try {
                return SecretKeyFactory.getInstance("PBKDF2WithSHA512");
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException3) {
                throw noSuchAlgorithmException2;
            }
        }
    }
}

