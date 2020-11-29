package com.github.houbb.data.struct.core.util.tree;

import org.junit.Test;

/**
 * 红黑树测试
 *
 * @author binbin.hou
 * @since 0.0.5
 */
public class RedBlackTreeTest {

    private static final int a[] = {10, 40, 30, 60, 90, 70, 20, 50, 80};

    @Test
    public void helloTest() {
        ISortTree<Integer> tree = new RedBlackTree<>();

        for (int i = 0; i < a.length; i++) {
            tree.add(a[i]);
        }

        System.out.println("树的详细信息");
        tree.print();

        // 删除
        tree.remove(10);
        System.out.println("删除 10 之后的详细信息：");
        tree.print();
    }

}
