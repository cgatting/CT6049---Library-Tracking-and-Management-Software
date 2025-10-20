/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.driver.OracleClob;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.CLOB;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.LOB_DATUM})
@Deprecated
public class OracleNClob
extends OracleClob
implements oracle.jdbc.internal.OracleNClob {
    protected OracleNClob() {
    }

    public OracleNClob(OracleConnection oracleConnection) throws SQLException {
        this(oracleConnection, null);
    }

    public OracleNClob(OracleConnection oracleConnection, byte[] byArray) throws SQLException {
        super(oracleConnection, byArray, (short)2);
    }

    public OracleNClob(CLOB cLOB) throws SQLException {
        this(cLOB.getPhysicalConnection(), cLOB.getBytes());
    }
}

