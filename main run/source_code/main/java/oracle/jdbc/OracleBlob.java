/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.SQLXML;
import oracle.jdbc.LargeObjectAccessMode;

public interface OracleBlob
extends Blob {
    public void open(LargeObjectAccessMode var1) throws SQLException;

    public void close() throws SQLException;

    public boolean isOpen() throws SQLException;

    public int getBytes(long var1, int var3, byte[] var4) throws SQLException;

    public boolean isEmptyLob() throws SQLException;

    public boolean isSecureFile() throws SQLException;

    public InputStream getBinaryStream(long var1) throws SQLException;

    public boolean isTemporary() throws SQLException;

    public SQLXML toSQLXML() throws SQLException;

    public SQLXML toSQLXML(int var1) throws SQLException;
}

