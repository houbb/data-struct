package com.github.houbb.data.struct.core.util.graph;

import com.github.houbb.data.struct.core.util.graph.component.Edge;
import com.github.houbb.data.struct.core.util.graph.component.GraphNode;
import com.github.houbb.heaven.util.guava.Guavas;

import java.util.Iterator;
import java.util.List;

/**
 * 链表实现的有向图
 *
 * 邻接链表（Adjacency List）实现的有向图
 *
 * TODO：首先学习 dag+DP 实现精确分词
 * 然后学习 map==> 实现 cache
 *
 * @author binbin.hou
 * @since 0.0.2
 */
public class ListDirectGraph<V> implements IDirectGraph<V> {

    /**
     * 节点链表
     * @since 0.0.2
     */
    private List<GraphNode<V>> nodeList;

    /**
     * 初始化有向图
     * @since 0.0.2
     */
    public ListDirectGraph() {
        this.nodeList = Guavas.newArrayList();
    }

    @Override
    public void addVertex(V v) {
        GraphNode<V> node = new GraphNode<>(v);

        // 直接加入到集合中
        this.nodeList.add(node);
    }

    @Override
    public boolean removeVertex(V v) {
        //1. 移除一个顶点
        //2. 所有和这个顶点关联的边也要被移除
        Iterator<GraphNode<V>> iterator = nodeList.iterator();
        while (iterator.hasNext()) {
            GraphNode<V> graphNode = iterator.next();

            if(v.equals(graphNode.getVertex())) {
                iterator.remove();
            }
        }

        return true;
    }

    @Override
    public V getVertex(int index) {
        return nodeList.get(index).getVertex();
    }

    @Override
    public void addEdge(Edge<V> edge) {
        //1. 新增一条边，直接遍历列表。
        // 如果存在这条的起始节点，则将这条边加入。
        // 如果不存在，则直接报错即可。

        for(GraphNode<V> graphNode : nodeList) {
            V from = edge.getFrom();
            V vertex = graphNode.getVertex();

            // 起始节点在开头
            if(from.equals(vertex)) {
                graphNode.getEdgeSet().add(edge);
            }
        }
    }

    @Override
    public boolean removeEdge(Edge<V> edge) {
        // 直接从列表中对应的节点，移除即可
        GraphNode<V> node = getGraphNode(edge);
        if(null != node) {
            // 移除目标为 to 的边
            node.remove(edge.getTo());
        }

        return true;
    }

    @Override
    public Edge<V> getEdge(int from, int to) {
        // 获取开始和结束的顶点
        V toVertex = getVertex(from);

        // 获取节点
        GraphNode<V> fromNode = nodeList.get(from);
        // 获取对应结束顶点的边
        return fromNode.get(toVertex);
    }

    /**
     * 获取图节点
     * @param edge 边
     * @return 图节点
     */
    private GraphNode<V> getGraphNode(final Edge<V> edge) {
        for(GraphNode<V> node : nodeList) {
            final V from = edge.getFrom();

            if(node.getVertex().equals(from)) {
                return node;
            }
        }

        return null;
    }

    /**
     * 广度优先遍历
     *
     * https://www.cnblogs.com/brucekun/p/8503042.html
     *
     * 队列中的元素为灰色，未访问过的为白色，已访问的为黑色。
     *
     * （1）根节点放入队列尾部，为灰色
     * （2）获取队列的收个元素（第一次就是取出 root），然后获取所有的连接边节点。
     *  将访问过的节点设置为黑色（加入队列中）
     *
     * @return 结果列表
     * @since 0.0.2
     */
    @Override
    public List<V> bfs() {
        return null;
    }

}
