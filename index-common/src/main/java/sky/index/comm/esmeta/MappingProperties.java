package sky.index.comm.esmeta;

import java.util.Map;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:04 2018/4/28
 * ES索引的基本信息
 */
public class MappingProperties {

  Map<String, Object> properties;

  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }

  public void setDynamic(String dynamic) {
    this.dynamic = dynamic;
  }

  public Map<String, Object> getProperties() {
    return this.properties;
  }

  public String getDynamic() {
    return this.dynamic;
  }

  private String dynamic = "strict";

  public MappingProperties(Map<String, Object> properties) {
    this.properties = properties;
  }

  public MappingProperties() {
  }
}
