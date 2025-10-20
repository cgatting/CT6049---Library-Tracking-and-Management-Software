/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class LobCommonAccessor
extends Accessor {
    long[] prefetchedDataOffset;
    int[] prefetchedDataLength;
    long[] prefetchedLength;
    int[] prefetchedChunkSize;

    LobCommonAccessor(Representation representation, OracleStatement oracleStatement, int n2, boolean bl) {
        super(representation, oracleStatement, n2, bl);
    }

    @Override
    void setNull(int n2, boolean bl) throws SQLException {
        super.setNull(n2, bl);
        if (bl && this.isPrefetched()) {
            this.prefetchedDataOffset[n2] = -1L;
            this.prefetchedDataLength[n2] = -1;
            this.prefetchedLength[n2] = -1L;
            this.prefetchedChunkSize[n2] = -1;
        }
    }

    @Override
    void setCapacity(int n2) {
        super.setCapacity(n2);
        if (this.isPrefetched() && this.prefetchedDataOffset == null) {
            this.prefetchedDataOffset = new long[n2];
            this.prefetchedDataLength = new int[n2];
            this.prefetchedLength = new long[n2];
            this.prefetchedChunkSize = new int[n2];
        } else if (this.isPrefetched() && n2 > this.prefetchedDataOffset.length) {
            Object[] objectArray = new long[n2];
            System.arraycopy(this.prefetchedDataOffset, 0, objectArray, 0, this.prefetchedDataOffset.length);
            this.prefetchedDataOffset = objectArray;
            objectArray = new int[n2];
            System.arraycopy(this.prefetchedDataLength, 0, objectArray, 0, this.prefetchedDataLength.length);
            this.prefetchedDataLength = (int[])objectArray;
            objectArray = new long[n2];
            System.arraycopy(this.prefetchedLength, 0, objectArray, 0, this.prefetchedLength.length);
            this.prefetchedLength = objectArray;
            objectArray = new int[n2];
            System.arraycopy(this.prefetchedChunkSize, 0, objectArray, 0, this.prefetchedChunkSize.length);
            this.prefetchedChunkSize = (int[])objectArray;
        }
    }

    @Override
    void insertNull(int n2) throws SQLException {
        if (this.isPrefetched()) {
            System.arraycopy(this.prefetchedDataOffset, n2, this.prefetchedDataOffset, n2 + 1, this.prefetchedDataOffset.length - n2 - 1);
            System.arraycopy(this.prefetchedDataLength, n2, this.prefetchedDataLength, n2 + 1, this.prefetchedDataLength.length - n2 - 1);
            System.arraycopy(this.prefetchedLength, n2, this.prefetchedLength, n2 + 1, this.prefetchedLength.length - n2 - 1);
            System.arraycopy(this.prefetchedChunkSize, n2, this.prefetchedChunkSize, n2 + 1, this.prefetchedChunkSize.length - n2 - 1);
        }
        super.insertNull(n2);
    }

    @Override
    Accessor copyForDefine(OracleStatement oracleStatement) {
        LobCommonAccessor lobCommonAccessor = (LobCommonAccessor)super.copyForDefine(oracleStatement);
        lobCommonAccessor.prefetchedDataOffset = null;
        lobCommonAccessor.prefetchedDataLength = null;
        lobCommonAccessor.prefetchedLength = null;
        lobCommonAccessor.prefetchedChunkSize = null;
        return lobCommonAccessor;
    }

    @Override
    protected void copyFromInternal(Accessor accessor, int n2, int n3) throws SQLException {
        super.copyFromInternal(accessor, n2, n3);
        if (this.isPrefetched()) {
            long l2;
            LobCommonAccessor lobCommonAccessor = (LobCommonAccessor)accessor;
            assert (lobCommonAccessor.isPrefetched()) : "srcLobAcc is not prefetched";
            int n4 = lobCommonAccessor.getPrefetchedDataLength(n2);
            long l3 = l2 = n4 <= this.getPrefetchedDataLength(n3) ? this.getPrefetchedDataOffset(n3) : this.statement.allocateRowDataSpace(n4);
            if (n4 > 0) {
                this.rowData.put(l2, lobCommonAccessor.rowData, lobCommonAccessor.getPrefetchedDataOffset(n2), n4);
            }
            this.setPrefetchedDataOffset(n3, l2);
            this.setPrefetchedDataLength(n3, n4);
            this.setPrefetchedLength(n3, lobCommonAccessor.getPrefetchedLength(n2));
            this.setPrefetchedChunkSize(n3, lobCommonAccessor.getPrefetchedChunkSize(n2));
        }
    }

    @Override
    void deleteRow(int n2) throws SQLException {
        super.deleteRow(n2);
        if (this.isPrefetched()) {
            this.rowData.freeSpace(this.getPrefetchedDataOffset(n2), this.getPrefetchedDataLength(n2));
            this.delete(this.prefetchedDataOffset, n2);
            this.delete(this.prefetchedDataLength, n2);
            this.delete(this.prefetchedLength, n2);
            this.delete(this.prefetchedChunkSize, n2);
        }
    }

    final boolean isPrefetched() {
        return !this.isDMLReturnedParam && this.lobPrefetchSizeForThisColumn > -1;
    }

    @Override
    void setNoPrefetch() {
        this.lobPrefetchSizeForThisColumn = -1;
        this.prefetchedDataOffset = null;
        this.prefetchedDataLength = null;
        this.prefetchedLength = null;
        this.prefetchedChunkSize = null;
    }

    final int getPrefetchLength() {
        return this.lobPrefetchSizeForThisColumn;
    }

    @Override
    void setPrefetchLength(int n2) {
        if (n2 == -1) {
            this.setNoPrefetch();
        } else {
            this.lobPrefetchSizeForThisColumn = n2;
            if (this.rowNull != null) {
                this.setCapacity(this.rowNull.length);
            }
        }
    }

    final void setPrefetchedDataOffset(int n2) {
        assert (this.prefetchedDataOffset.length > n2) : "prefetchedDataOffset.length: " + this.prefetchedDataOffset.length + " currentRow: " + n2;
        this.prefetchedDataOffset[n2] = this.rowData.getPosition();
    }

    final void setPrefetchedDataOffset(int n2, long l2) {
        assert (this.prefetchedDataOffset.length > n2) : "prefetchedDataOffset.length: " + this.prefetchedDataOffset.length + " currentRow: " + n2;
        this.prefetchedDataOffset[n2] = l2;
    }

    final void setPrefetchedDataLength(int n2, int n3) {
        assert (this.prefetchedDataLength.length > n2) : "prefetchedDataLength.length: " + this.prefetchedDataLength.length + " currentRow: " + n2;
        this.prefetchedDataLength[n2] = n3;
    }

    final void setPrefetchedLength(int n2, long l2) {
        assert (this.prefetchedLength.length > n2) : "prefetchedLength.length: " + this.prefetchedLength.length + " currentRow: " + n2;
        this.prefetchedLength[n2] = l2;
    }

    final void setPrefetchedChunkSize(int n2, int n3) {
        assert (this.prefetchedChunkSize.length > n2) : "prefetchedChunkSize.length: " + this.prefetchedChunkSize.length + " currentRow: " + n2;
        this.prefetchedChunkSize[n2] = n3;
    }

    final long getPrefetchedDataOffset(int n2) {
        return this.prefetchedDataOffset[n2];
    }

    final int getPrefetchedDataLength(int n2) {
        return this.prefetchedDataLength[n2];
    }

    final long getPrefetchedLength(int n2) {
        return this.prefetchedLength[n2];
    }

    final int getPrefetchedChunkSize(int n2) {
        return this.prefetchedChunkSize[n2];
    }

    final byte[] getPrefetchedData(int n2) {
        if (this.getPrefetchLength() > -1) {
            if (this.getPrefetchedDataLength(n2) == 0) {
                return new byte[0];
            }
            return this.rowData.get(this.getPrefetchedDataOffset(n2), this.getPrefetchedDataLength(n2));
        }
        return null;
    }

    @Override
    Object getObject(int n2) throws SQLException {
        return this.getOracleObject(n2);
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.getOracleObject(n2);
    }
}

