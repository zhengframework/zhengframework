package com.github.zhengframework.jpa.a;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Thing")
public class Thing {

  @Id
  private Integer id;
  private String name;

  public Thing(String name) {
    setName(name);
  }

  public Thing() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
