package com.dennyac.HbaseTest.designpattern;

/**
 * Created by shuyun on 2016/8/17.
 */
public class Singleton {

    //1、懒汉式单例类。在第一次调用的时候实例化自己
    private Singleton() {
    }
    private static Singleton singleton = null;
    // 静态工厂方法
    public static Singleton getInstance() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }

    //Singleton通过将构造方法限定为private避免了类在外部被实例化，在同一个虚拟机范围内，Singleton的唯一实例只能通过getInstance()方法访问。

    /*
    但是以上懒汉式单例的实现没有考虑线程安全问题，它是线程不安全的，并发环境下很可能出现多个Singleton实例，要实现线程安全，有以下三种方式，
    都是对getInstance这个方法改造，保证了懒汉式单例的线程安全
     */
    //1.1 在getInstance方法上加同步
    public static synchronized Singleton getInstance1() {
        if (singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }

    //1.2 双重检查锁定
    // 双重检查锁定不是线程安全的，如果要用这种方式，需要使用volatile关键字。
    private static volatile Singleton singletonVa = null;
    public static Singleton getInstance2() {
        if (singletonVa == null) {
            synchronized (Singleton.class) {
                if (singletonVa == null) {
                    singletonVa = new Singleton();
                }
            }
        }
        return singletonVa;
    }

    //2. 双重检查锁
    //双重检查锁不是线程安全的，如果要用这种方式，需要使用volatile关键字
    private static volatile Singleton singletonVo = null;
    public static Singleton getInstance3(){
        if(singletonVo == null){
            synchronized(Singleton.class){
                if(singletonVa == null){
                    singletonVa = new Singleton();
                }
            }
        }
        return singletonVo;
    }

    //1.3 静态内部类


    //2、饿汉式单例
    //饿汉式单例类.在类初始化时，已经自行实例化
    //饿汉式在类创建的同时就已经创建好一个静态的对象供系统使用，以后不再改变，所以天生是线程安全的。
//    public class Singleton {
//        private Singleton() {
//        }
//
//        private static final Singleton single = new Singleton();
//
//        // 静态工厂方法
//        public static Singleton getInstance() {
//            return single;
//        }
//    }


}


