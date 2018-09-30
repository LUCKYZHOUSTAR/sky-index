package sky.index.es.enums;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:28 2018/5/2
 */
public enum AggType {

  GLOBAL("global"),

  TERMS("terms"),

  TOP("top"),

  RANGE("range"),

  DATERANGE("daterange"),

  FILTER("filter");


  private String name;


  AggType(String name) {
    this.name = name;
  }
}
