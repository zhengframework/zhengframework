package com.github.zhengframework.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.github.zhengframework.guice.ClassScanner;
import com.google.inject.Injector;
import javax.inject.Inject;

public class HealthCheckScanner extends ClassScanner<HealthCheck> {

  @Inject
  public HealthCheckScanner(Injector injector) {
    super(injector, HealthCheck.class);
  }
}
