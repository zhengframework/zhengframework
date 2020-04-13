package com.github.zhengframework.metrics.example;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

public class TestService {

  @Gauge
  @Metered
  @Timed
  @Counted(monotonic = true)
  public String count() {
    return "1111";
  }
}
