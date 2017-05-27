package com.dennyac.HbaseTest.thread;

/**
 * Created by shuyun on 2016/8/18.
 */


import org.testng.annotations.Test;

/**
 * 使用继承Thread类方式实现
 * new一个thread或者写个thread子类，覆盖它的run方法。(new 一个thread并覆盖run方法实际上是匿名内部类的一种方式)
 * 继承Thread类，重写该类的run()方法
 *
 *
 * 使用Thread类模拟3个售票窗口共同卖10张火车票的程序
 *
 * 没有共享数据，每个线程各卖10张火车票
 *
 */

public class ThreadTest {
    @Test
    public  void main(){

//        //1、第一种实现方法
//        //不共享数据，各自卖各自的，共30
//        for(int i = 0; i < 3; i++) {
//            System.out.println("---------------" + i + "---------");
//            new Thread() {
//                @Override
//                public void run() {
//                    int tickets = 10;
//                    while (tickets > 0) {
//                        System.out.println(this.getName() + "卖出第【" + tickets-- + "】张火车票");
//                    }
//                }
//            }.start();
//        }


        //2、第一种实现方法
        //2.1 不共享数据，各自卖各自的，共30
//        for(int i = 0; i < 3; i++){
//            System.out.println(Thread.currentThread().getName()  + "--------" + i);
//            //创建一个新的线程  myThread  此线程进入新建状态
//            Thread myThread = new MyThread();
//            //调用start()方法使得线程进入就绪状态
//            //此时此线程并不一定会马上得以执行，这取决于CPU调度时机
//            //CPU调度就绪状态中的哪个线程具有一定的随机性
//            myThread.start();
//
//            //new Thread(new MyThread()).start();
//        }

        // 1.2  不共享数据，各自卖各自的，共30
        new MyThread().start();
        new MyThread().start();
        new MyThread().start();
    }

    public class MyThread extends Thread{
        private  int tickets = 10;

        // run()方法的方法体代表了线程需要完成的任务，称之为线程执行体
        @Override
        public void run() {
            while(tickets > 0){
                System.out.println(this.getName() + ": 卖出第【" + tickets-- + "】张火车票");
            }
        }
    }
}
