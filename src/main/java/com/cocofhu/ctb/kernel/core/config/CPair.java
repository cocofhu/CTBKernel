package com.cocofhu.ctb.kernel.core.config;

import java.util.Objects;

/**
 * @author cocofhu
 */
public class CPair<A,B> {
    protected final A first;
    protected final B second;

    public CPair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CPair<?, ?> cp
                && Objects.equals(first,cp.getFirst())
                && Objects.equals(second,cp.getSecond());
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "CTBPair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
