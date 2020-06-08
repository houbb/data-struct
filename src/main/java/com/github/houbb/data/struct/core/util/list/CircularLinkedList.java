package com.github.houbb.data.struct.core.util.list;

import java.util.Iterator;
import java.util.List;

/**
 * 循环链表
 *
 * [(java实现)单向循环链表](https://www.cnblogs.com/sang-bit/p/11610181.html)
 *
 * 解决问题：约瑟夫环问题
 * @author binbin.hou
 * @since 0.0.3
 */
public class CircularLinkedList<E> extends AbstractList<E> {

    @Override
    public boolean add(E e) {
        Node<E> oldTail = this.tail;

        Node<E> newNode = new Node<>(e);
        this.tail = newNode;

        if(oldTail == null) {
            // 新元素
            this.head = newNode;
        } else {
            // 连接到链表的最后
            oldTail.next(this.tail);
        }

        // 指向头部元素
        this.tail.next(this.head);

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
            this.tail.next(head);
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
            // 更新尾巴节点到头结点的引用
            this.tail.next(this.head);
        } else {
            //2. 中间
            Node<E> previousNode = getIndexNode(index-1);
            Node<E> removeNode = previousNode.next();
            result = removeNode.value();

            previousNode.next(removeNode.next());

            //3. 如果删除的是最后一个节点
            if(index == this.size - 1) {
                this.tail = previousNode;
                this.tail.next(this.tail);
            }
        }

        size--;
        return result;
    }

    public static void main(String[] args) {
        CircularLinkedList<Integer> list = new CircularLinkedList<>();

        //1. 初始化
        final int total = 41;
        final int space = 3;
        for(int i = 0; i < total; i++) {
            list.add(i+1);
        }

        // 筛选（找到第三个元素，并且移除，直到列表为空）
        Node<Integer> node = list.head;

        List<Integer> removeList = new ArrayList<>();
        while (!list.isEmpty()) {
            for(int i = 0; i < space; i++) {
                node = node.next();
            }

            Integer value = node.value();
            removeList.add(value);

            if(removeList.size() >= total) {
                break;
            }

            // 移除
            list.remove(value);
        }

        System.out.println(removeList);
    }

}
