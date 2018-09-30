package sky.index.core.indexer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lucky.sky.util.lang.FaultException;
import lucky.sky.util.log.Logger;
import lucky.sky.util.log.LoggerManager;
import org.springframework.util.StringUtils;
import sky.index.comm.constants.IndexConstants;
import sky.index.comm.dto.IndexMsg;
import sky.index.comm.util.Utils;
import sky.index.comm.util.serializer.TypeRef;
import sky.index.contract.IndexWrite;

/**
 * Created by jinweile on 2016/1/21.
 * 消息转成索引需要的Map对象
 */
public class IndexMsgHandler {

  static Logger logger = LoggerManager.getLogger(IndexMsgHandler.class);

  private IndexMsgHandler(){}
  /**
   * 增删改索引对象
   * @param msg
   * @param indexWrite
   */
  public static void indexObjHandler(String msg, IndexWrite indexWrite) {
//        IndexMsg<String> indexObj = Utils.parseObject(msg, new TypeReference<IndexMsg<String>>(){});
    IndexMsg<String> indexObj = Utils.parseObject(msg, new TypeRef<IndexMsg<String>>(){});
    String obj = indexObj.getObj();
    String info = indexObj.getClassName();
    String router = indexObj.getClusterRouter();
    if (StringUtils.isEmpty(router)){
      throw new FaultException(IndexConstants.CLIENT_ES_CLUSTER_KEY +" : 不可以为空");
    }
    Set<String> nullField = indexObj.getFieldName();
    if (indexObj.getIndexType() == IndexMsg.INDEXTYPE.CREATE){
      Map<String, Object> doc = Utils.parseHashMap(obj);
      createIndex(info, doc, indexWrite,router);
    } else if (indexObj.getIndexType() == IndexMsg.INDEXTYPE.CREATELIST){
      List<Map<String, Object>> docs = Utils.parseObject(obj, new TypeRef<List<Map<String, Object>>>(){});
      createIndex(info, docs, indexWrite,router);
    } else if (indexObj.getIndexType() == IndexMsg.INDEXTYPE.UPDATE){
      Map<String, Object> doc = Utils.parseHashMap(obj);
//            Map<String, Object> doc = Utils.parseObject(obj, new TypeRef<HashMap<String,Object>>() {});
      if(nullField !=null && !nullField.isEmpty()){
        for (String field : nullField) {
          doc.put(field, null);
        }
      }
      updateIndex(info, doc, indexWrite,router);
    } else if (indexObj.getIndexType() == IndexMsg.INDEXTYPE.UPDATELIST){
      List<Map<String, Object>> docs = Utils.parseListHashMap(obj);
      updateIndex(info, docs, indexWrite,router);
    } else if (indexObj.getIndexType() == IndexMsg.INDEXTYPE.DELETE) {
      String uniqueKeyValue = obj;
      deleteIndex(info, uniqueKeyValue, indexWrite,router);
    } else if (indexObj.getIndexType() == IndexMsg.INDEXTYPE.DELETELIST) {
      List<String> uniqueKeyValues = Utils.parseObject(obj, new TypeRef<List<String>>(){});
      deleteIndex(info, uniqueKeyValues, indexWrite,router);
    }
  }

  private static void createIndex(String info, Map<String, Object> doc, IndexWrite indexWrite,String clusterName) {
    indexWrite.create(info, doc,clusterName);
  }
  private static void createIndex(String info, List<Map<String, Object>> docs, IndexWrite indexWrite,String clusterName){
    indexWrite.create(info, docs,clusterName);
  }

  private static void updateIndex(String info, Map<String, Object> doc, IndexWrite indexWrite,String clusterName){
    indexWrite.update(info, doc,clusterName);
  }
  private static void updateIndex(String info, List<Map<String, Object>> docs, IndexWrite indexWrite,String clusterName){
    indexWrite.update(info, docs,clusterName);
  }

  private static void deleteIndex(String info, String uniqueKeyValue, IndexWrite indexWrite,String clusterName){
    indexWrite.delete(info, uniqueKeyValue,clusterName);
  }
  private static void deleteIndex(String info, List<String> uniqueKeyValue, IndexWrite indexWrite,String clusterName){
    indexWrite.delete(info, uniqueKeyValue,clusterName);
  }
}
