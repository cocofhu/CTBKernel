package com.cocofhu.ctb.kernel.core.exec.compiler;


import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.CStringUtils;
import com.cocofhu.ctb.kernel.util.ds.CPair;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;


public interface CExecutorCompiler {
    default CExecutorDefinition compiler(String expression) {
        System.out.println(expression);
        BiFunction<CPair<String,Character>,Integer, CPair<String,Integer>> fn = ((pair, i) -> {
            String src = pair.getFirst();
            Character target = pair.getSecond();
            boolean escape = false;
            boolean finished = false;
            if(i >= src.length()){
                throw new IllegalArgumentException("incomplete expression");
            }
            StringBuilder sb = new StringBuilder();
            while(i < src.length() && !finished){
                char ch = src.charAt(i);
                if(escape || ch != target){
                    escape = false;
                    sb.append(ch);
                } else if(ch == '\\') {
                    escape = true;
                }else{
                    finished = true;
                }
                System.out.println(ch);
                ++i;
            }

            if(finished || target == ' '){
                return new CPair<>(sb.toString(),i);
            }
            throw new IllegalArgumentException("incomplete expression");

        });

        if(CStringUtils.isEmpty(expression)){
            return null;
        }

        String[] execs = expression.split(">");
        if(execs.length == 1){
            String exec = execs[0].trim();
            int i = 0 ;
            CPair<String, Integer> pair = fn.apply(new CPair<>(exec, ' '), i);
            i = pair.getSecond();
            String execName = pair.getFirst();
            String key = null;
            String val = null;
            System.out.print(execName + "    ===>");
            while(i < exec.length()){
                char ch = exec.charAt(i);
                if(ch == '-'){
                    pair = fn.apply(new CPair<>(exec, ' '), i + 1);
                    i = pair.getSecond();
                    key = pair.getFirst();
                }
                if(ch == '\'') {
                    pair = fn.apply(new CPair<>(exec, '\''), i + 1);
                    i = pair.getSecond();
                    val = pair.getFirst();
                    System.out.print(key+":"+val+",");
                }
            }
            System.out.println();
            return null;
        }

        for (String exec : execs) {
            compiler(exec);
        }
        return null;
    }

    CExecutorDefinition acquireNewExecutorDefinition(String nameOrAlias);
}
