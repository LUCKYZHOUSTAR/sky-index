package sky.index.comm.esannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:14 2018/4/28
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ESAliases {

  String value();
}

