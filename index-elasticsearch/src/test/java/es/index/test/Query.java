//package es.index.test;
//
//import java.util.HashMap;
//import java.util.Map;
//import org.elasticsearch.action.bulk.BulkRequestBuilder;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.delete.DeleteResponse;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.rest.RestStatus;
//
///**
// * @Author:chaoqiang.zhou
// * @Date:Create in 下午2:53 2018/4/28
// */
//public class Query {
//  private static TransportClient client;
//
//  private static void open() throws Exception {
//    Map<String, String> map = new HashMap<String, String>();
//    map.put("cluster.name", "elasticsearch_wenbronk");
//    Settings.Builder settings = Settings.builder().put(map);
//  }
//  public void index() {
////    SearchRequestBuilder responsebuilder = client.prepareSearch("index").setTypes("type");
////    SearchResponse myresponse=responsebuilder.setQuery(
////        QueryBuilders.matchPhraseQuery("title", "molong1208 blog"))
////        .setFrom(0).setSize(10).setExplain(true).execute().actionGet();
//
//    //索引名称和type名称以及指定id,classname,classname,指定id，指定json来源
//    IndexResponse indexResponse = client.prepareIndex("", "").setId("").setSource("").get();
//
//    //  Index  name
//    String _index = indexResponse.getIndex();
////  Type  name
//    String _type = indexResponse.getType();
////  Document  ID  (generated  or  not)
//    String _id = indexResponse.getId();
////  Version  (if  it's  the  first  time  you  index  this  document,  you  will  get:  1)
//    long _version = indexResponse.getVersion();
////  isCreated()  is  true  if  the  document  is  a  new  one,  false  if  it  has  been  updated
//    RestStatus created = indexResponse.status();
//  }
//
//
//  public void query() {
//    //根据id进行查询
//    client.prepareGet("", "", "").setOperationThreaded(false).get();
//
//    DeleteResponse response = client.prepareDelete("twitter", "tweet", "1").get();
//
//    client.prepareUpdate("ttl", "doc", "1")
//        .setDoc("")
//        .get();
//
//    BulkRequestBuilder bulkRequest = client.prepareBulk();
//
//    bulkRequest.add(client.prepareIndex("twitter", "tweet", "1")
//        .setSource()
//    );
//
//    //批量插入操作
//    BulkResponse bulkResponse = bulkRequest.get();
//
//    bulkResponse.hasFailures();
//  }
//
//
//  public void get() {
//    SearchResponse response = client.prepareSearch("index1", "index2")
//        .setTypes("type1", "type2")
//        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//        .setQuery(
//            QueryBuilders.termQuery("multi", "test"))                                  //  Query
//        .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))//  Filter
//        .setFrom(0).setSize(60).setExplain(true)
//        .execute()
//        .actionGet();
//  }
//
//}
