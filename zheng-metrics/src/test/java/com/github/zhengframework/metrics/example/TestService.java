package com.github.zhengframework.metrics.example;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Timed;

public class TestService {
  @Timed
  @Counted(monotonic = true)
  public String count() {
    return "1111";
  }
}
