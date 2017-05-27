package com.dennyac.HbaseTest.ElasticSearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by shuyun on 2016/8/19.
 */
public class Test8 {
    public static void main(String[] args){

        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setCompressionEnabled(true).setAllowPoolingConnection(true);
        builder.setRequestTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
        builder.setIdleConnectionTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
        AsyncHttpClient client = new AsyncHttpClient(builder.build());

        String url = "http://es2.intraweb.shuyun.com/index_item_onsale/item_onsale/_search";

        String json = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"term\": {\n" +
                "            \"shop_id\": \"71677914\"\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"must_not\": [],\n" +
                "      \"should\": []\n" +
                "    }\n" +
                "  },\n" +
                "  \"from\": 0,\n" +
                "  \"size\": 100\n" +
                "}";

        try {


            File file = new File("71677914.txt");
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream((file));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            ListenableFuture<Response> future = client.preparePost(url).setHeader("content-type", "application/json").setBody(json.getBytes("UTF-8")).execute();
            future.get();
            System.out.println(future.get().getResponseBody());
            JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(future.get().getResponseBody());
            List<JsonNode> listSource = jsonNode.findValues("_source");
            for (JsonNode source : listSource) {
                bufferedWriter.write(source.toString());
                //bufferedWriter.write(searchHit.getSource().toString());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStreamWriter.close();
            fileOutputStream.close();

            client.close();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            client.close();
        }
    }
}
