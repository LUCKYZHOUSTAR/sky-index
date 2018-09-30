package sky.index.es;

import java.util.List;
import lucky.sky.util.lang.StrKit;
import lucky.sky.util.log.Logger;
import lucky.sky.util.log.LoggerManager;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.indices.IndexClosedException;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import sky.index.comm.dto.IndexQuery;
import sky.index.comm.dto.QueryResult;
import sky.index.comm.esmeta.ElasticIndex;
import sky.index.comm.util.Mapper;
import sky.index.comm.util.Utils;
import sky.index.comm.util.serializer.TypeRef;
import sky.index.contract.IndexRead;
import sky.index.es.client.TClient;
import sky.index.es.mapper.SearchMapper;


/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:31 2018/5/2
 */
@Component
public class ESRead implements IndexRead {

  Logger log = LoggerManager.getLogger(ESRead.class);

  TransportClient client;

  @Override
  public QueryResult search(String mapping, IndexQuery query, int pageIndex, int pageSize,
      String clusterName) {

    Mapper.EntityInfo info = Utils.parseObject(mapping, new TypeRef<Mapper.EntityInfo>() {
    });
    client = TClient.getClient(clusterName);
    ElasticIndex index = info.getIndex();
    SearchRequestBuilder builder = client.prepareSearch(index.getIndexName())
        .setTypes(index.getIndexType())
        .setSearchType(SearchType.QUERY_THEN_FETCH)
        .setFrom((pageIndex - 1) * pageSize)
        .setSize(pageSize);
    //查询条件
    BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
    String paramquery = SearchMapper.getQuery(query.getQuery());
    //设置 query
    if (paramquery != null) {
      booleanQueryBuilder.must(
          QueryBuilders.queryStringQuery(paramquery)
          //TODO:修改
//              .lowercaseExpandedTerms(false)
      );
    }

    String filterQuery = SearchMapper.getFilterQuery(
        query.getFilterQuery(),
        query.getFilterQueryExec()
    );
    //设置filterQuery
    if (filterQuery != null) {
      booleanQueryBuilder.must(
          QueryBuilders.queryStringQuery(filterQuery)
//                            .defaultOperator(QueryStringQueryBuilder.Operator.AND)
//                            .maxDeterminizedStates(100)
          //TODO:修改
//              .lowercaseExpandedTerms(false)
      );
    }
//        builder.setPostFilter(booleanQueryBuilder);
    builder.setQuery(booleanQueryBuilder);
    //设置聚合
    if (query.getGroupQuery() != null) {
      List<AbstractAggregationBuilder> builders = SearchMapper.processAgg(query.getGroupQuery());
      if (builders != null && !builders.isEmpty()) {
        for (AbstractAggregationBuilder b : builders) {
          builder.addAggregation(b);
        }
      }
    }
    //scriptQuery
//    if (!StrKit.isBlank(query.getScriptQuery())) {
//      booleanQueryBuilder.must(QueryBuilders.scriptQuery(
//          new Script(query.getScriptQuery(), ScriptService.ScriptType.INLINE, "groovy", null)));
//    }
    //设置sort
    List<SortBuilder> sort = SearchMapper.getSort(query.getSort());
    sort.forEach(s -> builder.addSort(s));
    SearchResponse response = null;
    try {
      response = builder.get();
    } catch (IndexNotFoundException e) {
      log.info("索引不存在，需要屏蔽掉改异常:{}", e);
    } catch (IndexClosedException e) {
      log.info("索引关闭，需要屏蔽掉改异常:{}", e);
    }

    return SearchMapper.getResult(response, query.getGroupQuery());
  }


  @Override
  public QueryResult scrollSearch(String mapping, IndexQuery query, String scrollId, int pageSize,
      String clusterName) {

    client = TClient.getClient(clusterName);
    SearchResponse response = null;
    TimeValue fetchTime = new TimeValue(60000);
    if (StrKit.isBlank(scrollId)) {
      Mapper.EntityInfo info = Utils.parseObject(mapping, new TypeRef<Mapper.EntityInfo>() {
      });
      ElasticIndex index = info.getIndex();
      SearchRequestBuilder builder = client.prepareSearch(index.getIndexName())
          .setTypes(index.getIndexType())
          .setScroll(fetchTime)
          .setSize(pageSize);
      //查询条件
      BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();
      String paramquery = SearchMapper.getQuery(query.getQuery());
      //设置 query
      if (paramquery != null) {
        booleanQueryBuilder.must(
            QueryBuilders.queryStringQuery(paramquery).analyzer(null)
        );
      }
      String filterQuery = SearchMapper.getFilterQuery(
          query.getFilterQuery(),
          query.getFilterQueryExec()
      );
      //设置filterQuery
      if (filterQuery != null) {
        booleanQueryBuilder.must(
            QueryBuilders.queryStringQuery(filterQuery).analyzer(null)
        );
      }
      //设置聚合
      if (query.getGroupQuery() != null) {
        List<AbstractAggregationBuilder> builders = SearchMapper.processAgg(query.getGroupQuery());
        if (builders != null && !builders.isEmpty()) {
          for (AbstractAggregationBuilder b : builders) {
            builder.addAggregation(b);
          }
        }
      }
      //scriptQuery
//      if (!StrKit.isBlank(query.getScriptQuery())) {
//        booleanQueryBuilder.must(QueryBuilders.scriptQuery(
//            new Script(query.getScriptQuery(), ScriptService.ScriptType.INLINE, "groovy", null)));
//      }
//            builder.setPostFilter(booleanQueryBuilder);
      builder.setQuery(booleanQueryBuilder);
      //设置sort
      List<SortBuilder> sort = SearchMapper.getSort(query.getSort());
      if (sort.isEmpty()) {
//        builder.addSort(SortParseElement.DOC_FIELD_NAME, SortOrder.ASC);
        builder.addSort("_doc", SortOrder.ASC);

      } else {
        sort.forEach(s -> builder.addSort(s));
      }
      try {
        response = builder.get();
      } catch (IndexNotFoundException e) {
        log.info("索引不存在，需要屏蔽掉改异常:{}", e);
      } catch (IndexClosedException e) {
        log.info("索引关闭，需要屏蔽掉改异常:{}", e);
      }
    } else {
      query.setGroupQuery(null);
      response = client.prepareSearchScroll(scrollId).setScroll(fetchTime).get();
    }
    return SearchMapper.getResult(response, query.getGroupQuery());
  }


  @Override
  public void cleanScrollId(String scrollId, String clusterName) {
    ClearScrollRequest request = new ClearScrollRequest();
    request.addScrollId(scrollId);
    client = TClient.getClient(clusterName);
    ClearScrollResponse response = client.clearScroll(request).actionGet();
    if (response.isSucceeded()) {
      log.info("释放scrollId : [{}] 成功！ ", scrollId);
    }
  }
}
