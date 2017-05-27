package com.dennyac.HbaseTest.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 淘宝客短链 / 商品数字ID转换工具类
 * (Tbk shortlink / num_iid convertor)
 */
public class ShorturlNumiidConvertUtil {

    public static String getNumIId(String shortLink) {
        String num_iid = null;

        System.out.println("访问地址:" + shortLink);
        try {
            /**
             * 第一次跳转(短链 => 获取点击链接)
             */
            //Each thread sleep randomly between {ONE_PARAM} and {TWO_PARAM} seconds.
            SleepUtil.sleepRandomSecons(1,1);

            String locaOne = HttpOwnUtil.sendGetRequest(shortLink);
            if(locaOne == null || locaOne.length() == 0)
                throw new RuntimeException("ERROR :::::::: 302第1次跳转地址未获取到响应地址.");
            System.out.println("302第1次跳转地址:" + locaOne);

            /**
             * 第二次跳转(点击链接 => 转换成js跳转链接)
             */
            //Each thread sleep randomly between {ONE_PARAM} and {TWO_PARAM} seconds.
            SleepUtil.sleepRandomSecons(1,1);
            String locaTwo = HttpOwnUtil.sendGetRequest(locaOne);
            if(locaTwo == null || locaTwo.length() == 0)
                throw new RuntimeException("ERROR :::::::: 302第2次跳转地址未获取到响应地址.");
            System.out.println("302第2次跳转地址:" + locaTwo);

            /**
             * 第三次跳转(js跳转链接 => 商品详情页)
             */
            //Each thread sleep randomly between {ONE_PARAM} and {TWO_PARAM} seconds.
            SleepUtil.sleepRandomSecons(1,1);
            String locaThree = HttpOwnUtil.sendGetRequestWithBody(locaTwo);
            System.out.println("302第3次跳转地址:" + locaThree);

            //Each thread sleep randomly between {ONE_PARAM} and {TWO_PARAM} seconds.
            SleepUtil.sleepRandomSecons(1,1);

            String regex = "id=\\d+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(locaThree);

            int cnt = 0;
            while (matcher.find() && cnt++ == 0) {
                num_iid = matcher.group().substring(3);
            }

            SleepUtil.sleepRandomSecons(1,1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return num_iid;

    }

    public static void main(String[] args) {
//        String shortLink = "http://s.click.taobao.com/Qpv0xTx";
        String shortLink = "https://detail.tmall.com/item.htm?id=520945622472";

        System.out.println("访问地址:" + shortLink);
        URL serverUrl = null;
        try {
            /**
             * 第一次跳转(短链 => 获取点击链接)
             */
            serverUrl = new URL(shortLink);
            HttpURLConnection conn1Jump = (HttpURLConnection) serverUrl
                    .openConnection();
            conn1Jump.setRequestMethod("GET");

            // 必须设置false，否则会自动redirect到Location的地址
            conn1Jump.setInstanceFollowRedirects(false);
            conn1Jump.connect();
            //response-header field,Location用于302重定向地址
            String location = conn1Jump.getHeaderField("Location");

            System.out.println("302第一次跳转地址:" + location);

            /**
             * 第二次跳转(点击链接 => 转换成js跳转链接)
             */
            serverUrl = new URL(location);
            HttpURLConnection conn2Jump = (HttpURLConnection) serverUrl
                    .openConnection();
            conn2Jump.setRequestMethod("GET");

            // 必须设置false，否则会自动redirect到Location的地址
            conn2Jump.setInstanceFollowRedirects(false);
            conn2Jump.connect();
            String locaTwo = conn2Jump.getHeaderField("Location");

            System.out.println("302第2次跳转地址:" + locaTwo);

            /**
             * 第三次跳转(js跳转链接 => 商品详情页)
             */
            String urlAfterDecode = URLDecoder.decode(locaTwo, "UTF-8").split("tu=")[1];
            serverUrl = new URL(urlAfterDecode);
            HttpURLConnection conn3Jump = (HttpURLConnection) serverUrl
                    .openConnection();
            conn3Jump.setRequestMethod("GET");
            conn3Jump.addRequestProperty("Referer", locaTwo);

            // 必须设置false，否则会自动redirect到Location的地址
            conn3Jump.setInstanceFollowRedirects(false);
            conn3Jump.connect();
            String locaThree = conn3Jump.getHeaderField("Location");

            System.out.println("302第3次跳转地址:" + locaThree);

            conn3Jump.disconnect();
            conn2Jump.disconnect();
            conn1Jump.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}