package com.dennyac.HbaseTest.ElasticSearch;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mortbay.util.UrlEncoded;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by shuyun on 2016/8/12.
 */
public class HttpClientTest {

    //只可以根据url得到数据
    public static void httpClientTest() throws IOException {
        final Stopwatch stopwatch = Stopwatch.createUnstarted().start();
        String responseMsg = "";

        HttpClient httpClient = new HttpClient();
       // String url = "http://172.18.2.180:9200/index_address/address/_search";
        String id = "~IgOMOo15IUf1NfENGQV+Gw==~1~_116476553_6";
        String url = "http://es2.intraweb.shuyun.com/index_rfm_new/rfm/" + UrlEncoded.encodeString(id);

        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        //3.执行getMethod,调用http接口
        httpClient.executeMethod(getMethod);
        //4.读取内容
        byte[] responseBody = getMethod.getResponseBody();
        //5.处理返回的内容
        responseMsg = new String(responseBody);
        System.out.println(responseMsg);
        System.out.println("query cost 1: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    //只可以根据url得到数据
    public static void httpGet(){
        final Stopwatch stopwatch = Stopwatch.createUnstarted().start();
        try {
            //String url = "http://172.18.2.180:9200/index_address/address/_search";
            String id = "~IgOMOo15IUf1NfENGQV+Gw==~1~_116476553_6";
           // String url = "http://es2.intraweb.shuyun.com/index_rfm_new/rfm/" + UrlEncoded.encodeString(id);
            //String url = "https://uland.taobao.com/coupon/edetail?e=huuWrBr87UIN%2BoQUE6FNzLHOv%2FKoJ4LsM%2FOCBWMCflrRpLchICCDoqC1%2FGlh90mJfFEjLtoHpPCLQAGMzNbHRBpywujSvOp2nUIklpPPqYL9BYNf2%2FonKY57aXDe3JrR2pd4uaXWR8D1g9gleiedVtDvjgqDLA1q&pid=mm_115940806_14998982_61982315&af=1";
            String url = "https://shop.m.taobao.com/shop/coupon.htm?sellerId=696902416&activityId=4aee3e6538d94b518e19b53d3b04f30c";

            // 创建HttpClient对象
            org.apache.http.client.HttpClient client = HttpClients.createDefault();
            // 创建GET请求（在构造器中传入URL字符串即可）
            HttpGet get = new HttpGet(url);
            // 调用HttpClient对象的execute方法获得响应
            HttpResponse response = client.execute(get);
            response.setHeader("content-type", "application/json");

            ///response.setEntity(new StringEntity(query, "utf-8"));
            // 调用HttpResponse对象的getEntity方法得到响应实体
            HttpEntity httpEntity = response.getEntity();
            // 使用EntityUtils工具类得到响应的字符串表示

            String result = EntityUtils.toString(httpEntity,"utf-8");
            System.out.println(result);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("query cost 2:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    //可以根据实际的query得到数据
    public static void httpPost(){
        final Stopwatch stopwatch = Stopwatch.createUnstarted().start();
        try {
            String query = "{\"from\":0,\"size\":10,\"query\":{\"bool\":{\"must\":[{\"match\":{\"buyer_nick\":{\"query\":\"11杨柳絮语言\",\"type\":\"phrase\"}}}]}}}";
            // String url = "http://172.18.2.180:9200/index_address/address/_search";

            String url = "https://shop.m.taobao.com/shop/coupon.htm?sellerId=696902416&activityId=4aee3e6538d94b518e19b53d3b04f30c";

            // 创建HttpClient对象
            org.apache.http.client.HttpClient client = HttpClients.createDefault();

            // 创建POST请求
            HttpPost post = new HttpPost(url);

            // 向POST请求中添加消息实体
            //post.setHeader("content-type", "application/json");
            //post.setEntity(new StringEntity(query, "utf-8"));

            // 得到响应并转化成字符串
            String responseMsg = "";
            HttpResponse response = client.execute(post);
            if(response.getStatusLine().getStatusCode() == 302){
                String locationUrl = response.getLastHeader("Location").getValue();
                System.out.println(locationUrl);
            } else {
                HttpEntity httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity, "UTF-8");
                System.out.println(result);
            }

        }catch (Exception e){

        } finally {

        }
        System.out.println("query cost 3:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    /**
     * 执行一个HTTP GET请求，返回请求响应的HTML
     *
     * @return 返回请求响应的HTML
     */
    public static String doGet() {
        //String url = "https://shop.m.taobao.com/shop/coupon.htm?sellerId=696902416&activityId=4aee3e6538d94b518e19b53d3b04f30c";
        String url="http://hws.m.taobao.com/cache/wdetail/5.0/?id=540459138947";
        String response = null;
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        try {

            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                response = method.getResponseBodyAsString();
            }
        } catch (URIException e) {
            System.out.println("执行HTTP Get请求时，编码查询字符串");
        } catch (IOException e) {
            System.out.println("执行HTTP Get请求" + url + "时，发生异常！");
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public static String sendGet() {
        String result = "";
        BufferedReader in = null;
        try {
            //String urlNameString = url + "?" + param;
            //String urlNameString = "https://shop.m.taobao.com/shop/coupon.htm?sellerId=696902416&activityId=4aee3e6538d94b518e19b53d3b04f30c";
            String urlNameString="http://hws.m.taobao.com/cache/wdetail/5.0/?id=540459138947";
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11");
            connection.setRequestProperty("REFERER", "http://www.baidu.com");
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
             //定义 BufferedReader输入流来读取URL的响应
            System.out.println("message信息:" + connection.getResponseMessage());
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }



    public static void main(String[] args) throws IOException {
        //httpClientTest();
        //httpGet();
        //httpPost();
        //sendGet();

        System.out.println(doGet());
        //System.out.println(sendGet());


//        String html = sendGetForStatus();
//        if(html.contains("该优惠券不存在或者已经过期！")){
//            System.out.println("bu存在");
//        }

        //String quanUrl = "https://uland.taobao.com/coupon/edetail?e=kiGA8uSGQ%2BMN%2BoQUE6FNzLHOv%2FKoJ4LsM%2FOCBWMCflrRpLchICCDoqC1%2FGlh90mJlEdKc6OpCBMIstK5O8VvSx0HgBdG%2FDDL%2F1M%2FBw7Sf%2FdophctbWodnnMmEY5JmKtjGXg8TH1ADyFAERRRAXgpOZFprvLPHRAQ&pid=mm_115940806_14998982_61982315&itemId=36711838016&af=1\n";
        String quanUrl = "https://shop.m.com/shop/coupon.htm?sellerId=696902416&activityId=4aee3e6538d94b518e19b53d3b04f30c";



    }



    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

}
