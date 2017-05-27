package com.dennyac.HbaseTest.designpattern;

/**
 * Created by shuyun on 2016/8/17.
 */

//1.3 静态内部类
//这种比上面1、2都好一些，既实现了线程安全，又避免了同步带来的性能影响。
public  class Singleton3 {
    private static class LazyHolder {
        private static final Singleton3 INSTANCE = new Singleton3();
    }

    private Singleton3() {
    }

    public static final Singleton3 getInstance() {
        return LazyHolder.INSTANCE;
    }
}