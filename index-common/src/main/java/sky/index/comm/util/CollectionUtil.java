package sky.index.comm.util;

import java.util.Collection;
import java.util.Map;
import org.omg.CORBA.Object;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午9:58 2018/5/2
 */
public class CollectionUtil {
  public static boolean isEmpty(Collection coll){
    if (coll == null || coll.isEmpty()){
      return true;
    }
    return false;
  }


  public static boolean notEmpty(Collection coll){
    return !isEmpty(coll);
  }


  public static boolean isEmptyMap(Map<Object,Object> map){
    if (map == null || map.isEmpty()){
      return true;
    }
    return false;
  }


  public static boolean notEmptyMap(Map<Object,Object> map){
    return !isEmptyMap(map);
  }

}
