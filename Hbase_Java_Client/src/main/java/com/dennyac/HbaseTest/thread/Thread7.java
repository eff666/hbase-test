package com.dennyac.HbaseTest.thread;

/**
 * Created by shuyun on 2016/12/9.
 */
class Thread7 extends Thread {
    public Thread7(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("" + this.getName() + "-----" + i); // 当i为30时，该线程就会把CPU时间让掉，让其他或者自己的线程执行（也就是谁先抢到谁执行）
            if (i == 3) {
                this.yield();
            }
        }
    }

    public static void main(String[] args) {
        Thread7 yt1 = new Thread7("张三");
        Thread7 yt2 = new Thread7("李四");
        yt1.start();
        yt2.start();
    }
}
