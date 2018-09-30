package sky.index.cilent.searcher;

import java.util.List;
import java.util.Map;
import sky.index.comm.dto.GroupQuery;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:38 2018/4/28
 */
public class SearchResult<T> {
  private Integer searchCount;

  private List<T> searchList;

  private Map<String,Map<Object,Long>> facetResult;

  /**
   * 分组group结果
   */
  private Map<String,Map<GroupQuery.Func,Object>> groupResult;

  private String scrollId;

  public String getScrollId() {
    return scrollId;
  }

  public void setScrollId(String scrollId) {
    this.scrollId = scrollId;
  }

  public Integer getSearchCount() {
    return searchCount;
  }

  public void setSearchCount(Integer searchCount) {
    this.searchCount = searchCount;
  }

  public List<T> getSearchList() {
    return searchList;
  }

  public void setSearchList(List<T> searchList) {
    this.searchList = searchList;
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
