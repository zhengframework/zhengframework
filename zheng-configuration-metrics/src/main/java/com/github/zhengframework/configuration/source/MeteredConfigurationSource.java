package com.github.zhengframework.configuration.source;

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

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.github.zhengframework.configuration.environment.Environment;
import java.util.Map;
import java.util.Objects;

public class MeteredConfigurationSource implements ConfigurationSource {

  private final ConfigurationSource delegate;

  private final Timer getConfigurationTimer;
  private final Timer initTimer;

  public MeteredConfigurationSource(MetricRegistry metricRegistry, String metricPrefix,
      ConfigurationSource delegate) {
    Objects.requireNonNull(metricRegistry);
    Objects.requireNonNull(metricPrefix);
    this.delegate = Objects.requireNonNull(delegate);
    getConfigurationTimer = metricRegistry.timer(metricPrefix + "source.getConfiguration");
    initTimer = metricRegistry.timer(metricPrefix + "source.init");
  }

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    Timer.Context context = getConfigurationTimer.time();
    try {
      return delegate.getConfiguration(environment);
    } finally {
      context.stop();
    }
  }

  @Override
  public void init() {
    Timer.Context context = initTimer.time();
    try {
      delegate.init();
    } finally {
      context.stop();
    }
  }

  @Override
  public void addListener(ConfigurationSourceListener listener) {
    delegate.addListener(listener);
  }

  @Override
  public void removeListener(ConfigurationSourceListener listener) {
    delegate.removeListener(listener);
  }
}
