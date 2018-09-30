package sky.index.comm.util.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午9:46 2018/5/2 因为es里面是utc的时间，所以需要序列化的时候进行转换
 */
public class DateSerializer implements ObjectSerializer {

  /**
   * 日期转换
   */
  @Override
  public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
      int features)
      throws IOException {
    if (object == null) {
      serializer.getWriter().writeNull();
      return;
    }

    Date date = (Date) object;
    LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    serializer.write(localDateTime.toLocalDate());
  }
}
