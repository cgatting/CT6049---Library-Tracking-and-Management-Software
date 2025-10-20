/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.EventListener;
import java.util.concurrent.Executor;
import oracle.jdbc.LogicalTransactionIdEventListener;
import oracle.jdbc.aq.AQNotificationListener;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.internal.JMSConsumerExceptionListener;
import oracle.jdbc.internal.JMSNotificationListener;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.internal.PDBChangeEventListener;
import oracle.jdbc.internal.XSEventListener;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class NTFEventListener {
    private final AQNotificationListener aqlistener;
    private final DatabaseChangeListener dcnlistener;
    private final XSEventListener xslistener;
    private final LogicalTransactionIdEventListener ltxidlistener;
    private final JMSNotificationListener jmslistener;
    private final PDBChangeEventListener pdbChangeListener;
    private final JMSConsumerExceptionListener ntfExceptionListener;
    private Executor executor = null;

    NTFEventListener(JMSConsumerExceptionListener jMSConsumerExceptionListener) throws SQLException {
        if (jMSConsumerExceptionListener == null) {
            throw (SQLException)DatabaseError.createSqlException(246).fillInStackTrace();
        }
        this.jmslistener = null;
        this.dcnlistener = null;
        this.aqlistener = null;
        this.xslistener = null;
        this.ltxidlistener = null;
        this.pdbChangeListener = null;
        this.ntfExceptionListener = jMSConsumerExceptionListener;
    }

    NTFEventListener(PDBChangeEventListener pDBChangeEventListener) throws SQLException {
        if (pDBChangeEventListener == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 246).fillInStackTrace();
        }
        this.jmslistener = null;
        this.dcnlistener = null;
        this.aqlistener = null;
        this.xslistener = null;
        this.ltxidlistener = null;
        this.pdbChangeListener = pDBChangeEventListener;
        this.ntfExceptionListener = null;
    }

    NTFEventListener(DatabaseChangeListener databaseChangeListener) throws SQLException {
        if (databaseChangeListener == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 246).fillInStackTrace();
        }
        this.jmslistener = null;
        this.dcnlistener = databaseChangeListener;
        this.aqlistener = null;
        this.xslistener = null;
        this.ltxidlistener = null;
        this.pdbChangeListener = null;
        this.ntfExceptionListener = null;
    }

    NTFEventListener(JMSNotificationListener jMSNotificationListener) throws SQLException {
        if (jMSNotificationListener == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 246).fillInStackTrace();
        }
        this.jmslistener = jMSNotificationListener;
        this.aqlistener = null;
        this.dcnlistener = null;
        this.xslistener = null;
        this.ltxidlistener = null;
        this.pdbChangeListener = null;
        this.ntfExceptionListener = null;
    }

    NTFEventListener(AQNotificationListener aQNotificationListener) throws SQLException {
        if (aQNotificationListener == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 246).fillInStackTrace();
        }
        this.jmslistener = null;
        this.aqlistener = aQNotificationListener;
        this.dcnlistener = null;
        this.xslistener = null;
        this.ltxidlistener = null;
        this.pdbChangeListener = null;
        this.ntfExceptionListener = null;
    }

    NTFEventListener(XSEventListener xSEventListener) throws SQLException {
        if (xSEventListener == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 246).fillInStackTrace();
        }
        this.jmslistener = null;
        this.aqlistener = null;
        this.dcnlistener = null;
        this.xslistener = xSEventListener;
        this.ltxidlistener = null;
        this.pdbChangeListener = null;
        this.ntfExceptionListener = null;
    }

    NTFEventListener(LogicalTransactionIdEventListener logicalTransactionIdEventListener) throws SQLException {
        if (logicalTransactionIdEventListener == null) {
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 246).fillInStackTrace();
        }
        this.jmslistener = null;
        this.aqlistener = null;
        this.dcnlistener = null;
        this.xslistener = null;
        this.ltxidlistener = logicalTransactionIdEventListener;
        this.pdbChangeListener = null;
        this.ntfExceptionListener = null;
    }

    void setExecutor(Executor executor) {
        this.executor = executor;
    }

    Executor getExecutor() {
        return this.executor;
    }

    EventListener getListener() {
        EventListener eventListener = this.dcnlistener;
        if (eventListener == null) {
            eventListener = this.aqlistener;
        }
        if (eventListener == null) {
            eventListener = this.jmslistener;
        }
        if (eventListener == null) {
            eventListener = this.pdbChangeListener;
        }
        return eventListener;
    }

    PDBChangeEventListener getPDBChangeEventListener() {
        return this.pdbChangeListener;
    }

    JMSNotificationListener getJMSListener() {
        return this.jmslistener;
    }

    JMSConsumerExceptionListener getNtfExceptionListener() {
        return this.ntfExceptionListener;
    }

    AQNotificationListener getAQListener() {
        return this.aqlistener;
    }

    DatabaseChangeListener getDCNListener() {
        return this.dcnlistener;
    }

    XSEventListener getXSEventListener() {
        return this.xslistener;
    }

    LogicalTransactionIdEventListener getLogicalTransactionIdEventListener() {
        return this.ltxidlistener;
    }

    protected OracleConnection getConnectionDuringExceptionHandling() {
        return null;
    }
}

