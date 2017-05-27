package com.dennyac.HbaseTest.thread;

import org.apache.commons.lang.math.RandomUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shuyun on 2016/9/4.
 */
//public class ThreadSingleton {
//    public static Integer data;
//    public static void main(String[] args){
//        for (int i = 0; i < 2; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    data = RandomUtils.nextInt();
//                    System.out.println("Thread: " + Thread.currentThread().getName() + " , Create Data is " + data);
//                    new A().print();
//                    new B().print();
//                }
//            }).start();
//        }
//    }
//    static class A{
//        public void print(){
//            System.out.println("Class: A , Thread: " + Thread.currentThread().getName() + ", Data : " + data);
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    static class B{
//        public void print(){
//            System.out.println("Class: B , Thread: " + Thread.currentThread().getName() + ", Data : " + data);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}


//public class ThreadSingleton {
//    public static Integer data;
//    public static void main(String[] args){
//        for (int i = 0; i < 2; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    data = RandomUtils.nextInt();
//                    System.out.println("Thread: " + Thread.currentThread().getName() + " , Create Data is " + data);
//                    new A().print();
//                    new B().print();
//                }
//            }).start();
//        }
//    }
//    static class A{
//        public void print(){
//            System.out.println("Class: A , Thread: " + Thread.currentThread().getName() + ", Data : " + data);
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    static class B{
//        public void print(){
//            System.out.println("Class: B , Thread: " + Thread.currentThread().getName() + ", Data : " + data);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}


//public class ThreadSingleton {
//    public static Map<Thread,Integer> dataMap = new HashMap<Thread, Integer>();
//    public static void main(String[] args){
//        for (int i = 0; i < 2; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Integer data = RandomUtils.nextInt();
//                    System.out.println("Thread: " + Thread.currentThread().getName() + " , Create Data is " + data);
//                    dataMap.put(Thread.currentThread(),data);
//                    new A().print();
//                    new B().print();
//                }
//            }).start();
//        }
//    }
//    static class A{
//        public void print(){
//            System.out.println("Class: A , Thread: " + Thread.currentThread().getName() + ", Data : " + dataMap.get(Thread.currentThread()));
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    static class B{
//        public void print(){
//            System.out.println("Class: B , Thread: " + Thread.currentThread().getName() + ", Data : " + dataMap.get(Thread.currentThread()));
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}

public class ThreadSingleton {
    public static ThreadLocal<Integer> dataMap = new ThreadLocal<Integer>();
    public static void main(String[] args){
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Integer data = RandomUtils.nextInt();
                    System.out.println("Thread: " + Thread.currentThread().getName() + " , Create Data is " + data);
                    dataMap.set(data);
                    new A().print();
                    new B().print();
                }
            }).start();
        }
    }
    static class A{
        public void print(){
            System.out.println("Class: A , Thread: " + Thread.currentThread().getName() + ", Data : " + dataMap.get());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class B{
        public void print(){
            System.out.println("Class: B , Thread: " + Thread.currentThread().getName() + ", Data : " + dataMap.get());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
