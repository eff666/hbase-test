package com.dennyac.HbaseTest.mysql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.fieldstats.FieldStats;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shuyun on 2016/8/29.
 */
public class MysqlToEsOrder {

    static ConcurrentLinkedQueue<String> queues = new ConcurrentLinkedQueue<String>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static TransportClient client = null;
    static BufferedWriter bufferedWriter = null;
    static Integer count = 1;


    public static void main(String[] agrs) throws Exception{
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "elasticsearch-cluster").build();
        client = TransportClient.builder().settings(settings).build();

        try{
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.204.250"), 9500));
        }
        catch(UnknownHostException error){
            System.out.print(error.getMessage());
        }
        final long aa = System.currentTimeMillis();

        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();
        for(int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                public void run() {
                    //Add transport addresses and do something with the client...
                    hashMap.put(Thread.currentThread().getName(), Boolean.FALSE);
                    final BulkProcessor bulkProcessor = BulkProcessor.builder(
                            client,
                            new BulkProcessor.Listener() {

                                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                                    if (bulkResponse.hasFailures()) {
                                        try {
                                            bufferedWriter.write("读取数据到:" + count);
                                            bufferedWriter.newLine();
                                            Map<String, String> failedDocuments = new HashMap<String, String>();
                                            for (BulkItemResponse item : bulkResponse.getItems()) {
                                                if (item.isFailed()) {
                                                    failedDocuments.put(item.getResponse().toString(), item.getFailureMessage());
                                                }
                                            }
                                            bufferedWriter.write("{" + failedDocuments + "}");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                public void beforeBulk(long executionId,
                                                       BulkRequest request) {
                                }

                                public void afterBulk(long executionId,
                                                      BulkRequest request,
                                                      Throwable failure) {
                                    //System.out.println("happen fail = " + failure.getMessage() + " ,cause = " + failure.getCause());
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
                                String json = queues.poll();
                                if (json == null) continue;
                                int index1 = json.indexOf("checksum");
                                int index2 = json.indexOf("}", index1);
                                index1 += 10;
                                String id = json.substring(index1 + 1, index2 - 1);

                                int index3 = json.indexOf("dp_id");
                                int index4 = json.indexOf(",", index3);
                                index3 += 7;
                                String routing = json.substring(index3 + 1, index4 - 1);

                                bulkProcessor.add(new IndexRequest("index_order_new", "order")
                                        .id(id).routing(routing).source(json));
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

        // 写入文件的路径
        File file = new File("/test/trade/data/trade_error_data.txt");
        FileOutputStream fileOutputStream = new FileOutputStream((file));
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        bufferedWriter = new BufferedWriter(outputStreamWriter);
        WriteData();
        bufferedWriter.close();
        outputStreamWriter.close();
        fileOutputStream.close();
        System.out.println("数据写入完毕");

    }


    // 写入大数据文件
    public static void WriteData() throws IOException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://jconnyxynbbjh.mysql.rds.aliyuncs.com/odps_results";
            conn = DriverManager.getConnection(url, "odpsresults", "lkejfioqpx334Fk");
            System.out.println("成功连接MySQL驱动程序");
            //select *,(@rowno:=@rowno+1) as row_num from dw_order_tmp,(select (@rowno:=0)) b order by tid, title, price, sku_properties_name, item_meal_name, num
            //select * from dw_order_tmp order by tid,title,price,sku_properties_name,item_meal_name,num
            ps = conn.prepareStatement("select * from dw_order_tmp order by tid,title,price,sku_properties_name,item_meal_name,num",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(Integer.MIN_VALUE);
            rs = ps.executeQuery();
            List<String> listColumn = Arrays.asList("part");
            //List<String> timeColumn = Arrays.asList("modified","created","end_time","consign_time","pay_time");

            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            ObjectMapper  objectMapper = new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                    .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            int rowNum = 1;
            while(rs.next())  {
                Map<String, String> map = new LinkedHashMap<>();
                for(int i = 1; i <= colCount; i++  )   {
                    String name = rsmd.getColumnName(i);
                    if(!listColumn.contains(name)) {
                        String value = rs.getString(i);
                        if(value != null && !"".equals(value.trim()) && value.trim().length() > 0) {
                            map.put(name, value);
                        }
                    }
                }
                map.put("row_num", String.valueOf(rowNum++));
                map.put("checksum", getCheckSum(map));
                count++;
                if(map != null && map.size() > 0){
                    queues.add(objectMapper.writeValueAsString(map));
                }

                if(count % 10000 == 0){
                    rowNum = 1;
                    int number = queues.size();
                    int jj = number/10000;
                    System.out.println("index: " + count + ", jj: " + jj + ", number: " + number);
                    while(jj > 0){
                        try {
                            Thread.sleep(1000*jj);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int number2 = queues.size();
                        jj = number2 / 10000;
                        System.out.println("index2: " + count + ", jj: " + jj + ", number2: " + number2);
                    }
                }
            }
            isInsert = new AtomicBoolean(false);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getCheckSum(Map<String, String> map ){
        String strCheckSum = checkNull(map, "tid") + checkNull(map, "title") + Double.valueOf(checkNull(map, "price")) + checkNull(map, "sku_properties_name") + checkNull(map, "item_meal_name") + checkNull(map, "row_num");
        //String strCheckSum = tid + title + price + skuPropertiesName + itemMealName + rowNum;
        String checkSum = generateMD5Scrent(strCheckSum);

        return checkSum;
    }

    private static String checkNull(Map<String, String> map, String key){
        String str = "";
        try {
             str = map.get(key)  == null ? "" : map.get(key);
        } catch (NullPointerException nu){
            str = "";
        }
        return str;
    }

    private static String generateMD5Scrent(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytesIn = md5.digest(str.getBytes("utf-8"));
            for (byte byteIn : bytesIn) {
                String bt = Integer.toHexString(byteIn & 0xff);
                if (bt.length() == 1)
                    sb.append(0).append(bt);
                else
                    sb.append(bt);
            }
        }catch ( Exception e){
            System.out.println(e.getMessage());
        }
        return sb.toString().toUpperCase();
    }

    public static  String stringToJson(String str) {
        str = str.replaceAll("'å'", "").replaceAll("'é'", "").replaceAll(" ", "").replaceAll("\"", "\"").replaceAll("'", "\'").replaceAll("\\s*", "");
        return str.toString();
    }


}
