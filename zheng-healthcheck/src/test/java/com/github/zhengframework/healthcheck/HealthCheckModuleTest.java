package com.github.zhengframework.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;
import com.google.inject.Injector;
import java.util.Map.Entry;
import org.junit.Test;

public class HealthCheckModuleTest {

  @Test
  public void configure() throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create()
        .enableAutoLoadModule()
        .build();

    application.start();
    Injector injector = application.getInjector();
    HealthCheckRegistry healthCheckRegistry = injector.getInstance(HealthCheckRegistry.class);

    for (Entry<String, Result> entry : healthCheckRegistry.runHealthChecks()
        .entrySet()) {
      Result result = entry.getValue();
      System.out.println(entry.getKey());
      boolean healthy = result.isHealthy();
      System.out.println(healthy);
      if (result.getMessage() != null) {
        System.out.println(result.getMessage());
      }
      if (!healthy) {
        Throwable throwable = result.getError();
        if (throwable != null) {
          throwable.printStackTrace();
        }
      }

    }
    HealthCheck systemLoadHealthCheck = healthCheckRegistry.getHealthCheck("SystemLoadHealthCheck");
    HealthCheck threadDeadlockHealthCheck = healthCheckRegistry
        .getHealthCheck("ThreadDeadlockHealthCheck");
    assert systemLoadHealthCheck != null;
    assert threadDeadlockHealthCheck != null;
    application.stop();
  }
}