package com.dennyac.HbaseTest.mysql;

import com.shuyun.crypt.CryptTools;
import com.shuyun.crypt.taobaosdk.com.taobao.api.security.SecurityClient;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by wanghaiwei on 2015/10/23.
 */

public class FileToEs {

    static ConcurrentLinkedQueue<String> queues = new ConcurrentLinkedQueue<String>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static Integer count = 0;

    public static void main(String[] agrs) throws Exception{
        System.out.println("现在时间是:" + Calendar.getInstance().getTime());
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "rube-es").build();
        final TransportClient client = TransportClient.builder()
                .settings(settings).build();

        try{
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.18.2.180"), 9300));
        }
        catch(UnknownHostException error){
            System.out.print(error.getMessage());
        }
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
                                String period = "";
                                String dp_id = "";
                                String buyNick = "";
                                try {
                                    int index1 = json.indexOf("period");
                                    int index2 = json.indexOf(",", index1);
                                    index1 += 8;
                                    period = json.substring(index1 + 1, index2 - 1);
                                } catch (NullPointerException nu){

                                }

                                try {
                                    int index3 = json.indexOf("dp_id");
                                    int index4 = json.indexOf(",", index3);
                                    index3 += 7;
                                    dp_id = json.substring(index3 + 1, index4 - 1);
                                } catch (NullPointerException nu){

                                }

                                try {
                                    int index5 = json.indexOf("buyer_nick");
                                    int index6 = json.indexOf(",", index5);
                                    index5 += 12;
                                    buyNick = json.substring(index5 + 1, index6 - 1);
                                } catch (NullPointerException nu){

                                }

//                                if(!isEncrypt(buyNick)){
//                                    //System.out.println(json);
//                                    buyNick = encrypt(buyNick);
//                                   // System.out.println(buyNick);
//                                    count++;
//                                }
                                String id = buyNick + "_" + dp_id + "_" + period;
                                //System.out.println(id);
                                bulkProcessor.add(new IndexRequest("index_rfm_new", "rfm").id(id).source(json));

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

        System.out.println("主线程for循环执行完毕..");

        File file = new File("F:\\datafortag\\rfm_reimport\\rfm_new_data.txt");
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

    //加密
    public static String encrypt(String data){
        if(Strings.isNullOrEmpty(data)){
            return data;
        }
        return CryptTools.encrypt(data, TYPE_NICK);
    }

    //是否加密
    public static boolean isEncrypt(String data){
        return CryptTools.isEncrypt(data, TYPE_NICK);
    }
    /** 加密数据类型_昵称 */
    public static final String TYPE_NICK = SecurityClient.NICK;

}

