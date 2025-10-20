/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.internal.OracleConnection;

public interface OracleCloseCallback {
    public void beforeClose(OracleConnection var1, Object var2);

    public void afterClose(Object var1);
}

