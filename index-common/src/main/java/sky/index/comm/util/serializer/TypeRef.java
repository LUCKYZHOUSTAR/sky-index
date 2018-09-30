package sky.index.comm.util.serializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午9:50 2018/5/2
 */
public class TypeRef<T> {

  private final Type type;

  private String refClassName;

  protected TypeRef() {
    Type superClass = getClass().getGenericSuperclass();
    type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    refClassName = type.getTypeName();
  }

  public Type getType() {
    return type;
  }

  public String getRefClassName() {
    return refClassName;
  }

//  public final static Type LIST_STRING = new TypeRef<List<String>>() {}.getType();
}
