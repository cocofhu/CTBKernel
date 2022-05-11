package com.cocofhu.ctb.kernel.core.config;

import java.util.Objects;

/**
 * @author cocofhu
 */
public class CTripe<A,B,C> {
    protected final A first;
    protected final B second;
    protected final C third;

    public CTripe(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public C getThird() {
        return third;
    }

    @Override
    public String toString() {
        return "CTripe{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CTripe<?, ?, ?> cTripe = (CTripe<?, ?, ?>) o;
        return Objects.equals(first, cTripe.first) && Objects.equals(second, cTripe.second) && Objects.equals(third, cTripe.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }
}
