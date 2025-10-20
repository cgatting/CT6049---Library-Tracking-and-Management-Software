/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.datasource.impl;

import oracle.jdbc.OraclePooledConnectionBuilder;
import oracle.jdbc.datasource.OraclePooledConnection;
import oracle.jdbc.internal.AbstractConnectionBuilder;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.CONN_POOL})
public abstract class OraclePooledConnectionBuilderImpl
extends AbstractConnectionBuilder<OraclePooledConnectionBuilderImpl, OraclePooledConnection>
implements OraclePooledConnectionBuilder {
}

