package com.github.houbb.data.struct.core.util.list;

import com.github.houbb.heaven.util.util.ArrayUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * 数组工具类
 * @since 0.0.1
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

    public static void main(String[] args) {
        List list = new java.util.ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(5);

        List list2 = Arrays.asList(3);
        list.removeAll(list2);
        System.out.println(list);
    }

}
