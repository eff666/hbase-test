package com.dennyac.HbaseTest.util;

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

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class TestTimerTask extends TimerTask{
    /** 加密数据类型_昵称 */
    public static final String TYPE_NICK = SecurityClient.NICK;

    public void run() {

        while (true) {
            boolean result = getFlag();
            if (result) {
                break;
            }
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("rfm数据返回ok，现在时间是：" + Calendar.getInstance().getTime());


    }

    public boolean getFlag(){
        Connection connn = null;
        PreparedStatement pss = null;
        ResultSet rss = null;
        boolean flag = false;
        ObjectMapper objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date1 = null;

            Date date = Calendar.getInstance().getTime();
            date1 = simpleDateFormat.format(date);

            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://jconnyxynbbjh.mysql.rds.aliyuncs.com/odps_results";
            connn = DriverManager.getConnection(url, "odpsresults", "lkejfioqpx334Fk");

            pss = connn.prepareStatement("select * from sync_label_re_result");
            rss = pss.executeQuery();
            Map<String, String> flagMap = new HashMap<>();
            while (rss.next()) {
                flagMap.put(rss.getString(2), rss.getString(1));
            }

            System.out.println(objectMapper.writeValueAsString(flagMap));
            String flags = "";
            try {
                flags = flagMap.get(date1);
            } catch (NullPointerException nu) {

            }
            if("1".equals(flags)){
                flag = true;
            }

        }catch (Exception e) {

        } finally{
            try {
                if(rss != null) {
                    rss.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(pss != null) {
                    pss.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(connn != null) {
                    connn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  flag;
    }

    public static void main (String[] ages){

        Timer timer = new Timer();//定时器
        TestTimerTask ts = new TestTimerTask();//定时任务目标
        //第一次执行定时任务的时间2016-09-15 10:00:00
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 16); //早晨6点
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        long period = 1000 * 60 * 60 * 24;//执行间隔24小时

        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = addDay(date, 1);
        }

        timer.scheduleAtFixedRate(ts, date, period);//设置执行参数并且开始定时任务
    }

    // 增加或减少天数
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}

