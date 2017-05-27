package com.dennyac.HbaseTest.ElasticSearch;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by shuyun on 2016/8/10.
 */
public class Update {
    public static void main(String[] args) {
        Settings settings = Settings.settingsBuilder().put("cluster.name", "rube-es").put("client.transport.ping_timeout", "200s").build();
        TransportClient tClient = TransportClient.builder().settings(settings).build();

        try {
            Client client = tClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.18.2.180"), 9300));

            //法一：
            //根据id更新某一个doc的某一列数据
            client.prepareUpdate("index_address", "address","11月你和我")
                        .setDoc(jsonBuilder().startObject().field("reciev_validate_h_location", "1111.11,333")).execute().actionGet();

            //法二：
            //根据id更新某一个doc
//            String json = "{\"buyer_nick\":\"11我以为我可以\",\"reciev_validate_h\":\"四川省&宜宾市&长宁县&老翁镇大湾乡黎明村#云南省&昭通市&永善县&溪洛渡镇永善县第一中学\",\"reciev_validate_l\":\"四川省&宜宾市&长宁县&老翁镇大湾乡黎明村\",\"reciev_validate_c\":\"云南省&昭通市&永善县&溪洛渡镇永善县第一中学\",\"location\":\"112.21,221.11\"}";
//            int index3 = json.indexOf("buyer_nick");
//            int index4 = json.indexOf(",", index3);
//            index3 += 12;
//            String id = json.substring(index3 + 1 , index4 -1);
//            System.out.println(id);
//            client.prepareIndex("index_address", "address")
//                    //.setRouting(routing)
//                    .setId(id)
//                    .setSource(json)
//                    .execute()
//                    .actionGet();
//            i++;
//        }

        } catch (UnknownHostException e){

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    //法三：
    public static void method2(String[] args) {

        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setCompressionEnabled(true).setAllowPoolingConnection(true);
        builder.setRequestTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
        builder.setIdleConnectionTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
        AsyncHttpClient client = new AsyncHttpClient(builder.build());

        String url = "http://172.18.2.180:9200/index_address/address/0807g17";

        //String json = "{\"buyer_nick\":\"11我以为我可以\",\"reciev_validate_h\":\"一中学\",\"reciev_validate_l\":\"四川省&宜宾市&长宁县&老翁镇大湾乡黎明村\",\"reciev_validate_c\":\"云南省&昭通市&永善县&溪洛渡镇永善县第一中学\",\"location\":\"112.21,221.11\",\"reciev_validate_h_location\":\"5555\"}";
        String json = "{\"buyer_nick\":\"08165145lyy\",\"reciev_validate_h\":\"上海&上海市&浦东新区&陆家嘴街道张杨路崂山路口书报亭(东方金融广场门口书报亭\",\"reciev_validate_l\":\"上海&上海市&浦东新区&陆家嘴街道张杨路崂山路口书报亭(东方金融广场门口书报亭\",\"reciev_validate_c\":\"上海&上海市&浦东新区&陆家嘴街道张杨路崂山路口书报亭(东方金融广场门口书报亭\",\"reciev_validate_l_location\":\"121.523219,31.226945\"}";


        try {
            //根据id更新doc的数据
            ListenableFuture<Response> future = client.preparePost(url).setHeader("content-type", "application/json").setBody(json.getBytes("UTF-8")).execute();

            future.get();
            System.out.println(future.get().getResponseBody());

            if(future.get().getStatusCode() != 200){
                //logger.warn("update the buyer_nick is " + listSource.get("buyer_nick").textValue() + "occur error.");
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            client.close();
        }
    }

    /**
     * 更新副本个数
     */
    public static void  updateIndex(String[] argv) throws Exception{
        Client client = null;
        Settings settings =  Settings.settingsBuilder()
                //可以更新的配置还有很多，见elasticsearch官网
                .put("number_of_replicas", 2).build();
        //首先创建索引库
        UpdateSettingsResponse updateSettingsResponse = client.admin().indices()
                .prepareUpdateSettings("testindex").setSettings(settings).execute().actionGet();
        System.out.println(updateSettingsResponse);
    }
}
