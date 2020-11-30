package com.github.houbb.data.struct.core.util.tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by MSI on 2016/1/5.
 * 该程序参考文章：http://blog.csdn.net/v_JULY_v/article/details/6530142/
 * https://www.cnblogs.com/wglspark/p/5146612.html
 */
public class BsubTree<T extends Comparable<T>> {


    private int m = 4;      //此B-树的阶数。关键字数等于阶数-1。m至少为2，m必须大于等于2。
    private int n;      //n是关键字最小个数
    private BTNode root;

    public BsubTree() {
        root = new BTNode(null, null);
        if (m >= 2) {
            if (m % 2 == 0) {
                n = m / 2 - 1;
            } else {
                n = m / 2;
            }
        } else {
            System.out.println("error");
            System.exit(0);
        }
    }

    public BsubTree(BTNode root) {
        this.root = root;
        if (m % 2 == 0) {
            n = m / 2;
        } else {
            n = m / 2 + 1;
        }
    }

    public BTNode findNode(T information) {
        return findNode(information, root);
    }   //isMember应该返回插入点

    private BTNode findNode(T info, BTNode node) {   //不论是否找到都返回一个node。
        BTNode member = null;

        if (node.informations.size() == 0) {//这种情况存在,只有root可能
            member = node;
            //System.out.println("error");
        } else {
            if (info.compareTo(node.informations.get(node.informations.size() - 1)) > 0) {    //info比节点最大的还大，则直接进入最右分支
                if (node.ptr.size() > 0) {//有孩子的情况，进入范围中的子节点
                    member = findNode(info, node.ptr.get(node.informations.size()));
                } else {//没有子节点，直接返回node
                    member = node;
                }
            } else {//没有判断没有子节点的情况，上一个if中判断了，这一个else中就忘了，怒
                if (node.ptr.size() > 0) {//有子节点
                    if (info.compareTo(node.informations.get(0)) < 0) {
                        member = findNode(info, node.ptr.get(0));
                    } else {
                        for (int i = 0; i < node.informations.size(); ++i) {
                            if (info.compareTo(node.informations.get(i)) == 0) {
                                member = node;
                                break;
                            } else if (info.compareTo(node.informations.get(i)) > 0 && info.compareTo(node.informations.get(i + 1)) < 0) {   //只要不是最右，info比之大的，进入它的孩子节点
                                member = findNode(info, node.ptr.get(i + 1));
                                break;
                            }
                        }
                    }
                } else {//没有子节点
                    member = node;
                }
            }
        }
        return member;
    }

    public void insert(T info) {
        BTNode temp = findNode(info);
        if (temp.informations.size() != 0) {
            for (T i : temp.informations) {
                if (i.compareTo(info) == 0) {
                    System.out.println("已存在所插入的值。");
                    return;
                }
            }
        }
        insert(info, temp, temp.parent);
        return;
    }

    private void insert(T info, BTNode node, BTNode parent) {//插入一定是在叶子节点
        if (node == null) {//insert中的node为空应该只有一种情况，node=root
            if (parent == null) {
                root = new BTNode(info, parent);
            } else {
                System.out.println("不应该出现的情况，请检查。");
                //node = new BTNode(info,parent);
            }
        } else {
            if (node.informations.size() == 0) {
                //System.out.println("这种情况应该不存在，请检查代码");//现在存在这种情况啦
                node.informations.add(info);
            } else if (node.informations.size() > 0 && node.informations.size() < m - 1) {
                if (info.compareTo(node.informations.get(node.informations.size() - 1)) > 0) {//info比node最右边最大的值还大，则直接插入
                    node.informations.add(info);
                } else {
                    for (int i = 0; i < node.informations.size(); ++i) {
                        if (info.compareTo(node.informations.get(i)) < 0) {
                            node.informations.add(i, info);
                            break;
                        }
                    }
                }
            } else if (node.informations.size() == m - 1) {//需要分裂
                if (info.compareTo(node.informations.get(node.informations.size() - 1)) > 0) {//info比node最右边最大的值还大，则直接插入
                    node.informations.add(info);
                } else {
                    for (int i = 0; i < node.informations.size(); ++i) {
                        if (info.compareTo(node.informations.get(i)) < 0) {
                            node.informations.add(i, info);
                            break;
                        }
                    }
                }

                split(node);
            } else {
                System.out.println("node 的size大于等于m-1，不应该出现，请检查代码");
            }
        }
    }

