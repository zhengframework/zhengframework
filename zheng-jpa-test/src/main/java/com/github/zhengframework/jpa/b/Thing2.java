package com.github.zhengframework.jpa.b;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Thing2")
public class Thing2 {

  @Id
  private Integer id;
  private String name;

  public Thing2(String name) {
    setName(name);
  }

  public Thing2() {
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
