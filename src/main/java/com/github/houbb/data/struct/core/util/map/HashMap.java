package com.github.houbb.data.struct.core.util.map;

import com.github.houbb.data.struct.util.HashUtil;
import com.github.houbb.heaven.util.lang.ObjectUtil;

import java.util.*;

/**
 * 自己实现的 hash map
 *
 * （1）所有的 hash 值相同的元素，放在同一个桶中
 * （2）新增时
 * 2.1 hash 值相同，且 equals() 的，则认为相同，使用替换。
 * 2.2 不同的，则使用链表，新增。
 * 新增时，进行判断，如果 size() 超出阈值，且 hash 的位置有元素，则进行 rehash()
 *
 * 这样是为了避免 hash 的桶数太少，导致的查找性能下降。
 * 个人觉得，现在觉得这种方式很浪费。
 * 比如：容量=8 当 size=6 的时候可能就要进行 rehash
 * （size >= 阈值，且 hash 的位置，已经存在元素。）
 * 某种角度而言，这是没有必要的。
 * 因为即使在同一个桶上的数据出现重复，如果桶中链表的数量小于8，其实遍历性能并不差。
 *
 * 优化思路：可以考虑将 rehash 的条件调整为，当前 hash 的桶位置有元素，且当前桶的链表数量已经达到了8。
 *
 * rehash 的优化思路：
 * 可以参考 redis，进行渐进式 rehash。
 *
 * （3）hash 的简化
 * jdk 实现中，对于 null 值做了特殊处理。其实感觉没必要，直接 null 的 hash 为0，比较的时候认为相等即可。
 * @author binbin.hou
 * @since 0.0.1
 * @param <K> key 泛型
 * @param <V> value 泛型
 */
public class HashMap<K,V> extends AbstractMap<K,V> implements Map<K,V> {

    /**
     * 容量
     * 默认为 8
     */
    private int capacity;

    /**
     * 统计大小的信息
     */
    private int size = 0;

    /**
     * 阈值
     * 阈值=容量*factor
     * 暂时不考虑最大值的问题
     *
     * 当达到这个阈值的时候，直接进行两倍的容量扩充+rehash。
     *
     * 单个桶的数量达到8才进行 rehash。
     */
    private int threshold = 8;

    /**
     * 用来存放信息的 table 数组。
     * 数组：数组的下标是一个桶，桶对应的元素 hash 值相同。
     * 桶里放置的是一个链表。
     *
     * 可以理解为 table 是一个 ArrayList
     * arrayList 中每一个元素，都是一个 LinkedList
     */
    private List<List<Entry<K, V>>> table;

    public HashMap() {
        this(8);
    }

    /**
     * 初始化 hash map
     * @param capacity 初始化容量
     */
    public HashMap(int capacity) {
        this.capacity = capacity;
        // 初始化最大为容量的个数，如果 hash 的非常完美的话。
        this.table = new ArrayList<>(capacity);
    }


    // 实现 CRUD

    /**
     * 存储一个值
     * @param key
     * @param value
     * @return
     */
    @Override
    public V put(K key, V value) {
        // 计算 index 值
        int hash = HashUtil.hash(key);
        int index = HashUtil.indexFor(hash, this.table.size());

        // 判断是否为替换
        List<Entry<K,V>> entryList = this.table.get(index);
        // 遍历
        for(Entry<K,V> entry : entryList) {
            // 二者的 key 都为 null，或者二者的 key equals()
            final K entryKey = entry.getKey();
            if(ObjectUtil.isNull(key, entryKey)
                || key.equals(entryKey)) {
                // 更新新的 entry
                entry.setValue(value);
            }
        }

        // 新增：

        return super.put(key, value);
    }

    /**
     * 创建一个新的明细
     * （1）获取当前 index 对应的链表
     * （2）判断是否需要 rehash
     * 如果当前链表存在，且大小超过阈值8，则进行 rehash。
     * （3）rehash 之后，或者不需要 rehash
     *
     * 都将最后的 index，然后将新元素放在对应的链表中。
     * @param tableIndex
     * @param key
     * @param value
     */
    private void createNewEntry(final int tableIndex,
                                final K key,
                                final V value) {

    }

    /**
     * 批量添加，为了避免多次 rehash 可以首先扩容完成后，在进行相关元素的存储。
     * @param m map 集合
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

}
