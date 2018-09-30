package sky.index.comm.enums;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:11 2018/4/28
 */
public enum ESDateFormat
{
  LocalDateTime("yyyy-MM-ddTHH:mm:ss"),  LocalDate("yyyy-MM-dd");

  private String pattern;

  private ESDateFormat(String pattern)
  {
    this.pattern = pattern;
  }

  public String getPattern()
  {
    return this.pattern;
  }
}