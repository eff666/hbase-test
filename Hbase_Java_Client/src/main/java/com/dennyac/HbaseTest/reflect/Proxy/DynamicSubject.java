package com.dennyac.HbaseTest.reflect.Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by shuyun on 2016/8/16.
 *
 * 1、代理模式：代理模式的作用=为其他对象提供一种代理以控制对这个对象的访问。
   2、代理模式的角色：
     抽象角色：声明真实对象和代理对象的共同接口
     代理角色：代理角色内部包含有真实对象的引用，从而可以操作真实对象。
     真实角色：代理角色所代表的真实对象，是我们最终要引用的对象。
    3、动态代理：
     java.lang.reflect.Proxy
     Proxy 提供用于创建动态代理类和实例的静态方法，它还是由这些方法创建的所有动态代理类的超类
     InvocationHandler
     是代理实例的调用处理程序 实现的接口，每个代理实例都具有一个关联的调用处理程序。对代理实例调用方法时，将对方法调用进行编码并将其指派到它的调用处理程序的 invoke 方法。
    4、
     动态Proxy是这样的一种类:
     它是在运行生成的类，在生成时你必须提供一组Interface给它，然后该class就宣称它实现了这些interface。你可以把该class的实例当作这些interface中的任何一个来用。当然，
        这个Dynamic Proxy其实就是一个Proxy，它不会替你作实质性的工作，在生成它的实例时你必须提供一个handler，由它接管实际的工作。
     在使用动态代理类时，我们必须实现InvocationHandler接口
 *
 */

//代理角色
    //包含真实角色的引用，从而可以操作真实角色
public class DynamicSubject implements InvocationHandler {

    private Object sub;
    public DynamicSubject(Object obj){
        this.sub = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Method:"+ method + ", Args:" + args);
        method.invoke(sub, args);//使用Method.invoke()方法将请求转发给被代理对象
        return null;
    }
}
