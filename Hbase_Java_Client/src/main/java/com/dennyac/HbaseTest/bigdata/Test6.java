package com.dennyac.HbaseTest.bigdata;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wanghaiwei on 2016/6/17.
 */
public class Test6 {

    static ConcurrentLinkedQueue<SearchHit> queues = new ConcurrentLinkedQueue<SearchHit>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);

    public static void main(String[] agrs) throws Exception{

        Settings settings = Settings.settingsBuilder()
                .put("client.transport.sniff", true)
                .put("client.transport.ping_timeout", "20s")
                .put("cluster.name", "elasticsearch-newversion").build();
        TransportClient client = TransportClient.builder()
                .settings(settings).build();

        final TransportClient client1 = TransportClient.builder()
                .settings(settings).build();

        try{
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.161.16"), 9500));
           // client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.145.181"), 9500));
        }
        catch(UnknownHostException error){

        }

        final long aa = System.currentTimeMillis();

        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();
        for(int i = 0; i < 50; i++) {
            new Thread(new Runnable() {

                public void run() {
                    //Add transport addresses and do something with the client...
                    hashMap.put(Thread.currentThread().getName(), Boolean.FALSE);
                    BulkProcessor bulkProcessor = BulkProcessor.builder(
                            client1,
                            new BulkProcessor.Listener() {

                                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                                    if(bulkResponse.hasFailures()) System.out.print("fail.......");
                                }

                                public void beforeBulk(long executionId,
                                                       BulkRequest request) {
                                }

                                public void afterBulk(long executionId,
                                                      BulkRequest request,
                                                      Throwable failure) {
                                    System.out.print("fail...................");
                                }
                            })
                            .setBulkActions(15000)
                            .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                            .setBackoffPolicy(
                                    BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                            .setConcurrentRequests(3)
                            .build();
                    while (true) {
                        if (!queues.isEmpty()) {
                            try {
                                SearchHit searchHit = queues.poll();
                                if (searchHit == null) continue;
                                bulkProcessor.add(new IndexRequest("index_customer_test", "customer")
                                        .source(searchHit.getSource()));
                            } catch (Exception e) {
                                System.out.print(e.getMessage());
                            }

                        }
                        if (queues.isEmpty() && !isInsert.get()) {
                            bulkProcessor.flush();
                            long jjj = System.currentTimeMillis() - aa;
                            System.out.print("   " + Thread.currentThread().getName() + ":" + jjj + "   ");
                            hashMap.put(Thread.currentThread().getName(), Boolean.TRUE);
                            while(hashMap.values().contains(Boolean.FALSE)){
                                try {
                                    Thread.currentThread().sleep(1 * 1000);
                                } catch(Exception e){
                                    e.printStackTrace(System.out);
                                }
                            }
                            bulkProcessor.close();
                            break;
                        }
                    }
                }
            }).start();
        }

        SearchResponse scrollResp = client.prepareSearch()
                .setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(60000))
                .setIndices("index_customer")
                .setTypes("customer")
                .setSize(10000)
                .setQuery(QueryBuilders.matchAllQuery())
                .execute().actionGet();

        long startTime = System.currentTimeMillis();
        System.out.println(startTime);
        int ii = 0;
        int index = 0;
        while (true) {
            queues.addAll(Arrays.asList(scrollResp.getHits().getHits()));
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
            System.out.println(ii + "  " + "use millis" + (startTime - System.currentTimeMillis()));
            index++;
            if(index == 2){
                int number = queues.size();
                int jj = number/120000;
                while(jj > 0){
                    Thread.sleep(1000*jj);
                    int number2 = queues.size();
                    jj = number2/120000;
                }
                index = 0;
            }
            if (scrollResp.getHits().getHits().length == 0) {
                isInsert = new AtomicBoolean(false);
                break;
            }
            ii++;

        }

        client.close();
        System.out.println(System.currentTimeMillis());
    }
}

