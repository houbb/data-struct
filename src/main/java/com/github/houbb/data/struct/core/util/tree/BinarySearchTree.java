package com.github.houbb.data.struct.core.util.tree;

import com.github.houbb.data.struct.core.util.tree.component.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树
 *
 * @author binbin.hou
 * @since 0.0.4
 */
public class BinarySearchTree<V extends Comparable<? super V>> implements ISortTree<V> {

    /**
     * 根节点
     *
     * @since 0.0.4
     */
    private TreeNode<V> root;

    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * * Returns true if the given target is in the binary tree.
     * * Uses a recursive helper.
     * * @since 0.0.4
     */
    public boolean contains(V data) {
        return (contains(root, data));
    }


    /**
     * Inserts the given data into the binary tree.
     * Uses a recursive helper.
     * * @param data 待插入元素
     */
    public void add(V data) {
        root = add(root, data);
    }


    @Override
    public int size() {
        return size(root);
    }

    @Override
    public int maxDepth() {
        return maxDepth(root);
    }

    @Override
    public V minValue() {
        TreeNode<V> current = root;
        while (current != null && current.getLeft() != null) {
            current = current.getLeft();
        }

        return current.getData();
    }

    @Override
    public V maxValue() {
        TreeNode<V> current = root;
        while (current != null && current.getRight() != null) {
            current = current.getRight();
        }

        return current.getData();
    }

    @Override
    public List<V> inOrder() {
        List<V> list = new ArrayList<>();
        inOrder(list, root);
        return list;
    }

    private void inOrder(List<V> list, TreeNode<V> treeNode) {
        if (treeNode == null) return;
        inOrder(list, treeNode.getLeft());
        list.add(treeNode.getData());
        inOrder(list, treeNode.getRight());
    }

    @Override
    public List<V> preOrder() {
        List<V> list = new ArrayList<>();
        preOrder(list, root);
        return list;
    }

    private void preOrder(List<V> list, TreeNode<V> treeNode) {
        if (treeNode == null) return;
        list.add(treeNode.getData());
        preOrder(list, treeNode.getLeft());
        preOrder(list, treeNode.getRight());
    }

    @Override
    public List<V> postOrder() {
        List<V> list = new ArrayList<>();
        postOrder(list, root);
        return list;
    }

    private void postOrder(List<V> list, TreeNode<V> treeNode) {
        if (treeNode == null) return;
        list.add(treeNode.getData());
        postOrder(list, treeNode.getLeft());
        postOrder(list, treeNode.getRight());
    }

    @Override
    public List<V> levelOrder() {
        List<V> result = new ArrayList<>();
        TreeNode<V> node = root;

        Queue<TreeNode<V>> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            node = queue.poll();

            result.add(node.getData());

            if (node.getLeft() != null)
                queue.add(node.getLeft());
            if (node.getRight() != null)
                queue.add(node.getRight());
        }

        return result;
    }

    @Override
    public List<List<V>> pathList() {
        List<List<V>> result = new ArrayList<>();
        List<V> path = new ArrayList<>();
        pathList(root, result, path, 0);

        return result;
    }

    /**
     * 打印思路
     *
     * ------5
     * -----/-\
     * ----4---8
     * ---/---/-\
     * --11  13  4
     * -/--\      \
     * 7----2      1
     *
     *  1. 首先获取一共多少层。可以实时计算，也可以设置一个变量。
     *  2.
     */
    @Override
    public void print() {

    }

    // 2
    // 1 3
    private void pathList(TreeNode<V> node, List<List<V>> result, List<V> path, int pathLength) {
        if (node == null) {
            return;
        }

        // 这里通过设置的方式
        if (path.size() > pathLength) {
            path.set(pathLength, node.getData());
        } else {
            path.add(node.getData());
        }
        pathLength++;
        // 左右节点都不存在，则说明是叶子节点
        if (node.getRight() == null && node.getLeft() == null) {
            List<V> newList = new ArrayList<>(pathLength);
            for (int i = 0; i < pathLength; i++) {
                newList.add(path.get(i));
            }
            result.add(newList);
        } else {
            // 尝试两个子树
            pathList(node.getLeft(), result, path, pathLength);
            pathList(node.getRight(), result, path, pathLength);
        }

    }


    private int maxDepth(TreeNode<V> node) {
        if (node == null) {
            return (0);
        } else {
            int lDepth = maxDepth(node.getLeft());
            int rDepth = maxDepth(node.getRight());

            // use the larger + 1
            return (Math.max(lDepth, rDepth) + 1);
        }
    }

    /**
     * 递归获取 size
     *
     * @param node 节点
     * @return 结果
     * @since 0.0.4
     */
    private int size(TreeNode<V> node) {
        if (node == null) {
            return (0);
        }
        return (size(node.getLeft()) + 1 + size(node.getRight()));
    }

    /**
     * 递归查询
     *
     * @param node 节点
     * @param data 元素
     * @return 是否包含
     * @since 0.0.4
     */
    private boolean contains(TreeNode<V> node, V data) {
        if (node == null) {
            return false;
        }

        if (node.getData().compareTo(data) == 0) {
            return true;
        } else if (data.compareTo(node.getData()) < 0) {
            // 小于节点，则查询左子树
            return (contains(node.getLeft(), data));
        } else {
            // 大于节点，则查询右子树
            return (contains(node.getRight(), data));
        }
    }

    /**
     * Recursive insert -- given a node pointer, recur down and
     * insert the given data into the tree. Returns the new
     * node pointer (the standard way to communicate
     * a changed pointer back to the caller).
     *
     * @param node 节点
     * @param data 数据
     * @since 0.0.4
     */
    private TreeNode<V> add(TreeNode<V> node, V data) {
        // 如果节点为空，则插入到当前位置
        if (node == null) {
            node = new TreeNode<>(data);
        } else {
            // 优先插在左边
            if (data.compareTo(node.getData()) <= 0) {
                node.setLeft(add(node.getLeft(), data));
            } else {
                node.setRight(add(node.getRight(), data));
            }
        }

        // in any case, return the new pointer to the caller
        return node;
    }

}
