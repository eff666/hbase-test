package com.dennyac.HbaseTest.thread;

/**
 * Created by shuyun on 2016/8/18.
 */


/**
 * 使用实现Runnable接口方式实现
 * 自定义一个runnable接口的实现类，然后构造一个thread，即对thread传入一个runnable接口类。
 *
 * 使用Runnable接口模拟3个售票窗口共同卖10张火车票的程序
 *
 * 共享数据，3个线程共同卖这10张火车票
 *
 */

public class RunableTest {

    private static int tickets = 10;

    public static void main(String[] args) {

        // 1、第一种方法
//        for(int i = 0; i < 3; i++) {
//            System.out.println(Thread.currentThread().getName());
//            new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    while (tickets > 0) {
//                        System.out.println(Thread.currentThread().getName() + ": 卖出第【" + tickets-- + "】张火车票");
//                    }
//                }
//            }).start();
//        }

        //2、第二种方法
        for(int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName());
            // 创建一个Runnable实现类的对象
            Runnable myRunnable = new MyThread();
            // 将myRunnable作为Thread target创建新的线程
            Thread thread = new Thread(myRunnable);
            // 调用start()方法使得线程进入就绪状态
            thread.start();
           // new Thread(new ThreadTest.MyThread()).start();
        }

        //1.2
//        Runnable runnable = new MyThread();
//        new Thread(runnable).start();
//        new Thread(runnable).start();
//        new Thread(runnable).start();

    }

    public static class MyThread implements Runnable{
        public void run() {
            while(tickets > 0){
                System.out.println(Thread.currentThread().getName() + ": 卖出第【" + tickets-- + "】张火车票");
            }
        }
    }

}
