package com.github.houbb.data.struct.core.util.tree.component;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class TreeNode<V extends Comparable<? super V>> {

    /**
     * 左节点
     * @since 0.0.4
     */
    private TreeNode<V> left;

    /**
     * 右节点
     * @since 0.0.4
     */
    private TreeNode<V> right;

    /**
     * 数据信息
     * @since 0.0.4
     */
    private V data;

    public TreeNode(V data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public TreeNode<V> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<V> left) {
        this.left = left;
    }

    public TreeNode<V> getRight() {
        return right;
    }

    public void setRight(TreeNode<V> right) {
        this.right = right;
    }

    public V getData() {
        return data;
    }

    public void setData(V data) {
        this.data = data;
    }
}