    public void delete(T info) {
        BTNode temp = findNode(info, root);
        if (temp.informations.size() == 0) {
            System.out.println("根节点为空！");
            return;
        }
        for (T i : temp.informations) {
            if (info.compareTo(i) == 0) {
                delete(info, temp);
                break;
            } else if (temp.informations.indexOf(i) == temp.informations.size() - 1) {//循环到最后一个值了，仍到这里，说明不存在要删除的值！
                System.out.println("不存在要删除的值！");
            }
        }
    }

    private void delete(T info, BTNode node) throws NoSuchElementException {
        if (node == null) { //其实到这里，就一定存在要删除的值了。
            throw new NoSuchElementException();
        } else {
            int i;
            for (i = 0; i < node.informations.size(); i++) {
                if (info.compareTo(node.informations.get(i)) == 0) {
                    node.informations.remove(i);    //删除关键字，其实要是索引向文件，也应该删除文件。
                    break;
                }
            }
            if (node.ptr.size() > 0) {//删除一个非叶子节点的关键字后，如果有孩子，则判断孩子的孩子，如果孩子有孩子，则将右孩子的孩子最深左孩子的第一个值赋给删除关键字的节点
                //每一个关键字，一定有两个孩子
                if (node.ptr.get(i + 1).ptr.size() == 0) {//孩子没有孩子的时候，只将孩子的最左关键字上升。
                    node.informations.add(i, node.ptr.get(i + 1).informations.get(0));
                    node.ptr.get(i + 1).informations.remove(0);
                    if (node.ptr.get(i + 1).informations.size() < n) {
                        dManageNode(node.ptr.get(i + 1));
                    }
                } else {//孩子有孩子的时候，则将右孩子的孩子最深左孩子的第一个值赋给删除关键字的节点
                    pullRLeftNode(node, i, node.ptr.get(i + 1), i);
                }

            } else {//如果没有孩子，要判断该节点关键字数量是否大于最小值
                if (node.informations.size() >= n) {//大于等于就没事，不用动
                    return;
                } else {//叶子节点中关键字数小于n，需要继续判断兄弟节点是否饱满
                    dManageNode(node);
                }
            }
        }
    }

    public String perOrder(BTNode node) {
        String result = "";
        if (node.ptr.size() > 0) {
            int i = 0;
            for (BTNode n : node.ptr) {
                result += perOrder(n);
                if (i < node.informations.size()) {
                    result += node.informations.get(i).toString() + ",";
                    ++i;
                }
            }
        } else {//叶子节点
            if (node.informations.size() > 0) {
                for (T t : node.informations) {
                    result += t.toString() + ",";
                }
            } else {//叶子节点没有空值的时候，除非是根节点，根节点为空值的时候，说句话意思意思
                result += "B-树为空！";
            }
        }
        return result;
    }

    public void split(BTNode node) {//进到这里的node都是m个关键字，需要提出m/2
        if (node == null) {
            System.out.println("error");
        } else {
            if (node.informations.size() != m) {
                System.out.println("error");
            } else {
                if (node.parent == null) {//node是root时
                    T temp = node.informations.get(n);//这里正好
                    root = new BTNode(temp, null);
                    node.informations.remove(n);//加进去了就要删掉！
                    root.ptr.add(node);
                    node.parent = root;
                    splitNewNode(node, n, root);
                } else {//一个非根节点
                    T temp = node.informations.get(n);
                    node.parent.informations.add(node.parent.ptr.indexOf(node), temp);
                    node.informations.remove(n);
                    splitNewNode(node, n, node.parent);
                    if (node.parent.informations.size() >= m) {
                        split(node.parent);
                    }
                }
            }
        }
    }

