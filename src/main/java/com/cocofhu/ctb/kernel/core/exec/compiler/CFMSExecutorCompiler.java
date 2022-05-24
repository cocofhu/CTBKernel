package com.cocofhu.ctb.kernel.core.exec.compiler;

import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.exception.compiler.CBadSyntaxException;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CPair;

import java.util.*;
import java.util.function.BiConsumer;

public class CFMSExecutorCompiler implements CExecutorCompiler {


    enum TokenType {
        NEXT,
        PROTOCOL,
        TOKEN,
        AS,
        BS,
        BE,
        EMPTY
    }

    private static class Token {
        public final String val;
        public final TokenType type;
        public final int pos;

        public static final Map<String, Boolean> PROTOCOLS = new HashMap<>();
        public static final Map<Character, Boolean> NEXT = new HashMap<>();

        static {
            PROTOCOLS.put("http", true);
            PROTOCOLS.put("rpc", true);
            NEXT.put('>', true);
            NEXT.put('|', true);
        }


        private Token(String val, TokenType type, int pos) {
            this.val = val;
            this.type = type;
            this.pos = pos;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "val='" + val + '\'' +
                    ", type=" + type +
                    ", pos=" + pos +
                    "}\n";
        }

        // 转义字符
        private static final char ESCAPE_CHAR = '\\';

        private static CPair<Integer, String> utilNext(String str, int start, char target, boolean escape) {
            StringBuilder sb = new StringBuilder();
            int len = str.length();
            int i = start;
            boolean escapeFound = false;
            boolean finished = false;
            while (i < len && !finished) {
                char ch = str.charAt(i);
                // 如果是转义字符走第一个分支
                if ((escape && escapeFound) || (ch != target && ch != ESCAPE_CHAR)) {
                    sb.append(ch);
                    escapeFound = false;
                } else if (ch == ESCAPE_CHAR && escape) {
                    escapeFound = true;
                } else {
                    finished = true;
                }
                ++i;
            }

            if (finished || target == ' ') {
                // 任意一个Token被解析出来时 都要删除多余的空格，去除多余的空格
                while (i < len && str.charAt(i) == ' ') ++i;
                return new CPair<>(i, sb.toString());
            }
            // 解析失败 不完整的字符串
            return new CPair<>(-1, null);
        }

        public static List<Token> parseTokens(String str) {
            // we accept empty string
            if (str == null) {
                throw new CBadSyntaxException("empty source code. ");
            }
            List<Token> tokens = new ArrayList<>();
            str = str.trim();
            int i = 0;
            int len = str.length();
            while (i < len) {
                char ch = str.charAt(i);
                CPair<Integer, String> pair = null;
                if (ch == ' ') {
                    // 删除无效空格
                    while (i < len && str.charAt(i) == ' ') ++i;
                } else if (ch == '"') {
                    pair = utilNext(str, i + 1, '"', true);
                } else if (ch == '\'') {
                    pair = utilNext(str, i + 1, '\'', true);
                } else if (ch == '-') {
                    if (i + 1 >= len) {
                        throw new CBadSyntaxException("incomplete source code", i);
                    }
                    char next = str.charAt(i + 1);
                    if (next == ' ') {
                        throw new CBadSyntaxException("incomplete source code", i, TokenType.TOKEN.toString(), "space");
                    }
                    tokens.add(new Token("-", TokenType.AS, i));
                    ++i;
                } else if (ch == '(') {
                    tokens.add(new Token("(", TokenType.BS, i));
                    ++i;
                } else if (ch == ')') {
                    tokens.add(new Token(")", TokenType.BE, i));
                    ++i;
                } else if (NEXT.containsKey(ch)) {
                    tokens.add(new Token("" + ch, TokenType.NEXT, i));
                    ++i;
                } else {
                    pair = utilNext(str, i, ' ', false);
                }

                if (pair != null) {
                    if (pair.getFirst() == -1) {
                        throw new CBadSyntaxException("incomplete source code", i);
                    }
                    String token = pair.getSecond();
                    if (i == 0 && PROTOCOLS.containsKey(token)) {
                        tokens.add(new Token(token, TokenType.PROTOCOL, i));
                    } else {
                        tokens.add(new Token(token, TokenType.TOKEN, i));
                    }
                    i = pair.getFirst();
                }
            }
            tokens.add(new Token(null, TokenType.EMPTY, i));
            return tokens;
        }
    }

