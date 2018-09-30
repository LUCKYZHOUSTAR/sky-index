package sky.index.es.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lucky.sky.util.lang.FaultException;
import lucky.sky.util.lang.StrKit;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import sky.index.comm.dto.GroupQuery;
import sky.index.comm.dto.IndexQuery;
import sky.index.comm.dto.QueryResult;
import sky.index.comm.util.CollectionUtil;
import sky.index.es.enums.AggType;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:29 2018/5/2 搜索结果转换工具
 */
public class SearchMapper {

  private SearchMapper() {
  }

  /**
   * 组装query
   */
  public static String getQuery(Map<String, String[]> query) {
    String result = operateMap(query);
    return "".equals(result) ? null : result;
  }

  /**
   * 组装fiterQuery
   */
  public static String getFilterQuery(Map<String, String[]> filterQuery, String filterQueryExec) {
    String query = operateMap(filterQuery);
    if (!"".equals(query) &&
        filterQueryExec != null &&
        !"".equals(filterQueryExec)) {
      query += " AND " + filterQueryExec;
    } else if ("".equals(query) &&
        filterQueryExec != null &&
        !"".equals(filterQueryExec)) {
      query += filterQueryExec;
    }

    return "".equals(query) ? "*:*" : query;
  }

  /**
   * 组装facetQuery
   */
  public static String getFacetQuery(Map<String, String[]> facetQuery) {
    return operateMap(facetQuery);
  }


  private static String operateMap(Map<String, String[]> query) {
    StringBuilder buf = new StringBuilder();
    int j = 0;
    for (Iterator<Entry<String, String[]>> it = query.entrySet().iterator(); it.hasNext(); ) {
      Entry<String, String[]> entry = it.next();
      if (j > 0) {
        buf.append(" AND ");
      }
      int i = 0;
      String[] vals = entry.getValue();
      boolean isArray = vals != null && vals.length > 1;
      if (isArray) {
        buf.append(" ( ");
      }
      if (vals != null) {
        for (String val : vals) {
          if (i > 0) {
            buf.append(" OR ");
          }
          buf.append(entry.getKey() + ":" + val);
          i++;
        }
      }
      if (isArray) {
        buf.append(" ) ");
      }
      j++;
    }
    return buf.toString();
  }

  /**
   * 组装facet字段
   */
  public static String[] getFacet(Set<String> facet) {
    return facet.toArray(new String[]{});
  }

  /**
   * 组装排序
   */
  public static List<SortBuilder> getSort(Map<String, IndexQuery.ORDER> sort) {
    List<SortBuilder> sortlist = new ArrayList<>();
    sort.entrySet().forEach(s -> {
      String key = s.getKey();
      IndexQuery.ORDER value = s.getValue();
      SortOrder sortOrder = value == IndexQuery.ORDER.asc ? SortOrder.ASC : SortOrder.DESC;

      SortBuilder clause = new FieldSortBuilder(key).order(sortOrder);
      sortlist.add(clause);
    });
    return sortlist;
  }

  /**
   * 获取结果
   */
  public static QueryResult getResult(SearchResponse response, GroupQuery query) {
    QueryResult result = new QueryResult();
    if (response == null) {
      result.setCount(0);
      return result;
    }
    //设置查询总条数
    result.setCount(Integer.valueOf(String.valueOf(response.getHits().getTotalHits())));
    //设置fqResult
    SearchHit[] hits = response.getHits().getHits();
    List<Map<String, Object>> fqResult = new ArrayList<>();
    for (SearchHit doc : hits) {
      Map<String, Object> source = doc.getSource();
      fqResult.add(source);
    }
    result.setFqResult(fqResult);
    //设置scrollId
    String scrollId = response.getScrollId();
    if (StrKit.notBlank(scrollId)) {
      result.setScrollId(scrollId);
    }
    //设置聚合结果
    if (query != null) {
      processAggResult(response, query, result);
    }

    return result;
  }

