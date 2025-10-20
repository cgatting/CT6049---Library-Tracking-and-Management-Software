/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.resource.ResourceException
 *  javax.resource.spi.ConnectionEvent
 *  javax.resource.spi.ConnectionEventListener
 *  javax.resource.spi.ConnectionRequestInfo
 *  javax.resource.spi.EISSystemException
 *  javax.resource.spi.IllegalStateException
 *  javax.resource.spi.LocalTransaction
 *  javax.resource.spi.ManagedConnection
 *  javax.resource.spi.ManagedConnectionMetaData
 *  javax.resource.spi.security.PasswordCredential
 */
package oracle.jdbc.connector;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.EISSystemException;
import javax.resource.spi.IllegalStateException;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import oracle.jdbc.connector.OracleLocalTransaction;
import oracle.jdbc.connector.OracleManagedConnectionMetaData;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.xa.OracleXAConnection;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONN_MANAGEMENT})
public class OracleManagedConnection
implements ManagedConnection {
    private OracleXAConnection xaConnection = null;
    private Hashtable connectionListeners = null;
    private Connection connection = null;
    private PrintWriter logWriter = null;
    private PasswordCredential passwordCredential = null;
    private OracleLocalTransaction localTxn = null;

    OracleManagedConnection(XAConnection xAConnection) {
        this.xaConnection = (OracleXAConnection)xAConnection;
        this.connectionListeners = new Hashtable(10);
    }

    public Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
            this.connection = this.xaConnection.getConnection();
            return this.connection;
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
    }

    public void destroy() throws ResourceException {
        try {
            if (this.xaConnection != null) {
                Connection connection = this.xaConnection.getPhysicalHandle();
                if (this.localTxn != null && this.localTxn.isBeginCalled || ((OracleConnection)connection).getTxnMode() == 1) {
                    throw new IllegalStateException("Could not close connection while transaction is active");
                }
            }
            if (this.connection != null) {
                this.connection.close();
            }
            if (this.xaConnection != null) {
                this.xaConnection.close();
            }
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
    }

    public void cleanup() throws ResourceException {
        try {
            if (this.connection != null) {
                if (this.localTxn != null && this.localTxn.isBeginCalled || ((OracleConnection)this.connection).getTxnMode() == 1) {
                    throw new IllegalStateException("Could not close connection while transaction is active");
                }
                this.connection.close();
            }
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
    }

    public void associateConnection(Object object) {
    }

    public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {
        this.connectionListeners.put(connectionEventListener, connectionEventListener);
    }

    public void removeConnectionEventListener(ConnectionEventListener connectionEventListener) {
        this.connectionListeners.remove(connectionEventListener);
    }

    public XAResource getXAResource() throws ResourceException {
        try {
            return this.xaConnection.getXAResource();
        }
        catch (SQLException sQLException) {
            throw new ResourceException((Throwable)sQLException);
        }
    }

    public LocalTransaction getLocalTransaction() throws ResourceException {
        if (this.localTxn == null) {
            this.localTxn = new OracleLocalTransaction(this);
        }
        return this.localTxn;
    }

    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return new OracleManagedConnectionMetaData(this);
    }

    public void setLogWriter(PrintWriter printWriter) throws ResourceException {
        this.logWriter = printWriter;
    }

    public PrintWriter getLogWriter() throws ResourceException {
        return this.logWriter;
    }

    Connection getPhysicalConnection() throws ResourceException {
        try {
            return this.xaConnection.getPhysicalHandle();
        }
        catch (Exception exception) {
            EISSystemException eISSystemException = new EISSystemException("Exception: " + exception.getMessage());
            eISSystemException.setLinkedException(exception);
            throw eISSystemException;
        }
    }

    void setPasswordCredential(PasswordCredential passwordCredential) {
        this.passwordCredential = passwordCredential;
    }

    PasswordCredential getPasswordCredential() {
        return this.passwordCredential;
    }

    void eventOccurred(int n2) throws ResourceException {
        Enumeration enumeration = this.connectionListeners.keys();
        block7: while (enumeration.hasMoreElements()) {
            ConnectionEventListener connectionEventListener = (ConnectionEventListener)enumeration.nextElement();
            ConnectionEvent connectionEvent = new ConnectionEvent((ManagedConnection)this, n2);
            switch (n2) {
                case 1: {
                    connectionEventListener.connectionClosed(connectionEvent);
                    continue block7;
                }
                case 2: {
                    connectionEventListener.localTransactionStarted(connectionEvent);
                    continue block7;
                }
                case 3: {
                    connectionEventListener.localTransactionCommitted(connectionEvent);
                    continue block7;
                }
                case 4: {
                    connectionEventListener.localTransactionRolledback(connectionEvent);
                    continue block7;
                }
                case 5: {
                    connectionEventListener.connectionErrorOccurred(connectionEvent);
                    continue block7;
                }
            }
            throw new IllegalArgumentException("Illegal eventType in eventOccurred(): " + n2);
        }
    }
}

