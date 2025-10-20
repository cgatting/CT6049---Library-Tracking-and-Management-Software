/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

final class Chain<T> {
    private final Link<T>[] vacant;
    private int highWaterMark;
    private Link<T> head = null;
    private Link<T> tail = null;
    private int size = 0;
    private final IntConsumer sizeChangeTrigger;

    Chain(Link<T>[] linkArray, int n2, IntConsumer intConsumer) {
        this.vacant = linkArray;
        this.highWaterMark = n2;
        this.sizeChangeTrigger = intConsumer;
    }

    Chain(Link<T>[] linkArray, int n3) {
        this(linkArray, n3, n2 -> {});
    }

    static <T> void addHead(T t2, Chain<T> ... chainArray) {
        Link<T> link;
        if (chainArray.length == 0) {
            link = null;
        } else {
            Link<T>[] linkArray = chainArray[0].vacant;
            int n2 = chainArray[0].highWaterMark;
            n2 = Math.max(0, n2 - 1);
            link = linkArray[n2];
            linkArray[n2] = null;
        }
        if (link == null) {
            new Link((Object)t2, (Chain[])chainArray);
        } else {
            ((Link)link).link(t2, chainArray);
        }
    }

    T removeHead() {
        return this.remove(this.head);
    }

    T removeTail() {
        return this.remove(this.tail);
    }

    private T remove(Link<T> link) {
        if (link == null) {
            return null;
        }
        this.highWaterMark = Math.min(this.highWaterMark + 1, this.vacant.length - 1);
        this.vacant[this.highWaterMark] = link;
        return (T)((Link)link).unlink();
    }

    int size() {
        return this.size;
    }

    void forEach(Consumer<? super T> consumer) {
        Integer n2 = null;
        Link link = this.head;
        while (null != link) {
            consumer.accept(link.value);
            if (n2 == null) {
                for (int i2 = 0; i2 < link.chains.length; ++i2) {
                    if (this != link.chains[i2]) continue;
                    n2 = i2;
                    break;
                }
            }
            link = link.next[n2];
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder().append("[ ");
        boolean[] blArray = new boolean[]{false};
        this.forEach(object -> {
            stringBuilder.append(blArray[0] ? ", " : "").append(object.toString());
            blArray[0] = true;
        });
        return stringBuilder.append(" ]").toString();
    }

    static final class Link<T> {
        private T value;
        private Chain<T>[] chains;
        private final Link<T>[] prev;
        private final Link<T>[] next;

        private Link(T t2, Chain<T> ... chainArray) {
            this.prev = new Link[chainArray.length];
            this.next = new Link[chainArray.length];
            this.link(t2, chainArray);
        }

        private void link(T t2, Chain<T> ... chainArray) {
            this.value = t2;
            this.chains = chainArray;
            for (int i2 = 0; i2 < chainArray.length; ++i2) {
                Chain<T> chain = chainArray[i2];
                ((Chain)chain).sizeChangeTrigger.accept(++((Chain)chain).size);
                this.next[i2] = ((Chain)chain).head;
                this.prev[i2] = null;
                if (null != ((Chain)chain).head) {
                    ((Chain)chain).head.prev[i2] = this;
                }
                ((Chain)chain).head = this;
                if (null != ((Chain)chain).tail) continue;
                ((Chain)chain).tail = this;
            }
        }

        private T unlink() {
            T t2 = this.value;
            this.value = null;
            for (int i2 = 0; i2 < this.chains.length; ++i2) {
                Chain<T> chain = this.chains[i2];
                ((Chain)chain).sizeChangeTrigger.accept(--((Chain)chain).size);
                if (this.prev[i2] == null) {
                    ((Chain)chain).head = this.next[i2];
                } else {
                    this.prev[i2].next[i2] = this.next[i2];
                }
                if (this.next[i2] == null) {
                    ((Chain)chain).tail = this.prev[i2];
                    continue;
                }
                this.next[i2].prev[i2] = this.prev[i2];
            }
            return t2;
        }

        public String toString() {
            return this.value.toString();
        }
    }
}

