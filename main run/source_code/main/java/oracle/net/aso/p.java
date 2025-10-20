/*
 * Decompiled with CFR 0.152.
 */
package oracle.net.aso;

interface p {
    public int getDigestLength();

    public void reset();

    public void update(byte[] var1, int var2, int var3);

    public int g(byte[] var1, int var2);

    default public String getProviderName() {
        return "JavaNet";
    }
}

