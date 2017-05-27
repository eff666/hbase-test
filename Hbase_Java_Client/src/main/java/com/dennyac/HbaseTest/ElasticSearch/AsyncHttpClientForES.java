package com.dennyac.HbaseTest.ElasticSearch;


import com.ning.http.client.*;
import org.mortbay.util.UrlEncoded;

import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by shuyun on 2016/8/5.
 */
public class AsyncHttpClientForES {

    public static void main(String[] args){

        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setCompressionEnabled(true).setAllowPoolingConnection(true);
        builder.setRequestTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
        builder.setIdleConnectionTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
        AsyncHttpClient client = new AsyncHttpClient(builder.build());
        String id = "~IgOMOo15IUf1NfENGQV Gw==~1~_116476553_1";
        //String url = "http://es2.intraweb.shuyun.com/index_rfm_new/rfm/" + UrlEncoded.encodeString(id);
        String url = "https://shop.m.taobao.com/shop/coupon.htm?sellerId=696902416&activityId=4aee3e6538d94b518e19b53d3b04f30c";
        //String json = "{\"buyer_nick\":\"11我以为我可以\",\"reciev_validate_h\":\"一中学\",\"reciev_validate_l\":\"四川省&宜宾市&长宁县&老翁镇大湾乡黎明村\",\"reciev_validate_c\":\"云南省&昭通市&永善县&溪洛渡镇永善县第一中学\",\"location\":\"112.21,221.11\",\"reciev_validate_h_location\":\"5555\"}";
        //String json = "{\"buyer_nick\":\"08165145lyy\",\"reciev_validate_h\":\"上海&上海市&浦东新区&陆家嘴街道张杨路崂山路口书报亭(东方金融广场门口书报亭\",\"reciev_validate_l\":\"上海&上海市&浦东新区&陆家嘴街道张杨路崂山路口书报亭(东方金融广场门口书报亭\",\"reciev_validate_c\":\"上海&上海市&浦东新区&陆家嘴街道张杨路崂山路口书报亭(东方金融广场门口书报亭\",\"reciev_validate_l_location\":\"121.523219,31.226945\"}";


        try {
           // client.preparePut()
//            Map<String, Collection<String>> map = new LinkedHashMap<String, Collection<String>>();
//            Collection<String> list = new ArrayList<>();
//            list.add("1111");
//            map.put("location", list);
 //          ListenableFuture<Response> future = client.preparePut(url).setHeader("content-type", "application/json").setBody(json.getBytes("UTF-8")).execute();
            ListenableFuture<Response> future = client.prepareGet(url).addHeader("content-type", "application/json").execute();
                    //.addParameter("location", "11,22.22").execute();
            //result
            future.get();
            System.out.println(future.get().getResponseBody());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            client.close();
        }
    }
}
