package sky.index.test.query.model;

import lombok.Getter;
import lombok.Setter;
import sky.index.comm.esannotation.SearchId;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 下午2:33 2018/4/28
 */

@Getter
@Setter
public class User {
  @SearchId
  private String id;


  private String userName;

  private String pwd;
}
