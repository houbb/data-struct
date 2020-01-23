package com.github.houbb.data.struct.core.util.graph;

import com.github.houbb.data.struct.core.util.graph.component.Edge;
import org.junit.Test;

import java.util.*;

/**
 * 链表实现的有向图
 *
 * @author binbin.hou
 * @since 0.0.2
 */
public class ListDirectGraphTest {

    @Test
    public void bfsTest() {
        IDirectGraph<String> directGraph = new ListDirectGraph<>();

        //1. 初始化顶点
        directGraph.addVertex("1");
        directGraph.addVertex("2");
        directGraph.addVertex("3");
        directGraph.addVertex("4");
        directGraph.addVertex("5");
        directGraph.addVertex("6");
        directGraph.addVertex("7");
        directGraph.addVertex("8");

        //2. 初始化边
        directGraph.addEdge(new Edge<>("1", "2"));
        directGraph.addEdge(new Edge<>("1", "3"));
        directGraph.addEdge(new Edge<>("2", "4"));
        directGraph.addEdge(new Edge<>("2", "5"));
        directGraph.addEdge(new Edge<>("3", "6"));
        directGraph.addEdge(new Edge<>("3", "7"));
        directGraph.addEdge(new Edge<>("4", "8"));
        directGraph.addEdge(new Edge<>("8", "5"));
        directGraph.addEdge(new Edge<>("6", "7"));

        //3. BFS 遍历
        List<String> bfsList = directGraph.bfs("1");
        System.out.println(bfsList);

        //4. DFS 遍历
        List<String> dfsList = directGraph.dfs("1");
        System.out.println(dfsList);
    }

}
