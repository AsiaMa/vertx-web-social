package com.oasis.social.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

/**
 * DataObject注解会生成UserConverter类，里面包含fromJson()和toJson()两个方法
 */
@DataObject(generateConverter = true, publicConverter = false)
public class User {
  private String id;
  private String accountName;
  private String nickName;
  private String password;
  private Integer age;

  public User(String id, String accountName, String nickName, String password, Integer age) {
    this.id = id;
    this.accountName = accountName;
    this.nickName = nickName;
    this.password = password;
    this.age = age;
  }

  public User(User other) {
    this.id = other.getId();
    this.accountName = other.getAccountName();
    this.nickName = other.getNickName();
    this.password = other.getPassword();
    this.age = other.getAge();
  }

  public User(JsonObject json) {
    UserConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    UserConverter.toJson(this, json);
    return json;
  }

  public String getId() {
    return this.id;
  }

  @Fluent
  public User setId(String id) {
    this.id = id;
    return this;
  }

  public String getAccountName() {
    return this.accountName;
  }

  @Fluent
  public User setAccountName(String accountName) {
    this.accountName = accountName;
    return this;
  }

  public String getNickName() {
    return this.nickName;
  }

  @Fluent
  public User setNickName(String nickName) {
    this.nickName = nickName;
    return this;
  }

  public String getPassword() {
    return this.password;
  }

  @Fluent
  public User setPassword(String password) {
    this.password = password;
    return this;
  }

  public Integer getAge() {
    return this.age;
  }

  @Fluent
  public User setAge(Integer age) {
    this.age = age;
    return this;
  }

  @Override
  public String toString() {
    return "User{" +
      "id='" + id + '\'' +
      ", accountName='" + accountName + '\'' +
      ", nickName='" + nickName + '\'' +
      ", password='" + password + '\'' +
      ", age=" + age +
      '}';
  }
}
