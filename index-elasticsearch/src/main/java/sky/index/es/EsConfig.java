package sky.index.es;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lucky.sky.util.config.ConfigException;
import lucky.sky.util.config.ConfigManager;
import lucky.sky.util.config.ConfigParser;
import lucky.sky.util.config.ConfigProperties;
import lucky.sky.util.config.SettingMap;
import lucky.sky.util.lang.XmlHelper;
import lucky.sky.util.log.Logger;
import lucky.sky.util.log.LoggerManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:30 2018/5/2
 */
public class EsConfig {

  private static HashMap<String, ESInfo> infos = new HashMap<>();
  static Logger log = LoggerManager.getLogger(EsConfig.class);

  private EsConfig() {
  }



  static {
    loadConfig();
  }

  private static void loadConfig() {
    String env = ConfigProperties.activeProfiles();
    String suffix="";
    if (env!=null && env.length()>0){
      String[] split = env.split("-");
      suffix = "."+split[0];
    }
    log.info("加载的ES 配置环境为:{} , 文件为:{}",env,suffix);
    String filePath = ConfigManager.findConfigPath("db.es"+suffix, ".conf", ".xml");
    if (filePath == null) {
      return;
    }
    Document doc = ConfigParser.resolveXml(filePath);
    NodeList nodes = doc.getElementsByTagName("database");
    for (int i = 0; i < nodes.getLength(); i++) {
      Element elem = (Element) nodes.item(i);
      ESInfoImpl di = new ESInfoImpl();
      NodeList settingNodes = elem.getElementsByTagName("setting");
      di.settings = XmlHelper.toSettingMap(settingNodes, "name", "value");
      di.hosts = di.settings.getString("hosts");
      di.clusterName = di.settings.getString("clusterName");
      if (infos.containsKey(di.clusterName)){
        continue;
      }
      infos.put(di.clusterName, di);
    }
  }

  /**
   * @param name
   * @return
   * @throws ConfigException if config can't be found
   */
  public static ESInfo get(String name) {
    ESInfo dbInfo = infos.get(name);
    if (dbInfo == null) {
      throw new ConfigException("can't find db config for " + name);
    }
    return dbInfo;
  }

  /**
   * @return
   * @throws lucky.sky.util.config.ConfigException if config can't be found
   */
  public static HashMap<String, ESInfo> get() {
    return infos;
  }


  /**
   * ES 配置信息
   */
  public interface ESInfo {

    /**
     * hosts
     * @return
     */
    String getHosts();

    /**
     * 集群名称
     * @return
     */
    String getClusterName();

    /**
     * shard 数量
     * @return
     */
    String getShardNum();

    /**
     * 副本数
     * @return
     */
    String getReplicaNum();

    /**
     * 其他配置
     * @return
     */
    SettingMap getSettings();


  }

  /**
   * es 配置
   */
  @Getter
  @Setter
  public static class ESInfoImpl implements ESInfo {
    private String hosts;
    private String clusterName;
    private String shardNum;
    private String replicaNum;
    private SettingMap settings;


  }
}
