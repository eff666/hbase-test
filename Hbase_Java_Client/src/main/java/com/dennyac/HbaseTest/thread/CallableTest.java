package com.dennyac.HbaseTest.thread;

import java.util.concurrent.*;

/**
 * Created by shuyun on 2016/8/19.
 */

/*
 * 使用Callable和Future接口创建线程。具体是创建Callable接口的实现类，并实现clall()方法。
 * 并使用FutureTask类来包装Callable实现类的对象，且以此FutureTask对象作为Thread对象的target来创建线程
 *
 *
 * 一般情况下，使用Runnable接口、Thread实现的线程我们都是无法返回结果的。但是如果对一些场合需要线程返回的结果。
 * 就要使用用Callable、Future、FutureTask、CompletionService这几个类。Callable只能在ExecutorService的线程池中跑，
 * 但有返回结果，也可以通过返回的Future对象查询执行状态。Future 本身也是一种设计模式，它是用来取得异步任务的结果。
 *
 *

 */
public class CallableTest {

    public static void main(String[] args) {

        // 1、 FutureTask
        // 方法一、直接通过New Thread来启动线程
//        Callable<Integer> myCallable = new MyCallable();    // 创建MyCallable对象
//        FutureTask<Integer> ft = new FutureTask<Integer>(myCallable); //使用FutureTask来包装MyCallable对象
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println(Thread.currentThread().getName() + "++++++++" + i);
//            if (i == 3) {
//                Thread thread = new Thread(ft);   //FutureTask对象作为Thread对象的target创建新的线程
//                thread.start();                      //线程进入到就绪状态
//            }
//        }
//        System.out.println("主线程for循环执行完毕..");
//
//        try {
//            int sum = ft.get();            //取得新创建的新线程中的call()方法返回的结果
//            System.out.println("sum = " + sum);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }



        // 方法二、通过线程池来启动线程
        ExecutorService executor = Executors.newCachedThreadPool();
        MyCallable task1 = new MyCallable();
        MyCallable task2 = new MyCallable();
        Future<Integer> result1 = executor.submit(task1);
        Future<Integer> result2 = executor.submit(task2);
        executor.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        try {
            System.out.println("task1返回结果:"  + result1.get());
            System.out.println("task2返回结果:"  + result2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }




    static class MyCallable implements Callable<Integer> {
        private int i = 0;


        // 与run()方法不同的是，call()方法具有返回值
        //线程执行体
        @Override
        public Integer call() {
            int sum = 0;

            for (; i < 10; i++) {
                System.out.println(Thread.currentThread().getName());
                sum += i;
            }
            return sum;
        }
    }
}


//class MyCallable implements Callable<Integer> {
//    private int i = 0;
//
//    // 与run()方法不同的是，call()方法具有返回值
//    @Override
//    public Integer call() {
//        int sum = 0;
//        for (; i < 10; i++) {
//            System.out.println(Thread.currentThread().getName() + "-------" + i);
//            sum += i;
//        }
//        return sum;
//    }
//}
