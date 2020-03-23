package com.dadazhishi.zheng.configuration;

import javax.inject.Inject;
import javax.inject.Named;

public class NamedAnnotation {

  @Inject
  @Named("apple.name")
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
