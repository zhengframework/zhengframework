package com.dadazhishi.zheng.metrics.servlet;

import com.codahale.metrics.health.HealthCheck;
import com.dadazhishi.zheng.service.ClassScanner;
import com.google.inject.Injector;
import javax.inject.Inject;

public class HealthCheckScanner extends ClassScanner<HealthCheck> {

  @Inject
  public HealthCheckScanner(Injector injector) {
    super(injector, HealthCheck.class);
  }
}
