package com.dennyac.HbaseTest.util;

import java.util.Scanner;

/**
 * Created by shuyun on 2016/8/11.
 */
public class ScannerTest {
    public static void main(String[] args) {
//        Scanner scan = new Scanner("222 33333 fff");
//        //next方式接收字符串
//        System.out.println("next方式接收：");
//        // 判断是否还有输入
//        if(scan.hasNext()){
//            String str1 = scan.next();//next()不能得到带有空格的字符串。
//            System.out.println("数据为1：" + str1);//222
//        }


//        if(scan.hasNextLine()){
//            String str2 = scan.nextLine();//nextLine()可以获得空白
//            System.out.println("数据为2："+str2);//222 33333 fff
//        }



        //利用scanner可以判断一个数的类型
        //Scanner scan = new Scanner(System.in);// 从键盘接收数据
        Scanner scan = new Scanner("123");
        int i = 0 ;
        float f = 0.0f ;
        // 判断输入的是否是整数
        if(scan.hasNextInt()){
            i = scan.nextInt() ;
            System.out.println("整数数据：" + i) ;
        }else{
            System.out.println("输入的不是整数！") ;
        }

        // 判断输入的是否是小数
        if(scan.hasNextFloat()){
            f = scan.nextFloat() ;
            System.out.println("小数数据：" + f) ;
        }else{
            System.out.println("输入的不是小数！") ;
        }

        scan.hasNextDouble();
        scan.hasNextBigInteger();
        scan.hasNextBoolean();
    }
}
