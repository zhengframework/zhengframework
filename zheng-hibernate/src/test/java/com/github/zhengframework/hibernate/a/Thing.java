package com.github.zhengframework.hibernate.a;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Thing")
public class Thing {

  @Id
  private UUID id = UUID.randomUUID();
  private String name;

  public Thing(String name) {
    setName(name);
  }

  public Thing() {
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
