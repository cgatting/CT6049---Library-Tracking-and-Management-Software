/*
 * Decompiled with CFR 0.152.
 */
package oracle.security.o3logon;

import oracle.security.o3logon.b;

public final class O3LoginClientHelper {
    private b ct;
    private boolean cu;

    public O3LoginClientHelper(boolean bl, boolean bl2) {
        this.cu = bl;
        this.ct = new b(bl2);
    }

    public final byte[] getSessionKey(String object, String string, byte[] byArray) {
        object = this.ct.a((String)object, string, this.cu);
        return this.ct.f((byte[])object, byArray);
    }

    public final byte[] getEPasswd(byte[] byArray, byte[] byArray2) {
        return this.ct.g(byArray, byArray2);
    }
}

