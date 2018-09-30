//package es.index.test;
//
//import com.alibaba.fastjson.JSON;
//import es.index.test.model.User;
//import indexer.IndexerMsger;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.rest.RestStatus;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import sky.index.comm.dto.IndexMsg;
//import sky.index.comm.dto.IndexQuery;
//import sky.index.comm.dto.IndexQuery.COMMAND;
//import sky.index.comm.util.FastJsonUtil;
//import sky.index.comm.util.Mapper;
//import sky.index.comm.util.Mapper.EntityInfo;
//import sky.index.comm.util.Utils;
//import sky.index.comm.util.serializer.TypeRef;
//import sky.index.service.contract.dto.SearchDto.SearchResponse;
//
///**
// * @Author:chaoqiang.zhou
// * @Date:Create in 下午2:44 2018/4/28 https://elasticsearch.cn/article/380
// */
//public class EsTest {
//
//  public final static String HOST = "10.70.209.21";
//
//  public final static int PORT = 9300; //http请求的端口是9200，客户端是9300
//
//  TransportClient client = null;
//
//  @Before
//  public void getConnect() throws UnknownHostException {
//    Settings settings = Settings.builder()
//        .put("cluster.name", "test_es").build();  //设置ES实例的名称
//    client = new PreBuiltTransportClient(settings);  //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
//    client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), 9300));
//
//  }
//
//
//  @After
//  public void closeConnect() {
//    if (null != client) {
//      client.close();
//    }
//  }
//
//
//  @Test
//  public void index() {
//
//    User user = new User();
//    user.setId("2323");
//    user.setPwd("2wer234");
//    user.setUserName("张三");
//    user.setLocalDateTime(LocalDateTime.now());
//
//    System.out.println(Utils.toJson(user));
//    String mapping = Mapper.getEntityMappingJson(user.getClass());
//    IndexMsg<User> indexMsg = IndexerMsger
//        .createMsg(mapping, IndexMsg.INDEXTYPE.CREATE, user, null);
//    String indexMsgJson = FastJsonUtil.bean2Json(indexMsg);
//
//    System.out.println(mapping);
//    System.out.println("--------");
//    System.out.println(indexMsgJson);
//    IndexMsg<String> indexObj = Utils.parseObject(indexMsgJson, new TypeRef<IndexMsg<String>>() {
//    });
//    System.out.println("classname" + indexObj.getClassName());
//    EntityInfo entityInfo = JSON.parseObject(indexObj.getClassName(), EntityInfo.class);
//    System.out.println("obj" + indexObj.getObj());
//    System.out.println("");
////
//
//    Map<String, Object> doc = Utils
//        .parseObject(indexObj.getObj(), new TypeRef<HashMap<String, Object>>() {
//        });
//    IndexResponse indexResponse = client
//        .prepareIndex(entityInfo.getIndex().getIndexName(), entityInfo.getIndex().getIndexType())
//        .setId("4567").setSource(indexObj.getObj()).get();
//    System.out.println(indexResponse.status());
//    long _version = indexResponse.getVersion();
//    //  Index  name
//    String _index = indexResponse.getIndex();
////  Type  name
//    String _type = indexResponse.getType();
////  Document  ID  (generated  or  not)
//    String _id = indexResponse.getId();
//    System.out.println("");
//  }
//
//
//  public void insert() {
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
//  @Test
//  public void queryById() {
//    //根据id进行查询,这种方式必须有id
//    //根据id进行查询,这种方式必须有id
//    GetResponse response = client.prepareGet("user", "user", "id").setOperationThreaded(false)
//        .get();
//    Map<String, Object> result = response.getSourceAsMap();
//    System.out.println("");
//  }
//
//
//  @Test
//  public void query() {
//
////    GetResponse response =  client.prepareGet().setIndex("user").setOperationThreaded(false).get();
////    Map<String, Object> result = response.getSourceAsMap();
////    System.out.println("");
//
//    try {
//      org.elasticsearch.action.search.SearchResponse searchResponse = client.prepareSearch()
//          .execute().actionGet();
//      System.out.println(searchResponse.getHits());
//      System.out.println(searchResponse.getHits().totalHits);
//      SearchHit[] result = searchResponse.getHits().getHits();
//      System.out.println(result[0].getSourceAsMap());
//      System.out.println("");
//    } catch (Exception e) {
//
//    }
//  }
//
//
//  @Test
//  public void queryByCondition() {
//
////    GetResponse response =  client.prepareGet().setIndex("user").setOperationThreaded(false).get();
////    Map<String, Object> result = response.getSourceAsMap();
////    System.out.println("");
//
//    try {
//      org.elasticsearch.action.search.SearchResponse searchResponse = client.prepareSearch()
//          .setIndices("user")
//          .setTypes("user")
//          .execute().actionGet();
//
//      System.out.println(searchResponse.getHits());
//      System.out.println(searchResponse.getHits().totalHits);
//      SearchHit[] result = searchResponse.getHits().getHits();
//      System.out.println(result[0].getSourceAsMap());
//      System.out.println("");
//    } catch (Exception e) {
//
//    }
//  }
//
//
//  @Test
//  public void queryByFilter() {
//
////    GetResponse response =  client.prepareGet().setIndex("user").setOperationThreaded(false).get();
////    Map<String, Object> result = response.getSourceAsMap();
////    System.out.println("");
//
//    try {
//      org.elasticsearch.action.search.SearchResponse searchResponse = client.prepareSearch()
//          .setIndices("user")
//          .setTypes("user")
//          .setQuery(QueryBuilders.termQuery("id", "2323"))
//          .execute().actionGet();
//
//      System.out.println(searchResponse.getHits());
//      System.out.println(searchResponse.getHits().totalHits);
//      SearchHit[] result = searchResponse.getHits().getHits();
//      System.out.println(result[0].getSourceAsMap());
//      System.out.println("");
//    } catch (Exception e) {
//
//    }
//  }
//
//
//  @Test
//  public void queryByFilter2() {
//
////    GetResponse response =  client.prepareGet().setIndex("user").setOperationThreaded(false).get();
////    Map<String, Object> result = response.getSourceAsMap();
////    System.out.println("");
//
//    IndexQuery indexQuery = new IndexQuery();
//    indexQuery.addQuery("localDateTime", COMMAND.EQ, LocalDateTime.now());
//    indexQuery.addQuery("name", COMMAND.GT, LocalDateTime.now());
//
//    Map<String, String[]> filter = indexQuery.getFilterQuery();
//    filter.forEach((k, v) -> {
//      System.out.println(k);
//      System.out.println(v);
//    });
//    System.out.println(indexQuery.getFilterQuery());
//    System.out.println(indexQuery.getFilterQuery());
////    try {
////      org.elasticsearch.action.search.SearchResponse searchResponse = client.prepareSearch()
////          .setIndices("user")
////          .setTypes("user")
////          .setQuery(QueryBuilders.rangeQuery("localdatetime"))
////      System.out.println(searchResponse.getHits());
////      System.out.println(searchResponse.getHits().totalHits);
////      SearchHit[] result = searchResponse.getHits().getHits();
////      System.out.println(result[0].getSourceAsMap());
////      System.out.println("");
////    } catch (Exception e) {
////
////      System.out.println(e);
////    }
//  }
//}