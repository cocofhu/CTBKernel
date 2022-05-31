package com.cocofhu.ctb.kernel.core.exec.compiler;

import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.exec.entity.CParameterDefinition;
import com.cocofhu.ctb.kernel.exception.compiler.CBadSyntaxException;
import com.cocofhu.ctb.kernel.util.CReflectionUtils;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * 有限状态机编译器
 * 语法说明：
 * <p>
 * 1、ReadFile -source 'a.txt' > Debug
 * 读取a.txt文件，将读取的String交给Debug来处理，通常Debug为一个输出程序
 * 2、http -url '/abc' -port 9090 : ReadFile -source 'a.txt' > Debug
 * 监听url '/abc' port 9090，接受到请求后 执行1所描述的指令，并将Debug的返回值作为http响应
 * 3、ReadFile -source 'a.txt' > http -url '/abc' -port 9090 : Debug
 * 与2类似，只是将读取完成的文件缓存起来，避免重复读取
 * 4、grpc -port 9090 : ReadFile -source 'a.txt' > Debug
 * 与2类似，只是协议改为grpc
 * 5、pcap -eth0 : checkIf9090TCP > IfTrueEmail -email 'cocofhu@outlook.com'
 * 监听eth0,当收到9090端口的TCP包时给'cocofhu@outlook.com'发送邮件
 *
 * @author cocofhu
 */
@Slf4j
public class CFMSExecutorCompiler implements CExecutorCompiler {

    /**
     * implement note
     * Grammar Definition
     * we defined command simply using bnf below:
     * command      :   |   execution
     * |   execution SPACE* SERVICE SPACE* execution;
     * <p>
     * execution    :       token (SPACE+ arguments)*
     * |   BS SPACE* execution SPACE* BE
     * |   execution (SPACE* NEXT SPACE* execution)*;
     * <p>
     * arguments    :       AS token SPACE+ token
     * |   arguments SPACE+ AS token SPACE+ token;
     * <p>
     * SPACE        :   ' ';
     * BS           :   '(';
     * BE           :   ')';
     * AS           :   '-';
     * NEXT         :   '>' | '|' ;
     * Token        :   just string
     */

    // 支持的普通任务类型
    public static final Set<Integer> GENERAL_TYPE = new HashSet<>();

    static {
        GENERAL_TYPE.add(CExecutorDefinition.TYPE_SCHEDULE);
        GENERAL_TYPE.add(CExecutorDefinition.TYPE_EXEC);
    }

    private final CExecutorDefinitionResolver resolver;

    public CFMSExecutorCompiler(CExecutorDefinitionResolver resolver) {
        this.resolver = resolver;
    }

    enum TokenType {
        NEXT,
        SERVICE,
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

        public static final Map<Character, Boolean> NEXT = new HashMap<>();

        public static final Set<Character> DELIMITERS = new HashSet<>();


        static {


            NEXT.put('>', true);
            // 还没有实现管道
//            NEXT.put('|', true);

            DELIMITERS.add(' ');
            DELIMITERS.add(':');
            DELIMITERS.add('(');
            DELIMITERS.add(')');
            DELIMITERS.add('-');
            DELIMITERS.add('>');
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
                    "}";
        }

        // 转义字符
        private static final char ESCAPE_CHAR = '\\';

        private static CPair<Integer, String> utilNext(String str, int start, char target) {
            Set<Character> characters = new HashSet<>();
            characters.add(target);
            // Always true
            return utilNext(str, start, characters, true);
        }

