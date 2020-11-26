package com.github.houbb.data.struct.core.util.tree;

import com.github.houbb.data.struct.core.util.tree.component.PrintTreeNode;
import com.github.houbb.data.struct.core.util.tree.component.TreeNode;
import com.github.houbb.heaven.util.lang.CharUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树
 *
 * @author binbin.hou
 * @since 0.0.4
 */
public class BinarySearchTree<V extends Comparable<? super V>> implements ISortTree<V> {

    /**
     * 根节点
     *
     * @since 0.0.4
     */
    private TreeNode<V> root;

    public BinarySearchTree() {
        this.root = null;
    }

    /**
     * * Returns true if the given target is in the binary tree.
     * * Uses a recursive helper.
     * * @since 0.0.4
     */
    public boolean contains(V data) {
        return (contains(root, data));
    }


    /**
     * Inserts the given data into the binary tree.
     * Uses a recursive helper.
     * * @param data 待插入元素
     */
    public void add(V data) {
        root = add(root, data);
    }

    /**
     * 情况 1：如果删除的节点没有右孩子，那么就选择它的左孩子来代替原来的节点。
     * 二叉查找树的性质保证了被删除节点的左子树必然符合二叉查找树的性质。
     * 因此左子树的值要么都大于，要么都小于被删除节点的父节点的值，这取决于被删除节点是左孩子还是右孩子。
     * 因此用被删除节点的左子树来替代被删除节点，是完全符合二叉搜索树的性质的。
     *
     * 情况 2：如果被删除节点的右孩子没有左孩子，那么这个右孩子被用来替换被删除节点。
     * 因为被删除节点的右孩子都大于被删除节点左子树的所有节点，同时也大于或小于被删除节点的父节点，这同样取决于被删除节点是左孩子还是右孩子。
     * 因此，用右孩子来替换被删除节点，符合二叉查找树的性质。
     *
     * 情况 3：如果被删除节点的右孩子有左孩子，就需要用被删除节点右孩子的左子树中的最下面的节点来替换它，
     * 就是说，我们用被删除节点的右子树中最小值的节点来替换。
     * @param data 元素
     */
    @Override
    public boolean remove(V data) {
        //引用当前节点，从根节点开始
        TreeNode<V> current = root;
        //应用当前节点的父节点
        TreeNode<V> parent = root;
        //是否为左节点
        boolean isLeftChild = true;

        while(current.getData().compareTo(data) != 0){
            parent = current;
            //进行比较，比较查找值和当前节点的大小
            if(current.getData().compareTo(data) > 0){
                current = current.getLeft();
                isLeftChild = true;
            } else {
                current = current.getRight();
                isLeftChild = false;
            }

            // 没有找到这个元素
            if(current == null){
                return false;
            }
        }

//        1. 该节点是叶子节点，没有子节点
//        要删除叶节点，只需要改变该节点的父节点的引用值，将指向该节点的引用设置为null就可以了。
        if(current.getLeft() == null && current.getRight() == null){
            // 根节点
            if(current == root){
                root = null;
            }else if(isLeftChild){
                //如果它是父节点的左子节点
                parent.setLeft(null);
            }else{
                parent.setRight(null);
            }

//            2、该节点有一个子节点
//            改变父节点的引用，将其直接指向要删除节点的子节点。
        }else if (current.getRight() == null){
            // 如果右节点为空，使用左节点，替代被删除的节点。
            if(current == root){
                root = current.getLeft();
            }else if(isLeftChild){
                parent.setLeft(current.getLeft());
            }else{
                parent.setRight(current.getLeft());
            }
        }else if (current.getLeft() == null){
            // 如果左节点为空，则使用右节点替代
            if(current == root){
                root = current.getRight();
            }else if(isLeftChild){
                parent.setLeft(current.getRight());
            }else{
                parent.setRight(current.getRight());;
            }
        } else {
//            3、该节点有两个子节点
//            要删除有两个子节点的节点，就需要使用它的中序后继来替代该节点。

            TreeNode<V> successor = getSuccessor(current);
            if(current == root){
                root = successor;
            } else if(isLeftChild) {
                parent.setLeft(successor);
            }else{
                parent.setRight(successor);
            }
            successor.setLeft(current.getLeft());
        }
        return true;
    }

    /**
     * 寻找中继节点
     * @param delNode 删除元素
     * @return 中继节点
     * @since 0.0.4
     */
    public TreeNode<V> getSuccessor(TreeNode<V> delNode){
        TreeNode<V> successor = delNode;
        TreeNode<V> successorParent = delNode;
        TreeNode<V> current = delNode.getRight();
        while(current != null){
            successorParent = successor;
            successor = current;
            current = current.getLeft();
        }
        if(successor != delNode.getRight()){
            successorParent.setLeft(successor.getRight());
            //将删除的节点的整个右子树挂载到中继节点的右子树上
            successor.setRight(delNode.getRight());
        }
        return successor;
    }


    @Override
    public int size() {
        return size(root);
    }

    @Override
    public int maxDepth() {
        return maxDepth(root);
    }

    @Override
    public V minValue() {
        TreeNode<V> current = root;
        while (current != null && current.getLeft() != null) {
            current = current.getLeft();
        }

        return current.getData();
    }

    @Override
    public V maxValue() {
        TreeNode<V> current = root;
        while (current != null && current.getRight() != null) {
            current = current.getRight();
        }

        return current.getData();
    }

    @Override
    public List<V> inOrder() {
        List<V> list = new ArrayList<>();
        inOrder(list, root);
        return list;
    }

    private void inOrder(List<V> list, TreeNode<V> treeNode) {
        if (treeNode == null) return;
        inOrder(list, treeNode.getLeft());
        list.add(treeNode.getData());
        inOrder(list, treeNode.getRight());
    }

