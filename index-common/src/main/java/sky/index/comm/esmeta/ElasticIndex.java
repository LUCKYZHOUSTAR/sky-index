package sky.index.comm.esmeta;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:02 2018/4/28
 */
public class ElasticIndex {

  String IndexName;
  String IndexType;

  public void setIndexName(String IndexName) {
    this.IndexName = IndexName;
  }

  public void setIndexType(String IndexType) {
    this.IndexType = IndexType;
  }

  public String getIndexName() {
    return this.IndexName;
  }

  public String getIndexType() {
    return this.IndexType;
  }
}