    private static class State {
        private final String name;
        private final Map<TokenType, State> nextStates;
        private final BiConsumer<State, Token> action;

        public State(String name, BiConsumer<State, Token> action) {
            this.name = name;
            this.action = action;
            this.nextStates = new HashMap<>();
        }

        @SuppressWarnings("unchecked")
        public void add(CPair<TokenType, State>... pairs) {
            for (CPair<TokenType, State> pair : pairs) {
                this.nextStates.put(pair.getFirst(), pair.getSecond());
            }
        }

        public void transition(State state, Token token) {
            if (action != null) {
                action.accept(state, token);
            }
        }
    }


    private class FSM {
        // 开始状态
        private final State start;
        // 结束状态
        private final State end;
        private final List<Token> tokens;
        // 当前状态
        private State currentState;
        // 括号深度
        private int bracketsDepth;
        // 当前Token的位置
        private int tokenPos;


        private String protocol = null;
        private String currentExecutionName = null;
        private String currentKey;
        private CDefaultWritableData<String, Object> currentData;


        @SuppressWarnings("unchecked")
        private FSM(List<Token> tokens) {
            this.tokens = tokens;
            start = new State("start", null);
            end = new State("end", null);

            // S1 存在两条入度 均为Next 不做任何处理交给S3初始化状态初始化状态
            State s1 = new State("S1", null);
            // S2 存在两条入度 都是增加括号深度
            State s2 = new State("S2", (state, token) -> ++bracketsDepth);

            State s3 = new State("S3", ((state, token) -> {
                if("S1".equals(state.name)){
                    currentExecutionName = token.val;
                    CExecutorDefinition definition = acquireNewExecutorDefinition(currentExecutionName);

//                    currentData = definition;
                }
            }));
            State s4 = new State("S4", null);
            State s5 = new State("S5", null);
            State s6 = new State("S6", null);
            // start 3
            start.add(new CPair<>(TokenType.BS, s2), new CPair<>(TokenType.TOKEN, s3), new CPair<>(TokenType.PROTOCOL, s3));
            // s1 2
            s1.add(new CPair<>(TokenType.BS, s2), new CPair<>(TokenType.TOKEN, s3));
            // s2 2
            s2.add(new CPair<>(TokenType.TOKEN, s3), new CPair<>(TokenType.BS, s2));
            // s3 4
            s3.add(new CPair<>(TokenType.NEXT, s1), new CPair<>(TokenType.AS, s4), new CPair<>(TokenType.BE, s6), new CPair<>(TokenType.EMPTY, end));
            // s4 1
            s4.add(new CPair<>(TokenType.TOKEN, s5));
            // s5 1
            s5.add(new CPair<>(TokenType.TOKEN, s3));
            // s6 3
            s6.add(new CPair<>(TokenType.NEXT, s1), new CPair<>(TokenType.BE, s6), new CPair<>(TokenType.EMPTY, end));


        }

        public FSM nextState() {

            return this;
        }

        public boolean hasNext() {
            return currentState != end;
        }


    }

    @Override
    public CExecutorDefinition compiler(String expression, int flag) {
        return CExecutorCompiler.super.compiler(expression, flag);
    }

    @Override
    public CExecutorDefinition acquireNewExecutorDefinition(String nameOrAlias) {
        return null;
    }

    public void testParseToken(String str) {
        System.out.println(Token.parseTokens(str));
    }
}
