/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.io.InputStream;
import java.sql.SQLException;
import oracle.jdbc.LargeObjectAccessMode;

public interface OracleBfile {
    public long length() throws SQLException;

    public byte[] getBytes(long var1, int var3) throws SQLException;

    public int getBytes(long var1, int var3, byte[] var4) throws SQLException;

    public InputStream getBinaryStream() throws SQLException;

    public InputStream getBinaryStream(long var1) throws SQLException;

    public long position(byte[] var1, long var2) throws SQLException;

    public long position(OracleBfile var1, long var2) throws SQLException;

    public String getName() throws SQLException;

    public String getDirAlias() throws SQLException;

    public void openFile() throws SQLException;

    public boolean isFileOpen() throws SQLException;

    public boolean fileExists() throws SQLException;

    public void closeFile() throws SQLException;

    public void open(LargeObjectAccessMode var1) throws SQLException;

    public void close() throws SQLException;

    public boolean isOpen() throws SQLException;
}

