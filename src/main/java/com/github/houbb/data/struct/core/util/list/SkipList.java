package com.github.houbb.data.struct.core.util.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 老马啸西风
 * @since 0.0.4
 */
public class SkipList<E> {

    /**
     * 最大层级
     * @since 0.0.4
     */
    private int maxLevel;

    /**
     * 当前层级（记录跳表中真实的最高层级）
     * @since 0.0.4
     */
    private int currentLevel;

    /**
     * 表头
     */
    private SkipListNode<E> head;

    /**
     * 表尾
     * @since 0.0.4
     */
    private SkipListNode<E> NIL;

    /**
     * 概率值
     * @since 0.0.4
     */
    private double p;

    /**
     * 作者建议的概率
     * @since 0.25
     */
    private static final double BEST_P = 0.25;

    /**
     * 默认值
     */
    public SkipList() {
        this(BEST_P, (int)Math.ceil(Math.log(Integer.MAX_VALUE) / Math.log(1 / BEST_P)) - 1);
    }

    /**
     * 指定概率和最大层级
     * @param probability 可能性
     * @param maxLevel 最大层级
     */
    public SkipList(double probability, int maxLevel) {
        this.p = probability;
        this.maxLevel = maxLevel;

        this.currentLevel = 1;
        this.head = new SkipListNode<>(Integer.MIN_VALUE, null, maxLevel);
        this.NIL = new SkipListNode<>(Integer.MAX_VALUE, null, maxLevel);

        //初始化一个新列表，以使列表的级别等于1，并且列表标题的所有前向指针都指向NIL
        for (int i = 0; i < maxLevel; i++) {
            this.head.forwards[i] = NIL;
        }
    }

    /**
     * 元素节点
     * @param <E>
     */
    private static class SkipListNode<E> {

        /**
         * key 信息
         * <p>
         * 这个是什么？index 吗？
         *
         * @since 0.0.4
         */
        int key;

        /**
         * 存放的元素
         */
        E value;

        /**
         * 向前的指针
         * <p>
         * 跳表是多层的，这个向前的指针，最多和层数一样。
         *
         * @since 0.0.4
         */
        SkipListNode<E>[] forwards;

        @SuppressWarnings("all")
        public SkipListNode(int key, E value, int maxLevel) {
            this.key = key;
            this.value = value;
            this.forwards = new SkipListNode[maxLevel];
        }

        @Override
        public String toString() {
            return "SkipListNode{" +
                    "key=" + key +
                    ", value=" + value +
                    ", forwards=" + Arrays.toString(forwards) +
                    '}';
        }
    }

    /**
     * 执行查询
     * @param searchKey 查找的 key
     * @return 结果
     * @since 0.0.4
     */
    public E search(final int searchKey) {
        // 从左边最上层开始向右
        SkipListNode<E> c = this.head;

        // 从已有的最上层开始遍历
        for(int i = currentLevel-1; i >= 0; i--) {
            while (c.forwards[i].key < searchKey) {
                // 当前节点在这一层直接向前
                c = c.forwards[i];
            }

            // 判断下一个元素是否满足条件
            if(c.forwards[i].key == searchKey) {
                return c.forwards[i].value;
            }
        }

        // 查询失败，元素不存在。
        return null;
    }

    /**
     * 插入元素
     *
     *
     * @param searchKey 查询的 key
     * @param newValue 元素
     * @since 0.0.4
     * @author 老马啸西风
     */
    @SuppressWarnings("all")
    public void insert(int searchKey, E newValue) {
        SkipListNode<E>[] updates = new SkipListNode[maxLevel];
        SkipListNode<E> curNode = this.head;

        for (int i = currentLevel - 1; i >= 0; i--) {
            while (curNode.forwards[i].key < searchKey) {
                curNode = curNode.forwards[i];
            }

            // curNode.key < searchKey <= curNode.forward[i].key
            updates[i] = curNode;
        }

        // 获取第一个元素
        curNode = curNode.forwards[0];
        if (curNode.key == searchKey) {
            // 更新对应的值
            curNode.value = newValue;
        } else {
            // 插入新元素
            int randomLevel = getRandomLevel();

            // 如果层级高于当前层级，则更新 currentLevel
            if (this.currentLevel < randomLevel) {
                for (int i = currentLevel; i < randomLevel; i++) {
                    updates[i] = this.head;
                }

                currentLevel = randomLevel;
            }

            // 构建新增的元素节点
            //head==>new  L-1
            //head==>pre==>new L-0
            SkipListNode<E> newNode = new SkipListNode<>(searchKey, newValue, randomLevel);
            for (int i = 0; i < randomLevel; i++) {
                newNode.forwards[i] = updates[i].forwards[i];
                updates[i].forwards[i] = newNode;
            }
        }
    }

    /**
     * 获取随机的级别
     * @return 级别
     * @since 0.0.4
     */
    private int getRandomLevel() {
        int lvl = 1;

        //Math.random() 返回一个介于 [0,1) 之间的数字
        while (lvl < this.maxLevel && Math.random() < this.p) {
            lvl++;
        }

        return lvl;
    }

    /**
     * 删除一个元素
     * @param searchKey 查询的 key
     * @since 0.0.4
     */
    @SuppressWarnings("all")
    public void delete(int searchKey) {
        SkipListNode<E>[] updates = new SkipListNode[maxLevel];
        SkipListNode<E> curNode = this.head;

        for (int i = currentLevel - 1; i >= 0; i--) {
            while (curNode.forwards[i].key < searchKey) {
                curNode = curNode.forwards[i];
            }
            // curNode.key < searchKey <= curNode.forward[i].key
            // 设置每一层对应的元素信息
            updates[i] = curNode;
        }

        // 最下面一层的第一个指向的元素
        curNode = curNode.forwards[0];
        if (curNode.key == searchKey) {
            for (int i = 0; i < currentLevel; i++) {
                if (updates[i].forwards[i] != curNode) {
                    break;
                }

                updates[i].forwards[i] = curNode.forwards[i];
            }

            // 移除无用的层级
            while (currentLevel > 0 && this.head.forwards[currentLevel-1] ==  this.NIL) {
                currentLevel--;
            }
        }
    }

    /**
     * 打印 list
     * @since 0.0.4
     */
    public void printList() {
        for (int i = currentLevel - 1; i >= 0; i--) {
            SkipListNode<E> curNode = this.head.forwards[i];
            System.out.print("HEAD->");
            while (curNode != NIL) {
                String line = String.format("(%s,%s)->", curNode.key, curNode.value);
                System.out.print(line);
                curNode = curNode.forwards[i];
            }
            System.out.println("NIL");
        }
    }

    public static void main(String[] args) {
        SkipList<String> list = new SkipList<>();
        list.insert(3, "耳朵听声音");
        list.insert(7, "镰刀来割草");
        list.insert(6, "口哨嘟嘟响");
        list.insert(4, "红旗迎风飘");
        list.insert(2, "小鸭水上漂");
        list.insert(9, "勺子能吃饭");
        list.insert(1, "铅笔细又长");
        list.insert(5, "秤钩来买菜");
        list.insert(8, "麻花扭一扭");
        list.printList();
        System.out.println("---------------");
        list.delete(3);
        list.delete(4);
        list.printList();

        System.out.println(list.search(8));
    }

}
