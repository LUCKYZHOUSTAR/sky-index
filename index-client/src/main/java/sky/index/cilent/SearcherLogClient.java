package sky.index.cilent;

import java.util.HashMap;
import java.util.Map;
import lucky.sky.mq.msg.rocketmq.provider.Publisher;
import lucky.sky.util.config.ConfigProperties;
import lucky.sky.util.log.Logger;
import lucky.sky.util.log.LoggerManager;
import sky.index.comm.constants.IndexConstants;
import sky.index.service.contract.iface.SearchLogService;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:20 2018/5/2 ES的搜索的client
 */
public class SearcherLogClient extends SearcherBaseClient<SearchLogService> {


  private Logger logger = LoggerManager.getLogger(SearcherLogClient.class);


  public SearcherLogClient() {
    router = ConfigProperties.getProperty(IndexConstants.CLIENT_ES_CLUSTER_KEY);
  }


  /**
   * 使用完scroll 方法后异步释放资源
   */
  @Override
  protected void cleanScroll(String scrollId) {
    Map<String, String> map = new HashMap<>();
    map.put("scrollId", scrollId);
    map.put("cluster", "log");
    map.put("router", router);
    //TODO:手动清除
    Publisher.get().sendMessage(IndexConstants.CMC_ES_SCROLL_CLEAN_TOPIC,
        IndexConstants.CMC_ES_SCROLL_CLEAN_CHANNEL, map);
    //TODO:
//    Publisher.get().publish(IndexConstants.CMC_ES_SCROLL_CLEAN_TOPIC, scrollId);
  }

}

