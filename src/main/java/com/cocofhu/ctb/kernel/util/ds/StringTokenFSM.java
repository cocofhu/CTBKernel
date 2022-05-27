//package com.cocofhu.ctb.kernel.util.ds;
//
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
///**
// * 字符串Token有限状态机
// */
//public class StringTokenFSM {
//
//    private State<Input> state;
//
//    static class Input {
//        String str;
//        int i;
//    }
//
//    static class State<T> {
//
//        private final int state;
//        public Map<Integer, CPair<State<T>, Transition<T>>> nextStates = new HashMap<>();
//
//        State(int state) {
//            this.state = state;
//        }
//
//        interface Transition<T> {
//            int NOT_MATCHED = 0;
//            int MATCHED = 1;
//
//            int match(T t);
//
//        }
//
//        State<T> next(T t) {
//
//        }
//
//    }
//
//
//
//    public void next() {
//        int maxMatchScore = 0;
//        State<T> nextState = null;
//        Set<Integer> keySet = nextStates.keySet();
//        for (Integer i : keySet) {
//            CPair<State<T>, State.Transition<T>> pair = nextStates.get(i);
//            State<T> state = pair.getFirst();
//            State.Transition<T> transition = pair.getSecond();
//            int score = transition.match(t);
//            if (score != State.Transition.NOT_MATCHED) {
//                if (maxMatchScore < score) {
//                    maxMatchScore = score;
//                    nextState = state;
//                }
//            }
//        }
//        return nextState;
//    }
//}
