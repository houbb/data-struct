package com.github.houbb.data.struct.core.util.tree;

import com.github.houbb.data.struct.core.util.tree.component.PrintTreeNode;
import com.github.houbb.data.struct.core.util.tree.component.TreeNode;
import com.github.houbb.data.struct.util.InnerStringUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * avl 平衡树
 *
 * @author binbin.hou
 * @since 0.0.5
 */
public class AvlTree<V extends Comparable<? super V>> implements ISortTree<V> {

    private static final Log log = LogFactory.getLog(AvlTree.class);

    /**
     * 内部节点
     *
     * @param <V> 泛型
     * @since 0.0.5
     */
    private static class Node<V> {

        /**
         * 左节点
         *
         * @since 0.0.5
         */
        private Node<V> left;

        /**
         * 右节点
         *
         * @since 0.0.5
         */
        private Node<V> right;

        /**
         * 数据信息
         *
         * @since 0.0.5
         */
        private V data;

        /**
         * 当前元素所在的高度
         *
         * @since 0.0.5
         */
        private int height;

        public Node(V data) {
            this.data = data;
            this.left = null;
            this.right = null;
            this.height = 1;
        }

    }

    /**
     * 根节点
     *
     * @since 0.0.5
     */
    private Node<V> root;

    /**
     * 整棵树的大小
     *
     * @since 0.0.5
     */
    private int size;


    /**
     * 构造器
     * <p>
     * 初始化一颗空树
     *
     * @since 0.0.5
     */
    public AvlTree() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public boolean isBalanced() {
        return isBalanced(root);
    }

    /**
     * 指定的节点是否平衡
     *
     * @param node 节点
     * @return 是否平衡
     * @since 0.0.5
     */
    private boolean isBalanced(Node<V> node) {
        if (node == null) {
            return true;
        }
        int balanceFactory = Math.abs(getBalanceFactor(node));
        if (balanceFactory > 1) {
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);
    }

