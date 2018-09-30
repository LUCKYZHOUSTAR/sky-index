package sky.index.service.contract.spring;

import lucky.sky.net.rpc.RpcClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import sky.index.service.contract.iface.SearchAdminService;
import sky.index.service.contract.iface.SearchLogService;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:33 2018/4/28
 */
@Configuration
@Lazy
@Order(Integer.MAX_VALUE)
public class IndexSearchServiceProxyAutoConfig {

  private String svr = "cmc.index.admin.search.service";

  @Bean
  @ConditionalOnMissingBean
  public SearchLogService searchLogServiceProxy() {
    return RpcClient.get(svr, SearchLogService.class);
  }

  @Bean
  @ConditionalOnMissingBean
  public SearchAdminService searchAdminServiceProxy() {
    return RpcClient.get(svr, SearchAdminService.class);
  }
}
