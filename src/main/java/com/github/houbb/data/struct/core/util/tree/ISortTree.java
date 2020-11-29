package com.github.houbb.data.struct.core.util.tree;

import java.util.List;

/**
 * 树接口
 * @author binbin.hou
 * @since 0.0.4
 */
public interface ISortTree<V extends Comparable<? super V>> {

    /**
     * 是否包含
     * @param data 元素
     * @return 是否包含
     * @since 0.0.4
     */
    boolean contains(V data);

    /**
     * 添加元素
     * @param data 元素
     * @since 0.0.4
     */
    void add(V data);

    /**
     * 删除节点
     * @param data 元素
     * @since 0.0.4
     * @return 是否删除
     */
    boolean remove(V data);

    /**
     * 返回元素的个数
     * @return 个数
     * @since 0.0.4
     */
    int getSize();

    /**
     * 是否为空
     * @return 是否为空
     * @since 0.0.5
     */
    boolean isEmpty();

    /**
     * 最大深度
     * @return 深度
     * @since 0.0.4
     */
    int getHeight();

    /**
     * 获取最小值
     * @return 最小值
     * @since 0.0.4
     */
    V getMinValue();

    /**
     * 获取最大值
     * @return 最大值
     * @since 0.0.4
     */
    V getMaxValue();

    /**
     * 中序遍历：即左-根-右遍历，对于给定的二叉树根，寻找其左子树；对于其左子树的根，再去寻找其左子树；递归遍历，直到寻找最左边的节点i，其必然为叶子，然后遍历i的父节点，再遍历i的兄弟节点。随着递归的逐渐出栈，最终完成遍历。
     * @since 0.0.4
     * @return 结果
     */
    List<V> inOrder();

    /**
     * 先序遍历：即根-左-右遍历，不再详述。
     * @since 0.0.4
     * @return 结果
     */
    List<V> preOrder();

    /**
     * 后序遍历：即左-右-根遍历，不再详述。
     * @since 0.0.4
     * @return 结果
     */
    List<V> postOrder();

    /**
     * 层级遍历
     * @return 结果
     * @since 0.0.4
     */
    List<V> levelOrder();

    /**
     * 获取所有路径列表
     * 从根节点，到叶子节点的路径
     * @return 0.0.4
     */
    List<List<V>> pathList();

    /**
     * 以树的形式打印出来元素
     *        2
     *       / \
     *      2   3
     *     /   /
     *    1   3
     *   /
     *  1
     * @since 0.0.4
     */
    void print();

    /**
     * 是否为平衡树
     * @return 平衡树
     * @since 0.0.5
     */
    boolean isBalanced();

}
