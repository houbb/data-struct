package com.github.houbb.data.struct.core.util.tree;

import com.github.houbb.data.struct.core.util.tree.component.TreeNode;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.List;

/**
 * 红黑树
 *
 * @author binbin.hou
 * @since 0.0.5
 */
public class RedBlackTree<T extends Comparable<? super T>> implements ISortTree<T> {

    private static final Log log = LogFactory.getLog(RedBlackTree.class);

    /**
     * 根节点
     * @since 0.0.5
     */
    private Node<T> root;

    /**
     * 整棵树的大小
     * @since 0.0.5
     */
    private int size;


    /**
     * 构造器
     * 初始化一颗空树
     *
     * @since 0.0.5
     */
    public RedBlackTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * 颜色标识
     * @since 0.0.5
     */
    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    @Override
    public boolean contains(T data) {
        return false;
    }

    @Override
    public void add(T data) {
        Node<T> node = new Node<T>(null, null, null, data, BLACK);
        this.add(node);
    }

    /**
     * 将结点插入到红黑树中
     * @param node 节点
     * @since 0.0.5
     */
    private void add(Node<T> node) {
        int cmp;
        Node<T> y = null;
        Node<T> x = this.root;

        // 1. 将红黑树当作一颗二叉查找树，将节点添加到二叉查找树中。
        while (x != null) {
            y = x;
            cmp = node.data.compareTo(x.data);
            if (cmp < 0)
                x = x.left;
            else
                x = x.right;
        }
        //1.2 找到合适的位置之后，插入新的节点
        node.parent = y;
        if (y != null) {
            cmp = node.data.compareTo(y.data);
            if (cmp < 0)
                y.left = node;
            else
                y.right = node;
        } else {
            this.root = node;
        }

        // 2. 设置节点的颜色为红色
        node.color = RED;

        // 3. 将它重新修正为一颗二叉查找树
        addFixUp(node);
    }

