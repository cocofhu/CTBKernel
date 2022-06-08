//package com.cocofhu.ctb.kernel.core.exec;
//
//
//import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;
//import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;
//
///**
// * @author cocofhu
// */
//public class CTreeExecutionRuntime implements CExecutionRuntime{
//
//    private volatile CDefaultLayerData<String, Object> currentLayer = new CDefaultLayerData<>();
//    private volatile CDefaultLayerData<String, Object> sinkLayer = new CDefaultLayerData<>();
//
//    private final CDefaultLayerData<String, Object> headerLayer = currentLayer;
//    private final CDefaultLayerData<String, Object> headerSinkLayer = sinkLayer;
//
//    @Override
//    public CDefaultLayerData<String, Object> getCurrentLayer() {
//        return null;
//    }
//
//    /**
//     * Note: start的时候不更改树中节点的状态，finish的时候同步状态，这样可以支持并发执行
//     * 例如:   A>(B,(C>D))>(E>F) 的执行过程为：
//     *      A.start();          // 任务启动 此时只为A提供必要的参数和做好相关记录
//     *      A.end();            // 任务结束 同步当前sinkObjects的值
//     *      (B,(C>D)).start()   // 并发列表任务启动，创建父级的参数,并发启动B、C>D
//     *          B.start()       // 启动B任务
//     *          B.end()         // 同步数据给父级
//     *          (C>D).start()   // 启动C>D任务
//     *              C.start()
//     *              C.end()
//     *              D.start()
//     *              D.end()
//     *          (C>D).end()
//     *          B.end()
//     *
//     */
//    @Override
//    public CReadOnlyData<String, Object> start(CReadOnlyData<String, Object> attachment, CExecutorRuntimeType type, CExecutor executor) {
//        return null;
//    }
//
//    @Override
//    public void finish(CReadOnlyData<String, Object> id) {
//
//    }
//}
