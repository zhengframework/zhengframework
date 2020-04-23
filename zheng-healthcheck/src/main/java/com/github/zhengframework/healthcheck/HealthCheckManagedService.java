package com.github.zhengframework.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.github.zhengframework.core.Service;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class HealthCheckManagedService implements Service {

  private final HealthCheckConfig config;
  private final HealthCheckRegistry healthCheckRegistry;
  private final HealthCheckScanner healthCheckScanner;
  private final Injector injector;
  private ScheduledExecutorService scheduledExecutorService;

  @Inject
  public HealthCheckManagedService(HealthCheckConfig config,
      HealthCheckRegistry healthCheckRegistry,
      HealthCheckScanner healthCheckScanner, Injector injector) {
    this.config = config;
    this.healthCheckRegistry = healthCheckRegistry;
    this.healthCheckScanner = healthCheckScanner;
    this.injector = injector;
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
    healthCheckScanner.accept(
        thing -> {
          if (thing instanceof NamedHealthCheck) {
            NamedHealthCheck namedHealthCheck = (NamedHealthCheck) thing;
            healthCheckRegistry.register(namedHealthCheck.getName(), thing);
          } else {
            healthCheckRegistry.register(thing.getClass().getSimpleName(), thing);
          }
        });
    if (config.isEnable()) {
      scheduledExecutorService.schedule(() -> {
        for (Map.Entry<String, HealthCheck.Result> entry : healthCheckRegistry.runHealthChecks()
            .entrySet()) {
          if (entry.getValue().isHealthy()) {
            log.trace("{} : OK {}", entry.getKey(),
                Strings.nullToEmpty(entry.getValue().getMessage()));
          } else {
            log.warn("{} : FAIL - {}", entry.getKey(),
                Strings.nullToEmpty(entry.getValue().getMessage()), entry.getValue().getError());
          }
        }
      }, config.getDuration(), config.getUnit());
    }
  }

  @Override
  public void stop() throws Exception {
    scheduledExecutorService.shutdownNow();
  }
}
