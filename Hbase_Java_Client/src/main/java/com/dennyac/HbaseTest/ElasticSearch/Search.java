package com.dennyac.HbaseTest.ElasticSearch;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.*;
import java.net.InetAddress;
/**
 * Created by shuyun on 2016/8/10.
 */
public class Search {
    public static TransportClient tClient;
    public static void main(String[] args) {

        try {
            Settings settings = Settings.settingsBuilder().put("cluster.name", "elasticsearch-cluster").put("client.transport.ping_timeout", "200s").build();
            tClient = TransportClient.builder().settings(settings).build();
            Client client = tClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.204.250"), 9500));
            //Client client = tClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.18.2.180"), 9300));

//            //ES是1.7的版本连接
////            Settings settings = ImmutableSettings.settingsBuilder()
////                    .put("cluster.name", "elasticsearch-cluster").put("client.transport.ping_timeout", "60s").build();
////            Client client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("10.10.138.183", 9400));
//
//
//             String query = "{\n" +
//                     "\"query\": {\n" +
//                     "\"bool\": {\n" +
//                     "\"must\": [\n" +
//                     "{\n" +
//                     "\"prefix\": {\n" +
//                     "\"receiver_district\": \"虹口\"\n" +
//                     "}\n" +
//                     "}\n" +
//                     "],\n" +
//                     "\"must_not\": [ ],\n" +
//                     "\"should\": [ ]\n" +
//                     "}\n" +
//                     "}\n" +
//                     "}";
//            String query = "{\"query\": {\"bool\": {\"must\": [{\"term\": {\"item.shop_id\": \"121944829\"}}]}},\"from\": 0,\"size\": 100}";
//
//            //2、读取es中的数据输出到本地文件
//          File file = new File("F:\\datafortag\\shuyun_id_test_hongkou.txt");
//          FileOutputStream fileOutputStream = null;
//          fileOutputStream = new FileOutputStream((file));
//
//          OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
//           BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
//           SearchResponse searchResponse = client.prepareSearch()
//                    .setSearchType(SearchType.DEFAULT)
//                    //.setQuery(QueryBuilders.matchPhraseQuery("item.shop_id", "121944829"))
//                    .setQuery(query)
//                    .setScroll(new TimeValue(60000))
//                    .setIndices("index_trade")
//                    .setTypes("trade")
//                    .setFrom(0)
//                    .setSize(10000)
//                    .execute()
//                    .actionGet();
//
////System.out.println(searchResponse);
//
//            for (SearchHit searchHit : searchResponse.getHits()) {
//                bufferedWriter.write(searchHit.getSourceAsString());
//                //bufferedWriter.write(searchHit.getSource().toString());
//                bufferedWriter.newLine();
//                //System.out.println(searchHit.getSource());
//            }
//            bufferedWriter.flush();
//            bufferedWriter.close();
//            outputStreamWriter.close();
//            fileOutputStream.close();



//            //1、将本地文件写入到es中
//            //读取本地文件，并将其解析插入到es中
//            //将本地文件写入到es中
//        File file = new File("F:\\datafortag\\shuyun_id.txt");
//        FileInputStream fileInputStream = new FileInputStream(file);
//        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        String json = null;
//        int i = 0;
//        while ((json = bufferedReader.readLine()) != null) {
////            System.out.println(json);
////            //id
////            int index1 = json.indexOf("num_iid");
////            int index2 = json.indexOf(",", index1);
////            index1 += 9;
////            String id = json.substring(index1, index2);
////            System.out.println(id);
////
////            int index3 = json.indexOf("shop_id");
////               int index4 = json.indexOf(",", index1);
////                index3 += 9;
////                String routing = json.substring(index3, index4);
////            System.out.println(routing);
//            client.prepareIndex("shuyun_id_tag_test", "shuyun_id")
//                    //.setRouting("71677914)
//                    //.setId(id)
//                    .setSource(json)
//                    .execute()
//                    .actionGet();
//            i++;
//        }
//        System.out.println(i);
//        bufferedReader.close();
//        inputStreamReader.close();
//        fileInputStream.close();
//        client.close();
//
        }
        catch (Exception e){
            e.printStackTrace();
        }



        //3、 批量插入本地数据到es
//        try {
//            Settings settings = Settings.settingsBuilder().put("cluster.name", "rube-es").put("client.transport.ping_timeout", "200s").build();
//            TransportClient tClient = TransportClient.builder().settings(settings).build();
//            Client client = tClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.18.2.180"), 9300));
//
//            //读取刚才导出的ES数据
//            File file = new File("F:\\datafortag\\121944829.txt");
//            //File file = new File("F:\\datafortag\\test.txt");
//             FileInputStream fileInputStream = new FileInputStream(file);
//             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
//             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String json = null;
//            Integer count = 0;
//            //开启批量插入
//            BulkRequestBuilder bulkRequest = client.prepareBulk();
//            while ((json = bufferedReader.readLine()) != null) {
//                String id = "";
//                String routing = "";
//                try {
//                    //id
//                    int index1 = json.indexOf("num_iid");
//                    int index2 = json.indexOf(",", index1);
//                    index1 += 9;
//                    id = json.substring(index1 + 1, index2 - 1);
//                    //System.out.println(id);
//
//                    int index3 = json.indexOf("dp_id");
//                    int index4 = json.indexOf(",", index3);
//                    index3 += 7;
//                    routing = json.substring(index3 + 1, index4 - 1);
//                    //System.out.println(routing);
//
//
////                    client.prepareIndex("odps_order_test", "dw_order_tmp")
////                            .setId(id)
////                            .setRouting(routing)
////                            .setSource(json)
////                            .execute().actionGet();
//                    bulkRequest.add(client.prepareIndex("index_rfm_new", "rfm").setId(id).setRouting(routing).setSource(json));
//                    //每一千条提交一次
//                    if (count % 100 == 0) {
//                        bulkRequest.execute().actionGet();
//                        System.out.println("提交了：" + count);
//                    }
//                    count++;
//
//                } catch (NullPointerException nu){
//                    System.out.println(nu.getMessage());
//                    System.out.println("data is :" + json);
//                }
//
//            }
//            bulkRequest.execute().actionGet();
//            System.out.println("插入完毕");
//            bufferedReader.close();
//            client.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//
//        }

    }
}
