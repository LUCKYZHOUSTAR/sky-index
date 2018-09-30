package sky.index.core.searcher;

import lucky.sky.util.lang.FaultException;
import org.apache.commons.lang3.StringUtils;
import sky.index.comm.constants.IndexConstants;
import sky.index.comm.dto.IndexQuery;
import sky.index.comm.dto.QueryResult;
import sky.index.comm.util.Utils;
import sky.index.contract.IndexRead;
import sky.index.comm.util.serializer.TypeRef;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:48 2018/5/2
 */
public class SearchHandler {
  private SearchHandler(){}

  /**
   * 解析检索条件，发送检索请求，并返回检索结果
   * @param mapping
   * @param query
   * @param pageIndex
   * @param pageSize
   * @param indexRead
   * @return
   */
  public static QueryResult handleQuery(String mapping,
      String query,
      int pageIndex,
      int pageSize,
      IndexRead indexRead){
    IndexQuery indexQuery = Utils.parseObject(query, new TypeRef<IndexQuery>(){});
    if (StringUtils.isEmpty(indexQuery.getClusterRouter())){
      throw new FaultException(IndexConstants.CLIENT_ES_CLUSTER_KEY +" : 不可以为空");
    }
    return search(mapping, indexQuery, pageIndex, pageSize, indexRead,indexQuery.getClusterRouter());
  }

  private static QueryResult search(String mapping,
      IndexQuery indexQuery,
      int pageIndex,
      int pageSize,
      IndexRead indexRead,String clusterName){
    return indexRead.search(mapping, indexQuery, pageIndex, pageSize,clusterName);
  }

  private static QueryResult scrollSearch(String mapping,
      IndexQuery indexQuery,
      String scrollId,
      int pageSize,
      IndexRead indexRead, String clusterName){
    return indexRead.scrollSearch(mapping, indexQuery, scrollId, pageSize,clusterName);
  }

  /**
   * 解析检索条件，发送检索请求，并返回检索结果
   * @param mapping
   * @param query
   * @param scrollId
   * @param pageSize
   * @param indexRead
   * @return
   */
  public static QueryResult handleQuery(String mapping,
      String query,
      String scrollId,
      int pageSize,
      IndexRead indexRead){
    IndexQuery indexQuery = Utils.parseObject(query, new TypeRef<IndexQuery>(){});
    if (StringUtils.isEmpty(indexQuery.getClusterRouter())){
      throw new FaultException(IndexConstants.CLIENT_ES_CLUSTER_KEY +" : 不可以为空");
    }
    return scrollSearch(mapping, indexQuery, scrollId, pageSize, indexRead,indexQuery.getClusterRouter());
  }
}
