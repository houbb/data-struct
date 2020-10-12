package com.github.houbb.data.struct.core.util.map;

import com.github.houbb.data.struct.util.HashUtil;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.util.*;

/**
 * 自己实现的 hash map
 * <p>
 * （1）所有的 hash 值相同的元素，放在同一个桶中
 * （2）新增时
 * 2.1 hash 值相同，且 equals() 的，则认为相同，使用替换。
 * 2.2 不同的，则使用链表，新增。
 * 新增时，进行判断，如果 size() 超出阈值，且 hash 的位置有元素，则进行 rehash()
 * <p>
 * 这样是为了避免 hash 的桶数太少，导致的查找性能下降。
 * 个人觉得，现在觉得这种方式很浪费。
 * 比如：容量=8 当 size=6 的时候可能就要进行 rehash
 * （size gte 阈值，且 hash 的位置，已经存在元素。）
 * 某种角度而言，这是没有必要的。
 * 因为即使在同一个桶上的数据出现重复，如果桶中链表的数量小于8，其实遍历性能并不差。
 * <p>
 * 优化思路：可以考虑将 rehash 的条件调整为，当前 hash 的桶位置有元素，且当前桶的链表数量已经达到了8。
 * <p>
 * rehash 的优化思路：
 * 可以参考 redis，进行渐进式 rehash。
 * <p>
 * （3）hash 的简化
 * jdk 实现中，对于 null 值做了特殊处理。其实感觉没必要，直接 null 的 hash 为0，比较的时候认为相等即可。
 *
 * @param <K> key 泛型
 * @param <V> value 泛型
 * @author binbin.hou
 * @see HashMap
 * @since 0.0.1
 */
