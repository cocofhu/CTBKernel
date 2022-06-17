package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;
import com.cocofhu.ctb.kernel.util.ds.CWritableData;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author cocofhu
 */
public interface CExecutionRuntime extends CWritableData<String, Object> {



    // -------------------------------------------------------------
    // ---------------------  Common Constants ---------------------
    // -------------------------------------------------------------
    // -------------------------------------------------------------

    enum CExecutorRuntimeType {
        SIMPLE("simple"),
        LIST("list"),
        SERVICE("service");

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





    CExecutionRuntime start(CReadOnlyData<String, Object> attachment, CExecutorRuntimeType type, CExecutor executor);
    boolean finish();





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
        return (Throwable) get(EXEC_EXCEPTION_KEY);
    }

    /**
     * 获得当前层下的返回值
     */
    default Object getReturnVal(){
        return get(EXEC_RETURN_VAL_KEY);
    }

    /**
     * 在当前层上获得返回值
     */
    default void setReturnVal(Object returnVal){
        put(EXEC_RETURN_VAL_KEY, returnVal);
    }

    /**
     * 在当前层上获得异常
     */
    default void setException(Throwable throwable){
        put(EXEC_EXCEPTION_KEY, throwable);
    }


}
