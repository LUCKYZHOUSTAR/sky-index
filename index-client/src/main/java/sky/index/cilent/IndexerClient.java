package sky.index.cilent;

import java.util.List;
import java.util.Set;
import lucky.sky.mq.msg.rocketmq.provider.Publisher;
import lucky.sky.net.rpc.RpcClient;
import lucky.sky.util.config.ConfigProperties;
import lucky.sky.util.lang.FaultException;
import org.apache.commons.lang3.StringUtils;
import sky.index.cilent.indexer.IndexerMsger;
import sky.index.comm.constants.IndexConstants;
import sky.index.comm.dto.IndexMsg;
import sky.index.comm.util.Mapper;
import sky.index.comm.util.Utils;
import sky.index.service.contract.dto.SearchDto.IndexRequest;
import sky.index.service.contract.iface.SearchAdminService;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:40 2018/4/28
 */
public class IndexerClient {

  private static final String UNIQUEVALERRORSTR = "唯一键值没有设定！";
  private static final String ERRORSTR = "需要索引的类没有设定！";
  private static final String INDEXSIZEERRORSTR = "index obj List is null or size = 0";
  private static final String INDEXERRORSTR = "index obj is null";
  private static final String UNIQUEERRORSTR = "唯一键名没有设定！";
  private static final String ROUTER = ConfigProperties
      .getProperty(IndexConstants.CLIENT_ES_CLUSTER_KEY);
  private static SearchAdminService searchService = RpcClient
      .get("cmc.index.admin.search.service", SearchAdminService.class);


  private IndexerClient() {
  }

