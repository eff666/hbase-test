package com.dennyac.HbaseTest.thread;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuyun on 2017/3/29.
 */
public class CounterExample {

    //同步代码块
    static class Counter {
        //注意，此处的变量是静态的，属于类变量
        static long count = 0;
        public  void add() {
            //同步的是这个类本身
            synchronized(Counter.class){
                count++;
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                System.out.println(Thread.currentThread().getName() + "--" + count);
            }
        }
    }

    static class CounterThread extends Thread {
        protected Counter counter = null;
        public CounterThread(Counter counter) {
            this.counter = counter;
        }
        public void run() {
            //用多个线程调用同步实例方法
            for (int i = 0; i < 5; i++) {
                counter.add();
            }
        }
    }

    public static void main(String[] args) {
        //构造两个实例，让每个线程访问一个实例
        Counter counter1 = new Counter();
        Counter counter2 = new Counter();
        CounterThread threadA = new CounterThread(counter1);
        CounterThread threadB = new CounterThread(counter2);
        threadA.start();
        threadB.start();
        List<String> list = new ArrayList<String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    }
}
