package com.github.houbb.data.struct.core.util.graph.shortestpath;

import com.github.houbb.data.struct.core.util.graph.shortestpath.impl.DijkstraShortestPath;
import org.junit.Test;

/**
 * @author binbin.hou
 * @since 0.0.3
 */
public class DijkstraShortestPathTest {

    @Test
    public void shortestPathTest() {
        final int M = 10000; // 代表正无穷

        // 二维数组每一行分别是 A、B、C、D、E 各点到其余点的距离,
        // A -> A 距离为0, 常量M 为正无穷
        int[][] graph = {
                {0, 4, M, 2, M},
                {4, 0, 4, 1, M},
                {M, 4, 0, 1, 3},
                {2, 1, 1, 0, 7},
                {M, M, 3, 7, 0}
        };

        int start = 0;

        IShortestPath shortestPath = new DijkstraShortestPath();
        int[] shortPath = shortestPath.shortestPath(graph, start);

        for (int i = 0; i < shortPath.length; i++) {
            System.out.println("从" + start + "出发到" + i + "的最短距离为：" + shortPath[i]);
        }
    }

}