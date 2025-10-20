/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.net.InetAddress;
import java.net.UnknownHostException;

@FunctionalInterface
public interface OracleHostnameResolver {
    public InetAddress[] getAllByName(String var1) throws UnknownHostException;
}

