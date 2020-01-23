package com.github.houbb.data.struct.core.util.graph;

import com.github.houbb.data.struct.api.IBFS;
import com.github.houbb.data.struct.api.IDFS;
import com.github.houbb.data.struct.core.util.graph.component.Edge;

/**
 * 有向图接口
 * 对于定点+边的操作：
 * （1）增加
 * （2）删除
 * （3）获取
 *
 * @author binbin.hou
 * @since 0.0.2
 */
public interface IDirectGraph<V> extends IBFS<V>, IDFS<V> {

    /**
     * 新增顶点
     * @param v 顶点
     * @since 0.0.2
     */
    void addVertex(final V v);

    /**
     * 删除顶点
     * @param v 顶点
     * @since 0.0.2
     * @return 是否删除成功
     */
    boolean removeVertex(final V v);

    /**
     * 获取顶点
     * @param index 下标
     * @since 0.0.2
     * @return 返回顶点信息
     */
    V getVertex(final int index);

    /**
     * 新增边
     * @param edge 边
     * @since 0.0.2
     */
    void addEdge(final Edge<V> edge);

    /**
     * 移除边
     * @param edge 边信息
     * @since 0.0.2
     * @return 是否成功
     */
    boolean removeEdge(final Edge<V> edge);

    /**
     * 获取边信息
     * @param from 开始节点
     * @param to 结束节点
     * @since 0.0.2
     * @return 边信息
     */
    Edge<V> getEdge(final int from, final int to);

}
