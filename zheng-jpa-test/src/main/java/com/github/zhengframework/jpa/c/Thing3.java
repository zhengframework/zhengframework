package com.github.zhengframework.jpa.c;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "Thing3")
public class Thing3 {

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private Integer id;
  private String name;

  public Thing3(String name) {
    setName(name);
  }

  public Thing3() {
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
