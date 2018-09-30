package sky.index.contract;

import sky.index.comm.dto.IndexQuery;
import sky.index.comm.dto.QueryResult;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 下午1:50 2018/4/28
 */
public interface IndexRead {
  /**
   * 检索数据
   * @param mapping 索引服务器url
   * @param query 检索条件
   * @param pageIndex 页码
   * @param pageSize 每页数据量
   * @return
   */
  QueryResult search(String mapping, IndexQuery query, int pageIndex, int pageSize,String clusterName);

  /**
   * 检索数据
   * @param mapping 索引服务器url
   * @param query 检索条件
   * @param scrollId scrollId 相当于与es保持的会话
   * @param pageSize 每页数据量
   * @return
   */
  QueryResult scrollSearch(String mapping, IndexQuery query,String scrollId, int pageSize,String clusterName);


  /**
   * 释放scrollId
   * @param scrollId
   */
  void cleanScrollId(String scrollId,String clusterName);
}