public class MyHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private static final Log log = LogFactory.getLog(MyHashMap.class);

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
     * <p>
     * 当达到这个阈值的时候，直接进行两倍的容量扩充+rehash。
     */
    private final double factor = 1.0;

    /**
     * 缩容
     *
     * @since 0.0.4
     */
    private final double reduceFactor = 0.5;

    /**
     * 最小大小
     *
     * @since 0.0.4
     */
    private static final int MIN_CAPACITY = 2;

    /**
     * 用来存放信息的 table 数组。
     * 数组：数组的下标是一个桶，桶对应的元素 hash 值相同。
     * 桶里放置的是一个链表。
     * <p>
     * 可以理解为 table 是一个 ArrayList
     * arrayList 中每一个元素，都是一个 DoubleLinkedList
     */
    private List<List<Entry<K, V>>> table;

    /**
     * 是否开启 debug 模式
     *
     * @since 0.0.3
     */
    private boolean debugMode = false;

    public MyHashMap() {
        this(8);
    }

    /**
     * 初始化 hash map
     *
     * @param capacity 初始化容量
     */
    public MyHashMap(int capacity) {
        this(capacity, false);
    }

    /**
     * 初始化 hash map
     *
     * @param capacity  初始化容量
     * @param debugMode 是否开启 debug 模式
     * @since 0.0.3
     */
    public MyHashMap(int capacity, boolean debugMode) {
        this.capacity = capacity;
        // 初始化最大为容量的个数，如果 hash 的非常完美的话。
        this.table = new ArrayList<>(capacity);
        // 初始化为空列表
        for (int i = 0; i < capacity; i++) {
            this.table.add(i, new ArrayList<Entry<K, V>>());
        }

        this.debugMode = debugMode;
    }


    // 实现 CRUD

    /**
     * 存储一个值
     *
     * @param key   键
     * @param value 值
     * @return 值
     */
    @Override
    public V put(K key, V value) {
        // 计算 index 值
        int hash = HashUtil.hash(key);
        int index = HashUtil.indexFor(hash, this.capacity);

        // 判断是否为替换
        List<Entry<K, V>> entryList = new ArrayList<>();
        if (index < this.table.size()) {
            entryList = this.table.get(index);
        }

        // 遍历
        for (Entry<K, V> entry : entryList) {
            // 二者的 key 都为 null，或者二者的 key equals()
            final K entryKey = entry.getKey();
            if (ObjectUtil.isNull(key, entryKey)
                    || key.equals(entryKey)) {
                // 更新新的 entry
                entry.setValue(value);

                if (debugMode) {
                    log.debug("put 为替换元素，table 信息为：");
                    printTable();
                }

                return value;
            }
        }

        // 新增：
        this.createNewEntry(hash, index, key, value);
        this.size++;

        if (debugMode) {
            log.debug("put 为新增元素，table 信息为：");
            printTable();
        }
        return value;
    }

    /**
     * 创建一个新的明细
     * （1）获取当前 index 对应的链表
     * （2）判断是否需要 rehash
     * 如果当前链表存在，且大小超过阈值8，则进行 rehash。
     * （3）rehash 之后，或者不需要 rehash
     * <p>
     * 都将最后的 index，然后将新元素放在对应的链表中。
     *
     * @param hash       hash 值
     * @param tableIndex 下标
     * @param key        key
     * @param value      value
     */
    private void createNewEntry(int hash,
                                int tableIndex,
                                final K key,
                                final V value) {
        Entry<K, V> entry = new DefaultMapEntry<>(key, value);

        // 是否需要扩容
        if (isNeedExpand()) {
            this.capacity = this.capacity * 2;
            rehash(this.capacity);

            // 重新计算 tableIndex
            tableIndex = HashUtil.indexFor(hash, this.capacity);
        }

        //  添加元素
        List<Entry<K, V>> list = new ArrayList<>();
        if (tableIndex < this.table.size()) {
            list = table.get(tableIndex);
        }
        list.add(entry);

        if (debugMode) {
            log.debug("Key: {} 对应的 tableIndex: {}", key, tableIndex);
        }
        this.table.set(tableIndex, list);
    }


    /**
     * 直接 rehash 的流程
     * <p>
     * 当然这个 rehash 的方法可以抽象，和 put 复用，暂时不做处理。
     *
     * @param newCapacity 新的 table 的容量
     * @since 0.0.3
     */
    private void rehash(final int newCapacity) {
        List<List<Entry<K, V>>> newTable = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newTable.add(i, new ArrayList<Entry<K, V>>());
        }

        // 遍历元素，全部放置到新的 table 中
        for (List<Entry<K, V>> list : table) {
            for (Entry<K, V> entry : list) {
                int hash = HashUtil.hash(entry);
                int index = HashUtil.indexFor(hash, newCapacity);

                //  添加元素
                // 获取列表，避免数组越界
                List<Entry<K, V>> newList = new ArrayList<>();
                if (index < newTable.size()) {
                    newList = newTable.get(index);
                }
                // 添加元素到列表
                // 元素不存在重复，所以不需要考虑更新
                newList.add(entry);
                newTable.set(index, newList);
            }
        }

        // 将新的 table 赋值到原来的 table 上
        this.table = newTable;

        if (debugMode) {
            log.debug("rehash: {} 完成，table 内容为：", newCapacity);
            printTable();
        }
    }

    /**
     * 是否需要扩容
     *
     * @return 是否
     * @since 0.0.3
     */
    private boolean isNeedExpand() {
        // 验证比例
        double rate = size * 1.0 / capacity * 1.0;
        return rate >= factor;
    }


    /**
     * 删除一个元素
     * （1）元素不存在，直接返回 null
     * （2）元素存在，移除元素，判断是否需要缩容
     *
     * @param key 元素信息
     * @return 结果
     * @since 0.0.3
     */
    @Override
    public V remove(Object key) {
        // 计算 index 值
        int hash = HashUtil.hash(key);
        int index = HashUtil.indexFor(hash, this.capacity);

        // 遍历
        List<Entry<K, V>> entryList = this.table.get(index);
        Iterator<Entry<K, V>> iterator = entryList.iterator();

        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();

            // 二者的 key 都为 null，或者二者的 key equals()
            final K entryKey = entry.getKey();
            if (ObjectUtil.isNull(key, entryKey)
                    || key.equals(entryKey)) {
                // 移除元素
                iterator.remove();
                size--;

                if (isNeedReduce()) {
                    this.capacity = this.capacity / 2;
                    rehash(this.capacity);

                    if(debugMode) {
                        log.debug("缩容完成");
                    }
                }

                if (debugMode) {
                    log.debug("删除后的 table 信息");
                    printTable();
                }
            }
        }

        return null;
    }

    /**
     * 是否需要缩容
     * （1）元素比例为一半
     * （2）当前容量大于2
     *
     * @return 是否
     * @since 0.0.4
     */
    private boolean isNeedReduce() {
        // 验证比例
        double rate = size * 1.0 / capacity * 1.0;
        return rate <= reduceFactor && (this.capacity > MIN_CAPACITY);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }


    /**
     * 打印 table 信息
     *
     * @since 0.0.3
     */
    private void printTable() {
        for (List<Entry<K, V>> list : this.table) {
            if(CollectionUtil.isEmpty(list)) {
                continue;
            }

            for (Entry<K, V> entry : list) {
                System.out.print(entry + " ");
            }
            System.out.println();
        }
    }

}
