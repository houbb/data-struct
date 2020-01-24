package com.github.houbb.data.struct.core.util.graph.shortestpath;

/**
 * 最短路径接口
 * @author binbin.hou
 * @since 0.0.3
 */
public interface IShortestPath {

    /**
     * 最短最短路径列表
     *
     * 接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）
     * 返回一个int[] 数组，表示从start到它的最短路径长度
     *
     * TODO: 这里需要一个将矩阵转换为 graph 对象的方法。
     *
     * @param graph 图信息的邻接矩阵标识
     * @param start 开始的节点位置
     * @return 结果
     * @since 0.0.1
     */
    int[] shortestPath(int[][] graph, int start);

}
