package sky.index.es.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import lucky.sky.util.lang.FaultException;
import lucky.sky.util.lang.StrKit;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import sky.index.comm.constants.IndexConstants;
import sky.index.es.EsConfig;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:34 2018/5/2 ES的客户端
 */
public class TClient {

  private static Map<String, TransportClient> clients = new HashMap<>();

  private static Object lock = new Object();

  private TClient() {
  }


  /**
   * 获取ES Client
   */
  public static TransportClient getClient(String clusterName) {
    if (clients.get(clusterName) == null) {
      synchronized (lock) {
        if (clients.get(clusterName) == null) {
          initClusters(clusterName);
        }
      }
    }
    TransportClient client = clients.get(clusterName);
    // 如果初始化完以后获得的连接为空那么就是集群名称不正确
    if (client == null) {
      throw new FaultException(IndexConstants.CLIENT_ES_CLUSTER_KEY + " : 不正确 :" + clusterName);
    }
    if (client.connectedNodes().isEmpty()) {
      synchronized (lock) {
        if (clients.get(clusterName).connectedNodes().isEmpty()) {
          initClusters(clusterName);
        }
      }
    }
    return clients.get(clusterName);
  }

  /**
   * 初始化集群
   */
  private static void initClusters(String clusterName) {
    //如果集群的名称不是elasticsearch， 就需要设置集群的名称
    HashMap<String, EsConfig.ESInfo> infos = EsConfig.get();
    for (Map.Entry<String, EsConfig.ESInfo> entry : infos.entrySet()) {
      EsConfig.ESInfo info = entry.getValue();
      String eshost = info.getHosts();
      String cluster = info.getClusterName();
      if (StrKit.isBlank(eshost) || StrKit.isBlank(cluster)) {
        throw new FaultException("elasticearch 连接配置不正确");
      }
      // 每次连接只创建指定名称的，避免客户端传错集群名称无限制的创建连接
      if (!cluster.equals(clusterName)) {
        continue;
      }
      Settings settings = Settings.builder().put("client.transport.sniff", true)
          .put("cluster.name", cluster).put("transport.type", "netty3")
          .put("http.type", "netty3").build();
//      Settings settings = Settings.Builder()
//          .put("cluster.name", cluster)
//          .put("client.transport.sniff", true)
//          .build();

//      TransportClient client = TransportClient.builder().settings(settings).build();
//      TransportClient client=new Pre
      TransportClient client = new PreBuiltTransportClient(settings);
      try {
        for (String address : eshost.split(",")) {
          String[] add = address.split(":");
          client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(add[0]),
              Integer.parseInt(add[1])));
        }
      } catch (UnknownHostException e) {
        throw new FaultException("elasticearch 连接配置不正确", e);
      }
      clients.put(cluster, client);
    }


  }
}
