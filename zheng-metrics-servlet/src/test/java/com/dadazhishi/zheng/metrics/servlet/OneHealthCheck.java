package com.dadazhishi.zheng.metrics.servlet;

import com.codahale.metrics.health.HealthCheck;

public class OneHealthCheck extends HealthCheck {

  @Override
  protected Result check() throws Exception {
    return Result.builder()
        .withDetail("key", "value")
        .healthy()
        .build();
  }
}
