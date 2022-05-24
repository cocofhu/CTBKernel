package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.anno.param.process.CAutoWiredProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CExecutorInputProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CValueProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CBeanRefProcess;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.creator.CDefaultBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.CExecutorMethod;
import com.cocofhu.ctb.kernel.core.exec.build.CExecutorUtils;
import com.cocofhu.ctb.kernel.core.exec.compiler.CExecutorCompiler;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultNoParameterConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.value.*;
import com.cocofhu.ctb.kernel.util.CCollections;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CPair;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author cocofhu
 */
public class CMethodBeanFactory extends CDefaultBeanFactory implements CExecutorCompiler {

    private final Map<String, CExecutorDefinition> executorDefinitionMap = new HashMap<>(32);
    private final Map<String, String> alias = new HashMap<>();


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

        public Token(String val, TokenType type) {
            this.val = val;
            this.type = type;
        }
    }

    private static class State {
        private final String name;
        private final Map<TokenType, State> nextStates;
        private final BiConsumer<State,Token> biConsumer;

        public State(String name, BiConsumer<State,Token> biConsumer) {
            this.name = name;
            this.biConsumer = biConsumer;
            this.nextStates = new HashMap<>();
        }

        @SuppressWarnings("unchecked")
        public void add(CPair<TokenType, State>... pairs) {
            for (CPair<TokenType, State> pair : pairs) {
                this.nextStates.put(pair.getFirst(), pair.getSecond());
            }
        }

        public void doAction(State state, Token token){
            if(biConsumer != null) {
                biConsumer.accept(state,token);
            }
        }

    }

    private class FSM {
        private final State start;
        private final State end;
        private State currentState;
        private int bracketsDepth;
        private int tokenPos;
        private String protocol = null;
        private String currentExecutionName = null;

        private String key;

        private CDefaultWritableData<String,Object> currentAttachment;


        private Token[] tokens = {
                new Token("AcquireConnection", TokenType.TOKEN),
                new Token(">", TokenType.NEXT),
                new Token("QueryAsMapList", TokenType.TOKEN),
                new Token("-", TokenType.AS),
                new Token("sql", TokenType.TOKEN),
                new Token("select * from test", TokenType.TOKEN),
                new Token("", TokenType.EMPTY)
        };


        @SuppressWarnings("unchecked")
        private FSM() {

            State s1 = new State("S1", (state, token) -> protocol = token.val);
            State s2 = new State("S2", (state, token) -> {
                if ("S3".equals(token.val)) {
                    ++bracketsDepth;
                } else {
                    --bracketsDepth;
                }
            });
            State s3 = new State("S3", (state, token) -> {
                if ("S5".equals(state.name)) {
                    currentAttachment.put(key, token.val);
                } else {

                    if ("S2".equals(state.name)) {
                        if (currentExecutionName != null) {
                            System.out.println(bracketsDepth);
                            System.out.println(currentExecutionName);
                            System.out.println(currentAttachment.toMap());
                        }
                    }

                    currentExecutionName = token.val;
                    System.out.println(currentExecutionName);
                    CExecutorDefinition definition = acquireNewExecutorDefinition(currentExecutionName);
                    currentAttachment = new CDefaultWritableData<>(definition.getAttachment());
                    definition.setAttachment(currentAttachment);
                }
            });
            State s4 = new State("S4", (state, token) -> {
            });
            State s5 = new State("S5", (state, token) -> key = token.val);
            start = new State("start", (state, token) -> {
            });
            end = new State("end", (state, token) -> {
                System.out.println(protocol);
                System.out.println(bracketsDepth);
                System.out.println(currentExecutionName);
                System.out.println(currentAttachment.toMap());
            });

            start.add(new CPair<>(TokenType.PROTOCOL, s1), new CPair<>(TokenType.BS, s2), new CPair<>(TokenType.TOKEN, s3));
            s1.add(new CPair<>(TokenType.TOKEN, s3), new CPair<>(TokenType.BS, s2));
            s2.add(new CPair<>(TokenType.TOKEN, s3));
            s3.add(new CPair<>(TokenType.EMPTY, end), new CPair<>(TokenType.AS, s4), new CPair<>(TokenType.BE, s2), new CPair<>(TokenType.NEXT, s2));
            s4.add(new CPair<>(TokenType.TOKEN, s5));
            s5.add(new CPair<>(TokenType.TOKEN, s3));
            currentState = start;
            tokenPos = 0;
        }

        public FSM nextState(){
            Token token = tokens[tokenPos++];
            State state = currentState.nextStates.get(token.type);
            if(state == null){
                System.out.println(start.name);
                System.out.println(currentState.nextStates);
                System.out.println(token.type + "::" + token.val);
            }
            state.doAction(currentState, token);
            currentState = state;
            System.out.println(currentState.name + ":::");
            return this;
        }

        public boolean hasNext(){
            return currentState != end;
        }



    }

    public CMethodBeanFactory(CBeanDefinitionResolver beanDefinitionResolver) {
        super(new CDefaultBeanInstanceCreator(new CConstructorResolver[]{new CDefaultConstructorResolver(), new CDefaultNoParameterConstructorResolver()}),
                beanDefinitionResolver,
                new CChainValueResolver(
                        new CValueResolver[]{
                                new CAnnotationValueResolver(CCollections.toList(new CValueProcess(), new CBeanRefProcess(), new CAutoWiredProcess(), new CExecutorInputProcess()))
                        })
        );

        List<CBeanDefinition> beanDefinitions = beanDefinitionResolver.resolveAll(getConfig());


        CExecutorDefinition job0 = CExecutorUtils.toExecDetail(this, new CExecutorMethod("Power", "mul"));
        CExecutorDefinition job1 = CExecutorUtils.toExecDetail(this, new CExecutorMethod("CParamExecutor", "transform"));
        CExecutorDefinition job2 = CExecutorUtils.toExecDetail(this, new CExecutorMethod("CDBUtils", "acquireConnection"));
        CExecutorDefinition job3 = CExecutorUtils.toExecDetail(this, new CExecutorMethod("CDBUtils", "queryAsMap"));

        CDefaultWritableData<String, Object> attachment = new CDefaultWritableData<>();
        attachment.put("source", CDefaultExecutionRuntime.EXEC_RETURN_VAL_KEY);
        attachment.put("dist", "ABC");
        job1.setAttachment(attachment);

        attachment = new CDefaultWritableData<>();
        attachment.put("driverName", "com.mysql.cj.jdbc.Driver");
        attachment.put("username", "root");
        job2.setAttachment(attachment);

        executorDefinitionMap.put("Power", job0);
        executorDefinitionMap.put("Transform", job1);
        executorDefinitionMap.put("AcquireConnection", job2);
        executorDefinitionMap.put("QueryAsMapList", job3);

    }


    @Override
    public CExecutorDefinition acquireNewExecutorDefinition(String nameOrAlias) {
        return (CExecutorDefinition) executorDefinitionMap.get(nameOrAlias).cloneSelf();
    }

//    @Override
//    public CExecutorDefinition compiler(String expression) {
//        return CExecutorCompiler.super.compiler(expression);
//    }

    public void f(){
        FSM fsm = new FSM();
        while(fsm.hasNext()){
            fsm.nextState();
        }
    }
}
