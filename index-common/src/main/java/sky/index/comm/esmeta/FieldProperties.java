package sky.index.comm.esmeta;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:03 2018/4/28
 */
public class FieldProperties {
  String type;
  String index;
  boolean store;

  public String getType()
  {
    return this.type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getIndex()
  {
    return this.index;
  }

  public void setIndex(String index)
  {
    this.index = index;
  }

  public boolean isStore()
  {
    return this.store;
  }

  public void setStore(boolean store)
  {
    this.store = store;
  }
}
