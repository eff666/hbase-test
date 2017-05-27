package com.dennyac.HbaseTest.designpattern.abstractfactory;

/**
 * Created by shuyun on 2017/4/24.
 */
public class AbstractFactoryPatternDemo {

    public static void main(String[] args){

        // 1、获取形状工厂
        AbstractFactory factory = FactoryProducer.getFactory("SHAPE");
        //获取形状为Circle的对象
        Shape shape1 = factory.getShape("CIRCLE");
        //调用Circle的draw方法
        shape1.draw();


        //获取形状为Rectangle的对象
        Shape shape2 = factory.getShape("RECTANGLE");
        //调用Rectangle的draw方法
        shape2.draw();


        // 2、获取颜色工厂
        AbstractFactory colorFactory = FactoryProducer.getFactory("COLOR");
        //获取颜色为Red的对象
        Color color1 = colorFactory.getColor("RED");
        //调用Red的fill方法
        color1.fill();

        //获取颜色为Blue的对象
        Color color3 = colorFactory.getColor("BLUE");
        //调用Blue的fill方法
        color3.fill();

    }
}
