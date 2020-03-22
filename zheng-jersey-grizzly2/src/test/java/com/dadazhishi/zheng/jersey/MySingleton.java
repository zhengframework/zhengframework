package com.dadazhishi.zheng.jersey;

import javax.inject.Inject;

public class MySingleton {

  @Inject
  private MySingleton() {
  }

  public String call() {
    return "MyService(" + hashCode() + ")";
  }

}
