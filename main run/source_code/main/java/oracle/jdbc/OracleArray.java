/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.OracleTypeMetaData;

public interface OracleArray
extends Array {
    public OracleTypeMetaData getOracleMetaData() throws SQLException;

    public int length() throws SQLException;

    public String getSQLTypeName() throws SQLException;

    public Object toJdbc() throws SQLException;

    public int[] getIntArray() throws SQLException;

    public int[] getIntArray(long var1, int var3) throws SQLException;

    public double[] getDoubleArray() throws SQLException;

    public double[] getDoubleArray(long var1, int var3) throws SQLException;

    public short[] getShortArray() throws SQLException;

    public short[] getShortArray(long var1, int var3) throws SQLException;

    public long[] getLongArray() throws SQLException;

    public long[] getLongArray(long var1, int var3) throws SQLException;

    public float[] getFloatArray() throws SQLException;

    public float[] getFloatArray(long var1, int var3) throws SQLException;

    public Map<?, ?> getJavaMap() throws SQLException;
}

