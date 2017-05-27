package com.dennyac.HbaseTest.bigdata;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wanghaiwei on 2015/10/23.
 */
public class WriteToFile {

    static ConcurrentLinkedQueue<SearchHit> queues = new ConcurrentLinkedQueue<SearchHit>();
    static AtomicBoolean isInsert = new AtomicBoolean(true);

    public static void main(String[] agrs) throws Exception{

        /*Settings settings = Settings.settingsBuilder()
                .put("client.transport.sniff", true)
                .put("client.transport.ping_timeout", "20s")
                .put("cluster.name", "elasticsearch.cluster").build();*/



        try{
            Settings settings = Settings.settingsBuilder().put("cluster.name", "rube-es").put("client.transport.ping_timeout", "200s").build();
            TransportClient tClient = TransportClient.builder().settings(settings).build();
            Client client = tClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.18.2.180"), 9300));


            final ConcurrentLinkedQueue<Integer> queues2 = new ConcurrentLinkedQueue<Integer>();
            for(int i = 0; i<10; i++){
                queues2.add(new Integer(i));
            }

            for(int i = 0; i < 1; i++){
                new Thread(new Runnable(){

                    public void run(){
                        try {
                            Thread.currentThread().sleep(1 * 1000);
                        } catch(Exception e){
                            e.printStackTrace(System.out);
                        }
                        File file = new File("/data/ellis/datafortag/" + queues2.poll() + ".txt");
                        FileOutputStream fileOutputStream = null;
                        OutputStreamWriter outputStream = null;
                        BufferedWriter bufferedWriter = null;
                        int i = 0;
                        try{
                            fileOutputStream = new FileOutputStream(file);
                            outputStream = new OutputStreamWriter(fileOutputStream);
                            bufferedWriter = new BufferedWriter(outputStream);
                            while (true){
                                if(!queues.isEmpty()) {
                                    SearchHit searchHit = queues.poll();
                                    if(searchHit == null) continue;
                                    i++;
                                    bufferedWriter.write(searchHit.getSourceAsString());
                                    bufferedWriter.newLine();
                                }
                                if(queues.isEmpty() && !isInsert.get()){
                                    System.out.println(Thread.currentThread().getName() + i);
                                    try{
                                        bufferedWriter.flush();
                                        bufferedWriter.close();
                                        outputStream.flush();
                                        outputStream.close();
                                        fileOutputStream.flush();
                                        fileOutputStream.close();
                                    }
                                    catch (Exception e){
                                        e.printStackTrace(System.out);
                                    }
                                    break;
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace(System.out);
                        }

                    }
                }).start();
            }

            SearchResponse scrollResp = client.prepareSearch()
                    .setSearchType(SearchType.SCAN)
                    .setScroll(new TimeValue(60000))
                    .setIndices("taobao")
                    .setTypes("item")
                    .setSize(100)
                    .setQuery(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("shop_id", "122005892")).should(QueryBuilders.termQuery("shop_id", "59568783")).should(QueryBuilders.termQuery("shop_id", "162363325")).should(QueryBuilders.termQuery("shop_id", "71677914")))
                    .execute().actionGet();

            long startTime = System.currentTimeMillis();
            System.out.println(startTime);
            int ii = 0;
            while (true) {
                queues.addAll(Arrays.asList(scrollResp.getHits().getHits()));
                scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
                System.out.println(ii + "  " + "use millis" + (startTime - System.currentTimeMillis()));
                if (scrollResp.getHits().getHits().length == 0) {
                    isInsert = new AtomicBoolean(false);
                    break;
                }
                ii++;
            }

            client.close();
            System.out.println(System.currentTimeMillis());
            }
        catch(Exception error){
            error.printStackTrace();
        }
    }
}

