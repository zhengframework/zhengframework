package com.github.zhengframework.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.SortedMap;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;

/**
 * Register health checks here. Bind your health checks as eager singletons; @Inject this class in
 * the constructor and add(this).
 */
@Slf4j
@Singleton
public class HealthChecks {

  private final HealthCheckRegistry healthCheckRegistry;

  @Inject
  public HealthChecks(HealthCheckRegistry healthCheckRegistry) {
    this.healthCheckRegistry = healthCheckRegistry;
  }

  /**
   * Registers a health check with the HealthCheckRegistry.
   *
   * @param name name
   * @param healthCheck healthCheck
   */
  public void add(String name, HealthCheck healthCheck) {
    healthCheckRegistry.register(name, healthCheck);
  }

  /**
   * Unregisters a health check with the HealthCheckRegistry.
   *
   * @param name healthCheck name
   */
  public void remove(String name) {
    healthCheckRegistry.unregister(name);
  }

  /**
   * Runs all of the healthchecks once and returns a map of the results
   *
   * @return a map of the health check results
   */
  public SortedMap<String, HealthCheck.Result> run() {
    return healthCheckRegistry.runHealthChecks();
  }

  public SortedMap<String, HealthCheck.Result> run(ExecutorService executorService) {
    return healthCheckRegistry.runHealthChecks(executorService);
  }


}