    @Override
    public List<V> preOrder() {
        List<V> list = new ArrayList<>();
        preOrder(list, root);
        return list;
    }

    private void preOrder(List<V> list, TreeNode<V> treeNode) {
        if (treeNode == null) return;
        list.add(treeNode.getData());
        preOrder(list, treeNode.getLeft());
        preOrder(list, treeNode.getRight());
    }

    @Override
    public List<V> postOrder() {
        List<V> list = new ArrayList<>();
        postOrder(list, root);
        return list;
    }

    private void postOrder(List<V> list, TreeNode<V> treeNode) {
        if (treeNode == null) return;
        list.add(treeNode.getData());
        postOrder(list, treeNode.getLeft());
        postOrder(list, treeNode.getRight());
    }

    @Override
    public List<V> levelOrder() {
        List<V> result = new ArrayList<>();
        TreeNode<V> node = root;

        Queue<TreeNode<V>> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            node = queue.poll();

            result.add(node.getData());

            if (node.getLeft() != null)
                queue.add(node.getLeft());
            if (node.getRight() != null)
                queue.add(node.getRight());
        }

        return result;
    }

    @Override
    public List<List<V>> pathList() {
        List<List<V>> result = new ArrayList<>();
        List<V> path = new ArrayList<>();
        pathList(root, result, path, 0);

        return result;
    }

    private PrintTreeNode<V> buildPrintNode(TreeNode<V> node, int level,
                                boolean isLeft, boolean isRight,
                                            boolean isEndLine) {
        PrintTreeNode<V> treeNode = new PrintTreeNode<>();

        treeNode.data(node.getData())
                .right(isRight)
                .level(level)
                .left(isLeft)
                .endLine(isEndLine);

        return treeNode;
    }

    /**
     * 打印思路
     */
    @Override
    public void print() {
        List<PrintTreeNode<V>> printList = new ArrayList<>();

        int level = 0;
        TreeNode<V> node = root;
        Queue<TreeNode<V>> queue = new LinkedList<>();
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
            tempList.add(node.getData());
            if (node.getLeft() != null) {
                queue.add(node.getLeft());
                levelArray[level+1]++;

                PrintTreeNode<V> leftNode = buildPrintNode(node.getLeft(), level+1,
                        true, false, false);
                printList.add(leftNode);
            }
            if (node.getRight() != null) {
                queue.add(node.getRight());
                levelArray[level+1]++;

                PrintTreeNode<V> rightNode = buildPrintNode(node.getRight(), level+1,
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
            String text = leftPad(xOffset, offset, node1.data());
            offset += text.length();
            System.out.print(text);

            if(node1.endLine()) {
                System.out.println();
                offset = 0;
            }
        }

        // 专门输出一行，连接符号行 /\
    }

    private String leftPad(int xoffset, int offset, V value) {
        int left = xoffset - offset;
        if(left <= 0) {
            return value.toString();
        }

        // 直接填充
        return CharUtil.repeat(' ', left)+value.toString();
    }

    // 2
    // 1 3
    private void pathList(TreeNode<V> node, List<List<V>> result, List<V> path, int pathLength) {
        if (node == null) {
            return;
        }

        // 这里通过设置的方式
        if (path.size() > pathLength) {
            path.set(pathLength, node.getData());
        } else {
            path.add(node.getData());
        }
        pathLength++;
        // 左右节点都不存在，则说明是叶子节点
        if (node.getRight() == null && node.getLeft() == null) {
            List<V> newList = new ArrayList<>(pathLength);
            for (int i = 0; i < pathLength; i++) {
                newList.add(path.get(i));
            }
            result.add(newList);
        } else {
            // 尝试两个子树
            pathList(node.getLeft(), result, path, pathLength);
            pathList(node.getRight(), result, path, pathLength);
        }

    }


    private int maxDepth(TreeNode<V> node) {
        if (node == null) {
            return (0);
        } else {
            int lDepth = maxDepth(node.getLeft());
            int rDepth = maxDepth(node.getRight());

            // use the larger + 1
            return (Math.max(lDepth, rDepth) + 1);
        }
    }

    /**
     * 递归获取 size
     *
     * @param node 节点
     * @return 结果
     * @since 0.0.4
     */
    private int size(TreeNode<V> node) {
        if (node == null) {
            return (0);
        }
        return (size(node.getLeft()) + 1 + size(node.getRight()));
    }

    /**
     * 递归查询
     *
     * @param node 节点
     * @param data 元素
     * @return 是否包含
     * @since 0.0.4
     */
    private boolean contains(TreeNode<V> node, V data) {
        if (node == null) {
            return false;
        }

        if (node.getData().compareTo(data) == 0) {
            return true;
        } else if (data.compareTo(node.getData()) < 0) {
            // 小于节点，则查询左子树
            return (contains(node.getLeft(), data));
        } else {
            // 大于节点，则查询右子树
            return (contains(node.getRight(), data));
        }
    }



    /**
     * Recursive insert -- given a node pointer, recur down and
     * insert the given data into the tree. Returns the new
     * node pointer (the standard way to communicate
     * a changed pointer back to the caller).
     *
     * @param node 节点
     * @param data 数据
     * @since 0.0.4
     */
    private TreeNode<V> add(TreeNode<V> node, V data) {
        // 如果节点为空，则插入到当前位置
        if (node == null) {
            node = new TreeNode<>(data);
        } else {
            // 优先插在左边
            if (data.compareTo(node.getData()) <= 0) {
                node.setLeft(add(node.getLeft(), data));
            } else {
                node.setRight(add(node.getRight(), data));
            }
        }

        // in any case, return the new pointer to the caller
        return node;
    }

}
