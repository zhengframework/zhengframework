package com.github.zhengframework.healthcheck;

/*-
 * #%L
 * zheng-healthcheck
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.github.zhengframework.service.Service;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Inject;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class HealthCheckManagedService implements Service {

  private final HealthCheckConfig config;
  private final HealthCheckRegistry healthCheckRegistry;
  private final HealthCheckScanner healthCheckScanner;
  private ScheduledExecutorService scheduledExecutorService;

  @Inject
  public HealthCheckManagedService(
      HealthCheckConfig config,
      HealthCheckRegistry healthCheckRegistry,
      HealthCheckScanner healthCheckScanner) {
    this.config = config;
    this.healthCheckRegistry = healthCheckRegistry;
    this.healthCheckScanner = healthCheckScanner;
    scheduledExecutorService =
        Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors(),
            new ThreadFactoryBuilder().setNameFormat("healthCheck-%d").setDaemon(true).build());
  }

  @Override
  public int order() {
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
      scheduledExecutorService.schedule(
          () -> {
            for (Map.Entry<String, HealthCheck.Result> entry :
                healthCheckRegistry.runHealthChecks().entrySet()) {
              if (entry.getValue().isHealthy()) {
                log.trace(
                    "{} : OK {}",
                    entry.getKey(),
                    Strings.nullToEmpty(entry.getValue().getMessage()));
              } else {
                log.warn(
                    "{} : FAIL - {}",
                    entry.getKey(),
                    Strings.nullToEmpty(entry.getValue().getMessage()),
                    entry.getValue().getError());
              }
            }
          },
          config.getDuration(),
          config.getUnit());
    }
  }

  @Override
  public void stop() throws Exception {
    scheduledExecutorService.shutdownNow();
  }
}
