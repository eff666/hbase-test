package com.dennyac.HbaseTest.ElasticSearch;

import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by shuyun on 2016/8/10.
 */
public class Delete {

    public static void main(String[] args) {
        Settings settings = Settings.settingsBuilder().put("cluster.name", "rube-es").put("client.transport.ping_timeout", "200s").build();
        TransportClient tClient = TransportClient.builder().settings(settings).build();

        try {
            Client client = tClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.18.2.180"), 9300));

            //3、删除index是taobao_v3
           // DeleteIndexResponse deleteIndexResponse = tClient.admin().indices().prepareDelete("taobao_v3").execute().actionGet();
            if("shuyun_id".equals("taobao_v3")){
                // DeleteIndexResponse deleteIndexResponse1 = tClient.admin().indices().prepareDelete("taobao_v3").execute().actionGet();

            }


            //法二：
            ClusterStateResponse response = tClient.admin().cluster().prepareState().execute().actionGet();
            String[] cluster = response.getState().getMetaData().getConcreteAllIndices();//得到所有index
            //response.getState().getNodes().getNodes().values().iterator().next().value.getAddress().toString();
            for (String index : cluster) {
                //删除所有索引
                System.out.println(index+"\t");
            DeleteIndexResponse deleteIndexResponse2 = tClient.admin().indices().prepareDelete(index).execute().actionGet();
            if("shuyun_id".equals(index)){
              //  DeleteIndexResponse deleteIndexResponse1 = tClient.admin().indices().prepareDelete(index).execute().actionGet();

            }
            }



//            DeleteByQueryResponse dqrb = client.prepareDeleteByQuery()
//                    .setTypes("wxt")
//                    .setQuery(QueryBuilders.boolQuery()
//                            .must(QueryBuilders.matchQuery("ID", "000099"))
//                            .must(QueryBuilders.matchQuery("number", 55)))
//                    .execute().actionGet();






        } catch (UnknownHostException e){

        }
    }


    /*
    * 2.0以后的版本，我们如何来进行批量的删除呢？
        我们可以先通过Search API查询，然后得到需要删除的批量数据的id，然后再通过id来删除。
    *
    * */
    public void deleteByTerm(Client client){
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        SearchResponse response = client.prepareSearch("megacorp").setTypes("employee")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("first_name", "xiaoming"))
                .setFrom(0).setSize(20).setExplain(true).execute().actionGet();

        for(SearchHit hit : response.getHits()){
            String id = hit.getId();
            bulkRequest.add(client.prepareDelete("megacorp", "employee", id).request());
        }

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            for(BulkItemResponse item : bulkResponse.getItems()){
                System.out.println(item.getFailureMessage());
            }
        }else {
            System.out.println("delete ok");
        }

    }
}
