/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.driver.AbstractShardingConnection;
import oracle.jdbc.driver.AbstractShardingResultSet;
import oracle.jdbc.driver.AbstractShardingStatement;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.internal.OracleArray;
import oracle.jdbc.internal.OracleBfile;
import oracle.jdbc.internal.OracleBlob;
import oracle.jdbc.internal.OracleClob;
import oracle.jdbc.internal.OracleNClob;
import oracle.jdbc.internal.OracleRef;
import oracle.jdbc.internal.OracleStruct;
import oracle.jdbc.proxy.annotation.GetCreator;
import oracle.jdbc.proxy.annotation.GetDelegate;
import oracle.jdbc.proxy.annotation.Methods;
import oracle.jdbc.proxy.annotation.Pre;
import oracle.jdbc.proxy.annotation.ProxyFor;
import oracle.jdbc.proxy.annotation.ProxyResult;
import oracle.jdbc.proxy.annotation.ProxyResultPolicy;
import oracle.jdbc.proxy.annotation.SetDelegate;
import oracle.jdbc.proxy.annotation.Signature;
import oracle.sql.DatumWithConnection;

@ProxyFor(value={OracleBlob.class, OracleClob.class, OracleNClob.class, OracleBfile.class, OracleStruct.class, OracleRef.class, OracleArray.class})
@ProxyResult(value=ProxyResultPolicy.MANUAL)
public abstract class AbstractShardingLob {
    @GetCreator
    protected abstract Object getCreator();

    @GetDelegate
    protected abstract Object getDelegate();

    @SetDelegate
    protected abstract void setDelegate(Object var1);

    public void close() throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        Object object = this.getDelegate();
        if (object instanceof OracleBlob) {
            ((OracleBlob)object).close();
            oracleConnection = ((OracleBlob)object).getInternalConnection();
        } else if (object instanceof OracleClob) {
            ((OracleClob)object).close();
            oracleConnection = ((OracleClob)object).getInternalConnection();
        } else if (object instanceof OracleNClob) {
            ((OracleNClob)object).close();
            oracleConnection = ((OracleNClob)object).getInternalConnection();
        } else if (object instanceof OracleBfile) {
            ((OracleBfile)object).close();
            oracleConnection = ((OracleBfile)object).getInternalConnection();
        }
        if (this.isCreatedByConnectionObject()) {
            AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    public void free() throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        Object object = this.getDelegate();
        if (object instanceof OracleBlob) {
            ((OracleBlob)object).free();
            oracleConnection = ((OracleBlob)object).getInternalConnection();
        } else if (object instanceof OracleClob) {
            ((OracleClob)object).free();
            oracleConnection = ((OracleClob)object).getInternalConnection();
        } else if (object instanceof OracleNClob) {
            ((OracleNClob)object).free();
            oracleConnection = ((OracleNClob)object).getInternalConnection();
        } else if (object instanceof OracleArray) {
            ((OracleArray)object).free();
            oracleConnection = ((OracleArray)object).getInternalConnection();
        }
        if (this.isCreatedByConnectionObject()) {
            AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    public void freeTemporary() throws SQLException {
        oracle.jdbc.internal.OracleConnection oracleConnection = null;
        Object object = this.getDelegate();
        if (object instanceof OracleBlob) {
            ((OracleBlob)object).freeTemporary();
            oracleConnection = ((OracleBlob)object).getInternalConnection();
        } else if (object instanceof OracleClob) {
            ((OracleClob)object).freeTemporary();
            oracleConnection = ((OracleClob)object).getInternalConnection();
        } else if (object instanceof OracleNClob) {
            ((OracleNClob)object).freeTemporary();
            oracleConnection = ((OracleNClob)object).getInternalConnection();
        }
        if (this.isCreatedByConnectionObject()) {
            AbstractShardingConnection abstractShardingConnection = (AbstractShardingConnection)this.getCreator();
            abstractShardingConnection.closeDatabaseConnection(oracleConnection);
        }
    }

    public oracle.jdbc.internal.OracleConnection getInternalConnection() {
        Object object = this.getCreator();
        if (object instanceof AbstractShardingConnection) {
            return (oracle.jdbc.internal.OracleConnection)object;
        }
        AbstractShardingResultSet abstractShardingResultSet = (AbstractShardingResultSet)this.getCreator();
        AbstractShardingStatement abstractShardingStatement = (AbstractShardingStatement)abstractShardingResultSet.getCreator();
        return (oracle.jdbc.internal.OracleConnection)abstractShardingStatement.getCreator();
    }

    public oracle.jdbc.OracleConnection getOracleConnection() throws SQLException {
        return this.getInternalConnection();
    }

    public Connection getJavaSqlConnection() throws SQLException {
        return this.getOracleConnection();
    }

    public OracleConnection getConnection() throws SQLException {
        throw new RuntimeException((SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace());
    }

    public void setPhysicalConnectionOf(Connection connection) {
        DatumWithConnection datumWithConnection = (DatumWithConnection)this.getDelegate();
        datumWithConnection.setPhysicalConnectionOf(connection);
    }

    @Pre
    @Methods(signatures={@Signature(name="setACProxy", args={Object.class}), @Signature(name="getACProxy", args={})})
    protected void preUnsupportedResultSetMethods(Method method, Object object, Object ... objectArray) {
        throw new RuntimeException((SQLException)DatabaseError.createUnsupportedFeatureSqlException().fillInStackTrace());
    }

    boolean isCreatedByConnectionObject() {
        Object object = this.getCreator();
        return object instanceof AbstractShardingConnection;
    }
}

