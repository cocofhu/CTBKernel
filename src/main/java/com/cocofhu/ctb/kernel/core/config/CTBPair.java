package com.cocofhu.ctb.kernel.core.config;

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
}