    /**
     * 红黑树插入修正函数
     *
     * 在向红黑树中插入节点之后(失去平衡)，再调用该函数；目的是将它重新塑造成一颗红黑树。
     * @param node 插入节点
     * @since 0.0.5
     */
    private void addFixUp(Node<T> node) {
        Node<T> parent, gparent;

        // 若“父节点存在，并且父节点的颜色是红色”
        while (((parent = parentOf(node))!=null) && isRed(parent)) {
            gparent = parentOf(parent);

            //若“父节点”是“祖父节点的左孩子”
            if (parent == gparent.left) {
                // Case 1条件：叔叔节点是红色
                Node<T> uncle = gparent.right;
                if ((uncle!=null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                // Case 2条件：叔叔是黑色，且当前节点是右孩子
                if (parent.right == node) {
                    Node<T> tmp;
                    leftRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 3条件：叔叔是黑色，且当前节点是左孩子。
                setBlack(parent);
                setRed(gparent);
                rightRotate(gparent);
            } else {    //若“z的父节点”是“z的祖父节点的右孩子”
                // Case 1条件：叔叔节点是红色
                Node<T> uncle = gparent.left;
                if ((uncle!=null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                // Case 2条件：叔叔是黑色，且当前节点是左孩子
                if (parent.left == node) {
                    Node<T> tmp;
                    rightRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 3条件：叔叔是黑色，且当前节点是右孩子。
                setBlack(parent);
                setRed(gparent);
                leftRotate(gparent);
            }
        }

        // 将根节点设为黑色
        setBlack(this.root);
    }

    @Override
    public boolean remove(T data) {
        Node<T> node = search(root, data);
        if(node == null) {
            return false;
        }

        remove(node);
        return true;
    }

    /**
     * 删除结点(node)
     * @param node 节点
     * @since 0.0.5
     */
    private void remove(Node<T> node) {
        Node<T> child, parent;
        boolean color;

        // 被删除节点的"左右孩子都不为空"的情况。
        if ( (node.left!=null) && (node.right!=null) ) {
            // 被删节点的后继节点。(称为"取代节点")
            // 用它来取代"被删节点"的位置，然后再将"被删节点"去掉。
            Node<T> replace = node;

            // 获取后继节点
            replace = replace.right;
            while (replace.left != null)
                replace = replace.left;

            // "node节点"不是根节点(只有根节点不存在父节点)
            if (parentOf(node)!=null) {
                if (parentOf(node).left == node)
                    parentOf(node).left = replace;
                else
                    parentOf(node).right = replace;
            } else {
                // "node节点"是根节点，更新根节点。
                this.root = replace;
            }

            // child是"取代节点"的右孩子，也是需要"调整的节点"。
            // "取代节点"肯定不存在左孩子！因为它是一个后继节点。
            child = replace.right;
            parent = parentOf(replace);
            // 保存"取代节点"的颜色
            color = colorOf(replace);

            // "被删除节点"是"它的后继节点的父节点"
            if (parent == node) {
                parent = replace;
            } else {
                // child不为空
                if (child!=null)
                    setParent(child, parent);
                parent.left = child;

                replace.right = node.right;
                setParent(node.right, replace);
            }

            replace.parent = node.parent;
            replace.color = node.color;
            replace.left = node.left;
            node.left.parent = replace;

            if (color == BLACK)
                removeFixUp(child, parent);

            node = null;
            return ;
        }

        if (node.left !=null) {
            child = node.left;
        } else {
            child = node.right;
        }

        parent = node.parent;
        // 保存"取代节点"的颜色
        color = node.color;

        if (child!=null)
            child.parent = parent;

        // "node节点"不是根节点
        if (parent!=null) {
            if (parent.left == node)
                parent.left = child;
            else
                parent.right = child;
        } else {
            this.root = child;
        }

        if (color == BLACK)
            removeFixUp(child, parent);
        node = null;
    }

    /**
     * 红黑树删除修正函数
     * @param node 节点
     * @param parent 父节点
     * @since 0.0.5
     */
    private void removeFixUp(Node<T> node, Node<T> parent) {
        Node<T> other;

        while ((node==null || isBlack(node)) && (node != this.root)) {
            if (parent.left == node) {
                other = parent.right;
                if (isRed(other)) {
                    // Case 1: x的兄弟w是红色的  
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    other = parent.right;
                }

                if ((other.left==null || isBlack(other.left)) &&
                        (other.right==null || isBlack(other.right))) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的  
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.right==null || isBlack(other.right)) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。  
                        setBlack(other.left);
                        setRed(other);
                        rightRotate(other);
                        other = parent.right;
                    }
                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.right);
                    leftRotate(parent);
                    node = this.root;
                    break;
                }
            } else {

                other = parent.left;
                if (isRed(other)) {
                    // Case 1: x的兄弟w是红色的  
                    setBlack(other);
                    setRed(parent);
                    rightRotate(parent);
                    other = parent.left;
                }

                if ((other.left==null || isBlack(other.left)) &&
                        (other.right==null || isBlack(other.right))) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的  
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.left==null || isBlack(other.left)) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。  
                        setBlack(other.right);
                        setRed(other);
                        leftRotate(other);
                        other = parent.left;
                    }

                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.left);
                    rightRotate(parent);
                    node = this.root;
                    break;
                }
            }
        }

        if (node!=null)
            setBlack(node);
    }

    /**
     * (递归实现)查找"红黑树x"中键值为key的节点
     * @param x 节点
     * @param key 数据
     * @return 结果
     * @since 0.0.5
     */
    private Node<T> search(Node<T> x, T key) {
        if (x==null)
            return x;

        int cmp = key.compareTo(x.data);
        if (cmp < 0)
            return search(x.left, key);
        else if (cmp > 0)
            return search(x.right, key);
        else
            return x;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public T getMinValue() {
        return null;
    }

    @Override
    public T getMaxValue() {
        return null;
    }

    @Override
    public List<T> inOrder() {
        return null;
    }

    @Override
    public List<T> preOrder() {
        return null;
    }

    @Override
    public List<T> postOrder() {
        return null;
    }

    @Override
    public List<T> levelOrder() {
        return null;
    }

    @Override
    public List<List<T>> pathList() {
        return null;
    }

    @Override
    public void print() {
        if(root != null) {
            print(root, root.data, 0);
        }
    }

    /**
     * 打印红黑树
     * @param tree 树节点
     * @param key 节点的键值
     * @param direction 方向 0，表示该节点是根节点; -1，表示该节点是它的父结点的左孩子; 1，表示该节点是它的父结点的右孩子。
     * @since 0.0.5
     */
    private void print(Node<T> tree, T key, int direction) {
        if(tree != null) {
            if(direction==0)    // tree是根节点
                System.out.printf("%2s(B) is root\n", tree.data);
            else                // tree是分支节点
                System.out.printf("%2s(%s) is %2s's %6s child\n", tree.data, isRed(tree)?"R":"B", key, direction==1?"right" : "left");

            print(tree.left, tree.data, -1);
            print(tree.right,tree.data,  1);
        }
    }

    @Override
    public boolean isBalanced() {
        return false;
    }

    /**
     * 内部节点
     *
     * @param <T> 泛型
     * @since 0.0.5
     */
    private static class Node<T> {

        /**
         * 父亲节点
         * @since 0.0.5
         */
        private Node<T> parent;

        /**
         * 左节点
         * @since 0.0.5
         */
        private Node<T> left;

        /**
         * 右节点
         * @since 0.0.5
         */
        private Node<T> right;

        /**
         * 数据信息
         * @since 0.0.5
         */
        private T data;

        /**
         * 颜色
         */
        private boolean color;

        public Node(T data) {
            this(null, null, null, data, BLACK);
        }

        public Node(Node<T> parent, Node<T> left, Node<T> right, T data, boolean color) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.data = data;
            this.color = color;
        }
    }

    /**
     * 对红黑树的节点(x)进行左旋转
     * @param x 节点
     * @since 0.0.5
     */
    private void leftRotate(Node<T> x) {
        // 设置x的右孩子为y
        Node<T> y = x.right;

        // 将 “y的左孩子” 设为 “x的右孩子”；
        // 如果y的左孩子非空，将 “x” 设为 “y的左孩子的父亲”
        x.right = y.left;
        if (y.left != null)
            y.left.parent = x;

        // 将 “x的父亲” 设为 “y的父亲”
        y.parent = x.parent;

        if (x.parent == null) {
            this.root = y;            // 如果 “x的父亲” 是空节点，则将y设为根节点
        } else {
            if (x.parent.left == x)
                x.parent.left = y;    // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
            else
                x.parent.right = y;    // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
        }

        // 将 “x” 设为 “y的左孩子”
        y.left = x;
        // 将 “x的父节点” 设为 “y”
        x.parent = y;
    }

    /**
     * 对红黑树的节点(y)进行右旋转
     * @param y 节点
     * @since 0.0.5
     */
    private void rightRotate(Node<T> y) {
        // 设置x是当前节点的左孩子。
        Node<T> x = y.left;

        // 将 “x的右孩子” 设为 “y的左孩子”；
        // 如果"x的右孩子"不为空的话，将 “y” 设为 “x的右孩子的父亲”
        y.left = x.right;
        if (x.right != null)
            x.right.parent = y;

        // 将 “y的父亲” 设为 “x的父亲”
        x.parent = y.parent;

        if (y.parent == null) {
            this.root = x;            // 如果 “y的父亲” 是空节点，则将x设为根节点
        } else {
            if (y == y.parent.right)
                y.parent.right = x;    // 如果 y是它父节点的右孩子，则将x设为“y的父节点的右孩子”
            else
                y.parent.left = x;    // (y是它父节点的左孩子) 将x设为“x的父节点的左孩子”
        }

        // 将 “y” 设为 “x的右孩子”
        x.right = y;

        // 将 “y的父节点” 设为 “x”
        y.parent = x;
    }

    /**
     * 变色-直接取反即可
     * @param color 颜色
     * @return 变色后的颜色
     * @since 0.0.5
     */
    private boolean changeColor(boolean color) {
        return !color;
    }

    private Node<T> parentOf(Node<T> node) {
        return node!=null ? node.parent : null;
    }
    private boolean colorOf(Node<T> node) {
        return node!=null ? node.color : BLACK;
    }
    private boolean isRed(Node<T> node) {
        return (node != null) && (node.color == RED);
    }
    private boolean isBlack(Node<T> node) {
        return !isRed(node);
    }
    private void setBlack(Node<T> node) {
        if (node!=null)
            node.color = BLACK;
    }
    private void setRed(Node<T> node) {
        if (node!=null)
            node.color = RED;
    }
    private void setParent(Node<T> node, Node<T> parent) {
        if (node!=null)
            node.parent = parent;
    }
    private void setColor(Node<T> node, boolean color) {
        if (node!=null)
            node.color = color;
    }
    
}
