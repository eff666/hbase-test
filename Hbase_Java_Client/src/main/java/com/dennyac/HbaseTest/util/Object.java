package com.dennyac.HbaseTest.util;

/**
 * Created by shuyun on 2016/8/22.
 */
public class Object {
    public static void main(String[] args) {
        Point p = new Point();  // 类型擦除
        p.setX(10);
        p.setY(20.8);
        int x = (Integer) p.getX();  // 向下转型
        double y = (Double) p.getY();
        System.out.println("This point is：" + x + ", " + y);
        printPoint(p);

        Point<String, String> p2 = new Point<String, String>();
        p2.setX("东京180度");
        p2.setY("北纬210度");
        printPoint(p2);
    }


////        Double[] str = {12.11,23.25, 6.32};
////        Double max = getMax(str);
////        System.out.println(max);
//    }
//    /*
//    //报错，不能判断出此.doubleValue()方法，修改如下
//    public static <T> T getMax(T array[]){
//        T max = null;
//        for(T element : array){
//            max = element.doubleValue() > max.doubleValue() ? element : max;
//        }
//        return max;
//    }
//    */
//    public static <T extends Number> T getMax(T array[]){
//        T max = null;
//        for(T element : array){
//            max = element.doubleValue() > 0 ? element : max;
//        }
//        return max;
//    }

    //为了避免类型擦除，可以使用通配符(?)
    //通配符(?)可以表示任意的数据类型
    public static void printPoint(Point<?, ?> p){// 使用通配符
        System.out.println("This point is: " + p.getX() + ", " + p.getY());
    }


}

class Point<T1, T2>{
    T1 x;
    T2 y;
    public T1 getX() {
        return x;
    }
    public void setX(T1 x) {
        this.x = x;
    }
    public T2 getY() {
        return y;
    }
    public void setY(T2 y) {
        this.y = y;
    }
}
