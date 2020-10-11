package com.github.houbb.data.struct.core.util.map;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author binbin.hou
 * @since 0.0.3
 */
public class MyProgressiveHashMapTest {

    @Test
    public void putTest() {
        Map<String, String> map = new MyProgressiveReHashMap<>(2, true);
        map.put("1", "1");
        map.put("1", "2");
    }

    @Test
    public void putReHashTest() {
        Map<String, String> map = new MyProgressiveReHashMap<>(2, true);
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");

        Assert.assertEquals("1", map.get("1"));
        Assert.assertEquals("2", map.get("2"));
        Assert.assertEquals("3", map.get("3"));
    }

}
