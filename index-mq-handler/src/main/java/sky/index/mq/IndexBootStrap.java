package sky.index.mq;

import lucky.sky.mq.msg.MsgApplication;
import lucky.sky.mq.msg.rocketmq.provider.Publisher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import sky.index.comm.constants.IndexConstants;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:58 2018/5/2
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class, scanBasePackages = {"sky.index.*"})
public class IndexBootStrap {

  public static void main(String[] args) {

    //指定消费者组的名称
    new MsgApplication(IndexBootStrap.class, args, "cmc_index_service").run();

//    Publisher.get().sendMessage(IndexConstants.CMC_ES_SCROLL_CLEAN_TOPIC,
//        IndexConstants.CMC_ES_SCROLL_CLEAN_CHANNEL, "2343");
  }
}
