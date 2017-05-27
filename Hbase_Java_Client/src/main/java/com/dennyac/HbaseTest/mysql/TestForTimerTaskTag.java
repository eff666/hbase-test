package com.dennyac.HbaseTest.mysql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shuyun.crypt.CryptTools;
import com.shuyun.crypt.taobaosdk.com.taobao.api.security.SecurityClient;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shuyun on 2016/10/28.
 */
public class TestForTimerTaskTag extends TimerTask {
    /**
     * 加密数据类型_昵称
     */
    public static final String TYPE_NICK = SecurityClient.NICK;

    static ConcurrentLinkedQueue<Pair<String, String>> queues = new ConcurrentLinkedQueue<Pair<String, String>>();

    static AtomicBoolean isInsert = new AtomicBoolean(true);
    static TransportClient client = null;

    public void run() {

//        while (true) {
//            boolean result = getFlag();
//            System.out.println(Calendar.getInstance().getTime() + ", tags_tags_result返回结果: " + result);
//            if (result) {
//                break;
//            }
//            try {
//                Thread.sleep(1000 * 60 * 60 * 24);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println("tags_tags_result返回结果是ok，现在时间是：" + Calendar.getInstance().getTime());

        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "elasticsearch-cluster").build();
        client = TransportClient.builder().settings(settings).build();

        try{
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.204.205"), 9500));
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.205.32"), 9500));
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.153.205.6"), 9500));
        }
        catch(UnknownHostException error){
            System.out.print(error.getMessage());
        }
        final long aa = System.currentTimeMillis();

        final ConcurrentHashMap<String, Boolean> hashMap = new ConcurrentHashMap();
        for(int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    //Add transport addresses and do something with the client...
                    hashMap.put(Thread.currentThread().getName(), Boolean.FALSE);
                    final BulkProcessor bulkProcessor = BulkProcessor.builder(
                            client,
                            new BulkProcessor.Listener() {

                                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
//                                    System.out.println("请求数量是:" + bulkRequest.numberOfActions());
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
                                    failure.printStackTrace();
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
                                Pair<String, String> json = queues.poll();
                                if (json == null) continue;
                                bulkProcessor.add(new IndexRequest("index_tag", "tag").id(json.getKey()).source(json.getValue()));
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

        System.out.println("主线程for循环执行完毕-------------------");
        System.out.println("现在时间是:" + Calendar.getInstance().getTime());
        WriteData("tags_tags_result");
        queues = new ConcurrentLinkedQueue<Pair<String, String>>();
        isInsert = new AtomicBoolean(true);
        System.out.println("现在时间是:" + Calendar.getInstance().getTime());
        System.out.println("数据写入完毕");
    }


    public static void WriteData(String tableName){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer count = 0;

        ObjectMapper objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://jconnyxynbbjh.mysql.rds.aliyuncs.com/odps_results";
            conn = DriverManager.getConnection(url, "odpsresults", "lkejfioqpx334Fk");
            System.out.println("写入数据开始，成功连接MySQL-------------------" + tableName);

            String sql = "select * from " + tableName;
            ps = conn.prepareStatement(sql,
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(Integer.MIN_VALUE);
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            while(rs.next())  {
                Map<String, String> map = new LinkedHashMap<>();
                for(int i = 1; i <= colCount; i++  ) {
                    String name = rsmd.getColumnName(i);
                    String value = rs.getString(i);
                    boolean flag = true;
                    try {
                        String.valueOf(value);
                    }catch (NumberFormatException num){
                        flag = false;
                    }
                    if (flag && value != null && !"".equals(value.trim()) && value.trim().length() > 0) {
                        map.put(name, value);
                    }
                }

                if(map != null && map.size() > 0){
                    Pair<String, String> map1 = new Pair<String, String>();
                    String buyerNick = "";
                    boolean falgs = true;
                    try {
                        buyerNick = map.get("buyer_nick");
                    } catch (NullPointerException nu){
                        falgs = false;
                    }

                    if(falgs) {
                        map1.setKey(buyerNick);
                        map1.setValue(objectMapper.writeValueAsString(map));
                        queues.add(map1);
                        count++;
                    }
                }

                if(count % 100000 == 0){
                    int number = queues.size();
                    int jj = number / 100000;
                    System.out.println("index: " + count + ", jj: " + jj + ", number: " + number);
                    while(jj > 0){
                        try {
                            Thread.sleep(1000*jj);
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
        }catch (Exception e) {
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
        System.out.println("-------------------" + tableName + "数据写入完毕,共有数据：" + count);
    }

    static class Pair<K,V> implements Serializable {

        private K key;

        public K getKey() { return key; }

        private V value;

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public V getValue() { return value; }

        //        public Pair( K key, V value) {
//            this.key = key;
//            this.value = value;
//        }
        public Pair( ) {
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

    //加密
    public static String encrypt(String data){
        if(Strings.isNullOrEmpty(data)){
            return data;
        }
        return CryptTools.encrypt(data, TYPE_NICK);
    }

    public boolean getFlag() {
        Connection connn = null;
        PreparedStatement pss = null;
        ResultSet rss = null;
        boolean flag = false;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date date = Calendar.getInstance().getTime();
            //if (date.before(new Date())) {
            date = addDay(date, -1);//前一天
            //}
            String date1 = simpleDateFormat.format(date);

            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://jconnyxynbbjh.mysql.rds.aliyuncs.com/odps_results";
            connn = DriverManager.getConnection(url, "odpsresults", "lkejfioqpx334Fk");

            pss = connn.prepareStatement("select * from tags_tags_result");
            rss = pss.executeQuery();
            Map<String, String> flagMap = new LinkedHashMap<>();
            while (rss.next()) {
                flagMap.put(rss.getString(2), rss.getString(1));
            }
            String flags = "";
            try {
                flags = flagMap.get(date1);
            } catch (NullPointerException nu) {

            }
            if ("1".equals(flags)) {
                flag = true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rss != null) {
                    rss.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (pss != null) {
                    pss.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connn != null) {
                    connn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static void main(String[] ages) {

        Timer timer = new Timer();//定时器
        TestForTimerTaskTag ts = new TestForTimerTaskTag();//定时任务目标

        Calendar calendar = Calendar.getInstance();
       // calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        long period = 1000 * 60 * 60 * 24 * 15;//执行间隔15天

        if (date.before(new Date())) {
            date = addDay(date, 1);
        }
        System.out.println("程序启动时间：" + date);
        timer.scheduleAtFixedRate(ts, date, period);
    }

    // 增加或减少天数
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}


