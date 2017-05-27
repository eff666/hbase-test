package com.dennyac.HbaseTest.file;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuyun on 2017/3/31.
 */


/**
 * 水果名称注解
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
 @interface FruitName {
    String value() default "";
}

/**
 * 水果颜色注解
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@interface FruitColor {
    /**
     * 颜色枚举
     *
     */
    public enum Color{ YELLOW,RED,GREEN};

    /**
     * 颜色属性
     * @return
     */
    Color fruitColor() default Color.GREEN;

}

class Apple {

    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor = FruitColor.Color.RED)
    private String appleColor;

}


