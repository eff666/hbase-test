package com.dennyac.HbaseTest.ElasticSearch;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by shuyun on 2016/8/10.
 */
public class Create {

    public static void main(String[] args) {
        Settings settings = Settings.settingsBuilder().put("cluster.name", "rube-es").put("client.transport.ping_timeout", "200s").build();
        TransportClient tClient = TransportClient.builder().settings(settings).build();

        try {
            Client client = tClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.18.2.180"), 9300));

            String index = "";
            String type = "";
            String jsondata = "";

            //法一：
//            IndexResponse requestBuilder = client.prepareIndex(index, type).execute().actionGet();
//            System.out.print(requestBuilder);


           //法二：
            IndexResponse response = client.prepareIndex(index, type)
                    .setSource(jsondata)
                    .execute()
                    .actionGet();



            //利用java api创建索引为index_name，创建索引类型为index_type指定这个mapping的方法如下
            //省略读取mapping文件的java代码，内容保存在mapping_json中。
//            Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
//            client.admin().indices().prepareCreate("index_name").execute().actionGet();
//            client.admin().indices().preparePutMapping("index_name").setType("index_type").setSource(mapping_json).execute().act


        } catch (UnknownHostException e){

        }
    }
}
