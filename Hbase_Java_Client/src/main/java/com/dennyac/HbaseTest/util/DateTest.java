package com.dennyac.HbaseTest.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Joiner;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.mortbay.util.UrlEncoded;

import java.io.*;
import java.security.MessageDigest;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shuyun on 2016/7/28.
 */
public class DateTest {

    public static void main(String[] args) throws Exception, FileNotFoundException {

//        String name = "select * from rfm where dp_id in(\"66674010\")";
//        String[] rfm_index_1 = new String[]{"66674010", "73524352", "35715903", "72233068", "148992930"};
//        int dpSize = rfm_index_1.length;
//        for(int size = 0; size < dpSize; size++) {
//            String sql = "select count(*) from rfm where dp_id = \"" + rfm_index_1[size] + "\"";
//            System.out.println(sql);
//
//        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDates = simpleDateFormat.format(new Date());
        Date datessssss = null;
        //try {
            datessssss = simpleDateFormat.parse(strDates);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        long time = datessssss.getTime() / 1000;
        System.out.println(time);

//        String todayString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(todayString);
//        Date date2 = addDay(date1, -4);
//        System.out.println(date1);
//        System.out.println(date2);
//        long today = date1.getTime() / 1000;
//        long yesterday = date2.getTime() / 1000;
//        System.out.println(today);
//        System.out.println(yesterday);





        //将不符合目标要求的日期先转为date型，在转为符合要求的格式
        //将"2016-10-12 00:46:00.55"和"2016-10-12"格式都转为"yyyy-MM-dd HH:mm:ss"格式
            Calendar.getInstance();
        System.out.println(Calendar.getInstance().getTime());
        String dateString = "2016-10-12 00:46:00.55";
        //DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        SimpleDateFormat  outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(outputFormat.format(outputFormat.parse(dateString)));


        String strDate = "2016-10-12";
        java.util.Date dates = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
        System.out.println(dates.toString());
        String value = outputFormat.format(dates);
        System.out.println(value);


        String datess = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Date datesss = new SimpleDateFormat("yyyy-MM-dd").parse(datess);
        System.out.println(datesss.getTime() / 1000);


        System.out.println(0.04 + 0.01);
             System.out.println(1.0100 - 0.111);
              System.out.println(0.11 * 100);
                System.out.println(111.11 / 1000);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1); //早晨6点
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        System.out.println(date);
//        String result = "216-07-20";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//        try {
//
//            System.out.println(sdf.format());
//
//            Date date = sdf.parse(result);
//
//            System.out.println(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


//        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");//设置解析格式
//        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");//设置输出格式
//        Date date = dateFormat1.parse("2005-12-01");
//        System.out.println(dateFormat2.format(date));

//        String DateStr1 = "2011-10-17 10:20:16";
//        String DateStr2 = "2011-10-07 15:50:35";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
//        Date dateTime1 = dateFormat.parse(DateStr1);
//        Date dateTime2 = dateFormat.parse(DateStr2);
//        if(dateTime1.compareTo(dateTime2) < 0){
//            System.out.println("1xiao");
//        }
//        String DateStr1 = "0";
//        String DateStr2 = "2016-08-12 00:00:00";
//        Date dateTime1 = dateFormat.parse(DateStr1);
//        Date dateTime2 = dateFormat.parse(DateStr2);
//        Date dateTime3 = dateFormat.parse("2016-08-11 01:00:00");
//        System.out.println(DateStr1);
//        if(dateTime1.compareTo(dateTime3) > 0 && dateTime2.compareTo(dateTime3) < 0){
//            System.out.println("123");
//        }else if(dateFormat.parse(DateStr2).compareTo(dateTime3) > 0) {
//            System.out.println("3");
//        }
//        System.out.println("现在时间是:" + Calendar.getInstance().getTime());
//
//        List<String> shop_ids = new ArrayList<>();
//        shop_ids.add("123");
//        shop_ids.add("234");
//        shop_ids.add("345");
//        shop_ids.add("567");
//        String url = String.format("%s-123", shop_ids.size() > 0 ? "?routing=" + Joiner.on(",").join(shop_ids) : "");
//        System.out.println(url);
//        System.out.println(shop_ids.size() > 0 ? "?routing=" + Joiner.on(",").join(shop_ids) : "");
//        System.out.println(Joiner.on(",").join(shop_ids));
//        System.out.println(UrlEncoded.encodeString(Joiner.on(",").join(shop_ids)));

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String date1 = null;
//        try {
//            Date date = Calendar.getInstance().getTime();
//            date1 = simpleDateFormat.format(date);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(date1);





//        File file = new File("F:\\datafortag\\rfm_new.txt");
//        FileInputStream fileInputStream = new FileInputStream(file);
//        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        String json = null;
//        int i = 0;
//        while ((json = bufferedReader.readLine()) != null && i < 5) {
//            i++;
//            System.out.println(json);
//            //id
//            int index1 = json.indexOf("period");
//            int index2 = json.indexOf(",", index1);
//            index1 += 8;
//            String period = json.substring(index1 + 1, index2 - 1);
//            System.out.println(period);
//
//            int index3 = json.indexOf("dp_id");
//            int index4 = json.indexOf(",", index3);
//            index3 += 7;
//            String dp_id = json.substring(index3 + 1, index4 - 1);
//            System.out.println(dp_id);
//
//            int index5 = json.indexOf("buyer_nick");
//            int index6 = json.indexOf(",", index5);
//            index5 += 12;
//            String buyNick = json.substring(index5 + 1, index6 - 1);
//            System.out.println(buyNick);
//
//            String id = buyNick + "_" + dp_id + "_" + period;
//            System.out.println(id);
//
//
//        }


//        String tid = "2199685711640782";
//        String title = "[MLDDY034]16秋冬新 高品线 20绒80羊毛 经典西装领直身大衣";
//        double price = 750;
//        String skuPropertiesName = "颜色分类:冷 灰 9月26日发;尺码:L码";
//        String itemMealName = "";
//        String rowNum = "1";
//        //String strCheckSum = tid + title + price + skuPropertiesName + itemMealName + rowNum;
//        Map<String, String> map = new LinkedMap();
//        map.put("tid", "2199685711640782");
//        map.put("title", "[MLDDY034]16秋冬新 高品线 20绒80羊毛 经典西装领直身大衣");
//        map.put("price", "750");
//        map.put("sku_properties_name", "颜色分类:冷 灰 9月26日发;尺码:L码");
//        map.put("itemmealname", "");
//        map.put("row_num", "1");
//        String strCheckSum = checkNull(map, "tid") + checkNull(map, "title") + Double.valueOf(checkNull(map, "price")) + checkNull(map, "sku_properties_name") + checkNull(map, "item_meal_name") + checkNull(map, "row_num");
//
//        String checkSum = generateMD5Scrent(strCheckSum);
//        System.out.println(checkSum);



