package com.github.houbb.data.struct.core.util.list;

/**
 * 单链表
 * @author binbin.hou
 * @since 0.0.3
 */
public class SingleLinkedList<E> extends AbstractList<E> {

    /**
     * 添加元素
     * @param e 元素
     * @return 是否成功
     * @since 0.0.3
     */
    @Override
    public boolean add(E e) {
        Node<E> oldTail = this.tail;

        // 新节点创建, next 为空
        // 新节点成为新的 tail
        Node<E> newNode = new Node<>(e, null, null);
        this.tail = newNode;

        // 如果原始列表为空
        if(oldTail == null) {
            this.head = newNode;
        } else {
            // 元素放在原始末尾节点之后
            oldTail.next(this.tail);
        }

        size++;
        return true;
    }

    @Override
    public void add(int index, E element) {
        rangeCheck(index);

        // 插入末尾
        if(index == this.size) {
            this.add(element);
            return;
        }

        // 插入到了开头 && 列表不为空
        if(0 == index) {
            this.head = new Node<>(element, null, this.head);
        } else {
            // 循环找到需要插入的位置
            Node<E> previousNode = getIndexNode(index-1);
            Node<E> oldNextNode = previousNode.next();
            Node<E> newNode = new Node<>(element, null, oldNextNode);
            previousNode.next(newNode);
        }

        size++;
    }


    @Override
    public E remove(int index) {
        rangeCheck(index);

        //1. 头结点
        E result = null;
        if(index == 0) {
            // 直接忽略旧的 head
            result = this.head.value();
            this.head = this.head.next();
        } else {
            //2. 中间
            Node<E> previousNode = getIndexNode(index-1);
            Node<E> removeNode = previousNode.next();
            result = removeNode.value();

            previousNode.next(removeNode.next());

            //3. 如果删除的是最后一个节点
            if(index == this.size - 1) {
                this.tail = previousNode;
            }
        }

        size--;
        return result;
    }

}
