package sky.index.cilent;

import java.util.HashMap;
import java.util.Map;
import lucky.sky.mq.msg.rocketmq.provider.Publisher;
import lucky.sky.util.config.ConfigProperties;
import lucky.sky.util.log.Logger;
import lucky.sky.util.log.LoggerManager;
import sky.index.comm.constants.IndexConstants;
import sky.index.service.contract.iface.SearchAdminService;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:40 2018/4/28
 */
public class SearcherClient extends SearcherBaseClient<SearchAdminService> {

  private Logger logger = LoggerManager.getLogger(SearcherClient.class);


  public SearcherClient() {
    router = ConfigProperties.getProperty(IndexConstants.CLIENT_ES_CLUSTER_KEY);
    logger.info("获取到客户端的es.cluster.router值为:{}", router);
  }


  /**
   * 使用完scroll 方法后异步释放资源
   */
  @Override
  protected void cleanScroll(String scrollId) {
    Map<String, String> map = new HashMap<>();
    map.put("scrollId", scrollId);
    map.put("cluster", "admin");
    map.put("router", router);
    //TODO:手动清除
    Publisher.get().sendMessage(IndexConstants.CMC_ES_SCROLL_CLEAN_TOPIC,
        IndexConstants.CMC_ES_SCROLL_CLEAN_CHANNEL, map);
    //TODO:清楚scrollid
  }
}
