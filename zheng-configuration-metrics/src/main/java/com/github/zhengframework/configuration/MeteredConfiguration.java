package com.github.zhengframework.configuration;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
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
      MetricRegistry metricRegistry,
      String metricPrefix,
      Configuration delegate) {
    Objects.requireNonNull(metricRegistry);
    Objects.requireNonNull(metricPrefix);
    this.delegate = Objects.requireNonNull(delegate);
    getTimer = metricRegistry.timer(MetricRegistry.name(metricPrefix, "configuration.get"));
    keySetTimer = metricRegistry.timer(MetricRegistry.name(metricPrefix, "configuration.keySet"));
    asMapTimer = metricRegistry.timer(MetricRegistry.name(metricPrefix, "configuration.asMap"));
    prefixTimer = metricRegistry.timer(MetricRegistry.name(metricPrefix, "configuration.prefix"));
    prefixSetTimer = metricRegistry
        .timer(MetricRegistry.name(metricPrefix, "configuration.prefixSet"));
    prefixMapTimer = metricRegistry
        .timer(MetricRegistry.name(metricPrefix, "configuration.prefixMap"));
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
