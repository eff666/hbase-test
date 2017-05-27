package com.dennyac.HbaseTest.ElasticSearch;

import com.dennyac.HbaseTest.util.DESCoder;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.collect.HppcMaps;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.*;
import java.net.InetAddress;

/**
 * Created by shuyun on 2016/8/18.
 */

//大数据量的读写操作
public class Test {
    public static void main(String[] args) {
        try {
            Settings settings = Settings.settingsBuilder().put("cluster.name", "rube-es").put("client.transport.ping_timeout", "200s").build();
            TransportClient tClient = TransportClient.builder().settings(settings).build();
            Client client = tClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.18.2.180"), 9300));


            //读取刚才导出的ES数据
            BufferedReader br = new BufferedReader(new FileReader("F:\\datafortag\\71677914.txt"));
            String json = null;
            int count = 0;
            //开启批量插入
            BulkRequestBuilder bulkRequest = client.prepareBulk();

//            BulkProcessor bulkProcessor = BulkProcessor.builder(
//                    client,
//                    new BulkProcessor.Listener() {
//                        @Override
//                        public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
//                            System.out.print("after...................");
//                        }
//                        @Override
//                        public void beforeBulk(long executionId,
//                                               BulkRequest request) {
//                            System.out.print("before...................");
//                        }
//                        @Override
//                        public void afterBulk(long executionId,
//                                              BulkRequest request,
//                                              Throwable failure) {
//                            System.out.print("fail...................");
//                        }
//                    })
//                    .setBulkActions(100)
//                    .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
//                    .setFlushInterval(TimeValue.timeValueSeconds(5))
//                    .setConcurrentRequests(1)
//                    .build();


            SearchResponse searchResponse = client.prepareSearch()
                    .setSearchType(SearchType.DEFAULT)
                    //.setQuery(QueryBuilders.matchPhraseQuery("item.shop_id", "121944829"))
                    .setQuery(QueryBuilders.matchAllQuery())
                    .setScroll(new TimeValue(60000))
                    .setIndices("index_address")
                    .setTypes("address")
                    .setFrom(0)
                    .setSize(100)
                    .execute()
                    .actionGet();

//System.out.println(searchResponse);

            for (SearchHit searchHit : searchResponse.getHits()) {
                Object numid = searchHit.getSource().get("num_iid");
                String coderNumid = coder(numid);

                searchHit.getSourceAsString();
                System.out.println(searchHit.getSource());
            }




            while ((json = br.readLine()) != null) {
                bulkRequest.add(client.prepareIndex("index_test", "item_test").setSource(json));
                int index1 = json.indexOf("num_iid");
                int index2 = json.indexOf(",", index1);
                index1 += 9;
                String id = json.substring(index1, index2);
//                bulkProcessor.add(new IndexRequest("index_test", "item_test")
//                        .routing("71677914").id(id).source(json));
                //每一千条提交一次
                if (count % 10 == 0) {
                    bulkRequest.execute().actionGet();
                    System.out.println("提交了：" + count);
                }
                count++;
            }
            bulkRequest.execute().actionGet();
            System.out.println("插入完毕");
//            bulkProcessor.flush();
//            bulkProcessor.close();
            br.close();
            client.close();

        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }



    }

    private static String coder(Object numid){
        try {
        //生成秘钥
            String key = null;
            key = DESCoder.initKey("shuyun123");

            System.err.println("密钥:\t" + key);

            //加密
            byte[] inputData = numid.toString().getBytes();
            inputData = DESCoder.encrypt(inputData, key);
            System.err.println("加密后:\t" + inputData);

            //解密
            byte[] outputData = DESCoder.decrypt(inputData, key);
            String outputStr = new String(outputData);
            System.err.println("解密后:\t" + outputStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
