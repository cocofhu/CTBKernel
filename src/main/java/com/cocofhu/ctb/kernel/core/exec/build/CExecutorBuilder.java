package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerDataSet;


public interface CExecutorBuilder {


    /**
     * 将CExecDetail转换为执行器
     * @param execDetail        执行对象，执行过程中为修改此对象的为确定的引用参数为确定的类型或者名字
     * @param builder           全局转换器，如果执行对象的Type不支持转换，可以交给全部转换器转换
     * @param context           执行器上下文
     * @param contextTypes      作用域上的类型
     * @param checkInput        是否检查输入参数的完整性
     * @return 执行器和上一次的输出(去除Removal的实际输出)
     */
    CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder, CExecutionRuntime context,
                         CDefaultLayerDataSet<String,Class<?>> contextTypes, boolean checkInput);

    default CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder, CExecutionRuntime context){
        return toExecutor(execDetail,builder,context,new CDefaultLayerDataSet<>(),false);
    }


}
