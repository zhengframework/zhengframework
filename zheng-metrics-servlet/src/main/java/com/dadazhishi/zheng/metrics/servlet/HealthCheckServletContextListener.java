package com.dadazhishi.zheng.metrics.servlet;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import javax.inject.Inject;

public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener {

  private final HealthCheckRegistry healthCheckRegistry;

  @Inject
  public HealthCheckServletContextListener(
      HealthCheckRegistry healthCheckRegistry) {
    this.healthCheckRegistry = healthCheckRegistry;
  }

  @Override
  protected HealthCheckRegistry getHealthCheckRegistry() {
    return healthCheckRegistry;
  }
}
