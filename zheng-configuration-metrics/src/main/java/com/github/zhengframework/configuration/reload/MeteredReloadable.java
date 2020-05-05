package com.github.zhengframework.configuration.reload;

/*-
 * #%L
 * zheng-configuration-metrics
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

import static java.util.Objects.requireNonNull;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public class MeteredReloadable implements Reloadable {

  private final Reloadable delegate;
  private final Timer reloadTimer;

  public MeteredReloadable(
      MetricRegistry metricRegistry, String metricPrefix, Reloadable delegate) {
    requireNonNull(metricRegistry);
    requireNonNull(metricPrefix);
    this.delegate = requireNonNull(delegate);

    reloadTimer = metricRegistry.timer(metricPrefix + "reloadable.reload");
  }

  @Override
  public void reload() {
    Timer.Context context = reloadTimer.time();
    try {
      delegate.reload();
    } finally {
      context.stop();
    }
  }
}
