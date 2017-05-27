package com.dennyac.HbaseTest.mysql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shuyun.crypt.taobaosdk.com.taobao.api.security.SecurityClient;
import org.elasticsearch.action.bulk.*;
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
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by wanghaiwei on 2016/6/17.
 */

public class TestForMember {
    /**
     * 加密数据类型_昵称
     */
    public static final String TYPE_NICK = SecurityClient.NICK;

    static ConcurrentLinkedQueue<Pair<String, String, String>> queues = new ConcurrentLinkedQueue<Pair<String, String, String>>();

    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static TransportClient client = null;

    static List<String> listDpId = new ArrayList<>();

    static{

        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            File file = new File("/test-xiuyuan/member/member.txt");
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String json = "";
            while((json = bufferedReader.readLine()) != null){
                listDpId.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] agrs) throws Exception{

        Settings settings1 = Settings.settingsBuilder()
                .put("client.transport.ping_timeout", "20s")
                .put("cluster.name", "elasticsearch-cluster-2.0").build();

        final TransportClient client1 = TransportClient.builder()
                .settings(settings1).build();

        try{
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.26.233.151"), 9500));
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.26.234.6"), 9500));
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.26.233.233"), 9500));
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.26.233.243"), 9500));
            client1.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.26.233.237"), 9500));
        }
        catch(UnknownHostException error){

        }

        final long aa = System.currentTimeMillis();

        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();

        for(int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    //Add transport addresses and do something with the client...
                    hashMap.put(Thread.currentThread().getName(), Boolean.FALSE);
                    BulkProcessor bulkProcessor = BulkProcessor.builder(
                            client1,
                            new BulkProcessor.Listener() {

                                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                                    if (bulkResponse.hasFailures()) {
                                        for (BulkItemResponse item : bulkResponse.getItems()) {
                                            if (item.isFailed()) {
                                                System.out.println("失败信息:-------------------" + item.getFailureMessage());
                                            }
                                        }
                                    }
                                }

                                public void beforeBulk(long executionId,
                                                       BulkRequest request) {
                                }

                                public void afterBulk(long executionId,
                                                      BulkRequest request,
                                                      Throwable failure) {
                                    System.out.println("fail-------------------" + failure.getMessage() + " ,cause = " + failure.getCause());
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
                                Pair<String, String, String> json = queues.poll();
                                if (json == null) continue;
                                bulkProcessor.add(new IndexRequest(json.getIndex(), "member").id(json.getKey()).source(json.getValue()));
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


        System.out.println("主线程for循环执行完毕-------------------");

        System.out.println("现在时间是:" + Calendar.getInstance().getTime());
        WriteData();
        System.out.println("数据写入完毕");
        System.out.println("现在时间是:" + Calendar.getInstance().getTime());

    }

    // 写入大数据文件
    public static void WriteData() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer count = 0;
        List<String> columnName = Arrays.asList("close_trade_count", "flag", "relation_source", "city", "trade_count",
                "avg_price", "etl_time", "buyer_id", "item_close_count", "item_num",
                "last_trade_time", "close_trade_amount", "buyer_nick", "province",
                "trade_amount", "grade", "group_ids", "dp_id", "biz_order_id", "status"
                );
        List<String> columnDateName = Arrays.asList("etl_time", "last_trade_time");
        List<String> columnDoubleName = Arrays.asList("avg_price", "close_trade_amount", "trade_amount");
        List<String> columnLongName = Arrays.asList("close_trade_count", "flag", "relation_source", "trade_count","buyer_id", "item_close_count", "item_num", "grade", "biz_order_id");
        List<String> columnStringName = Arrays.asList("city", "buyer_nick", "province", "group_ids", "dp_id", "status");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(new SimpleDateFormat("yyy-MM-dd HH:mm:ss"))
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://jconnyxynbbjh.mysql.rds.aliyuncs.com/odps_results";
            conn = DriverManager.getConnection(url, "odpsresults", "lkejfioqpx334Fk");
            System.out.println("写入数据开始，成功连接MySQL-------------------");

            String sql = "select * from dw_member_odps";
            ps = conn.prepareStatement(sql,
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(Integer.MIN_VALUE);
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            while (rs.next()) {  //while控制行数
                Map<String, String> map = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    String name = rsmd.getColumnName(i);
                    if (columnName.contains(name)) {
                        String value = rs.getString(i);
                        boolean flag = true;
                        if(value != null && !"".equals(value.trim()) && value.trim().length() > 0) {
                            if (columnDateName.contains(name)) {
                                try {
                                    //"2012-03-04 00:46:00" 或者 "2012-03-04 00:46:00.55"
                                    Date dateValue = dateFormat.parse(value);
                                    value = dateFormat.format(dateValue);
                                } catch (Exception e) {
                                    try {
                                        //"2012-03-04"
                                        Date dateValues = new SimpleDateFormat("yyyy-MM-dd").parse(value);
                                        value = dateFormat.format(dateValues);
                                    }catch (Exception ex){
                                        flag = false;
                                    }
                                }
                            } else if (columnDoubleName.contains(name)) {
                                try {
                                    Double.valueOf(value);
                                } catch (NumberFormatException num) {
                                    flag = false;
                                }
                            } else if (columnLongName.contains(name)) {
                                try {
                                    Long.valueOf(value);
                                } catch (NumberFormatException num) {
                                    flag = false;
                                }
                            } else if (columnStringName.contains(name)) {
                                try {
                                    String.valueOf(value);
                                } catch (NumberFormatException num) {
                                    flag = false;
                                }
                            }

                            if (flag) {
                                map.put(name, value);
                            }
                        }
                    }
                }

                if (map != null && map.size() > 0) {
                    Pair<String, String, String> map1 = new Pair<String, String, String>();
                    String buyerNick = "";
                    String dpId = "";
                    try {
                        buyerNick = map.get("buyer_nick");
                    } catch (NullPointerException nu) {
                    }

                    try {
                        dpId = String.valueOf(map.get("dp_id"));
                    } catch (NullPointerException nu) {
                    }

                    String index = "index_member_8";
                    if (listDpId.get(0).contains(dpId)) {
                        index = "index_member_1";
                    } else if (listDpId.get(1).contains(dpId)) {
                        index = "index_member_2";
                    } else if (listDpId.get(2).contains(dpId)) {
                        index = "index_member_3";
                    } else if (listDpId.get(3).contains(dpId)) {
                        index = "index_member_4";
                    } else if (listDpId.get(4).contains(dpId)) {
                        index = "index_member_5";
                    } else if (listDpId.get(5).contains(dpId)) {
                        index = "index_member_6";
                    } else if (listDpId.get(6).contains(dpId)) {
                        index = "index_member_7";
                    } else if (listDpId.get(7).contains(dpId)) {
                        index = "index_member_8";
                    }

                    String id = buyerNick + "_" + dpId;
                    map1.setIndex(index);
                    map1.setKey(id);
                    map1.setValue(objectMapper.writeValueAsString(map));
                    queues.add(map1);
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
            isInsert = new AtomicBoolean(false);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-------------------" + "数据写入完毕,共有数据：" + count);
    }

    static class Pair<I, K, V> implements Serializable {
        private I index;

        private K key;

        public K getKey() {
            return key;
        }

        private V value;

        public V getValue() {
            return value;
        }

        public I getIndex() {
            return index;
        }

        public Pair() {
            this.index = index;
            this.key = key;
            this.value = value;
        }

        public void setIndex(I index) {
            this.index = index;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return index + ":" + key + "=" + value;
        }

        @Override
        public int hashCode() {
            return index.hashCode() + 13 * (key.hashCode() * 13 + (value == null ? 0 : value.hashCode()));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof Pair) {
                Pair pair = (Pair) o;
                if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
                if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
                if (index != null ? !index.equals(pair.index) : pair.index != null) return false;
                return true;
            }
            return false;
        }
    }
}