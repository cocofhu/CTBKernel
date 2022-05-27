package com.cocofhu.ctb.kernel.util.ds.fsm;

import com.cocofhu.ctb.kernel.util.ds.CPair;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * 有限状态机状态
 * @param <T> 输入参数 必须实现CMatch用于匹配
 */
public class CState<T> {
    final String name;
    final List<CPair<CMatch<T>,CState<T>>> nextStates;
    private final BiConsumer<CState<T>, T> action;

    public CState(String name, BiConsumer<CState<T>, T> action) {
        this.name = name;
        this.nextStates = new ArrayList<>();
        this.action = action;
    }

    /**
     * 为当前状态添加边
     * @param matcher   输入参数匹配函数
     * @param state     下一个状态
     */
    public CState<T> add(CMatch<T> matcher, CState<T> state) {
        this.nextStates.add(new CPair<>(matcher, state));
        return this;
    }

    /**
     * 当状态发生改变是会自动调用该方法
     * @param state     前一个状态
     * @param input     导致改变状态到输入
     */
    public void transition(CState<T> state, T input) {
        if (action != null) {
            action.accept(state, input);
        }
    }
    // 寻找到最佳匹配
    public CState<T> findNextState(T input){
        int maxScore = 0;
        CState<T> state = null;
        for (CPair<CMatch<T>, CState<T>> pair : nextStates) {
            int score = pair.getFirst().match(input);
            if (score != CMatch.NOT_MATCHED && maxScore < score) {
                maxScore = score;
                state = pair.getSecond();
            }
        }
        return state;
    }

    public String getName() {
        return name;
    }
}
