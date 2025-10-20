/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.ByteArray;
import oracle.jdbc.driver.DynamicByteArray;
import oracle.jdbc.driver.PhysicalConnection;
import oracle.jdbc.driver.T4CMAREngine;

final class DirectPathBufferMarshaler {
    private static final byte KPCDP_STR_RHDR_OVERFLOW = -128;
    private static final byte KPCDP_STR_RHDR_ERROR = 64;
    private static final byte KPCDP_STR_RHDR_FRC = 32;
    private static final byte KPCDP_STR_RHDR_FAST = 16;
    private static final byte KPCDP_STR_RHDR_FIRST = 8;
    private static final byte KPCDP_STR_RHDR_LAST = 4;
    private static final byte KPCDP_STR_RHDR_PREV = 2;
    private static final byte KPCDP_STR_RHDR_NEXT = 1;
    private static final byte FRC_HEADER_FLAGS = 60;
    private static final short KDRCSSHC = 250;
    private static final short KDRCSLNG = 254;
    private static final short KDRCSNUL = 255;
    private static final short KPCDP_STR_CLEN_NULL = -1;
    private static final short KPCDP_STR_BYTE_CLEN_NULL = 255;
    private static final short KPCDP_STR_CLEN_FOLLOWS = 254;
    private static final short KPCDP_STR_CLEN_EMPTY = -2;
    private static final short KPCDP_STR_CLEN_ADT = -3;
    private static final short KPCDP_STR_CLEN_ALIGN = -4;
    private static final int KPCDP_STR_CLEN_MAX = 65520;
    private static final short KPCDP_STR_BYTE_CLEN_MAX = 250;
    private static final short KPCDP_STR_SUBTYPE_INDEX_LEN = 2;
    private static final int MAX_PIECE_SIZE = 65520;
    private static final int FAST_HEADER_SIZE = 4;
    private static final int MAX_FAST_DATA = 65516;
    private static final int SLOW_HEADER_SIZE = 2;
    private static final short MAX_PIECE_COLUMNS = 255;
    private static final int MAX_DATA_LENGTH_ENCODING = 3;
    private static final int MAX_DATA_LENGTH = 65513;
    private static final int STREAM_BUFFER_SIZE = 131072;

    private DirectPathBufferMarshaler() {
    }

    static BufferPlanner createBufferPlanner(int n2, int n3, ByteArray byteArray, long[] lArray, int[] nArray, InputStream[][] inputStreamArray, Accessor[] accessorArray, PhysicalConnection physicalConnection) throws IOException {
        DataSegmentSequence dataSegmentSequence = new DataSegmentSequence(n3, n2, (DynamicByteArray)byteArray, nArray, lArray, inputStreamArray, () -> physicalConnection.getByteBuffer(131072), byArray -> physicalConnection.cacheBuffer((byte[])byArray));
        return new BufferPlanner(n2, n3, DirectPathBufferMarshaler.calculateFastColumns(accessorArray), dataSegmentSequence);
    }

    static void marshal(BufferPlanner bufferPlanner, T4CMAREngine t4CMAREngine) throws IOException {
        RowPieceCursor rowPieceCursor = bufferPlanner.cursor();
        while (rowPieceCursor.nextPiece()) {
            int n2;
            DirectPathBufferMarshaler.marshalHeader(rowPieceCursor, t4CMAREngine);
            while (0 <= (n2 = rowPieceCursor.nextData())) {
                if (rowPieceCursor.isDataNull()) {
                    DirectPathBufferMarshaler.marshalNullDataLength(t4CMAREngine);
                } else {
                    DirectPathBufferMarshaler.marshalDataLength(n2, t4CMAREngine);
                }
                rowPieceCursor.writeData(t4CMAREngine);
            }
        }
    }

    private static void marshalHeader(RowPieceCursor rowPieceCursor, T4CMAREngine t4CMAREngine) throws IOException {
        if (rowPieceCursor.isFirst() && rowPieceCursor.isLast() && rowPieceCursor.isFast()) {
            t4CMAREngine.marshalUB1((short)60);
            t4CMAREngine.marshalNativeUB2((short)rowPieceCursor.getRowSize(), false);
        } else {
            int n2 = 0;
            if (rowPieceCursor.isFirst()) {
                n2 = (byte)(n2 | 8);
            } else if (rowPieceCursor.splitsWithPrevious()) {
                n2 = (byte)(n2 | 2);
            }
            if (rowPieceCursor.isLast()) {
                n2 = (byte)(n2 | 4);
            } else if (rowPieceCursor.splitsWithNext()) {
                n2 = (byte)(n2 | 1);
            }
            t4CMAREngine.marshalUB1((short)n2);
        }
        t4CMAREngine.marshalUB1((byte)rowPieceCursor.getDataCount());
    }

