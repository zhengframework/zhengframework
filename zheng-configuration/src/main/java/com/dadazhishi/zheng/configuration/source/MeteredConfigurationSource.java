package com.github.zhengframework.configuration.source;

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
