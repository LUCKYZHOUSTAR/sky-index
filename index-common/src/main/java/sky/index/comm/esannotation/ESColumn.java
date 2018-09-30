package sky.index.comm.esannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import sky.index.comm.enums.Analyzed;
import sky.index.comm.enums.Store;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午10:07 2018/4/28
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ESColumn
{
  Store store() default Store.STORE;

  Analyzed analyzed() default Analyzed.NOT_ANALYZED;
}

