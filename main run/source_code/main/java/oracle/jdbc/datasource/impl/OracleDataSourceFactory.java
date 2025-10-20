/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.datasource.impl;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;
import oracle.jdbc.datasource.impl.OracleConnectionPoolDataSource;
import oracle.jdbc.datasource.impl.OracleDataSource;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.Blind;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.PropertiesBlinder;
import oracle.jdbc.logging.annotations.Supports;
import oracle.jdbc.pool.OracleOCIConnectionPool;
import oracle.jdbc.xa.client.OracleXADataSource;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONN_POOL})
public class OracleDataSourceFactory
implements ObjectFactory {
    private static final String CONNECTION_PROPERTIES = "connectionProperties";
    private static final String ORACLE_CONN_DATA_POOL_SOURCE = "oracle.jdbc.datasource.impl.OracleConnectionPoolDataSource";
    private static final String ORACLE_CONN_DATA_POOL_SOURCE_OLD = "oracle.jdbc.pool.OracleConnectionPoolDataSource";
    private static final String ORACLE_OCI_CONN_POOL = "oracle.jdbc.pool.OracleOCIConnectionPool";
    private static final String ORACLE_DATA_SOURCE = "oracle.jdbc.datasource.impl.OracleDataSource";
    private static final String ORACLE_DATA_SOURCE_OLD = "oracle.jdbc.pool.OracleDataSource";
    private static final String ORACLE_XA_DATA_SOURCE = "oracle.jdbc.xa.client.OracleXADataSource";

    public Object getObjectInstance(Object object, Name name, Context context, Hashtable hashtable) throws Exception {
        String string;
        Object object2;
        Reference reference = (Reference)object;
        OracleDataSource oracleDataSource = null;
        String string2 = reference.getClassName();
        Properties properties = new Properties();
        if (string2.equals(ORACLE_DATA_SOURCE) || string2.equals(ORACLE_DATA_SOURCE_OLD) || string2.equals(ORACLE_XA_DATA_SOURCE)) {
            oracleDataSource = string2.equals(ORACLE_DATA_SOURCE) ? new OracleDataSource() : (string2.equals(ORACLE_DATA_SOURCE_OLD) ? new oracle.jdbc.pool.OracleDataSource() : new OracleXADataSource());
            object2 = null;
            object2 = (StringRefAddr)reference.get(CONNECTION_PROPERTIES);
            if (object2 != null) {
                string = (String)((StringRefAddr)object2).getContent();
                Properties properties2 = this.extractConnectionProperties(string);
                oracleDataSource.setConnectionProperties(properties2);
            }
        } else if (string2.equals(ORACLE_CONN_DATA_POOL_SOURCE)) {
            oracleDataSource = new OracleConnectionPoolDataSource();
        } else if (string2.equals(ORACLE_CONN_DATA_POOL_SOURCE_OLD)) {
            oracleDataSource = new oracle.jdbc.pool.OracleConnectionPoolDataSource();
        } else if (string2.equals(ORACLE_OCI_CONN_POOL)) {
            oracleDataSource = new OracleOCIConnectionPool();
            object2 = null;
            string = null;
            String string3 = null;
            String string4 = null;
            String string5 = null;
            String string6 = null;
            String string7 = null;
            StringRefAddr stringRefAddr = null;
            Object var17_18 = null;
            String string8 = null;
            stringRefAddr = (StringRefAddr)reference.get("connpool_min_limit");
            if (stringRefAddr != null) {
                object2 = (String)stringRefAddr.getContent();
            }
            if ((stringRefAddr = (StringRefAddr)reference.get("connpool_max_limit")) != null) {
                string = (String)stringRefAddr.getContent();
            }
            if ((stringRefAddr = (StringRefAddr)reference.get("connpool_increment")) != null) {
                string3 = (String)stringRefAddr.getContent();
            }
            if ((stringRefAddr = (StringRefAddr)reference.get("connpool_active_size")) != null) {
                string4 = (String)stringRefAddr.getContent();
            }
            if ((stringRefAddr = (StringRefAddr)reference.get("connpool_pool_size")) != null) {
                string5 = (String)stringRefAddr.getContent();
            }
            if ((stringRefAddr = (StringRefAddr)reference.get("connpool_timeout")) != null) {
                string6 = (String)stringRefAddr.getContent();
            }
            if ((stringRefAddr = (StringRefAddr)reference.get("connpool_nowait")) != null) {
                string7 = (String)stringRefAddr.getContent();
            }
            if ((stringRefAddr = (StringRefAddr)reference.get("transactions_distributed")) != null) {
                string8 = (String)stringRefAddr.getContent();
            }
            properties.put("connpool_min_limit", object2);
            properties.put("connpool_max_limit", string);
            properties.put("connpool_increment", string3);
            properties.put("connpool_active_size", string4);
            properties.put("connpool_pool_size", string5);
            properties.put("connpool_timeout", string6);
            if (string7 == "true") {
                properties.put("connpool_nowait", string7);
            }
            if (string8 == "true") {
                properties.put("transactions_distributed", string8);
            }
        } else {
            return null;
        }
        if (oracleDataSource != null) {
            object2 = null;
            object2 = (StringRefAddr)reference.get("url");
            if (object2 != null) {
                oracleDataSource.setURL((String)((StringRefAddr)object2).getContent());
            }
            if ((object2 = (StringRefAddr)reference.get("userName")) != null || (object2 = (StringRefAddr)reference.get("u")) != null || (object2 = (StringRefAddr)reference.get("user")) != null) {
                oracleDataSource.setUser((String)((StringRefAddr)object2).getContent());
            }
            if ((object2 = (StringRefAddr)reference.get("passWord")) != null || (object2 = (StringRefAddr)reference.get("password")) != null) {
                oracleDataSource.setPassword((String)((StringRefAddr)object2).getContent());
            }
            if ((object2 = (StringRefAddr)reference.get("description")) != null || (object2 = (StringRefAddr)reference.get("describe")) != null) {
                oracleDataSource.setDescription((String)((StringRefAddr)object2).getContent());
            }
            if ((object2 = (StringRefAddr)reference.get("driverType")) != null || (object2 = (StringRefAddr)reference.get("driver")) != null) {
                oracleDataSource.setDriverType((String)((StringRefAddr)object2).getContent());
            }
            if ((object2 = (StringRefAddr)reference.get("serverName")) != null || (object2 = (StringRefAddr)reference.get("host")) != null) {
                oracleDataSource.setServerName((String)((StringRefAddr)object2).getContent());
            }
            if ((object2 = (StringRefAddr)reference.get("databaseName")) != null || (object2 = (StringRefAddr)reference.get("sid")) != null) {
                oracleDataSource.setDatabaseName((String)((StringRefAddr)object2).getContent());
            }
            if ((object2 = (StringRefAddr)reference.get("serviceName")) != null) {
                oracleDataSource.setServiceName((String)((StringRefAddr)object2).getContent());
            }
            if ((object2 = (StringRefAddr)reference.get("networkProtocol")) != null || (object2 = (StringRefAddr)reference.get("protocol")) != null) {
                oracleDataSource.setNetworkProtocol((String)((StringRefAddr)object2).getContent());
            }
            if ((object2 = (StringRefAddr)reference.get("portNumber")) != null || (object2 = (StringRefAddr)reference.get("port")) != null) {
                string = (String)((StringRefAddr)object2).getContent();
                oracleDataSource.setPortNumber(Integer.parseInt(string));
            }
            if ((object2 = (StringRefAddr)reference.get("tnsentryname")) != null || (object2 = (StringRefAddr)reference.get("tns")) != null) {
                oracleDataSource.setTNSEntryName((String)((StringRefAddr)object2).getContent());
            } else if (string2.equals(ORACLE_OCI_CONN_POOL)) {
                string = null;
                object2 = (StringRefAddr)reference.get("connpool_is_poolcreated");
                if (object2 != null) {
                    string = (String)((StringRefAddr)object2).getContent();
                }
                if ("true".equals(string)) {
                    ((OracleOCIConnectionPool)oracleDataSource).setPoolConfig(properties);
                }
            }
        }
        return oracleDataSource;
    }

    @Blind(value=PropertiesBlinder.class)
    private Properties extractConnectionProperties(String string) throws SQLException {
        String[] stringArray;
        Properties properties = new Properties();
        string = string.substring(1, string.length() - 1);
        for (String string2 : stringArray = string.split(";")) {
            int n2 = string2.length();
            int n3 = string2.indexOf("=");
            if (n2 == 0 || n3 <= 0) {
                throw (SQLException)DatabaseError.createSqlException(190).fillInStackTrace();
            }
            String string3 = string2.substring(0, n3);
            String string4 = string2.substring(n3 + 1, n2);
            properties.setProperty(string3.trim(), string4.trim());
        }
        return properties;
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }
}

