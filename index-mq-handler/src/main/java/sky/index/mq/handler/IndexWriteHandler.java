package sky.index.mq.handler;

import lucky.sky.mq.msg.AbstractHandler;
import lucky.sky.mq.msg.MsgHandler;
import lucky.sky.mq.msg.rocketmq.provider.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sky.index.comm.constants.IndexConstants;
import sky.index.comm.util.Utils;
import sky.index.contract.IndexWrite;
import sky.index.core.indexer.IndexMsgHandler;
import sky.index.comm.util.serializer.TypeRef;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:59 2018/5/2
 */


/**
 * 异步的写入到es操作
 */
@Component
@MsgHandler(topic = IndexConstants.MX_INDEX_WRITE_ES_TOPIC, tag = IndexConstants.MX_INDEX_WRITE_ES_CHANNEL)
public class IndexWriteHandler extends AbstractHandler<String> {

  @Autowired
  IndexWrite indexWrite;

  @Override
  protected void process(String raw, Msg msg) {
    String result = Utils.parseObject(raw, new TypeRef<String>() {
    });
    IndexMsgHandler.indexObjHandler(result, indexWrite);
  }
}
