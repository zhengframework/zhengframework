package com.github.zhengframework.configuration;

import static java.util.Objects.requireNonNull;

import com.codahale.metrics.MetricRegistry;
import com.github.zhengframework.configuration.environment.DefaultEnvironment;
import com.github.zhengframework.configuration.environment.Environment;
import com.github.zhengframework.configuration.reload.CachedConfigurationSource;
import com.github.zhengframework.configuration.reload.ImmediateReloadStrategy;
import com.github.zhengframework.configuration.reload.MeteredReloadable;
import com.github.zhengframework.configuration.reload.ReloadStrategy;
import com.github.zhengframework.configuration.reload.Reloadable;
import com.github.zhengframework.configuration.source.ConfigurationSource;
import com.github.zhengframework.configuration.source.EmptyConfigurationSource;
import com.github.zhengframework.configuration.source.MeteredConfigurationSource;

public class ConfigurationBuilder {

  private ConfigurationSource configurationSource;
  private ReloadStrategy reloadStrategy;
  private Environment environment;
  private MetricRegistry metricRegistry;
  private String prefix;

  public ConfigurationBuilder() {
    configurationSource = new EmptyConfigurationSource();
    reloadStrategy = new ImmediateReloadStrategy();
    environment = new DefaultEnvironment();
    prefix = "";
  }

  public static ConfigurationBuilder builder() {
    return new ConfigurationBuilder();
  }

  public ConfigurationBuilder withConfigurationSource(ConfigurationSource configurationSource) {
    this.configurationSource = configurationSource;
    return this;
  }

  public ConfigurationBuilder withReloadStrategy(ReloadStrategy reloadStrategy) {
    this.reloadStrategy = reloadStrategy;
    return this;
  }

  public ConfigurationBuilder withEnvironment(Environment environment) {
    this.environment = environment;
    return this;
  }

  public ConfigurationBuilder withMetrics(MetricRegistry metricRegistry, String prefix) {
    this.prefix = requireNonNull(prefix);
    this.metricRegistry = metricRegistry;
    return this;
  }

  public Configuration build() {
    final CachedConfigurationSource cachedConfigurationSource = new CachedConfigurationSource(
        configurationSource);
    if (metricRegistry != null) {
      configurationSource = new MeteredConfigurationSource(metricRegistry, prefix,
          cachedConfigurationSource);
    }
    cachedConfigurationSource.init();

    Reloadable reloadable = () -> cachedConfigurationSource.reload(environment);

    if (metricRegistry != null) {
      reloadable = new MeteredReloadable(metricRegistry, prefix, reloadable);
    }
    reloadable.reload();
    reloadStrategy.register(reloadable);

    SimpleConfiguration configuration = new SimpleConfiguration(cachedConfigurationSource,
        environment);
    if (metricRegistry != null) {
      return new MeteredConfiguration(metricRegistry, prefix, configuration);
    }
    return configuration;
  }

}
