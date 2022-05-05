package com.cocofhu.ctb.kernel.core.config;

/**
 * @author cocofhu
 */
public class CTBPair<A,B> {
    private final A first;
    private final B second;

    public CTBPair(A first, B second) {
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
    public String toString() {
        return "CTBPair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