    private static void marshalDataLength(int n2, T4CMAREngine t4CMAREngine) throws IOException {
        if (DirectPathBufferMarshaler.sizeOfColumnLength(n2) == 1) {
            t4CMAREngine.marshalUB1((byte)n2);
        } else {
            t4CMAREngine.marshalUB1((short)-2);
            t4CMAREngine.marshalNativeUB2((short)n2, false);
        }
    }

    private static void marshalNullDataLength(T4CMAREngine t4CMAREngine) throws IOException {
        t4CMAREngine.marshalUB1((short)-1);
    }

    private static int sizeOfColumnLength(int n2) {
        return n2 <= 250 ? 1 : 3;
    }

    private static boolean calculateFastColumns(Accessor[] accessorArray) {
        boolean bl = true;
        for (int i2 = 0; bl && i2 < accessorArray.length; ++i2) {
            bl = DirectPathBufferMarshaler.isFastType(accessorArray[i2].describeType);
        }
        return bl;
    }

    private static boolean isFastType(int n2) {
        return n2 == 1 || n2 == 96 || n2 == 178 || n2 == 185 || n2 == 179 || n2 == 186 || n2 == 180 || n2 == 187 || n2 == 231 || n2 == 232 || n2 == 181 || n2 == 188 || n2 == 182 || n2 == 189 || n2 == 183 || n2 == 190 || n2 == 2 || n2 == 12 || n2 == 23 || n2 == 100 || n2 == 101 || n2 == 121;
    }

    private static int[] growAndSet(int[] nArray, int n2, int n3) {
        int[] nArray2 = DirectPathBufferMarshaler.growToIndex(Integer.TYPE, nArray, n2);
        nArray2[n2] = n3;
        return nArray2;
    }

    private static byte[] growAndSet(byte[] byArray, int n2, byte by) {
        byte[] byArray2 = DirectPathBufferMarshaler.growToIndex(Byte.TYPE, byArray, n2);
        byArray2[n2] = by;
        return byArray2;
    }

    private static <T> T growToIndex(Class<?> clazz, T t2, int n2) {
        Object object;
        int n3 = Array.getLength(t2);
        if (n3 > n2) {
            object = t2;
        } else {
            int n4 = Math.max(n2, n3 + (n3 >> 1)) + 1;
            object = Array.newInstance(clazz, n4);
            System.arraycopy(t2, 0, object, 0, n3);
        }
        return object;
    }

    private static class DataSegmentSequence {
        private int dataLimit;
        private int dataIndex;
        private int bindLimit;
        private int bindIndex;
        private final int[] directLengths;
        private final long[] directOffsets;
        private final DynamicByteArray directBindData;
        private int streamLimit;
        private int streamIndex;
        private int[] streamLengths;
        private final Supplier<byte[]> bufferSupplier;
        private final Consumer<byte[]> bufferRecycler;
        private byte[] streamBuffer;
        private int streamBufferReadPos;
        private int streamBufferWritePos;
        private boolean streamBufferIsFull;
        private final InputStream[][] bindStreams;
        private final int columnLimit;
        private final int totalBindCount;
        private int[] splits;
        private int splitLimit;
        private byte[] tempBuf1;

        private DataSegmentSequence(int n2, int n3, DynamicByteArray dynamicByteArray, int[] nArray, long[] lArray, InputStream[][] inputStreamArray, Supplier<byte[]> supplier, Consumer<byte[]> consumer) {
            this.columnLimit = n2;
            this.totalBindCount = n2 * n3;
            this.directBindData = dynamicByteArray;
            this.directLengths = nArray;
            this.directOffsets = lArray;
            this.bindStreams = inputStreamArray;
            this.bufferRecycler = consumer;
            this.bufferSupplier = supplier;
        }

