package sky.index.service.impl;

import lucky.sky.util.log.Logger;
import lucky.sky.util.log.LoggerManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sky.index.comm.dto.QueryResult;
import sky.index.comm.util.Utils;
import sky.index.contract.IndexRead;
import sky.index.contract.IndexWrite;
import sky.index.core.indexer.IndexMsgHandler;
import sky.index.core.searcher.SearchHandler;
import sky.index.service.contract.dto.SearchDto;
import sky.index.service.contract.dto.SearchDto.IndexRequest;
import sky.index.service.contract.dto.SearchDto.SearchRequest;
import sky.index.service.contract.dto.SearchDto.SearchResponse;
import sky.index.service.contract.iface.SearchLogService;
import sky.index.service.impl.mapper.SearchResponseMapper;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:52 2018/5/2
 */

@Component
public class SearchLogServiceImpl implements SearchLogService {


  Logger logger = LoggerManager.getLogger(SearchLogServiceImpl.class);

  @Autowired
  IndexWrite indexWrite;


  @Autowired
  IndexRead indexRead;




  /**
   * 搜索
   *
   * @param request
   */
  @Override
  public SearchResponse search(SearchRequest request) {
    int pageIndex = request.getPageInfo().getPageIndex();
    int pageSize = request.getPageInfo().getPageSize();
    logger.info("进行查询的条件是: [{}],分页条件为: [pageIndex:{},pageSize:{}] ",
        request.getKeyword(), pageIndex,pageSize);
    QueryResult queryResult = SearchHandler.handleQuery(
        request.getObjectType(),
        request.getKeyword(),
        pageIndex,
        pageSize,
        indexRead);
    if (logger.isDebugEnabled()) {
      logger.debug("查询的结果是:  {} ", Utils.toJson(queryResult));
    }
    //搜索结果转成SearchResponse
    return SearchResponseMapper.of(queryResult);
  }

  @Override
  public SearchDto.ScrollSearchResponse scrollSearch(SearchDto.ScrollSearchRequest request) {
    String scrollId = request.getScrollId();
    int pageSize = request.getPageSize();
    logger.info("进行查询的条件是: [{}],分页条件为: [scrollId:{},pageSize:{}]",
        request.getKeyword(), scrollId,pageSize);
    QueryResult queryResult = SearchHandler.handleQuery(
        request.getObjectType(),
        request.getKeyword(),
        scrollId,
        pageSize,
        indexRead);
    if (logger.isDebugEnabled()) {
      logger.debug("查询的结果是:  {} ",Utils.toJson(queryResult));
    }
    //搜索结果转成SearchResponse
    if (StringUtils.isEmpty(queryResult.getScrollId())){
      queryResult.setScrollId(scrollId);
    }
    return SearchResponseMapper.ofscroll(queryResult);
  }

  /**
   * 操作索引
   */
  @Override
  public void index(IndexRequest request) {
    String indexMsgJson = request.getIndexMsgJson();
    logger.info("需要操作的索引是:{}", indexMsgJson);
    IndexMsgHandler.indexObjHandler(indexMsgJson, indexWrite);
  }

  @Override
  public void closeScroll(SearchDto.CloseScrollRequest request) {
    String scrollId = request.getScrollId();
    indexRead.cleanScrollId(scrollId,request.getClusterRouter());
  }

}
