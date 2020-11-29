package com.github.houbb.data.struct.core.util.tree;

import org.junit.Test;

/**
 * @author binbin.hou
 * @since 0.0.4
 */
public class BinarySearchTreeTest {

    @Test
    public void build123Test() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(2);
        tree.add(1);
        tree.add(3);

        System.out.println(tree.inOrder());
        System.out.println(tree.preOrder());

        System.out.println(tree.postOrder());
        System.out.println(tree.levelOrder());

        System.out.println(tree.getMinValue());
        System.out.println(tree.getMaxValue());
        System.out.println(tree.getHeight());
        System.out.println(tree.getSize());

        System.out.println(tree.contains(2));
        System.out.println(tree.contains(5));
    }

    //1 2 3 4 5 6 7 8
    @Test
    public void rootToLeafPathsTest() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(2);
        tree.add(7);
        tree.add(1);
        tree.add(3);
        tree.add(6);
        tree.add(9);

        System.out.println(tree.pathList());
    }

    //1 2 3 4 5 6 7 8
    @Test
    public void print2Test() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(2);
        tree.add(7);
        tree.add(1);
        tree.add(3);
        tree.add(6);
        tree.add(9);

        System.out.println(tree.inOrder());
        System.out.println(tree.levelOrder());
    }

    //1 2 3 4 5 6 7 8
    @Test
    public void printTest() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(5);
        tree.add(2);
        tree.add(7);
        tree.add(1);
        tree.add(3);
        tree.add(6);
        tree.add(9);

        tree.print();

//        tree.remove(9);
//        System.out.println("After Remove 9: ");
//        tree.print();
//
//        tree.remove(7);
//        System.out.println("After Remove 7: ");
//        tree.print();
//
//        tree.remove(2);
//        System.out.println("After Remove 2: ");
//        tree.print();
//
//        tree.remove(5);
//        System.out.println("After Remove 5: ");
//        tree.print();
    }

    //1 2 3 4 5 6 7 8
    @Test
    public void print3Test() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(1);
        tree.add(2);
        tree.add(3);
        tree.add(4);
        tree.add(5);
        tree.add(6);
        tree.add(7);

        tree.print();
    }


}
