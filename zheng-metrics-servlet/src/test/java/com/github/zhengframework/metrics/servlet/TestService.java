package com.github.zhengframework.metrics.servlet;

import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

@SuppressWarnings("UnusedReturnValue")
public class TestService {

  @Metered
  @Gauge
  @Timed
  @Counted(monotonic = true)
  public String count() {
    return "1111";
  }
}
