package sky.index.comm.dto;

import java.util.Set;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:14 2018/4/28
 * 消息发送的实体
 */


public class IndexMsg<T>
{
  /**
   * 索引类型枚举
   */
  public enum INDEXTYPE { CREATE(1), CREATELIST(2), UPDATE(3), UPDATELIST(4), DELETE(5), DELETELIST(6);
    private int indextype;
    private INDEXTYPE(int indextype){
      this.indextype = indextype;
    }

    /**
     * 获取枚举值
     * @return
     */
    public int value(){
      return this.indextype;
    }
  }

  /** 索引类型 */
  private INDEXTYPE indexType;

  /** 索引对象的全类名（包括包名） */
  private String className;


  /** 需要更新为NULL的字段名称  **/
  private Set<String> fieldName;

  /** 需要索引的对象（可以是个List） */
  private T obj;

  /** ES 集群路由关键字 **/
  private String clusterRouter;

  public INDEXTYPE getIndexType() {
    return indexType;
  }

  public void setIndexType(INDEXTYPE indexType) {
    this.indexType = indexType;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public T getObj() {
    return obj;
  }

  public void setObj(T obj) {
    this.obj = obj;
  }

  public Set<String> getFieldName() {
    return fieldName;
  }

  public void setFieldName(Set<String> fieldName) {
    this.fieldName = fieldName;
  }

  public String getClusterRouter() {
    return clusterRouter;
  }

  public void setClusterRouter(String clusterRouter) {
    this.clusterRouter = clusterRouter;
  }
}

