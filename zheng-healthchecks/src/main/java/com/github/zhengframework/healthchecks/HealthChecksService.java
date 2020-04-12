package com.github.zhengframework.healthchecks;

import com.codahale.metrics.health.HealthCheck;
import com.github.zhengframework.core.Service;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;

/**
 * This service periodically runs health checks at a configured interval and reports any health
 * issues via slf4j
 */
@Singleton
@Slf4j
public class HealthChecksService implements Service {

  private final HealthChecks healthChecks;
  private final HealthChecksConfig config;
  private ScheduledExecutorService scheduledExecutorService;

  @Inject
  public HealthChecksService(HealthChecksConfig config,
      HealthChecks healthChecks) {
    this.config = config;
    this.healthChecks = healthChecks;
    scheduledExecutorService = Executors
        .newScheduledThreadPool(Runtime.getRuntime().availableProcessors(),
            new ThreadFactoryBuilder()
                .setNameFormat("healthCheck-%d")
                .setDaemon(true)
                .build());
  }

  @Override
  public int priority() {
    return 0;
  }

  @Override
  public void start() throws Exception {
    if (config.getInterval() != null) {
      scheduledExecutorService.schedule(() -> {
        for (Map.Entry<String, HealthCheck.Result> entry : healthChecks.run().entrySet()) {
          if (entry.getValue().isHealthy()) {
            log.trace("{} : OK {}", entry.getKey(),
                Strings.nullToEmpty(entry.getValue().getMessage()));
          } else {
            log.warn("{} : FAIL - {}", entry.getKey(),
                Strings.nullToEmpty(entry.getValue().getMessage()), entry.getValue().getError());
          }
        }
      }, config.getInterval().getQuantity(), config.getInterval().getUnit());
    }
  }

  @Override
  public void stop() throws Exception {
    scheduledExecutorService.shutdownNow();
  }
}