  /**
   * 异步创建索引
   */
  public static <T> void createIndexAsy(T obj) {
    if (obj == null) {
      throw new FaultException(INDEXERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(obj.getClass());
    IndexMsg<T> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.CREATE,
        obj,
        null);

    //把对象序列化成json并发送到消息队列mq
    sendMsg(indexMsg);
  }

  /**
   * 同步创建索引
   */
  public static <T> void createIndex(T obj) {
    if (obj == null) {
      throw new FaultException(INDEXERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(obj.getClass());
    IndexMsg<T> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.CREATE,
        obj,
        null);

    //把对象序列化成json并发送到消息队列mq
    writeMsg(indexMsg);
  }

  /**
   * 异步批量创建索引
   */
  public static <T> void createIndexListAsy(List<T> objs) {
    if (objs == null || objs.isEmpty()) {
      throw new FaultException(INDEXSIZEERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(objs.get(0).getClass());
    IndexMsg<List<T>> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.CREATELIST,
        objs,
        null);

    //把对象序列化成json并发送到消息队列mq
    sendMsg(indexMsg);
  }

  /**
   * 同步批量创建索引
   */
  public static <T> void createIndexList(List<T> objs) {
    if (objs == null || objs.isEmpty()) {
      throw new FaultException(INDEXSIZEERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(objs.get(0).getClass());
    IndexMsg<List<T>> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.CREATELIST,
        objs,
        null);

    //把对象序列化成json并发送到消息队列mq
    writeMsg(indexMsg);
  }

  /**
   * 异步更新索引
   *
   * @param needNullFields 需要更新为NULL的字段名称
   */
  public static <T> void updateIndexAsy(T obj, Set<String> needNullFields) {
    if (obj == null) {
      throw new FaultException(INDEXERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(obj.getClass());
    IndexMsg<T> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.UPDATE,
        obj,
        needNullFields);

    //把对象序列化成json并发送到消息队列mq
    sendMsg(indexMsg);
  }

  /**
   * 同步更新索引
   *
   * @param needNullFields 需要更新为NULL的字段名称
   */
  public static <T> void updateIndex(T obj, Set<String> needNullFields) {
    if (obj == null) {
      throw new FaultException(INDEXERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(obj.getClass());
    IndexMsg<T> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.UPDATE,
        obj,
        needNullFields);

    //把对象序列化成json并发送到消息队列mq
    writeMsg(indexMsg);
  }

  /**
   * 异步更新索引
   */
  public static <T> void updateIndexAsy(T obj) {
    if (obj == null) {
      throw new FaultException(INDEXERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(obj.getClass());
    IndexMsg<T> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.UPDATE,
        obj,
        null);

    //把对象序列化成json并发送到消息队列mq
    sendMsg(indexMsg);
  }

  /**
   * 同步更新索引
   */
  public static <T> void updateIndex(T obj) {
    if (obj == null) {
      throw new FaultException(INDEXERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(obj.getClass());
    IndexMsg<T> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.UPDATE,
        obj,
        null);

    //把对象序列化成json并发送到消息队列mq
    writeMsg(indexMsg);
  }

  /**
   * 异步批量更新索引
   */
  public static <T> void updateIndexListAsy(List<T> objs) {
    if (objs == null || objs.isEmpty()) {
      throw new FaultException(INDEXSIZEERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(objs.get(0).getClass());
    IndexMsg<List<T>> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.UPDATELIST,
        objs,
        null);

    //把对象序列化成json并发送到消息队列mq
    sendMsg(indexMsg);
  }

  /**
   * 同步批量更新索引
   */
  public static <T> void updateIndexList(List<T> objs) {
    if (objs == null || objs.isEmpty()) {
      throw new FaultException(INDEXSIZEERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(objs.get(0).getClass());
    IndexMsg<List<T>> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.UPDATELIST,
        objs,
        null);

    //把对象序列化成json并发送到消息队列mq
    writeMsg(indexMsg);
  }

  /**
   * 异步删除单条索引
   *
   * @param tclass 需要索引的类
   * @param uniqueKeyValue 唯一键值
   */
  public static <T> void deleteIndexAsy(Class<T> tclass, Object uniqueKeyValue) {
    if (tclass == null) {
      throw new FaultException(ERRORSTR);
    }
    if (uniqueKeyValue == null) {
      throw new FaultException(UNIQUEVALERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(tclass);
    IndexMsg<Object> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.DELETE,
        uniqueKeyValue,
        null);

    //把对象序列化成json并发送到消息队列mq
    sendMsg(indexMsg);
  }

  /**
   * 删除单条索引
   *
   * @param tclass 需要索引的类
   * @param uniqueKeyValue 唯一键值
   */
  public static <T> void deleteIndex(Class<T> tclass, Object uniqueKeyValue) {
    if (tclass == null) {
      throw new FaultException(ERRORSTR);
    }
    if (uniqueKeyValue == null) {
      throw new FaultException(UNIQUEVALERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(tclass);
    IndexMsg<Object> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.DELETE,
        uniqueKeyValue,
        null);

    //把对象序列化成json并发送到消息队列mq
    writeMsg(indexMsg);
  }

  /**
   * 批量删除索引
   *
   * @param tclass 需要索引的类
   * @param uniqueKeyValues 唯一键值List
   */
  public static <T> void deleteIndexListAsy(Class<T> tclass, List<Object> uniqueKeyValues) {
    if (tclass == null) {
      throw new FaultException(ERRORSTR);
    }
    if (uniqueKeyValues == null || uniqueKeyValues.isEmpty()) {
      throw new FaultException(UNIQUEVALERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(tclass);
    IndexMsg<List<Object>> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.DELETELIST,
        uniqueKeyValues,
        null);

    //把对象序列化成json并发送到消息队列mq
    sendMsg(indexMsg);
  }

  /**
   * 同步批量删除索引
   *
   * @param tclass 需要索引的类
   * @param uniqueKeyValues 唯一键值List
   */
  public static <T> void deleteIndexList(Class<T> tclass, List<Object> uniqueKeyValues) {
    if (tclass == null) {
      throw new FaultException(ERRORSTR);
    }
    if (uniqueKeyValues == null || uniqueKeyValues.isEmpty()) {
      throw new FaultException(UNIQUEVALERRORSTR);
    }
    String mapping = Mapper.getEntityMappingJson(tclass);
    IndexMsg<List<Object>> indexMsg = IndexerMsger.createMsg(
        mapping,
        IndexMsg.INDEXTYPE.DELETELIST,
        uniqueKeyValues,
        null);

    //把对象序列化成json并发送到消息队列mq
    writeMsg(indexMsg);
  }

  private static <T> void sendMsg(IndexMsg<T> indexMsg) {
    if (StringUtils.isEmpty(ROUTER)) {
      throw new FaultException(IndexConstants.CLIENT_ES_CLUSTER_KEY + " : 不可以为空");
    }
    indexMsg.setClusterRouter(ROUTER);
    String indexMsgJson = Utils.toJson(indexMsg);
    //TODO:发送异步的消息
    Publisher.get().sendMessage(IndexConstants.MX_INDEX_WRITE_ES_TOPIC, IndexConstants.MX_INDEX_WRITE_ES_CHANNEL, indexMsgJson);
  }

  private static <T> void writeMsg(IndexMsg<T> indexMsg) {
    if (StringUtils.isEmpty(ROUTER)) {
      throw new FaultException(IndexConstants.CLIENT_ES_CLUSTER_KEY + " : 不可以为空");
    }
    indexMsg.setClusterRouter(ROUTER);
    String indexMsgJson = Utils.toJson(indexMsg);
    IndexRequest request = new IndexRequest();
    request.setIndexMsgJson(indexMsgJson);
    searchService.index(request);
  }
}
