package com.github.houbb.data.struct.core.util.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * COW HashMap
 * @author 老马啸西风
 * @since 0.0.4
 * @param <K> key
 * @param <V> value
*/
public class CopyOnWriteHashMap<K,V> implements Map<K, V>, Cloneable {

    /**
     * 内部 HashMap
     * @since 0.0.4
     */
    private volatile Map<K, V> internalMap;

    /**
     * 可重入锁
     * @since 0.0.4
     */
    private final ReentrantLock lock;

    /**
     * 无参构造器
     *
     * 初始化对应的属性
     * @since 0.0.4
     */
    public CopyOnWriteHashMap() {
        this.internalMap = new HashMap<K, V>();
        this.lock = new ReentrantLock();
    }

    @Override
    public int size() {
        return internalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return internalMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return internalMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return internalMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            // 拷贝一份
            Map<K, V> newMap = new HashMap<>(internalMap);
            V val = newMap.put(key, value);

            // 设置为新的
            internalMap = newMap;
            return val;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            // 拷贝一份
            Map<K, V> newMap = new HashMap<>(internalMap);
            V val = newMap.remove(key);

            // 设置为新的
            internalMap = newMap;
            return val;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            // 拷贝一份
            Map<K, V> newMap = new HashMap<>(internalMap);
            newMap.putAll(m);

            // 设置为新的
            internalMap = newMap;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            internalMap.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        return internalMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return internalMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return internalMap.entrySet();
    }

}