        private int addSegment(int n2) throws IOException {
            int n3;
            if (this.streamBufferIsFull) {
                throw new IllegalStateException("Need to flush data before pushing more.");
            }
            if (this.bindLimit == this.totalBindCount) {
                throw new IllegalStateException("There is no more data to push.");
            }
            if (this.isStream(this.bindLimit)) {
                if (this.readStreamedBind(this.bindLimit, n2)) {
                    ++this.bindLimit;
                } else {
                    this.setSplit(this.dataLimit);
                }
                n3 = this.streamLengths[this.streamLimit - 1];
            } else if ((n3 = this.directLengths[this.bindLimit++]) > n2) {
                throw new UnsupportedOperationException("Splitting direct binds is not supported");
            }
            ++this.dataLimit;
            return n3;
        }

        private boolean isRowPushed(int n2) {
            return this.bindLimit >= this.columnLimit * (n2 + 1);
        }

        private boolean isSplit(int n2) {
            return this.splits != null && Arrays.binarySearch(this.splits, 0, this.splitLimit, this.dataIndex + n2) >= 0;
        }

        private int nextWriteLength() {
            if (this.dataIndex >= this.dataLimit) {
                return -1;
            }
            if (this.isStream(this.bindIndex)) {
                return this.streamLengths[this.streamIndex];
            }
            return this.directLengths[this.bindIndex];
        }

        private boolean isNextNull() {
            return this.directOffsets[this.bindIndex] == -1L;
        }

        private void write(T4CMAREngine t4CMAREngine) throws IOException {
            if (this.dataIndex >= this.dataLimit) {
                throw new IllegalStateException("There are no data segments left to write.");
            }
            if (!this.isNextNull()) {
                if (this.isStream(this.bindIndex)) {
                    this.writeStreamedBind(t4CMAREngine);
                } else {
                    this.writeDirectBind(t4CMAREngine);
                }
            }
            if (!this.isSplit(0)) {
                ++this.bindIndex;
            }
            ++this.dataIndex;
        }

        private boolean isFull() {
            return this.streamBufferIsFull;
        }

        private void writeDirectBind(T4CMAREngine t4CMAREngine) throws IOException {
            this.directBindData.marshalB1Array(t4CMAREngine, this.directOffsets[this.bindIndex], this.directLengths[this.bindIndex]);
        }

        private boolean readStreamedBind(int n2, int n3) throws IOException {
            if (this.streamBuffer == null) {
                this.streamBuffer = this.bufferSupplier.get();
            }
            if (this.streamLengths == null) {
                this.streamLengths = new int[2];
            }
            int n4 = this.streamBufferWritePos;
            InputStream inputStream = this.bindStreams[n2 / this.columnLimit][n2 % this.columnLimit];
            int n5 = Math.min(n3, this.streamBuffer.length - this.streamBufferWritePos);
            if (this.tempBuf1 != null) {
                System.arraycopy(this.tempBuf1, 0, this.streamBuffer, this.streamBufferWritePos++, 1);
                --n5;
                this.tempBuf1 = null;
            }
            int n6 = -1;
            while (n5 > 0 && (n6 = inputStream.read(this.streamBuffer, this.streamBufferWritePos, n5)) != -1) {
                this.streamBufferWritePos += n6;
                n5 -= n6;
            }
            this.streamBufferIsFull = this.streamBuffer.length == this.streamBufferWritePos;
            this.streamLengths = DirectPathBufferMarshaler.growAndSet(this.streamLengths, this.streamLimit++, this.streamBufferWritePos - n4);
            if (n6 < 0) {
                return true;
            }
            int n7 = inputStream.read();
            if (n7 < 0) {
                return true;
            }
            this.tempBuf1 = new byte[]{(byte)n7};
            return false;
        }

        private void writeStreamedBind(T4CMAREngine t4CMAREngine) throws IOException {
            int n2 = this.streamLengths[this.streamIndex];
            t4CMAREngine.marshalB1Array(this.streamBuffer, this.streamBufferReadPos, n2);
            this.streamBufferReadPos += n2;
            if (++this.streamIndex == this.streamLimit) {
                this.resetStreamBuffer();
            }
        }

