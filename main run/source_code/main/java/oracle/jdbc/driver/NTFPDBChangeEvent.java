/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.PDBChangeEvent;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
public class NTFPDBChangeEvent
implements PDBChangeEvent {
    OracleConnection conn;

    NTFPDBChangeEvent(OracleConnection oracleConnection) {
        this.conn = oracleConnection;
    }

    @Override
    public OracleConnection getConnection() {
        return this.conn;
    }
}

