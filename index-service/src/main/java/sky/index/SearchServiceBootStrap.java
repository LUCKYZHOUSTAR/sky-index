package sky.index;

import lucky.sky.net.rpc.RpcApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:55 2018/5/2
 */

@SpringBootApplication(scanBasePackages = {"sky.*"})
public class SearchServiceBootStrap {

  public static void main(String[] args) {
//    System.setProperty("es.set.netty.runtime.available.processors", "false");
    new RpcApplication(SearchServiceBootStrap.class, args).run();
  }

}
