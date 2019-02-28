package com.barton.thread;

public class ThreadLocalDemo {
    private static ThreadLocal<Object> threadLocal = new ThreadLocal<Object>() {
        @Override
        protected Object initialValue() {
            System.out.println("调用get方法时，当前线程共享变量没有设置，调用initialValue获取默认值！");
            return null;
        }
    };

    public static void main(String[] args) {
        new Thread(new MyIntegerTask("thread_1")).start();
        new Thread(new MyIntegerTask("thread_2")).start();
        new Thread(new MyStringTask("thread_5")).start();
        new Thread(new MyStringTask("thread_6")).start();
    }

    public static class MyIntegerTask implements Runnable {
        private String name;

        MyIntegerTask(String name) {
            this.name = name;
        }

        public void run() {
            for (int i = 0; i < 5; i++) {
                // ThreadLocal.get方法获取线程变量
                if (null == ThreadLocalDemo.threadLocal.get()) {
                    // ThreadLocal.et方法设置线程变量
                    ThreadLocalDemo.threadLocal.set(0);
                    System.out.println("线程" + name + ": 0");
                } else {
                    int num = (Integer) ThreadLocalDemo.threadLocal.get();
                    ThreadLocalDemo.threadLocal.set(num + 1);
                    System.out.println("线程" + name + ": " + ThreadLocalDemo.threadLocal.get());
                    if (i == 3) {
                        ThreadLocalDemo.threadLocal.remove();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class MyStringTask implements Runnable {
        private String name;

        MyStringTask(String name) {
            this.name = name;
        }

        public void run() {
            for (int i = 0; i < 5; i++) {
                if (null == ThreadLocalDemo.threadLocal.get()) {
                    ThreadLocalDemo.threadLocal.set("a");
                    System.out.println("线程" + name + ": a");
                } else {
                    String str = (String) ThreadLocalDemo.threadLocal.get();
                    ThreadLocalDemo.threadLocal.set(str + "a");
                    System.out.println("线程" + name + ": " + ThreadLocalDemo.threadLocal.get());
                }
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
