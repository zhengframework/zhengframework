package com.github.zhengframework.metrics.servlet;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import javax.inject.Inject;

public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener {

  private final HealthCheckRegistry healthCheckRegistry;

  @Inject
  public HealthCheckServletContextListener(
      HealthCheckRegistry healthCheckRegistry, HealthCheckScanner scanner) {
    this.healthCheckRegistry = healthCheckRegistry;
    scanner.accept(thing -> healthCheckRegistry.register(thing.getClass().getName(), thing));
  }

  @Override
  protected HealthCheckRegistry getHealthCheckRegistry() {
    return healthCheckRegistry;
  }
}
