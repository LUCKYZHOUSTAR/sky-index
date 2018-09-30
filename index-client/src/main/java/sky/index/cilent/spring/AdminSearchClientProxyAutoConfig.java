package sky.index.cilent.spring;



import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import sky.index.cilent.SearcherClient;
import sky.index.cilent.SearcherLogClient;

/**
 * 提供 Spring 自动注入默认服务代理功能。
 */
@Configuration
@Lazy
@Order(Ordered.LOWEST_PRECEDENCE)
public class AdminSearchClientProxyAutoConfig {


  /**
   * 自动注入
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  public SearcherClient searcherClientProxy(){
    return new SearcherClient();
  }

  /**
   * 自动注入
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  public SearcherLogClient searcherLogClientProxy(){
    return new SearcherLogClient();
  }

}