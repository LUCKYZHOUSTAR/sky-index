package sky.index.test;

import com.sun.jna.platform.win32.Winnetwk.RESOURCEDISPLAYTYPE;
import java.util.List;
import java.util.Map;
import lucky.sky.net.rpc.RpcApplication;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sky.index.cilent.SearcherClient;
import sky.index.cilent.searcher.SearchResult;
import sky.index.client.model.Mycore;
import sky.index.comm.dto.GroupQuery;
import sky.index.comm.dto.IndexQuery;
import sky.index.comm.dto.IndexQuery.COMMAND;
import sky.index.comm.util.FastJsonUtil;
import sky.index.comm.util.serializer.TypeRef;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午11:18 2018/7/3
 */
@SpringBootApplication(scanBasePackages = {"sky.*"})
public class QueryTest {


  public static void main(String[] args) {

    scrollSearch(args);

//    pageSearch(args);

//    scroolidpageSearch(args);

//    searchAggs(args);
  }


  /*
  scroll查询操作
   */
  public static void scrollSearch(String[] args) {

    SearcherClient searcherClient = new RpcApplication(QueryTest.class, args).run()
        .getBean(SearcherClient.class);

    if (searcherClient == null) {
      System.out.println("没有获取到");
      return;
    }

    IndexQuery indexQuery = new IndexQuery();
    indexQuery.addQuery("name", IndexQuery.COMMAND.EQ, "张三");
    searcherClient.scrollSearch(new TypeRef<List<Mycore>>() {
    }, Mycore.class, indexQuery, 20, result -> {
      System.out.println("=---------------------------------------=");
      System.out.println(FastJsonUtil.bean2Json(result));

      //处理你的业务逻辑(处理这900条数据，要控制在1分钟以内)

      List<Mycore> mycores = result.getSearchList();
      mycores.forEach(data -> {
        System.out.println(data.getAge());
      });
      return true; //return false 后不再返回数据，return true 后继续返回数据

    });

  }


  /*
  分页查询
   */
  public static void pageSearch(String[] args) {

    SearcherClient searcherClient = new RpcApplication(QueryTest.class, args).run()
        .getBean(SearcherClient.class);
    IndexQuery indexQuery = new IndexQuery();
    indexQuery.addQuery("name", IndexQuery.COMMAND.EQ, "张三");
    indexQuery.addQuery("age", COMMAND.GTE, 100);
    SearchResult<Mycore> result = searcherClient.search
        (new TypeRef<List<Mycore>>() {
         }, Mycore.class,
            indexQuery, 1, 10);

    System.out.println("");

  }


  /*
 分页查询
  */
  public static void scroolidpageSearch(String[] args) {

    SearcherClient searcherClient = new RpcApplication(QueryTest.class, args).run()
        .getBean(SearcherClient.class);
    IndexQuery indexQuery = new IndexQuery();
    indexQuery.addQuery("name", IndexQuery.COMMAND.EQ, "张三");
    SearchResult<Mycore> result = searcherClient.searchByScrollId
        (new TypeRef<List<Mycore>>() {
         }, Mycore.class,
            indexQuery, 10, null);

    System.out.println("");

  }





  public static void searchAggs(String[] args){

    SearcherClient searcherClient = new RpcApplication(QueryTest.class, args).run()
        .getBean(SearcherClient.class);

    if (searcherClient == null) {
      System.out.println("没有获取到");
      return;
    }

    IndexQuery indexQuery = new IndexQuery();
    GroupQuery group = new GroupQuery();
    group.addAggField("age");
    group.addFuncAgg("age",GroupQuery.Func.AVG);
    group.addFuncAgg("age",GroupQuery.Func.COUNT);
    group.addFuncAgg("age",GroupQuery.Func.MAX);
    group.addFuncAgg("age",GroupQuery.Func.MIN);
    group.addFuncAgg("age",GroupQuery.Func.SUM);
    indexQuery.setGroupQuery(group);
    SearchResult<Mycore> result = searcherClient.search(new TypeRef<List<Mycore>>() {
    }, Mycore.class, indexQuery, 1, 30);
    Map<String, Map<Object, Long>> facetResult = result.getFacetResult();
    for (Map.Entry<String, Map<Object, Long>> entry : facetResult.entrySet()) {
      System.out.println("field: "+entry.getKey());
      Map<Object, Long> value = entry.getValue();
      for (Map.Entry<Object, Long> map : value.entrySet()) {
        System.out.println("key : "+map.getKey() + "  count: "+map.getValue());
      }
    }



    Map<String, Map<GroupQuery.Func, Object>> groupResult = result.getGroupResult();
    for (Map.Entry<String, Map<GroupQuery.Func, Object>> entry : groupResult.entrySet()) {
      System.out.println("field: "+entry.getKey());
      Map<GroupQuery.Func, Object> value = entry.getValue();
      for (Map.Entry<GroupQuery.Func, Object> map : value.entrySet()) {
        System.out.println(map.getKey().name() + "  :  "+map.getValue());
      }
    }

  }


  public static void searchAggss(String[] args){

    SearcherClient searcherClient = new RpcApplication(QueryTest.class, args).run()
        .getBean(SearcherClient.class);

    if (searcherClient == null) {
      System.out.println("没有获取到");
      return;
    }

    IndexQuery indexQuery = new IndexQuery();
    GroupQuery group = new GroupQuery();
    group.addAggField("age");
    group.addFuncAgg("age",GroupQuery.Func.AVG);
    group.addFuncAgg("age",GroupQuery.Func.COUNT);
    group.addFuncAgg("age",GroupQuery.Func.MAX);
    group.addFuncAgg("age",GroupQuery.Func.MIN);
    group.addFuncAgg("age",GroupQuery.Func.SUM);
    indexQuery.setGroupQuery(group);




    SearchResult<Mycore> result = searcherClient.search(new TypeRef<List<Mycore>>() {
    }, Mycore.class, indexQuery, 1, 30);
    Map<String, Map<Object, Long>> facetResult = result.getFacetResult();
    for (Map.Entry<String, Map<Object, Long>> entry : facetResult.entrySet()) {
      System.out.println("field: "+entry.getKey());
      Map<Object, Long> value = entry.getValue();
      for (Map.Entry<Object, Long> map : value.entrySet()) {
        System.out.println("key : "+map.getKey() + "  count: "+map.getValue());
      }
    }



    Map<String, Map<GroupQuery.Func, Object>> groupResult = result.getGroupResult();
    for (Map.Entry<String, Map<GroupQuery.Func, Object>> entry : groupResult.entrySet()) {
      System.out.println("field: "+entry.getKey());
      Map<GroupQuery.Func, Object> value = entry.getValue();
      for (Map.Entry<GroupQuery.Func, Object> map : value.entrySet()) {
        System.out.println(map.getKey().name() + "  :  "+map.getValue());
      }
    }

  }


}
