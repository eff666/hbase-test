package com.dennyac.HbaseTest.designpattern.abstractfactory;

/**
 * Created by shuyun on 2017/4/24.
 */
public abstract class AbstractFactory {

    abstract Shape getShape(String shapeType);
    abstract Color getColor(String colorType);
}
