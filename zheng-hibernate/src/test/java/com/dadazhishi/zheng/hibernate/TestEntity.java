package com.github.zhengframework.hibernate;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TestEntity {

  @Id
  private UUID id = UUID.randomUUID();
}
