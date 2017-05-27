package com.dennyac.HbaseTest.http;

import java.util.Random;

/**
 * Created by david on 16/9/5.
 */
public class SleepUtil {

    /**
     * random sleep with specific seconds.
     *
     * @param nSeconds
     */
    public static void sleepSpecificSeconds(int nSeconds) {
        try {
            Thread.sleep(nSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param baseSeconds 休眠时间基数
     * @param seedSeconds 休眠时间随机数基数
     */
    public static void sleepRandomSecons(int baseSeconds, int seedSeconds) {
        //random sleep between 5 and 15 seconds.
        try {
            int sleepTime = (baseSeconds + new Random().nextInt(seedSeconds)) * 1000;
            System.out.println(":::::::: will sleep for " + sleepTime/1000 + " seconds");

            Thread.sleep(
                    sleepTime
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sleepRandomSecons(5, 10);
    }

}
