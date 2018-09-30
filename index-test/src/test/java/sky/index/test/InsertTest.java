package sky.index.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import sky.index.cilent.IndexerClient;
import sky.index.client.model.Mycore;

/**
 * @Author:chaoqiang.zhou
 * @Date:Create in 上午9:47 2018/7/3
 */
public class InsertTest {

  /***
   *
   *
   *
   *
   * Asy结尾的方法，是异步的方法，其他的用法相同
   */

  /**
   * 插入单个实体，以id为主键，如果没有则插入，如果有则全量更新
   */
  @Test
  public void insert() {

    Mycore mycore = new Mycore();
    mycore.setId("100");
    mycore.setAge(1000);

    mycore.setCreateTime(LocalDateTime.now());

    mycore.setUpdateTime(LocalDate.now());
    mycore.setName("张三");
    mycore.setFly(true);
    mycore.setUserScore(4.56f);
    mycore.setUserDScore(8.90d);
    IndexerClient.createIndex(mycore);
  }




  @Test
  public void insertArrays(){
    Mycore mycore = new Mycore();
    mycore.setId("100");
    mycore.setAge(1000);

    String [] fields=new String[]{"323","2323"};
    mycore.setArrays(fields);
    IndexerClient.createIndex(mycore);
  }

  /**
   * 插入集合，以id为主键，如果没有则插入，如果有则全量更新
   */
  @Test
  public void insertList() {
    List<Mycore> mycoreList = new ArrayList<>();

    Mycore mycore = new Mycore();
    mycore.setId("10056");
    mycore.setAge(1000);
    mycore.setCreateTime(LocalDateTime.now());
    mycore.setUpdateTime(LocalDate.now());
    mycore.setName("张三");
    mycore.setFly(true);
    mycore.setUserScore(4.56f);
    mycore.setUserDScore(8.90d);
    mycoreList.add(mycore);
    IndexerClient.createIndexList(mycoreList);
  }


  /*
  以id为主键，有的话更新，没有的话，插入
   */
  @Test
  public void update() {
    Mycore mycore = new Mycore();
    mycore.setId("102");
    mycore.setAge(90);

    mycore.setCreateTime(LocalDateTime.now());

    mycore.setUpdateTime(LocalDate.now());
    mycore.setName("张三");
    mycore.setFly(true);
    mycore.setUserScore(4.56f);
    mycore.setUserDScore(8.90d);
    IndexerClient.updateIndex(mycore);
  }


  /*
 以id为主键，有的话更新，没有的话，插入
  */
  @Test
  public void updateList() {
    List<Mycore> mycoreList = new ArrayList<>();

    Mycore mycore = new Mycore();
    mycore.setId("100");
    mycore.setAge(1000);
    mycore.setCreateTime(LocalDateTime.now());
    mycore.setUpdateTime(LocalDate.now());
    mycore.setName("李四");
    mycore.setFly(true);
    mycore.setUserScore(4.56f);
    mycore.setUserDScore(8.90d);
    mycoreList.add(mycore);
    IndexerClient.updateIndexList(mycoreList);
  }


  /*
  更新字段，把一些字段设置为null
   */
  @Test
  public void updateField() {
    /*
    需要设置为null的字段信息
     */
    Set<String> fileds = new HashSet<>();
    fileds.add("age");
    Mycore mycore = new Mycore();
    mycore.setId("102");
    mycore.setAge(90);

    mycore.setCreateTime(LocalDateTime.now());

    mycore.setUpdateTime(LocalDate.now());
    mycore.setName("张三");
    mycore.setFly(true);
    mycore.setUserScore(4.56f);
    mycore.setUserDScore(8.90d);
    IndexerClient.updateIndex(mycore, fileds);
  }


  /*
  删除以100为id的索引
   */
  @Test
  public void delete() {

    Mycore mycore = new Mycore();
    mycore.setId("100");
    mycore.setAge(1000);

    mycore.setCreateTime(LocalDateTime.now());

    mycore.setUpdateTime(LocalDate.now());
    mycore.setName("张三");
    mycore.setFly(true);
    mycore.setUserScore(4.56f);
    mycore.setUserDScore(8.90d);
    IndexerClient.deleteIndex(Mycore.class, "100");
  }


  @Test
  public void deleteList() {
    List<Object> ids = new ArrayList<>();
    ids.add("102");
    IndexerClient.deleteIndexList(Mycore.class, ids);

  }
}
