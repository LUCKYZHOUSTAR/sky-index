package es.index.test.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import sky.index.comm.esannotation.SearchId;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in
 */
@Getter
@Setter
public class User {




  @SearchId
  private String id;

  private String userName;

  private String pwd;


  private LocalDateTime localDateTime;
}
