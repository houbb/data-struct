package com.github.houbb.data.struct.core.util.map;

import org.junit.Test;

/**
 * @author binbin.hou
 * @since 0.0.3
 */
public class MyHashMapTest {

    @Test
    public void putTest() {
        MyHashMap<String, String> map = new MyHashMap<>(2, true);
        map.put("1", "1");
        map.put("1", "2");
    }

    @Test
    public void putReHashTest() {
        MyHashMap<String, String> map = new MyHashMap<>(2, true);
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "2");
    }

    @Test
    public void removeTest() {
        MyHashMap<String, String> map = new MyHashMap<>(2, true);
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");

        map.remove("3");
        map.remove("2");
        map.remove("1");
    }

}
