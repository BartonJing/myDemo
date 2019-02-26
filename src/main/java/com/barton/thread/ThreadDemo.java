package com.barton.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 生产消费 线程 Demo
 */
public class ThreadDemo {

    public static void main(String arts []){
        ThreadDemo t = new ThreadDemo();
        BlockingQueue queue = new LinkedBlockingQueue(5);
        ExecutorService es = Executors.newCachedThreadPool();
        //ExecutorService es = Executors.newFixedThreadPool(2);
        Producer producer_1 = t.new Producer("producer_1",queue);
        Producer producer_2 = t.new Producer("producer_2",queue);
        Producer producer_3 = t.new Producer("producer_3",queue);
        Consumer consumer_1 = t.new Consumer("consumer_1",queue);
        Consumer consumer_2 = t.new Consumer("consumer_2",queue);
        es.submit(producer_1);
        es.submit(producer_2);
        es.submit(producer_3);
        es.submit(consumer_1);
        es.submit(consumer_2);

    }

    /**
     * 消费
     */
    public class Consumer implements Runnable{
        private String name;
        private BlockingQueue queue;

        public Consumer(String name, BlockingQueue queue) {
            this.name = name;
            this.queue = queue;
        }

        public void run() {
            while (true){
                try {
                    Product product = (Product)queue.take();
                    System.out.println(name+"准备消费" + product);
                    Thread.sleep(500);
                    System.out.println(name+"已经消费" + product);
                    System.out.println("===============");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class Consumer1 implements Callable<Boolean> {
        private String name;
        private BlockingQueue queue;

        public Consumer1(String name, BlockingQueue queue) {
            this.name = name;
            this.queue = queue;
        }

        public Boolean call() throws Exception {
            return null;
        }
    }


    /**
     * 生产
     */
    public class Producer implements Runnable{
        private String name;
        private BlockingQueue queue;
        public Producer(String name,BlockingQueue queue){
            this.name = name;
            this.queue = queue;
        }
        public void run() {
            while (true){
                try {
                    Product product = new Product((int)(Math.random()*100));
                    System.out.println(name+"准备生产" + product);
                    Thread.sleep(500);
                    queue.put(product);
                    System.out.println(name+"已经生产" + product);
                    System.out.println("===============");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public class Product {
        private int id;

        public Product(int id) {
            this.id = id;
        }

        public String toString() {// 重写toString方法
            return "产品：" + this.id;
        }
    }


}
