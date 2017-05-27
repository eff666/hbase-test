package com.dennyac.HbaseTest.ElasticSearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;
import com.shuyun.crypt.CryptTools;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class WriteToFile {

    public static void main(String[] agrs) throws Exception{
		try{
            AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
            builder.setCompressionEnabled(true).setAllowPoolingConnection(true);
            builder.setRequestTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
            builder.setIdleConnectionTimeoutInMs((int) TimeUnit.MINUTES.toMillis(1));
            AsyncHttpClient client = new AsyncHttpClient(builder.build());

            String url = "http://es1.intraweb.shuyun.com/index_trade_encrypt/trade/_search";

            String json = "{\n" +
                    "  \"from\": 0,\n" +
                    "  \"size\": 10000,\n" +
                    "  \"query\": {\n" +
                    "    \"bool\": {\n" +
                    "      \"must\": [\n" +
                    "        {\n" +
                    "          \"match_all\": {}\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"sort\": [\n" +
                    "    {\n" +
                    "      \"received_payment\": {\n" +
                    "        \"order\": \"desc\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"_source\": {\n" +
                    "    \"includes\": [\n" +
                    "      \"received_payment\",\n" +
                    "      \"total_fee\",\n" +
                    "      \"receiver_mobile\",\n" +
                    "      \"receiver_address\",\n" +
                    "      \"total_fee\",\n" +
                    "      \"seller_nick\"\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";

			File file = new File("F:\\datafortag\\trade.txt");
			FileOutputStream fileOutputStream = null;
			fileOutputStream = new FileOutputStream((file));
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            ListenableFuture<Response> future = client.preparePost(url).setHeader("content-type", "application/json").setBody(json.getBytes("UTF-8")).execute();
            future.get();
            //System.out.println(future.get().getResponseBody());
            JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(future.get().getResponseBody());
            List<JsonNode> listSource = jsonNode.findValues("_source");
            for (JsonNode source : listSource) {
                bufferedWriter.write(source.toString() + "++++++++++++++++++++" + CryptTools.decrypt(source.get("receiver_mobile").textValue(), "phone"));
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

        }
    }
}
