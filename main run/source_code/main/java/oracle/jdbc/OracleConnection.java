/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import oracle.jdbc.LogicalTransactionId;
import oracle.jdbc.LogicalTransactionIdEventListener;
import oracle.jdbc.OracleOCIFailover;
import oracle.jdbc.OracleSavepoint;
import oracle.jdbc.OracleShardingKey;
import oracle.jdbc.aq.AQDequeueOptions;
import oracle.jdbc.aq.AQEnqueueOptions;
import oracle.jdbc.aq.AQMessage;
import oracle.jdbc.aq.AQNotificationRegistration;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.diagnostics.SecuredLogger;
import oracle.jdbc.pool.OracleConnectionCacheCallback;
import oracle.sql.ARRAY;
import oracle.sql.BINARY_DOUBLE;
import oracle.sql.BINARY_FLOAT;
import oracle.sql.DATE;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TypeDescriptor;

public interface OracleConnection
extends Connection {
    public static final byte ACCESSMODE_JAVAPROP = 1;
    public static final byte ACCESSMODE_SYSTEMPROP = 2;
    public static final byte ACCESSMODE_BOTH = 3;
    public static final byte ACCESSMODE_FILEPROP = 4;
    public static final String CONNECTION_PROPERTY_RETAIN_V9_BIND_BEHAVIOR = "oracle.jdbc.RetainV9LongBindBehavior";
    public static final String CONNECTION_PROPERTY_RETAIN_V9_BIND_BEHAVIOR_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_RETAIN_V9_BIND_BEHAVIOR_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_USER_NAME = "user";
    public static final String CONNECTION_PROPERTY_USER_NAME_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_USER_NAME_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_NEW_PASSWORD = "oracle.jdbc.newPassword";
    public static final String CONNECTION_PROPERTY_NEW_PASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_NEW_PASSWORD_ACCESSMODE = 1;
    public static final String CONNECTION_PROPERTY_DATABASE = "database";
    public static final String CONNECTION_PROPERTY_DATABASE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_DATABASE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_AUTOCOMMIT = "autoCommit";
    public static final String CONNECTION_PROPERTY_AUTOCOMMIT_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_AUTOCOMMIT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_PROTOCOL = "protocol";
    public static final String CONNECTION_PROPERTY_PROTOCOL_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_PROTOCOL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_STREAM_CHUNK_SIZE = "oracle.jdbc.StreamChunkSize";
    public static final String CONNECTION_PROPERTY_STREAM_CHUNK_SIZE_DEFAULT = "32767";
    public static final byte CONNECTION_PROPERTY_STREAM_CHUNK_SIZE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_SET_FLOAT_AND_DOUBLE_USE_BINARY = "SetFloatAndDoubleUseBinary";
    public static final String CONNECTION_PROPERTY_SET_FLOAT_AND_DOUBLE_USE_BINARY_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_SET_FLOAT_AND_DOUBLE_USE_BINARY_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_TERMINAL = "v$session.terminal";
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_TERMINAL_DEFAULT = "unknown";
    public static final byte CONNECTION_PROPERTY_THIN_VSESSION_TERMINAL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_MACHINE = "v$session.machine";
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_MACHINE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_VSESSION_MACHINE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_OSUSER = "v$session.osuser";
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_OSUSER_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_VSESSION_OSUSER_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_PROGRAM = "v$session.program";
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_PROGRAM_DEFAULT = "JDBC Thin Client";
    public static final byte CONNECTION_PROPERTY_THIN_VSESSION_PROGRAM_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_PROCESS = "v$session.process";
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_PROCESS_DEFAULT = "1234";
    public static final byte CONNECTION_PROPERTY_THIN_VSESSION_PROCESS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_INAME = "v$session.iname";
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_INAME_DEFAULT = "jdbc_ttc_impl";
    public static final byte CONNECTION_PROPERTY_THIN_VSESSION_INAME_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_ENAME = "v$session.ename";
    public static final String CONNECTION_PROPERTY_THIN_VSESSION_ENAME_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_VSESSION_ENAME_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_PROFILE = "oracle.net.profile";
    public static final String CONNECTION_PROPERTY_THIN_NET_PROFILE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_PROFILE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_SERVICES = "oracle.net.authentication_services";
    public static final String CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_SERVICES_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_SERVICES_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB5_MUTUAL = "oracle.net.kerberos5_mutual_authentication";
    public static final String CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB5_MUTUAL_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB5_MUTUAL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB5_CC_NAME = "oracle.net.kerberos5_cc_name";
    public static final String CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB5_CC_NAME_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB5_CC_NAME_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB_REALM = "oracle.net.KerberosRealm";
    public static final String CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB_REALM_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB_REALM_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB_JAAS_LOGIN_MODULE = "oracle.net.KerberosJaasLoginModule";
    public static final String CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB_JAAS_LOGIN_MODULE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB_JAAS_LOGIN_MODULE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_SET_FIPS_MODE = "oracle.net.setFIPSMode";
    public static final String CONNECTION_PROPERTY_THIN_NET_SET_FIPS_MODE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_SET_FIPS_MODE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_ALLOW_WEAK_CRYPTO = "oracle.net.allow_weak_crypto";
    public static final String CONNECTION_PROPERTY_THIN_NET_ALLOW_WEAK_CRYPTO_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_THIN_NET_ALLOW_WEAK_CRYPTO_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_ENCRYPTION_LEVEL = "oracle.net.encryption_client";
    public static final String CONNECTION_PROPERTY_THIN_NET_ENCRYPTION_LEVEL_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_ENCRYPTION_LEVEL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_ENCRYPTION_TYPES = "oracle.net.encryption_types_client";
    public static final String CONNECTION_PROPERTY_THIN_NET_ENCRYPTION_TYPES_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_ENCRYPTION_TYPES_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_CHECKSUM_LEVEL = "oracle.net.crypto_checksum_client";
    public static final String CONNECTION_PROPERTY_THIN_NET_CHECKSUM_LEVEL_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_CHECKSUM_LEVEL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_CHECKSUM_TYPES = "oracle.net.crypto_checksum_types_client";
    public static final String CONNECTION_PROPERTY_THIN_NET_CHECKSUM_TYPES_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_CHECKSUM_TYPES_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_CRYPTO_SEED = "oracle.net.crypto_seed";
    public static final String CONNECTION_PROPERTY_THIN_NET_CRYPTO_SEED_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_CRYPTO_SEED_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_USE_JCE_API = "oracle.net.useJCEAPI";
    public static final String CONNECTION_PROPERTY_THIN_USE_JCE_API_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_THIN_USE_JCE_API_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_TCP_NO_DELAY = "oracle.jdbc.TcpNoDelay";
    public static final String CONNECTION_PROPERTY_THIN_TCP_NO_DELAY_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_THIN_TCP_NO_DELAY_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_READ_TIMEOUT = "oracle.jdbc.ReadTimeout";
    public static final String CONNECTION_PROPERTY_THIN_READ_TIMEOUT_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_READ_TIMEOUT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_OUTBOUND_CONNECT_TIMEOUT = "oracle.net.OUTBOUND_CONNECT_TIMEOUT";
    public static final String CONNECTION_PROPERTY_THIN_OUTBOUND_CONNECT_TIMEOUT_DEFAULT = "0";
    public static final byte CONNECTION_PROPERTY_THIN_OUTBOUND_CONNECT_TIMEOUT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_CONNECT_TIMEOUT = "oracle.net.CONNECT_TIMEOUT";
    public static final String CONNECTION_PROPERTY_THIN_NET_CONNECT_TIMEOUT_DEFAULT = "0";
    public static final byte CONNECTION_PROPERTY_THIN_NET_CONNECT_TIMEOUT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_DISABLE_OUT_OF_BAND_BREAK = "oracle.net.disableOob";
    public static final String CONNECTION_PROPERTY_THIN_NET_DISABLE_OUT_OF_BAND_BREAK_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_THIN_NET_DISABLE_OUT_OF_BAND_BREAK_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_USE_ZERO_COPY_IO = "oracle.net.useZeroCopyIO";
    public static final String CONNECTION_PROPERTY_THIN_NET_USE_ZERO_COPY_IO_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_THIN_NET_USE_ZERO_COPY_IO_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_USE_1900_AS_YEAR_FOR_TIME = "oracle.jdbc.use1900AsYearForTime";
    public static final String CONNECTION_PROPERTY_USE_1900_AS_YEAR_FOR_TIME_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_USE_1900_AS_YEAR_FOR_TIME_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_TIMESTAMPTZ_IN_GMT = "oracle.jdbc.timestampTzInGmt";
    public static final String CONNECTION_PROPERTY_TIMESTAMPTZ_IN_GMT_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_TIMESTAMPTZ_IN_GMT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_TIMEZONE_AS_REGION = "oracle.jdbc.timezoneAsRegion";
    public static final String CONNECTION_PROPERTY_TIMEZONE_AS_REGION_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_TIMEZONE_AS_REGION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_SSL_CERTIFICATE_ALIAS = "oracle.net.ssl_certificate_alias";
    public static final String CONNECTION_PROPERTY_THIN_SSL_CERTIFICATE_ALIAS_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_SSL_CERTIFICATE_ALIAS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_SSL_SERVER_DN_MATCH = "oracle.net.ssl_server_dn_match";
    public static final String CONNECTION_PROPERTY_THIN_SSL_SERVER_DN_MATCH_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_SSL_SERVER_DN_MATCH_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_SSL_SERVER_CERT_DN = "oracle.net.ssl_server_cert_dn";
    public static final String CONNECTION_PROPERTY_THIN_SSL_SERVER_CERT_DN_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_SSL_SERVER_CERT_DN_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_SSL_VERSION = "oracle.net.ssl_version";
    public static final String CONNECTION_PROPERTY_THIN_SSL_VERSION_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_SSL_VERSION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_SSL_CIPHER_SUITES = "oracle.net.ssl_cipher_suites";
    public static final String CONNECTION_PROPERTY_THIN_SSL_CIPHER_SUITES_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_SSL_CIPHER_SUITES_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTORE = "javax.net.ssl.keyStore";
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTORE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTORE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTORETYPE = "javax.net.ssl.keyStoreType";
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTORETYPE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTORETYPE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTOREPASSWORD = "javax.net.ssl.keyStorePassword";
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTOREPASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTOREPASSWORD_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTORE = "javax.net.ssl.trustStore";
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTORE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTORE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTORETYPE = "javax.net.ssl.trustStoreType";
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTORETYPE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTORETYPE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTOREPASSWORD = "javax.net.ssl.trustStorePassword";
    public static final String CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTOREPASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTOREPASSWORD_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_SSL_KEYMANAGERFACTORY_ALGORITHM = "ssl.keyManagerFactory.algorithm";
    public static final String CONNECTION_PROPERTY_THIN_SSL_KEYMANAGERFACTORY_ALGORITHM_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_SSL_KEYMANAGERFACTORY_ALGORITHM_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_SSL_TRUSTMANAGERFACTORY_ALGORITHM = "ssl.trustManagerFactory.algorithm";
    public static final String CONNECTION_PROPERTY_THIN_SSL_TRUSTMANAGERFACTORY_ALGORITHM_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_SSL_TRUSTMANAGERFACTORY_ALGORITHM_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_OLDSYNTAX = "oracle.net.oldSyntax";
    public static final String CONNECTION_PROPERTY_THIN_NET_OLDSYNTAX_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_OLDSYNTAX_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_JNDI_LDAP_CONNECT_TIMEOUT = "com.sun.jndi.ldap.connect.timeout";
    public static final String CONNECTION_PROPERTY_THIN_JNDI_LDAP_CONNECT_TIMEOUT_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_JNDI_LDAP_CONNECT_TIMEOUT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_JNDI_LDAP_READ_TIMEOUT = "com.sun.jndi.ldap.read.timeout";
    public static final String CONNECTION_PROPERTY_THIN_JNDI_LDAP_READ_TIMEOUT_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_JNDI_LDAP_READ_TIMEOUT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_WALLET_LOCATION = "oracle.net.wallet_location";
    public static final String CONNECTION_PROPERTY_WALLET_LOCATION_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_WALLET_LOCATION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_WALLET_PASSWORD = "oracle.net.wallet_password";
    public static final String CONNECTION_PROPERTY_WALLET_PASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_WALLET_PASSWORD_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_CIPHER_SUITES = "oracle.net.ldap.ssl.supportedCiphers";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_CIPHER_SUITES_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_CIPHER_SUITES_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_VERSIONS = "oracle.net.ldap.ssl.supportedVersions";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_VERSIONS_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_VERSIONS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYSTORE = "oracle.net.ldap.ssl.keyStore";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYSTORE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYSTORE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYSTORE_TYPE = "oracle.net.ldap.ssl.keyStoreType";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYSTORE_TYPE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYSTORE_TYPE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYSTORE_PASSWORD = "oracle.net.ldap.ssl.keyStorePassword";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYSTORE_PASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYSTORE_PASSWORD_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYMANAGER_FACTORY_ALGORITHM = "oracle.net.ldap.ssl.keyManagerFactory.algorithm";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYMANAGER_FACTORY_ALGORITHM_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_KEYMANAGER_FACTORY_ALGORITHM_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTSTORE = "oracle.net.ldap.ssl.trustStore";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTSTORE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTSTORE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTSTORE_TYPE = "oracle.net.ldap.ssl.trustStoreType";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTSTORE_TYPE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTSTORE_TYPE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTSTORE_PASSWORD = "oracle.net.ldap.ssl.trustStorePassword";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTSTORE_PASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTSTORE_PASSWORD_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTMANAGER_FACTORY_ALGORITHM = "oracle.net.ldap.ssl.trustManagerFactory.algorithm";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTMANAGER_FACTORY_ALGORITHM_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_TRUSTMANAGER_FACTORY_ALGORITHM_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_WALLET_LOCATION = "oracle.net.ldap.ssl.walletLocation";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_WALLET_LOCATION_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_WALLET_LOCATION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_WALLET_PASSWORD = "oracle.net.ldap.ssl.walletPassword";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_WALLET_PASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_WALLET_PASSWORD_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SECURITY_AUTHENTICATION = "oracle.net.ldap.security.authentication";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SECURITY_AUTHENTICATION_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SECURITY_AUTHENTICATION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SECURITY_PRINCIPAL = "oracle.net.ldap.security.principal";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SECURITY_PRINCIPAL_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SECURITY_PRINCIPAL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SECURITY_CREDENTIALS = "oracle.net.ldap.security.credentials";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SECURITY_CREDENTIALS_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SECURITY_CREDENTIALS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_CONTEXT_PROTOCOL = "oracle.net.ldap.ssl.ssl_context_protocol";
    public static final String CONNECTION_PROPERTY_THIN_LDAP_SSL_CONTEXT_PROTOCOL_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_LDAP_SSL_CONTEXT_PROTOCOL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_PROXY_CLIENT_NAME = "oracle.jdbc.proxyClientName";
    public static final String CONNECTION_PROPERTY_PROXY_CLIENT_NAME_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_PROXY_CLIENT_NAME_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DEFAULT_USE_NIO = "oracle.jdbc.useNio";
    public static final String CONNECTION_PROPERTY_DEFAULT_USE_NIO_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_DEFAULT_USE_NIO_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_HTTPS_PROXY_HOST = "oracle.net.httpsProxyHost";
    public static final String CONNECTION_PROPERTY_THIN_HTTPS_PROXY_HOST_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_HTTPS_PROXY_HOST_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_HTTPS_PROXY_PORT = "oracle.net.httpsProxyPort";
    public static final String CONNECTION_PROPERTY_THIN_HTTPS_PROXY_PORT_DEFAULT = "0";
    public static final byte CONNECTION_PROPERTY_THIN_HTTPS_PROXY_PORT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_THIN_NET_CONNECTIONID_PREFIX = "oracle.net.connectionIdPrefix";
    public static final String CONNECTION_PROPERTY_THIN_NET_CONNECTIONID_PREFIX_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_THIN_NET_CONNECTIONID_PREFIX_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_OCI_DRIVER_CHARSET = "JDBCDriverCharSetId";
    public static final String CONNECTION_PROPERTY_OCI_DRIVER_CHARSET_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_OCI_DRIVER_CHARSET_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_EDITION_NAME = "oracle.jdbc.editionName";
    public static final String CONNECTION_PROPERTY_EDITION_NAME_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_EDITION_NAME_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_INTERNAL_LOGON = "internal_logon";
    public static final String CONNECTION_PROPERTY_INTERNAL_LOGON_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_INTERNAL_LOGON_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_CREATE_DESCRIPTOR_USE_CURRENT_SCHEMA_FOR_SCHEMA_NAME = "oracle.jdbc.createDescriptorUseCurrentSchemaForSchemaName";
    public static final String CONNECTION_PROPERTY_CREATE_DESCRIPTOR_USE_CURRENT_SCHEMA_FOR_SCHEMA_NAME_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_CREATE_DESCRIPTOR_USE_CURRENT_SCHEMA_FOR_SCHEMA_NAME_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_OCI_SVC_CTX_HANDLE = "OCISvcCtxHandle";
    public static final String CONNECTION_PROPERTY_OCI_SVC_CTX_HANDLE_DEFAULT = "0";
    public static final byte CONNECTION_PROPERTY_OCI_SVC_CTX_HANDLE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_OCI_ENV_HANDLE = "OCIEnvHandle";
    public static final String CONNECTION_PROPERTY_OCI_ENV_HANDLE_DEFAULT = "0";
    public static final byte CONNECTION_PROPERTY_OCI_ENV_HANDLE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_OCI_ERR_HANDLE = "OCIErrHandle";
    public static final String CONNECTION_PROPERTY_OCI_ERR_HANDLE_DEFAULT = "0";
    public static final byte CONNECTION_PROPERTY_OCI_ERR_HANDLE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_PRELIM_AUTH = "prelim_auth";
    public static final String CONNECTION_PROPERTY_PRELIM_AUTH_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_PRELIM_AUTH_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_SET_NEW_PASSWORD = "OCINewPassword";
    public static final String CONNECTION_PROPERTY_SET_NEW_PASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_SET_NEW_PASSWORD_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DEFAULT_EXECUTE_BATCH = "defaultExecuteBatch";
    public static final String CONNECTION_PROPERTY_DEFAULT_EXECUTE_BATCH_DEFAULT = "1";
    public static final byte CONNECTION_PROPERTY_DEFAULT_EXECUTE_BATCH_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH = "defaultRowPrefetch";
    public static final String CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH_DEFAULT = "10";
    public static final byte CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE = "oracle.jdbc.defaultLobPrefetchSize";
    public static final String CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE_DEFAULT = "32768";
    public static final byte CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ENABLE_DATA_IN_LOCATOR = "oracle.jdbc.enableDataInLocator";
    public static final String CONNECTION_PROPERTY_ENABLE_DATA_IN_LOCATOR_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_ENABLE_DATA_IN_LOCATOR_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ENABLE_READ_DATA_IN_LOCATOR = "oracle.jdbc.enableReadDataInLocator";
    public static final String CONNECTION_PROPERTY_ENABLE_READ_DATA_IN_LOCATOR_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_ENABLE_READ_DATA_IN_LOCATOR_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_REPORT_REMARKS = "remarksReporting";
    public static final String CONNECTION_PROPERTY_REPORT_REMARKS_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_REPORT_REMARKS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_INCLUDE_SYNONYMS = "includeSynonyms";
    public static final String CONNECTION_PROPERTY_INCLUDE_SYNONYMS_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_INCLUDE_SYNONYMS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_RESTRICT_GETTABLES = "restrictGetTables";
    public static final String CONNECTION_PROPERTY_RESTRICT_GETTABLES_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_RESTRICT_GETTABLES_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ACCUMULATE_BATCH_RESULT = "AccumulateBatchResult";
    public static final String CONNECTION_PROPERTY_ACCUMULATE_BATCH_RESULT_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_ACCUMULATE_BATCH_RESULT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_USE_FETCH_SIZE_WITH_LONG_COLUMN = "useFetchSizeWithLongColumn";
    public static final String CONNECTION_PROPERTY_USE_FETCH_SIZE_WITH_LONG_COLUMN_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_USE_FETCH_SIZE_WITH_LONG_COLUMN_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_PROCESS_ESCAPES = "processEscapes";
    public static final String CONNECTION_PROPERTY_PROCESS_ESCAPES_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_PROCESS_ESCAPES_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_FIXED_STRING = "fixedString";
    public static final String CONNECTION_PROPERTY_FIXED_STRING_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_FIXED_STRING_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DEFAULTNCHAR = "defaultNChar";
    public static final String CONNECTION_PROPERTY_DEFAULTNCHAR_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_DEFAULTNCHAR_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_RESOURCE_MANAGER_ID = "RessourceManagerId";
    public static final String CONNECTION_PROPERTY_RESOURCE_MANAGER_ID_DEFAULT = "0000";
    public static final byte CONNECTION_PROPERTY_RESOURCE_MANAGER_ID_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DISABLE_DEFINECOLUMNTYPE = "disableDefineColumnType";
    public static final String CONNECTION_PROPERTY_DISABLE_DEFINECOLUMNTYPE_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_DISABLE_DEFINECOLUMNTYPE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_CONVERT_NCHAR_LITERALS = "oracle.jdbc.convertNcharLiterals";
    public static final String CONNECTION_PROPERTY_CONVERT_NCHAR_LITERALS_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_CONVERT_NCHAR_LITERALS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_AUTO_COMMIT_SPEC_COMPLIANT = "oracle.jdbc.autoCommitSpecCompliant";
    public static final String CONNECTION_PROPERTY_AUTO_COMMIT_SPEC_COMPLIANT_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_AUTO_COMMIT_SPEC_COMPLIANT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_JDBC_STANDARD_BEHAVIOR = "oracle.jdbc.JDBCStandardBehavior";
    public static final String CONNECTION_PROPERTY_JDBC_STANDARD_BEHAVIOR_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_JDBC_STANDARD_BEHAVIOR_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_J2EE13_COMPLIANT = "oracle.jdbc.J2EE13Compliant";
    public static final String CONNECTION_PROPERTY_J2EE13_COMPLIANT_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_J2EE13_COMPLIANT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DMS_PARENT_NAME = "DMSName";
    public static final String CONNECTION_PROPERTY_DMS_PARENT_NAME_DEFAULT = "Driver";
    public static final byte CONNECTION_PROPERTY_DMS_PARENT_NAME_ACCESSMODE = 1;
    public static final String CONNECTION_PROPERTY_DMS_PARENT_TYPE = "DMSType";
    public static final String CONNECTION_PROPERTY_DMS_PARENT_TYPE_DEFAULT = "JDBC_Driver";
    public static final byte CONNECTION_PROPERTY_DMS_PARENT_TYPE_ACCESSMODE = 1;
    public static final String CONNECTION_PROPERTY_DMS_STMT_METRICS = "oracle.jdbc.DMSStatementMetrics";
    public static final String CONNECTION_PROPERTY_DMS_STMT_METRICS_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_DMS_STMT_METRICS_ACCESSMODE = 1;
    public static final String CONNECTION_PROPERTY_DMS_STMT_CACHING_METRICS = "oracle.jdbc.DMSStatementCachingMetrics";
    public static final String CONNECTION_PROPERTY_DMS_STMT_CACHING_METRICS_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_DMS_STMT_CACHING_METRICS_ACCESSMODE = 1;
    public static final String CONNECTION_PROPERTY_MAP_DATE_TO_TIMESTAMP = "oracle.jdbc.mapDateToTimestamp";
    public static final String CONNECTION_PROPERTY_MAP_DATE_TO_TIMESTAMP_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_MAP_DATE_TO_TIMESTAMP_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_USE_THREADLOCAL_BUFFER_CACHE = "oracle.jdbc.useThreadLocalBufferCache";
    public static final String CONNECTION_PROPERTY_USE_THREADLOCAL_BUFFER_CACHE_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_USE_THREADLOCAL_BUFFER_CACHE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DRIVER_NAME_ATTRIBUTE = "oracle.jdbc.driverNameAttribute";
    public static final String CONNECTION_PROPERTY_DRIVER_NAME_ATTRIBUTE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_DRIVER_NAME_ATTRIBUTE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_MAX_CACHED_BUFFER_SIZE = "oracle.jdbc.maxCachedBufferSize";
    public static final String CONNECTION_PROPERTY_MAX_CACHED_BUFFER_SIZE_DEFAULT = "30";
    public static final byte CONNECTION_PROPERTY_MAX_CACHED_BUFFER_SIZE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_IMPLICIT_STATEMENT_CACHE_SIZE = "oracle.jdbc.implicitStatementCacheSize";
    public static final String CONNECTION_PROPERTY_IMPLICIT_STATEMENT_CACHE_SIZE_DEFAULT = "0";
    public static final byte CONNECTION_PROPERTY_IMPLICIT_STATEMENT_CACHE_SIZE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_LOB_STREAM_POS_STANDARD_COMPLIANT = "oracle.jdbc.LobStreamPosStandardCompliant";
    public static final String CONNECTION_PROPERTY_LOB_STREAM_POS_STANDARD_COMPLIANT_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_LOB_STREAM_POS_STANDARD_COMPLIANT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_STRICT_ASCII_CONVERSION = "oracle.jdbc.strictASCIIConversion";
    public static final String CONNECTION_PROPERTY_STRICT_ASCII_CONVERSION_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_STRICT_ASCII_CONVERSION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_CONNECTION_CLASS = "oracle.jdbc.DRCPConnectionClass";
    public static final String CONNECTION_PROPERTY_CONNECTION_CLASS_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_CONNECTION_CLASS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DRCP_TAG_NAME = "oracle.jdbc.DRCPTagName";
    public static final String CONNECTION_PROPERTY_DRCP_TAG_NAME_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_DRCP_TAG_NAME_ACCESSMODE = 1;
    public static final String CONNECTION_PROPERTY_CONNECTION_PURITY = "oracle.jdbc.DRCPConnectionPurity";
    public static final String CONNECTION_PROPERTY_CONNECTION_PURITY_DEFAULT = "SELF";
    public static final byte CONNECTION_PROPERTY_CONNECTION_PURITY_ACCESSMODE = 1;
    public static final String CONNECTION_PROPERTY_USE_DRCP_MULTIPLE_TAG = "oracle.jdbc.UseDRCPMultipletag";
    public static final String CONNECTION_PROPERTY_USE_DRCP_MULTIPLE_TAG_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_USE_DRCP_MULTIPLE_TAG_ACCESSMODE = 1;
    public static final String CONNECTION_PROPERTY_DRCP_PLSQL_CALLBACK = "oracle.jdbc.DRCPPLSQLCallback";
    public static final String CONNECTION_PROPERTY_DRCP_PLSQL_CALLBACK_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_DRCP_PLSQL_CALLBACK_ACCESSMODE = 1;
    public static final String CONNECTION_PROPERTY_THIN_FORCE_DNS_LOAD_BALANCING = "oracle.jdbc.thinForceDNSLoadBalancing";
    public static final String CONNECTION_PROPERTY_THIN_FORCE_DNS_LOAD_BALANCING_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_THIN_FORCE_DNS_LOAD_BALANCING_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ENABLE_TEMP_LOB_REF_COUNT = "oracle.jdbc.enableTempLobRefCnt";
    public static final String CONNECTION_PROPERTY_ENABLE_TEMP_LOB_REF_COUNT_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_ENABLE_TEMP_LOB_REF_COUNT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_NET_KEEPALIVE = "oracle.net.keepAlive";
    public static final String CONNECTION_PROPERTY_NET_KEEPALIVE_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_NET_KEEPALIVE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_SQL_TRANSLATION_PROFILE = "oracle.jdbc.sqlTranslationProfile";
    public static final String CONNECTION_PROPERTY_SQL_TRANSLATION_PROFILE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_SQL_TRANSLATION_PROFILE_ACCESSMODE = 3;
    public static final String CONNECTION_PROPERTY_SQL_ERROR_TRANSLATION_FILE = "oracle.jdbc.sqlErrorTranslationFile";
    public static final String CONNECTION_PROPERTY_SQL_ERROR_TRANSLATION_FILE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_SQL_ERROR_TRANSLATION_FILE_ACCESSMODE = 3;
    public static final String CONNECTION_PROPERTY_READONLY_INSTANCE_ALLOWED = "oracle.jdbc.readOnlyInstanceAllowed";
    public static final String CONNECTION_PROPERTY_READONLY_INSTANCE_ALLOWED_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_READONLY_INSTANCE_ALLOWED_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ENABLE_RESULTSET_CACHE = "oracle.jdbc.enableResultSetCache";
    public static final String CONNECTION_PROPERTY_ENABLE_RESULTSET_CACHE_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_ENABLE_RESULTSET_CACHE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ENABLE_QUERY_RESULT_CACHE = "oracle.jdbc.enableQueryResultCache";
    public static final String CONNECTION_PROPERTY_ENABLE_QUERY_RESULT_CACHE_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_ENABLE_QUERY_RESULT_CACHE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_BACKWARD_COMPATIBLE_UPDATEABLE_RESULTSET = "oracle.jdbc.backwardCompatibileUpdateableResultSet";
    public static final String CONNECTION_PROPERTY_BACKWARD_COMPATIBLE_UPDATEABLE_RESULTSET_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_BACKWARD_COMPATIBLE_UPDATEABLE_RESULTSET_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ALLOWED_LOGON_VERSION = "oracle.jdbc.allowedLogonVersion";
    public static final String CONNECTION_PROPERTY_ALLOWED_LOGON_VERSION_DEFAULT = "8";
    public static final byte CONNECTION_PROPERTY_ALLOWED_LOGON_VERSION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_COMMIT_OPTION = "oracle.jdbc.commitOption";
    public static final String CONNECTION_PROPERTY_COMMIT_OPTION_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_COMMIT_OPTION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DOWN_HOSTS_TIMEOUT = "oracle.net.DOWN_HOSTS_TIMEOUT";
    public static final String CONNECTION_PROPERTY_DOWN_HOSTS_TIMEOUT_DEFAULT = "600";
    public static final byte CONNECTION_PROPERTY_DOWN_HOSTS_TIMEOUT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_FAN_ENABLED = "oracle.jdbc.fanEnabled";
    public static final String CONNECTION_PROPERTY_FAN_ENABLED_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_FAN_ENABLED_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_TNS_ADMIN = "oracle.net.tns_admin";
    public static final String CONNECTION_PROPERTY_TNS_ADMIN_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_TNS_ADMIN_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_NETWORK_COMPRESSION = "oracle.net.networkCompression";
    public static final String CONNECTION_PROPERTY_NETWORK_COMPRESSION_DEFAULT = "off";
    public static final byte CONNECTION_PROPERTY_NETWORK_COMPRESSION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_NETWORK_COMPRESSION_LEVELS = "oracle.net.networkCompressionLevels";
    public static final String CONNECTION_PROPERTY_NETWORK_COMPRESSION_LEVELS_DEFAULT = "(high)";
    public static final byte CONNECTION_PROPERTY_NETWORK_COMPRESSION_LEVELS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_NETWORK_COMPRESSION_THRESHOLD = "oracle.net.networkCompressionThreshold";
    public static final String CONNECTION_PROPERTY_NETWORK_COMPRESSION_THRESHOLD_DEFAULT = "1024";
    public static final byte CONNECTION_PROPERTY_NETWORK_COMPRESSION_THRESHOLD_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_CONFIG_FILE = "oracle.jdbc.config.file";
    public static final String CONNECTION_PROPERTY_CONFIG_FILE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_CONFIG_FILE_ACCESSMODE = 3;
    public static final String CONNECTION_PROPERTY_WEBSOCKET_USER = "oracle.net.websocketUser";
    public static final String CONNECTION_PROPERTY_WEBSOCKET_USER_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_WEBSOCKET_USER_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_WEBSOCKET_PASSWORD = "oracle.net.websocketPassword";
    public static final String CONNECTION_PROPERTY_WEBSOCKET_PASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_WEBSOCKET_PASSWORD_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_SOCKS_PROXY_HOST = "oracle.net.socksProxyHost";
    public static final String CONNECTION_PROPERTY_SOCKS_PROXY_HOST_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_SOCKS_PROXY_HOST_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_SOCKS_PROXY_PORT = "oracle.net.socksProxyPort";
    public static final String CONNECTION_PROPERTY_SOCKS_PROXY_PORT_DEFAULT = "1080";
    public static final byte CONNECTION_PROPERTY_SOCKS_PROXY_PORT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_SOCKS_REMOTE_DNS = "oracle.net.socksRemoteDNS";
    public static final String CONNECTION_PROPERTY_SOCKS_REMOTE_DNS_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_SOCKS_REMOTE_DNS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DEFAULT_CONNECTION_VALIDATION = "oracle.jdbc.defaultConnectionValidation";
    public static final String CONNECTION_PROPERTY_DEFAULT_CONNECTION_VALIDATION_DEFAULT = "NETWORK";
    public static final byte CONNECTION_PROPERTY_DEFAULT_CONNECTION_VALIDATION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ENABLE_AC_SUPPORT = "oracle.jdbc.enableACSupport";
    public static final String CONNECTION_PROPERTY_ENABLE_AC_SUPPORT_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_ENABLE_AC_SUPPORT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ENABLE_TG_SUPPORT = "oracle.jdbc.enableTGSupport";
    public static final String CONNECTION_PROPERTY_ENABLE_TG_SUPPORT_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_ENABLE_TG_SUPPORT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ENABLE_IMPLICIT_REQUESTS = "oracle.jdbc.enableImplicitRequests";
    public static final String CONNECTION_PROPERTY_ENABLE_IMPLICIT_REQUESTS_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_ENABLE_IMPLICIT_REQUESTS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_DRCP_MULTIPLEXING_IN_REQUEST_APIS = "oracle.jdbc.DRCPMultiplexingInRequestAPIs";
    public static final String CONNECTION_PROPERTY_DRCP_MULTIPLEXING_IN_REQUEST_APIS_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_DRCP_MULTIPLEXING_IN_REQUEST_APIS_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_CONTINUE_BATCH_ON_ERROR = "oracle.jdbc.continueBatchOnError";
    public static final String CONNECTION_PROPERTY_CONTINUE_BATCH_ON_ERROR_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_CONTINUE_BATCH_ON_ERROR_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_TCP_KEEPIDLE = "oracle.net.TCP_KEEPIDLE";
    public static final String CONNECTION_PROPERTY_TCP_KEEPIDLE_DEFAULT = "-1";
    public static final byte CONNECTION_PROPERTY_TCP_KEEPIDLE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_TCP_KEEPINTERVAL = "oracle.net.TCP_KEEPINTERVAL";
    public static final String CONNECTION_PROPERTY_TCP_KEEPINTERVAL_DEFAULT = "-1";
    public static final byte CONNECTION_PROPERTY_TCP_KEEPINTERVAL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_USE_SHARDING_DRIVER_CONNECTION = "oracle.jdbc.useShardingDriverConnection";
    public static final String CONNECTION_PROPERTY_USE_SHARDING_DRIVER_CONNECTION_DEFAULT = "false";
    public static final byte CONNECTION_PROPERTY_USE_SHARDING_DRIVER_CONNECTION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_TCP_KEEPCOUNT = "oracle.net.TCP_KEEPCOUNT";
    public static final String CONNECTION_PROPERTY_TCP_KEEPCOUNT_DEFAULT = "-1";
    public static final byte CONNECTION_PROPERTY_TCP_KEEPCOUNT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_REQUEST_SIZE_LIMIT = "oracle.jdbc.replay.protectedRequestSizeLimit";
    public static final String CONNECTION_PROPERTY_REQUEST_SIZE_LIMIT_DEFAULT = "2147483647";
    public static final byte CONNECTION_PROPERTY_REQUEST_SIZE_LIMIT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ONS_WALLET_FILE = "oracle.jdbc.ons.walletfile";
    public static final String CONNECTION_PROPERTY_ONS_WALLET_FILE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_ONS_WALLET_FILE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ONS_WALLET_PASSWORD = "oracle.jdbc.ons.walletpassword";
    public static final String CONNECTION_PROPERTY_ONS_WALLET_PASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_ONS_WALLET_PASSWORD_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ONS_PROTOCOL = "oracle.jdbc.ons.protocol";
    public static final String CONNECTION_PROPERTY_ONS_PROTOCOL_DEFAULT = "TCP";
    public static final byte CONNECTION_PROPERTY_ONS_PROTOCOL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_LOGIN_TIMEOUT = "oracle.jdbc.loginTimeout";
    public static final String CONNECTION_PROPERTY_LOGIN_TIMEOUT_DEFAULT = "0";
    public static final byte CONNECTION_PROPERTY_LOGIN_TIMEOUT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_IN_BAND_NOTIFICATION = "oracle.jdbc.inbandNotification";
    public static final String CONNECTION_PROPERTY_IN_BAND_NOTIFICATION_DEFAULT = "true";
    public static final byte CONNECTION_PROPERTY_IN_BAND_NOTIFICATION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_SSL_CONTEXT_PROTOCOL = "oracle.net.ssl_context_protocol";
    public static final String CONNECTION_PROPERTY_SSL_CONTEXT_PROTOCOL_DEFAULT = "TLS";
    public static final byte CONNECTION_PROPERTY_SSL_CONTEXT_PROTOCOL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_TOKEN_AUTHENTICATION = "oracle.jdbc.tokenAuthentication";
    public static final String CONNECTION_PROPERTY_TOKEN_AUTHENTICATION_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_TOKEN_AUTHENTICATION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_TOKEN_LOCATION = "oracle.jdbc.tokenLocation";
    public static final String CONNECTION_PROPERTY_TOKEN_LOCATION_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_TOKEN_LOCATION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_PASSWORD_AUTHENTICATION = "oracle.jdbc.passwordAuthentication";
    public static final String CONNECTION_PROPERTY_PASSWORD_AUTHENTICATION_DEFAULT = "PASSWORD_VERIFIER";
    public static final byte CONNECTION_PROPERTY_PASSWORD_AUTHENTICATION_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_OCI_IAM_URL = "oracle.jdbc.ociIamUrl";
    public static final String CONNECTION_PROPERTY_OCI_IAM_URL_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_OCI_IAM_URL_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_OCI_TENANCY = "oracle.jdbc.ociTenancy";
    public static final String CONNECTION_PROPERTY_OCI_TENANCY_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_OCI_TENANCY_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_OCI_COMPARTMENT = "oracle.jdbc.ociCompartment";
    public static final String CONNECTION_PROPERTY_OCI_COMPARTMENT_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_OCI_COMPARTMENT_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_OCI_DATABASE = "oracle.jdbc.ociDatabase";
    public static final String CONNECTION_PROPERTY_OCI_DATABASE_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_OCI_DATABASE_ACCESSMODE = 7;
    public static final String CONNECTION_PROPERTY_ACCESS_TOKEN = "oracle.jdbc.accessToken";
    public static final String CONNECTION_PROPERTY_ACCESS_TOKEN_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_ACCESS_TOKEN_ACCESSMODE = 5;
    public static final String CONNECTION_PROPERTY_PASSWORD = "password";
    public static final String CONNECTION_PROPERTY_PASSWORD_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_PASSWORD_ACCESSMODE = 5;
    public static final String CONNECTION_PROPERTY_SERVER = "server";
    public static final String CONNECTION_PROPERTY_SERVER_DEFAULT = null;
    public static final byte CONNECTION_PROPERTY_SERVER_ACCESSMODE = 1;
    public static final int DATABASE_OK = 0;
    public static final int DATABASE_CLOSED = -1;
    public static final int DATABASE_NOTOK = -2;
    public static final int DATABASE_TIMEOUT = -3;
    public static final int INVALID_CONNECTION = 4096;
    public static final int PROXY_SESSION = 1;
    public static final int ABANDONED_CONNECTION_CALLBACK = 1;
    public static final int RELEASE_CONNECTION_CALLBACK = 2;
    public static final int ALL_CONNECTION_CALLBACKS = 4;
    public static final int CONNECTION_RELEASE_LOCKED = 256;
    public static final int CONNECTION_RELEASE_LOW = 512;
    public static final int CONNECTION_RELEASE_HIGH = 1024;
    public static final int PROXYTYPE_USER_NAME = 1;
    public static final int PROXYTYPE_DISTINGUISHED_NAME = 2;
    public static final int PROXYTYPE_CERTIFICATE = 3;
    public static final String PROXY_TYPE = "PROXY_TYPE";
    public static final String PROXY_USER_NAME = "PROXY_USER_NAME";
    public static final String PROXY_USER_PASSWORD = "PROXY_USER_PASSWORD";
    public static final String PROXY_DISTINGUISHED_NAME = "PROXY_DISTINGUISHED_NAME";
    public static final String PROXY_CERTIFICATE = "PROXY_CERTIFICATE";
    public static final String PROXY_ROLES = "PROXY_ROLES";
    public static final String CLIENT_INFO_KEY_SEPARATOR = ".";
    public static final String OCSID_NAMESPACE = "OCSID";
    public static final String OCSID_ACTION_KEY = "ACTION";
    public static final String OCSID_CLIENTID_KEY = "CLIENTID";
    public static final String OCSID_ECID_KEY = "ECID";
    public static final String OCSID_MODULE_KEY = "MODULE";
    public static final String OCSID_DBOP_KEY = "DBOP";
    public static final String OCSID_SEQUENCE_NUMBER_KEY = "SEQUENCE_NUMBER";
    public static final String OCSID_CLIENT_INFO_KEY = "CLIENT_INFO";
    public static final int END_TO_END_ACTION_INDEX = 0;
    public static final int END_TO_END_CLIENTID_INDEX = 1;
    public static final int END_TO_END_ECID_INDEX = 2;
    public static final int END_TO_END_MODULE_INDEX = 3;
    public static final int END_TO_END_STATE_INDEX_MAX = 4;
    public static final String NETWORK_COMPRESSION_OFF = "off";
    public static final String NETWORK_COMPRESSION_ON = "on";
    public static final String NETWORK_COMPRESSION_AUTO = "auto";
    public static final String NETWORK_COMPRESSION_LEVEL_LOW = "low";
    public static final int NETWORK_COMPRESSION_LEVEL_LOW_VALUE = 1;
    public static final String NETWORK_COMPRESSION_LEVEL_HIGH = "high";
    public static final int NETWORK_COMPRESSION_LEVEL_HIGH_VALUE = 2;
    public static final int NETWORK_COMPRESSION_THRESHOLD_MIN = 200;
    public static final int CACHE_SIZE_NOT_SET = -1;
    public static final String NTF_TIMEOUT = "NTF_TIMEOUT";
    public static final String NTF_QOS_PURGE_ON_NTFN = "NTF_QOS_PURGE_ON_NTFN";
    public static final String NTF_QOS_RELIABLE = "NTF_QOS_RELIABLE";
    public static final String NTF_QOS_SECURE = "NTF_QOS_SECURE";
    public static final String NTF_ASYNC_DEQ = "NTF_ASYNC_DEQ";
    public static final String NTF_AQ_PAYLOAD = "NTF_AQ_PAYLOAD";
    public static final String NTF_USE_SSL = "NTF_USE_SSL";
    public static final String NTF_QOS_TX_ACK = "NTF_QOS_TX_ACK";
    public static final String NTF_QOS_AUTO_ACK = "NTF_QOS_AUTO_ACK";
    public static final String NTF_LOCAL_TCP_PORT = "NTF_LOCAL_TCP_PORT";
    public static final int NTF_DEFAULT_TCP_PORT = 47632;
    public static final String NTF_LOCAL_HOST = "NTF_LOCAL_HOST";
    public static final String NTF_GROUPING_CLASS = "NTF_GROUPING_CLASS";
    public static final String NTF_GROUPING_CLASS_NONE = "NTF_GROUPING_CLASS_NONE";
    public static final String NTF_GROUPING_CLASS_TIME = "NTF_GROUPING_CLASS_TIME";
    public static final String NTF_GROUPING_VALUE = "NTF_GROUPING_VALUE";
    public static final String NTF_GROUPING_TYPE = "NTF_GROUPING_TYPE";
    public static final String NTF_GROUPING_TYPE_SUMMARY = "NTF_GROUPING_TYPE_SUMMARY";
    public static final String NTF_GROUPING_TYPE_LAST = "NTF_GROUPING_TYPE_LAST";
    public static final String NTF_GROUPING_START_TIME = "NTF_GROUPING_START_TIME";
    public static final String NTF_GROUPING_REPEAT_TIME = "NTF_GROUPING_REPEAT_TIME";
    public static final String NTF_GROUPING_REPEAT_FOREVER = "NTF_GROUPING_REPEAT_FOREVER";
    public static final String DCN_NOTIFY_ROWIDS = "DCN_NOTIFY_ROWIDS";
    public static final String DCN_IGNORE_INSERTOP = "DCN_IGNORE_INSERTOP";
    public static final String DCN_IGNORE_UPDATEOP = "DCN_IGNORE_UPDATEOP";
    public static final String DCN_IGNORE_DELETEOP = "DCN_IGNORE_DELETEOP";
    public static final String DCN_NOTIFY_CHANGELAG = "DCN_NOTIFY_CHANGELAG";
    public static final String DCN_QUERY_CHANGE_NOTIFICATION = "DCN_QUERY_CHANGE_NOTIFICATION";
    public static final String DCN_BEST_EFFORT = "DCN_BEST_EFFORT";
    public static final String DCN_CLIENT_INIT_CONNECTION = "DCN_CLIENT_INIT_CONNECTION";
    public static final String DCN_USE_HOST_CONNECTION_ADDR_INFO = "DCN_USE_HOST_CONNECTION_ADDR_INFO";
    public static final String AQ_USE_HOST_CONNECTION_ADDR_INFO = "AQ_USE_HOST_CONNECTION_ADDR_INFO";

    public void commit(EnumSet<CommitOption> var1) throws SQLException;

    public void archive(int var1, int var2, String var3) throws SQLException;

    public void openProxySession(int var1, Properties var2) throws SQLException;

    public boolean getAutoClose() throws SQLException;

    public int getDefaultExecuteBatch();

    public int getDefaultRowPrefetch();

    public Object getDescriptor(String var1);

    public String[] getEndToEndMetrics() throws SQLException;

    public short getEndToEndECIDSequenceNumber() throws SQLException;

    public boolean getIncludeSynonyms();

    public boolean getRestrictGetTables();

    public Object getJavaObject(String var1) throws SQLException;

    public boolean getRemarksReporting();

    public String getSQLType(Object var1) throws SQLException;

    public int getStmtCacheSize();

    public short getStructAttrCsId() throws SQLException;

    public String getUserName() throws SQLException;

    public String getCurrentSchema() throws SQLException;

    public boolean getUsingXAFlag();

    public boolean getXAErrorFlag();

    public int pingDatabase() throws SQLException;

    public int pingDatabase(int var1) throws SQLException;

    public void putDescriptor(String var1, Object var2) throws SQLException;

    public void registerSQLType(String var1, Class<?> var2) throws SQLException;

    public void registerSQLType(String var1, String var2) throws SQLException;

    public void setAutoClose(boolean var1) throws SQLException;

    public void setDefaultExecuteBatch(int var1) throws SQLException;

    public void setDefaultRowPrefetch(int var1) throws SQLException;

    public void setEndToEndMetrics(String[] var1, short var2) throws SQLException;

    public void setIncludeSynonyms(boolean var1);

    public void setRemarksReporting(boolean var1);

    public void setRestrictGetTables(boolean var1);

    public void setStmtCacheSize(int var1) throws SQLException;

    public void setStmtCacheSize(int var1, boolean var2) throws SQLException;

    public void setStatementCacheSize(int var1) throws SQLException;

    public int getStatementCacheSize() throws SQLException;

    public void setImplicitCachingEnabled(boolean var1) throws SQLException;

    public boolean getImplicitCachingEnabled() throws SQLException;

    public void setExplicitCachingEnabled(boolean var1) throws SQLException;

    public boolean getExplicitCachingEnabled() throws SQLException;

    public void purgeImplicitCache() throws SQLException;

    public void purgeExplicitCache() throws SQLException;

    public PreparedStatement getStatementWithKey(String var1) throws SQLException;

    public CallableStatement getCallWithKey(String var1) throws SQLException;

    public void setUsingXAFlag(boolean var1);

    public void setXAErrorFlag(boolean var1);

    public void shutdown(DatabaseShutdownMode var1) throws SQLException;

    public void startup(String var1, int var2) throws SQLException;

    public void startup(DatabaseStartupMode var1) throws SQLException;

    public void startup(DatabaseStartupMode var1, String var2) throws SQLException;

    public PreparedStatement prepareStatementWithKey(String var1) throws SQLException;

    public CallableStatement prepareCallWithKey(String var1) throws SQLException;

    public void setCreateStatementAsRefCursor(boolean var1);

    public boolean getCreateStatementAsRefCursor();

    public void setSessionTimeZone(String var1) throws SQLException;

    public String getSessionTimeZone();

    public String getSessionTimeZoneOffset() throws SQLException;

    public Properties getProperties();

    public Connection _getPC();

    public boolean isLogicalConnection();

    public void registerTAFCallback(OracleOCIFailover var1, Object var2) throws SQLException;

    public OracleConnection unwrap();

    public void setWrapper(OracleConnection var1);

    public oracle.jdbc.internal.OracleConnection physicalConnectionWithin();

    public OracleSavepoint oracleSetSavepoint() throws SQLException;

    public OracleSavepoint oracleSetSavepoint(String var1) throws SQLException;

    public void oracleRollback(OracleSavepoint var1) throws SQLException;

    public void oracleReleaseSavepoint(OracleSavepoint var1) throws SQLException;

    @Deprecated
    default public void close(Properties properties) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void close(int var1) throws SQLException;

    public boolean isProxySession();

    @Deprecated
    default public void applyConnectionAttributes(Properties properties) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default public Properties getConnectionAttributes() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default public Properties getUnMatchedConnectionAttributes() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default public void registerConnectionCacheCallback(OracleConnectionCacheCallback oracleConnectionCacheCallback, Object object, int n2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default public void setConnectionReleasePriority(int n2) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default public int getConnectionReleasePriority() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public void setPlsqlWarnings(String var1) throws SQLException;

    public AQNotificationRegistration[] registerAQNotification(String[] var1, Properties[] var2, Properties var3) throws SQLException;

    public void unregisterAQNotification(AQNotificationRegistration var1) throws SQLException;

    public AQMessage dequeue(String var1, AQDequeueOptions var2, byte[] var3) throws SQLException;

    public AQMessage dequeue(String var1, AQDequeueOptions var2, byte[] var3, int var4) throws SQLException;

    public AQMessage dequeue(String var1, AQDequeueOptions var2, String var3) throws SQLException;

    public void enqueue(String var1, AQEnqueueOptions var2, AQMessage var3) throws SQLException;

    public int enqueue(String var1, AQEnqueueOptions var2, AQMessage[] var3) throws SQLException;

    public AQMessage[] dequeue(String var1, AQDequeueOptions var2, String var3, int var4) throws SQLException;

    public AQMessage[] dequeue(String var1, AQDequeueOptions var2, byte[] var3, int var4, int var5) throws SQLException;

    public DatabaseChangeRegistration registerDatabaseChangeNotification(Properties var1) throws SQLException;

    public DatabaseChangeRegistration getDatabaseChangeRegistration(int var1) throws SQLException;

    public void unregisterDatabaseChangeNotification(DatabaseChangeRegistration var1) throws SQLException;

    public void unregisterDatabaseChangeNotification(int var1, String var2, int var3) throws SQLException;

    public void unregisterDatabaseChangeNotification(int var1) throws SQLException;

    public void unregisterDatabaseChangeNotification(long var1, String var3) throws SQLException;

    public ARRAY createARRAY(String var1, Object var2) throws SQLException;

    public Array createOracleArray(String var1, Object var2) throws SQLException;

    public BINARY_DOUBLE createBINARY_DOUBLE(double var1) throws SQLException;

    public BINARY_FLOAT createBINARY_FLOAT(float var1) throws SQLException;

    public DATE createDATE(Date var1) throws SQLException;

    public DATE createDATE(Time var1) throws SQLException;

    public DATE createDATE(Timestamp var1) throws SQLException;

    public DATE createDATE(Date var1, Calendar var2) throws SQLException;

    public DATE createDATE(Time var1, Calendar var2) throws SQLException;

    public DATE createDATE(Timestamp var1, Calendar var2) throws SQLException;

    public DATE createDATE(String var1) throws SQLException;

    public INTERVALDS createINTERVALDS(String var1) throws SQLException;

    public INTERVALYM createINTERVALYM(String var1) throws SQLException;

    public NUMBER createNUMBER(boolean var1) throws SQLException;

    public NUMBER createNUMBER(byte var1) throws SQLException;

    public NUMBER createNUMBER(short var1) throws SQLException;

    public NUMBER createNUMBER(int var1) throws SQLException;

    public NUMBER createNUMBER(long var1) throws SQLException;

    public NUMBER createNUMBER(float var1) throws SQLException;

    public NUMBER createNUMBER(double var1) throws SQLException;

    public NUMBER createNUMBER(BigDecimal var1) throws SQLException;

    public NUMBER createNUMBER(BigInteger var1) throws SQLException;

    public NUMBER createNUMBER(String var1, int var2) throws SQLException;

    public TIMESTAMP createTIMESTAMP(Date var1) throws SQLException;

    public TIMESTAMP createTIMESTAMP(DATE var1) throws SQLException;

    public TIMESTAMP createTIMESTAMP(Time var1) throws SQLException;

    public TIMESTAMP createTIMESTAMP(Timestamp var1) throws SQLException;

    public TIMESTAMP createTIMESTAMP(Timestamp var1, Calendar var2) throws SQLException;

    public TIMESTAMP createTIMESTAMP(String var1) throws SQLException;

    public TIMESTAMPTZ createTIMESTAMPTZ(Date var1) throws SQLException;

    public TIMESTAMPTZ createTIMESTAMPTZ(Date var1, Calendar var2) throws SQLException;

    public TIMESTAMPTZ createTIMESTAMPTZ(Time var1) throws SQLException;

    public TIMESTAMPTZ createTIMESTAMPTZ(Time var1, Calendar var2) throws SQLException;

    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp var1) throws SQLException;

    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp var1, Calendar var2) throws SQLException;

    public TIMESTAMPTZ createTIMESTAMPTZ(Timestamp var1, ZoneId var2) throws SQLException;

    public TIMESTAMPTZ createTIMESTAMPTZ(String var1) throws SQLException;

    public TIMESTAMPTZ createTIMESTAMPTZ(String var1, Calendar var2) throws SQLException;

    public TIMESTAMPTZ createTIMESTAMPTZ(DATE var1) throws SQLException;

    public TIMESTAMPLTZ createTIMESTAMPLTZ(Date var1, Calendar var2) throws SQLException;

    public TIMESTAMPLTZ createTIMESTAMPLTZ(Time var1, Calendar var2) throws SQLException;

    public TIMESTAMPLTZ createTIMESTAMPLTZ(Timestamp var1, Calendar var2) throws SQLException;

    public TIMESTAMPLTZ createTIMESTAMPLTZ(String var1, Calendar var2) throws SQLException;

    public TIMESTAMPLTZ createTIMESTAMPLTZ(DATE var1, Calendar var2) throws SQLException;

    public void cancel() throws SQLException;

    public void abort() throws SQLException;

    public TypeDescriptor[] getAllTypeDescriptorsInCurrentSchema() throws SQLException;

    public TypeDescriptor[] getTypeDescriptorsFromListInCurrentSchema(String[] var1) throws SQLException;

    public TypeDescriptor[] getTypeDescriptorsFromList(String[][] var1) throws SQLException;

    public String getDataIntegrityAlgorithmName() throws SQLException;

    public String getEncryptionAlgorithmName() throws SQLException;

    public String getAuthenticationAdaptorName() throws SQLException;

    public boolean isUsable();

    public void setDefaultTimeZone(TimeZone var1) throws SQLException;

    public TimeZone getDefaultTimeZone() throws SQLException;

    public void setApplicationContext(String var1, String var2, String var3) throws SQLException;

    public void clearAllApplicationContext(String var1) throws SQLException;

    public void addLogicalTransactionIdEventListener(LogicalTransactionIdEventListener var1) throws SQLException;

    public void addLogicalTransactionIdEventListener(LogicalTransactionIdEventListener var1, Executor var2) throws SQLException;

    public void removeLogicalTransactionIdEventListener(LogicalTransactionIdEventListener var1) throws SQLException;

    public LogicalTransactionId getLogicalTransactionId() throws SQLException;

    public boolean isDRCPEnabled() throws SQLException;

    public boolean isDRCPMultitagEnabled() throws SQLException;

    public String getDRCPReturnTag() throws SQLException;

    public String getDRCPPLSQLCallbackName() throws SQLException;

    public boolean attachServerConnection() throws SQLException;

    public void detachServerConnection(String var1) throws SQLException;

    public boolean needToPurgeStatementCache() throws SQLException;

    public DRCPState getDRCPState() throws SQLException;

    public void beginRequest() throws SQLException;

    public void endRequest() throws SQLException;

    public boolean setShardingKeyIfValid(OracleShardingKey var1, OracleShardingKey var2, int var3) throws SQLException;

    public void setShardingKey(OracleShardingKey var1, OracleShardingKey var2) throws SQLException;

    public boolean setShardingKeyIfValid(OracleShardingKey var1, int var2) throws SQLException;

    public void setShardingKey(OracleShardingKey var1) throws SQLException;

    public boolean isValid(ConnectionValidation var1, int var2) throws SQLException;

    public String getEncryptionProviderName() throws SQLException;

    public String getChecksumProviderName() throws SQLException;

    public String getNetConnectionId() throws SQLException;

    public void disableLogging() throws SQLException;

    public void enableLogging() throws SQLException;

    public void dumpLog() throws SQLException;

    public SecuredLogger getLogger() throws SQLException;

    public static enum ConnectionValidation {
        NONE,
        LOCAL,
        SOCKET,
        NETWORK,
        SERVER,
        COMPLETE;

    }

    public static enum DRCPState {
        DETACHED,
        ATTACHED_IMPLICIT,
        ATTACHED_EXPLICIT;

    }

    public static enum CommitOption {
        WRITEBATCH(1),
        WRITEIMMED(2),
        WAIT(4),
        NOWAIT(8);

        private final int code;

        private CommitOption(int n3) {
            this.code = n3;
        }

        public final int getCode() {
            return this.code;
        }
    }

    public static enum DatabaseStartupMode {
        NO_RESTRICTION(0),
        FORCE(1),
        RESTRICT(2);

        private final int mode;

        private DatabaseStartupMode(int n3) {
            this.mode = n3;
        }

        public final int getMode() {
            return this.mode;
        }
    }

    public static enum DatabaseShutdownMode {
        CONNECT(0),
        TRANSACTIONAL(1),
        TRANSACTIONAL_LOCAL(2),
        IMMEDIATE(3),
        ABORT(4),
        FINAL(5);

        private final int mode;

        private DatabaseShutdownMode(int n3) {
            this.mode = n3;
        }

        public final int getMode() {
            return this.mode;
        }
    }
}

