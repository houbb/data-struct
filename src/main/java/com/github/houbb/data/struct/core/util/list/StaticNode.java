package com.github.houbb.data.struct.core.util.list;

/**
 * 静态节点
 * @author binbin.hou
 * @param <E> 泛型
 * @since 0.0.3
 */
class StaticNode<E> {

    /**
     * 当前元素节点
     * @since 0.0.3
     */
    private E value;

    /**
     * 游标
     * @since 0.0.3
     */
    private int cursor;

    public StaticNode(E value, int cursor) {
        this.value = value;
        this.cursor = cursor;
    }

    public E getValue() {
        return value;
    }

    public int getCursor() {
        return cursor;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }
}