    public void splitNewNode(BTNode node, int n, BTNode parent) {
        BTNode newnode = new BTNode(node.informations.get(n), node.parent);

        newnode.informations.addAll(node.informations.subList(n + 1, node.informations.size()));

        node.informations.removeAll(node.informations.subList(n, node.informations.size()));
        //newnode.parent=node.parent;//新增节点的父节点
        node.parent.ptr.add(node.parent.ptr.indexOf(node) + 1, newnode);   //新增节点加到父节点上
        if (node.ptr.size() > 0) {  //处理新增节点的孩子
            newnode.ptr.addAll(node.ptr.subList(n + 1, node.ptr.size()));
            node.ptr.removeAll(node.ptr.subList(n + 1, node.ptr.size()));
            for (BTNode bn : newnode.ptr) {    //子节点移到了新节点上，但是子节点的父节点没有处理！！！T_T
                bn.parent = newnode;
            }
        }

    }

    public void combine(BTNode lnode, BTNode rnode) {
        if (lnode.informations.size() < n) {
            lnode.informations.add(lnode.parent.informations.get(lnode.parent.ptr.indexOf(lnode)));
            lnode.parent.informations.remove(lnode.parent.ptr.indexOf(lnode));
        } else if (rnode.informations.size() < n) {
            rnode.informations.add(0, rnode.parent.informations.get(lnode.parent.ptr.indexOf(lnode)));
            rnode.parent.informations.remove(lnode.parent.ptr.indexOf(lnode));
        } else {
            System.out.println("error");
        }

        lnode.informations.addAll(rnode.informations);
        lnode.ptr.addAll(rnode.ptr);
        for (BTNode n : rnode.ptr) {
            n.parent = lnode;
        }
        lnode.parent.ptr.remove(lnode.parent.ptr.indexOf(lnode) + 1);
        if (lnode.parent.parent == null && lnode.parent.informations.size() == 0) {//父节点是根节点
            lnode.parent = null;    //lnode为新的根节点
            root = lnode;
            return;
        }
        if (lnode.parent.informations.size() < n) {
            dManageNode(lnode.parent);
        }
    }

    public void lrotate(BTNode lnode, BTNode rnode) {
        lnode.informations.add(lnode.parent.informations.get(lnode.parent.ptr.indexOf(lnode)));
        lnode.parent.informations.remove(lnode.parent.ptr.indexOf(lnode));
        lnode.parent.informations.add(lnode.parent.ptr.indexOf(lnode), rnode.informations.get(0));
        rnode.informations.remove(0);
        if (rnode.ptr.size() > 0) {//要判断叶子节点没有孩子！
            lnode.ptr.add(rnode.ptr.get(0));
            rnode.ptr.remove(0);
            lnode.ptr.get(lnode.ptr.size() - 1).parent = lnode;
        }

    }

    public void rrotate(BTNode lnode, BTNode rnode) {
        rnode.informations.add(rnode.parent.informations.get(lnode.parent.ptr.indexOf(lnode)));
        rnode.parent.informations.remove(lnode.parent.ptr.indexOf(lnode));
        rnode.parent.informations.add(lnode.parent.ptr.indexOf(lnode), lnode.informations.get(lnode.informations.size() - 1));
        lnode.informations.remove(lnode.informations.size() - 1);
        if (lnode.ptr.size() > 0) {
            rnode.ptr.add(0, lnode.ptr.get(lnode.ptr.size() - 1));
            lnode.ptr.remove(lnode.ptr.size() - 1);
            rnode.ptr.get(0).parent = rnode;
        }
    }

