/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import oracle.jdbc.driver.InsensitiveScrollableResultSet;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.internal.Monitor;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class ForwardOnlyResultSet
extends InsensitiveScrollableResultSet {
    ForwardOnlyResultSet(PhysicalConnection physicalConnection, OracleStatement oracleStatement) throws SQLException {
        super(physicalConnection, oracleStatement);
    }

    @Override
    protected boolean isForwardOnly() {
        return true;
    }

    @Override
    public int getType() throws SQLException {
        this.ensureOpen("getType");
        return 1003;
    }

    @Override
    boolean isComplete() throws SQLException {
        return this.closed || this.isEmptyResultSet() || this.currentRow == this.fetchedRowCount;
    }

    @Override
    public void close() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.closed) {
                return;
            }
            this.statement.cleanAllRowLobs();
            super.close();
        }
    }
}

