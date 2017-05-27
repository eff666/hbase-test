package com.dennyac.HbaseTest.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shuyun on 2016/9/14.
 */
public class TimeTask extends TimerTask {
    Calendar cal = Calendar.getInstance();

    public void run() {

//        //每月5号执行
//        if(cal.get(Calendar.DATE) == 5){
//            try {
//                //需要执行的代码
//                System.out.println("123");
//            } catch (Exception e) {
//                //logger.error(e);
//                e.printStackTrace();
//            }
//            System.gc();
//            //......
//        }
//
//        //周日执行
//        if(cal.get(Calendar.DAY_OF_WEEK) == 1){
//            try {
//                //需要执行的代码
//                System.out.println("456");
//            } catch (Exception e) {
//                // logger.error(e);
//                e.printStackTrace();
//            }
//            System.gc();
//        }
//
//        //周日执行
//        if(cal.get(Calendar.SECOND) == 10){
//            try {
//                //需要执行的代码
//                System.out.println("789");
//            } catch (Exception e) {
//                // logger.error(e);
//                e.printStackTrace();
//            }
//            System.gc();
//        }
        //Calendar cal = Calendar.getInstance();

        System.out.println("现在时间是:" + Calendar.getInstance().getTime());
    }

    public static void main (String[] ages){
//        Timer timer = new Timer();//定时器
//        TimeTask ts = new TimeTask();//定时任务目标
//        Date firstTime = new Date();//第一次执行时间
//        long period = 1000 * 6;//执行间隔1天
//
//        timer.scheduleAtFixedRate(ts, firstTime, period);//设置执行参数并且开始定时任务


        BigDecimal b1 = new BigDecimal(Double.toString(0.8));
        BigDecimal b2 = new BigDecimal(7);

        System.out.println(b2.multiply(b2).doubleValue());
    }
}
