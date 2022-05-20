package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerDataSet;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;

/**
 * @author cocofhu
 */
public interface CExecutionRuntime {


    enum CExecutorRuntimeType {
        SIMPLE("SIMPLE"),
        LIST("LIST"),
        ARGS_COPY("ARGS_COPY"),
        PIPE("PIPE");
        private final String value;

        CExecutorRuntimeType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    String EXEC_RETURN_VAL_KEY = "EXEC_RETURN_VAL_KEY";
    String EXEC_EXCEPTION_KEY = "EXEC_EXCEPTION_KEY";

    String EXEC_CONTEXT_KEY = "EXEC_CONTEXT_KEY";


    CDefaultLayerDataSet<String, Object> getCurrentLayer();

    void startNew(CReadOnlyDataSet<String, Object> attachment, boolean copyCurrent, CExecutorRuntimeType type, CExecutor executor);

    void stopCurrent();


    /**
     * 当前层下是否发生过异常
     */
    default boolean hasException(){
        return getException() != null;
    }

    /**
     * 获取当前层下的异常
     */
    default Throwable getException(){
        return (Throwable) getCurrentLayer().get(EXEC_EXCEPTION_KEY, 0);
    }

    /**
     * 获得当前层下的返回值
     */
    default Object getReturnVal(){
        return getCurrentLayer().get(EXEC_RETURN_VAL_KEY, 0);
    }

    /**
     * 在当前层上获得返回值
     */
    default void setReturnVal(Object returnVal){
        getCurrentLayer().put(EXEC_RETURN_VAL_KEY, returnVal);
    }

    /**
     * 在当前层上获得异常
     */
    default void setException(Throwable throwable){
        getCurrentLayer().put(EXEC_EXCEPTION_KEY, throwable);
    }


}
