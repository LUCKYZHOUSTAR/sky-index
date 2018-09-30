package sky.index.comm.util;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:01 2018/5/2
 */
public class StrUtil {
  /**
   * 判断是否为空
   * @param str
   * @return
   */
  public static boolean isEmpty(Object...str){
    for (Object s : str) {
      if (s==null || "".equals(s.toString())) {
        return true;
      }
    }
    return false;
  }

}
