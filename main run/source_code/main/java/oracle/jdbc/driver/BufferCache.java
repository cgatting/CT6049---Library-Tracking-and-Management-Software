/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import oracle.jdbc.internal.OracleConnection;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.DisableTrace;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
class BufferCache<T> {
    private static final double ln2 = Math.log(2.0);
    private static final int BUFFERS_PER_BUCKET = 8;
    private static final int MIN_INDEX = 12;
    private final InternalStatistics stats;
    private final int[] bufferSize;
    private final SoftReference<T>[][] buckets;
    private final int[] top;

    BufferCache(int n2) {
        int n3 = n2 < 31 ? n2 : (int)Math.ceil(Math.log(n2) / ln2);
        int n4 = Math.max(0, n3 - 12 + 1);
        this.buckets = new SoftReference[n4][8];
        this.top = new int[n4];
        this.bufferSize = new int[n4];
        int n5 = 4096;
        for (int i2 = 0; i2 < this.bufferSize.length; ++i2) {
            this.bufferSize[i2] = n5;
            n5 <<= 1;
        }
        this.stats = new InternalStatistics(this.bufferSize);
    }

    T get(Class<?> clazz, int n2) {
        int n3 = this.bufferIndex(n2);
        if (n3 >= this.buckets.length) {
            this.stats.requestTooBig();
            return (T)Array.newInstance(clazz, n2);
        }
        while (this.top[n3] > 0) {
            int n4 = n3;
            int n5 = this.top[n4] - 1;
            this.top[n4] = n5;
            SoftReference<T> softReference = this.buckets[n3][n5];
            this.buckets[n3][this.top[n3]] = null;
            T t2 = softReference.get();
            if (t2 == null) continue;
            this.stats.cacheHit(n3);
            return t2;
        }
        this.stats.cacheMiss(n3);
        return (T)Array.newInstance(clazz, this.bufferSize[n3]);
    }

    void put(T t2) {
        int n2 = Array.getLength(t2);
        int n3 = this.bufferIndex(n2);
        if (n3 >= this.buckets.length || n2 != this.bufferSize[n3]) {
            this.stats.cacheTooBig();
            return;
        }
        if (this.top[n3] < 8) {
            this.stats.bufferCached(n3);
            int n4 = n3;
            int n5 = this.top[n4];
            this.top[n4] = n5 + 1;
            this.buckets[n3][n5] = new SoftReference<T>(t2);
        } else {
            int n6 = this.top[n3];
            while (n6 > 0) {
                if (this.buckets[n3][--n6].get() != null) continue;
                this.stats.refCleared(n3);
                this.buckets[n3][n6] = new SoftReference<T>(t2);
                return;
            }
            this.stats.bucketFull(n3);
        }
    }

    OracleConnection.BufferCacheStatistics getStatistics() {
        return this.stats;
    }

    private int bufferIndex(int n2) {
        for (int i2 = 0; i2 < this.bufferSize.length; ++i2) {
            if (n2 > this.bufferSize[i2]) continue;
            return i2;
        }
        return Integer.MAX_VALUE;
    }

    private static final class InternalStatistics
    implements OracleConnection.BufferCacheStatistics {
        private static int CACHE_COUNT = 0;
        private final int cacheId = ++CACHE_COUNT;
        private final int[] sizes;
        private final int[] nCacheHit;
        private final int[] nCacheMiss;
        private int nRequestTooBig;
        private final int[] nBufferCached;
        private final int[] nBucketFull;
        private final int[] nRefCleared;
        private int nCacheTooBig;

        InternalStatistics(int[] nArray) {
            this.sizes = nArray;
            int n2 = nArray.length;
            this.nCacheHit = new int[n2];
            this.nCacheMiss = new int[n2];
            this.nRequestTooBig = 0;
            this.nBufferCached = new int[n2];
            this.nBucketFull = new int[n2];
            this.nRefCleared = new int[n2];
            this.nCacheTooBig = 0;
        }

        void cacheHit(int n2) {
            int n3 = n2;
            this.nCacheHit[n3] = this.nCacheHit[n3] + 1;
        }

        void cacheMiss(int n2) {
            int n3 = n2;
            this.nCacheMiss[n3] = this.nCacheMiss[n3] + 1;
        }

        void requestTooBig() {
            ++this.nRequestTooBig;
        }

        void bufferCached(int n2) {
            int n3 = n2;
            this.nBufferCached[n3] = this.nBufferCached[n3] + 1;
        }

        void bucketFull(int n2) {
            int n3 = n2;
            this.nBucketFull[n3] = this.nBucketFull[n3] + 1;
        }

        void refCleared(int n2) {
            int n3 = n2;
            this.nRefCleared[n3] = this.nRefCleared[n3] + 1;
        }

        void cacheTooBig() {
            ++this.nCacheTooBig;
        }

        @Override
        public int getId() {
            return this.cacheId;
        }

        @Override
        public int[] getBufferSizes() {
            int[] nArray = new int[this.sizes.length];
            System.arraycopy(this.sizes, 0, nArray, 0, this.sizes.length);
            return nArray;
        }

        @Override
        public int getCacheHits(int n2) {
            return this.nCacheHit[n2];
        }

        @Override
        public int getCacheMisses(int n2) {
            return this.nCacheMiss[n2];
        }

        public int getRequestsTooBig() {
            return this.nRequestTooBig;
        }

        @Override
        public int getBuffersCached(int n2) {
            return this.nBufferCached[n2];
        }

        @Override
        public int getBucketsFull(int n2) {
            return this.nBucketFull[n2];
        }

        @Override
        public int getReferencesCleared(int n2) {
            return this.nRefCleared[n2];
        }

        @Override
        public int getTooBigToCache() {
            return this.nCacheTooBig;
        }

        @DisableTrace
        public String toString() {
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            for (int i2 = 0; i2 < this.sizes.length; ++i2) {
                n2 += this.nCacheHit[i2];
                n3 += this.nCacheMiss[i2];
                n4 += this.nBufferCached[i2];
                n5 += this.nBucketFull[i2];
                n6 += this.nRefCleared[i2];
            }
            String string = "oracle.jdbc.driver.BufferCache<" + this.cacheId + ">\n\tTotal Hits   :\t" + n2 + "\n\tTotal Misses :\t" + (n3 + this.nRequestTooBig) + "\n\tTotal Cached :\t" + n4 + "\n\tTotal Dropped:\t" + (n5 + this.nCacheTooBig) + "\n\tTotal Cleared:\t" + n6 + "\n";
            return string;
        }
    }
}

