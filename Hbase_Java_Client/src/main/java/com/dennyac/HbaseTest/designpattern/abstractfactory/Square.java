package com.dennyac.HbaseTest.designpattern.abstractfactory;

/**
 * Created by shuyun on 2017/4/24.
 */
public class Square implements Shape {

    @Override
    public void draw(){
        System.out.println("Square::draw() method");
    }
}
