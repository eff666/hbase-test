//package com.dennyac.HbaseTest.util;
//
//
//import java.util.regex.Pattern;
//
///**
// * Created by shuyun on 2016/8/1.
// *
// * 根据两个位置的经纬度，来计算两地的距离（单位为KM）
// * 参数为double类型
// *  lat1 用户经度
// *  lng1 用户纬度
// *  lat2 商家经度
// *  lng2 商家纬度
// *
// */
//
//public class Distance {
//
//    private static final double EARTH_RADIUS = 6378.137;
//
//    private static double rad(double d){
//        return d * Math.PI / 180.0;
//    }
//
//
//    public static double GetDistance(double long1, double lat1, double long2, double lat2) {
//        double a, b, d, sa2, sb2;
//        lat1 = rad(lat1);
//        lat2 = rad(lat2);
//        a = lat1 - lat2;
//        b = rad(long1 - long2);
//
//        sa2 = Math.sin(a / 2.0);
//        sb2 = Math.sin(b / 2.0);
//        d = 2   * EARTH_RADIUS
//                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
//                * Math.cos(lat2) * sb2 * sb2));
//        return d;
//    }
//
//
//    public static void main(String[] args) {
//
//        //1、根据两点间的经纬度计算距离，单位：km
////        System.out.println(GetDistance(116.5542, 39.81621, 116.5539, 39.81616));//0.025公里
////        System.out.println(GetDistance(117.1181, 36.68484, 117.01, 36.66123));//9.993公里
////
////        System.out.println(GetDistance(112.9084, 28.14203, 112.9083, 28.14194));//0.01
////
////        System.out.println(GetDistance(121.5373, 38.86827, 121.5372, 38.86832));//0.007
////
////        System.out.println(GetDistance(20.5, 118.2, 21.1, 117.6));//73.656公里
////
////        System.out.println(GetDistance(121.445140,31.177779, 121.444832,31.179313));//0.173
//
//
////        //2、通过java api从高德地图获取经纬度
////        String url = "http://restapi.amap.com/v3/geocode/geo?address=上海市徐汇区天钥桥路900号&output=JSON&key=dcfc2a8babaf1c8fb0c42111d098606e";
////
////        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
////        builder.setCompressionEnabled(true).setAllowPoolingConnection(true);
////        builder.setRequestTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
////        builder.setIdleConnectionTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
////
////        AsyncHttpClient client = new AsyncHttpClient(builder.build());
////        try {
////            ListenableFuture<Response> future = client.prepareGet(url).execute();
////            String result = future.get().getResponseBody();
////            System.out.println(result);
////            JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(future.get().getResponseBody());
////            if(jsonNode.findValue("status").textValue().equals("1")) {
////                JsonNode listSource = jsonNode.findValue("location");
////                System.out.println(listSource);
////                for(String location : listSource.textValue().split(",")){
////                    System.out.println(location);
////                    //System.out.println(Double.valueOf(location));
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        } finally {
////            if(client != null){
////                client.close();
////            }
////        }
//
//
//        //3、 判断String是否是数字
//
//        String str = "-125，2222.88";
//        System.out.println(isNumeric(str));
//        System.out.println(Pattern.compile("-?\\d+.\\d").matcher(str).find());
//    }
//
//
//    //3.判断字符串是否是数字
//    public static boolean isNumeric(String strs){
//
////        //1、Pattern.split(CharSequence input)
////        //,用于分隔字符串
////        Pattern p = Pattern.compile("\\d+");
////        String[] str = p.split("123:456:678.789");
////        //结果:str[0]="123:" str[1]="456:" str[2]="678.789"
////
////        //2、Pattern.matcher(String regex,CharSequence input)是一个静态方法,用于快速匹配字符串,该方法适合用于只匹配一次,且匹配全部字符串.
////        Pattern.matches("\\d+","2223");//返回true
////        Pattern.matches("\\d+","2223aa");//返回false,需要匹配到所有字符串才能返回true,这里aa不能匹配到
////        Pattern.matches("\\d+","22bb23");//返回false,需要匹配到所有字符串才能返回true,这里bb不能匹配到
//
//
////        //3、Pattern.matches(CharSequence input)、
////        //判断整个输入字符串是否符合匹配正则表达式,
////        //起始处开始匹配
////        Pattern p = Pattern.compile("\\d+");
////        Matcher m = p.matcher("22bb23");
////        m.matches();//返回false,因为bb不能被\d+匹配,导致整个字符串匹配未成功.
////        Matcher m2=p.matcher("2223");
////        m2.matches();//返回true,因为\d+匹配到了整个字符串
//
//
////        //4、Matcher.lookingAt()
////       //判断该字符串（不是整个字符串）的部分是否能够匹配,对前面的字符串进行匹配,只有匹配到的字符串在最前面才返回true
////        //起始处开始匹配
////        Pattern p = Pattern.compile("\\d+");
////        Matcher m = p.matcher("22bb23");
////        m.lookingAt();//返回true,因为\d+匹配到了前面的22
////        Matcher m2=p.matcher("aa2223");
////        m2.lookingAt();//返回false,因为\d+不能匹配前面的aa
//
//
////        //5、Matcher.find()
////        //尝试查找与该模式匹配的输入序列的下一个子序列
////        //对字符串进行匹配,匹配到的字符串可以在任何位置
////        Pattern p=Pattern.compile("\\d+");
////        Matcher m=p.matcher("22bb23");
////        m.find();//返回true
////        Matcher m2=p.matcher("aa2223");
////        m2.find();//返回true
////        Matcher m3=p.matcher("aa2223bb");
////        m3.find();//返回true
////        Matcher m4=p.matcher("aabb");
////        m4.find();//返回false.
//
////        //6、Mathcer.start()/ Matcher.end()/ Matcher.group()
////        // 当使用matches(),lookingAt(),find()执行匹配操作后,就可以利用以上三个方法得到更详细的信息.
////        //     start()返回匹配到的子字符串在字符串中的索引位置.
////        //     end()返回匹配到的子字符串的最后一个字符在字符串中的索引位置.
////        //     group()返回匹配到的子字符串
////        Pattern p = Pattern.compile("\\d+");
////        Matcher m = p.matcher("aaa2223bb");
////        m.find();//匹配2223
////        m.start();//返回3
////        m.end();//返回7,返回的是2223后的索引号
////        m.group();//返回2223
////
////        Matcher m2 = p.matcher("2223bb");
////        m.lookingAt();   //匹配2223
////        m.start();   //返回0,由于lookingAt()只能匹配前面的字符串,所以当使用lookingAt()匹配时,start()方法总是返回0
////        m.end();   //返回4
////        m.group();   //返回2223
////
////        Matcher m3 = p.matcher("2223bb");
////        m.matches();   //匹配整个字符串
////        m.start();   //返回0,原因相信大家也清楚了
////        m.end();   //返回6,原因相信大家也清楚了,因为matches()需要匹配所有字符串
////        m.group();   //返回2223bb
//
////        //7、整合
////        Pattern p=Pattern.compile("([a-z]+)(\\d+)");
////        Matcher m=p.matcher("aaa2223bb");
////        m.find();   //匹配aaa2223
////        m.groupCount();   //返回2,因为有2组
////        m.start(1);   //返回0 返回第一组匹配到的子字符串在字符串中的索引号
////        m.start(2);   //返回3
////        m.end(1);   //返回3 返回第一组匹配到的子字符串的最后一个字符在字符串中的索引位置.
////        m.end(2);   //返回7
////        m.group(1);   //返回aaa,返回第一组匹配到的子字符串
////        m.group(2);   //返回2223,返回第二组匹配到的子字符串
//
//
//
//
//        return true;
//    }
//
//
//}
