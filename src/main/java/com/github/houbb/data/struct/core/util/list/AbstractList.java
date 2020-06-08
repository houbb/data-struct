package com.github.houbb.data.struct.core.util.list;

/**
 * @author binbin.hou
 * @since 0.0.3
 */
public abstract class AbstractList<E> extends AbstractListAdaptor<E> {

    /**
     * 大小
     * @since 0.0.3
     */
    protected int size;

    /**
     * 头结点
     * @since 0.0.3
     */
    protected Node<E> head;

    /**
     * 尾巴节点
     *
     * 作用：避免新增的时候，重复遍历。
     * @since 0.0.3
     */
    protected Node<E> tail;

    public AbstractList() {
        this.init();
    }

    /**
     * 初始化列表
     * @since 0.0.3
     */
    private void init() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * 添加元素
     * @param e 元素
     * @return 是否成功
     * @since 0.0.3
     */
    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }


    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index < 0) {
            return false;
        }

        this.remove(index);
        return true;
    }

    @Override
    public int indexOf(Object o) {
        Node<E> node = this.head;

        for(int i = 0 ; i < this.size; i++) {
            E value = node.value();

            // 这里是否考虑 NULL？暂时忽略
            if(value.equals(o)) {
                return i;
            }

            node = node.next();
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        Node<E> node = this.head;

        int index = -1;

        for(int i = 0 ; i < this.size-1; i++) {
            E value = node.value();

            // 遍历循环，从而保证是最后一个
            // 后期可以使用双向链表，优化这个操作。
            if(value.equals(o)) {
                index = i;
            }

            node = node.next();
        }

        return index;
    }

    @Override
    public E get(int index) {
        Node<E> node = getIndexNode(index);
        return node.value();
    }

    @Override
    public E set(int index, E element) {
        rangeCheck(index);

        // 设置元素即可
        Node<E> currentNode = getIndexNode(index);
        E oldValue = currentNode.value();

        // 更新
        currentNode.value(element);
        return oldValue;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size <= 0;
    }

    @Override
    public void clear() {
        this.init();
    }

    /**
     * 从 head 开始循环遍历，获取对应的节点
     * @param index 下标
     * @return 节点
     * @since 0.0.3
     */
    protected Node<E> getIndexNode(final int index) {
        rangeCheck(index);

        Node<E> previousNode = this.head;
        for(int i = 0; i < index; i++) {
            previousNode = previousNode.next();
        }

        return previousNode;
    }

    /**
     * 范围校验
     * @param index 索引
     * @since 0.0.3
     */
    protected void rangeCheck(int index) {
        if(index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }

}
