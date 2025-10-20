/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.resource.ResourceException
 *  javax.resource.spi.EISSystemException
 *  javax.resource.spi.IllegalStateException
 *  javax.resource.spi.LocalTransaction
 *  javax.resource.spi.LocalTransactionException
 */
package oracle.jdbc.connector;

import java.sql.Connection;
import java.sql.SQLException;
import javax.resource.ResourceException;
import javax.resource.spi.EISSystemException;
import javax.resource.spi.IllegalStateException;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.LocalTransactionException;
import oracle.jdbc.connector.OracleManagedConnection;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONN_MANAGEMENT})
public class OracleLocalTransaction
implements LocalTransaction {
    private OracleManagedConnection managedConnection = null;
    private Connection connection = null;
    boolean isBeginCalled = false;
    private static final String RAERR_LTXN_COMMIT = "commit without begin";
    private static final String RAERR_LTXN_ROLLBACK = "rollback without begin";

    OracleLocalTransaction(OracleManagedConnection oracleManagedConnection) throws ResourceException {
        this.managedConnection = oracleManagedConnection;
        this.connection = oracleManagedConnection.getPhysicalConnection();
        this.isBeginCalled = false;
    }

    public void begin() throws ResourceException {
        try {
            if (((OracleConnection)this.connection).getTxnMode() == 1) {
                throw new IllegalStateException("Could not start a new transaction inside an active transaction");
            }
            if (this.connection.getAutoCommit()) {
                this.connection.setAutoCommit(false);
            }
            this.isBeginCalled = true;
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
        this.managedConnection.eventOccurred(2);
    }

    public void commit() throws ResourceException {
        if (!this.isBeginCalled) {
            throw new LocalTransactionException("begin() must be called before commit()", RAERR_LTXN_COMMIT);
        }
        try {
            this.connection.commit();
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
        this.isBeginCalled = false;
        this.managedConnection.eventOccurred(3);
    }

    public void rollback() throws ResourceException {
        if (!this.isBeginCalled) {
            throw new LocalTransactionException("begin() must be called before rollback()", RAERR_LTXN_ROLLBACK);
        }
        try {
            this.connection.rollback();
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
        this.isBeginCalled = false;
        this.managedConnection.eventOccurred(4);
    }
}

