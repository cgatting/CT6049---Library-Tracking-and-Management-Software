/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.resource.ResourceException
 *  javax.resource.spi.EISSystemException
 *  javax.resource.spi.ManagedConnectionMetaData
 */
package oracle.jdbc.connector;

import java.sql.SQLException;
import javax.resource.ResourceException;
import javax.resource.spi.EISSystemException;
import javax.resource.spi.ManagedConnectionMetaData;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDatabaseMetaData;
import oracle.jdbc.connector.OracleManagedConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONN_MANAGEMENT})
public class OracleManagedConnectionMetaData
implements ManagedConnectionMetaData {
    private OracleManagedConnection managedConnection = null;
    private OracleDatabaseMetaData databaseMetaData = null;

    OracleManagedConnectionMetaData(OracleManagedConnection oracleManagedConnection) throws ResourceException {
        try {
            this.managedConnection = oracleManagedConnection;
            OracleConnection oracleConnection = (OracleConnection)oracleManagedConnection.getPhysicalConnection();
            this.databaseMetaData = (OracleDatabaseMetaData)oracleConnection.getMetaData();
        }
        catch (Exception exception) {
            EISSystemException eISSystemException = new EISSystemException("Exception: " + exception.getMessage());
            eISSystemException.setLinkedException(exception);
            throw eISSystemException;
        }
    }

    public String getEISProductName() throws ResourceException {
        try {
            return this.databaseMetaData.getDatabaseProductName();
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
    }

    public String getEISProductVersion() throws ResourceException {
        try {
            return this.databaseMetaData.getDatabaseProductVersion();
        }
        catch (Exception exception) {
            EISSystemException eISSystemException = new EISSystemException("Exception: " + exception.getMessage());
            eISSystemException.setLinkedException(exception);
            throw eISSystemException;
        }
    }

    public int getMaxConnections() throws ResourceException {
        try {
            return this.databaseMetaData.getMaxConnections();
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
    }

    public String getUserName() throws ResourceException {
        try {
            return this.databaseMetaData.getUserName();
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
    }
}

