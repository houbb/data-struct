package com.github.houbb.data.struct.core.concurrent;

import java.util.Random;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * 入门案例
 * @author 老马啸西风
 * @since 1.0.0
 */
public class LinkedTransferQueueDemo {
    
    private static class Producer implements Runnable {

        private final TransferQueue<String> queue;

        public Producer(TransferQueue<String> queue) {
            this.queue = queue;
        }

        private String produce() {
            return Thread.currentThread().getName()+": your lucky number " + (new Random().nextInt(100));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (queue.hasWaitingConsumer()) {
                        queue.transfer(produce());
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Consumer implements Runnable {
        private final TransferQueue<String> queue;

        public Consumer(TransferQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName()+": consumer 【" + queue.take()+"】");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TransferQueue<String> queue = new LinkedTransferQueue<>();
        Thread producer = new Thread(new Producer(queue));
        producer.setDaemon(true); //设置为守护进程使得线程执行结束后程序自动结束运行
        producer.start();
        for (int i = 0; i < 5; i++) {
            Thread consumer = new Thread(new Consumer(queue));
            consumer.setDaemon(true);
            consumer.start();
            try {
                // 消费者进程休眠一秒钟，以便以便生产者获得CPU，从而生产产品
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