    public void dManageNode(BTNode node) {//叶子节点中关键字数小于n，需要继续判断兄弟节点是否饱满，是旋转还是合并
        if (node.parent == null) {
            return;
        } else {
            int x = node.parent.ptr.indexOf(node);
            if (x == 0) {//被删除关键字所在节点，是父节点最左边的节点时，判断右兄弟，而且肯定有右兄弟
                if (node.parent.ptr.get(x + 1).informations.size() == n) {//刚脱贫，需要合并
                    combine(node, node.parent.ptr.get(x + 1));
                } else if (node.parent.ptr.get(x + 1).informations.size() > n) {//关键字数大于最小值，丰满
                    lrotate(node, node.parent.ptr.get(x + 1));
                } else {
                    System.out.println("error");
                }
            } else if (x == node.parent.ptr.size() - 1) {//是父节点最右边的节点时，判断左兄弟
                if (node.parent.ptr.get(x - 1).informations.size() == n) {//左兄弟刚脱贫，需要合并
                    combine(node.parent.ptr.get(x - 1), node);
                } else if (node.parent.ptr.get(x - 1).informations.size() > n) {//关键字数大于最小值，丰满
                    rrotate(node.parent.ptr.get(x - 1), node);
                } else {
                    System.out.println("error");
                }
            } else {//node在父节点的子节点的中间，需要先判断左兄弟，再判断右兄弟。靠，感觉判断兄弟是否饱满，还是应该写一个函数，也许可以传递两个值
                //先跟饱满的借，除非两个兄弟都刚脱贫。
                if (node.parent.ptr.get(x - 1).informations.size() > n) {//左兄弟丰满
                    rrotate(node.parent.ptr.get(x - 1), node);
                } else if (node.parent.ptr.get(x + 1).informations.size() > n) {//右兄弟丰满
                    lrotate(node, node.parent.ptr.get(x + 1));
                } else {//左右兄弟都刚脱贫，需要合并
                    combine(node.parent.ptr.get(x - 1), node);
                }
            }
        }

    }

    public void pullRLeftNode(BTNode donode, int j, BTNode node, int i) {//节点删除关键字后，如果该节点有孩子，则孩子需要贡献关键字，由于孩子减少了关键字还需要向下借，一直递归到叶子。

        if (node.ptr.get(0).ptr.size() > 0) {
            pullRLeftNode(donode, j, node.ptr.get(0), 0);
        } else {
            donode.informations.add(j, node.ptr.get(0).informations.get(0));
            node.ptr.get(0).informations.remove(0);
            if (node.ptr.get(0).informations.size() < n) {
                dManageNode(node.ptr.get(0));
            }
        }
    }

    /**
     * 内部节点
     */
    private class BTNode {
        BTNode parent;  //父节点
        List<T> informations = new ArrayList<T>();  //关键字的信息
        List<BTNode> ptr = new ArrayList<BTNode>();     //分支

        public BTNode(T information, BTNode parent) {
            if (information != null) {
                informations.add(information);
                this.parent = parent;
            } else {
                this.parent = null;
            }
        }

        boolean isLeaf() {
            return (ptr.size() == 0);
        }

        boolean isNode() {
            return (ptr.size() != 0);
        }

        int infoLength() {
            return informations.size();
        }

        int ptrLength() {
            return ptr.size();
        }

    }

    private static String stringInput(String inputRequest) throws IOException {
        System.out.println(inputRequest);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("test B - balanced tree operations");
        System.out.println("*****************************");

        BsubTree<Integer> tree = new BsubTree<Integer>();

        String input;
        Integer value;
        do {
            input = stringInput("please select: [i]nsert, [d]elete, [s]how, [e]xit");
            switch (input.charAt(0)) {
                case 'i':
                    value = Integer.parseInt(stringInput("insert: "), 10);
                    tree.insert(value);
                    break;
                case 'd':
                    value = Integer.parseInt(stringInput("delete: "), 10);
                    tree.delete(value);
                    break;
                case 's':
                    System.out.println(tree.perOrder(tree.root));
                    break;
//                case 'h':
//                    System.out.println(tree.getHeight());
            }
        } while ((input.charAt(0) != 'e'));
    }

}