        String jsons = "{\n" +
                "    \"oid\": \"2199685711640782\",\n" +
                "    \"status\": \"WAIT_SELLER_SEND_GOODS\",\n" +
                "    \"dp_id\": \"35650978," +
                "    \"modified\": \"2016-08-23 09:40:30\",\n" +
                "    \"created\": \"2016-08-23 09:39:47\",\n" +
                "    \"adjust_fee\": 0,\n" +
                "    \"buyer_nick\": \"chongjingzy\",\n" +
                "    \"cid\": \"50013194\",\n" +
                "    \"discount_fee\": 7.35,\n" +
                "    \"is_oversold\": \"false\",\n" +
                "    \"num\": 1,\n" +
                "    \"num_iid\": \"534904993046\",\n" +
                "    \"outer_iid\": \"MLDDY034\",\n" +
                "    \"outer_sku_id\": \"*6925888221450\",\n" +
                "    \"payment\": 727.65,\n" +
                "    \"pic_path\": \"http://img01.taobaocdn.com/bao/uploaded/i1/98246648/TB2yPgktVXXXXbHXXXXXXXXXXXX_!!98246648.jpg\",\n" +
                "    \"price\": 750,\n" +
                "    \"refund_status\": \"NO_REFUND\",\n" +
                "    \"sku_properties_name\": \"颜色分类:冷 灰 9月26日发;尺码:L码\",\n" +
                "    \"tid\": \"2199685711640782\",\n" +
                "    \"title\": \"[MLDDY034]16秋冬新 高品线 20绒80羊毛 经典西装领直身大衣\",\n" +
                "    \"total_fee\": \"742.65\",\n" +
                "    \"type\": \"fixed\",\n" +
                "    \"pay_time\": \"2016-08-23 09:39:56\",\n" +
                "    \"row_num\": \"1\",\n" +
                "    \"seller_rate\": \"false\",\n" +
                "    \"buyer_rate\": \"false\",\n" +
                "    \"part_mjz_discount\": \"15.00\",\n" +
                "    \"checksum\": \"b5d53475aaa539565f1e67cd6f532924" +
                "}";

//            int index1 = json.indexOf("checksum");
//            int index2 = json.indexOf("}", index1);
//            index1 += 10;
//            String id = json.substring(index1 + 1, index2 - 1);
//            System.out.println(id);
//
//            int index3 = json.indexOf("dp_id");
//            int index4 = json.indexOf(",", index3);
//            index3 += 7;
//            String routing = json.substring(index3 + 1, index4 - 1);
//            System.out.println(routing);//35650978







    }

    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    private static String checkNull(Map<String, String> map, String key){
        String str = "";
        try {
            str = map.get(key)  == null ? "" : map.get(key);
        } catch (NullPointerException nu){
            nu.printStackTrace();
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
        return sb.toString();
    }


    public static  String string2Json(Map<String, String> map) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(map);
        return json;
    }

}
