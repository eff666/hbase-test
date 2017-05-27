package com.dennyac.HbaseTest.designpattern.abstractfactory;

/**
 * Created by shuyun on 2017/4/24.
 */
public class Blue implements Color {
    @Override
    public void fill(){
        System.out.println("Blue::fill() method");
    }
}