        private boolean isStream(int n2) {
            if (this.bindStreams == null) {
                return false;
            }
            int n3 = n2 / this.columnLimit;
            int n4 = n2 % this.columnLimit;
            return this.bindStreams.length > n3 && this.bindStreams[n3] != null && this.bindStreams[n3].length > n4 && this.bindStreams[n3][n4] != null;
        }

        private void setSplit(int n2) {
            if (this.splits == null) {
                this.splits = new int[2];
            }
            this.splits = DirectPathBufferMarshaler.growAndSet(this.splits, this.splitLimit++, n2);
        }

        private void resetStreamBuffer() {
            this.bufferRecycler.accept(this.streamBuffer);
            this.streamBuffer = null;
            this.streamBufferReadPos = 0;
            this.streamBufferWritePos = 0;
            this.streamBufferIsFull = false;
            this.streamIndex = 0;
            this.streamLimit = 0;
        }
    }

    private static class RowPieceCursor {
        private int rowIndex;
        private int pieceIndex;
        private int pieceIndexOfRow;
        private boolean isFirstPieceOfRow;
        private boolean isLastPieceOfRow;
        private int dataIndex;
        private int dataLimit;
        private boolean splitFromPrevious;
        private boolean splitToNext;
        private final BufferPlanner rowPiecePlan;
        private final DataSegmentSequence dataStream;

        private RowPieceCursor(BufferPlanner bufferPlanner, DataSegmentSequence dataSegmentSequence) {
            this.rowPiecePlan = bufferPlanner;
            this.dataStream = dataSegmentSequence;
            this.dataIndex = -1;
            this.dataLimit = 0;
            this.pieceIndex = -1;
            this.isLastPieceOfRow = true;
            this.rowIndex = -1;
        }

        private boolean nextPiece() {
            if (this.dataIndex + 1 != this.dataLimit) {
                throw new IllegalStateException("Unwritten data remains for the current piece.");
            }
            if (this.pieceIndex + 1 < this.rowPiecePlan.getPieceCount()) {
                ++this.pieceIndex;
                this.isFirstPieceOfRow = this.isLastPieceOfRow;
                if (this.isFirstPieceOfRow) {
                    ++this.rowIndex;
                    this.pieceIndexOfRow = 0;
                } else {
                    ++this.pieceIndexOfRow;
                }
                this.isLastPieceOfRow = this.pieceIndexOfRow + 1 == this.rowPiecePlan.getPieceCount(this.rowIndex) && this.dataStream.isRowPushed(this.rowIndex);
                this.dataIndex = -1;
                this.dataLimit = this.rowPiecePlan.getDataCount(this.pieceIndex);
                this.splitFromPrevious = this.splitToNext;
                this.splitToNext = this.dataStream.isSplit(this.dataLimit - 1);
                return true;
            }
            this.pieceIndexOfRow = -1;
            return false;
        }

        private int nextData() {
            if (this.dataIndex + 1 < this.dataLimit) {
                ++this.dataIndex;
                return this.dataStream.nextWriteLength();
            }
            return -1;
        }

        private boolean isDataNull() {
            return this.dataStream.isNextNull();
        }

        private void writeData(T4CMAREngine t4CMAREngine) throws IOException {
            if (this.dataIndex >= this.dataLimit) {
                throw new IllegalStateException("No remaining data to write for the current piece.");
            }
            this.dataStream.write(t4CMAREngine);
        }

        private boolean isFirst() {
            return this.isFirstPieceOfRow;
        }

        private boolean isLast() {
            return this.isLastPieceOfRow;
        }

        private boolean isFast() {
            return this.rowPiecePlan.allFastTypes();
        }

        private int getDataCount() {
            return this.dataLimit;
        }

        private int getRowSize() {
            return this.rowPiecePlan.rowSizes[this.rowIndex];
        }

        private boolean splitsWithPrevious() {
            return this.splitFromPrevious;
        }

        private boolean splitsWithNext() {
            return this.splitToNext;
        }
    }

    static class BufferPlanner {
        private final boolean allFastTypes;
        private final int[] pieceCounts;
        private final int[] rowSizes;
        private final DataSegmentSequence dataSequence;
        private int totalPieceCount;
        private int pushBytesRemaining;
        private int pushedBytesTotal;
        private byte[] dataCounts;
        private int rowLimit;
        private final int totalRows;
        private RowPieceCursor cursor;

