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
class SensitiveScrollableResultSet
extends InsensitiveScrollableResultSet {
    protected long beginLastFetchedIndex;
    protected long endLastFetchedIndex;

    SensitiveScrollableResultSet(PhysicalConnection physicalConnection, OracleStatement oracleStatement) throws SQLException {
        super(physicalConnection, oracleStatement);
        if (this.fetchedRowCount > 0L) {
            this.beginLastFetchedIndex = 0L;
            this.endLastFetchedIndex = this.fetchedRowCount - 1L;
        } else {
            this.beginLastFetchedIndex = -1L;
            this.endLastFetchedIndex = -1L;
        }
    }

    @Override
    public int getType() throws SQLException {
        this.ensureOpen("getType");
        return 1005;
    }

    @Override
    public boolean next() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (super.next()) {
                this.handleRefetch();
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    @Override
    public boolean first() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (super.first()) {
                this.handleRefetch();
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    @Override
    public boolean last() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (super.last()) {
                this.handleRefetch();
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    @Override
    public boolean absolute(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (super.absolute(n2)) {
                this.handleRefetch();
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    @Override
    public boolean relative(int n2) throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (super.relative(n2)) {
                this.handleRefetch();
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    @Override
    public boolean previous() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (super.previous()) {
                this.handleRefetch();
                boolean bl = true;
                return bl;
            }
            boolean bl = false;
            return bl;
        }
    }

    @Override
    int refreshRows(long l2, int n2) throws SQLException {
        int n3 = super.refreshRows(l2, n2);
        if (n3 != 0) {
            this.beginLastFetchedIndex = l2;
            this.endLastFetchedIndex = l2 + (long)n3 - 1L;
        }
        return n3;
    }

    @Override
    void removeCurrentRowFromCache() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            long l2 = this.currentRow;
            super.removeCurrentRowFromCache();
            if (!this.isEmptyResultSet()) {
                if (l2 < this.beginLastFetchedIndex) {
                    --this.beginLastFetchedIndex;
                }
                if (l2 <= this.endLastFetchedIndex) {
                    --this.endLastFetchedIndex;
                }
                if (!this.isAfterLast()) {
                    this.handleRefetch();
                }
            }
        }
    }

    protected boolean handleRefetch() throws SQLException {
        try (Monitor.CloseableLock closeableLock = this.connection.acquireCloseableLock();){
            if (this.beginLastFetchedIndex <= this.currentRow && this.currentRow <= this.endLastFetchedIndex) {
                boolean bl = false;
                return bl;
            }
            this.refreshRow();
            boolean bl = true;
            return bl;
        }
    }
}

