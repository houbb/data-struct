package com.github.houbb.data.struct.core.util.map;

import java.util.Map;

/**
 * 默认的 map entry
 * TODO: 调整为 fluent 模式。
 * @author binbin.hou
 * @since 0.0.1
 */
public class DefaultMapEntry<K,V> implements Map.Entry<K,V>{

    private K key;

    private V value;

    @Override
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return this.value;
    }

}
