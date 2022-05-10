package com.cocofhu.ctb.kernel.core.config;

/**
 * 带有层级结构的数据集，实现该接口的类 需要满足新建和回退不同的层
 * 比如:
 * <pre>
 *      CLayerDataSet data = ...;
 *      // 新建层
 *      data.newLayer();
 *      // 该操作将会在最新的层上进行操作
 *      data.put("x","y");
 *      // "y"
 *      data.get("x");
 *      // 回退一层
 *      data.backLayer(1);
 *      // null 因为此时添加操作已经被回退
 *      data.get("x");
 * </pre>
 */
public interface CDefaultLayerDataSet extends CLayerDataSet<String, Object> {
}
