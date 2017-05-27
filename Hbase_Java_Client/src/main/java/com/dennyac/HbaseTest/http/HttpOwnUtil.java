package com.dennyac.HbaseTest.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by david on 16/9/6.
 */
public class HttpOwnUtil {

    /**
     * 获取淘宝短链首次302跳转地址
     * @param url the request url
     * @return
     */
    public static String sendGetRequest(String url) {
        String respURL = null;
        HttpURLConnection conn = null;

        try {
            URL serverUrl = new URL(url);
            conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");

            // 必须设置false，否则会自动redirect到Location的地址
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            respURL = conn.getResponseMessage();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnResoure(conn);
        }

        return respURL;
    }

    /**
     * 根据tu=后地址进行最终商品地址的跳转, 跳转时必须加上Referer属性(用于指定父url来源)
     *
     * @param url the request url
     * @return
     */
    public static String sendGetRequestWithBody(String url) {
        String respURL = null;
        HttpURLConnection conn = null;

        try {
            String urlAfterDecode = URLDecoder.decode(url, "UTF-8");//.split("tu=")[1];
            URL serverUrl = new URL(urlAfterDecode);
            conn = (HttpURLConnection) serverUrl
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Referer", url);

            // 必须设置false，否则会自动redirect到Location的地址
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            respURL = conn.getHeaderField("Location");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConnResoure(conn);
        }

        return respURL;
    }

    /**
     * Release connection resource.
     *
     * @param conn
     */
    public static void releaseConnResoure(HttpURLConnection conn) {
        if (conn != null) {
            conn.disconnect();
        }
    }

    public static void main(String[] args) {
//        String url = "http://www.juubao.com/jump-index-id-29208.html";
//        System.out.println(sendGetRequestWithBody(url));

        String url = "https://detail.tmall.com/item.htm?id=520945622472";

        System.out.println(sendGetRequest(url));
    }

}
