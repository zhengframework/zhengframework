package com.dadazhishi.zheng.metrics.example;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Timed;

public class TestService {

  @Timed
  @Counted
  public String count() {
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "1111";
  }
}
