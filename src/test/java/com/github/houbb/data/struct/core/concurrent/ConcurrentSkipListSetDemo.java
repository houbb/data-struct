package com.github.houbb.data.struct.core.concurrent;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class ConcurrentSkipListSetDemo {

//    private static Set<String> set = new ConcurrentSkipListSet<>();
    private static Set<String> set = new ConcurrentSkipListSet<>();

    public static void main(String[] args) {
        new MyThread("a").start();
        new MyThread("b").start();
    }

    private static class MyThread extends Thread {
        MyThread(String name) {
            super(name);
        }
        @Override
        public void run() {
            int i = 0;
            while (i++ < 5) {
                // “线程名” + "序号"
                String val = Thread.currentThread().getName() + (i%6);
                set.add(val);

                // 遍历 set
                for(String s : set) {
                    System.out.print(s+", ");
                }
                System.out.println();
            }
        }
    }

}
