package com.cocofhu.ctb.kernel.util.ds.fsm;


import com.cocofhu.ctb.kernel.util.CStringUtils;

import java.util.ArrayList;
import java.util.List;

public class CStringFSM {


    static class SimpleFSM extends CStringFSM{
        public SimpleFSM(char ch) {
            super(new CState<>("END", null), new CState<>("Start", null));
            start.add(input -> input.ch == ch, end);
        }
    }
    static class MoreFSM extends CStringFSM{
        public MoreFSM(char ch) {
            super(new CState<>("END", null), new CState<>("Start", null));
            start.add(input -> input.ch == ch, end);
        }
    }

    protected final CState<Token> end;
    protected final CState<Token> start;

    static class Token {
        final int pos;
        final char ch;

        public Token(int pos, char ch) {
            this.pos = pos;
            this.ch = ch;
        }
    }

    public CStringFSM(CState<Token> end, CState<Token> start) {
        this.end = end;
        this.start = start;


        List<CState<Token>> states = new ArrayList<>();


//        CState<Token> start = new CState<>("start", null);
//        CState<Token> s1 = new CState<>("s1", null);
//        CState<Token> s2 = new CState<>("s2", null);
//        CState<Token> s3 = new CState<>("s3", null);
//        CState<Token> s4 = new CState<>("s4", null);
//        states.add(start.add(input -> input.ch == '[', s1));
//        states.add(s1.add(input -> true, s2));
//        states.add(s2.add(input -> input.ch == '-', s3));
//        states.add(s3.add(input -> true, s3));






//        this.tokens = tokens;
//        CState<Token> start = new CState<>("integer-start",(last, token) -> {});
//        CState<Token> s1 = new CState<>("integer-s1",(last, token) -> {});
//        start.add(input -> input.ch > '0' && input.ch < '9' ? CMatch.STANDARD : CMatch.NOT_MATCHED, s1);


        // integer:    [1-9][0-9]*
        // right-value: integer | float | string
        // (*str+(\s+ -str* right-value)
    }

    int match(String str, int i){
        if(str == null || i >= str.length()){
            return start == end ? i : -1;
        }
        CState<Token> current = start;
        while(current != end && i < str.length()){
            Token token = new Token(i, str.charAt(i));
            CState<Token> nextState = current.findNextState(token);
            if(nextState == null){
                return -1;
            }
            nextState.transition(current, token);
            current = nextState;
            ++i;
        }
        return current == end ? i : -1;
    }

    public static void main(String[] args) {
        System.out.println(new SimpleFSM('c').match("c", 0));
    }
}
