package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;


public interface CExecutorBuilder {


    /**
     * 将CExecutorDefinition转换为执行器
     * @param execDetail        执行对象，执行过程中为修改此对象的为确定的引用参数为确定的类型或者名字
     * @param builder           全局转换器，如果执行对象的Type不支持转换，可以交给全部转换器转换
     * @param contextTypes      作用域上的类型
     * @param layer             执行对象所在的层级，用于跟踪异常
     * @param checkInput        是否检查输入参数的完整性
     */
    CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder, CDefaultLayerData<String,Class<?>> contextTypes, int layer, boolean checkInput);

    default CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder){
        return toExecutor(execDetail,builder,new CDefaultLayerData<>(),0,false);
    }

    default CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder, boolean checkInput){
        return toExecutor(execDetail,builder,new CDefaultLayerData<>(),0,checkInput);
    }

}
