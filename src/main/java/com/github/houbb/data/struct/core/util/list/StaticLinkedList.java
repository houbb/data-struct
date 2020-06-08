package com.github.houbb.data.struct.core.util.list;

/**
 * 静态链表
 *
 * 1. 对于没有指针的语言，使用数组模拟指针
 * 2. 暂时不做扩容的支持
 *
 * @author binbin.hou
 * @since 0.0.3
 */
public class StaticLinkedList<E> extends AbstractListAdaptor<E> {

    /**
     * 列表大小
     * @since 0.0.3
     */
    private int size;

    /**
     * 存放元素的数组
     * @since 0.0.3
     */
    private transient StaticNode[] array;

    /**
     * 针对数组特殊处理
     *
     * 1. 第一个数组素存放未使用元素的下表
     * 2. 最后一个数组存放第一个插入元素的下标志，类似于头结点
     * @param capacity 容量
     * @since 0.0.3
     */
    public StaticLinkedList(final int capacity) {
        array = new StaticNode[capacity+2];
        this.size = 0;

        array[0] = new StaticNode(null, 1);
        // 第一个元素的位置下标是 1
        array[array.length-1] = new StaticNode(null, 1);
    }

    public StaticLinkedList() {
        this(8);
    }

    @Override
    public boolean add(E e) {
        // 暂时不做扩容，过大。
        if(size >= array.length - 2) {
            return false;
        }

        int cursor = array[0].getCursor();
        int nextCursor = cursor+1;
        array[cursor] = new StaticNode(e, nextCursor);
        // 更新下一个空闲下标
        array[0].setCursor(nextCursor);

        size++;
        return true;
    }

    @Override
    public void add(int index, E element) {
        rangeCheck(index);

        // [a] [b] [c] [d] {E}
        // {E}.cursor = [b].cursor;
        // {E} 插入 [b] 后，则修改 [b].cursor={E}.index;

        // 获取新的位置的下标
        StaticNode<E> newNode;
        int cursor = array[0].getCursor();
        // 第一个位置
        if(index == 0) {
            //1. 新元素的节点.cursor 第一个元素下标志
            int oldFirstIndex = getPreviousIndex(0);
            newNode = new StaticNode<>(element, oldFirstIndex);
            //2. 更新头指针 cursor
            array[array.length-1].setCursor(cursor);;
        } else {
            int previousIndex = getPreviousIndex(index - 1);
            int oldNextCursor = array[previousIndex].getCursor();
            newNode = new StaticNode<>(element, oldNextCursor);
            array[previousIndex].setCursor(cursor);
        }

        array[cursor] = newNode;
        size++;
    }

    /**
     * [a] [b] [c] [d]
     *
     * 比如像删除 [b]
     *
     * 1. 下一个空元素，设置为待删除的 cursor
     * 2. space[0]cursor = delete.index;
     *
     * ---------------------------------
     * 普通删除：
     * [a].cursor = [b].cursor;
     * 删除第一个节点：
     * [lastIndex].cursor = [b].index;
     * @param index 下标
     * @return 结果
     */
    @Override
    public E remove(int index) {
        rangeCheck(index);

        if(index == 0) {
            // 下一个元素
            int nextIndex = array[index].getCursor();
            array[array.length-1].setCursor(nextIndex);
        } else {
            StaticNode previousNode = getPreviousNode(index-1);

            // 设置为下一个元素的 cursor
            previousNode.setCursor(array[previousNode.getCursor()].getCursor());
        }

        // 释放被删除的元素
        // 旧的 cursor 设置为删除节点的下一个空元素位置
        array[index].setCursor(array[0].getCursor());
        // 更新为最新的空列表位置
        array[0].setCursor(index);

        size--;
        return super.remove(index);
    }

    /**
     * 获取前一个元素的位置
     * @param index 标志
     * @return 结果
     * @since 0.0.3
     */
    private int getPreviousIndex(final int index) {
        int firstIndex = array[array.length-1].getCursor();

        for(int i = 0; i < index; i++) {
            // 一次寻找下一个元素
            firstIndex = array[firstIndex].getCursor();
        }

        return firstIndex;
    }

    /**
     * 获取前一个元素
     * @param index 标志
     * @return 结果
     * @since 0.0.3
     */
    private StaticNode getPreviousNode(final int index) {
        StaticNode staticNode = array[array.length-1];

        for(int i = 0; i < index; i++) {
            // 一次寻找下一个元素
            staticNode = array[staticNode.getCursor()];
        }

        return staticNode;
    }


    /**
     * 范围校验
     * @param index 索引
     * @since 0.0.3
     */
    private void rangeCheck(int index) {
        if(index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }
}