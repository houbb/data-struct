package com.github.houbb.data.struct.core.util.list;

/**
 * 链表节点
 * @author binbin.hou
 * @since 0.0.1
 * @param <E> 泛型
 */
class Node<E> {

    /**
     * 当前元素节点
     */
    private E value;

    /**
     * 前一个节点
     */
    private Node<E> previous;

    /**
     * 后一个节点
     */
    private Node<E> next;

    /**
     * @param value 值
     * @since 0.0.3
     */
    public Node(E value) {
        this(value, null);
    }

    /**
     *
     * @param value 值
     * @param previous 前一个元素
     * @since 0.0.3
     */
    public Node(E value, Node<E> previous) {
        this(value, previous, null);
    }

    public Node(E value, Node<E> previous, Node<E> next) {
        this.value = value;
        this.previous = previous;
        this.next = next;
    }

    public E value() {
        return value;
    }

    public Node<E> value(E value) {
        this.value = value;
        return this;
    }

    public Node<E> previous() {
        return previous;
    }

    public Node<E> previous(Node<E> previous) {
        this.previous = previous;
        return this;
    }

    public Node<E> next() {
        return next;
    }

    public Node<E> next(Node<E> next) {
        this.next = next;
        return this;
    }
}
