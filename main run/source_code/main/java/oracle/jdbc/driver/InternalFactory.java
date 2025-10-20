/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.aq.AQAgent;
import oracle.jdbc.aq.AQMessage;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.driver.AQAgentI;
import oracle.jdbc.driver.AQMessageI;
import oracle.jdbc.driver.AQMessagePropertiesI;
import oracle.jdbc.driver.JMSMessageI;
import oracle.jdbc.driver.JMSMessagePropertiesI;
import oracle.jdbc.driver.KeywordValueI;
import oracle.jdbc.driver.KeywordValueLongI;
import oracle.jdbc.driver.T4CRowidAccessor;
import oracle.jdbc.driver.XSAttributeI;
import oracle.jdbc.driver.XSKeyvalI;
import oracle.jdbc.driver.XSNamespaceI;
import oracle.jdbc.driver.XSPrincipalI;
import oracle.jdbc.driver.XSSecureIdI;
import oracle.jdbc.driver.XSSessionNamespaceI;
import oracle.jdbc.driver.XSSessionParametersI;
import oracle.jdbc.internal.JMSMessage;
import oracle.jdbc.internal.JMSMessageProperties;
import oracle.jdbc.internal.KeywordValue;
import oracle.jdbc.internal.KeywordValueLong;
import oracle.jdbc.internal.XSAttribute;
import oracle.jdbc.internal.XSKeyval;
import oracle.jdbc.internal.XSNamespace;
import oracle.jdbc.internal.XSPrincipal;
import oracle.jdbc.internal.XSSecureId;
import oracle.jdbc.internal.XSSecurityPermission;
import oracle.jdbc.internal.XSSessionNamespace;
import oracle.jdbc.internal.XSSessionParameters;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
public final class InternalFactory {
    private static final String PERMISSION_NAME = "callJdbcXS";
    private static final XSSecurityPermission XS_SECURITY_PERMISSION = new XSSecurityPermission("callJdbcXS");

    private InternalFactory() {
        throw new RuntimeException("Cannot be instantiated");
    }

    static void xsSecurityCheck() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(XS_SECURITY_PERMISSION);
        }
    }

    public static XSAttribute createXSAttribute() throws SQLException {
        InternalFactory.xsSecurityCheck();
        return new XSAttributeI();
    }

    public static XSNamespace createXSNamespace() throws SQLException {
        InternalFactory.xsSecurityCheck();
        return new XSNamespaceI();
    }

    public static XSSecureId createXSecureId() throws SQLException {
        InternalFactory.xsSecurityCheck();
        return new XSSecureIdI();
    }

    public static XSPrincipal createXSPrincipal() throws SQLException {
        InternalFactory.xsSecurityCheck();
        return new XSPrincipalI();
    }

    public static XSKeyval createXSKeyval() throws SQLException {
        InternalFactory.xsSecurityCheck();
        return new XSKeyvalI();
    }

    public static XSSessionNamespace createXSSessionNamespace() throws SQLException {
        InternalFactory.xsSecurityCheck();
        return new XSSessionNamespaceI();
    }

    public static XSSessionParameters createXSSessionParameters() throws SQLException {
        InternalFactory.xsSecurityCheck();
        return new XSSessionParametersI();
    }

    public static KeywordValue createKeywordValue(int n2, String string, byte[] byArray) throws SQLException {
        return new KeywordValueI(n2, string, byArray);
    }

    public static KeywordValueLong createKeywordValueLong(int n2, String string, byte[] byArray) throws SQLException {
        return new KeywordValueLongI(n2, string, byArray);
    }

    public static AQMessageProperties createAQMessageProperties() throws SQLException {
        return new AQMessagePropertiesI();
    }

    public static AQAgent createAQAgent() throws SQLException {
        return new AQAgentI();
    }

    public static AQMessage createAQMessage(AQMessageProperties aQMessageProperties) throws SQLException {
        return new AQMessageI((AQMessagePropertiesI)aQMessageProperties);
    }

    public static JMSMessage createJMSMessage(JMSMessageProperties jMSMessageProperties) {
        return new JMSMessageI((JMSMessagePropertiesI)jMSMessageProperties);
    }

    public static JMSMessageProperties createJMSMessageProperties() throws SQLException {
        return new JMSMessagePropertiesI();
    }

    public static byte[] urowid2rowid(long[] lArray) {
        return T4CRowidAccessor.rowidToString(lArray);
    }

    public static long[] rowid2urowid(byte[] byArray, int n2, int n3) throws SQLException {
        return T4CRowidAccessor.stringToRowid(byArray, n2, n3);
    }
}

