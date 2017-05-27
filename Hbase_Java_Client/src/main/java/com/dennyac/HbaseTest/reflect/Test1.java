package com.dennyac.HbaseTest.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by shuyun on 2016/8/15.
 */
public class Test1 {
    public static void main(String[] agrs) throws Exception{

        Class<?> classType = ExtendType.class;

        //1、通过一个对象获得完整的包名
        //System.out.println(classType.getName());//com.dennyac.HbaseTest.reflect.ExtendType

        //2、实例化Class类对象的三种方法
//        Class<?> class1 = null;
//        Class<?> class2 = null;
//        Class<?> class3 = null;
//        // 运用static method Class.forName()
//        class1 = Class.forName("com.dennyac.HbaseTest.reflect.ExtendType");
//        //调用getClass
//        class2 = new ExtendType().getClass();
//        //运用.class 语法
//        class3 = ExtendType.class;
//        System.out.println("类名称   " + class1.getName());//com.dennyac.HbaseTest.reflect.ExtendType
//        System.out.println("类名称   " + class2.getName());//com.dennyac.HbaseTest.reflect.ExtendType
//        System.out.println("类名称   " + class3.getName());//com.dennyac.HbaseTest.reflect.ExtendType


        //3、获取一个对象的父类与实现的接口
//        Class<?> clazz = Class.forName("com.dennyac.HbaseTest.reflect.ExtendType");
//        // 取得父类
//        Class<?> parentClass = clazz.getSuperclass();
//        System.out.println("clazz的父类为：" + parentClass.getName());//clazz的父类为：com.dennyac.HbaseTest.reflect.Type
//
//        // 获取所有的接口
//        Class<?> intes[] = clazz.getInterfaces();
//        System.out.println("clazz实现的接口有：");
//        for (int i = 0; i < intes.length; i++) {
//            System.out.println((i + 1) + "：" + intes[i].getName());
//        }


        //4、获取某个类中的全部构造函数
        // 4.1 使用getConstructors获取构造器，取得全部的构造函数
//        Constructor<?>[] constructors = classType.getConstructors();
//        for (Constructor<?> m : constructors) {
//            System.out.println(m);
//        }
//        // 4.2 查看每个构造方法需要的参数
//        for (int i = 0; i < constructors.length; i++) {
//
//            Class<?> clazzs[] = constructors[i].getParameterTypes();
//            System.out.print("cons[" + i + "] (");
//            for (int j = 0; j < clazzs.length; j++) {
//                if (j == clazzs.length - 1)
//                    System.out.print(clazzs[j].getName());
//                else
//                    System.out.print(clazzs[j].getName() + ",");
//            }
//            System.out.println(")");
//        }
//
//        // 4.3 使用getDeclaredConstructors获取构造器
//        constructors = classType.getDeclaredConstructors();
//        for (Constructor<?> m : constructors) {
//            System.out.println(m);
//        }





        //5、获取某个类的全部属性
//        System.out.println("==========实现的接口或者父类的属性==========");
//        // 5.1 使用getFields获取属性
//        Field[] fields = classType.getFields();
//        for (Field f : fields) {
//            System.out.println(f);
//        }
//
//        System.out.println("===============本类属性===============");
//        // 5.2 使用getDeclaredFields获取属性
//        fields = classType.getDeclaredFields();
//        for (Field f : fields) {
//            System.out.println(f);
//        }
//
//        System.out.println("===============实现的接口或者父类的属性的权限修饰符===============");
//        // 5.3 获取某个类的属性的权限修饰符
//        Field[] filed1 = classType.getFields();
//        for (int j = 0; j < filed1.length; j++) {
//            // 权限修饰符
//            int mo = filed1[j].getModifiers();
//            String priv = Modifier.toString(mo);
//            // 属性类型
//            Class<?> type = filed1[j].getType();
//            System.out.println(priv + " " + type.getName() + " " + filed1[j].getName() + ";");
//        }
        /*
        可见getFields和getDeclaredFields区别：
        getFields返回的是申明为public的属性，包括父类中定义，
        getDeclaredFields返回的是指定类定义的所有定义的属性，不包括父类的。
        */


        //6、 获取某个类的全部方法
        // 6.1 使用getMethods获取方法
//        Method[] methods = classType.getMethods();
//        System.out.println("getMethods()");
//        for (Method m : methods) {
//            System.out.println(m);
//        }
//
//        System.out.println();
//
//        // 6.2 使用getDeclaredMethods获取方法
//        methods = classType.getDeclaredMethods();
//        System.out.println("getDeclaredMethods()");
//        for (Method m : methods) {
//            System.out.println(m);
//        }

    }
}
