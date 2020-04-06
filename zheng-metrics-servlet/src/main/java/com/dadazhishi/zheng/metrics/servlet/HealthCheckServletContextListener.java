package com.dadazhishi.zheng.metrics.servlet;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.dadazhishi.zheng.service.ClassScanner.Visitor;
import javax.inject.Inject;

public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener {

  private final HealthCheckRegistry healthCheckRegistry;

  @Inject
  public HealthCheckServletContextListener(
      HealthCheckRegistry healthCheckRegistry, HealthCheckScanner scanner) {
    this.healthCheckRegistry = healthCheckRegistry;
    scanner.accept(new Visitor<HealthCheck>() {
      @Override
      public void visit(HealthCheck thing) {
        healthCheckRegistry.register(thing.getClass().getName(), thing);
      }
    });
  }

  @Override
  protected HealthCheckRegistry getHealthCheckRegistry() {
    return healthCheckRegistry;
  }
}
