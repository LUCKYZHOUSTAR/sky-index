package sky.index.comm.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:08 2018/4/28
 */
public enum ESDataType {
  STRING("java.lang.String", "string"), INTEGER("java.lang.Integer", "integer"), INT("int",
      "integer"), SHORT("java.lang.Short", "short"), SSHORT("short", "short"), BOOLEAN(
      "java.lang.Boolean", "boolean"), SBOOLEAN("boolean", "boolean"), DOUBLE("java.lang.Double",
      "double"), SDOUBLE("double", "double"), FLOAT("java.lang.Float", "float"), SFLOAT("float",
      "float"), LOCALDATE("java.time.LocalDate", "date"), LOCALDATETIME("java.time.LocalDateTime",
      "date"), SLONG("long", "long"), LONG("java.lang.Long", "long"), OBJECT("java.lang.Object",
      "object"), NESTED("Nested", "nested");

  private String javaType;
  private String esType;
  private static final Map<String, String> caches = new ConcurrentHashMap();
  private static Object _lock = new Object();

  private ESDataType(String javaType, String esType) {
    this.javaType = javaType;
    this.esType = esType;
  }

  public String getEsType() {
    return this.esType;
  }

  public String getJavaType() {
    return this.javaType;
  }

  public static String getEsType(String javaType) {
    if (caches.isEmpty()) {
      synchronized (_lock) {
        if (caches.isEmpty()) {
          for (ESDataType s : (ESDataType[]) ESDataType.class.getEnumConstants()) {
            caches.put(s.getJavaType(), s.getEsType());
          }
        }
      }
    }
    return (String) caches.get(javaType);
  }
}
