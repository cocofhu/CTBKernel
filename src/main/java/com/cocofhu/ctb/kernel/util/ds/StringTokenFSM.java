package com.cocofhu.ctb.kernel.util.ds;

/**
 * 字符串Token有限状态机
 */
public class StringTokenFSM {
    static class StateTable{

    }
    @FunctionalInterface
    interface Transition{
        boolean match(String src,int start);
    }

}
