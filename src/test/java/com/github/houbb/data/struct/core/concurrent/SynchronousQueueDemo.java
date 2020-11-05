package com.github.houbb.data.struct.core.concurrent;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author 老马啸西风
 * @since 1.0.0
 */
public class SynchronousQueueDemo {

    public static void main(String[] args) {
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        new Writer(queue).start();
        new Reader(queue).start();
    }

    private static class Writer extends Thread {
        SynchronousQueue<Integer> queue;

        public Writer(SynchronousQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println("开始设置第 " + i + " 个元素");
                try {
                    TimeUnit.SECONDS.sleep(2);
                    queue.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 消息读取者
     */
    private static class Reader extends Thread {
        SynchronousQueue<Integer> queue;

        public Reader(SynchronousQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("读取信息: " + queue.take() + "\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
