package com.dennyac.HbaseTest.designpattern.abstractfactory;

/**
 * Created by shuyun on 2017/4/24.
 */
public class Green implements Color {

    @Override
    public void fill(){
        System.out.println("Green::fill() method");
    }
}
