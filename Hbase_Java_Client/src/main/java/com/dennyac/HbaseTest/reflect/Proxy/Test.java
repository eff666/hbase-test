package com.dennyac.HbaseTest.reflect.Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by shuyun on 2016/8/16.
 */
public class Test {
    public static void main(String[] args){

        //通过Proxy.newProxyInstance构建代理对象
        RealSubject realSub = new RealSubject();
        InvocationHandler handler = new DynamicSubject(realSub);
        Class<?> classType = handler.getClass();

        Subject sub = (Subject) Proxy.newProxyInstance(classType.getClassLoader(), realSub.getClass().getInterfaces(), handler);
        System.out.println(sub.getClass());
        /*输出：
        class com.sun.proxy.$Proxy0 新建的代理对象，它实现指定的接口
         */


        //通过调用代理对象的方法去调用真实角色的方法。
        //调用真实角色RealSubject的Request()方法
        sub.Request();
        /* 输出：
        Method:public abstract void com.dennyac.HbaseTest.reflect.Proxy.Subject.Request(), Args:null
        RealSubject
         */

    }
}
