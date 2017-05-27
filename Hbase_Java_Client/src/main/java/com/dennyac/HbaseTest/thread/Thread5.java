package com.dennyac.HbaseTest.thread;

/**
 * Created by shuyun on 2016/12/9.
 */

//主线程比子线程早结束

class Thread5 extends Thread {

    private String name;

    public Thread5(String name) {
        super(name);
        this.name = name;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " 线程运行开始!");
        for (int i = 0; i < 2; i++) {
            System.out.println("子线程" + name + "运行 : " + i);
            try {
                sleep((int) Math.random() * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " 线程运行结束!");
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "主线程运行开始!");
        Thread5 mTh1 = new Thread5("A");
        Thread5 mTh2 = new Thread5("B");
        mTh1.start();
        mTh2.start();
        System.out.println(Thread.currentThread().getName() + "主线程运行结束!");
    }
}
