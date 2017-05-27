
package com.dennyac.HbaseTest.mysql;

import com.shuyun.crypt.CryptTools;
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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestForCustomer {

    static ConcurrentLinkedQueue<Pair<String, Map>> queues = new ConcurrentLinkedQueue<Pair<String, Map>>();
    static ConcurrentLinkedQueue<SearchHit[]> queuesforconverse = new ConcurrentLinkedQueue<>();
    final static String[] ids = {"customerno"};
    final static Map<String, String> fields = new HashMap<>();
    static{
        fields.put("customerno", "nick");
        fields.put("full_name", "receiver_name");
        fields.put("mobile", "phone");
        Collections.unmodifiableMap(fields);
    }

    static AtomicBoolean isInsert = new AtomicBoolean(true);

    public static void main(String[] agrs) throws Exception{
        Thread.sleep(1000 * 60 * 60 * 7);

        Settings settings = Settings.settingsBuilder()
                .put("client.transport.ping_timeout", "20s")
                .put("cluster.name", "elasticsearch-cluster").build();
        TransportClient client = TransportClient.builder()
                .settings(settings).build();

        final TransportClient client1 = TransportClient.builder()
                .settings(settings).build();

        try{
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.205.4"), 9500));
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.204.205"), 9500));
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.205.32"), 9500));
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.205.6"), 9500));
        }
        catch(UnknownHostException error){

        }

        final long aa = System.currentTimeMillis();

        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();


        for(int i = 0; i < 6; i++) {
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
                            .setBulkActions(10000)
                            .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                            .setBackoffPolicy(
                                    BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                            .setConcurrentRequests(1)
                            .build();
                    while (true) {
                        if (!queues.isEmpty()) {
                            try {
                                Pair<String, Map> searchHit = queues.poll();
                                if (searchHit == null) continue;
                                // router
                                bulkProcessor.add(new IndexRequest("index_customer_new", "customer")
                                        .source(searchHit.getValue()).id(searchHit.getKey()));
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
                                    Thread.currentThread().sleep(10 * 1000);
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

        for(int i = 0; i < 4; i++) {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        SearchHit[] hits = queuesforconverse.poll();
                        if(hits == null){
                            continue;
                        }
                        Pair<String, Map>[] pairs = new Pair[hits.length];
                        int i = 0;
                        for(SearchHit hit : hits){
                            Map retVal = new HashMap<>(hit.getSource());
                            for(String field : fields.keySet()){
                                Object object = retVal.get(field);
                                if(object != null){
                                    if(object instanceof String){
                                        retVal.put(field, CryptTools.encrypt((String) object, fields.get(field)));
                                    }
                                }
                            }
                            String id = hit.getId();
                            if(ids.length > 0){
                                StringBuilder sb = new StringBuilder();
                                for(String s : ids){
                                    sb.append(retVal.get(s));
                                }
                                id = sb.toString();
                            }
                            pairs[i] = new Pair<>(id, retVal);
                            i++;
                        }
                        queues.addAll(Arrays.asList(pairs));
                        if (queuesforconverse.isEmpty() && !isInsert.get()) {
                            System.out.println(Thread.currentThread() + "converse over");
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
                .setSize(1000)
                .setQuery(QueryBuilders.matchAllQuery())
                .execute().actionGet();

        long startTime = System.currentTimeMillis();
        System.out.println(startTime);
        int ii = 0;
        int index = 0;
        long timeforlive = 80000;
        TimeValue timeValue = new TimeValue(timeforlive);
        while (true) {

//            queues.addAll(Arrays.asList(converse(scrollResp.getHits().getHits(), fields, ids)));
            queuesforconverse.add(scrollResp.getHits().getHits());
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(timeValue).execute().actionGet();
            System.out.println(ii + "  " + "use millis" + (startTime - System.currentTimeMillis()));
            index++;
            if(index == 2){
                timeforlive = 80000;
                timeValue = new TimeValue(timeforlive);
                int number = queues.size() + queuesforconverse.size() * 1000 * 9;
                int sleepTime = 0;
                int jj = number/80000;
                while(jj > 0){
                    if(sleepTime > 60 ){
                        timeValue = new TimeValue(timeforlive + 20000);
                        break;
                    }
                    Thread.sleep(1000);
                    sleepTime++;
                    int number2 = queues.size() + queuesforconverse.size() * 1000 * 9;
                    jj = number2/80000;
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

    private static Pair<String, Map>[] converse(SearchHit[] hits, Map<String, String> fields, String[] ids){
        if(hits == null){
            return null;
        }
        Pair<String, Map>[] pairs = new Pair[hits.length];
        int i = 0;
        for(SearchHit hit : hits){
            Map retVal = new HashMap<>(hit.getSource());
            for(String field : fields.keySet()){
                Object object = retVal.get(field);
                if(object != null){
                    if(object instanceof String){
                        retVal.put(field, CryptTools.encrypt((String) object, fields.get(field)));
                    }
                }
            }
            String id = hit.getId();
            if(ids.length > 0){
                StringBuilder sb = new StringBuilder();
                for(String s : ids){
                    sb.append(retVal.get(s));
                }
                id = sb.toString();
            }
            pairs[i] = new Pair<>(id, retVal);
            i++;
        }
        return pairs;
    }

    static class Pair<K,V> implements Serializable {

        private K key;

        public K getKey() { return key; }

        private V value;

        public V getValue() { return value; }

        public Pair( K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public int hashCode() {
            return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof Pair) {
                Pair pair = (Pair) o;
                if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
                if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
                return true;
            }
            return false;
        }
    }
}