        private BufferPlanner(int n2, int n3, boolean bl, DataSegmentSequence dataSegmentSequence) {
            this.totalRows = n2;
            this.allFastTypes = bl;
            this.dataSequence = dataSegmentSequence;
            this.pieceCounts = new int[n2];
            this.rowSizes = new int[n2];
            this.dataCounts = new byte[n2 + 1];
        }

        int preparePlan() throws IOException {
            this.resetPushState();
            while (this.pushData() || this.pushPiece() || this.pushRow()) {
            }
            return this.pushedBytesTotal;
        }

        boolean isComplete() {
            return this.rowLimit == this.totalRows;
        }

        int getRowByOffset(int n2, int n3) {
            int n4 = 0;
            if ((n2 != 0 || n3 != 0) && this.rowSizes != null && this.rowSizes.length > 0) {
                int n5 = 0;
                int n6 = 0;
                for (int i2 = 0; i2 < this.rowSizes.length; ++i2) {
                    if (n2 >= n5 && n3 <= (n6 += this.rowSizes[i2])) {
                        n4 = i2 + 1;
                        break;
                    }
                    n5 = n6;
                }
            }
            return n4;
        }

        private RowPieceCursor cursor() {
            if (this.cursor == null) {
                this.cursor = new RowPieceCursor(this, this.dataSequence);
            }
            return this.cursor;
        }

        private boolean pushData() throws IOException {
            int n2 = this.getDataCount(this.totalPieceCount);
            if (this.dataSequence.isRowPushed(this.rowLimit)) {
                return false;
            }
            if (n2 == 255) {
                return false;
            }
            if (this.dataSequence.isFull()) {
                return false;
            }
            int n3 = this.dataSequence.addSegment(65513);
            int n4 = DirectPathBufferMarshaler.sizeOfColumnLength(n3) + n3;
            if (n4 > this.pushBytesRemaining) {
                if (n2 > 0) {
                    this.pushPiece();
                } else {
                    throw new IllegalStateException("Data will not fit in an empty piece.");
                }
            }
            this.pushBytesRemaining -= n4;
            int n5 = this.rowLimit;
            this.rowSizes[n5] = this.rowSizes[n5] + n4;
            int n6 = this.totalPieceCount;
            this.dataCounts[n6] = (byte)(this.dataCounts[n6] + 1);
            return true;
        }

        private boolean pushPiece() {
            if (this.getDataCount(this.totalPieceCount) > 0) {
                int n2 = this.rowLimit;
                this.pieceCounts[n2] = this.pieceCounts[n2] + 1;
                this.dataCounts = (byte[])DirectPathBufferMarshaler.growToIndex(Byte.TYPE, this.dataCounts, ++this.totalPieceCount);
            }
            this.pushBytesRemaining = 65516;
            return !this.dataSequence.isFull() && !this.dataSequence.isRowPushed(this.rowLimit);
        }

        private boolean pushRow() {
            boolean bl = this.dataSequence.isRowPushed(this.rowLimit);
            int n2 = this.pieceCounts[this.rowLimit];
            if (n2 > 0) {
                if (n2 == 1 && bl && this.allFastTypes) {
                    int n3 = this.rowLimit;
                    this.rowSizes[n3] = this.rowSizes[n3] + 4;
                } else {
                    int n4 = this.rowLimit;
                    this.rowSizes[n4] = this.rowSizes[n4] + 2 * n2;
                }
                this.pushedBytesTotal += this.rowSizes[this.rowLimit];
            }
            if (bl) {
                ++this.rowLimit;
            }
            return this.rowLimit != this.totalRows && !this.dataSequence.isFull();
        }

        private void resetPushState() {
            this.pushedBytesTotal = 0;
            this.pushBytesRemaining = 65516;
            if (this.rowLimit >= 0) {
                this.pieceCounts[this.rowLimit] = 0;
                this.rowSizes[this.rowLimit] = 0;
            }
        }

        private boolean allFastTypes() {
            return this.allFastTypes;
        }

        private int getDataCount(int n2) {
            return this.dataCounts[n2] & 0xFF;
        }

        private int getPieceCount(int n2) {
            return this.pieceCounts[n2];
        }

        private int getPieceCount() {
            return this.totalPieceCount;
        }
    }
}

