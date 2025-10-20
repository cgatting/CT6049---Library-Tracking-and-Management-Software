/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.resource.ResourceException
 *  javax.resource.spi.ConnectionManager
 *  javax.resource.spi.ConnectionRequestInfo
 *  javax.resource.spi.EISSystemException
 *  javax.resource.spi.ManagedConnection
 *  javax.resource.spi.ManagedConnectionFactory
 *  javax.resource.spi.ResourceAdapterInternalException
 *  javax.resource.spi.SecurityException
 *  javax.resource.spi.security.PasswordCredential
 */
package oracle.jdbc.connector;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.EISSystemException;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.SecurityException;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import oracle.jdbc.connector.OracleConnectionRequestInfo;
import oracle.jdbc.connector.OracleManagedConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONN_MANAGEMENT})
public class OracleManagedConnectionFactory
implements ManagedConnectionFactory {
    private XADataSource xaDataSource = null;
    private String xaDataSourceName = null;
    private static final String RAERR_MCF_SET_XADS = "invalid xads";
    private static final String RAERR_MCF_GET_PCRED = "no password credential";

    public OracleManagedConnectionFactory() throws ResourceException {
    }

    public OracleManagedConnectionFactory(XADataSource xADataSource) throws ResourceException {
        this.xaDataSource = xADataSource;
        this.xaDataSourceName = "XADataSource";
    }

    public void setXADataSourceName(String string) {
        this.xaDataSourceName = string;
    }

    public String getXADataSourceName() {
        return this.xaDataSourceName;
    }

    public Object createConnectionFactory(ConnectionManager connectionManager) throws ResourceException {
        if (this.xaDataSource == null) {
            this.setupXADataSource();
        }
        return (DataSource)((Object)this.xaDataSource);
    }

    public Object createConnectionFactory() throws ResourceException {
        return this.createConnectionFactory(null);
    }

    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        try {
            if (this.xaDataSource == null) {
                this.setupXADataSource();
            }
            XAConnection xAConnection = null;
            PasswordCredential passwordCredential = this.getPasswordCredential(subject, connectionRequestInfo);
            xAConnection = passwordCredential == null ? this.xaDataSource.getXAConnection() : this.xaDataSource.getXAConnection(passwordCredential.getUserName(), new String(passwordCredential.getPassword()));
            OracleManagedConnection oracleManagedConnection = new OracleManagedConnection(xAConnection);
            oracleManagedConnection.setPasswordCredential(passwordCredential);
            oracleManagedConnection.setLogWriter(this.getLogWriter());
            return oracleManagedConnection;
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
    }

    public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        PasswordCredential passwordCredential = this.getPasswordCredential(subject, connectionRequestInfo);
        for (Object e2 : set) {
            OracleManagedConnection oracleManagedConnection;
            if (!(e2 instanceof OracleManagedConnection) || !(oracleManagedConnection = (OracleManagedConnection)e2).getPasswordCredential().equals((Object)passwordCredential)) continue;
            return oracleManagedConnection;
        }
        return null;
    }

    public void setLogWriter(PrintWriter printWriter) throws ResourceException {
        try {
            if (this.xaDataSource == null) {
                this.setupXADataSource();
            }
            this.xaDataSource.setLogWriter(printWriter);
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
    }

    public PrintWriter getLogWriter() throws ResourceException {
        try {
            if (this.xaDataSource == null) {
                this.setupXADataSource();
            }
            return this.xaDataSource.getLogWriter();
        }
        catch (SQLException sQLException) {
            EISSystemException eISSystemException = new EISSystemException("SQLException: " + sQLException.getMessage());
            eISSystemException.setLinkedException((Exception)sQLException);
            throw eISSystemException;
        }
    }

    private void setupXADataSource() throws ResourceException {
        try {
            Object object;
            InitialContext initialContext = null;
            try {
                object = System.getProperties();
                initialContext = new InitialContext((Hashtable<?, ?>)object);
            }
            catch (java.lang.SecurityException securityException) {
                // empty catch block
            }
            if (initialContext == null) {
                initialContext = new InitialContext();
            }
            if ((object = (XADataSource)initialContext.lookup(this.xaDataSourceName)) == null) {
                throw new ResourceAdapterInternalException("Invalid XADataSource object");
            }
            this.xaDataSource = object;
        }
        catch (NamingException namingException) {
            ResourceException resourceException = new ResourceException("NamingException: " + namingException.getMessage());
            resourceException.setLinkedException((Exception)namingException);
            throw resourceException;
        }
    }

    private PasswordCredential getPasswordCredential(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        if (subject != null) {
            Set<PasswordCredential> set = subject.getPrivateCredentials(PasswordCredential.class);
            for (PasswordCredential passwordCredential : set) {
                if (!passwordCredential.getManagedConnectionFactory().equals((Object)this)) continue;
                return passwordCredential;
            }
            throw new SecurityException("Can not find user/password information", RAERR_MCF_GET_PCRED);
        }
        if (connectionRequestInfo == null) {
            return null;
        }
        OracleConnectionRequestInfo oracleConnectionRequestInfo = (OracleConnectionRequestInfo)connectionRequestInfo;
        PasswordCredential passwordCredential = new PasswordCredential(oracleConnectionRequestInfo.getUser(), oracleConnectionRequestInfo.getPassword().toCharArray());
        passwordCredential.setManagedConnectionFactory((ManagedConnectionFactory)this);
        return passwordCredential;
    }
}

