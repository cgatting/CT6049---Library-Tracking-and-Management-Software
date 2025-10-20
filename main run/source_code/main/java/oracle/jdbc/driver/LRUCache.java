/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import oracle.jdbc.driver.Chain;

final class LRUCache<T> {
    private Chain.Link<T>[] vacant = new Chain.Link[]{null};
    private int highWaterMark = 0;
    private final boolean implicit;
    private static final int NSTMTS = 3;
    private static final int MRSETS = 6;
    private final Map<String, Chain<T>>[] cache;
    private Chain<T> history = new Chain<T>(this.vacant, this.highWaterMark);

    LRUCache(boolean bl) {
        this.implicit = bl;
        this.cache = new Map[bl ? 18 : 1];
        for (int i2 = 0; i2 < this.cache.length; ++i2) {
            this.cache[i2] = null;
        }
    }

    void vacancy(int n2) {
        if (n2 > 1) {
            this.vacant = new Chain.Link[Math.min(n2, 1000)];
            this.highWaterMark = 0;
        }
    }

    T removeMostRecent(int n2, int n3, String string) {
        int n4;
        int n5 = n4 = this.implicit ? n2 * 3 + n3 : 0;
        if (this.cache[n4] != null) {
            Chain<T> chain = this.cache[n4].get(string);
            return chain == null ? null : (T)chain.removeHead();
        }
        return null;
    }

    T removeLeastRecent() {
        return this.history.removeTail();
    }

    void add(T t2, int n3, int n4, String string2) {
        assert (Objects.nonNull(t2));
        assert (Objects.nonNull(string2));
        int n5 = this.implicit ? n3 * 3 + n4 : 0;
        Map map = Objects.isNull(this.cache[n5]) ? new HashMap<String, Chain<T>>() : this.cache[n5];
        IntConsumer intConsumer = n2 -> {
            if (0 == n2) {
                map.remove(string2);
            }
        };
        Function<String, Chain> function = string -> new Chain<T>(this.vacant, this.highWaterMark, intConsumer);
        Chain chain = map.computeIfAbsent(string2, function);
        Chain.addHead(t2, chain, this.history);
    }

    int size() {
        return this.history.size();
    }

    void forEach(Consumer<? super T> consumer) {
        this.history.forEach(consumer);
    }

    public String toString() {
        return "cache=" + Arrays.deepToString(this.cache) + ", history=" + this.history.toString();
    }

    void close() {
        for (int i2 = 0; i2 < this.cache.length; ++i2) {
            if (this.cache[i2] == null) continue;
            this.cache[i2].clear();
            this.cache[i2] = null;
        }
        this.history = null;
        this.vacant = null;
        this.highWaterMark = 0;
    }
}

