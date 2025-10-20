/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.security.BasicPermission;
import oracle.jdbc.logging.annotations.DisableTrace;

@DisableTrace
public final class OracleSQLPermission
extends BasicPermission {
    private static final String[] allowedTargets = new String[]{"callAbort", "clientInfo\\..*"};

    private final void checkTarget(String string) {
        for (int i2 = 0; i2 < allowedTargets.length; ++i2) {
            if (!string.matches(allowedTargets[i2])) continue;
            return;
        }
        throw new IllegalArgumentException(string);
    }

    public OracleSQLPermission(String string) {
        super(string);
        this.checkTarget(string);
    }

    public OracleSQLPermission(String string, String string2) {
        super(string, string2);
        this.checkTarget(string);
    }
}

