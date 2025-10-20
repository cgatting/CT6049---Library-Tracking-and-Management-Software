/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLData;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.OracleDataFactory;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypeMetaData;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.DatumWithConnection;
import oracle.sql.ORADataFactory;
import oracle.sql.REF;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.OBJECT_PROCESSING})
@Deprecated
public class OracleRef
extends DatumWithConnection
implements oracle.jdbc.internal.OracleRef,
Serializable,
Cloneable {
    static final boolean DEBUG = false;
    static final long serialVersionUID = 1328446996944583167L;
    String typename;
    transient StructDescriptor descriptor;
    Object acProxy;

    @Override
    public String getBaseTypeName() throws SQLException {
        if (this.typename == null) {
            if (this.descriptor != null) {
                this.typename = this.descriptor.getName();
            } else {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 52).fillInStackTrace();
            }
        }
        return this.typename;
    }

    public OracleRef(String string, Connection connection, byte[] byArray) throws SQLException {
        super(byArray);
        if (connection == null || string == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        this.typename = string;
        this.descriptor = null;
        this.setPhysicalConnectionOf(connection);
    }

    public OracleRef(StructDescriptor structDescriptor, Connection connection, byte[] byArray) throws SQLException {
        super(byArray);
        if (connection == null || structDescriptor == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 68).fillInStackTrace();
        }
        this.descriptor = structDescriptor;
        this.setPhysicalConnectionOf(connection);
    }

    @Override
    public Object getValue(Map<String, Class<?>> map) throws SQLException {
        STRUCT sTRUCT = this.getSTRUCT();
        Object object = sTRUCT != null ? sTRUCT.toJdbc((Map)map) : null;
        return object;
    }

    @Override
    public Object getValue() throws SQLException {
        STRUCT sTRUCT = this.getSTRUCT();
        Object object = sTRUCT != null ? sTRUCT.toJdbc() : null;
        return object;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public STRUCT getSTRUCT() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            STRUCT sTRUCT;
            block16: {
                sTRUCT = null;
                OraclePreparedStatement oraclePreparedStatement = (OraclePreparedStatement)this.getInternalConnection().prepareStatement("select deref(:1) from dual");
                oraclePreparedStatement.setRowPrefetch(1);
                oraclePreparedStatement.setRef(1, this);
                OracleResultSet oracleResultSet = (OracleResultSet)oraclePreparedStatement.executeQuery();
                try {
                    if (oracleResultSet.next()) {
                        sTRUCT = oracleResultSet.getSTRUCT(1);
                        break block16;
                    }
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 52).fillInStackTrace();
                }
                finally {
                    oracleResultSet.close();
                    oracleResultSet = null;
                    oraclePreparedStatement.close();
                    oraclePreparedStatement = null;
                }
            }
            STRUCT sTRUCT2 = sTRUCT;
            return sTRUCT2;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setValue(Object object) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.getInternalConnection().acquireCloseableLock();){
            STRUCT sTRUCT = STRUCT.toSTRUCT(object, this.getInternalConnection());
            if (sTRUCT.getInternalConnection() != this.getInternalConnection()) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 77, "Incompatible connection object").fillInStackTrace();
            }
            if (!this.getBaseTypeName().equals(sTRUCT.getSQLTypeName())) {
                throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 77, "Incompatible type").fillInStackTrace();
            }
            byte[] byArray = sTRUCT.toBytes();
            byte[] byArray2 = sTRUCT.getDescriptor().getOracleTypeADT().getTOID();
            CallableStatement callableStatement = null;
            try {
                callableStatement = this.getInternalConnection().prepareCall("begin :1 := sys.dbms_pickler.update_through_ref (:2, :3, :4, :5); end;");
                callableStatement.registerOutParameter(1, 2);
                callableStatement.setBytes(2, this.shareBytes());
                callableStatement.setInt(3, 0);
                callableStatement.setBytes(4, byArray2);
                callableStatement.setBytes(5, byArray);
                callableStatement.execute();
                int n2 = 0;
                n2 = callableStatement.getInt(1);
                if (n2 != 0) {
                    throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 77, "ORA-" + n2).fillInStackTrace();
                }
            }
            finally {
                if (callableStatement != null) {
                    callableStatement.close();
                }
                callableStatement = null;
            }
        }
    }

    @Override
    public OracleTypeMetaData getOracleMetaData() throws SQLException {
        return this.getDescriptor();
    }

    @Override
    public StructDescriptor getDescriptor() throws SQLException {
        if (this.descriptor == null) {
            this.descriptor = StructDescriptor.createDescriptor(this.typename, (Connection)this.getInternalConnection());
        }
        return this.descriptor;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        String string = this.getBaseTypeName();
        return string;
    }

    @Override
    public Object getObject(Map<String, Class<?>> map) throws SQLException {
        STRUCT sTRUCT = this.getSTRUCT();
        Object object = sTRUCT != null ? sTRUCT.toJdbc((Map)map) : null;
        return object;
    }

    @Override
    public Object getObject() throws SQLException {
        STRUCT sTRUCT = this.getSTRUCT();
        Object object = sTRUCT != null ? sTRUCT.toJdbc() : null;
        return object;
    }

    @Override
    public void setObject(Object object) throws SQLException {
        try (PreparedStatement preparedStatement = null;){
            preparedStatement = this.getInternalConnection().prepareStatement("call sys.utl_ref.update_object( :1, :2 )");
            preparedStatement.setRef(1, this);
            preparedStatement.setObject(2, object);
            preparedStatement.execute();
        }
    }

    @Override
    public Object toJdbc() throws SQLException {
        return this;
    }

    public Object toJdbc(Map<String, Class<?>> map) throws SQLException {
        Class clazz;
        Object object = this;
        if (map != null && (clazz = this.getDescriptor().getClass(map)) != null) {
            object = this.toClass(clazz, map);
        }
        return object;
    }

    public Object toClass(Class<?> clazz, Map<String, Class<?>> map) throws SQLException {
        Object object;
        block7: {
            object = null;
            try {
                if (clazz == null || clazz == REF.class || clazz == Ref.class || clazz == oracle.jdbc.OracleRef.class || clazz == oracle.jdbc.internal.OracleRef.class) {
                    object = this;
                    break block7;
                }
                Object obj = clazz.newInstance();
                if (obj instanceof SQLData) {
                    object = this;
                    break block7;
                }
                if (obj instanceof ORADataFactory) {
                    ORADataFactory oRADataFactory = (ORADataFactory)obj;
                    object = oRADataFactory.create(this, 2006);
                    break block7;
                }
                if (obj instanceof OracleDataFactory) {
                    OracleDataFactory oracleDataFactory = (OracleDataFactory)obj;
                    object = oracleDataFactory.create(this, 2006);
                    break block7;
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
        return new REF[n2];
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        REF rEF = null;
        try {
            rEF = new REF(this.getBaseTypeName(), (Connection)this.getInternalConnection(), this.getBytes());
        }
        catch (SQLException sQLException) {
            throw new CloneNotSupportedException(sQLException.getMessage());
        }
        return rEF;
    }

    @Override
    @DisableTrace
    public boolean equals(Object object) {
        boolean bl = false;
        try {
            bl = object instanceof OracleRef && super.equals(object) && this.getBaseTypeName().equals(((OracleRef)object).getSQLTypeName());
        }
        catch (Exception exception) {
        }
        return bl;
    }

    @Override
    @DisableTrace
    public int hashCode() {
        int n2;
        block4: {
            byte[] byArray;
            block5: {
                block3: {
                    byArray = this.shareBytes();
                    n2 = 0;
                    if ((byArray[2] & 5) != 5) break block3;
                    for (int i2 = 0; i2 < 4; ++i2) {
                        n2 *= 256;
                        n2 += byArray[8 + i2] & 0xFF;
                    }
                    break block4;
                }
                if ((byArray[2] & 3) != 3) break block5;
                for (int i3 = 0; i3 < 4 && i3 < byArray.length; ++i3) {
                    n2 *= 256;
                    n2 += byArray[6 + i3] & 0xFF;
                }
                break block4;
            }
            if ((byArray[2] & 2) != 2) break block4;
            for (int i4 = 0; i4 < 4; ++i4) {
                n2 *= 256;
                n2 += byArray[8 + i4] & 0xFF;
            }
        }
        return n2;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.shareBytes());
        try {
            objectOutputStream.writeUTF(this.getBaseTypeName());
        }
        catch (SQLException sQLException) {
            throw new IOException(sQLException.getMessage());
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.setBytes((byte[])objectInputStream.readObject());
        this.typename = objectInputStream.readUTF();
    }

    @Override
    public Connection getJavaSqlConnection() throws SQLException {
        return super.getJavaSqlConnection();
    }

    public void setTypeName(String string) {
        this.typename = string;
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

