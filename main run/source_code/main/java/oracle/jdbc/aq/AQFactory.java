/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.aq;

import java.sql.SQLException;
import oracle.jdbc.aq.AQAgent;
import oracle.jdbc.aq.AQMessage;
import oracle.jdbc.aq.AQMessageProperties;
import oracle.jdbc.driver.InternalFactory;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.ADVANCED_QUEUING})
public abstract class AQFactory {
    public static AQMessage createAQMessage(AQMessageProperties aQMessageProperties) throws SQLException {
        return InternalFactory.createAQMessage(aQMessageProperties);
    }

    public static AQMessageProperties createAQMessageProperties() throws SQLException {
        return InternalFactory.createAQMessageProperties();
    }

    public static AQAgent createAQAgent() throws SQLException {
        return InternalFactory.createAQAgent();
    }
}

