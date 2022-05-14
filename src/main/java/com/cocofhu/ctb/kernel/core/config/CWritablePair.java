package com.cocofhu.ctb.kernel.core.config;

import java.util.Objects;

/**
 * @author cocofhu
 */
public class CWritablePair<A,B> {
    protected A first;
    protected B second;

    public CWritablePair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CWritablePair<?, ?> that = (CWritablePair<?, ?>) o;
        return Objects.equals(first, that.first) && Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "CWritablePair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
