package sky.index.cilent.indexer;

import java.util.Set;
import sky.index.comm.dto.IndexMsg;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:37 2018/4/28
 */
public class IndexerMsger {
  private IndexerMsger (){}

  /**
   * 构建索引消息
   * @param className
   * @param indexType
   * @param obj
   * @param fieldName
   * @return
   */
  public static <T> IndexMsg<T> createMsg(String className
      , IndexMsg.INDEXTYPE indexType
      , T obj
      ,Set<String> fieldName){
    IndexMsg<T> indexMsg = new IndexMsg<>();
    indexMsg.setClassName(className);
    indexMsg.setIndexType(indexType);
    indexMsg.setObj(obj);
    indexMsg.setFieldName(fieldName);

    return indexMsg;
  }
}