    /**
     * 获取节点的平衡因子
     *
     * @param node 节点
     * @return 平衡因子
     * @since 0.0.5
     */
    private int getBalanceFactor(Node<V> node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    /**
     * 获取当前节点的高度
     *
     * @param node 节点
     * @return 高度
     * @since 0.0.5
     */
    private int getHeight(Node<V> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }


    @Override
    public boolean contains(V data) {
        Node<V> node = getNode(root, data);
        return node != null;
    }

    /**
     * 右旋
     *
     * @since 0.0.5
     */
    private Node<V> rightRotate(Node<V> y) {
        System.out.println("右旋执行前：");
        print();

        Node<V> x = y.left;
        Node<V> t3 = x.right;
        y.left = t3;
        x.right = y;

        //更新height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        // 更新根节点
        if(y == root) {
            this.root = x;
        }
        System.out.println("右旋执行后：");
        print();
        return x;
    }

    /**
     * 左旋
     *
     * @since 0.0.5
     */
    private Node<V> leftRotate(Node<V> y) {
        System.out.println("左旋执行前：");
        print();

        Node<V> x = y.right;
        Node<V> t3 = x.left;
        x.left = y;
        y.right = t3;

        //更新height
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        // 更新根节点
        if(y == root) {
            this.root = x;
        }

        System.out.println("左旋执行后：");
        print();

        return x;
    }

    @Override
    public void add(V data) {
        this.root = add(root, data);
    }

    /**
     * 插入元素
     *
     * @param node 节点
     * @param v    待插入元素
     * @return 结果
     * @since 0.0.5
     */
    private Node<V> add(Node<V> node, V v) {
        if (node == null) {
            size++;
            return new Node<>(v);
        }
        if (v.compareTo(node.data) < 0) {
            node.left = add(node.left, v);
        } else if (v.compareTo(node.data) > 0) {
            node.right = add(node.right, v);
        }

        //更新height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        //计算平衡因子
        int balanceFactor = getBalanceFactor(node);
        if (balanceFactor > 1 && getBalanceFactor(node.left) > 0) {
            //右旋LL
            return rightRotate(node);
        }
        if (balanceFactor < -1 && getBalanceFactor(node.right) < 0) {
            //左旋RR
            return leftRotate(node);
        }
        //LR
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        //RL
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    @Override
    public boolean remove(V data) {
        Node<V> node = getNode(root, data);
        if (node != null) {
            root = remove(root, data);
            return true;
        }
        return false;
    }

    /**
     * 返回以node为根节点的二分搜索树中，key所在的节点
     *
     * @param node 节点
     * @param v    元素
     * @return 结果
     * @since 0.0.5
     */
    private Node<V> getNode(Node<V> node, V v) {
        if (node == null) {
            return null;
        }
        if (v.equals(node.data)) {
            return node;
        } else if (v.compareTo(node.data) < 0) {
            return getNode(node.left, v);
        } else {
            return getNode(node.right, v);
        }
    }

    /**
     * 返回以node为根的二分搜索树的最小值所在的节点
     * <p>
     * ps: 实际上就是最左子树
     *
     * @param node 节点
     * @return 结果
     * @since 0.0.5
     */
    private Node<V> getMiniNode(Node<V> node) {
        if (node.left == null) {
            return node;
        }
        return getMiniNode(node.left);
    }

    /**
     * 删除一个元素
     *
     * @param node 节点
     * @param v    元素
     * @return 结果
     */
    private Node<V> remove(Node<V> node, V v) {
        if (node == null) {
            return null;
        }
        Node<V> retNode;
        if (v.compareTo(node.data) < 0) {
            node.left = remove(node.left, v);
            retNode = node;
        } else if (v.compareTo(node.data) > 0) {
            node.right = remove(node.right, v);
            retNode = node;
        } else {   // e.compareTo(node.e) == 0
            // 待删除节点左子树为空的情况
            if (node.left == null) {
                Node<V> rightNode = node.right;
                node.right = null;
                size--;
                retNode = rightNode;
            }
            // 待删除节点右子树为空的情况
            else if (node.right == null) {
                Node<V> leftNode = node.left;
                node.left = null;
                size--;
                retNode = leftNode;
            } else {
                // 待删除节点左右子树均不为空的情况
                // 找到比待删除节点大的最小节点, 即待删除节点右子树的最小节点
                // 用这个节点顶替待删除节点的位置
                Node<V> successor = getMiniNode(node.right);
                successor.right = remove(node.right, successor.data);
                successor.left = node.left;

                node.left = node.right = null;

                retNode = successor;
            }
        }
        if (retNode == null) {
            return null;
        }
        //维护平衡
        //更新height
        retNode.height = 1 + Math.max(getHeight(retNode.left), getHeight(retNode.right));
        //计算平衡因子
        int balanceFactor = getBalanceFactor(retNode);
        if (balanceFactor > 1 && getBalanceFactor(retNode.left) >= 0) {
            //右旋LL
            return rightRotate(retNode);
        }
        if (balanceFactor < -1 && getBalanceFactor(retNode.right) <= 0) {
            //左旋RR
            return leftRotate(retNode);
        }
        //LR
        if (balanceFactor > 1 && getBalanceFactor(retNode.left) < 0) {
            node.left = leftRotate(retNode.left);
            return rightRotate(retNode);
        }
        //RL
        if (balanceFactor < -1 && getBalanceFactor(retNode.right) > 0) {
            node.right = rightRotate(retNode.right);
            return leftRotate(retNode);
        }
        return node;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.getSize() == 0;
    }

    @Override
    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(TreeNode<V> node) {
        if (node == null) {
            return (0);
        } else {
            int lDepth = getHeight(node.getLeft());
            int rDepth = getHeight(node.getRight());

            // use the larger
            return (Math.max(lDepth, rDepth));
        }
    }

    @Override
    public V getMinValue() {
        return null;
    }

    @Override
    public V getMaxValue() {
        return null;
    }

    @Override
    public List<V> inOrder() {
        List<V> list = new ArrayList<>();
        inOrder(list, root);
        return list;
    }

    private void inOrder(List<V> list, Node<V> treeNode) {
        if (treeNode == null) return;
        inOrder(list, treeNode.left);
        list.add(treeNode.data);
        inOrder(list, treeNode.right);
    }

    @Override
    public List<V> preOrder() {
        return null;
    }

    @Override
    public List<V> postOrder() {
        return null;
    }

    @Override
    public List<V> levelOrder() {
        return null;
    }

    @Override
    public List<List<V>> pathList() {
        return null;
    }

    /**
     * 打印思路
     */
    @Override
    public void print() {
        List<PrintTreeNode<V>> printList = new ArrayList<>();

        int level = 0;
        Node<V> node = root;
        Queue<Node<V>> queue = new LinkedList<>();
        queue.add(node);

        //root
        PrintTreeNode<V> printTreeNode = buildPrintNode(node, level, false, false, true);
        printList.add(printTreeNode);

        // 入队的时候构建元素
        int[] levelArray = new int[1000];
        levelArray[level] = 1;
        // 临时存放元素的列表
        List<V> tempList = new ArrayList<>();

        while (!queue.isEmpty()) {
            node = queue.poll();
            tempList.add(node.data);
            if (node.left != null) {
                queue.add(node.left);
                levelArray[level+1]++;

                PrintTreeNode<V> leftNode = buildPrintNode(node.left, level+1,
                        true, false, false);
                printList.add(leftNode);
            }
            if (node.right != null) {
                queue.add(node.right);
                levelArray[level+1]++;

                PrintTreeNode<V> rightNode = buildPrintNode(node.right, level+1,
                        false, true, false);
                printList.add(rightNode);
            }
            // 判断是否为当前这行最后一个元素
            if(tempList.size() == levelArray[level]) {
                printList.get(printList.size()-1).endLine(true);

                tempList.clear();
                level++;
            }
        }


        // 中序遍历，确定 x 坐标
        List<V> inOrders = inOrder();
        for(PrintTreeNode<V> node1 : printList) {
            V value = node1.data();
            int index = inOrders.indexOf(value);
            node1.offset(index);
        }

        // 输出
        int offset = 0;
        for(PrintTreeNode<V> node1 : printList) {
            int xOffset = node1.offset();
            String text = InnerStringUtil.leftPad(xOffset, offset, node1.data());
            offset += text.length();
            System.out.print(text);

            if(node1.endLine()) {
                System.out.println();
                offset = 0;
            }
        }

        // 专门输出一行，连接符号行 /\
    }

    /**
     * 构建打印节点
     * @param node 节点
     * @param level 层级
     * @param isLeft 是否为左节点
     * @param isRight 是否为右节点
     * @param isEndLine 是否为结束
     * @return 结果
     * @since 0.0.5
     */
    private PrintTreeNode<V> buildPrintNode(Node<V> node, int level,
                                            boolean isLeft, boolean isRight,
                                            boolean isEndLine) {
        PrintTreeNode<V> treeNode = new PrintTreeNode<>();

        treeNode.data(node.data)
                .right(isRight)
                .level(level)
                .left(isLeft)
                .endLine(isEndLine);

        return treeNode;
    }

}
