package com.github.zhengframework.configuration.reload;

/*-
 * #%L
 * zheng-configuration
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

@Slf4j
public class PeriodicalReloadStrategy implements ReloadStrategy {

  final ThreadFactory factory =
      new BasicThreadFactory.Builder()
          .namingPattern("PeriodicalReloadStrategy-%s").daemon(true)
          .build();

  private final long duration;
  private final TimeUnit unit;
  private final ScheduledExecutorService executorService;
  private final Map<Reloadable, ScheduledFuture<?>> tasks = new ConcurrentHashMap<Reloadable, ScheduledFuture<?>>();


  public PeriodicalReloadStrategy(long duration, TimeUnit unit) {
    this.duration = duration;
    this.unit = unit;
    executorService = Executors
        .newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), factory);
  }


  @Override
  public void register(Reloadable resource) {
    log.debug("Registering resource " + resource
        + " with reload time of {} {}", duration, unit.toString().toLowerCase());
    ScheduledFuture<?> scheduledFuture = executorService
        .scheduleWithFixedDelay(resource::reload, duration, duration, unit);
    tasks.put(resource, scheduledFuture);
  }

  @Override
  public void deregister(Reloadable resource) {
    log.debug("De-registering resource {}", resource);
    ScheduledFuture<?> scheduledFuture = tasks.remove(resource);
    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
    }
  }
}
