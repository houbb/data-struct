package com.github.houbb.data.struct.core.util.list;

import org.junit.Test;

import java.util.List;

/**
 * @author binbin.hou
 * @since 0.0.3
 */
public class CircularLinkedListTest {

    /**
     * 约瑟夫环问题
     *
     * 据说著名犹太历史学家 Josephus有过以下的故事：在罗马人占领乔塔帕特后，39 个犹太人与Josephus及他的朋友躲到一个洞中，39个犹太人决定宁愿死也不要被敌人抓到，于是决定了一个自杀方式，41个人排成一个圆圈，由第1个人开始报数，每报数到第3人该人就必须自杀，然后再由下一个重新报数，直到所有人都自杀身亡为止。然而Josephus 和他的朋友并不想遵从。
     *
     * 首先从一个人开始，越过k-2个人（因为第一个人已经被越过），并杀掉第k个人。
     * 接着，再越过k-1个人，并杀掉第k个人。
     * 这个过程沿着圆圈一直进行，直到最终只剩下一个人留下，这个人就可以继续活着。问题是，给定了和，一开始要站在什么地方才能避免被处决？Josephus要他的朋友先假装遵从，他将朋友与自己安排在第16个与第31个位置，于是逃过了这场死亡游戏。
     * @since 0.0.3
     */
    @Test
    public void josephRingTest() {
        List<Integer> list = new CircularLinkedList<>();

        //1. 初始化
        final int total = 31;
        final int space = 3;
        for(int i = 0; i < total; i++) {
            list.add(i);
        }
        Lists.print(list);

        //2. 删除
    }

}
