package com.github.zhengframework.configuration;

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
import com.codahale.metrics.Timer.Context;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class MeteredConfiguration implements Configuration {

  private final Configuration delegate;

  private final Timer getTimer;
  private final Timer keySetTimer;
  private final Timer asMapTimer;
  private final Timer prefixTimer;
  private final Timer prefixSetTimer;
  private final Timer prefixMapTimer;

  public MeteredConfiguration(
      MetricRegistry metricRegistry, String metricPrefix, Configuration delegate) {
    Objects.requireNonNull(metricRegistry);
    Objects.requireNonNull(metricPrefix);
    this.delegate = Objects.requireNonNull(delegate);
    getTimer = metricRegistry.timer(MetricRegistry.name(metricPrefix, "configuration.get"));
    keySetTimer = metricRegistry.timer(MetricRegistry.name(metricPrefix, "configuration.keySet"));
    asMapTimer = metricRegistry.timer(MetricRegistry.name(metricPrefix, "configuration.asMap"));
    prefixTimer = metricRegistry.timer(MetricRegistry.name(metricPrefix, "configuration.prefix"));
    prefixSetTimer =
        metricRegistry.timer(MetricRegistry.name(metricPrefix, "configuration.prefixSet"));
    prefixMapTimer =
        metricRegistry.timer(MetricRegistry.name(metricPrefix, "configuration.prefixMap"));
  }

  @Override
  public Optional<String> get(String key) {
    try (Context context = getTimer.time()) {
      try {
        return delegate.get(key);
      } finally {
        context.stop();
      }
    }
  }

  @Override
  public Set<String> keySet() {
    try (Context context = keySetTimer.time()) {
      try {
        return delegate.keySet();
      } finally {
        context.stop();
      }
    }
  }

  @Override
  public Map<String, String> asMap() {
    try (Context context = asMapTimer.time()) {
      try {
        return delegate.asMap();
      } finally {
        context.stop();
      }
    }
  }

  @Override
  public Configuration prefix(String prefix) {
    try (Context context = prefixTimer.time()) {
      try {
        return delegate.prefix(prefix);
      } finally {
        context.stop();
      }
    }
  }

  @Override
  public List<Configuration> prefixList(String prefix) {
    try (Context context = prefixSetTimer.time()) {
      try {
        return delegate.prefixList(prefix);
      } finally {
        context.stop();
      }
    }
  }

  @Override
  public Set<Configuration> prefixSet(String prefix) {
    try (Context context = prefixSetTimer.time()) {
      try {
        return delegate.prefixSet(prefix);
      } finally {
        context.stop();
      }
    }
  }

  @Override
  public Map<String, Configuration> prefixMap(String prefix) {
    try (Context context = prefixMapTimer.time()) {
      try {
        return delegate.prefixMap(prefix);
      } finally {
        context.stop();
      }
    }
  }
}
