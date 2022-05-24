package com.cocofhu.ctb.kernel.core.exec.compiler;

import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.exception.compiler.CBadSyntaxException;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CPair;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * 有限状态机 编译脚本
 *
 * @author cocofhu
 */
public class CFMSExecutorCompiler implements CExecutorCompiler {

    private final CExecutorDefinitionResolver resolver;

    public CFMSExecutorCompiler(CExecutorDefinitionResolver resolver) {
        this.resolver = resolver;
    }

    enum TokenType {
        NEXT,
        //        PROTOCOL,
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
                throw new CBadSyntaxException(null, "empty source code. ");
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
                        throw new CBadSyntaxException(str, "incomplete source code", i);
                    }
                    char next = str.charAt(i + 1);
                    if (next == ' ') {
                        throw new CBadSyntaxException(str, "syntax error", i, "space");
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
                        throw new CBadSyntaxException(str, "incomplete source code", i);
                    }
                    String token = pair.getSecond();
                    tokens.add(new Token(token, TokenType.TOKEN, i));
                    i = pair.getFirst();
                }
            }
            tokens.add(new Token("EOF", TokenType.EMPTY, i));
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
        // 结束状态
        private final State end;
        //
        private final List<Token> tokens;
        // 当前状态
        private State currentState;
        // 括号深度
        private int bracketsDepth = 0;
        // 当前Token的位置
        private int tokenPos = 0;

        private final String sourceCode;

        // first: 括号深度
        private final Map<Integer, List<CExecutorDefinition>> executions = new HashMap<>();

        private String currentExecutionName = null;
        private String currentArgumentName = null;
        private CDefaultWritableData<String, Object> currentData = null;

        private final List<CExecutorDefinition> builtDefinitions = new ArrayList<>();


        @SuppressWarnings("unchecked")
        private FSM(List<Token> tokens, String sourceCode) {
            this.tokens = tokens;
            this.sourceCode = sourceCode;

            // 开始状态
            State start = new State("Start", null);

            // S1 存在2条入度 均为Next 不做任何处理交给S3初始化状态初始化状态
            State s1 = new State("S1", null);
            // S2 存在2条入度 都是增加括号深度
            State s2 = new State("S2", (state, token) -> ++bracketsDepth);
            // S3 存在4条入度
            State s3 = new State("S3", ((state, token) -> {
                if ("S1".equals(state.name) || "Start".equals(state.name) || "S2".equals(state.name)) {
                    currentExecutionName = token.val;
                    CExecutorDefinition definition = resolver.acquireNewExecutorDefinition(currentExecutionName);
                    if (definition == null) {
                        throw new CBadSyntaxException(sourceCode, "execution not found", token.pos, token.val);
                    }
                    currentData = new CDefaultWritableData<>(definition.getAttachment());
                    definition.setAttachment(currentData);
                    List<CExecutorDefinition> currentBracketsDepthExecutions = executions.computeIfAbsent(bracketsDepth, k -> new ArrayList<>());
                    currentBracketsDepthExecutions.add(definition);
                } else if ("S5".equals(state.name)) {
                    String val = token.val;
                    currentData.put(currentArgumentName, val);
                }
            }));
            // S4 存在1条入度    参数开始
            State s4 = new State("S4", null);
            // S5 存在1条入度    记录参数名字
            State s5 = new State("S5", (state, token) -> currentArgumentName = token.val);
            // S6 存在两条入度    都是有括号处理
            State s6 = new State("S6", (state, token) -> {
                if (bracketsDepth <= 0) {
                    throw new CBadSyntaxException(this.sourceCode, "syntax error", token.pos, ")");
                }
                List<CExecutorDefinition> currentBracketsDepthExecutions = executions.computeIfAbsent(bracketsDepth, k -> new ArrayList<>());
                addToBuilt(currentBracketsDepthExecutions);
                currentBracketsDepthExecutions.clear();
                --bracketsDepth;
            });

            end = new State("End", (state, token) -> {
                List<CExecutorDefinition> currentBracketsDepthExecutions = executions.computeIfAbsent(0, k -> new ArrayList<>());
                addToBuilt(currentBracketsDepthExecutions);
            });

            // start 3
            start.add(new CPair<>(TokenType.BS, s2), new CPair<>(TokenType.TOKEN, s3));
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

            currentState = start;


        }

        private int addToBuilt(List<CExecutorDefinition> currentBracketsDepthExecutions) {
            if (currentBracketsDepthExecutions.size() == 1) {
                builtDefinitions.add(currentBracketsDepthExecutions.get(0));
            } else if (currentBracketsDepthExecutions.size() > 1) {
                builtDefinitions.add(new CExecutorDefinition("", "", "", currentBracketsDepthExecutions.toArray(new CExecutorDefinition[0]), null));
            }
            return builtDefinitions.size();
        }

        public FSM nextState() {
            Token token = tokens.get(tokenPos++);
            State state = currentState.nextStates.get(token.type);
            if (state == null) {
                throw new CBadSyntaxException(this.sourceCode, "syntax error", token.pos, token.val);
            }
            state.transition(currentState, token);
            currentState = state;
//            System.out.println(currentState.name + ":::");
            return this;
        }

        public boolean hasNext() {
            return currentState != end;
        }

        public List<CExecutorDefinition> getBuiltDefinitions() {
            return builtDefinitions;
        }
    }

    @Override
    public CExecutorDefinition compiler(String expression, int flag) {
        FSM fsm = new FSM(Token.parseTokens(expression), expression);
        while (fsm.hasNext()) {
            fsm.nextState();
        }
        List<CExecutorDefinition> builtDefinitions = fsm.getBuiltDefinitions();
        if (builtDefinitions.size() == 1) {
            return builtDefinitions.get(0);
        } else {
            return new CExecutorDefinition("", "", "", builtDefinitions.toArray(new CExecutorDefinition[0]), null);
        }
    }


    public void testParseToken(String str) {
        System.out.println(Token.parseTokens(str));
    }
}
