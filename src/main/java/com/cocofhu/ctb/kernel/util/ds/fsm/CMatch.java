package com.cocofhu.ctb.kernel.util.ds.fsm;

public interface CMatch<T> {
    int NOT_MATCHED = 0 ;
    int STANDARD = 10;
    int HIGH = 20;
    default int match(T input){
        return standardMatch(input) ? STANDARD : NOT_MATCHED;
    }

    boolean standardMatch(T input);
}
