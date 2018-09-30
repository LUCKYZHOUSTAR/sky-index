package sky.index.mq.handler;

import java.util.HashMap;
import lucky.sky.mq.msg.AbstractHandler;
import lucky.sky.mq.msg.MsgHandler;
import lucky.sky.mq.msg.rocketmq.provider.Msg;
import lucky.sky.util.log.Logger;
import lucky.sky.util.log.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sky.index.comm.constants.IndexConstants;
import sky.index.contract.IndexRead;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:59 2018/5/2
 */
@Component
@MsgHandler(topic = IndexConstants.CMC_ES_SCROLL_CLEAN_TOPIC, tag = IndexConstants.CMC_ES_SCROLL_CLEAN_CHANNEL)
public class CleanScrollIdHandler extends AbstractHandler<HashMap<String, String>> {

  private Logger log = LoggerManager.getLogger(CleanScrollIdHandler.class);

  @Autowired
  IndexRead indexRead;

  @Override
  protected void process(HashMap<String, String> map, Msg msg) {

    log.debug("需要清除的scrollId:{}", map);
    String scrollId = map.get("scrollId");
    String clusterName = map.get("router");
    this.indexRead.cleanScrollId(scrollId, clusterName);
  }
}
