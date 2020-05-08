package com.github.zhengframework.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
public class HealthCheckModuleTest {

  @Inject private Injector injector;

  @Test
  @WithZhengApplication
  public void configure() throws Exception {
    HealthCheckRegistry healthCheckRegistry = injector.getInstance(HealthCheckRegistry.class);

    for (Entry<String, Result> entry : healthCheckRegistry.runHealthChecks().entrySet()) {
      Result result = entry.getValue();
      log.info("{}", entry.getKey());
      boolean healthy = result.isHealthy();
      log.info("{}", healthy);
      if (result.getMessage() != null) {
        log.info("{}", result.getMessage());
      }
      if (!healthy) {
        Throwable throwable = result.getError();
        if (throwable != null) {
          log.info("healthy=FAIL", throwable);
        }
      }
    }
    HealthCheck systemLoadHealthCheck = healthCheckRegistry.getHealthCheck("SystemLoadHealthCheck");
    HealthCheck threadDeadlockHealthCheck =
        healthCheckRegistry.getHealthCheck("ThreadDeadlockHealthCheck");
    assert systemLoadHealthCheck != null;
    assert threadDeadlockHealthCheck != null;
  }
}
