package sky.index.service.contract.constant;


import sky.index.service.contract.dto.SearchDto;
import sky.index.service.contract.dto.SearchDto.CloseScrollRequest;
import sky.index.service.contract.dto.SearchDto.IndexRequest;
import sky.index.service.contract.dto.SearchDto.ScrollSearchRequest;
import sky.index.service.contract.dto.SearchDto.ScrollSearchResponse;
import sky.index.service.contract.dto.SearchDto.SearchRequest;
import sky.index.service.contract.dto.SearchDto.SearchResponse;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:32 2018/4/28 后台通用的搜索的服务的信息
 */
public interface SearchService {

  /**
   * 搜索
   **/
  SearchResponse search(SearchRequest request);

  /**
   * 全量搜索
   **/
  ScrollSearchResponse scrollSearch(ScrollSearchRequest request);

  /**
   * 建索引
   **/
  void index(IndexRequest request);

  /**
   * 释放scroll
   **/
  void closeScroll(CloseScrollRequest request);
}
