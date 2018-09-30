package sky.index.comm.constants;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午9:49 2018/4/28
 */
public class IndexConstants {

  /**
   * 写索引操作的es实现 topic
   **/
  public static final String MX_INDEX_WRITE_ES_TOPIC = "cmc_index_write_es_topic";

  /**
   * 写索引操作的es实现 channel
   **/
  public static final String MX_INDEX_WRITE_ES_CHANNEL = "cmc_index_write_es_channel";


  /**
   * 异步清除es scrollId topic
   **/
  public static final String CMC_ES_SCROLL_CLEAN_TOPIC = "cmc_es_scroll_clean_topic";

  /**
   * 异步清除es scrollId channel
   **/
  public static final String CMC_ES_SCROLL_CLEAN_CHANNEL = "cmc_es_scroll_clean_channel";

  /**
   * 路由ES 集群的 集群名称
   **/
  public static final String CLIENT_ES_CLUSTER_KEY = "es.cluster.router";
}
