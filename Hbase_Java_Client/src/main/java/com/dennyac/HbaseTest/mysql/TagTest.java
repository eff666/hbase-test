package com.dennyac.HbaseTest.mysql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shuyun on 2016/9/13.
 */
public class TagTest {

    static ConcurrentLinkedQueue<String> queues = new ConcurrentLinkedQueue<String>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static TransportClient client = null;
    static Integer count = 1;
    static List<String> listColumn = Arrays.asList("oid", "status", "dp_id", "modified", "created",
            "adjust_fee", "buyer_nick", "cid", "discount_fee", "is_oversold", "item_meal_id", "item_meal_name", "num", "num_iid", "outer_iid",
            "outer_sku_id", "payment", "pic_path", "price", "refund_id", "refund_status", "seller_nick", "sku_id", "sku_properties_name", "tid",
            "title", "total_fee", "type", "pay_time", "row_num", "seller_rate", "buyer_rate", "logistics_company", "invoice_no", "zhengji_status",
            "divide_order_fee", "part_mjz_discount", "checksum");

    static ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

    public static void main(String[] agrs) throws Exception {
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "elasticsearch-cluster").build();
        client = TransportClient.builder().settings(settings).build();

        try {
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.204.250"), 9500));
            //client.addTransportAddress(new InetSocketTransportAddress(InetSocketAddress("http://es2.intraweb.shuyun.com/")));
        } catch (UnknownHostException error) {
            System.out.print(error.getMessage());
        }
        final long aa = System.currentTimeMillis();

        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                Integer num  = 1;
                public void run() {
                    //Add transport addresses and do something with the client...
                    hashMap.put(Thread.currentThread().getName(), Boolean.FALSE);
                    final BulkProcessor bulkProcessor = BulkProcessor.builder(
                            client,
                            new BulkProcessor.Listener() {

                                //批量成功后执行
                                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                                    if (bulkResponse.hasFailures()) {
                                        //System.out.println("数据读取到：" + count);

                                    }
                                }

                                //批量提交之前执行
                                public void beforeBulk(long executionId,
                                                       BulkRequest request) {
                                }

                                //批量失败后执行
                                public void afterBulk(long executionId,
                                                      BulkRequest request,
                                                      Throwable failure) {

                                    System.out.println("happen fail = " + failure.getMessage() + " ,cause = " + failure.getCause());
                                }
                            })
                            .setBulkActions(1000)
                            .setBulkSize(new ByteSizeValue(100, ByteSizeUnit.MB))
                            .setBackoffPolicy(
                                    BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                            .setConcurrentRequests(1)
                            .build();
                    while (true) {
                        if (!queues.isEmpty()) {
                            try {
                                String json = queues.poll();
                                //System.out.println(json);
                                if (json == null) continue;
//                                int index1 = json.indexOf("checksum");
//                                int index2 = json.indexOf("}", index1);
//                                index1 += 10;
//                                String id = json.substring(index1 + 1, index2 - 1);
//
//                                int index3 = json.indexOf("dp_id");
//                                int index4 = json.indexOf(",", index3);
//                                index3 += 7;
//                                String routing = json.substring(index3 + 1, index4 - 1);
//                                count++;

                                bulkProcessor.add(new IndexRequest("index_tag_test", "tag").source(json));
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
                                    Thread.currentThread().sleep(1 * 1000);
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

        System.out.println("主线程for循环执行完毕..");

        // 写入文件的路径
        //for(int j = 29; j <= 78; j++) {
        File file = new File("/test-yuan/rfm/test_timer_rfm/tag/tags.txt");
        //File file = new File("F:\\datafortag\\tags.txt");
        System.out.println("读取文件开始," + file);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        Integer index = 0;
        String json = null;
        while ((json = bufferedReader.readLine()) != null) {
            Map<String, String> map = new LinkedHashMap<>();
            String[] rows = json.split(";");
            map.put("buyer_nick", rows[0]);

            int size = rows.length;
            for (int i = 1; i < size; i++) {
                List<String> list = new ArrayList<>();
                for(String str : rows[i].split(":")){
                    try {
                        String.valueOf(str);
                        list.add(str);
                    } catch (Exception e){

                    }
                }

                if (list != null && list.size() > 1) {
                    map.put(list.get(0), list.get(1));
                }

            }

            if (map != null && map.size() > 0) {
                queues.add(objectMapper.writeValueAsString(map));
                index++;
                //System.out.println(objectMapper.writeValueAsString(map));
            }
            if (index % 10000 == 0) {
                int number = queues.size();
                int jj = number / 10000;
                System.out.println("index: " + index + ", jj: " + jj + ", number: " + number);
                while (jj > 0) {
                    Thread.sleep(1000 * jj);
                    int number2 = queues.size();
                    jj = number2 / 10000;
                    System.out.println("index: " + index + ", jj: " + jj + ", number2: " + number2);
                }
                //index = 0;
            }
        }
        isInsert = new AtomicBoolean(false);
        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();
        System.out.println("共有" + index + "个文件");
        System.out.println("读取文件结束");

        //}

    }
}
