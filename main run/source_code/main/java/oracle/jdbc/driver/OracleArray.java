/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OracleTypeMetaData;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.Datum;
import oracle.sql.DatumWithConnection;
import oracle.sql.ORADataFactory;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLLECTION_DATUM})
@Deprecated
public class OracleArray
extends DatumWithConnection
implements oracle.jdbc.internal.OracleArray {
    static final byte KOPUP_INLINE_COLL = 1;
    ArrayDescriptor descriptor;
    Object objArray;
    Map<?, ?> javaMap = null;
    Datum[] datumArray;
    byte[] locator;
    byte prefixFlag;
    byte[] prefixSegment;
    int numElems = -1;
    boolean enableBuffering = false;
    boolean enableIndexing = false;
    boolean isFreed = false;
    public static final int ACCESS_FORWARD = 1;
    public static final int ACCESS_REVERSE = 2;
    public static final int ACCESS_UNKNOWN = 3;
    int accessDirection = 3;
    long lastIndex;
    long lastOffset;
    long[] indexArray;
    long imageOffset;
    long imageLength;
    Object acProxy;

    public OracleArray(ArrayDescriptor arrayDescriptor, Connection connection, Object object) throws SQLException {
        OracleArray.assertNotNull(arrayDescriptor);
        this.descriptor = arrayDescriptor;
        OracleArray.assertNotNull(connection);
        if (!arrayDescriptor.getInternalConnection().isDescriptorSharable(((OracleConnection)connection).physicalConnectionWithin())) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Cannot construct ARRAY instance, invalid connection").fillInStackTrace();
        }
        arrayDescriptor.setConnection(connection);
        this.setPhysicalConnectionOf(connection);
        this.datumArray = object == null ? new Datum[0] : this.descriptor.toOracleArray(object, 1L, -1);
    }

    public OracleArray(ArrayDescriptor arrayDescriptor, byte[] byArray, Connection connection) throws SQLException {
        super(byArray);
        OracleArray.assertNotNull(arrayDescriptor);
        this.descriptor = arrayDescriptor;
        OracleArray.assertNotNull(connection);
        if (!arrayDescriptor.getInternalConnection().isDescriptorSharable(((OracleConnection)connection).physicalConnectionWithin())) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Cannot construct ARRAY instance, invalid connection").fillInStackTrace();
        }
        arrayDescriptor.setConnection(connection);
        this.setPhysicalConnectionOf(connection);
        this.datumArray = null;
        this.locator = null;
    }

    @Override
    public String getBaseTypeName() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            String string = this.descriptor.getBaseName();
            return string;
        }
    }

    @Override
    public int getBaseType() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            int n2 = this.descriptor.getBaseType();
            return n2;
        }
    }

    @Override
    public Object getArray() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            Object[] objectArray = this.descriptor.toJavaArray(this, this, 1L, -1, this.getMap(), this.enableBuffering);
            return objectArray;
        }
    }

    @Override
    public Map<?, ?> getJavaMap() throws SQLException {
        this.getArray();
        return this.javaMap;
    }

    @Override
    public Object getArray(Map<String, Class<?>> map) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            Object[] objectArray = this.descriptor.toJavaArray(this, this, 1L, -1, map, this.enableBuffering);
            return objectArray;
        }
    }

    @Override
    public Object getArray(long l2, int n2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            if (l2 < 1L || n2 < 0) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "Invalid arguments, 'index' should be >= 1 and 'count' >= 0. An exception is thrown.").fillInStackTrace();
            }
            Object[] objectArray = this.descriptor.toJavaArray(this, this, l2, n2, this.getMap(), false);
            return objectArray;
        }
    }

    @Override
    public Object getArray(long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            if (l2 < 1L || n2 < 0) {
                throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "Invalid arguments, 'index' should be >= 1 and 'count' >= 0. An exception is thrown.");
            }
            Object[] objectArray = this.descriptor.toJavaArray(this, this, l2, n2, map, false);
            return objectArray;
        }
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            ResultSet resultSet = this.getResultSet(this.getInternalConnection().getTypeMap());
            return resultSet;
        }
    }

    @Override
    public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            ResultSet resultSet = this.descriptor.toResultSet(this, this, 1L, -1, map, this.enableBuffering);
            return resultSet;
        }
    }

    @Override
    public ResultSet getResultSet(long l2, int n2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            ResultSet resultSet = this.getResultSet(l2, n2, this.getInternalConnection().getTypeMap());
            return resultSet;
        }
    }

    @Override
    public ResultSet getResultSet(long l2, int n2, Map<String, Class<?>> map) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            if (l2 < 1L || n2 < -1) {
                throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "getResultSet()");
            }
            ResultSet resultSet = this.descriptor.toResultSet(this, this, l2, n2, map, false);
            return resultSet;
        }
    }

    @Override
    public Datum[] getOracleArray() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            Datum[] datumArray = this.descriptor.toOracleArray(this, this, 1L, -1, this.enableBuffering);
            return datumArray;
        }
    }

    @Override
    public int length() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            int n2 = this.descriptor.toLength(this, this);
            return n2;
        }
    }

    @Override
    public Datum[] getOracleArray(long l2, int n2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            if (l2 < 1L || n2 < 0) {
                throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68, "getOracleArray()");
            }
            Datum[] datumArray = this.descriptor.toOracleArray(this, this, l2, n2, false);
            return datumArray;
        }
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            String string = null;
            if (this.descriptor == null) {
                throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 61, "ARRAY");
            }
            string = this.descriptor.getName();
            String string2 = string;
            return string2;
        }
    }

    @Override
    public Map<String, Class<?>> getMap() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        return this.getInternalConnection().getTypeMap();
    }

    @Override
    public OracleTypeMetaData getOracleMetaData() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        return this.getDescriptor();
    }

    @Override
    public ArrayDescriptor getDescriptor() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        return this.descriptor;
    }

    @Override
    public byte[] toBytes() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            byte[] byArray = this.descriptor.toBytes(this, this, this.enableBuffering);
            return byArray;
        }
    }

    @Override
    public void setDatumArray(Datum[] datumArray) {
        this.datumArray = datumArray;
    }

    @Override
    public void setObjArray(Object object) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            if (object == null) {
                throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Invalid argument, 'oarray' should not be null. An exception is thrown.");
            }
            this.objArray = object;
        }
    }

    @Override
    public void setJavaMap(Map<?, ?> map) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            this.javaMap = map;
        }
    }

    @Override
    public void setLocator(byte[] byArray) {
        if (byArray != null && byArray.length != 0) {
            this.locator = byArray;
        }
    }

    @Override
    public void setPrefixSegment(byte[] byArray) {
        if (byArray != null && byArray.length != 0) {
            this.prefixSegment = byArray;
        }
    }

    @Override
    public void setPrefixFlag(byte by) {
        this.prefixFlag = by;
    }

    @Override
    public byte[] getLocator() {
        return this.locator;
    }

    @Override
    public void setLength(int n2) {
        this.numElems = n2;
    }

    @Override
    public boolean hasDataSeg() {
        return this.locator == null;
    }

    @Override
    public boolean isInline() {
        return (this.prefixFlag & 1) == 1;
    }

    @Override
    public Object toJdbc() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        return null;
    }

    public Object toJdbc(Map<String, Class<?>> map) throws SQLException {
        Class clazz;
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        Object object = this;
        if (map != null && (clazz = this.descriptor.getClass(map)) != null) {
            object = this.toClass(clazz, map);
        }
        return object;
    }

    Object toClass(Class<?> clazz, Map<String, Class<?>> map) throws SQLException {
        Object object;
        block7: {
            if (this.isFreed) {
                throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
            }
            object = null;
            try {
                if (clazz == null || clazz == ARRAY.class || clazz == Array.class || clazz == oracle.jdbc.OracleArray.class || clazz == oracle.jdbc.internal.OracleArray.class) {
                    object = this;
                    break block7;
                }
                Object obj = clazz.newInstance();
                if (obj instanceof ORADataFactory) {
                    ORADataFactory oRADataFactory = (ORADataFactory)obj;
                    object = oRADataFactory.create(this, 2003);
                    break block7;
                }
                if (obj instanceof OracleDataFactory) {
                    OracleDataFactory oracleDataFactory = (OracleDataFactory)obj;
                    object = oracleDataFactory.create(this, 2003);
                    break block7;
                }
                throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 49, this.descriptor.getName());
            }
            catch (InstantiationException instantiationException) {
                throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 49, "InstantiationException: " + instantiationException.getMessage());
            }
            catch (IllegalAccessException illegalAccessException) {
                throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 49, "IllegalAccessException: " + illegalAccessException.getMessage());
            }
        }
        return object;
    }

    @Override
    public boolean isConvertibleTo(Class<?> clazz) {
        return false;
    }

    @Override
    public Object makeJdbcArray(int n2) {
        return new Object[n2][];
    }

    @Override
    public int[] getIntArray() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            int[] nArray = (int[])this.descriptor.toNumericArray(this, this, 1L, -1, 4, this.enableBuffering);
            return nArray;
        }
    }

    @Override
    public int[] getIntArray(long l2, int n2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            int[] nArray = (int[])this.descriptor.toNumericArray(this, this, l2, n2, 4, false);
            return nArray;
        }
    }

    @Override
    public double[] getDoubleArray() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            double[] dArray = (double[])this.descriptor.toNumericArray(this, this, 1L, -1, 5, this.enableBuffering);
            return dArray;
        }
    }

    @Override
    public double[] getDoubleArray(long l2, int n2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            double[] dArray = (double[])this.descriptor.toNumericArray(this, this, l2, n2, 5, false);
            return dArray;
        }
    }

    @Override
    public short[] getShortArray() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            short[] sArray = (short[])this.descriptor.toNumericArray(this, this, 1L, -1, 8, this.enableBuffering);
            return sArray;
        }
    }

    @Override
    public short[] getShortArray(long l2, int n2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            short[] sArray = (short[])this.descriptor.toNumericArray(this, this, l2, n2, 8, false);
            return sArray;
        }
    }

    @Override
    public long[] getLongArray() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            long[] lArray = (long[])this.descriptor.toNumericArray(this, this, 1L, -1, 7, this.enableBuffering);
            return lArray;
        }
    }

    @Override
    public long[] getLongArray(long l2, int n2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            long[] lArray = (long[])this.descriptor.toNumericArray(this, this, l2, n2, 7, false);
            return lArray;
        }
    }

    @Override
    public float[] getFloatArray() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            float[] fArray = (float[])this.descriptor.toNumericArray(this, this, 1L, -1, 6, this.enableBuffering);
            return fArray;
        }
    }

    @Override
    public float[] getFloatArray(long l2, int n2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            float[] fArray = (float[])this.descriptor.toNumericArray(this, this, l2, n2, 6, false);
            return fArray;
        }
    }

    @Override
    public void setAutoBuffering(boolean bl) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            this.enableBuffering = bl;
        }
    }

    @Override
    public boolean getAutoBuffering() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        return this.enableBuffering;
    }

    @Override
    public void setAutoIndexing(boolean bl, int n2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            this.enableIndexing = bl;
            this.accessDirection = n2;
        }
    }

    @Override
    public void setAutoIndexing(boolean bl) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            this.enableIndexing = bl;
            this.accessDirection = 3;
        }
    }

    @Override
    public boolean getAutoIndexing() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        return this.enableIndexing;
    }

    @Override
    public int getAccessDirection() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        return this.accessDirection;
    }

    @Override
    public void setLastIndexOffset(long l2, long l3) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        this.lastIndex = l2;
        this.lastOffset = l3;
    }

    @Override
    public void setIndexOffset(long l2, long l3) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        if (this.indexArray == null) {
            this.indexArray = new long[this.numElems];
        }
        this.indexArray[(int)l2 - 1] = l3;
    }

    @Override
    public long getLastIndex() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        return this.lastIndex;
    }

    @Override
    public long getLastOffset() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        return this.lastOffset;
    }

    @Override
    public long getOffset(long l2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        long l3 = -1L;
        if (this.indexArray != null) {
            l3 = this.indexArray[(int)l2 - 1];
        }
        return l3;
    }

    @Override
    public void setImage(byte[] byArray, long l2, long l3) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        this.setShareBytes(byArray);
        this.imageOffset = l2;
        this.imageLength = l3;
    }

    @Override
    public void setImageLength(long l2) throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        this.imageLength = l2;
    }

    @Override
    public long getImageOffset() {
        return this.imageOffset;
    }

    @Override
    public long getImageLength() {
        return this.imageLength;
    }

    @Override
    public String stringValue() throws SQLException {
        if (this.isFreed) {
            throw DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 291);
        }
        Datum[] datumArray = this.getOracleArray();
        String string = "[";
        for (int i2 = 0; i2 < datumArray.length; ++i2) {
            if (i2 != 0) {
                string = string + ", ";
            }
            string = datumArray[i2] == null ? string + "null" : string + datumArray[i2].stringValue();
        }
        string = string + "]";
        return string;
    }

    @Override
    public void free() throws SQLException {
        this.isFreed = true;
    }

    public boolean isFreed() {
        return this.isFreed;
    }

    @Override
    public int getNumElems() {
        return this.numElems;
    }

    @Override
    public Datum[] getDatumArray() {
        return this.datumArray;
    }

    @Override
    public Object getObjArray() {
        return this.objArray;
    }

    @Override
    public void setNullObjArray() {
        this.objArray = null;
    }

    @Override
    public void setACProxy(Object object) {
        this.acProxy = object;
    }

    @Override
    public Object getACProxy() {
        return this.acProxy;
    }
}

