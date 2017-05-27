package com.dennyac.HbaseTest.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shuyun on 2016/7/25.
 */
public class ClassPath {

    public static void main(String[] args){
        //System.setProperty("hadoop.home.dir", System.getProperty("bin"));

        System.out.println(ClassLoader.getSystemResource("hadoop-common-220-bin-master"));

        System.out.println(ClassLoader.getSystemResourceAsStream("hadoop-common-220-bin-master"));

        System.out.println(ClassLoader.class.getClassLoader().getSystemResource("hadoop-common-220-bin-master"));


        //1、利用System.getProperty()函数获取当前路径：
        //System.out.println(System.setProperty("user.dir", "c:users"));
        System.out.println(System.getProperty("users.dir"));//user.dir指定了当前的路径

        //2、使用File提供的函数获取当前路径：
        File directory = new File("");//设定为当前文件夹
        try{
            System.out.println(directory.getCanonicalPath());//获取标准的路径
            System.out.println(directory.getAbsolutePath());//获取绝对路径
        }catch(Exception e){
            System.out.println("12");
        }

        //3、在类中取得路径：类的绝对路径
        System.out.println(Class.class.getClass().getResource("/").getPath());

        System.out.println(Thread.currentThread().getContextClassLoader().getResource("./").getPath());
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());//ClassPath的绝对URI路径
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(".").getPath());//项目的绝对路径





    }
}
