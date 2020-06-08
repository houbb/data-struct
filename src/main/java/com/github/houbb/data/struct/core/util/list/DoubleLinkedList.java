package com.github.houbb.data.struct.core.util.list;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 双向链表
 * @author binbin.hou
 * @since 0.0.1
 */
public class DoubleLinkedList<E> implements List<E> {

    /**
     * 元素大小
     */
    private int size;

    /**
     * 头节点
     */
    private Node<E> head;

    /**
     * 尾巴节点
     */
    private Node<E> tail;

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 遍历整个链表，判断是否存在该元素
     * （1）二者不为空，且相等
     * （2）对象为 null，且链表中也有元素为 null
     *
     * （3）如果链表为 null，直接返回 false。
     * @param o 目标元素
     * @return 是否存在
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) > -1;
    }

    /**
     * 是否包含所有
     * （1）如果结合为 null，则认为不包含。
     * （2）循环遍历，如果有一个不存在，则认为不包含。
     * @param c 集合
     * @return 是否包含所有。
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        if(CollectionUtil.isEmpty(c)) {
            return false;
        }

        for(Object o : c) {
            if(!this.contains(o)) {
                return false;
            }
        }
        return true;
    }



    /**
     * 添加一个元素
     *
     * @param e 元素
     * @return 是否添加成功
     */
    @Override
    public boolean add(E e) {
        // 直接添加在尾部。
        Node<E> originalTail = this.tail;

        // 新节点的下一个为空。上一个为原来的尾巴节点
        // 最新的节点成为尾巴节点
        Node<E> newNode = new Node<>(e, originalTail, null);
        this.tail = newNode;

        // 判断是否为空
        if(originalTail == null) {
            this.head = newNode;
        } else {
            // 将新元素链接到最后段
            originalTail.next(newNode);
        }

        // 增加 size
        size++;
        return true;
    }

    /**
     * 移除一个元素
     * 整体流程：从头开始遍历，对比二者的内容是否相同。
     *（1）如果列表为空，直接返回 false。
     *（2）如果元素不存在，则返回 false。
     *
     * 移除方式：
     * 直接将移除节点的前一个节点,next 设置为待移除节点的 next。
     * @param o 元素
     * @return 是否移除成功
     */
    @Override
    public boolean remove(Object o) {
        if(ObjectUtil.isNull(head)) {
            return false;
        }

        for (Node<E> node = head; node != null; node = node.next()) {
            E value = node.value();

            if(ObjectUtil.isEqualsOrNull(o, value)) {
                Node<E> previous = node.previous();
                Node<E> next = node.next();

                //1. 如果移除的是 head 节点
                if(ObjectUtil.isNull(previous)) {
                    this.head = next;
                    this.head.previous(null);
                }
                if(ObjectUtil.isNull(next)) {
                    //2. 如果移除的是 tail 节点
                    this.tail = previous;
                    this.tail.next(null);
                }
                if(ObjectUtil.isNotNull(previous)
                    && ObjectUtil.isNotNull(next)) {
                    //3. 中间节点
                    previous.next(next);
                    next.previous(previous);
                }

                size--;
                return true;
            }
        }

        return false;
    }



    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    /**
     * @see #addAll(Collection) 在末尾添加所有。
     * @param index 下标
     * @param c 元素集合
     * @return 是否添加成功
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    /**
     * @see #add(Object) 在末尾添加元素
     * @param index 下标
     * @param element 元素
     */
    @Override
    public void add(int index, E element) {
        // 指定位置。
    }

    /**
     * 移除指定位置的元素
     * @param index 下标
     * @return 移除的元素结果。如果没移除，返回 null。
     */
    @Override
    public E remove(int index) {
        // 范围校验

        // 循环
        Node<E> node = head;
        for(int i = 0; i < index; i++) {
            node = node.next();
        }

        // 移除元素
        E elem = node.value();


        return elem;
    }

    /**
     * @see #contains(Object) 是否包含
     * @param o 对象
     * @return 是否包含
     */
    @Override
    public int indexOf(Object o) {
        if(ObjectUtil.isNull(this.head)) {
            return -1;
        }

        // 从前向后遍历链表
        int i = 0;
        for(Node<E> node = head; node != null; node = node.next()) {
            E value = node.value();
            if(ObjectUtil.isEqualsOrNull(value, o)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(ObjectUtil.isNull(this.tail)) {
            return -1;
        }

        // 从后向前遍历链表
        int i = size-1;
        for(Node<E> node = tail; node != null; node = node.previous()) {
            E value = node.value();
            if(ObjectUtil.isEqualsOrNull(value, o)) {
                return i;
            }
            i--;
        }
        return -1;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

}
