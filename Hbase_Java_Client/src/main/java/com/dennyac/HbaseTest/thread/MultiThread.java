package com.dennyac.HbaseTest.thread;

import com.dennyac.HbaseTest.util.Object;

/**
 * Created by shuyun on 2016/8/25.
 * 思想：
 * 1、为确保线程执行顺序，必须确定唤醒、等待的顺序，所以每一个线程需要有两个对象锁。一个为前一个线程的对象锁，一个为自身对象锁。
 * 2、必须先持有前一个对象锁，即前一个线程要先释放自身对象锁，再去申请自身对象锁，两者兼备时打印；
 *    之后调用self.notify()释放自身对象锁，唤醒下一个等待线程，
 *    再调用prev.wait()，释放前一个对象锁，终止当前线程
 *
 *
 */
class MultiThreadTest implements Runnable{
    private String name;//线程名字
    private Object prev;//前一个线程对象锁
    private Object self;//自身对象锁

    MultiThreadTest(String name, Object prev, Object self){
        this.name = name;
        this.prev = prev;
        this.self = self;
    }

    @Override
    public void run() {
        int count = 10;
        while (count > 0){
            synchronized(prev){
                synchronized(self){
                    System.out.print(name);
                    count--;

                    self.notify();
                }
                try {
                    prev.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        Object a = new Object();
        Object b = new Object();
        Object c = new Object();
        new Thread(new MultiThreadTest("A", c, a)).start();//先去持有前一个对象锁，再去申请自身对象锁，两者兼备时打印
        new Thread(new MultiThreadTest("B", a, b)).start();
        new Thread(new MultiThreadTest("C", b, c)).start();
    }
}
