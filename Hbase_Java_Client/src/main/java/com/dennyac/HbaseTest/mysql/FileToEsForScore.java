package com.dennyac.HbaseTest.mysql;

import com.shuyun.crypt.CryptTools;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by wanghaiwei on 2016/6/17.
 */

public class FileToEsForScore {

    static ConcurrentLinkedQueue<Pair<String, Map>> queues = new ConcurrentLinkedQueue<>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);
    public static final String BUUYER_NICK = "buyer_nick";
    public static final String DP_ID = "dp_id";
    public static final String SCORE = "score";

    public static void main(String[] agrs) throws Exception {

        Settings settings = Settings.settingsBuilder()
                .put("client.transport.ping_timeout", "20s")
                .put("cluster.name", "elasticsearch-cluster").build();

        final TransportClient client1 = TransportClient.builder()
                .settings(settings).build();

        try {
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.204.205"), 9500));
        } catch (UnknownHostException error) {

        }

        final long aa = System.currentTimeMillis();

        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    //Add transport addresses and do something with the client...
                    hashMap.put(Thread.currentThread().getName(), Boolean.FALSE);
                    BulkProcessor bulkProcessor = BulkProcessor.builder(
                            client1,
                            new BulkProcessor.Listener() {

                                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                                    if (bulkResponse.hasFailures()) System.out.print("fail.......");
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
                                bulkProcessor.add(new IndexRequest("index_score", "score")
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
                            while (hashMap.values().contains(Boolean.FALSE)) {
                                try {
                                    Thread.currentThread().sleep(10 * 1000);
                                } catch (Exception e) {
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

        System.out.println("主线程for循环执行完毕-------------------");

        for(int i = 7 ; i <= 20; i++){

            String name = "";
            if(i < 10){
                name = "0" + i;
            } else {
                name = String.valueOf(i);
            }
            WriteData("part-" + name);
            queues = new ConcurrentLinkedQueue<Pair<String, Map>>();
            isInsert = new AtomicBoolean(true);
            //Thread.sleep(10000);
            System.out.println("现在时间是:" + Calendar.getInstance().getTime());
        }
        isInsert = new AtomicBoolean(false);
    }

    // 写入大数据文件
    public static void WriteData(String tableName) throws Exception {
        Integer count = 0;
        File file = new File("/shuyun/test-xiuyuan/file/" + tableName);
        System.out.println("-------------------开始写入：" + file);
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        if (file.exists()) {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                String[] strs = str.split("\t");
                if (strs.length != 3) {
                    continue;
                }
                String buyerNick = CryptTools.encrypt(strs[1], "nick");
                String key = buyerNick + "_" + strs[0];
                Map map = new HashMap<String, String>();
                map.put(DP_ID, strs[0]);
                map.put(BUUYER_NICK, buyerNick);
                map.put(SCORE, strs[2]);
                if (map != null && map.size() > 0) {
                    queues.add(new Pair(key, map));
                    count++;
                }
                if (count % 100000 == 0) {
                    int number = queues.size();
                    int jj = number / 100000;
                    System.out.println("index: " + count + ", jj: " + jj + ", number: " + number);
                    while (jj > 0) {
                        try {
                            Thread.sleep(1000 * jj);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int number2 = queues.size();
                        jj = number2 / 100000;
                        System.out.println("index2: " + count + ", jj: " + jj + ", number2: " + number2);
                    }
                }
            }
        } else {
            System.out.println("文件不存在" );
        }
        //isInsert = new AtomicBoolean(false);
        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();
        System.out.println("-------------------" + tableName + "数据写入完毕,共有数据：" + count);
    }

    static class Pair<K, V> implements Serializable {

        private K key;

        public K getKey() {
            return key;
        }

        private V value;

        public V getValue() {
            return value;
        }

        public Pair(K key, V value) {
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

