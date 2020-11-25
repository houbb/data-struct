package com.github.houbb.data.struct.core.concurrent;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author binbin.hou
 * @since 1.0.0
 */
public class ExchangerDemo {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        final Exchanger<String> exchanger = new Exchanger<>();
        executor.execute(new ExchangeRunnable(exchanger, "one"));
        executor.execute(new ExchangeRunnable(exchanger, "two"));

        executor.shutdown();
    }

    private static class ExchangeRunnable implements Runnable {

        private final Exchanger<String> exchanger;

        private final String data;

        private ExchangeRunnable(Exchanger<String> exchanger, String data) {
            this.exchanger = exchanger;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() +" 正在把数据 "+ data + " 交换出去" );
                Thread.sleep((long) (Math.random()*1000));

                String data2 = exchanger.exchange(data);
                System.out.println(Thread.currentThread().getName() + " 交换数据到  "+ data2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
