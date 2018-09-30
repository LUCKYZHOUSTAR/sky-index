package sky.index.comm.enums;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:07 2018/4/28
 */
public enum Analyzed
{
  ANALYZED("analyzed"),  NOT_ANALYZED("not_analyzed");

  private String name;

  private Analyzed(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return this.name;
  }
}

