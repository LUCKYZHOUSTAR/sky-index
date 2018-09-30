package sky.index.client.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import sky.index.comm.esannotation.SearchId;

public class Mycore {


  @SearchId
  String id;

  String type;

  String name;

  String telphone;

  long age;


  String [] arrays;
  int dd;
  float userScore;

  double userDScore;

  boolean isFly;

  LocalDate updateTime;




  LocalDateTime createTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTelphone() {
    return telphone;
  }

  public void setTelphone(String telphone) {
    this.telphone = telphone;
  }

  public long getAge() {
    return age;
  }

  public void setAge(long age) {
    this.age = age;
  }


  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }


  public int getDd() {
    return dd;
  }

  public void setDd(int dd) {
    this.dd = dd;
  }

  public float getUserScore() {
    return userScore;
  }

  public void setUserScore(float userScore) {
    this.userScore = userScore;
  }

  public double getUserDScore() {
    return userDScore;
  }

  public void setUserDScore(double userDScore) {
    this.userDScore = userDScore;
  }

  public boolean isFly() {
    return isFly;
  }

  public void setFly(boolean fly) {
    isFly = fly;
  }

  public LocalDate getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDate updateTime) {
    this.updateTime = updateTime;
  }


  public String[] getArrays() {
    return arrays;
  }

  public void setArrays(String[] arrays) {
    this.arrays = arrays;
  }
}
