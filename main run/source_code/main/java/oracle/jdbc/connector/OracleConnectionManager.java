/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.resource.ResourceException
 *  javax.resource.spi.ConnectionManager
 *  javax.resource.spi.ConnectionRequestInfo
 *  javax.resource.spi.ManagedConnection
 *  javax.resource.spi.ManagedConnectionFactory
 */
package oracle.jdbc.connector;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONN_MANAGEMENT})
public class OracleConnectionManager
implements ConnectionManager {
    public Object allocateConnection(ManagedConnectionFactory managedConnectionFactory, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        ManagedConnection managedConnection = managedConnectionFactory.createManagedConnection(null, connectionRequestInfo);
        return managedConnection.getConnection(null, connectionRequestInfo);
    }
}

