/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.diagnostics;

@FunctionalInterface
public interface Releaser {
    public void release(int var1, Object var2);
}

