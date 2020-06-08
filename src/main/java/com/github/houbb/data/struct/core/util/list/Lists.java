package com.github.houbb.data.struct.core.util.list;

import com.github.houbb.heaven.util.util.ArrayUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * 数组工具类
 * （1）ArrayList
 * （2）DoubleLinkedList
 * （3）CowList
 * （4）SkipList
 *
 * @since 0.0.1
 * @author binbin.hou
 */
public final class Lists {

    private Lists(){}

    /**
     * Shared empty array instance used for empty instances.
     * @since 0.0.1
     */
    public static final Object[] EMPTY_ARRAY = {};

    /**
     * 创建 arrayList
     * @param elements 元素对象
     * @param <E> 泛型
     * @return 结果列表
     * @since 0.0.1
     */
    public static <E> List<E> newArrayList(final E ... elements) {
        if(ArrayUtil.isEmpty(elements)) {
            return Collections.emptyList();
        }

        List<E> list = new ArrayList<>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }

    /**
     * 输出
     *
     * 1. 用于没有实现 iterator
     * @param list 列表
     * @param <E> 泛型
     * @since 0.0.3
     */
    public static <E> void print(final List<E> list) {
        for(int i = 0; i < list.size(); i++) {
            E elem = list.get(i);
            System.out.print(elem+"->");
        }
    }

}