  /**
   * 处理聚合结果
   */
  public static List<AbstractAggregationBuilder> processAgg(GroupQuery group) {
    List<AbstractAggregationBuilder> list = new ArrayList<>();
    Set<String> fields = group.getFaectField();
    if (CollectionUtil.notEmpty(fields)) {
      for (String field : fields) {
        //TODO:升级修改代码示例
//        TermsBuilder termsBuilder = AggregationBuilders.terms(AggType.TERMS.name() + field).field(field);
        TermsAggregationBuilder termsBuilder = AggregationBuilders
            .terms(AggType.TERMS.name() + field).field(field);
        list.add(termsBuilder);
      }
    }
    Map<String, GroupQuery.Func[]> groupAgg = group.getGroupAgg();
    if (groupAgg != null && !groupAgg.isEmpty()) {
      for (Map.Entry<String, GroupQuery.Func[]> entry : groupAgg.entrySet()) {
        String field = entry.getKey();
        GroupQuery.Func[] value = entry.getValue();
        for (GroupQuery.Func func : value) {
          addGroup(list, func, field);
        }
      }
    }
    Map<String, GroupQuery.RangeAgg<Number>[]> numRangeAgg = group.getNumRangeAgg();
    if (numRangeAgg != null && !numRangeAgg.isEmpty()) {
      for (Map.Entry<String, GroupQuery.RangeAgg<Number>[]> entry : numRangeAgg.entrySet()) {
        String field = entry.getKey();
        for (GroupQuery.RangeAgg<Number> range : entry.getValue()) {
          String name = AggType.RANGE.name() + range.getKey() + field;
          if (range.getStart() == null) {
            list.add(AggregationBuilders.range(name).addUnboundedTo((double) range.getEnd()));
          } else if (range.getEnd() == null) {
            list.add(AggregationBuilders.range(name).addUnboundedFrom((double) range.getEnd()));
          } else {
            list.add(AggregationBuilders.range(name)
                .addRange((double) range.getStart(), (double) range.getEnd()));
          }

        }
      }
    }
//    Map<String, GroupQuery.RangeAgg<Temporal>[]> dateRangeAgg = group.getDateRangeAgg();
//    if (dateRangeAgg != null && !dateRangeAgg.isEmpty()) {
//      for (Map.Entry<String, GroupQuery.RangeAgg<Temporal>[]> entry : dateRangeAgg.entrySet()) {
//        String field = entry.getKey();
//        for (GroupQuery.RangeAgg<Temporal> range : entry.getValue()) {
//          if (range.getStart() == null) {
//            list.add(
//                AggregationBuilders.dateRange(AggType.DATERANGE.name() + field)
//                    .addUnboundedTo(range.getEnd())
//            );
//          } else if (range.getEnd() == null) {
//            list.add(
//                AggregationBuilders.dateRange(AggType.DATERANGE.name() + field)
//                    .addUnboundedFrom(range.getEnd())
//            );
//          } else {
//            list.add(
//                AggregationBuilders.dateRange(AggType.DATERANGE.name() + field)
//                    .addRange(range.getStart(), range.getEnd())
//            );
//          }
//        }
//      }
//    }

    //TODO:升级修改
    Map<String, GroupQuery.RangeAgg<DateTime>[]> dateRangeAgg = group.getDateRangeAgg();
    if (dateRangeAgg != null && !dateRangeAgg.isEmpty()) {
      for (Map.Entry<String, GroupQuery.RangeAgg<DateTime>[]> entry : dateRangeAgg.entrySet()) {
        String field = entry.getKey();
        for (GroupQuery.RangeAgg<DateTime> range : entry.getValue()) {
          if (range.getStart() == null) {
            list.add(
                AggregationBuilders.dateRange(AggType.DATERANGE.name() + field)
                    .addUnboundedTo(range.getEnd())
            );
          } else if (range.getEnd() == null) {
            list.add(
                AggregationBuilders.dateRange(AggType.DATERANGE.name() + field)
                    .addUnboundedFrom(range.getEnd())
            );
          } else {
            list.add(
                AggregationBuilders.dateRange(AggType.DATERANGE.name() + field)
                    .addRange(range.getStart(), range.getEnd())
            );
          }
        }
      }
    }
    Map<String, String[]> aggQuery = group.getAggQuery();
//    if (aggQuery != null && !aggQuery.isEmpty()) {
//      String query = operateMap(aggQuery);
//      list.add(
//          AggregationBuilders.filter(AggType.FILTER.name())
//              .filter(QueryBuilders.queryStringQuery(query))
//      );
//    }

    //TODO:升级修改代码
    if (aggQuery != null && !aggQuery.isEmpty()) {
      String query = operateMap(aggQuery);
      list.add(
          AggregationBuilders.filter(AggType.FILTER.name(), QueryBuilders.queryStringQuery(query))
      );
    }
    return list;
  }

  private static List<AbstractAggregationBuilder> addGroup(List<AbstractAggregationBuilder> list,
      GroupQuery.Func func, String field) {
    field = field.replace(func.name(), "");
    if (func.equals(GroupQuery.Func.MAX)) {

      list.add(AggregationBuilders.max(func.name() + field).field(field));
    } else if (func.equals(GroupQuery.Func.MIN)) {
      list.add(
          AggregationBuilders.min(func.name() + field).field(field)
      );
    } else if (func.equals(GroupQuery.Func.SUM)) {
      list.add(
          AggregationBuilders.sum(func.name() + field).field(field)
      );
    } else if (func.equals(GroupQuery.Func.COUNT)) {
      list.add(
          AggregationBuilders.count(func.name() + field).field(field)
      );
    } else if (func.equals(GroupQuery.Func.AVG)) {
      list.add(
          AggregationBuilders.avg(func.name() + field).field(field)
      );
    } else {
      throw new FaultException("不支持该类型的聚合");
    }
    return list;

  }


  private static void processAggResult(SearchResponse response, GroupQuery group,
      QueryResult result) {
    Aggregations agg = response.getAggregations();

    Set<String> fields = group.getFaectField();
    if (CollectionUtil.notEmpty(fields)) {
      Map<String, Map<Object, Long>> aggResult = new HashMap<>();
      for (String field : fields) {
        Terms faect = agg.get(AggType.TERMS.name() + field);
        Map<Object, Long> m = new HashMap<>();
        for (Terms.Bucket bucket : faect.getBuckets()) {
          m.put(bucket.getKey(), bucket.getDocCount());
        }
        aggResult.put(field, m);
      }
      result.setFacetResult(aggResult);
    }
    Map<String, GroupQuery.Func[]> groupAgg = group.getGroupAgg();
    if (groupAgg != null && !groupAgg.isEmpty()) {
      Map<String, Map<GroupQuery.Func, Object>> aggGroup = new HashMap<>();
      for (Map.Entry<String, GroupQuery.Func[]> entry : groupAgg.entrySet()) {
        String field = entry.getKey();
        GroupQuery.Func[] value = entry.getValue();
        Map<GroupQuery.Func, Object> m = new HashMap<>();
        for (GroupQuery.Func func : value) {
          NumericMetricsAggregation.SingleValue groupValue =
              agg.get(func.name() + field.replace(func.name(), ""));
          m.put(func, groupValue.value());
        }
        aggGroup.put(field, m);
      }
      result.setGroupResult(aggGroup);
    }


  }
}
