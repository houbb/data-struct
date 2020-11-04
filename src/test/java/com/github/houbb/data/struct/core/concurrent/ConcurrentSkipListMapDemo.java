package com.github.houbb.data.struct.core.concurrent;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class ConcurrentSkipListMapDemo {

    public static void main(String[] args) {
        ConcurrentSkipListMap<String, Integer> map = new ConcurrentSkipListMap<String, Integer>();
        map.put("one", 1);
        map.put("two", 2);

        for (String key : map.keySet()) {
            System.out.print("[" + key + "," + map.get(key) + "] ");
        }

        System.out.println("\n\n开始删除元素 1");
        map.remove("one");
        for (String key : map.keySet()) {
            System.out.print("[" + key + "," + map.get(key) + "] ");
        }
    }

}
