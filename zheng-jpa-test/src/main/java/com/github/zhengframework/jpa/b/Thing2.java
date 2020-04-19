package com.github.zhengframework.jpa.b;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Thing2")
public class Thing2 {

  @Id
  private UUID id = UUID.randomUUID();
  private String name;

  public Thing2(String name) {
    setName(name);
  }

  public Thing2() {
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
