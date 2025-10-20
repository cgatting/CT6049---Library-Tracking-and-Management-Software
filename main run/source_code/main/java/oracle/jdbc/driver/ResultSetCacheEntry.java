/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.AccessorPrototype;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.ResultSetCacheEntryKey;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
final class ResultSetCacheEntry {
    private final AtomicReference<QueryResultState> queryResultState = new AtomicReference<QueryResultState>(QueryResultState.FETCHING);
    private int numRows = -1;
    private ByteArray rowData = null;
    private AccessorPrototype[] accessorPrototypes = null;
    String userName;
    ResultSetCacheEntryKey key;
    long queryId;
    long sizeInMemory;

    ResultSetCacheEntry(ResultSetCacheEntryKey resultSetCacheEntryKey, long l2) {
        assert (l2 != 0L);
        this.key = resultSetCacheEntryKey;
        this.queryId = l2;
    }

    ResultSetCacheEntryKey getResultSetCacheEntryKey() {
        return this.key;
    }

    void initialize(int n2, ByteArray byteArray, Accessor[] accessorArray, long l2) throws SQLException {
        assert (this.queryResultState.get() != QueryResultState.VALID) : "queryResultState: " + this.queryResultState;
        assert (n2 >= 0) : "_numRows: " + n2;
        assert (byteArray != null) : "null _rowData";
        assert (accessorArray != null) : "null _accessors";
        if (this.queryResultState.get() == QueryResultState.INVALID) {
            return;
        }
        this.numRows = n2;
        this.rowData = byteArray;
        this.accessorPrototypes = new AccessorPrototype[accessorArray.length];
        for (int i2 = 0; i2 < accessorArray.length; ++i2) {
            assert (accessorArray[i2] != null) : "null _accessor: " + i2;
            this.accessorPrototypes[i2] = accessorArray[i2].newPrototype(this.numRows);
        }
        this.sizeInMemory = l2;
        this.queryResultState.compareAndSet(QueryResultState.FETCHING, QueryResultState.VALID);
    }

    boolean isFetching() {
        return this.queryResultState.get() == QueryResultState.FETCHING;
    }

    boolean isValid() {
        return this.queryResultState.get() == QueryResultState.VALID;
    }

    boolean isInvalid() {
        return this.queryResultState.get() == QueryResultState.INVALID;
    }

    void invalidate() {
        this.queryResultState.set(QueryResultState.INVALID);
    }

    int getNumberOfRows() {
        assert (this.queryResultState.get() != QueryResultState.FETCHING) : "queryResultState: " + this.queryResultState;
        return this.numRows;
    }

    ByteArray getRowData() {
        assert (this.queryResultState.get() != QueryResultState.FETCHING) : "queryResultState: " + this.queryResultState;
        return this.rowData;
    }

    long getQueryId() {
        return this.queryId;
    }

    long getSizeInMemory() {
        return this.sizeInMemory;
    }

    Accessor[] newAccessors(OracleStatement oracleStatement) throws SQLException {
        assert (this.queryResultState.get() != QueryResultState.FETCHING) : "queryResultState: " + this.queryResultState;
        assert (oracleStatement != null) : "null stmt";
        Accessor[] accessorArray = new Accessor[this.accessorPrototypes.length];
        for (int i2 = 0; i2 < this.accessorPrototypes.length; ++i2) {
            accessorArray[i2] = this.accessorPrototypes[i2].newAccessor(oracleStatement);
        }
        return accessorArray;
    }

    static enum QueryResultState {
        FETCHING,
        VALID,
        INVALID;

    }
}

