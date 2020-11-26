package com.github.houbb.data.struct.core.util.tree.component;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class PrintTreeNode<V extends Comparable<? super V>> {

    private V data;

    /**
     * 左节点
     */
    private boolean isLeft;

    /**
     * 右节点
     */
    private boolean isRight;

    /**
     * 是否为最后一个元素
     */
    private boolean isEndLine;

    /**
     * 当前层级
     */
    private int level;

    /**
     * x 轴的偏移量
     */
    private int offset;

    public V data() {
        return data;
    }

    public PrintTreeNode<V> data(V data) {
        this.data = data;
        return this;
    }

    public boolean left() {
        return isLeft;
    }

    public PrintTreeNode<V> left(boolean left) {
        isLeft = left;
        return this;
    }

    public boolean right() {
        return isRight;
    }

    public PrintTreeNode<V> right(boolean right) {
        isRight = right;
        return this;
    }

    public boolean endLine() {
        return isEndLine;
    }

    public PrintTreeNode<V> endLine(boolean endLine) {
        isEndLine = endLine;
        return this;
    }

    public int level() {
        return level;
    }

    public PrintTreeNode<V> level(int level) {
        this.level = level;
        return this;
    }

    public int offset() {
        return offset;
    }

    public PrintTreeNode<V> offset(int offset) {
        this.offset = offset;
        return this;
    }

}
