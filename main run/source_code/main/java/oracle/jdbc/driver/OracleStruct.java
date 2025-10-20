/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Hashtable;
import java.util.Map;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OracleTypeMetaData;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CustomDatumFactory;
import oracle.sql.Datum;
import oracle.sql.DatumWithConnection;
import oracle.sql.ORADataFactory;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.OBJECT_DATUM})
@Deprecated
public class OracleStruct
extends DatumWithConnection
implements oracle.jdbc.internal.OracleStruct {
    StructDescriptor descriptor;
    Datum[] datumArray;
    Object[] objectArray;
    boolean enableLocalCache = false;
    long imageOffset;
    long imageLength;
    Object acProxy;

    public OracleStruct(StructDescriptor structDescriptor, Connection connection, Object[] objectArray) throws SQLException {
        OracleStruct.assertNotNull(structDescriptor);
        this.descriptor = structDescriptor;
        OracleStruct.assertNotNull(connection);
        if (!structDescriptor.getInternalConnection().isDescriptorSharable(((OracleConnection)connection).physicalConnectionWithin())) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Cannot construct STRUCT instance, invalid connection").fillInStackTrace();
        }
        structDescriptor.setConnection(connection);
        if (!this.descriptor.isInstantiable()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Cannot construct STRUCT instance for a non-instantiable object type").fillInStackTrace();
        }
        this.setPhysicalConnectionOf(connection);
        this.datumArray = objectArray != null ? this.descriptor.toArray(objectArray) : new Datum[this.descriptor.getLength()];
    }

    public OracleStruct(StructDescriptor structDescriptor, Connection connection, Map<?, ?> map) throws SQLException {
        OracleStruct.assertNotNull(structDescriptor);
        this.descriptor = structDescriptor;
        OracleStruct.assertNotNull(connection);
        if (!structDescriptor.getInternalConnection().isDescriptorSharable(((OracleConnection)connection).physicalConnectionWithin())) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Cannot construct STRUCT instance, invalid connection").fillInStackTrace();
        }
        structDescriptor.setConnection(connection);
        if (!this.descriptor.isInstantiable()) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Cannot construct STRUCT instance for a non-instantiable object type").fillInStackTrace();
        }
        this.setPhysicalConnectionOf(connection);
        this.datumArray = this.descriptor.toOracleArray(map);
    }

    public OracleStruct(StructDescriptor structDescriptor, byte[] byArray, Connection connection) throws SQLException {
        super(byArray);
        OracleStruct.assertNotNull(structDescriptor);
        this.descriptor = structDescriptor;
        OracleStruct.assertNotNull(connection);
        if (!structDescriptor.getInternalConnection().isDescriptorSharable(((OracleConnection)connection).physicalConnectionWithin())) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Cannot construct STRUCT instance, invalid connection").fillInStackTrace();
        }
        structDescriptor.setConnection(connection);
        this.setPhysicalConnectionOf(connection);
        this.datumArray = null;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            String string = this.descriptor.getName();
            return string;
        }
    }

    @Override
    public Object[] getAttributes() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            Object[] objectArray;
            Object[] objectArray2 = objectArray = this.getAttributes(this.getMap());
            return objectArray2;
        }
    }

    @Override
    public Object[] getAttributes(Map<String, Class<?>> map) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            Object[] objectArray = this.descriptor.toArray(this, this, map, this.enableLocalCache);
            return objectArray;
        }
    }

    @Override
    public OracleTypeMetaData getOracleMetaData() throws SQLException {
        return this.getDescriptor();
    }

    @Override
    public StructDescriptor getDescriptor() throws SQLException {
        return this.descriptor;
    }

    @Override
    public void setDescriptor(StructDescriptor structDescriptor) {
        this.descriptor = structDescriptor;
    }

    @Override
    public Datum[] getOracleAttributes() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            Datum[] datumArray = this.descriptor.toOracleArray(this, this, this.enableLocalCache);
            return datumArray;
        }
    }

    @Override
    public Map<String, Class<?>> getMap() {
        Map<String, Class<?>> map = null;
        try {
            map = this.getInternalConnection().getTypeMap();
        }
        catch (SQLException sQLException) {
        }
        return map;
    }

    @Override
    public byte[] toBytes() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            byte[] byArray = this.descriptor.toBytes(this, this, this.enableLocalCache);
            return byArray;
        }
    }

    @Override
    public void setDatumArray(Datum[] datumArray) {
        try {
            this.datumArray = datumArray == null ? new Datum[this.descriptor.getLength()] : datumArray;
        }
        catch (SQLException sQLException) {
        }
    }

    @Override
    public Datum[] getDatumArray() {
        return this.datumArray;
    }

    @Override
    public void setNullDatumArray() {
        this.datumArray = null;
    }

    @Override
    public Object[] getObjectArray() {
        return this.objectArray;
    }

    @Override
    public void setNullObjectArray() {
        this.objectArray = null;
    }

    @Override
    public void setObjArray(Object[] objectArray) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            this.objectArray = objectArray == null ? new Object[]{} : objectArray;
        }
    }

    @Override
    public Object toJdbc() throws SQLException {
        Map<String, Class<?>> map = this.getMap();
        return this.toJdbc(map);
    }

    @Override
    public Object toJdbc(Map<String, Class<?>> map) throws SQLException {
        Class clazz;
        Object object = this;
        if (map != null && (clazz = this.descriptor.getClass(map)) != null) {
            object = this.toClass(clazz, map);
        }
        return object;
    }

    @Override
    public <T> T toClass(Class<T> clazz) throws SQLException {
        return (T)this.toClass(clazz, this.getMap());
    }

    @Override
    public Object toClass(Class<?> clazz, Map<String, Class<?>> map) throws SQLException {
        Object object;
        block8: {
            object = null;
            try {
                if (clazz == null || clazz == STRUCT.class || clazz == Struct.class || clazz == oracle.jdbc.OracleStruct.class || clazz == oracle.jdbc.internal.OracleStruct.class) {
                    object = this;
                    break block8;
                }
                Object obj = clazz.newInstance();
                if (obj instanceof SQLData) {
                    ((SQLData)obj).readSQL(this.descriptor.toJdbc2SQLInput(this, this, map), this.descriptor.getName());
                    object = obj;
                    break block8;
                }
                if (obj instanceof ORADataFactory) {
                    ORADataFactory oRADataFactory = (ORADataFactory)obj;
                    object = oRADataFactory.create(this, 2002);
                    break block8;
                }
                if (obj instanceof OracleDataFactory) {
                    OracleDataFactory oracleDataFactory = (OracleDataFactory)obj;
                    object = oracleDataFactory.create(this, 2002);
                    break block8;
                }
                if (obj instanceof CustomDatumFactory) {
                    CustomDatumFactory customDatumFactory = (CustomDatumFactory)obj;
                    object = customDatumFactory.create(this, 2002);
                    break block8;
                }
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 49, this.descriptor.getName()).fillInStackTrace();
            }
            catch (InstantiationException instantiationException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 49, "InstantiationException: " + instantiationException.getMessage()).fillInStackTrace();
            }
            catch (IllegalAccessException illegalAccessException) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 49, "IllegalAccessException: " + illegalAccessException.getMessage()).fillInStackTrace();
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
        return new Object[n2];
    }

    @Override
    public void setAutoBuffering(boolean bl) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            this.enableLocalCache = bl;
        }
    }

    @Override
    public boolean getAutoBuffering() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            boolean bl = this.enableLocalCache;
            return bl;
        }
    }

    @Override
    public void setImage(byte[] byArray, long l2, long l3) throws SQLException {
        this.setShareBytes(byArray);
        this.imageOffset = l2;
        this.imageLength = l3;
    }

    @Override
    public void setImageLength(long l2) throws SQLException {
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

    public CustomDatumFactory getFactory(Hashtable<?, ?> hashtable, String string) throws SQLException {
        String string2 = this.getSQLTypeName();
        Object obj = hashtable.get(string2);
        if (obj == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Unable to convert a \"" + string2 + "\" to a \"" + string + "\" or a subclass of \"" + string + "\"").fillInStackTrace();
        }
        return (CustomDatumFactory)obj;
    }

    @Override
    public ORADataFactory getORADataFactory(Hashtable<?, ?> hashtable, String string) throws SQLException {
        String string2 = this.getSQLTypeName();
        Object obj = hashtable.get(string2);
        if (obj == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Unable to convert a \"" + string2 + "\" to a \"" + string + "\" or a subclass of \"" + string + "\"").fillInStackTrace();
        }
        return (ORADataFactory)obj;
    }

    public OracleDataFactory getOracleDataFactory(Hashtable<?, ?> hashtable, String string) throws SQLException {
        String string2 = this.getSQLTypeName();
        Object obj = hashtable.get(string2);
        if (obj == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 1, "Unable to convert a \"" + string2 + "\" to a \"" + string + "\" or a subclass of \"" + string + "\"").fillInStackTrace();
        }
        return (OracleDataFactory)obj;
    }

    public String debugString() {
        StringWriter stringWriter = new StringWriter();
        String string = null;
        try {
            StructDescriptor structDescriptor = this.getDescriptor();
            stringWriter.write("name = " + structDescriptor.getName());
            int n2 = structDescriptor.getLength();
            stringWriter.write(" length = " + n2);
            Object[] objectArray = this.getAttributes();
            for (int i2 = 0; i2 < n2; ++i2) {
                stringWriter.write(" attribute[" + i2 + "] = " + objectArray[i2]);
            }
            string = stringWriter.toString();
        }
        catch (SQLException sQLException) {
            string = "StructDescriptor missing or bad";
        }
        return string;
    }

    @Override
    public boolean isInHierarchyOf(String string) throws SQLException {
        return this.descriptor.isInHierarchyOf(string);
    }

    @Override
    public Connection getJavaSqlConnection() throws SQLException {
        return super.getJavaSqlConnection();
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

