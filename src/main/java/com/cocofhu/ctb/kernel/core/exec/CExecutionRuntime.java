package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;

import java.util.UUID;

/**
 * @author cocofhu
 */
public interface CExecutionRuntime {



    // -------------------------------------------------------------
    // ---------------------  Common Constants ---------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------

    enum CExecutorRuntimeType {
        SIMPLE("simple"),
        LIST("list"),
        ARGS_COPY("copy"),
        SERVICE("service"),
        RESTORE("restore");

        private final String value;

        CExecutorRuntimeType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * 返回值
     */
    String EXEC_RETURN_VAL_KEY = "EXEC_RETURN_VAL_KEY";
    /**
     * 异常
     */
    String EXEC_EXCEPTION_KEY = "EXEC_EXCEPTION_KEY";
    /**
     * Runtime
     */
    String EXEC_CONTEXT_KEY = "EXEC_CONTEXT_KEY";




    CDefaultLayerData<String, Object> getCurrentLayer();

    UUID start(CReadOnlyData<String, Object> attachment, CExecutorRuntimeType type, CExecutor executor);
    void finish(UUID uuid);
//    void startSimpleExecution(CReadOnlyData<String, Object> attachment, CExecutor executor);
//    void startList(CReadOnlyData<String, Object> attachment, CExecutor executor);
//    void startSimple(CReadOnlyData<String, Object> attachment, CExecutor executor);
//    void startSimple(CReadOnlyData<String, Object> attachment, CExecutor executor);












    // -------------------------------------------------------------
    // -----------------  Common Default Methods--------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------

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
