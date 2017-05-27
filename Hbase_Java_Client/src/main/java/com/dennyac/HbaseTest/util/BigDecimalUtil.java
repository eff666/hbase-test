package com.dennyac.HbaseTest.util;

import com.sun.javafx.collections.MappingChange;
import org.apache.hadoop.hbase.util.Hash;

import java.math.BigDecimal;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * float和double只能用来做科学计算或者是工程计算，在商业计算中我们要用java.math.BigDecimal
 * 如果需要精确计算，要用BigDecimal
 *
 */
public class BigDecimalUtil {

    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 提供精确的加法运算。
     *
     * @param v1  被加数
     * @param v2  加数
     * @return    两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 计算double类型和int类型乘积
     *
     * 若不使用BigDecimal处理，则0.3*3=0.899999999 ， 不等于0.9
     *
     * 如果需要精确计算，原则是使用BigDecimal并且一定要用String来构造
     *
     * @param num1  double类型
     * @param num2  int类型
     * @return
     */
    public static double mul(double num1, int num2) {
        BigDecimal bd = new BigDecimal(Double.toString(num1));
        BigDecimal bd2 = new BigDecimal(num2);
        return bd.multiply(bd2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1  被减数
     * @param v2  减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1  被乘数
     * @param v2  乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1  被除数
     * @param v2  除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1  被除数
     * @param v2  除数
     * @param scale  表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v  需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static void main(String[] args) {

        List<String> list = new LinkedList<>();

        Map<String, String> map = new HashMap<>();
        map = Collections.synchronizedMap(map);

        //不精确的方式，
        double d1 = 0.3;
        int num = 3;
        System.out.println(d1 * num);

        try {
            sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //精确的方式
        double d = 0.3;
        double d2 = mul(d,3);
        System.out.println(String.valueOf(d2));
    }
}
