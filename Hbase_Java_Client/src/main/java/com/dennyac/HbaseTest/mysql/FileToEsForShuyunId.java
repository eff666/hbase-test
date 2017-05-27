package com.dennyac.HbaseTest.mysql;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by wanghaiwei on 2015/10/23.
 */

public class FileToEsForShuyunId {


    static ConcurrentLinkedQueue<String> queues = new ConcurrentLinkedQueue<String>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static Integer count = 0;

    public static void main(String[] agrs) throws Exception{
        System.out.println("现在时间是:" + Calendar.getInstance().getTime());
        // set cluster name
        Settings settings = Settings.settingsBuilder()
                .put("client.transport.ping_timeout", "20s")
                .put("cluster.name", "elasticsearch-cluster").build();

        final TransportClient client = TransportClient.builder()
                .settings(settings).build();

        // add transport addresses
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.138.183"), 9500));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.145.181"), 9500));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.161.16"), 9500));


        final long aa = System.currentTimeMillis();

        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();
        for(int i = 0; i < 5; i++) {
            new Thread(new Runnable() {

                public void run() {
                    //Add transport addresses and do something with the client...
                    hashMap.put(Thread.currentThread().getName(), Boolean.FALSE);
                    BulkProcessor bulkProcessor = BulkProcessor.builder(
                            client,
                            new BulkProcessor.Listener() {

                                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                                    if(bulkResponse.hasFailures()) System.out.println("fail.11......");
                                }

                                public void beforeBulk(long executionId,
                                                       BulkRequest request) {
                                }

                                public void afterBulk(long executionId,
                                                      BulkRequest request,
                                                      Throwable failure) {
                                    System.out.println("fail..22..........");
                                }
                            })
                            .setBulkActions(5000)
                            .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                            .setBackoffPolicy(
                                    BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                            .setConcurrentRequests(1)
                            .build();
                    while (true) {
                        if (!queues.isEmpty()) {
                            try {
                                String json = queues.poll();
                                if (json == null) continue;
                               // System.out.println(json);
                                String buyNick = "";
                                try {
                                    int index5 = json.indexOf("buyer_nick");
                                    int index6 = json.indexOf(",", index5);
                                    index5 += 12;
                                    buyNick = json.substring(index5 + 1, index6 - 1);
                                } catch (NullPointerException nu){

                                }
                                //System.out.println(id);
                                bulkProcessor.add(new IndexRequest("shuyun_id", "shuyun_id").id(buyNick).source(json));

                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        if (queues.isEmpty() && !isInsert.get()) {
                            bulkProcessor.flush();
                            long jjj = System.currentTimeMillis() - aa;
                            System.out.println("   " + Thread.currentThread().getName() + ":" + jjj + "   ");
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

        System.out.println("主线程for循环执行完毕..");

        File file = new File("/data2/xiuyuan/file/shuyun_id-2016-11-25");
        //File file = new File("F:\\datafortag\\maimaimai\\shuyun_id-2016-11-25");
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String json = null;
        Integer index = 0;
        while((json = bufferedReader.readLine()) != null){
            queues.add(json);
            index++;
            if(index % 20000 == 0){
                int number = queues.size();
                int jj = number/20000;
                System.out.println("index: " + index + ", jj: " + jj + ", number: " + number);
                while(jj > 0){
                    Thread.sleep(1000*jj);
                    int number2 = queues.size();
                    jj = number2/20000;
                    System.out.println("index: " + index + ", jj: " + jj + ", number2: " + number2);
                }
            }
        }
        isInsert = new AtomicBoolean(false);
        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();
        System.out.println("现在时间是:" + Calendar.getInstance().getTime());
        //System.out.println("未加密数据：" + count);
        System.out.println("共有数据：" + index);
    }

}

