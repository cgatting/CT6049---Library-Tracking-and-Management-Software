/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.resource.NotSupportedException
 *  javax.resource.ResourceException
 *  javax.resource.spi.ActivationSpec
 *  javax.resource.spi.BootstrapContext
 *  javax.resource.spi.ResourceAdapter
 *  javax.resource.spi.ResourceAdapterInternalException
 *  javax.resource.spi.endpoint.MessageEndpointFactory
 */
package oracle.jdbc.connector;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONN_MANAGEMENT})
public class OracleResourceAdapter
implements ResourceAdapter {
    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
    }

    public void stop() {
    }

    public void endpointActivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) throws NotSupportedException {
    }

    public void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) {
    }

    public XAResource[] getXAResources(ActivationSpec[] activationSpecArray) throws ResourceException {
        return new XAResource[0];
    }
}

