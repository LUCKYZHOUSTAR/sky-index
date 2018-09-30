package sky.index.service.impl.mapper;

import sky.index.comm.dto.QueryResult;
import sky.index.comm.util.Utils;
import sky.index.service.contract.dto.SearchDto;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:50 2018/5/2
 */
public class SearchResponseMapper {


  private SearchResponseMapper() {
  }


  /**
   * 搜索结果转换
   */
  public static SearchDto.SearchResponse of(QueryResult queryResult) {
    SearchDto.SearchResponse searchResponse = new SearchDto.SearchResponse();
    searchResponse.setFacets(Utils.toJson(queryResult.getFacetResult()));
    searchResponse.setItems(Utils.toJson(queryResult.getFqResult()));
    searchResponse.setGroups(Utils.toJson(queryResult.getGroupResult()));
    searchResponse.setTotalCount(queryResult.getCount());

    return searchResponse;
  }

  /**
   * 搜索结果转换
   */
  public static SearchDto.ScrollSearchResponse ofscroll(QueryResult queryResult) {
    SearchDto.ScrollSearchResponse searchResponse = new SearchDto.ScrollSearchResponse();
    searchResponse.setFacets(Utils.toJson(queryResult.getFacetResult()));
    searchResponse.setItems(Utils.toJson(queryResult.getFqResult()));
    searchResponse.setTotalCount(queryResult.getCount());
    searchResponse.setGroups(Utils.toJson(queryResult.getGroupResult()));
    searchResponse.setScrollId(queryResult.getScrollId());
    return searchResponse;
  }
}
