package com.dennyac.HbaseTest.reflect.Proxy;

/**
 * Created by shuyun on 2016/8/16.
 */

//真实角色
    //代理对象所代表的真实角色，最终为代理角色引用的对象
public class RealSubject implements Subject {
    @Override
    public void Request() {
        System.out.println("test RealSubject");
    }
}