        private static CPair<Integer, String> utilNext(String str, int start, Set<Character> target, boolean escape) {
            StringBuilder sb = new StringBuilder();
            int len = str.length();
            int i = start;
            boolean escapeFound = false;
            boolean finished = false;
            while (i < len) {
                char ch = str.charAt(i);
                // 如果是转义字符走第一个分支
                if ((escape && escapeFound) || (!target.contains(ch) && ch != ESCAPE_CHAR)) {
                    sb.append(ch);
                    escapeFound = false;
                } else if (ch == ESCAPE_CHAR && escape) {
                    escapeFound = true;
                } else {
                    finished = true;
                    break;
                }
                ++i;
            }

            if (finished || target.contains(' ')) {
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
                    pair = utilNext(str, i + 1, '"');
                } else if (ch == '\'') {
                    pair = utilNext(str, i + 1, '\'');
                } else if (ch == ':') {
                    tokens.add(new Token(":", TokenType.SERVICE, i));
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
                } else if (ch == '-') {
                    pair = utilNext(str, i + 1, DELIMITERS, false);
                    if (pair.getFirst() == -1 || pair.getSecond().length() == 0) {
                        throw new CBadSyntaxException(str, "incomplete source code", i);
                    }
                    // need to add one to skip '-'
                    tokens.add(new Token("-", TokenType.AS, i++));
                } else {
                    pair = utilNext(str, i, DELIMITERS, false);
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
        // 词法分析之后的所有token
        private final List<Token> tokens;
        // 当前状态
        private State currentState;
        // 括号深度 1代表第一层括号，为了方便处理前后各加了一个括号
        private int bracketsDepth = 0;
        // 当前Token的位置
        private int tokenPos = 0;
        // 源代码，执行表达式
        private final String sourceCode;
        // first: 括号深度
        // second:未解析完成的CExecutorDefinition
        private final Map<Integer, List<CExecutorDefinition>> executions = new HashMap<>();
        // 当前正在解析的CExecutorDefinition
        private String currentExecutionName = null;
        // 当前正在创建的CExecutorDefinition
        private CExecutorDefinition currentExecution = null;
        // 当前环境下的参数名
        private String currentArgumentName = null;
        // 当前环境下的参数名所对应的类型
        private Class<?> currentType = null;
        // 当前正在解析的CExecutorDefinition对应的附加参数
        private CDefaultWritableData<String, Object> currentData = null;
        // 已经完成编译的CExecutorDefinition
        private final List<CExecutorDefinition> builtDefinitions = new ArrayList<>();
        // 暴露的服务，可能为空
        private CExecutorDefinition service;


        @SuppressWarnings("unchecked")
        private FSM(List<Token> tokens, String sourceCode) {
            this.tokens = tokens;
            this.sourceCode = sourceCode;

            // 开始状态
            State start = new State("Start", ((state, token) -> {
                if ("S3".equals(state.name) && token.type == TokenType.SERVICE) {
                    if (bracketsDepth != 0) {
                        throw new CBadSyntaxException(sourceCode, "unmatched brackets", token.pos);
                    }
                    if (service != null) {
                        throw new CBadSyntaxException(sourceCode, "service only can be defined once", token.pos);
                    }


                    List<CExecutorDefinition> currentBracketsDepthExecutions = executions.computeIfAbsent(0, k -> new ArrayList<>());
                    addToBuilt(currentBracketsDepthExecutions);

                    if (builtDefinitions.size() == 1) {
                        service = builtDefinitions.get(0);
                    } else {
                        service = new CExecutorDefinition("", "", "", builtDefinitions.toArray(new CExecutorDefinition[0]), null);
                    }
                    currentBracketsDepthExecutions.clear();
                }

                // 检查底层类型是否为服务类型
                // NOTE：后续看要不要只检查底层这里检查目前是递归检查
                CExecutorDefinition d = service;
                while (d != null && d.getType() != CExecutorDefinition.TYPE_SVC) {
                    d = (d.getSubJobs() == null || d.getSubJobs().length == 0) ? null : d.getSubJobs()[d.getSubJobs().length - 1];
                }
                if (d == null || d.getType() != CExecutorDefinition.TYPE_SVC) {
                    throw new CBadSyntaxException(sourceCode, "service only can be defined as service type", token.pos);
                }
                // 恢复底层类型
                d.setType(d.getSubJobs() != null ? CExecutorDefinition.TYPE_SCHEDULE : CExecutorDefinition.TYPE_EXEC);
                builtDefinitions.clear();

            }));

            // S1 存在2条入度 均为Next 不做任何处理交给S3初始化状态初始化状态
            State s1 = new State("S1", null);
            // S2 存在2条入度 都是增加括号深度
            State s2 = new State("S2", (state, token) -> ++bracketsDepth);
            // S3 存在4条入度
            State s3 = new State("S3", ((state, token) -> {
                if ("S1".equals(state.name) || "Start".equals(state.name) || "S2".equals(state.name)) {

                    // 如果上一个任务是不是基本任务类型, 只有基本任务类型才允许用 next 连起来
                    // 比如上一个类型是服务类型，这就说明一个服务类型后面接的不是":", 这在语法上是不允许的.
                    if (currentExecution != null && !GENERAL_TYPE.contains(currentExecution.getType())) {
                        throw new CBadSyntaxException(sourceCode, "bad next execution, only basic executions can be connected by next", token.pos, token.val);
                    }
                    // 如果服务已经定义了，此时又出现一个服务定义, 这在语法上是不允许的.
                    if (service != null && currentExecution != null && currentExecution.getType() == CExecutorDefinition.TYPE_SVC) {
                        throw new CBadSyntaxException(sourceCode, "service only can be defined once", token.pos, token.val);
                    }

                    currentExecutionName = token.val;
                    CExecutorDefinition definition = resolver.acquireNewExecutorDefinition(currentExecutionName);
                    if (definition == null) {
                        throw new CBadSyntaxException(sourceCode, "execution not found", token.pos, token.val);
                    }
                    currentData = new CDefaultWritableData<>(definition.getAttachment());
                    definition.setAttachment(currentData);

                    List<CExecutorDefinition> currentBracketsDepthExecutions = executions.computeIfAbsent(bracketsDepth, k -> new ArrayList<>());
                    currentBracketsDepthExecutions.add(definition);

                    // 保存当前正在处理的Execution
                    currentExecution = definition;

                } else if ("S5".equals(state.name)) {
                    String val = token.val;
                    currentData.put(currentArgumentName, ConverterUtils.convert(val, currentType));
                }
            }));
            // S4 存在1条入度    参数开始
            State s4 = new State("S4", null);
            // S5 存在1条入度    记录参数名字
            State s5 = new State("S5", (state, token) -> {
                currentArgumentName = token.val;
                CPair<Class<?>, String> pair = findTypeByName(currentArgumentName);
                Class<?> type = pair.getFirst();
                String msg = pair.getSecond();
                if (type == null) {
                    throw new CBadSyntaxException(sourceCode, msg, token.pos);
                } else {
                    currentType = type;
                }
            });
            // S6 存在3条入度
            State s6 = new State("S6", (state, token) -> {
                if ("S6".equals(state.name) || "S3".equals(state.name)) {
                    if (bracketsDepth <= 0) {
                        throw new CBadSyntaxException(this.sourceCode, "syntax error", token.pos, ")");
                    }
                    // 遇到右括号，拿出当前层的执行器放在上一层
                    List<CExecutorDefinition> currentBracketsDepthExecutions = executions.computeIfAbsent(bracketsDepth, k -> new ArrayList<>());
                    // 如果是括号创建的 用ListExecutor实例化
                    CExecutorDefinition definition = new CExecutorDefinition("", "", "", currentBracketsDepthExecutions.toArray(new CExecutorDefinition[0]), null);
                    // 反正前一层的括号里
                    executions.computeIfAbsent(bracketsDepth - 1, k -> new ArrayList<>()).add(definition);
                    // 创建List创建的attachment
                    currentData = new CDefaultWritableData<>();
                    definition.setAttachment(currentData);

                    currentBracketsDepthExecutions.clear();
                    --bracketsDepth;
                } else if ("S5".equals(state.name)) {
                    String val = token.val;
                    currentData.put(currentArgumentName, ConverterUtils.convert(val, currentType));
                }

            });

            end = new State("End", (state, token) -> {
                if (bracketsDepth != 0) {
                    throw new CBadSyntaxException(sourceCode, "unmatched brackets. ");
                }
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
            s3.add(new CPair<>(TokenType.NEXT, s1), new CPair<>(TokenType.AS, s4), new CPair<>(TokenType.BE, s6), new CPair<>(TokenType.EMPTY, end), new CPair<>(TokenType.SERVICE, start));
            // s4 1
            s4.add(new CPair<>(TokenType.TOKEN, s5));
            // s5 2
            s5.add(new CPair<>(TokenType.TOKEN, s3), new CPair<>(TokenType.TOKEN, s6));
            // s6 4
            s6.add(new CPair<>(TokenType.NEXT, s1), new CPair<>(TokenType.AS, s4), new CPair<>(TokenType.BE, s6), new CPair<>(TokenType.EMPTY, end));

            currentState = start;


        }

        private CPair<Class<?>, String> findTypeByName(String name) {
            CParameterDefinition[] inputs = currentExecution.getInputs();
            if (inputs != null) {
                for (CParameterDefinition parameter : inputs) {
                    if (name.equals(parameter.getName())) {
                        Object type = parameter.getType();
                        if (type.getClass() == Class.class) {
                            Class<?> clazz = (Class<?>) type;
                            if (CReflectionUtils.isBasicDataType(clazz)) {
                                return new CPair<>(clazz, null);
                            } else {
                                return new CPair<>(null, "only basic data types can be use in source code, unexpected type:" + clazz.getName());
                            }
                        } else {
                            return new CPair<>(null, "reference type can not be use in source code, try using '>' to pass reference arguments");
                        }
                    }
                }
            }
            return new CPair<>(null, "unnecessary argument :" + name);
        }

        private void addToBuilt(List<CExecutorDefinition> currentBracketsDepthExecutions) {
            builtDefinitions.add(instanceGeneralDefinition(currentBracketsDepthExecutions));
        }

        private CExecutorDefinition instanceGeneralDefinition(List<CExecutorDefinition> currentBracketsDepthExecutions) {
            if (currentBracketsDepthExecutions.size() == 1) {
                return currentBracketsDepthExecutions.get(0);
            } else if (currentBracketsDepthExecutions.size() > 1) {
                return new CExecutorDefinition("", "", "", currentBracketsDepthExecutions.toArray(new CExecutorDefinition[0]), null);
            } else {
                // never reach here!
                throw new CBadSyntaxException(sourceCode, "empty definitions");
            }
        }


        public void nextState() {
            Token token = tokens.get(tokenPos++);
            State state = currentState.nextStates.get(token.type);
            if (state == null) {
                throw new CBadSyntaxException(this.sourceCode, "syntax error", token.pos, token.val);
            }
            state.transition(currentState, token);
            currentState = state;
        }

        public boolean hasNext() {
            return currentState != end;
        }

        public List<CExecutorDefinition> getBuiltDefinitions() {
            return builtDefinitions;
        }

        public CExecutorDefinition getService() {
            return service;
        }
    }

    @Override
    public CExecutorDefinition compiler(String expression, int flag) {
        List<Token> tokens = Token.parseTokens(expression);
        FSM fsm = new FSM(tokens, expression);
        while (fsm.hasNext()) {
            fsm.nextState();
        }
        List<CExecutorDefinition> builtDefinitions = fsm.getBuiltDefinitions();
        CExecutorDefinition service = fsm.getService();
        if (service != null) {
            if (builtDefinitions.size() == 0) {
                throw new CBadSyntaxException(expression, "service has no action. ");
            }
            return CExecutorDefinition.newServiceDefinition(service, new CExecutorDefinition("", "", "", builtDefinitions.toArray(new CExecutorDefinition[0]), null));
        } else {
            if (builtDefinitions.size() == 1) {
                return builtDefinitions.get(0);
            } else {
                return new CExecutorDefinition("", "", "", builtDefinitions.toArray(new CExecutorDefinition[0]), null);
            }
        }
    }
}
