package com.github.zhengframework.jpa.c;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Thing3")
public class Thing3 {

  @Id
  private UUID id = UUID.randomUUID();
  private String name;

  public Thing3(String name) {
    setName(name);
  }

  public Thing3() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
