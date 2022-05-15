package com.cocofhu.ctb.kernel.util.ds;

import java.util.Objects;

/**
 * @author cocofhu
 */
public class CWritableTripe<A,B,C> {
    protected A first;
    protected B second;
    protected C third;

    public CWritableTripe(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
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

    public C getThird() {
        return third;
    }

    public void setThird(C third) {
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CWritableTripe<?, ?, ?> that = (CWritableTripe<?, ?, ?>) o;
        return Objects.equals(first, that.first) && Objects.equals(second, that.second) && Objects.equals(third, that.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "CWritableTripe{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
