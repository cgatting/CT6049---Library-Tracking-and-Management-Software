/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.Clob;
import java.sql.SQLException;
import java.sql.SQLXML;
import oracle.jdbc.LargeObjectAccessMode;

public interface OracleClob
extends Clob {
    public void open(LargeObjectAccessMode var1) throws SQLException;

    public void close() throws SQLException;

    public boolean isOpen() throws SQLException;

    public boolean isTemporary() throws SQLException;

    public boolean isEmptyLob() throws SQLException;

    public boolean isSecureFile() throws SQLException;

    public SQLXML toSQLXML() throws SQLException;

    public SQLXML toSQLXML(String var1) throws SQLException;
}

