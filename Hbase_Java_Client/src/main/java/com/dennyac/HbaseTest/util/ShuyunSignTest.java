package com.dennyac.HbaseTest.util;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by shuyun on 2016/8/2.
 */
public class ShuyunSignTest {

    public static String generateSign(String callerService, String contextPath, String version, String timestamp, String serviceSecret, String requestPath) {
        String sign = "";
        if (callerService == null  || contextPath == null
                || timestamp == null || serviceSecret == null) {
            return sign;
        }
        Map<String, String> map = new LinkedHashMap<>();
        map.put("callerService", callerService);
        map.put("contextPath", contextPath);
        try {
        if (requestPath != null) {
            StringBuilder sb = new StringBuilder();
            for(String part : requestPath.split("/")) {
                sb.append("/").append(URLEncoder.encode(part,"utf-8"));
            }
            map.put("requestPath", sb.toString().substring(1));
        }
        map.put("timestamp", timestamp);
        map.put("v", version);

            sign = generateMD5Sign(serviceSecret, map);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        return sign;
    }

    private static String generateMD5Sign(String secret, Map<String, String> parameters) throws NoSuchAlgorithmException,       UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(generateConcatSign(secret, parameters).getBytes("utf-8"));
        return byteToHex(bytes);
    }

    private static String generateConcatSign(String secret, Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder().append(secret);
        Set<String> keys = parameters.keySet();
        for (String key : keys) {
            sb.append(key).append(parameters.get(key));
        }
        return sb.append(secret).toString();
    }

    private static String byteToHex(byte[] bytesIn) {
        StringBuilder sb = new StringBuilder();
        for (byte byteIn : bytesIn) {
            String bt = Integer.toHexString(byteIn & 0xff);
            if (bt.length() == 1)
                sb.append(0).append(bt);
            else
                sb.append(bt);
        }
        return sb.toString().toUpperCase();
    }

    public static void main(String[] args){
        //注意，日期必须使用如下格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String time = sdf.format(cal.getTime()).toString();
        String sign = generateSign("shuyun-searchapi", "shuyun-searchapi", "1.0" ,time, "73687QcHhjxskUzwgCXmL", "/address");

        //System.out.println(time);
        //System.out.println(sign);

        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setCompressionEnabled(true).setAllowPoolingConnection(true);
        builder.setRequestTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
        builder.setIdleConnectionTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
        AsyncHttpClient client = new AsyncHttpClient(builder.build());

        String url = "http://uld.api.shuyun.com/shuyun-searchapi/1.0/address?address=%7B%22buyer_nick%22:%2208165145lyy%22,%22address_data%22:%22121.445140,31.177779%22,%22address_type%22:%5B%22reciev_validate_l%22,%22reciev_validate_c%22,%22reciev_validate_h%22%5D,%22context%22:%22shuzun%22%7D";
        try {
            ListenableFuture<Response> future = client.prepareGet(url)
                    .addHeader("x-caller-service", "shuyun-searchapi")
                    .addHeader("x-caller-timestamp", time)
                    .addHeader("x-caller-sign", sign)
                    .addHeader("cache-control", "no-cache")
                    .execute();
            //result
            System.out.println(future.get().getResponseBody());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            client.close();
        }
    }
}
