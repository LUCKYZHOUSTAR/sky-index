package sky.index.es;

import sky.index.comm.dto.IndexQuery;
import sky.index.comm.dto.QueryResult;
import sky.index.contract.IndexRead;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:32 2018/5/2
 */
public class ESRead2 implements IndexRead {

  @Override
  public QueryResult search(String mapping, IndexQuery query, int pageIndex, int pageSize,
      String clusterName) {
    return null;
  }

  @Override
  public QueryResult scrollSearch(String mapping, IndexQuery query, String scrollId, int pageSize,
      String clusterName) {
    return null;
  }

  @Override
  public void cleanScrollId(String scrollId, String clusterName) {

  }
}
