package sky.index.comm.dto;

import java.util.List;
import java.util.Map;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:22 2018/4/28
 */
public class QueryResult {

  /**
   * 检索结果总数
   */
  private int count;

  /**
   * 检索结果集
   */
  private List<Map<String, Object>> fqResult;

  /**
   * 聚合结果集
   */
  private Map<String,Map<Object,Long>> facetResult;

  /**
   * 分组group结果
   */
  private Map<String,Map<GroupQuery.Func,Object>> groupResult;

  /**
   * scrollId
   */
  private String scrollId;

  public String getScrollId() {
    return scrollId;
  }

  public void setScrollId(String scrollId) {
    this.scrollId = scrollId;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public List<Map<String, Object>> getFqResult() {
    return fqResult;
  }


  public void setFqResult(List<Map<String, Object>> fqResult) {
    this.fqResult = fqResult;
  }

  public Map<String, Map<Object, Long>> getFacetResult() {
    return facetResult;
  }

  public void setFacetResult(Map<String, Map<Object, Long>> facetResult) {
    this.facetResult = facetResult;
  }

  public Map<String, Map<GroupQuery.Func, Object>> getGroupResult() {
    return groupResult;
  }

  public void setGroupResult(Map<String, Map<GroupQuery.Func, Object>> groupResult) {
    this.groupResult = groupResult;
  }
}
