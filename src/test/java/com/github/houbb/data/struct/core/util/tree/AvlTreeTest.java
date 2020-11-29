package com.github.houbb.data.struct.core.util.tree;

import org.junit.Test;

/**
 * @author binbin.hou
 * @since 0.0.4
 */
public class AvlTreeTest {


    /**
     * ll-右旋测试
     */
    @Test
    public void llTest() {
        AvlTree<Integer> avlTree = new AvlTree<>();
        avlTree.add(3);
        avlTree.add(2);
        avlTree.add(1);
    }

    /**
     * rr-左旋测试
     */
    @Test
    public void rrTest() {
        AvlTree<Integer> avlTree = new AvlTree<>();
        avlTree.add(1);
        avlTree.add(2);
        avlTree.add(3);
    }

    /**
     * lr-左旋+右旋测试
     */
    @Test
    public void lrTest() {
        AvlTree<Integer> avlTree = new AvlTree<>();
        avlTree.add(3);
        avlTree.add(1);
        avlTree.add(2);
    }

    /**
     * rl-右旋+左旋测试
     */
    @Test
    public void rlTest() {
        AvlTree<Integer> avlTree = new AvlTree<>();
        avlTree.add(1);
        avlTree.add(3);
        avlTree.add(2);
    }

}
