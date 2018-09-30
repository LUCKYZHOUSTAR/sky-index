package sky.index.cilent.searcher;

import java.util.List;
import java.util.Map;
import lucky.sky.util.data.PageInfo;
import lucky.sky.util.log.Logger;
import lucky.sky.util.log.LoggerManager;
import sky.index.comm.dto.GroupQuery;
import sky.index.comm.dto.IndexQuery;
import sky.index.comm.util.Utils;
import sky.index.comm.util.serializer.TypeRef;
import sky.index.service.contract.dto.SearchDto;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:39 2018/4/28
 */
public class SearcherMsger {
  private static Logger logger = LoggerManager.getLogger(SearcherMsger.class);
  private SearcherMsger(){}

  /**
   * 构建检索消息
   * @param mapping
   * @param indexQuery
   * @param pageIndex
   * @param pageSize
   * @return
   */
  public static SearchDto.SearchRequest createRequest(String mapping, IndexQuery indexQuery, int pageIndex, int pageSize) {
    SearchDto.SearchRequest searchRequest = new SearchDto.SearchRequest();
    searchRequest.setObjectType(mapping);
    if (indexQuery == null) {
      searchRequest.setKeyword(Utils.toJson(new IndexQuery()));
    } else {
      searchRequest.setKeyword(Utils.toJson(indexQuery));
    }
    PageInfo pageInfo = new PageInfo();
    pageInfo.setPageIndex(pageIndex);
    pageInfo.setPageSize(pageSize);
    searchRequest.setPageInfo(pageInfo);

    return searchRequest;
  }

  /**
   * 构建检索消息
   * @param mapping
   * @param indexQuery
   * @param scrollId
   * @param pageSize
   * @return
   */
  public static SearchDto.ScrollSearchRequest createRequest(String mapping, IndexQuery indexQuery, String scrollId, int pageSize) {
    SearchDto.ScrollSearchRequest searchRequest = new SearchDto.ScrollSearchRequest();
    searchRequest.setObjectType(mapping);
    if (indexQuery == null) {
      searchRequest.setKeyword(Utils.toJson(new IndexQuery()));
    } else {
      searchRequest.setKeyword(Utils.toJson(indexQuery));
    }
    searchRequest.setPageSize(pageSize);
    searchRequest.setScrollId(scrollId);
    return searchRequest;
  }

  /**
   * 把搜索结果转换成强类型
   * @param typeRef
   * @param searchResponse
   * @return
   */
  public static <T> SearchResult<T> parseSearchResult(TypeRef<List<T>> typeRef, SearchDto.SearchResponse searchResponse){
    String fqResult = searchResponse.getItems();
    String facetResult = searchResponse.getFacets();
    String groupResult = searchResponse.getGroups();
//		logger.info("fqResult :{},facetResult:{},groupResult{}",fqResult,facetResult,groupResult);
    List<T> searchList = Utils.parseObject(fqResult, typeRef);
    Map<String, Map<Object,Long>> facetMap = Utils.parseObject(
        facetResult,
        new TypeRef<Map<String, Map<Object,Long>>>(){});
    Map<String,Map<GroupQuery.Func,Object>> groupMap = Utils.parseObject(
        groupResult,
        new TypeRef<Map<String,Map<GroupQuery.Func,Object>>>(){});
    SearchResult<T> searchResult = new SearchResult<>();
    searchResult.setSearchList(searchList);
    searchResult.setFacetResult(facetMap);
    searchResult.setGroupResult(groupMap);
    searchResult.setSearchCount(searchResponse.getTotalCount());

    return searchResult;
  }

  /**
   * 把搜索结果转换成强类型
   * @param typeRef
   * @param searchResponse
   * @return
   */
  public static <T> SearchResult<T> parseSearchResult(TypeRef<List<T>> typeRef, SearchDto.ScrollSearchResponse searchResponse){
    String fqResult = searchResponse.getItems();
    String facetResult = searchResponse.getFacets();
    String groupResult = searchResponse.getGroups();
//		logger.info("fqResult :{},facetResult:{},groupResult{}",fqResult,facetResult,groupResult);
    List<T> searchList = Utils.parseObject(fqResult, typeRef);
    Map<String,Map<Object,Long>>  facetMap = Utils.parseObject(
        facetResult,
        new TypeRef<Map<String, Map<Object,Long>>>(){});
    Map<String,Map<GroupQuery.Func,Object>> groupMap = Utils.parseObject(
        groupResult,
        new TypeRef<Map<String,Map<GroupQuery.Func,Object>>>(){});
    SearchResult<T> searchResult = new SearchResult<>();
    searchResult.setSearchList(searchList);
    searchResult.setFacetResult(facetMap);
    searchResult.setGroupResult(groupMap);
    searchResult.setSearchCount(searchResponse.getTotalCount());
    searchResult.setScrollId(searchResponse.getScrollId());
    return searchResult;
  }
}
